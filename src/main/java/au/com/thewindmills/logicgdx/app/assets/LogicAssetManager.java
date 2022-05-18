package au.com.thewindmills.logicgdx.app.assets;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import au.com.thewindmills.logicgdx.utils.AppConstants;

public class LogicAssetManager {

    public static final int TILE_SIZE = 16;
    public static final int WIRE_TILE_SIZE = 8;
    public static final String BODY_SHEET = "component-sheet";
    public static final String FONT_SHEET = "font-sheet";
    public static final String WIRE_SHEET = "wire-sheet";
    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 12;

    private static final String CHAR_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890'!<> ";

    public static final String spritePath(String name) {
        return String.format(AppConstants.TEXTURE_PATH,name);
    }

    private AssetManager manager;
    private Set<String> imagePaths;
    private TextureRegion[][] bodySheet;
    private TextureRegion[][] font;
    private TextureRegion[] wireSheet;

    private int fontRows;
    private int fontCols;

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

        bodySheet = TextureRegion.split(this.getImage(BODY_SHEET), TILE_SIZE, TILE_SIZE);
        font = TextureRegion.split(this.getImage(FONT_SHEET), FONT_WIDTH, FONT_HEIGHT);
        wireSheet = TextureRegion.split(this.getImage(WIRE_SHEET), WIRE_TILE_SIZE, WIRE_TILE_SIZE)[0];

        fontRows = font.length;
        fontCols = font[0].length;
        System.out.println("Done!");
    }

    public boolean update() {
        return manager.update();
    }

    public Texture getImage(String spriteName) {
        return manager.get(spritePath(spriteName));
    }

    public void drawText(Batch batch, String text, float x, float y) {
        for (int i = 0; i < text.length(); i++) {
            batch.draw(getChar(text.substring(i, i+1)), x + (FONT_WIDTH*i), y);
        }
    }

    public int getTextWidth(String text) {
        return FONT_WIDTH*text.length();
    }

    public void drawTextCentered(Batch batch, String text, float x, float y) {
        x -= Math.round(getTextWidth(text)*0.5f);
        y -= Math.round(FONT_HEIGHT*0.5f);
        drawText(batch, text, x, y);
    }

    public TextureRegion getChar(String c) {
        if (c.length() != 1) {
            throw new IllegalArgumentException("Invalid string '" + c + "' for getChar");
        }

        if (CHAR_MAP.contains(c)) {

            int i = CHAR_MAP.indexOf(c);
            int x = i;
            int y = 0;
            while (x >= fontCols) {
                x -= fontCols;
                y++;
            }
            return font[y][x];

        } else {
            return font[fontRows-1][fontCols-1];
        }


    }

    public TextureRegion getSprite(SheetSection section) {
        switch (section) {
            case L:
                return bodySheet[0][0];
            case R:
                return bodySheet[0][1];
            case T:
                return bodySheet[0][2];
            case B:
                return bodySheet[1][0];
            case MX:
                return bodySheet[1][1];
            case MO:
                return bodySheet[1][2];
            case IX:
                return bodySheet[2][0];
            case IO:
                return bodySheet[2][1];
            case EW:
                return wireSheet[0];
            case NS:
                return wireSheet[1];
            case WN:
                return wireSheet[2];
            case WX:
                return wireSheet[3];
            case WO:
                return wireSheet[4];
            default:
                return bodySheet[2][2];
            
        }   
    }
 
    public void addImage(String spriteName) {
        imagePaths.add(spritePath(spriteName));
    }

    public void addImages() {
        this.addImage(BODY_SHEET);
        this.addImage(FONT_SHEET);
        this.addImage(WIRE_SHEET);
    }



    public void dispose() {
        manager.dispose();
    }
    
}
