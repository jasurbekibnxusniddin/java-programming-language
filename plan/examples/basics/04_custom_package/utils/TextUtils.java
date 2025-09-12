package utils;  

public class TextUtils {
    public String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}
