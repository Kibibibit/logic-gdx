package au.com.thewindmills.kibi.appEngine.gfx.ui;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class UiPanel extends UiEntity {

    public UiPanel(AppData data, String layer, int depth, float x, float y, float width, float height) {
        super(data, layer, depth, new Vector2(x,y), new RectShape(x,y,width,height));
    }

    public UiPanel(Vector2 relativePos, float width, float height, UiEntity parent) {
        super(relativePos, new RectShape(relativePos.x, relativePos.y,width,height), parent);
    }

    @Override
    protected void draw(Batches batches) {}

    @Override
    public void step(float delta) {}

    @Override
    public void dispose() {
    }

    
    
}
