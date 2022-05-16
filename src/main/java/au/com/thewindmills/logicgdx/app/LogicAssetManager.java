package au.com.thewindmills.logicgdx.app;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class LogicAssetManager {

    private static final String SPRITE_PATH = "assets/sprites/%s.png";

    public static final String spritePath(String name) {
        return String.format(SPRITE_PATH,name);
    }

    private AssetManager manager;
    private Set<String> imagePaths;


    public LogicAssetManager() {
        imagePaths = new HashSet<>();
        manager = new AssetManager();
    }

    public boolean isFinished() {
        return manager.isFinished();
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public void loadImages() {
        for (String path : imagePaths) {
            manager.load(path, Texture.class);
        }

        float progress = -1.0f;
        
        while (!manager.update()) {
            if (manager.getProgress() != progress) {
                System.out.println(manager.getProgress());
                progress = manager.getProgress();
            }
            
        }
        System.out.println("Images loaded!");
    }

    public boolean update() {
        return manager.update();
    }

    public Texture getImage(String spriteName) {
        return manager.get(spritePath(spriteName));
    }
 
    public void addImage(String spriteName) {
        imagePaths.add(spritePath(spriteName));
    }

    public void dispose() {
        manager.dispose();
    }
    
}
