package com.terrain.generators;

import com.terrain.chunk.Chunk;
import com.util.noise.PerlinNoise3D;

public abstract class BaseTerrainGenerator implements TerrainGenerator {
	
	private PerlinNoise3D perlinNoise;
	
	public BaseTerrainGenerator() {
		this.perlinNoise = new PerlinNoise3D();
	}
	
	public BaseTerrainGenerator(int hash) {
		this.perlinNoise = new PerlinNoise3D(hash);
	}
	
	public BaseTerrainGenerator(String seed) {
		this(seed.hashCode());
	}

	@Override
	public PerlinNoise3D getPerlinNoise() {
		return perlinNoise;
	}
	
	@Override
	public void generate(Chunk chunk, double smoothness, int octaves, double persistence) {
		generate(chunk);
	}
}
