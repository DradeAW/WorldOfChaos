package engine.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final public class Vector2f {

	/**
	 * Vector2f at position zero.
	 */
	final public static @NotNull Vector2f zero = new Vector2f(0, 0);

	/**
	 * X axis.
	 */
	final public static @NotNull Vector2f xAxis = new Vector2f(1, 0);

	/**
	 * Y axis.
	 */
	final public static @NotNull Vector2f yAxis = new Vector2f(0, 1);

	/**
	 * XY axis.
	 */
	final public static @NotNull Vector2f xyAxis = new Vector2f(1, 1);


	/**
	 * Vector2f's x position.
	 */
	private float x;

	/**
	 * Vector2f's y position.
	 */
	private float y;

	/**
	 * Creates a new Vector2f instance at position zero.
	 */
	public Vector2f() {
		this(Vector2f.zero);
	}

	/**
	 * Creates a new Vector2f instance at the same position as r.
	 *
	 * @param r Vector2f to copy.
	 */
	public Vector2f(final @NotNull Vector2f r) {
		this(r.getX(), r.getY());
	}

	/**
	 * Creates a new Vector2f instance at position (x ; y).
	 *
	 * @param x Vector2f's x position
	 * @param y Vector2f's y position
	 */
	public Vector2f(final float x, final float y) {
		this.set(x, y);
	}


	@Override
	public @NotNull String toString() {
		return "(" + this.getX() + " ; " + this.getY() + ")";
	}

	@Contract(value = "null -> false", pure = true)
	@Override
	public boolean equals(final Object obj) {
		if(!(obj instanceof Vector2f)) return false;

		final Vector2f r = (Vector2f) obj;
		return this.getX() == r.getX() && this.getY() == r.getY();
	}

	@Override
	public int hashCode() {
		float hash = 1;
		hash = hash * 13 + this.getX();
		hash = hash * 61 + this.getY();

		return (int) hash;
	}

	/**
	 * Returns the absolute position of the Vector2f.
	 *
	 * @return Absolute position of the Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f abs() {
		return new Vector2f(Math.abs(this.getX()), Math.abs(this.getY()));
	}

	/**
	 * Returns the Vector2f's length.
	 *
	 * @return Vector2f's length
	 */
	@Contract(pure = true)
	public float length() {
		return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
	}

	/**
	 * Returns a normalized version of the Vector2f.
	 *
	 * @return Normalized version of the Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f normalized() {
		final float length = this.length();

		return new Vector2f(this.getX() / length, this.getY() / length);
	}

	/**
	 * Normalizes the Vector2f.
	 */
	public void normalize() {
		final float length = this.length();

		this.set(this.getX() / length, this.getY() / length);
	}

	/**
	 * Rotates the Vector2f around (0 ; 0).
	 *
	 * @param a Angle to rotate counter-clockwise (in radians)
	 */
	public void rotate(final float a) {
		final double cos = Math.cos(a);
		final double sin = Math.sin(a);

		this.set((float)(this.getX() * cos - this.getY() * sin), (float)(this.getX() * sin + this.getY() * cos));
	}

	/**
	 * Rotates the Vector2f around the Vector2f passed.
	 *
	 * @param a Angle to rotate counter-clockwise (in radians)
	 * @param center Point around which the rotation will perform
	 */
	public void rotate(final float a, final @NotNull Vector2f center) {
		this.subtract(center);
		this.rotate(a);
		this.addition(center);
	}

	/**
	 * Returns a rotated version of the Vector2f around (0 ; 0).
	 *
	 * @param a Angle to rotate in reverse clockwise (in radians)
	 * @return Rotates version of the Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f rotated(final float a) {
		final Vector2f r = new Vector2f(this);

		r.rotate(a);

		return r;
	}

	/**
	 * Returns a rotated version of the Vector2f around Vector2f passed.
	 *
	 * @param a Angle to rotate in reverse clockwise (in radians)
	 * @param center Point around which the rotation will perform
	 * @return Rotated version of the Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f rotated(final float a, final @NotNull Vector2f center) {
		final Vector2f r = new Vector2f(this);

		r.rotate(a, center);

		return r;
	}

	/**
	 * Returns a Vector2f with the max x and y coordinates of both.
	 *
	 * @param r Vector2f to check with
	 * @return new Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f max(final @NotNull Vector2f r) {
		final Vector2f a = new Vector2f();

		a.setX(Math.max(this.getX(), r.getX()));
		a.setY(Math.max(this.getY(), r.getY()));

		return a;
	}

	/**
	 * Returns the distance between the Vector2f and r.
	 *
	 * @param r Vector2f to calculate with
	 * @return Distance between the Vector2f and r
	 */
	@Contract(pure = true)
	public float distanceTo(final @NotNull Vector2f r) {
		final Vector2f dist = this.sub(r);

		return dist.length();
	}

	/**
	 * Returns the dot product between the Vector2f and r.
	 *
	 * @param r Vector2f to dot with
	 * @return new float
	 */
	@Contract(pure = true)
	public float dot(final @NotNull Vector2f r) {
		return this.getX() * r.getX() + this.getY() * r.getY();
	}

	/**
	 * Returns the cross product between the Vector2f and r.
	 *
	 * @param r Vector2f to cross with
	 * @return Cross product between the Vector2f and r
	 */
	@Contract(pure = true)
	public float cross(final @NotNull Vector2f r) {
		return this.getX() * r.getY() - this.getY() * r.getX();
	}

	/**
	 * Returns the Vector2f's x position.
	 *
	 * @return Vector2f's x position
	 */
	@Contract(pure = true)
	public float getX() {
		return this.x;
	}

	/**
	 * Returns the Vector2f's y position.
	 *
	 * @return Vector2f's y position
	 */
	@Contract(pure = true)
	public float getY() {
		return this.y;
	}

	/**
	 * Sets the Vector2f x position.
	 *
	 * @param x X position to set
	 */
	public void setX(final float x) {
		this.x = x;
	}

	/**
	 * Sets the Vector2f's y position.
	 *
	 * @param y Y position to set
	 */
	public void setY(final float y) {
		this.y = y;
	}

	/**
	 * Sets the Vector2f's position to (x ; y).
	 * @param x X position to set
	 * @param y Y position to set
	 */
	public void set(final float x, final float y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * Adds (x ; y) to the Vector2f.
	 *
	 * @param x X amount to add
	 * @param y Y amount to add
	 */
	public void addition(final float x, final float y) {
		this.set(this.getX() + x, this.getY() + y);
	}

	/**
	 * Adds (r ; r) to the Vector2f.
	 *
	 * @param r Float to add
	 */
	public void addition(final float r) {
		this.addition(r, r);
	}

	/**
	 * Adds r to the Vector2f.
	 *
	 * @param r Vector2f to add
	 */
	public void addition(final @NotNull Vector2f r) {
		this.addition(r.getX(), r.getY());
	}

	/**
	 * Subtracts (x ; y) to the Vector2f.
	 *
	 * @param x X amount to subtract by
	 * @param y Y amount to subtract by
	 */
	public void subtract(final float x, final float y) {
		this.set(this.getX() - x, this.getY() - y);
	}

	/**
	 * Subtracts (r ; r) to the Vector2f.
	 *
	 * @param r Float to subtract by
	 */
	public void subtract(final float r) {
		this.subtract(r, r);
	}

	/**
	 * Subtracts r to the Vector2f.
	 *
	 * @param r Vector2f to subtract by
	 */
	public void subtract(final @NotNull Vector2f r) {
		this.subtract(r.getX(), r.getY());
	}

	/**
	 * multiplies (x ; y) to the Vector2f.
	 *
	 * @param x X amount to multiply by
	 * @param y Y amount to multiply by
	 */
	public void multiply(final float x, final float y) {
		this.set(this.getX() * x, this.getY() * y);
	}

	/**
	 * multiplies (r ; r) to the Vector2f.
	 *
	 * @param r Float to multiply by
	 */
	public void multiply(final float r) {
		this.multiply(r, r);
	}

	/**
	 * multiplies r to the Vector2f.
	 *
	 * @param r Vector2f to multiply by
	 */
	public void multiply(final @NotNull Vector2f r) {
		this.multiply(r.getX(), r.getY());
	}

	/**
	 * Returns the addition between the Vector2f and (x ; y).
	 *
	 * @param x X amount to add
	 * @param y Y amount to add
	 * @return Addition between the Vector2f and (x ; y)
	 */
	@Contract(pure = true)
	public @NotNull Vector2f add(final float x, final float y) {
		return new Vector2f(this.getX() + x, this.getY() + y);
	}

	/**
	 * Returns the addition between the Vector2f and r.
	 *
	 * @param r Float to add
	 * @return Addition between the Vector2f and r
	 */
	@Contract(pure = true)
	public @NotNull Vector2f add(final float r) {
		return this.add(r, r);
	}

	/**
	 * Returns the addition between the Vector2f and r.
	 *
	 * @param r Vector2f to add
	 * @return Addition between the Vector2f and r
	 */
	@Contract(pure = true)
	public @NotNull Vector2f add(final @NotNull Vector2f r) {
		return this.add(r.getX(), r.getY());
	}

	/**
	 * Returns the subtraction between the Vector2f and (x ; y).
	 *
	 * @param x X amount to subtract by
	 * @param y Y amount to subtract by
	 * @return Subtraction between the Vector2f and (x ; y)
	 */
	@Contract(pure = true)
	public @NotNull Vector2f sub(final float x, final float y) {
		return new Vector2f(this.getX() - x, this.getY() - y);
	}

	/**
	 * Returns the subtraction between the Vector2f and r.
	 *
	 * @param r Float to subtract by
	 * @return Subtraction between the Vector2f and r
	 */
	@Contract(pure = true)
	public @NotNull Vector2f sub(final float r) {
		return this.sub(r, r);
	}

	/**
	 * Returns the subtraction between the Vector2f and r.
	 *
	 * @param r Vector2f to subtract by
	 * @return Subtraction between the Vector2f and r
	 */
	@Contract(pure = true)
	public @NotNull Vector2f sub(final @NotNull Vector2f r) {
		return this.sub(r.getX(), r.getY());
	}

	/**
	 * Returns the multiplication between the Vector2f and (x ; y).
	 *
	 * @param x X amount to multiply by
	 * @param y Y amount to multiply by
	 * @return Multiplication between the Vector2f and (x ; y)
	 */
	@Contract(pure = true)
	public @NotNull Vector2f mul(final float x, final float y) {
		return new Vector2f(this.getX() * x, this.getY() * y);
	}

	/**
	 * Returns the multiplication between the Vector2f and r.
	 *
	 * @param r Float to multiply by
	 * @return Multiplication between the Vector2f and r
	 */
	@Contract(pure = true)
	public @NotNull Vector2f mul(final float r) {
		return this.mul(r, r);
	}

	/**
	 * Returns the multiplication between the Vector2f and r.
	 *
	 * @param r Vector2f to multiply by
	 * @return Multiplication between the Vector2f and r
	 */
	@Contract(pure = true)
	public @NotNull Vector2f mul(final @NotNull Vector2f r) {
		return this.mul(r.getX(), r.getY());
	}

}