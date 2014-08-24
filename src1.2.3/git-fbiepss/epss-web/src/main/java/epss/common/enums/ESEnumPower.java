package epss.common.enums;

import java.util.Hashtable;

/**
 * ���ϵͳ ����ö��.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnumPower {
    POWER_TYPE0("0","�ܰ���ͬ"),
    POWER_TYPE1("1","�ɱ��ƻ�"),
    POWER_TYPE2("2","�ְ���ͬ"),
    POWER_TYPE3("3","��������"),
    POWER_TYPE4("4","���Ͻ���"),
    POWER_TYPE5("5","�۸����"),
    POWER_TYPE6("6","����ͳ��"),
    POWER_TYPE7("7","��������");
    private String code = null;
    private String title = null;
    private static Hashtable<String, ESEnumPower> aliasEnums;

    ESEnumPower(String code, String title){
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

    public static ESEnumPower getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
