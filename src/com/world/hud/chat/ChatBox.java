package com.world.hud.chat;

import com.world.hud.HUD;
import com.world.hud.HUDCategory;
import engine.game.components.RenderedComponent;
import engine.game.objects.text.Text;
import engine.math.Vector2f;
import engine.rendering.texture.Material;
import engine.rendering.texture.Texture;
import engine.util.Color;
import engine.util.Window;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatBox extends HUDCategory {

	/**
	 * ChatBox's width (in openGL units).
	 */
	final public static float WIDTH = 0.55471698f * Window.getRatio();

	/**
	 * ChatBox's height when closed (in openGL units).
	 */
	final public static float HEIGHT_CLOSED = 0.26455026455f;

	/**
	 * ChatBox's height when opened (in openGL units).
	 */
	final public static float HEIGHT_OPENED = 0.489417989f;

	/**
	 * ChatBox's position (in openGL units).
	 */
	final public static @NotNull Vector2f POS = new Vector2f(0.011320755f * Window.getRatio(), 0.026455026f);

	/**
	 * ChatBox's background.
	 */
	final public static @NotNull Material BACKGROUND = new Material(new Texture("black1x1"), new Color(0, 0, 0, 0.7f));

	/**
	 * Text colors.
	 * "regular" = white,
	 * "success" = green,
	 * "info" = blue,
	 * "warning" = yellow,
	 * "error" = red
	 */
	final private static @NotNull HashMap<String, Color> TEXT_COLORS = new HashMap<>();

	static {
		ChatBox.TEXT_COLORS.put("regular", new Color(230, 230, 230));
		ChatBox.TEXT_COLORS.put("success", new Color(0, 156, 29));
		ChatBox.TEXT_COLORS.put("info", new Color(0, 48, 181));
		ChatBox.TEXT_COLORS.put("warning", new Color(224, 209, 0));
		ChatBox.TEXT_COLORS.put("error", new Color(181, 0, 0));
	}

	/**
	 * ChatBox's bar.
	 */
	final private @NotNull ChatBar chatbar;

	/**
	 * Contains all the previously types messages.
	 */
	final private @NotNull ArrayList<Text> textMessages;

	/**
	 * Creates a new ChatBox instance.
	 *
	 * @param parentHUD ChatBox's HUD parent
	 */
	public ChatBox(final @NotNull HUD parentHUD) {
		super("Chat box", ChatBox.WIDTH, ChatBox.HEIGHT_CLOSED, parentHUD);

		this.setPosition(ChatBox.POS);

		this.addComponent(new RenderedComponent(ChatBox.BACKGROUND, ChatBox.WIDTH,ChatBox.HEIGHT_CLOSED, true));

		this.chatbar = new ChatBar(this);
		this.addChild(this.chatbar);
		this.chatbar.init();

		textMessages = new ArrayList<>();
	}

	/**
	 * Sets a message in the chat box.
	 *
	 * @param message Message to set
	 */
	protected void setMessage(final @NotNull Text message) {
		// TODO implement this method. Need to add to ArrayList and move all the messages positions.
	}

	/**
	 * Returns the text color (see ChatBox.TEXT_COLORS).
	 *
	 * @param index Key in the HashMap
	 * @return ChatBox::TEXT_COLORS[index]
	 */
	public static Color getTextColor(final @NotNull String index) {
		if(ChatBox.TEXT_COLORS.containsKey(index)) {
			return ChatBox.TEXT_COLORS.get(index);
		}

		System.err.println("Error: Couldn't find index \"" + index + "\" in ChatBox.TEXT_COLORS");
		new Exception().printStackTrace();
		return null;
	}

}