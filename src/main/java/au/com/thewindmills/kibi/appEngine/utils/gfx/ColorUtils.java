package au.com.thewindmills.kibi.appEngine.utils.gfx;

import com.badlogic.gdx.graphics.Color;

/**
 * Some helper methods for making colors
 * 
 * @author Kibi
 */
public class ColorUtils {
    
    /**
     * Get a color by rgba values
     * @param r
     * @param g
     * @param b
     * @param a - transparency
     * @return
     */
    public static Color color(float r, float g, float b, float a) {
        return new Color(r,g,b,a);
    }

    /**
     * Get a color by rgba values
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static Color color(float r, float g, float b) {
        return color(r,g,b,1f);
    }

    /**
     * Returns a shade of grey with the given shade
     * @param shade
     * @param a - transparency
     * @return
     */
    public static Color grey(float shade, float a) {
        return new Color(shade, shade, shade, a);
    }
    
    /**
     * Returns a shade of grey with the given shade
     * @param shade
     * @return
     */
    public static Color grey(float shade) {
        return grey(shade, 1f);
    }

    /**
     * Returns a shade of red with the given shade
     * @param shade
     * @param a - transparency
     * @return
     */
    public static Color red(float shade, float a) {
        return color(shade, 0f, 0f, a);
    }

    /**
     * Returns a shade of red with the given shade
     * @param shade
     * @return
     */
    public static Color red(float shade) {
        return red(shade, 1f);
    }

    /**
     * Returns a shade of blue with the given shade
     * @param shade
     * @param a - transparency
     * @return
     */
    public static Color blue(float shade, float a) {
        return color(0f, 0f, shade, a);
    }

    /**
     * Returns a shade of blue with the given shade
     * @param shade
     * @return
     */
    public static Color blue(float shade) {
        return blue(shade, 1f);
    }

    /**
     * Returns a shade of green with the given shade
     * @param shade
     * @param a - transparency
     * @return
     */
    public static Color green(float shade, float a) {
        return color(0f, shade, 0f, a);
    }

    /**
     * Returns a shade of green with the given shade
     * @param shade
     * @return
     */
    public static Color green(float shade) {
        return red(shade, 1f);
    }


}
