package com.terrain.chunk;

import com.terrain.block.BlockType;

public class UniformChunk extends AbstractChunk {

	private ChunkPosition position;
	private short uniformBlock;
	private long creationTime = System.nanoTime();

	public UniformChunk(ChunkPosition position, byte uniformBlockId) {
		this.position = new ChunkPosition(position.getXPosition(), position.getZPosition());
		this.uniformBlock = makeBlock(uniformBlockId);
	}
	
	public UniformChunk(ChunkPosition position, BlockType uniformBlockType) {
		this(position, uniformBlockType.getBlockId());
	}
	
	private short makeBlock(BlockType type) {
		return makeBlock(type.getBlockId());
	}

	private short makeBlock(byte uniformBlockId) {
		return uniformBlockId;
	}

	public void setUniformBlockType(BlockType type) {
		uniformBlock = makeBlock(type);
	}
	
	public void setUniformBlockId(byte blockId) {
		uniformBlock = makeBlock(blockId);
	}

	@Override
	public short getBlockAt(int relativeX, int relativeY, int relativeZ) {
		return uniformBlock;
	}

	@Override
	public void setBlockAt(int relativeX, int relativeY, int relativeZ, BlockType type) {
		throw new UnsupportedOperationException(
				"Individiual blocks cannot be modified on a UniformChunk, use setUniformBlockType(type) instead");
	}

	@Override
	public ChunkPosition getChunkPosition() {
		return position;
	}
	
	public void setChunkPosition(ChunkPosition position) {
		setChunkPosition(position.getXPosition(), position.getZPosition());
	}
	
	public void setChunkPosition(int xPosition, int zPosition) {
		this.position.setXPosition(xPosition);
		this.position.setZPosition(zPosition);
	}

	@Override
	public long getLastModifiedTime() {
		return creationTime;
	}

}
