package com.world.hud.status;

import com.MainComponent;
import com.objects.characters.Hero;
import engine.game.components.RenderedComponent;
import engine.math.Vector2f;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import engine.util.Time;
import org.jetbrains.annotations.NotNull;

public class ManaBar extends StatusBar {

	/**
	 * Texture of mana bar's content.
	 */
	final public static Material BAR_CONTENT = new Material(new Texture("blank1x1"));

	/**
	 * Texture of mana bar's border when full mana.
	 */
	final public static Material BAR_BORDER_FULL = new Material(new Texture("hud/status/bar_border_mana_full"));

	/**
	 * Texture of mana bar's border when low mana.
	 */
	final public static Material BAR_BORDER_NOTFULL = new Material(new Texture("hud/status/bar_border_mana_notfull"));

	/**
	 * Creates a new ManaBar instance.
	 *
	 * @param hero Hero to keep track of.
	 */
	protected ManaBar(final @NotNull Hero hero) {
		super("mana", ManaBar.BAR_CONTENT, hero);

		this.setPosition(new Vector2f(StatusBar.X_BETWEEN_BARS, 0));
	}

	@Override
	public float getStatus() {
		final long elapsedTime = Time.getNanoTime() - MainComponent.APPLICATION_START;
		final float mana = elapsedTime * this.getStatusMax() / 30e9f;

		return mana > this.getStatusMax() ? this.getStatusMax() : mana;
	}

	@Override
	public float getStatusMax() {
		return 200.0f;
	}

	@Override
	protected Material getFullBorderMaterial() {
		return ManaBar.BAR_BORDER_FULL;
	}

	@Override
	protected Material getNotFullBorderMaterial() {
		return ManaBar.BAR_BORDER_NOTFULL;
	}

}