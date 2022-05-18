package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class WirePartActor extends Actor {

    WireActor parent;
    boolean flipped = false;

    public WirePartActor(WireActor parent) {
        this.parent = parent;
        this.setPosition(4, 4);
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public boolean getFlipped() {
        return flipped;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        SheetSection borderSprite = SheetSection.EW;
        SheetSection fillSprite = SheetSection.WX;

        int xMult = flipped ? 1 : 0;
        int yMult = 0;

        if (this.getHeight() > this.getWidth()) {
            borderSprite = SheetSection.NS;
            xMult = 0;
            yMult = flipped ? 1 : 0;
        }

        if (this.parent.getState()) {
            fillSprite = SheetSection.WO;
        }

        batch.draw(
                parent.getManager().getSprite(fillSprite),
                this.getX() - (xMult * this.getWidth()),
                this.getY() - (yMult * this.getHeight()),
                this.getWidth(),
                this.getHeight());
        batch.draw(
                parent.getManager().getSprite(borderSprite),
                this.getX() - (xMult * this.getWidth()),
                this.getY() - (yMult * this.getHeight()),
                this.getWidth(),
                this.getHeight());

    }

}
