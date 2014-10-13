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
                ByteArrayOutputStream byteArrayOutputStreamTemp = new ByteArrayOutputStream();
                String imagPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");                
                if (templateFileName.contains("progStlItemSubStlmentAccount.xls")) {
                    short rowNum=(Short)beansMap.get("actSubstlNum");
                    XLSTransformer transformer = new XLSTransformer();
                    is = new BufferedInputStream(new FileInputStream(templateFileName));
                    HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                    HSSFSheet sheet1 = wb.getSheet("Sheet0");
                    HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
                    if (beansMap.get("qMngImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("qMngImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorQMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 9, (short) 4, rowNum + 10);
                            patriarch.createPicture(anchorQMng,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("qCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorQCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 10, (short) 4, rowNum + 11);
                            patriarch.createPicture(anchorQCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("qDoubleCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorQDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 11, (short) 4, rowNum + 12);
                            patriarch.createPicture(anchorQDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mMngImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("mMngImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorMMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 10, rowNum + 9, (short) 9, rowNum + 10);
                            patriarch.createPicture(anchorMMng,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("mCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorMCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 10, rowNum + 10, (short) 9, rowNum + 11);
                            patriarch.createPicture(anchorMCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("mDoubleCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorMDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 10, rowNum + 11, (short) 9, rowNum + 12);
                            patriarch.createPicture(anchorMDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("pApproveImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("pApproveImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorPApprove =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 14, (short) 4, rowNum + 15);
                            patriarch.createPicture(anchorPApprove,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("pActImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("pActImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorPAct =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 15, (short) 4, rowNum + 16);
                            patriarch.createPicture(anchorPAct,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("pFileImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("pFileImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorPFile = new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 16, (short) 4, rowNum + 17);
                            patriarch.createPicture(anchorPFile,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    os = response.getOutputStream();
                    response.reset();
                    response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(excelFilename, "UTF-8"));
                    response.setContentType("application/msexcel");
                    wb.write(os);
                } else {
                    short rowNum=(Short)beansMap.get("progStlItemSubStlmentNum");
                    XLSTransformer transformer = new XLSTransformer();
                    is = new BufferedInputStream(new FileInputStream(templateFileName));
                    HSSFWorkbook wb = (HSSFWorkbook) transformer.transformXLS(is, beansMap);
                    HSSFSheet sheet1 = wb.getSheet("Sheet0");
                    HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
                    if (beansMap.get("qMngImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("qMngImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorQMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 13, (short) 4, rowNum + 14);
                            patriarch.createPicture(anchorQMng,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("qCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorQCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 14, (short) 4, rowNum + 15);
                            patriarch.createPicture(anchorQCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("qDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("qDoubleCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorQDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 5, rowNum + 15, (short) 4, rowNum + 16);
                            patriarch.createPicture(anchorQDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mMngImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("mMngImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorMMng =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 10, rowNum + 13, (short) 9, rowNum + 14);
                            patriarch.createPicture(anchorMMng,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("mCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorMCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 10, rowNum + 14, (short) 9, rowNum + 15);
                            patriarch.createPicture(anchorMCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                    }
                    if (beansMap.get("mDoubleCheckImagName") != null) {
                        BufferedImage bufferedImageTemp = getImg(imagPath, String.valueOf(beansMap.get("mDoubleCheckImagName")));
                        if(bufferedImageTemp!=null) {
                            ImageIO.write(bufferedImageTemp, "png", byteArrayOutputStreamTemp);
                            HSSFClientAnchor anchorMDoubleCheck =
                                    new HSSFClientAnchor(0, 0, 2, 2, (short) 10, rowNum + 15, (short) 9, rowNum + 16);
                            patriarch.createPicture(anchorMDoubleCheck,
                                    wb.addPicture(byteArrayOutputStreamTemp.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
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
            return ImageIO.read(new File(imgPathpara +"/"+ imgNamePara));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
