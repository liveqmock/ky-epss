package epss.common.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToolUtil {
    public static final BigDecimal bigDecimal0=new BigDecimal(0);
    public static Boolean strIsDigit(String strPara){
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (strPara.matches(strRegex) ){
            return true;
        }
        return false;
    }

    public static String getStrLastMonth(String strThisPeriodPara){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(simpleDateFormat.parse(strThisPeriodPara));
            cd.add(Calendar.MONTH, -1);//减少一月
            return simpleDateFormat.format(cd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getStrThisMonth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
            Calendar cd = Calendar.getInstance();
            return simpleDateFormat.format(cd.getTime());
    }

    public static String getStrToday(){
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static int getDateByStr(String strStartDate,String strEndDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        int intDiff=0;
        try {
            Date startDate=simpleDateFormat.parse(strStartDate);
            Date endDate=simpleDateFormat.parse(strEndDate);
            if(startDate.after(endDate)){
                Date t=startDate;
                startDate=endDate;
                endDate=t;
            }
            Calendar startCalendar=Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar=Calendar.getInstance();
            endCalendar.setTime(endDate);
            //intDiff=endDate.getMonth()-startDate.getMonth();
            int intYear=endCalendar.get(Calendar.YEAR)-startCalendar.get(Calendar.YEAR);
            int intMonth=endCalendar.get(Calendar.MONTH)-startCalendar.get(Calendar.MONTH);
            intDiff = intYear*12+intMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return intDiff;
    }

    public static Date getLastMonthDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    public static int lookIndex(String strTempPara,char charPara,Integer intPara){
        int count=0;
        char arr[] = strTempPara.toCharArray();

        for(int i=0;i<arr.length;i++){
            if(charPara==arr[i]){
                count++;
                if(count==intPara){
                    return i;//返回所寻找的字符第五次出现的索引
                }
            }
        }
        return -1;//代表要寻找的字符没有出现五次
    }

    public static BigDecimal getBdIgnoreNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?new BigDecimal(0):bigDecimalPara;
    }

    public static String getStrIgnoreNull(String strValue){
        return strValue==null?"":strValue;
    }

    public static Integer getIntIgnoreNull(Integer intValue){
        return intValue==null?0:intValue ;
    }

    public static BigDecimal getBdFrom0ToNull(BigDecimal bigDecimalPara){
        return bigDecimalPara.equals(new BigDecimal(0))?null:bigDecimalPara;
    }

    public static String padLeft_DoLevel(Integer integerPara,String strTemp){
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < integerPara-1; j++) {
            stringBuffer.append("&#8195;");
        }
        return stringBuffer.toString()+strTemp;
    }

    public static String padLeftSpace_DoLevel(Integer integerPara,String strTemp){
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < integerPara-1; j++) {
            stringBuffer.append("   ");
        }
        return stringBuffer.toString()+strTemp;
    }

    /*字符串忽略空的情况*/
    public static String getIgnoreSpaceOfStr(String strFrom){
        if(strFrom==null){
            return "";
        }
        Integer intIndexof=strFrom.lastIndexOf(";")+1;
        String strTemp=strFrom.substring(intIndexof);
        return strTemp ;
    }
}

