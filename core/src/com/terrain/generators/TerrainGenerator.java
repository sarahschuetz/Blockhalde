package com.terrain.generators;

import com.terrain.chunk.TerrainChunk;

public interface TerrainGenerator {
    void generate(TerrainChunk chunk, String seed);
    void generate(TerrainChunk chunk, int hash);
}
