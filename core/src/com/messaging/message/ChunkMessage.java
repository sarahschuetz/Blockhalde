package com.messaging.message;

import com.terrain.chunk.ChunkPosition;

public class ChunkMessage {
	private final ChunkPosition chunkPosition;
	
	public ChunkMessage (ChunkPosition chunkPosition) {
		this.chunkPosition = chunkPosition;
	}

	public ChunkPosition getChunkPosition() {
		return chunkPosition;
	}
}