package au.com.thewindmills.kibi.appEngine.objects.entities;

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

    public ShapeEntity(AppData data, String layer, Vector2 pos, AbstractShape shape) {
        super(data, layer, pos);
        this.shape = shape;
    }

    public AbstractShape getShape() {
        return this.shape;
    }


    @Override
    public void preDraw(Batches batches) {
        shape.draw(batches);
    }

    

}
