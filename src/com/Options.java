package com;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public interface Options {

	/**
	 * Defines if the Engine is going to print things in the console
	 * to tell what's going on.
	 */
	final public static boolean DEBUG = true;

	/**
	 * Defines the Window width (in px)
	 * The Window border is not included.
	 */
	final public static int WINDOW_WIDTH = 464 * (GameOptions.getSelect("resolution").getIndex() + 1);

	/**
	 * Defines the Window height (in px)
	 * The Window border is not included.
	 */
	final public static int WINDOW_HEIGHT = 256 * (GameOptions.getSelect("resolution").getIndex() + 1);

	/**
	 * Defines the Window title.
	 */
	final public static @NotNull String WINDOW_TITLE = "World of Chaos Menu v0.0 Alpha";

	/**
	 * Defines the maximum frames that will be done in a second
	 * This human eye can see about 24 images a second so 30/60 is a good value for a ratio result/performances.
	 */
	final public static AtomicInteger MAX_FRAMES_PER_SECOND = GameOptions.getSlide("maxFPS").getValueReference();

	/**
	 * Size of a tile in openGL units (2 is the height of the window).
	 */
	final public static float TILE_SIZE = 0.1f;

	/**
	 * Defines the application's language.
	 */
	final public static @NotNull String LANGUAGE = "ENG";

}