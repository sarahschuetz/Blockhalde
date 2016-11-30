package com.blockhalde;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.terrain.block.BlockType;

public class BlockChunkTest {

	private BlockChunk chunk;
	
	@Before
	public void setUp() throws Exception {
		chunk = new BlockChunk();
	}
	
	@Test
	public void testGetLastBlock() {
		chunk.setBlockAt(BlockType.DIRT , 15, 255, 15);
		assertEquals(BlockType.DIRT.getBlockId(), chunk.getBlockAt(15, 255, 15));
	}

	@Test
	public void testGetBlockTypeAt() {
		chunk.setBlockAt(BlockType.DIRT , 12, 200, 14);
		
		assertEquals(BlockType.DIRT.getBlockId(), chunk.getBlockAt(12, 200, 14));
		
		chunk.setBlockAt(BlockType.WATER, 1, 9, 0);
		
		assertEquals(BlockType.WATER.getBlockId(), chunk.getBlockAt(1, 9, 0));
	}

	@Test
	public void testOffsetToX() {
		int flat = chunk.coordsToFlatIndex(15, 230, 11);
		assertEquals(15, chunk.flatIndexToX(flat));
	}

	@Test
	public void testOffsetToY() {
		int flat = chunk.coordsToFlatIndex(15, 230, 11);
		assertEquals(230, chunk.flatIndexToY(flat));
	}

	@Test
	public void testOffsetToZ() {
		int flat = chunk.coordsToFlatIndex(15, 230, 11);
		assertEquals(11, chunk.flatIndexToZ(flat));
	}

}
