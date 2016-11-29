package com.terrain.world;

import com.terrain.chunk.Chunk;

public interface WorldInterface {
	/**
     * Returns the chunk that is located at the specified position in the world.
     */
	Chunk getChunk(int xPosition, int zPosition);


	//This is quite pointless
	//Lets assume its not:
	//The importance of this message is not that easy definable since its dependent on the users perception
	//Having said that, its still pointless
}