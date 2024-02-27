package com.world;

import com.Game;
import com.objects.characters.Hero;
import com.world.hud.HUD;
import com.world.map.MainMap;
import engine.game.objects.GameObject;
import engine.game.objects.map.Map;
import engine.math.Vector2f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class WorldObject extends GameObject {

	/**
	 * WorldObject's depth.
	 */
	final public static float DEPTH = 0.5f;

	/**
	 * Reference to the parent's game class.
	 */
	final private @NotNull Game game;

	/**
	 * World's map.
	 */
	private Map map;

	/**
	 * World's hero.
	 */
	final private @NotNull Hero hero;

	/**
	 * World's HUD.
	 */
	final private @NotNull HUD hud;

	/**
	 * Create a new WorldObject instance.
	 *
	 * @param game Parent's game
	 */
	public WorldObject(final @NotNull Game game) {
		super("Menu Object", Float.MAX_VALUE, Float.MAX_VALUE);

		this.game = game;
		this.hero = new Hero("Dradeliomecus");
		this.hud = new HUD(this.getHero());
		this.setPosition(new Vector2f(0.0f, 0.0f));
		this.setDepth(WorldObject.DEPTH);
	}

	@Override
	final public @NotNull WorldObject init() {
		this.getHero().init();
		this.addChild(this.getHero());
		this.addChild(this.hud);

		this.getGame().getRenderingEngine().setMainCamera(this.getHero().getCamera());

		this.map = new MainMap();
		this.addChild(this.map);
		this.map.init();
		Map.setInstance(this.map);

		this.addChild(new TestTree().init());
		this.addChild(new TestFireBall().init());

		return this;
	}

	/**
	 * Returns a reference to the parent's game.
	 *
	 * @return WorldObject.game
	 */
	@Contract(pure = true)
	final public @NotNull Game getGame() {
		return this.game;
	}

	/**
	 * Returns the Hero.
	 *
	 * @return WorldObject.hero
	 */
	@Contract(pure = true)
	private @NotNull Hero getHero() {
		return this.hero;
	}

}