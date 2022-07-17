package dev.devious.engine.ecs.components;

import dev.devious.engine.ecs.Component;
import dev.devious.engine.rendering.Model;

public class Renderable extends Component {
	private Model model;
	private float scale;

	public Renderable(Model model, float scale) {
		this.model = model;
		this.scale = scale;
	}

	public Model getModel() {
		return model;
	}

	public float getScale() {
		return scale;
	}
}
