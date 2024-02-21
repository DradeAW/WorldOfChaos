package engine.audio;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.lwjgl.openal.AL10.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

class AudioData {

	/**
	 * Audio's data.
	 */
	final private @NotNull ByteBuffer data;

	/**
	 * Audio's format.
	 */
	final private int format;

	/**
	 * Audio's sample rate (ex: 44100Hz).
	 */
	final private int sampleRate;

	/**
	 * Creates a new AudioData's instance.
	 * Not callable: use AudioData::create()
	 *
	 * @param data Audio's data
	 * @param format Audio's format
	 * @param sampleRate Audio's sample rate
	 */
	private AudioData(final @NotNull ByteBuffer data, final int format, final int sampleRate) {
		this.data = data;
		this.format = format;
		this.sampleRate = sampleRate;
	}

	/**
	 * Returns the audio's data.
	 *
	 * @return AudioData.data
	 */
	@Contract(pure = true)
	final @NotNull ByteBuffer getData() {
		return this.data;
	}

	/**
	 * Returns the audio's format.
	 *
	 * @return AudioData.format
	 */
	@Contract(pure = true)
	final int getFormat() {
		return this.format;
	}

	/**
	 * Returns the audio's sample rate.
	 *
	 * @return AudioData.sampleRate
	 */
	@Contract(pure = true)
	final int getSampleRate() {
		return this.sampleRate;
	}

	/**
	 * Clear the audio's data buffer.
	 */
	final void dispose() {
		this.getData().clear();
	}

	/**
	 * Returns an AudioData corresponding to a file.
	 * Returns null if there was a problem while loading the audio file.
	 *
	 * @param path File's path (without /media/audio/ ; extension required)
	 * @return new AudioData
	 */
	public static @Nullable	AudioData create(final @NotNull String path) {
		try {
			return create(AudioSystem.getAudioInputStream(support.File.getURL("/media/audio/" + path)));
		} catch(final IOException | UnsupportedAudioFileException e) {
			System.err.println("Error while loading audio file: " + path);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns an AudioData corresponding to the input.
	 * Returns null if there is a problem while reading the audio input.
	 *
	 * @param input AudioInputStream
	 * @return new AudioData
	 */
	public static @Nullable AudioData create(final @NotNull AudioInputStream input) {
		final AudioFormat format = input.getFormat();

		// Getting the channels
		final int sampleSize = format.getSampleSizeInBits();
		int channels = 0;

		switch(format.getChannels()) {
			case 1:
				if(sampleSize == 8) channels = AL_FORMAT_MONO8;
				else if(sampleSize == 16) channels = AL_FORMAT_MONO16;
				else
					assert false : "Error: illegal sample size, only accept mono 8 and 16.";
				break;
			case 2:
				if(sampleSize == 8) channels = AL_FORMAT_STEREO8;
				else if(sampleSize == 16) channels = AL_FORMAT_STEREO16;
				else
					assert false : "Error: illegal sample size, only accept stereo 8 and 16.";
				break;
			default:
				assert false : "Error: illegal number of channels, only accept mono and stereo.";
		}

		// Reading data into a buffer
		final ByteBuffer buffer;
		try {
			int available = input.available();
			int read, total = 0;
			if(available <= 0) {
				available = format.getChannels() * (int) input.getFrameLength() * sampleSize / 8;
			}
			final byte[] buf = new byte[available];

			while((read = input.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
				total += read;
			}
			buffer = AudioData.convertAudioBytes(buf, sampleSize == 16, format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
			input.close();
		} catch(final IOException e) {
			System.err.println("Error while loading audio file");
			e.printStackTrace();
			return null;
		}

		return new AudioData(buffer, channels, (int) format.getSampleRate());
	}

	/**
	 * I have no idea what this function does, copy paste from WaveData in newest lwjgl version.
	 *
	 * @param audio_bytes Audio bytes read from input
	 * @param two_bytes_data True = stereo
	 * @param order Order
	 * @return new ByteBuffer
	 */
	private static @NotNull ByteBuffer convertAudioBytes(final byte[] audio_bytes, final boolean two_bytes_data, final @NotNull ByteOrder order) {
		final ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
		dest.order(ByteOrder.nativeOrder());
		final ByteBuffer src = ByteBuffer.wrap(audio_bytes);
		src.order(order);
		if (two_bytes_data) {
			final ShortBuffer dest_short = dest.asShortBuffer();
			final ShortBuffer src_short = src.asShortBuffer();
			while (src_short.hasRemaining())
				dest_short.put(src_short.get());
		} else {
			while (src.hasRemaining())
				dest.put(src.get());
		}
		dest.rewind();
		return dest;
	}

}