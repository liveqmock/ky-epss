package epss.view.attachment;

import epss.common.utils.ApplicationContextUtil;
import epss.service.SubcttInfoService;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-4
 * Time: ÉÏÎç10:38
 * To change this template use File | Settings | File Templates.
 */
public class ShowServlet extends HttpServlet {
    private SubcttInfoService subcttInfoService;

    @Override
    public void init() throws ServletException {
        ApplicationContext ac = ApplicationContextUtil.getApplicationContext();
        subcttInfoService = (SubcttInfoService) ac.getBean("subcttInfoService");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> fileNames = new ArrayList<>();
        String attachment = subcttInfoService.getSubcttInfoByPkid(request.getParameter("strPkid")).getAttachment();
        if (attachment != null) {
            List<String> fileNamesTemp = Arrays.asList(attachment.split(";"));
            if (fileNamesTemp.size() > 0) {
                for (String fileNameTemp : fileNamesTemp) {
                    fileNames.add(fileNameTemp);
                }
            }
        }

        request.setAttribute("fileNames", fileNames);
        String operType = request.getParameter("operType");
        if(operType.equals("Mng")){
            request.getRequestDispatcher("/UI/epss/attachment/attachment_Mng.jsp").forward(request, response);
        }else if(operType.equals("Qry")){
            request.getRequestDispatcher("/UI/epss/attachment/attachment_Qry.jsp").forward(request, response);
        }
    }
}
