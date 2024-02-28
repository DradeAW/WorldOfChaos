package engine.physics;

/*
  The PhysicsEngine class is responsible for handling all the physics objects.

  The Physics are based on Chris Hecker's "Rigid Body Dynamics" series of articles
  https://www.chrishecker.com/Rigid_Body_Dynamics
  and only handles convex objects (concave objects might result in unexpected behavior!).
 */

import com.objects.characters.Character;
import engine.game.objects.map.Map;
import engine.math.Vector2f;
import engine.physics.colliders.AABBCollider;
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
			for(int i = 0; i < PhysicsEngine.getObjects().size(); i++) {
				final PhysicsObject object1 = PhysicsEngine.getObjects().get(i);
				final Collider collider1 = object1.asCollider();
				final ArrayList<PhysicsObject> potentialColliders = new ArrayList<>();

				for(int j = i + 1; j < PhysicsEngine.getObjects().size(); j++) {
					// TODO: Check position before adding.
					potentialColliders.add(PhysicsEngine.getObjects().get(j));
				}

				for(final PhysicsObject object2 : potentialColliders) {
					final Collider collider2 = object2.asCollider();

					final @Nullable Vector2f normal = collider1.intersect(collider2);
					if(normal == null) continue;

					if(object2.getMovementsAllowed() == MovementsAllowed.IMMOBILE) {
						object1.move(normal.mul(-1));
						PhysicsEngine.resolveCollision(object1, null, normal.normalized());
					} else if(object1.getMovementsAllowed() == MovementsAllowed.IMMOBILE) {
						object2.move(normal);
						PhysicsEngine.resolveCollision(object2, null, normal.normalized());
					} else {  // TODO: Not taking velocity and/or mass into account?
						object1.move(normal.mul(-0.5f));
						object2.move(normal.mul(0.5f));
						PhysicsEngine.resolveCollision(object1, object2, normal.normalized());
					}
				}

				if(!object1.canFly() && object1.getMovementsAllowed() != MovementsAllowed.IMMOBILE) {
					final ArrayList<AABBCollider> tiles = Map.getInstance().getTilesOnAsColliders(object1.getPosition(), object1.getPhysicsWidth(), object1.getPhysicsHeight(), object1.canWalk(), object1.canSwim());

					for(final AABBCollider tile : tiles) {
						final @Nullable Vector2f normal = collider1.intersect(tile);
						if(normal == null) continue;

						object1.move(normal.mul(-1));
						PhysicsEngine.resolveCollision(object1, null, normal.normalized());
					}
				}
			}
		}
	}

	/**
	 * Resolves a collision between two objects.
	 *
	 * @param object1 First PhysicsObject
	 * @param object2 Second PhysicsObject
	 * @param normal Collision's normal vector (normalized).
	 */
	private static void resolveCollision(final @NotNull PhysicsObject object1, final @Nullable PhysicsObject object2, final @NotNull Vector2f normal) {
		final Vector2f velocity2 = object2 == null ? Vector2f.zero : object2.getLinearVelocity();
		final Vector2f relativeVelocity = velocity2.sub(object1.getLinearVelocity());
		if(relativeVelocity.dot(normal) > 0) return;

		final float invMass1 = 1 / object1.getMass();
		final float invMass2 = object2 == null ? 0 : 1 / object2.getMass();

		final float e = object2 == null ? object1.getRestitution() : Math.min(object1.getRestitution(), object2.getRestitution());
		final float j = -(1 + e) * relativeVelocity.dot(normal) / (invMass1 + invMass2);

		final Vector2f impulse = normal.mul(j);
		if(!(object1 instanceof Character))
			object1.addLinearVelocity(impulse.mul(-invMass1));
		if(object2 != null && !(object2 instanceof Character))
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