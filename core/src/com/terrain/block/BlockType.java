package com.terrain.block;

/**
 * Defines all block types which are specified in a byte
 * data range.
 */
public enum BlockType {
	AIR((byte) 0, -1.0f, null, null, null),
	WATER((byte) 1, -1.0f, null, null, null),
	DIRT((byte) 2, 0.1f, "dirt", "dirt", "dirt"),
	STONE((byte) 3, 0.5f, "stone", "stone", "stone"),
	GRASS((byte) 4, 0.1f, "grass_top", "grass_side", "dirt"),
	TNT((byte) 5, 0.05f, "tnt_top", "tnt_side", "tnt_bottom"),
	SNOW((byte) 6, 0.1f, "snow", "grass_side_snowed", "dirt");
	
	public static BlockType fromBlockId(short id) {
		return BlockType.values()[id];
	}
	
	private byte blockId;
	
	private String topTextureName;
	private String sideTextureName;
	private String bottomTextureName;
	/**
	 * Contains a value proportional to how easy a block type is to destroy.
	 * 
	 * 0.0f means it is destroyed instantly, 1.0f is the maximum sturdiness.
	 * -1.0f is a special value that indicates that the block is indestructible.
	 */
	private float sturdiness;
	
	BlockType(byte blockId, float sturdiness, String topTex, String sideTex, String bottomTex) {
		
		this.sturdiness = sturdiness;
		this.topTextureName = topTex;
		this.bottomTextureName = bottomTex;
		this.sideTextureName = sideTex;
		this.blockId = blockId;
	}
	
	public String getTopTextureName() {
		return topTextureName;
	}
	
	public String getBottomTextureName() {
		return bottomTextureName;
	}
	
	public String getSideTextureName() {
		return sideTextureName;
	}

	public byte getBlockId() {
		return blockId;
	}
	
	public float getSturdiness() {
		return sturdiness;
	}
	
	public boolean isDestructible() {
		return sturdiness != -1.0f;
	}
}