package com.terrain.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.msg.MessageManager;
import com.badlogic.msg.Telegram;
import com.badlogic.msg.Telegraph;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.ChunkUtil;
import com.terrain.chunk.TerrainChunk;
import com.terrain.generators.SimplePerlinTerrainGenerator;
import com.terrain.generators.TerrainGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class WorldManagementSystem extends EntitySystem implements WorldInterface {
    // All loaded chunks are placed in this map
    private final Map<ChunkPosition, Chunk> worldChunks = new HashMap<ChunkPosition, Chunk>();

    // All visible chunks in respect to the player position and the draw distance are stored here
    private final List<Chunk> visibleChunks = new ArrayList<Chunk>();

    // Defines how many chunks are drawn around the player
    // TODO: Find a cool name
    private int drawDistance = 3;

    // TODO: Change so that Seed is not fix implemented here
    private TerrainGenerator terrainGenerator = new SimplePerlinTerrainGenerator("Herst Bertl");
//       private TerrainGenerator terrainGenerator = new PurePerlinTerrainGenerator("Herst Bertl");

    // for testing purposes
    private Chunk currentPlayerChunk;

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
    protected void destroyChunk(ChunkPosition chunkPosition) {
        if (worldChunks.containsKey(chunkPosition)) {
            worldChunks.remove(chunkPosition);
            MessageManager.getInstance().dispatchMessage(0, null, null, MessageIdConstants.CHUNK_DELETED_MSG_ID, new ChunkMessage(chunkPosition));
        }
    }
    
    protected void deleteAllChunks() {
    	final Set<ChunkPosition> keys = worldChunks.keySet();
    	for(ChunkPosition c : keys) {
    		destroyChunk(c);
    	}
    }
    
    public void generateChunksAroundPlayer(ChunkPosition position) {
    	Objects.nonNull(position);
    	
    	final ChunkPosition origin = ChunkUtil.getRelativeChunkPosition(position);
    	for (int x = -drawDistance + origin.getXPosition(); x <= drawDistance + origin.getXPosition(); x++) {
    		for (int z = -drawDistance + origin.getZPosition(); z <= drawDistance + origin.getZPosition(); z++) {
    			Chunk chunk = getChunk(x * Chunk.X_MAX, z * Chunk.Z_MAX);

    			if (chunk == null) {
    				createChunk(x * Chunk.X_MAX, z * Chunk.Z_MAX);
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
        final ChunkPosition chunkPosition = ChunkUtil.getChunkPositionFor(xPosition, zPosition);
        getChunk(chunkPosition);
        return null;
    }
	
	@Override
	public Chunk getChunk(ChunkPosition chunkPosition) {
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
    public byte getBlockType(int x, int y, int z) {
        Chunk chunk = getChunk(x, z);
        if (chunk != null) {
            return chunk.getBlockTypeAt(x % Chunk.X_MAX, y % Chunk.Y_MAX, z % Chunk.Z_MAX);
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
        MessageManager.getInstance().addListener(new Telegraph() {
			@Override
			public boolean handleMessage(Telegram msg) {
				final ChunkPosition position = ((ChunkMessage) msg.extraInfo).getChunkPosition();
				// TODO: Delete old chunks ==
				// TODO: Generate new chunks around the player (drawDistance, chunkPosition)
				
				// TODO: Don't delete all chunks, only the difference between new and old chunks
				deleteAllChunks();
				generateChunksAroundPlayer(position);
				System.out.println("Chunk update blabla");
				return true;
			}
		}, MessageIdConstants.PLAYER_CHANGED_CHUNK_POSITION_MSG_ID);
    }

    @Override
    public void update(float deltaTime) {
    }
}