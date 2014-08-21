package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.CttInfoShow;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.repository.model.EsInitStl;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;;

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
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class CstplInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplInfoAction.class);
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
    private String strNotPassToStatus;
    private CttInfoShow cttInfoShowSelected;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowUpd;
    private List<CttInfoShow> cttInfoShowList;

    private String strSubmitType;
    private String rowSelectedFlag;

    // ����֮�䴫�ݹ����Ĳ���
    private String strBelongToPkid;

    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;
    //����
    private CttInfoShow cttInfoShowAttachment;
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //�ϴ������ļ�
    private StreamedContent downloadFile;
    private UploadedFile uploadedFile;


    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // ���ܰ���ͬ���ݹ������ܰ���ͬ��
        if (parammap.containsKey("strTkcttInfoPkid")) {
            strBelongToPkid = parammap.get("strTkcttInfoPkid").toString();
        }
        initData();
    }
    public void initData() {
        this.attachmentList=new ArrayList<AttachmentModel>();
        this.cttInfoShowList = new ArrayList<CttInfoShow>();
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowQry.setParentPkid(strBelongToPkid);
        cttInfoShowSel = new CttInfoShow();
        cttInfoShowSel.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowSel.setParentPkid(strBelongToPkid);
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowUpd.setParentPkid(strBelongToPkid);
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "";
        esFlowControl.getBackToStatusFlagList("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE1.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "CSTPL" + esCommon.getStrToday() + "001";
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
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
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
            this.cttInfoShowList.clear();
            cttInfoShowQry.setParentPkid(strBelongToPkid);
            cttInfoShowList = esFlowService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (cttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
            rowSelectedFlag = "false";
        } catch (Exception e) {
            logger.error("��ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��ͬ��Ϣ��ѯʧ��");
        }
        return null;
    }

    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return cttInfoService.getCttInfoByPkId(strPkid);
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   CttInfoShow cttInfoShowSelected) {
        try {
            strSubmitType = strSubmitTypePara;
            cttInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(cttInfoShowSelected.getCreatedBy()));
            cttInfoShowSelected.setLastUpdByName(esCommon.getOperNameByOperId(cttInfoShowSelected.getLastUpdBy()));
            // ��ѯ
            if (strPowerTypePara.equals("Qry")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
            } else// ά��
            if (strPowerTypePara.equals("Mng")) {
                if (strSubmitTypePara.equals("Sel")) {
                    cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                    rowSelectedFlag = "true";
                }else if (strSubmitTypePara.equals("Upd")) {
                        cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                        rowSelectedFlag = "false";
                }
            } else {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                rowSelectedFlag = "true";
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShow) {
        if (StringUtils.isEmpty(cttInfoShow.getId())) {
            MessageUtil.addError("�������ͬ�ţ�");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShow.getName())) {
            MessageUtil.addError("�������ͬ����");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShow.getSignDate())) {
            MessageUtil.addError("������ǩ�����ڣ�");
            return false;
        }
        return true;
    }

    /**
     * ����Ȩ�޽������
     *
     * @param strPowerTypePara
     */
    public void onClickForPowerAction(String strPowerTypePara) {
        try {
            String strPowerTypeTemp="";
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    esFlowControl.mngFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    esFlowControl.mngNotFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // ԭ�����ͨ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    flowCtrlService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // ԭ�����δ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    flowCtrlService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
                strPowerTypeTemp="Check";
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // ԭ�򣺸���ͨ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    flowCtrlService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    cttInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    flowCtrlService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
                strPowerTypeTemp="DoubleCheck";
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // ԭ����׼ͨ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    flowCtrlService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����Ƿ�ʹ��
                    String strCttTypeTemp = "";
                    if (cttInfoShowSel.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE1.getCode();
                    } else if (cttInfoShowSel.getCttType().equals(ESEnum.ITEMTYPE1.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE2.getCode();
                    }

                    // ����д����ʵ��Խ���˻�
                    cttInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // ԭ����׼δ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());

                    flowCtrlService.updateRecordByCtt(cttInfoShowSel);

                    List<EsInitStl> esInitStlListTemp =
                            esFlowService.selectIsUsedInQMPBySubcttPkid(cttInfoShowSel.getPkid());
                    if (esInitStlListTemp.size() > 0) {
                        MessageUtil.addInfo("�������Ѿ���["
                                + ESEnum.valueOfAlias(esInitStlListTemp.get(0).getStlType()).getTitle()
                                + "]ʹ�ã�������׼δ��,�����ر༭��");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cttInfoShowSel.getPkid()) > 0) {
                            MessageUtil.addInfo("�������Ѿ���[" + ESEnum.valueOfAlias(strCttTypeTemp).getTitle()
                                    + "]ʹ�ã�������׼δ��,�����ر༭��");
                        } else {
                            MessageUtil.addInfo("������׼δ����");
                        }
                    }
                }
                strPowerTypeTemp="Approve";
            }
            // ���²�ѯ���Ѳ����Ľ��
            onQueryAction(strPowerTypeTemp,"false");
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * �ύά��Ȩ��
     *
     * @param
     */
    public void onClickForMngAction() {
            if (!submitPreCheck(cttInfoShowUpd)) {
                return;
            }
                cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE1.getCode());
                updRecordAction(cttInfoShowUpd);
                onQueryAction("Mng","false");
    }
    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoService.updateRecord(cttInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    //������ط���
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
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void download(String strAttachment){
        try{
            if(StringUtils .isEmpty(strAttachment) ){
                MessageUtil.addError("·��Ϊ�գ��޷����أ�");
                logger.error("·��Ϊ�գ��޷����أ�");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("�����ļ�ʧ��", e);
            MessageUtil.addError("�����ļ�ʧ��,"+e.getMessage()+strAttachment);
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
                    MessageUtil.addError("�����Ѵ��ڣ�");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("����·��("+sb.toString()+")�����ѳ����������ֵ4000��������⣬����ϵϵͳ����Ա��");
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

    /*�����ֶ� Start*/
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
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

    public CttInfoShow getCttInfoShowSelected() {
        return cttInfoShowSelected;
    }

    public void setCttInfoShowSelected(CttInfoShow cttInfoShowSelected) {
        this.cttInfoShowSelected = cttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
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

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
    }

    public CttInfoShow getCttInfoShowAttachment() {
        return cttInfoShowAttachment;
    }

    public void setCttInfoShowAttachment(CttInfoShow cttInfoShowAttachment) {
        this.cttInfoShowAttachment = cttInfoShowAttachment;
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
    /*�����ֶ� End*/
}
