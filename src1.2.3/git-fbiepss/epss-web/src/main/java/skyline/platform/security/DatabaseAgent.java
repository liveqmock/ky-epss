package skyline.platform.security;

import skyline.platform.db.ConnectionManager;
import skyline.platform.db.DBUtil;
import skyline.platform.db.DatabaseConnection;
import skyline.platform.db.RecordSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: The persistent state (ie. database).</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ZhongTian</p>
 *
 * @author WangHaiLei
 * @version 1.4
 *          $ UpdateDate: Y-M-D-H-M:
 *          2003-11-25-10-55
 *          2004-02-28-11-05
 *          2003-03-01-22-15
 *          $
 */
public class DatabaseAgent {
    /**
     * Retrieve all the basic information of a Operator, Except password.
     *
     * @param
     * @return Map which includes all the basic info in a HashMap with Key & Value pare.
     * @throws java.lang.Exception
     */
    public Map getBasicOfOperator(String operatorId)
            throws Exception {

        // 如果操作员根本不存在，则返回null。
        if (operatorId == null) {
            return null;
        }

        String SQL_getBasicOfOperator = "" +
                "SELECT * " +
                "FROM oper " +
                "WHERE id = '" + operatorId + "'";

        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;
        try {
            cm = ConnectionManager.getInstance();
            dc = cm.getConnection();
            rs = dc.executeQuery(SQL_getBasicOfOperator);
        } catch (Exception ex) {
            System.err.println(" [" + ex + "] ");
        }

        String nameOfOperator = null;
        String idOfOperator = null;
        String departmentId = null;
        String isSuper = null;
        String operatorType = null;
        String sexOfOperator = null;
        String emailOfOperator = null;
        String mobilePhone = null;
        String operatorPhone = null;
        String otherPhone = null;
        String enabledOfOperator = null;
        String combinedSequence = null;
        String isCombined = null;

        int statusOfOperator = 0;

        if (rs == null)
            rs = new RecordSet();

        if (!rs.next()) {
            return null;
        } else {
            try {
                if (rs.getString("name") != null) {
                    nameOfOperator = rs.getString("name").trim();
                } else {
                    nameOfOperator = "";
                }
                if (rs.getString("id") != null) {
                    idOfOperator = rs.getString("id").trim();
                } else {
                    idOfOperator = "";
                }
                if (rs.getString("deptid") != null) {
                    departmentId = rs.getString("deptid").trim();
                } else {
                    departmentId = "";
                }
                if (rs.getString("issuper") != null) {
                    isSuper = rs.getString("issuper").trim();
                } else {
                    isSuper = "";
                }
                if (rs.getString("opertype") != null) {
                    operatorType = rs.getString("opertype").trim();
                } else {
                    operatorType = "";
                }
                if (rs.getString("mobilephone") != null) {
                    mobilePhone = rs.getString("mobilephone").trim();
                } else {
                    mobilePhone = "";
                }
                if (rs.getString("operphone") != null) {
                    operatorPhone = rs.getString("operphone").trim();
                } else {
                    operatorPhone = "";
                }
                if (rs.getString("otherphone") != null) {
                    otherPhone = rs.getString("otherphone").trim();
                } else {
                    otherPhone = "";
                }
                if (rs.getString("cmbtxnseq") != null) {
                    combinedSequence = rs.getString("cmbtxnseq").trim();
                } else {
                    combinedSequence = "";
                }
                if (rs.getString("iscmbend") != null) {
                    isCombined = rs.getString("iscmbend").trim();
                } else {
                    isCombined = "";
                }
                if (rs.getString("sex") != null) {
                    sexOfOperator = rs.getString("sex").trim();
                } else {
                    sexOfOperator = "";
                }
                if (rs.getString("email") != null) {
                    emailOfOperator = rs.getString("email").trim();
                } else {
                    emailOfOperator = "";
                }
                if (rs.getString("operenabled") != null) {
                    enabledOfOperator = rs.getString("operenabled").trim();
                } else {
                    enabledOfOperator = "";
                }

                cm.releaseConnection(dc);
            } catch (Exception ex1) {
                System.err.println("[" + ex1 + "] ");
            }

            Map basicInfo = new HashMap();

            basicInfo.put("opnm", nameOfOperator);
            basicInfo.put("opid", idOfOperator);
            basicInfo.put("sexo", sexOfOperator);
            basicInfo.put("emai", emailOfOperator);
            basicInfo.put("enab", enabledOfOperator);
            basicInfo.put("issu", isSuper);
            basicInfo.put("depi", departmentId);
            basicInfo.put("otyp", operatorType);
            basicInfo.put("mobp", mobilePhone);
            basicInfo.put("opep", operatorPhone);
            basicInfo.put("othp", otherPhone);
            basicInfo.put("comb", combinedSequence);
            basicInfo.put("isco", isCombined);

            return basicInfo;
        }
    }
    /**
     * 20100820 zhanrui   取得某操作员所有菜单项
     *
     * @param operatorId
     * @return
     * @throws Exception
     */
    public List<MenuItemBean> getMenuItems(String targetMachine) throws Exception {
        String SQL_GetMenuItemsForAnOperator = "" +
                " SELECT DISTINCT m.menuid            AS menuItemId," +
                "                 m.parentmenuid      AS menuItemPId," +
                "                 m.menuLevel         AS menuLevel," +
                "                 m.menulabel         AS menuItemLabel," +
                "                 m.isleaf            AS menuItemIsLeaf," +
                "                 m.menuaction        AS menuItemUrl," +
                "                 m.menudesc          AS menuItemDescription," +
                "                 m.OpenWindow        AS menuItemOpenWindow," +
                "                 m.WindowWidth       AS menuItemWindowWidth," +
                "                 m.WindowHeight      AS menuItemWindowHeight," +
                "                 m.Levelidx," +
                "                 m.targetmachine" +
                " FROM   " +
                "       PTMENU m" +
                " WHERE m.targetmachine = '" + targetMachine + "' " +
                " ORDER BY m.menuLevel," +
                "          m.Levelidx ";
        ConnectionManager cm = null;
        DatabaseConnection dc = null;
        RecordSet rs = null;

        List<MenuItemBean> listTemp = new ArrayList();
        try {
            cm = ConnectionManager.getInstance();
            dc = cm.get();
            rs = dc.executeQuery(SQL_GetMenuItemsForAnOperator);
            if (rs == null)
                rs = new RecordSet();
            while (rs.next()) {
                String menuItemId = null;
                if (rs.getString("menuItemId") != null) {
                    menuItemId = rs.getString("menuItemId").trim();
                } else {
                    menuItemId = "";
                }

                String menuItemPId = null;
                if (rs.getString("menuItemPId") != null) {
                    menuItemPId = rs.getString("menuItemPId").trim();
                } else {
                    menuItemId = "";
                }

                int menuLevel = rs.getInt("menuLevel");

                String menuItemLabel = null;
                if (rs.getString("menuItemLabel") != null) {
                    menuItemLabel = rs.getString("menuItemLabel").trim();
                } else {
                    menuItemLabel = "";
                }

                String menuItemIsLeaf = null;
                if (rs.getString("menuItemIsLeaf") != null) {
                    menuItemIsLeaf = rs.getString("menuItemIsLeaf").trim();
                } else {
                    menuItemIsLeaf = "";
                }

                String menuItemUrl = null;
                if (rs.getString("menuItemUrl") != null) {
                    menuItemUrl = rs.getString("menuItemUrl").trim();
                } else {
                    menuItemUrl = "#";
                }

                String menuItemDescription = null;
                if (rs.getString("menuItemDescription") != null) {
                    menuItemDescription = rs.getString("menuItemDescription").trim();
                } else {
                    menuItemDescription = "#";
                }
                String menuItemOpenWindow = "0";
                if (rs.getString("menuItemOpenWindow") != null) {
                    menuItemOpenWindow = rs.getString("menuItemOpenWindow").trim();
                } else {
                    menuItemOpenWindow = "0";
                }

                String menuItemWindowWidth = "0";
                if (rs.getString("menuItemWindowWidth") != null) {
                    menuItemWindowWidth = rs.getString("menuItemWindowWidth").trim();
                } else {
                    menuItemWindowWidth = "0";
                }

                String menuItemWindowHeight = "0";
                if (rs.getString("menuItemWindowHeight") != null) {
                    menuItemWindowHeight = rs.getString("menuItemWindowHeight").trim();
                } else {
                    menuItemWindowHeight = "0";
                }
                String targetmachine = "";
                if (rs.getString("targetmachine") != null) {
                    targetmachine = rs.getString("targetmachine").trim();
                } else {
                    targetmachine = "";
                }
                String childcount = "";
                if (rs.getString("childcount") != null) {
                    childcount = rs.getString("childcount").trim();
                } else {
                    childcount = "";
                }
                listTemp.add(new MenuItemBean(menuItemId, menuItemPId, menuLevel, menuItemLabel, menuItemIsLeaf, menuItemUrl, menuItemDescription, menuItemOpenWindow, menuItemWindowWidth, menuItemWindowHeight, targetmachine, childcount));
            }
        } catch (Exception ex) {
            System.err.println("[" + ex + "] ");
        }
        return listTemp;
    }
}
