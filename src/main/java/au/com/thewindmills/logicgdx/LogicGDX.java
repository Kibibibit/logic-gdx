package au.com.thewindmills.logicgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.LogicAssetManager;
import au.com.thewindmills.logicgdx.gui.ProgramUI;

public class LogicGDX extends ApplicationAdapter {


    private AppStage stage;
    private Viewport viewport;
    private Camera camera;
    private ProgramUI programUI;
    private LogicAssetManager manager;


    @Override
    public void create() {

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        

        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        programUI = new ProgramUI();
        programUI.init(); 

        manager = new LogicAssetManager();

        manager.addImage("AND");
        manager.loadImages();

        stage = new AppStage(viewport, manager);
        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height){
       viewport.update(width,height);
       camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }

    @Override
    public void render() {
        camera.update();
        ScreenUtils.clear(0, 0, 0, 1);
        stage.draw();
        programUI.render();
    }

    @Override
    public void dispose() {
        stage.dispose();
        programUI.dispose();
        manager.dispose();
    }

}
