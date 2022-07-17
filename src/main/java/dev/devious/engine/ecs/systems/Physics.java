package dev.devious.engine.ecs.systems;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.System;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.Position;
import dev.devious.engine.ecs.components.Rotation;
import dev.devious.engine.ecs.components.Velocity;

import java.util.List;

public class Physics extends System {
	private final World world;

	public Physics(World world) {
		this.world = world;
	}

	@Override
	public void process() {
		List<Entity> entities = world.getEntities();
		for (Entity entity : entities) {
			if (entity.hasComponent(Position.class) && entity.hasComponent(Velocity.class)) {
				Position entityPosition = (Position) entity.getComponent(Position.class);
				Rotation entityRotation = (Rotation) entity.getComponent(Rotation.class);
				Velocity entityVelocity = (Velocity) entity.getComponent(Velocity.class);

				if (entityVelocity.getZ() != 0) {
					entityPosition.setX(entityPosition.getX() + (float) Math.sin(Math.toRadians(entityRotation.getY())) * -1 * entityVelocity.getZ());
					entityPosition.setZ(entityPosition.getZ() + (float) Math.cos(Math.toRadians(entityRotation.getY())) * entityVelocity.getZ());
				}

				if (entityVelocity.getX() != 0) {
					entityPosition.setX(entityPosition.getX() + (float) Math.sin(Math.toRadians(entityRotation.getY() - 90)) * -1 * entityVelocity.getX());
					entityPosition.setZ(entityPosition.getZ() + (float) Math.cos(Math.toRadians(entityRotation.getY() - 90)) * entityVelocity.getX());
				}

				entityPosition.setY(entityPosition.getY() + entityVelocity.getY());

				entityVelocity.reduceVelocity();
			}
		}
	}
}
