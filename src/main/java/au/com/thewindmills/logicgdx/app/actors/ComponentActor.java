package au.com.thewindmills.logicgdx.app.actors;

import java.io.IOException;

import com.badlogic.gdx.scenes.scene2d.Group;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.models.IoComponent;

public class ComponentActor extends Group {

    private static final int MIN_HEIGHT = 2;
    private static final int MIN_WIDTH = 4;

    private IoComponent component;
    private LogicAssetManager manager;

    private AppStage appStage;

    private int height;
    private int width;

    public ComponentActor(String name, LogicAssetManager manager, AppStage stage) {
        super();
        this.manager = manager;
        try {
            component = IoComponent.fromJson(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.getMatrix().addChild(this.component);
        this.appStage = stage;
        height = MIN_HEIGHT;
        if (component.getInputCount()*1.5f > height) {
            height = (int) Math.round(component.getInputCount()*1.5f);
        }

        width = MIN_WIDTH;

        setWidth(name);        

        this.addActor(new ComponentBodyActor(height, width, this));

        int i = 0;

        float section = (LogicAssetManager.TILE_SIZE * height) / component.getInputCount();
        float start = (LogicAssetManager.TILE_SIZE * height) / (component.getInputCount() + 1);
        int halfTile = LogicAssetManager.TILE_SIZE / 2;

        for (Long input : component.getInputs()) {
            this.addActor(
                    new ComponentIoActor(true, input, this)
                            .atX(this.getX() - LogicAssetManager.TILE_SIZE)
                            .atY(this.getY() + start - (halfTile * (i + 1)) + (section * i)));
            i++;
            component.update(input, true);
        }
        for (Long input: component.getInputs()) {
            component.update(input, false);
        }
        i = 0;
        section = (LogicAssetManager.TILE_SIZE * height) / component.getOutputCount();
        start = (LogicAssetManager.TILE_SIZE * height) / (component.getOutputCount() + 1);
        for (Long output : component.getOutputs()) {
            this.addActor(
                    new ComponentIoActor(false, output, this)
                            .atX(this.getX() + (width * LogicAssetManager.TILE_SIZE))
                            .atY(this.getY() + start - (halfTile * (i + 1)) + (section * i)));
            i++;
        }
    }

    protected void setWidth(String name) {

        int longestText = 0;
        for (String label : component.getLabels().values()) {
            if (manager.getTextWidth(label) > longestText) {
                longestText = manager.getTextWidth(label);
            }
        }

        while (width < (manager.getTextWidth(name) + (2 * (longestText+LogicAssetManager.TILE_SIZE)))/LogicAssetManager.TILE_SIZE) {
            width++;
        }
    }

    public AppStage getAppStage() {
        return appStage;
    }

    public boolean getComponentInputState(long id) {
        return component.getInputState(id);
    }

    public boolean getComponentOutputState(long id) {
        return component.getOutputState(id);
    }

    public LogicAssetManager getManager() {
        return manager;
    }

    public IoComponent getComponent() {
        return component;
    }

    public ComponentActor atX(float x) {
        this.setX(x);
        return this;
    }

    public ComponentActor atY(float y) {
        this.setY(y);
        return this;
    }

    @Override
    public int hashCode() {
        return (int) this.component.getId();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ComponentActor) {
            return other.hashCode() == this.hashCode();
        }
        return false;
    }

}
