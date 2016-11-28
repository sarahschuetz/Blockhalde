package com.terrain;

import com.util.noise.PerlinNoise3D;

public class TerrainChunk implements Chunk {
	
	private final short[][][] blocks = new short[X_MAX][Y_MAX][Z_MAX];
	private final PerlinNoise3D perlinNoise = new PerlinNoise3D("Heinzibert".hashCode());
	
	public TerrainChunk() {
		for(int x = 0; x < X_MAX; x++) {
			for(int y = 0; y < Y_MAX; y++) {
				for(int z = 0; z < Z_MAX; z++) {
					double noise = perlinNoise.calcPerlinAt((double) x / X_MAX, (double) y / Y_MAX, (double) z / Z_MAX);
					
					if(noise < 0.67) {
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
		return X_MAX;
	}

	@Override
	public int getHeight() {
		return Y_MAX;
	}

	@Override
	public int getDepth() {
		return Z_MAX;
	}

	@Override
	public short getBlockAt(int x, int y, int z) {
		return blocks[x][y][z];
	}
}
