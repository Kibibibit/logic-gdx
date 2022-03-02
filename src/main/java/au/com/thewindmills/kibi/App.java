package au.com.thewindmills.kibi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import au.com.thewindmills.kibi.appEngine.LogicApp;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LogicApp app = new LogicApp();
        new Lwjgl3Application(app, app.config);
    }
}
