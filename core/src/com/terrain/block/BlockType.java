package com.terrain.block;

/**
 * Defines all block types which are specified in a byte
 * data range.
 */
public enum BlockType {
	AIR(-1.0f, null, null, null),
	WATER(-1.0f, null, null, null),
	DIRT(0.1f, "dirt", "dirt", "dirt"),
	STONE(0.5f, "stone", "stone", "stone"),
	GRASS(0.1f, "grass_top", "grass_side", "dirt");
	
	public static BlockType fromBlockId(int id) {
		return BlockType.values()[id];
	}
	
	private String topTextureName;
	private String bottomTextureName;
	private String sideTextureName;
	/**
	 * Contains a value proportional to how easy a block type is to destroy.
	 * 
	 * 0.0f means it is destroyed instantly, 1.0f is the maximum sturdiness.
	 * -1.0f is a special value that indicates that the block is indestructible.
	 */
	private float sturdiness;
	
	BlockType(float sturdiness, String topTex, String sideTex, String bottomTex) {
		this.sturdiness = sturdiness;
		this.topTextureName = topTex;
		this.bottomTextureName = bottomTex;
		this.sideTextureName = sideTex;
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
		return (byte) ordinal();
	}
	
	public float getSturdiness() {
		return sturdiness;
	}
	
	public boolean isDestructible() {
		return sturdiness != -1.0f;
	}
}