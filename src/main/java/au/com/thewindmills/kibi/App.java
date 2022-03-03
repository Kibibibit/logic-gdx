package au.com.thewindmills.kibi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import au.com.thewindmills.kibi.appEngine.LogicApp;

/**
 * Main entry point for the application
 * 
 * @author Kibi
 */
public class App 
{
    // The lwjgl backend, referenced here in case we need it
    static Lwjgl3Application lwjgl;

    public static void main( String[] args )
    {

        // Create the actual application object
        LogicApp app = new LogicApp();
        // Start the application
        new Lwjgl3Application(app, app.config);

        
        System.exit(0);
    }
}
