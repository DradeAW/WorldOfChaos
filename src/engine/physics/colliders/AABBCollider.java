package engine.physics.colliders;

import engine.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AABBCollider extends Collider {

	/**
	 * AABB's position (left-bottom corner).
	 */
	private @NotNull Position position;

	/**
	 * AABB's width.
	 */
	private int width;

	/**
	 * AABB's height.
	 */
	private int height;

	/**
	 * Creates a new AABBCollider instance.
	 *
	 * @param pos Position to set (left-bottom corner) (sends a copy and not the pointer)
	 * @param width Width to set
	 * @param height Height to set
	 */
	public AABBCollider(final @NotNull Position pos, final int width, final int height) {
		this(pos.getX(), pos.getY(), width, height);
	}

	/**
	 * Creates a new AABBCollider's instance.
	 *
	 * @param x X position to set (left-bottom corner)
	 * @param y Y position to set (left-bottom corner)
	 * @param width Width to set
	 * @param height Height to set
	 */
	public AABBCollider(final long x, final long y, final int width, final int height) {
		this.position = new Position(x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates a new AABBCollider's instance.
	 *
	 * @param minExtents Left-bottom corner to set
	 * @param maxExtents Right-top corner to set
	 */
	public AABBCollider(final @NotNull Position minExtents, final @NotNull Position maxExtents) {
		if(minExtents.getX() > maxExtents.getX() || minExtents.getY() > maxExtents.getY()) {
			System.err.println("Error: AABBCollider's minExtents must be left-bottom corner.");
			System.err.println("Given:");
			System.err.println("  - minExtents: " + minExtents);
			System.err.println("  - maxExtents: " + maxExtents);
			new Exception().printStackTrace();
			this.position = new Position();
			this.width = 0;
			this.height = 0;
		}

		this.position = new Position(minExtents);
		this.width = (int)(maxExtents.getX() - minExtents.getX());
		this.height = (int)(maxExtents.getY() - minExtents.getY());
	}

	@Override
	public @NotNull String toString() {
		return super.toString() + " (" + this.getPosition() + " ; " + this.getMaxExtents() + ")";
	}

	@Contract(pure = true)
	@Override
	public @Nullable CollisionInfo intersect(final @NotNull Collider collider) {
		if(collider instanceof PointCollider || collider instanceof SegmentCollider) {
			return collider.intersect(this);
		} else if(collider instanceof AABBCollider) {
			if(((AABBCollider) collider).getPosition().getX() >= this.getPosition().getX() + this.getWidth() || ((AABBCollider) collider).getPosition().getY() >= this.getPosition().getY() + this.getHeight() || this.getPosition().getX() >= ((AABBCollider) collider).getPosition().getX() + ((AABBCollider) collider).getWidth() || this.getPosition().getY() >= ((AABBCollider) collider).getPosition().getY() + ((AABBCollider) collider).getHeight()) {
				return null;
			}

			final Position minExtents = new Position(Math.max(this.getPosition().getX(), ((AABBCollider) collider).getPosition().getX()), Math.max(this.getPosition().getY(), ((AABBCollider) collider).getPosition().getY()));
			final Position maxExtents = new Position(Math.min(this.getMaxExtents().getX(), ((AABBCollider) collider).getMaxExtents().getX()), Math.min(this.getMaxExtents().getY(), ((AABBCollider) collider).getMaxExtents().getY()));
			return new CollisionInfo(new AABBCollider(minExtents, maxExtents), null);
		} else {
			System.err.println("Error: Collision between AABBCollider and " + collider + " isn't implemented yet.");
			return null;
		}
	}

	@Contract(pure = true)
	@Override
	public boolean contains(final @NotNull Position point) {
		return point.getX() > this.getPosition().getX()
			&& point.getX() < this.getPosition().getX() + this.getWidth()
			&& point.getY() > this.getPosition().getY()
			&& point.getY() < this.getPosition().getY() + this.getHeight();
	}

	@Contract(pure = true)
	@Override
	public boolean on(final @NotNull Position point) {
		return this.containsOrOn(point) && !this.contains(point);
	}

	@Contract(pure = true)
	@Override
	public boolean containsOrOn(final @NotNull Position point) {
		return point.getX() >= this.getPosition().getX()
			&& point.getX() <= this.getPosition().getX() + this.getWidth()
			&& point.getY() >= this.getPosition().getY()
			&& point.getY() <= this.getPosition().getY() + this.getHeight();
	}

	/**
	 * Returns a copy of the AABB's position (and not the pointer).
	 *
	 * @return new Position(AABBCollider.position)
	 */
	@Contract(pure = true)
	final public @NotNull Position getPosition() {
		return new Position(this.position);
	}

	/**
	 * Returns the AABB's top-right corner.
	 *
	 * @return AABBCollider.position + width,height
	 */
	@Contract(pure = true)
	final public @NotNull Position getMaxExtents() {
		return this.getPosition().addXY(this.getWidth(), this.getHeight());
	}

	/**
	 * Returns the AABBCollider's width.
	 *
	 * @return AABBCollider.width
	 */
	@Contract(pure = true)
	final public int getWidth() {
		return this.width;
	}

	/**
	 * Returns the AABBCollider's height.
	 *
	 * @return AABBCollider.height
	 */
	@Contract(pure = true)
	final public int getHeight() {
		return this.height;
	}

	/**
	 * Returns the Position of all 4 corners.
	 *
	 * @return new Position[4]
	 */
	@Contract(pure = true)
	final public @NotNull Position[] getCorners() {
		return new Position[] {
			this.getPosition(),
			this.getPosition().addX(this.getWidth()),
			this.getMaxExtents(),
			this.getPosition().addY(this.getHeight())
		};
	}

	/**
	 * Returns the AABBCollider's as 4 lines.
	 *
	 * @return new SegmentCollider[4]
	 */
	@Contract(pure = true)
	final public @NotNull SegmentCollider[] asLines() {
		final Position[] corners = this.getCorners();

		return new SegmentCollider[] {
			new SegmentCollider(corners[0], corners[1]),
			new SegmentCollider(corners[1], corners[2]),
			new SegmentCollider(corners[2], corners[3]),
			new SegmentCollider(corners[3], corners[0])
		};
	}

	@Contract(pure = true)
	@Override
	public int area() {
		return this.getWidth() * this.getHeight();
	}

}