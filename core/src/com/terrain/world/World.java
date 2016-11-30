package com.terrain.world;

import com.badlogic.gdx.math.Vector3;
import com.render.BlockChunk;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.TerrainChunk;
import com.terrain.generators.SimplePerlinTerrainGenerator;
import com.terrain.generators.TerrainGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World implements WorldInterface {
	// All loaded chunks are placed in this map
	private final Map<ChunkPosition, Chunk> worldChunks = new HashMap<ChunkPosition, Chunk>();

	// All visible chunks in respect to the player position and the draw
	// distance are stored here
	private final List<Chunk> visibleChunks = new ArrayList<Chunk>();

	private int drawDistance = 1;
	// TODO: Add player position and generate chunks based on it.

	// for testing purposes
	private Chunk currentPlayerChunk;
	private Vector3 playerPosition = new Vector3(0 * Chunk.X_MAX, 1, 0 * Chunk.Z_MAX);

	public World() {
		// temporary solution to test the system
		for (int x = -9; x < 9; x++) {
			for (int z = -9; z < 9; z++) {
				createChunk(x * Chunk.X_MAX, z * Chunk.Z_MAX);
			}
		}

		currentPlayerChunk = getChunk((int) playerPosition.x, (int) playerPosition.z);
		calculateVisibleChunks(playerPosition, drawDistance);
	}

	/**
	 * Creates a new blank chunk at the specified position in the world
	 */
	protected void createChunk(int xPosition, int zPosition) {
		ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
		Chunk chunk = new TerrainChunk(chunkPosition, this);

		// TODO: Make the terrain generator somehow changeable
		TerrainGenerator terrainGenerator = new SimplePerlinTerrainGenerator();
		terrainGenerator.generate((TerrainChunk) chunk, "Herst");

		worldChunks.put(chunkPosition, chunk);
	}

	/**
	 * Removes the chunk that is located at the specified position in the world
	 */
	protected void destroyChunk(int xPosition, int zPosition) {
		ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
		if (worldChunks.containsKey(chunkPosition)) {
			worldChunks.remove(chunkPosition);
		}
	}

	/**
	 * calculates the visible chunks based on the player position and the draw
	 * distance
	 */
	protected void calculateVisibleChunks(Vector3 playerPosition, int drawDistance) {
		visibleChunks.clear();
		currentPlayerChunk = getChunk((int) playerPosition.x, (int) playerPosition.z);
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

	/**
	 * Sets a blocktype at the specified world position.
	 */
	public void setBlock(int x, int y, int z, BlockType blockType) {
		Chunk chunk = getChunk(x, z);
		if (chunk != null) {
			// TODO set up write permission interface for chunk, then replace
			// the following line
			((TerrainChunk) chunk).setBlock(x % Chunk.X_MAX, y % Chunk.Y_MAX, z % Chunk.Z_MAX, blockType);
		}
	}

	/**
	 * Sets the amount of chunks that are rendered in each direction
	 */
	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}
	

	@Override
	public Chunk getChunk(int xPosition, int zPosition) {
		ChunkPosition chunkPosition = new ChunkPosition(xPosition, zPosition);
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
}
