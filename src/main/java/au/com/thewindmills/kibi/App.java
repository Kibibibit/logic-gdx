package au.com.thewindmills.kibi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import au.com.thewindmills.kibi.appEngine.LogicApp;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.TruthTable;
import au.com.thewindmills.kibi.logicApp.utils.BinaryUtils;

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

        System.out.println("--- MAKING TRUTH TABLES ---");

        ConnectionMap testMap = new ConnectionMap();

        TruthTable and = new TruthTable(2, 1, testMap);
        and.setRow("00", "0");
        and.setRow("01", "0");
        and.setRow("10", "0");
        and.setRow("11", "1");

        TruthTable not = new TruthTable(1, 1, testMap);
        not.setRow("0", "1");
        not.setRow("1", "0");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TruthTable not2 = new TruthTable(1, 1, testMap);
        not2.setRow("0", "1");
        not2.setRow("1", "0");

        System.out.println("----------");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- CONNECTING AND TO NOT ---");


        testMap.addConnection(not, 0, and, 0);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- CONNECTING NOT TO NOT2 ---");

        testMap.addConnection(not2, 0, not, 0);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- UPDATING AND ---");

        and.trigger(0, true);
        and.trigger(1, true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- UPDATING AND AGAIN ---");


        and.trigger(1, false);

        /*
        // Create the actual application object
        LogicApp app = new LogicApp();
        // Start the application
        new Lwjgl3Application(app, app.config);

        
        System.exit(0);
        */
    }
}
