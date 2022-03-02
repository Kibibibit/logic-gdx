package au.com.thewindmills.kibi.appEngine.objects;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;

public abstract class AppObject {

    /**
     * The next object will have this id;
     */
    public static long ID_NEXT = 0;

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

    private boolean shouldDispose = false;

    public AppObject(AppData data, Vector2 pos) {
        this.id = ID_NEXT;
        ID_NEXT++;
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

    public abstract void update(float delta);

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
