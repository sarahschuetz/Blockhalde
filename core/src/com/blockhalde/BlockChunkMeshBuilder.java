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

public class BlockChunkMeshBuilder {

	private BlockChunk chunk;
	private List<Mesh> meshes = new ArrayList<Mesh>();
	private long lastMeshBuildTime = Long.MIN_VALUE;
	
	public BlockChunkMeshBuilder(BlockChunk chunk) {
		this.chunk = chunk;
	}
	
	private void updateMesh() {
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

		for(int x = 0; x < chunkWidth; ++x) {
			for(int y = 0; y < chunkHeight; ++y) {
				
				for(int z = 0; z < chunkDepth; ++z) {
				
					center.set(firstX + x * blockSize,
							   firstY + y * blockSize,
							   firstZ + z * blockSize);
					
					
					// Front plane
					bottomLeft.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
					bottomRight.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
					topRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
					topLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
					normal.set(0, 0, 1);
					addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					
					// Back plane
					bottomLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
					bottomRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
					topRight.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
					topLeft.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
					normal.set(0, 0, -1);
					addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Top plane
					bottomLeft.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
					bottomRight.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
					topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
					topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
					normal.set(0, 1, 0);
					addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Bottom plane
					bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
					bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
					topRight.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
					topLeft.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
					normal.set(0, -1, 0);
					addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Left plane
					bottomLeft.set(-blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
					bottomRight.set(-blockSizeHalved, -blockSizeHalved, blockSizeHalved);
					topRight.set(-blockSizeHalved, blockSizeHalved, blockSizeHalved);
					topLeft.set(-blockSizeHalved, blockSizeHalved, -blockSizeHalved);
					normal.set(-1, 0, 0);
					addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
					
					// Right plane
					bottomLeft.set(blockSizeHalved, -blockSizeHalved, blockSizeHalved);
					bottomRight.set(blockSizeHalved, -blockSizeHalved, -blockSizeHalved);
					topRight.set(blockSizeHalved, blockSizeHalved, -blockSizeHalved);
					topLeft.set(blockSizeHalved, blockSizeHalved, blockSizeHalved);
					normal.set(1, 0, 0);
					addCubePlane(builder, center, bottomLeft, bottomRight, topRight, topLeft, normal);
				}
				
				if(builder.getNumVertices() > 30000) {
					Mesh partMesh = builder.end();
					meshes.add(partMesh);
					
					builder.begin(Usage.Position | Usage.Normal | Usage.TextureCoordinates, GL20.GL_TRIANGLES);
				}
			}
		}
		
		if(builder.getNumVertices() > 0) {
			Mesh partMesh = builder.end();
			meshes.add(partMesh);
		}
		
		lastMeshBuildTime = System.nanoTime();
	}

	public void addCubePlane(MeshBuilder builder,
			                 Vector3 center,
			                 Vector3 bottomLeftOffset,
			                 Vector3 bottomRightOffset,
			                 Vector3 topRightOffset,
			                 Vector3 topLeftOffset,
			                 Vector3 commonNormal) {
		
		
		Vector3 position = new Vector3(center.x + bottomLeftOffset.x, center.y + bottomLeftOffset.y, center.z + bottomLeftOffset.z);
		VertexInfo frontLeftBottom = new VertexInfo().setPos(position).setNor(commonNormal).setUV(new Vector2(0, 0));
		
		position.set(center.x + bottomRightOffset.x, center.y + bottomRightOffset.y, center.z + bottomRightOffset.z);
		VertexInfo frontRightBottom = new VertexInfo().setPos(position).setNor(commonNormal).setUV(new Vector2(1, 0));

		position.set(center.x + topRightOffset.x, center.y + topRightOffset.y, center.z + topRightOffset.z);
		VertexInfo frontRightTop = new VertexInfo().setPos(position).setNor(commonNormal).setUV(new Vector2(1, 1));
		
		position.set(center.x + topLeftOffset.x, center.y + topLeftOffset.y, center.z + topLeftOffset.z);
		VertexInfo frontLeftTop = new VertexInfo().setPos(position).setNor(commonNormal).setUV(new Vector2(0, 1));
		
		builder.rect(frontLeftBottom, frontRightBottom, frontRightTop, frontLeftTop);
	}
	
	public List<Mesh> getMeshes() {
		if(chunk.getLastModifiedTime() > lastMeshBuildTime) {
			updateMesh();
		}
		
		return meshes;
	}

}
