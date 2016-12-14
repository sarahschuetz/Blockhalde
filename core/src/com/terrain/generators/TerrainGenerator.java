package com.terrain.generators;

import com.terrain.chunk.Chunk;
import com.util.noise.PerlinNoise3D;

public interface TerrainGenerator {
    void generate(Chunk chunk);
}
