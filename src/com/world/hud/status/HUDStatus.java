package com.world.hud.status;

import com.world.hud.HUD;
import com.world.hud.HUDCategory;
import engine.math.Vector2f;
import org.jetbrains.annotations.NotNull;

public class HUDStatus extends HUDCategory {

	/**
	 * Category' width.
	 */
	final public static float WIDTH = 0.57936508f;

	/**
	 * Category's height.
	 */
	final public static float HEIGHT = 0.57936508f;

	/**
	 * Category's position.
	 */
	final public static Vector2f POS = new Vector2f(0.029100529f, 0.632275132f);

	/**
	 * Creates a new HUDStatus instance.
	 *
	 * @param parentHUD Category's HUD parent
	 */
	public HUDStatus(final @NotNull HUD parentHUD) {
		super("Minimap", HUDStatus.WIDTH, HUDStatus.HEIGHT, parentHUD);

		this.setPosition(HUDStatus.POS);

		this.addChild(new HealthBar(this.getHero()));
		this.addChild(new ManaBar(this.getHero()));
	}

}