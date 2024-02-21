package engine.rendering;

import engine.game.components.Camera;
import engine.game.components.GameComponent;
import engine.game.components.lights.BaseLight;
import engine.game.objects.GameObject;
import engine.math.Vector3f;
import engine.rendering.shader.forward.AmbientShader;
import engine.rendering.shader.Shader;
import engine.util.Color;
import engine.util.Window;
import engine.util.profiling.Profiler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;

final public class RenderingEngine {

	/**
	 * Game's main camera to render with.
	 */
	private @NotNull Camera mainCamera;

	/**
	 * Environment ambient light.
	 */
	private @NotNull Color ambientLight;

	/**
	 * Environment's lights.
	 */
	final private @NotNull ArrayList<BaseLight> lights;

	/**
	 * RenderingEngine's active light to render with.
	 */
	private BaseLight activeLight;

	/**
	 * Creates a new RenderingEngine instance.
	 */
	public RenderingEngine() {
		setClearScreenColor(Color.BLACK);

		glDisable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);

		RenderingEngine.setTextures(true);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		this.mainCamera = new Camera();
		//this.ambientLight = new Color(15, 15, 15);
		this.ambientLight = new Color(255, 255, 255, 1);
		this.lights = new ArrayList<>();
	}

	/**
	 * Renders a object (and all his children/components).
	 *
	 * @param object Object to render
	 */
	public void render(final @NotNull GameObject object) {
		Profiler.startProfileTimer("Render-WindowClear");
		Window.bindAsRenderTarget();
		RenderingEngine.clearScreen();
		Profiler.stopProfileTimer("Render-WindowClear");

		final Shader ambientShader = AmbientShader.getInstance();
		ambientShader.setRenderingEngine(this);

		final ArrayList<GameComponent> renderLater = new ArrayList<>();
		object.render(ambientShader, renderLater);
		for(final @NotNull GameComponent component : renderLater) {
			component.render(ambientShader, null);
		}

		/*glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);

		for(final BaseLight light : this.getLights()) {
			this.setActiveLight(light);
			final Shader shader = light.getShader();
			shader.setRenderingEngine(this);
			object.render(shader);
		}

		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);*/
	}

	/**
	 * Returns the environment ambient light.
	 *
	 * @return RenderingEngine.ambientLight
	 */
	@Contract(pure = true)
	public @NotNull Color getAmbientLight() {
		return this.ambientLight;
	}

	/**
	 * Returns the environment lights.
	 *
	 * @return RenderingEngine.lights
	 */
	@Contract(pure = true)
	private @NotNull ArrayList<BaseLight> getLights() {
		return this.lights;
	}

	/**
	 * Returns the RenderingEngine's active light to render with.
	 *
	 * @return RenderingEngine.activeLight
	 */
	@Contract(pure = true)
	public BaseLight getActiveLight() {
		return this.activeLight;
	}

	/**
	 * Adds a light to the environment.
	 *
	 * @param light Light to add
	 */
	public void addLight(final BaseLight light) {
		this.getLights().add(light);
	}

	/**
	 * Sets the environment ambient light.
	 *
	 * @param ambientLight Color to set
	 */
	private void setAmbientLight(final @NotNull Color ambientLight) {
		this.ambientLight = ambientLight;
	}

	/**
	 * Sets the RenderingEngine's active light to render with.
	 *
	 * @param activeLight Light to set
	 */
	private void setActiveLight(final BaseLight activeLight) {
		this.activeLight = activeLight;
	}

	/**
	 * Returns the game's main camera.
	 *
	 * @return RenderingEngine.mainCamera
	 */
	@Contract(pure = true)
	public @NotNull Camera getMainCamera() {
		return this.mainCamera;
	}

	/**
	 * Sets the game's main camera.
	 *
	 * @param camera Camera to set
	 */
	public void setMainCamera(final @NotNull Camera camera) {
		this.mainCamera = camera;
	}

	/**
	 * Returns the OpenGL version currently used.
	 *
	 * @return OpenGL version.
	 */
	@Contract(pure = true)
	public static @NotNull String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	/**
	 * Clears everything on the screen.
	 */
	private static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Sets the color that will we used to clear the screen in RenderingEngine.clearScreen().
	 *
	 * @param color Color to set
	 */
	private static void setClearScreenColor(final @NotNull Color color) {
		final Vector3f c = color.toVector3f();
		glClearColor(c.getX(), c.getY(), c.getZ(), color.getAlpha());
	}

	/**
	 * Enable/disable the textures.
	 *
	 * @param enabled true=enable, false=disable
	 */
	private static void setTextures(final boolean enabled) {
		if(enabled) glEnable(GL_TEXTURE_2D);
		else        glDisable(GL_TEXTURE_2D);
	}

}