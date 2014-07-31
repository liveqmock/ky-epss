package epss.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextUtil {

    private static ApplicationContext applicationContext;
    static {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //applicationContext = new FileSystemXmlApplicationContext("WebRoot/WEB-INF/applicationContext.xml");
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

