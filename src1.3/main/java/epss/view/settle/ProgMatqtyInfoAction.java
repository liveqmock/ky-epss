package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusRemark;
import epss.repository.model.ProgMatqtyInfo;
import epss.repository.model.SubcttInfo;
import epss.repository.model.model_show.ProgMatqtyInfoShow;
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
public class ProgMatqtyInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMatqtyInfoAction.class);
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgMatqtyInfoShow progMatqtyInfoShowQry;
    private String strNotPassToStatus;
    private ProgMatqtyInfoShow progMatqtyInfoShowSelected;
    private ProgMatqtyInfoShow progMatqtyInfoShowSel;
    private ProgMatqtyInfoShow progMatqtyInfoShowAdd;
    private ProgMatqtyInfoShow progMatqtyInfoShowUpd;
    private ProgMatqtyInfoShow progMatqtyInfoShowDel;

    private List<ProgMatqtyInfoShow> progMatqtyInfoShowList;

    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String rowSelectedFlag;
    private String strCstplInfoPkid;
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progMatqtyInfoShowList = new ArrayList<ProgMatqtyInfoShow>();
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
        progMatqtyInfoShowQry = new ProgMatqtyInfoShow();
        progMatqtyInfoShowSel = new ProgMatqtyInfoShow();
        progMatqtyInfoShowAdd = new ProgMatqtyInfoShow();
        progMatqtyInfoShowUpd = new ProgMatqtyInfoShow();
        progMatqtyInfoShowDel = new ProgMatqtyInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag="false";
    }

    public void resetActionForAdd(){
        progMatqtyInfoShowAdd =new ProgMatqtyInfoShow();
        strSubmitType="Add";
        rowSelectedFlag="false";
    }

    public void setMaxNoPlusOne(){
        try {
            Integer intTemp;
            String strMaxId= progMatqtyInfoService.getStrMaxProgMatqtyInfoId();
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
                progMatqtyInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                progMatqtyInfoShowUpd.setId(strMaxId);
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
                    progMatqtyInfoShowQry.setFlowStatusBegin("");
                    progMatqtyInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                    break;
                case "MngPass":
                    break;
                case "MngFail":
                    progMatqtyInfoShowQry.setFlowStatusBegin("");
                    progMatqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    progMatqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progMatqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    progMatqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    progMatqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    progMatqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    progMatqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "Print":
                    progMatqtyInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    progMatqtyInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.progMatqtyInfoShowList.clear();
            List<ProgMatqtyInfoShow> progMatqtyInfoShowTemp =
                    progMatqtyInfoService.getProgMatqtyInfoListByFlowStatusBegin_End(progMatqtyInfoShowQry);
            for(ProgMatqtyInfoShow esISSOMPCUnit: progMatqtyInfoShowTemp){
                for(SelectItem itemUnit:subcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getSubcttInfoPkid())){
                        progMatqtyInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progMatqtyInfoShowList.isEmpty()) {
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
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progMatqtyInfoShowSelected.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInFlowStatuslist(progMatqtyInfoShowSelected.getFlowStatus());
            progMatqtyInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(progMatqtyInfoShowSelected.getCreatedBy()));
            progMatqtyInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(progMatqtyInfoShowSelected.getUpdatedBy()));

            switch (strPowerTypePara){
                case "Qry":// ��ѯ
                    progMatqtyInfoShowSel =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
                    break;
                case "Mng":// ά��
                    if(strSubmitTypePara.equals("Sel")){
                        progMatqtyInfoShowSel =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
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
                            progMatqtyInfoShowUpd =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
                        }else if(strSubmitTypePara.equals("Del")){
                            rowSelectedFlag="false";
                            progMatqtyInfoShowDel =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
                        }
                    }
                    break;
                case "Check": // Ȩ�޿���
                    rowSelectedFlag="true";
                    //�������̻���,��ʾ��ͬ���˻ص�״̬
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progMatqtyInfoShowSel =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
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
                    progMatqtyInfoShowSel =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
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
                    progMatqtyInfoShowSel =(ProgMatqtyInfoShow) BeanUtils.cloneBean(progMatqtyInfoShowSelected);
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
    private boolean submitPreCheck(ProgMatqtyInfoShow progMatqtyInfoShow){
        if (StringUtils.isEmpty(progMatqtyInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(progMatqtyInfoShow.getSubcttInfoPkid())) {
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
                    progMatqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progMatqtyInfoShowSel.setFlowStatus(null);
                    progMatqtyInfoShowSel.setFlowStatusRemark(null);
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
                // ��ʼ
                strStatusFlagBegin=null;
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progMatqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progMatqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
                // ��ʼ
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS0.getCode();
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
                    // ״̬��־������
                    progMatqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // ����д����ʵ��Խ���˻�
                    progMatqtyInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
                // ���
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS1.getCode();
                // ����
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS2.getCode();
            } else if(strPowerType.contains("Approve")){// ��׼
                if(strPowerType.equals("ApprovePass")){
                    // ״̬��־����׼
                    progMatqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    updRecordAction(progMatqtyInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                }else if(strPowerType.equals("ApproveFail")){
                    // ����д����ʵ��Խ���˻�
                    progMatqtyInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    progMatqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    updRecordAction(progMatqtyInfoShowSel);

                    MessageUtil.addInfo("������׼δ����");
                }
                // ����
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS2.getCode();
                // ��׼
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS3.getCode();
            }
            // ���²�ѯ���Ѳ����Ľ��
            ProgMatqtyInfoShow progMatqtyInfoShowTemp =new ProgMatqtyInfoShow();
                 progMatqtyInfoShowTemp.setFlowStatusBegin(strStatusFlagBegin);
            progMatqtyInfoShowTemp.setFlowStatusEnd(strStatusFlagEnd);

            this.progMatqtyInfoShowList.clear();
            List<ProgMatqtyInfoShow> progMatqtyInfoShowListTemp =
                    progMatqtyInfoService.getProgMatqtyInfoListByFlowStatusBegin_End(progMatqtyInfoShowTemp);

            for(ProgMatqtyInfoShow esISSOMPCUnit: progMatqtyInfoShowListTemp){
                for(SelectItem itemUnit:subcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getSubcttInfoPkid())){
                        progMatqtyInfoShowList.add(esISSOMPCUnit);
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
            if(!submitPreCheck(progMatqtyInfoShowAdd)){
                return;
            }
            List<ProgMatqtyInfo> progInfoListTemp =
                    progMatqtyInfoService.getProgMatqtyInfoListByModel(progMatqtyInfoShowAdd);
            if(progInfoListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp=progMatqtyInfoService.subCttStlCheckForMng(
                    progMatqtyInfoShowAdd.getSubcttInfoPkid(),
                    progMatqtyInfoShowAdd.getStageNo());
            if(!"".equals(strTemp)){
                MessageUtil.addError(strTemp);
                return;
            }
            addRecordAction(progMatqtyInfoShowAdd);
        }
        else if(strSubmitType.equals("Upd")){
            if(!submitPreCheck(progMatqtyInfoShowUpd)){
                return;
            }
            updRecordAction(progMatqtyInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            delRecordAction(progMatqtyInfoShowDel);
        }
        progMatqtyInfoShowList.clear();
        ProgMatqtyInfoShow progMatqtyInfoShowTemp =new ProgMatqtyInfoShow();
        List<ProgMatqtyInfoShow> progMatqtyInfoShowListTemp =
                progMatqtyInfoService.getProgMatqtyInfoListByFlowStatusBegin_End(progMatqtyInfoShowTemp);

        for(ProgMatqtyInfoShow esISSOMPCUnit: progMatqtyInfoShowListTemp){
            for(SelectItem itemUnit:subcttList){
                if(itemUnit.getValue().equals(esISSOMPCUnit.getSubcttInfoPkid())){
                    progMatqtyInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }
    private void addRecordAction(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        try {
            progMatqtyInfoService.insertRecord(progMatqtyInfoShowPara) ;
            progMatqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progMatqtyInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        try {
            progMatqtyInfoService.updateRecord(progMatqtyInfoShowPara) ;
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgMatqtyInfoShow progMatqtyInfoShowPara){
        try {
            // ɾ����ϸ����
            int deleteItemsByInitStlSubcttEngNum= progMatqtyItemService.deleteItemsByInitStlSubcttEng(
                    progMatqtyInfoShowPara.getPkid());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum= progMatqtyInfoService.deleteRecord(progMatqtyInfoShowPara.getPkid()) ;
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
    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public List<ProgMatqtyInfoShow> getProgMatqtyInfoShowList() {
        return progMatqtyInfoShowList;
    }

    public void setProgMatqtyInfoShowList(List<ProgMatqtyInfoShow> progMatqtyInfoShowList) {
        this.progMatqtyInfoShowList = progMatqtyInfoShowList;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public ProgMatqtyInfoService getProgMatqtyInfoService() {
        return progMatqtyInfoService;
    }

    public void setProgMatqtyInfoService(ProgMatqtyInfoService progMatqtyInfoService) {
        this.progMatqtyInfoService = progMatqtyInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgMatqtyInfoShow getProgMatqtyInfoShowQry() {
        return progMatqtyInfoShowQry;
    }

    public void setProgMatqtyInfoShowQry(ProgMatqtyInfoShow progMatqtyInfoShowQry) {
        this.progMatqtyInfoShowQry = progMatqtyInfoShowQry;
    }

    public ProgMatqtyInfoShow getProgMatqtyInfoShowSelected() {
        return progMatqtyInfoShowSelected;
    }

    public void setProgMatqtyInfoShowSelected(ProgMatqtyInfoShow progMatqtyInfoShowSelected) {
        this.progMatqtyInfoShowSelected = progMatqtyInfoShowSelected;
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

    public ProgMatqtyInfoShow getProgMatqtyInfoShowSel() {
        return progMatqtyInfoShowSel;
    }

    public void setProgMatqtyInfoShowSel(ProgMatqtyInfoShow progMatqtyInfoShowSel) {
        this.progMatqtyInfoShowSel = progMatqtyInfoShowSel;
    }

    public ProgMatqtyInfoShow getProgMatqtyInfoShowAdd() {
        return progMatqtyInfoShowAdd;
    }

    public void setProgMatqtyInfoShowAdd(ProgMatqtyInfoShow progMatqtyInfoShowAdd) {
        this.progMatqtyInfoShowAdd = progMatqtyInfoShowAdd;
    }

    public ProgMatqtyInfoShow getProgMatqtyInfoShowUpd() {
        return progMatqtyInfoShowUpd;
    }

    public void setProgMatqtyInfoShowUpd(ProgMatqtyInfoShow progMatqtyInfoShowUpd) {
        this.progMatqtyInfoShowUpd = progMatqtyInfoShowUpd;
    }

    public ProgMatqtyInfoShow getProgMatqtyInfoShowDel() {
        return progMatqtyInfoShowDel;
    }

    public void setProgMatqtyInfoShowDel(ProgMatqtyInfoShow progMatqtyInfoShowDel) {
        this.progMatqtyInfoShowDel = progMatqtyInfoShowDel;
    }
    /*�����ֶ�End*/

    public String getStrCstplInfoPkid() {
        return strCstplInfoPkid;
    }

    public void setStrCstplInfoPkid(String strCstplInfoPkid) {
        this.strCstplInfoPkid = strCstplInfoPkid;
    }

}
