package com.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;

public class ChunkMeshBuilder {

	private VertexInfo leftBottom = new VertexInfo();
	private VertexInfo leftTop = new VertexInfo();
	private VertexInfo rightBottom = new VertexInfo();
	private VertexInfo rightTop = new VertexInfo();
	
	private TextureAtlas atlas;

	public ChunkMeshBuilder() {
		atlas = new TextureAtlas(Gdx.files.internal("textures/pack.atlas"));
	}
	
	public void updateMesh(Chunk chunk, Mesh mesh, int subchunkIdx) {
		Vector3 center = new Vector3(); 
		Vector3 bottomLeft = new Vector3();
		Vector3 bottomRight = new Vector3();
		Vector3 topRight = new Vector3();
		Vector3 topLeft = new Vector3();
		Vector3 normal = new Vector3();
		Vector2 uvBottomLeft = new Vector2();
		Vector2 uvTopRight = new Vector2();

		float blockSize = 1.0f;
		float blockSizeHalved = blockSize * 0.5f;

		int chunkWidth = chunk.getWidth();
		int chunkDepth = chunk.getDepth();

		// X coordinate of the leftmost blocks so that the mesh is symmetrical
		float firstX = blockSizeHalved + chunk.getChunkPosition().getXPosition();
		float firstY = blockSizeHalved;
		float firstZ = blockSizeHalved + chunk.getChunkPosition().getZPosition();

		MeshBuilder builder = new MeshBuilder();
		builder.begin(Usage.Position | Usage.Normal | Usage.TextureCoordinates, GL20.GL_TRIANGLES);
		
		for (int x = 0; x < chunkWidth; ++x) {
			for (int y = subchunkIdx*16; y < (subchunkIdx+1)*16; ++y) {
				for (int z = 0; z < chunkDepth; ++z) {

					short blockTypeIdx = chunk.getBlockAt(x, y, z);
					
					if (blockTypeIdx != BlockType.AIR.getBlockId()) {
						BlockType blockType = BlockType.fromBlockId(blockTypeIdx);
						
						center.set(chunk.getChunkPosition().getXPosition() + firstX + x * blockSize, firstY + y * blockSize, chunk.getChunkPosition().getZPosition() + firstZ + z * blockSize);

						AtlasRegion region = atlas.findRegion(blockType.getSideTextureName());
						uvBottomLeft.set(region.getU(), region.getV());
						uvTopRight.set(region.getU2(), region.getV2());
						
						if ((z + 1) == chunk.getDepth()
								|| chunk.getBlockAt(x, y, z + 1) == BlockType.AIR.getBlockId()) {
							// Front plane
							bottomLeft.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							normal.set(0, 0, 1);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal, uvBottomLeft, uvTopRight);
						}

						if (z == 0 || chunk.getBlockAt(x, y, z - 1) == BlockType.AIR.getBlockId()) {
							// Back plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topRight.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(0, 0, -1);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal, uvBottomLeft, uvTopRight);
						}
						
						if ((x + 1) == chunk.getWidth()
								|| chunk.getBlockAt(x + 1, y, z) == BlockType.AIR.getBlockId()) {
							// Right plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							normal.set(1, 0, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal, uvBottomLeft, uvTopRight);
						}

						if (x == 0 || chunk.getBlockAt(x - 1, y, z) == BlockType.AIR.getBlockId()) {
							// Left plane
							bottomLeft.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(-1, 0, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal, uvBottomLeft, uvTopRight);
						}

						if ((y + 1) == chunk.getHeight()
								|| chunk.getBlockAt(x, y + 1, z) == BlockType.AIR.getBlockId()) {
							
							region = atlas.findRegion(blockType.getTopTextureName());
							uvBottomLeft.set(region.getU(), region.getV());
							uvTopRight.set(region.getU2(), region.getV2());
							
							// Top plane
							bottomLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(0, 1, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal, uvBottomLeft, uvTopRight);
						}

						if (y == 0 || chunk.getBlockAt(x, y - 1, z) == BlockType.AIR.getBlockId()) {
							
							region = atlas.findRegion(blockType.getBottomTextureName());
							uvBottomLeft.set(region.getU(), region.getV());
							uvTopRight.set(region.getU2(), region.getV2());
							
							// Bottom plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							normal.set(0, -1, 0);
							addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal, uvBottomLeft, uvTopRight);
						}
					}
				}

				if (builder.getNumVertices() > 30000) {
					throw new RuntimeException("Too many vertices");
				}
			}
		}

		builder.end(mesh);
	}

	public void addCubePlane(MeshBuilder builder, Vector3 center, Vector3 bottomLeftOffset, Vector3 bottomRightOffset,
			Vector3 topRightOffset, Vector3 topLeftOffset, Vector3 normal, Vector2 uvBottomLeft, Vector2 uvTopRight) {

		Vector3 position = new Vector3(center.x + bottomLeftOffset.x, center.y + bottomLeftOffset.y,
				center.z + bottomLeftOffset.z);
		leftBottom.setPos(position).setNor(normal).setUV(uvBottomLeft.x, uvBottomLeft.y);

		position.set(center.x + bottomRightOffset.x, center.y + bottomRightOffset.y, center.z + bottomRightOffset.z);
		rightBottom.setPos(position).setNor(normal).setUV(uvTopRight.x, uvBottomLeft.y);

		position.set(center.x + topRightOffset.x, center.y + topRightOffset.y, center.z + topRightOffset.z);
		rightTop.setPos(position).setNor(normal).setUV(uvTopRight.x, uvTopRight.y);

		position.set(center.x + topLeftOffset.x, center.y + topLeftOffset.y, center.z + topLeftOffset.z);
		leftTop.setPos(position).setNor(normal).setUV(uvBottomLeft.x, uvTopRight.y);

		builder.rect(leftBottom, rightBottom, rightTop, leftTop);
	}
}
