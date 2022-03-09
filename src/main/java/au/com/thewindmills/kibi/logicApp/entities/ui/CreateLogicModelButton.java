package au.com.thewindmills.kibi.logicApp.entities.ui;

import com.badlogic.gdx.math.Vector2;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiRectButton;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
import au.com.thewindmills.kibi.logicApp.entities.ComponentBody;
import au.com.thewindmills.kibi.logicApp.entities.io.IoComponent;
import au.com.thewindmills.kibi.logicApp.entities.io.LightComponent;
import au.com.thewindmills.kibi.logicApp.entities.io.SwitchComponent;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

public class CreateLogicModelButton extends UiRectButton{

    private String filename;
    private JSONObject object;

    public CreateLogicModelButton(Vector2 relativePos, Vector2 size, UiEntity parent, JSONObject object, String filename) {
        super(relativePos, size, parent, (String) object.get("name"));
        this.filename = filename;
        this.object = object;
        this.isDraggable = true;
      
    }

    private ComponentBody createEntity(float x, float y) {
        if (filename.equals(IoComponent.IO_FILENAME)) {
            if (object.get(LogicModel.FIELD_NAME).equals(SwitchComponent.SWITCH_NAME)) {
                return new SwitchComponent(getParent().getData(), Layers.MAIN, 0, new Vector2(x, y));
            } else if (object.get(LogicModel.FIELD_NAME).equals(LightComponent.LIGHT_NAME)) {
                return new LightComponent(getParent().getData(), Layers.MAIN, 0, new Vector2(x, y));
            } else {
                LOGGER.severe("Unknown IO component!");
                return null;
            }
        } else {
            return new ComponentBody(getParent().getData(), Layers.MAIN, getParent().getDepth(), new Vector2(x, y), filename);
        }
    }

    @Override
    public void doOnMousePressed(int button) {;
    }

    @Override
    public void doOnMouseReleased(int button) {
        createEntity(
            this.getData().getCamera().position.x,
            this.getData().getCamera().position.y
        );
    }

    @Override
    public void onMouseDragged() {
        ComponentBody newBody = createEntity(
            this.getData().getMouse().getCameraPos().x,
            this.getData().getMouse().getCameraPos().y
        );
        this.onMouseLeave();
        this.setBeingDragged(false);
        newBody.setBeingDragged(true);

        this.getData().getMouse().drag(newBody);

    }


    
}
