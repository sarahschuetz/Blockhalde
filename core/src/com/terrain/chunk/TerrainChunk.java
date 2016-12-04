package com.terrain.chunk;

import com.terrain.block.BlockType;

public class TerrainChunk implements Chunk {

    private final short[][][] blocks = new short[X_MAX][Y_MAX][Z_MAX];
	private final short[] blocks_flat = new short[X_MAX * Y_MAX * Z_MAX];
    private ChunkPosition chunkPosition;

    public TerrainChunk(ChunkPosition chunkPosition) {
        this.chunkPosition = chunkPosition;
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
        return relativeX < Chunk.X_MAX && relativeY < Chunk.Y_MAX && relativeZ < Chunk.Z_MAX && relativeX > -1 && relativeY > -1 && relativeZ > -1 ? blocks[relativeX][relativeY][relativeZ] : BlockType.AIR.getBlockId();
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
