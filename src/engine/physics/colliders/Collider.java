package engine.physics.colliders;

/*
  Base class for all colliders.

  Uses the "Separating Axis Theorem" to check for collisions.
 */

import engine.math.Vector2f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class Collider {

	/**
	 * Returns the normal of the collision (with the length being the depth of the collision).
	 * Returns null if no collision was found.
	 *
	 * @param collider Collider to check with
	 * @return new Vector2f
	 */
	@Contract(pure = true)
	public abstract @Nullable Vector2f intersect(final @NotNull Collider collider);

	/**
	 * Used by the Separating Axis Theory to project the Collider on an axis.
	 * Returns the min and max values of the projection.
	 *
	 * @param axis Axis to project on (will be normalized internally)
	 * @return new float[2]
	 */
	@Contract(pure = true)
	public abstract float[] projectOnAxis(final @NotNull Vector2f axis);

	/**
	 * Returns the Collider's area.
	 *
	 * @return new int
	 */
	@Contract(pure = true)
	public abstract float area();

	/**
	 * Uses the Separating Axis Theorem to check for collisions.
	 * Given the axes to check, returns the normal of the collision (with the length being the depth of the collision).
	 * Returns null if no collision was found.
	 *
	 * @param collider1 The first Collider object
	 * @param collider2 The second Collider object
	 * @param axes The axes to check
	 * @return new Vector2f
	 */
	public static @Nullable Vector2f seperatingAxisTheorem(final @NotNull Collider collider1, final @NotNull Collider collider2, final @NotNull Vector2f[] axes) {
		float depth = Float.MAX_VALUE;
		Vector2f normal = null;

		for(final @NotNull Vector2f axis : axes) {
			final float[] projection1 = collider1.projectOnAxis(axis);
			final float[] projection2 = collider2.projectOnAxis(axis);

			if(projection1[0] >= projection2[1] || projection2[0] >= projection1[1]) return null;

			final float currentOverlap = Math.min(projection1[1], projection2[1]) - Math.max(projection1[0], projection2[0]);
			if(currentOverlap < depth) {
				depth = currentOverlap;
				normal = axis.normalized();
			}
		}

		if(normal == null) return null;

		return normal.mul(depth);

	}

}