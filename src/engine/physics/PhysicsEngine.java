package engine.physics;

/*
  The PhysicsEngine class is responsible for handling all the physics objects.

  The Physics are based on Chris Hecker's "Rigid Body Dynamics" series of articles
  https://www.chrishecker.com/Rigid_Body_Dynamics
 */

import engine.game.objects.map.Map;
import engine.math.Vector2f;
import engine.physics.colliders.Collider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
				if(!object.isMoving()) continue;
				object.updatePosition(delta / PhysicsEngine.ITERATIONS_PER_FRAME);
			}

			// Then, we detect and resolve collisions.
			// TODO: Here, asCollider() can be called multiple times for the same object.
			for(int i = 0; i < PhysicsEngine.objects.size(); i++) {
				final PhysicsObject object = PhysicsEngine.objects.get(i);
				final Collider collider1 = object.asCollider();
				final ArrayList<Collider> potentialColliders = new ArrayList<>();

				for(int j = i + 1; j < PhysicsEngine.objects.size(); j++) {
					// TODO: Check position before checking for collision.
					potentialColliders.add(PhysicsEngine.objects.get(j).asCollider());
				}

				if(!object.canFly()) {
					potentialColliders.addAll(Map.getInstance().getTilesOnAsColliders(object.getPosition(), object.getPhysicsWidthAsInt(), object.getPhysicsHeightAsInt(), object.canWalk(), object.canSwim()));
				}

				for(final Collider collider2 : potentialColliders) {
					final Vector2f normal = collider1.intersect(collider2);
					if(normal == null)
						continue;

					
				}
			}
		}
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