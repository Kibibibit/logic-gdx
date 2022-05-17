package au.com.thewindmills.logicgdx.app.assets;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import au.com.thewindmills.logicgdx.utils.AppConstants;

public class LogicAssetManager {

    public static final int TILE_SIZE = 16;
    public static final String SPRITE_SHEET = "sheet";

    public static final String spritePath(String name) {
        return String.format(AppConstants.TEXTURE_PATH,name);
    }

    private AssetManager manager;
    private Set<String> imagePaths;
    private TextureRegion[][] sheet;


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
        System.out.println("Images loaded!, creating spritesheet");

        sheet = TextureRegion.split(this.getImage(SPRITE_SHEET), TILE_SIZE, TILE_SIZE);

        System.out.println("Done!");
    }

    public boolean update() {
        return manager.update();
    }

    public Texture getImage(String spriteName) {
        return manager.get(spritePath(spriteName));
    }

    public TextureRegion getSprite(SheetSection section) {
        switch (section) {
            case L:
                return sheet[0][0];
            case R:
                return sheet[0][1];
            case T:
                return sheet[0][2];
            case B:
                return sheet[1][0];
            case MX:
                return sheet[1][1];
            case MO:
                return sheet[1][2];
            case IX:
                return sheet[2][0];
            case IO:
                return sheet[2][1];
            default:
                return sheet[2][2];
            
        }   
    }
 
    public void addImage(String spriteName) {
        imagePaths.add(spritePath(spriteName));
    }

    public void addImages() {
        this.addImage(SPRITE_SHEET);
    }



    public void dispose() {
        manager.dispose();
    }
    
}
