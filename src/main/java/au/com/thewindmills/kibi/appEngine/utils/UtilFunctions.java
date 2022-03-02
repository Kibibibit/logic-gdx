package au.com.thewindmills.kibi.appEngine.utils;

/**
 * A few general utility functions I couldn't work out where to put
 * 
 * @author Kibi
 */
public class UtilFunctions {
    
    /**
     * Returns true if element is in array, and is the correct type
     * @param array - The array to search
     * @param element - The element to search for
     * @return - True if element is found
     */
    public static boolean arrayContains(Object[] array, Object element) {
        for (Object o : array) {
            if (o.equals(element) && array.getClass().getComponentType().equals(o.getClass())) {
                return true;
            }
        }
        return false;
    }

}
