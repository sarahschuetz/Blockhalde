package com.terrain;

import com.terrain.generators.FlatTerrainGenerator;
import com.terrain.generators.SimplePerlinTerrainGenerator;
import com.terrain.generators.TerrainGenerator;

import java.util.HashMap;
import java.util.Map;

public class World {

    //all loaded chunks are placed in this map
    private final Map<ChunkPosition, Chunk> worldChunks = new HashMap<ChunkPosition, Chunk>();

    public World(){
        //temporary solution to test the system
       for(int x = 0; x < 7; x++){
           for(int z = 0; z < 7; z++){
               createChunk(x*Chunk.X_MAX,z*Chunk.Z_MAX);
           }
       }
    }

    /**
     * creates a new blank chunk at the specified position in the world
     * @param xPosition
     * @param zPosition
     */
    public void createChunk(int xPosition, int zPosition ){
        ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        Chunk chunk = new TerrainChunk(chunkPosition, this);
        TerrainGenerator terrainGenerator = new SimplePerlinTerrainGenerator();
        terrainGenerator.generate((TerrainChunk)chunk, "Heinzibert");
        worldChunks.put(chunkPosition, chunk);
    }

    /**
     * removes the chunk that is located at the specified position in the world
     * @param xPosition
     * @param zPosition
     */
    public void destroyChunk(int xPosition, int zPosition){
        ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        if(worldChunks.containsKey(chunkPosition)){
            Chunk chunk = worldChunks.remove(chunkPosition);
        }
    }

    /**
     * returns the chunk that is located at the specified position in the world
     * @param xPosition
     * @param zPosition
     * @return
     */
    public Chunk getChunk(int xPosition, int zPosition){
        ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        if(worldChunks.containsKey(chunkPosition)){
            return worldChunks.get(chunkPosition);
        }
        return null;
    }
}
