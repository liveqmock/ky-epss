package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnum {
    ITEMTYPE0("0","总包合同"),
    ITEMTYPE1("1","成本计划"),
    ITEMTYPE2("2","分包合同"),
    ITEMTYPE3("3","分包数量结算"),
    ITEMTYPE4("4","分包材料结算"),
    ITEMTYPE5("5","分包价格结算"),
    ITEMTYPE6("6","总包数量统计"),
    ITEMTYPE7("7","总包数量计量");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ESEnum> aliasEnums;

    ESEnum(String code, String title){
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

    public static ESEnum getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
