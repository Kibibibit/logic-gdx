package au.com.thewindmills.kibi.appEngine;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;

import org.lwjgl.system.CallbackI.P;

import au.com.thewindmills.kibi.appEngine.objects.AppObject;

public class AppData {

    /**
     * The amount of ticks since the application started
     */
    private static Long ticks = 0L;

    /**
     * The time the last frame took place
     */
    private static long lastFrame;

    /**
     * All objects currently in the application being used
     */
    private final List<AppObject> objects;

    /**
     * Objects that need to be added into {@link AppData#objects} next tick
     */
    private final List<AppObject> objectBuffer;

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

        this.application = application;
    }

    /**
     * Gets called in {@link LogicApp#create()}
     */
    public void init() {
        lastFrame = System.currentTimeMillis();
    }

    public static float deltaTime() {
        long deltaTimeInMillis = System.currentTimeMillis() - lastFrame;
        float deltaTimeInSeconds = ((float) deltaTimeInMillis)/1000f;
        return deltaTimeInSeconds;
    }

    /**
     * Gets called after {@link LogicApp#update()}
     * @param delta
     */
    private void step(float delta) {

    }

    /**
     * Dispose any {@link AppObject}s or {@link AppEntity}s that need disposing,
     * then call {@link AppData#dispose()}
     */
    private void disposeCore() {
        objects.removeIf((AppObject obj) -> {
            return obj.willDispose();
        });

        this.dispose();
    }

    /**
     * Dispose of any excess stuff here
     */
    private void dispose() {

    }

    public void render() {


        float deltaTime = deltaTime();


        lastFrame = System.currentTimeMillis();

    }

    /**
     * The main update loop of the application
     */
    public void update(float delta) {
        ticks++;

        this.disposeCore();

        if (this.objectBuffer.size() > 0) {
            this.objects.addAll(this.objectBuffer);
            this.objectBuffer.clear();
        }

        for (AppObject obj : objects){
            obj.update(delta);
        }

        this.step(delta);

    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

}
