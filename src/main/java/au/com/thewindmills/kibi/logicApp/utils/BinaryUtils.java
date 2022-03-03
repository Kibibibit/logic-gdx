package au.com.thewindmills.kibi.logicApp.utils;

public class BinaryUtils {

    public static int parse(String s) {
        return Integer.parseInt(s.strip().replaceAll(" ", ""),2);
    }

    public static int findBits(int s) {

        int bits = 1;

        while (Math.pow(2,bits) < s) {
            bits++;
        }
        
        return bits;

    }

    public static int grayCode(int s) {
        return s ^ (s >> 1);
    }

    public static int inverseGrayCode(int s) {
        int inv = 0;

        for (; s != 0; s = s >> 1) {
            inv ^= s;
        }

        return inv;
    }
    
    public static int getValueAtBit(int value, int pos) {
        return ((value >> pos) % 2) << pos;
    }

    public static boolean bitActive(int value, int pos) {
        return (value >> pos) % 2 == 1;
    }

    public static int getValueFromBits(boolean[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                result += Math.pow(2,i);
            }
        }
        return result;
    }

    public static boolean[] getBitsFromString(String value) {

        
        boolean[] output = new boolean[value.length()];

        
        for (int i = 0; i < value.length(); i++) {
            if (value.substring(i,i+1).equals("1")) {
                output[i] = true;
            } else if (value.substring(i, i+1).equals("0")) {
                output[i] = false;
            } else {
                System.err.println(value + " is not a valid binary strin!");
            }
        }

        return output;

    }
}
