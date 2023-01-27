package com.github.kingwaggs.productmanager.coupang.sdk.domain;

public class StringUtil {

    public StringUtil() {
    }

    public static boolean containsIgnoreCase(String[] array, String value) {
        String[] var2 = array;
        int var3 = array.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String str = var2[var4];
            if (value == null && str == null) {
                return true;
            }

            if (value != null && value.equalsIgnoreCase(str)) {
                return true;
            }
        }

        return false;
    }

    public static String join(String[] array, String separator) {
        int len = array.length;
        if (len == 0) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            out.append(array[0]);

            for (int i = 1; i < len; ++i) {
                out.append(separator).append(array[i]);
            }

            return out.toString();
        }
    }
}
