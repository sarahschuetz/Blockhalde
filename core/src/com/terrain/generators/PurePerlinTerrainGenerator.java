package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.util.noise.PerlinNoise3D;

public class PurePerlinTerrainGenerator extends BasePerlinTerrainGenerator {
    
    private int maxBaseTerrainHeight = 10; // base height for terrain, you can not dig deeper
    private int maxBaseDirtHeight = 30; // base height for dirt
    private int maxBaseAirHeight = 100; // base height for air
    private int maxMaxTerrainHeight = 200; // only air from here
    private int maxBaseSnowHeight = 150;

	public PurePerlinTerrainGenerator() {
		super();
	}

	public PurePerlinTerrainGenerator(int hash) {
		super(hash);
	}

	public PurePerlinTerrainGenerator(String seed) {
		super(seed);
	}
	
	private void correctSnow(Chunk chunk) {
		for(int x = 0; x < Chunk.X_MAX; x++ ) {
			for(int z = 0; z < Chunk.Z_MAX; z++) {
				for(int y = 0; y < Chunk.Y_MAX - 1; y++) {
					
					if(chunk.getBlockAt(x, y, z) == BlockType.SNOW.getBlockId()) {
						if(chunk.getBlockAt(x, y + 1, z) != BlockType.AIR.getBlockId()) {
							chunk.setBlockAt(x, y, z, BlockType.DIRT);
						}
					}
				}
			}
		}
	}

	@Override
	public void generate(Chunk chunk) {
		PerlinNoise3D perlinNoise = getPerlinNoise();
		
		for(int x = 0; x < Chunk.X_MAX; x++ ) {
			for(int z = 0; z < Chunk.Z_MAX; z++) {
				
				double heightMap = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, 0, 2, 1); // generate Heightmap from 2d perlin noise 
				double heightMap2 = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, 0, 3, 1); // generate Heightmap from 2d perlin noise 
				double heightMap3 = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, 0, 1, 1); // generate Heightmap from 2d perlin noise 
				
				heightMap = (heightMap + heightMap2) * 0.5;
				heightMap = (heightMap + heightMap3) * 0.5;
						
				int minTerrainHeight = (int) (maxBaseTerrainHeight * heightMap) + 1; // between 1 maxBaseTerrain
				int minDirtHeight = maxBaseDirtHeight - (int) Math.ceil(20 * heightMap);
				int minAirHeight = (int) Math.ceil(maxBaseAirHeight * heightMap);
				int maxTerrainHeight = maxMaxTerrainHeight - (int) Math.ceil(20 * heightMap); // between maxMaxTerrainHeight and (maxMaxTerrainHeight - 20)
				int snowHeight = maxBaseSnowHeight - (int) Math.ceil(50 * heightMap);
				
				for(int y = 0; y < (heightMap * Chunk.Y_MAX); y++) {
//				for(int y = 0; y < Chunk.Y_MAX; y++) {
                	double density = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, y / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, octaves, persistence); // Find out the density
                	double density2 = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, y / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, 5, persistence); // Find out the density
                	
                	density = (density + density2) * 0.5;
                	
                	// outside terrain area
                	if(y < minTerrainHeight) {
                		chunk.setBlockAt(x, y, z, BlockType.TNT);
                	} else if (y > maxTerrainHeight) {
                		chunk.setBlockAt(x, y, z, BlockType.AIR);
                	} else {
                		
                		// generate terrain
                		if(y < minDirtHeight) { // only generate stone
                			chunk.setBlockAt(x, y, z, BlockType.STONE);
                		} else { 
                			if(y < minAirHeight) { // generate stone and dirt
                				if(density < 0.4) {
                					chunk.setBlockAt(x, y, z, BlockType.DIRT);
                				} else {
                					chunk.setBlockAt(x, y, z, BlockType.STONE);
                				}
                			} else { // generate stone, dirt and air
                				if(density < 0.4) {
                					chunk.setBlockAt(x, y, z, BlockType.AIR);
                				} else if(density < 0.6) {
                					if(y > snowHeight) {
                						chunk.setBlockAt(x, y, z, BlockType.SNOW);
                					} else {
                						chunk.setBlockAt(x, y, z, BlockType.DIRT);
                					}
                				} else {
                					chunk.setBlockAt(x, y, z, BlockType.STONE);
                				}
                			}
                		}
                	}
                }
            }
		}
		
		correctSnow(chunk);
	}
}
