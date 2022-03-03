package au.com.thewindmills.kibi.appEngine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import au.com.thewindmills.kibi.appEngine.utils.constants.AppConstants;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;
import au.com.thewindmills.kibi.appEngine.utils.processors.AppInputProcessor;

/**
 * The main application object, containing cameras,
 * window settings, controlling loops and startup code
 * 
 * @author Kibi
 */
public class LogicApp extends ApplicationAdapter{

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

    /**
     * Boolean that controls the update loop
     */
    private boolean running = false;

    /**
     * Set to true when the app is paused
     */
    private boolean paused = false;

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

    /**
     * The set of batches used for rendering
     */
    private Batches batches;

    /**
     * The set of batches used for rendering static layers
     */
    private Batches staticBatches;

    /**
     * Functional interface so that we can call System.exit when the app is closed
     */
    private AppInterface appInterface;

    /**
     * Main constructor, Generally call this one
     */
    public LogicApp(AppInterface appInterface) {
        this(AppConstants.TITLE, AppConstants.FRAME_WIDTH, AppConstants.FRAME_HEIGHT, appInterface);
    }


    /**
     * Constructor for more detailed controll over the app
     * @param title - The title of the window
     * @param frameWidth - The width of the window
     * @param frameHeight - The height of the window
     * @param appInterface - This should call {@link App#close()}
     */
    public LogicApp(String title, int frameWidth, int frameHeight, AppInterface appInterface) {

        /**
         * Define the config settings
         */
        config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(title);
        config.setWindowedMode(frameWidth, frameHeight);

        //Need to disable audio to stop a crash when the app closes due to there being no AL lib
        config.disableAudio(true);

        this.appInterface = appInterface;

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
        
        //Create out data object
        data = new AppData(this);

        

    }

    /**
     * Set up the update loop, camera, and batches.
     * Called at the end of {@link LogicApp#create()}
     */
    private void start() {
        this.getCamera().setToOrtho(false, frameWidth, frameHeight);

        
        this.setRunning(true);
        

        /**
         * Run the update loop
         */
        updateThread = new Thread(new Runnable() {
           @Override
           public void run() {
               while (isRunning()) {
                getData().update();
               }
           } 
        });

        updateThread.start();
    }
    
    @Override
    public void create() {
        this.batches = new Batches();
        this.staticBatches = new Batches();
        this.getData().setCamera(camera);
        this.getData().init();

        Gdx.input.setInputProcessor(new AppInputProcessor(this.getData()));

        this.start();
    }

    @Override
    public void render() {

        ScreenUtils.clear(AppConstants.CLEAR_COLOR);
        this.getData().render(this.batches, this.staticBatches);
    }

    @Override
    public void dispose() {
        this.getData().dispose();
        this.batches.dispose();
        this.appInterface.onClose();
    }

    @Override
    public void pause() {
        super.pause();
        this.paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        this.paused = false;
    }

    public boolean getPaused() {
        return this.paused;
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
