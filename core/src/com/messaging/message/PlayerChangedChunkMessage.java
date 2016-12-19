package com.messaging.message;

import com.terrain.chunk.ChunkPosition;

public class PlayerChangedChunkMessage extends ChunkMessage {
	private final ChunkPosition lastChunkPosition;

	public PlayerChangedChunkMessage(ChunkPosition chunkPosition, ChunkPosition lastChunkPosition) {
		super(chunkPosition);
		this.lastChunkPosition = lastChunkPosition;
	}

	public ChunkPosition getLastChunkPosition() {
		return lastChunkPosition;
	}
}