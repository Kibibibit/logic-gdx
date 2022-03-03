package au.com.thewindmills.kibi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import au.com.thewindmills.kibi.appEngine.LogicApp;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.TruthTable;
import au.com.thewindmills.kibi.logicApp.utils.Binary;

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

        int testNum = 69;

        System.out.println(Binary.getValueFromBits(Binary.getBitsFromValue(testNum)));

        ConnectionMap testMap = new ConnectionMap();

        TruthTable and = new TruthTable(2, 1, testMap);
        and.setRow("00", "0");
        and.setRow("01", "0");
        and.setRow("10", "0");
        and.setRow("11", "1");

        TruthTable not = new TruthTable(1, 1, testMap);
        not.setRow("0", "1");
        not.setRow("1", "0");

        TruthTable not2 = new TruthTable(1, 1, testMap);
        not.setRow(0, 1);
        not.setRow(1, 0);

        testMap.addConnection(not, 0, and, 0);
        testMap.addConnection(not2 , 0, not, 0);

        and.trigger(0, true);
        and.trigger(1, true);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        // Create the actual application object
        LogicApp app = new LogicApp();
        // Start the application
        new Lwjgl3Application(app, app.config);

        
        System.exit(0);
        */
    }
}
