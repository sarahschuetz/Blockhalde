package com.blockhalde.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;

public class Player extends Entity {
	public Player(Camera camera) {
		add(new PlayerDataComponent());
		add(new CameraComponent(camera));
		add(new PositionComponent(camera.position));
	}
}
