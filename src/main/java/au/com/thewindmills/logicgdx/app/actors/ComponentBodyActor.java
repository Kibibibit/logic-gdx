package au.com.thewindmills.logicgdx.app.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.app.assets.SheetSection;

public class ComponentBodyActor extends Actor {

    private ComponentActor parent;
    private int tilesHeight;
    private int tilesWidth;
    private String name;

    public ComponentBodyActor(int height, int width, ComponentActor parent) {
        super();
        this.parent = parent;

        this.tilesHeight = height;
        this.tilesWidth = width;

        this.name = parent.getComponent().getName();

        this.setHeight(height * LogicAssetManager.TILE_SIZE);
        this.setWidth(width * LogicAssetManager.TILE_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(parent.getManager().getSprite(SheetSection.MX), this.getX(), this.getY(), this.getWidth(), this.getHeight());

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

        batch.setColor(Color.BLACK);

        parent.getManager().drawTextCentered(batch, name, this.getX()+(this.getWidth()*0.5f), this.getY()+(this.getHeight()*0.5f));

        batch.setColor(Color.WHITE);
    }

    public void drag(float x, float y) {
        this.getParent().setPosition(x, y);
    }

}
