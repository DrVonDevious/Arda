package dev.devious.engine.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {
	private UUID id;
	private List<Component> components;

	public Entity(UUID id) {
		this.id = id;
		this.components = new ArrayList<>();
	}

	public UUID getId() {
		return id;
	}

	public void addComponent(Component component) {
		components.add(component);
	}

	public boolean hasComponent(Class<?> componentType) {
		for (Component component : components) {
			if (componentType.isInstance(component)) {
				return true;
			}
		}

		return false;
	}

	public Component getComponent(Class<?> componentType) {
		for (Component component : components) {
			if (componentType.isInstance(component)) {
				return component;
			}
		}
		return null;
	}
}
