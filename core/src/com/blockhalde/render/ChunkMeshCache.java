package com.blockhalde.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.WorldInterface;
import com.terrain.world.WorldManagementSystem;

public class ChunkMeshCache {

	private WorldManagementSystem worldManagementSystem;
	
	/**
	 * The cache may keep meshes for this many subchunks in VRAM before starting to
	 * overwrite old chunks when a new one is loaded.
	 */
	private static final int MAX_CACHED_SUBCHUNKS = 9 * 16 * 10;

	private static final ChunkPosition FARAWAY_CHUNKPOS = new ChunkPosition(Integer.MAX_VALUE, Integer.MAX_VALUE);
	private List<CachedSubchunk> cachedSubs = new ArrayList<CachedSubchunk>(24);
	private ChunkMeshBuilder builder;
	
	private final SubchunkDistanceComparator comp = new SubchunkDistanceComparator();

	private Camera cam;
	
	private long lastUpdateStartTime;
	
	public ChunkMeshCache(WorldManagementSystem worldManagementSystem, Camera cam) {
		this.cam = cam;
		this.worldManagementSystem = worldManagementSystem;
		builder = new ChunkMeshBuilder(worldManagementSystem);
		allocateCache();
		
		if(worldManagementSystem.getVisibleChunks().size() > MAX_CACHED_SUBCHUNKS) {
			// FIXME the cache size should be determined by the draw distance, but it does not
			//       have a getter as of yet
			throw new RuntimeException("Cache is too small to handle all the visible chunks");
		}
	}
	
	/**
	 * Populates cachedSubs with placeholder instances.
	 */
	private void allocateCache() {
		for(int i = 0; i < MAX_CACHED_SUBCHUNKS; ++i) {
			CachedSubchunk subchunk = new CachedSubchunk();
			subchunk.chunkPos = FARAWAY_CHUNKPOS;
			subchunk.mesh = new Mesh(false, 16*16*16*6*4, 16*16*16*6*6,
					new VertexAttributes(
							new VertexAttribute(Usage.Position, 3, "a_position"),
							new VertexAttribute(Usage.Normal, 3, "a_normal"),
							new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"),
							new VertexAttribute(Usage.ColorPacked, 4, "a_color")
					)
			);
			
			cachedSubs.add(subchunk);
		}
	}
	
	private void sortCache() {
		comp.setReferencePosition(cam.position);
		Collections.sort(cachedSubs, comp);
	}
	
	/**
	 * Asks the worldManagementSystem for visible chunks and ensures all of them are in the cache.
	 */
	private void uploadUncachedMeshes() {
		// Start overwriting subchunks in the cache at the most distant subchunk
		// Any insertion will destroy ordering of the cache, though it will be sorted again
		// upon next update
		int cacheInsertionIdx = cachedSubs.size() - 1;
		
		for(Chunk visibleChunk: worldManagementSystem.getVisibleChunks()) {
			for(int y = 0; y < RenderSystem.SUBCHUNK_HEIGHT; ++y) {
				CachedSubchunk cacheEntry = findCachedSubchunk(visibleChunk.getChunkPosition(), y);
				
				if(cacheEntry == null) {
					cacheEntry = cachedSubs.get(cacheInsertionIdx--);
					cacheEntry.chunkPos = visibleChunk.getChunkPosition();
					cacheEntry.subchunkIdx = y;
					update(cacheEntry, visibleChunk);
					updateNeighbors(cacheEntry.chunkPos);
				}
			}
		}
	}
	
	private void updateNeighbors(ChunkPosition chunkPos) {
		for(int x = chunkPos.getXPosition() - 16; x < chunkPos.getXPosition() + 17; x += 32) {
			for(int z = chunkPos.getZPosition() - 16; z < chunkPos.getZPosition() + 17; z += 32) {
				for(int subchunkIdx = 0; subchunkIdx < 16; ++subchunkIdx) {
					CachedSubchunk cached = findCachedSubchunk(x, subchunkIdx, z);
					if(cached != null && cached.lastMeshUpdate < lastUpdateStartTime) {
						update(cached, worldManagementSystem.getChunk(x, z));
					}
				}
			}
		}
	}

	private void update(CachedSubchunk cached, Chunk chunk) {
		builder.updateMesh(cached.mesh, chunk.getChunkPosition(), cached.subchunkIdx);
		cached.lastMeshUpdate = System.nanoTime();
	}
	
	private CachedSubchunk findCachedSubchunk(int x, int subchunkIdx, int z) {
		for(CachedSubchunk subchunk: cachedSubs) {
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
	
	/**
	 * Checks modification dates of subchunks and uploads a new mesh to the graphics
	 * card if necessary.
	 */
	private void updateOutOfDateMeshes() {
		for(CachedSubchunk cached: cachedSubs) {
			if(!cached.isUnused()) {
				Chunk chunk = worldManagementSystem.getChunk(cached.chunkPos.getXPosition(), cached.chunkPos.getZPosition());
				// FIXME this should be the actual modification date not just some fantasy value
				// this would be cool : lastSubchunkModification = chunk.getLastSubchunkUpdateTime(cached.subchunkIdx)
				long lastSubchunkUpdate = Long.MIN_VALUE;
				long lastMeshUpdate = cached.lastMeshUpdate;
	
				if(lastMeshUpdate < lastSubchunkUpdate) {
					update(cached, chunk);
				}
			}
		}
	}
	
	public void update() {
		lastUpdateStartTime = System.nanoTime();
		
		//long start = System.currentTimeMillis();
		sortCache();
		//long afterSort = System.currentTimeMillis();
		uploadUncachedMeshes();
		//long afterUploadingUncached = System.currentTimeMillis();
		updateOutOfDateMeshes();
		//long end = System.currentTimeMillis();
		
		//System.out.println("Sorting:               " + (afterSort-start) + "ms");
		//System.out.println("Uploading uncached:    " + (afterUploadingUncached-afterSort) + "ms");
		//System.out.println("Uploading invalidated: " + (end-afterUploadingUncached) + "ms");
	}
	
	public List<CachedSubchunk> getCachedSubchunks() {
		return cachedSubs;
	}
}
