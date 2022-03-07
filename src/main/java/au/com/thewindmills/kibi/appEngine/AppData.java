package au.com.thewindmills.kibi.appEngine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiAppBar;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiButton;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiButton.ButtonPress;
import au.com.thewindmills.kibi.appEngine.objects.AppObject;
import au.com.thewindmills.kibi.appEngine.objects.MouseObject;
import au.com.thewindmills.kibi.appEngine.objects.entities.AppEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.AppConstants;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;
import au.com.thewindmills.kibi.appEngine.utils.gfx.ColorUtils;
import au.com.thewindmills.kibi.logicApp.entities.ComponentBody;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.TruthTable;

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
    private static Long ticks = 0L;

    /**
     * The amount of frames that have passed since the app started
     */
    private static Long frames = 0L;


    //TODO: replace these all with an ID hashmap in future to increase lookup speeds
    /**
     * All objects currently in the application being used
     */
    private final List<AppObject> objects;

    /**
     * Objects that need to be added into {@link AppData#objects} next tick
     */
    private final List<AppObject> objectBuffer;

    /**
     * Defines the layers that objects can be rendered on. Lower indexes mean theyll
     * be rendered earlier
     */
    private final String[] layers;

    /**
     * All entities that need rendering. The Key of each entry is the Layer that the
     * objects will be rendered on
     */
    private final Map<String, List<AppEntity>> entities;

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
     * the system time of the last tick
     */
    private static long lastTick;

    /**
     * The ticks per second of the application
     */
    private int tps = AppConstants.TPS;

    /**
     * The number of milliseconds between each tick
     */
    private int mspt;

    /**
     * A mouse object that keeps track of various mouse details
     */
    private MouseObject mouse;

    /**
     * Primary constructor, use this one!
     * 
     * @param application - The application to link to this data object
     */
    public AppData(LogicApp application) {
        objects = new ArrayList<AppObject>();
        objectBuffer = new ArrayList<AppObject>();
        entities = new HashMap<String, List<AppEntity>>();
        entityBuffer = new ArrayList<AppEntity>();

        layers = Layers.LAYERS;

        for (String layer : layers) {
            entities.put(layer, new ArrayList<AppEntity>());
        }

        // The mouse object is essential and so is created here
        mouse = new MouseObject(this);

        this.application = application;
        // Milliseconds per tick is equal to 1/ticks per second, multiplied by 1000, or
        // just 1000 over tps
        mspt = (int) (1000f / (float) tps);
    }

    /**
     * Gets called in {@link LogicApp#create()}
     */
    public void init() {

        lastTick = System.currentTimeMillis();

        createUi();

    }

    /**
     * Gets called at the end of {@link AppData#init()}
     */
    private void createUi() {
        //TODO: Create a builder pattern for this
        //Set the constants for this somewhere

        UiAppBar appBar = new UiAppBar(this, Layers.UI, 0, 25)
            .withFillColor(ColorUtils.grey(0.3f))
            .withStrokeColor(ColorUtils.grey(0.3f));

        // UiEntity appBar = new UiPanel(this, Layers.UI, 0, 0, Gdx.graphics.getHeight()-25, Gdx.graphics.getWidth(), 25);
        // appBar.setFillColor(new Color(0.3f, 0.3f, 0.3f, 1));
        // appBar.setStrokeColor(new Color(0.3f, 0.3f, 0.3f, 1));

        
        UiEntity button = new UiButton(new Vector2(2.5f, 2.5f), new RectShape(0, 0, 50, 20), appBar, new ButtonPress() {
            public void onPressed(int button) {
                if (button == Input.Buttons.LEFT) {
                    System.out.println("Hello!");
                }
                
            }

            public void onReleased(int button) {
                if (button == Input.Buttons.LEFT) {
                    System.out.println("Goodbye!");
                }
            }
        });
        button.setFillColor(new Color(0.35f, 0.35f, 0.35f, 1));
        button.setStrokeColor(new Color(0.3f, 0.3f, 0.3f, 1));


        ConnectionMap testMap = new ConnectionMap();
        AppEntity testDrag = new ComponentBody(this, Layers.MAIN, 0, new Vector2(50,50), new RectShape(50 ,50, 100, 100), new TruthTable(2, 1, testMap));


    }

    /**
     * Gets called after {@link AppData#update())}, assuming the app isn't paused
     * 
     * @param delta
     */
    private void step(float delta) {
    }

    /**
     * Dispose any {@link AppObject}s or {@link AppEntity}s that need disposing,
     * then call {@link AppData#cleanup()}
     */
    private void cleanupCore() {

        // First remove all objects

        objects.removeIf((AppObject obj) -> {
            return obj.willDispose();
        });

        // Then any entities
        for (String layer : layers) {
            entities.get(layer).removeIf((AppEntity entity) -> {
                return entity.willDispose();
            });
        }

        this.cleanup();
    }

    /**
     * Dispose of any excess stuff here, called before each update loop
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
     * 
     * @param batch - {@link Batches} used to render the objects
     */
    public void render(Batches batches, Batches staticBatches) {
        frames++;

        if (this.entityBuffer.size() > 0) {
            for (AppEntity entity : this.entityBuffer) {
                entities.get(entity.getLayer()).add(entity);
                entities.get(entity.getLayer()).sort(Comparator.comparingInt(AppEntity::getDepth));
            }
            this.entityBuffer.clear();
        }

        this.getCamera().update();

        batches.begin();

        //We only do this for batches, as static batches are based on the window and not the camera
        batches.setProjectionMatrix(this.getCamera().combined);

        staticBatches.begin();

        for (String layer : layers) {

            for (AppEntity entity : entities.get(layer)) {
                if (entity.isVisible()) {
                    entity.render(entity.onStaticLayer() ? staticBatches : batches);
                }
            }
        }

        this.draw(batches);

        batches.end();
        staticBatches.end();
    }

    /**
     * Any draw methods unrelated to entities should go here
     * 
     * @param batch
     */
    private void draw(Batches batches) {
    }

    /**
     * The main update loop of the application
     */
    public void update() {

        // need to make sure that it's been long enough for another tick
        long tickTimeMillis = System.currentTimeMillis();

        float delta = tickTimeMillis - lastTick;

        if (delta < mspt) {
            return;
        }

        ticks++;

        this.cleanupCore();

        if (this.objectBuffer.size() > 0) {
            this.objects.addAll(this.objectBuffer);
            this.objectBuffer.clear();
        }

        for (AppObject obj : objects) {
            obj.update(delta);
        }

        if (!application.getPaused())
            this.step(delta);

        lastTick = System.currentTimeMillis();

    }

    /**
     * Adds a new object into the buffers.
     * 
     * @param object
     * @return - If the object was added successfully (false usually means null
     *         object)
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

    public MouseObject getMouse() {
        return this.mouse;
    }

    public AppObject getObjectById(long id) {
        for (AppObject object : objects) {
            if (object.id == id) {
                return object;
            }
        }
        return null;
    }

    public Map<String, List<AppEntity>> getEntities() {
        return this.entities;
    }

}
