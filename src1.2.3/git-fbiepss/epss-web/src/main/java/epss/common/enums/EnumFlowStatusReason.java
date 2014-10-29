package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum EnumFlowStatusReason {
    FLOW_STATUS_REASON0("0","录入完毕"),
    FLOW_STATUS_REASON1("1","审核通过"),
    FLOW_STATUS_REASON2("2","审核未过"),
    FLOW_STATUS_REASON3("3","复核通过"),
    FLOW_STATUS_REASON4("4","复核未过"),
    FLOW_STATUS_REASON5("5","批准通过"),
    FLOW_STATUS_REASON6("6","批准未过"),
    FLOW_STATUS_REASON7("7","记账通过"),
    FLOW_STATUS_REASON8("8","归档成功");
    private String code = null;
    private String title = null;
    private static Hashtable<String, EnumFlowStatusReason> aliasEnums;

    EnumFlowStatusReason(String code, String title){
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

    public static EnumFlowStatusReason getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
