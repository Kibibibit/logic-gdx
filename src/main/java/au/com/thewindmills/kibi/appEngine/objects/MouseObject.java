package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import au.com.thewindmills.kibi.appEngine.AppData;
/**
 * Represents the mouse within the game, stores methods for firing events
 * and such.
 * 
 * @author Kibi
 */
public class MouseObject extends AppObject {

    /**
     * The position of the mouse in space based on the camera
     */
    private Vector2 cameraPos;

    /**
     * The position of the mouse last tick, relative to the camera
     */
    private Vector2 lastCameraPos;

    /**
     * The last global position of the mouse
     */
    private Vector2 lastPos;

    /**
     * The change in position from last tick, relative to the camera
     */
    private Vector2 deltaCameraPos;

    /**
     * The change in global position from last tick
     */
    private Vector2 deltaPos;

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


        //Only worht updating if there is actually a change
        if (!this.getPos().equals(this.lastPos)) {
            this.setCameraPos();

            this.deltaPos = this.getPos().cpy().sub(this.lastPos);
            this.deltaCameraPos = this.getCameraPos().sub(this.lastCameraPos);

            this.lastPos.set(this.getPos().cpy());
            this.lastCameraPos.set(this.cameraPos.cpy());
        }
    }

    public Vector2 getGlobalPos() {
        return this.getPos().cpy();
    }

    private void setCameraPos() {
        if (this.getData().getCamera() != null) {
            Vector3 v3 = this.getData().getCamera().unproject(new Vector3(this.getPos().x, this.getPos().y, 0));
            this.cameraPos.set(v3.x, v3.y);
        }
    }

    public Vector2 getCameraPos() {
        return this.cameraPos.cpy();
    }

    public Vector2 getDeltaPos() {
        return this.deltaPos.cpy();
    }

    public Vector2 getDeltaCameraPos() {
        return this.deltaCameraPos.cpy();
    }

    @Override
    public void dispose() {

    }

}
