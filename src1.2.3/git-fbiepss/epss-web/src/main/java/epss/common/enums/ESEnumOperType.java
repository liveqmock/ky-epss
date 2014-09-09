package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnumOperType {
    OPER_Type0 ("0","增加"),
    OPER_Type1("1","更新"),
    OPER_Type2("2","删除");
    private String code = null;
    private String title = null;
    private static Hashtable<String, ESEnumOperType> aliasEnums;

    ESEnumOperType(String code, String title){
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

    public static ESEnumOperType getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
