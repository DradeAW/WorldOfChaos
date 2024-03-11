package engine.physics.colliders;

import engine.math.Vector2f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CircleCollider extends Collider {

	/**
	 * Circle's center.
	 */
	final private @NotNull Vector2f center;

	/**
	 * Circle's radius.
	 */
	private float radius;

	/**
	 * Creates a new CircleCollider instance.
	 *
	 * @param center Circle's center to set (sends a copy and not the pointer)
	 * @param radius Circle's radius to set
	 */
	public CircleCollider(final @NotNull Vector2f center, final float radius) {
		this.center = new Vector2f(center);
		this.radius = radius;
	}

	/**
	 * Creates a new CircleCollider instance.
	 *
	 * @param x Circle's center x position
	 * @param y Circle's center y position
	 * @param radius Radius to set
	 */
	public CircleCollider(final float x, final float y, final float radius) {
		this.center = new Vector2f(x, y);
		this.radius = radius;
	}

	/**
	 * Creates a new CircleCollider instance.
	 */
	public CircleCollider() {
		this(new Vector2f(), 0);

	}

	@Contract(pure = true)
	@Override
	public @Nullable Vector2f intersect(final @NotNull Collider collider){
		if(collider instanceof CircleCollider circleCollider) {

            final float distance = this.getCenter().distanceTo(circleCollider.getCenter());
			final float radii = this.getRadius() + circleCollider.getRadius();

			if(distance >= radii) {
				return null;
			}

			final Vector2f normal = circleCollider.getCenter().sub(this.getCenter()).normalized();
			return normal.mul(radii - distance);
		} else if(collider instanceof AABBCollider aabbCollider) {

            final Vector2f[] axes = new Vector2f[] {
				new Vector2f(1, 0),
				new Vector2f(0, 1),
				this.getCenter().sub(this.getCenter().closestPoint(aabbCollider.getVertices()))  // TODO: Really needed??
			};

			final Vector2f normal = Collider.seperatingAxisTheorem(this, aabbCollider, axes);
			if(normal == null) return null;

			if(normal.dot(aabbCollider.getCenter().sub(this.getCenter())) < 0) {
				normal.multiply(-1);
			}
			return normal;
		} else {
			System.err.print("Error: Collision between CircleCollider and " + collider + " is not implemented yet.");
			new Exception().printStackTrace();
			return null;
		}
	}

	@Contract(pure = true)
	@Override
	public float[] projectOnAxis(final @NotNull Vector2f axis) {
		final Vector2f axisRadius = axis.normalized().mul(this.getRadius());

		final Vector2f p1 = this.getCenter().sub(axisRadius);
		final Vector2f p2 = this.getCenter().add(axisRadius);

		float min = p1.projectOnAxis(axis);
		float max = p2.projectOnAxis(axis);

		if(min > max) {
			final float temp = min;
			min = max;
			max = temp;
		}

		return new float[]{min, max};
	}

	/**
	 * Returns a copy of the Circle's center (and not the pointer).
	 *
	 * @return new Position(CircleCollider.center).
	 */
	@Contract(pure = true)
	final public @NotNull Vector2f getCenter() {
		return new Vector2f(this.center);
	}

	/**
	 * Returns the Circle's radius.
	 *
	 * @return CircleCollider.radius
	 */
	@Contract(pure = true)
	final public float getRadius() {
		return this.radius;
	}

	@Contract(pure = true)
	@Override
	public float area() {
		return Math.round(Math.PI * this.radius * this.radius);
	}

	/**
	 * Sets the Circle's center coordinates.
	 *
	 * @param center Center to set
	 */
	public void setCenter(final @NotNull Vector2f center) {
		this.center.set(center);
	}

	/**
	 * Sets the Circle's radius.
	 *
	 * @param radius Radius to set
	 */
	public void setRadius(final float radius) {
		assert radius >= 0.0f : "Error: CircleCollider.setRadius(" + radius + ") - radius must be positive.";
		this.radius = radius;
	}

}