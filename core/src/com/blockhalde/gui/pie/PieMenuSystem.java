package com.blockhalde.gui.pie;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Elastic;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.blockhalde.gui.RendererGUI;

/**
 * The {@link PieMenuSystem} serves a exquisite pie menu.
 * Is using tweenengine @see <a href="https://code.google.com/archive/p/java-universal-tween-engine/wikis/GetStarted.wiki">tweenengine wiki</a> for smooth motion.
 */
public class PieMenuSystem extends EntitySystem{
	private static final float RADIUS = 22.0f;
	private float degreeInterval;
	private Vector2 center;
	private Vector2 localPos;
	private Stage stage;
	private boolean isActive;
	private Array<PieSlice> slices;

	/***
	 * This system implements a pie menu. Pops up when clicking right mouse button.
	 * Pie menu slices are created in constructor.
	 * Additional menu slices can be added with {@link #addPieSlice(String, Command)}.
	 */
	public PieMenuSystem(){
		this.stage = RendererGUI.instance().getStage();
		isActive = false;
		center = new Vector2(Gdx.graphics.getWidth()*0.5f, Gdx.graphics.getHeight()*0.5f);
		slices = new Array<PieSlice>();
		
		Command cmdInventory = new Command(){
			public void execute() {
				System.out.println("Inventory");
			}
		};
		
		slices.add(new PieSlice("Inventory").setCommand(cmdInventory));
		slices.add(new PieSlice("Craft").setCommand(cmdInventory));
		slices.add(new PieSlice("Debug").setCommand(cmdInventory));
		slices.add(new PieSlice("Call Frank").setCommand(cmdInventory));
		slices.add(new PieSlice("Call Heinzibert").setCommand(cmdInventory));
		
		for(PieSlice p : slices){
			stage.addActor(p);
		}
		calcPositions();
		
		setActive(false);
	}
	
	/**
	 * Activates/deactivates the pie menu system.
	 * When active, mouse input is processed by {@link PieMenuSystem}
	 */
	public void setActive(boolean active){
		isActive = active;
		
		if(isActive){
			for(PieSlice p : slices){
				p.setVisible(true);
				Tween.from(p, ActorAccessor.XY, 3.0f).target(0, 0).ease(Elastic.INOUT);
			}
		}else{
			for(PieSlice p : slices){
				p.setVisible(false);
			}
		}
	}
	
	private void calcPositions(){
		degreeInterval = -360 / slices.size;
		localPos = new Vector2(0,RADIUS);
		
		for(int n = 0; n < slices.size; n++){
			PieSlice pie = slices.get(n);
			pie.setPosition(center.x + localPos.x, center.y + localPos.y);
			localPos.rotate(degreeInterval);
		}
	}
	
	/**
	 * add pie slice to menu
	 * @param name the rendered text of the pie slice label
	 * @param command the command executed when slice is selected
	 */
	public void addPieSlice(String name, Command command){
		slices.add(new PieSlice(name).setCommand(command));
		calcPositions();
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		for(PieSlice p : slices){
			p.remove();
		}
		
		super.removedFromEngine(engine);
	}

	public boolean isActive() {
		return isActive;
	}
	
	private int lastScreenX, lastScreenY = 0;
	
	public void mouseMoved(int screenX, int screenY) {
		if(isActive){
			Vector2 dir = new Vector2(lastScreenX - screenX, lastScreenY - screenY);
			float angle = dir.angle();
			int index = Math.abs(Math.round(angle/degreeInterval));
			System.out.println(index);
			slices.get(index).setActive(true);
		}
		
		lastScreenX = screenX;
		lastScreenY = screenY;
	}
	
	/*
	protected void positionChanged() {
		degreeInterval = -360 / slices.size();
		localPos = new Vector2(0,RADIUS);
		
		for(int n = 0; n < slices.size(); n++){
			if(slices.get(n).isActive()){
				shapeRenderer.setColor(Color.GREEN);
			}else{
				shapeRenderer.setColor(Color.WHITE);
			}
			Label label = slices.get(n).getLabel();
			Vector2 localPosTxt = localPos.cpy().scl(3f);
			if(localPos.x > 0){
				label.setPosition(getX() + localPosTxt.x, getY() + localPosTxt.y - label.getGlyphLayout().height * 0.5f);
			}else if(localPos.x < 0){
				label.setPosition(getX() + localPosTxt.x - label.getGlyphLayout().width, getY() + localPosTxt.y - label.getGlyphLayout().height * 0.5f);
			}else{
				label.setPosition(getX() + localPosTxt.x - (label.getGlyphLayout().width * 0.5f), getY() + localPosTxt.y  - label.getGlyphLayout().height * 0.5f);
			}
			
			
			localPos.rotate(degreeInterval);
		}
		
		super.positionChanged();
	}
	
	

	*/
	
	/*
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//batch.end();
		
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		
		localPos = new Vector2(0,RADIUS);
		for(int n = 0; n < slices.size(); n++){
			if(slices.get(n).isActive()){
				shapeRenderer.setColor(Color.GREEN);
			}else{
				shapeRenderer.setColor(Color.WHITE);
			}
			shapeRenderer.circle(getX()+localPos.x, getY()+localPos.y, 4f);
			localPos.rotate(degreeInterval);
		}
		Tween.to(localPos, 0, 1.0f).target(20, 30).ease(Elastic.INOUT);

		shapeRenderer.end();
		//batch.begin();
		super.draw(batch, parentAlpha);
	}
	*/
	
}
