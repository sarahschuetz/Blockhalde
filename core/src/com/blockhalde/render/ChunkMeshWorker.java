package com.blockhalde.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.utils.Pool;
import com.terrain.world.WorldInterface;

public class ChunkMeshWorker implements Runnable {

	/**
	 * If this many chunk mesh requests have been enqueued, start processing in
	 * the worker thread.
	 */
	public static final int NOTIFY_WORKER_THRESHOLD = 6;

	private ForkJoinPool taskPool = ForkJoinPool.commonPool();
	/**
	 * Blocks if more than 128 subchunks are pending for mesh generation
	 **/
	private BlockingQueue<ChunkMeshRequest> pendingRequests = new ArrayBlockingQueue<>(128);
	private BlockingQueue<CachedSubchunk> cachedSubchunks;
	
	private TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/blocks.atlas"));
	private WorldInterface world;
	private Pool<ChunkMeshBuilder> builderPool = new Pool<ChunkMeshBuilder>() {
		@Override
		protected ChunkMeshBuilder newObject() {
			return new ChunkMeshBuilder(world, atlas);
		}

	};
	private boolean running = false;

	public ChunkMeshWorker(WorldInterface world, BlockingQueue<CachedSubchunk> targetQueue) {
		this.world = world;
		this.cachedSubchunks = targetQueue;
	}

	@Override
	public void run() {
		running = true;
		while(running) {
			final List<ChunkMeshRequest> runRequests = new ArrayList<>();
			final List<Future<MeshBuilder>> futureMeshes;
			final List<ChunkMeshBuilder> inUseBuilders = new ArrayList<>();

			pendingRequests.drainTo(runRequests);
			
			if(runRequests.size() > 0) {
				// Start a future for each subchunk mesh generation
				for(ChunkMeshRequest req: runRequests) {
					ChunkMeshBuilder builder = builderPool.obtain();
					builder.init(req.position, req.subchunkIdx);
					inUseBuilders.add(builder);
				}
				
				futureMeshes = taskPool.invokeAll(inUseBuilders);
				
				
				for(int i = inUseBuilders.size()-1; i >= 0; --i) {
					final ChunkMeshBuilder builder = inUseBuilders.get(i);
					final ChunkMeshRequest req = runRequests.get(i);
					final Future<MeshBuilder> fut = futureMeshes.get(i);
					
					try {
						final MeshBuilder meshBuilder = fut.get();
						
						if(meshBuilder != null) {
							Gdx.app.postRunnable(new Runnable() {
								
								@Override
								public void run() {
									try {
										cachedSubchunks.put(new CachedSubchunk(meshBuilder.end(), req.position, req.subchunkIdx, System.nanoTime()));
									} catch (InterruptedException e) {
										throw new RuntimeException(e);
									}
								}
							});
						}
					} catch (InterruptedException | ExecutionException e) {
						throw new RuntimeException(e);
					}
					
					builderPool.free(builder);
				}
			}
		}
	}

	public void enqueue(ChunkMeshRequest request) {
		try {
			pendingRequests.put(request);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public BlockingQueue<CachedSubchunk> getCachedSubchunks() {
		return cachedSubchunks;
	}
	
	public void shutdown() {
		running = false;
	}
}
