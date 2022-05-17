package au.com.thewindmills.logicgdx.app;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.actors.ComponentBodyActor;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;

public class AppStage extends Stage {

    LogicAssetManager manager;
    Actor touchedActor = null;
    boolean dragging = false;
    Vector2 mouseOffset = new Vector2();

    public AppStage(Viewport viewport, LogicAssetManager manager) {
        super(viewport);
        this.manager = manager;

    }

    private Vector2 getStageCoords(int screenX, int screenY) {
        return this.screenToStageCoordinates(new Vector2(screenX, screenY));
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 stageCoords = getStageCoords(screenX, screenY);
        if (button == Input.Buttons.LEFT) {

            
            Actor actor = this.hit(stageCoords.x, stageCoords.y, true);

            if (actor != null) {
                if (actor instanceof ComponentBodyActor) {
                    touchedActor = actor;
                    mouseOffset.set(actor.getParent().getX() - stageCoords.x, actor.getParent().getY() - stageCoords.y);
                    return true;
                }

            }

        } else if (button == Input.Buttons.MIDDLE) {
            Actor actor = this.hit(stageCoords.x, stageCoords.y, true);
            if (actor instanceof ComponentBodyActor) {
                ((ComponentBodyActor) actor).getParent().remove();
            }
        }

        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (touchedActor != null) {
            touchedActor = null;
            return true;
        }
        dragging = false;
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 stageCoords = getStageCoords(screenX, screenY);
        dragging = true;
        if (touchedActor != null) {
            if (touchedActor instanceof ComponentBodyActor) {
                ((ComponentBodyActor) touchedActor).drag(stageCoords.x + mouseOffset.x, stageCoords.y + mouseOffset.y);
            }

            return true;
        }

        return false;
    }

}
