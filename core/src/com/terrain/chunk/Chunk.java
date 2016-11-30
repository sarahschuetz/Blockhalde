package com.terrain.chunk;

import com.terrain.block.BlockType;

public interface Chunk {
	static final int X_MAX = 16;
	static final int Y_MAX = 256;
	static final int Z_MAX = 16;

	int getWidth();
	int getHeight();
	int getDepth();
	
	void setBlockAt(int x, int y, int z, BlockType type);
	short getBlockAt(int x, int y, int z);
	ChunkPosition getChunkPosition();
	ChunkPosition getRelativeChunkPosition();
}
