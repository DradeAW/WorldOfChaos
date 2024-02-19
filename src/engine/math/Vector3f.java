package engine.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

final public class Vector3f {

	/**
	 * Vector3f's x position.
	 */
	private float x;

	/**
	 * Vector3f's y position.
	 */
	private float y;

	/**
	 * Vector3f's z position.
	 */
	private float z;

	/**
	 * Creates a new Vector3f instance.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 * @param z Z position to set
	 */
	public Vector3f(final float x, final float y, final float z) {
		this.set(x, y, z);
	}

	/**
	 * Creates a new Vector3f instance at the same position as r.
	 *
	 * @param r Vector3f to copy.
	 */
	public Vector3f(final @NotNull Vector3f r) {
		this(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Creates a new Vector3f instance at position (r.x ; r.y ; z)
	 *
	 * @param r X and Y position to set
	 * @param z Z position to set
	 */
	public Vector3f(final @NotNull Vector2f r, final float z) {
		this(r.getX(), r.getY(), z);
	}

	/**
	 * Creates a new Vector3f instance at position (0 ; 0 ; 0).
	 */
	public Vector3f() {
		this(0, 0, 0);
	}

	@Override
	public @NotNull String toString() {
		return "(" + this.getX() + " ; " + this.getY() + " ; " + this.getZ() + ")";
	}

	@Contract(value = "null -> false", pure = true)
	@Override
	public boolean equals(final Object obj) {
		if(!(obj instanceof Vector3f)) return false;

		final Vector3f r = (Vector3f) obj;
		return this.getX() == r.getX() && this.getY() == r.getY() && this.getZ() == r.getZ();
	}

	/**
	 * Returns the x and y coordinates as a Vector2f.
	 * Completely ignores the y value.
	 *
	 * @return new Vector2f
	 */
	public Vector2f asVector2f() {
		return new Vector2f(this.getX(), this.getY());
	}

	/**
	 * Returns the max value between x, y & z.
	 *
	 * @return new float
	 */
	@Contract(pure = true)
	public float maxValue() {
		float max = this.getY();
		if(this.getX() > this.getY()) {
			max = this.getX();
		}

		if(this.getZ() > max) {
			max = this.getZ();
		}

		return max;
	}

	/**
	 * Returns the Vector3f's x position.
	 *
	 * @return Vector3f's x position
	 */
	@Contract(pure = true)
	public float getX() {
		return this.x;
	}

	/**
	 * Returns the Vector3f's y position.
	 *
	 * @return Vector3f's y position
	 */
	@Contract(pure = true)
	public float getY() {
		return this.y;
	}

	/**
	 * Returns the Vector3f's z position.
	 *
	 * @return Vector3f's z position
	 */
	@Contract(pure = true)
	public float getZ() {
		return this.z;
	}

	/**
	 * Returns the Vector3f containing (x ; y).
	 *
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getXY() {
		return new Vector2f(this.getX(), this.getY());
	}

	/**
	 * Returns the Vector3f containing (x ; z).
	 *
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getXZ() {
		return new Vector2f(this.getX(), this.getZ());
	}

	/**
	 * Returns the Vector3f containing (y ; z).
	 *
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getYZ() {
		return new Vector2f(this.getY(), this.getZ());
	}

	/**
	 * Sets the Vector3f's position.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 * @param z Z position to set
	 */
	public void set(final float x, final float y, final float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	/**
	 * Sets the Vector3f's position to the same coordinates as r.
	 *
	 * @param r Coordinates to set
	 */
	public void set(final @NotNull Vector3f r) {
		this.set(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Sets the Vector3f's x position.
	 *
	 * @param x X position to set
	 */
	public void setX(final float x) {
		this.x = x;
	}

	/**
	 * Sets the Vector3f's y position.
	 *
	 * @param y Y position to set
	 */
	public void setY(final float y) {
		this.y = y;
	}

	/**
	 * Sets the Vector3f's z position.
	 *
	 * @param z Z position to set
	 */
	public void setZ(final float z) {
		this.z = z;
	}

	/**
	 * Sets the Vector3f's x & y position.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 */
	public void setXY(final float x, final float y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * Sets the Vector3f's x & y position.
	 *
	 * @param r Vector2f to set
	 */
	public void setXY(final @NotNull Vector2f r) {
		this.setXY(r.getX(), r.getY());
	}

	/**
	 * Sets the Vector3f's x & z position.
	 *
	 * @param x X position to set
	 * @param z Z position to set
	 */
	public void setXZ(final float x, final float z) {
		this.setX(x);
		this.setZ(z);
	}

	/**
	 * Sets the Vector3f's x & z position.
	 *
	 * @param r Vector2f to set
	 */
	public void setXZ(final @NotNull Vector2f r) {
		this.setXZ(r.getX(), r.getY());
	}

	/**
	 * Sets the Vector3f's y & z position.
	 *
	 * @param y Y position to set
	 * @param z Z position to set
	 */
	public void setYZ(final float y, final float z) {
		this.setY(y);
		this.setZ(z);
	}

	/**
	 * Sets the Vector3f's y & z position.
	 *
	 * @param r Vector2f to set
	 */
	public void setYZ(final @NotNull Vector2f r) {
		this.setYZ(r.getX(), r.getY());
	}

	/**
	 * Adds (x ; y ; z) to the Vector3f.
	 *
	 * @param x X amount to add
	 * @param y Y amount to add
	 * @param z Z amount to add
	 */
	public void addition(final float x, final float y, final float z) {
		this.set(this.getX() + x, this.getY() + y, this.getZ() + z);
	}

	/**
	 * Adds (r ; r ; r) to the Vector3f.
	 *
	 * @param r Float to add
	 */
	public void addition(final float r) {
		this.addition(r, r, r);
	}

	/**
	 * Adds r to the Vector3f.
	 *
	 * @param r Vector3f to add
	 */
	public void addition(final @NotNull Vector3f r) {
		this.addition(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Subtracts (x ; y ; z) to the Vector3f.
	 *
	 * @param x X amount to subtract
	 * @param y Y amount to subtract
	 * @param z Z amount to subtract
	 */
	public void subtract(final float x, final float y, final float z) {
		this.set(this.getX() - x, this.getY() - y, this.getZ() - z);
	}

	/**
	 * Subtracts (r ; r ; r) to the Vector3f.
	 *
	 * @param r Float to subtract
	 */
	public void subtract(final float r) {
		this.subtract(r, r, r);
	}

	/**
	 * Subtracts r to the Vector3f.
	 *
	 * @param r Vector3f to subtract
	 */
	public void subtract(final @NotNull Vector3f r) {
		this.subtract(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Multiplies (x ; y ; z) to the Vector3f.
	 *
	 * @param x X amount to multiply
	 * @param y Y amount to multiply
	 * @param z Z amount to multiply
	 */
	public void multiply(final float x, final float y, final float z) {
		this.set(this.getX() * x, this.getY() * y, this.getZ() * z);
	}

	/**
	 * Multiplies (r ; r ; r) to the Vector3f.
	 *
	 * @param r Float to multiply
	 */
	public void multiply(final float r) {
		this.multiply(r, r, r);
	}

	/**
	 * Multiplies r to the Vector3f.
	 *
	 * @param r Vector3f to multiply
	 */
	public void multiply(final @NotNull Vector3f r) {
		this.multiply(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Returns the addition between the Vector3f and (x ; y ; z).
	 *
	 * @param x X amount to add
	 * @param y Y amount to add
	 * @param z Z amount to add
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f add(final float x, final float y, final float z) {
		return new Vector3f(this.getX() + x, this.getY() + y, this.getZ() + z);
	}

	/**
	 * Returns the addition between the Vector3f and r.
	 *
	 * @param r Float to add
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f add(final float r) {
		return this.add(r, r, r);
	}

	/**
	 * Returns the addition between the Vector3f and r.
	 *
	 * @param r Vector3f to add
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f add(final @NotNull Vector3f r) {
		return this.add(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Returns the subtraction between the Vector3f and (x ; y ; z).
	 *
	 * @param x X amount to subtract
	 * @param y Y amount to subtract
	 * @param z Z amount to subtract
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f sub(final float x, final float y, final float z) {
		return new Vector3f(this.getX() - x, this.getY() - y, this.getZ() - z);
	}

	/**
	 * Returns the subtraction between the Vector3f and r.
	 *
	 * @param r Float to subtract
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f sub(final float r) {
		return this.sub(r, r, r);
	}

	/**
	 * Returns the subtraction between the Vector3f and r.
	 *
	 * @param r Vector3f to subtract
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f sub(final @NotNull Vector3f r) {
		return this.sub(r.getX(), r.getY(), r.getZ());
	}

	/**
	 * Returns the multiplication between the Vector3f and (x ; y ; z).
	 *
	 * @param x X amount to multiply
	 * @param y Y amount to multiply
	 * @param z Z amount to multiply
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f mul(final float x, final float y, final float z) {
		return new Vector3f(this.getX() * x, this.getY() * y, this.getZ() * z);
	}

	/**
	 * Returns the multiplication between the Vector3f and r.
	 *
	 * @param r Float to multiply
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f mul(final float r) {
		return this.mul(r, r, r);
	}

	/**
	 * Returns the multiplication between the Vector3f and r.
	 *
	 * @param r Vector3f to multiply
	 * @return new Vector3f
	 */
	@Contract(pure = true)
	public @NotNull Vector3f mul(final @NotNull Vector3f r) {
		return this.mul(r.getX(), r.getY(), r.getZ());
	}

}