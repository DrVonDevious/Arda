package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;

public class GodMode extends Component {
	private boolean isActive;

	public GodMode(boolean activated) {
		this.isActive = activated;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}
}
