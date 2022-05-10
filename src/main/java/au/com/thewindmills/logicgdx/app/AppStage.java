package au.com.thewindmills.logicgdx.app;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.actors.IoActor;
import au.com.thewindmills.logicgdx.app.actors.LogicActor;

public class AppStage extends Stage {

    public AppStage(Viewport viewport) {
        super(viewport);
        

        Group actors = new Group();

        actors.addActor(new IoActor("data/sprites/AND.png"));
        actors.setZIndex(100);

        this.addActor(actors);

    }

    private void disposeActor(Actor actor) {
        if (actor instanceof Group) {
            for (Actor actor2 : ((Group) actor).getChildren()) {
                disposeActor(actor2);
            }
        } else if (actor instanceof LogicActor) {
            ((LogicActor) actor).dispose();
        }
    }

    @Override
    public void dispose() {

        for (Actor actor : this.getActors()) {
            disposeActor(actor);
        }

        super.dispose();
    }

}
