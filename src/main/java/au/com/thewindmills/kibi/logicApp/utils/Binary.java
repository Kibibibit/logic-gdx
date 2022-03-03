package au.com.thewindmills.kibi.logicApp.utils;

public class Binary {

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

    public static boolean[] getBitsFromValue(int value) {
        int power = 0;
        while (Math.pow(2,power) < value) {
            power++;
        }
        power--;
        
        boolean[] output = new boolean[power+1];

        while (value > 0) {
            if (value >= Math.pow(2,power)) {
                output[power] = true;
                value -= Math.pow(2,power);
            }
            power--;
        }

        return output;

    }
}
