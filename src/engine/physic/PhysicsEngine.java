package engine.physic;

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
		for(int i = 0; i < PhysicsEngine.ITERATIONS_PER_FRAME; i++) {
			for(final PhysicsObject object : PhysicsEngine.objects) {
				if(!object.isMoving()) continue;
				object.move(delta / PhysicsEngine.ITERATIONS_PER_FRAME);
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