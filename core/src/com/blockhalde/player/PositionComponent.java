package com.blockhalde.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * An extension of the {@link Component} class which contains position attributes.
 * @author shaendro
 */
public class PositionComponent extends Component {
	private Vector3 position;

	/**
	 * Creates a {@link PositionComponent} with the given {@link Vector3} as its position.
	 * @param position The 
	 */
	public PositionComponent(Vector3 position) {
		this.position = new Vector3(position);
	}

	/**
	 * @return A {@link Vector3} which contains the position values.
	 */
	public Vector3 getPosition() {
		return position;
	}
}
