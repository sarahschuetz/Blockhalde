package com.terrain.chunk;

import com.terrain.block.BlockType;
import com.util.FlagUtils;

public class TerrainChunk implements Chunk {

	private final short[] blocks = new short[X_MAX * Y_MAX * Z_MAX];
    private ChunkPosition chunkPosition;
    private long lastModifiedTime = System.nanoTime();

    public TerrainChunk(ChunkPosition chunkPosition) {
        this.chunkPosition = chunkPosition;
    }
    
    @Override
    public int getWidth() {
        return X_MAX;
    }

    @Override
    public int getHeight() {
        return Y_MAX;
    }

    @Override
    public int getDepth() {
        return Z_MAX;
    }
    
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
   
    @Override
    public short getBlockAt(int relativeX, int relativeY, int relativeZ) {
    	int offset = coordsToFlatIndex(relativeX, relativeY, relativeZ);
    	
    	if(offset < 0) {
    		return BlockType.AIR.getBlockId();
    	}
    	
    	return blocks[offset];
    }

    @Override
    public byte getBlockTypeAt(int relativeX, int relativeY, int relativeZ) {
    	final short block = getBlockAt(relativeX, relativeY, relativeZ);
    	return FlagUtils.getByteOf(block, 0);
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
    
    @Override
    public ChunkPosition getChunkPosition() {
        return chunkPosition;
    }
    
    public ChunkPosition getRelativeChunkPosition() {
        return new ChunkPosition(chunkPosition.getXPosition() / Chunk.X_MAX, chunkPosition.getZPosition() / Chunk.Z_MAX);
    }
}
