package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum EnumFlowStatusRemark {
    FLOW_STATUS_REMARK0("0","录入完毕"),
    FLOW_STATUS_REMARK1("1","审核通过"),
    FLOW_STATUS_REMARK2("2","审核未过"),
    FLOW_STATUS_REMARK3("3","复核通过"),
    FLOW_STATUS_REMARK4("4","复核未过"),
    FLOW_STATUS_REMARK5("5","批准通过"),
    FLOW_STATUS_REMARK6("6","批准未过");
    private String code = null;
    private String title = null;
    private static Hashtable<String, EnumFlowStatusRemark> aliasEnums;

    EnumFlowStatusRemark(String code, String title){
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static EnumFlowStatusRemark valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
