package au.com.thewindmills.kibi.appEngine.utils.maths;

import com.badlogic.gdx.math.Vector2;

public class AngleUtils {


    public static float lengthDirX(double angle, float r) {
        return ((float) Math.cos(angle))*r;
    }

    public static float lengthDirY(double angle, float r) {
        return ((float) Math.sin(angle))*r;
    }

    public static Vector2 lengthDir(double angle, float r) {
        return new Vector2(
            lengthDirX(angle, r),
            lengthDirY(angle, r)
        );
    }

    
}
