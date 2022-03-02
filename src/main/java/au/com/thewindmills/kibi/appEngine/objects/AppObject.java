package au.com.thewindmills.kibi.appEngine.objects;

public abstract class AppObject {
 
    private boolean willDispose = false;


    public abstract void update(float delta);

    public void markForDisposal() {
        this.willDispose = true;
    }

    public void dispose() {

    }

    public boolean willDispose() {
        return this.willDispose;
    }

}
