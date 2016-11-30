package com.blockhalde;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.blockhalde.gui.RendererGUI;
import com.blockhalde.input.PhysicalInputProcessor;
import com.blockhalde.input.PlayerVirtualController;
import com.blockhalde.input.VirtualController;
import com.render.CameraSystem;
import com.render.ChunkMeshBuilder;
import com.render.ChunkMeshCache;
import com.render.ChunkMeshCache.CachedSubchunk;
import com.render.RenderSystem;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.TerrainChunk;
import com.terrain.world.World;

import java.util.ArrayList;
import java.util.List;

public class Blockhalde extends ApplicationAdapter {
	
	//private ChunkMeshBuilder chunkMeshBuilder;
	private InputProcessor inputProcessor;
	
	private World world;
	private Engine engine;
	private CameraSystem cameraSystem;

	@Override
	public void create() {
		engine = new Engine();
		world = new World();
		
		cameraSystem = new CameraSystem();
		engine.addSystem(cameraSystem);
		engine.addSystem(new RenderSystem(world));
		
		inputProcessor = new PhysicalInputProcessor(cameraSystem.camVC);
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.input.setCursorCatched(true);
	}
	
	@Override
	public void resize(int width, int height){
		cameraSystem.resize(width, height);
		RendererGUI.instance().resize(width, height);
	}

	@Override
	public void render() {
	 	
		RendererGUI.instance().setDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cameraSystem.cam.position.toString() + 
				"\nM: toggle menu, Q + E: iterate items");
		RendererGUI.instance().render();

		engine.update(Gdx.graphics.getRawDeltaTime());
	}
}