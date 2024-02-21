package engine.rendering;

import engine.math.Vector2f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final public class Vertex {

	/**
	 * One Vertex's size (how many floats are present).
	 */
	final public static int SIZE = 4;

	/**
	 * Vertex's position.
	 */
	private @NotNull Vector2f position;

	/**
	 * Vertex's texture coordinates.
	 */
	private @NotNull Vector2f textureCoords;

	/**
	 * Creates a new Vertex instance
	 * Texture coordinates will be set to (0; 0).
	 *
	 * @param position Position to set
	 */
	public Vertex(final @NotNull Vector2f position) {
		this(position, Vector2f.zero);
	}

	/**
	 * Creates a new Vertex instance.
	 *
	 * @param position Position to set
	 * @param textureCoords Texture coordinates to set
	 */
	public Vertex(final @NotNull Vector2f position, final @NotNull Vector2f textureCoords) {
		this.position = position;
		this.textureCoords = textureCoords;
	}

	/**
	 * Returns the Vertex's position.
	 *
	 * @return Vertex's position
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getPos() {
		return this.position;
	}

	/**
	 * Returns the Vertex's texture coordinates.
	 *
	 * @return Vertex's texture coordinates
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getTextureCoords() {
		return this.textureCoords;
	}

	/**
	 * Returns a float array containing all the vertex data.
	 *
	 * @return new float[4]
	 */
	@Contract(pure = true)
	public float[] getElements() {
		return new float[] {
			this.getPos().getX(),
			this.getPos().getY(),
			this.getTextureCoords().getX(),
			this.getTextureCoords().getY()
		};
	}

	/**
	 * Sets the Vertex's position
	 * Be careful! If you do not want the position to change, you should
	 * send a copy of the Vector2f so if it changes, the position won't.
	 *
	 * @param position Position to set
	 */
	public void setPos(final @NotNull Vector2f position) {
		this.position = position;
	}

	/**
	 * Sets the Vertex's texture coordinates
	 * Be careful! If you do not want the coordinates to change, you should
	 * send a copy of the Vector2f so if it changes, the coordinates won't.
	 *
	 * @param textureCoords Position to set
	 */
	public void setTextureCoords(final @NotNull Vector2f textureCoords) {
		this.textureCoords = textureCoords;
	}

	/**
	 * Sets the Vertex's values.
	 *
	 * @param position Position to set
	 * @param textureCoords Texture coordinates to set
	 */
	public void set(final @NotNull Vector2f position, final @NotNull Vector2f textureCoords) {
		this.setPos(position);
		this.setTextureCoords(textureCoords);
	}

}