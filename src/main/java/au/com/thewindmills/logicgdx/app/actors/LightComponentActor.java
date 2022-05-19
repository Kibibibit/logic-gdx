package au.com.thewindmills.logicgdx.app.actors;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;

public class LightComponentActor extends IoParentActor{

    private static int nameIndex = 0;
    private static int nameCount = 1;

    public static final int LIGHT_SIZE = 3;

    public LightComponentActor(LogicAssetManager manager, AppStage stage) {
        super(manager, stage, false);
        this.setName("LIGHT");
    }

    @Override
    protected ComponentBodyActor getBodyActor(IoParentActor parent) {
        return new LightBodyActor(getSize(), getSize(), parent);
    }

    @Override
    protected int getSize() {
        return LIGHT_SIZE;
    }

    @Override
    protected String generateIoName() {
        return generateIoName(nameIndex, nameCount) + "'";
    }

    @Override
    protected void setNameIndex(int newIndex) {
        nameIndex = newIndex;
    }

    @Override
    protected void setNameCount(int newCount) {
        nameCount = newCount;
    }

   
    
}
