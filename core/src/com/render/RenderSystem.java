package com.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.render.ChunkMeshCache.CachedSubchunk;
import com.terrain.world.World;

public class RenderSystem extends EntitySystem {

	private ChunkMeshCache meshCache;
	private Texture texture;
	private ShaderProgram shader;
	private Engine engine;
	
	public RenderSystem(World world) {
		meshCache = new ChunkMeshCache(world);
		texture = new Texture(Gdx.files.internal("textures/pack.png"));
	}

	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		shader.begin();
		
		CameraSystem c = engine.getSystem(CameraSystem.class);
		
		// Normal matrix is inverse transposed modelview matrix
		Matrix4 normalMatrix = c.cam.view.cpy().inv().tra();
		
		shader.setUniformMatrix("u_view", c.cam.view);
		shader.setUniformMatrix("u_projection", c.cam.projection);

		texture.bind();
		shader.setUniformi("u_texture", 0);
		shader.setUniformMatrix("u_normalMatrix", normalMatrix);

		for(CachedSubchunk cache: meshCache.getCachedSubs()) {
			cache.mesh.render(shader, GL20.GL_TRIANGLES);
		}
		
		shader.end();
	}

	@Override
	public void addedToEngine(Engine engine) {
		
		this.engine = engine;
		long start = System.currentTimeMillis();
		meshCache.update();
		System.out.println("Initial generation time: " + (System.currentTimeMillis() - start));
		
		shader = new ShaderProgram(Gdx.files.internal("shaders/blocks.vs.glsl"),
				                   Gdx.files.internal("shaders/blocks.fs.glsl"));
		
		if(!shader.isCompiled()) {
			System.out.println("Something went wrong during shader compilation, have a look at the log:");
			System.out.println(shader.getLog());
		}
		
		super.addedToEngine(engine);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		// TODO Auto-generated method stub
		super.removedFromEngine(engine);
	}
	
	
}
