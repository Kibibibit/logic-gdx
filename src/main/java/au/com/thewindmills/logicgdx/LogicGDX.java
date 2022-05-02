package au.com.thewindmills.logicgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.utils.ScreenUtils;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class LogicGDX extends ApplicationAdapter {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowHandle;

    @Override
    public void create() {
        
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        ImGui.createContext();
        windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();

        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        ImGui.button("I'm a Button!");
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        GLFW.glfwPollEvents();
        
    }

    @Override
    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();

    }

}
