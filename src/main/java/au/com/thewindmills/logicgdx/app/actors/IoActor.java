package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class IoActor extends LogicActor {

    Texture texture;

    public IoActor(String path) {
        texture = new Texture(Gdx.files.internal(path));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, this.getOriginX(), this.getOriginY());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
