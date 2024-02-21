package engine.util;

import com.GameOptions;
import engine.math.Vector2f;
import engine.util.profiling.Profiler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

final public class Input {

	/**
	 * Numbers of keys to check every frame
	 * The bigger this number is, the more keys you
	 * can get but the slower the application will be.
	 */
	final public static int NUM_KEYCODES = 256;

	/**
	 * Numbers of mouse buttons to check every frame
	 * The bigger this number is, the more mouse buttons you
	 * can get but the slower the application will be.
	 */
	final public static int NUM_MOUSEBUTTONS = 5;

	/**
	 * Don't know what that is for.
	 */
	final public static int KEY_NONE = 0x00;

	/**
	 * Key ESCAPE at the top left of the Keyboard.
	 */
	final public static int KEY_ESCAPE = 0x01;

	/**
	 * Key 1 on main keyboard.
	 */
	final public static int KEY_1 = 0x02;

	/**
	 * Key 2 on main keyboard.
	 */
	final public static int KEY_2 = 0x03;

	/**
	 * Key 3 on main keyboard.
	 */
	final public static int KEY_3 = 0x04;

	/**
	 * Key 4 on main keyboard.
	 */
	final public static int KEY_4 = 0x05;

	/**
	 * Key 5 on main keyboard.
	 */
	final public static int KEY_5 = 0x06;

	/**
	 * Key 6 on main keyboard.
	 */
	final public static int KEY_6 = 0x07;

	/**
	 * Key 7 on main keyboard.
	 */
	final public static int KEY_7 = 0x08;

	/**
	 * Key 8 on main keyboard.
	 */
	final public static int KEY_8 = 0x09;

	/**
	 * Key 9 on main keyboard.
	 */
	final public static int KEY_9 = 0x0A;

	/**
	 * Key 0 on main keyboard.
	 */
	final public static int KEY_0 = 0x0B;

	/**
	 * Key minus (-) on main keyboard.
	 */
	final public static int KEY_MINUS = 0x0C;

	/**
	 * Key = on main keyboard.
	 */
	final public static int KEY_EQUALS = 0x0D;

	/**
	 * Key backspace on main keyboard.
	 */
	final public static int KEY_BACK = 0x0E;

	/**
	 * Key TAB (upper caps lock key) on main keyboard.
	 */
	final public static int KEY_TAB = 0x0F;

	/**
	 * Key Q.
	 */
	final public static int KEY_Q = 0x10;

	/**
	 * Key W.
	 */
	final public static int KEY_W = 0x11;

	/**
	 * Key E.
	 */
	final public static int KEY_E = 0x12;

	/**
	 * Key R.
	 */
	final public static int KEY_R = 0x13;

	/**
	 * Key T.
	 */
	final public static int KEY_T = 0x14;

	/**
	 * Key Y.
	 */
	final public static int KEY_Y = 0x15;

	/**
	 * Key U.
	 */
	final public static int KEY_U = 0x16;

	/**
	 * Key I.
	 */
	final public static int KEY_I = 0x17;

	/**
	 * Key O.
	 */
	final public static int KEY_O = 0x18;

	/**
	 * Key P.
	 */
	final public static int KEY_P = 0x19;

	/**
	 * Key left brackets on qwerty
	 * Key to the right of 0 on azerty.
	 */
	final public static int KEY_LBRACKET = 0x1A;

	/**
	 * Key right brackets on qwerty
	 * Key ^ on azerty.
	 */
	final public static int KEY_RBRACKET = 0x1B;

	/**
	 * Key return/enter on main keyboard.
	 */
	final public static int KEY_RETURN = 0x1C;

	/**
	 * Key ctrl (on the left) or alt gr. // TODO : Why the fuck does alt gr works ?
	 */
	final public static int KEY_LCONTROL = 0x1D;

	/**
	 * Key A.
	 */
	final public static int KEY_A = 0x1E;

	/**
	 * Key S.
	 */
	final public static int KEY_S = 0x1F;

	/**
	 * Key D.
	 */
	final public static int KEY_D = 0x20;

	/**
	 * Key F.
	 */
	final public static int KEY_F = 0x21;

	/**
	 * Key G.
	 */
	final public static int KEY_G = 0x22;

	/**
	 * Key H.
	 */
	final public static int KEY_H = 0x23;

	/**
	 * Key J.
	 */
	final public static int KEY_J = 0x24;

	/**
	 * Key K.
	 */
	final public static int KEY_K = 0x25;

	/**
	 * Key L.
	 */
	final public static int KEY_L = 0x26;

	/**
	 * Key ; on qwerty
	 * Key $ on azerty.
	 */
	final public static int KEY_SEMICOLON = 0x27;

	/**
	 * Key ' on qwerty
	 * Key ² on azerty.
	 */
	final public static int KEY_APOSTROPHE = 0x28;

	/**
	 * Key ù on azerty.
	 */
	final public static int KEY_GRAVE = 0x29;

	/**
	 * Key shift on the left (under caps lock).
	 */
	final public static int KEY_LSHIFT = 0x2A;

	/**
	 * Key \ on qwerty
	 * Key * on azerty.
	 */
	final public static int KEY_BACKSLASH = 0x2B;

	/**
	 * Key Z.
	 */
	final public static int KEY_Z = 0x2C;

	/**
	 * Key X.
	 */
	final public static int KEY_X = 0x2D;

	/**
	 * Key C.
	 */
	final public static int KEY_C = 0x2E;

	/**
	 * Key V.
	 */
	final public static int KEY_V = 0x2F;

	/**
	 * Key B.
	 */
	final public static int KEY_B = 0x30;

	/**
	 * Key N.
	 */
	final public static int KEY_N = 0x31;

	/**
	 * Key M.
	 */
	final public static int KEY_M = 0x32;

	/**
	 * Key coma (,) on main keyboard.
	 */
	final public static int KEY_COMMA = 0x33;

	/**
	 * Key . on qwerty on main keyboard
	 * Key ; on azerty on main keyboard.
	 */
	final public static int KEY_PERIOD = 0x34;

	/**
	 * Key / on qwerty on main keyboard
	 * Key : on azerty on main keyboard.
	 */
	final public static int KEY_SLASH = 0x35;

	/**
	 * Key shift on the right (under enter key).
	 */
	final public static int KEY_RSHIFT = 0x36;

	/**
	 * Key multiply (*) on numpad.
	 */
	final public static int KEY_MULTIPLY = 0x37;

	/**
	 * Key alt on main keyboard (to the left of space).
	 */
	final public static int KEY_LALT = 0x38;

	/**
	 * Key space on main keyboard.
	 */
	final public static int KEY_SPACE = 0x39;

	/**
	 * Key caps lock / capital (under tab).
	 */
	final public static int KEY_CAPSLOCK = 0x3A;

	/**
	 * Key F1 on main keyboard.
	 */
	final public static int KEY_F1 = 0x3B;

	/**
	 * Key F2 on main keyboard.
	 */
	final public static int KEY_F2 = 0x3C;

	/**
	 * Key F3 on main keyboard.
	 */
	final public static int KEY_F3 = 0x3D;

	/**
	 * Key F4 on main keyboard.
	 */
	final public static int KEY_F4 = 0x3E;

	/**
	 * Key F5 on main keyboard.
	 */
	final public static int KEY_F5 = 0x3F;

	/**
	 * Key F6 on main keyboard.
	 */
	final public static int KEY_F6 = 0x40;

	/**
	 * Key F7 on main keyboard.
	 */
	final public static int KEY_F7 = 0x41;

	/**
	 * Key F8 on main keyboard.
	 */
	final public static int KEY_F8 = 0x42;

	/**
	 * Key F9 on main keyboard.
	 */
	final public static int KEY_F9 = 0x43;

	/**
	 * Key F10 on main keyboard.
	 */
	final public static int KEY_F10 = 0x44;

	/**
	 * Key num lock on numpad.
	 */
	final public static int KEY_NUMLOCK = 0x45;

	/**
	 * Key scroll lock (to the right of screen shot).
	 */
	final public static int KEY_SCROLLLOCK = 0x46;

	/**
	 * Key 7 on numpad.
	 */
	final public static int KEY_NUMPAD7 = 0x47;

	/**
	 * Key 8 on numpad.
	 */
	final public static int KEY_NUMPAD8 = 0x48;

	/**
	 * Key 9 on numpad.
	 */
	final public static int KEY_NUMPAD9 = 0x49;

	/**
	 * Key minus (-) on numpad.
	 */
	final public static int KEY_SUBTRACT = 0x4A;

	/**
	 * Key 4 on numpad.
	 */
	final public static int KEY_NUMPAD4 = 0x4B;

	/**
	 * Key 5 on numpad.
	 */
	final public static int KEY_NUMPAD5 = 0x4C;

	/**
	 * Key 6 on numpad.
	 */
	final public static int KEY_NUMPAD6 = 0x4D;

	/**
	 * Key add (+) on numpad.
	 */
	final public static int KEY_ADD = 0x4E;

	/**
	 * Key 1 on numpad.
	 */
	final public static int KEY_NUMPAD1 = 0x4F;


	/**
	 * Key 2 on numpad.
	 */
	final public static int KEY_NUMPAD2 = 0x50;

	/**
	 * Key 3 on numpad.
	 */
	final public static int KEY_NUMPAD3 = 0x51;

	/**
	 * Key 0 on numpad.
	 */
	final public static int KEY_NUMPAD0 = 0x52;

	/**
	 * Key decimal (.) on numpad.
	 */
	final public static int KEY_DECIMAL = 0x53;

	/**
	 * Key 11 on main keyboard.
	 */
	final public static int KEY_F11 = 0x57;

	/**
	 * Key 12 on main keyboard.
	 */
	final public static int KEY_F12 = 0x58;

	//final public static int KEY_F13 = 0x64; /* (NEC PC98) */
	//final public static int KEY_F14 = 0x65; /* (NEC PC98) */
	//final public static int KEY_F15 = 0x66; /* (NEC PC98) */
	//final public static int KEY_NUMPADEQUALS = 0x8D; /* = on numeric keypad (NEC PC98) */
	//final public static int KEY_AT = 0x91; /* (NEC PC98) */
	//final public static int KEY_COLON = 0x92; /* (NEC PC98) */
	//final public static int KEY_UNDERLINE = 0x93; /* (NEC PC98) */
	//final public static int KEY_STOP = 0x95; /* (NEC PC98) */
	//final public static int KEY_UNLABELED = 0x97; /* (J3100) */

	/**
	 * Key is not used, if numpad enter pressed, it will trigger the normal enter key
	 */
	//final public static int KEY_NUMPADENTER = 0x9C;

	/**
	 * Key control (on the right).
	 */
	final public static int KEY_RCONTROL = 0x9D;

	//final public static int KEY_NUMPADCOMMA = 0xB3; /* , on numeric keypad (NEC PC98) */

	/**
	 * Key divide (/) on numpad.
	 */
	final public static int KEY_DIVIDE = 0xB5;

	//final public static int KEY_SYSRQ = 0xB7;

	/**
	 * Key Alt Gr.
	 */
	final public static int KEY_ALTGR = 0xB8;
	//final public static int KEY_PAUSE = 0xC5; /* Pause */
	//final public static int KEY_HOME = 0xC7; /* Home on arrow keypad */

	/**
	 * Key up on arrow keypad (up arrow).
	 */
	final public static int KEY_UP = 0xC8;

	/**
	 * Key page up on arrow keypad.
	 */
	final public static int KEY_PRIOR = 0xC9;

	/**
	 * Key left on arrow keypad (left arrow).
	 */
	final public static int KEY_LEFT = 0xCB;

	/**
	 * Key right on arrow keypad (right arrow).
	 */
	final public static int KEY_RIGHT = 0xCD;

	/**
	 * Key end on arrow keypad (to the right of delete).
	 */
	final public static int KEY_END = 0xCF;

	/**
	 * Key down on arrow keypad (down arrow).
	 */
	final public static int KEY_DOWN = 0xD0;

	/**
	 * Key page down on arrow keypad (to the right of end).
	 */
	final public static int KEY_NEXT = 0xD1;

	/**
	 * Key insert on arrow keypad.
	 */
	final public static int KEY_INSERT = 0xD2;

	/**
	 * Key delete on arrow keypad.
	 */
	final public static int KEY_DELETE = 0xD3;

	/**
	 * Key left Windows on main keyboard.
	 */
	final public static int KEY_LWIN = 0xDB;

	/**
	 * Key right Windows on main keyboard. // TODO: Let Gwenael test this key
	 */
	final public static int KEY_RWIN = 0xDC; /* Right Windows key */
	//final public static int KEY_APPS = 0xDD; /* AppMenu key */
	//final public static int KEY_POWER = 0xDE;
	//final public static int KEY_SLEEP = 0xDF;

	/**
	 * Mouse left button.
	 */
	final public static int MOUSE_LEFT_BUTTON = 0;

	/**
	 * Mouse right button.
	 */
	final public static int MOUSE_RIGHT_BUTTON = 1;

	/**
	 * Mouse scroll button (when clicked, not scrolled).
	 */
	final public static int MOUSE_SCROLL_BUTTON = 2;

	/**
	 * Mouse side button that allows to go back one page on a browser.
	 */
	final public static int MOUSE_LEFT_THUMB_BUTTON = 3;

	/**
	 * Mouse side button on the top of the left thumb button.
	 */
	final public static int MOUSE_RIGHT_THUMB_BUTTON = 4;

	/**
	 * Keys pressed on the last frame.
	 */
	final private static boolean[] lastKeys = new boolean[NUM_KEYCODES];

	/**
	 * Mouse buttons clicked on the last frame.
	 */
	final private static boolean[] lastMouse = new boolean[NUM_MOUSEBUTTONS];

	/**
	 * Keys pressed on the this frame.
	 */
	final private static boolean[] currentKeys = new boolean[NUM_KEYCODES];

	/**
	 * Mouse buttons clicked on the this frame.
	 */
	final private static boolean[] currentMouse = new boolean[NUM_MOUSEBUTTONS];

	/**
	 * Last key pressed.
	 */
	private static int lastKeyPressed;

	/**
	 * Last time a key has been pressed (in nanoseconds).
	 */
	private static long lastTimeKeyPressed;

	/**
	 * Last mouse button pressed.
	 */
	private static int lastMouseButtonPressed;

	/**
	 * Last time a mouse button has been pressed (in nanoseconds).
	 */
	private static long lastTimeMouseButtonPressed;

	/**
	 * Keys that have been pressed this frame.
	 */
	private static @NotNull ArrayList<Integer> keysPressedThisFrame = new ArrayList<>();

	/**
	 * Mouse buttons that have been pressed this frame.
	 */
	private static @NotNull ArrayList<Integer> mouseButtonsPressedThisFrame = new ArrayList<>();

	/**
	 * Blocks the keys input.
	 */
	private static boolean blockKeys;

	/**
	 * Initializes the inputs.
	 */
	public static void init() {
		try {
			Keyboard.create();
			Mouse.create();
		} catch(final LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Input.lastKeyPressed = 0;
		Input.lastTimeKeyPressed = 0L;
		Input.lastMouseButtonPressed = 0;
		Input.lastTimeMouseButtonPressed = 0L;
		Input.keysPressedThisFrame = new ArrayList<>();
		Input.mouseButtonsPressedThisFrame = new ArrayList<>();
		Input.blockKeys = false;
	}

	/**
	 * Destroys the inputs.
	 */
	public static void destroy() {
		Keyboard.destroy();
		Mouse.destroy();
	}

	/**
	 * Updates all the inputs for the current frame.
	 */
	public static void update() {
		Profiler.startProfileTimer("Input-Update");

		System.arraycopy(Input.currentKeys, 0, Input.lastKeys, 0, Input.NUM_KEYCODES);
		System.arraycopy(Input.currentMouse, 0, Input.lastMouse, 0, Input.NUM_MOUSEBUTTONS);

		Input.keysPressedThisFrame.clear();
		Input.mouseButtonsPressedThisFrame.clear();

		while(Keyboard.next()) {
			final int code = Keyboard.getEventKey();

			if(code == 0 || code < Input.NUM_KEYCODES) {
				if(Keyboard.getEventKeyState()) {
					Input.currentKeys[code] = true;
					Input.lastKeyPressed = code;
					Input.lastTimeKeyPressed = Time.getNanoTime();
					Input.keysPressedThisFrame.add(code);
				} else {
					Input.currentKeys[code] = false;
				}
			}
		}

		while(Mouse.next()) {
			final int code = Mouse.getEventButton();

			if(code >= 0 && (code == 0 || code < Input.NUM_MOUSEBUTTONS)) {
				if(Mouse.getEventButtonState()) {
					Input.currentMouse[code] = true;
					Input.lastMouseButtonPressed = code;
					Input.lastTimeMouseButtonPressed = Time.getNanoTime();
					Input.mouseButtonsPressedThisFrame.add(code);
				} else {
					Input.currentMouse[code] = false;
				}
			}
		}
		Profiler.stopProfileTimer("Input-Update");
	}

	/**
	 * Returns the user's keyboard layout.
	 *
	 * @return String
	 */
	@Contract(pure = true)
	public static @NotNull String getKeyboardLayout() {
		return GameOptions.getSelect("keyboard").getOptionSelected();
	}

	/**
	 * Returns the character corresponding to a key code.
	 * Returns 0 if key cannot be converted to char (e.g. escape, shift ...).
	 *
	 * @param keyCode Key code
	 * @return new char
	 */
	@Contract(pure = true)
	public static char getKeyChar(final int keyCode) {
		final boolean capsOn = Input.isCapsOn();
		final boolean altGrOn = Input.isAltGrOn();
		final boolean shiftOn = Input.getKey(KEY_LSHIFT, true) || Input.getKey(KEY_RSHIFT, true);
		final boolean digitsOn;

		switch(Input.getKeyboardLayout()) {
			case "azerty":
				digitsOn = capsOn && !altGrOn;
				break;
			case "qwerty":
				digitsOn = !capsOn;
				break;
			default:
				System.err.println("Error in Input.getKeyChar(keyCode), keyboard layout not recognized.");
				System.err.println("Keyboard layout = \"" + Input.getKeyboardLayout() + "\"");
				digitsOn = true;
		}

		switch(keyCode) { // TODO: For digits when !digitsOn and qwerty, put the correct char.
			case KEY_1:
				return digitsOn ? '1' : altGrOn ? 0 : '&';
			case KEY_2:
				return digitsOn ? '2' : altGrOn ? 0 : 'é'; // TODO: Check altGrOn char is correct
			case KEY_3:
				return digitsOn ? '3' : altGrOn ? '#' : '\"';
			case KEY_4:
				return digitsOn ? '4' : altGrOn ? '{' : '\'';
			case KEY_5:
				return digitsOn ? '5' : altGrOn ? '[' : '(';
			case KEY_6:
				return digitsOn ? '6' : altGrOn ? '|' : '-';
			case KEY_7:
				return digitsOn ? '7' : altGrOn ? 0 : 'è'; // TODO: Check altGrOn char is correct
			case KEY_8:
				return digitsOn ? '8' : altGrOn ? '\\' : '_';
			case KEY_9:
				return digitsOn ? '9' : altGrOn ? '^' : 'ç';
			case KEY_0:
				return digitsOn ? '0' : altGrOn ? '@' : 'à';
			case KEY_NUMPAD0:
				return shiftOn || altGrOn ? 0 : '0';
			case KEY_NUMPAD1:
				return shiftOn || altGrOn ? 0 : '1';
			case KEY_NUMPAD2:
				return shiftOn || altGrOn ? 0 : '2';
			case KEY_NUMPAD3:
				return shiftOn || altGrOn ? 0 : '3';
			case KEY_NUMPAD4:
				return shiftOn || altGrOn ? 0 : '4';
			case KEY_NUMPAD5:
				return shiftOn || altGrOn ? 0 : '5';
			case KEY_NUMPAD6:
				return shiftOn || altGrOn ? 0 : '6';
			case KEY_NUMPAD7:
				return shiftOn || altGrOn ? 0 : '7';
			case KEY_NUMPAD8:
				return shiftOn || altGrOn ? 0 : '8';
			case KEY_NUMPAD9:
				return shiftOn || altGrOn ? 0 : '9';
			case KEY_SUBTRACT:
				return altGrOn ? 0 : '-';
			case KEY_ADD:
				return altGrOn ? 0 : '+';
			case KEY_MULTIPLY:
				return altGrOn ? 0 : '*';
			case KEY_DIVIDE:
				return altGrOn ? 0 : '/';
			case KEY_DECIMAL:
				return altGrOn || shiftOn ? 0 : '.';
			case KEY_Q:
				return altGrOn ? 0 : capsOn ? 'Q' : 'q';
			case KEY_W:
				return altGrOn ? 0 : capsOn ? 'W' : 'w';
			case KEY_E:
				return altGrOn ? '€' : capsOn ? 'E' : 'e';
			case KEY_R:
				return altGrOn ? 0 : capsOn ? 'R' : 'r';
			case KEY_T:
				return altGrOn ? 0 : capsOn ? 'T' : 't';
			case KEY_Y:
				return altGrOn ? 0 : capsOn ? 'Y' : 'y';
			case KEY_U:
				return altGrOn ? 0 : capsOn ? 'U' : 'u';
			case KEY_I:
				return altGrOn ? 0 : capsOn ? 'I' : 'i';
			case KEY_O:
				return altGrOn ? 0 : capsOn ? 'O' : 'o';
			case KEY_P:
				return altGrOn ? 0 : capsOn ? 'P' : 'p';
			case KEY_A:
				return altGrOn ? 0 : capsOn ? 'A' : 'a';
			case KEY_S:
				return altGrOn ? 0 : capsOn ? 'S' : 's';
			case KEY_D:
				return altGrOn ? 0 : capsOn ? 'D' : 'd';
			case KEY_F:
				return altGrOn ? 0 : capsOn ? 'F' : 'f';
			case KEY_G:
				return altGrOn ? 0 : capsOn ? 'G' : 'g';
			case KEY_H:
				return altGrOn ? 0 : capsOn ? 'H' : 'h';
			case KEY_J:
				return altGrOn ? 0 : capsOn ? 'J' : 'j';
			case KEY_K:
				return altGrOn ? 0 : capsOn ? 'K' : 'k';
			case KEY_L:
				return altGrOn ? 0 : capsOn ? 'L' : 'l';
			case KEY_Z:
				return altGrOn ? 0 : capsOn ? 'Z' : 'z';
			case KEY_X:
				return altGrOn ? 0 : capsOn ? 'X' : 'x';
			case KEY_C:
				return altGrOn ? 0 : capsOn ? 'C' : 'c';
			case KEY_V:
				return altGrOn ? 0 : capsOn ? 'V' : 'v';
			case KEY_B:
				return altGrOn ? 0 : capsOn ? 'B' : 'b';
			case KEY_N:
				return altGrOn ? 0 : capsOn ? 'N' : 'n';
			case KEY_M:
				return altGrOn ? 0 : capsOn ? 'M' : 'm';
			case KEY_SPACE:
				return ' ';
			default:
				return 0;
		}
	}

	/**
	 * Returns whether the caps lock is on.
	 *
	 * @return new boolean
	 */
	@Contract(pure = true)
	public static boolean isCapsLockOn() { // TODO: CAPS_LOCK not working ...
		return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
	}

	/**
	 * Returns whether caps are on (considering caps lock and shift key).
	 *
	 * @return new boolean
	 */
	@Contract(pure = true)
	public static boolean isCapsOn() {
		boolean capsOn = Input.isCapsLockOn();

		if(Input.getKey(Input.KEY_LSHIFT, true) || Input.getKey(Input.KEY_RSHIFT, true)) {
			capsOn = !capsOn;
		}

		return capsOn;
	}

	/**
	 * Returns whether or not Alt Gr is being pressed.
	 *
	 * @return new boolean
	 */
	@Contract(pure = true)
	public static boolean isAltGrOn() {
		return Input.getKey(Input.KEY_ALTGR, true);
	}

	/**
	 * Returns if the key is being pressed (returns false if Input.blockKeys is true).
	 *
	 * @param keyCode Key's code
	 * @return Key is being pressed ?
	 */
	@Contract(pure = true)
	public static boolean getKey(final int keyCode) {
		return Input.getKey(keyCode, false);
	}

	/**
	 * Returns if the key is being pressed.
	 *
	 * @param keyCode Key's code
	 * @param overrideBlock if true, does not check Input.blockKeys
	 * @return Key is being pressed ?
	 */
	@Contract(pure = true)
	public static boolean getKey(final int keyCode, final boolean overrideBlock) {
		if(!overrideBlock && Input.blockKeys) return false;
		return Keyboard.isKeyDown(keyCode);
	}

	/**
	 * Returns if the key has just been pressed (returns false if Input.blockKeys is true).
	 *
	 * @param keyCode Key's code
	 * @return Key has just been pressed ?
	 */
	@Contract(pure = true)
	public static boolean getKeyDown(final int keyCode) {
		return Input.getKeyDown(keyCode, false);
	}

	/**
	 * Returns if the key has just been pressed.
	 *
	 * @param keyCode Key's code
	 * @param overrideBlock if true, does not check Input.blockKeys
	 * @return Key has just been pressed ?
	 */
	@Contract(pure = true)
	public static boolean getKeyDown(final int keyCode, final boolean overrideBlock) {
		if(!overrideBlock && Input.blockKeys) return false;
		return getKey(keyCode, true) && !lastKeys[keyCode];
	}

	/**
	 * Returns if the key has just been released (returns false if Input.blockKeys is true).
	 *
	 * @param keyCode Key's code
	 * @return Key has just been released ?
	 */
	@Contract(pure = true)
	public static boolean getKeyUp(final int keyCode) {
		return Input.getKeyUp(keyCode, false);
	}

	/**
	 * Returns if the key has just been released.
	 *
	 * @param keyCode Key's code
	 * @param overrideBlock if true, does not check Input.blockKeys
	 * @return Key has just been released ?
	 */
	@Contract(pure = true)
	public static boolean getKeyUp(final int keyCode, final boolean overrideBlock) {
		if(!overrideBlock && Input.blockKeys) return false;
		return !getKey(keyCode, true) && lastKeys[keyCode];
	}

	/**
	 * Returns the last key pressed by the user.
	 *
	 * @return Last key pressed
	 */
	@Contract(pure = true)
	public static int getLastKeyPressed() {
		return Input.lastKeyPressed;
	}

	/**
	 * Returns the last time a key has been pressed (in nanoseconds).
	 *
	 * @return Last time a key has been pressed
	 */
	@Contract(pure = true)
	public static long getLastTimeKeyPressed() {
		return Input.lastTimeKeyPressed;
	}

	/**
	 * Returns an array list of all the key pressed this current frame (in order).
	 *
	 * @return Input.keysPressedThisFrame
	 */
	@Contract(pure = true)
	public static @NotNull ArrayList<Integer> getKeysPressedThisFrame() {
		return Input.keysPressedThisFrame;
	}

	/**
	 * Sets whether or not keys input are blocked.
	 *
	 * @param blockKeys true = keys input are blocked
	 */
	public static void blockKeys(final boolean blockKeys) {
		Input.blockKeys = blockKeys;
	}

	/**
	 * Returns if the mouse button is clicked.
	 *
	 * @param mouseButton Mouse button code
	 * @return Mouse button is being clicked ?
	 */
	public static boolean getMouse(final int mouseButton) {
		return Mouse.isButtonDown(mouseButton);
	}

	/**
	 * Returns if the mouse button has just been clicked.
	 *
	 * @param mouseButton Mouse button code
	 * @return Mouse button has just been clicked ?
	 */
	public static boolean getMouseDown(final int mouseButton) {
		return getMouse(mouseButton) && !lastMouse[mouseButton];
	}

	/**
	 * Returns if the mouse button has just been released.
	 *
	 * @param mouseButton Mouse button code
	 * @return Mouse button has just been released ?
	 */
	public static boolean getMouseUp(final int mouseButton) {
		return !getMouse(mouseButton) && lastMouse[mouseButton];
	}

	/**
	 * Returns the last mouse button pressed by the user.
	 *
	 * @return Last mouse button pressed
	 */
	@Contract(pure = true)
	public static int getLastMouseButtonPressed() {
		return Input.lastMouseButtonPressed;
	}

	/**
	 * Returns the last time a mouseButton has been pressed (in nanoseconds).
	 *
	 * @return Last time a mouse button has been pressed
	 */
	@Contract(pure = true)
	public static long getLastTimeMouseButtonPressed() {
		return Input.lastTimeMouseButtonPressed;
	}

	/**
	 * Returns an array list of all the mouse buttons pressed this current frame (order).
	 *
	 * @return Input.mouseButtonsPressedThisFrame
	 */
	@Contract(pure = true)
	public static @NotNull ArrayList<Integer> getMouseButtonsPressedThisFrame() {
		return Input.mouseButtonsPressedThisFrame;
	}

	/**
	 * Returns the current mouse position on the Window
	 * The origin is at the bottom left of the window.
	 *
	 * @return Mouse position (x ; y)
	 */
	public static @NotNull Vector2f getMousePosition() {
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}

	/**
	 * Sets the mouse position.
	 *
	 * @param pos Position to set
	 */
	public static void setMousePosition(final @NotNull Vector2f pos) {
		Mouse.setCursorPosition((int) pos.getX(), (int) pos.getY());
	}

	/**
	 * Return how much the user scrolled over the last frame (positive is up).
	 *
	 * @return new int
	 */
	public static int getScrollAmount() {
		return Mouse.getDWheel();
	}

	/**
	 * Sets if the mouse cursor is visible.
	 *
	 * @param enabled true = mouse cursor visible
	 */
	public static void setCursorVisibility(final boolean enabled) {
		Mouse.setGrabbed(!enabled);
	}

	/**
	 * Returns if the mouse cursor is visible.
	 *
	 * @return Mouse cursor visible
	 */
	@Contract(pure = true)
	public static boolean isCursorVisible() {
		return Mouse.isGrabbed();
	}

}