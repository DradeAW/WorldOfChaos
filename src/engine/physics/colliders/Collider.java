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
	 * @param axis Axis to project on (must be normalized!!)
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

}