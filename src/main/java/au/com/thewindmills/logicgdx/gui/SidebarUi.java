package au.com.thewindmills.logicgdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import au.com.thewindmills.logicgdx.LogicGDX;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.ImGuiWindowFlags;

public class SidebarUi extends AbstractUi {

    @Override
    public void ui(LogicGDX logicGDX) {
        ImGuiViewport viewport = ImGui.getMainViewport();

        ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY() + ToolbarUi.TOOLBAR_HEIGHT);
        ImGui.setNextWindowSize(100, viewport.getSizeY() - ToolbarUi.TOOLBAR_HEIGHT);
        ImGui.setNextWindowViewport(viewport.getID());

        int windowFlags = 0
                | ImGuiWindowFlags.NoDocking
                | ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoSavedSettings
                | ImGuiWindowFlags.NoBringToFrontOnFocus;

        ImGui.pushStyleVar(imgui.flag.ImGuiStyleVar.WindowBorderSize, 0);
        ImGui.begin("Sidebar", windowFlags);
        ImGui.popStyleVar();

        // Place buttons here
        if (ImGui.button("SWITCH")) {
            logicGDX.addSwitch();
        }

        if (Gdx.files.internal("assets/data").isDirectory()) {
            for (FileHandle file : Gdx.files.internal("assets/data").list(".json")) {
                
                if (ImGui.button(file.nameWithoutExtension())) {
                    logicGDX.addActor(file.nameWithoutExtension());
                }

            }
        }

        ImGui.end();

    }

}
