package com.messaging.message;

import com.terrain.chunk.ChunkPosition;

public class ChunkMessage {
	private final ChunkPosition chunkPosition;
	private final long time = System.nanoTime();
	
	public ChunkMessage (ChunkPosition chunkPosition) {
		this.chunkPosition = chunkPosition;
	}

	public ChunkPosition getChunkPosition() {
		return chunkPosition;
	}
	
	public long getTime() {
		return time;
	}
}