package au.com.thewindmills.kibi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import au.com.thewindmills.kibi.appEngine.AppInterface;
import au.com.thewindmills.kibi.appEngine.LogicApp;

/**
 * Hello world!
 *
 */
public class App 
{
    static Lwjgl3Application lwjgl;

    public static void main( String[] args )
    {
        LogicApp app = new LogicApp(new AppInterface() {
            @Override
            public void onClose() {
                close();
            }
        });
        new Lwjgl3Application(app, app.config);
    }


    public static void close() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
