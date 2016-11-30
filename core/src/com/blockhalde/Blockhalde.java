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

	@Override
	public void create() {
		engine = new Engine();
		world = new World();
		
		engine.addSystem(new CameraSystem());
		engine.addSystem(new RenderSystem(world));

//		for(int i = 0; i < world.getVisibleChunks().size(); i++){
//			Chunk chunk = world.getVisibleChunks().get(i);
//			if(chunk!=null){
//				BlockChunkMeshBuilder blockChunkMeshBuilder = new BlockChunkMeshBuilder(chunk);
//				for(int j = 0; j < blockChunkMeshBuilder.getMeshes().size(); j++){
//					meshes.add(blockChunkMeshBuilder.getMeshes().get(j));
//				}
//			}
//		}
		
		//chunk.setBlockTypeAt(BlockType.WATER, 10, 100, 10);
		
		//inputProcessor = new PhysicalInputProcessor(camVC);
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.input.setCursorCatched(true);
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
	}
	
	@Override
	public void resize(int width, int height){
		//cam.viewportWidth =  width;
		//cam.viewportHeight = height;
		RendererGUI.instance().resize(width, height);
	}

	@Override
	public void render() {
	 	
		/*RendererGUI.instance().setDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cam.position.toString() + 
				"\nM: toggle menu, Q + E: iterate items");
		RendererGUI.instance().render();*/

		engine.update(Gdx.graphics.getRawDeltaTime());
	}
}