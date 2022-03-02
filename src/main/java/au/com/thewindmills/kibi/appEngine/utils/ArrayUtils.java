package au.com.thewindmills.kibi.appEngine.utils;

/**
 * A few general utility functions I couldn't work out where to put
 * 
 * @author Kibi
 */
public class ArrayUtils {
    
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
     * @see {@link ArrayUtils#arrayContains(Object[], Object, boolean)}
     * @param array
     * @param element
     * @return
     */
    public static boolean arrayContains(Object[] array, Object element) {
        return arrayContains(array, element, true);
    }


    /**
     * Finds the index of the first occurence of element within array, or null if it is not in the list
     * @param array - array of objects to check over
     * @param element - the element to find
     * @param strict - If true, the type of element and array must match
     * @return - the index of the first occurence of element
     */
    public static Integer firstIndexOf(Object[] array, Object element, boolean strict) {
        int i = 0;
        for (Object o : array) {
            if (o.equals(element)  && (array.getClass().getComponentType().equals(o.getClass()) && strict)) {
                return i;
            }
            i++;
        }
        return null;
    }

    /**
     * Finds the index of the first occurence of element within array, or null if it is not in the list
     * @param array - array of objects to check over
     * @param element - the element to find
     * @return - the index of the first occurence of element
     */
    public static Integer firstIndexOf(Object[] array, Object element) {
        return firstIndexOf(array, element, true);
    }

}
