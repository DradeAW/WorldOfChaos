package engine.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final public class Matrix4f{

	/**
	 * Matrix value.
	 */
	private float[][] m;

	/**
	 * Creates a new Matrix4f instance.
	 */
	public Matrix4f() {
		this.m = new float[4][4];
	}

	/**
	 * Creates a copy of a Matrix4f.
	 *
	 * @param m Value to set
	 */
	public Matrix4f(final @NotNull Matrix4f m) {
		this.setM(m.getM());
	}

	@Override
	public String toString() {
		final StringBuilder string = new StringBuilder(super.toString() + "\n");

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				string.append(this.get(j, i)).append(" ");
			}
			string.append("\n");
		}

		return string.toString();
	}

	/**
	 * Initializes the Matrix4f to the identity Matrix to have a clear Matrix4f.
	 *
	 * @return this
	 */
	public @NotNull Matrix4f initIdentity() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				this.set(i, j, i == j ? 1 : 0);
			}
		}

		return this;
	}

	/**
	 * Makes the Matrix4f to be to the position set.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 * @param z Z position to set
	 * @return this
	 */
	public @NotNull Matrix4f initPosition(final float x, final float y, final float z) {
		this.initIdentity();
		this.set(0, 3, x);
		this.set(1, 3, y);
		this.set(2, 3, z);

		return this;
	}

	/**
	 * Makes the Matrix4f to be to the position set.
	 *
	 * @param r Position to set
	 * @return this
	 */
	public @NotNull Matrix4f initPosition(final @NotNull Vector2f r) {
		return this.initPosition(r.getX(), r.getY(), 0);
	}

	/**
	 * Makes the Matrix4f to be to the rotation set.
	 *
	 * @param x Rotation around the x axis (in radians)
	 * @param y Rotation around the y axis (in radians)
	 * @param z Rotation around the z axis (in radians)
	 * @return this
	 */
	public @NotNull Matrix4f initRotation(final double x, final double y, final double z) {
		final Matrix4f rx = new Matrix4f().initIdentity();
		final Matrix4f ry = new Matrix4f().initIdentity();
		final Matrix4f rz = new Matrix4f().initIdentity();

		final float xCos = (float) Math.cos(x);
		final float xSin = (float) Math.sin(x);
		final float yCos = (float) Math.cos(y);
		final float ySin = (float) Math.sin(y);
		final float zCos = (float) Math.cos(z);
		final float zSin = (float) Math.sin(z);

		rx.set(0, 0, xCos);
		rx.set(0, 1, -xSin);
		rx.set(1, 0, xSin);
		rx.set(1, 1, xCos);
		ry.set(0, 0, yCos);
		ry.set(0, 1, -ySin);
		ry.set(1, 0, ySin);
		ry.set(1, 1, yCos);
		rz.set(0, 0, zCos);
		rz.set(0, 1, -zSin);
		rz.set(1, 0, zSin);
		rz.set(1, 1, zCos);

		this.m = rz.mul(ry.mul(rx)).getM();

		return this;
	}

	/**
	 * Makes the Matrix4f to be the scale set.
	 *
	 * @param x Scale for x
	 * @param y Scale for y
	 * @param z Scale for z
	 * @return this
	 */
	public @NotNull Matrix4f initScale(final float x, final float y, final float z) {
		this.initIdentity();
		this.set(0, 0, x);
		this.set(1, 1, y);
		this.set(2, 2, z);

		return this;
	}

	/**
	 * Makes the Matrix4f to be the scale set.
	 *
	 * @param r Scale to set
	 * @return this
	 */
	public @NotNull Matrix4f initScale(final @NotNull Vector2f r) {
		return this.initScale(r.getX(), r.getY(), 1);
	}

	/**
	 * Makes the Matrix4f to be the projection set.
	 *
	 * @param ratio Window's ratio (width / height)
	 * @return this
	 */
	public @NotNull Matrix4f initProjection(final float ratio) {
		this.initIdentity();

		final float zNear = 0.01f;
		final float zFar = 1000f;
		final float range = zNear - zFar;

		this.set(0, 0, 1.0f / ratio);
		this.set(2, 2, (-zNear - zFar) / range);
		this.set(2, 3, 2 * zFar * zNear / range);

		return this;
	}

	/**
	 * Returns the multiplication between the Matrix4f and r.
	 *
	 * @param r Matrix4f to multiply by
	 * @return multiplication between the Matrix4f and r
	 */
	@Contract(pure = true)
	public @NotNull Matrix4f mul(final @NotNull Matrix4f r) {
		final Matrix4f result = new Matrix4f();

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				result.set(i, j,
					this.get(i, 0) * r.get(0, j) +
					this.get(i, 1) * r.get(1, j) +
					this.get(i, 2) * r.get(2, j) +
					this.get(i, 3) * r.get(3, j)
				);
			}
		}

		return result;
	}

	/**
	 * Returns a copy of the Matrix4f's value.
	 *
	 * @return Matrix4f's value
	 */
	public float[][] getM() {
		final float[][] m = new float[4][4];

		for(int i = 0; i < 4; i++) {
			System.arraycopy(this.m[i], 0, m[i], 0, 4);
		}

		return m;
	}

	/**
	 * Returns the Matrix4f's value at [x][y].
	 *
	 * @param x Matrix4f's column
	 * @param y Matrix4f's row
	 * @return Matrix4f's value[x][y]
	 */
	@Contract(pure = true)
	public float get(final int x, final int y) {
		return this.m[x][y];
	}

	/**
	 * Sets the Matrix4f's value.
	 *
	 * @param m Value to set
	 * @throws IllegalArgumentException if m is not a float[4][4]
	 */
	public void setM(final float[][] m) {
		if(m.length != 4) {
			System.err.println("Error: The value to set in the Matrix4f must be a float[4][4]\nValue set: float[" + m.length + "][" + (m.length == 0 ? "null" : ("[" + m[0].length) + "]"));
			throw new IllegalArgumentException();
		} else {
			for(int i = 0; i < 4; i++) {
				if(m[i].length != 4) {
					System.err.println("Error: The value to set in the Matrix4f must be a float[4][4]\nfloat[" + i + "] has a length of " + m[i].length);
					throw new IllegalArgumentException();
				}
			}
		}

		this.m = m;
	}

	/**
	 * Sets the Matrix4f's value at [x][y].
	 *
	 * @param x Matrix4f's column
	 * @param y Matrix4f's row
	 * @param value Value to set
	 * @throws IllegalArgumentException if x or y are not between 0 and 3
	 */
	public void set(final int x, final int y, final float value) {
		if(x < 0 || x > 3 || y < 0 || y > 3) {
			System.err.println("Error: Matrix4f.set() must have an x and y value between 0 and 3. x=" + x + " & y=" + y);
			throw new IllegalArgumentException();
		}

		this.m[x][y] = value;
	}

}