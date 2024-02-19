package engine.game;

import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.util.profiling.Profiler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final public class Transform {

	/**
	 * Transform's position.
	 */
	final private @NotNull Vector3f position;

	/**
	 * Transform's rotation.
	 */
	private double rotation;

	/**
	 * Transform's scale.
	 */
	private @NotNull Vector2f scale;

	/**
	 * Transform has changed.
	 */
	private boolean hasChanged;

	/**
	 * Transform's transformation matrix.
	 */
	private Matrix4f transformation;

	/**
	 * Transform's parent.
	 */
	private @Nullable Transform parent;

	/**
	 * Creates a new Transform instance.
	 */
	public Transform() {
		this.position = new Vector3f();
		this.setRotation(0.0);
		this.scale = new Vector2f(1, 1);
		this.setDepth(0.0f);
		this.setHasChanged(true);

		this.setParent(null);
	}

	/**
	 * Returns the Transform's transformation matrix.
	 *
	 * @return Transform.transformation
	 */
	@Contract(pure = true)
	private Matrix4f getTransformation() {
		return this.transformation;
	}

	/**
	 * Returns the calculated transformation matrix.
	 *
	 * @return new Matrix4f
	 */
	final public @NotNull Matrix4f getTransformedTransformation() {
		final boolean profiling = !Profiler.isProfileTimerRunning("Render-TransformCalc.");

		if(profiling) {
			Profiler.startProfileTimer("Render-TransformCalc.");
		}

		if(this.hasChanged()) {
			final Matrix4f translation = new Matrix4f().initPosition(this.getPosition().getX(), this.getPosition().getY(), this.getDepth());
			final Matrix4f rotation = new Matrix4f().initRotation(0, 0, this.getRotation());
			final Matrix4f scale = new Matrix4f().initScale(this.getScale());
			this.setTransformation(translation.mul(rotation.mul(scale)));

			this.setHasChanged(false);
		}

		final Matrix4f parentMatrix;

		if(this.getParent() != null) {
			parentMatrix = this.getParent().getTransformedTransformation();
		} else {
			parentMatrix = new Matrix4f().initIdentity();
		}

		final Matrix4f result = parentMatrix.mul(this.getTransformation());

		if(profiling) {
			Profiler.stopProfileTimer("Render-TransformCalc.");
		}

		return result;
	}

	/**
	 * Returns a reference to the Transform's position.
	 *
	 * @return Transform.position
	 */
	@Contract(pure = true)
	public @NotNull Vector3f getPositionReference() {
		return this.position;
	}

	/**
	 * Returns a copy of the Transform's position.
	 *
	 * @return Transform.position.getXY()
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getPosition() {
		return this.position.asVector2f();
	}

	/**
	 * Returns the Transform's position added by all parent's position.
	 *
	 * @return new Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getTransformedPosition() {
		final Vector2f r = new Vector2f(this.getPosition());

		if(this.getParent() != null) {
			r.addition(this.getParent().getTransformedPosition());
		}

		return r;
	}

	/**
	 * Returns the Transform's rotation on the z axis.
	 *
	 * @return Transform.rotation
	 */
	@Contract(pure = true)
	public double getRotation() {
		return this.rotation;
	}

	/**
	 * Returns the Transform's rotation added by all parent's rotation.
	 *
	 * @return new double
	 */
	public double getTransformedRotation() {
		double r = this.getRotation();

		if(this.getParent() != null) {
			r += this.getParent().getTransformedRotation();
		}

		return r;
	}

	/**
	 * Returns a copy of the Transform's scale (x ; y) multiplicator (1 = nothing changes).
	 *
	 * @return Transform.scale
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getScale() {
		return new Vector2f(this.scale);
	}

	/**
	 * Returns the Transform's scale multiplied by all parent's scale.
	 *
	 * @return new Vector2f
	 */
	@Contract(pure = true)
	public @NotNull Vector2f getTransformScale() {
		final Vector2f r = new Vector2f(this.getScale());

		if(this.getParent() != null) {
			r.multiply(this.getParent().getTransformScale());
		}

		return r;
	}

	/**
	 * Returns the Transform's depth.
	 *
	 * @return Transform.position.z
	 */
	@Contract(pure = true)
	public float getDepth() {
		return this.position.getZ();
	}

	/**
	 * Returns the Transform's depth added by all parent's depth.
	 *
	 * @return new float
	 */
	@Contract(pure = true)
	public float getTransformedDepth() {
		float r = this.getDepth();

		if(this.getParent() != null) {
			r += this.getParent().getTransformedDepth();
		}

		return r;
	}

	/**
	 * Returns if the Transform has changed.
	 *
	 * @return Transform.hasChanged
	 */
	@Contract(pure = true)
	public boolean hasChanged() {
		return this.hasChanged;
	}

	/**
	 * Returns the Transform's parent.
	 *
	 * @return Transform.parent
	 */
	@Contract(pure = true)
	private @Nullable Transform getParent() {
		return this.parent;
	}

	/**
	 * Sets the Transform's position (the Vector will be copied !).
	 *
	 * @param position Position to set
	 */
	public void setPosition(final @NotNull Vector3f position) {
		if(!(new Vector3f(this.getPosition(), this.getDepth()).equals(position))) {
			this.position.set(position);

			this.setHasChanged(true);
		}
	}

	/**
	 * Sets the Transform's position (the Vector will be a copy !).
	 *
	 * @param position Position to set
	 */
	public void setPosition(final @NotNull Vector2f position) {
		if(!this.getPosition().equals(position)) {
			this.position.setXY(position);

			this.setHasChanged(true);
		}
	}

	/**
	 * Sets the Transform's position.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 */
	public void setPosition(final float x, final float y) {
		this.setPosition(new Vector2f(x, y));
	}

	/**
	 * Sets the Transform's position.
	 *
	 * @param x X position to set
	 * @param y Y position to set
	 * @param z Z position to set
	 */
	public void setPosition(final float x, final float y, final float z) {
		this.setPosition(new Vector3f(x, y, z));
	}

	/**
	 * Sets the Transform's rotation.
	 *
	 * @param rotation Rotation to set
	 */
	public void setRotation(final double rotation) {
		if(this.getRotation() != rotation) {
			this.rotation = rotation;

			this.setHasChanged(true);
		}
	}

	/**
	 * Sets the Transform's scale (Vector2f will be copied !).
	 *
	 * @param scale Scale to set
	 */
	public void setScale(final @NotNull Vector2f scale) {
		if(!this.getScale().equals(scale)) {
			this.scale = new Vector2f(scale);

			this.setHasChanged(true);
		}
	}

	/**
	 * Sets the Transform's scale.
	 *
	 * @param x X scale to set
	 * @param y Y scale to set
	 */
	public void setScale(final float x, final float y) {
		this.setScale(new Vector2f(x, y));
	}

	/**
	 * Sets the Transform's depth.
	 *
	 * @param depth Depth to set
	 */
	public void setDepth(final float depth) {
		if(this.getDepth() != depth) {
			this.position.setZ(depth);

			this.setHasChanged(true);
		}
	}

	/**
	 * Sets if the Transform has changed.
	 *
	 * @param hasChanged true = has changed
	 */
	private void setHasChanged(final boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	/**
	 * Sets the Transform's transformation matrix.
	 *
	 * @param transformation Matrix4f to set
	 */
	private void setTransformation(final Matrix4f transformation) {
		this.transformation = transformation;
	}

	/**
	 * Sets the Transform's parent.
	 *
	 * @param parentTransform Parent to set
	 */
	final public void setParent(final @Nullable Transform parentTransform) {
		this.parent = parentTransform;
	}

}