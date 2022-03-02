package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.objects.AppObject;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

/**
 * An abstract class that any visible {@link AppObject} extends.
 * 
 * @author Kibi
 */
public abstract class AppEntity extends AppObject {

    /**
     * Controls if the object can be seen or not
     */
    private boolean visible = false;

    /**
     * Sets what layer the object is drawn on
     */
    private String layer;

    public AppEntity(AppData data, String layer, Vector2 pos) {
        super(data, pos);
        this.layer = layer;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void markForDisposal() {
        //Set visible to flase so the object disappears
        this.setVisible(false);
        super.markForDisposal();
    }

    /**
     * Called before the draw method, override as needed. <br>
     * {@link ShapeEntity} draws their shapes here 
     * @param batches - {@link Batches} to draw with
     */
    protected void preDraw(Batches batches) {}

    /** 
     * Main draw method, called every {@link AppEntity#render(Batches)}
     * @param batches - {@link Batches} to draw with
    */
    protected abstract void draw(Batches batches);

    /**
     * Called after the draw method, override as needed
     * @param batches - {@link Batches} to draw with
     */
    protected void postDraw(Batches batches) {}

    /**
     * Called every {@link AppData#render(Batches)}
     * @param batches
     */
    public final void render(Batches batches) {
        this.preDraw(batches);
        this.draw(batches);
        this.postDraw(batches);
    }

    public final String getLayer() {
        return this.layer;
    }

}
