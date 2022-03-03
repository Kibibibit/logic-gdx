package au.com.thewindmills.kibi.logicApp.models;

public abstract class AbstractModel {
    public final long id;

    public static long nextId = 0L;

    public AbstractModel() {
        this.id = nextId;
        nextId++;
    }


}
