package com.terrain;

public interface Chunk {
	
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int DEPTH = 256;
	
	int getWidth();
	int getHeight();
	int getDepth();
	
	short getBlockAt(int x, int y, int z);
}
