package com.blockhalde.gui.pie;

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
import com.badlogic.gdx.utils.Array;
import com.blockhalde.gui.RendererGUI;

/**
 * The {@link PieMenuSystem} serves a exquisite pie menu. Pops up when clicking right mouse button.
 * Pie menu slices are created in constructor. Additional menu slices can be added with {@link #addPieSlice(String, Command)}.
 * It is using tweenengine @see <a href="https://code.google.com/archive/p/java-universal-tween-engine/wikis/GetStarted.wiki">tweenengine wiki</a> for smooth motion.
 */
public class PieMenuSystem extends EntitySystem{
	private static final float RADIUS = 60.0f;
	private float degreeInterval;
	private Vector2 center;
	private Vector2 localPos;
	private Stage stage;
	private boolean isActive;
	private Array<PieSlice> slices;
	public TweenManager tweenManager;

	public PieMenuSystem(){
		this.stage = RendererGUI.instance().getStage();
		Tween.registerAccessor(Actor.class, new ActorAccessor());
		center = new Vector2(Gdx.graphics.getWidth()*0.5f, Gdx.graphics.getHeight()*0.5f);
		slices = new Array<PieSlice>();
		tweenManager = new TweenManager();
		
		// --- SETUP TEST SLICES ---
		Command cmdInventory = new Command(){
			public void execute() {
				System.out.println("Inventory");
			}
		};
		
		Command cmdDebug = new Command(){
			public void execute() {
				System.out.println("Debug");
			}
		};
		
		Command cmdCraft = new Command(){
			public void execute() {
				System.out.println("Craft");
			}
		};
		
		Command cmdCall = new Command(){
			public void execute() {
				System.out.println("H: Hi, here's Heinzibert!");
				
				new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	System.out.println("H: What do you want?");
				            }
				        }, 
				        2000 
				);
				
				new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	System.out.println("H: Hello?");
				            }
				        }, 
				        5000 
				);

			}
		};
		
		slices.add(new PieSlice("Inventory").setCommand(cmdInventory));
		slices.add(new PieSlice("Craft").setCommand(cmdCraft));
		//slices.add(new PieSlice("Debug").setCommand(cmdDebug));
		slices.add(new PieSlice("Call Heinzibert").setCommand(cmdCall));
		// -------------------
		
		for(PieSlice p : slices){
			stage.addActor(p);
		}
		calcPositions();
		
		isActive = true;
		setActive(false);
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
	
	/**
	 * Activates/deactivates the pie menu system.
	 * When active, mouse input is processed by {@link PieMenuSystem}
	 */
	public void setActive(boolean active){
		if(active != isActive){
			isActive = active;

			tweenManager.killAll();
			
			if(isActive){
				// tween in pie slices
				for(PieSlice p : slices){
					
					p.setVisible(true);
					Tween.to(p, ActorAccessor.XY, 0.4f).target(p.getOriginalPos().x, p.getOriginalPos().y).ease(Elastic.OUT).start(tweenManager);
				}
			}else{
				// tween out pie slices
				tweenManager.killAll();
				
				for(final PieSlice p : slices){
					TweenCallback tc = new TweenCallback(){
						public void onEvent(int type, BaseTween<?> baseTween) {
							p.setVisible(false);
						}
					};
					Tween.to(p, ActorAccessor.XY, 0.3f).target(center.x, center.y).ease(TweenEquations.easeInBack).start(tweenManager).setCallback(tc);
					
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
		degreeInterval = -360 / slices.size;
		localPos = new Vector2(0,RADIUS);
		
		for(int n = 0; n < slices.size; n++){
			PieSlice pie = slices.get(n);
			pie.setPosition(center.x, center.y);
			pie.setOriginalPos(localPos.cpy().scl(1.2f).add(center));
			localPos.rotate(degreeInterval);
		}
	}
	
	private int lastScreenX, lastScreenY = 0;
	
	public void touchDragged(int screenX, int screenY) {
		if(isActive){
			Vector2 dir = new Vector2(lastScreenX - screenX, lastScreenY - screenY);
			dir.rotate(-90);
			float angle = dir.angle();
			int index = Math.abs(Math.round(angle/degreeInterval));
			//System.out.println(dir.len2());
			
			if(index < slices.size)
			for(int i = 0; i < slices.size; i++){
				PieSlice p = slices.get(i);
				if(i != index){ //|| dir.len2() < 10){
					if(p.isActive()){
						p.setActive(false);
						Tween.to(slices.get(i), ActorAccessor.XY, 0.1f).target(p.getOriginalPos().x, p.getOriginalPos().y).ease(TweenEquations.easeInOutCubic).start(tweenManager);
					}
				}else{
					if(!p.isActive()){
						p.setActive(true);
						if(!tweenManager.containsTarget(p)){
							Vector2 target = p.getOriginalPos().cpy().sub(center).scl(1.5f).add(center);
							Tween.to(slices.get(i), ActorAccessor.XY, 0.1f).target(target.x, target.y).ease(TweenEquations.easeInOutCubic).start(tweenManager);
						}
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
