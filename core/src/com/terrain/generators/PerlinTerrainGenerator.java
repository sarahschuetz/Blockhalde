package com.terrain.generators;

import com.util.noise.PerlinNoise3D;

public interface PerlinTerrainGenerator extends TerrainGenerator {
	
    PerlinNoise3D getPerlinNoise();
	
	double getSmoothness();
	void setSmoothness(double smoothness);
	
	int getOctaves();
	void setOctaves(int octaves);
	
	double getPersistence();
	void setPersistence(double persistence);
	
	double getFrequency();
	void setFrequency(double frequency);
}
