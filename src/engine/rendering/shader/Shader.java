package engine.rendering.shader;

import engine.game.Transform;
import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.rendering.texture.Material;
import engine.rendering.RenderingEngine;
import engine.util.BufferUtil;
import engine.util.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

public class Shader {

	/**
	 * Shader's program.
	 */
	final private int program;

	/**
	 * Shader's uniforms.
	 */
	final private @NotNull HashMap<String, Integer> uniforms;

	/**
	 * Pointer to the rendering engine.
	 */
	private RenderingEngine renderingEngine;

	/**
	 * Creates a new Shader instance.
	 *
	 * @param name Shader's name.
	 */
	public Shader(final @NotNull String name) {
		this.program = glCreateProgram();
		this.uniforms = new HashMap<>();

		assert this.program != 0 : "Error, " + name + " Shader's creation failed: Could not find valid memory location for program.";  // Invalid memory location.

		this.addVertexShader(Shader.load(name + ".vs"));
		this.addFragmentShader(Shader.load(name + ".fs"));

		this.addAllAttributes();

		this.compile();

		this.addAllUniforms();
	}

	@Override
	final protected void finalize() {
		try {
			super.finalize();
		} catch(final Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		// TODO
		// glDetachShader(this.program, vsId);
		// glDetachShader(this.program, fsId);

		// glDeleteShader(vsId);
		// glDeleteShader(fsId);
		glDeleteProgram(this.program);
	}

	/**
	 * Binds the Shader's program to tell OpenGL to use it.
	 */
	final public void bind() {
		glUseProgram(this.program);
	}

	/**
	 * Unbinds the current shader.
	 */
	public static void unbind() {
		glUseProgram(0);
	}

	/**
	 * Sets an attribute location for the Shader's program.
	 *
	 * @param location Attribute's location.
	 * @param attributeName Attribute's name
	 */
	final protected void setAttribLocation(final int location, final String attributeName) {
		glBindAttribLocation(this.program, location, attributeName);
	}

	/**
	 * Adds a Vertex Shader to the Shader's program.
	 *
	 * @param text Vertex shader's content
	 */
	final public void addVertexShader(final String text) {
		this.addToProgram(text, GL_VERTEX_SHADER);
	}

	/**
	 * Adds a Fragment Shader to the Shader's program.
	 *
	 * @param text Fragment shader's content
	 */
	final public void addFragmentShader(final String text) {
		this.addToProgram(text, GL_FRAGMENT_SHADER);
	}

	/**
	 * Adds a Geometry Shader to the Shader's program.
	 *
	 * @param text Vertex shader's content
	 */
	final public void addGeometryShader(final String text) {
		this.addToProgram(text, GL_GEOMETRY_SHADER);
	}

	/**
	 * Adds some Shader's code to the Shader's program.
	 *
	 * @param text Code/content to add
	 * @param type Shader's type (vertex, fragment, geometry)
	 */
	private void addToProgram(final String text, final int type) {
		final int shader = glCreateShader(type);
		assert shader != 0 : "Error: Shader creation failed: Could not find valid memory location when adding shader.";

		glShaderSource(shader, text);
		glCompileShader(shader);
		assert glGetShaderi(shader, GL_COMPILE_STATUS) != 0 : glGetShaderInfoLog(shader, 1024);

		glAttachShader(this.program, shader);
	}

	/**
	 * Compiles the Shader.
	 */
	final public void compile() {
		glLinkProgram(this.program);
		assert glGetProgrami(this.program, GL_LINK_STATUS) != 0 : glGetProgramInfoLog(this.program, 1024);

		glValidateProgram(this.program);
		assert glGetProgrami(this.program, GL_VALIDATE_STATUS) != 0 : glGetProgramInfoLog(this.program, 1024);
	}

	/**
	 * Updates all the Shader's uniforms.
	 *
	 * @param material Material
	 * @param transform transform class
	 */
	public void updateUniforms(final @NotNull Material material, final @NotNull Transform transform) {
		material.getTexture().bind();

		for(final String uniformName : this.uniforms.keySet()) {
			switch(uniformName) {
				case "transform" :
					this.setUniform("transform", transform.getTransformedTransformation());
					break;
				case "transformProjected" :
					final Matrix4f projection = this.getRenderingEngine().getMainCamera().getProjectionMatrix();
					this.setUniform("transformProjected", projection.mul(transform.getTransformedTransformation()));
					break;
				case "materialColor" :
					this.setUniform("materialColor", material.getColor());
					break;
			}
		}
	}

	/**
	 * Returns the rendering engine.
	 *
	 * @return Shader.renderingEngine
	 */
	final protected @NotNull RenderingEngine getRenderingEngine() {
		return this.renderingEngine;
	}

	/**
	 * Sets the rendering engine.
	 *
	 * @param renderingEngine Rendering engine to set
	 */
	final public void setRenderingEngine(final @NotNull RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}

	/**
	 * Adds an uniform to the Shader.
	 *
	 * @param uniformName Uniform's name
	 */
	final public void addUniform(final @NotNull String uniformName) {
		final int uniformLocation = glGetUniformLocation(this.program, uniformName);
		assert uniformLocation != 0xFFFFFFFF : "Error: Could not find uniform: " + uniformName;  // Uniform not found.

		this.uniforms.put(uniformName, uniformLocation);
	}

	/**
	 * Adds all the uniforms contained in the Shader.
	 */
	final protected void addAllUniforms() {
		for(int i = 0; i < glGetProgrami(this.program, GL_ACTIVE_UNIFORMS); i++) {
			this.addUniform(glGetActiveUniform(this.program, i, 128));
		}
	}

	/**
	 * Adds all the attributes contained in the Shader.
	 */
	final protected void addAllAttributes() {
		for(int i = 0; i < glGetProgrami(this.program, GL_ACTIVE_ATTRIBUTES); i++) {
			this.setAttribLocation(i, glGetActiveAttrib(this.program, i, 128));
		}
	}

	/**
	 * Sets an Integer into an uniform.
	 *
	 * @param uniformName Uniform's name
	 * @param value Uniform's value to put
	 */
	final public void setUniform(final @NotNull String uniformName, final int value) {
		glUniform1i(this.uniforms.get(uniformName), value);
	}

	/**
	 * Sets a Float into an uniform.
	 *
	 * @param uniformName Uniform's name
	 * @param value Uniform's value to put
	 */
	final public void setUniform(final @NotNull String uniformName, final float value) {
		glUniform1f(this.uniforms.get(uniformName), value);
	}

	/**
	 * Sets a Vector2f into an uniform.
	 *
	 * @param uniformName Uniform's name
	 * @param value Uniform's values to put
	 */
	final public void setUniform(final @NotNull String uniformName, final @NotNull Vector2f value) {
		glUniform2f(this.uniforms.get(uniformName), value.getX(), value.getY());
	}

	/**
	 * Sets a Matrix4f into an uniform.
	 *
	 * @param uniformName Uniform's name
	 * @param value Uniform's values to put
	 */
	final public void setUniform(final @NotNull String uniformName, final @NotNull Matrix4f value) {
		glUniformMatrix4(this.uniforms.get(uniformName), true, BufferUtil.createFlippedBuffer(value));
	}

	/**
	 * Sets a Color into an uniform.
	 *
	 * @param uniformName Uniform's name
	 * @param value Uniform's value to put
	 */
	final public void setUniform(final @NotNull String uniformName, final @NotNull Color value) {
		this.setUniform(uniformName, value.toVector3f(), value.getAlpha());
	}

	/**
	 * Sets a Vector4f/Color into an uniform.
	 *
	 * @param uniformName Uniform's name
	 * @param value Uniform's value to put
	 * @param value2 Uniform's second value to put
	 */
	final public void setUniform(final @NotNull String uniformName, final @NotNull Vector3f value, final float value2) {
		glUniform4f(this.uniforms.get(uniformName), value.getX(), value.getY(), value.getZ(), value2);
	}

	/**
	 * Loads the Shader's source code from a file.
	 *
	 * @param filename File name
	 * @return File content (source code)
	 */
	private static @NotNull String load(final @NotNull String filename) {
		final String INCLUDE_DIRECTIVE = "#include";

		final StringBuilder shaderSource = new StringBuilder();

		final ArrayList<String> lines = support.File.getLinesFromFile("/media/shader/" + filename);
		for(final String line : lines) {
			if(!line.isEmpty() && line.charAt(0) == '#' && !line.startsWith("#version")) {
				if(line.startsWith(INCLUDE_DIRECTIVE)) {
					String filenameToInclude = "";
					boolean include = false;
					for(int i = INCLUDE_DIRECTIVE.length(); i < line.length(); i++) {
						if(include) {
							if(line.charAt(i) == '\"')  include = false;
							else                        filenameToInclude += line.charAt(i);
						} else if(line.charAt(i) == '\"') {
							include = true;
						}
					}
					shaderSource.append(Shader.load(filenameToInclude));
				} else {
					System.err.println("Error: this line cannot be understood by the engine:\n" + line + "\nFound in " + filename + " ; line has been skipped");
				}
			} else {
				shaderSource.append(line).append("\n");
			}
		}

		return shaderSource.toString();
	}

}