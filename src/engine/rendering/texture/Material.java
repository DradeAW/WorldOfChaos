package engine.rendering.texture;

import engine.util.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final public class Material {

	/**
	 * Material's Image.
	 */
	private @NotNull Image image;

	/**
	 * Material's Color.
	 */
	private @NotNull Color color;

	/**
	 * Creates a new Material instance.
	 *
	 * @param image Image to set
	 */
	public Material(final @NotNull Image image) {
		this(image, new Color());
	}

	/**
	 * Creates a new Material instance.
	 *
	 * @param image Image to set
	 * @param color Color to set
	 */
	public Material(final @NotNull Image image, final @NotNull Color color) {
		this.set(image, color);
	}

	/**
	 * Updates the Material.
	 *
	 * @param delta Delta
	 */
	public void update(final double delta) {
		this.getImage().update(delta);
	}

	/**
	 * Returns the Material's image.
	 *
	 * @return Material.image
	 */
	@Contract(pure = true)
	public @NotNull Image getImage() {
		return this.image;
	}

	/**
	 * Returns the Material's texture.
	 *
	 * @return Material.image.texture
	 */
	public @NotNull Texture getTexture() {
		return this.getImage().getTexture();
	}

	/**
	 * Returns the Material's color.
	 *
	 * @return Material's color
	 */
	@Contract(pure = true)
	public @NotNull Color getColor() {
		return this.color;
	}

	/**
	 * Sets the Material's values.
	 *
	 * @param image Image to set
	 * @param color Color to set
	 */
	public void set(final Image image, final Color color) {
		this.setImage(image);
		this.setColor(color);
	}

	/**
	 * Sets te Material's texture.
	 *
	 * @param image Image to set
	 */
	public void setImage(final @NotNull Image image) {
		this.image = image;
	}

	/**
	 * Sets the Material's color.
	 *
	 * @param color Color to set
	 */
	public void setColor(final @NotNull Color color) {
		this.color = color;
	}

}