package epss.service.servlet;

import epss.repository.model.model_show.TaskShow;
import epss.service.ScrollInfoService;
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
import java.util.List;

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
            String strDefaultMenuUpt= null;
            ScrollInfoService scrollInfoService= (ScrollInfoService) request.getSession().getAttribute("scrollInfoService");
            String operatorId=((OperatorManager) request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME)).getOperatorId();
            List<TaskShow> taskShowTempList = scrollInfoService.getViewMsgFromTask("AjaxRequest",operatorId);
            if (taskShowTempList != null) {
                String strViewMngTemp="待处理任务：";
                for (TaskShow taskShowItem : taskShowTempList) {
                    strViewMngTemp+=taskShowItem.getName()+";";
                }
                strDefaultMenuUpt=strViewMngTemp.substring("待处理任务：".length());
            }
            if (strDefaultMenuUpt==null){
                response.getWriter().write("");
                return;
            }
            String[] strDefaultMenuUptArray=strDefaultMenuUpt.split(";");
            String strJson=menuBean.generateJsonStream(
                    ((OperatorManager)request.getSession().getAttribute(SystemAttributeNames.USER_INFO_NAME)).getOperatorId()
                    ,"default");
            JSONObject json = JSONObject.fromObject(strJson);
            JSONArray jsonArray= (JSONArray) json.get("item");
            int i=0;
            for (Object object:jsonArray){
                String strJsonTemp= ((JSONObject)object).toString();
                for (int j=i;j<strDefaultMenuUptArray.length;j++){
                    if (strDefaultMenuUptArray[j].contains(JSONObject.fromObject(strJsonTemp).get("text").toString())){
                        ((JSONObject)object).put("text",strDefaultMenuUptArray[j]);
                        i++;
                        break;
                    }
                }
            }
            response.getWriter().write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
