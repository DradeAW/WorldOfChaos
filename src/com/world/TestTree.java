package com.world;

import com.Options;
import engine.game.components.RenderedComponent;
import engine.math.Vector2f;
import engine.physics.CollisionBehaviour;
import engine.physics.MovementsAllowed;
import engine.physics.PhysicsObject;
import engine.physics.colliders.AABBCollider;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;

public class TestTree extends PhysicsObject {

	/**
	 * Tree's material.
	 */
	final public static Material MATERIAL = new Material(new Texture("test_tree"));

	/**
	 * Create a new TestTree instance.
	 */
	public TestTree() {
		super("Test tree", Options.TILE_SIZE, Options.TILE_SIZE * 2, MovementsAllowed.IMMOBILE);

		this.setPosition(new Vector2f(26 * Options.TILE_SIZE, 22 * Options.TILE_SIZE));
		this.setDepth(-0.3f);

		final RenderedComponent renderedComponent = new RenderedComponent(TestTree.MATERIAL, Options.TILE_SIZE, Options.TILE_SIZE * 2);
		this.addComponent(renderedComponent);
	}

	@Override
	public AABBCollider asCollider() {
		return this.asAABBCollider();
	}

	@Override
	protected float getPhysicsHeight() {
		return Options.TILE_SIZE / 2;
	}

}