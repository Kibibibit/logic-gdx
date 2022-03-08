package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;

public class UiRectButton extends UiButton {

    public UiRectButton(AppData data, String layer, int depth, Vector2 pos, Vector2 size, ButtonPress buttonPress) {
        super(data, layer, depth, pos, new RectShape(pos.x, pos.y, size.x, size.y), buttonPress);
    }

    public UiRectButton(Vector2 relativePos, Vector2 size, UiEntity parent, ButtonPress buttonPress) {
        super(relativePos, new RectShape(relativePos.x, relativePos.y, size.x, size.y), parent, buttonPress);
    }    

}
