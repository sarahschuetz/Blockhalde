package com.terrain.chunk;

import com.terrain.block.BlockType;

public interface Chunk {
	int X_MAX = 16;
	int Y_MAX = 256;
	int Z_MAX = 16;

	int getWidth();
	int getHeight();
	int getDepth();
	
	short getBlockAt(int relativeX, int relativeY, int relativeZ);
	void setBlockAt(int relativeX, int relativeY, int relativeZ, BlockType type);

	ChunkPosition getChunkPosition();
	ChunkPosition getRelativeChunkPosition();
	
	long getLastModifiedTime();
}
