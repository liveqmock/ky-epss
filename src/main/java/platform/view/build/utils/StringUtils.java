package platform.view.build.utils;

/* Generated by Together */

/**
 * 字符串操作工具类。
 *
 * @author zhouwei
 * $Date: 2006/05/17 09:19:45 $
 * @version 1.0
 *
 * 版权：leonwoo
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;

import platform.view.build.db.DBUtil;

public class StringUtils {

  /**
   * 获得String数组的调试字符串
   * 
   * @param debugArray
   * @return
   */
  public static String arryToDebugString(String[] debugArray) {
    StringBuffer temp = new StringBuffer();

    for (int i = 0; i < debugArray.length; i++) {
      temp.append(i).append("=").append(debugArray[i]).append("\n");
    }

    return temp.toString();
  }

  /**
   * 将Object的Array数组转化成同长度的String数组
   * 
   * @param objArray
   * @return
   */
  public static String[] objectArrayToString(Object[] objArray) {
    String[] rtn = new String[objArray.length];

    for (int i = 0; i < objArray.length; i++) {
      rtn[i] = (String) objArray[i];
    }

    return rtn;
  }

  /**
   * 根据指定得编码格式转码
   */
  public static String convertCharset(String content, String from, String to) {
    try {
      return new String(content.getBytes(from), to);
    } catch (Exception e) {
      throw new RuntimeException("转码失败", e.getCause());
    }
  }

  /**
   * 将传入的字符串按照分割符分割成数组 如： "a|b|c" --> {a,b,c}
   * 
   * @param strInput
   * @param comma
   * @return
   */
  public static String[] commaToArray(String strInput, String comma) {
    if (strInput == null) {
      return new String[0];
    }
    if (comma == null) {
      comma = ",";
    }

    String[] strResult = strInput.split(comma);
    // StringTokenizer st = new StringTokenizer( strInput , comma );
    // int iCount = 0;
    // String[] strResult = new String[ st.countTokens() ];
    // while ( st.hasMoreTokens() )
    // {
    // strResult[ iCount ] = st.nextToken();
    // iCount++;
    // }
    return strResult;
  }

  /**
   * 将传入的字符串按照分割符分割成数组(默认用","分割) 如： "a|b|c" --> {a,b,c}
   * 
   * @param strInput
   * @return
   */
  public static String[] commaToArray(String strInput) {
    return commaToArray(strInput, ",");
  }

  /**
   * 将传入的字符串按照分割符分割成数组 如： "a|b|c" --> {a,b,c}
   * 
   * @param strInput
   * @param comma
   * @return
   */
  public static Vector commaToVector(String strInput, String comma) {
    if (strInput == null) {
      return new Vector();
    }
    if (comma == null) {
      comma = ",";
    }

    String[] strResult = strInput.split(comma);

    Vector vv = new Vector();
    for (int i = 0; i < strResult.length; i++) {
      vv.add(strResult[i]);
    }

    return vv;
  }

  /**
   * 将传入的字符串按照分割符分割成数组(默认用","分割) 如： "a|b|c" --> {a,b,c}
   * 
   * @param strInput
   * @return
   */
  public static Vector commaToVector(String strInput) {
    return commaToVector(strInput, ",");
  }

  /**
   * 将时间按照指定的模式格式化 如： StringUtil.toDateFormat(new Date(),"yyyy-MM-dd hh:mm:ss")
   * 
   * @param date
   * @param datepatern
   * @return
   */
  public static String toDateFormat(Date date, String datepatern) {
    if (date != null) {
      SimpleDateFormat sdf = new SimpleDateFormat(datepatern);
      return sdf.format(date);
    } else
      return "";
  }

  /**
   * 将时间按照指定的模式格式化 如： StringUtil.toDateFormat(new String,"yyyy-MM-dd hh:mm:ss")
   * 
   * @param date
   * @param datepatern
   * @return
   */
  public static String toDateFormat(String date, String datepatern) {
    if (date != null && !date.equals("")) {
      try {
        Date dd = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA).parse(date);
        return toDateFormat(dd, datepatern);
      } catch (Exception e) {
        return date;
      }
    } else
      return "";
  }

  /**
   * 判断checkbox是否选中
   * 
   * @param field
   *          -- 域
   * @param value
   *          -- 值
   * @return
   */
  public static String isCbSelected(String fields, String value) {
    boolean found = false;
    String[] field = commaToArray(fields);
    for (int i = 0; i < field.length; i++) {
      if (field[i].equals(value)) {
        found = true;
        break;
      }
    }
    if (found)
      return "checked";
    return "";
  }

  /**
   * 判断radiobutton是否选中
   * 
   * @param field
   *          -- 域
   * @param value
   *          -- 值
   * @return
   */
  public static String isRbSelected(String field, String value) {
    return (field.equals(value)) ? "checked" : "";
  }

  /**
   * 判断select-option是否选中
   * 
   * @param field
   *          -- 域
   * @param value
   *          -- 值
   * @return
   */
  public static String isSoSelected(String field, String value) {
    return (field.equals(value)) ? "selected" : "";
  }

  public static String doPreProcess(String strValue) {
    if (strValue == null || strValue.equalsIgnoreCase("null")) {
      return "";
    } else {
      return strValue.trim();
    }
  }

  public static String getChineseDate(String date) {
    return "to_char(" + date + ",'YYYY')||'年'||" + "to_char(" + date + ",'MM')||'月'||" + "to_char(" + date
        + ",'DD')||'日 '||" + "to_char(" + date + ",'HH24')||'时'||" + "to_char(" + date + ",'MI')||'分'||" + "to_char("
        + date + ",'SS')||'秒'";
  }

  public static String getSimpleChineseDate(String date) {
    return "to_char(" + date + ",'YYYY')||'年'||" + "to_char(" + date + ",'MM')||'月'||" + "to_char(" + date
        + ",'DD')||'日'";
  }

  /**
   * 得到完整的时间字符串
   * 
   * @param vday
   *          String
   * @param vhour
   *          String
   * @param vmi
   *          String
   * @return String
   */
  public static String getDateTime(String vday, String vhour, String vmi, String vmm) {
    String dtstr = "";
    if (vday != null && !vday.equals(""))
      if (vhour != null && !vhour.equals(""))
        if (vmi != null && !vmi.equals(""))
          if (vmm != null && !vmm.equals(""))
            dtstr = vday + " " + vhour + ":" + vmi + ":" + vmm;
          else
            dtstr = vday + " " + vhour + ":" + vmi;
        else
          dtstr = vday + " " + vhour;
      else
        dtstr = vday;
    else
      throw new IllegalArgumentException("日期参数错误！");

    return dtstr;
  }

  /**
   * 辅助函数，按照日期格式拆分日期字符串，并返回Calendar对象
   * 
   * @param bussDate
   *          String
   * @return Calendar
   */
  public static Calendar getBussCalendar(String bussDate) {
    // 按照日期编码规则，拆分日期的各个域
    String[] dd = delimitedListToStringArray(bussDate, "-");
    if (dd.length != 3) {
      throw new IllegalArgumentException("非法的日期参数");
    }
    String year = dd[0];
    String month = dd[1];
    String day = dd[2];

    // 得到当前日历函数
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"), Locale.CHINA);
    calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

    return calendar;
  }

  private static final String FOLDER_SEPARATOR = "/"; // folder separator

  private static final String WINDOWS_FOLDER_SEPARATOR = "\\"; // Windows folder
  // separator

  private static final String TOP_PATH = ".."; // top folder

  private static final String CURRENT_PATH = "."; // current folder

  // ---------------------------------------------------------------------
  // General convenience methods for working with Strings
  // ---------------------------------------------------------------------

  /**
   * Check if a String has length.
   * <p>
   * 
   * <pre>
   * StringUtils.hasLength(null) = false
   * StringUtils.hasLength(&quot;&quot;) = false
   * StringUtils.hasLength(&quot; &quot;) = true
   * StringUtils.hasLength(&quot;Hello&quot;) = true
   * </pre>
   * 
   * @param str
   *          the String to check, may be null
   * @return <code>true</code> if the String is not null and has length
   */
  public static boolean hasLength(String str) {
    return (str != null && str.length() > 0);
  }

  /**
   * Check if a String has text. More specifically, returns <code>true</code> if
   * the string not <code>null<code>, it's <code>length is > 0</code>, and it has at least one non-whitespace
   * character.
   * <p>
   * 
   * <pre>
   * StringUtils.hasText(null) = false
   * StringUtils.hasText(&quot;&quot;) = false
   * StringUtils.hasText(&quot; &quot;) = false
   * StringUtils.hasText(&quot;12345&quot;) = true
   * StringUtils.hasText(&quot; 12345 &quot;) = true
   * </pre>
   * 
   * @param str
   *          the String to check, may be null
   * @return <code>true</code> if the String is not null, length > 0, and not
   *         whitespace only
   * @see Character#isWhitespace
   */
  public static boolean hasText(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return false;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Trim leading whitespace from the given String.
   * 
   * @param str
   *          the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimLeadingWhitespace(String str) {
    if (str.length() == 0) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
      buf.deleteCharAt(0);
    }
    return buf.toString();
  }

  /**
   * Trim trailing whitespace from the given String.
   * 
   * @param str
   *          the String to check
   * @return the trimmed String
   * @see Character#isWhitespace
   */
  public static String trimTrailingWhitespace(String str) {
    if (str.length() == 0) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str);
    while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
      buf.deleteCharAt(buf.length() - 1);
    }
    return buf.toString();
  }

  /**
   * Test if the given String starts with the specified prefix, ignoring
   * upper/lower case.
   * 
   * @param str
   *          the String to check
   * @param prefix
   *          the prefix to look for
   * @see String#startsWith
   */
  public static boolean startsWithIgnoreCase(String str, String prefix) {
    if (str == null || prefix == null) {
      return false;
    }
    if (str.startsWith(prefix)) {
      return true;
    }
    if (str.length() < prefix.length()) {
      return false;
    }
    String lcStr = str.substring(0, prefix.length()).toLowerCase();
    String lcPrefix = prefix.toLowerCase();
    return lcStr.equals(lcPrefix);
  }

  /**
   * Count the occurrences of the substring in string s.
   * 
   * @param str
   *          string to search in. Return 0 if this is null.
   * @param sub
   *          string to search for. Return 0 if this is null.
   */
  public static int countOccurrencesOf(String str, String sub) {
    if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
      return 0;
    }
    int count = 0, pos = 0, idx = 0;
    while ((idx = str.indexOf(sub, pos)) != -1) {
      ++count;
      pos = idx + sub.length();
    }
    return count;
  }

  /**
   * Replace all occurences of a substring within a string with another string.
   * 
   * @param inString
   *          String to examine
   * @param oldPattern
   *          String to replace
   * @param newPattern
   *          String to insert
   * @return a String with the replacements
   */
  public static String replace(String inString, String oldPattern, String newPattern) {
    if (inString == null) {
      return null;
    }
    if (oldPattern == null || newPattern == null) {
      return inString;
    }

    StringBuffer sbuf = new StringBuffer();
    // output StringBuffer we'll build up
    int pos = 0; // our position in the old string
    int index = inString.indexOf(oldPattern);
    // the index of an occurrence we've found, or -1
    int patLen = oldPattern.length();
    while (index >= 0) {
      sbuf.append(inString.substring(pos, index));
      sbuf.append(newPattern);
      pos = index + patLen;
      index = inString.indexOf(oldPattern, pos);
    }
    sbuf.append(inString.substring(pos));

    // remember to append any characters to the right of a match
    return sbuf.toString();
  }

  /**
   * Delete all occurrences of the given substring.
   * 
   * @param pattern
   *          the pattern to delete all occurrences of
   */
  public static String delete(String inString, String pattern) {
    return replace(inString, pattern, "");
  }

  /**
   * Delete any character in a given string.
   * 
   * @param charsToDelete
   *          a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's
   *          and new lines.
   */
  public static String deleteAny(String inString, String charsToDelete) {
    if (inString == null || charsToDelete == null) {
      return inString;
    }
    StringBuffer out = new StringBuffer();
    for (int i = 0; i < inString.length(); i++) {
      char c = inString.charAt(i);
      if (charsToDelete.indexOf(c) == -1) {
        out.append(c);
      }
    }
    return out.toString();
  }

  // ---------------------------------------------------------------------
  // Convenience methods for working with formatted Strings
  // ---------------------------------------------------------------------

  /**
   * Unqualify a string qualified by a '.' dot character. For example,
   * "this.name.is.qualified", returns "qualified".
   * 
   * @param qualifiedName
   *          the qualified name
   */
  public static String unqualify(String qualifiedName) {
    return unqualify(qualifiedName, '.');
  }

  /**
   * Unqualify a string qualified by a separator character. For example,
   * "this:name:is:qualified" returns "qualified" if using a ':' separator.
   * 
   * @param qualifiedName
   *          the qualified name
   * @param separator
   *          the separator
   */
  public static String unqualify(String qualifiedName, char separator) {
    return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
  }

  /**
   * Capitalize a <code>String</code>, changing the first letter to upper case
   * as per {@link Character#toUpperCase(char)}. No other letters are changed.
   * 
   * @param str
   *          the String to capitalize, may be null
   * @return the capitalized String, <code>null</code> if null
   */
  public static String capitalize(String str) {
    return changeFirstCharacterCase(str, true);
  }

  public static String getPara(HttpServletRequest req, String paraName) {
    String temp = req.getParameter(paraName);
    if (temp != null) {
      return convertCharset(temp.trim(), "ISO-8859-1", "GBK");
    } else {
      return "";
    }
  }

  public static String getNoInjectPara(HttpServletRequest req, String paraName) {
    String temp = getPara(req, paraName);

    if (temp.equals(""))
      return temp;

    String reg = "(or |=|'|delete|update|select|drop|all|OR|DELETE|AND|UPDATE|SELECT|DROP|TRUNCATE|truncate)";

    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(temp);

    if (matcher.find()) {
      System.out.print(temp);
      throw new IllegalArgumentException("您提交的数据中含有非法的字符，请修改后重新提交！！！");
    }

    return temp;
  }

  /**
   * Uncapitalize a <code>String</code>, changing the first letter to lower case
   * as per {@link Character#toLowerCase(char)}. No other letters are changed.
   * 
   * @param str
   *          the String to uncapitalize, may be null
   * @return the uncapitalized String, <code>null</code> if null
   */
  public static String uncapitalize(String str) {
    return changeFirstCharacterCase(str, false);
  }

  private static String changeFirstCharacterCase(String str, boolean capitalize) {
    if (str == null || str.length() == 0) {
      return str;
    }
    StringBuffer buf = new StringBuffer(str.length());
    if (capitalize) {
      buf.append(Character.toUpperCase(str.charAt(0)));
    } else {
      buf.append(Character.toLowerCase(str.charAt(0)));
    }
    buf.append(str.substring(1));
    return buf.toString();
  }

  /**
   * Extract the filename from the given path, e.g. "mypath/myfile.txt" ->
   * "myfile.txt".
   * 
   * @param path
   *          the file path
   * @return the extracted filename
   */
  public static String getFilename(String path) {
    int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
  }

  /**
   * Apply the given relative path to the given path, assuming standard Java
   * folder separation (i.e. "/" separators);
   * 
   * @param path
   *          the path to start from (usually a full file path)
   * @param relativePath
   *          the relative path to apply (relative to the full file path above)
   * @return the full file path that results from applying the relative path
   */
  public static String applyRelativePath(String path, String relativePath) {
    int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
    if (separatorIndex != -1) {
      String newPath = path.substring(0, separatorIndex);
      if (!relativePath.startsWith("/")) {
        newPath += "/";
      }
      return newPath + relativePath;
    } else {
      return relativePath;
    }
  }

  /**
   * Normalize the path by suppressing sequences like "path/.." and inner simple
   * dots folders.
   * <p>
   * The result is convenient for path comparison. For other uses, notice that
   * Windows separators ("\") are replaced by simple dashes.
   * 
   * @param path
   *          the original path
   * @return the normalized path
   */
  public static String cleanPath(String path) {
    String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
    String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
    List pathElements = new LinkedList();
    int tops = 0;
    for (int i = pathArray.length - 1; i >= 0; i--) {
      if (CURRENT_PATH.equals(pathArray[i])) {
        // do nothing
      } else if (TOP_PATH.equals(pathArray[i])) {
        tops++;
      } else {
        if (tops > 0) {
          tops--;
        } else {
          pathElements.add(0, pathArray[i]);
        }
      }
    }
    return collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
  }

  /**
   * Compare two paths after normalization of them.
   * 
   * @param path1
   *          First path for comparizon
   * @param path2
   *          Second path for comparizon
   * @return True if the two paths are equivalent after normalization
   */
  public static boolean pathEquals(String path1, String path2) {
    return cleanPath(path1).equals(cleanPath(path2));
  }

  /**
   * Parse the given locale string into a <code>java.util.Locale</code>. This is
   * the inverse operation of Locale's <code>toString</code>.
   * 
   * @param localeString
   *          the locale string, following <code>java.util.Locale</code>'s
   *          toString format ("en", "en_UK", etc). Also accepts spaces as
   *          separators, as alternative to underscores.
   * @return a corresponding Locale instance
   */
  public static Locale parseLocaleString(String localeString) {
    String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
    String language = parts.length > 0 ? parts[0] : "";
    String country = parts.length > 1 ? parts[1] : "";
    String variant = parts.length > 2 ? parts[2] : "";
    return (language.length() > 0 ? new Locale(language, country, variant) : null);
  }

  // ---------------------------------------------------------------------
  // Convenience methods for working with String arrays
  // ---------------------------------------------------------------------

  /**
   * Append the given String to the given String array, returning a new array
   * consisting of the input array contents plus the given String.
   * 
   * @param arr
   *          the array to append to
   * @param str
   *          the String to append
   * @return the new array
   */
  public static String[] addStringToArray(String[] arr, String str) {
    String[] newArr = new String[arr.length + 1];
    System.arraycopy(arr, 0, newArr, 0, arr.length);
    newArr[arr.length] = str;
    return newArr;
  }

  /**
   * Turn given source String array into sorted array.
   * 
   * @param source
   *          the source array
   * @return the sorted array (never null)
   */
  public static String[] sortStringArray(String[] source) {
    if (source == null) {
      return new String[0];
    }
    Arrays.sort(source);
    return source;
  }

  /**
   * Split a String at the first occurrence of the delimiter. Does not include
   * the delimiter in the result.
   * 
   * @param toSplit
   *          the string to split
   * @param delimiter
   *          to split the string up with
   * @return a two element array with index 0 being before the delimiter, and
   *         index 1 being after the delimiter (neither element includes the
   *         delimiter); or null if the delimiter wasn't found in the given
   *         input String
   */
  public static String[] split(String toSplit, String delimiter) {
    int offset = toSplit.indexOf(delimiter);
    if (offset < 0) {
      return null;
    }
    String beforeDelimiter = toSplit.substring(0, offset);
    String afterDelimiter = toSplit.substring(offset + delimiter.length());
    return new String[] { beforeDelimiter, afterDelimiter };
  }

  /**
   * Take an array Strings and split each element based on the given delimiter.
   * A <code>Properties</code> instance is then generated, with the left of the
   * delimiter providing the key, and the right of the delimiter providing the
   * value.
   * <p>
   * Will trim both the key and value before adding them to the
   * <code>Properties</code> instance.
   * 
   * @param array
   *          the array to process
   * @param delimiter
   *          to split each element using (typically the equals symbol)
   * @return a <code>Properties</code> instance representing the array contents,
   *         or <code>null</code> if the array to process was null or empty
   */
  public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
    return splitArrayElementsIntoProperties(array, delimiter, null);
  }

  /**
   * Take an array Strings and split each element based on the given delimiter.
   * A <code>Properties</code> instance is then generated, with the left of the
   * delimiter providing the key, and the right of the delimiter providing the
   * value.
   * <p>
   * Will trim both the key and value before adding them to the
   * <code>Properties</code> instance.
   * 
   * @param array
   *          the array to process
   * @param delimiter
   *          to split each element using (typically the equals symbol)
   * @param charsToDelete
   *          one or more characters to remove from each element prior to
   *          attempting the split operation (typically the quotation mark
   *          symbol), or <code>null</code> if no removal should occur
   * @return a <code>Properties</code> instance representing the array contents,
   *         or <code>null</code> if the array to process was null or empty
   */
  public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

    if (array == null || array.length == 0) {
      return null;
    }

    Properties result = new Properties();
    for (int i = 0; i < array.length; i++) {
      String element = array[i];
      if (charsToDelete != null) {
        element = deleteAny(array[i], charsToDelete);
      }
      String[] splittedElement = split(element, delimiter);
      if (splittedElement == null) {
        continue;
      }
      result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
    }
    return result;
  }

  /**
   * Tokenize the given String into a String array via a StringTokenizer. Trims
   * tokens and omits empty tokens.
   * <p>
   * The given delimiters string is supposed to consist of any number of
   * delimiter characters. Each of those characters can be used to separate
   * tokens. A delimiter is always a single character; for multi-character
   * delimiters, consider using <code>delimitedListToStringArray</code>
   * 
   * @param str
   *          the String to tokenize
   * @param delimiters
   *          the delimiter characters, assembled as String (each of those
   *          characters is individually considered as delimiter).
   * @return an array of the tokens
   * @see java.util.StringTokenizer
   * @see String#trim
   * @see #delimitedListToStringArray
   */
  public static String[] tokenizeToStringArray(String str, String delimiters) {
    return tokenizeToStringArray(str, delimiters, true, true);
  }

  /**
   * Tokenize the given String into a String array via a StringTokenizer.
   * <p>
   * The given delimiters string is supposed to consist of any number of
   * delimiter characters. Each of those characters can be used to separate
   * tokens. A delimiter is always a single character; for multi-character
   * delimiters, consider using <code>delimitedListToStringArray</code>
   * 
   * @param str
   *          the String to tokenize
   * @param delimiters
   *          the delimiter characters, assembled as String (each of those
   *          characters is individually considered as delimiter)
   * @param trimTokens
   *          trim the tokens via String's <code>trim</code>
   * @param ignoreEmptyTokens
   *          omit empty tokens from the result array (only applies to tokens
   *          that are empty after trimming; StringTokenizer will not consider
   *          subsequent delimiters as token in the first place).
   * @return an array of the tokens
   * @see java.util.StringTokenizer
   * @see String#trim
   * @see #delimitedListToStringArray
   */
  public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
      boolean ignoreEmptyTokens) {

    StringTokenizer st = new StringTokenizer(str, delimiters);
    List tokens = new ArrayList();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (trimTokens) {
        token = token.trim();
      }
      if (!ignoreEmptyTokens || token.length() > 0) {
        tokens.add(token);
      }
    }
    return (String[]) tokens.toArray(new String[tokens.size()]);
  }

  /**
   * Take a String which is a delimited list and convert it to a String array.
   * <p>
   * A single delimiter can consists of more than one character: It will still
   * be considered as single delimiter string, rather than as bunch of potential
   * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
   * 
   * @param str
   *          the input String
   * @param delimiter
   *          the delimiter between elements (this is a single delimiter, rather
   *          than a bunch individual delimiter characters)
   * @return an array of the tokens in the list
   * @see #tokenizeToStringArray
   */
  public static String[] delimitedListToStringArray(String str, String delimiter) {
    if (str == null) {
      return new String[0];
    }
    if (delimiter == null) {
      return new String[] { str };
    }

    List result = new ArrayList();
    int pos = 0;
    int delPos = 0;
    while ((delPos = str.indexOf(delimiter, pos)) != -1) {
      result.add(str.substring(pos, delPos));
      pos = delPos + delimiter.length();
    }
    if (str.length() > 0 && pos <= str.length()) {
      // Add rest of String, but not in case of empty input.
      result.add(str.substring(pos));
    }

    return (String[]) result.toArray(new String[result.size()]);
  }

  /**
   * Convert a CSV list into an array of Strings.
   * 
   * @param str
   *          CSV list
   * @return an array of Strings, or the empty array if s is null
   */
  public static String[] commaDelimitedListToStringArray(String str) {
    return delimitedListToStringArray(str, ",");
  }

  /**
   * Convenience method to convert a CSV string list to a set. Note that this
   * will suppress duplicates.
   * 
   * @param str
   *          CSV String
   * @return a Set of String entries in the list
   */
  public static Set commaDelimitedListToSet(String str) {
    Set set = new TreeSet();
    String[] tokens = commaDelimitedListToStringArray(str);
    for (int i = 0; i < tokens.length; i++) {
      set.add(tokens[i]);
    }
    return set;
  }

  /**
   * Convenience method to return a String array as a delimited (e.g. CSV)
   * String. E.g. useful for toString() implementations.
   * 
   * @param arr
   *          array to display. Elements may be of any type (toString will be
   *          called on each element).
   * @param delim
   *          delimiter to use (probably a ",")
   */
  public static String arrayToDelimitedString(Object[] arr, String delim) {
    if (arr == null) {
      return "";
    }

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < arr.length; i++) {
      if (i > 0) {
        sb.append(delim);
      }
      sb.append(arr[i]);
    }
    return sb.toString();
  }

  /**
   * Convenience method to return a Collection as a delimited (e.g. CSV) String.
   * E.g. useful for toString() implementations.
   * 
   * @param coll
   *          Collection to display
   * @param delim
   *          delimiter to use (probably a ",")
   * @param prefix
   *          string to start each element with
   * @param suffix
   *          string to end each element with
   */
  public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
    if (coll == null) {
      return "";
    }

    StringBuffer sb = new StringBuffer();
    Iterator it = coll.iterator();
    int i = 0;
    while (it.hasNext()) {
      if (i > 0) {
        sb.append(delim);
      }
      sb.append(prefix).append(it.next()).append(suffix);
      i++;
    }
    return sb.toString();
  }

  /**
   * Convenience method to return a Collection as a delimited (e.g. CSV) String.
   * E.g. useful for toString() implementations.
   * 
   * @param coll
   *          Collection to display
   * @param delim
   *          delimiter to use (probably a ",")
   */
  public static String collectionToDelimitedString(Collection coll, String delim) {
    return collectionToDelimitedString(coll, delim, "", "");
  }

  /**
   * Convenience method to return a String array as a CSV String. E.g. useful
   * for toString() implementations.
   * 
   * @param arr
   *          array to display. Elements may be of any type (toString will be
   *          called on each element).
   */
  public static String arrayToCommaDelimitedString(Object[] arr) {
    return arrayToDelimitedString(arr, ",");
  }

  /**
   * Convenience method to return a Collection as a CSV String. E.g. useful for
   * toString() implementations.
   * 
   * @param coll
   *          Collection to display
   */
  public static String collectionToCommaDelimitedString(Collection coll) {
    return collectionToDelimitedString(coll, ",");
  }

  /**
   * 在字符串的前面补上制定的字符到指定的长度
   * 
   * @param oristr
   * @param prestr
   * @param len
   * @return
   */
  public static String addPrefix(String oristr, String prestr, int len) {
    if (oristr.length() > len)
      throw new IllegalArgumentException("original string is too long!!!");

    StringBuffer sb = new StringBuffer(oristr);
    for (int i = 0; i < len - oristr.length(); i++) {
      sb.insert(0, prestr);
    }

    return sb.toString();

  }

  public static void getLoadForm(Object bean, JspWriter out) throws Exception {
    if (bean == null)
      return;

    out.println("<script language=\"javascript\">");
    out.println("  function load_form() {");

    Map mm = PropertyUtils.describe(bean);
    Iterator it = mm.keySet().iterator();

    while (it.hasNext()) {
      String key = (String) it.next();
      // String 类型
      if (PropertyUtils.getPropertyType(bean, key) == String.class) {
        String value = (String) mm.get(key);
        out.println("    initFormElement('" + key + "','" + Basic.encode(value) + "');");
      }// int 类型
      else if (PropertyUtils.getPropertyType(bean, key) == int.class) {
        String value = String.valueOf(mm.get(key));
        out.println("    initFormElement('" + key + "','" + Basic.encode(value) + "');");
      }
      // double 类型
      else if (PropertyUtils.getPropertyType(bean, key) == double.class) {
        String value = DBUtil.doubleToStr(Double.parseDouble(String.valueOf(mm.get(key))));
        out.println("    initFormElement('" + key + "','" + Basic.encode(value) + "');");
      }
    }
    out.println("  }");
    out.println("</script>");
  }

  public static String getNullString(String ori) {
    if (ori == null)
      return "";
    else
      return ori;
  }

  /**
   * 根据传入的数组转化为删除条件的in语句。
   * 
   * @param deleStr
   * @return
   */
  public static String getDeleStr(String[] deleStr) {
    try {
      StringBuffer tmpStr = new StringBuffer();
      int i = 0;
      for (; i < deleStr.length - 1; i++) {
        tmpStr.append("'").append(deleStr[i]).append("',");
      }
      tmpStr.append("'").append(deleStr[i]).append("'");

      return tmpStr.toString();

    } catch (Exception ex) {
      return "''";
    }
  }

  public static void main(String[] args) {
    // System.out.println(StringUtil.arrayToComma(new String[]{"a","b","c"}));
    // System.out.println(StringUtil.commaToArray("a|b|c")[0]);
    // System.out.println(StringUtil.commaToArray("a|b|c")[1]);
    // System.out.println(StringUtil.commaToArray("a|b|c")[2]);
    // System.out.println(StringUtil.toDateFormat("2003-09-18 09:18:18","yyyy-MM-dd hh:mm"));
    // String[] ddd = StringUtils.commaToArray("1,2,,,5",",");

    // System.out.print(ddd.length);
    // System.out.println(StringUtils.getDateTime("2004-09-13","13","34",null));

    // System.out.println(ddd.length);

  }

}
