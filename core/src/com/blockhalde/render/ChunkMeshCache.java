package com.blockhalde.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.World;

public class ChunkMeshCache {

	private World world;
	
	/**
	 * The cache may keep meshes for this many subchunks in VRAM before starting to
	 * overwrite old chunks when a new one is loaded.
	 */
	private static final int MAX_CACHED_SUBCHUNKS = 9 * 16;
	private List<CachedSubchunk> cachedSubs = new ArrayList<CachedSubchunk>(24);
	private ChunkMeshBuilder builder;
	
	private final SubchunkDistanceComparator comp = new SubchunkDistanceComparator();
	
	public ChunkMeshCache(World world) {
		this.world = world;
		builder = new ChunkMeshBuilder();
		allocateCache();
		
		if(world.getVisibleChunks().size() > MAX_CACHED_SUBCHUNKS) {
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
			subchunk.mesh = new Mesh(false, 16*16*16*6*4, 16*16*16*6*6, 
					new VertexAttributes(
							new VertexAttribute(Usage.Position, 3, "a_position"),
							new VertexAttribute(Usage.Normal, 3, "a_normal"),
							new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")
					)
			);
			
			cachedSubs.add(subchunk);
		}
	}
	
	private void sortCache() {
		// FIXME this should be the player position, not (0,0,0)
		comp.setReferencePosition(new Vector3(0, 0, 0));
		Collections.sort(cachedSubs, comp);
	}
	
	/**
	 * Asks the world for visible chunks and ensures all of them are in the cache.
	 */
	private void uploadUncachedMeshes() {
		// Start overwriting subchunks in the cache at the most distant subchunk
		// Any insertion will destroy ordering of the cache, though it will be sorted again
		// upon next update
		int cacheInsertionIdx = cachedSubs.size() - 1;
		
		for(Chunk visibleChunk: world.getVisibleChunks()) {
			for(int y = 0; y < RenderSystem.SUBCHUNK_HEIGHT; ++y) {
				CachedSubchunk cacheEntry = findCachedSubchunk(visibleChunk.getChunkPosition(), y);
				
				if(cacheEntry == null) {
					cacheEntry = cachedSubs.get(cacheInsertionIdx--);
					cacheEntry.chunkPos = visibleChunk.getChunkPosition();
					cacheEntry.subchunkIdx = y;
					update(cacheEntry, visibleChunk);
				}
			}
		}
	}
	
	private void update(CachedSubchunk cached, Chunk chunk) {
		builder.updateMesh(chunk, cached.mesh, cached.subchunkIdx);
		cached.lastMeshUpdate = System.nanoTime();
	}
	
	private CachedSubchunk findCachedSubchunk(ChunkPosition chunkPos, int subchunkIdx) {
		for(CachedSubchunk subchunk: cachedSubs) {
			if(!subchunk.isUnused() && subchunk.subchunkIdx == subchunkIdx &&
			   subchunk.chunkPos.getXPosition() == chunkPos.getXPosition() &&
			   subchunk.chunkPos.getZPosition() == chunkPos.getZPosition()) {
				
				return subchunk;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks modification dates of subchunks and uploads a new mesh to the graphics
	 * card if necessary.
	 */
	private void updateOutOfDateMeshes() {
		for(CachedSubchunk cached: cachedSubs) {
			if(!cached.isUnused()) {
				Chunk chunk = world.getChunk(cached.chunkPos.getXPosition(), cached.chunkPos.getZPosition());
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
