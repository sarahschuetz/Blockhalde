package com.terrain.chunk;

/**
 * Utility class for nice chunk based methods :) 
 */
public class ChunkUtil {
	public static ChunkPosition getChunkPositionFor(int absoluteX, int absoulteZ) {
		return new ChunkPosition(absoluteX / Chunk.X_MAX * Chunk.X_MAX, absoulteZ / Chunk.Z_MAX * Chunk.Z_MAX);
	}
	
	public static ChunkPosition getRelativeChunkPosition(ChunkPosition absoluteChunkPosition) {
		return new ChunkPosition(absoluteChunkPosition.getXPosition() / Chunk.X_MAX, absoluteChunkPosition.getZPosition() / Chunk.Z_MAX);
	}
}