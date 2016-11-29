package com.terrain.world;

import com.terrain.chunk.Chunk;

public interface WorldInterface {
	/**
     * Returns the chunk that is located at the specified position in the world.
     */
	Chunk getChunk(int xPosition, int zPosition);
	// TODO: get relative chunk
	// TODO: Get all chunks
	// TODO: Dimensions (How many chunks), Center
}