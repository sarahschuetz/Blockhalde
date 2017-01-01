package com.terrain.generators;

import com.util.noise.PerlinNoise3D;

public abstract class BasePerlinTerrainGenerator extends BaseTerrainGenerator implements PerlinTerrainGenerator {
	
	protected PerlinNoise3D perlinNoise;

	protected double smoothness = 1.0;
	protected int octaves = 2;
	protected double persistence = 5.0;
	protected double frequency = 1.0;
	
	public BasePerlinTerrainGenerator() {
		this.perlinNoise = new PerlinNoise3D();
	}
	
	public BasePerlinTerrainGenerator(int hash) {
		this.perlinNoise = new PerlinNoise3D(hash);
	}
	
	public BasePerlinTerrainGenerator(String seed) {
		this(seed.hashCode());
	}

	@Override
	public PerlinNoise3D getPerlinNoise() {
		return perlinNoise;
	}
	
	@Override
	public double getSmoothness() {
		return smoothness;
	}

	@Override
	public void setSmoothness(double smoothness) {
		this.smoothness = smoothness;
	}

	@Override
	public int getOctaves() {
		return octaves;
	}

	@Override
	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}

	@Override
	public double getPersistence() {
		return persistence;
	}

	@Override
	public void setPersistence(double persistence) {
		this.persistence = persistence;
	}

	@Override
	public double getFrequency() {
		return frequency;
	}

	@Override
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
}
