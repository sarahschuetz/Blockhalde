package com.blockhalde;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;

public class BlockChunk {

	/** Width, depth and height of a block in the world in meters */
	public static final float BLOCK_SIZE = 1.0f;
	public static final float BLOCK_SIZE_HALF = BLOCK_SIZE / 2;
	
	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_DEPTH = 16;
	public static final int CHUNK_HEIGHT = 256;
	public static final int BLOCK_COUNT = CHUNK_WIDTH * CHUNK_DEPTH * CHUNK_HEIGHT;
	/**
	 * For each block each side has 4 unique vertices
	 */
	public static final int CHUNK_VERTEX_COUNT = BLOCK_COUNT * 4 * 6;
	/**
	 * Each block side has two triangles with three indexes.
	 */
	public static final int CHUNK_INDEX_COUNT = BLOCK_COUNT * 6 * 2 * 3;
	
	
	private byte[] blockTypes = new byte[CHUNK_WIDTH * CHUNK_DEPTH * CHUNK_HEIGHT];
	
	/**
	 * For each block each side has 4 unique vertices
	 */
	private float[] verts = new float[CHUNK_VERTEX_COUNT * (3+3+2)];
	private int vertCount = 0;
	/**
	 * Each block side has two triangles with three indexes.
	 */
	private short[] indices = new short[CHUNK_INDEX_COUNT];
	private int indexCount = 0;
	/**
	 */
	private Mesh mesh = new Mesh(
		false,
		verts.length,
		indices.length,
		new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, "a_position"),
			new VertexAttribute(Usage.Normal, 3, "a_normal"),
			new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords")
		)
	);
	private boolean meshDirty = true;
	
	public byte getBlockTypeAt(int x, int y, int z) {
		int offset = coordsToFlatIndex(x, y, z);
		return blockTypes[offset];
	}
	
	public byte getBlockTypeAt(int offset) {
		return blockTypes[offset];
	}
	
	public void setBlockTypeAt(byte type, int x, int y, int z)
	{
		setBlockTypeAt(type, coordsToFlatIndex(x, y, z));
	}
	
	public void setBlockTypeAt(byte type, int offset)
	{
		if(offset < 0 || offset >= BLOCK_COUNT) {
			throw new IndexOutOfBoundsException("Flat offset is out of bounds: " + offset);
		}
		
		blockTypes[offset] = type;
		meshDirty = true;
	}

	public int coordsToFlatIndex(int x, int y, int z) {
		if(x < 0 || x >= CHUNK_WIDTH || y < 0 || y >= CHUNK_HEIGHT || z < 0 || z >= CHUNK_DEPTH) {
			throw new IndexOutOfBoundsException("Index set: (" + x + "," + y + "," + z + ") is out of bounds");
		}
		
		int offset = x + z * CHUNK_WIDTH + y * (CHUNK_WIDTH + CHUNK_HEIGHT);
		return offset;
	}
	
	public int flatIndexToX(int offset) {
		return offset % (CHUNK_WIDTH + CHUNK_HEIGHT) % CHUNK_WIDTH;
	}
	
	public int flatIndexToY(int offset) {
		return offset / (CHUNK_WIDTH+CHUNK_HEIGHT);
	}
	
	public int flatIndexToZ(int offset) {
		offset -= offset / (CHUNK_WIDTH+CHUNK_HEIGHT) * (CHUNK_WIDTH+CHUNK_HEIGHT);
		return offset / CHUNK_WIDTH;
	}
	
	private void updateMesh() {
		vertCount = 0;
		indexCount = 0;
		
		Vector3 center = new Vector3();
		
		
		// X coordinate of the leftmost blocks so that the mesh is symmetrical
		float firstX = (-CHUNK_WIDTH / 2) * BLOCK_SIZE + BLOCK_SIZE_HALF;
		float firstY = (-CHUNK_HEIGHT / 2) * BLOCK_SIZE + BLOCK_SIZE_HALF;
		float firstZ = (-CHUNK_DEPTH / 2) * BLOCK_SIZE + BLOCK_SIZE_HALF;
		
		for(int x = 0; x < CHUNK_WIDTH; ++x) {
			for(int y = 0; y < CHUNK_HEIGHT; ++y) {
				for(int z = 0; z < CHUNK_DEPTH; ++z) {
					center.set(firstX + x * BLOCK_SIZE,
							   firstY + y * BLOCK_SIZE,
							   firstZ + z * BLOCK_SIZE);
					
					// Front face, front left bottom vertex
					int frontLeftBottom = indexCount;
					// Position
					verts[vertCount++] = center.x - BLOCK_SIZE_HALF;
					verts[vertCount++] = center.y - BLOCK_SIZE_HALF;
					verts[vertCount++] = center.z + BLOCK_SIZE_HALF;
					// Normal
					verts[vertCount++] = 0f;
					verts[vertCount++] = 0f;
					verts[vertCount++] = 1f;
					// UVs
					verts[vertCount++] = 0f;
					verts[vertCount++] = 0f;
					
					// Front face, front right bottom vertex
					int frontRightBottom = indexCount + 1;
					// Position
					verts[vertCount++] = center.x + BLOCK_SIZE_HALF;
					verts[vertCount++] = center.y - BLOCK_SIZE_HALF;
					verts[vertCount++] = center.z + BLOCK_SIZE_HALF;
					// Normal
					verts[vertCount++] = 0f;
					verts[vertCount++] = 0f;
					verts[vertCount++] = 1f;
					// UVs
					verts[vertCount++] = 1f;
					verts[vertCount++] = 0f;
					
					// Front face, top right top vertex
					int frontRightTop = indexCount + 2;
					// Position
					verts[vertCount++] = center.x + BLOCK_SIZE_HALF;
					verts[vertCount++] = center.y + BLOCK_SIZE_HALF;
					verts[vertCount++] = center.z + BLOCK_SIZE_HALF;
					// Normal
					verts[vertCount++] = 0f;
					verts[vertCount++] = 0f;
					verts[vertCount++] = 1f;
					// UVs
					verts[vertCount++] = 1f;
					verts[vertCount++] = 1f;
					
					// Front face, top left top vertex
					int frontLeftTop = indexCount + 3;
					// Position
					verts[vertCount++] = center.x - BLOCK_SIZE_HALF;
					verts[vertCount++] = center.y + BLOCK_SIZE_HALF;
					verts[vertCount++] = center.z + BLOCK_SIZE_HALF;
					// Normal
					verts[vertCount++] = 0f;
					verts[vertCount++] = 0f;
					verts[vertCount++] = 1f;
					// UVs
					verts[vertCount++] = 0f;
					verts[vertCount++] = 1f;
					
					indices[indexCount++] = (short) frontLeftBottom;
					indices[indexCount++] = (short) frontRightBottom;
					indices[indexCount++] = (short) frontRightTop;
					
					indices[indexCount++] = (short) frontLeftBottom;
					indices[indexCount++] = (short) frontRightTop;
					indices[indexCount++] = (short) frontLeftTop;
				}
			}
		}
		
		mesh.setVertices(verts, 0, vertCount);
		mesh.setIndices(indices, 0, indexCount);
	}
	
	public Mesh getMesh() {
		if(meshDirty) {
			updateMesh();
		}
		
		return mesh;
	}

}
