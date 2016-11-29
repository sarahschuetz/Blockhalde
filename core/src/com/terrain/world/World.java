package com.terrain.world;

import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.TerrainChunk;
import com.terrain.generators.SimplePerlinTerrainGenerator;
import com.terrain.generators.TerrainGenerator;

import java.util.HashMap;
import java.util.Map;

public class World implements WorldInterface {
    //All loaded chunks are placed in this map
    private final Map<ChunkPosition, Chunk> worldChunks = new HashMap<ChunkPosition, Chunk>();
    
    // TODO: Add player position and generate chunks based on it.

    public World() {
        //temporary solution to test the system
       for(int x = 0; x < 7; x++) {
           for(int z = 0; z < 7; z++) {
               createChunk(x * Chunk.X_MAX, z * Chunk.Z_MAX);
           }
       }
    }

    /**
     * Creates a new blank chunk at the specified position in the world
     */
    protected void createChunk(int xPosition, int zPosition ) {
        ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        Chunk chunk = new TerrainChunk(chunkPosition, this);
        
        // TODO: Make the terrain generator somehow changeable
        TerrainGenerator terrainGenerator = new SimplePerlinTerrainGenerator();
        terrainGenerator.generate((TerrainChunk)chunk, "Herst");
        worldChunks.put(chunkPosition, chunk);
    }

    /**
     * Removes the chunk that is located at the specified position in the world
     */
    protected void destroyChunk(int xPosition, int zPosition) {
        ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        if(worldChunks.containsKey(chunkPosition)) {
            worldChunks.remove(chunkPosition);
        }
    }

    @Override
    public Chunk getChunk(int xPosition, int zPosition) {
        ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        if(worldChunks.containsKey(chunkPosition)){
            return worldChunks.get(chunkPosition);
        }
        return null;
    }
}
