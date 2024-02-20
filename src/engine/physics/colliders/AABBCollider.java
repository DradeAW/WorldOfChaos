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
    private @NotNull Vector2f position;

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

	@Contract(pure = true)
	@Override
	public @Nullable Vector2f intersect(final @NotNull Collider collider) {
		if(collider instanceof AABBCollider) {
			final AABBCollider aabbCollider = (AABBCollider) collider;

			final float xOverlap = Math.min(this.getMaxX(), aabbCollider.getMaxX()) - Math.max(this.getMinX(), aabbCollider.getMinX());
			final float yOverlap = Math.min(this.getMaxY(), aabbCollider.getMaxY()) - Math.max(this.getMinY(), aabbCollider.getMinY());

			if(xOverlap < 0 || yOverlap < 0) {
				return null;
			}

			if(xOverlap < yOverlap) {
				return new Vector2f(xOverlap * (this.getMinX() < aabbCollider.getMinX() ? 1 : -1), 0);
			} else {
				return new Vector2f(0, yOverlap * (this.getMinY() < aabbCollider.getMinY() ? 1 : -1));
			}
		}

        return null; // TODO.
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

	@Contract(pure = true)
	@Override
	public float area() {
		return this.width * this.height;
	}

}
