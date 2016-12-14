package com.blockhalde.player;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.msg.MessageManager;
import com.messaging.MessageIdConstants;
import com.messaging.message.ChunkMessage;
import com.terrain.chunk.ChunkPosition;
import com.terrain.chunk.ChunkUtil;

public class PlayerPositionTrackingSystem extends EntitySystem {
	private final Logger logger = new Logger("PlayerPositionTrackingSystem", Logger.ERROR);
	private PositionComponent playerPosition;
	private ChunkPosition currentChunkPosition;
	
	@Override
	public void addedToEngine(Engine engine) {
		setupPlayerData(engine);
		sendPlayerChangedChunkMessage();
		
		if(playerPosition == null) {
			logger.error("No player position component found.");
		}
	}
	
	@Override
	public void update(float deltaTime) {
		final ChunkPosition newChunkPosition = ChunkUtil.getChunkPositionFor((int) playerPosition.getPosition().x, (int) playerPosition.getPosition().z);
		
		if(!currentChunkPosition.equals(newChunkPosition)) {
			currentChunkPosition = newChunkPosition;
			sendPlayerChangedChunkMessage();
		}
	}
	
	private void setupPlayerData(Engine engine) {
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.one(PlayerDataComponent.class, PositionComponent.class).get());
		Player player = (Player) entities.get(0);
		playerPosition = player.getComponent(PositionComponent.class);
		currentChunkPosition = ChunkUtil.getChunkPositionFor((int) playerPosition.getPosition().x, (int) playerPosition.getPosition().z);
	}
	
	private void sendPlayerChangedChunkMessage() {
		MessageManager.getInstance().dispatchMessage(0f, null, null, MessageIdConstants.PLAYER_CHANGED_CHUNK_POSITION_MSG_ID, new ChunkMessage(currentChunkPosition));
	}
}