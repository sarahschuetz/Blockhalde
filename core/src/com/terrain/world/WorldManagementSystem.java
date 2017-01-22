package com.terrain.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.msg.MessageManager;
import com.badlogic.msg.Telegram;
import com.badlogic.msg.Telegraph;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.messaging.message.ChunkUpdateMessage;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.ChunkUtil;
import com.terrain.chunk.TerrainChunk;
import com.terrain.generators.PurePerlinTerrainGenerator;
import com.terrain.generators.TerrainGenerator;
import com.util.FlagUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorldManagementSystem extends EntitySystem implements WorldInterface {
    // All loaded chunks are placed in this map
    private final Map<ChunkPosition, Chunk> worldChunks = new HashMap<ChunkPosition, Chunk>();

    // All visible chunks in respect to the player position and the draw distance are stored here
    private final List<Chunk> visibleChunks = new ArrayList<Chunk>();

    // Defines how many chunks are drawn around the player
    private int drawDistance = 6;

    // TODO: Change so that Seed is not fix implemented here
//    private PerlinTerrainGenerator terrainGenerator = new SimplePerlinTerrainGenerator("Herst Bertl");
    private TerrainGenerator terrainGenerator = new PurePerlinTerrainGenerator("Herst Bertl");

     /**
     * Creates a new blank chunk at the specified position in the world
     */
    protected void createChunk(int xPosition, int zPosition) {
        final ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
        final Chunk chunk = new TerrainChunk(chunkPosition);

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
//    	Chunk[] chunks = null;
//    	chunks = worldChunks.values().toArray(new Chunk[] {});
//    	
//    	for(int i = 0; i < chunks.length; i++) {
//    		destroyChunk(chunks[i].getChunkPosition());
//    		chunks[i] = null;
//    	}
    	
    	//worldChunks.clear();
    	
    	// TODO: Delete!
    	// TODO: Copy data which should be deleted into a buffer for data persistence.
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
    	if (x < 0) x = x - Chunk.X_MAX;
    	if (z < 0) z = z - Chunk.Z_MAX;
        final Chunk chunk = getChunk(x, z);
        if (chunk != null) {
        	int relativeX = x < 0 ? Chunk.X_MAX + (x % Chunk.X_MAX) - 1 : x % Chunk.X_MAX;
        	int relativeZ = z < 0 ? Chunk.Z_MAX + (z % Chunk.Z_MAX) - 1 : z % Chunk.Z_MAX;
            chunk.setBlockAt(relativeX, y, relativeZ, blockType);
            
            // Send update message (TODO: eventually do this in Chunk implementation)
            MessageManager.getInstance().dispatchMessage(0f, null, null,
    				MessageIdConstants.BLOCK_UPDATED_MSG_ID,
    				new ChunkUpdateMessage(chunk.getChunkPosition(),
    						x, y, z));
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
        return getChunk(chunkPosition);
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
    	if (x < 0) x = x - Chunk.X_MAX;
    	if (z < 0) z = z - Chunk.Z_MAX;
        Chunk chunk = getChunk(x, z);
        if (chunk != null) {
        	int relativeX = x < 0 ? Chunk.X_MAX + (x % Chunk.X_MAX) - 1 : x % Chunk.X_MAX;
        	int relativeZ = z < 0 ? Chunk.Z_MAX + (z % Chunk.Z_MAX) - 1 : z % Chunk.Z_MAX;
        	return chunk.getBlockAt(relativeX, y, relativeZ);
        }
        return BlockType.AIR.getBlockId();
    }

    @Override
    public byte getBlockType(int x, int y, int z) {
    	return FlagUtils.getByteOf(getBlock(x, y, z), 0);
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
				deleteAllChunks();
				generateChunksAroundPlayer(position);
				return true;
			}
		}, MessageIdConstants.PLAYER_CHANGED_CHUNK_POSITION_MSG_ID);
    }

    @Override
    public void update(float deltaTime) {
    }
}