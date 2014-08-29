package epss.common.enums;

import java.util.Hashtable;

/**
 * ���ϵͳ ����ö��.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnumSubcttType {
    TYPE0("0","����"),
    TYPE1("1","����"),
    TYPE2("2","��ȫʩ����ʩ��"),
    TYPE3("3","�����Ͳ���"),
    TYPE4("4","�����Ͱ�ȫʩ����ʩ��"),
    TYPE5("5","���ϺͰ�ȫʩ����ʩ��"),
    TYPE6("6","�������ϺͰ�ȫ��ʩ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ESEnumSubcttType> aliasEnums;

    ESEnumSubcttType(String code, String title){
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

    public static ESEnumSubcttType getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}