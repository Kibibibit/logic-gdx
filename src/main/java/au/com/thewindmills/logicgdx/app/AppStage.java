package au.com.thewindmills.logicgdx.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import au.com.thewindmills.logicgdx.LogicGDX;
import au.com.thewindmills.logicgdx.app.actors.ComponentActor;
import au.com.thewindmills.logicgdx.app.actors.ComponentBodyActor;
import au.com.thewindmills.logicgdx.app.actors.ComponentIoActor;
import au.com.thewindmills.logicgdx.app.actors.IoParentActor;
import au.com.thewindmills.logicgdx.app.actors.LightBodyActor;
import au.com.thewindmills.logicgdx.app.actors.LightComponentActor;
import au.com.thewindmills.logicgdx.app.actors.SwitchBodyActor;
import au.com.thewindmills.logicgdx.app.actors.SwitchButtonActor;
import au.com.thewindmills.logicgdx.app.actors.SwitchComponentActor;
import au.com.thewindmills.logicgdx.app.actors.WireActor;
import au.com.thewindmills.logicgdx.app.assets.LogicAssetManager;
import au.com.thewindmills.logicgdx.models.ChipComponent;
import au.com.thewindmills.logicgdx.models.ConnectionMatrix;
import au.com.thewindmills.logicgdx.models.UpdateResponse;

public class AppStage extends Stage {

    LogicAssetManager manager;
    Actor touchedActor = null;
    WireActor drawingActor = null;
    boolean dragging = false;
    boolean drawing = false;
    Vector2 mouseOffset = new Vector2();
    LogicGDX logicGDX;

    Group componentActors;
    Group wireActors;

    ConnectionMatrix matrix;

    public AppStage(Viewport viewport, LogicAssetManager manager, LogicGDX logicGDX) {
        super(viewport);
        this.manager = manager;

        componentActors = new Group();
        wireActors = new Group();

        matrix = new ConnectionMatrix();
        this.logicGDX = logicGDX;

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
                if (actor instanceof ComponentBodyActor && !drawing) {
                    touchedActor = actor;
                    mouseOffset.set(actor.getParent().getX() - stageCoords.x, actor.getParent().getY() - stageCoords.y);
                    return true;
                }
                if (actor instanceof SwitchButtonActor && !drawing) {
                    touchedActor = actor;
                    mouseOffset.set(actor.getParent().getX() - stageCoords.x, actor.getParent().getY() - stageCoords.y);
                    return true;
                }
                if (actor instanceof ComponentIoActor) {
                    if (!drawing) {
                        drawing = true;
                        drawingActor = new WireActor(manager, (ComponentIoActor) actor);
                        this.wireActors.addActor(drawingActor);
                    } else {
                        drawing = false;
                        if (drawingActor.validEnd((ComponentIoActor) actor)) {

                            drawingActor.stopDrawing((ComponentIoActor) actor);
                            String mapping;
                            if (drawingActor.getStart().isInput()) {

                                if (drawingActor.getStart().getWire() != null) {
                                    removeWire(drawingActor.getStart().getWire());
                                    drawingActor.getStart().setWire(null);
                                }
                                drawingActor.getStart().setWire(drawingActor);
                                mapping = matrix.setMatrix(drawingActor.getStart().getIoId(),
                                        drawingActor.getEnd().getIoId(), true, true);
                            } else {

                                if (drawingActor.getEnd().getWire() != null) {
                                    removeWire(drawingActor.getEnd().getWire());
                                    drawingActor.getEnd().setWire(null);
                                }

                                drawingActor.getEnd().setWire(drawingActor);
                                mapping = matrix.setMatrix(drawingActor.getEnd().getIoId(),
                                        drawingActor.getStart().getIoId(), true, true);
                            }
                            drawingActor.setMapping(mapping);

                        } else {
                            drawingActor.remove();
                        }

                        drawingActor = null;

                    }
                    return true;
                }

                return false;

            }

        } else if (button == Input.Buttons.MIDDLE && !drawing && !dragging) {
            Actor actor = this.hit(stageCoords.x, stageCoords.y, true);
            if (actor instanceof ComponentBodyActor || actor instanceof SwitchButtonActor) {
                ComponentActor componentActor;
                if (actor instanceof ComponentBodyActor) {
                    componentActor = (ComponentActor) ((ComponentBodyActor) actor).getParent();
                } else {
                    componentActor = (ComponentActor) ((SwitchButtonActor) actor).getParent();
                }

                List<WireActor> toRemove = new ArrayList<>();
                for (Actor wire : wireActors.getChildren()) {
                    WireActor wireActor = (WireActor) wire;
                    if (wireActor.getStart().getParentActor().getComponent().getId() == componentActor.getComponent()
                            .getId() ||
                            wireActor.getEnd().getParentActor().getComponent().getId() == componentActor.getComponent()
                                    .getId()) {
                        toRemove.add(wireActor);
                    }
                }

                for (WireActor wire : toRemove) {
                    removeWire(wire);
                }

                componentActor.remove();
            }
            if (actor instanceof WireActor) {
                removeWire((WireActor) actor);

            }
        } else if (button == Input.Buttons.RIGHT) {
            if (drawing) {
                drawing = false;
                drawingActor.remove();
            } else {
                Actor actor = this.hit(stageCoords.x, stageCoords.y, true);

                if (actor instanceof LightBodyActor || actor instanceof SwitchButtonActor
                        || actor instanceof SwitchBodyActor) {
                    if (!logicGDX.isNaming()) {
                        logicGDX.name((IoParentActor) actor.getParent());
                    }

                }
            }
        }

        return false;

    }

    public void update(UpdateResponse updateResponse) {
        if (updateResponse.updated) {

            for (long id : updateResponse.result.keySet()) {
                matrix.update(id, updateResponse.result.get(id));
            }

        }
    }

    public void removeWire(WireActor actor) {
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (touchedActor != null) {

            if (button == Input.Buttons.LEFT) {
                if (touchedActor instanceof SwitchButtonActor && !dragging) {
                    ((SwitchButtonActor) touchedActor).toggle();
                }

                touchedActor = null;
                dragging = false;
                return true;
            }

        }
        dragging = false;
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 stageCoords = getStageCoords(screenX, screenY);

        if (touchedActor != null) {
            dragging = true;
            if (touchedActor instanceof ComponentBodyActor) {
                ((ComponentBodyActor) touchedActor).drag(stageCoords.x + mouseOffset.x, stageCoords.y + mouseOffset.y);
            }

            if (touchedActor instanceof SwitchButtonActor) {
                ((SwitchButtonActor) touchedActor).drag(stageCoords.x + mouseOffset.x, stageCoords.y + mouseOffset.y);
            }

            return true;
        }

        return false;
    }

    public void makeIc(String name) {

        ChipComponent ic = new ChipComponent(name);

        List<SwitchComponentActor> switches = new ArrayList<>();
        List<LightComponentActor> lights = new ArrayList<>();
        List<ComponentActor> children = new ArrayList<>();

        Set<String> inNames = new HashSet<>();
        Set<String> outNames = new HashSet<>();

        for (Actor actor : wireActors.getChildren()) {
            WireActor wire = (WireActor) actor;

            ComponentActor start = wire.getStart().getParentActor();
            ComponentActor end = wire.getEnd().getParentActor();

            if (start instanceof SwitchComponentActor) {
                if (!switches.contains((SwitchComponentActor) start)) {
                    if (inNames.add(((SwitchComponentActor) start).getIoName())) {
                        switches.add((SwitchComponentActor) start);
                    } else {
                        System.err.println("DUPLICATE INPUT");
                        return;
                    }
                }
            } else if (start instanceof LightComponentActor) {
                if (!lights.contains((LightComponentActor) start)) {
                    if (outNames.add(((LightComponentActor) start).getIoName())) {
                        lights.add((LightComponentActor) start);
                    } else {
                        System.err.println("DUPLICATE OUTPUT");
                        return;
                    }
                }
            } else {
                if (!children.contains((ComponentActor) start)) {
                    children.add(start);
                }
            }

            if (end instanceof SwitchComponentActor) {
                if (!switches.contains((SwitchComponentActor) end)) {
                    if (inNames.add(((SwitchComponentActor) end).getIoName())) {
                        switches.add((SwitchComponentActor) end);
                    } else {
                        System.err.println("DUPLICATE INPUT");
                        return;
                    }
                }
            } else if (end instanceof LightComponentActor) {
                if (!lights.contains((LightComponentActor) end)) {
                    if (outNames.add(((LightComponentActor) end).getIoName())) {
                        lights.add((LightComponentActor) end);
                    } else {
                        System.err.println("DUPLICATE OUTPUT");
                        return;
                    }
                }
            } else {
                if (!children.contains(end)) {
                    children.add(end);
                }
            }

            
        }

        switches.sort(new Comparator<SwitchComponentActor>() {
            public int compare(SwitchComponentActor a, SwitchComponentActor b) {
                return Math.round(a.getY() - b.getY());
            }
        });

        lights.sort(new Comparator<LightComponentActor>() {
            public int compare(LightComponentActor a, LightComponentActor b) {
                return Math.round(a.getY() - b.getY());
            }
        });

        for (SwitchComponentActor actor : switches) {
            ic.addInput(actor.getIoName());
        }

        for (LightComponentActor actor : lights) {
            ic.addOutput(actor.getIoName());
        }

        for (ComponentActor actor : children) {
            ic.addChild(actor.getComponent());
        }


        for (Actor actor : wireActors.getChildren()) {
            WireActor wire = (WireActor) actor;

            ComponentActor start = wire.getStart().getParentActor();
            ComponentActor end = wire.getEnd().getParentActor();


            if (start instanceof IoParentActor || end instanceof IoParentActor) {

                if (start instanceof IoParentActor && end instanceof IoParentActor) {

                    IoParentActor startIo = (IoParentActor) start;
                    IoParentActor endIo = (IoParentActor) end;

                    if (wire.startIsInput()) {
                        ic.setExternalMapping(ic.getIoId(endIo.getIoName()), ic.getIoId(startIo.getIoName()), true);
                    } else {
                        ic.setExternalMapping(ic.getIoId(startIo.getIoName()), ic.getIoId(endIo.getIoName()), true);
                    }
                } else if (start instanceof IoParentActor) {
                    IoParentActor startIo = (IoParentActor) start;
                    
                    if (wire.startIsInput()) {
                        ic.setExternalMappingOut(wire.getEnd().getIoId(), ic.getIoId(startIo.getIoName()), true);
                    } else {
                        ic.setExternalMappingIn(wire.getEnd().getIoId(), ic.getIoId(startIo.getIoName()), true);
                    }
                } else if (end instanceof IoParentActor) {
                    IoParentActor endIo = (IoParentActor) end;
                    
                    if (!wire.startIsInput()) {
                        ic.setExternalMappingOut(wire.getStart().getIoId(), ic.getIoId(endIo.getIoName()), true);
                    } else {
                        ic.setExternalMappingIn(wire.getStart().getIoId(), ic.getIoId(endIo.getIoName()), true);
                    }
                }

            } else {

                if (wire.startIsInput()) {
                    ic.setInternalMapping(wire.getStart().getIoId(), wire.getEnd().getIoId(), true);
                } else {
                    ic.setInternalMapping(wire.getEnd().getIoId(), wire.getStart().getIoId(), true);
                }
                

            }

        }


        try {
            ic.saveJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
