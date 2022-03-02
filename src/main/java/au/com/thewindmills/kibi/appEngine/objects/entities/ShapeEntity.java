package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.utils.Batches;

public abstract class ShapeEntity extends AppEntity {

    private final AbstractShape shape;

    public ShapeEntity(AppData data, Vector2 pos, AbstractShape shape) {
        super(data, pos);
        this.shape = shape;
    }


    @Override
    public void preDraw(Batches batches) {
        shape.draw(batches);
    }

    

}
