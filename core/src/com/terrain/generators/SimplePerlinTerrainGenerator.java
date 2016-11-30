package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.TerrainChunk;
import com.util.noise.PerlinNoise3D;


public class SimplePerlinTerrainGenerator implements TerrainGenerator {
    private int minimumHeight = 120;
//    private int maximumHeight = 150;

    @Override
    public void generate(TerrainChunk chunk, String seed) {
        generate(chunk, seed.hashCode());
    }

	@Override
	public void generate(TerrainChunk chunk, int hash) {
		PerlinNoise3D perlinNoise = new PerlinNoise3D(hash);

        for(int x = 0; x < Chunk.X_MAX; x++ ) {
            for(int y = 0; y < Chunk.Y_MAX; y++) {
                for(int z = 0; z < Chunk.Z_MAX; z++) {

                    if(y <= minimumHeight) {
                        chunk.setBlock(x, y, z, BlockType.DIRT);
                   // }else if(y >maximumHeight){
                     //   chunk.setBlock(x,y,z, BlockType.AIR);
                    } else {
                        double noise = perlinNoise.calcPerlinAt(
                        		(x + chunk.getChunkPosition().getXPosition()) / (double) Chunk.X_MAX,
                        		y / (double) Chunk.Y_MAX,
                        		(z + chunk.getChunkPosition().getZPosition()) / (double) Chunk.Z_MAX);
                        
                        if(noise > 0.8f - ((y - minimumHeight) * 0.08f)) {
                            chunk.setBlock(x, y, z, BlockType.AIR);
                        } else {
                            chunk.setBlock(x, y, z, BlockType.DIRT);
                        }
                    }
                }
            }
        }
	}
}
