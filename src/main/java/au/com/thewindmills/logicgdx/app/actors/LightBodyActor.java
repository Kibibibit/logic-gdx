package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class LightBodyActor extends ComponentBodyActor {
    protected LightComponentActor lightParent;

    public LightBodyActor(int height, int width, ComponentActor parent) {
        super(height, width, parent);
        lightParent = (LightComponentActor) getParentActor();
    }

    @Override
    protected void drawText(Batch batch) {
        batch.setColor(Color.BLACK);

        lightParent.getManager().drawTextCentered(batch, lightParent.getIoName(), this.getX() + (this.getWidth() * 0.5f),
                this.getY() + (this.getHeight() * 0.5f));

        batch.setColor(Color.WHITE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        SheetSection fillSprite = SheetSection.MX;
        if (lightParent.getState()) {
            fillSprite = SheetSection.MO;
        }

        batch.draw(lightParent.getManager().getSprite(fillSprite), this.getX(), this.getY(), this.getWidth(),
                this.getHeight());

        batch.draw(
                lightParent.getManager().getSprite(SheetSection.L),
                this.getX(),
                this.getY(),
                LogicAssetManager.TILE_SIZE,
                LogicAssetManager.TILE_SIZE * getTilesHeight());

        batch.draw(
                lightParent.getManager().getSprite(SheetSection.R),
                this.getX() + ((getTilesWidth() - 1) * LogicAssetManager.TILE_SIZE),
                this.getY(),
                LogicAssetManager.TILE_SIZE,
                LogicAssetManager.TILE_SIZE * getTilesHeight());

        batch.draw(
                lightParent.getManager().getSprite(SheetSection.B),
                this.getX(),
                this.getY(),
                LogicAssetManager.TILE_SIZE * getTilesWidth(),
                LogicAssetManager.TILE_SIZE);

        batch.draw(
                lightParent.getManager().getSprite(SheetSection.T),
                this.getX(),
                this.getY() + ((getTilesHeight() - 1) * LogicAssetManager.TILE_SIZE),

                LogicAssetManager.TILE_SIZE * getTilesWidth(),
                LogicAssetManager.TILE_SIZE);

        drawText(batch);
    }

}
