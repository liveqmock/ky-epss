package epss.view.attachment;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import epss.common.utils.ApplicationContextUtil;
import epss.repository.model.EsCttInfo;
import epss.service.EsCttInfoService;
import org.springframework.context.ApplicationContext;

public class UploadServlet extends HttpServlet {

    private EsCttInfoService esCttInfoService;

    @Override
    public void init() throws ServletException {
        ApplicationContext ac = ApplicationContextUtil.getApplicationContext();
        esCttInfoService = (EsCttInfoService) ac.getBean("esCttInfoService");
    }

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // �½�һ��jsmartUpLoad����
        SmartUpload smartUpload = new SmartUpload();
        // �ϴ���ʼ��

        smartUpload.initialize(this.getServletConfig(), request, response);
        try {
            // �趨�ϴ�����
            // ����ÿ���ϴ��ļ�����󳤶�;������趨Ϊ1024*1024*20
            smartUpload.setMaxFileSize(1024 * 1024 * 10);
            // �������ϴ����ݵĳ���
            smartUpload.setTotalMaxFileSize(1024 * 1024 * 20);
            // ���������ϴ����ļ����͡�����doc��txt��bat�ļ�
            smartUpload.setAllowedFilesList("jpg,png,gif,pdf,doc,xls,docx,PDF");
            // ���ƽ�ֹ�ϴ����ļ�����,��ֹexe��jsp����û����չ�����ļ�
            smartUpload.setDeniedFilesList("exe,jsp,html,");
            // �ϴ��ļ�
            smartUpload.upload();
            // ���ļ����浽ָ����Ŀ¼��
            File file = new File(this.getServletContext().getRealPath("/upload"));
            if (!file.exists()) {
                file.mkdirs();
            }

            List<String> fileNames = new ArrayList<>();

            /*�������ݿ��ȡsqlSession*/
            String strPkid= request.getParameter("strPkid");
            EsCttInfo esCttInfo = esCttInfoService.getCttInfoByPkId(strPkid);
            String strAttachment = esCttInfo.getAttachment();

            StringBuffer sb = null;
            if (strAttachment != null){
                List<String> fileNamesTemp = Arrays.asList(strAttachment.split(";"));
                if (fileNamesTemp.size() > 0) {
                    for (String fileNameTemp : fileNamesTemp) {
                        fileNames.add(fileNameTemp);
                    }
                }
                sb = new StringBuffer(strAttachment);
            }else{
                sb = new StringBuffer();
            }
            com.jspsmart.upload.Files files = smartUpload.getFiles();
            int fileCount = files.getCount();
            for (int i = 0; fileCount > 0 && i < fileCount; i++) {
                String fileName = files.getFile(i).getFileName();
                if (!fileName.equals("") && !fileNames.contains(fileName)) {
                    smartUpload.save(file.getAbsolutePath());
                    fileNames.add(files.getFile(i).getFileName());
                    sb.append(fileName).append(";");
                }
            }

            esCttInfo.setAttachment(sb.toString());
            esCttInfoService.updateRecord(esCttInfo);

            request.setAttribute("fileNames", fileNames);
            request.getRequestDispatcher("/UI/epss/attachment/attachment_Mng.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SmartUploadException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
