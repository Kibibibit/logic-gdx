package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiPanel;
import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;

public class UiListView extends UiPanel {

    private static final float SCROLL_SPEED = 2.5f;

    private static final float SPACING = 2f;

    private final float height;

    private float currentY = 0;

    private float nextChild = 0;

    public UiListView(AppData data, String layer, int depth, float x, float y, float width, float height) {
        super(data, layer, depth, x, y, width, height);
        this.height = height;
    }


    @Override
    public void mouseScrolled(float amountX, float amountY) {

        float change = amountY * SCROLL_SPEED;


        currentY += change;

        if (currentY < 0) {
            change = 0;
            currentY = 0;
            
        }

        if (currentY > nextChild-height) {
            change = 0;
            currentY = nextChild-height;
        }

        for (UiEntity child : this.children) {
            child.setRelativePos(child.getRelativePos().x, child.getRelativePos().y + change);
        }

    }


    @Override
    public void addChild(UiEntity child) {

        child.setRelativePos(DrawConstants.STROKE_WIDTH, height-nextChild-DrawConstants.STROKE_WIDTH-child.getShape().getHeight());

        nextChild += child.getShape().getHeight() + SPACING;

        super.addChild(child);
    }

    
}
