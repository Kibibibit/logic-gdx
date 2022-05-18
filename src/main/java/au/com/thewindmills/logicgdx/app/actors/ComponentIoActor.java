package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class ComponentIoActor extends Actor {

    private boolean isInput;
    private Long ioId;
    private ComponentActor parent;
    private WireActor wire;

    public ComponentIoActor(boolean isInput, Long id, ComponentActor parent) {
        super();
        this.isInput = isInput;
        this.ioId = id;
        this.parent = parent;
        this.setWidth(LogicAssetManager.TILE_SIZE);
        this.setHeight(LogicAssetManager.TILE_SIZE);
    }

    public void draw(Batch batch, float parentAlpha) {

        SheetSection sprite = SheetSection.IX;


        if (getState()) {
            
            sprite = SheetSection.IO;
            
        }
        batch.draw(parent.getManager().getSprite(sprite), this.getX(), this.getY());
    }

    public WireActor getWire() {
        return wire;
    }

    public void setWire(WireActor wire) {
        this.wire = wire;
    }

    public boolean getState() {
        if (isInput) {
            return parent.getComponentInputState(ioId);
        } else {
            return parent.getComponentOutputState(ioId);
        }
    }

    public String getIoName() {
        return ":" + ioId +":"+ parent.getComponent().getIoLabel(ioId);
    }

    public boolean isInput() {
        return isInput;
    }
    
    public long getIoId() {
        return ioId;
    }

    public ComponentActor getParentActor() {
        return parent;
    }

    public ComponentIoActor atX(float x) {
        this.setX(x);
        return this;
    }

    public ComponentIoActor atY(float y) {
        this.setY(y);
        return this;
    }
}
