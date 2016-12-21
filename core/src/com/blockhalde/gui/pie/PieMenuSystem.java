package com.blockhalde.gui.pie;

import java.awt.Dimension;
import java.awt.Toolkit;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.blockhalde.gui.RendererGUI;

/**
 * The {@link PieMenuSystem} serves a exquisite pie menu. Pops up when clicking right mouse button.
 * Pie menu slices are created in constructor. Additional menu slices can be added with {@link #addPieSlice(String, Command)}.
 * It is using tweenengine @see <a href="https://code.google.com/archive/p/java-universal-tween-engine/wikis/GetStarted.wiki">tweenengine wiki</a> for smooth motion.
 */
public class PieMenuSystem extends EntitySystem{
	private static final float RADIUS = 35.0f;
	private float degreeInterval;
	private Vector2 center;
	private Vector2 localPos;
	private Stage stage;
	private boolean isActive;
	private Array<PieSlice> slices;
	private TweenManager tweenManager;
	private ArrowActor arrow;

	public PieMenuSystem(){
		this.stage = RendererGUI.instance().getStage();
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		center = new Vector2(Gdx.graphics.getWidth()*0.5f, Gdx.graphics.getHeight()*0.5f);
		slices = new Array<PieSlice>();
		tweenManager = new TweenManager();
		
		// --- SETUP TEST SLICES ---
		Command cmdInventory = new Command(){
			public void execute() {
				RendererGUI.instance().toggleMenu();
			}
		};
		
		Command cmdFullscreen = new Command(){
			Dimension windowSize = new Dimension(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			public void execute() {
				if(Gdx.graphics.isFullscreen()){
					Gdx.graphics.setDisplayMode(windowSize.width, windowSize.height, false);
				}else{
					Gdx.graphics.setDisplayMode(screenSize.width, screenSize.height, true);
				}
				
			}
		};
		
		slices.add(new PieSlice("Inventory").setCommand(cmdInventory));
		slices.add(new PieSlice("Fullscreen").setCommand(cmdFullscreen));
		// -------------------
		
		for(PieSlice p : slices){
			stage.addActor(p);
		}
		calcPositions();
		
		arrow = new ArrowActor();
		arrow.setPosition(center.x, center.y);
		
		stage.addActor(arrow);
		
		isActive = true;
		setActive(false);
	}
	
	/**
	 * add pie slice to menu
	 * @param name the rendered text of the pie slice label
	 * @param command the command executed when slice is selected
	 */
	public void addPieSlice(String name, Command command){
		PieSlice slice = new PieSlice(name).setCommand(command);
		slices.add(slice);
		calcPositions();
		stage.addActor(slice);
	}
	
	/**
	 * Activates/deactivates the pie menu system.
	 * When active, mouse input is processed by {@link PieMenuSystem}
	 */
	public void setActive(boolean active){
		if(active != isActive){
			isActive = active;

			tweenManager.killAll();
			
			if(isActive){
				if(arrow != null) arrow.setVisible(true);
				meanDirection.setZero();
				arrow.setVector(meanDirection.cpy());
				
				// tween in
				for(PieSlice p : slices){
					p.setVisible(true);
					Tween.to(p, ActorAccessor.XY, 0.4f).target(p.getOriginalPos().x, p.getOriginalPos().y).ease(Elastic.OUT).start(tweenManager);
					Tween.to(p, ActorAccessor.ALPHA,  0.05f).target(1).ease(TweenEquations.easeOutQuad).start(tweenManager);
				}
			}else{
				if(arrow != null) arrow.setVisible(false);

				tweenManager.killAll();
				
				// tween out
				for(final PieSlice p : slices){
					TweenCallback tc = new TweenCallback(){
						public void onEvent(int type, BaseTween<?> baseTween) {
							p.setVisible(false);
						}
					};
					Tween.to(p, ActorAccessor.XY, 0.3f).target(center.x, center.y).ease(TweenEquations.easeInBack).start(tweenManager).setCallback(tc);
					Tween.to(p, ActorAccessor.ALPHA, 0.4f).target(0).ease(TweenEquations.easeOutQuad).start(tweenManager);
					
					if(p.isActive()){
						p.execute();
					}
					p.setActive(false);
				}
			}
		}
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	private void calcPositions(){
		degreeInterval = 360 / slices.size;
		localPos = new Vector2(0,RADIUS);
		
		for(int n = 0; n < slices.size; n++){
			PieSlice pie = slices.get(n);
			pie.setPosition(center.x, center.y);
			float degree = degreeInterval*n;
			if(degree < 30 || degree >= 330){
				pie.setAlignment(Align.top);
			}else if(degree >= 30 && degree < 150){
				pie.setAlignment(Align.right);
			}else if(degree >= 150 && degree < 210){
				pie.setAlignment(Align.bottom);
			}else{

				pie.setAlignment(Align.left);
			}
			pie.setOriginalPos(localPos.cpy().scl(1.2f).add(center));
			localPos.rotate(-degreeInterval);
		}
	}
	
	private int lastScreenX, lastScreenY = 0;
	private Vector2 meanDirection = new Vector2();
	
	public void touchDragged(int screenX, int screenY) {
		if(isActive){
			
			// mouse coords to stage coords
			Vector2 mouseDir = new Vector2(screenX - lastScreenX , lastScreenY - screenY);
			
			// calc mean
			meanDirection.add(mouseDir);
			if(meanDirection.len2() >= 625){
				meanDirection.nor().scl(25);
			}
			
			arrow.setVector(meanDirection.cpy());
			
			// calculate index of selected
			float angle = meanDirection.cpy().rotate(-90-degreeInterval*0.5f).angle(); // rotate to 12 o'clock and minus half the degree interval
			int index = Math.abs((int)((angle)/degreeInterval));
			if(index >= slices.size){
				index = slices.size-1;
			}
			index = slices.size - index - 1;	// invert index (because angle() is counterclockwise)
			
			for(int i = 0; i < slices.size; i++){
				PieSlice p = slices.get(i);
				// check index & if direction vector is long enough for selection
				if(i == index && meanDirection.len2() > 600){ 
					if(!p.isActive()){
						p.setActive(true);
						tweenManager.killTarget(p);
						p.getColor().a = 1;
						Vector2 target = p.getOriginalPos().cpy().sub(center).scl(1.1f).add(center);
						Tween.to(slices.get(i), ActorAccessor.XY, 0.08f).target(target.x, target.y).ease(TweenEquations.easeInOutCubic).start(tweenManager);
						Tween.to(slices.get(i), ActorAccessor.SCALEXY, 0.08f).target(1.2f, 1.2f).ease(TweenEquations.easeInOutCubic).start(tweenManager);
					}
				}else{
					if(p.isActive() ){
						p.setActive(false);
						Tween.to(slices.get(i), ActorAccessor.XY, 0.08f).target(p.getOriginalPos().x, p.getOriginalPos().y).ease(TweenEquations.easeInOutCubic).start(tweenManager);
						Tween.to(slices.get(i), ActorAccessor.SCALEXY, 0.08f).target(1f, 1f).ease(TweenEquations.easeInOutCubic).start(tweenManager);
					}
				}
			}
			
		}
		
		lastScreenX = screenX;
		lastScreenY = screenY;
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		for(PieSlice p : slices){
			p.remove();
		}
		
		super.removedFromEngine(engine);
	}
	
	@Override
	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}
}
