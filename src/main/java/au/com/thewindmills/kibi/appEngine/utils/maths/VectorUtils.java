package au.com.thewindmills.kibi.appEngine.utils.maths;

import com.badlogic.gdx.math.Vector2;

public class VectorUtils {

    public static float triArea(Vector2 a, Vector2 b, Vector2 c) {
        //Bx * Ay - Ax * By) + (Cx * By - Bx * Cy) + (Ax * Cy - Cx * Ay)
        return (float) Math.abs(
            ((b.x * a.y) - (a.x * b.y)) +
            ((c.x * b.y) - (b.x * c.y)) +
            ((a.x * c.y) - (c.x * a.y))
        )/2;

    }
    
}
