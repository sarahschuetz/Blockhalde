package com.render;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.render.BlockChunk;
import com.terrain.block.BlockType;
import com.terrain.chunk.ChunkPosition;

public class BlockChunkTest {

	private BlockChunk chunk;
	
	@Before
	public void setUp() throws Exception {
		chunk = new BlockChunk(new ChunkPosition(0, 0), null);
	}
	
	@Test
	public void testGetLastBlock() {
		chunk.setBlockAt(15, 255, 15, BlockType.DIRT);
		assertEquals(BlockType.DIRT.getBlockId(), chunk.getBlockAt(15, 255, 15));
	}

	@Test
	public void testGetBlockTypeAt() {
		chunk.setBlockAt(12, 200, 14, BlockType.DIRT);
		
		assertEquals(BlockType.DIRT.getBlockId(), chunk.getBlockAt(12, 200, 14));
		
		chunk.setBlockAt(1, 9, 0, BlockType.WATER);
		
		assertEquals(BlockType.WATER.getBlockId(), chunk.getBlockAt(1, 9, 0));
	}

}
