package epss.view.attachment;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import epss.common.utils.ApplicationContextUtil;
import epss.service.EsCttInfoService;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EsCttInfoService esCttInfoService;
    @Override
    public void init() throws ServletException {
        ApplicationContext ac = ApplicationContextUtil.getApplicationContext();
        esCttInfoService = (EsCttInfoService) ac.getBean("esCttInfoService");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String strFileName=new String(request.getParameter("fileName").getBytes("iso8859-1"),"gbk");

        // 新建一个smartUpload对象O
        SmartUpload smartUpload = new SmartUpload();
        // 初始化
        smartUpload.initialize(this.getServletConfig(), request, response);
        // 设定contentDisposition为null以禁止浏览器自动打开文件
        // 保证单击链接后是下载文件。
        smartUpload.setContentDisposition(null);
        // 下载文件
        try {
            smartUpload.downloadFile(this.getServletContext().getRealPath(
                    "/upload") + "/"
                    + new String(request.getParameter("fileName").getBytes("iso8859-1"),"gbk"));
        } catch (SmartUploadException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
