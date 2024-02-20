package engine.game.components;

import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.util.Window;
import org.jetbrains.annotations.NotNull;

final public class Camera extends GameComponent {

	/**
	 * Camera's projection matrix.
	 */
	private Matrix4f projectionMatrix;

	/**
	 * Camera's last position.
	 */
	private Vector2f lastPos;

	/**
	 * Creates a new Camera instance.
	 */
	public Camera() {
		super(0, 0);
	}

	@Override
	public @NotNull Camera init() {
		this.updateProjectionMatrix();

		return this;
	}

	/**
	 * Returns the Camera's position.
	 *
	 * @return Camera.transform.getTransformedPosition()
	 */
	public Vector2f getPos() {
		return this.getTransform().getTransformedPosition();
	}

	/**
	 * Returns the Camera's position reference.
	 *
	 * @return Camera.transform.position
	 */
	public Vector3f getPositionReference() {
		return this.getTransform().getPositionReference();
	}

	/**
	 * Returns the Camera's projection matrix.
	 *
	 * @return Camera.projectionMatrix
	 */
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	/**
	 * Returns if the Camera ahs moved on this frame.
	 *
	 * @return Camera has moved
	 */
	public boolean hasMoved() {
		return (this.lastPos == null) || (!this.lastPos.equals(this.getPos()));
	}

	/**
	 * Updates the Camera's projection matrix.
	 */
	private void updateProjectionMatrix() {
		final Matrix4f cameraMatrix     = new Matrix4f().initPosition(-this.getPos().getX(), -this.getPos().getY(), 0);
		final Matrix4f projectionMatrix = new Matrix4f().initProjection(Window.getRatio());

		this.projectionMatrix = projectionMatrix.mul(cameraMatrix);
	}

	@Override
	public void update(final double delta) {
		if(this.hasMoved()) this.updateProjectionMatrix();

		this.lastPos = new Vector2f(this.getPos());
	}

}