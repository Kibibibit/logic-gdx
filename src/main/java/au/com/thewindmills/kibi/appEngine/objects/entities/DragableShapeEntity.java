package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;

public abstract class DragableShapeEntity extends ShapeEntity {

    public DragableShapeEntity(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos, shape);
        this.isDragable = true;
    }

    @Override
    public abstract void mouseDragged();
    
}
