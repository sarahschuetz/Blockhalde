package com.terrain.chunk;

import com.terrain.block.BlockType;

public class FlatArrayChunk implements Chunk {

	private static final int CHUNK_WIDTH = 16;
	private static final int CHUNK_DEPTH = 16;
	private static final int CHUNK_HEIGHT = 256;
	private static final int BLOCK_COUNT = CHUNK_WIDTH * CHUNK_DEPTH * CHUNK_HEIGHT;
	
	private short[] blockTypes = new short[BLOCK_COUNT];
	
	private long lastModifiedTime = System.nanoTime();
	
	private ChunkPosition pos;
	
	public FlatArrayChunk(ChunkPosition pos) {
		this.pos = pos;
	}

	/**
	 * Gets the block count in X direction. Assuming a block size of 1, this
	 * is equivalent to the physical width of the chunk.
	 * 
	 * @return Block count in X direction
	 */
	public int getWidth() {
		return CHUNK_WIDTH;
	}
	
	/**
	 * Gets the block count in Y direction. Assuming a block size of 1, this
	 * is equivalent to the physical height of the chunk.
	 * 
	 * @return Block count in Y direction
	 */
	public int getHeight() {
		return CHUNK_HEIGHT;
	}
	
	/**
	 * Gets the block count in Z direction. Assuming a block size of 1, this
	 * is equivalent to the physical depth of the chunk.
	 * 
	 * @return Block count in Z direction
	 */
	public int getDepth() {
		return CHUNK_DEPTH;
	}
	
	/**
	 * Gets the difference in time in nanoseconds between a fixed but arbitrary
	 * origin time and the last point in time where a block in the block chunk
	 * was modified in any way. If the chunk has never been modified, this method
	 * returns the time of creation of the chunk.
	 * 
	 * @return Nanosecond time of last block modification
	 */
	public long getLastModifiedTime() {
		return lastModifiedTime;
	}
	
	private void setLastModifiedNow() {
		lastModifiedTime = System.nanoTime();
	}
	
	public short getBlockAt(int x, int y, int z) {
		int offset = coordsToFlatIndex(x, y, z);
		return blockTypes[offset];
	}
	
	public short getBlockAt(int offset) {
		return blockTypes[offset];
	}
	
	@Override
	public void setBlockAt(int x, int y, int z, BlockType type) {
		setBlockAt(type.getBlockId(), coordsToFlatIndex(x, y, z));
	}
	
	public void setBlockAt(short type, int offset)
	{
		if(offset < 0 || offset >= BLOCK_COUNT) {
			throw new IndexOutOfBoundsException("Flat offset is out of bounds: " + offset);
		}
		
		blockTypes[offset] = type;
		setLastModifiedNow();
	}
	
	public boolean isOpaqueAt(int x, int y, int z) {
		short type = getBlockAt(x, y, z);
		return type != BlockType.AIR.getBlockId() && type != BlockType.WATER.getBlockId();
	}

	public int coordsToFlatIndex(int x, int y, int z) {
		if(x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_DEPTH) {
			throw new IndexOutOfBoundsException("Index set: (" + x + "," + y + "," + z + ") is out of bounds");
		}
		
		int offset = x + CHUNK_WIDTH * (y + CHUNK_DEPTH * z);
		return offset;
	}
	
	@Override
	public ChunkPosition getChunkPosition() {
		return pos;
	}

	@Override
	public ChunkPosition getRelativeChunkPosition() {
		return new ChunkPosition(pos.getXPosition() / Chunk.X_MAX, pos.getZPosition() / Chunk.Z_MAX);
	}
}
