package epss.common.enums;

import java.util.Hashtable;

/**
 * 会计系统 公用枚举.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum EnumResType {
    RES_TYPE0("0","总包合同"),
    RES_TYPE1("1","成本计划"),
    RES_TYPE2("2","分包合同"),
    RES_TYPE3("3","分包进度工程量结算"),
    RES_TYPE4("4","分包进度材料消耗量结算"),
    RES_TYPE8("8","分包进度费用结算"),
    RES_TYPE5("5","分包进度结算单"),
    RES_TYPE6("6","总包进度工程量统计结算"),
    RES_TYPE7("7","总包进度工程量计量结算");


    private String code = null;
    private String title = null;
    private static Hashtable<String, EnumResType> aliasEnums;

    EnumResType(String code, String title){
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

    public static EnumResType getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
