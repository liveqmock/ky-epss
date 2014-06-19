package epss.view.settle;

import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusRemark;
import epss.repository.model.ProgWorkqtyInfo;
import epss.repository.model.SubcttInfo;
import epss.repository.model.model_show.ProgWorkqtyInfoShow;
import epss.repository.model.model_show.SubcttInfoShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.service.*;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
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
public class ProgWorkqtyInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgWorkqtyInfoAction.class);
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;

    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgWorkqtyInfoShow progWorkqtyInfoShowQry;
    private String strNotPassToStatus;
    private ProgWorkqtyInfoShow progWorkqtyInfoShowSelected;
    private ProgWorkqtyInfoShow progWorkqtyInfoShowSel;
    private ProgWorkqtyInfoShow progWorkqtyInfoShowAdd;
    private ProgWorkqtyInfoShow progWorkqtyInfoShowUpd;
    private ProgWorkqtyInfoShow progWorkqtyInfoShowDel;

    private List<ProgWorkqtyInfoShow> progWorkqtyInfoShowList;

    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String rowSelectedFlag;
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progWorkqtyInfoShowList = new ArrayList<ProgWorkqtyInfoShow>();
        String strCstplInfoPkid="";
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strCstplInfoPkid")){
            strCstplInfoPkid=parammap.get("strCstplInfoPkid").toString();
        }
        resetAction();

        SubcttInfoShow subcttInfoTemp=new SubcttInfoShow();
        subcttInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        subcttInfoTemp.setCstplInfoPkid(strCstplInfoPkid);
        List<SubcttInfo> subcttInfoList =
                subcttInfoService.getSubcttInfoListByModel(subcttInfoTemp);
        subcttList=new ArrayList<SelectItem>();
        if(subcttInfoList.size()>0){
            SelectItem selectItem=new SelectItem("","ȫ��");
            subcttList.add(selectItem);
            for(SubcttInfo itemUnit: subcttInfoList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progWorkqtyInfoShowQry = new ProgWorkqtyInfoShow();
        progWorkqtyInfoShowSel = new ProgWorkqtyInfoShow();
        progWorkqtyInfoShowAdd = new ProgWorkqtyInfoShow();
        progWorkqtyInfoShowUpd = new ProgWorkqtyInfoShow();
        progWorkqtyInfoShowDel = new ProgWorkqtyInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag="false";
    }

    public void resetActionForAdd(){
        progWorkqtyInfoShowAdd =new ProgWorkqtyInfoShow();
        strSubmitType="Add";
        rowSelectedFlag="false";
    }

    public void setMaxNoPlusOne(){
        try {
            Integer intTemp;
            String strMaxId= progWorkqtyInfoService.getStrMaxProgWorkqtyInfoId();
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLM"+ esCommon.getStrToday()+"001";
            }
            else{
                if(strMaxId .length()>3){
                    String strTemp=strMaxId.substring(strMaxId .length() -3).replaceFirst("^0+","");
                    if(ToolUtil.strIsDigit(strTemp)){
                        intTemp=Integer.parseInt(strTemp) ;
                        intTemp=intTemp+1;
                        strMaxId=strMaxId.substring(0,strMaxId.length()-3)+StringUtils.leftPad(intTemp.toString(),3,"0");
                    }else{
                        strMaxId+="001";
                    }
                }
            }
            if("Add".equals(strSubmitType)) {
                progWorkqtyInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                progWorkqtyInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    public void onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            switch (strQryFlag){
                case "Qry":
                    progWorkqtyInfoShowQry.setFlowStatusBegin("");
                    progWorkqtyInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                    break;
                case "MngPass":
                    break;
                case "MngFail":
                    progWorkqtyInfoShowQry.setFlowStatusBegin("");
                    progWorkqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    progWorkqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progWorkqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    progWorkqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    progWorkqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    progWorkqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    progWorkqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "Print":
                    progWorkqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    progWorkqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.progWorkqtyInfoShowList.clear();
            List<ProgWorkqtyInfoShow> progWorkqtyInfoShowTemp =
                    progWorkqtyInfoService.getProgWorkqtyInfoListByFlowStatusBegin_End(progWorkqtyInfoShowQry);
            for(ProgWorkqtyInfoShow esISSOMPCUnit: progWorkqtyInfoShowTemp){
                for(SelectItem itemUnit:subcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getSubcttInfoPkid())){
                        progWorkqtyInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progWorkqtyInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progWorkqtyInfoShowSelected.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInFlowStatuslist(progWorkqtyInfoShowSelected.getFlowStatus());
            progWorkqtyInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(progWorkqtyInfoShowSelected.getCreatedBy()));
            progWorkqtyInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(progWorkqtyInfoShowSelected.getUpdatedBy()));

            switch (strPowerTypePara){
                case "Qry":// ��ѯ
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                    break;
                case "Mng":// ά��
                    if(strSubmitTypePara.equals("Sel")){
                        progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                        rowSelectedFlag="true";
                    }else if(strSubmitTypePara.equals("Add")){
                    }else{
                        if(!strStatusFlagCode.equals("")&&
                                !strStatusFlagCode.equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                            MessageUtil.addInfo("�����Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                            return;
                        }
                        if(strSubmitTypePara.equals("Upd")){
                            rowSelectedFlag="false";
                            progWorkqtyInfoShowUpd =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                        }else if(strSubmitTypePara.equals("Del")){
                            rowSelectedFlag="false";
                            progWorkqtyInfoShowDel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                        }
                    }
                    break;
                case "Check": // Ȩ�޿���
                    rowSelectedFlag="true";
                    //�������̻���,��ʾ��ͬ���˻ص�״̬
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("�������ݻ�δ¼����ϣ�����ʱ���ܽ�����˲�����");
                    }else if(!strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS0.getCode())&&!
                            strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS1.getCode())){
                        MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                    }
                    break;
                case "DoubleCheck":
                    rowSelectedFlag="true";
                    //�������̻���,��ʾ��ͬ���˻ص�״̬
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("�������ݻ�δ¼����ϣ�����ʱ���ܽ��и��˲�����");
                    }else if(strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS0.getCode())){
                        MessageUtil.addInfo("�������ݸո�¼�룬��δ��ˣ�����ʱ���ܽ��и��ˣ�");
                    }else if(!strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS1.getCode())&&!
                            strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS2.getCode())){
                        MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                    }
                    break;
                case "Approve":
                    rowSelectedFlag="true";
                    //�������̻���,��ʾ��ͬ���˻ص�״̬
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("�������ݻ�δ¼����ϣ�����ʱ���ܽ�����׼������");
                    }else if(strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS0.getCode())){
                        MessageUtil.addInfo("�������ݸո�¼�룬��δ��ˣ�����ʱ���ܽ�����׼��");
                    }else if(!strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS2.getCode())&&!
                            strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS3.getCode())){
                        MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                    }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
    */
    private boolean submitPreCheck(ProgWorkqtyInfoShow progWorkqtyInfoShow){
        if (StringUtils.isEmpty(progWorkqtyInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(progWorkqtyInfoShow.getSubcttInfoPkid())) {
            MessageUtil.addError("�������ܰ���ͬ��");
            return false;
        }
        return true ;
    }

    /**
     * ����Ȩ�޽������
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            // ��ѯ���������
            String strStatusFlagBegin="";
            String strStatusFlagEnd="";
            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progWorkqtyInfoShowSel.setFlowStatus(null);
                    progWorkqtyInfoShowSel.setFlowStatusRemark(null);
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
                // ��ʼ
                strStatusFlagBegin=null;
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
                // ��ʼ
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS0.getCode();
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
                    // ״̬��־������
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // ����д����ʵ��Խ���˻�
                    progWorkqtyInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
                // ���
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS1.getCode();
                // ����
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS2.getCode();
            } else if(strPowerType.contains("Approve")){// ��׼
                if(strPowerType.equals("ApprovePass")){
                    // ״̬��־����׼
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                }else if(strPowerType.equals("ApproveFail")){
                    // ����д����ʵ��Խ���˻�
                    progWorkqtyInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);

                    MessageUtil.addInfo("������׼δ����");
                }
                // ����
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS2.getCode();
                // ��׼
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS3.getCode();
            }
            // ���²�ѯ���Ѳ����Ľ��
            ProgWorkqtyInfoShow progWorkqtyInfoShowTemp =new ProgWorkqtyInfoShow();
                 progWorkqtyInfoShowTemp.setFlowStatusBegin(strStatusFlagBegin);
            progWorkqtyInfoShowTemp.setFlowStatusEnd(strStatusFlagEnd);

            this.progWorkqtyInfoShowList.clear();
            List<ProgWorkqtyInfoShow> progWorkqtyInfoShowListTemp =
                    progWorkqtyInfoService.getProgWorkqtyInfoListByFlowStatusBegin_End(progWorkqtyInfoShowTemp);

            for(ProgWorkqtyInfoShow esISSOMPCUnit: progWorkqtyInfoShowListTemp){
                for(SelectItem itemUnit:subcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getSubcttInfoPkid())){
                        progWorkqtyInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction(){
        if(strSubmitType.equals("Add")){
            if(!submitPreCheck(progWorkqtyInfoShowAdd)){
                return;
            }
            List<ProgWorkqtyInfo> progInfoListTemp =
                    progWorkqtyInfoService.getProgWorkqtyInfoListByModel(progWorkqtyInfoShowAdd);
            if(progInfoListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp=progWorkqtyInfoService.subCttStlCheckForMng(
                    progWorkqtyInfoShowAdd.getSubcttInfoPkid(),
                    progWorkqtyInfoShowAdd.getStageNo());
            if(!"".equals(strTemp)){
                MessageUtil.addError(strTemp);
                return;
            }
            addRecordAction(progWorkqtyInfoShowAdd);
        }
        else if(strSubmitType.equals("Upd")){
            if(!submitPreCheck(progWorkqtyInfoShowUpd)){
                return;
            }
            updRecordAction(progWorkqtyInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            delRecordAction(progWorkqtyInfoShowDel);
        }
        progWorkqtyInfoShowList.clear();
        ProgWorkqtyInfoShow progWorkqtyInfoShowTemp =new ProgWorkqtyInfoShow();
        List<ProgWorkqtyInfoShow> progWorkqtyInfoShowListTemp =
                progWorkqtyInfoService.getProgWorkqtyInfoListByFlowStatusBegin_End(progWorkqtyInfoShowTemp);

        for(ProgWorkqtyInfoShow esISSOMPCUnit: progWorkqtyInfoShowListTemp){
            for(SelectItem itemUnit:subcttList){
                if(itemUnit.getValue().equals(esISSOMPCUnit.getSubcttInfoPkid())){
                    progWorkqtyInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }
    private void addRecordAction(ProgWorkqtyInfoShow progWorkqtyInfoShowPara){
        try {
            progWorkqtyInfoService.insertRecord(progWorkqtyInfoShowPara) ;
            progWorkqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progWorkqtyInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgWorkqtyInfoShow progWorkqtyInfoShowPara){
        try {
            progWorkqtyInfoService.updateRecord(progWorkqtyInfoShowPara) ;
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgWorkqtyInfoShow progWorkqtyInfoShowPara){
        try {
            // ɾ����ϸ����
            int deleteItemsByInitStlSubcttEngNum= progWorkqtyItemService.deleteItemsByInitStlSubcttEng(
                    progWorkqtyInfoShowPara.getPkid());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum= progWorkqtyInfoService.deleteRecord(progWorkqtyInfoShowPara.getPkid()) ;
            if (deleteItemsByInitStlSubcttEngNum<=0&&deleteRecordOfRegistNum<=0){
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ�Start*/
    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public List<ProgWorkqtyInfoShow> getProgWorkqtyInfoShowList() {
        return progWorkqtyInfoShowList;
    }

    public void setProgWorkqtyInfoShowList(List<ProgWorkqtyInfoShow> progWorkqtyInfoShowList) {
        this.progWorkqtyInfoShowList = progWorkqtyInfoShowList;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public ProgWorkqtyInfoService getProgWorkqtyInfoService() {
        return progWorkqtyInfoService;
    }

    public void setProgWorkqtyInfoService(ProgWorkqtyInfoService progWorkqtyInfoService) {
        this.progWorkqtyInfoService = progWorkqtyInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgWorkqtyInfoShow getProgWorkqtyInfoShowQry() {
        return progWorkqtyInfoShowQry;
    }

    public void setProgWorkqtyInfoShowQry(ProgWorkqtyInfoShow progWorkqtyInfoShowQry) {
        this.progWorkqtyInfoShowQry = progWorkqtyInfoShowQry;
    }

    public ProgWorkqtyInfoShow getProgWorkqtyInfoShowSelected() {
        return progWorkqtyInfoShowSelected;
    }

    public void setProgWorkqtyInfoShowSelected(ProgWorkqtyInfoShow progWorkqtyInfoShowSelected) {
        this.progWorkqtyInfoShowSelected = progWorkqtyInfoShowSelected;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public List<SelectItem> getSubcttList() {
        return subcttList;
    }

    public void setSubcttList(List<SelectItem> subcttList) {
        this.subcttList = subcttList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public ProgWorkqtyInfoShow getProgWorkqtyInfoShowSel() {
        return progWorkqtyInfoShowSel;
    }

    public void setProgWorkqtyInfoShowSel(ProgWorkqtyInfoShow progWorkqtyInfoShowSel) {
        this.progWorkqtyInfoShowSel = progWorkqtyInfoShowSel;
    }

    public ProgWorkqtyInfoShow getProgWorkqtyInfoShowAdd() {
        return progWorkqtyInfoShowAdd;
    }

    public void setProgWorkqtyInfoShowAdd(ProgWorkqtyInfoShow progWorkqtyInfoShowAdd) {
        this.progWorkqtyInfoShowAdd = progWorkqtyInfoShowAdd;
    }

    public ProgWorkqtyInfoShow getProgWorkqtyInfoShowUpd() {
        return progWorkqtyInfoShowUpd;
    }

    public void setProgWorkqtyInfoShowUpd(ProgWorkqtyInfoShow progWorkqtyInfoShowUpd) {
        this.progWorkqtyInfoShowUpd = progWorkqtyInfoShowUpd;
    }

    public ProgWorkqtyInfoShow getProgWorkqtyInfoShowDel() {
        return progWorkqtyInfoShowDel;
    }

    public void setProgWorkqtyInfoShowDel(ProgWorkqtyInfoShow progWorkqtyInfoShowDel) {
        this.progWorkqtyInfoShowDel = progWorkqtyInfoShowDel;
    }
    /*�����ֶ�End*/
}
