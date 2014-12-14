package skyline.util;

import epss.repository.model.model_show.AttachmentModel;
import org.apache.commons.lang.*;
import org.jdom.Element;
import skyline.platform.db.ConnectionManager;
import skyline.platform.db.DBUtil;
import skyline.platform.db.DatabaseConnection;
import skyline.platform.db.RecordSet;
import skyline.platform.form.config.SystemAttributeNames;
import skyline.platform.security.OperatorManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ToolUtil {
    public static final BigDecimal bigDecimal0=new BigDecimal(0);
    public static Boolean strIsDigit(String strPara){
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (strPara.matches(strRegex) ){
            return true;
        }
        return false;
    }

    public static OperatorManager getOperatorManager(){
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            throw new RuntimeException("用户未登录！");
        }
        return om;
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

    public static BigDecimal getBdFrom0ToNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?null:(bigDecimal0.compareTo(bigDecimalPara)==0?null:bigDecimalPara);
    }

    public static String getStrIgnoreNull(String strValue){
        return strValue==null?"":strValue;
    }

    public static Integer getIntIgnoreNull(Integer intValue){
        return intValue==null?0:intValue ;
    }

    public static String padLeft_DoLevel(Integer integerPara,String strTemp){
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < integerPara; j++) {
            stringBuffer.append("&#8195;");
        }
        return stringBuffer.toString()+strTemp;
    }

    public static String padLeftSpace_DoLevel(Integer integerPara,String strTemp){
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < integerPara; j++) {
            stringBuffer.append("   ");
        }
        return stringBuffer.toString()+strTemp;
    }

    public static String getMaxIdPlusOne(String strPreMaxIdPara,String strMaxIdPara){
        try {
            String strMaxId;

            strMaxId= strMaxIdPara;
            if(org.apache.commons.lang.StringUtils.isEmpty(strMaxId)){
                strMaxId=strPreMaxIdPara+ ToolUtil.getStrToday()+"001";
            }
            else if(strIsDigit(strMaxId)) {
                Integer intTemp=Integer.parseInt(strMaxId) ;
                intTemp=intTemp+1;
                strMaxId=intTemp.toString();
            }else if(strMaxId .length()>3){
                String strTemp=strMaxId.substring(strMaxId .length() -3).replaceFirst("^0+","");
                if(strIsDigit(strTemp)){
                    Integer intTemp=Integer.parseInt(strTemp) ;
                    intTemp=intTemp+1;
                    strMaxId=strMaxId.substring(0,strMaxId.length()-3)+ org.apache.commons.lang.StringUtils.leftPad(intTemp.toString(), 3, "0");
                }else{
                    strMaxId+="001";
                }
            }
            return strMaxId;
        } catch (Exception e) {
            throw e;
        }
    }

    //附件相关方法
    public static List<AttachmentModel> getListAttachmentByStrAttachment(String strAttachmentPara){
        List<AttachmentModel> attachmentListTemp=new ArrayList<>();
        if(strAttachmentPara!=null){
            attachmentListTemp.clear();
            if (!org.apache.commons.lang.StringUtils.isEmpty(strAttachmentPara)) {
                String strTemps[] = strAttachmentPara.split(";");
                for (int i = 0; i < strTemps.length; i++) {
                    AttachmentModel attachmentModelTemp = new AttachmentModel(i + "", strTemps[i], strTemps[i]);
                    attachmentListTemp.add(attachmentModelTemp);
                }
            }
        }
        return attachmentListTemp;
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

    public static String getStrLastUpdDate() {
        Date date =new Date();
        return dateFormat(date, "yyyy-MM-dd");
    }
    public static String getStrLastUpdTime() {
        Date date =new Date();
        return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getStrDateThisPeriod() {
        Date date =new Date();
        return dateFormat(date, "yyyyMM");
    }

    public static String getStrDateLastPeriod() {
        Date dateNow =new Date();
        Date dateLast = getLastMonthDate(dateNow);
        return ToolUtil.dateFormat(dateLast, "yyyyMM");
    }

    public static String getStrToday() {
        Date date =new Date();
        return dateFormat(date, "yyyyMMdd");
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

    /**
     * 将长时间格式化
     * @param date
     * @param strPattern
     * @return
     */
    public static String dateFormat(Date date,String strPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(strPattern);
        return formatter.format(date);
    }

    //取出某表某字段的最大值
    public static String  getFieldMax(DatabaseConnection dc,String Keyfield,String tableName,String whereStr){
        String maxcount ="1";
        try{
            String SQLStr = "select max(to_number(" + Keyfield + "))as maxcount from  " + tableName + " " +whereStr ;
            System.out.println("SQLStr="+SQLStr);
            RecordSet rs = dc.executeQuery(SQLStr);
            if(rs.next())
                maxcount = rs.getString(0);
            if(maxcount == null )
                maxcount = "1";
            else
                maxcount = "" + (Integer.parseInt(maxcount) +1);
        }catch(Exception e){
        }
        return maxcount;
    }
    //取出某表某字段的最大值
    public static String  getFieldMax(DatabaseConnection dc,String Keyfield,String tableName){
        return getFieldMax(dc,Keyfield,tableName,"");
    }

    public static String getUserName(String operPkidPara){
        ConnectionManager cm  = ConnectionManager.getInstance();
        DatabaseConnection dc = cm.get();
        String username ="";
        try{
            String SQLStr = "select NAME as username from OPER  where PKID='"+operPkidPara+"'";
            RecordSet rs = dc.executeQuery(SQLStr);
            if(rs.next())
                username = rs.getString(0);
            if(username == null)
                username = "";
        }catch(Exception e){
        }
        return username;
    }

    public static String getUserName(DatabaseConnection dc,String operID){
        String username ="";
        try{
            String SQLStr = "select NAME as username from OPER  where ID='"+operID+"'";
            RecordSet rs = dc.executeQuery(SQLStr);
            if(rs.next())
                username = rs.getString(0);
            if(username == null)
                username = "";
        }catch(Exception e){
        }
        return username;
    }
    // 生成添加语句
    public String createInsertSQL(String fieldStr,Element insertElement,String tablename){
        String sqlstr = "insert into  "+tablename +"(";
        String fieldname ="";
        String fieldvalue ="";

        List listchild = insertElement.getChildren();
        for (int j=0; j< listchild.size();j++){
            Element childnode = (Element)listchild.get(j);
            if (!fieldname.equals(""))
                fieldname +="," ;
            if (!fieldvalue.equals(""))
                fieldvalue +="," ;
            if (childnode.getAttributeValue("type").equals("text")){
                fieldname += childnode.getAttributeValue("name");
                fieldvalue+="'" + childnode.getAttributeValue("value")+"'";
            }

            if (childnode.getAttributeValue("type").equals("date")){
                fieldname += childnode.getAttributeValue("name");
                fieldvalue+= DBUtil.formatDateTime(childnode.getAttributeValue("value"));
            }

            if (childnode.getAttributeValue("type").equals("datetime")){
                fieldname += childnode.getAttributeValue("name");
                Calendar rightNow = Calendar.getInstance();
                String timeStr = rightNow.get(rightNow.HOUR_OF_DAY)+":"+rightNow.get(rightNow.MINUTE)+":"+rightNow.get(rightNow.SECOND);

                fieldvalue += DBUtil.formatDateTime(childnode.getAttributeValue("value")+" "+timeStr);
            }
            if ((childnode.getAttributeValue("type").equals("int"))||(childnode.getAttributeValue("type").equals("checkbox"))){
                fieldname  += childnode.getAttributeValue("name");
                fieldvalue += childnode.getAttributeValue("value");
            }
        }

        String[] recoderS = fieldStr.split(";");
        for (int i=0; i< recoderS.length;i++){
            if (!fieldname.equals(""))
                fieldname +="," ;
            if (!fieldvalue.equals(""))
                fieldvalue +="," ;
            String[] fields = recoderS[i].split("&");
            if (fields[2].equals("text")){
                fieldname += fields[0];
                fieldvalue+="'" + fields[1]+"'";
            }
            if ((fields[2].equals("date"))||(fields[2].equals("datetime"))){
                fieldname += fields[0];
                fieldvalue+=DBUtil.formatDateTime(fields[1]);
            }
            if (fields[2].equals("int")){
                fieldname += fields[0];
                fieldvalue+= fields[1];
            }
        }
        sqlstr += fieldname +" ) " + "values( "+ fieldvalue+" )";
        System.out.print(sqlstr);
        return sqlstr;
    }
    ///生成更新语句
    public String createupdateSQL(String fieldStr,Element updateElement,String tablename,String whereStr){
        String sqlstr = "update  "+tablename +"  set  ";
        String updatevalue ="";
        List listchild = updateElement.getChildren();
        for (int j=0; j< listchild.size()-1;j++){
            Element childnode = (Element)listchild.get(j);
            if (!updatevalue.equals(""))
                updatevalue +="," ;
            if (childnode.getAttributeValue("type").equals("text")){
                updatevalue += childnode.getAttributeValue("name")+" = '" + childnode.getAttributeValue("value")+"'";
            }
            if (childnode.getAttributeValue("type").equals("date")){
                updatevalue += childnode.getAttributeValue("name")+ "=" + DBUtil.formatDateTime(childnode.getAttributeValue("value"));
            }
            if (childnode.getAttributeValue("type").equals("datetime")){
                Calendar rightNow = Calendar.getInstance();
                String timeStr = rightNow.get(rightNow.HOUR_OF_DAY)+":"+rightNow.get(rightNow.MINUTE)+":"+rightNow.get(rightNow.SECOND);
                updatevalue +=childnode.getAttributeValue("name")+"="+DBUtil.formatDateTime(childnode.getAttributeValue("value")+" "+timeStr);
            }
            if ((childnode.getAttributeValue("type").equals("int"))||(childnode.getAttributeValue("type").equals("checkbox"))){
                updatevalue += childnode.getAttributeValue("name")+" = " + childnode.getAttributeValue("value")+"";
            }
        }
        if (!fieldStr.equals("")){
            String[] recoderS = fieldStr.split(";");
            for (int i=0; i< recoderS.length;i++){
                if (!updatevalue.equals(""))
                    updatevalue +="," ;
                String[] fields = recoderS[i].split("&");
                if (fields[2].equals("text")){
                    updatevalue += fields[0] + "='" + fields[1]+"'";
                }
                if ((fields[2].equals("date"))||(fields[2].equals("datetime"))){
                    updatevalue += fields[0] + "=" + DBUtil.formatDateTime(fields[1]);
                }
                if (fields[2].equals("int")){
                    updatevalue += fields[0] + "=" + fields[1]+"";
                }
            }
        }
        Element childRoot = (Element) listchild.get(listchild.size()-1);
        //添加更新条件
        sqlstr += updatevalue +" where (1=1)";
        if (!childRoot.getText().equals("no")) {
            sqlstr += childRoot.getText();
        }
        sqlstr += whereStr;
        return sqlstr;
    }
    ///生成删除语句
    public String createdeleteSQL(Element deleteElement,String tablename,String whereStr){
        String sqlstr = "delete  from   " + tablename +" where (1=1) ";
        List listchild = deleteElement.getChildren();
        Element childnode = (Element)listchild.get(0);
        if (childnode.getText().length() > 0)
            sqlstr +=  childnode.getText();
        sqlstr +=  whereStr;
        System.out.print(sqlstr);
        return sqlstr;
    }

    public static Calendar getDate(String sDate) {
        if ( sDate == null )
            return null;
        sDate = sDate.trim();
        if ( sDate.length() == 7 ) {
            sDate += "-01";
        }
        StringTokenizer st = new StringTokenizer(sDate,"-");
        int year=2100;
        int month=0;
        int day=1;
        try {
            year  = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken())-1;
            day   = Integer.parseInt(st.nextToken());
        } catch ( Exception e ) {
            return null;
        }
        return new GregorianCalendar(year,month,day);
    }
    public static String getDateString(Calendar sDate) {
        if ( sDate == null )
            return "";
        return (new java.text.SimpleDateFormat("yyyy-MM-dd")).format(sDate.getTime());
    }
    public static String getYearMonth(Calendar sDate) {
        if ( sDate == null )
            return "";
        return (new java.text.SimpleDateFormat("yyyy-MM")).format(sDate.getTime());
    }

    public static String getMoneyString(double money) {
        NumberFormat nf =NumberFormat.getInstance();
        ((DecimalFormat) nf).applyPattern("#,000.00");
        String moneyStr=nf.format(money);
        return ""+moneyStr;
    }

    public static String getDateString(String date) {
        if ( date == null )
            return date;
        String[] dates = date.split("-");
        if ( dates.length >= 2 ) {
            return dates[0] + "年" + dates[1] + "月";
        } else {
            return date;
        }
    }
    public static String getChnDate(String date) {
        if ( date == null )
            return date;
        String[] dates = date.split("-");
        return dates[0]+"年"+dates[1]+"月"+dates[2]+"日";
    }
    public static String inttostr(String intStr){
        String returnStr ="";
        for (int i=0; i <intStr.length() ;i++ ){
            if ( String.valueOf(intStr.charAt(i)).equals("1"))
                returnStr +="a";
            if ( String.valueOf(intStr.charAt(i)).equals("2"))
                returnStr +="b";
            if ( String.valueOf(intStr.charAt(i)).equals("3"))
                returnStr +="c";
            if ( String.valueOf(intStr.charAt(i)).equals("4"))
                returnStr +="d";
            if ( String.valueOf(intStr.charAt(i)).equals("5"))
                returnStr +="e";
            if ( String.valueOf(intStr.charAt(i)).equals("6"))
                returnStr +="f";
            if ( String.valueOf(intStr.charAt(i)).equals("7"))
                returnStr +="g";
            if ( String.valueOf(intStr.charAt(i)).equals("8"))
                returnStr +="h";
            if ( String.valueOf(intStr.charAt(i)).equals("9"))
                returnStr +="i";
            if ( String.valueOf(intStr.charAt(i)).equals("0"))
                returnStr +="j";
        }
        return returnStr;
    }

    public static String strtoint(String intStr){
        String returnStr ="";
        for (int i=0; i <intStr.length() ;i++ ){
            if ( String.valueOf(intStr.charAt(i)).equals("a"))
                returnStr +="1";
            if ( String.valueOf(intStr.charAt(i)).equals("b"))
                returnStr +="2";
            if ( String.valueOf(intStr.charAt(i)).equals("c"))
                returnStr +="3";
            if ( String.valueOf(intStr.charAt(i)).equals("d"))
                returnStr +="4";
            if ( String.valueOf(intStr.charAt(i)).equals("e"))
                returnStr +="5";
            if ( String.valueOf(intStr.charAt(i)).equals("f"))
                returnStr +="6";
            if ( String.valueOf(intStr.charAt(i)).equals("g"))
                returnStr +="7";
            if ( String.valueOf(intStr.charAt(i)).equals("h"))
                returnStr +="8";
            if ( String.valueOf(intStr.charAt(i)).equals("i"))
                returnStr +="9";
            if ( String.valueOf(intStr.charAt(i)).equals("j"))
                returnStr +="0";
        }
        return returnStr;
    }

    public static BigDecimal getBdIgnoreZeroNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?bigDecimal0:(bigDecimal0.compareTo(bigDecimalPara)==0?bigDecimal0:bigDecimalPara);
    }
    //算小计大计bd,str并存
    public static BigDecimal getBdFromStrOrBdIgnoreNull(Object objPara){
        if(objPara==null){
            return  bigDecimal0;
        }else if(objPara.getClass().equals((new BigDecimal(0)).getClass())){
            return (BigDecimal)objPara;
        }else {
            return objPara.equals("")?bigDecimal0:new BigDecimal(objPara.toString().replace(",","").replace("%",""));
        }
    }
    //将%以bd形式存入数据库
    public static BigDecimal getBdFromStrInPercent(String strPara){
        return   getStrIgnoreNull(strPara).equals("")?bigDecimal0:(new BigDecimal(strPara.replace("%","")).divide(new BigDecimal(100)));
    }
    //将数据库bd取出并转化为str
    public static String getStrFromBdIgnoreZeroNull(String strFormatPara, BigDecimal bigDecimalPara){
        if (bigDecimal0.compareTo(getBdIgnoreNull(bigDecimalPara))==0){
            return "";
        }else{
            DecimalFormat  df =new DecimalFormat(strFormatPara);
            return df.format(getBdIgnoreZeroNull(bigDecimalPara));
        }

    }
    public static void main(String[] argv) {
        System.out.println(getDateString("2004-10-20"));
    }
}

