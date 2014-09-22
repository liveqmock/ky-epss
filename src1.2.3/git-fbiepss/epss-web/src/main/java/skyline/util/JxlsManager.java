package skyline.util;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.platform.utils.PropertyManager;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

/**
 * EXCEL输出.
 * User: zhanrui
 * Date: 11-9-29
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class JxlsManager {
    private static final Logger logger = LoggerFactory.getLogger(JxlsManager.class);

     public String exportList(String filename,Map beansMap,String strFileName) {
        try {
            String reportPath = PropertyManager.getProperty("prj_root_dir");
            String templateFileName = reportPath + "/report/"+strFileName;
            outputExcel(beansMap, templateFileName, filename);
        } catch (Exception e) {
            logger.error("报表处理错误！", e);
            throw new RuntimeException("报表处理错误！", e);
        }
        return null;
    }

    private void outputExcel(Map beansMap, String templateFileName, String excelFilename) throws IOException {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            XLSTransformer transformer = new XLSTransformer();
            is = new BufferedInputStream(new FileInputStream(templateFileName));
            HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(excelFilename, "UTF-8"));
            response.setContentType("application/msexcel");
            wb.write(os);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    private void outputExcel1(Map beansMap, String templateFileName, String excelFilename) throws IOException {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            if (!templateFileName.contains("actSubstl.xls") && !templateFileName.contains("progStlItemSubStlment.xls")) {
                XLSTransformer transformer = new XLSTransformer();
                is = new BufferedInputStream(new FileInputStream(templateFileName));
                HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                os = response.getOutputStream();
                response.reset();
                response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(excelFilename, "UTF-8"));
                response.setContentType("application/msexcel");
                wb.write(os);
            } else {//结算单上实现签名图片功能，图片的位置取决于beansMap.size()及表头所占的列数
                ByteArrayOutputStream byteArrayOutQMng = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutQCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutQDoubleCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutMMng = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutMCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutMDoubleCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutPApprove = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutPAct = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutPFile = new ByteArrayOutputStream();
                String imagPath = PropertyManager.getProperty("prj_root_dir") + "/signature/";
                if (templateFileName.contains("actSubstl.xls")) {
                    XLSTransformer transformer = new XLSTransformer();
                    is = new BufferedInputStream(new FileInputStream(templateFileName));
                    HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                    HSSFSheet sheet1 = wb.getSheet("Sheet0");
                    HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
                    if (beansMap.get("qMngImagName") != null) {
                        BufferedImage bufferImgQMng = getImg(imagPath, String.valueOf(beansMap.get("qMngImagName")));
                        ImageIO.write(bufferImgQMng, "png", byteArrayOutQMng);
                        HSSFClientAnchor anchorQMng = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 9, (short) 4, beansMap.size() - 8);
                        patriarch.createPicture(anchorQMng, wb.addPicture(byteArrayOutQMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("qCheckImagName") != null) {
                        BufferedImage bufferImgQCheck = getImg(imagPath, String.valueOf(beansMap.get("qCheckImagName")));
                        ImageIO.write(bufferImgQCheck, "png", byteArrayOutQCheck);
                        HSSFClientAnchor anchorQCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 8, (short) 4, beansMap.size() - 7);
                        patriarch.createPicture(anchorQCheck, wb.addPicture(byteArrayOutQMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("qDoubleCheckImagName") != null) {
                        BufferedImage bufferImgQDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("qDoubleCheckImagName")));
                        ImageIO.write(bufferImgQDoubleCheck, "png", byteArrayOutQDoubleCheck);
                        HSSFClientAnchor anchorQDoubleCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 7, (short) 4, beansMap.size() - 6);
                        patriarch.createPicture(anchorQDoubleCheck, wb.addPicture(byteArrayOutQDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("mMngImagName") != null) {
                        BufferedImage bufferImgMMng = getImg(imagPath, String.valueOf(beansMap.get("mMngImagName")));
                        ImageIO.write(bufferImgMMng, "png", byteArrayOutMMng);
                        HSSFClientAnchor anchorMMng = new HSSFClientAnchor(0, 0, 2, 2, (short) 10, beansMap.size() - 9, (short) 9, beansMap.size() - 8);
                        patriarch.createPicture(anchorMMng, wb.addPicture(byteArrayOutMMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("mCheckImagName") != null) {
                        BufferedImage bufferImgMCheck = getImg(imagPath, String.valueOf(beansMap.get("mCheckImagName")));
                        ImageIO.write(bufferImgMCheck, "png", byteArrayOutMCheck);
                        HSSFClientAnchor anchorMCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 10, beansMap.size() - 8, (short) 9, beansMap.size() - 7);
                        patriarch.createPicture(anchorMCheck, wb.addPicture(byteArrayOutMCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("mDoubleCheckImagName") != null) {
                        BufferedImage bufferImgMDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("mDoubleCheckImagName")));
                        ImageIO.write(bufferImgMDoubleCheck, "png", byteArrayOutMDoubleCheck);
                        HSSFClientAnchor anchorMDoubleCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 10, beansMap.size() - 7, (short) 9, beansMap.size() - 6);
                        patriarch.createPicture(anchorMDoubleCheck, wb.addPicture(byteArrayOutMDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("pApproveImagName") != null) {
                        BufferedImage bufferImgPApprove = getImg(imagPath, String.valueOf(beansMap.get("pApproveImagName")));
                        ImageIO.write(bufferImgPApprove, "png", byteArrayOutPApprove);
                        HSSFClientAnchor anchorPApprove = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 4, (short) 4, beansMap.size() - 3);
                        patriarch.createPicture(anchorPApprove, wb.addPicture(byteArrayOutPApprove.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("pActImagName") != null) {
                        BufferedImage bufferImgPAct = getImg(imagPath, String.valueOf(beansMap.get("pActImagName")));
                        ImageIO.write(bufferImgPAct, "png", byteArrayOutPAct);
                        HSSFClientAnchor anchorPAct = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 3, (short) 4, beansMap.size() - 2);
                        patriarch.createPicture(anchorPAct, wb.addPicture(byteArrayOutPAct.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("pFileCheckImagName") != null) {
                        BufferedImage bufferImgPFile = getImg(imagPath, String.valueOf(beansMap.get("pFileCheckImagName")));
                        ImageIO.write(bufferImgPFile, "png", byteArrayOutPFile);
                        HSSFClientAnchor anchorPFile = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 2, (short) 4, beansMap.size() - 1);
                        patriarch.createPicture(anchorPFile, wb.addPicture(byteArrayOutPFile.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    os = response.getOutputStream();
                    response.reset();
                    response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(excelFilename, "UTF-8"));
                    response.setContentType("application/msexcel");
                    wb.write(os);
                } else {
                    XLSTransformer transformer = new XLSTransformer();
                    is = new BufferedInputStream(new FileInputStream(templateFileName));
                    HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                    HSSFSheet sheet1 = wb.getSheet("Sheet0");
                    HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
                    if (beansMap.get("qMngImagName") != null) {
                        BufferedImage bufferImgQMng = getImg(imagPath, String.valueOf(beansMap.get("qMngImagName")));
                        ImageIO.write(bufferImgQMng, "png", byteArrayOutQMng);
                        HSSFClientAnchor anchorQMng = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 5, (short) 4, beansMap.size() - 4);
                        patriarch.createPicture(anchorQMng, wb.addPicture(byteArrayOutQMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("qCheckImagName") != null) {
                        BufferedImage bufferImgQCheck = getImg(imagPath, String.valueOf(beansMap.get("qCheckImagName")));
                        ImageIO.write(bufferImgQCheck, "png", byteArrayOutQCheck);
                        HSSFClientAnchor anchorQCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 4, (short) 4, beansMap.size() - 3);
                        patriarch.createPicture(anchorQCheck, wb.addPicture(byteArrayOutQCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("qDoubleCheckImagName") != null) {
                        BufferedImage bufferImgQDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("qDoubleCheckImagName")));
                        ImageIO.write(bufferImgQDoubleCheck, "png", byteArrayOutQDoubleCheck);
                        HSSFClientAnchor anchorQDoubleCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, beansMap.size() - 3, (short) 4, beansMap.size() - 2);
                        patriarch.createPicture(anchorQDoubleCheck, wb.addPicture(byteArrayOutQDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("mMngImagName") != null) {
                        BufferedImage bufferImgMMng = getImg(imagPath, String.valueOf(beansMap.get("mMngImagName")));
                        ImageIO.write(bufferImgMMng, "png", byteArrayOutMMng);
                        HSSFClientAnchor anchorMMng = new HSSFClientAnchor(0, 0, 2, 2, (short) 10, beansMap.size() - 5, (short) 9, beansMap.size() - 4);
                        patriarch.createPicture(anchorMMng, wb.addPicture(byteArrayOutMMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("mCheckImagName") != null) {
                        BufferedImage bufferImgMCheck = getImg(imagPath, String.valueOf(beansMap.get("mCheckImagName")));
                        ImageIO.write(bufferImgMCheck, "png", byteArrayOutMCheck);
                        HSSFClientAnchor anchorMCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 10, beansMap.size() - 4, (short) 9, beansMap.size() - 3);
                        patriarch.createPicture(anchorMCheck, wb.addPicture(byteArrayOutMCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    if (beansMap.get("mDoubleCheckImagName") != null) {
                        BufferedImage bufferImgMDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("mDoubleCheckImagName")));
                        ImageIO.write(bufferImgMDoubleCheck, "png", byteArrayOutMDoubleCheck);
                        HSSFClientAnchor anchorMDoubleCheck = new HSSFClientAnchor(0, 0, 2, 2, (short) 10, beansMap.size() - 3, (short) 9, beansMap.size() - 2);
                        patriarch.createPicture(anchorMDoubleCheck, wb.addPicture(byteArrayOutMDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    os = response.getOutputStream();
                    response.reset();
                    response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(excelFilename, "UTF-8"));
                    response.setContentType("application/msexcel");
                    wb.write(os);
                }
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
    private BufferedImage getImg(String imgPathpara, String imgNamePara) {
        try {
            return ImageIO.read(new File(imgPathpara + imgNamePara));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
