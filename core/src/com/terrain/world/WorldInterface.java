package com.terrain.world;

import com.terrain.chunk.Chunk;

import java.util.List;

public interface WorldInterface {
	/**
     * Returns the chunk that is located at the specified position in the world.
     */
	Chunk getChunk(int xPosition, int zPosition);

	/**
	 * Gets the blocktype at the specified world position
	 */
	short getBlock(int x, int y, int z);

	/**
	 * Returns all chunks that are visible at the moment
     */
	List<Chunk> getVisibleChunks();
	// TODO: get relative chunk
}
