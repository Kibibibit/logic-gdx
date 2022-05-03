package au.com.thewindmills.logicgdx.gui;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.ImGuiWindowFlags;

public class ToolbarUi extends AbstractUi {

    public static final int TOOLBAR_HEIGHT = 25;

    public void ui() {
        ImGuiViewport viewport = ImGui.getMainViewport();

        ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY());
        ImGui.setNextWindowSize(viewport.getSizeX(), TOOLBAR_HEIGHT);
        ImGui.setNextWindowViewport(viewport.getID());

        int windowFlags = 0
                | ImGuiWindowFlags.NoDocking
                | ImGuiWindowFlags.NoTitleBar
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoSavedSettings
                | ImGuiWindowFlags.NoBringToFrontOnFocus;

        ImGui.pushStyleVar(imgui.flag.ImGuiStyleVar.WindowBorderSize, 0);

        ImGui.begin("Main Toolbar", windowFlags);
        ImGui.popStyleVar();

        ImGui.button("File");
        ImGui.sameLine();

        ImGui.button("Make IC");
        ImGui.sameLine();

        ImGui.end();

        
    }

}
