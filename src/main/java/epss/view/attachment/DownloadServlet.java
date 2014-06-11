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

        // �½�һ��smartUpload����O
        SmartUpload smartUpload = new SmartUpload();
        // ��ʼ��
        smartUpload.initialize(this.getServletConfig(), request, response);
        // �趨contentDispositionΪnull�Խ�ֹ������Զ����ļ�
        // ��֤�������Ӻ��������ļ���
        smartUpload.setContentDisposition(null);
        // �����ļ�
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
