package au.com.thewindmills.kibi.appEngine.gfx.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.ShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.ArrayUtils;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;

/**
 * These entities are designed with ui elements in mind, so have parent/child relations with eachother and
 * other UI related utility functions
 * 
 * @author Kibi
 */
public abstract class UiEntity extends ShapeEntity {

    private final List<UiEntity> children;

    private UiEntity parent;

    private Vector2 relativePos;

    public UiEntity(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos, shape);

        if (!ArrayUtils.arrayContains(Layers.STATIC_LAYERS, this.getLayer())) {
            LOGGER.warning("UI entity with id " + this.id + " has not been assinged to a static layer (Assigned to layer: " + this.getLayer()+")");
        }

        this.parent = null;
        this.relativePos = new Vector2(0,0);

        this.children = new ArrayList<UiEntity>();
    }

    public UiEntity(Vector2 relativePos, AbstractShape shape, UiEntity parent) {
        this(parent.getData(), parent.getLayer(), parent.getDepth()+1, parent.getPos().cpy().add(relativePos), shape);
        this.parent = parent;
        this.relativePos = relativePos;
        this.parent.addChild(this);
    }

    @Override
    protected void preStep(float delta) {
        for (UiEntity child : children) {
            child.getPos().set(this.getPos().x + child.getRelativePos().x, this.getPos().y + child.getRelativePos().y);
            child.getShape().setPos(this.getPos().x + child.getRelativePos().x, this.getPos().y + child.getRelativePos().y);
        }
    }

    protected void addChild(UiEntity child) {
        this.children.add(child);
    }

    protected Vector2 getRelativePos() {
        return this.relativePos;
    }

    public UiEntity withStrokeColor(Color color) {
        this.setStrokeColor(color);
        return this;
    }

    public UiEntity withFillColor(Color color) {
        this.setFillColor(color);
        return this;
    }

    
    
    
}
