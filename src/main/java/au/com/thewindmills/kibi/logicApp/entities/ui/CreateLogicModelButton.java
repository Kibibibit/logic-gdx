package au.com.thewindmills.kibi.logicApp.entities.ui;

import com.badlogic.gdx.Gdx;
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

    public CreateLogicModelButton(Vector2 relativePos, Vector2 size, UiEntity parent, JSONObject object, String filename) {
        super(relativePos, size, parent, (String) object.get("name"), new ButtonPress() {

            @Override
            public void onPressed(int button) {}

            @Override
            public void onReleased(int button) {
                
                if (filename.equals(IoComponent.IO_FILENAME)) {
                    if (object.get(LogicModel.FIELD_NAME).equals(SwitchComponent.SWITCH_NAME)) {
                        new SwitchComponent(parent.getData(), Layers.MAIN, 0, new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2));
                    } else if (object.get(LogicModel.FIELD_NAME).equals(LightComponent.LIGHT_NAME)) {
                        new LightComponent(parent.getData(), Layers.MAIN, 0, new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2));
                    } else {
                        LOGGER.severe("Unknown IO component!");
                    }
                } else {
                    new ComponentBody(parent.getData(), Layers.MAIN, parent.getDepth(), new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), filename);
                }

            }
            
        });
    }

    
}
