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
	
	public static final int CHUNK_WIDTH = 8;
	public static final int CHUNK_DEPTH = 8;
	public static final int CHUNK_HEIGHT = 8;
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
	
	private int nextIdx;
	/**
	 */
	private Mesh mesh = new Mesh(
		false,
		CHUNK_VERTEX_COUNT,
		CHUNK_INDEX_COUNT,
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
	
	public boolean isOpaqueAt(int x, int y, int z) {
		byte type = getBlockTypeAt(x, y, z);
		return type != BlockType.AIR && type != BlockType.WATER;
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
		Vector3 bottomLeft = new Vector3();
		Vector3 bottomRight = new Vector3();
		Vector3 topRight = new Vector3();
		Vector3 topLeft = new Vector3();
		Vector3 normal = new Vector3();
		
		// X coordinate of the leftmost blocks so that the mesh is symmetrical
		float firstX = (-CHUNK_WIDTH / 2) * BLOCK_SIZE + BLOCK_SIZE_HALF;
		float firstY = (-CHUNK_HEIGHT / 2) * BLOCK_SIZE + BLOCK_SIZE_HALF;
		float firstZ = (-CHUNK_DEPTH / 2) * BLOCK_SIZE + BLOCK_SIZE_HALF;
		
		nextIdx = 0;

		for(int x = 0; x < CHUNK_WIDTH; ++x) {
			for(int y = 0; y < CHUNK_HEIGHT; ++y) {
				for(int z = 0; z < CHUNK_DEPTH; ++z) {
				
					center.set(firstX + x * BLOCK_SIZE,
							   firstY + y * BLOCK_SIZE,
							   firstZ + z * BLOCK_SIZE);
					
					// Front plane
					bottomLeft.set(-BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					bottomRight.set(BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					topRight.set(BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					topLeft.set(-BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					normal.set(0, 0, 1);
					addCubePlane(center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					
					// Back plane
					bottomLeft.set(BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					bottomRight.set(-BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					topRight.set(-BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					topLeft.set(BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					normal.set(0, 0, -1);
					addCubePlane(center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Top plane
					bottomLeft.set(-BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					bottomRight.set(BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					topRight.set(BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					topLeft.set(-BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					normal.set(0, 1, 0);
					addCubePlane(center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Bottom plane
					bottomLeft.set(BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					bottomRight.set(-BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					topRight.set(-BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					topLeft.set(BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					normal.set(0, -1, 0);
					addCubePlane(center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Left plane
					bottomLeft.set(-BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					bottomRight.set(-BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					topRight.set(-BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					topLeft.set(-BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					normal.set(-1, 0, 0);
					addCubePlane(center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Right plane
					bottomLeft.set(BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					bottomRight.set(BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					topRight.set(BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, -BLOCK_SIZE_HALF);
					topLeft.set(BLOCK_SIZE_HALF, BLOCK_SIZE_HALF, BLOCK_SIZE_HALF);
					normal.set(1, 0, 0);
					addCubePlane(center, bottomLeft, bottomRight, topRight, topLeft, normal);
				}
			}
		}
		
		mesh.setVertices(verts, 0, vertCount);
		mesh.setIndices(indices, 0, indexCount);
		
		meshDirty = false;
	}

	public void addCubePlane(Vector3 center,
			                 Vector3 bottomLeftOffset,
			                 Vector3 bottomRightOffset,
			                 Vector3 topRightOffset,
			                 Vector3 topLeftOffset,
			                 Vector3 commonNormal) {
		// Front face, front left bottom vertex
		int frontLeftBottom = nextIdx++;
		// Position
		verts[vertCount++] = center.x + bottomLeftOffset.x;
		verts[vertCount++] = center.y + bottomLeftOffset.y;
		verts[vertCount++] = center.z + bottomLeftOffset.z;
		// Normal
		verts[vertCount++] = commonNormal.x;
		verts[vertCount++] = commonNormal.y;
		verts[vertCount++] = commonNormal.z;
		// UVs
		verts[vertCount++] = 0f;
		verts[vertCount++] = 0f;
		
		// Front face, front right bottom vertex
		int frontRightBottom = nextIdx++;
		// Position
		verts[vertCount++] = center.x + bottomRightOffset.x;
		verts[vertCount++] = center.y + bottomRightOffset.y;
		verts[vertCount++] = center.z + bottomRightOffset.z;
		// Normal
		verts[vertCount++] = commonNormal.x;
		verts[vertCount++] = commonNormal.y;
		verts[vertCount++] = commonNormal.z;
		// UVs
		verts[vertCount++] = 1f;
		verts[vertCount++] = 0f;
		
		// Front face, top right top vertex
		int frontRightTop = nextIdx++;
		// Position
		verts[vertCount++] = center.x + topRightOffset.x;
		verts[vertCount++] = center.y + topRightOffset.y;
		verts[vertCount++] = center.z + topRightOffset.z;
		// Normal
		verts[vertCount++] = commonNormal.x;
		verts[vertCount++] = commonNormal.y;
		verts[vertCount++] = commonNormal.z;
		// UVs
		verts[vertCount++] = 1f;
		verts[vertCount++] = 1f;
		
		// Front face, top left top vertex
		int frontLeftTop = nextIdx++;
		// Position
		verts[vertCount++] = center.x + topLeftOffset.x;
		verts[vertCount++] = center.y + topLeftOffset.y;
		verts[vertCount++] = center.z + topLeftOffset.z;
		// Normal
		verts[vertCount++] = commonNormal.x;
		verts[vertCount++] = commonNormal.y;
		verts[vertCount++] = commonNormal.z;
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
	
	public Mesh getMesh() {
		if(meshDirty) {
			updateMesh();
		}
		
		return mesh;
	}

}
