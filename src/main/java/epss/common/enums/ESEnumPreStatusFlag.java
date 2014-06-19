package epss.common.enums;

import java.util.Hashtable;

/**
 * ���ϵͳ ����ö��.
 * User: hanjianlong
 * Date: 2013-05-20
 * Time: 9:58:32
 * To change this template use File | Settings | File Templates.
 */
public enum ESEnumPreStatusFlag {
    PRE_STATUS_FLAG0("0","¼�����"),
    PRE_STATUS_FLAG1("1","���ͨ��"),
    PRE_STATUS_FLAG2("2","���δ��"),
    PRE_STATUS_FLAG3("3","����ͨ��"),
    PRE_STATUS_FLAG4("4","����δ��"),
    PRE_STATUS_FLAG5("5","��׼ͨ��"),
    PRE_STATUS_FLAG6("6","��׼δ��"),
    PRE_STATUS_FLAG7("7","����ͨ��"),
    PRE_STATUS_FLAG8("8","�鵵�ɹ�");
    private String code = null;
    private String title = null;
    private static Hashtable<String, ESEnumPreStatusFlag> aliasEnums;

    ESEnumPreStatusFlag(String code, String title){
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

    public static ESEnumPreStatusFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
