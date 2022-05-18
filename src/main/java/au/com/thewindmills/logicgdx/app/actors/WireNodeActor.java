package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class WireNodeActor extends Actor {

    WireActor parent;

    public WireNodeActor(WireActor parent) {
        this.parent = parent;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        SheetSection fillSprite = SheetSection.WX;
        if (parent.getState()) {
            fillSprite = SheetSection.WO;
        }

        batch.draw(parent.getManager().getSprite(fillSprite), this.getX(), this.getY());
        batch.draw(parent.getManager().getSprite(SheetSection.WN), this.getX(), this.getY());
    }
    
}
