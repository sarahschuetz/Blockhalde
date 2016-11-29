package com.terrain;

public interface Chunk {
	
	static final int X_MAX = 16;
	static final int Y_MAX = 256;
	static final int Z_MAX = 16;

	int getWidth();
	int getHeight();
	int getDepth();
	
	short getBlockAt(int x, int y, int z);
	ChunkPosition getChunkPosition();

}
