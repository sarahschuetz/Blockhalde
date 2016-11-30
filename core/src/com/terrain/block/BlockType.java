package com.terrain.block;

/**
 * Defines all block types which are specified in a byte
 * data range.
 */
public enum BlockType {
	AIR((byte) 0),
	WATER((byte) 1),
	DIRT((byte) 2),
	STONE((byte) 3);
	
	
	private byte blockId;
	
	BlockType(byte id) {
		blockId = id;
	}

	public byte getBlockId() {
		return blockId;
	}
}