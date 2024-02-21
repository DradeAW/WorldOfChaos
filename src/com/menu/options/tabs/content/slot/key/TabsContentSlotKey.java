package com.menu.options.tabs.content.slot.key;

import com.GameOptions;
import com.menu.options.tabs.content.TabsContent;
import com.menu.options.tabs.content.slot.TabsContentSlot;
import engine.math.Vector2f;
import engine.util.Input;

import java.util.ArrayList;

public class TabsContentSlotKey extends TabsContentSlot {

    /**
     * Creates a new TabsContentSlotKey instance.
     *
     * @param label Label to display
     * @param index Index in the GameOptions
     * @param pos TabsContentSlotKey's position
     * @param contentParent Panel's content
     */
    public TabsContentSlotKey(final String label, final String index, final Vector2f pos, final TabsContent contentParent) {
        super(label, index, new TabsContentSlotKeyInput(index), pos, contentParent);
    }

    @Override
    public void update(final double delta) {
        super.update(delta);

        if(this.hasClickedOffButton() && Input.getMouseDown(Input.MOUSE_LEFT_BUTTON)) {
            this.setSelected(false);
            this.getInput().setRenderedComponent(GameOptions.getKey(this.getIndex()).get());
        }

        if(this.isSelected()) {
            final ArrayList<Integer> keys = Input.getKeysPressedThisFrame();
            final ArrayList<Integer> mouseButtons = Input.getMouseButtonsPressedThisFrame();
            final int code;

            if(keys.size() > 0) {
                code = keys.get(0);

                if(GameOptions.isKeyAlreadyPicked(this.getIndex(), code)) {
                    this.getParentContent().addChild(new PanelKeyUsed(this.getParentContent()));
                    return;
                }

                GameOptions.getKey(this.getIndex()).set(code);
                this.getInput().setRenderedComponent(code);
                this.setSelected(false);
            } else if(mouseButtons.size() > 0) {
                if(mouseButtons.get(0) == Input.MOUSE_LEFT_BUTTON) {
                    this.setPreventNextClick(true);
                }

                code = mouseButtons.get(0) + Input.NUM_KEYCODES;

                if(GameOptions.isKeyAlreadyPicked(this.getIndex(), code)) {
                    this.getParentContent().addChild(new PanelKeyUsed(this.getParentContent()));
                    return;
                }

                GameOptions.getKey(this.getIndex()).set(code);
                this.getInput().setRenderedComponent(code);
                this.setSelected(false);
            }
        }
    }

    @Override
    public void onClick() {
        super.onClick();

        this.setSelected(true);
        this.getInput().setSelected();
    }

    @Override
    public TabsContentSlotKeyInput getInput() {
        return (TabsContentSlotKeyInput) super.getInput();
    }

}