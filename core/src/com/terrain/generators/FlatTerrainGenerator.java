package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;


public class FlatTerrainGenerator extends BaseTerrainGenerator {
   
	public FlatTerrainGenerator() {
		super();
	}

	private final int height = 125;

	@Override
	public void generate(Chunk chunk) {
		for(int x = 0; x < Chunk.X_MAX; x++ ) {
            for(int y = 0; y < Chunk.Y_MAX; y++) {
                for(int z = 0; z < Chunk.Z_MAX; z++) {
                    if(y <= height) {
                        chunk.setBlockAt(x,y,z, BlockType.DIRT);
                    } else {
                        chunk.setBlockAt(x,y,z, BlockType.AIR);
                    }
                }
            }
        }
	}
}