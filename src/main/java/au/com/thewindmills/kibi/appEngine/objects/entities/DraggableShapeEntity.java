package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;

public abstract class DraggableShapeEntity extends ShapeEntity {

    public DraggableShapeEntity(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos, shape);
        this.isDraggable = true;
    }

    @Override
    public void mouseDragged() {
        Vector2 mousePos = this.onStaticLayer() ? getData().getMouse().getGlobalPos() : getData().getMouse().getCameraPos();

        this.setPos(mousePos.cpy().sub(this.mouseOffset));
        this.getShape().setPos(this.getPos());
    }
    
}
