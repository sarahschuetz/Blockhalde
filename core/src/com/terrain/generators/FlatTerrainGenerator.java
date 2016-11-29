package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.TerrainChunk;

public class FlatTerrainGenerator implements TerrainGenerator {
    private final int height = 125;

    @Override
    public void generate(TerrainChunk chunk, String seed) {
    	generate(chunk, seed.hashCode());
    }

	@Override
	public void generate(TerrainChunk chunk, int hash) {
		for(int x = 0; x < Chunk.X_MAX; x++ ) {
            for(int y = 0; y < Chunk.Y_MAX; y++) {
                for(int z = 0; z < Chunk.Z_MAX; z++) {
                    if(y <= height) {
                        chunk.setBlock(x,y,z, BlockType.DIRT);
                    } else {
                        chunk.setBlock(x,y,z, BlockType.AIR);
                    }
                }
            }
        }
	}
}