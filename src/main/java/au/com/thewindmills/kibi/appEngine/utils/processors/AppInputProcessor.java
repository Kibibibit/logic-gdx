package au.com.thewindmills.kibi.appEngine.utils.processors;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import au.com.thewindmills.kibi.appEngine.AppData;
/**
 * Implementation of {@link InputProcessor} to handle mouse and keyboard events.
 * 
 * @author Kibi
 */
public class AppInputProcessor implements InputProcessor {

    /**
     * A reference to the AppData so we can access our mouse object and other input objects
     */
    private final AppData data;

    /**
     * Stores the state of each mouse button the last time {@link #touchUp()} or {@link #touchDown()} was called
     * Primaryily used to keep track of which button was used to drag an object
     */
    private final Map<Integer, Boolean> currentButtonState;
    /**
     * Stores the state of each mouse button this time {@link #touchUp()} or {@link #touchDown()} was called
     */
    private final Map<Integer, Boolean> lastButtonState;

    public AppInputProcessor(AppData data) {
        this.data = data;

        this.currentButtonState = new HashMap<Integer, Boolean>();
        this.lastButtonState = new HashMap<Integer, Boolean>();
        
    }

    /**
     * Helper method to make sure the hashmaps store the buttons
     * @param button - Button to add
     */
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
        newButton(Input.Buttons.LEFT);
        newButton(Input.Buttons.MIDDLE);
        if (!this.currentButtonState.get(Input.Buttons.LEFT)) {

            if (!this.currentButtonState.get(Input.Buttons.MIDDLE)) {
                return this.mouseMoved(screenX, screenY);
            }
            if (this.data.getMouse() != null) {
                this.data.getMouse().pan(screenX, screenY);
                return false;
            }
        }

        if (data.getMouse() != null) {
            data.getMouse().mouseDragged(screenX, screenY);
        }


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

        if (data.getMouse() != null) {
            data.getMouse().mouseScrolled(amountX, amountY);
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

       if ((keycode >= Input.Keys.A && keycode <= Input.Keys.Z) || 
            keycode == Input.Keys.ENTER ||
            keycode == Input.Keys.SPACE ||
            keycode == Input.Keys.BACKSPACE
        ) {
           if (data.getMouse() != null) {
               // Mouse?
               data.getMouse().typeKey(keycode);
           }
       }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACKSPACE && data.getMouse() != null) {
            data.getMouse().delete();
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    
    
}
