package com;

import com.menu.MenuObject;
import com.world.WorldObject;
import engine.game.CoreGame;
import engine.game.objects.text.FontLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final public class Game extends CoreGame {

	/**
	 * Doesn't load anything on the next frame.
	 */
	final public static byte LOAD_NONE = 0;

	/**
	 * Loads the menu on the next frame.
	 */
	final public static byte LOAD_MENU = 1;

	/**
	 * Loads the world on the next frame.
	 */
	final public static byte LOAD_WORLD = 2;

	/**
	 * Game's menu object.
	 */
	private MenuObject menuObject;

	/**
	 * Game's world object.
	 */
	private WorldObject worldObject;

	/**
	 * What do load on the next frame (to avoid ConcurrentModificationException).
	 */
	private byte load;

	@Override
	public @NotNull Game init() {
		FontLoader.loadAll();

		this.load = Game.LOAD_NONE;

		this.menuObject = new MenuObject(this);
		this.addToRootObject(this.getMenuObject());
		this.getMenuObject().init();

		return this;
	}

	@Override
	public void update(final double delta) {
		if(this.load == Game.LOAD_MENU) {
			if(this.getWorldObject() != null) {
				this.removeFromRootObject(this.getWorldObject());
				this.worldObject = null;
			}

			this.menuObject = new MenuObject(this);
			this.addToRootObject(this.getMenuObject());
			this.getMenuObject().init();
		} else if(this.load == Game.LOAD_WORLD) {
			if(this.getMenuObject() != null) {
				this.removeFromRootObject(this.getMenuObject());
				this.menuObject.destroy();
				this.menuObject = null;
			}

			this.worldObject = new WorldObject(this);
			this.addToRootObject(this.getWorldObject());
			this.getWorldObject().init();
		}

		this.load = Game.LOAD_NONE;

		super.update(delta);
	}

	/**
	 * Loads the menu.
	 */
	public void loadMenu() {
		this.load = Game.LOAD_MENU;
	}

	/**
	 * Loads the world.
	 */
	public void loadWorld() {
		this.load = Game.LOAD_WORLD;
	}

	/**
	 * Returns the Game's menu object.
	 *
	 * @return Game.menuObject
	 */
	@Contract(pure = true)
	private MenuObject getMenuObject() {
		return this.menuObject;
	}

	/**
	 * Returns the Game's world object.
	 *
	 * @return Game.worldObject
	 */
	@Contract(pure = true)
	private WorldObject getWorldObject() {
		return this.worldObject;
	}

}