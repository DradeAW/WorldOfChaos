package engine.rendering.texture;

import org.jetbrains.annotations.NotNull;

abstract public class Image {

	/**
	 * Returns the Image's texture.
	 *
	 * @return Texture
	 */
	abstract @NotNull Texture getTexture();

	/**
	 * Updates the Image.
	 *
	 * @param delta Delta
	 */
	public void update(final double delta) {

	}

}