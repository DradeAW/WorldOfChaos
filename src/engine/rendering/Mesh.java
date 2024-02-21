package engine.rendering;

import engine.math.Vector2f;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

final public class Mesh {

	/**
	 * Mesh's Vertex Buffer Object.
	 */
	final private int vbo;

	/**
	 * Mesh's Vertex Buffer Object for the triangle's indices.
	 */
	final private int vboi;

	/**
	 * Mesh's Vertex Array Object.
	 */
	final private int vao;

	/**
	 * Mesh's size.
	 */
	private int size;

	/**
	 * Creates a new Mesh instance.
	 */
	public Mesh() {
		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();
		this.vboi = glGenBuffers();
		this.size = 0;
	}

	@Override
	protected void finalize() {
		try {
			super.finalize();
		} catch(final Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		glDeleteBuffers(this.vboi);
		glDeleteBuffers(this.vbo);
		glDeleteVertexArrays(this.vao);
	}

	/**
	 * Creates a flipped float buffer containing the vertices data.
	 *
	 * @param vertices Vertices to use
	 * @return new FloatBuffer
	 */
	public @NotNull FloatBuffer createVerticesBuffer(final @NotNull Vertex[] vertices) {
		final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);

		for(final Vertex vertex : vertices) {
			verticesBuffer.put(vertex.getElements());
		}

		verticesBuffer.flip();
		return verticesBuffer;
	}

	/**
	 * Sets the Mesh's vertices.
	 *
	 * @param vertices Vertex[] to set
	 */
	public void setVertices(final @NotNull Vertex[] vertices) {
		this.size = vertices.length;

		glBindVertexArray(this.vao);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, this.createVerticesBuffer(vertices), GL_STATIC_DRAW);

		glVertexAttribPointer(0, 2, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 8);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);

		final byte[] indices = {
			0, 1, 2,
			2, 3, 0
		};
		final int indicesCount = indices.length;
		final ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vboi);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	/**
	 * Draws the Mesh on the screen.
	 */
	public void draw() {
		glBindVertexArray(this.vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vboi);

		//glDrawArrays(GL_QUADS, 0, this.size);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	/**
	 * Creates and returns a basic mesh width * height.
	 *
	 * @param width Mesh's width
	 * @param height Mesh's height
	 * @return new Mesh
	 */
	public static @NotNull Mesh create(final float width, final float height) {
		final Mesh mesh = new Mesh();

		final Vertex[] vertices = new Vertex[] {
			new Vertex(new Vector2f(0, height), new Vector2f(0, 0)),
			new Vertex(new Vector2f(0, 0), new Vector2f(0, 1)),
			new Vertex(new Vector2f(width, 0), new Vector2f(1, 1)),
			new Vertex(new Vector2f(width, height), new Vector2f(1, 0))
		};
		mesh.setVertices(vertices);

		return mesh;
	}

	/**
	 * Creates and returns a basic mesh width*height with an inverted y texture.
	 *
	 * @param width Mesh's width
	 * @param height Mesh's height
	 * @return new Mesh
	 */
	public static @NotNull Mesh createWithInvertedTexture(final float width, final float height) {
		final Mesh mesh = new Mesh();

		final Vertex[] vertices = new Vertex[] {
			new Vertex(new Vector2f(0, height), new Vector2f(0, 1)),
			new Vertex(new Vector2f(0, 0), new Vector2f(0, 0)),
			new Vertex(new Vector2f(width, 0), new Vector2f(1, 0)),
			new Vertex(new Vector2f(width, height), new Vector2f(1, 1))
		};
		mesh.setVertices(vertices);

		return mesh;
	}

}