package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.*;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.CttInfoShow;
import epss.service.CttInfoService;
import epss.service.CttItemService;
import epss.service.FlowCtrlService;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TkcttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttInfoAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private CttInfoShow cttInfoShowQry;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;
    private CttInfoShow cttInfoShowAttachment;
    private List<CttInfoShow> cttInfoShowList;

    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;
    private UploadedFile uploadedFile;

    private String strSubmitType;
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;
    //更新时检验用，符合立刻关闭，不符合进行提升
    @PostConstruct
    public void init() {
        initData();
    }
    public void initData() {
        this.attachmentList=new ArrayList<AttachmentModel>();
        this.cttInfoShowList = new ArrayList<CttInfoShow>();
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowSel = new CttInfoShow();
        cttInfoShowSel.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowDel = new CttInfoShow();
        cttInfoShowDel.setCttType(ESEnum.ITEMTYPE0.getCode());
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "";
        esFlowControl.getBackToStatusFlagList("Qry");
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE0.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "TKCTT" + esCommon.getStrToday() + "001";
            } else {
                if (strMaxId.length() > 3) {
                    String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                    if (ToolUtil.strIsDigit(strTemp)) {
                        intTemp = Integer.parseInt(strTemp);
                        intTemp = intTemp + 1;
                        strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                    } else {
                        strMaxId += "001";
                    }
                }
            }
            cttInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            if (strQryFlag.equals("Qry")) {

            } else if (strQryFlag.contains("Mng")) {
                cttInfoShowQry.setStrStatusFlagBegin(null);
                cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
            } else if (strQryFlag.contains("Check")) {
                if (strQryFlag.contains("DoubleCheck")) {
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                }
            }  else if (strQryFlag.contains("Approve")) {
                if (strQryFlag.equals("ApprovedQry")) {
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                }
            }
            cttInfoShowQry.setPeriodNo("NULL");
            this.cttInfoShowList.clear();
            cttInfoShowList = esFlowService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
            if(strQryMsgOutPara.equals("true"))  {
                if (cttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("合同信息查询失败", e);
            MessageUtil.addError("合同信息查询失败");
        }
        return null;
    }

    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return cttInfoService.getCttInfoByPkId(strPkid);
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   CttInfoShow cttInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            // 查询
            cttInfoShowPara.setCreatedByName(esCommon.getOperNameByOperId(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(esCommon.getOperNameByOperId(cttInfoShowPara.getLastUpdBy()));
            if (strPowerTypePara.equals("Qry")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            } else if (strPowerTypePara.equals("Mng")) { // 维护
                if (strSubmitTypePara.equals("Sel")) {
                    cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                }else {
                    if (strSubmitTypePara.equals("Upd")) {
                        cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                    } else if (strSubmitTypePara.equals("Del")) {
                        cttInfoShowDel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                    }
                }
            } else {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if (StringUtils.isEmpty(cttInfoShowPara.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        if (StringUtils.isEmpty(cttInfoShowPara.getSignPartA())) {
            MessageUtil.addError("请输入签订甲方！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignPartB())) {
            MessageUtil.addError("请输入签订乙方！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttStartDate())) {
            MessageUtil.addError("请输入合同开始时间！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttEndDate())) {
            MessageUtil.addError("请输入合同截止时间！");
            return false;
        }
        return true;
    }
    public void attachmentStrToList(){

        String strAttachmentTemp=cttInfoShowAttachment.getAttachment();
        if(strAttachmentTemp!=null){
            attachmentList.clear();
            if (!StringUtils.isEmpty(strAttachmentTemp)) {
                String strTemps[] = strAttachmentTemp.split(";");
                for (int i = 0; i < strTemps.length; i++) {
                AttachmentModel attachmentModelTemp = new AttachmentModel(i + "", strTemps[i], strTemps[i]);
                    attachmentList.add(attachmentModelTemp);
                }
            }
        }else{
            attachmentList.clear();
        }
    }

    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/" + attachmentModelPara.getCOLUMN_NAME());
    }

    public void delAttachmentRecordAction(AttachmentModel attachmentModelPara){
        try {
            File deletingFile = new File(attachmentModelPara.getCOLUMN_PATH());
            deletingFile.delete();
            attachmentList.remove(attachmentModelPara) ;
            StringBuffer sbTemp = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sbTemp.append(item.getCOLUMN_PATH() + ";");
            }
            cttInfoShowAttachment.setAttachment(sbTemp.toString());
            updRecordAction(cttInfoShowAttachment);
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void download(String strAttachment){
        try{
            if(StringUtils .isEmpty(strAttachment) ){
                MessageUtil.addError("路径为空，无法下载！");
                logger.error("路径为空，无法下载！");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("下载文件失败", e);
            MessageUtil.addError("下载文件失败,"+e.getMessage()+strAttachment);
        }
    }

    public void upload(FileUploadEvent event) {
        BufferedInputStream inStream = null;
        FileOutputStream fileOutputStream = null;
        UploadedFile uploadedFile = event.getFile();
        AttachmentModel attachmentModel = new AttachmentModel();
        if (uploadedFile != null) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload");
            File superFile = new File(path);
            if (!superFile.exists()) {
                superFile.mkdirs();
            }
            File descFile = new File(superFile, uploadedFile.getFileName());
            attachmentModel.setCOLUMN_ID(ToolUtil.getIntIgnoreNull(attachmentList.size()) + "");
            attachmentModel.setCOLUMN_NAME(uploadedFile.getFileName());
            attachmentModel.setCOLUMN_PATH(descFile.getAbsolutePath());
            for (AttachmentModel item : attachmentList){
                if (item.getCOLUMN_NAME().equals(attachmentModel.getCOLUMN_NAME())) {
                    MessageUtil.addError("附件已存在！");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("附件路径("+sb.toString()+")长度已超过最大允许值4000，不能入库，请联系系统管理员！");
                return;
            }
            cttInfoShowAttachment.setAttachment(sb.toString());
            updRecordAction(cttInfoShowAttachment);
            try {
                inStream = new BufferedInputStream(uploadedFile.getInputstream());
                fileOutputStream = new FileOutputStream(descFile);
                byte[] buf = new byte[1024];
                int num;
                while ((num = inStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
         if (strSubmitType.equals("Upd")) {
             if (!submitPreCheck(cttInfoShowUpd)) {
                 return;
             }
             if (cttInfoService.isExistInDb(cttInfoShowUpd)) {
                 MessageUtil.addError("该记录已存在，请重新录入！");
             } else {
                 updRecordAction(cttInfoShowUpd);
                 MessageUtil.addInfo("更新数据完成。");
             }
        } else if (strSubmitType.equals("Del")) {
            deleteRecordAction(cttInfoShowDel);
            MessageUtil.addInfo("删除数据完成。");
        }
        onQueryAction("Mng","false");
    }

    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE0.getCode());
            cttInfoService.updateRecord(cttInfoShowPara);
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE0.getCode());
            int deleteRecordNumOfCttItem= cttItemService.deleteRecord(cttInfoShowPara);
            int deleteRecordNumOfCtt= cttInfoService.deleteRecord(cttInfoShowPara.getPkid());
            int deleteRecordNumOfPower= flowCtrlService.deleteRecord(
                    cttInfoShowPara.getCttType(),
                    cttInfoShowPara.getPkid(),
                    "NULL");
            if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfPower<=0&&deleteRecordNumOfCttItem<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段 Start*/

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public CttInfoShow getCttInfoShowQry() {
        return cttInfoShowQry;
    }

    public void setCttInfoShowQry(CttInfoShow cttInfoShowQry) {
        this.cttInfoShowQry = cttInfoShowQry;
    }

    public List<CttInfoShow> getCttInfoShowList() {
        return cttInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowDel() {
        return cttInfoShowDel;
    }

    public void setCttInfoShowDel(CttInfoShow cttInfoShowDel) {
        this.cttInfoShowDel = cttInfoShowDel;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    public CttInfoShow getCttInfoShowAttachment() {
        return cttInfoShowAttachment;
    }

    public void setCttInfoShowAttachment(CttInfoShow cttInfoShowAttachment) {
        this.cttInfoShowAttachment = cttInfoShowAttachment;
    }
/*智能字段 End*/

}
