package au.com.thewindmills.logicgdx;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import au.com.thewindmills.logicgdx.models.TruthTable;
import au.com.thewindmills.logicgdx.utils.AppConstants;

/**
 * Main entry point for the application
 * 
 * @author Kibi
 */
public class App {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode(AppConstants.APP_WIDTH, AppConstants.APP_HEIGHT);
        config.setTitle(AppConstants.APP_TITLE);
        config.setResizable(false);

        makeDefaultGates();

        new Lwjgl3Application(new LogicGDX(), config);
    }

    private static void makeDefaultGates() {


        TruthTable buffer = new TruthTable("BUFFER");

        buffer.addOutput("O");
        buffer.addInput("I");

        buffer.setRow(new HashSet<String>(),new HashMap<String, Boolean>() {
            {
                put("O", false);
            }
        });
        buffer.setRow(new HashSet<String>(){{add("I");}},new HashMap<String, Boolean>() {
            {
                put("O", true);
            }
        });


        TruthTable andGate = new TruthTable("AND");

        andGate.addInput("A");
        andGate.addInput("B");
        andGate.addOutput("O");

        andGate.setRow(new HashSet<String>(), new HashMap<String, Boolean>() {
            {
                put("O", false);
            }
        });

        andGate.setRow(new HashSet<String>() {
            {
                add("A");
            }
        }, new HashMap<String, Boolean>() {
            {
                put("O", false);
            }
        });

        andGate.setRow(new HashSet<String>() {
            {
                add("B");
            }
        }, new HashMap<String, Boolean>() {
            {
                put("O", false);
            }
        });

        andGate.setRow(new HashSet<String>() {
            {
                add("A");
                add("B");
            }
        }, new HashMap<String, Boolean>() {
            {
                put("O", true);
            }
        });

        TruthTable not = new TruthTable("NOT");

        not.addInput("A");
        not.addOutput("!A");

        not.setRow(new HashSet<String>(), new HashMap<String, Boolean>() {
            {
                put("!A", true);
            }
        });
        not.setRow(new HashSet<String>() {
            {
                add("A");
            }
        }, new HashMap<String, Boolean>() {
            {
                put("!A", false);
            }
        });

        try {
            buffer.saveJsonObject();
            andGate.saveJsonObject();
            not.saveJsonObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
