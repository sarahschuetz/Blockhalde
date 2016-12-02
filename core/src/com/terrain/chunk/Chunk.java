package com.terrain.chunk;

import com.terrain.block.BlockType;

public interface Chunk {
	int X_MAX = 16;
	int Y_MAX = 256;
	int Z_MAX = 16;

	int getWidth();
	int getHeight();
	int getDepth();
	
	void setBlockAt(int x, int y, int z, BlockType type);
	short getBlockAt(int relativeX, int relativeY, int relativeZ);

	ChunkPosition getChunkPosition();
	ChunkPosition getRelativeChunkPosition();
}
