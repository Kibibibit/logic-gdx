package au.com.thewindmills.kibi.appEngine.utils.gfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;

/**
 * Stores all the renderers needed, so we can avoid having to pass multiple in
 * to every render call.
 * 
 * @author Kibi
 */
public class Batches {

    /**
     * A shape renderer for drawing basic shapes. Used for drawing
     * {@link AbstractShape}s
     */
    public final ShapeRenderer shapeRenderer;

    /**
     * A renderer for drawing objects with set sprites.
     */
    public final SpriteBatch spriteBatch;

    /**
     * Default (and only!) constructor
     */
    public Batches() {
        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
        this.spriteBatch = new SpriteBatch();
    }

    /**
     * Calls setProjectionMatrix on all renderers
     * 
     * @param matrix
     */
    public void setProjectionMatrix(Matrix4 matrix) {
        this.shapeRenderer.setProjectionMatrix(matrix);
        this.spriteBatch.setProjectionMatrix(matrix);
    }

    public void begin() {
        this.shapeRenderer.begin();
        this.spriteBatch.begin();
    }

    public void begin(ShapeRenderer.ShapeType shapeType) {
        this.spriteBatch.begin();
        this.shapeRenderer.begin(shapeType);
    }

    public void end() {
        this.spriteBatch.end();
        this.shapeRenderer.end();
    }

    public void beginText() {
        this.spriteBatch.begin();
    }

    public void endText() {
        this.spriteBatch.end();
    }

    public void dispose() {
        this.shapeRenderer.dispose();
        this.spriteBatch.dispose();
    }

}
