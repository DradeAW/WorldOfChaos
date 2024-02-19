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
	public @Nullable Vector2f intersect(final @NotNull Collider collider){
        return null; // TODO.
    }

	@Contract(pure = true)
	@Override
	public float area() {
		return this.width * this.height;
	}

}
