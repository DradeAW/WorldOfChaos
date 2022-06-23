package com.world.hud.chat;

import com.GameOptions;
import engine.game.components.RenderedComponent;
import engine.game.objects.GameObject;
import engine.game.objects.text.FontLoader;
import engine.game.objects.text.Text;
import engine.math.Vector2f;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import engine.util.Color;
import engine.util.Input;
import engine.util.Window;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatBar extends GameObject {

	/**
	 * ChatBar's width (in openGL units).
	 */
	final public static float WIDTH = 0.543396226f * Window.getRatio();

	/**
	 * ChatBar's height (in openGL units).
	 */
	final public static float HEIGHT = 0.0396825397f*2;

	/**
	 * ChatBar's position (in openGL units).
	 */
	final public static @NotNull Vector2f POS = new Vector2f(0.005660377f * Window.getRatio(), 0.00529100529f);

	/**
	 * ChatBar's background opacity when not focused.
	 */
	final public static float OPACITY_NOT_FOCUSED = 1 - (0.3f * (1 - ChatBox.BACKGROUND.getColor().getAlpha()));

	/**
	 * ChatBar's background opacity when focused.
	 */
	final public static float OPACITY_FOCUSED = 1 - (0.1f * (1 - ChatBox.BACKGROUND.getColor().getAlpha()));

	/**
	 * ChatBox parent.
	 */
	final private @NotNull ChatBox parent;

	/**
	 * When player press enter, ChatBar gains focus.
	 */
	private boolean focus = false;

	/**
	 * ChatBar's background.
	 */
	final private @NotNull RenderedComponent background;

	/**
	 * Message being currently typed.
	 */
	private Text message;

	/**
	 * Creates a new ChatBar's instance.
	 *
	 * @param parent ChatBox parent.
	 */
	protected ChatBar(final @NotNull ChatBox parent) {
		super("Chat bar", ChatBar.WIDTH, ChatBar.HEIGHT);

		this.setPosition(ChatBar.POS);
		this.setDepth(-0.05f);

		this.parent = parent;

		this.background = new RenderedComponent(new Material(new Texture("black1x1"), new Color(0, 0, 0, ChatBar.OPACITY_NOT_FOCUSED)), ChatBar.WIDTH, ChatBar.HEIGHT, true);
		this.addComponent(this.background);
	}

	@Override
	public void update(final double delta) {
		super.update(delta);

		boolean messageChanged = false;
		if(this.isFocused()) {
			for(final int keyCode : Input.getKeysPressedThisFrame()) {
				if(keyCode == Input.KEY_RETURN) {
					break;
				} else if(keyCode == Input.KEY_BACK) {
					if(this.message.getText().length() == 0) continue;
					this.message.setText(this.message.getText().substring(0, this.message.getText().length() - 1));
				} else {
					final char c = Input.getKeyChar(keyCode);
					if(c != 0) {
						this.message.setText(this.message.getText() + c);
					}
				}
				messageChanged = true;
			}
		}

		if(messageChanged) {
			this.message.print(); // TODO: Maybe try something to not reprint the whole message all the time a key is pressed.
		}

		if(Input.getKeyDown(GameOptions.getKey("openConsole").get(), true)) {
			this.changeFocus();
			Input.blockKeys(this.isFocused());

			this.background.getMaterial().getColor().setAlpha(this.isFocused() ? ChatBar.OPACITY_FOCUSED : ChatBar.OPACITY_NOT_FOCUSED);

			if(this.isFocused()) {
				this.message = new Text("", FontLoader.getFont("tiny"), ChatBar.WIDTH, ChatBar.HEIGHT);
				this.message.setTextDepth(-0.01f); // TODO: Scale the message, maybe create a class for it?
				this.message.setColor(ChatBox.getTextColor("regular"));
				this.addChild(this.message);
			} else {
				if(this.message == null) {
					System.err.println("Error: ChatBar.message should not be null as the focus is true");
					new Exception().printStackTrace();
					return;
				}
				if(!this.message.getText().equals("")) {
					this.getParent().setMessage(this.message);
				}

				this.removeChild(this.message);
				this.message = null;
			}
		}
	}

	/**
	 * Return the ChatBox parent.
	 *
	 * @return ChatBar.parent
	 */
	@Contract(pure = true)
	private @NotNull ChatBox getParent() {
		return this.parent;
	}

	/**
	 * Returns whether the ChatBar has focus or not.
	 *
	 * @return ChatBar.focus
	 */
	@Contract(pure = true)
	private boolean isFocused() {
		return this.focus;
	}

	/**
	 * Changes the ChatBar's focus (true --> false, false --> true).
	 */
	private void changeFocus() {
		this.focus = !this.focus;
	}

}