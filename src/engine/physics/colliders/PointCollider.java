package engine.physics.colliders;

import engine.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PointCollider extends Collider {

	/**
	 * Point's position.
	 */
	private @NotNull Position position;

	/**
	 * Creates a new PointCollider instance.
	 *
	 * @param position Position to set (sends a copy and not the pointer)
	 */
	public PointCollider(final @NotNull Position position) {
		this.position = new Position(position);
	}

	/**
	 * Creates a new PointCollider instance.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 */
	public PointCollider(final long x, final long y) {
		this.position = new Position(x, y);
	}

	@Override
	public @NotNull String toString() {
		return super.toString() + " (" + this.getPosition() + ")";
	}

	@Contract(pure = true)
	@Override
	public @Nullable CollisionInfo intersect(final @NotNull Collider collider) {
		if(collider instanceof PointCollider) {
			if(this.position.equals(((PointCollider) collider).getPosition())) {
				return new CollisionInfo(new PointCollider(this.position), this.position);
			}
		} else {
			if(collider.contains(this.position)) {
				return new CollisionInfo(new PointCollider(this.position), this.position);
			}
		}

		return null;
	}

	@Contract(pure = true)
	@Override
	public boolean contains(final @NotNull Position point) {
		return false;
	}

	@Contract(pure = true)
	@Override
	public boolean on(final @NotNull Position point) {
		return this.position.getX() == point.getX() && this.position.getY() == point.getY();
	}

	@Contract(pure = true)
	@Override
	public boolean containsOrOn(final @NotNull Position point) {
		return this.on(point);
	}

	/**
	 * Returns a copy of the Point's position (and not the pointer).
	 *
	 * @return new Position(Point.position)
	 */
	@Contract(pure = true)
	final public @NotNull Position getPosition() {
		return new Position(this.position);
	}

	@Contract(pure = true)
	@Override
	public int area() {
		return 0;
	}

}