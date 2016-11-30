package com.blockhalde;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Mesh;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.World;

public class BlockMeshCache {

	private World world;
	
	class SubChunkIndex {
		ChunkPosition chunkPos;
		int subchunkIdx;
		Mesh mesh;
	}
	
	private List<SubChunkIndex> meshes = new ArrayList<SubChunkIndex>(24);
	private BlockChunkMeshBuilder
	
	public BlockMeshCache(World world) {
		this.world = world;
	}
	
	public void update() {
		for(Chunk visibleChunk: world.getVisibleChunks()) {
			for(int y = 0; y < 16; ++y) {
				
			}
		}
	}

}
