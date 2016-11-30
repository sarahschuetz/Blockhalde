package com.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class CameraSystem extends EntitySystem {
	
	private PerspectiveCamera cam;

	@Override
	public void addedToEngine(Engine engine) {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 50f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
	}
	
	public void resize(int width, int height){
		cam.viewportWidth =  width;
		cam.viewportHeight = height;
		cam.update();
	}

	public PerspectiveCamera getCam() {
		return cam;
	}

}
