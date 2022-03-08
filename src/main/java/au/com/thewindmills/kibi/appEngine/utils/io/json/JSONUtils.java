package au.com.thewindmills.kibi.appEngine.utils.io.json;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import au.com.thewindmills.kibi.appEngine.utils.io.system.SystemUtils;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

/**
 * Contains methods for writing and reading JSONObjects
 * from .json files.
 * 
 * @author Kibi
 */
public class JSONUtils {

    protected static final Logger LOGGER = Logger.getLogger("JSONUtils");

    /**
     * Writes the given json object into a file.
     * This will overwrite any existing files with the same name in the save folder
     * @param object
     */
    public static void writeToFile(JSONObject object) {

        if (!object.containsKey(LogicModel.FIELD_NAME)) {
            LOGGER.warning("No Name field! Not saving");
        }

        String name = (String) object.get("name");

        String filename = name+".json";

        String path = getMainFolder();

        File directory = new File(path);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(path+"/"+filename);

        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(object.toJSONString());
            bw.close();
            fw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a JSONObject based on the given json file.
     * Returns null in the event of an error
     * @param filename
     * @return
     */
    public static JSONObject loadJsonObject(String filename) {

        FileReader fr;

        try {
            fr = new FileReader(getMainFolder()+"/"+filename);
        } catch (FileNotFoundException e) {
            LOGGER.warning("No file with name " + filename + " found!");
            return null;
        }



        JSONParser parser = new JSONParser();
        JSONObject out = null;

        try {
            out = (JSONObject) parser.parse(fr);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }



        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return out;

    }

    /**
     * Returns the save location based on the operating system
     * //TODO: Work out windows and mac locations
     * @return
     */
    public static String getMainFolder() {

        switch(SystemUtils.OS) {
            case LINUX:
                return System.getProperty("user.home") + "/.logicgdx/models/";
            default:
                return "./models/";
            
        }

    }
    
}
