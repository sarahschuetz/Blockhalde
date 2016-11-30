package com.render;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.World;

public class ChunkMeshCache {

	private World world;
	
	public class CachedSubchunk {
		public ChunkPosition chunkPos;
		public int subchunkIdx;
		public Mesh mesh;
	}
	
	private List<CachedSubchunk> cachedSubs = new ArrayList<CachedSubchunk>(24);
	private ChunkMeshBuilder builder;
	
	public ChunkMeshCache(World world) {
		this.world = world;
		builder = new ChunkMeshBuilder();
	}
	
	public void update() {
		cachedSubs.clear();
		
		// TODO keep list contents and only remove entries that are far away from the player
		// TODO update chunks that were modified, add new list entries for chunks that are not there yet
		
		for(Chunk visibleChunk: world.getVisibleChunks()) {
			for(int y = 0; y < 16; ++y) {
				CachedSubchunk subchunk = new CachedSubchunk();
				subchunk.chunkPos = visibleChunk.getChunkPosition();
				subchunk.subchunkIdx = y;
				subchunk.mesh = new Mesh(false, 16*16*16*6*4, 16*16*16*6*6, 
						new VertexAttributes(
								new VertexAttribute(Usage.Position, 3, "a_position"),
								new VertexAttribute(Usage.Normal, 3, "a_normal"),
								new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0")
						)
				);
				
				builder.updateMesh(visibleChunk, subchunk.mesh, y);
				
				cachedSubs.add(subchunk);
			}
		}
	}
	
	public List<CachedSubchunk> getCachedSubs() {
		return cachedSubs;
	}
}
