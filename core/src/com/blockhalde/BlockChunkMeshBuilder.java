package com.blockhalde;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;

public class BlockChunkMeshBuilder {

	private Chunk chunk;
	private List<Mesh> meshes = new ArrayList<Mesh>();
	private boolean meshDirty = true;
	
	VertexInfo leftBottom = new VertexInfo();
	VertexInfo leftTop = new VertexInfo();
	VertexInfo rightBottom = new VertexInfo();
	VertexInfo rightTop = new VertexInfo();

	public BlockChunkMeshBuilder(Chunk chunk) {
		this.chunk = chunk;
	}

	public void updateMesh() {
		meshes.clear();

		Vector3 center = new Vector3();
		Vector3 bottomLeft = new Vector3();
		Vector3 bottomRight = new Vector3();
		Vector3 topRight = new Vector3();
		Vector3 topLeft = new Vector3();
		Vector3 normal = new Vector3();

		float blockSize = 1.0f;
		float blockSizeHalved = blockSize * 0.5f;

		int chunkWidth = chunk.getWidth();
		int chunkHeight = chunk.getHeight();
		int chunkDepth = chunk.getDepth();

		// X coordinate of the leftmost blocks so that the mesh is symmetrical
		float firstX = (-chunkWidth / 2.0f) * blockSize + blockSizeHalved;
		float firstY = (-chunkHeight / 2.0f) * blockSize + blockSizeHalved;
		float firstZ = (-chunkDepth / 2.0f) * blockSize + blockSizeHalved;

		MeshBuilder builder = new MeshBuilder();
		builder.begin(Usage.Position | Usage.Normal | Usage.TextureCoordinates, GL20.GL_TRIANGLES);

		for (int x = 0; x < chunkWidth; ++x) {
			for (int y = 0; y < chunkHeight; ++y) {

				for (int z = 0; z < chunkDepth; ++z) {

					if (chunk.getBlockAt(x, y, z) != BlockType.AIR.getBlockId()) {
						center.set(chunk.getChunkPosition().getXPosition() + firstX + x * blockSize, firstY + y * blockSize, chunk.getChunkPosition().getZPosition() + firstZ + z * blockSize);

						if ((z + 1) == chunk.getDepth()
								|| chunk.getBlockAt(x, y, z + 1) == BlockType.AIR.getBlockId()) {
							// Front plane
							bottomLeft.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							normal.set(0, 0, 1);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
						}

						if (z == 0 || chunk.getBlockAt(x, y, z - 1) == BlockType.AIR.getBlockId()) {
							// Back plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topRight.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(0, 0, -1);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
						}

						if ((y + 1) == chunk.getHeight()
								|| chunk.getBlockAt(x, y + 1, z) == BlockType.AIR.getBlockId()) {
							// Top plane
							bottomLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(0, 1, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
						}

						if (y == 0 || chunk.getBlockAt(x, y - 1, z) == BlockType.AIR.getBlockId()) {
							// Bottom plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							normal.set(0, -1, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
						}

						if ((x + 1) == chunk.getWidth()
								|| chunk.getBlockAt(x + 1, y, z) == BlockType.AIR.getBlockId()) {
							// Right plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							normal.set(1, 0, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
						}

						if (x == 0 || chunk.getBlockAt(x - 1, y, z) == BlockType.AIR.getBlockId()) {
							// Left plane
							bottomLeft.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(-1, 0, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
						}
					}
				}

				if (builder.getNumVertices() > 30000) {
					Mesh partMesh = builder.end();
					meshes.add(partMesh);

					builder.begin(Usage.Position | Usage.Normal | Usage.TextureCoordinates, GL20.GL_TRIANGLES);
				}
			}
		}

		if (builder.getNumVertices() > 0) {
			Mesh partMesh = builder.end();
			meshes.add(partMesh);
		}
	}

	public void addCubePlane(MeshBuilder builder, Vector3 center, Vector3 bottomLeftOffset, Vector3 bottomRightOffset,
			Vector3 topRightOffset, Vector3 topLeftOffset, Vector3 commonNormal) {

		Vector3 position = new Vector3(center.x + bottomLeftOffset.x, center.y + bottomLeftOffset.y,
				center.z + bottomLeftOffset.z);
		leftBottom.setPos(position).setNor(commonNormal).setUV(new Vector2(0, 0));

		position.set(center.x + bottomRightOffset.x, center.y + bottomRightOffset.y, center.z + bottomRightOffset.z);
		rightBottom.setPos(position).setNor(commonNormal).setUV(new Vector2(1, 0));

		position.set(center.x + topRightOffset.x, center.y + topRightOffset.y, center.z + topRightOffset.z);
		rightTop.setPos(position).setNor(commonNormal).setUV(new Vector2(1, 1));

		position.set(center.x + topLeftOffset.x, center.y + topLeftOffset.y, center.z + topLeftOffset.z);
		leftTop.setPos(position).setNor(commonNormal).setUV(new Vector2(0, 1));

		builder.rect(leftBottom, rightBottom, rightTop, leftTop);
	}

	public List<Mesh> getMeshes() {
		if (meshDirty) {
			long time = 0;
			time = System.currentTimeMillis();
			updateMesh();
			time = System.currentTimeMillis() - time;
			System.out.println(time);
			meshDirty = false;
		}
		return meshes;
	}

}
