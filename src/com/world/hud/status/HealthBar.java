package com.world.hud.status;

import com.objects.characters.Hero;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import org.jetbrains.annotations.NotNull;

public class HealthBar extends StatusBar {

	/**
	 * Texture of life bar's content.
	 */
	final public static Material BAR_CONTENT = new Material(new Texture("blank1x1"));

	/**
	 * Texture of life bar's border when full life.
	 */
	final public static Material BAR_BORDER_FULL = new Material(new Texture("hud/status/bar_border_life_full"));

	/**
	 * Texture of life bar's border when low life.
	 */
	final public static Material BAR_BORDER_NOTFULL = new Material(new Texture("hud/status/bar_border_life_notfull"));

	/**
	 * Creates a new HealthBar instance.
	 *
	 * @param hero Hero to keep track of.
	 */
	protected HealthBar(final @NotNull Hero hero) {
		super("life", HealthBar.BAR_CONTENT, hero);
	}

	@Override
	public float getStatus() {
		return 80.0f;
	}

	@Override
	public float getStatusMax() {
		return this.getHero().getAttribute().getTotalHealth();
	}

	@Override
	protected Material getFullBorderMaterial() {
		return HealthBar.BAR_BORDER_FULL;
	}

	@Override
	protected Material getNotFullBorderMaterial() {
		return HealthBar.BAR_BORDER_NOTFULL;
	}

}