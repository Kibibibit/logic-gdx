package au.com.thewindmills.logicgdx.app;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.actors.IoActor;

public class AppStage extends Stage {


    LogicAssetManager manager;

    public AppStage(Viewport viewport, LogicAssetManager manager) {
        super(viewport);
        this.manager = manager;
        

        Group actors = new Group();

        actors.addActor(new IoActor("AND",manager));
        actors.setZIndex(100);

        this.addActor(actors);

    }


}
