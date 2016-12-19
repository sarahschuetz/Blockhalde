package com.messaging.message;

import com.terrain.chunk.ChunkPosition;

public class ChunkUpdateMessage extends ChunkMessage {
	private final int xPosition;
	private final int yPosition;
	private final int zPosition;
	
	public ChunkUpdateMessage(ChunkPosition chunkPosition, int xPosition, int yPosition, int zPosition) {
		super(chunkPosition);
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.zPosition = zPosition;
	}

	public int getxPosition() {
		return xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public int getzPosition() {
		return zPosition;
	}
}
