package skyline.platform.utils;
/* Generated by Together */


import skyline.platform.db.*;

import java.lang.reflect.Method;

public class DbUtil
{
     public static final String METHOD_PRIFIX="set";



   /**
    * 将sql语句中特殊的字符进行替换，保证sql语句的完整性。
    * @param str
    * @return
    */
   public static String sqlEncode( String str )
   {
       if ( str == null || str.equals( "" ) )
       {
           return "";
       }
       String tmpStr = str;
       StringBuffer result = new StringBuffer();
       for ( int i = 0; i < tmpStr.length(); i++ )
       {
           char c = tmpStr.charAt( i );
           if ( c == '\'' )
           {
               result.append( "''" );
           }
           else
           {
               result.append( c );
           }
       }

       return result.toString();
   }

   public static Method getMethod(Object oo,String mname) {
     try {
          Method method = oo.getClass().getMethod(toMethodName(mname), new Class[] {String.class});
          return method;
     } catch(Exception ex) {
          return null;
     }
   }

   public static String toMethodName(String mname) {
        return METHOD_PRIFIX+mname.substring(0,1).toUpperCase()+mname.substring(1);
   }
   public static RecordSet getRecord(String sqlStr) {
      DatabaseConnection dc = null;
      try{
        dc = ConnectionManager.getInstance().getConnection();
        RecordSet rs = dc.executeQuery(sqlStr);
        return rs;
      }catch(Exception e) {
        e.printStackTrace();
        return null;
      }finally{
        if (dc != null)
          dc.close();
      }
    }


}
