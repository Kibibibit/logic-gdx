package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;


/**
 * A generic object class that anything that should interact with the app each tick should
 * extend. These objects are not visible, for that extend {@link AppEntity}
 * 
 * @see AppEntity
 * @author Kibi
 */
public abstract class AppObject {

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
    public abstract void update(float delta);


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
