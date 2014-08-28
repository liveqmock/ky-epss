
package skyline.platform.form.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import skyline.platform.advance.utils.PropertyManager;
import skyline.platform.db.ConnectionManager;
import skyline.platform.db.DBUtil;
import skyline.platform.db.DatabaseConnection;
import skyline.platform.db.RecordSet;
import skyline.platform.form.config.ElementBean;
import skyline.platform.form.config.EnumerationBean;
import skyline.platform.form.config.EnumerationType;
import skyline.platform.form.util.datatype.ComponentType;

/**
 *  FORM组件的抽象类 Notes: 1.请添加所有属性的setter和getter方法
 *
 *@author     请替换
 *@created    2003年10月10日
 *@version    1.0
 */
public abstract class AbstractFormComponent {
    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年10月11日
     */
    public static Logger logger = Logger.getLogger("zt.platform.form.component.AbstractComponent");
    /**
     *  Page FORM
     *
     *@author    sun
     *@since     2003年10月11日
     */
    public final static String CSS_PAGE_FORM_TABLE_TD = "page_form_td";
    /**
     *  Page FORM
     *
     *@author    sun
     *@since     2003年10月11日
     */
    public final static String CSS_PAGE_FORM_TABLE_TITLE_TD = "page_form_title_td";
    ElementBean element = null;

    /**
     *  Constructor for the AbstractFormComponent object
     *
     *@param  element  Description of the Parameter
     */
    public AbstractFormComponent(ElementBean element) {
        this.element = element;
        int type = element.getComponetTp();
        if (type == ComponentType.CHECKBOX_TYPE ||
                type == ComponentType.LIST_TYPE ||
                type == ComponentType.RADIO_TYPE) {
            this.initValues();
        }
    }

    /**
     *  Description of the Method
     */
    private void initValues() {

        //nameset标示显示内容，valueset标示存储值
        logger.info("Init values because the list,radio,checkbox");
        int valueSetType = this.element.getValuesettype();
        String keyAndValueStr = element.getValueset();
        logger.info("keyAndValueStr is " + keyAndValueStr);
        if (valueSetType == 1) {
            String keyAndValues[] = keyAndValueStr.split("\\|");

            this.nameset = new String[keyAndValues.length];
            this.valueset = new String[keyAndValues.length];
            for (int i = 0; i < keyAndValues.length; i++) {
                logger.info("keyAndValues[" + i + "] is :" + keyAndValues[i]);
                String keyAndValue[] = keyAndValues[i].split(":");
                valueset[i] = keyAndValue[0];
                nameset[i] = keyAndValue[1];
            }
        } else if (valueSetType == 2) {
            String[] tableAndField = keyAndValueStr.split(":");
            String sql = "select " + tableAndField[1] + "," + tableAndField[2] + " from " + tableAndField[0] + " ";
            logger.info("sql is : " + sql);
            ConnectionManager manager = ConnectionManager.getInstance();
            DatabaseConnection con = manager.getConnection();
            RecordSet rs = con.executeQuery(sql);
            this.nameset = new String[rs.getRecordCount()];
            this.valueset = new String[rs.getRecordCount()];
            logger.info("RecordSet count is : " + rs.getRecordCount());
            int i = 0;
            while (rs.next()) {
                Object o1;
                Object o2;
                o1 = rs.getObject(0);
                o2 = rs.getObject(1);
                if (o1 == null || o2 == null) {
                    logger.warning("Null name or value ");
                } else {
                    valueset[i] = o1.toString().trim();
                    nameset[i] = DBUtil.fromDB(o2.toString().trim());
                }
                i++;
            }
            con.commit();
            manager.releaseConnection(con);
        } else if (valueSetType == 3) {
            ConnectionManager manager = ConnectionManager.getInstance();
            DatabaseConnection con = manager.getConnection();
            RecordSet rs = con.executeQuery(keyAndValueStr);
            this.nameset = new String[rs.getRecordCount()];
            this.valueset = new String[rs.getRecordCount()];
            int i = 0;
            while (rs.next()) {
                Object o1;
                Object o2;
                o1 = rs.getObject(0);
                o2 = rs.getObject(1);
                if (o1 == null || o2 == null) {
                    logger.warning("Null name or value ");
                } else {
                    valueset[i] = o1.toString().trim();
                    nameset[i] = DBUtil.fromDB(o2.toString().trim());
                }

                i++;
            }
            con.commit();
            manager.releaseConnection(con);
        } else if (valueSetType == 4) {
//            String enuname = element.getEnutpname();
            String enuname = keyAndValueStr;
            if (enuname == null || enuname.equals("")) {
                logger.severe("Please set the " + element.getName() + "'s enum enuname");
            } else {
                EnumerationBean enu = EnumerationType.getEnu(enuname);
                if (enu == null) {
                    logger.severe("Please config the " + element.getName() +
                            "'s enum with enuname of " + enuname);
                } else {
                    Map enus = enu.getEnu();
                    this.nameset = new String[enus.size()];
                    this.valueset = new String[enus.size()];
                    int i = 0;
                    for (Iterator iter = enus.keySet().iterator(); iter.hasNext(); ) {
                        Object key = (Object) iter.next();
                        Object value = enu.getValue(key);
                        valueset[i] = key.toString();
                        nameset[i] = value.toString();
                        i++;
                    }

                }
            }
        } else {
            logger.severe("values set type is unknown " + valueSetType);
        }
    }

    private static Map components;

    static {
        components = new HashMap();


    }

    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年10月11日
     */
    protected String[] nameset;
    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年10月11日
     */
    protected String[] valueset;

//    /**
//     * 文字对齐方向(left、right、center)
//     */
//    protected String align;
//
//    /**
//     * 边框尺寸
//     */
//    protected String border;
//
//    /**
//     * HTML组件的class值
//     */
//    protected String cClass;
//
//    /**
//     * 组件的样式风格
//     */
//    protected String style;
//
//    /**
//     * 高度
//     */
//    protected int height;
//
//    /**
//     * 宽度
//     */
//    protected int width;
//
//    /**
//     * 文字是否折行（用于textarea）
//     */
//    protected String wrap;
//
//    /**
//     * 组件的TAB键序号
//     */
//    protected String tabindex;
//
//    /**
//     * ID
//     */
//    protected String id;
//
//    /**
//     * 标题
//     */
//    protected String title;
//
//    /**
//     * 名称
//     */
//    protected String name;
//
//    /**
//     * 组件类型
//     */
//    protected String type;
//
//    /**
//     * 数据类型
//     */
//    protected String datatype;
//
//    /**
//     * 组件的值
//     */
    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年10月11日
     */
    private String[] value;

    /**
     *  Description of the Field
     *
     *@author    sun
     *@since     2003年10月11日
     */
    protected boolean readonly = false;
    protected skyline.platform.form.control.SessionContext ctx;
    private String instanceId;
    private boolean changeEvent;

    /**
     *@return     String
     *@roseuid    3F738709032D
     */
    public String toHTML() {
        return "error";
    }

    /**
     *  初始化 根据e的相关属性初始化类属性
     *
     *@param  e
     *@roseuid    3F7EA0DD0366
     */
    protected void init(ElementBean e) { }

    /**
     *  根据ElementBean的实例e构造一个AbstarctFormComponent实例，它的具体类型包括枚举（e
     *  numration）、参考性文本框(reference text)、布尔类型(boolean)
     *  文本框（text）、文本区域(textarea)、隐藏域(hidden)、复选框(checkbox)、
     *  单选按钮(radio)、下拉式列表(list)、文本标签(label)、脚本(js)、按钮（button）、
     *  提交（submit）、重置（reset）。 组件类型在ComponentType中已经定义。
     *
     *@param  e
     *@return     zt.platform.form.component.AbstractFormComponent
     *@roseuid    3F73FFE8016E
     */
    public static AbstractFormComponent getInstance(ElementBean e) {
        int type = e.getComponetTp();
//        System.out.println(e.getName()+"==="+e.getComponetTp());
        Class c = null;
        try {
            c = Class.forName((String) components.get(new Integer(type)));
            ClassLoader cl = c.getClassLoader();
            try {
                Object o[] = {e};
                AbstractFormComponent com = (AbstractFormComponent) c.getConstructors()[0].newInstance(o);
                return com;
            } catch (Exception ex1) {
                ex1.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     *  Gets the value attribute of the AbstractFormComponent object
     *
     *@return    The value value
     */
    public String[] getValues() {
        if (this.value == null) {
            String v[] = {""};
            return v;
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null) {
                value[i] = "";

            }
        }
        return value;
    }

    /**
     *  Sets the value attribute of the AbstractFormComponent object
     *
     *@param  value  The new value value
     */
    public void setValues(String[] value) {
        this.value = value;
    }

    /**
     *  Gets the valueset attribute of the AbstractFormComponent object
     *
     *@return    The valueset value
     */
    public String[] getValueset() {
        return valueset;
    }

    /**
     *  Sets the valueset attribute of the AbstractFormComponent object
     *
     *@param  valueset  The new valueset value
     */
    public void setValueset(String[] valueset) {
        this.valueset = valueset;
    }

    /**
     *  Gets the nameset attribute of the AbstractFormComponent object
     *
     *@return    The nameset value
     */
    public String[] getNameset() {
        return nameset;
    }

    /**
     *  Sets the nameset attribute of the AbstractFormComponent object
     *
     *@param  nameset  The new nameset value
     */
    public void setNameset(String[] nameset) {
        this.nameset = nameset;
    }

    /**
     *  Gets the readonly attribute of the AbstractFormComponent object
     *
     *@return    The readonly value
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     *  Sets the readonly attribute of the AbstractFormComponent object
     *
     *@param  readonly  The new readonly value
     */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public static boolean useTd() {
        if (PropertyManager.getProperty("zt.platform.component.TdSeparatorIsUsed") == null) {
            return true;
        } else {
            return PropertyManager.getProperty("zt.platform.component.TdSeparatorIsUsed").equals("true");
        }
    }

    /**
     *  Gets the header attribute of the AbstractFormComponent object
     *
     *@return    The header value
     */
    protected String getHeader() {
        String tmp = "";
        if ( !element.isIsnull() ) {
            tmp = "*";
        }
        if (useTd()) {
            String header = "<td class=\"" + CSS_PAGE_FORM_TABLE_TITLE_TD + "\" nowrap>" +
                    element.getCaption() + tmp +
                    "</td><td class=\"" + CSS_PAGE_FORM_TABLE_TD + "\" >" + element.getHeadstr();
            return header;
        } else {
            String header = "<td class=\"" + CSS_PAGE_FORM_TABLE_TD + "\" nowrap>" + element.getCaption() + tmp +" " +
                    element.getHeadstr();
            return header;
        }
    }

    /**
     *  Gets the ctx attribute of the AbstractFormComponent object
     *
     *@return    The ctx value
     */
    public skyline.platform.form.control.SessionContext getCtx() {
        return ctx;
    }

    /**
     *  Sets the ctx attribute of the AbstractFormComponent object
     *
     *@param  ctx  The new ctx value
     */
    public void setCtx(skyline.platform.form.control.SessionContext ctx) {
        this.ctx = ctx;
    }

    /**
     *  Gets the instanceId attribute of the AbstractFormComponent object
     *
     *@return    The instanceId value
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     *  Sets the instanceId attribute of the AbstractFormComponent object
     *
     *@param  instanceId  The new instanceId value
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     *  Gets the changeEvent attribute of the AbstractFormComponent object
     *
     *@return    The changeEvent value
     */
    public boolean isChangeEvent() {
        return changeEvent;
    }

    /**
     *  Sets the changeEvent attribute of the AbstractFormComponent object
     *
     *@param  changeEvent  The new changeEvent value
     */
    public void setChangeEvent(boolean changeEvent) {
        this.changeEvent = changeEvent;
    }
}
