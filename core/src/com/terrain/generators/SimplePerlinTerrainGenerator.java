package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.util.noise.PerlinNoise3D;


public class SimplePerlinTerrainGenerator implements TerrainGenerator {
    private int minimumDirtHeight = 120;
    private int minimumGrassHeight = 160;
    private int minimumTNTHeight = 206;
    private int minimumAirHeight = 200;


    @Override
    public void generate(Chunk chunk, String seed) {
        generate(chunk, seed.hashCode());
    }

	@Override
	public void generate(Chunk chunk, int hash) {
		PerlinNoise3D perlinNoise = new PerlinNoise3D(hash);

        for(int x = 0; x < Chunk.X_MAX; x++ ) {
            for(int y = 0; y < Chunk.Y_MAX; y++) {
                for(int z = 0; z < Chunk.Z_MAX; z++) {

                    if(y <= minimumDirtHeight) {
                        chunk.setBlockAt(x, y, z, BlockType.STONE);
                    }else if(y <= minimumGrassHeight){
                        chunk.setBlockAt(x, y, z, BlockType.DIRT);
                    } else {
                        double noise = perlinNoise.calcPerlinAt(
                        		(x + chunk.getChunkPosition().getXPosition()) / (double) Chunk.X_MAX,
                        		y / (double) Chunk.Y_MAX,
                        		(z + chunk.getChunkPosition().getZPosition()) / (double) Chunk.Z_MAX);
                        
                        if(noise > 0.8f - ((y - minimumAirHeight) * 0.05f)) {
                            chunk.setBlockAt(x, y, z, BlockType.AIR);
                        } else {
                            if(y <= minimumTNTHeight){
                                chunk.setBlockAt(x, y, z, BlockType.GRASS);
                            }else{
                                chunk.setBlockAt(x, y, z, BlockType.TNT);
                            }
                        }
                    }
                }
            }
        }
	}
}
