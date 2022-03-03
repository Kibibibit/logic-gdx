package au.com.thewindmills.kibi.appEngine.utils.maths;

public class RandomUtils {
    
    /**
     * Returns a random int between min and max, inclusive
     * @param min
     * @param max
     * @return
     */
    public static int randomIntInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
