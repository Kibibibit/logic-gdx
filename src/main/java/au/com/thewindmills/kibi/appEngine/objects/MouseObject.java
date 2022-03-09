package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.objects.entities.AppEntity;
import au.com.thewindmills.kibi.appEngine.utils.ArrayUtils;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
import au.com.thewindmills.kibi.appEngine.utils.processors.AppInputProcessor;

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
     * The object currently being highlighted by the mouse
     */
    private AppEntity contextEntity = null;

    /**
     * The object that was highlighted last event by the mouse
     */
    private AppEntity previousContextEntity = null;

    private boolean dragging = false;

    public MouseObject(AppData data) {
        super(data, new Vector2(0, 0));
        this.cameraPos = new Vector2(0, 0);
        this.lastCameraPos = cameraPos.cpy();
        this.deltaCameraPos = new Vector2(0, 0);

    }

    /**
     * Event called whenever {@link AppInputProcessor#mouseMoved(int, int)} is
     * called.
     * Note that the origin is at the top left of the screen
     * 
     * @param screenX - mouse's current x
     * @param screenY - mouse's current y
     */
    public void mouseMoved(int screenX, int screenY) {
        this.previousContextEntity = this.contextEntity;
        this.lastCameraPos = this.getCameraPos();
        this.setPos((float) screenX, (float) screenY);
        this.setCameraPos();
        this.deltaCameraPos = this.lastCameraPos.sub(this.cameraPos);
        if (dragging) return;
        this.updateContextEntity();
        this.updateContextEntityCurrent();

        if (this.previousContextEntity != null && this.contextEntity != null) {
            if (this.previousContextEntity.id != this.contextEntity.id) {
                this.previousContextEntity.onMouseLeave();
                this.contextEntity.onMouseEnter();
            }
        } else if (this.previousContextEntity != null) {
            this.previousContextEntity.onMouseLeave();
        } else if (this.contextEntity != null) {
            this.contextEntity.onMouseEnter();
        }
    }

    /**
     * Called when button was down last tick, and up this tick
     * 
     * @param button - The button that was pressed
     */
    public void buttonPressed(int button) {

        if (this.contextEntity != null) {
            this.contextEntity.onMousePressed(button);
        }

    }

    /**
     * Called when button was up last tick, and down this tick
     * 
     * @param button - The button that was released
     */
    public void buttonReleased(int button) {

        if (button == Input.Buttons.LEFT) {
            dragging = false;
        }
        

        if (this.contextEntity != null) {
            this.contextEntity.onMouseReleased(button);
        }

    }

    /**
     * Checks to see if the current context entitiy is still under the mouse
     * Probably could put back into updateContextEntity
     */
    private void updateContextEntityCurrent() {
        if (this.contextEntity != null) {
            Vector2 posToCheck = this.getGlobalPos();

            if (!ArrayUtils.arrayContains(Layers.STATIC_LAYERS, this.contextEntity.getLayer())) {
                posToCheck = this.getCameraPos();
            }

            if (!this.contextEntity.inBounds(posToCheck)) {
                this.contextEntity = null;
            }
        }
    }

    /**
     * Updates the current object that is under the mouse
     */
    private void updateContextEntity() {
        this.previousContextEntity = this.contextEntity;
        for (String layer : Layers.LAYERS) {
            Vector2 posToCheck = this.getGlobalPos();

            if (!ArrayUtils.arrayContains(Layers.STATIC_LAYERS, layer)) {
                posToCheck = this.getCameraPos();
            }

            for (AppEntity entity : this.getData().getEntities().get(layer)) {
                if (entity.inBounds(posToCheck)) {
                    if (this.contextEntity == null) {
                        this.contextEntity = entity;
                        continue;
                    } else {
                        if (layer.equals(entity.getLayer())) {
                            if (entity.getDepth() >= contextEntity.getDepth()) {
                                this.contextEntity = entity;
                                continue;
                            }
                        }

                        int layerIndex = ArrayUtils.firstIndexOf(Layers.LAYERS, layer);
                        int contextEntityLayerIndex = ArrayUtils.firstIndexOf(Layers.LAYERS, contextEntity.getLayer());

                        if (layerIndex > contextEntityLayerIndex) {
                            this.contextEntity = entity;
                            continue;
                        }

                    }
                }

            }

        }

    }

    public Vector2 getGlobalPos() {
        Vector2 globalPos = this.getPos().cpy();
        // We want origin to be bottom left, but input is weirdly calculated from top
        // left
        globalPos.y = Gdx.graphics.getHeight() - globalPos.y;
        return globalPos;
    }

    private void setCameraPos() {
        if (this.getData().getCamera() != null) {
            Vector3 v3 = new Vector3(this.getPos().x, this.getPos().y, 0);
            this.getData().getCamera().unproject(v3);
            this.cameraPos.set(v3.x, v3.y);
        }
    }

    public AppEntity getContextEntity() {
        return contextEntity;
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

    @Override
    protected void step(float delta) {
    }

    public void mouseDragged(int screenX, int screenY) {

        //Only trigger drag events if the current entity is actually Draggable
        if (this.contextEntity != null) {
            if (this.contextEntity.isDraggable()) {
                dragging = true;
                this.contextEntity.mouseDragged();
            }
        }
        //Otherwise, just act like the mouse moved
        this.mouseMoved(screenX, screenY);

    }


    public void mouseScrolled(float amountX, float amountY) {



        if (this.contextEntity != null) {
            this.contextEntity.mouseScrolled(amountX, amountY);
        } else {
            this.getData().getCamera().position.x += amountX;
            this.getData().getCamera().position.y -= amountY;
        }

        this.mouseMoved((int)this.getPos().x, (int) this.getPos().y);

        

    }

}
