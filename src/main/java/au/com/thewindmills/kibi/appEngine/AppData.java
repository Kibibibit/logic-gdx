package au.com.thewindmills.kibi.appEngine;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;

import au.com.thewindmills.kibi.appEngine.objects.AppEntity;
import au.com.thewindmills.kibi.appEngine.objects.AppObject;
import au.com.thewindmills.kibi.appEngine.utils.Batches;

public class AppData {

    /**
     * The amount of ticks since the application started
     */
    private static Long TICKS = 0L;

    /**
     * The amount of frames that have passed since the app started
     */
    private static Long FRAMES = 0L;

    /**
     * All objects currently in the application being used
     */
    private final List<AppObject> objects;

    /**
     * Objects that need to be added into {@link AppData#objects} next tick
     */
    private final List<AppObject> objectBuffer;

    /**
     * All entities that need rendering
     */
    private final List<AppEntity> entities;

    /**
     * Entities that need to be added into {@link AppData#entities} next frame
     */
    private final List<AppEntity> entityBuffer;

    /**
     * The main camera from {@link LogicApp#getCamera()}
     */
    private Camera camera;

    /**
     * Reference to the main application
     */
    private LogicApp application;

    
    public AppData(LogicApp application) {
        objects = new ArrayList<AppObject>();
        objectBuffer = new ArrayList<AppObject>();
        entities = new ArrayList<AppEntity>();
        entityBuffer = new ArrayList<AppEntity>();

        this.application = application;
    }

    /**
     * Gets called in {@link LogicApp#create()}
     */
    public void init() {

    }


    /**
     * Gets called after {@link LogicApp#update()}
     * @param delta
     */
    private void step(float delta) {

    }

    /**
     * Dispose any {@link AppObject}s or {@link AppEntity}s that need disposing,
     * then call {@link AppData#cleanup()}
     */
    private void cleanupCore() {
        objects.removeIf((AppObject obj) -> {
            return obj.willDispose();
        });

        entities.removeIf((AppEntity entity) -> {
            return entity.willDispose();
        });

        this.cleanup();
    }

    /**
     * Dispose of any excess stuff here
     */
    private void cleanup() {

    }


    /**
     * Called when {@link LogicApp#dispose()} is called 
     */
    public void dispose() {

    }

    /**
     * Renders all entities, and adds entitiys from the buffer into the main list
     * @param batch
     */
    public void render(Batches batches) {
        FRAMES++;

        if (this.entityBuffer.size() > 0) {
            this.entities.addAll(this.entityBuffer);
            this.entityBuffer.clear();
        }

        for (AppEntity entity : entities) {
            if (entity.isVisible()) {
                entity.render(batches);
            }
        }

        this.draw(batches);
    }

    /**
     * Any draw methods should go in here
     * @param batch
     */
    private void draw(Batches batches) {

    }

    /**
     * The main update loop of the application
     */
    public void update(float delta) {
        TICKS++;

        this.cleanupCore();

        if (this.objectBuffer.size() > 0) {
            this.objects.addAll(this.objectBuffer);
            this.objectBuffer.clear();
        }

        for (AppObject obj : objects){
            obj.update(delta);
        }

        this.step(delta);

    }

    /**
     * Adds a new object into the buffers.
     * @param object
     * @return - If the object was added successfully (false usually means null object)
     */
    public boolean addObject(AppObject object) {
        if (object == null) {
            return false;
        }
        if (object instanceof AppEntity) {
            this.entityBuffer.add((AppEntity) object);
        }
        this.objectBuffer.add(object);

        return true;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

}
