package au.com.thewindmills.logicgdx.gui;

import au.com.thewindmills.logicgdx.LogicGDX;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class ToolbarUi extends AbstractUi {

    public static final int TOOLBAR_HEIGHT = 25;

    private boolean isOpen = false;

    private ImString icName;

    public void ui(LogicGDX logicGDX) {
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

        if (ImGui.button("Make IC")) {

            if (!isOpen) {
                isOpen = true;
                icName = new ImString();
            }
        }


        if (isOpen) {

            int popUpFlags = 0
                | ImGuiWindowFlags.NoDocking
                | ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoSavedSettings
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoCollapse;

            ImGui.begin("Make IC", popUpFlags);

            ImGui.text("Create a new IC based on the current screen!");

            ImGui.inputText("Name", icName);

            if (ImGui.button("Cancel")) {
                isOpen = false;
                icName = null;
            }

            ImGui.sameLine();
            if (ImGui.button("Make")) {
                logicGDX.makeIc(icName.get());
                icName = null;
                isOpen = false;

            }


            ImGui.end();
        }
        

        


        ImGui.sameLine();

        ImGui.end();

        
    }

}
