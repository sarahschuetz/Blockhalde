package com.terrain;

import com.util.noise.PerlinNoise3D;

public class TerrainChunk implements Chunk {
	
	private final short[][][] blocks = new short[WIDTH][HEIGHT][DEPTH];
	private final PerlinNoise3D perlinNoise = new PerlinNoise3D();
	
	public TerrainChunk() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				for(int z = 0; z < DEPTH; z++) {
					double noise = perlinNoise.calcPerlinAt(x, y, z);
					
					if(noise < 0.3) {
						blocks[x][y][z] = BlockType.AIR.getBlockId();
					} else {
						blocks[x][y][z] = BlockType.DIRT.getBlockId();
					}
				}
			}
		}
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
