package au.com.thewindmills.kibi.appEngine.utils.maths;

/**
 * This class contains functions related to handling binary numbers,
 * including gray code, or converting binary strings into ints.
 * <br><br>
 * All methods are unsigned
 * 
 * @author Kibi
 */
public class BinaryUtils {

    /**
     * Takes in a String of 1's and 0's, and returns the integer of that number
     * @param s - The string to parse
     * @return - An unsigned integer representation of the string
     */
    public static int parse(String s) {
        return Integer.parseInt(s.strip().replaceAll(" ", ""),2);
    }

    /**
     * Takes in an integer, and returns the amount of bits needed to represent that number in binary
     * @param s - Number to check
     * @return - The amount of bits needed to store that number
     */
    public static int findBits(int s) {

        int bits = 0;

        while (Math.pow(2,bits) < s) {
            bits++;
        }
        
        return bits;

    }

    /**
     * Converts the given int into its' gray code version.
     * <br><br>
     * 0, 1, 2, 3 (00, 01, 10, 11) becomes
     * <br><br>
     * 0, 1, 3, 2 (00, 01, 11, 10)
     * <br><br>
     * This means only one digit changes between each number
     * @param s - number to convert to gray code
     * @return - the result
     */
    public static int grayCode(int s) {
        return s ^ (s >> 1);
    }

    /**
     * Takes a gray coded number and returns in its' original form.
     * 
     * @see {@link BinaryUtils#grayCode(int)}
     * 
     * @param s
     * @return
     */
    public static int inverseGrayCode(int s) {
        int inv = 0;

        for (; s != 0; s = s >> 1) {
            inv ^= s;
        }

        return inv;
    }
    
    /**
     * Returns either 0 or 2 to the power of pos, if bit pos is set in value
     * @param value - The value to check
     * @param pos - the bit to check
     * @return
     */
    public static int getValueAtBit(int value, int pos) {
        return ((value >> pos) % 2) << pos;
    }

    /**
     * Returns true if the bit at pos in value is set to 1
     * @param value - The value to check
     * @param pos - the bit to check
     * @return
     */
    public static boolean bitActive(int value, int pos) {
        return (value >> pos) % 2 == 1;
    }

    /**
     * Takes in a boolean array representing a binary number where
     * true -> 1 and false -> 0, and returns it as an integer
     * @param array
     * @return
     */
    public static int getValueFromBits(boolean[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                result += Math.pow(2,i);
            }
        }
        return result;
    }

    /**
     * Takes in a boolean array representing a binary number where
     * true -> 1 and false -> 0, and returns it as a string
     * @param array
     * @return
     */
    public static String getStringFromBits(boolean[] array) {

        String out = "";

        for (int i = 0; i < array.length; i++) {
            out = out + (array[i] ? "1" : "0");
        }

        return out;

    }

    /**
     * Takes in a binary string of 1's and 0's and converts it to a boolean array
     * of trues and falses
     * @param value
     * @return
     */
    public static boolean[] getBitsFromString(String value) {

        
        boolean[] output = new boolean[value.length()];

        
        for (int i = 0; i < value.length(); i++) {
            if (value.substring(i,i+1).equals("1")) {
                output[i] = true;
            } else if (value.substring(i, i+1).equals("0")) {
                output[i] = false;
            } else {
                System.err.println(value + " is not a valid binary string!");
            }
        }

        return output;

    }
}
