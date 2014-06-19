
package platform.view.build.form.startup;

import platform.view.build.advance.utils.PropertyManager;
import platform.view.build.form.config.*;


/**
 * @author ���滻
 * @version 1.0
 */
public class FormEngineStartup {

    /**
     * @param argv
     * @roseuid 3F73B39B0316
     */

    public static void init() {
        TableBeanManager.getTable("testtbl");
        FormBeanManager.getForm("000001");
        EnumerationType.getEnu("");
        PropertyManager.getProperty("-100");
        MemoryManager mm = new MemoryManager();
        mm.start();
    }

    public static void main(String[] argv) {
        init();
    }
}
