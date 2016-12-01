package com.terrain.chunk;

import com.terrain.block.BlockType;
import com.terrain.world.World;
import com.util.noise.PerlinNoise3D;

public class TerrainChunk implements Chunk {
	
	private final short[][][] blocks = new short[X_MAX][Y_MAX][Z_MAX];
	private final PerlinNoise3D perlinNoise = new PerlinNoise3D("Heinzibert".hashCode());
	private ChunkPosition chunkPosition;
	private World world;
	
	public TerrainChunk(ChunkPosition chunkPosition, World world) {
		this.chunkPosition = chunkPosition;
		this.world = world;

		for(int x = 0; x < X_MAX; x++) {
			for(int y = 0; y < Y_MAX; y++) {
				for(int z = 0; z < Z_MAX; z++) {
					double noise = perlinNoise.calcPerlinAt((double) x / X_MAX, (double) y / Y_MAX, (double) z / Z_MAX);
					
					if(noise < 0.67) {
						blocks[x][y][z] = BlockType.AIR.getBlockId();
					} else {
						BlockType type;
						
						// NOTE TO THE TERRAIN TEAM
						
						// THIS IS JUST FOR DEMONSTRATION PURPOSES (FUN)
						// YOU CAN DELETE THIS
						
						if(y > 100) {
							type = BlockType.TNT;
						} else if(y > 20) {
							type = BlockType.GRASS;
						} else if(y > 10) {
							type = BlockType.DIRT;
						} else {
							type = BlockType.STONE;
						}
						
						blocks[x][y][z] = type.getBlockId();
					}
				}
			}
		}
	}

	public void setBlock(int x, int y, int z, BlockType blockType){
		
	}

	@Override
	public int getWidth() {
		return X_MAX;
	}

	@Override
	public int getHeight() {
		return Y_MAX;
	}

	@Override
	public int getDepth() {
		return Z_MAX;
	}

	@Override
	public short getBlockAt(int relativeX, int relativeY, int relativeZ) {
		return relativeX < Chunk.X_MAX && relativeY < Chunk.Y_MAX && relativeZ < Chunk.Z_MAX ? blocks[relativeX][relativeY][relativeZ] : BlockType.AIR.getBlockId();
	}
	
	@Override
	public void setBlockAt(int x, int y, int z, BlockType type) {
		blocks[x][y][z] = type.getBlockId();
	}

	@Override
	public ChunkPosition getChunkPosition() {
		return chunkPosition;
	}

	@Override
	public ChunkPosition getRelativeChunkPosition() {
		return new ChunkPosition(chunkPosition.getXPosition() / Chunk.X_MAX, chunkPosition.getZPosition() / Chunk.Z_MAX);
	}

}
