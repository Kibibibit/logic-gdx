package au.com.thewindmills.kibi;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

import au.com.thewindmills.kibi.appEngine.LogicApp;
import au.com.thewindmills.kibi.appEngine.utils.io.json.JSONUtils;
import au.com.thewindmills.kibi.logicApp.entities.io.IoComponent;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.TruthTable;

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
        createBaseGates();
        // Create the actual application object
        LogicApp app = new LogicApp();
        // Start the application

        try {
            new Lwjgl3Application(app, app.config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        
        System.exit(0);
    }

    /**
     * We always need to have NOT and AND, so they must be recreated on startup
     * incase the user has overwritten them
     */
    public static void createBaseGates() {

        ConnectionMap dummy = new ConnectionMap();

        TruthTable not = new TruthTable("NOT", 1, 1, dummy);
        not.setRow("0", "1");
        not.setRow("1", "0");

        TruthTable and = new TruthTable("AND", 2, 1, dummy);
        and.setRow("00", "0");
        and.setRow("01", "0");
        and.setRow("10", "0");
        and.setRow("11", "1");

        TruthTable ioTable = new TruthTable(IoComponent.IO_NAME, 1, 1, dummy);
        ioTable.setRow("0","0");
        ioTable.setRow("1", "1");
        
        
        JSONUtils.writeToFile(not.toJson());
        JSONUtils.writeToFile(and.toJson());
        JSONUtils.writeToFile(ioTable.toJson());
    }

}
