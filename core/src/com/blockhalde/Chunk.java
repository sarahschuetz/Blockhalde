package com.blockhalde;

public interface Chunk {
	int getWidth();
	int getHeight();
	int getDepth();
	
	int getBlockAt(int x, int y, int z);
}
