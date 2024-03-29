package engine.physics.colliders;

/*
  Axis-Aligned Bounding Box (AABB) collider.
  Does NOT support rotation.
 */

import engine.math.Vector2f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AABBCollider extends Collider {

    /**
     * AABBCollider's bottom-left corner.
     */
    final private @NotNull Vector2f position;

    /**
     * AABBCollider's width.
     */
    private float width;

    /**
     * AABBCollider's height.
     */
    private float height;

	/**
	 * Creates a new AABBCollider instance.
	 *
	 * @param position AABB position to set (bottom-left corner ; sends a copy and not the pointer)
	 * @param width AABB width
	 * @param height AABB height
	 */
	public AABBCollider(final @NotNull Vector2f position, final float width, final float height) {
		this.position = new Vector2f(position);
		this.width = width;
        this.height = height;
	}

	/**
	 * Creates a new AABBCollider instance.
	 *
	 * @param x AABB position on the x-axis
     * @param y AABB position on the y-axis
	 * @param width AABB width
	 * @param height AABB height
	 */
	public AABBCollider(final float x, final float y, final float width, final float height) {
		this(new Vector2f(x, y), width, height);
	}

	/**
	 * Creates a new AABBCollider instance.
	 */
	public AABBCollider() {
		this(new Vector2f(), 0, 0);
	}

	@Contract(pure = true)
	@Override
	public @Nullable Vector2f intersect(final @NotNull Collider collider) {
		if(collider instanceof CircleCollider) {
			final CircleCollider circleCollider = (CircleCollider) collider;
			final Vector2f normal = circleCollider.intersect(this);

			return normal == null ? null : normal.mul(-1);
		} else if(collider instanceof AABBCollider) {
			final AABBCollider aabbCollider = (AABBCollider) collider;

			final float xOverlap = Math.min(this.getMaxX(), aabbCollider.getMaxX()) - Math.max(this.getMinX(), aabbCollider.getMinX());
			final float yOverlap = Math.min(this.getMaxY(), aabbCollider.getMaxY()) - Math.max(this.getMinY(), aabbCollider.getMinY());

			if(xOverlap <= 0 || yOverlap <= 0) {
				return null;
			}

			if(xOverlap < yOverlap) {
				return new Vector2f(xOverlap * (this.getMinX() < aabbCollider.getMinX() ? 1 : -1), 0);
			} else {
				return new Vector2f(0, yOverlap * (this.getMinY() < aabbCollider.getMinY() ? 1 : -1));
			}
		} else {
			System.err.print("Error: Collision between AABBCollider and " + collider + " is not implemented yet.");
			new Exception().printStackTrace();
			return null;
		}
    }

	@Contract(pure = true)
	@Override
	public float[] projectOnAxis(final @NotNull Vector2f axis) {
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;

		for(final Vector2f vertex : this.getVertices()) {
			final float projection = vertex.projectOnAxis(axis);

			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}

		return new float[]{min, max};
	}

	/**
	 * Returns the AABBCollider's x position (left side).
	 *
	 * @return AABBCollider.position.x
	 */
	public float getMinX() {
		return this.position.getX();
	}

	/**
	 * Returns the AABBCollider's x position (right side).
	 *
	 * @return AABBCollider.position.x + AABBCollider.width
	 */
	public float getMaxX() {
		return this.position.getX() + this.width;
	}

	/**
	 * Returns the AABBCollider's y position (bottom side).
	 *
	 * @return AABBCollider.position.y
	 */
	public float getMinY() {
		return this.position.getY();
	}

	/**
	 * Returns the AABBCollider's y position (top side).
	 *
	 * @return AABBCollider.position.y + AABBCollider.height
	 */
	public float getMaxY() {
		return this.position.getY() + this.height;
	}

	/**
	 * Returns the AABBCollider's four vertices (i.e. corners).
	 *
	 * @return new Vector2f[4]
	 */
	public Vector2f[] getVertices() {
		return new Vector2f[]{
			new Vector2f(this.position),
			this.position.add(this.width, 0),
			this.position.add(this.width, this.height),
			this.position.add(0, this.height)
		};
	}

	/**
	 * Returns the AABBCollider's center.
	 *
	 * @return new Vector2f
	 */
	public Vector2f getCenter() {
		return this.position.add(this.width / 2, this.height / 2);
	}

	@Contract(pure = true)
	@Override
	public float area() {
		return this.width * this.height;
	}

	/**
	 * Sets the AABBCollider's bottom-left corner coordinates.
	 *
	 * @param position Position to set
	 */
	public void setPosition(final @NotNull Vector2f position) {
		this.position.set(position);
	}

	/**
	 * Sets the AABBCollider's width.
	 *
	 * @param width Width to set
	 */
	public void setWidth(final float width) {
		assert width >= 0 : "Error: AABBCollider.width must be positive or null.";
		this.width = width;
	}

	/**
	 * Sets the AABBCollider's height.
	 *
	 * @param height Height to set
	 */
	public void setHeight(final float height) {
		assert height >= 0 : "Error: AABBCollider.height must be positive or null.";
		this.height = height;
	}

}
