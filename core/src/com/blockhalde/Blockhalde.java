package com.blockhalde;

import java.util.Random;

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
import com.blockhalde.player.PlayerPositionTrackingSystem;
import com.blockhalde.render.CameraSystem;
import com.blockhalde.render.RenderSystem;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.messaging.message.ChunkUpdateMessage;
import com.terrain.block.BlockType;
import com.terrain.world.WorldManagementSystem;
import com.util.noise.debug.DebugPerlinNoiseSystem;

public class Blockhalde extends ApplicationAdapter {
	
	private Engine engine;
	private CameraSystem cameraSystem;

	@Override
	public void create() {
//		MessageManager.getInstance().setDebugEnabled(true);
		// !!!!!Add message listener before world system creation!!!!!
		MessageManager msgManager = MessageManager.getInstance();
		
		msgManager.addListener(new Telegraph() {
			@Override
			public boolean handleMessage(Telegram msg) {
				final ChunkMessage chunkMessage = (ChunkMessage) msg.extraInfo;

				// this ensures both rendersystem and world are loaded
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						engine.getSystem(RenderSystem.class).loadChunk(chunkMessage);
					}
				});
				
				return true;
			}
		}, MessageIdConstants.CHUNK_CREATED_MSG_ID);
		
		msgManager.addListener(new Telegraph() {
			@Override
			public boolean handleMessage(Telegram msg) {
				final ChunkUpdateMessage chunkMessage = (ChunkUpdateMessage) msg.extraInfo;

				// this ensures both rendersystem and world are loaded
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						RenderSystem sys = engine.getSystem(RenderSystem.class);
						sys.updateBlock(chunkMessage.getxPosition(), chunkMessage.getyPosition(), chunkMessage.getzPosition());
					}
				});
				
				return true;
			}
		}, MessageIdConstants.BLOCK_UPDATED_MSG_ID);
		
		engine = new Engine();
		
		cameraSystem = new CameraSystem();
		engine.addSystem(cameraSystem);
		engine.addSystem(new PieMenuSystem());
		engine.addSystem(new InputSystem());
		engine.addSystem(new WorldManagementSystem());
		engine.addSystem(new RenderSystem());
		engine.addSystem(new DebugPerlinNoiseSystem());
		
		engine.addEntity(new Player(cameraSystem.getCam()));
		engine.addSystem(new PlayerPositionTrackingSystem());
	}
	
	@Override
	public void resize(int width, int height){
		cameraSystem.resize(width, height);
		RendererGUI.instance().resize(width, height);
		engine.getSystem(InputSystem.class).resize(width, height);
	}

	@Override
	public void render() {
		testBlockUpdates();
		
		engine.update(Gdx.graphics.getRawDeltaTime());
		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
		MessageManager.getInstance().update();
		RendererGUI.instance().addDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cameraSystem.getCam().position.toString() + 
				"\nN: toggle Perlin Noise Debug View (use V and B to go up and down the Z axis)" + 
				"\nright click for pie menu");
		RendererGUI.instance().render();
	}

	private void testBlockUpdates() {
		final int minX = -100;
		final int maxX = 100;
		final int minY = 50;
		final int maxY = 150;
		final int minZ = -100;
		final int maxZ = 100;
		
		Random rnd = new Random();
		int x = minX + rnd.nextInt(maxX - minX);
		int y = minY + rnd.nextInt(maxY - minY);
		int z = minZ + rnd.nextInt(maxZ - minZ);

		engine.getSystem(WorldManagementSystem.class).setBlock(x, y, z, BlockType.TNT);
	}
}