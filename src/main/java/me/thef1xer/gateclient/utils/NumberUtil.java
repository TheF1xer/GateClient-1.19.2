package me.thef1xer.gateclient.utils;

public class NumberUtil {

    public static boolean isInt(String s) {
        // TODO: Maybe regex one day?

        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
