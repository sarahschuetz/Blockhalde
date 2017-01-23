package com.blockhalde.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.terrain.chunk.ChunkPosition;

/**
 * <p>
 * Used to pass a request from the {@link ChunkMeshBuilder} to the {@link ChunkMeshDirector}
 * to generate or re-generate the mesh used to render a specific subchunk of the world.
 * </p>
 * 
 * <p>
 * The {@link ChunkMeshDirector}, in turn, will populate the included mesh builder with the
 * data from the selected subchunk. Then, it will send the cached subchunk back to the
 * {@link ChunkMeshBuilder}
 * </p>
 * 
 * @author phil
 */
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