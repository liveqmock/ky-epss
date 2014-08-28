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
     * operatorid是从login(operatorid, password)中得到的。
     */
    private String fimgSign = "";

    private String operatorid = null;
    /*
    20100820 zhanrui
    当前权限下的按照targetmachine分类的菜单项
    目前只分为两大类
    1、default：主要是业务菜单（targetmachine 为空或 为default）
    2、system：主要是系统管理相关菜单
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
        // //创建图片标示
        createImgSign();
    }

    /**
     * 返回一个Operator Object。这个Ojbect中包含该操作员的基本信息，包括：operid, email, enabled, sex,
     * status, opername。
     *
     * @return Operator Ojbect.
     */
    public PtOperBean getOperator() {
        return operator;
    }

    /**
     * 得到当前操作员的operatorname。
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
     * 操作员签到，验证operid+passwd是否正确 签到成功后 1.isLogin=true 2.取得该操作员相关的所有角色 3.初始化资源列表
     * 4.取得操作员的菜单
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

            String sss = "登录时间 :" + loginTime + " IP: " + remoteAddr
                    + " 机器名称 : " + remoteHost;

            operator.setFillstr600(sss);
            PtDeptBean ptpdet = new PtDeptBean();
            operator.setPtDeptBean((PtDeptBean) ptpdet
                    .findFirstByWhere("where deptid='" + operator.getDeptid()
                            + "'"));
            isLogin = true;
            // 初始化菜单。
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
            logger.error("获取登录信息错误！", e);
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
     * 签退
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
