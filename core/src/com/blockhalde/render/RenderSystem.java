package com.blockhalde.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.messaging.message.ChunkMessage;
import com.messaging.message.ChunkUpdateMessage;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.WorldManagementSystem;

public class RenderSystem extends EntitySystem {
	
	public static final int MAX_CACHED_SUBCHUNKS = 13 * 13 * 16;
	
	public static final int SUBCHUNK_HEIGHT = 16;
	
	/**
	 * The maximum amount of cached subchunks received from the worker thread
	 * in a single frame. This serves to limit load lag.
	 */
	public static final int SUBCHUNK_CACHE_INCLUSION_CHUNK_SIZE = 16; 
	
	private Texture texture;
	private Texture fogGradient;
	private ShaderProgram shader;
	private Engine engine;
	private WorldManagementSystem world;
	private ChunkMeshWorker worker;
	private BlockingQueue<CachedSubchunk> workerQueue = new ArrayBlockingQueue<>(1024);
	private List<CachedSubchunk> cache = new ArrayList<>();
	private Skybox skybox;
	private SubchunkDistanceComparator distComp = new SubchunkDistanceComparator();
	
	@Override
	public void addedToEngine(Engine engine) {
		texture = new Texture(Gdx.files.internal("textures/blocks.png"), true);
		texture.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);

		fogGradient = new Texture(Gdx.files.internal("textures/fog-gradient.png"), true);
		fogGradient.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);
		
		this.engine = engine;

		world = engine.getSystem(WorldManagementSystem.class);

		shader = new ShaderProgram(Gdx.files.internal("shaders/blocks.vs.glsl"),
				                   Gdx.files.internal("shaders/blocks.fs.glsl"));
		
		skybox = new Skybox();
		
		if(!shader.isCompiled()) {
			System.out.println("Something went wrong during shader compilation, have a look at the log:");
			System.out.println(shader.getLog());
		}
		
		Gdx.gl.glClearColor(0.76f, 0.92f, 1f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		
		worker = new ChunkMeshWorker(workerQueue);
		
		new Thread(worker).start();
	}
	
	private void enqueue(ChunkMeshRequest req) {
		worker.enqueue(req);
	}
	
	public void updateBlock(int blockX, int blockY, int blockZ) {
		int chunkX = blockX / 16 * 16;
		int subchunkIdx = blockY / 16;
		int chunkZ = blockZ / 16 * 16;
		
		enqueueSubchunk(chunkX, subchunkIdx, chunkZ);
	}
	
	public void loadChunk(ChunkMessage chunkMessage) {
		int x = chunkMessage.getChunkPosition().getXPosition();
		int z = chunkMessage.getChunkPosition().getZPosition();

		Chunk[] neighborhood = {
			world.getChunk(x, z),
			world.getChunk(x, z + 16),
			world.getChunk(x, z - 16),
			world.getChunk(x + 16, z),
			world.getChunk(x - 16, z)
		};
		
		for(Chunk neighbor: neighborhood) {
			if(neighbor != null) {
				enqueueChunk(neighbor);
			}
		}
	}

	/**
	 * Enqueues the given chunk if all neighbouring chunks have already been loaded.
	 * 
	 * @param chunk
	 */
	public void enqueueChunk(Chunk chunk) {
		int x = chunk.getChunkPosition().getXPosition();
		int z = chunk.getChunkPosition().getZPosition();
		
		
		Chunk center = chunk;
		Chunk posX = world.getChunk(x + 16, z);
		Chunk negX = world.getChunk(x - 16, z);
		Chunk posZ = world.getChunk(x, z + 16);
		Chunk negZ = world.getChunk(x, z - 16);
		
		// If all neighbors of the new chunk or of a neighbor of the newly loaded chunk
		// are already loaded, generate the center chunk
		if(posX != null && negX != null && posZ != null && negZ != null) {
			for(int subchunkIdx = 0; subchunkIdx < 16; ++subchunkIdx) {
				ChunkMeshRequest req = new ChunkMeshRequest(center, posZ, negZ, posX, negX, subchunkIdx);
				enqueue(req);
			}
		}
	}
	
	public void enqueueSubchunk(int x, int subchunkIdx, int z) {
		Chunk center = world.getChunk(x, z);
		Chunk posX = world.getChunk(x + 16, z);
		Chunk negX = world.getChunk(x - 16, z);
		Chunk posZ = world.getChunk(x, z + 16);
		Chunk negZ = world.getChunk(x, z - 16);
		
		// If all neighbors of the new chunk or of a neighbor of the newly loaded chunk
		// are already loaded, generate the center chunk
		if(posX != null && negX != null && posZ != null && negZ != null) {
			ChunkMeshRequest req = new ChunkMeshRequest(center, posZ, negZ, posX, negX, subchunkIdx);
			enqueue(req);
		}
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		worker.shutdown();
	}

	@Override
	public void update(float deltaTime) {
//		world.generateNearChunks();
		
		drainWorkerQueue();
		freeExcessCacheSpace();
		
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		Camera cam = engine.getSystem(CameraSystem.class).getCam();
		
		skybox.render(cam);
		
		shader.begin();

		// No model matrix necessary, position is encoded in mesh data
		shader.setUniformMatrix("u_view", cam.view);
		shader.setUniformMatrix("u_projection", cam.projection);


		texture.bind(0);
		shader.setUniformi("u_texture", 0);

		fogGradient.bind(1);
		shader.setUniformi("u_fogGradient", 1);

		texture.bind(0);

		for(CachedSubchunk cached: cache) {
			if(!cached.isUnused()) {
				cached.mesh.render(shader, GL20.GL_TRIANGLES);
			}
		}
		
		shader.end();
		
	}
	
	private void freeExcessCacheSpace() {
		int cachedCount = cache.size();
		int excessCount = cachedCount - MAX_CACHED_SUBCHUNKS;
		
		if(excessCount > 0) {
			Vector3 camPosition = engine.getSystem(CameraSystem.class).getCam().position;
			distComp.setReferencePosition(camPosition);
			Collections.sort(cache, distComp);
			
			for(int i = 0; i < excessCount; ++i) {
				cache.remove(cache.size() - 1);
			}
		}
	}

	private CachedSubchunk findCachedSubchunk(int x, int subchunkIdx, int z) {
		for(CachedSubchunk subchunk: cache) {
			if(!subchunk.isUnused() && subchunk.subchunkIdx == subchunkIdx &&
			   subchunk.chunkPos.getXPosition() == x &&
			   subchunk.chunkPos.getZPosition() == z) {
				return subchunk;
			}
		}
		
		return null;
	}
	
	private CachedSubchunk findCachedSubchunk(ChunkPosition chunkPos, int subchunkIdx) {
		return findCachedSubchunk(chunkPos.getXPosition(), subchunkIdx, chunkPos.getZPosition());
	}

	private void drainWorkerQueue() {
		CachedSubchunk cached;
		int cacheInsertions = 0;
		
		while(cacheInsertions <= SUBCHUNK_CACHE_INCLUSION_CHUNK_SIZE && (cached = workerQueue.poll()) != null) {
			CachedSubchunk alreadyCached = findCachedSubchunk(cached.chunkPos, cached.subchunkIdx);
			if(alreadyCached != null) {
				cache.remove(alreadyCached);
			}
			
			cache.add(cached);
			++cacheInsertions;
		}
	}
	
}
