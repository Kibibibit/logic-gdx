package au.com.thewindmills.kibi.appEngine.utils.constants;

/**
 * Stores static references of layer names
 * 
 * @author Kibi
 */
public class Layers {
    /**
     * Entities rendered here will be in the background
     */
    public static final String BACKGROUND = "LAYER_BACKGROUND";

    /**
     * Entities rendered here will be below the main layer, for things like shadows
     */
    public static final String BELOW_MAIN = "LAYER_BELOW_MAIN";

    /**
     * The main layer, the majority of entities should be rendered here
     */
    public static final String MAIN = "LAYER_MAIN";

    /**
     * Entities rendered here will be above the main layer, for things like particle
     * effects or halos
     */
    public static final String ABOVE_MAIN = "LAYER_ABOVE_MAIN";

    /**
     * Entities rendered here will be below the main ui layer, and should be drawn
     * relative to the frame
     */
    public static final String BELOW_UI = "LAYER_BELOW_UI";

    /**
     * This is the ui layer, entities drawn here will be drawn relative to the
     * frame, and not the camera
     */
    public static final String UI = "LAYER_UI";

    /**
     * Entities drawn here will be static like the main ui layer, but drawn above, for things such as dropdowns
     */
    public static final String ABOVE_UI = "LAYER_ABOVE_UI";

    /**
     * Ordered list of layers. Entities from each layer should be rendered in this order
     */
    public static final String[] LAYERS = new String[] { Layers.BACKGROUND, Layers.BELOW_MAIN, Layers.MAIN,
            Layers.ABOVE_MAIN, Layers.BELOW_UI, Layers.UI, Layers.ABOVE_UI };

    /**
     * These layers should not have their renderers projected based on the camera
     */
    public static final String[] STATIC_LAYERS = new String[] { Layers.BELOW_UI, Layers.UI, Layers.ABOVE_UI };
}
