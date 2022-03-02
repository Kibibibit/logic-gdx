package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import au.com.thewindmills.kibi.appEngine.AppData;

public class MouseObject extends AppObject {

    Vector2 cameraPos;

    Vector2 lastCameraPos;
    Vector2 lastPos;

    Vector2 deltaCameraPos;
    Vector2 deltaPos;

    public MouseObject(AppData data, Vector2 pos) {
        super(data, pos);
        this.lastPos = pos.cpy();
        this.deltaPos = new Vector2(0, 0);
        this.cameraPos = new Vector2(0, 0);
        this.lastCameraPos = cameraPos.cpy();
        this.deltaCameraPos = new Vector2(0, 0);

    }

    @Override
    public void update(float delta) {
        this.setPos((float) Gdx.input.getX(), (float) Gdx.input.getY());

        if (!this.getPos().equals(this.lastPos)) {
            this.setCameraPos();

            this.deltaPos = this.getPos().cpy().sub(this.lastPos);
            this.deltaCameraPos = this.getCameraPos().sub(this.lastCameraPos);

            this.lastPos.set(this.getPos().cpy());
            this.lastCameraPos.set(this.cameraPos.cpy());
        }
    }

    public Vector2 getGlobalPos() {
        return this.getPos();
    }

    private void setCameraPos() {
        if (this.getData().getCamera() != null) {
            Vector3 v3 = this.getData().getCamera().unproject(new Vector3(this.getPos().x, this.getPos().y, 0));
            this.cameraPos.set(v3.x, v3.y);
        }
    }

    public Vector2 getCameraPos() {
        return this.cameraPos;
    }

    @Override
    public void dispose() {

    }

}
