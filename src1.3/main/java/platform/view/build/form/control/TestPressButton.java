package platform.view.build.form.control;

import java.util.*;

import platform.view.build.db.DatabaseConnection;
import platform.view.build.form.util.FormInstance;
import platform.view.build.form.util.event.*;


public class TestPressButton extends FormActions
{
    public int buttonEvent(SessionContext ctx, DatabaseConnection conn, FormInstance instance,
                           String button,
                           ErrorMessages msgs, EventManager manager)
    {
        System.out.println("==============================");
        for ( Enumeration enu = ctx.getParameterNames() ; enu.hasMoreElements() ; ) {
            String name = (String)enu.nextElement();
            String value = ctx.getParameter(name);
            System.out.println(name+"="+value);
        }
        System.out.println("==============================");
        this.trigger(manager,instance,"000002",ctx.getParameter("id"));
        return 0;
    }

}