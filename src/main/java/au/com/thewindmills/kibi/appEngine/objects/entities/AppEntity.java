package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.objects.AppObject;
import au.com.thewindmills.kibi.appEngine.utils.ArrayUtils;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
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
    private boolean visible = true;

    /**
     * Sets what layer the object is drawn on
     */
    private final String layer;
    
    /**
     * Determines if the mouse being dragged with this object should have different behaviour to normal mouse movement
     */
    protected boolean isDraggable = false;

    /**
     * This depth controls the draw order of the entities, lower depths are drawn first
     */
    private final int depth;

    /**
     * True if this app is on a static draw layer
     */
    private final boolean onStaticLayer;


    private boolean beingDragged = false;

    private boolean triggerReleaseAfterDrag = false;

    private boolean isScrollable = false;

    private Color textColor = null;

    protected Vector2 mouseOffset;

    protected boolean canDelete = false;

    public AppEntity(AppData data, String layer, int depth, Vector2 pos) {
        super(data, pos);
        this.layer = layer;
        this.depth = depth;
        this.mouseOffset = new Vector2(0,0);

        onStaticLayer = ArrayUtils.arrayContains(Layers.STATIC_LAYERS, layer);

    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void markForDisposal() {
        //Set visible to false so the object disappears
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


    public void onRenderText(Batches batches) {}

    public final void renderText(Batches batches) {
        if (this.textColor != null) {
            batches.font.setColor(this.textColor);
            onRenderText(batches);
        }
    }

    /**
     * Called every {@link AppData#render(Batches)}
     * @param batches
     */
    public final void render(Batches batches) {
        this.preDraw(batches);
        this.draw(batches);
        this.postDraw(batches);
    }

    public abstract boolean inBounds(float x, float y);

    public boolean inBounds(Vector2 point) {
        return this.inBounds(point.x, point.y);
    }

    public final String getLayer() {
        return this.layer;
    }

    public final int getDepth() {
        return this.depth;
    }

    public boolean onStaticLayer() {
        return this.onStaticLayer;
    }
    
    public final void setTextColor(Color color) {
        this.textColor = color;
    }

    public final Color getTextColor() {
        return this.textColor;
    }

    /**
     * Event called when the mouse clicks on this Entity
     * @param button - The mouse button that was pressed
     */
    public final void onMousePressed(int button) {

        Vector2 mousePos = this.onStaticLayer ? this.getData().getMouse().getGlobalPos() : this.getData().getMouse().getCameraPos();

        this.mouseOffset.set(mousePos.cpy().sub(this.getPos()));

        this.doOnMousePressed(button);
    }

    /**
     * Event called when the mouse is released on this Entity
     * @param button - The mouse button that was released
     */
    public final void onMouseReleased(int button) {

        this.mouseOffset.set(0, 0);

        if (!beingDragged || triggerReleaseAfterDrag) {
            this.doOnMouseReleased(button);   
        }
        beingDragged = false;

        

    }

    public void doOnMousePressed(int button) {}

    public void doOnMouseReleased(int button) {}


   

    /**
     * Event called when the mouse selects this componenet,
     * override as needed
     */
    public void onMouseEnter() {}

    /**
     * Event called when the mouse deselects this component,
     * override as needed
     */
    public void onMouseLeave() {}

    public final void mouseDragged() {
        beingDragged = true;
        onMouseDragged();
    }

    /**
     * Event called when the mouse is dragged across the screen.
     * Override as needed
     */
    public void onMouseDragged() {}

    /**
     * Event called with the mouse wheel is scrolled.
     * Override as needed
     * @param amountX - The amount the wheel went sideways?
     * @param amountY - the vertical scroll amount
     */
    public void mouseScrolled(float amountX, float amountY) {}
    

    public void setTriggerReleaseAfterDrag(boolean v) {
        this.triggerReleaseAfterDrag = v;
    }

    public boolean isDraggable() {
        return this.isDraggable;
    }

    public boolean isScrollable() {
        return this.isScrollable;
    }

    public void setIsScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public void setCanDelete(boolean v) {
        this.canDelete = v;
    }

    public boolean canDelete() {
        return this.canDelete;
    }

    public void setBeingDragged(boolean v) {
        this.beingDragged = v;
    }

}
