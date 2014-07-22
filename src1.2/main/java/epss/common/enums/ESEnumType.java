package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnumType {
    TYPE0("0","数量"),
    TYPE1("1","材料"),
    TYPE2("2","安全施工措施费"),
    TYPE3("3","数量和材料"),
    TYPE4("4","数量和安全施工措施费"),
    TYPE5("5","材料和安全施工措施费"),
    TYPE6("6","材料和安全措施费");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ESEnumType> aliasEnums;

    ESEnumType(String code, String title){
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

    public static ESEnumType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
