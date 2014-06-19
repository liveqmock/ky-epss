package epss.view.attachment;

import epss.common.utils.ApplicationContextUtil;
import epss.repository.model.SubcttInfo;
import epss.service.SubcttInfoService;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SubcttInfoService subcttInfoService;
    @Override
    public void init() throws ServletException {
        ApplicationContext ac = ApplicationContextUtil.getApplicationContext();
        subcttInfoService = (SubcttInfoService) ac.getBean("subcttInfoService");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String strFileName=new String(request.getParameter("fileName").getBytes("iso8859-1"),"gbk");

        File file = new File(this.getServletContext().getRealPath("/upload") + "/" + strFileName);
        file.delete();
        String attachment =  subcttInfoService.getSubcttInfoByPkid(request.getParameter("strPkid")).getAttachment();
        List<String> fileNames =  new ArrayList<>();
        if (attachment != null){
            List<String> fileNamesTemp = Arrays.asList(attachment.split(";"));
            if (fileNamesTemp.size() > 0) {
                for (String fileNameTemp : fileNamesTemp) {
                    fileNames.add(fileNameTemp);
                }
            }
        }
        fileNames.remove(file.getName());

        StringBuffer sb = new StringBuffer();
        for (String fileNameTemp : fileNames) {
            sb.append(fileNameTemp).append(";");
        }

        String strPkid= request.getParameter("strPkid");
        SubcttInfo cttInfo = subcttInfoService.getSubcttInfoByPkid(strPkid);
        cttInfo.setAttachment(sb.toString());
        subcttInfoService.updateRecord(cttInfo);

        request.setAttribute("fileNames", fileNames);
        request.getRequestDispatcher("/UI/epss/attachment/attachment_Mng.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
