package au.com.thewindmills.logicgdx.app.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;

public abstract class IoParentActor extends ComponentActor {


    private ComponentBodyActor bodyActor;
    private String ioName;

    public IoParentActor(LogicAssetManager manager, AppStage stage, boolean input) {
        super("BUFFER", manager, stage);
        ioName = generateIoName();
        List<Actor> removeActors = new ArrayList<>();
        for (Actor actor : this.getChildren()) {
            if (actor instanceof ComponentIoActor) {
                if (((ComponentIoActor) actor).isInput() == input) {
                    removeActors.add(actor);
                } else {
                    actor.setY((float) ((getSize()*LogicAssetManager.TILE_SIZE*0.5f) - (LogicAssetManager.TILE_SIZE*0.5f)));
                }
            }
            if (actor instanceof ComponentBodyActor) {
                bodyActor = getBodyActor(this);
                removeActors.add(actor);
            }
        }
        for (Actor a : removeActors) {
            a.remove();
        }

        this.addActor(bodyActor);

    }
    @Override
    protected void setWidth(String name) {
    }

    protected abstract String generateIoName();
    protected abstract void setNameIndex(int newIndex);
    protected abstract void setNameCount(int newCount);

    protected String generateIoName(int index, int count) {

        String out = "";

        if (index < 26) {
            for (int i = 0; i < count; i++) {
                out = out + LogicAssetManager.CHAR_MAP.substring(index, index+1);
            }
            setNameIndex(index+1);
        } else {
            setNameIndex(0);
            setNameCount(count+1);
            out = generateIoName();
        }
        return out;
    }
    

    protected abstract int getSize();

    protected abstract ComponentBodyActor getBodyActor(IoParentActor parent);

    public boolean getState() {
        return this.getComponent().getInputState(this.getComponent().getInputs().toArray(new Long[]{})[0]);
    }

    public void setIoName(String name) {
        this.ioName = name;
    }

    public String getIoName() {
        return this.ioName;
    }

    
}