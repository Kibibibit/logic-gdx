package au.com.thewindmills.kibi.appEngine.utils.io.json;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.utils.io.system.SystemUtils;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

/**
 * Contains methods for writing and reading JSONObjects
 * from .json files.
 * 
 * @author Kibi
 */
public class JSONUtils {

    protected static final Logger LOGGER = Logger.getLogger("JSONWriter");

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

    public static String getMainFolder() {

        switch(SystemUtils.OS) {
            case LINUX:
                return System.getProperty("user.home") + "/.logicgdx/models/";
            default:
                return "./models/";
            
        }

    }
    
}
