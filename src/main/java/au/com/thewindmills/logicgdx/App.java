package au.com.thewindmills.logicgdx;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import au.com.thewindmills.logicgdx.utils.AppConstants;

/**
 * Main entry point for the application
 * 
 * @author Kibi
 */
public class App 
{
    public static void main( String[] args ) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode(AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
        config.setTitle(AppConstants.APP_TITLE);


        new Lwjgl3Application(new LogicGDX(), config);
    }

}
