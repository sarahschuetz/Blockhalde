package com.blockhalde.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.terrain.chunk.ChunkPosition;

/**
 * <p>
 * Encapsulates the position of a subchunk, a modification date, a mesh builder,
 * and a lazily allocated mesh.
 * </p>
 * 
 * <p>
 * This is the format used by the {@link RenderSystem} used to store cached
 * subchunks for later drawing onto the screen. It is also the format used by
 * {@link ChunkMeshDirector} to enqueue finished subchunks for inclusion into the
 * cache of the render system.
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
		this.chunkPos = chunkPos;
		this.subchunkIdx = subchunkIdx;
		this.lastMeshUpdate = lastMeshUpdate;
		this.builder = builder;
	}

	public Mesh getMesh() {
		if (mesh == null) {
			mesh = builder.end();
			builder = null;
		}

		return mesh;
	}
}