package au.com.thewindmills.kibi.appEngine;

/**
 * Functional interface so we can call System.exit when the app is closed
 * 
 * @author Kibi
 */
public interface AppInterface {

    /**
     * Should call {@link System#exit()}
     */
    public void onClose();
}
