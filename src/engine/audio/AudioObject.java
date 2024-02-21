package engine.audio;

import com.GameOptions;
import engine.math.Vector2f;
import org.jetbrains.annotations.NotNull;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_MAX_DISTANCE;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_REFERENCE_DISTANCE;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;

public class AudioObject {

	/**
	 * ID of the buffer (containing the sound data).
	 */
	final private int buffer;

	/**
	 * ID of the source (where the sound is emitted).
	 */
	final private int source;

	/**
	 * Creates a new AudioObject instance.
	 *
	 * @param fileName File's name from the folder "media/audio" with the extension.
	 */
	public AudioObject(final String fileName) {
		this.buffer = alGenBuffers();
		assert alGetError() == AL_NO_ERROR : "Error while trying to generate buffer for audio.";  // Checking if the buffer was correctly made.

		// Loading the audio data and putting it in the buffer.
		final AudioData audioData = AudioData.create(fileName);
		assert audioData != null : "Error while loading audio's data.";

		alBufferData(this.buffer, audioData.getFormat(), audioData.getData(), audioData.getSampleRate());
		audioData.dispose();

		// Creates the source of the buffer.
		this.source = alGenSources();
		assert alGetError() == AL_NO_ERROR : "Error while trying to generate source for audio.";

		alSourcei(this.source, AL_BUFFER, this.buffer);
		alSourcef(this.source, AL_REFERENCE_DISTANCE, 1.0f); // TODO: Shouldn't be here.
		this.setVolume(1.0f);
	}

	/**
	 * Sets the audio's volume (where 1.0 is the normal volume).
	 *
	 * @param volume Volume to set
	 */
	public void setVolume(final float volume) {
		alSourcef(this.source, AL_GAIN, volume*GameOptions.getSlide("generalVolume").getValue());
	}

	/**
	 * Sets the audio's pitch (where 1.0 is the normal pitch).
	 *
	 * @param pitch Pitch to set
	 */
	public void setPitch(final float pitch) {
		alSourcef(this.source, AL_PITCH, pitch);
	}

	/**
	 * Tells whether the audio should loop.
	 *
	 * @param loops true = audio loops
	 */
	public void loops(final boolean loops) {
		alSourcei(this.source, AL_LOOPING, loops ? AL_TRUE : AL_FALSE);
	}

	/**
	 * Updates the audio's position.
	 *
	 * @param pos Position to set
	 */
	public void update(final @NotNull Vector2f pos) {
		alSource3f(this.source, AL_POSITION, pos.getX(), pos.getY(), 0.0f);
	}

	/**
	 * Updates the audio's position and velocity.
	 *
	 * @param pos Position to set
	 * @param velocity Velocity to set
	 */
	public void update(final @NotNull Vector2f pos, final @NotNull Vector2f velocity) {
		this.update(pos);
		alSource3f(this.source, AL_VELOCITY, velocity.getX(), velocity.getY(), 0.0f);
	}

	/**
	 * Plays the sound.
	 */
	public void play() {
		alSourcePlay(this.source);
	}

	/**
	 * Pauses the sound (if you call play(), it will continue where it left off).
	 */
	public void pause() {
		alSourcePause(this.source);
	}

	/**
	 * Stops the sound (if you call play(), it will restart from the beginning).
	 */
	public void stop() {
		alSourceStop(this.source);
	}

	/**
	 * Sets the maximum distance the sound can be heard.
	 *
	 * @param distance Distance to set
	 */
	public void setMaxDistance(final float distance) {
		alSourcef(this.source, AL_MAX_DISTANCE, distance);
	}

	@Override
	final protected void finalize() {
		try {
			super.finalize();
		} catch(final Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		alDeleteSources(this.source);
		alDeleteBuffers(this.buffer);
	}

}