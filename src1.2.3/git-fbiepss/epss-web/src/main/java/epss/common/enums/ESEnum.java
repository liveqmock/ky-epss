package epss.common.enums;

import java.util.Hashtable;

/**
 * ���ϵͳ ����ö��.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnum {
    ITEMTYPE0("0","�ܰ���ͬ"),
    ITEMTYPE1("1","�ɱ��ƻ�"),
    ITEMTYPE2("2","�ְ���ͬ"),
    ITEMTYPE3("3","�ְ���������"),
    ITEMTYPE4("4","�ְ����Ͻ���"),
    ITEMTYPE5("5","�ְ��۸����"),
    ITEMTYPE6("6","�ܰ�����ͳ��"),
    ITEMTYPE7("7","�ܰ���������");

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
