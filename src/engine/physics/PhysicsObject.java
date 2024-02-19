package engine.physics;

import com.Options;
import engine.game.objects.GameObject;
import engine.game.objects.map.Map;
import engine.math.Vector2f;
import engine.physics.colliders.Collider;
import engine.util.Direction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
	 * Object's restitution coefficient (bounciness).
	 * 1.0 is a perfect elastic collision (i.e. no energy loss).
	 * 0.0 is a perfectly inelastic collision (i.e. no bouncing).
	 */
	private float restitution = 1.0f;

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
	 * @param movements Object's movements that are allowed
	 */
	public PhysicsObject(final @NotNull String name, final float width, final float height, final MovementsAllowed movements) {
		super(name, width, height);

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
	 * Adds velocity to the PhysicsObject's linear velocity.
	 *
	 * @param velocity Velocity to add
	 */
	final public void addLinearVelocity(final @NotNull Vector2f velocity) {
		this.linearVelocity.addition(velocity);
	}

	/**
	 * Returns the PhysicsObject's mass (in kg).
	 *
	 * @return PhysicsObject.mass
	 */
	@Contract(pure = true)
	final public float getMass() {
		return this.mass;
	}

	/**
	 * Returns the PhysicsObject's restitution coefficient.
	 *
	 * @return PhysicsObject.restitution
	 */
	@Contract(pure = true)
	final public float getRestitution() {
		return this.restitution;
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
	/*protected AABBCollider asAABBCollider() {
		return new AABBCollider(this.getPositionReference(), this.getPhysicsWidthAsInt(), this.getPhysicsHeightAsInt());
	}*/

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
	}

}