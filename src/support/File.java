package support;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

final public class File {

	/**
	 * Get an image according to the filename, cuts the program if fails.
	 *
	 * @param filename Image's name
	 * @return BufferedImage loaded
	 */
	public static BufferedImage getImage(final @NotNull String filename){
		return getImage(filename, true);
	}

	/**
	 * Gets an ImageIcon according to the filename, cuts the program if fails.
	 *
	 * @param filename Images name
	 * @return ImageIcon loaded
	 */
	public static ImageIcon getImageIcon(final @NotNull String filename){
		return getImageIcon(filename, true);
	}

	/**
	 * Gets an ImageIcon according to the filename, cuts the program if fails.
	 *
	 * @param filename Image's name
	 * @param stop Is the program cut if the getImageIcon fails ?
	 * @return ImageIcon loaded
	 */
	public static ImageIcon getImageIcon(final @NotNull String filename, final boolean stop){
		return new ImageIcon(getImage(filename, stop));
	}

	/**
	 * Gets an image according to the filename.
	 *
	 * @param filename Image's name
	 * @param stop Is the program cut if the getImage fails ?
	 * @return BufferedImage loaded
	 */
	public static BufferedImage getImage(final @NotNull String filename, final boolean stop){
		try {
			return ImageIO.read(File.class.getResourceAsStream(filename));
		} catch(final Exception e) {
			if(stop) {
				System.err.println("ERROR in File.getImage() with filename = " + filename + " & stop = true");
				e.printStackTrace();
				System.exit(1);
			}
		}
		return null;
	}

	/**
	 * Returns the file's extension.
	 *
	 * @param filename File's name
	 * @return File's extension
	 */
	public static @NotNull String getExtension(final @NotNull String filename){
		final String[] split = filename.split("\\.");
		return split[split.length - 1];
	}

	/**
	 * Returns a StringBuilder with all the lines of a file.
	 *
	 * @param filename File's name
	 * @return StringBuilder loaded
	 */
	public static @NotNull StringBuilder getStringBuilderFromFile(final @NotNull String filename){
		final StringBuilder sb = new StringBuilder();

		final InputStream is = File.class.getResourceAsStream(filename);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while((line = reader.readLine()) != null){
				sb.append(line).append("\n");
			}
			reader.close();
		} catch(final Exception e) {
			System.err.println("ERROR in File.getStringBuilderFromFile() with filename = " + filename);
			e.printStackTrace();
			System.exit(1);
		}

		return sb;
	}

	/**
	 * Return an ArrayList of ArrayList of Characters contained in the file as r.get(line).get(character).
	 *
	 * @param filename File's name to load
	 * @return Characters loaded
	 */
	public static @NotNull ArrayList<ArrayList<Character>> getCharactersFromTextFile(@NotNull String filename) {
		if(filename.length() > 4 && !filename.substring(filename.length() - 4).equals(".txt")) {
			filename += ".txt";
		} else if(filename.length() <= 4) {
			filename += ".txt";
		}
		final ArrayList<ArrayList<Character>> r = new ArrayList<>();
		String line;
		int i = 0;
		try {
			final InputStream is = File.class.getResourceAsStream(filename);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			while((line = reader.readLine()) != null) {
				r.add(new ArrayList<>());
				for(int ii = 0; ii < line.length(); ii++) {
					r.get(i).add(line.charAt(ii));
				}
				i++;
			}
			reader.close();
		} catch(final Exception e) {
			System.err.println("ERROR in File.getCharactersFromTextFile() with filename = " + filename);
			e.printStackTrace();
			System.exit(1);
		}
		return r;
	}

	/**
	 * Returns an ArrayList of string with all the lines one by one.
	 *
	 * @param filename File's name to read
	 * @return Lines from a file
	 */
	public static @NotNull ArrayList<String> getLinesFromFile(final @NotNull String filename){
		final ArrayList<String> lines = new ArrayList<>();
		String line;
		try{
			final InputStream is = File.class.getResourceAsStream(filename);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((line = reader.readLine()) != null){
				lines.add(line);
			}
			reader.close();
		} catch(final Exception e) {
			System.err.println("ERROR in File.getLinesFromFile() with filename = " + filename);
			e.printStackTrace();
			System.exit(1);
		}

		return lines;
	}

	/**
	 * Writes some text into a file.
	 *
	 * @param fileName File's name
	 * @param text Text to print
	 */
	public static void writeToFile(final @NotNull String fileName, final @NotNull String text) {
		final String folderPath;
		String temp = new String(fileName);

		// Loops to find the last existing directory to mkdir.
		while(true) {
			final int lastSlash = temp.lastIndexOf('/');
			assert lastSlash != -1 : "Error: Could not write to file \"" +  fileName + "\"\nFolder not found.";

			final String parentName = temp.substring(0, lastSlash);
			final URL url = File.getURL(parentName);

			if(url == null) {
				temp = parentName;

				continue;
			}

			folderPath = url.toString().substring(5) + fileName.substring(lastSlash, fileName.lastIndexOf('/'));
			break;
		}

		final java.io.File folder = new java.io.File(folderPath);
		folder.mkdirs();
		assert folder.exists() : "Error: Couldn't create folder to write to file \"" + fileName + "\"";

		final java.io.File file = new java.io.File(folderPath + fileName.substring(fileName.lastIndexOf('/')));

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(final IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		try {
			final FileWriter fileWriter = new FileWriter(file);
			final BufferedWriter writer = new BufferedWriter(fileWriter);

			writer.write(text);
			writer.close();
		} catch(final IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Returns an URL object from a filename.
	 *
	 * @param fileName File name to read
	 * @return new URL
	 */
	public static URL getURL(final @NotNull String fileName) {
		return File.class.getResource(fileName);
	}

	/**
	 * Makes all directories if they don't exist to a given file or folder.
	 *
	 * @param file File or folder's
	 * @return true if directories were created.
	 */
	public static boolean makeDirectories(final @NotNull java.io.File file) {
		if(!file.isDirectory()) {
			return file.getParentFile().mkdirs();
		} else {
			return file.mkdirs();
		}
	}

	/**
	 * Returns whether a file exists or not.
	 *
	 * @param fileName File name to read
	 * @return new boolean
	 */
	public static boolean exists(final @NotNull String fileName) {
		return File.getURL(fileName) != null;
	}

	/**
	 * Returns a folder. Returns null if it does not point to a folder.
	 *
	 * @param folderName Folder's name to read
	 * @return new File
	 */
	public static @Nullable	java.io.File getFolder(final @NotNull String folderName) {
		final URL path = File.getURL(folderName);

		if(path == null) return null;
		final java.io.File folder = new java.io.File(path.getFile());

		return folder.isDirectory() ? folder : null;
	}

	/**
	 * Returns if a folder is empty, returns true if the folderName does not lead to a directory.
	 *
	 * @param folderName Folder's name to read
	 * @return new boolean
	 */
	public static boolean isEmpty(final @NotNull String folderName) {
		final java.io.File folder = File.getFolder(folderName);

		return folder == null || folder.list().length == 0;
	}

	/**
	 * Returns if a folder has at least one folder/directory.
	 *
	 * @param folderName Folder's name to read
	 * @return new boolean
	 */
	public static boolean hasFolders(final @NotNull String folderName) {
		final java.io.File folder = File.getFolder(folderName);

		if(folder == null) {
			return false;
		}

		for(final java.io.File file : folder.listFiles()) {
			if(file.isDirectory()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Serializes an object in a file.
	 *
	 * @param obj Object to serialize
	 * @param filename File's name to write in
	 */
	public static void serialize(final @NotNull Object obj, final @NotNull String filename) {
		final java.io.File file = new java.io.File(filename); // TODO: Check if this works (normally, I use an URL).
		makeDirectories(file);

		final ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(obj);
			oos.close();
		} catch(final Exception e) {
			System.err.println("ERROR in File.serialize() with filename = " + filename + " & obj = " + obj);
			e.printStackTrace();
		}
	}

	/**
	 * Unserializes an object contained in a file.
	 *
	 * @param filename File's name to read in
	 * @return Object unserializes
	 */
	public static Object unserialize(final @NotNull String filename){
		try {
			final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new java.io.File(filename))));
			final Object obj =  ois.readObject();
			ois.close();
			return obj;
		} catch(final Exception e) {
			System.err.println("ERROR in File.unserialize() with filename = " + filename);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Plays a music.
	 *
	 * @param filename Music file to play
	 * @param repeat Should the music be repeated continuously when done ?
	 */
	public static void playMusic(final @NotNull String filename, final boolean repeat){
		final Clip clip = File.getMusic(filename, repeat);
		clip.start();
	}

	/**
	 * Returns a music.
	 *
	 * @param filename Music file to get
	 * @param repeat Should the music be repeated continuously when done ?
	 * @return Music gotten
	 */
	public static Clip getMusic(final @NotNull String filename, final boolean repeat){
		Clip clip = null;
		try {
			final InputStream is = File.class.getResourceAsStream(filename);
			final AudioInputStream ais = AudioSystem.getAudioInputStream(is);
			clip = AudioSystem.getClip();
			clip.open(ais);
			if(repeat) clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch(final Exception e) {
			System.err.println("ERROR in File.getMusic() with filename = " + filename + " & repeat = " + repeat);
			System.err.println(filename);
			e.printStackTrace();
		}
		return clip;
	}

}