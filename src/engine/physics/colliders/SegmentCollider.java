package engine.physics.colliders;

import engine.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class SegmentCollider extends Collider {

	/**
	 * Segment's starting point.
	 */
	private @NotNull Position p1;

	/**
	 * Segment's ending point.
	 */
	private @NotNull Position p2;

	/**
	 * Creates a new SegmentCollider instance.
	 *
	 * @param p1 Starting point to set
	 * @param p2 Ending point to set
	 */
	public SegmentCollider(final @NotNull Position p1, final @NotNull Position p2) {
		if(p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
			System.err.println("Error: P1 and P2 are the same while initializing SegmentCollider.");
			System.err.println("Future errors are bound to happen.");
			new Exception().printStackTrace();
		}

		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public @NotNull String toString() {
		return super.toString() + " (" + this.getP1() + " ; " + this.getP2() + ")";
	}

	@Contract(pure = true)
	@Override
	public @Nullable CollisionInfo intersect(final @NotNull Collider collider) {
		if(collider instanceof PointCollider) {
			return collider.intersect(this);
		} else if(collider instanceof SegmentCollider) {
			final SegmentCollider line = (SegmentCollider) collider;
			// Line equation : ax + by + c = 0
			final long a1 = this.p1.getY() - this.p2.getY();
			final long b1 = this.p2.getX() - this.p1.getX();
			final long c1 = this.p1.getX()*this.p2.getY() - this.p2.getX()*this.p1.getY();
			final long a2 = line.p1.getY() - line.p2.getY();
			final long b2 = line.p2.getX() - line.p1.getX();
			final long c2 = line.p1.getX()*line.p2.getY() - line.p2.getX()*line.p1.getY();
			final double det = a1*b2 - a2*b1;

			if(det == 0) { // Both lines are parallel.
				final double factor = a1 == 0 ? (double)b1/(double)b2 : (double)a1/(double)a2;
				if(Math.abs(c2 * factor - c1) >= 1) return null;

				if(a1 != 0) {
					final long ymin1 = Math.min(this.getP1().getY(), this.getP2().getY());
					final long ymax1 = Math.max(this.getP1().getY(), this.getP2().getY());
					final long ymin2 = Math.min(line.getP1().getY(), line.getP2().getY());
					final long ymax2 = Math.max(line.getP1().getY(), line.getP2().getY());

					if(ymin1 >= ymax2 || ymax1 <= ymin2) return null;
					return new CollisionInfo(new SegmentCollider(new Position(this.getP1().getX(), Math.max(ymin1, ymin2)), new Position(this.getP1().getX(), Math.min(ymax1, ymax2))), null);
				} else {
					final long xmin1 = Math.min(this.getP1().getX(), this.getP2().getX());
					final long xmax1 = Math.max(this.getP1().getX(), this.getP2().getX());
					final long xmin2 = Math.min(line.getP1().getX(), line.getP2().getX());
					final long xmax2 = Math.max(line.getP1().getX(), line.getP2().getX());

					if(xmin1 >= xmax2 || xmax1 <= xmin2) return null;

					final long x1 = Math.max(xmin1, xmin2);
					final long y1 = -(a1*x1 + c1)/b1;
					final long x2 = Math.min(xmax1, xmax2);
					final long y2 = -(a1*x2 + c1)/b1;
					return new CollisionInfo(new SegmentCollider(new Position(x1, y1), new Position(x2, y2)), null);
				}
			} else {
				final int x = (int) Math.round((double)(b1*c2 - b2*c1)/det);
				final int y = (int) Math.round((double)(a2*c1 - a1*c2)/det);
				final Position result = new Position(x, y);

				if(this.contains(result) && line.contains(result)) {
					return new CollisionInfo(new PointCollider(result), result);
				} else {
					return null;
				}
			}
		} else if(collider instanceof AABBCollider) {
			if(collider.contains(this.getP1()) && collider.contains(this.getP2())) {
				return new CollisionInfo(new SegmentCollider(this.getP1(), this.getP2()), null);
			}

			final HashSet<Position> intersections = new HashSet<>();

			for(final SegmentCollider line : ((AABBCollider) collider).asLines()) {
				final CollisionInfo collision = this.intersect(line);
				if(collision == null) {
					continue;
				}

				if(collision.getCollisionArea() instanceof PointCollider) {
					intersections.add(((PointCollider) collision.getCollisionArea()).getPosition());
				} else if(collision.getCollisionArea() instanceof SegmentCollider) {
					intersections.add(((SegmentCollider) collision.getCollisionArea()).getP1());
					intersections.add(((SegmentCollider) collision.getCollisionArea()).getP2());
				} else {
					System.err.println("Error: Don't know how to handle that");
					System.err.println(collision.getCollisionArea());
					return null;
				}
			}

			if(intersections.size() > 2) {
				System.err.println("Error: Line intersect at more than 2 different points with AABB Collider.");
				System.err.println("Segment: " + this + " ; [" + this.getP1() + ";" + this.getP2() + "]");
				System.err.println("AABB Collider: " + collider);
				System.err.println("\tMin extents: " + ((AABBCollider) collider).getPosition());
				System.err.println("\tMax extents: " + ((AABBCollider) collider).getMaxExtents());
				System.err.println("Collisions found:");
				for(final Position pt : intersections) {
					System.err.println("\t- " + pt);
				}
				System.err.println("Returning null...");
				return null;
			} else if(intersections.size() == 0) { // No collision or line going through a corner.
				if(collider.contains(this.getP1())) {
					final Position[] corners = ((AABBCollider) collider).getCorners();
					for(final Position corner : corners) {
						if(this.containsOrOn(corner)) {
							return new CollisionInfo(new SegmentCollider(this.getP1(), corner), null);
						}
					}
				} else if(collider.contains(this.getP2())) {
					final Position[] corners = ((AABBCollider) collider).getCorners();
					for(final Position corner : corners) {
						if(this.containsOrOn(corner)) {
							return new CollisionInfo(new SegmentCollider(corner, this.getP2()), corner);
						}
					}
				}

				return null;
			} else if(intersections.size() == 2) {
				final Position[] pts = intersections.toArray(new Position[0]);
				if(pts[0].distanceToXY(this.getP1()) < pts[1].distanceToXY(this.getP1())) {
					return new CollisionInfo(new SegmentCollider(pts[0], pts[1]), pts[0]);
				} else {
					return new CollisionInfo(new SegmentCollider(pts[1], pts[0]), pts[1]);
				}
			} else { // intersections.size() == 1
				final Position[] pts = intersections.toArray(new Position[0]);
				if(collider.contains(this.getP1())) {
					return new CollisionInfo(new SegmentCollider(this.getP1(), pts[0]), null);
				} else {
					return new CollisionInfo(new SegmentCollider(pts[0], this.getP2()), pts[0]);
				}
			}
		} else if(collider instanceof CircleCollider) {
			// Line equation : ax + by + c = 0 ; Circle equation : (x-p)² + (y-q)² = r²
			final long a = this.p1.getY() - this.p2.getY();
			final long b = this.p2.getX() - this.p1.getX();
			final long c = this.p1.getX()*this.p2.getY() - this.p2.getX()*this.p1.getY();
			final long p = ((CircleCollider) collider).getCenter().getX();
			final long q = ((CircleCollider) collider).getCenter().getY();
			final long r = ((CircleCollider) collider).getRadius();

			if(b == 0) {
				if(b <= p-r || b >= p+r) return null; // TODO: This doesn't seem correct.
				// x = b ==> (y-q)² = r² - (b-p)²
				final long k = Math.round(Math.sqrt(r*r - (b-p)*(b-p)));
				final SegmentCollider collision = new SegmentCollider(new Position(b, q-k), new Position(b, q+k));
				// TODO: Finish.
			}

			System.err.print("Error: Collision between SegmentCollider and CircleCollider not implemented yet.");
			new Exception().printStackTrace();

			return null;
		} else {
			System.err.println("Error: Collision between SegmentCollider and " + collider + " isn't implemented yet.");
			new Exception().printStackTrace();
			return null;
		}
	}

	@Contract(pure = true)
	@Override
	public boolean contains(final @NotNull Position point) {
		return this.on(point) && !point.equalsXY(this.p1) && !point.equalsXY(this.p2);
	}

	@Contract(pure = true)
	@Override
	public boolean on(final @NotNull Position point) {
		// Line equation : ax + by + c = 0
		final long a = this.p1.getY() - this.p2.getY();
		final long b = this.p2.getX() - this.p1.getX();
		final long c = this.p1.getX()*this.p2.getY() - this.p2.getX()*this.p1.getY();

		if(Math.abs(a*point.getX() + b*point.getY() + c) > 1) return false;

		if(point.getX() < Math.min(this.p1.getX(), this.p2.getX())) return false;
		if(point.getX() > Math.max(this.p1.getX(), this.p2.getX())) return false;
		if(point.getY() < Math.min(this.p1.getY(), this.p2.getY())) return false;
		if(point.getY() > Math.max(this.p1.getY(), this.p2.getY())) return false;

		return true;
	}

	@Contract(pure = true)
	@Override
	public boolean containsOrOn(final @NotNull Position point) {
		return this.on(point);
	}

	/**
	 * Returns a copy of the Segment's starting point (and not the pointer).
	 *
	 * @return new Position(SegmentCollider.p1)
	 */
	@Contract(pure = true)
	final public @NotNull Position getP1() {
		return new Position(this.p1);
	}

	/**
	 * Returns a copy of the Segment's ending point (and not the pointer).
	 *
	 * @return new Position(SegmentCollider.p2)
	 */
	@Contract(pure = true)
	final public @NotNull Position getP2() {
		return new Position(this.p2);
	}

	@Contract(pure = true)
	@Override
	public int area() {
		return 0;
	}

}