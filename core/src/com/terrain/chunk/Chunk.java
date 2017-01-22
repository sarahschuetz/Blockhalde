package com.terrain.chunk;

import com.terrain.block.BlockType;

public interface Chunk {
	int X_MAX = 16;
	int Y_MAX = 256;
	int Z_MAX = 16;

    /**
	 * Gets the block count in X direction. Assuming a block size of 1, this
	 * is equivalent to the physical width of the chunk.
	 * 
	 * @return Block count in X direction
	 */
	int getWidth();
	
    /**
	 * Gets the block count in Y direction. Assuming a block size of 1, this
	 * is equivalent to the physical height of the chunk.
	 * 
	 * @return Block count in Y direction
	 */
	int getHeight();
	
    /**
	 * Gets the block count in Z direction. Assuming a block size of 1, this
	 * is equivalent to the physical depth of the chunk.
	 * 
	 * @return Block count in Z direction
	 */
	int getDepth();
	
	 /**
     * Gets the block on the specified position (relativeX, relativeY, relativeZ)
     * using position relative to chunk. <br>
     * 
     * This short contains the full information of the block:<br>
     * <pre>
     * 0001 0011 | 0001 1100 <br>
     * Attributes   BlockId</pre>
     */
	short getBlockAt(int relativeX, int relativeY, int relativeZ);
	
	/**
	 * Gets only the BlockID of the specified block position. 
	 */
	byte getBlockTypeAt(int relativeX, int relativeY, int relativeZ);
	
	void setBlockAt(int relativeX, int relativeY, int relativeZ, BlockType type);

	/**
     * Get the position of the chunk.
     */
	ChunkPosition getChunkPosition();
	
	/**
	 * Gets the difference in time in nanoseconds between a fixed but arbitrary
	 * origin time and the last point in time where a block in the block chunk
	 * was modified in any way. If the chunk has never been modified, this method
	 * returns the time of creation of the chunk.
	 * 
	 * @return Nanosecond time of last block modification
	 */
	long getLastModifiedTime();
}
