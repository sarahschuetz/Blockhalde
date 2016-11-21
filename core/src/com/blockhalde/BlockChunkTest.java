package com.blockhalde;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BlockChunkTest {

	private BlockChunk chunk;
	
	@Before
	public void setUp() throws Exception {
		chunk = new BlockChunk();
	}

	@Test
	public void testGetBlockTypeAt() {
		chunk.setBlockTypeAt(BlockType.SOIL, 12, 200, 14);
		
		assertEquals(chunk.getBlockTypeAt(12, 200, 14), BlockType.SOIL);
		
		chunk.setBlockTypeAt(BlockType.WATER, 1, 9, 0);
		
		assertEquals(chunk.getBlockTypeAt(1, 9, 0), BlockType.WATER);
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
