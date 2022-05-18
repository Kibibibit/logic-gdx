package au.com.thewindmills.logicgdx.app.actors;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;

public class SwitchComponentActor extends IoParentActor {

    public static final int SWITCH_SIZE = 4;

    public SwitchComponentActor(LogicAssetManager manager, AppStage stage) {
        super(manager, stage, true);
        this.addActor(new SwitchButtonActor(this));

    }

    public void toggle() {
        long id = this.getComponent().getInputs().toArray(new Long[]{})[0];
        boolean state = this.getComponent().getInputState(id);
        this.getAppStage().update(this.getComponent().update(id, !state));
    }

    @Override
    protected ComponentBodyActor getBodyActor(IoParentActor parent) {
        return new SwitchBodyActor(getSize(), getSize(), parent);
    }

    @Override
    protected int getSize() {
        return SWITCH_SIZE;
    }
    
}
