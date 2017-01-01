package com.terrain.generators;

import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.util.noise.PerlinNoise3D;

public class PurePerlinTerrainGenerator extends BasePerlinTerrainGenerator {
	
	private int minimumDirtHeight = 60;
//    private int minimumGrassHeight = 160;
//    private int minimumTNTHeight = 206;
    private int minimumAirHeight = 250;

	public PurePerlinTerrainGenerator() {
		super();
	}

	public PurePerlinTerrainGenerator(int hash) {
		super(hash);
	}

	public PurePerlinTerrainGenerator(String seed) {
		super(seed);
	}

	@Override
	public void generate(Chunk chunk) {
		PerlinNoise3D perlinNoise = getPerlinNoise();
		
		for(int x = 0; x < Chunk.X_MAX; x++ ) {
			for(int z = 0; z < Chunk.Z_MAX; z++) {
				
				double height = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness, 0); // generate Heightmap from 2d perlin noise 
				
				for(int y = 0; y < (height * Chunk.Y_MAX); y++) {
//				for(int y = 0; y < Chunk.Y_MAX; y++) {
                	double density = perlinNoise.calcPerlinAt((x + chunk.getChunkPosition().getXPosition()) / smoothness, y / smoothness, (z + chunk.getChunkPosition().getZPosition()) / smoothness); // Find out the density
                	
                	if(y > minimumAirHeight) {
                		if(density > 0.3) {
                			chunk.setBlockAt(x, y, z, BlockType.AIR);
                		} else {
                			chunk.setBlockAt(x, y, z, BlockType.TNT);
                		}
                	} else if(y > minimumDirtHeight) {
                		if(density > 0.8) {
                			chunk.setBlockAt(x, y, z, BlockType.STONE);
                		} else if(density > 0.6) {
                			chunk.setBlockAt(x, y, z, BlockType.GRASS);
                		} else if(density > 0.4) {
                			chunk.setBlockAt(x, y, z, BlockType.SNOW);
                		} else if(density > 0.2) {
                			chunk.setBlockAt(x, y, z, BlockType.TNT);
                		}  else {
                    		chunk.setBlockAt(x, y, z, BlockType.DIRT);
                    	}
                	} else {
                		chunk.setBlockAt(x, y, z, BlockType.DIRT);
                	}
                }
            }
		}
	}
	
	
//	local smoothness = math.random(20, 30)
//		    for x = 1, mapScale do
//		        wait()
//		        for z = 1, mapScale do
	
//		            local height = noise(x/smoothness, z/smoothness, 0) --Find out the height 
	
//		            for y = 1, (height*mapHeight)+10 do
//		                local density = noise(x/smoothness, y/smoothness, z/smoothness) --Find out the density
//		                if y > waterLvl then
//		                    if density*10 > 0 then
//		                        t:setCell(x, y, z, 1, 0, 0) --Sets cell at position x, y, z
//		                    end
//		                else
//		                    t:setCell(x, y, z, 1, 0, 0)
//		            end
//		        end
//		    end
//		end   
	
	
}
