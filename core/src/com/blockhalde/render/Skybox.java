package com.blockhalde.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Skybox{

	protected final Pixmap[] data = new Pixmap[6];
	protected ShaderProgram shader;
	protected Cubemap cubemap;
	protected Mesh mesh;

	public Skybox() {
		// load shader
		shader = new ShaderProgram(Gdx.files.internal("shaders/skybox.vs.glsl"),
				Gdx.files.internal("shaders/skybox.fs.glsl"));
		if (!shader.isCompiled())
			throw new GdxRuntimeException(shader.getLog());
		
		// load texture
		//FileHandle f = Gdx.files.internal("textures/starrynight.png");
		//cubemap = new Cubemap(f, f, f, f, f, f);
		cubemap = new Cubemap(Gdx.files.internal("textures/front.png"), Gdx.files.internal("textures/lrb.png"),
							  Gdx.files.internal("textures/top.png"), Gdx.files.internal("textures/bottom.png"),
							  Gdx.files.internal("textures/lrb.png"), Gdx.files.internal("textures/lrb.png"));
		
		// create cube
		mesh = genCube();
	}

	public void render(Camera camera) {
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		shader.begin();
		cubemap.bind(0);
	    shader.setUniformi("u_cubeTexture", 0);
	    shader.setUniformMatrix("u_view", camera.view);
		shader.setUniformMatrix("u_projection", camera.projection);
		shader.setUniformf("u_camPosition", camera.position);
	    //shader.setUniformMatrix("u_view", new Matrix4().idt());
		mesh.render(shader, GL20.GL_TRIANGLES);

		shader.end();
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
	}
	
	public static Mesh genCube() {
		Mesh mesh = new Mesh(true, 24, 36, new VertexAttribute(Usage.Position,
				3, "a_position"), new VertexAttribute(Usage.Normal, 3,
				"a_normal"), new VertexAttribute(Usage.TextureCoordinates, 2,
				"a_texcoords"));

		float[] cubeVerts = { -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f,
				-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f,
				0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f,
				-0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f,
				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f,
				0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, };

		float[] cubeNormals = { 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
				-1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
				0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
				-1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, };

		float[] cubeTex = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, };

		float[] vertices = new float[24 * 8];
		int pIdx = 0;
		int nIdx = 0;
		int tIdx = 0;
		for (int i = 0; i < vertices.length;) {
			vertices[i++] = cubeVerts[pIdx++];
			vertices[i++] = cubeVerts[pIdx++];
			vertices[i++] = cubeVerts[pIdx++];
			vertices[i++] = cubeNormals[nIdx++];
			vertices[i++] = cubeNormals[nIdx++];
			vertices[i++] = cubeNormals[nIdx++];
			vertices[i++] = cubeTex[tIdx++];
			vertices[i++] = cubeTex[tIdx++];
		}

		short[] indices = { 0, 2, 1, 0, 3, 2, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8,
				10, 11, 12, 15, 14, 12, 14, 13, 16, 17, 18, 16, 18, 19, 20, 23,
				22, 20, 22, 21 };

		mesh.setVertices(vertices);
		mesh.setIndices(indices);

		return mesh;
	}

}