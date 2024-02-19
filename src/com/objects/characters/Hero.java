package com.objects.characters;

import com.GameOptions;
import com.Options;
import engine.game.components.Camera;
import engine.game.components.RenderedComponent;
import engine.math.Vector2f;
import engine.rendering.texture.Animation;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import engine.util.Direction;
import engine.util.Input;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Hero extends Character {

	/**
	 * The Hero's instance.
	 */
	private static Hero instance;

	/**
	 * Hero's Textures.
	 */
	private static Material[] materials = new Material[] {
		new Material(new Texture("temp_hero/hero_NORTH_0")),
		new Material(new Animation(new Texture[] {new Texture("temp_hero/hero_NORTH_1"), new Texture("temp_hero/hero_NORTH_2")}, 1.0)),
		new Material(new Texture("temp_hero/hero_EAST_0")),
		new Material(new Animation(new Texture[] {new Texture("temp_hero/hero_EAST_1"), new Texture("temp_hero/hero_EAST_2")}, 1.0)),
		new Material(new Texture("temp_hero/hero_SOUTH_0")),
		new Material(new Animation(new Texture[] {new Texture("temp_hero/hero_SOUTH_1"), new Texture("temp_hero/hero_SOUTH_2")}, 1.0)),
		new Material(new Texture("temp_hero/hero_WEST_0")),
		new Material(new Animation(new Texture[] {new Texture("temp_hero/hero_WEST_1"), new Texture("temp_hero/hero_WEST_2")}, 1.0)),
	};

	/**
	 * Hero's camera.
	 */
	final private Camera camera;

	/**
	 * Hero's rendered component.
	 */
	final private @NotNull RenderedComponent renderedComponent;

	/**
	 * Hero's width.
	 */
	final public static float SIZE = 0.9f * Options.TILE_SIZE;

	/**
	 * Hero's speed.
	 */
	final public static float SPEED = 4.0f;

	/**
	 * Hero's run multiplicator.
	 */
	final public static float RUN_MULTIPLICATOR = 1.5f;

	/**
	 * Creates a new Hero instance.
	 *
	 * @param name Hero's name
	 */
	public Hero(final @NotNull String name) {
		super(name, Hero.SIZE, Hero.SIZE);

		if(Hero.instance != null) {
			System.err.println("Error: Hero constructor is called when a Hero already exists.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		Hero.instance = this;

		this.camera = new Camera();
		this.renderedComponent = new RenderedComponent(Hero.materials[4], Hero.SIZE, Hero.SIZE);

		this.setPosition(new Vector2f(20, 15).mul(Options.TILE_SIZE)); // TODO: Give real position.
		this.setDepth(-0.2f);
		this.addComponent(this.camera);
		this.addComponent(this.renderedComponent);
	}

	@Override
	public Hero init() {
		this.getCamera().init();

		return this;
	}

	@Override
	final public void update(final double delta) {
		final Vector2f velocity = new Vector2f(0, 0);

		// First we set the direction, then we normalize the vector and multiply by boosts.
		if(Input.getKey(GameOptions.getKey("moveNorth").get())) velocity.addition(0, 1.0f);
		if(Input.getKey(GameOptions.getKey("moveSouth").get())) velocity.addition(0, -1.0f);
		if(Input.getKey(GameOptions.getKey("moveEast").get())) velocity.addition(1.0f, 0);
		if(Input.getKey(GameOptions.getKey("moveWest").get())) velocity.addition(-1.0f, 0);
		if(velocity.length() > 0.001f) velocity.normalize(); // Normalize if velocity isn't nul.
		if(Input.getKey(GameOptions.getKey("sprint").get())) {
			velocity.multiply(Hero.RUN_MULTIPLICATOR);

			for(int i = 0; i < 4; i++) {
				((Animation) Hero.materials[2*i+1].getImage()).setMaxTime(0.7f); // TODO: There must be a better way to do this (right now, checks and sets every frame).
			}
		} else {
			for(int i = 0; i < 4; i++) {
				((Animation) Hero.materials[2*i+1].getImage()).setMaxTime(1.0f);
			}
		}
		velocity.multiply(Hero.SPEED);
		// TODO: Double key to sprint

		if(!velocity.equals(this.getLinearVelocity())) {
			this.setLinearVelocity(velocity);
			this.refreshTexture();
		} else {
			this.setLinearVelocity(velocity);
		}

		super.update(delta);
	}

	@Override
	public AABBCollider asCollider() {
		return this.asAABBCollider();
	}

	@Override
	protected void refreshTexture() {
		final byte direction = this.getDirection().getDirection();

		if(direction == Direction.NORTH || direction == Direction.NORTH_EAST || direction == Direction.NORTH_WEST) {
			this.renderedComponent.setMaterial(Hero.materials[this.isMoving() ? 1 : 0]);
		} else if(direction == Direction.SOUTH || direction == Direction.SOUTH_EAST || direction == Direction.SOUTH_WEST || direction == Direction.NONE) {
			this.renderedComponent.setMaterial(Hero.materials[this.isMoving() ? 5 : 4]);
		} else if(direction == Direction.EAST) {
			this.renderedComponent.setMaterial(Hero.materials[this.isMoving() ? 3 : 2]);
		} else if(direction == Direction.WEST) {
			this.renderedComponent.setMaterial(Hero.materials[this.isMoving() ? 7 : 6]);
		} else {
			System.err.println("Direction unknown!");
			new Exception().printStackTrace();
		}
	}

	/**
	 * Returns the Hero's camera.
	 *
	 * @return Hero.camera
	 */
	@Contract(pure = true)
	final public Camera getCamera() {
		return this.camera;
	}

	/**
	 * Returns the hero.
	 *
	 * @return Hero::instance
	 */
	@Contract(pure = true)
	public static Hero getInstance() {
		return Hero.instance;
	}

}