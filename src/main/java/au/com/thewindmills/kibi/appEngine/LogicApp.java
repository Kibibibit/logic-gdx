package au.com.thewindmills.kibi.appEngine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import au.com.thewindmills.kibi.appEngine.utils.Batches;
import au.com.thewindmills.kibi.appEngine.utils.constants.AppConstants;


public class LogicApp extends ApplicationAdapter {

    /**
     * Configuration for the lwjgl backend, like height,
     * width, windowed mode and title.
     */
    public final Lwjgl3ApplicationConfiguration config;
    /**
     * The height and width of the lwjgl window
     */
    public final int frameWidth, frameHeight;
    /**
     * The title of the lwjgl window
     */
    public final String title;


    private boolean running = false;

    /**
     * The camera of the app;
     */
    private OrthographicCamera camera;

    /**
     * The main data object storing objects and such
     */
    private AppData data;

    /**
     * The thread running the main update loop
     */
    private Thread updateThread;

    private Batches batches;

    public LogicApp() {
        this(AppConstants.TITLE, AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT);
    }

    public LogicApp(String title, int frameWidth, int frameHeight) {

        /**
         * Define the config settings
         */
        config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(title);
        config.setWindowedMode(frameWidth, frameHeight);

        /**
         * Set the public final vars
         */
        this.title = title;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        /**
         * Create the camera
         */
        this.camera = new OrthographicCamera();

        this.batches = new Batches();

        data = new AppData(this);

    }

    private void start() {
        this.getCamera().setToOrtho(false, frameWidth, frameHeight);
        this.batches.setProjectionMatrix(this.getCamera().combined);
        this.setRunning(true);

        /**
         * Run the update loop
         */
        updateThread = new Thread(new Runnable() {
           @Override
           public void run() {
               while (isRunning()) {
                getData().update(Gdx.graphics.getDeltaTime());
               }
           } 
        });
        updateThread.start();
    }
    
    @Override
    public void create() {
        this.getData().setCamera(camera);
        this.getData().init();

        this.start();
    }

    @Override
    public void render() {
        ScreenUtils.clear(AppConstants.CLEAR_COLOR);
        this.getData().render(this.batches);
    }

    @Override
    public void dispose() {
        this.getData().dispose();
        this.batches.dispose();
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public AppData getData() {
        return this.data;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
