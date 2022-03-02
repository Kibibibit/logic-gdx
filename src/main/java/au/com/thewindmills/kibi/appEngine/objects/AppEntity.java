package au.com.thewindmills.kibi.appEngine.objects;

import au.com.thewindmills.kibi.appEngine.utils.Batches;

public abstract class AppEntity extends AppObject {

    private boolean visible = false;

    public boolean isVisible() {
        return this.visible;
    }


    public void render(Batches batches) {
        
    }
    
}
