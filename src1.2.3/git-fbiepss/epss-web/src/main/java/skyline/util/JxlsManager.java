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

import java.io.ByteArrayOutputStream;
import java.io.File;
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
        return "true";
    }

    private void outputExcel(Map beansMap, String templateFileName, String excelFilename) throws IOException {
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            if (!templateFileName.contains("progStlItemSubStlmentAccount.xls") &&
                !templateFileName.contains("progStlItemSubStlmentApprove.xls")) {
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
                ByteArrayOutputStream byteArrayOutputStreamTempQMng = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempQCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempQDoubleCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempMMng = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempMCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempMDoubleCheck = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempPApprove = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempPAct = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOutputStreamTempPFile = new ByteArrayOutputStream();
                String imagPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
                // 记账
                if (templateFileName.contains("progStlItemSubStlmentAccount.xls")) {
                    short rowNum=(Short)beansMap.get("actSubstlNum");
                    XLSTransformer transformer = new XLSTransformer();
                    is = new BufferedInputStream(new FileInputStream(templateFileName));
                    HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                    HSSFSheet sheet1 = wb.getSheet("Sheet0");
                    if (beansMap.get("qMngImagName") != null) {
                        BufferedImage bufferedImageTempQMng = getImg(imagPath, String.valueOf(beansMap.get("qMngImagName")));
                        if(bufferedImageTempQMng!=null) {
                            ImageIO.write(bufferedImageTempQMng, "jpg", byteArrayOutputStreamTempQMng);
                            HSSFClientAnchor anchorQMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 9, (short) 5, rowNum + 10);
                            HSSFPatriarch patriarchQMng = sheet1.createDrawingPatriarch();
                            patriarchQMng.createPicture(anchorQMng,
                                    wb.addPicture(byteArrayOutputStreamTempQMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qCheckImagName") != null) {
                        BufferedImage bufferedImageTempQCheck = getImg(imagPath, String.valueOf(beansMap.get("qCheckImagName")));
                        if(bufferedImageTempQCheck!=null) {
                            ImageIO.write(bufferedImageTempQCheck, "jpg", byteArrayOutputStreamTempQCheck);
                            HSSFClientAnchor anchorQCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 10, (short) 5, rowNum + 11);
                            HSSFPatriarch patriarchQCheck = sheet1.createDrawingPatriarch();
                            patriarchQCheck.createPicture(anchorQCheck,
                                    wb.addPicture(byteArrayOutputStreamTempQCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTempQDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("qDoubleCheckImagName")));
                        if(bufferedImageTempQDoubleCheck!=null) {
                            ImageIO.write(bufferedImageTempQDoubleCheck, "jpg", byteArrayOutputStreamTempQDoubleCheck);
                            HSSFClientAnchor anchorQDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 11, (short) 5, rowNum + 12);
                            HSSFPatriarch patriarchQDoubleCheck = sheet1.createDrawingPatriarch();
                            patriarchQDoubleCheck.createPicture(anchorQDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTempQDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mMngImagName") != null) {
                        BufferedImage bufferedImageTempMMng = getImg(imagPath, String.valueOf(beansMap.get("mMngImagName")));
                        if(bufferedImageTempMMng!=null) {
                            ImageIO.write(bufferedImageTempMMng, "jpg", byteArrayOutputStreamTempMMng);
                            HSSFClientAnchor anchorMMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 9, rowNum + 9, (short) 10, rowNum + 10);
                            HSSFPatriarch patriarchMMng = sheet1.createDrawingPatriarch();
                            patriarchMMng.createPicture(anchorMMng,
                                    wb.addPicture(byteArrayOutputStreamTempMMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mCheckImagName") != null) {
                        BufferedImage bufferedImageTempMCheck = getImg(imagPath, String.valueOf(beansMap.get("mCheckImagName")));
                        if(bufferedImageTempMCheck!=null) {
                            ImageIO.write(bufferedImageTempMCheck, "jpg", byteArrayOutputStreamTempMCheck);
                            HSSFClientAnchor anchorMCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 9, rowNum + 10, (short) 10, rowNum + 11);
                            HSSFPatriarch patriarchMCheck = sheet1.createDrawingPatriarch();
                            patriarchMCheck.createPicture(anchorMCheck,
                                    wb.addPicture(byteArrayOutputStreamTempMCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTempMDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("mDoubleCheckImagName")));
                        if(bufferedImageTempMDoubleCheck!=null) {
                            ImageIO.write(bufferedImageTempMDoubleCheck, "jpg", byteArrayOutputStreamTempMDoubleCheck);
                            HSSFClientAnchor anchorMDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 9, rowNum + 11, (short) 10, rowNum + 12);
                            HSSFPatriarch patriarchMDoubleCheck = sheet1.createDrawingPatriarch();
                            patriarchMDoubleCheck.createPicture(anchorMDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTempMDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("pApproveImagName") != null) {
                        BufferedImage bufferedImageTempPApprove = getImg(imagPath, String.valueOf(beansMap.get("pApproveImagName")));
                        if(bufferedImageTempPApprove!=null) {
                            ImageIO.write(bufferedImageTempPApprove, "jpg", byteArrayOutputStreamTempPApprove);
                            HSSFClientAnchor anchorPApprove =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 14, (short) 5, rowNum + 15);
                            HSSFPatriarch patriarchPApprove = sheet1.createDrawingPatriarch();
                            patriarchPApprove.createPicture(anchorPApprove,
                                    wb.addPicture(byteArrayOutputStreamTempPApprove.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("pActImagName") != null) {
                        BufferedImage bufferedImageTempPAct = getImg(imagPath, String.valueOf(beansMap.get("pActImagName")));
                        if(bufferedImageTempPAct!=null) {
                            ImageIO.write(bufferedImageTempPAct, "jpg", byteArrayOutputStreamTempPAct);
                            HSSFClientAnchor anchorPAct =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 15, (short) 5, rowNum + 16);
                            HSSFPatriarch patriarchPAct = sheet1.createDrawingPatriarch();
                            patriarchPAct.createPicture(anchorPAct,
                                    wb.addPicture(byteArrayOutputStreamTempPAct.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("pFileImagName") != null) {
                        BufferedImage bufferedImageTempPFile = getImg(imagPath, String.valueOf(beansMap.get("pFileImagName")));
                        if(bufferedImageTempPFile!=null) {
                            ImageIO.write(bufferedImageTempPFile, "jpg", byteArrayOutputStreamTempPFile);
                            HSSFClientAnchor anchorPFile =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 16, (short) 5, rowNum + 17);
                            HSSFPatriarch patriarchPFile = sheet1.createDrawingPatriarch();
                            patriarchPFile.createPicture(anchorPFile,
                                    wb.addPicture(byteArrayOutputStreamTempPFile.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    os = response.getOutputStream();
                    response.reset();
                    response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(excelFilename, "UTF-8"));
                    response.setContentType("application/msexcel");
                    wb.write(os);
                }
                // 批准
                else {
                    short rowNum=(Short)beansMap.get("progStlItemSubStlmentNum");
                    XLSTransformer transformer = new XLSTransformer();
                    is = new BufferedInputStream(new FileInputStream(templateFileName));
                    HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                    HSSFSheet sheet1 = wb.getSheet("Sheet0");
                    if (beansMap.get("qMngImagName") != null) {
                        BufferedImage bufferedImageTempQMng = getImg(imagPath, String.valueOf(beansMap.get("qMngImagName")));
                        if(bufferedImageTempQMng!=null) {
                            ImageIO.write(bufferedImageTempQMng, "jpg", byteArrayOutputStreamTempQMng);
                            HSSFClientAnchor anchorQMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 13, (short) 5, rowNum + 14);
                            HSSFPatriarch patriarchQMng = sheet1.createDrawingPatriarch();
                            patriarchQMng.createPicture(anchorQMng,
                                    wb.addPicture(byteArrayOutputStreamTempQMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qCheckImagName") != null) {
                        BufferedImage bufferedImageTempQCheck = getImg(imagPath, String.valueOf(beansMap.get("qCheckImagName")));
                        if(bufferedImageTempQCheck!=null) {
                            ImageIO.write(bufferedImageTempQCheck, "jpg", byteArrayOutputStreamTempQCheck);
                            HSSFClientAnchor anchorQCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 14, (short) 5, rowNum + 15);
                            HSSFPatriarch patriarchQCheck = sheet1.createDrawingPatriarch();
                            patriarchQCheck.createPicture(anchorQCheck,
                                    wb.addPicture(byteArrayOutputStreamTempQCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTempQDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("qDoubleCheckImagName")));
                        if(bufferedImageTempQDoubleCheck!=null) {
                            ImageIO.write(bufferedImageTempQDoubleCheck, "jpg", byteArrayOutputStreamTempQDoubleCheck);
                            HSSFClientAnchor anchorQDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 4, rowNum + 15, (short) 5, rowNum + 16);
                            HSSFPatriarch patriarchQDoubleCheck = sheet1.createDrawingPatriarch();
                            patriarchQDoubleCheck.createPicture(anchorQDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTempQDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mMngImagName") != null) {
                        BufferedImage bufferedImageTempMMng = getImg(imagPath, String.valueOf(beansMap.get("mMngImagName")));
                        if(bufferedImageTempMMng!=null) {
                            ImageIO.write(bufferedImageTempMMng, "jpg", byteArrayOutputStreamTempMMng);
                            HSSFClientAnchor anchorMMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 9, rowNum + 13, (short) 10, rowNum + 14);
                            HSSFPatriarch patriarchMMng = sheet1.createDrawingPatriarch();
                            patriarchMMng.createPicture(anchorMMng,
                                    wb.addPicture(byteArrayOutputStreamTempMMng.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mCheckImagName") != null) {
                        BufferedImage bufferedImageTempMCheck = getImg(imagPath, String.valueOf(beansMap.get("mCheckImagName")));
                        if(bufferedImageTempMCheck!=null) {
                            ImageIO.write(bufferedImageTempMCheck, "jpg", byteArrayOutputStreamTempMCheck);
                            HSSFClientAnchor anchorMCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 9, rowNum + 14, (short) 10, rowNum + 15);
                            HSSFPatriarch patriarchMCheck = sheet1.createDrawingPatriarch();
                            patriarchMCheck.createPicture(anchorMCheck,
                                    wb.addPicture(byteArrayOutputStreamTempMCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTempMDoubleCheck = getImg(imagPath, String.valueOf(beansMap.get("mDoubleCheckImagName")));
                        if(bufferedImageTempMDoubleCheck!=null) {
                            ImageIO.write(bufferedImageTempMDoubleCheck, "jpg", byteArrayOutputStreamTempMDoubleCheck);
                            HSSFClientAnchor anchorMDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 9, rowNum + 15, (short) 10, rowNum + 16);
                            HSSFPatriarch patriarchMDoubleCheck = sheet1.createDrawingPatriarch();
                            patriarchMDoubleCheck.createPicture(anchorMDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTempMDoubleCheck.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    HttpServletResponse response =
                            (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
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
            return ImageIO.read(new File(imgPathpara +File.separator+ imgNamePara));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
