package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import au.com.thewindmills.logicgdx.utils.AppConstants;

public class IoActor extends LogicActor {

    Texture texture;

    public IoActor(String path) {
        texture = new Texture(Gdx.files.internal(path));
        this.setPosition(AppConstants.APP_WIDTH/2, AppConstants.APP_HEIGHT/2);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, this.getX(), this.getY());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
