package com.objects.characters;

import engine.physics.MovementsAllowed;
import engine.physics.PhysicsObject;
import engine.physics.colliders.AABBCollider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class Character extends PhysicsObject {

	/**
	 * Character's attribute.
	 */
	//final private @NotNull Attribute attribute;

	/**
	 * Character's level.
	 */
	private int level;

	/**
	 * Creates a new Character instance.
	 *
	 * @param name Character's name
	 * @param width Character's width
	 * @param height Character's height
	 * /@param attribute Character's attribute
	 */
	public Character(final @NotNull String name, final float width, final float height/*, final @NotNull Attribute attribute*/) {
		super(name, width, height, new AABBCollider(), MovementsAllowed.ONLY_WALK);

		//this.attribute = attribute;
	}

	/**
	 * Returns the Character's level.
	 *
	 * @return Character.lvl
	 */
	@Contract(pure = true)
	private int getLvl() {
		return this.level;
	}

	/**
	 * The Character gains a level.
	 */
	private void lvlUp() {
		this.level++;
	}

	/**
	 * Sets the Character's level.
	 *
	 * @param level Level to set
	 */
	private void setLvl(final int level) {
		this.level = level;
	}

}