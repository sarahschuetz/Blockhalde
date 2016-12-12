package com.terrain.chunk;

import com.terrain.block.BlockType;
import com.util.FlagUtils;

/**
 * <p>
 * Provides default implementations for all methods in the {@link Chunk}
 * interface.
 * </p>
 * 
 * <p>
 * The default behavior of the following methods is to throw an exception when
 * called:
 * <p>
 * 
 * <ul>
 * <li><code>getBlockAt(int relativeX, int relativeY, int relativeZ)</code>
 * <li>
 * <code>setBlockAt(int relativeX, int relativeY, int relativeZ, BlockType type)</code>
 * <li><code>getChunkPosition()</code>
 * <li><code>getLastModifiedTime()</code>
 * </ul>
 * 
 * <p>
 * Concrete subclasses of {@link AbstractChunk} can either override the methods
 * or rely on users of the subclass to never call the non-overriden methods.
 * </p>
 */
public abstract class AbstractChunk implements Chunk {

	@Override
	public short getBlockAt(int relativeX, int relativeY, int relativeZ) {
		throw new UnsupportedOperationException("getBlockAt(int,int,int) is unimplemented");
	}

	@Override
	public void setBlockAt(int relativeX, int relativeY, int relativeZ, BlockType type) {
		throw new UnsupportedOperationException("setBlockAt(int,int,int,BlockType) is unimplemented");
	}

	@Override
	public ChunkPosition getChunkPosition() {
		throw new UnsupportedOperationException("getChunkPosition() is unimplemented");
	}

	@Override
	public long getLastModifiedTime() {
		throw new UnsupportedOperationException("getLastModifiedTime() is unimplemented");
	}
	
	@Override
	public int getWidth() {
		return X_MAX;
	}

	@Override
	public int getHeight() {
		return Y_MAX;
	}

	@Override
	public int getDepth() {
		return Z_MAX;
	}

	@Override
	public byte getBlockTypeAt(int relativeX, int relativeY, int relativeZ) {
		final short block = getBlockAt(relativeX, relativeY, relativeZ);
		return FlagUtils.getByteOf(block, 0);
	}

	@Override
	public ChunkPosition getRelativeChunkPosition() {
		ChunkPosition chunkPosition = getChunkPosition();
		return new ChunkPosition(chunkPosition.getXPosition() / Chunk.X_MAX,
				chunkPosition.getZPosition() / Chunk.Z_MAX);
	}

}
