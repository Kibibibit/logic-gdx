package au.com.thewindmills.kibi.appEngine;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;

import au.com.thewindmills.kibi.appEngine.objects.AppObject;
import au.com.thewindmills.kibi.appEngine.objects.entities.AppEntity;
import au.com.thewindmills.kibi.appEngine.utils.Batches;
import au.com.thewindmills.kibi.appEngine.utils.constants.AppConstants;
import au.com.thewindmills.kibi.models.components.Gate;

/**
 * Stores a list of all {@link AppObject}s, controls their 
 * update loops, render loops, and things like controlling the camera
 * 
 * @author Kibi
 */
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


    /**
     * the epoch tim of the last tick
     */
    private static long LAST_TICK;

    /**
     * The ticks per second of the application
     */
    private int tps = AppConstants.TPS;

    /**
     * Primary constructor, use this one!
     * @param application - The application to link to this data object
     */    
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

        LAST_TICK = System.currentTimeMillis();

        //TODO: clear this out - just some testing
        Gate gate = new Gate(this, 20, 20);
        gate.setVisible(true);

    }


    /**
     * Gets called after {@link LogicApp#update(float)}, assuming the app isn't paused
     * @param delta
     */
    private void step(float delta) {
    }

    /**
     * Dispose any {@link AppObject}s or {@link AppEntity}s that need disposing,
     * then call {@link AppData#cleanup()}
     */
    private void cleanupCore() {

        //First remove all objects

        objects.removeIf((AppObject obj) -> {
            return obj.willDispose();
        });

        //Then any entities

        entities.removeIf((AppEntity entity) -> {
            return entity.willDispose();
        });

        this.cleanup();
    }

    /**
     * Dispose of any excess stuff here, called before each update loop
     */
    private void cleanup() {}


    /**
     * Called when {@link LogicApp#dispose()} is called 
     */
    public void dispose() {}

    /**
     * Renders all entities, and adds entitiys from the buffer into the main list
     * @param batch - {@link Batches} used to render the objects
     */ 
    public void render(Batches batches) {
        FRAMES++;

        if (this.entityBuffer.size() > 0) {
            this.entities.addAll(this.entityBuffer);
            this.entityBuffer.clear();
        }

        batches.begin();

        for (AppEntity entity : entities) {
            if (entity.isVisible()) {
                entity.render(batches);
            }
        }

        this.draw(batches);

        batches.end();
    }

    /**
     * Any draw methods unrelated to entities should go here
     * @param batch
     */
    private void draw(Batches batches) {
    }

    /**
     * The main update loop of the application
     */
    public void update() {
        TICKS++;

        long tickTimeMillis = System.currentTimeMillis();

        float delta =  (float) LAST_TICK-tickTimeMillis;
        

        this.cleanupCore();

        if (this.objectBuffer.size() > 0) {
            this.objects.addAll(this.objectBuffer);
            this.objectBuffer.clear();
        }

        for (AppObject obj : objects){
            obj.update(delta);
        }

        if (!application.getPaused()) this.step(delta);

        float secondsPerTick = 1/(float) this.tps;
        long millisPerTick = ((long) secondsPerTick) * 1000;
        try {
            Thread.sleep(millisPerTick);
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception in update thread!");
            e.printStackTrace();
        }



        LAST_TICK = System.currentTimeMillis();


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
            System.out.println("Adding a new entity!");
            this.entityBuffer.add((AppEntity) object);
        }
        this.objectBuffer.add(object);

        return true;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public LogicApp getApplication() {
        return this.application;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void setTPS(int tps) {
        this.tps = tps;
    }

}
