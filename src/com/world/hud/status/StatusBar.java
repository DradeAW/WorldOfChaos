package com.world.hud.status;

import com.objects.characters.Hero;
import engine.game.components.RenderedComponent;
import engine.game.objects.GameObject;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class StatusBar extends GameObject {

	/**
	 * Bar's width.
	 */
	final public static float WIDTH = 0.08994709f;

	/**
	 * Bar's height.
	 */
	final public static float HEIGHT = 0.8968253968f;

	/**
	 * X position between 2 bars.
	 */
	final public static float X_BETWEEN_BARS = 0.1455026455f;

	/**
	 * Bar's background texture.
	 */
	final public static Material BAR_BACKGROUND = new Material(new Texture("hud/status/bar_background"));

	/**
	 * Pointer's to the hero.
	 */
	final private Hero hero;

	/**
	 * Bar's border.
	 */
	final private RenderedComponent barBorder;

	/**
	 * Is the status bar full at the moment (i.e. which border is currently begin used).
	 */
	private boolean isFull = false;

	/**
	 * Creates a new StatusBar instance.
	 *
	 * @param name Bar's name
	 * @param hero Hero to keep track of
	 */
	protected StatusBar(final @NotNull String name, final @NotNull Hero hero) {
		super("HUD status bar [" + name + "]", StatusBar.WIDTH, StatusBar.HEIGHT);

		this.hero = hero;

		this.addComponent(new RenderedComponent(StatusBar.BAR_BACKGROUND, StatusBar.WIDTH, StatusBar.HEIGHT));

		this.barBorder = new RenderedComponent(this.getNotFullBorderMaterial(), StatusBar.WIDTH, StatusBar.HEIGHT, true);
		this.addComponent(this.barBorder);
	}

	/**
	 * Returns the current value for the status bar.
	 *
	 * @return status
	 */
	public abstract float getStatus();

	/**
	 * Returns the maximum value for the status bar.
	 *
	 * @return max_status
	 */
	public abstract float getStatusMax();

	/**
	 * Returns the Material for the StatusBar border when it's full.
	 *
	 * @return full_status_border
	 */
	protected abstract Material getFullBorderMaterial();

	/**
	 * Returns the Material for the StatusBar border when it's not full.
	 *
	 * @return notfull_status_border
	 */
	protected abstract Material getNotFullBorderMaterial();

	@Override
	public void update(final double delta) {
		super.update(delta);

		final boolean isFull = this.getStatus() >= this.getStatusMax();

		if(this.isFull != isFull) {
			this.isFull = isFull;
			this.barBorder.setMaterial(isFull ? this.getFullBorderMaterial() : this.getNotFullBorderMaterial());
		}
	}

	/**
	 * Returns the pointer to the hero.
	 *
	 * @return StatusBar.hero
	 */
	@Contract(pure = true)
	final protected Hero getHero() {
		return this.hero;
	}

	/**
	 * Returns whether the current border being used is for a full bar of not.
	 *
	 * @return StatusBar.isFull
	 */
	final protected boolean isFull() {
		return this.isFull;
	}

}