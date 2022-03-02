package au.com.thewindmills.kibi.models.components;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.ShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

/**
 * THIS IS A TEST CLASS
 */
public class Gate extends ShapeEntity {

    public Gate(AppData data, Vector2 pos) {
        super(data, Layers.MAIN, pos, new RectShape(pos.x, pos.y, 20, 20));
    }

    public Gate(AppData data, float x, float y) {
        this(data, new Vector2(x,y));
    }

    @Override
    protected void draw(Batches batches) {
        
        
    }

    @Override
    public void update(float delta) {
        Vector2 mousePos = this.getData().getMouse().getCameraPos();

        if (this.getShape().inBounds(mousePos)) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        
    }
    
}
