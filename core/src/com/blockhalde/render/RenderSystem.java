package com.blockhalde.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.terrain.world.WorldManagementSystem;

public class RenderSystem extends EntitySystem {

	public static final int SUBCHUNK_HEIGHT = 16;
	
	private ChunkMeshCache meshCache;
	private Texture texture;
	private ShaderProgram shader;
	private Engine engine;
	private WorldManagementSystem worldManagementSystem;
	
	public RenderSystem() {
		texture = new Texture(Gdx.files.internal("textures/blocks.png"));
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;

		worldManagementSystem = engine.getSystem(WorldManagementSystem.class);

		//long start = System.currentTimeMillis();
		meshCache = new ChunkMeshCache(worldManagementSystem);
		meshCache.update();
		//System.out.println("Initial mesh generation time: " + (System.currentTimeMillis() - start) + "ms");
		
		shader = new ShaderProgram(Gdx.files.internal("shaders/blocks.vs.glsl"),
				                   Gdx.files.internal("shaders/blocks.fs.glsl"));
		
		if(!shader.isCompiled()) {
			System.out.println("Something went wrong during shader compilation, have a look at the log:");
			System.out.println(shader.getLog());
		}
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
	}

	@Override
	public void update(float deltaTime) {
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		//long start = System.currentTimeMillis();
		meshCache.update();
		//System.out.println("Mesh update time: " + (System.currentTimeMillis() - start) + "ms");
		
		shader.begin();
		
		Camera cam = engine.getSystem(CameraSystem.class).getCam();
		
		// Normal matrix is inverse transposed modelview matrix
		Matrix4 normalMatrix = cam.view.cpy().inv().tra();
		
		// No model matrix necessary, position is encoded in mesh data
		shader.setUniformMatrix("u_view", cam.view);
		shader.setUniformMatrix("u_projection", cam.projection);

		texture.bind();
		shader.setUniformi("u_texture", 0);
		shader.setUniformMatrix("u_normalMatrix", normalMatrix);

		for(CachedSubchunk cached: meshCache.getCachedSubchunks()) {
			if(!cached.isUnused()) {
				cached.mesh.render(shader, GL20.GL_TRIANGLES);
			}
		}
		
		shader.end();
	}
	
}
