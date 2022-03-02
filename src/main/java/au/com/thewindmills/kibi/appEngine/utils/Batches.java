package au.com.thewindmills.kibi.appEngine.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 * Stores the all the renders needed each frame
 */
public class Batches {

    public final ShapeRenderer shapeRenderer;
    public final SpriteBatch spriteBatch;

    public Batches() {
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
        this.spriteBatch = new SpriteBatch();
    }
    
    public void setProjectionMatrix(Matrix4 matrix) {
        this.shapeRenderer.setProjectionMatrix(matrix);
        this.spriteBatch.setProjectionMatrix(matrix);
    }

    public void begin() {
        this.shapeRenderer.begin();
        this.spriteBatch.begin();
    }

    public void begin(ShapeRenderer.ShapeType shapeType) {
        this.shapeRenderer.begin(shapeType);
        this.spriteBatch.begin();
    }

    public void end() {
        this.shapeRenderer.end();
        this.spriteBatch.end();
    }

    public void dispose() {
        this.shapeRenderer.dispose();
        this.spriteBatch.dispose();
    }

}
