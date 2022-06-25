package com.world.hud.status;

import com.objects.characters.Hero;
import engine.game.components.RenderedComponent;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import org.jetbrains.annotations.NotNull;

public class LifeBar extends StatusBar {

	/**
	 * Texture of life bar's border when full life.
	 */
	final public static Material BAR_BORDER_FULL = new Material(new Texture("hud/status/bar_border_life_full"));

	/**
	 * Texture of life bar's border when low life.
	 */
	final public static Material BAR_BORDER_NOTFULL = new Material(new Texture("hud/status/bar_border_life_notfull"));

	/**
	 * Creates a new LifeBar instance.
	 *
	 * @param hero Hero to keep track of.
	 */
	protected LifeBar(final @NotNull Hero hero) {
		super("life", hero);
	}

	@Override
	public float getStatus() {
		return 150.0f;
	}

	@Override
	public float getStatusMax() {
		return 200.0f;
	}

	@Override
	protected Material getFullBorderMaterial() {
		return LifeBar.BAR_BORDER_FULL;
	}

	@Override
	protected Material getNotFullBorderMaterial() {
		return LifeBar.BAR_BORDER_NOTFULL;
	}

}