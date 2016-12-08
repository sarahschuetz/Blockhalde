package com.blockhalde.gui.pie;

import com.badlogic.gdx.scenes.scene2d.Actor;
import aurelienribon.tweenengine.TweenAccessor;

/**
 * {@link TweenAccessor} implementation for tweening {@link Actor} position
 */
public class ActorAccessor implements TweenAccessor<Actor> {
	
	public static final int X = 1;
	public static final int Y = 2;
	public static final int XY = 3;
	public static final int SCALEXY = 4;

	@Override
	public int getValues(Actor actor, int type, float[] returnValues) {
		switch (type) {
		case X:
			returnValues[0] = actor.getX();
			return 1;
		case Y:
			returnValues[0] = actor.getY();
			return 1;
		case XY:
			returnValues[0] = actor.getX();
			returnValues[1] = actor.getY();
			return 2;
		case SCALEXY:
			returnValues[0] = actor.getScaleX();
			returnValues[1] = actor.getScaleY();
		default:
			assert false;
			return 0;
		}
	}

	@Override
	public void setValues(Actor actor, int type, float[] newValues) {
		switch (type) {
		case X:
			actor.setX(newValues[0]);
			break;
		case Y:
			actor.setY(newValues[1]);
			break;
		case XY:
			actor.setPosition(newValues[0], newValues[1]);
			break;
		case SCALEXY:
			actor.setScale(newValues[0], newValues[1]);
			break;
		default:
			assert false;
			break;
		}
		
		
	}

}
