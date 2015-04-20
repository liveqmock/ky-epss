package skyline.util;

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

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringUtils {

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
   * 将时间按照指定的模式格式化
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
   * 将时间按照指定的模式格式化 如：toDateFormat(new String,"yyyy-MM-dd hh:mm:ss")
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

  private static final String FOLDER_SEPARATOR = "/"; // folder separator

  private static final String WINDOWS_FOLDER_SEPARATOR = "\\"; // Windows folder

  private static final String TOP_PATH = ".."; // top folder

  private static final String CURRENT_PATH = "."; // current folder

  // ---------------------------------------------------------------------
  // General convenience methods for working with Strings
  // ---------------------------------------------------------------------

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
   * @see java.lang.Character#isWhitespace
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
   * @see java.lang.String#trim
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
   * @see java.lang.String#trim
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
  }
}
