package com.terrain.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.msg.MessageManager;
import com.blockhalde.render.CameraSystem;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.TerrainChunk;
import com.terrain.generators.PurePerlinTerrainGenerator;
import com.terrain.generators.SimplePerlinTerrainGenerator;
import com.terrain.generators.TerrainGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldManagementSystem extends EntitySystem implements WorldInterface {
    // All loaded chunks are placed in this map
    private final Map<ChunkPosition, Chunk> worldChunks = new HashMap<ChunkPosition, Chunk>();

    // All visible chunks in respect to the player position and the draw distance are stored here
    private final List<Chunk> visibleChunks = new ArrayList<Chunk>();

    // Defines how many chunks are drawn around the player
    private int drawDistance = 1;

    // TODO: Add player position and generate chunks based on it.
    private Camera camera;
    
    // TODO: Change so that Seed is not fix implemented here
    private TerrainGenerator terrainGenerator = new SimplePerlinTerrainGenerator("Herst Bertl");
//       private TerrainGenerator terrainGenerator = new PurePerlinTerrainGenerator("Herst Bertl");

    // for testing purposes
    private Chunk currentPlayerChunk;
    private Vector3 playerPosition = new Vector3(0 * Chunk.X_MAX, 0, 3 * Chunk.Z_MAX);

     /**
     * Creates a new blank chunk at the specified position in the world
     */
    protected void createChunk(int xPosition, int zPosition) {
        final ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        final Chunk chunk = new TerrainChunk(chunkPosition);

        // TODO: Make the terrain generator somehow changeable
        terrainGenerator.generate(chunk);

        worldChunks.put(chunkPosition, chunk);
        
        MessageManager.getInstance().dispatchMessage(0f, null, null, MessageIdConstants.CHUNK_CREATED_MSG_ID, new ChunkMessage(chunkPosition));
    }

    /**
     * Removes the chunk that is located at the specified position in the world
     */
    protected void destroyChunk(int xPosition, int zPosition) {
        final ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        if (worldChunks.containsKey(chunkPosition)) {
            worldChunks.remove(chunkPosition);
            MessageManager.getInstance().dispatchMessage(0, null, null, MessageIdConstants.CHUNK_DELETED_MSG_ID, new ChunkMessage(chunkPosition));
        }
    }

    /**
     * calculates the visible chunks based on the player position and the draw
     * distance
     */
    protected void calculateVisibleChunks(Vector3 playerPosition, int drawDistance) {
        visibleChunks.clear();
        Chunk newPlayerChunk = getChunk((int) playerPosition.x, (int) playerPosition.z);
        if(newPlayerChunk != currentPlayerChunk && newPlayerChunk!=null){
            currentPlayerChunk = newPlayerChunk;
            ChunkPosition origin = currentPlayerChunk.getRelativeChunkPosition();
            for (int x = -drawDistance + origin.getXPosition(); x <= drawDistance + origin.getXPosition(); x++) {
                for (int z = -drawDistance + origin.getZPosition(); z <= drawDistance + origin.getZPosition(); z++) {
                    Chunk chunk = getChunk(x * Chunk.X_MAX, z * Chunk.Z_MAX);
                    if (chunk != null) {
                        visibleChunks.add(chunk);
                    }
                }
            }
        }
    }
   
    @Override
    public void setBlock(int x, int y, int z, BlockType blockType) {
        final Chunk chunk = getChunk(x, z);
        if (chunk != null) {
            chunk.setBlockAt(x % Chunk.X_MAX, y % Chunk.Y_MAX, z % Chunk.Z_MAX, blockType);
        }
    }

    /**
     * Sets the amount of chunks that are rendered in each direction
     */
    public void setDrawDistance(int drawDistance) {
        this.drawDistance = drawDistance;
    }
    
    public TerrainGenerator getTerrainGenerator() {
    	return terrainGenerator;
    }

    //-------- WorldInterface Implementation

	@Override
    public Chunk getChunk(int xPosition, int zPosition) {
        ChunkPosition chunkPosition = new ChunkPosition(xPosition/Chunk.X_MAX* Chunk.X_MAX, zPosition/Chunk.Z_MAX*Chunk.Z_MAX);
        if (worldChunks.containsKey(chunkPosition)) {
            return worldChunks.get(chunkPosition);
        }
        return null;
    }

    @Override
    public short getBlock(int x, int y, int z) {
        Chunk chunk = getChunk(x, z);
        if (chunk != null) {
            return chunk.getBlockAt(x % Chunk.X_MAX, y % Chunk.Y_MAX, z % Chunk.Z_MAX);
        }
        return BlockType.AIR.getBlockId();
    }

    @Override
    public List<Chunk> getVisibleChunks() {
        return visibleChunks;
    }
    
    @Override
    public int getDrawDistance() {
    	return drawDistance;
    }
    //-------- EntitySystem overridden methods

    @Override
    public void addedToEngine(Engine engine) {
        CameraSystem cameraSystem = engine.getSystem(CameraSystem.class);
        if(cameraSystem!=null){
            camera = cameraSystem.getCam();
        }

        // temporary solution to test the system
        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                createChunk(x * Chunk.X_MAX, z * Chunk.Z_MAX);
            }
        }

        //TODO: change later
        //calculateVisibleChunks(camera.position, drawDistance);
        calculateVisibleChunks(playerPosition, drawDistance);
    }

    @Override
    public void update(float deltaTime) {
        //TODO: change later
        //calculateVisibleChunks(camera.position, drawDistance);
        calculateVisibleChunks(playerPosition, drawDistance);
    }
}