package com.blockhalde.player;

import com.badlogic.ashley.core.Component;

public class DebugComponent extends Component {
	private boolean immortal = true;
	private boolean flying = true;

	public DebugComponent() {}

	public boolean isImmortal() {
		return immortal;
	}

	public void setImmortal(boolean immortal) {
		this.immortal = immortal;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean gravityOn) {
		this.flying = gravityOn;
	}
}
