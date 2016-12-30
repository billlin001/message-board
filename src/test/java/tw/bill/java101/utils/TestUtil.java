package tw.bill.java101.utils;

public class TestUtil {
    public static String createStringWithLength(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) result.append("a");
        return result.toString();
    }
}