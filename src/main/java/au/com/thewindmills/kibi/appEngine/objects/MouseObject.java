package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import au.com.thewindmills.kibi.appEngine.AppData;

public class MouseObject extends AppObject {

    public MouseObject(AppData data, Vector2 pos) {
        super(data, pos);
    }

    @Override
    public void update(float delta) {
        this.setPos((float) Gdx.input.getX(), (float) Gdx.input.getY());
        
    }


    public Vector2 getGlobalPos() {
        return this.getPos();
    }

    public Vector2 getCameraPos() {
        Vector3 v3 = this.getData().getCamera().unproject(new Vector3(this.getPos().x, this.getPos().y, 0));
        return new Vector2(v3.x, v3.y);
    }

    

    @Override
    public void dispose() {
        
    }
    
}
