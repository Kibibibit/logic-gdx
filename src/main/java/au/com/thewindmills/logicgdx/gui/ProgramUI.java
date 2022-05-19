package au.com.thewindmills.logicgdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import au.com.thewindmills.logicgdx.LogicGDX;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImString;

public class ProgramUI extends AbstractUi {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowHandle;
    private ToolbarUi toolbarUi;
    private SidebarUi sidebarUi;

    private ImString label;

    private boolean namingEvent = false;

    public ProgramUI() {
        toolbarUi = new ToolbarUi();
        sidebarUi = new SidebarUi();
    }

    @Override
    public void ui(LogicGDX logicGDX) {
        toolbarUi.ui(logicGDX);
        sidebarUi.ui(logicGDX);

        if (logicGDX.isNaming() != namingEvent) {
            namingEvent = logicGDX.isNaming();
            if (namingEvent) {
                logicGDX.getStage().setDialogOpen(true);
                label = new ImString();
            } else {
                logicGDX.getStage().setDialogOpen(false);
                label = null;
            }
        }

        if (logicGDX.isNaming()) {
            int popUpFlags = 0
                | ImGuiWindowFlags.NoDocking
                | ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoSavedSettings
                | ImGuiWindowFlags.NoResize
                | ImGuiWindowFlags.NoCollapse;
            ImGui.begin("Rename Element", popUpFlags);
            


                ImGui.text("Rename this element!");

            ImGui.inputText("Name", label);

            if (ImGui.button("Cancel")) {
                logicGDX.stopNaming(null);
            }

            ImGui.sameLine();
            if (ImGui.button("Rename")) {
                logicGDX.stopNaming(label.get());

            }

            ImGui.end();
        }


    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        ImGui.createContext();
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
        windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();

        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();
    }

    public void render(LogicGDX logicGDX) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        this.ui(logicGDX);

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        GLFW.glfwPollEvents();
    }

    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
