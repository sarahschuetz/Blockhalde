package com.blockhalde;

import com.terrain.BlockType;

public class BlockChunk {

	private static final int CHUNK_WIDTH = 16;
	private static final int CHUNK_DEPTH = 16;
	private static final int CHUNK_HEIGHT = 256;
	private static final int BLOCK_COUNT = CHUNK_WIDTH * CHUNK_DEPTH * CHUNK_HEIGHT;
	
	private byte[] blockTypes = new byte[BLOCK_COUNT];
	
	private long lastModifiedTime = System.nanoTime();
	
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
	
	public byte getBlockTypeAt(int x, int y, int z) {
		int offset = coordsToFlatIndex(x, y, z);
		return blockTypes[offset];
	}
	
	public byte getBlockTypeAt(int offset) {
		return blockTypes[offset];
	}
	
	public void setBlockTypeAt(BlockType type, int x, int y, int z) {
		setBlockTypeAt(type.getBlockId(), coordsToFlatIndex(x, y, z));
	}
	
	public void setBlockTypeAt(byte type, int x, int y, int z) {
		setBlockTypeAt(type, coordsToFlatIndex(x, y, z));
	}
	
	public void setBlockTypeAt(byte type, int offset)
	{
		if(offset < 0 || offset >= BLOCK_COUNT) {
			throw new IndexOutOfBoundsException("Flat offset is out of bounds: " + offset);
		}
		
		blockTypes[offset] = type;
		setLastModifiedNow();
	}
	
	public boolean isOpaqueAt(int x, int y, int z) {
		byte type = getBlockTypeAt(x, y, z);
		return type != BlockType.AIR.getBlockId() && type != BlockType.WATER.getBlockId();
	}

	public int coordsToFlatIndex(int x, int y, int z) {
		if(x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_DEPTH) {
			throw new IndexOutOfBoundsException("Index set: (" + x + "," + y + "," + z + ") is out of bounds");
		}
		
		int offset = x + z * CHUNK_WIDTH + y * (CHUNK_WIDTH + CHUNK_HEIGHT);
		return offset;
	}
	
	public int flatIndexToX(int offset) {
		return offset % (CHUNK_WIDTH + CHUNK_HEIGHT) % CHUNK_WIDTH;
	}
	
	public int flatIndexToY(int offset) {
		return offset / (CHUNK_WIDTH+CHUNK_HEIGHT);
	}
	
	public int flatIndexToZ(int offset) {
		offset -= offset / (CHUNK_WIDTH+CHUNK_HEIGHT) * (CHUNK_WIDTH+CHUNK_HEIGHT);
		return offset / CHUNK_WIDTH;
	}
}
