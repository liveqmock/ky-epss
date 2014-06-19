package epss.view.settle;

import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusRemark;
import epss.repository.model.ProgMeaInfo;
import epss.repository.model.TkcttInfo;
import epss.repository.model.model_show.ProgMeaInfoShow;
import epss.repository.model.model_show.TkcttInfoShow;
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

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgMeaInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMeaInfoAction.class);
    @ManagedProperty(value = "#{progMeaInfoService}")
    private ProgMeaInfoService progMeaInfoService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;

    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgMeaInfoShow progMeaInfoShowQry;
    private String strNotPassToStatus;
    private ProgMeaInfoShow progMeaInfoShowSelected;
    private ProgMeaInfoShow progMeaInfoShowSel;
    private ProgMeaInfoShow progMeaInfoShowAdd;
    private ProgMeaInfoShow progMeaInfoShowUpd;
    private ProgMeaInfoShow progMeaInfoShowDel;

    private List<ProgMeaInfoShow> progMeaInfoShowList;

    private List<SelectItem> tkcttList;

    private String strSubmitType;
    private String rowSelectedFlag;
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progMeaInfoShowList = new ArrayList<ProgMeaInfoShow>();
        resetAction();

        TkcttInfoShow tkcttInfoTemp=new TkcttInfoShow();
        tkcttInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        List<TkcttInfo> tkcttInfoList =
                tkcttInfoService.getTkcttInfoListByModel(tkcttInfoTemp);
        tkcttList=new ArrayList<SelectItem>();
        if(tkcttInfoList.size()>0){
            SelectItem selectItem=new SelectItem("","ȫ��");
            tkcttList.add(selectItem);
            for(TkcttInfo itemUnit: tkcttInfoList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                tkcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progMeaInfoShowQry =new ProgMeaInfoShow();
        progMeaInfoShowSel =new ProgMeaInfoShow();
        progMeaInfoShowAdd =new ProgMeaInfoShow();
        progMeaInfoShowUpd =new ProgMeaInfoShow();
        progMeaInfoShowDel =new ProgMeaInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag="false";
    }

    public void resetActionForAdd(){
        progMeaInfoShowAdd =new ProgMeaInfoShow();
        strSubmitType="Add";
        rowSelectedFlag="false";
    }

    public void setMaxNoPlusOne(){
        try {
            Integer intTemp;
            String strMaxId= progMeaInfoService.getStrMaxProgMeaInfoId();
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLMea"+ esCommon.getStrToday()+"001";
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
                progMeaInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                progMeaInfoShowUpd.setId(strMaxId);
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
                    progMeaInfoShowQry.setFlowStatusBegin("");
                    progMeaInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                    break;
                case "MngPass":
                    break;
                case "MngFail":
                    progMeaInfoShowQry.setFlowStatusBegin("");
                    progMeaInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    progMeaInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progMeaInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    progMeaInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    progMeaInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    progMeaInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    progMeaInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "Print":
                    progMeaInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    progMeaInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.progMeaInfoShowList.clear();
            List<ProgMeaInfoShow> progMeaInfoShowTemp =
                    progMeaInfoService.getProgMeaInfoListByFlowStatusBegin_End(progMeaInfoShowQry);
            for(ProgMeaInfoShow esISSOMPCUnit: progMeaInfoShowTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getTkcttInfoPkid())){
                        progMeaInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progMeaInfoShowList.isEmpty()) {
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
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progMeaInfoShowSelected.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInFlowStatuslist(progMeaInfoShowSelected.getFlowStatus());
            progMeaInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(progMeaInfoShowSelected.getCreatedBy()));
            progMeaInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(progMeaInfoShowSelected.getUpdatedBy()));

            switch (strPowerTypePara){
                case "Qry":// ��ѯ
                    progMeaInfoShowSel =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
                    break;
                case "Mng":// ά��
                    if(strSubmitTypePara.equals("Sel")){
                        progMeaInfoShowSel =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
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
                            progMeaInfoShowUpd =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
                        }else if(strSubmitTypePara.equals("Del")){
                            rowSelectedFlag="false";
                            progMeaInfoShowDel =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
                        }
                    }
                    break;
                case "Check": // Ȩ�޿���
                    rowSelectedFlag="true";
                    //�������̻���,��ʾ��ͬ���˻ص�״̬
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progMeaInfoShowSel =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
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
                    progMeaInfoShowSel =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
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
                    progMeaInfoShowSel =(ProgMeaInfoShow) BeanUtils.cloneBean(progMeaInfoShowSelected);
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
    private boolean submitPreCheck(ProgMeaInfoShow progMeaInfoShow){
        if (StringUtils.isEmpty(progMeaInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(progMeaInfoShow.getTkcttInfoPkid())) {
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
                    progMeaInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progMeaInfoShowSel.setFlowStatus(null);
                    progMeaInfoShowSel.setFlowStatusRemark(null);
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
                // ��ʼ
                strStatusFlagBegin=null;
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progMeaInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progMeaInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
                // ��ʼ
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS0.getCode();
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
                    // ״̬��־������
                    progMeaInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // ����д����ʵ��Խ���˻�
                    progMeaInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
                // ���
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS1.getCode();
                // ����
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS2.getCode();
            } else if(strPowerType.contains("Approve")){// ��׼
                if(strPowerType.equals("ApprovePass")){
                    // ״̬��־����׼
                    progMeaInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    updRecordAction(progMeaInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                }else if(strPowerType.equals("ApproveFail")){
                    // ����д����ʵ��Խ���˻�
                    progMeaInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    progMeaInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    updRecordAction(progMeaInfoShowSel);

                    MessageUtil.addInfo("������׼δ����");
                }
                // ����
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS2.getCode();
                // ��׼
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS3.getCode();
            }
            // ���²�ѯ���Ѳ����Ľ��
            ProgMeaInfoShow progMeaInfoShowTemp =new ProgMeaInfoShow();
            progMeaInfoShowTemp.setFlowStatusBegin(strStatusFlagBegin);
            progMeaInfoShowTemp.setFlowStatusEnd(strStatusFlagEnd);

            this.progMeaInfoShowList.clear();
            List<ProgMeaInfoShow> progMeaInfoShowListTemp =
                    progMeaInfoService.getProgMeaInfoListByFlowStatusBegin_End(progMeaInfoShowTemp);

            for(ProgMeaInfoShow esISSOMPCUnit: progMeaInfoShowListTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getTkcttInfoPkid())){
                        progMeaInfoShowList.add(esISSOMPCUnit);
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
            if(!submitPreCheck(progMeaInfoShowAdd)){
                return;
            }
            List<ProgMeaInfo> progInfoListTemp =
                    progMeaInfoService.getProgMeaInfoListByModel(progMeaInfoShowAdd);
            if(progInfoListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            // ���ɴ������ڲ���Ϊ������
            String strMeaQtyMaxStageNo = ToolUtil.getStrIgnoreNull(
                    progMeaInfoService.getMaxStageNo(progMeaInfoShowAdd.getTkcttInfoPkid()));
            String strMeaQtyMaxStageNoStatus=ToolUtil.getStrIgnoreNull(
                    progMeaInfoService.getFlowStatus(progMeaInfoShowAdd.getTkcttInfoPkid(),strMeaQtyMaxStageNo));

            if (progMeaInfoShowAdd.getStageNo().compareTo(strMeaQtyMaxStageNo)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                MessageUtil.addError("Ӧ¼�����[" + strMeaQtyMaxStageNo + "]�ڵ��ܰ���������!");
                return;
            }else {
                if (!strMeaQtyMaxStageNo.equals("")&&(EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strMeaQtyMaxStageNoStatus) > 0||"".equals(strMeaQtyMaxStageNoStatus))) {//�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                    MessageUtil.addError("�ܰ����������[" + strMeaQtyMaxStageNo + "]�����ݻ�δ��׼ͨ��������¼�������ݣ�");
                    return;
                }
            }
            addRecordAction(progMeaInfoShowAdd);
        }
        else if(strSubmitType.equals("Upd")){
            if(!submitPreCheck(progMeaInfoShowUpd)){
                return;
            }
            updRecordAction(progMeaInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            delRecordAction(progMeaInfoShowDel);
        }
        ProgMeaInfoShow progMeaInfoShowTemp =new ProgMeaInfoShow();
        this.progMeaInfoShowList =
                progMeaInfoService.getProgMeaInfoListByFlowStatusBegin_End(progMeaInfoShowTemp);
    }

    private void addRecordAction(ProgMeaInfoShow progMeaInfoShowPara){
        try {
            progMeaInfoService.insertRecord(progMeaInfoShowPara) ;
            // ����׼�˵���һ�׶ε������õ���һ�׶��У���Ϊ��ʼ�ۼ���
            progMeaItemService.setFromLastStageApproveDataToThisStageBeginData(progMeaInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgMeaInfoShow progMeaInfoShowPara){
        try {
            progMeaInfoService.updateRecord(progMeaInfoShowPara) ;
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgMeaInfoShow progMeaInfoShowPara){
        try {
            // ɾ����ϸ����
            int deleteItemsByInitStlTkcttEngNum= progMeaItemService.deleteItemsByInitStlTkcttEng(
                    progMeaInfoShowPara.getPkid());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum= progMeaInfoService.deleteRecord(progMeaInfoShowPara.getPkid()) ;
            if (deleteItemsByInitStlTkcttEngNum<=0&&deleteRecordOfRegistNum<=0){
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

    public ProgMeaInfoService getProgMeaInfoService() {
        return progMeaInfoService;
    }

    public void setProgMeaInfoService(ProgMeaInfoService progMeaInfoService) {
        this.progMeaInfoService = progMeaInfoService;
    }

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService progMeaItemService) {
        this.progMeaItemService = progMeaItemService;
    }

    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public ProgMeaInfoShow getProgMeaInfoShowQry() {
        return progMeaInfoShowQry;
    }

    public void setProgMeaInfoShowQry(ProgMeaInfoShow progMeaInfoShowQry) {
        this.progMeaInfoShowQry = progMeaInfoShowQry;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public ProgMeaInfoShow getProgMeaInfoShowSelected() {
        return progMeaInfoShowSelected;
    }

    public void setProgMeaInfoShowSelected(ProgMeaInfoShow progMeaInfoShowSelected) {
        this.progMeaInfoShowSelected = progMeaInfoShowSelected;
    }

    public ProgMeaInfoShow getProgMeaInfoShowSel() {
        return progMeaInfoShowSel;
    }

    public void setProgMeaInfoShowSel(ProgMeaInfoShow progMeaInfoShowSel) {
        this.progMeaInfoShowSel = progMeaInfoShowSel;
    }

    public ProgMeaInfoShow getProgMeaInfoShowAdd() {
        return progMeaInfoShowAdd;
    }

    public void setProgMeaInfoShowAdd(ProgMeaInfoShow progMeaInfoShowAdd) {
        this.progMeaInfoShowAdd = progMeaInfoShowAdd;
    }

    public ProgMeaInfoShow getProgMeaInfoShowUpd() {
        return progMeaInfoShowUpd;
    }

    public void setProgMeaInfoShowUpd(ProgMeaInfoShow progMeaInfoShowUpd) {
        this.progMeaInfoShowUpd = progMeaInfoShowUpd;
    }

    public ProgMeaInfoShow getProgMeaInfoShowDel() {
        return progMeaInfoShowDel;
    }

    public void setProgMeaInfoShowDel(ProgMeaInfoShow progMeaInfoShowDel) {
        this.progMeaInfoShowDel = progMeaInfoShowDel;
    }

    public List<ProgMeaInfoShow> getProgMeaInfoShowList() {
        return progMeaInfoShowList;
    }

    public void setProgMeaInfoShowList(List<ProgMeaInfoShow> progMeaInfoShowList) {
        this.progMeaInfoShowList = progMeaInfoShowList;
    }

    public List<SelectItem> getTkcttList() {
        return tkcttList;
    }

    public void setTkcttList(List<SelectItem> tkcttList) {
        this.tkcttList = tkcttList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public void setRowSelectedFlag(String rowSelectedFlag) {
        this.rowSelectedFlag = rowSelectedFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }
    /*�����ֶ�End*/

}
