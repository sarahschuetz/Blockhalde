package com.blockhalde.render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.utils.Pool;

public class ChunkMeshWorker implements Runnable {

	private ForkJoinPool taskPool = ForkJoinPool.commonPool();
	private BlockingQueue<ChunkMeshRequest> pendingRequests = new LinkedBlockingDeque<>();
	private BlockingQueue<CachedSubchunk> cacheInsertions;
	
	private TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/blocks.atlas"));
	private Pool<ChunkMeshBuilder> builderPool = new Pool<ChunkMeshBuilder>() {
		@Override
		protected ChunkMeshBuilder newObject() {
			return new ChunkMeshBuilder(atlas);
		}

	};
	private boolean running = false;

	public ChunkMeshWorker(BlockingQueue<CachedSubchunk> targetQueue) {
		this.cacheInsertions = targetQueue;
	}

	@Override
	public void run() {
		running = true;
		
		final List<ChunkMeshRequest> runRequests = new ArrayList<>();
		while(running) {
			final List<Future<MeshBuilder>> futureMeshes;
			final List<ChunkMeshBuilder> inUseBuilders = new ArrayList<>();
			runRequests.clear();

			pendingRequests.drainTo(runRequests);
			
			if(runRequests.size() > 0) {
				// Start a future for each subchunk mesh generation
				for(ChunkMeshRequest req: runRequests) {
					ChunkMeshBuilder builder = builderPool.obtain();
					builder.init(req);
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
							cacheInsertions.put(new CachedSubchunk(meshBuilder, req.getPosition(), req.subchunkIdx, System.nanoTime()));
						}
					} catch (InterruptedException | ExecutionException e) {
						throw new RuntimeException(e);
					}
					
					builderPool.free(builder);
				}
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public void enqueue(ChunkMeshRequest request) {
		try {
			synchronized(pendingRequests) {
				boolean notInQueueYet = true;
				
				for(ChunkMeshRequest pendingReq: pendingRequests) {
					if(pendingReq.equals(request)) {
						notInQueueYet = false;
						break;
					}
				}
				
				if(notInQueueYet) {
					pendingRequests.put(request);
				}
				
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void shutdown() {
		running = false;
	}
}
