package skyline.platform.utils;

public class StringMathFormat {
    public static String strFormat(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        double d = Double.parseDouble(str);
        if (d == 0) {
            return "0.000000";
        }
        d = d / 1000000;
        return d + "";
    }

    //本金格式化
    public static String strFormat2(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        str = Long.parseLong(str) + "";
        if ("0".equals(str)) {
            return "0.00";
        }
        StringBuffer sb = new StringBuffer(str);
        char[] chars = sb.reverse().toString().toCharArray();
        StringBuffer tmp = new StringBuffer("");
        for (int i = 0; i < chars.length; i++) {
            tmp.append(chars[i]);
            if (i == 1) {
                tmp.append(".");
            } else if (i % 3 == 1 && i != chars.length - 1) {
                tmp.append(",");
            }
        }
        return tmp.reverse().toString();
    }
}
