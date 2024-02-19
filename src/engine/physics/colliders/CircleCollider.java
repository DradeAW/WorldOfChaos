package engine.physics.colliders;

import engine.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CircleCollider extends Collider {

	/**
	 * Circle's center.
	 */
	private @NotNull Position center;

	/**
	 * Circle's radius.
	 */
	private int radius;

	/**
	 * Creates a new CircleCollider instance.
	 *
	 * @param center Circle's center to set (sends a copy and not the pointer)
	 * @param radius Circle's radius to set
	 */
	public CircleCollider(final @NotNull Position center, final int radius) {
		this.center = new Position(center);
		this.radius = radius;
	}

	/**
	 * Creates a new CircleCollider instance.
	 *
	 * @param x Circle's center x position
	 * @param y Circle's center y position
	 * @param radius Radius to set
	 */
	public CircleCollider(final long x, final long y, final int radius) {
		this.center = new Position(x, y);
		this.radius = radius;
	}

	@Contract(pure = true)
	@Override
	public @Nullable CollisionInfo intersect(final @NotNull Collider collider) {
		if(collider instanceof PointCollider || collider instanceof SegmentCollider || collider instanceof AABBCollider) {
			return collider.intersect(this);
		} else {
			System.err.println("Error: Collision between CircleCollider and " + collider + " isn't implemented yet.");
			return null;
		}
	}

	@Contract(pure = true)
	@Override
	public boolean contains(final @NotNull Position point) {
		return this.center.distanceToXY(point) < this.getRadius();
	}

	@Contract(pure = true)
	@Override
	public boolean on(final @NotNull Position point) {
		return Math.abs(this.center.distanceToXY(point) - this.getRadius()) < 1;
	}

	@Contract(pure = true)
	@Override
	public boolean containsOrOn(final @NotNull Position point) {
		return this.center.distanceToXY(point) < this.getRadius() + 1;
	}

	/**
	 * Returns a copy of the Circle's center (and not the pointer).
	 *
	 * @return new Position(CircleCollider.center).
	 */
	@Contract(pure = true)
	final public @NotNull Position getCenter() {
		return new Position(this.center);
	}

	/**
	 * Returns the Circle's radius.
	 *
	 * @return CircleCollider.radius
	 */
	@Contract(pure = true)
	final public int getRadius() {
		return this.radius;
	}

	@Contract(pure = true)
	@Override
	public int area() {
		return (int) Math.round(Math.PI * this.getRadius() * this.getRadius());
	}

}