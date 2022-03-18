package au.com.thewindmills.kibi.logicApp.entities.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiRectButton;

public class CreateIcModelButton extends UiRectButton {

    public CreateIcModelButton( UiEntity parent) {
        super(new Vector2(parent.getSize().x - 60, 0), new Vector2(60,parent.getSize().y), parent, "Make IC");
    }
    
    @Override
    public void doOnMouseReleased(int button) {

        if (button == Input.Buttons.LEFT) {
            this.getData().getConnectionMap().createIc();
        }

    }

}
