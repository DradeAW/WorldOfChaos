package engine.physics;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/*
 * What movements can the Physics Object perform ?
 * Walk, swim, immobile, fly ?
 */

public enum MovementsAllowed {

	/**
	 * The object can move at all.
	 */
	IMMOBILE ("Immobile"),

	/**
	 * The object flies (like a projectile). Can collide with trees.
	 */
	FLY ("Flying"),

	/**
	 * The object flies (like a dragon). Cannot collide.
	 */
	FLY_HIGH ("Flying high"),

	/**
	 * The object can only walk on the ground.
	 */
	ONLY_WALK ("Only walk"),

	/**
	 * The object can only swim in the water (not high water).
	 */
	ONLY_SWIM ("Only swim"),

	/**
	 * = ONLY_WALK + ONLY_SWIM.
	 */
	WALK_AND_SWIM ("Walk and Swim");

	/**
	 * Value's toString().
	 */
	final private @NotNull String name;

	/**
	 * Creates a new CollisionBehaviour.
	 *
	 * @param name Value's name
	 */
	MovementsAllowed(final @NotNull String name) {
		this.name = name;
	}

	@Contract(pure = true)
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Returns whether the movements allow movement (i.e. is it not immobile?).
	 *
	 * @return new boolean
	 */
	final public boolean canMove() {
		return this != IMMOBILE;
	}

	/**
	 * Returns whether the current movements allow walking/running.
	 * Note: it does not include flying.
	 *
	 * @return new boolean
	 */
	@Contract(pure = true)
	final public boolean canWalk() {
		switch(this) {
			case ONLY_WALK: return true;
			case WALK_AND_SWIM: return true;
		}

		return false;
	}

	/**
	 * Returns whether the current movements allow swimming.
	 * Note: it does not include flying.
	 *
	 * @return new boolean
	 */
	@Contract(pure = true)
	final public boolean canSwim() {
		switch(this) {
			case ONLY_SWIM: return true;
			case WALK_AND_SWIM: return true;
		}

		return false;
	}

	/**
	 * Returns whether the current movements allow flying.
	 *
	 * @return new boolean
	 */
	@Contract(pure = true)
	final public boolean canFly() {
		switch(this) {
			case FLY: return true;
			case FLY_HIGH: return true;
		}

		return false;
	}

}