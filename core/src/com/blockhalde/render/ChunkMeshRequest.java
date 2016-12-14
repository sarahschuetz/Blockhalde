package com.blockhalde.render;

import com.terrain.chunk.Chunk;
import com.terrain.chunk.ChunkPosition;

public class ChunkMeshRequest {
	/**
	 * Holds the chunk that contains the subchunk that should be generated with this request.
	 */
	public Chunk centerChunk;
	public Chunk posZChunk;
	public Chunk negZChunk;
	public Chunk posXChunk;
	public Chunk negXChunk;
	
	public int subchunkIdx;
	
	public ChunkMeshRequest(Chunk centerChunk, Chunk posZChunk, Chunk negZChunk, Chunk posXChunk, Chunk negXChunk,
			int subchunkIdx) {
		super();
		this.centerChunk = centerChunk;
		this.posZChunk = posZChunk;
		this.negZChunk = negZChunk;
		this.posXChunk = posXChunk;
		this.negXChunk = negXChunk;
		this.subchunkIdx = subchunkIdx;
	}
	
	public ChunkPosition getPosition() {
		return centerChunk.getChunkPosition();
	}

	@Override
	public int hashCode() {
		ChunkPosition position = getPosition();
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + subchunkIdx;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		ChunkMeshRequest other = (ChunkMeshRequest) obj;
		ChunkPosition position = getPosition();
		ChunkPosition otherPosition = other.getPosition();
		
		if (position == null) {
			if (otherPosition != null)
				return false;
		} else if (!position.equals(otherPosition))
			return false;
		if (subchunkIdx != other.subchunkIdx)
			return false;
		return true;
	}

}
