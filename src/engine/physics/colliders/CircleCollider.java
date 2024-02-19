package engine.physics.colliders;

import engine.math.Vector2f;
import engine.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CircleCollider extends Collider {

	/**
	 * Circle's center.
	 */
	private @NotNull Vector2f center;

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
	public CircleCollider(final @NotNull Vector2f center, final int radius) {
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
	public CircleCollider(final float x, final float y, final int radius) {
		this.center = new Vector2f(x, y);
		this.radius = radius;
	}

	@Contract(pure = true)
	@Override
	public @Nullable Vector2f intersect(final @NotNull Collider collider){
		return null;
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
	public float area() {
		return Math.round(Math.PI * this.getRadius() * this.getRadius());
	}

}