package com.blockhalde.gui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.blockhalde.gui.grid.BackgroundActor;
import com.blockhalde.gui.grid.BlurActor;
import com.blockhalde.gui.grid.GridSystem;
import com.blockhalde.gui.pie.PieMenuSystem;

public class GUIManagement extends EntitySystem {
	
	Engine engine;
	private Stage stage;
	Actor backgroundActor;
	Actor blurActor;
	
	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		this.stage = RendererGUI.instance().getStage();
		drawBackground();
		super.addedToEngine(engine);
	}
	
	@Override
	public void update(float deltaTime) {
		manageMenus();
	}
	
	public void manageMenus() {
		
		if ((engine.getSystem(PieMenuSystem.class).isActive() || engine.getSystem(GridSystem.class).inventoryIsVisible()) && !backgroundActor.isVisible()) {
			backgroundActor.setVisible(true);
			blurActor.setVisible(true);
		} else if (!engine.getSystem(PieMenuSystem.class).isActive() && !engine.getSystem(GridSystem.class).inventoryIsVisible() && backgroundActor.isVisible()) {
			backgroundActor.setVisible(false);
			blurActor.setVisible(false);
		}
		
	}
	
	private void drawBackground() {
		backgroundActor = new BackgroundActor(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new Color(0.0f, 0.0f, 0.0f, 0.5f));
		blurActor = new BlurActor();
		stage.addActor(backgroundActor);
	}

}
