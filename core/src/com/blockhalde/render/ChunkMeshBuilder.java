package com.blockhalde.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.terrain.chunk.ChunkPosition;
import com.terrain.world.WorldInterface;

public class ChunkMeshBuilder {

    private VertexInfo leftBottom = new VertexInfo();
    private VertexInfo leftTop = new VertexInfo();
    private VertexInfo rightBottom = new VertexInfo();
    private VertexInfo rightTop = new VertexInfo();

    private TextureAtlas atlas;
	private WorldInterface world;

    public ChunkMeshBuilder(WorldInterface world) {
    	this.world = world;
        atlas = new TextureAtlas(Gdx.files.internal("textures/blocks.atlas"));
    }

    public void updateMesh(Mesh mesh, ChunkPosition pos, int subchunkIdx) {
        Vector3 center = new Vector3();
        Vector3 bottomLeft = new Vector3();
        Vector3 bottomRight = new Vector3();
        Vector3 topRight = new Vector3();
        Vector3 topLeft = new Vector3();
        Vector3 normal = new Vector3();
        Vector2 uvBottomLeft = new Vector2();
        Vector2 uvTopRight = new Vector2();

        float blockSizeHalved = 0.5f;

        int xPos = pos.getXPosition();
        int zPos = pos.getZPosition();

        Chunk chunk = world.getChunk(xPos, zPos);
        
        int chunkWidth = chunk.getWidth();
        int chunkDepth = chunk.getDepth();

        // X coordinate of the leftmost blocks so that the mesh is symmetrical
        float firstX = blockSizeHalved + xPos * chunkWidth;
        float firstY = blockSizeHalved;
        float firstZ = blockSizeHalved + zPos * chunkDepth;

//        System.out.println(pos.getXPosition() + "/" + pos.getZPosition());
        
        MeshBuilder builder = new MeshBuilder();
        //get attributes from mesh - otherwise only leads to compatibilty problems when new attributes are introduced
        builder.begin(mesh.getVertexAttributes(), GL20.GL_TRIANGLES);

        for (int x = xPos; x < xPos + chunkWidth; ++x) {
            for (int y = subchunkIdx * 16; y < (subchunkIdx + 1) * 16; ++y) {
                for (int z = zPos; z < zPos + chunkDepth; ++z) {

                    short blockTypeIdx = world.getBlockType(x, y, z);

                    if (blockTypeIdx != BlockType.AIR.getBlockId()) {
                        BlockType blockType = BlockType.fromBlockId(blockTypeIdx);

                        center.set(x + blockSizeHalved,
                        		   y + blockSizeHalved,
                        		   z + blockSizeHalved);

                        AtlasRegion region = atlas.findRegion(blockType.getSideTextureName());

                        // Counterintuively, (u,v) seems to be the bottom right and (u2,u2)
                        // the top left in an AtlasRegion
                        // That sure sounds like something that should be documented in libgdx
                        uvBottomLeft.set(region.getU(), region.getV2());
                        uvTopRight.set(region.getU2(), region.getV());

                        // ao values for the block
                        int bottomLeftFront = getVertexAO(chunk.getBlockAt(x-1, y, z+1), chunk.getBlockAt(x, y-1, z+1),chunk.getBlockAt(x-1, y-1, z+1));
                        int bottomRightFront = getVertexAO(chunk.getBlockAt(x+1, y, z+1), chunk.getBlockAt(x, y-1, z+1),chunk.getBlockAt(x+1, y-1, z+1));
                        int topLeftFront = getVertexAO(chunk.getBlockAt(x-1, y, z+1), chunk.getBlockAt(x, y+1, z+1),chunk.getBlockAt(x-1, y+1, z+1));
                        int topRightFront = getVertexAO(chunk.getBlockAt(x+1, y, z+1), chunk.getBlockAt(x, y+1, z+1),chunk.getBlockAt(x+1, y+1, z+1));
                        int bottomLeftBack = getVertexAO(chunk.getBlockAt(x-1, y, z-1), chunk.getBlockAt(x, y-1, z-1),chunk.getBlockAt(x-1, y-1, z-1));
                        int bottomRightBack = getVertexAO(chunk.getBlockAt(x+1, y, z-1), chunk.getBlockAt(x, y-1, z-1),chunk.getBlockAt(x+1, y-1, z-1));
                        int topLeftBack = getVertexAO(chunk.getBlockAt(x-1, y, z-1), chunk.getBlockAt(x, y+1, z-1),chunk.getBlockAt(x-1, y+1, z-1));
                        int topRightBack = getVertexAO(chunk.getBlockAt(x+1, y, z-1), chunk.getBlockAt(x, y+1, z-1),chunk.getBlockAt(x+1, y+1, z-1));

                        if (world.getBlockType(x, y, z + 1) == BlockType.AIR.getBlockId()) {
                            // Front plane
                            bottomLeft.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
                            bottomRight.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
                            topRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
                            topLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
                            normal.set(0, 0, 1);
                            addCubePlane(builder, center, bottomLeft, bottomLeftFront, bottomRight, bottomRightFront, topRight, topRightFront, topLeft, topLeftFront, normal, uvBottomLeft, uvTopRight);
                        }

                        if (world.getBlockType(x, y, z - 1) == BlockType.AIR.getBlockId()) {
                            // Back plane
                            bottomLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
                            bottomRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
                            topRight.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
                            topLeft.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
                            normal.set(0, 0, -1);
                            addCubePlane(builder, center, bottomLeft, bottomRightBack, bottomRight, bottomLeftBack, topRight, topLeftBack, topLeft, topRightBack, normal, uvBottomLeft, uvTopRight);
                        }

                        if (world.getBlockType(x + 1, y, z) == BlockType.AIR.getBlockId()) {
                            // Right plane
                            bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
                            bottomRight.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
                            topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
                            topLeft.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
                            normal.set(1, 0, 0);
                            addCubePlane(builder, center, bottomLeft, bottomRightFront, bottomRight, bottomRightBack, topRight, topRightBack, topLeft, topRightFront, normal, uvBottomLeft, uvTopRight);
                        }

                        if (world.getBlockType(x - 1, y, z) == BlockType.AIR.getBlockId()) {
                            // Left plane
                            bottomLeft.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
                            bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
                            topRight.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
                            topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
                            normal.set(-1, 0, 0);
                            addCubePlane(builder, center, bottomLeft, bottomLeftBack, bottomRight, bottomLeftFront, topRight, topLeftFront, topLeft, topLeftBack, normal, uvBottomLeft, uvTopRight);
                        }

                        if (world.getBlockType(x, y + 1, z) == BlockType.AIR.getBlockId()) {

                            region = atlas.findRegion(blockType.getTopTextureName());
                            uvBottomLeft.set(region.getU(), region.getV());
                            uvTopRight.set(region.getU2(), region.getV2());

                            // Top plane
                            bottomLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
                            bottomRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
                            topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
                            topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
                            normal.set(0, 1, 0);
                            addCubePlane(builder, center, bottomLeft, topLeftFront, bottomRight, topRightFront, topRight, topRightBack, topLeft, topLeftBack, normal, uvBottomLeft, uvTopRight);
                        }

                        if (world.getBlockType(x, y - 1, z) == BlockType.AIR.getBlockId()) {

                            region = atlas.findRegion(blockType.getBottomTextureName());
                            uvBottomLeft.set(region.getU(), region.getV());
                            uvTopRight.set(region.getU2(), region.getV2());

                            // Bottom plane
                            bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
                            bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
                            topRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
                            topLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
                            normal.set(0, -1, 0);
                            addCubePlane(builder, center, bottomLeft, bottomLeftFront, bottomRight, bottomRightFront, topRight, bottomRightBack, topLeft, bottomLeftBack, normal, uvBottomLeft, uvTopRight);
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

    public void addCubePlane(MeshBuilder builder, Vector3 center, Vector3 bottomLeftOffset, int bottomLeftAO, Vector3 bottomRightOffset,
                             int bottomRightAO, Vector3 topRightOffset, int topRightAO, Vector3 topLeftOffset, int topLeftAO, Vector3 normal, Vector2 uvBottomLeft, Vector2 uvTopRight) {

        Vector3 position = new Vector3(center.x + bottomLeftOffset.x, center.y + bottomLeftOffset.y,
                center.z + bottomLeftOffset.z);
        leftBottom.setPos(position).setNor(normal).setCol(bottomLeftAO,0,0,0).setUV(uvBottomLeft.x, uvBottomLeft.y);

        position.set(center.x + bottomRightOffset.x, center.y + bottomRightOffset.y, center.z + bottomRightOffset.z);
        rightBottom.setPos(position).setNor(normal).setCol(bottomRightAO,0,0,0).setUV(uvTopRight.x, uvBottomLeft.y);

        position.set(center.x + topRightOffset.x, center.y + topRightOffset.y, center.z + topRightOffset.z);
        rightTop.setPos(position).setNor(normal).setCol(topRightAO,0,0,0).setUV(uvTopRight.x, uvTopRight.y);

        position.set(center.x + topLeftOffset.x, center.y + topLeftOffset.y, center.z + topLeftOffset.z);
        leftTop.setPos(position).setNor(normal).setCol(topLeftAO,0,0,0).setUV(uvBottomLeft.x, uvTopRight.y);

        builder.rect(leftBottom, rightBottom, rightTop, leftTop);
    }

    public int getVertexAO(short neighborSide1, short neighborSide2, short neighborCorner) {
        if(neighborSide1 != BlockType.AIR.getBlockId() && neighborSide2 != BlockType.AIR.getBlockId())
            return 0;
        int ao = 0;
        if(neighborSide1 != BlockType.AIR.getBlockId()) ao++;
        if(neighborSide2 != BlockType.AIR.getBlockId()) ao++;
        if(neighborCorner != BlockType.AIR.getBlockId()) ao++;
        return 2 - ao;
    }
}
