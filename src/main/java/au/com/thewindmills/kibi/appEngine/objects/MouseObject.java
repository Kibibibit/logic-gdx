package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.objects.entities.AppEntity;
import au.com.thewindmills.kibi.appEngine.utils.ArrayUtils;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
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
     * The change in position from last tick, relative to the camera
     */
    private Vector2 deltaCameraPos;

    /**
     * The id of the object currently being highlighted by the mouse
     */
    private AppEntity contextEntity = null;

    public MouseObject(AppData data) {
        super(data, new Vector2(0,0));
        this.cameraPos = new Vector2(0, 0);
        this.lastCameraPos = cameraPos.cpy();
        this.deltaCameraPos = new Vector2(0, 0);

    }

    @Override
    public void step(float delta) {
        this.setPos((float) Gdx.input.getX(), (float) Gdx.input.getY());


        if (!this.getDeltaPos().isZero()) {
            this.setCameraPos();
            this.deltaCameraPos = this.cameraPos.sub(this.lastCameraPos);
            this.updateContextEntity();
        }
    }

    @Override
    public void postStep(float delta) {
        if (!this.getDeltaPos().isZero()) {
            this.lastCameraPos = this.getCameraPos();
        }
    }

    /**
     * Updates the current object that is under the mouse
     */
    private void updateContextEntity() {
        for (String layer : Layers.LAYERS) {
            Vector2 posToCheck = this.getGlobalPos();
            
            if (!ArrayUtils.arrayContains(Layers.STATIC_LAYERS, layer)) {
                posToCheck = this.getCameraPos();
            }

            for (AppEntity entity : this.getData().getEntities().get(layer)) {
                if (entity.inBounds(posToCheck)) {
                    if (this.contextEntity == null) {
                        this.contextEntity = entity;
                        break;
                    } else {
                        if (layer.equals(entity.getLayer())) {
                            if (entity.getDepth() >= contextEntity.getDepth()) {
                                this.contextEntity = entity;
                                break;
                            }
                        }

                        int layerIndex = ArrayUtils.firstIndexOf(Layers.LAYERS, layer);
                        int contextEntityLayerIndex = ArrayUtils.firstIndexOf(Layers.LAYERS, contextEntity.getLayer());

                        if (layerIndex > contextEntityLayerIndex) {
                            this.contextEntity = entity;
                        }
                        break;

                    }
                }
                
            }
            

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

    public Vector2 getDeltaCameraPos() {
        return this.deltaCameraPos.cpy();
    }

    @Override
    public void dispose() {

    }

}
