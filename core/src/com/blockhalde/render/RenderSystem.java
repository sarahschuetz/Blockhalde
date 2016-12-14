package com.blockhalde.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.msg.MessageManager;
import com.badlogic.msg.Telegram;
import com.badlogic.msg.Telegraph;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.WorldManagementSystem;

public class RenderSystem extends EntitySystem {

	public static final int SUBCHUNK_HEIGHT = 16;
	
	private ChunkMeshCache meshCache;
	private Texture texture;
	private ShaderProgram shader;
	private Engine engine;
	private WorldManagementSystem world;
	private ChunkMeshWorker worker;
	private BlockingQueue<CachedSubchunk> workerQueue = new ArrayBlockingQueue<>(1024);
	private List<CachedSubchunk> cache = new ArrayList<>();
	
	@Override
	public void addedToEngine(Engine engine) {
		texture = new Texture(Gdx.files.internal("textures/blocks.png"));
		
		this.engine = engine;

		world = engine.getSystem(WorldManagementSystem.class);

		CameraSystem camSys = engine.getSystem(CameraSystem.class);
		Camera cam = camSys.getCam();
		
		//long start = System.currentTimeMillis();
		//meshCache = new ChunkMeshCache(worldManagementSystem, cam);
		//meshCache.update();
		//System.out.println("Initial mesh generation time: " + (System.currentTimeMillis() - start) + "ms");
		
		shader = new ShaderProgram(Gdx.files.internal("shaders/blocks.vs.glsl"),
				                   Gdx.files.internal("shaders/blocks.fs.glsl"));
		
		if(!shader.isCompiled()) {
			System.out.println("Something went wrong during shader compilation, have a look at the log:");
			System.out.println(shader.getLog());
		}
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		
		worker = new ChunkMeshWorker(world, workerQueue);
		
		new Thread(worker).start();
	}
	
	private void enqueue(ChunkMeshRequest req) {
		CachedSubchunk cached = findCachedSubchunk(req.getPosition(), req.subchunkIdx);
		
		boolean chunkDirty = false;
		
		if(cached == null || true) {
			chunkDirty = true;
		}
		
		if(chunkDirty) {
			worker.enqueue(req);
		}
	}
	
	private boolean hasFullNeighborhood(ChunkPosition pos) {
		int x = pos.getXPosition();
		int z = pos.getZPosition();
		
		return world.getChunk(x - 16, z) != null &&
		       world.getChunk(x + 16, z) != null &&
		       world.getChunk(x, z - 16) != null &&
		       world.getChunk(x, z + 16) != null;
	}
	
	/**
	 * Gets an array of five chunks containing the center and the four
	 * neighbouring chunks. If any neighbor is missing, returns null
	 * instead.
	 * 
	 * @param center
	 * @return
	 */
	/*Chunk[] getNeighborhood(ChunkPosition center) {
		int x = chunkMessage.getChunkPosition().getXPosition();
		int z = chunkMessage.getChunkPosition().getZPosition();

		Chunk centerChunk = world.getChunk(x, z);
		
		return new Chunk[] {
				
		};
	}*/
	
	public void loadChunk(ChunkMessage chunkMessage) {
		System.out.println("Chunk created:    " + chunkMessage.getChunkPosition().getXPosition() + "/" + chunkMessage.getChunkPosition().getZPosition());
		
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
				x = neighbor.getChunkPosition().getXPosition();
				z = neighbor.getChunkPosition().getZPosition();
				
				Chunk center = neighbor;
				Chunk posX = world.getChunk(x + 16, z);
				Chunk negX = world.getChunk(x - 16, z);
				Chunk posZ = world.getChunk(x, z + 16);
				Chunk negZ = world.getChunk(x, z - 16);
				
				// If all neighbors are loaded, generate the center chunk
				if(posX != null && negX != null && posZ != null && negZ != null) {
					for(int subchunkIdx = 0; subchunkIdx < 16; ++subchunkIdx) {
						ChunkMeshRequest req = new ChunkMeshRequest(center, posZ, negZ, posX, negX, subchunkIdx);
						enqueue(req);
					}
				}
			}
		}
		
		
		/*for(int subchunkIdx = 0; subchunkIdx < 16; ++subchunkIdx) {
			enqueue(new ChunkMeshRequest(chunkMessage.getChunkPosition(), subchunkIdx));
			
			if(neighbour1 != null) {
				enqueue(new ChunkMeshRequest(neighbour1.getChunkPosition(), subchunkIdx));
			}
			
			if(neighbour2 != null) {
				enqueue(new ChunkMeshRequest(neighbour2.getChunkPosition(), subchunkIdx));
			}
			
			if(neighbour3 != null) {
				enqueue(new ChunkMeshRequest(neighbour3.getChunkPosition(), subchunkIdx));
			}
			
			if(neighbour4 != null) {
				enqueue(new ChunkMeshRequest(neighbour4.getChunkPosition(), subchunkIdx));
			}
		}*/
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		worker.shutdown();
	}

	@Override
	public void update(float deltaTime) {
//		world.generateNearChunks();
		
		drainWorkerQueue();
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		//long start = System.currentTimeMillis();
		//meshCache.update();
		//System.out.println("Mesh update time: " + (System.currentTimeMillis() - start) + "ms");
		
		shader.begin();
		
		Camera cam = engine.getSystem(CameraSystem.class).getCam();
		
		// No model matrix necessary, position is encoded in mesh data
		shader.setUniformMatrix("u_view", cam.view);
		shader.setUniformMatrix("u_projection", cam.projection);

		texture.bind();
		shader.setUniformi("u_texture", 0);

		for(CachedSubchunk cached: cache) {
			if(!cached.isUnused()) {
				cached.mesh.render(shader, GL20.GL_TRIANGLES);
			}
		}
		
		shader.end();
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
		while((cached = workerQueue.poll()) != null) {
			CachedSubchunk alreadyCached = findCachedSubchunk(cached.chunkPos, cached.subchunkIdx);
			if(alreadyCached != null) {
				cache.remove(alreadyCached);
			}
			
			cache.add(cached);
		}
	}
	
}
