package au.com.thewindmills.kibi.appEngine.utils.io.system;


/**
 * Handles some utils related to finding the system operating system
 * 
 * @author Kibi
 */
public class SystemUtils {


    /**
     * The operating system currently in use
     */
    public static final OS OS = getOS(); 

    /**
     * Gets the operating system
     * @return
     */
    public static OS getOS() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.startsWith("linux")) {
            return au.com.thewindmills.kibi.appEngine.utils.io.system.OS.LINUX;
        }
        if (osName.startsWith("windows")) {
            return au.com.thewindmills.kibi.appEngine.utils.io.system.OS.WINDOWS;
        }

        return au.com.thewindmills.kibi.appEngine.utils.io.system.OS.UNKNOWN;
    }
    
}
