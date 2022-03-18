package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiPanel;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;

public class UiTextPopUp extends UiPanel {

    private final UiRectButton close;
    private final UiRectButton confirm;
    private final UiTextfield textfield;

    public UiTextPopUp(AppData data, int depth, float width, float height) {
        super(data, Layers.ABOVE_UI, depth, Gdx.graphics.getWidth() / 2 - width / 2,
                Gdx.graphics.getHeight() / 2 - height / 2, width, height);


        close = new CloseButton(this);
        close.setTextColor(Colors.BLACK);

        confirm = new ConfirmButton(this);
        confirm.setTextColor(Colors.BLACK);

        textfield = new UiTextfield(
                new Vector2(10, (this.getSize().y / 2) + 10),
                this.getSize().x - 20, 20, this);

    }

    public String getValue() {
        return this.textfield.getValue();
    }

    public void typeKey(int keycode) {
        if (keycode != Keys.ENTER) {
            textfield.typeKey(keycode);
            return;
        }
        this.getData().closePopUp(this.getValue().strip());
        
    }

    private class CloseButton extends UiRectButton {

        private final static int closeSize = 15;

        public CloseButton(UiTextPopUp parent) {
            super(
                    new Vector2(parent.getSize().x-closeSize,parent.getSize().y-closeSize),
                    new Vector2(closeSize, closeSize), parent, "X");
        }

        @Override
        public void doOnMouseReleased(int button) {
            this.getData().closePopUp(null);
        }
    }

    private class ConfirmButton extends UiRectButton {

        private final static int confirmHeight = 20;
        private final static int confirmWidth = 40;

        public ConfirmButton(UiTextPopUp parent) {
            super(
                    new Vector2(10,10 ),
                    new Vector2(confirmWidth, confirmHeight), parent, "OK");
        }

        @Override
        public void doOnMouseReleased(int button) {
            this.getData().closePopUp(((UiTextPopUp) this.getParent()).getValue());
        }
    }

}
