package au.com.thewindmills.logicgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.AppStage;
import au.com.thewindmills.logicgdx.app.actors.ComponentActor;
import au.com.thewindmills.logicgdx.app.actors.IoParentActor;
import au.com.thewindmills.logicgdx.app.actors.LightComponentActor;
import au.com.thewindmills.logicgdx.app.actors.SwitchComponentActor;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.gui.ProgramUI;
import au.com.thewindmills.logicgdx.utils.AppConstants;

public class LogicGDX extends ApplicationAdapter {


    private AppStage stage;
    private Viewport viewport;
    private Camera camera;
    private ProgramUI programUI;
    private LogicAssetManager manager;

    private IoParentActor naming = null;


    @Override
    public void create() {

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        

        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        programUI = new ProgramUI();
        programUI.init(); 

        manager = new LogicAssetManager();
        manager.addImages();
        manager.loadImages();

        stage = new AppStage(viewport, manager, this);
        
        Gdx.input.setInputProcessor(stage);
    }

    public AppStage getStage() {
        return this.stage;
    }

    public boolean isNaming() {
        return naming != null;
    }

    public void name(IoParentActor actor) {
        naming = actor;
    }

    public void stopNaming(String name) {
        if (name != null) {
            naming.setIoName(name);
        }
        
        naming = null;
    }

    public void makeIc(String name) {
        this.stage.makeIc(name);
    }

    public void addActor(String name) {
        this.stage.getComponentActors().addActor(new ComponentActor(name, this.manager,this.stage).atX(AppConstants.APP_WIDTH/2).atY(AppConstants.APP_HEIGHT/2));
    }

    public void addSwitch() {
        this.stage.getComponentActors().addActor(new SwitchComponentActor(this.manager,this.stage).atX(AppConstants.APP_WIDTH/2).atY(AppConstants.APP_HEIGHT/2));
    }

    public void addLight() {
        this.stage.getComponentActors().addActor(new LightComponentActor(this.manager,this.stage).atX(AppConstants.APP_WIDTH/2).atY(AppConstants.APP_HEIGHT/2));
    }

    @Override
    public void resize(int width, int height){
       viewport.update(width,height);
       camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }

    @Override
    public void render() {
        camera.update();
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
        stage.draw();
        programUI.render(this);
    }

    @Override
    public void dispose() {
        stage.dispose();
        programUI.dispose();
        manager.dispose();
    }

    

}
