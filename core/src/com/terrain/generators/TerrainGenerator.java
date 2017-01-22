package com.terrain.generators;
import com.terrain.chunk.Chunk;

public interface TerrainGenerator {
    void generate(Chunk chunk);
}
