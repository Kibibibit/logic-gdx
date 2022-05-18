package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;

public class SwitchComponentActor extends ComponentActor {

    public final static int SWITCH_SIZE = 4;

    private SwitchBodyActor bodyActor;
    private String switchName = "S";

    public SwitchComponentActor(LogicAssetManager manager, AppStage stage) {
        super("BUFFER", manager, stage);
        ComponentBodyActor removeActor = null;
        for (Actor actor : this.getChildren()) {
            if (actor instanceof ComponentIoActor) {
                if (((ComponentIoActor) actor).isInput()) {
                    actor.remove();
                }
            }
            if (actor instanceof ComponentBodyActor) {
                bodyActor = new SwitchBodyActor(SWITCH_SIZE, SWITCH_SIZE, this);
                removeActor = (ComponentBodyActor) actor;
            }
        }
        if (removeActor != null) {
            removeActor.remove();
        }

        this.addActor(bodyActor);
        
        this.addActor(new SwitchButtonActor(this));

    }
    public boolean getState() {
        return this.getComponent().getInputState(this.getComponent().getInputs().toArray(new Long[]{})[0]);
    }

    public void setSwitchName(String name) {
        this.switchName = name;
    }

    public String getSwitchName() {
        return this.switchName;
    }

    public void toggle() {
        long id = this.getComponent().getInputs().toArray(new Long[]{})[0];
        boolean state = this.getComponent().getInputState(id);
        this.getAppStage().update(this.getComponent().update(id, !state));
    }
    
}
