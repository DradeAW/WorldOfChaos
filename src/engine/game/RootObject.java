package engine.game;

import engine.game.objects.GameObject;
import org.jetbrains.annotations.NotNull;

final public class RootObject extends GameObject {

	/**
	 * Creates the RootObject instance.
	 */
	RootObject() {
		super("Root Object", Float.MAX_VALUE, Float.MAX_VALUE);
	}

	@Override
	public @NotNull RootObject init() {
		return this;
	}

	/**
	 * Adds an object as a child of the root object.
	 *
	 * @param child GameObject to add
	 */
	public void addChildToRootObject(final @NotNull GameObject child) {
		this.addChildInstantly(child);
	}

	/**
	 * Removes an object from the root object's children list.
	 *
	 * @param child GameObject to remove
	 */
	public void removeChildFromRootObject(final @NotNull GameObject child) {
		this.removeChild(child);
	}

}