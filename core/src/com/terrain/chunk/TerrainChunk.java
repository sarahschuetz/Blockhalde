package com.terrain.chunk;

import com.terrain.block.BlockType;

public class TerrainChunk implements Chunk {

	private final short[] blocks = new short[X_MAX * Y_MAX * Z_MAX];
    private ChunkPosition chunkPosition;
    private long lastModifiedTime = System.nanoTime();

    public TerrainChunk(ChunkPosition chunkPosition) {
        this.chunkPosition = chunkPosition;
    }
    
    /**
	 * Gets the block count in X direction. Assuming a block size of 1, this
	 * is equivalent to the physical width of the chunk.
	 * 
	 * @return Block count in X direction
	 */
    @Override
    public int getWidth() {
        return X_MAX;
    }

    /**
	 * Gets the block count in Y direction. Assuming a block size of 1, this
	 * is equivalent to the physical height of the chunk.
	 * 
	 * @return Block count in Y direction
	 */
    @Override
    public int getHeight() {
        return Y_MAX;
    }

    /**
	 * Gets the block count in Z direction. Assuming a block size of 1, this
	 * is equivalent to the physical depth of the chunk.
	 * 
	 * @return Block count in Z direction
	 */
    @Override
    public int getDepth() {
        return Z_MAX;
    }
    
	/**
	 * Gets the difference in time in nanoseconds between a fixed but arbitrary
	 * origin time and the last point in time where a block in the block chunk
	 * was modified in any way. If the chunk has never been modified, this method
	 * returns the time of creation of the chunk.
	 * 
	 * @return Nanosecond time of last block modification
	 */
    @Override
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
    
    /**
     * Set last modified time to current system time
     */
	private void setLastModifiedNow() {
		lastModifiedTime = System.nanoTime();
	}
    
    private int coordsToFlatIndex(int x, int y, int z) {
		if(x < 0 || x >= X_MAX || y < 0 || y >= Y_MAX || z < 0 || z >= Z_MAX) {
			return -1;
		}
		
		int offset = x + X_MAX * (y + Y_MAX * z);
		return offset;
	}
    
    /**
     * Gets the block on the specified position (relativeX, relativeY, relativeZ)
     * using position relative to chunk
     */
    @Override
    public short getBlockAt(int relativeX, int relativeY, int relativeZ) {
    	int offset = coordsToFlatIndex(relativeX, relativeY, relativeZ);
    	
    	if(offset < 0) {
    		return BlockType.AIR.getBlockId();
    	}
    	
    	return blocks[offset];
    }

    @Override
    public void setBlockAt(int relativeX, int relativeY, int relativeZ, BlockType type) {
        setBlockAt(coordsToFlatIndex(relativeX, relativeY, relativeZ), type.getBlockId());
    }
    
    private void setBlockAt(int offset, short type)
	{
		if(offset < 0 || offset >= X_MAX * Y_MAX * Z_MAX) {
			throw new IndexOutOfBoundsException("Flat offset is out of bounds: " + offset);
		}
		
		blocks[offset] = type;
		setLastModifiedNow();
	}
    
    /**
     * Get the position of the chunk
     */
    @Override
    public ChunkPosition getChunkPosition() {
        return chunkPosition;
    }
    
    /**
     * 
     */
    @Override
    public ChunkPosition getRelativeChunkPosition() {
        return new ChunkPosition(chunkPosition.getXPosition() / Chunk.X_MAX, chunkPosition.getZPosition() / Chunk.Z_MAX);
    }
}
