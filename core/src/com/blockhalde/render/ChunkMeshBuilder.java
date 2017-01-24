package com.blockhalde.render;

import java.util.concurrent.Callable;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.terrain.block.BlockType;
import com.terrain.chunk.Chunk;

/**
 * <p>
 * A {@link ChunkMeshBuilder} is a poolable object that can be used to create a new
 * libgdx {@link MeshBuilder} and populate it with the data of a selected subchunk using
 * the data of the chunk containing the subchunk, as well as the data of the adjacent
 * chunks. The chunk data is encapsulated in the {@link ChunkMeshRequest} that is passed
 * to the <code>init(request)</code> method.
 * </p>
 * 
 * <p>
 * Since {@link ChunkMeshBuilder} implements the {@link Callable} interface, it can easily
 * be integrated with concurrency frameworks, such as <em>ForkJoin</em>.
 * </p>
 */
public class ChunkMeshBuilder implements Poolable, Callable<MeshBuilder> {

	private static final byte AIR_ID = BlockType.AIR.getBlockId();

	public static final VertexAttributes BLOCK_MESH_ATTRS = new VertexAttributes(
			new VertexAttribute(Usage.Position, 3, "a_position"), new VertexAttribute(Usage.Normal, 3, "a_normal"),
			new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"),
			new VertexAttribute(Usage.ColorPacked, 4, "a_color"));

	private VertexInfo leftBottom = new VertexInfo();
	private VertexInfo leftTop = new VertexInfo();
	private VertexInfo rightBottom = new VertexInfo();
	private VertexInfo rightTop = new VertexInfo();

	private Vector3 center = new Vector3();
	private Vector3 bottomLeft = new Vector3();
	private Vector3 bottomRight = new Vector3();
	private Vector3 topRight = new Vector3();
	private Vector3 topLeft = new Vector3();
	private Vector3 normal = new Vector3();
	private Vector2 uvBottomLeft = new Vector2();
	private Vector2 uvTopRight = new Vector2();

	private TextureAtlas atlas;
	private ChunkMeshRequest req;

	public ChunkMeshBuilder(TextureAtlas atlas) {
		this.atlas = atlas;
	}

	byte blockTypeAt(int relativeX, int relativeY, int relativeZ) {
		if (relativeX < 0) {
			return req.negXChunk.getBlockTypeAt(Chunk.X_MAX + relativeX, relativeY, relativeZ);
		} else if (relativeX >= Chunk.X_MAX) {
			return req.posXChunk.getBlockTypeAt(relativeX - Chunk.X_MAX, relativeY, relativeZ);
		} else if (relativeZ < 0) {
			return req.negZChunk.getBlockTypeAt(relativeX, relativeY, Chunk.Z_MAX + relativeZ);
		} else if (relativeZ >= Chunk.Z_MAX) {
			return req.posZChunk.getBlockTypeAt(relativeX, relativeY, relativeZ - Chunk.Z_MAX);
		} else {
			return req.centerChunk.getBlockTypeAt(relativeX, relativeY, relativeZ);
		}
	}

	/**
	 * <p>
	 * Used after retrieving a chunkmeshbuilder from the pool to configure it
	 * for the generation of a specific subchunk. The initialization parameters
	 * are encapsulated in {@link ChunkMeshRequest}.
	 * </p>
	 * 
	 * <p>
	 * After initialization
	 * </p>
	 * 
	 * @param req
	 */
	public void init(ChunkMeshRequest req) {
		this.req = req;
	}

	@Override
	public MeshBuilder call() throws Exception {
		float blockSizeHalved = 0.5f;

		final MeshBuilder builder = new MeshBuilder();
		// get attributes from mesh - otherwise only leads to compatibilty
		// problems when new attributes are introduced
		builder.begin(BLOCK_MESH_ATTRS, GL20.GL_TRIANGLES);

		for (int x = 0; x < Chunk.X_MAX; ++x) {
			for (int y = req.subchunkIdx * 16; y < (req.subchunkIdx + 1) * 16; ++y) {
				for (int z = 0; z < Chunk.Z_MAX; ++z) {

					byte blockId = blockTypeAt(x, y, z);

					if (blockId != BlockType.AIR.getBlockId()) {
						BlockType blockType = BlockType.fromBlockId(blockId);

						center.set(x + blockSizeHalved + req.getPosition().getXPosition(), y + blockSizeHalved,
								z + req.getPosition().getZPosition() + blockSizeHalved);

						AtlasRegion region = atlas.findRegion(blockType.getSideTextureName());

						// Counterintuively, (u,v) seems to be the bottom
						// right and (u2,u2)
						// the top left in an AtlasRegion
						// That sure sounds like something that should be
						// documented in libgdx
						uvBottomLeft.set(region.getU(), region.getV2());
						uvTopRight.set(region.getU2(), region.getV());

						// ao values for the block
						int bottomLeftFront = getVertexAO(blockTypeAt(x - 1, y, z + 1), blockTypeAt(x, y - 1, z + 1),
								blockTypeAt(x - 1, y - 1, z + 1));
						int bottomRightFront = getVertexAO(blockTypeAt(x + 1, y, z + 1), blockTypeAt(x, y - 1, z + 1),
								blockTypeAt(x + 1, y - 1, z + 1));
						int topLeftFront = getVertexAO(blockTypeAt(x - 1, y, z + 1), blockTypeAt(x, y + 1, z + 1),
								blockTypeAt(x - 1, y + 1, z + 1));
						int topRightFront = getVertexAO(blockTypeAt(x + 1, y, z + 1), blockTypeAt(x, y + 1, z + 1),
								blockTypeAt(x + 1, y + 1, z + 1));
						int bottomLeftBack = getVertexAO(blockTypeAt(x - 1, y, z - 1), blockTypeAt(x, y - 1, z - 1),
								blockTypeAt(x - 1, y - 1, z - 1));
						int bottomRightBack = getVertexAO(blockTypeAt(x + 1, y, z - 1), blockTypeAt(x, y - 1, z - 1),
								blockTypeAt(x + 1, y - 1, z - 1));
						int topLeftBack = getVertexAO(blockTypeAt(x - 1, y, z - 1), blockTypeAt(x, y + 1, z - 1),
								blockTypeAt(x - 1, y + 1, z - 1));
						int topRightBack = getVertexAO(blockTypeAt(x + 1, y, z - 1), blockTypeAt(x, y + 1, z - 1),
								blockTypeAt(x + 1, y + 1, z - 1));

						if (blockTypeAt(x, y, z + 1) == AIR_ID) {
							// Front plane
							bottomLeft.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							normal.set(0, 0, 1);
							addCubePlane(builder, center, bottomLeft, bottomLeftFront, bottomRight, bottomRightFront,
									topRight, topRightFront, topLeft, topLeftFront, normal, uvBottomLeft, uvTopRight);
						}

						if (blockTypeAt(x, y, z - 1) == AIR_ID) {
							// Back plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topRight.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(0, 0, -1);
							addCubePlane(builder, center, bottomLeft, bottomRightBack, bottomRight, bottomLeftBack,
									topRight, topLeftBack, topLeft, topRightBack, normal, uvBottomLeft, uvTopRight);
						}

						if (blockTypeAt(x + 1, y, z) == AIR_ID) {
							// Right plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							normal.set(1, 0, 0);
							addCubePlane(builder, center, bottomLeft, bottomRightFront, bottomRight, bottomRightBack,
									topRight, topRightBack, topLeft, topRightFront, normal, uvBottomLeft, uvTopRight);
						}

						if (blockTypeAt(x - 1, y, z) == AIR_ID) {
							// Left plane
							bottomLeft.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(-1, 0, 0);
							addCubePlane(builder, center, bottomLeft, bottomLeftBack, bottomRight, bottomLeftFront,
									topRight, topLeftFront, topLeft, topLeftBack, normal, uvBottomLeft, uvTopRight);
						}

						if (blockTypeAt(x, y + 1, z) == AIR_ID) {

							region = atlas.findRegion(blockType.getTopTextureName());
							uvBottomLeft.set(region.getU(), region.getV());
							uvTopRight.set(region.getU2(), region.getV2());

							// Top plane
							bottomLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
							bottomRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
							topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
							normal.set(0, 1, 0);
							addCubePlane(builder, center, bottomLeft, topLeftFront, bottomRight, topRightFront,
									topRight, topRightBack, topLeft, topLeftBack, normal, uvBottomLeft, uvTopRight);
						}

						if (blockTypeAt(x, y - 1, z) == AIR_ID) {

							region = atlas.findRegion(blockType.getBottomTextureName());
							uvBottomLeft.set(region.getU(), region.getV());
							uvTopRight.set(region.getU2(), region.getV2());

							// Bottom plane
							bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
							topRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							topLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
							normal.set(0, -1, 0);
							addCubePlane(builder, center, bottomLeft, bottomLeftFront, bottomRight, bottomRightFront,
									topRight, bottomRightBack, topLeft, bottomLeftBack, normal, uvBottomLeft,
									uvTopRight);
						}
					}
				}

				if (builder.getNumVertices() > 30000) {
					throw new RuntimeException("Too many vertices");
				}
			}
		}

		if (builder.getNumVertices() == 0) {
			return null;
		} else {
			return builder;
		}
	}

	private void addCubePlane(MeshBuilder builder, Vector3 center, Vector3 bottomLeftOffset, int bottomLeftAO,
			Vector3 bottomRightOffset, int bottomRightAO, Vector3 topRightOffset, int topRightAO, Vector3 topLeftOffset,
			int topLeftAO, Vector3 normal, Vector2 uvBottomLeft, Vector2 uvTopRight) {

		Vector3 position = new Vector3(center.x + bottomLeftOffset.x, center.y + bottomLeftOffset.y,
				center.z + bottomLeftOffset.z);
		leftBottom.setPos(position).setNor(normal).setCol(bottomLeftAO, 0, 0, 0).setUV(uvBottomLeft.x, uvBottomLeft.y);

		position.set(center.x + bottomRightOffset.x, center.y + bottomRightOffset.y, center.z + bottomRightOffset.z);
		rightBottom.setPos(position).setNor(normal).setCol(bottomRightAO, 0, 0, 0).setUV(uvTopRight.x, uvBottomLeft.y);

		position.set(center.x + topRightOffset.x, center.y + topRightOffset.y, center.z + topRightOffset.z);
		rightTop.setPos(position).setNor(normal).setCol(topRightAO, 0, 0, 0).setUV(uvTopRight.x, uvTopRight.y);

		position.set(center.x + topLeftOffset.x, center.y + topLeftOffset.y, center.z + topLeftOffset.z);
		leftTop.setPos(position).setNor(normal).setCol(topLeftAO, 0, 0, 0).setUV(uvBottomLeft.x, uvTopRight.y);

		builder.rect(leftBottom, rightBottom, rightTop, leftTop);
	}

	private int getVertexAO(short neighborSide1, short neighborSide2, short neighborCorner) {
		if (neighborSide1 != BlockType.AIR.getBlockId() && neighborSide2 != BlockType.AIR.getBlockId())
			return 0;
		int ao = 0;
		if (neighborSide1 != BlockType.AIR.getBlockId())
			ao++;
		if (neighborSide2 != BlockType.AIR.getBlockId())
			ao++;
		if (neighborCorner != BlockType.AIR.getBlockId())
			ao++;
		return 3 - ao;
	}

	@Override
	public void reset() {
	}
}
