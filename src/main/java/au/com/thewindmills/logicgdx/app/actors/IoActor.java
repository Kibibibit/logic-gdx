package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.g2d.Batch;

import au.com.thewindmills.logicgdx.app.LogicAssetManager;
import au.com.thewindmills.logicgdx.utils.AppConstants;

public class IoActor extends LogicActor {

    private LogicAssetManager manager;
    private String spriteName;

    public IoActor(String spriteName, LogicAssetManager manager) {
        this.manager = manager;
        this.spriteName = spriteName;
        this.setPosition(AppConstants.APP_WIDTH/2, AppConstants.APP_HEIGHT/2);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(manager.getImage(spriteName), this.getX(), this.getY());
    }

}
