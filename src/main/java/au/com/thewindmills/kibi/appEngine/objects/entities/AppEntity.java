package au.com.thewindmills.kibi.appEngine.objects.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.objects.AppObject;
import au.com.thewindmills.kibi.appEngine.utils.Batches;

public abstract class AppEntity extends AppObject {

    private boolean visible = false;

    public AppEntity(AppData data, Vector2 pos) {
        super(data, pos);
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void markForDisposal() {
        this.setVisible(false);
        super.markForDisposal();
    }


    protected void preDraw(Batches batches) {}
    protected abstract void draw(Batches batches);
    protected void postDraw(Batches batches) {}

    public final void render(Batches batches) {
        this.preDraw(batches);
        this.draw(batches);
        this.postDraw(batches);
    }

}
