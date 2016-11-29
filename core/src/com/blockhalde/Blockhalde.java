package com.blockhalde;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blockhalde.gui.RendererGUI;
import com.terrain.chunk.Chunk;
import com.terrain.chunk.TerrainChunk;
import com.terrain.world.World;

import java.util.ArrayList;
import java.util.List;

public class Blockhalde extends ApplicationAdapter {

	private PerspectiveCamera cam;
	private TerrainChunk chunk;
	private BlockChunkMeshBuilder chunkMeshBuilder;
	private ShaderProgram shader;
	private CameraInputController inputController;
	
	private Texture texture;


	private World world;
	private List<Mesh> meshes = new ArrayList<Mesh>();

	@Override
	public void create() {
		world = new World();

		for(int i = 0; i < world.getVisibleChunks().size(); i++){
			Chunk chunk = world.getVisibleChunks().get(i);
			if(chunk!=null){
				BlockChunkMeshBuilder blockChunkMeshBuilder = new BlockChunkMeshBuilder(chunk);
				for(int j = 0; j < blockChunkMeshBuilder.getMeshes().size(); j++){
					meshes.add(blockChunkMeshBuilder.getMeshes().get(j));
				}
			}
		}

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 50f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		
		//chunk.setBlockTypeAt(BlockType.WATER, 10, 100, 10);
		
		texture = new Texture("textures/dirt.gif");
		
		shader = new ShaderProgram(Gdx.files.internal("shaders/blocks.vs.glsl"),
				                   Gdx.files.internal("shaders/blocks.fs.glsl"));
		
		if(!shader.isCompiled()) {
			System.out.println("Something went wrong during shader compilation, have a look at the log:");
			System.out.println(shader.getLog());
		}
		
		inputController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(inputController);
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
	}
	
	@Override
	public void resize(int width, int height){
		cam.viewportWidth =  width;
		cam.viewportHeight = height;
		RendererGUI.instance().resize(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		shader.begin();
		
		// Normal matrix is inverse transposed modelview matrix
		Matrix4 normalMatrix = cam.view.cpy().inv().tra();
		
		shader.setUniformMatrix("u_view", cam.view);
		shader.setUniformMatrix("u_projection", cam.projection);

		texture.bind();
		shader.setUniformi("u_texture", 0);
		shader.setUniformMatrix("u_normalMatrix", normalMatrix);


		for(int i = 0; i < meshes.size(); i++){
			meshes.get(i).render(shader, GL20.GL_TRIANGLES);
		}

		shader.end();
	 	
		RendererGUI.instance().setDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cam.position.toString() + 
				"\nM: toggle menu, Q + E: iterate items");
		RendererGUI.instance().render();
	}
}
