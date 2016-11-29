package com.terrain.generators;

import com.terrain.TerrainChunk;

public interface TerrainGenerator {

    void generate(TerrainChunk chunk, String seed);

}
