package au.com.thewindmills.logicgdx.app;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.app.actors.ComponentBodyActor;
import au.com.thewindmills.logicgdx.app.actors.ComponentIoActor;
import au.com.thewindmills.logicgdx.app.actors.WireActor;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.models.ConnectionMatrix;

public class AppStage extends Stage {

    LogicAssetManager manager;
    Actor touchedActor = null;
    WireActor drawingActor = null;
    boolean dragging = false;
    boolean drawing = false;
    Vector2 mouseOffset = new Vector2();

    Group componentActors;
    Group wireActors;

    ConnectionMatrix matrix;

    public AppStage(Viewport viewport, LogicAssetManager manager) {
        super(viewport);
        this.manager = manager;

        componentActors = new Group();
        wireActors = new Group();

        matrix = new ConnectionMatrix();

        this.addActor(wireActors);
        this.addActor(componentActors);

    }

    public ConnectionMatrix getMatrix() {
        return this.matrix;
    }

    public Group getComponentActors() {
        return componentActors;
    }

    public Group getWireActors() {
        return wireActors;
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
                if (actor instanceof ComponentIoActor) {
                    if (!drawing) {
                        drawing = true;
                        drawingActor = new WireActor(manager, (ComponentIoActor) actor);
                        this.wireActors.addActor(drawingActor);
                        System.out.println("Making a wire!");
                    } else {
                        drawing = false;
                        if (drawingActor.validEnd((ComponentIoActor) actor)) {
                            
                            drawingActor.stopDrawing((ComponentIoActor) actor);

                            if (drawingActor.getStart().isInput()) {
                                matrix.setMatrix(drawingActor.getStart().getIoId(), drawingActor.getEnd().getIoId(), true, true);
                            } else {
                                matrix.setMatrix(drawingActor.getEnd().getIoId(), drawingActor.getStart().getIoId(), true, true);
                            }

                            
                        } else {
                            drawingActor.remove();
                        }

                        drawingActor = null;

                    }
                    return true;
                }

            }

        } else if (button == Input.Buttons.MIDDLE && !drawing && !dragging) {
            Actor actor = this.hit(stageCoords.x, stageCoords.y, true);
            if (actor instanceof ComponentBodyActor) {
                ((ComponentBodyActor) actor).getParent().remove();
            }
            if (actor instanceof WireActor) {

                if (((WireActor) actor).getStart().isInput()) {
                    matrix.setMatrix(
                        ((WireActor) actor).getStart().getIoId(),
                        ((WireActor) actor).getEnd().getIoId(), false, 
                        true);
                } else {
                    matrix.setMatrix(
                        ((WireActor) actor).getEnd().getIoId(),
                        ((WireActor) actor).getStart().getIoId(), false, 
                        true);
                }

                actor.remove();
            }
        } else if (button == Input.Buttons.RIGHT) {
            if (drawing) {
                drawing = false;
                drawingActor.remove();
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
