package au.com.thewindmills.kibi.logicApp.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.DragableShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class ComponentBody extends DragableShapeEntity {

    public ComponentBody(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos, shape);
    }

    @Override
    public void mouseDragged() {
        
    }

    @Override
    protected void draw(Batches batches) {
        
    }

    @Override
    protected void step(float delta) {
        
    }

    @Override
    public void dispose() {
        
    }
    
}
