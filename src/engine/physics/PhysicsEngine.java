package engine.physics;

/*
  The PhysicsEngine class is responsible for handling all the physics objects.

  The Physics are based on Chris Hecker's "Rigid Body Dynamics" series of articles
  https://www.chrishecker.com/Rigid_Body_Dynamics
 */

import engine.math.Vector2f;
import engine.physics.colliders.Collider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

final public class PhysicsEngine {

	/**
	 * How many iterations are we doing per frame to have smooth physics.
	 */
	final public static int ITERATIONS_PER_FRAME = 5;

	/**
	 * PhysicsEngine's objects to handle.
	 */
	final private static ArrayList<PhysicsObject> objects = new ArrayList<>();

	/**
	 * Called every frame at the beginning of update, so that it will calculate new positions.
	 *
	 * @param delta Time of a frame
	 */
	public static void update(final double delta) {
		for(int it = 0; it < PhysicsEngine.ITERATIONS_PER_FRAME; it++) {
			// First we move each object.
			for(final PhysicsObject object : PhysicsEngine.objects) {
				if(!object.isMoving()) {
					object.updateCollider();
					continue;
				}
				object.updatePosition(delta / PhysicsEngine.ITERATIONS_PER_FRAME);
			}

			// Then, we detect and resolve collisions.
			// TODO: Here, asCollider() can be called multiple times for the same object.
			for(int i = 0; i < PhysicsEngine.objects.size(); i++) {
				final PhysicsObject object = PhysicsEngine.objects.get(i);
				final ArrayList<PhysicsObject> potentialColliders = new ArrayList<>();

				for(int j = i + 1; j < PhysicsEngine.objects.size(); j++) {
					// TODO: Check position before adding.
					potentialColliders.add(PhysicsEngine.objects.get(j));
				}

				// TODO: Add collision with map tiles.
				/*if(!object.canFly()) {
					potentialColliders.addAll(Map.getInstance().getTilesOnAsColliders(object.getPosition(), object.getPhysicsWidthAsInt(), object.getPhysicsHeightAsInt(), object.canWalk(), object.canSwim()));
				}*/

				for(final PhysicsObject object2 : potentialColliders) {
					final Collider collider1 = object.asCollider();
					final Collider collider2 = object2.asCollider();

					final @Nullable Vector2f normal = collider1.intersect(collider2);
					if(normal == null) continue;

					if(object2.getMovementsAllowed() == MovementsAllowed.IMMOBILE) {
						object.move(normal.mul(-1));
					} else if(object.getMovementsAllowed() == MovementsAllowed.IMMOBILE) {
						object2.move(normal);
					} else {  // TODO: Not taking velocity and/or mass into account?
						object.move(normal.mul(-0.5f));
						object2.move(normal.mul(0.5f));
					}

					PhysicsEngine.resolveCollision(object, object2, normal);
				}
			}
		}
	}

	/**
	 * Resolves a collision between two objects.
	 *
	 * @param object1 First PhysicsObject
	 * @param object2 Second PhysicsObject
	 * @param normal Collision's normal vector.
	 */
	private static void resolveCollision(final @NotNull PhysicsObject object1, final @NotNull PhysicsObject object2, final @NotNull Vector2f normal) {
		final Vector2f relativeVelocity = object2.getLinearVelocity().sub(object1.getLinearVelocity());
		if(relativeVelocity.dot(normal) > 0) return;

		final float invMass1 = 1 / object1.getMass();
		final float invMass2 = 1 / object2.getMass();

		final float e = Math.min(object1.getRestitution(), object2.getRestitution());
		final float j = -(1 + e) * relativeVelocity.dot(normal) / (invMass1 + invMass2);

		final Vector2f impulse = normal.mul(j);
		object1.addLinearVelocity(impulse.mul(-invMass1));
		object2.addLinearVelocity(impulse.mul(invMass2));
	}

	/**
	 * Returns the PhysicsEngine's objects.
	 *
	 * @return PhysicsEngine::objects
	 */
	@Contract(pure = true)
	static ArrayList<PhysicsObject> getObjects() {
		return PhysicsEngine.objects;
	}

	/**
	 * Adds a PhysicsObject to the engine.
	 *
	 * @param object Object to add
	 */
	public static void addObject(final @NotNull PhysicsObject object) {
		PhysicsEngine.objects.add(object);
	}

	/**
	 * Removes a PhysicsObject from the engine.
	 *
	 * @param object Object to remove
	 */
	public static void removeObject(final @NotNull PhysicsObject object) {
		final boolean removed = PhysicsEngine.objects.remove(object);

		if(!removed) { // Object was not found, thus list was not changed.
			System.err.println("Error: This object couldn't be removed from PhysicsEngine.");
			System.err.println(object);
			new Exception().printStackTrace();
		}
	}

}