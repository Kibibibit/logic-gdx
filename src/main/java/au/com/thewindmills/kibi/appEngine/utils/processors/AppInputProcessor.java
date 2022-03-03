package au.com.thewindmills.kibi.appEngine.utils.processors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.InputProcessor;

import au.com.thewindmills.kibi.appEngine.AppData;

public class AppInputProcessor implements InputProcessor {


    private final AppData data;

    private final Map<Integer, Boolean> currentButtonState;
    private final Map<Integer, Boolean> lastButtonState;

    public AppInputProcessor(AppData data) {
        this.data = data;

        this.currentButtonState = new HashMap<Integer, Boolean>();
        this.lastButtonState = new HashMap<Integer, Boolean>();
        
    }

    private void newButton(int button) {
        if (!lastButtonState.containsKey(button)) {
            lastButtonState.put(button, false);
        }

        if (!currentButtonState.containsKey(button)) {
            currentButtonState.put(button, false);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        newButton(button);
        
        lastButtonState.replace(button, currentButtonState.get(button));
        currentButtonState.replace(button, true);

        if (lastButtonState.get(button) == false && currentButtonState.get(button) == true) {
            if (data.getMouse() != null) {
                data.getMouse().buttonPressed(button);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        newButton(button);
        
        lastButtonState.replace(button, currentButtonState.get(button));
        currentButtonState.replace(button, false);

        if (lastButtonState.get(button) == true && currentButtonState.get(button) == false) {
            if (data.getMouse() != null) {
                data.getMouse().buttonReleased(button);
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (data.getMouse() != null) {
            data.getMouse().mouseMoved(screenX, screenY);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    
    
}
