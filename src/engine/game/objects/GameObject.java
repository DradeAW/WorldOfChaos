package engine.game.objects;

import engine.CoreEngine;
import engine.audio.AudioObject;
import engine.game.Transform;
import engine.game.components.GameComponent;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.physics.PhysicsEngine;
import engine.physics.PhysicsObject;
import engine.rendering.shader.Shader;
import engine.util.GameInterface;
import engine.util.Input;
import engine.util.Units;
import engine.util.Window;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GameObject implements GameInterface {

	/**
	 * Do we ignore the check to render if the object is out of bounds.
	 */
	public static boolean ignoreRenderOutOfBounds = false;

	/**
	 * Reference to the CoreEngine.
	 */
	private CoreEngine coreEngine;

	/**
	 * GameObject's name.
	 */
	final private @NotNull String name;

	/**
	 * GameObject's children.
	 */
	final private @NotNull ArrayList<GameObject> children;

	/**
	 * GameObject's children to add at the end of the update.
	 */
	final private @NotNull ArrayList<GameObject> childrenToAdd;

	/**
	 * GameObject's children to delete at the end of the update.
	 */
	final private @NotNull ArrayList<GameObject> childrenToRemove;

	/**
	 * GameObject's components.
	 */
	final private @NotNull ArrayList<GameComponent> components;

	/**
	 * GameObject's audios.
	 */
	final private @NotNull ArrayList<AudioObject> audioObjects;

	/**
	 * GameObject's transformations (position, rotation, scale).
	 */
	final private @NotNull Transform transform;

	/**
	 * Is the GameObject rendered if outside the parent object.
	 */
	private boolean renderOutsideParent;

	/**
	 * GameObject's width (in openGL units).
	 */
	private float width;

	/**
	 * GameObject's height (in openGL units).
	 */
	private float height;

	/**
	 * Creates a new GameObject instance.
	 *
	 * @param name Name to set
	 * @param width Width to set
	 * @param height Height to set
	 */
	public GameObject(final @NotNull String name, final float width, final float height) {
		this.name = name;
		this.children = new ArrayList<>();
		this.childrenToAdd = new ArrayList<>();
		this.childrenToRemove = new ArrayList<>();
		this.components = new ArrayList<>();
		this.audioObjects = new ArrayList<>();
		this.transform = new Transform();
		this.setRenderOutsideParent(false);
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	public GameObject init() {
		return this;
	}

	@Override
	public void render(final Shader shader, final @Nullable ArrayList<GameComponent> renderLater) {
		for(final @NotNull GameObject object : this.getChildren()) {
			if(GameObject.ignoreRenderOutOfBounds || (object.isInScreen() && (this.renderOutsideParent() || object.isInObjectFrame(this)))) {
				object.render(shader, renderLater);
			}
		}

		for(final @NotNull GameComponent component : this.getComponents()) {
			component.render(shader, renderLater);
		}
	}

	@Override
	public void update(final double delta) {
		for(final @NotNull GameObject object : this.getChildren()) {
			object.update(delta);
		}

		for(final @NotNull GameComponent component : this.getComponents()) {
			component.update(delta);
		}

		for(final @NotNull AudioObject audio : this.getAudioObjects()) {
			final Vector2f pos = this.getPosition().add(this.getWidth()/2, this.getHeight()/2);

			if(this instanceof PhysicsObject) {
				audio.update(pos, ((PhysicsObject) this).getLinearVelocity());
			} else {
				audio.update(pos);
			}
		}

		for(final @NotNull GameObject child : this.getChildrenToAdd()) {
			this.addChildInstantly(child);
		}
		for(final @NotNull GameObject child : this.getChildrenToRemove()) {
			this.getChildren().remove(child);
			if(child instanceof PhysicsObject) {
				PhysicsEngine.removeObject((PhysicsObject) child);
			}
		}
		this.getChildrenToAdd().clear();
		this.getChildrenToRemove().clear();
	}

	@Override
	public void input() {
		for(final @NotNull GameObject object : this.getChildren()) {
			object.input();
		}

		for(final @NotNull GameComponent component : this.getComponents()) {
			component.input();
		}
	}

	/**
	 * Adds everything to the core engine.
	 *
	 * @param coreEngine Core engine to set
	 */
	public void addToEngine(final CoreEngine coreEngine) {
		this.coreEngine = coreEngine;

		for(final @NotNull GameObject object : this.getChildren()) {
			object.addToEngine(coreEngine);
		}

		for(final @NotNull GameComponent component : this.getComponents()) {
			component.addToEngine(coreEngine);
		}
	}

	@Override
	public @NotNull String toString() {
		String str = this.getName();

		str += " " + this.getTransform().getTransformedPosition().toString();
		str += "\n width: " + (this.getWidth() == Float.MAX_VALUE ? "∞" : this.getWidth()) + "; height: " + (this.getHeight() == Float.MAX_VALUE ? "∞" : this.getHeight());

		return super.toString() + " - " + str;
	}

	/**
	 * Returns the reference to the CoreEngine.
	 *
	 * @return GameObject.coreEngine
	 */
	@Contract(pure = true)
	final protected CoreEngine getCoreEngine() {
		return this.coreEngine;
	}

	/**
	 * Returns the GameObject's name.
	 *
	 * @return GameObject.name
	 */
	@Contract(pure = true)
	final public @NotNull String getName() {
		return this.name;
	}

	/**
	 * Returns the GameObject's list of children.
	 *
	 * @return GameObject.children
	 */
	@Contract(pure = true)
	final protected @NotNull ArrayList<GameObject> getChildren() {
		return this.children;
	}

	/**
	 * Returns whether the GameObject contains this child.
	 *
	 * @param child Child to check
	 * @return GameObject.children.contains(child)
	 */
	final protected boolean hasChild(final GameObject child) {
		return this.getChildren().contains(child);
	}

	/**
	 * Returns the GameObject's list of the children to add.
	 *
	 * @return GameObject.childrenToAdd
	 */
	@Contract(pure = true)
	private @NotNull ArrayList<GameObject> getChildrenToAdd() {
		return this.childrenToAdd;
	}

	/**
	 * Returns the GameObject's list of the children to remove.
	 *
	 * @return GameObject.childrenToRemove
	 */
	@Contract(pure = true)
	private @NotNull ArrayList<GameObject> getChildrenToRemove() {
		return this.childrenToRemove;
	}

	/**
	 * Returns the GameObject's list of components.
	 *
	 * @return GameObject.components
	 */
	@Contract(pure = true)
	final protected @NotNull ArrayList<GameComponent> getComponents() {
		return this.components;
	}

	/**
	 * Returns the GameObject's list of audios.
	 *
	 * @return GameObject.components
	 */
	@Contract(pure = true)
	final protected @NotNull ArrayList<AudioObject> getAudioObjects() {
		return this.audioObjects;
	}

	/**
	 * Returns the GameObject's transform.
	 *
	 * @return GameObject.transform
	 */
	protected @NotNull Transform getTransform() {
		return this.transform;
	} // TODO: Needs to be private, protected for debugging.

	/**
	 * Is the GameObject rendered if outside its parent.
	 *
	 * @return GameObject.renderOutsideParent
	 */
	@Contract(pure = true)
	final protected boolean renderOutsideParent() {
		return this.renderOutsideParent;
	}

	/**
	 * Returns the GameObject's width (in openGL units).
	 *
	 * @return GameObject.width
	 */
	@Contract(pure = true)
	final public float getWidth() {
		return this.width;
	}

	/**
	 * Returns the GameObject's height (in openGL units).
	 *
	 * @return return GameObject.height
	 */
	@Contract(pure = true)
	final public float getHeight() {
		return this.height;
	}

	/**
	 * Returns a copy of the GameObject's position.
	 *
	 * @return GameObject.transform.getTransformedPosition()
	 */
	final public @NotNull Vector2f getPosition() {
		return this.getTransform().getTransformedPosition();
	}

	/**
	 * Returns a copy of the GameObject's non-transformed position.
	 *
	 * @return GameObject.transform.getPosition()
	 */
	final public @NotNull Vector2f getObjectPosition() {
		return this.getTransform().getPosition();
	}

	/**
	 * Returns a reference to the GameObject's Transform's position.
	 *
	 * @return GameObject.transform.position
	 */
	final public @NotNull Vector3f getPositionReference() {
		return this.getTransform().getPositionReference();
	}

	/**
	 * Returns the GameObject's position on the screen (0 ; 0) is the bottom left corner (in OpenGL units).
	 *
	 * @return new Vector2f | null if not in screen
	 */
	final protected @Nullable Vector2f getPositionOnScreen() {
		if(this.getCoreEngine() == null) {
			System.err.println("Error: Core Engine has not been set and call on getPositionOnScreen()");
			System.err.println(this + "\n");
			new Exception().printStackTrace();
			return null;
		}

		final Vector2f cameraPosition = this.getCoreEngine().getRenderingEngine().getMainCamera().getPos();
		final Vector2f bottomLeftCorner = cameraPosition.sub(Window.getRatio(), 1);
		final Vector2f topRightCorner = cameraPosition.add(Window.getRatio(), 1);
		final Vector2f selfPosition = this.getPosition();

		if(selfPosition.getX() > topRightCorner.getX() || selfPosition.getY() > topRightCorner.getY()) {
			return null;
		}
		if(selfPosition.getX() + this.getWidth() < bottomLeftCorner.getX() || selfPosition.getY() + this.getHeight() < bottomLeftCorner.getY()) {
			return null;
		}

		return selfPosition.sub(bottomLeftCorner);
	}

	/**
	 * Returns if the GameObject is in the screen.
	 *
	 * @return new boolean
	 */
	final public boolean isInScreen() {
		return this.getPositionOnScreen() != null;
	}

	/**
	 * Returns if the GameObject is in another GameObject (on the screen).
	 *
	 * @param object Object to check with
	 * @return new boolean
	 */
	final public boolean isInObjectFrame(final @NotNull GameObject object) {
		if(this.getPosition().getX() > object.getPosition().getX() + object.getWidth()) {
			return false;
		} else if(this.getPosition().getX() + this.getWidth() < object.getPosition().getX()) {
			return false;
		} else if(this.getPosition().getY() > object.getPosition().getY() + object.getHeight()) {
			return false;
		} else if(this.getPosition().getY() + this.getHeight() < object.getPosition().getY()) {
			return false;
		}

		return true;
	}

	/**
	 * Returns whether the mouse is over the GameObject.
	 *
	 * @return new boolean
	 */
	final public boolean isMouseOver() {
		final Vector2f screenPosition = this.getPositionOnScreen();

		if(screenPosition == null) {
			return false;
		}

		final Vector2f mousePosition = Units.pixelsToOpenGL(Input.getMousePosition());

		return mousePosition.getX() >= screenPosition.getX() && mousePosition.getY() >= screenPosition.getY() && mousePosition.getX() <= screenPosition.getX() + this.getWidth() && mousePosition.getY() <= screenPosition.getY() + this.getHeight();
	}

	/**
	 * Adds a child to the GameObject.
	 *
	 * @param child GameObject to add
	 */
	final public void addChild(final @NotNull GameObject child) {
		this.getChildrenToAdd().add(child);
	}

	/**
	 * Adds a child to the GameObject right away.
	 *
	 * @param child GameObject to add
	 */
	final protected void addChildInstantly(final @NotNull GameObject child) {
		this.getChildren().add(child);

		if(this.getCoreEngine() != null) {
			child.addToEngine(this.getCoreEngine());
		}
		child.getTransform().setParent(this.getTransform());

		if(child instanceof PhysicsObject) {
			PhysicsEngine.addObject((PhysicsObject) child);
		}
	}

	/**
	 * Adds a component to the GameObject.
	 *
	 * @param component GameComponent to add
	 */
	final protected void addComponent(final @NotNull GameComponent component) {
		this.getComponents().add(component);

		if(this.getCoreEngine() != null) {
			component.addToEngine(this.getCoreEngine());
		}

		component.setParent(this, this.getTransform());
	}

	/**
	 * Adds an audio to the GameObject (so that the position/velocity is automatically updated).
	 *
	 * @param audioObject AudioObject to add
	 */
	final protected void addAudioObject(final @NotNull AudioObject audioObject) {
		this.getAudioObjects().add(audioObject);
	}

	/**
	 * Removes a child from the GameObject.
	 *
	 * @param child Child to remove
	 */
	final public void removeChild(final GameObject child) {
		this.getChildrenToRemove().add(child);
	}

	/**
	 * Removes a component from the GameObject.
	 *
	 * @param component Component to remove
	 */
	final protected void removeComponent(final GameComponent component) {
		this.getComponents().remove(component);
	}

	/**
	 * Sets whether or not the GameObject is rendered outside its parent.
	 *
	 * @param enabled true = rendered
	 */
	final protected void setRenderOutsideParent(final boolean enabled) {
		this.renderOutsideParent = enabled;
	}

	/**
	 * Sets the GameComponent's width.
	 *
	 * @param width Width to set
	 */
	protected void setWidth(final float width) {
		this.width = width;
	}

	/**
	 * Sets the GameComponent's height.
	 *
	 * @param height Height to set
	 */
	protected void setHeight(final float height) {
		this.height = height;
	}

	/**
	 * Sets the GameObject's Transform's position.
	 *
	 * @param position Position to set
	 */
	final public void setPosition(final @NotNull Vector2f position) {
		this.getTransform().setPosition(position);
	}

	/**
	 * Sets the GameObject's Transform's rotation.
	 *
	 * @param rotation Rotation to et
	 */
	final protected void setRotation(final double rotation) {
		this.getTransform().setRotation(rotation);
	}

	/**
	 * Sets the GameObject's Transform's scale.
	 *
	 * @param scale Scale to set
	 */
	final protected void setScale(final @NotNull Vector2f scale) {
		this.getTransform().setScale(scale);
	}

	/**
	 * Sets the GameObject's Transform's depth.
	 *
	 * @param depth Depth to set
	 */
	final protected void setDepth(final float depth) {
		this.getTransform().setDepth(depth);
	}

}