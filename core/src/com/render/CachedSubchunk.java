package com.render;

import com.badlogic.gdx.graphics.Mesh;
import com.terrain.chunk.ChunkPosition;

public class CachedSubchunk {
	public ChunkPosition chunkPos;
	public int subchunkIdx = -1;
	public long lastMeshUpdate = Long.MIN_VALUE;
	public Mesh mesh;
	
	public boolean isUnused() {
		return subchunkIdx == -1;
	}
}