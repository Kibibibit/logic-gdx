package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class WireActor extends Actor {
    private LogicAssetManager manager;
    private ComponentIoActor start;
    private ComponentIoActor end;
    private boolean startIsInput;
    private String mapping;
    private boolean drawing = true;

    public WireActor(LogicAssetManager manager, ComponentIoActor start) {
        super();
        this.manager = manager;
        this.start = start;
        startIsInput = start.isInput();
        this.setPosition(start.getX() + start.getParent().getX(), start.getY() + start.getParent().getY());
        this.setHeight(LogicAssetManager.WIRE_TILE_SIZE);
        this.setWidth(LogicAssetManager.WIRE_TILE_SIZE);
        this.setOrigin(LogicAssetManager.WIRE_TILE_SIZE / 2, LogicAssetManager.WIRE_TILE_SIZE / 2);

    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getMapping() {
        return mapping;
    }

    public LogicAssetManager getManager() {
        return manager;
    }

    public boolean startIsInput() {
        return startIsInput;
    }

    public boolean getState() {
        return start.getState();
    }

    public void stopDrawing(ComponentIoActor output) {
        this.end = output;
        drawing = false;
    }

    public ComponentIoActor getStart() {
        return start;
    }

    public ComponentIoActor getEnd() {
        return end;
    }

    public boolean validEnd(ComponentIoActor actor) {
        if (start.isInput() != actor.isInput()) {
            if (start.getParentActor().getComponent().getId() != actor.getParentActor().getComponent().getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setPosition(start.getX() + start.getParent().getX() + (start.getWidth() / 4),
                start.getY() + start.getParent().getY() + (start.getHeight() / 4));

        Vector2 point;
        if (drawing) {
            point = this.getStage().screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        } else {
            point = new Vector2(end.getParent().getX()+end.getX()+(end.getWidth()/4), end.getParent().getY() + end.getY()+(end.getHeight()/4));
        }

        this.setWidth(Vector2.dst(this.getX(), this.getY(), point.x, point.y));
        this.setRotation((float) Math.toDegrees(Math.atan2(point.y - this.getY(), point.x - this.getX())));

        SheetSection fillSprite = SheetSection.WX;
        if (!startIsInput) {
            if (start.getState()) {
                fillSprite = SheetSection.WO;
            }
        } else {
            if (end != null) {
                if (end.getState()) {
                    fillSprite = SheetSection.WO;
                }
            }
            
        }

        float drawWidth = this.getWidth();
        if (drawing) {
            this.setWidth(this.getWidth()-LogicAssetManager.WIRE_TILE_SIZE);
        }
        

        batch.draw(
                manager.getSprite(fillSprite),
                this.getX(),
                this.getY(),
                this.getOriginX(),
                this.getOriginY(),
                drawWidth,
                this.getHeight(), this.getScaleX(), this.getScaleY(),
                this.getRotation());

        batch.draw(
                manager.getSprite(SheetSection.EW),
                this.getX(),
                this.getY(),
                this.getOriginX(),
                this.getOriginY(),
                drawWidth,
                this.getHeight(), this.getScaleX(), this.getScaleY(),
                this.getRotation());
    }

}
