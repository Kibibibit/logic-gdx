package au.com.thewindmills.kibi.logicApp.models;

/**
 * Superclass for all abstract models within the system.
 * Mostly just used for id handeling
 * 
 * @author Kibi
 */
public abstract class AbstractModel {

    /**
     * The id of this model, unique
     */
    public final long id;

    /**
     * When an AbstractModel is created, this will be its id, and then this will be incremented
     */
    public static long nextId = 0L;

    public AbstractModel() {
        this.id = nextId;
        nextId++;
    }


}
