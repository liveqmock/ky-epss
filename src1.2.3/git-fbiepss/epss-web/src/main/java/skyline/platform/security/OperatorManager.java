package skyline.platform.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.platform.db.ConnectionManager;
import skyline.platform.system.manage.dao.PtDeptBean;
import skyline.platform.system.manage.dao.PtOperBean;
import skyline.platform.utils.BusinessDate;
import skyline.security.MD5Helper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: OperatorManager.java
 * </p>
 * <p>
 * Description: This class includes the basic behaviors of operator.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author WangHaiLei
 * @version 1.6 $ UpdateDate: Y-M-D-H-M: 2003-12-02-09-50 2004-03-01-20-35 $
 */
public class OperatorManager implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(OperatorManager.class);
    /**
     * operatorid�Ǵ�login(operatorid, password)�еõ��ġ�
     */
    private String fimgSign = "";

    private String operatorid = null;
    /*
    20100820 zhanrui
    ��ǰȨ���µİ���targetmachine����Ĳ˵���
    Ŀǰֻ��Ϊ������
    1��default����Ҫ��ҵ��˵���targetmachine Ϊ�ջ� Ϊdefault��
    2��system����Ҫ��ϵͳ������ز˵�
     */
    private Map jsonMap = new HashMap();

    private MenuBean mb;

    private List jsplist = null;

    private PtOperBean operator;

    private String remoteAddr = null;

    private String remoteHost = null;

    private String loginTime = BusinessDate.getTodaytime();

    private boolean isLogin = false;

    public OperatorManager() {
        // //����ͼƬ��ʾ
        createImgSign();
    }

    /**
     * ����һ��Operator Object�����Ojbect�а����ò���Ա�Ļ�����Ϣ��������operid, email, enabled, sex,
     * status, opername��
     *
     * @return Operator Ojbect.
     */
    public PtOperBean getOperator() {
        return operator;
    }

    /**
     * �õ���ǰ����Ա��operatorname��
     *
     * @return
     */
    public String getOperatorName() {
        return operator.getOpername();
    }

    /**
     *
     * @return
     */
    public String getOperatorId() {
        return operatorid;
    }

    /**
     * ����Աǩ������֤operid+passwd�Ƿ���ȷ ǩ���ɹ��� 1.isLogin=true 2.ȡ�øò���Ա��ص����н�ɫ 3.��ʼ����Դ�б�
     * 4.ȡ�ò���Ա�Ĳ˵�
     *
     * @param operid
     * @param password
     * @return boolean
     * @roseuid 3F80B6360281
     */
    public boolean login(String operid, String password) {
        ConnectionManager cm = ConnectionManager.getInstance();
        try {
            String loginWhere = "where operid='" + operid
                    + "' and operpasswd ='" + MD5Helper.getMD5String(password) + "'and operenabled='1'";
            this.operatorid = operid;
            operator = new PtOperBean();
            operator = (PtOperBean) operator.findFirstByWhere(loginWhere);
            if (operator == null) {
                isLogin = false;
                return false;
            }

            String sss = "��¼ʱ�� :" + loginTime + " IP: " + remoteAddr
                    + " �������� : " + remoteHost;

            operator.setFillstr600(sss);
            PtDeptBean ptpdet = new PtDeptBean();
            operator.setPtDeptBean((PtDeptBean) ptpdet
                    .findFirstByWhere("where deptid='" + operator.getDeptid()
                            + "'"));
            isLogin = true;
            // ��ʼ���˵���
            try {
                mb = new MenuBean();
                this.jsonMap.put("default", mb.generateJsonStream("default"));
            } catch (Exception ex3) {
                ex3.printStackTrace();
                System.err.println("Wrong when getting menus of operator: [ "
                        + ex3 + "]");
            }
            return isLogin;
        } catch (Exception e) {
            logger.error("��ȡ��¼��Ϣ����", e);
            return false;
        } finally {
            cm.release();
            //session.close();
        }
    }

    /**
     * @return ArrayList
     * @roseuid 3F80B71A01BC
     */
    public List getJspList() {
        return jsplist;
    }

    /**
     * @return boolean
     * @roseuid 3F80B71A00BC
     */
    public boolean isLogin() {
        return isLogin;
    }

    /**
     * ǩ��
     */
    public void logout() {
        isLogin = false;
        operator = null;
        operatorid = null;
        mb = null;
        remoteHost = null;
        remoteAddr = null;
        loginTime = null;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    private void createImgSign() {
        fimgSign = "";
        try {
            int rad = (int) Math.round(Math.random() * 10);
            if (rad == 10)
                rad = 9;
            fimgSign += rad;

            rad = (int) Math.round(Math.random() * 10);
            if (rad == 10)
                rad = 9;
            fimgSign += rad;

            rad = (int) Math.round(Math.random() * 10);
            if (rad == 10)
                rad = 9;
            fimgSign += rad;

            rad = (int) Math.round(Math.random() * 10);
            if (rad == 10)
                rad = 9;
            fimgSign += rad;
        } catch (Exception e) {

        }
    }

    public String getJsonString(String target){
        return (String)this.jsonMap.get(target);
    }
}
