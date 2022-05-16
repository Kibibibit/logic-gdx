package au.com.thewindmills.logicgdx.app;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.actors.IoActor;

public class AppStage extends Stage {

    LogicAssetManager manager;
    Actor touchedActor = null;
    Vector2 mouseOffset = new Vector2();

    public AppStage(Viewport viewport, LogicAssetManager manager) {
        super(viewport);
        this.manager = manager;

        Group actors = new Group();

        actors.addActor(new IoActor("AND", manager));
        actors.setZIndex(100);
        this.addActor(actors);

    }

    private Vector2 getStageCoords(int screenX, int screenY) {
        return this.screenToStageCoordinates(new Vector2(screenX, screenY));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (button != Input.Buttons.LEFT) {
            return false;
        }
        Vector2 stageCoords = getStageCoords(screenX, screenY);
        Actor actor = this.hit(stageCoords.x, stageCoords.y, true);

        if (actor != null) {
            touchedActor = actor;
            mouseOffset.set(actor.getX() - stageCoords.x, actor.getY() - stageCoords.y);
            return true;
        }

        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (touchedActor != null) {
            touchedActor = null;
            return true;
        }
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 stageCoords = getStageCoords(screenX, screenY);

        if (touchedActor != null) {
            touchedActor.setPosition(stageCoords.x + mouseOffset.x, stageCoords.y + mouseOffset.y);
            return true;
        }

        return false;
    }

}
