package au.com.thewindmills.logicgdx.app.actors;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;

public class LightComponentActor extends IoParentActor{

    public static final int LIGHT_SIZE = 3;

    public LightComponentActor(LogicAssetManager manager, AppStage stage) {
        super(manager, stage, false);
    }

    @Override
    protected ComponentBodyActor getBodyActor(IoParentActor parent) {
        return new LightBodyActor(getSize(), getSize(), parent);
    }

    @Override
    protected int getSize() {
        return LIGHT_SIZE;
    }
    
}
