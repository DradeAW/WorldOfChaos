package engine.physics;

import com.Options;
import engine.game.objects.GameObject;
import engine.game.objects.map.Map;
import engine.math.Vector2f;
import engine.physics.colliders.AABBCollider;
import engine.physics.colliders.Collider;
import engine.physics.colliders.SegmentCollider;
import engine.util.Direction;
import engine.util.Position;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import support.ArrayList;

public abstract class PhysicsObject extends GameObject {

	/**
	 * Object's velocity/speed (represented as a vector).
	 * Unit is number of tiles/second.
	 */
	private @NotNull Vector2f linearVelocity = new Vector2f(0, 0);

	/**
	 * Object's mass.
	 * Unit in kilograms.
	 */
	private float mass = 1.0f;

	/**
	 * Object's collider representation.
	 */
	private @NotNull CollisionBehaviour collisionBehaviour;

	/**
	 * Object's movements that are allowed.
	 */
	final private MovementsAllowed movementsAllowed;

	/**
	 * Direction the object is facing.
	 */
	final private Direction direction = new Direction();

	/**
	 * Creates a new PhysicsObject instance.
	 *
	 * @param name Name to set
	 * @param width Width to set
	 * @param height Height to set
	 * @param collisionBehaviour Object's behaviour on collision
	 * @param movements Object's movements that are allowed
	 */
	public PhysicsObject(final @NotNull String name, final float width, final float height, final @NotNull CollisionBehaviour collisionBehaviour, final MovementsAllowed movements) {
		super(name, width, height);

		this.collisionBehaviour = collisionBehaviour;
		this.movementsAllowed = movements;
	}

	/**
	 * Returns a copy of the PhysicsObject's linear velocity (not the pointer itself).
	 *
	 * @return new Vector2f(PhysicsObject.velocity)
	 */
	@Contract(pure = true)
	final public @NotNull Vector2f getLinearVelocity() {
		return new Vector2f(this.linearVelocity);
	}

	/**
	 * Returns the PhysicsObject's mass (in kg).
	 *
	 * @return PhysicsObject.mass
	 */
	@Contract(pure = true)
	final protected float getMass() {
		return this.mass;
	}

	/**
	 * Returns how the Physics Object behaves when it collides.
	 *
	 * @return PhysicsObject.collisionBehaviour
	 */
	@Contract(pure = true)
	final protected CollisionBehaviour getCollisionBehaviour() {
		return this.collisionBehaviour;
	}

	/**
	 * Returns the Physics Object's movements allowed.
	 *
	 * @return PhysicsObject.movementsAllowed
	 */
	@Contract(pure = true)
	final protected MovementsAllowed getMovementsAllowed() {
		return this.movementsAllowed;
	}

	/**
	 * Returns whether the object can walk on the ground.
	 *
	 * @return boolean
	 */
	@Contract(pure = true)
	final public boolean canWalk() {
		return this.getMovementsAllowed().canWalk();
	}

	/**
	 * Returns whether the object can swim in the water.
	 *
	 * @return boolean
	 */
	@Contract(pure = true)
	final public boolean canSwim() {
		return this.getMovementsAllowed().canSwim();
	}

	/**
	 * Returns whether the object can fly.
	 *
	 * @return boolean
	 */
	@Contract(pure = true)
	final public boolean canFly() {
		return this.getMovementsAllowed().canFly();
	}

	/**
	 * Returns whether the object can move on ground tiles.
	 *
	 * @return boolean
	 */
	@Contract(pure = true)
	final public boolean canMoveOnGround() {
		return this.canWalk() || this.canFly();
	}

	/**
	 * Returns whether the Physics Object is moving (position changing this frame).
	 *
	 * @return boolean
	 */
	final public boolean isMoving() {
		return !(this.getMovementsAllowed().equals(MovementsAllowed.IMMOBILE) || this.getLinearVelocity().equals(Vector2f.zero));
	}

	/**
	 * Returns the Physics's object hitbox's width.
	 * By default, is the object's width (can be overridden).
	 *
	 * @return float
	 */
	protected float getPhysicsWidth() {
		return this.getWidth();
	}

	/**
	 * Returns the Physics's object hitbox's height.
	 * By default, is the object's height (can be overridden).
	 *
	 * @return float
	 */
	protected float getPhysicsHeight() {
		return this.getHeight();
	}

	/**
	 * Returns the Physics's object hitbox's width as an integer.
	 * By default, is the object's width (can be overridden).
	 *
	 * @return int
	 */
	protected int getPhysicsWidthAsInt() {
		return this.getWidthAsInt();
	}

	/**
	 * Returns the Physics's object hitbox's height as an integer.
	 * By default, is the object's height (can be overridden).
	 *
	 * @return int
	 */
	protected int getPhysicsHeightAsInt() {
		return this.getHeightAsInt();
	}

	/**
	 * Returns the direction the Object is facing.
	 *
	 * @return PhysicsObject.direction
	 */
	@Contract(pure = true)
	final public Direction getDirection() {
		return this.direction;
	}

	/**
	 * Sets the PhysicsObject's velocity (in number of tiles / s).
	 * This assigns the pointer, so be careful modifying the vector after.
	 *
	 * @param velocity Velocity to set
	 */
	final protected void setLinearVelocity(final @NotNull Vector2f velocity) {
		this.linearVelocity = velocity;
	}

	/**
	 * Sets the PhysicsObject's mass.
	 *
	 * @param mass Mass to set (in kg)
	 */
	final protected void setMass(final float mass) {
		this.mass = mass;
	}

	/**
	 * Sets the direction the Object is facing.
	 *
	 * @param direction Direction to set
	 */
	protected void setDirection(final byte direction) {
		this.getDirection().setDirection(direction);
	}

	/**
	 * Returns a Collider that represents the Physics Object at the time it is called.
	 * Need to take into account the position, rotation, scale ...
	 *
	 * @return new Collider
	 */
	public abstract @NotNull Collider asCollider();

	/**
	 * Returns an AABBCollider depending on the Position, width ahd height of the Physics Object.
	 * Might not match the actual Physics Object: see PhysicsObject.asCollider().
	 *
	 * @return new AABBCollider
	 */
	protected AABBCollider asAABBCollider() {
		return new AABBCollider(this.getPositionReference(), this.getPhysicsWidthAsInt(), this.getPhysicsHeightAsInt());
	}

	/**
	 * Called when (this) collides with another object.
	 * Isn't called for immobile object that collides with an object that moves on them.
	 *
	 * @param object Object (this) collides with
	 */
	protected void onCollision(final @NotNull PhysicsObject object) {

	}

	/**
	 * Automatically called when object is changing direction.
	 * Does nothing on its own, needs to be overridden.
	 */
	protected void refreshTexture() {

	}

	/**
	 * Refresh the Object's direction based on its velocity.
	 *
	 * @param setNone Set direction to none if nul velocity?
	 */
	protected void refreshDirection(final boolean setNone) {
		if(this.getLinearVelocity().equals(Vector2f.zero)) {
			if(setNone) this.setDirection(Direction.NONE);
			return;
		}

		float theta;

		if(this.getLinearVelocity().getX() == 0) {
			theta = this.getLinearVelocity().getY() > 0 ? (float) Math.PI/2 : (float) -Math.PI/2;
		} else {
			theta = (float) Math.atan(this.getLinearVelocity().getY() / this.getLinearVelocity().getX());
			if(this.getLinearVelocity().getX() < 0) theta += theta < 0 ? Math.PI : -Math.PI;
		}

		final byte direction;
		if(theta >= -Math.PI/8 && theta <= Math.PI/8) direction = Direction.EAST;
		else if(theta > Math.PI/8 && theta< 3*Math.PI/8) direction = Direction.NORTH_EAST;
		else if(theta >= 3*Math.PI/8 && theta <= 5*Math.PI/8) direction = Direction.NORTH;
		else if(theta > 5*Math.PI/8 && theta < 7*Math.PI/8) direction = Direction.NORTH_WEST;
		else if(theta < -Math.PI/8 && theta > -3*Math.PI/8) direction = Direction.SOUTH_EAST;
		else if(theta <= -3*Math.PI/8 && theta >= -5*Math.PI/8) direction = Direction.SOUTH;
		else if(theta < -5*Math.PI/8 && theta > -7*Math.PI/8) direction = Direction.SOUTH_WEST;
		else if(theta < Math.PI+.00001f || theta > Math.PI-.00001f) direction = Direction.WEST;
		else {
			System.err.println("Error: Couldn't find direction from velocity.");
			System.err.println("Velocity: " + this.getLinearVelocity() + " ; theta=" + theta);
			new Exception().printStackTrace();
			direction = 0;
		}

		if(this.getDirection().getDirection() != direction) {
			this.setDirection(direction);
			this.refreshTexture();
		}
	}

	/**
	 * Moves the PhysicsObject by a certain amount.
	 *
	 * @param translation Translation to apply
	 */
	final void move(final @NotNull Vector2f translation) {
		this.setPosition(this.getPosition().add(translation));
	}

	/**
	 * Moves the PhysicsObject for a frame.
	 * This checks for map's tiles but not yet for intersection/collisions.
	 *
	 * @param delta Time of a frame
	 */
	final void updatePosition(final double delta) {
		final float tileSpeed = Map.getInstance().getTileSpeedOn(this.getPosition(), this.getPhysicsWidth(), this.getPhysicsHeight());
		final @NotNull Vector2f translation = this.getLinearVelocity().mul((float)(delta * Options.TILE_SIZE * (this.canFly() ? 1 : tileSpeed)));
		this.move(translation);
		this.refreshDirection(false);

		/*
		if(this.getLinearVelocity().equals(Vector2f.zero)) return; // No need to move if nul velocity.

		this.refreshDirection(false);

		// TODO: There might be a faster way to do this.
		final float tileSpeed = Map.getInstance().getTileSpeedOn(this.getPosition(), this.getPhysicsWidth(), this.getPhysicsHeight());
		final @NotNull Vector2f velocity = this.getLinearVelocity().mul((float)(delta * Options.TILE_SIZE * (this.canFly() ? 1 : tileSpeed)));
		final @NotNull Position futurePos = this.getPositionReference().addXYAsFloat(velocity);

		int length = this.getPositionReference().distanceToXY(futurePos);
		final @NotNull Position posToSet = new Position(futurePos);

		final @NotNull ArrayList<Collider> potentialColliders = new ArrayList<>();

		// Adding all neighbours tiles as potential colliders
		if(!this.canFly()) {
			potentialColliders.addAll(Map.getInstance().getTilesOnAsColliders(futurePos, this.getPhysicsWidthAsInt(), this.getPhysicsHeightAsInt(), this.canWalk(), this.canSwim()));
		}

		// Check with all other Physics Object
		for(final @NotNull PhysicsObject object : PhysicsEngine.getObjects()) {
			if(object == this) {
				continue;
			}

			if(this.willCollideWith(futurePos, object)) {
				potentialColliders.add(object.asCollider());
			}
		}

		for(final Collider potentialCollider : potentialColliders) {
			final Position colPos = this.handleCollision(futurePos, potentialCollider);

			if(this.getPositionReference().distanceToXY(colPos) <= length) {
				length = this.getPositionReference().distanceToXY(colPos);
				posToSet.set(colPos);
			}
		}

		futurePos.set(posToSet); // TODO: Is futurePos necessary?

		// Checking collisions with tiles (new).
		if(!this.canFly()) {
			for(final AABBCollider tileCollider : Map.getInstance().getTilesOnAsColliders(futurePos, this.getPhysicsWidthAsInt(), this.getPhysicsHeightAsInt(), this.canWalk(), this.canSwim())) {
				final Position colPos = this.handleCollision(futurePos, tileCollider);

				if(this.getPositionReference().distanceToXY(colPos) <= length) {
					length = this.getPositionReference().distanceToXY(colPos);
					posToSet.set(colPos);
				}
			}

			futurePos.set(posToSet);
		}

		this.setPosition(futurePos.asVector2f());*/
	}

	/**
	 * Handles the physics of the collision.
	 *
	 * @param futurePos Future pos of this
	 * @param collider Object this is colliding with
	 * @return new Position (position to set to avoid collision)
	 */
	private @NotNull Position handleCollision(final @NotNull Position futurePos, final @NotNull Collider collider) { // TODO: Also take in rotation and scale ...
		// First we need to know which corner(s) of (this) is the problem.
		// TODO: Does this corner system work with all colliders?
		final Position[] previousCorners = {this.getPositionReference(), this.getPositionReference().addX(this.getPhysicsWidthAsInt()), this.getPositionReference().addXY(this.getPhysicsWidthAsInt(), this.getPhysicsHeightAsInt()), this.getPositionReference().addY(this.getPhysicsHeightAsInt())};
		final Position[] corners = {futurePos, futurePos.addX(this.getPhysicsWidthAsInt()), futurePos.addXY(this.getPhysicsWidthAsInt(), this.getPhysicsHeightAsInt()), futurePos.addY(this.getPhysicsHeightAsInt())};
		final ArrayList<Integer> cornersColl = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			if(collider.containsOrOn(corners[i])) {
				cornersColl.add(i);
			}
		}

		Position posToSet = new Position(futurePos);
		if(cornersColl.size() == 0) {
			System.err.println("Error: handleCollision() called but no collision has been found.");
			System.err.println("Corners:");
			for(final Position corner : corners) {
				System.err.println("  - " + corner);
			}
			System.err.println("collider: " + collider);
			new Exception().printStackTrace();
			return posToSet;
		}

		double length = previousCorners[0].distanceToXY(corners[0]);
		for(final int cornerID : cornersColl) {
			final SegmentCollider trajectory = new SegmentCollider(previousCorners[cornerID], corners[cornerID]);

			if(collider instanceof AABBCollider) {
				final CollisionInfo collisionInfo = trajectory.intersect(collider);

				if(collisionInfo == null) {
					System.err.println("Error: collisionInfo is null and shouldn't be.");
					System.err.println(this);
					System.err.println(collider);
					new Exception().printStackTrace();
					return posToSet;
				}

				final Position pointOfCollision = collisionInfo.getCollisionPoint();
				if(pointOfCollision == null) {
					System.err.println("Error: collisionPoint is null (the object probably started inside the AABBCollider).");
					System.err.println(this);
					System.err.println(collider);
					return posToSet;
				}

				final double distanceToCollision = previousCorners[cornerID].distanceToXY(pointOfCollision);
				if(distanceToCollision < length) {
					length = distanceToCollision;
					posToSet = new Position(futurePos).subtract(corners[cornerID].sub(pointOfCollision));
				}
			} else {
				System.err.println("Error: Collision not implemented with this type of collider: " + collider);
				new Exception().printStackTrace();
				return posToSet;
			}
		}

		this.setLinearVelocity(new Vector2f(Vector2f.zero)); // TODO: This doesn't always happen like that.
		return posToSet;
	}

	/**
	 * Returns a Vector2f containing how deep the 2 objects collide (with new position for (this)).
	 * Returns null if it doesn't collide.
	 * If (this) is on the bottom/left, values are negative.
	 *
	 * @param pos (this) new position
	 * @param object Object to check with
	 * @return new Vector2f
	 */
	final public @Nullable Vector2f getCollisionWith(final @NotNull Vector2f pos, final @NotNull PhysicsObject object) {
		final Position thisPos = new Position(pos);
		final Position objPos = object.getPositionReference();
		long xCheck = Math.min(thisPos.getX() + Math.round(this.getPhysicsWidth()*Position.RATIO) - objPos.getX(), objPos.getX() + Math.round(object.getPhysicsWidth()*Position.RATIO) - thisPos.getX());
		long yCheck = Math.min(thisPos.getY() + Math.round(this.getPhysicsHeight()*Position.RATIO) - objPos.getY(), objPos.getY() + Math.round(object.getPhysicsHeight()*Position.RATIO) - thisPos.getY());
		if(xCheck <= 0 || yCheck <= 0) return null;
		if(pos.getX() < object.getPosition().getX()) xCheck *= -1;
		if(pos.getY() < object.getPosition().getY()) yCheck *= -1;

		return new Vector2f((float)((double)xCheck/Position.RATIO), (float)((double)yCheck/Position.RATIO));
	}

	/**
	 * Returns a Vector2f containing how deep the 2 objects collide.
	 * Returns null if it doesn't collide.
	 * If (this) is on the bottom/left, values are negative.
	 *
	 * @param object Object to check with
	 * @return new Vector2f
	 */
	final public @Nullable Vector2f getCollisionWith(final @NotNull PhysicsObject object) {
		return this.getCollisionWith(this.getPosition(), object);
	}

	/**
	 * Returns whether this PhysicsObject collides with the object passed in parameter.
	 *
	 * @param object Object to check with
	 * @return new boolean
	 */
	final public boolean collidesWith(final @NotNull PhysicsObject object) {
		/*if(this.getPosition().getX() > object.getPosition().getX() + object.getPhysicsWidth()) return false;
		if(object.getPosition().getX() > this.getPosition().getX() + this.getPhysicsWidth()) return false;
		if(this.getPosition().getY() > object.getPosition().getY() + object.getPhysicsHeight()) return false;
		if(object.getPosition().getY() > this.getPosition().getY() + this.getPhysicsHeight()) return false;

		return true;*/
		return this.getCollisionWith(object) != null;
	}

	/**
	 * Returns whether this PhysicsObjects will collide with another after movement.
	 *
	 * @param futurePos Future pos of this
	 * @param object PhysicsObject to check against
	 * @return new boolean
	 */
	final protected boolean willCollideWith(final @NotNull Position futurePos, final @NotNull PhysicsObject object) {
		// TODO: Use the collider of the Physics Object instead of just its position.
		if(futurePos.getX() > object.getPositionReference().getX() + object.getPhysicsWidthAsInt()) return false;
		if(object.getPositionReference().getX() > futurePos.getX() + this.getPhysicsWidthAsInt()) return false;
		if(futurePos.getY() > object.getPositionReference().getY() + object.getPhysicsHeightAsInt()) return false;
		if(object.getPositionReference().getY() > futurePos.getY() + this.getPhysicsHeightAsInt()) return false;

		return true;
	}

}