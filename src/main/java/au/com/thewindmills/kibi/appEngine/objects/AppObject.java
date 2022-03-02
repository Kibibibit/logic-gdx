package au.com.thewindmills.kibi.appEngine.objects;

import java.util.logging.Logger;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.objects.entities.AppEntity;

//TODO: Vector stuff - replace with floats

/**
 * A generic object class that anything that should interact with the app each tick should
 * extend. These objects are not visible, for that extend {@link AppEntity}
 * 
 * @see AppEntity
 * @author Kibi
 */
public abstract class AppObject {

    protected static final Logger LOGGER = Logger.getLogger("AppObject");

    /**
     * The next object will have this id;
     */
    public static long idNext = 0;

    /**
     * the id of this object
     */
    public final long id;

    /**
     * The position of the object
     */
    private Vector2 pos;

    /**
     * The app data object
     */
    private final AppData data;

    /**
     * If this is true, this object will be disposed
     * at the start of the next tick
     */
    private boolean shouldDispose = false;

    public AppObject(AppData data, Vector2 pos) {
        this.id = idNext;
        idNext++;
        this.pos = pos;
        this.data = data;

        data.addObject(this);
    }

    public final void setPos(float x, float y) {
        this.pos.set(x, y);
    }

    public final void setPos(Vector2 newPos) {
        this.setPos(newPos.x, newPos.y);
    }

    public final Vector2 getPos() {
        return this.pos;
    }

    public final void addPos(float x, float y) {
        this.pos.add(x, y);
    }

    public final void addPos(Vector2 otherPos) {
        this.addPos(otherPos.x, otherPos.y);
    }

    /**
     * Called during {@link AppData#update(float)}
     * @param delta - time in milliseconds since the last tick
     */
    public void update(float delta) {
        this.preStep(delta);
        this.update(delta);
        this.postStep(delta);
    }

    /**
     * Called at the start of each {@link AppObject#update(float)}, before step.
     * Override as needed
     * @param delta - time in milliseconds since last tick
     */
    protected void preStep(float delta) {}

    /**
     * Called in each {@link AppObject#update(float)}. All update code for each object should go here
     * @param delta - time in milliseconds since last tick
     */
    protected abstract void step(float delta);

    /**
     * Called at the end of each {@link AppObject#update(float)}, after step.
     * Override as needed
     * @param delta - time in milliseconds since last tick
     */
    protected void postStep(float delta) {}


    /**
     * This will cause this object to be disposed next frame
     */
    public void markForDisposal() {
        this.shouldDispose = true;
    }

    public abstract void dispose();

    public boolean willDispose() {
        return this.shouldDispose;
    }

    public AppData getData() {
        return this.data;
    }

}
