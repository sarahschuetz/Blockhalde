package com.blockhalde.player;

import com.badlogic.ashley.core.Component;

/**
 * An extension of the {@link Component} class which contains {@link Player} debugging attributes such as immortality, ability to fly.
 * @author shaendro
 */
public class DebugComponent extends Component {
	private boolean immortal = false;
	private boolean flying = false;

	/**
	 * Creates the {@link DebugComponent} with inactive debugging attributes.
	 */
	public DebugComponent() {}

	/**
	 * @return A boolean describing immortality
	 */
	public boolean isImmortal() {
		return immortal;
	}

	/**
	 * @param immortal A boolean describing immortality
	 */
	public void setImmortal(boolean immortal) {
		this.immortal = immortal;
	}

	/**
	 * @return A boolean describing the ability to fly
	 */
	public boolean isFlying() {
		return flying;
	}

	/**
	 * @param flying A boolean describing the ability to fly
	 */
	public void setFlying(boolean flying) {
		this.flying = flying;
	}
}
