package au.com.thewindmills.kibi.appEngine.utils.constants;

import com.badlogic.gdx.graphics.Color;

import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiButton;
import au.com.thewindmills.kibi.appEngine.utils.gfx.ColorUtils;

/**
 * A few constants and color defaults for drawing objects
 * 
 * @author Kibi
 */
public class DrawConstants {
    /**
     * The default stroke color of an {@link AbstractShape}
     */
    public static final Color STROKE_COLOR = Colors.BLACK;

    /**
     * The default fill color of an {@link AbstractShape}
     */
    public static final Color FILL_COLOR = Colors.WHITE;

    /**
     * The default hover color of {@link UiButton}
     */
    public static final Color HOVER_COLOR = ColorUtils.grey(0.4f);

    /**
     * The default radius of a circle for a node
     */
    public static final float NODE_RADIUS = 9;

    /**
     * The average default stroke width of a shape
     */
    public static final float STROKE_WIDTH = 2;
}
