package au.com.thewindmills.logicgdx;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.thewindmills.logicgdx.models.ChipComponent;
import au.com.thewindmills.logicgdx.models.TruthTable;
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

        TruthTable table = new TruthTable("AND");

        table.addInput("A");
        table.addInput("B");
        table.addOutput("O");

        table.setRow(new HashSet<String>(), new HashMap<String, Boolean>(){{
            put("O", false);
        }});

        table.setRow(new HashSet<String>(){{
            add("A");
        }}, new HashMap<String, Boolean>(){{
            put("O", false);
        }});

        table.setRow(new HashSet<String>(){{
            add("B");
        }}, new HashMap<String, Boolean>(){{
            put("O", false);
        }});

        table.setRow(new HashSet<String>(){{
            add("A");
            add("B");
        }}, new HashMap<String, Boolean>(){{
            put("O", true);
        }});

        TruthTable table2 = new TruthTable("NOT");

        table2.addInput("A");
        table2.addOutput("!A");

        table2.setRow(new HashSet<String>(), new HashMap<String, Boolean>(){{
            put("!A", true);
        }});
        table2.setRow(new HashSet<String>(){{add("A");}}, new HashMap<String, Boolean>(){{
            put("!A", false);
        }});


        ChipComponent chip = new ChipComponent("NAND");

        chip.addInput("A'");
        chip.addInput("B'");
        chip.addOutput("O'");

        chip.addChild(table);

        chip.setExternalMappingIn(table.getIoId("A"), chip.getIoId("A'"), true);
        chip.setExternalMappingIn(table.getIoId("B"), chip.getIoId("B'"), true);

        chip.addChild(table2);

        chip.setInternalMapping(table2.getIoId("A"), table.getIoId("O"), true);


        chip.setExternalMappingOut(table2.getIoId("!A"),chip.getIoId("O'"), true);

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(chip.toJsonObject());
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        new Lwjgl3Application(new LogicGDX(), config);
    }

}
