package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.util.noise.PerlinNoise3D;

public class PurePerlinTerrainGenerator implements TerrainGenerator {
	
//	private int minimumDirtHeight = 120;
//    private int minimumGrassHeight = 160;
//    private int minimumTNTHeight = 206;
    private int minimumAirHeight = 60;

	@Override
	public void generate(Chunk chunk, String seed) {
		generate(chunk, seed.hashCode());
	}

	@Override
	public void generate(Chunk chunk, int hash) {
		PerlinNoise3D perlinNoise = new PerlinNoise3D(hash);
		double smoothness = Math.random() * 10.0 + 120.0; // random between 20 & 30
		
		for(int x = 0; x < Chunk.X_MAX; x++ ) {
			for(int z = 0; z < Chunk.Z_MAX; z++) {
				
				double height = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, 0); // Find out the height 
				
				for(int y = 0; y < (height * Chunk.Y_MAX); y++) {
                	double density = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, y / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness); // Find out the density
                	
                	if(y > minimumAirHeight) {
                		if(density > 0.7) {
                			chunk.setBlockAt(x, y, z, BlockType.AIR);
                		} 
                	} else {
                		chunk.setBlockAt(x, y, z, BlockType.DIRT);
                	}
                	
                	
//                	double noise = perlinNoise.calcPerlinAt(
//                    		(x + chunk.getChunkPosition().getXPosition()) / (double) Chunk.X_MAX,
//                    		y / (double) Chunk.Y_MAX,
//                    		(z + chunk.getChunkPosition().getZPosition()) / (double) Chunk.Z_MAX, 3, 7);
                	
//                	if(noise < 0.5) {
//                		chunk.setBlockAt(x, y, z, BlockType.DIRT);
//                	} else {
//                		chunk.setBlockAt(x, y, z, BlockType.AIR);
//                	}
                }
            }
		}
	}
}
