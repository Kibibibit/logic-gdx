package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class SwitchButtonActor extends Actor {

    private SwitchComponentActor parent;

    private int tilesHeight = SwitchComponentActor.SWITCH_SIZE-2;
    private int tilesWidth = SwitchComponentActor.SWITCH_SIZE-2;

    public SwitchButtonActor(SwitchComponentActor parent) {
        this.parent = parent;
        this.setPosition(LogicAssetManager.TILE_SIZE, LogicAssetManager.TILE_SIZE);
        this.setWidth(LogicAssetManager.TILE_SIZE*tilesWidth);
        this.setHeight(LogicAssetManager.TILE_SIZE*tilesHeight);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        SheetSection fillSprite = SheetSection.MX;
        if (parent.getState()) {
            fillSprite = SheetSection.MO;
        }

        batch.draw(parent.getManager().getSprite(fillSprite), this.getX(), this.getY(), this.getWidth(), this.getHeight());

        batch.draw(
            parent.getManager().getSprite(SheetSection.L),
                this.getX(),
                this.getY(),
                LogicAssetManager.TILE_SIZE,
                LogicAssetManager.TILE_SIZE * tilesHeight);

        batch.draw(
            parent.getManager().getSprite(SheetSection.R),
                this.getX() + ((tilesWidth - 1) * LogicAssetManager.TILE_SIZE),
                this.getY(),
                LogicAssetManager.TILE_SIZE,
                LogicAssetManager.TILE_SIZE * tilesHeight);

        batch.draw(
            parent.getManager().getSprite(SheetSection.B),
                this.getX(),
                this.getY(),
                LogicAssetManager.TILE_SIZE * tilesWidth,
                LogicAssetManager.TILE_SIZE);

        batch.draw(
            parent.getManager().getSprite(SheetSection.T),
                this.getX(),
                this.getY() + ((tilesHeight - 1) * LogicAssetManager.TILE_SIZE),

                LogicAssetManager.TILE_SIZE * tilesWidth,
                LogicAssetManager.TILE_SIZE);

        drawText(batch);
    }

    protected void drawText(Batch batch) {
        batch.setColor(Color.BLACK);

        parent.getManager().drawTextCentered(batch,parent.getIoName(), this.getX()+(this.getWidth()*0.5f), this.getY()+(this.getHeight()*0.5f));

        batch.setColor(Color.WHITE);
    }

    public void drag(float x, float y) {
        this.parent.setPosition(x, y);
    }


    public void toggle() {
        System.out.println("TOGGLING!");
        this.parent.toggle();
    }

}
