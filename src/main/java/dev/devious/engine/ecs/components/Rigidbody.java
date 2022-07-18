package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;

public class Rigidbody extends Component {
	private boolean onGround = false;

	public Rigidbody() {

	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setIsOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}
