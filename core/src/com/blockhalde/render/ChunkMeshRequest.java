package com.blockhalde.render;

import com.terrain.chunk.ChunkPosition;

public class ChunkMeshRequest {

	ChunkPosition position;
	int subchunkIdx;
	
	public ChunkMeshRequest(ChunkPosition position, int subchunkIdx) {
		this.position = position;
		this.subchunkIdx = subchunkIdx;
	}

}
