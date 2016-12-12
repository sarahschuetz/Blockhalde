package com.blockhalde;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.msg.GdxAI;
import com.badlogic.msg.MessageDispatcher;
import com.badlogic.msg.MessageManager;
import com.badlogic.msg.Telegram;
import com.badlogic.msg.Telegraph;
import com.blockhalde.gui.RendererGUI;
import com.blockhalde.gui.pie.PieMenuSystem;
import com.blockhalde.input.InputSystem;
import com.blockhalde.player.Player;
import com.blockhalde.render.CameraSystem;
import com.blockhalde.render.RenderSystem;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.terrain.world.WorldManagementSystem;
import com.util.noise.debug.DebugPerlinNoiseSystem;

public class Blockhalde extends ApplicationAdapter {
	
	private Engine engine;
	private CameraSystem cameraSystem;

	@Override
	public void create() {
//		MessageManager.getInstance().setDebugEnabled(true);
		// !!!!!Add message listener before world system creation!!!!!
//		MessageManager.getInstance().addListener(new Telegraph() {
//			
//			@Override
//			public boolean handleMessage(Telegram msg) {
//				final ChunkMessage chunkMessage = (ChunkMessage) msg.extraInfo;
//				System.out.print("Chunk created: ");
//				System.out.println("X: " + chunkMessage.getChunkPosition().getXPosition());
//				System.out.println("Z: " + chunkMessage.getChunkPosition().getZPosition());
//				return true;
//			}
//		}, MessageIdConstants.CHUNK_CREATED_MSG_ID);
		
		engine = new Engine();
		
		cameraSystem = new CameraSystem();
		engine.addSystem(cameraSystem);
		engine.addSystem(new PieMenuSystem());
		engine.addSystem(new InputSystem());
		engine.addSystem(new WorldManagementSystem());
		engine.addSystem(new RenderSystem());
		engine.addSystem(new DebugPerlinNoiseSystem());
		
		engine.addEntity(new Player(cameraSystem.getCam()));
	}
	
	@Override
	public void resize(int width, int height){
		cameraSystem.resize(width, height);
		RendererGUI.instance().resize(width, height);
		engine.getSystem(InputSystem.class).resize(width, height);
	}

	@Override
	public void render() {
		engine.update(Gdx.graphics.getRawDeltaTime());
		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
		MessageManager.getInstance().update();
		RendererGUI.instance().addDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cameraSystem.getCam().position.toString() + 
				"\nN: toggle Perlin Noise Debug View (use V and B to go up and down the Z axis)" + 
				"\nright click for pie menu");
		RendererGUI.instance().render();
	}
}