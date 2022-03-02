package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

/**
 * An entity that is visible due to the shape it draws, defined by <br>
 * an {@link AbstractShape} object.
 * 
 * @author Kibi
 */
public abstract class ShapeEntity extends AppEntity {

    private final AbstractShape shape;

    public ShapeEntity(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos);
        this.shape = shape;
        this.shape.setPos(this.getPos());
    }

    public void setStrokeColor(Color color) {
        this.shape.setStrokeColor(color);
    }

    public void setFillColor(Color color) {
        this.shape.setFillColor(color);
    }

    public AbstractShape getShape() {
        return this.shape;
    }

    @Override
    public final boolean inBounds(float x, float y) {
        return this.shape.inBounds(x, y);
    }

    @Override
    public void preDraw(Batches batches) {
        shape.draw(batches);
    }

    

}
