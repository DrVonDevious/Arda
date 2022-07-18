package dev.devious.engine.ecs.systems;

import dev.devious.engine.ecs.Entity;
import dev.devious.engine.ecs.System;
import dev.devious.engine.ecs.World;
import dev.devious.engine.ecs.components.*;
import dev.devious.engine.rendering.Terrain;

import java.util.List;

public class Physics extends System {
	private final World world;
	private final Terrain terrain;

	public Physics(World world, Terrain terrain) {
		this.world = world;
		this.terrain = terrain;
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

				float terrainHeight = terrain.getVertexHeight(entityPosition.getX(), entityPosition.getZ());

				if (entity.hasComponent(Rigidbody.class)) {
					Rigidbody rigidbody = (Rigidbody) entity.getComponent(Rigidbody.class);

					if (entityPosition.getY() < terrainHeight) {
						rigidbody.setIsOnGround(true);
						entityPosition.setY(terrainHeight);
					} else if (entityPosition.getY() > terrainHeight) {
						rigidbody.setIsOnGround(false);
					}

					if (rigidbody.isOnGround()) {
						entityVelocity.setY(0);
					} else {
						entityVelocity.setY(-1);
					}

					if (entity.hasComponent(GodMode.class)) {
						GodMode godMode = (GodMode) entity.getComponent(GodMode.class);
						if (godMode.isActive()) entityVelocity.setY(0);
					}
				}
			}
		}
	}
}
