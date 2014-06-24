//Source file: e:\\java\\zt\\platform\\form\\config\\TableBeanManager.java

package platform.view.build.form.config;

import java.util.*;
import java.util.logging.*;

import platform.view.build.db.*;

/**
 *  ��������Meta��Ϣ ƽ̨�����б���MetaData��Ϣ���������ݿ��У���ϵͳ����ʱ�����е���Ϣ ���ص��ڴ��У��Լӿ�ϵͳ�������ٶȡ�
 *  ��static�����г�ʼ���������ر����ֶ�enabled=1�����м�¼����Ϣ����Ϣ ��Դ��systblinfomain��systblinfodetl��
 *
 *@author     ���滻
 *@created    2003��10��11��
 *@version    1.0
 */
public class TableBeanManager {

    /**
     *  �������������������еı�������Ϣ ��ŷ�ʽ:tablename-TableBean Instanceֵ��
     *
     *@author    sun
     *@since     2003��10��11��
     */
    private static Map tableBeans;
    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003��10��11��
     */
    public static Logger logger = Logger.getLogger("zt.build.form.config.TableBeanManager");

    static {
        init();
    }


    /**
     *  ���ձ�����ȡ����Ϣ����ʵ��
     *
     *@param  name
     *@return       zt.build.form.config.TableBean
     *@roseuid      3F7161650260
     */
    public static TableBean getTable(String name) {
        return (TableBean) tableBeans.get(name);
    }

    public static void init(){
        logger.info("*********************TableBeanManager static init***********************");
        tableBeans = new HashMap();
        //tablename,type,enabled,dynamictbl,description
        ConnectionManager manager = ConnectionManager.getInstance();
        DatabaseConnection con = manager.getConnection();
        RecordSet rs = con.executeQuery("Select * from PTTBLINFOMAIN where enabled='1'");
        while (rs.next()) {

            String tableName = rs.getString("tablename").trim();
            logger.info("*************Find Table of " + tableName);
            TableBean tb = new TableBean(tableName);
            int type = Integer.parseInt(rs.getString("type").trim());
            tb.setType(type);

            if (type == 1) {
                tb.setDynamicTbl(rs.getString("dynamictbl"));
            }else{
                tb.setDynamicTbl("");
            }
            tb.setDescription(rs.getString("description")==null?"":DBUtil.fromDB(rs.getString("description").trim()));
            tableBeans.put(tb.getName(), tb);
        }

        //for each tablebeans ,get the value!

        Iterator itb = tableBeans.values().iterator();
        String tempStr = "select * from PTTBLINFODETL where tablename=";
        //PreparedStatement pst=con.getPreparedStatement(tempStr);
        while (itb.hasNext()) {
            TableBean tb = (TableBean) itb.next();
            String tableName = tb.getName();

            //pst.setString(1, tableName);
            RecordSet fieldRs = con.executeQuery(tempStr + "'" + tb.getName() + "'");
            logger.info("-------------------------------Table " + tableName + " all fields--------------------------------------------");
            //Add all fields!
            //tablename,seqno,name,isprimarykey,datatype,searchkey,length,isnull,isrepeat,caption,description
            //defaultvalue,precision,decimaldigits,reftbl,refname,refvalue,refvhere,
            while (fieldRs.next()) {

                FieldBean field = new FieldBean();
                field.setSeqno(fieldRs.getInt("seqno"));
                field.setName( fieldRs.getString("name").trim());

                field.setIsPrimary(fieldRs.getBoolean("isprimarykey"));
                field.setDatatype(fieldRs.getInt("datatype"));
                field.setIsSearch(fieldRs.getBoolean("searchkey"));
                field.setLength(fieldRs.getInt("length"));
                field.setIsnull(fieldRs.getBoolean("isnull"));
                field.setIsrepeat(fieldRs.getBoolean("isrepeat"));
                //caption have not??
                field.setCaption((fieldRs.getString("caption")==null)?"":DBUtil.fromDB(fieldRs.getString("caption").trim()));
                field.setDescription(DBUtil.fromDB((fieldRs.getString("description")==null)?"":fieldRs.getString("description")));
                field.setDefaultValue( fieldRs.getString("defaultvalue")==null?"":DBUtil.fromDB(fieldRs.getString("defaultvalue").trim()));
                field.setPrecision(fieldRs.getInt("precision"));
                field.setDecimals(fieldRs.getInt("decimaldigits"));

                field.setReftbl(fieldRs.getString("reftbl"));
                field.setRefnamefld( fieldRs.getString("refname"));
                field.setRefvaluefld( fieldRs.getString("refvalue"));
                field.setRefWhere(fieldRs.getString("refwhere"));
                field.setEnutpname(fieldRs.getString("enuid"));
                field.setNeedEncode(fieldRs.getBoolean("needencode"));
                tb.addField(field);
            }
            logger.info("-------------------------------Table " + tableName + " all fields end --------------------------------------------");

        }
        con.commit();
        manager.releaseConnection(con);
        logger.info("*********************TableBeanManager static init end***********************");

    }
}