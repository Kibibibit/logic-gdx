package au.com.thewindmills.logicgdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ProgramUI extends AbstractUi {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowHandle;
    private ToolbarUi toolbarUi;
    private SidebarUi sidebarUi;

    public ProgramUI() {
        toolbarUi = new ToolbarUi();
        sidebarUi = new SidebarUi();
    }

    @Override
    public void ui() {
        toolbarUi.ui();
        sidebarUi.ui();
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

    public void render() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        this.ui();

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
