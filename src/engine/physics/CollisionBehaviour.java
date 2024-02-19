package engine.physics;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Each PhysicsObject has a collision behaviour : what does it do?
 * This does not include everything (function onCollision()),
 * this is for basic behaviour (bounce off a wall or not?).
 */
public enum CollisionBehaviour { // TODO: Make better names than "object" and "bounce".

	/**
	 * Behaves like a basic object.
	 */
	OBJECT ("Object"),

	/**
	 * Bounces of wall without losing velocity.
	 */
	BOUNCE ("Bounce");

	/**
	 * Value's toString().
	 */
	final private @NotNull String name;

	/**
	 * Creates a new CollisionBehaviour.
	 *
	 * @param name Value's name
	 */
	CollisionBehaviour(final @NotNull String name) {
		this.name = name;
	}

	@Contract(pure = true)
	@Override
	public String toString() {
		return this.name;
	}

}