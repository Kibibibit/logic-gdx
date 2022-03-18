package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiPanel;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class UiTextfield extends UiPanel {


    private String value = "";

    public UiTextfield(Vector2 relativePos, float width, float height, UiEntity parent) {
        super(relativePos, width, height, parent);
        this.setTextColor(Colors.BLACK);
    }

    public String getValue() {
        return this.value;
    }

    public String clearValue() {
        String out = value;
        this.value = "";
        return out;
    }


    @Override
    public void onRenderText(Batches batches) {
        batches.font.draw(
            batches.spriteBatch, value, 
            this.getPos().x,
            this.getPos().y + this.getShape().getHeight()/2 + (batches.font.getCapHeight()/2), 
            this.getShape().getWidth(), 
            Align.center, 
            false);
    }



    public void typeKey(int keycode) {

        if (keycode == Input.Keys.ENTER) {
            System.out.println(this.clearValue());
            return;
        }

        if (keycode == Input.Keys.BACKSPACE) {
            value = value.substring(0, Math.max(0, value.length() -1));
            return;
        }

        if (keycode == Input.Keys.SPACE) {
            value += " ";
            return;
        }

        value += Input.Keys.toString(keycode);

    }



}
