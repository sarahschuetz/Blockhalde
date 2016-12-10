package com.blockhalde.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent extends Component {
	private Vector3 position;

	public PositionComponent(Vector3 position) {
		this.position = new Vector3(position);
	}

	public Vector3 getPosition() {
		return position;
	}
}
