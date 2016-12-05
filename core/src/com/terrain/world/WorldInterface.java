package com.terrain.world;

import com.terrain.block.BlockType;
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
     * Sets a blocktype at the specified world position.
     */
	public void setBlock(int x, int y, int z, BlockType blockType);
	
	/**
	 * Returns all chunks that are visible at the moment
     */
	List<Chunk> getVisibleChunks();
	
	/**
	 * Returns the <strong>draw distance in blocks.</strong><br>
	 * Example: If the draw distance is <strong>1</strong>, it means that <strong>one 'ring'</strong> around the player block is in the memory. 
	 */
	int getDrawDistance();
}