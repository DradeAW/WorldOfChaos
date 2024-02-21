package engine.game.components.lights;

import engine.CoreEngine;
import engine.game.components.GameComponent;
import engine.rendering.shader.Shader;
import engine.util.Color;
import org.jetbrains.annotations.NotNull;

public class BaseLight extends GameComponent {

	/**
	 * BaseLight's color.
	 */
	private @NotNull Color color;

	/**
	 * BaseLight's intensity (between 0 and 1).
	 */
	private float intensity;

	/**
	 * BaseLight's shader to render with.
	 */
	private Shader shader;

	/**
	 * Creates a new BaseLight instance.
	 *
	 * @param color Color to set
	 * @param intensity Intensity to set
	 * @param shader Shader to set
	 */
	public BaseLight(final @NotNull Color color, final float intensity, final Shader shader) {
		this.setColor(color);
		this.setIntensity(intensity);
		this.setShader(shader);
	}

	@Override
	public void addToEngine(final @NotNull CoreEngine coreEngine) {
		coreEngine.getRenderingEngine().addLight(this);
	}

	/**
	 * Returns the BaseLight's color.
	 *
	 * @return BaseLight.color
	 */
	final public @NotNull Color getColor() {
		return this.color;
	}

	/**
	 * Returns the BaseLight's intensity.
	 *
	 * @return BaseLight.intensity
	 */
	final public float getIntensity() {
		return this.intensity;
	}

	/**
	 * Returns the light's Shader.
	 *
	 * @return BaseLight.shader
	 */
	final public Shader getShader() {
		return this.shader;
	}

	/**
	 * Sets the BaseLight's color.
	 *
	 * @param color Color to set
	 */
	private void setColor(final @NotNull Color color) {
		this.color = color;
	}

	/**
	 * Sets the BaseLight's intensity.
	 *
	 * @param intensity Intensity to set
	 */
	private void setIntensity(final float intensity) {
		this.intensity = intensity;
	}

	/**
	 * Sets the light's shader.
	 *
	 * @param shader Shader to set
	 */
	private void setShader(final Shader shader) {
		this.shader = shader;
	}

}