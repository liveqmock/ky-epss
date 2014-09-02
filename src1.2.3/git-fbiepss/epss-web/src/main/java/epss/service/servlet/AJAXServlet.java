package epss.service.servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import skyline.platform.form.config.SystemAttributeNames;
import skyline.platform.security.MenuBean;
import skyline.platform.security.OperatorManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by XIANGYANG on 2014/8/15.
 */
public class AJAXServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("AJAX请求已经到达Sevlet。。。。");
        // 解决AJAX的中文问题
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        MenuBean menuBean=new MenuBean();
        try {
            String strJson=menuBean.generateJsonStream(
                    ((OperatorManager)request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME)).getOperatorId()
                    ,"default");
            response.getWriter().write(strJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
