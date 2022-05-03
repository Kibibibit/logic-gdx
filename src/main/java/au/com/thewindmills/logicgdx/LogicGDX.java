package au.com.thewindmills.logicgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.ScreenUtils;

import au.com.thewindmills.logicgdx.gui.ProgramUI;

public class LogicGDX extends ApplicationAdapter {
    
    private ProgramUI programUI;

    @Override
    public void create() {
        programUI = new ProgramUI();
        programUI.init(); 
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        programUI.render();
    }

    @Override
    public void dispose() {
        programUI.dispose();
    }

}
