package com.blockhalde.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.terrain.chunk.ChunkPosition;

public class CachedSubchunk {
	public ChunkPosition chunkPos;
	public int subchunkIdx = -1;
	public long lastMeshUpdate = Long.MIN_VALUE;
	public MeshBuilder builder;
	private Mesh mesh;
	
	public CachedSubchunk(MeshBuilder builder, ChunkPosition chunkPos, int subchunkIdx, long lastMeshUpdate) {
		super();
		this.chunkPos = chunkPos;
		this.subchunkIdx = subchunkIdx;
		this.lastMeshUpdate = lastMeshUpdate;
		this.builder = builder;
	}

	public Mesh getMesh() {
		if(mesh == null) {
			mesh = builder.end();
			builder = null;
		}
		
		return mesh;
	}


	public boolean isUnused() {
		return subchunkIdx == -1;
	}
}