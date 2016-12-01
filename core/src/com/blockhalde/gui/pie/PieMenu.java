package com.blockhalde.gui.pie;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class PieMenu extends Actor {
	static final float RADIUS = 16.0f;
	
	private List<PieSlice> slices;
	private float degreeInterval;
	private Vector2 localPos;
	private ShapeRenderer shapeRenderer;
	private Stage stage;
	
	/***
	 * Create pie menu. Pass position as parameter.
	 */
	public PieMenu(Stage stage){
		this.stage = stage;
		slices = new LinkedList<PieSlice>();
		shapeRenderer = new ShapeRenderer();
		
		Command cmdInventory = new Command(){
			public void execute() {
				System.out.println("Inventory");
			}
		};
		
		slices.add(new PieSlice("Inventory").toggleActive().setCommand(cmdInventory));
		stage.addActor(slices.get(slices.size()-1).getLabel());
		slices.add(new PieSlice("Craft"));
		stage.addActor(slices.get(slices.size()-1).getLabel());
		slices.add(new PieSlice("Debug Mode"));
		stage.addActor(slices.get(slices.size()-1).getLabel());
		slices.add(new PieSlice("Call Frank"));
		stage.addActor(slices.get(slices.size()-1).getLabel());
		slices.add(new PieSlice("Call Heinzibert"));
		stage.addActor(slices.get(slices.size()-1).getLabel());
		
		degreeInterval = -360 / slices.size();
		localPos = new Vector2(0,RADIUS);
	}

	public void add(PieSlice slice){
		degreeInterval = -360 / slices.size();
		slices.add(slice);
		stage.addActor(slice.getLabel());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		//batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		
		for(int n = 0; n < slices.size(); n++){
			if(slices.get(n).isActive()){
				shapeRenderer.setColor(Color.GREEN);
			}else{
				shapeRenderer.setColor(Color.WHITE);
			}
			Label label = slices.get(n).getLabel();
			Vector2 localPosTxt = localPos.cpy().scl(2f);
			label.setPosition(getX() + localPosTxt.x, getY() + localPosTxt.y);
			label.setAlignment((localPos.x > 0) ? Align.left : Align.right);
			
			shapeRenderer.circle(getX()+localPos.x, getY()+localPos.y, 5);
			
			localPos.rotate(degreeInterval);
		}
		
		shapeRenderer.end();
		//batch.begin();
		super.draw(batch, parentAlpha);
	}
}
