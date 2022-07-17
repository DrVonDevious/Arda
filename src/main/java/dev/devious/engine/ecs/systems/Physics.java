package dev.devious.engine.ecs.systems;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.System;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.Position;
import dev.devious.engine.ecs.components.Velocity;
import org.joml.Vector3f;

import java.util.List;

public class Physics extends System {
	private World world;

	public Physics(World world) {
		this.world = world;
	}

	public void process() {
		List<Entity> entities = world.getEntities();
		for (Entity entity : entities) {
			if (entity.hasComponent(Position.class) && entity.hasComponent(Velocity.class)) {
				Position entityPosition = (Position) entity.getComponent(Position.class);
				Velocity entityVelocity = (Velocity) entity.getComponent(Velocity.class);
				entityPosition.setXYZ(new Vector3f(
					entityPosition.getX() + entityVelocity.getX(),
					entityPosition.getX() + entityVelocity.getX(),
					entityPosition.getX() + entityVelocity.getX()
				));
			}
		}
	}
}
