package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiPanel;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class UiTextfield extends UiPanel {

    private boolean highlighted = false;
    private final BitmapFont font;

    private Color strokeColor;

    private String value = "";

    public UiTextfield(Vector2 relativePos, float width, float height, UiEntity parent) {
        super(relativePos, width, height, parent);
        this.strokeColor = this.getShape().getStrokeColor();
        font = new BitmapFont();
        font.setColor(Colors.BLACK);
    }

    public String getValue() {
        return this.value;
    }

    public String clearValue() {
        String out = value;
        this.value = "";
        return out;
    }

    public void setHighlight(boolean highlight) {
        this.highlighted = highlight;
    }

    @Override
    public void renderText(Batches batches) {
        this.font.draw(
            batches.spriteBatch, value, 
            this.getPos().x,
            this.getPos().y + this.getShape().getHeight()/2 + (this.font.getCapHeight()/2), 
            this.getShape().getWidth(), 
            Align.center, 
            false);
    }

    @Override
    public void preDraw(Batches batches) {
        if (highlighted) {
            this.setStrokeColor(Colors.BLUE);
        } else {
            this.setStrokeColor(strokeColor);
        }

        super.preDraw(batches);
    }


    @Override
    public void doOnMouseReleased(int button) {
        this.highlighted = !this.highlighted;
        if (highlighted) {
            if (this.getData().getMouse().getHighlightedTextfield() != null) this.getData().getMouse().getHighlightedTextfield().setHighlight(false);
            this.getData().getMouse().setHighlightedTextfield(this);
        }
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

    @Override
    public void dispose() {
        font.dispose();
    }


}
