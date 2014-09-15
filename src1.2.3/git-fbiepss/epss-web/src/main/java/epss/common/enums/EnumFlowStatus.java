package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum EnumFlowStatus {
    FLOW_STATUS0("0","录入"),
    FLOW_STATUS1("1","审核"),
    FLOW_STATUS2("2","复核"),
    FLOW_STATUS3("3","批准"),
    FLOW_STATUS4("4","记账"),
    FLOW_STATUS5("5","归档");
    private String code = null;
    private String title = null;
    private static Hashtable<String, EnumFlowStatus> aliasEnums;

    EnumFlowStatus(String code, String title){
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

    public static EnumFlowStatus getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
