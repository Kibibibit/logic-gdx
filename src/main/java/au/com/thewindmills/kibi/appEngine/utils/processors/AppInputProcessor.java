package au.com.thewindmills.kibi.appEngine.utils.processors;

import com.badlogic.gdx.InputProcessor;

import au.com.thewindmills.kibi.appEngine.AppData;

public class AppInputProcessor implements InputProcessor {


    private final AppData data;

    public AppInputProcessor(AppData data) {
        this.data = data;
    }


    //TODO: Set up someway of firing pressed and released events, track each button, maybed a hashmap?
    //Have a last event version of the hashmap too
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
