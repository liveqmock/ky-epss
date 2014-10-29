package epss.common.enums;

import java.util.Hashtable;

/**
 * ���ϵͳ ����ö��.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum EnumResType {
    RES_TYPE0("0","�ܰ���ͬ"),
    RES_TYPE1("1","�ɱ��ƻ�"),
    RES_TYPE2("2","�ְ���ͬ"),
    RES_TYPE3("3","�ְ���������"),
    RES_TYPE4("4","�ְ����Ͻ���"),
    RES_TYPE5("5","�ְ��۸����"),
    RES_TYPE6("6","�ܰ�����ͳ��"),
    RES_TYPE7("7","�ܰ���������");

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
