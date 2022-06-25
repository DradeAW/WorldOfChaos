package engine.game.components;

import engine.CoreEngine;
import engine.game.objects.GameObject;
import engine.game.Transform;
import engine.rendering.shader.Shader;
import engine.util.GameInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

abstract public class GameComponent implements GameInterface {

	/**
	 * GameComponent's parent.
	 */
	private GameObject parent;

	/**
	 * GameComponent's width.
	 */
	private float width;

	/**
	 * GameComponent's height.
	 */
	private float height;

	/**
	 * Pointer to the GameObject's Transform.
	 */
	private Transform transform;

	/**
	 * Creates a new GameComponent instance.
	 */
	public GameComponent() {
		this(0, 0);
	}

	/**
	 * Creates a new GameComponent instance.
	 *
	 * @param width GameComponent's width
	 * @param height GameComponent's height
	 */
	public GameComponent(final float width, final float height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	public @NotNull GameComponent init() {
		return this;
	}

	/**
	 * Returns the GameComponent's parent.
	 *
	 * @return GameComponent's parent
	 */
	@Contract(pure = true)
	final protected GameObject getParent() {
		return this.parent;
	}

	/**
	 * Returns the GameComponent's Transform.
	 *
	 * @return GameComponent.transform
	 */
	@Contract(pure = true)
	final protected Transform getTransform() {
		return this.transform;
	}

	/**
	 * Returns the GameComponent's width.
	 *
	 * @return GameComponent.width
	 */
	@Contract(pure = true)
	final public float getWidth() {
		return this.width;
	}

	/**
	 * Returns the GameComponent's height.
	 *
	 * @return GameComponent.height
	 */
	@Contract(pure = true)
	final public float getHeight() {
		return this.height;
	}

	/**
	 * Sets the GameComponent's width.
	 *
	 * @param width Component's width to set
	 */
	final public void setWidth(final float width) {
		this.width = width;
	}

	/**
	 * Sets the GameComponent's height.
	 *
	 * @param height Component's height to set
	 */
	final public void setHeight(final float height) {
		this.height = height;
	}

	/**
	 * Sets the GameComponent's parent.
	 *
	 * @param parent Parent to set
	 * @param parentTransform Parent's transform
	 */
	final public void setParent(final @NotNull GameObject parent, final @NotNull Transform parentTransform) {
		this.parent = parent;
		this.transform = parentTransform;
	}

	@Override
	public void render(final @NotNull Shader shader, final @Nullable ArrayList<GameComponent> renderLater) {

	}

	@Override
	public void update(final double delta) {

	}

	@Override
	public void input() {

	}

	/**
	 * Adds everything to the core engine.
	 *
	 * @param coreEngine Core engine to set
	 */
	public void addToEngine(final @NotNull CoreEngine coreEngine) {

	}

}