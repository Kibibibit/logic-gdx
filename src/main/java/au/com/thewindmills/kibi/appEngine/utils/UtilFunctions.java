package au.com.thewindmills.kibi.appEngine.utils;

/**
 * A few general utility functions I couldn't work out where to put
 * 
 * @author Kibi
 */
public class UtilFunctions {
    
    /**
     * Returns true if element is in array, and is the correct type (if strict is set)
     * @param array - The array to search
     * @param element - The element to search for
     * @param strict - Should element be the same type as array
     * @return - True if element is found
     */
    public static boolean arrayContains(Object[] array, Object element, boolean strict) {
        for (Object o : array) {
            if (o.equals(element) && (array.getClass().getComponentType().equals(o.getClass()) && strict)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Defaults with a strict to true
     * @see {@link UtilFunctions#arrayContains(Object[], Object, boolean)}
     * @param array
     * @param element
     * @return
     */
    public static boolean arrayContains(Object[] array, Object element) {
        return arrayContains(array, element, true);
    }

}
