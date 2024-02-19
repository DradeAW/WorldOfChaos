package engine.audio;

import com.GameOptions;
import com.objects.characters.Hero;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
//import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.AL10.alListenerf;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE_CLAMPED;

final public class AudioEngine {

	/**
	 * Creates a new AudioEngine instance.
	 */
	public AudioEngine() {
		try {
			AL.create();
		} catch(final LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		alDistanceModel(AL_LINEAR_DISTANCE_CLAMPED);
	}

	/**
	 * Updates the AudioEngine.
	 */
	final public void update() {
		alListenerf(AL_GAIN, GameOptions.getSlide("generalVolume").getValue()*0.01f);
		if(Hero.getInstance() != null) {
			alListener3f(AL_POSITION, Hero.getInstance().getPosition().getX(), Hero.getInstance().getPosition().getY(), 0.0f);
			alListener3f(AL_VELOCITY, Hero.getInstance().getLinearVelocity().getX(), Hero.getInstance().getLinearVelocity().getY(), 0.0f);
			//alListener3f(AL_ORIENTATION, 0.0f, 0.0f, 0.0f); // TODO (Warning: putting 0, 0, 0 generates alGetError)
		} else {
			alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f);
		}

		if(alGetError() != AL_NO_ERROR) {
			System.err.println("Error in the audio engine");
			new Exception().printStackTrace();
		}
	}

	/**
	 * Called when application closes.
	 */
	final public void cleanUp() {
		AL.destroy();
	}

}