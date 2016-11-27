package com.terrain;

public class TerrainChunk implements Chunk {
	
	private final short[][][] blocks = new short[WIDTH][HEIGHT][DEPTH];
	
	public TerrainChunk() {
		
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public int getDepth() {
		return DEPTH;
	}

	@Override
	public short getBlockAt(int x, int y, int z) {
		return blocks[x][y][z];
	}
}
