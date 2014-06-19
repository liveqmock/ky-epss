package epss.view.settle;

import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusRemark;
import epss.repository.model.ProgEstInfo;
import epss.repository.model.TkcttInfo;
import epss.repository.model.model_show.ProgEstInfoShow;
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
public class ProgEstInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgEstInfoAction.class);
    @ManagedProperty(value = "#{progEstInfoService}")
    private ProgEstInfoService progEstInfoService;
    @ManagedProperty(value = "#{progEstItemService}")
    private ProgEstItemService progEstItemService;
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgEstInfoShow progEstInfoShowQry;
    private String strNotPassToStatus;
    private ProgEstInfoShow progEstInfoShowSelected;
    private ProgEstInfoShow progEstInfoShowSel;
    private ProgEstInfoShow progEstInfoShowAdd;
    private ProgEstInfoShow progEstInfoShowUpd;
    private ProgEstInfoShow progEstInfoShowDel;

    private List<ProgEstInfoShow> progEstInfoShowList;

    private List<SelectItem> tkcttList;

    private String strSubmitType;
    private String rowSelectedFlag;
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progEstInfoShowList = new ArrayList<ProgEstInfoShow>();
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
        progEstInfoShowQry = new ProgEstInfoShow();
        progEstInfoShowSel = new ProgEstInfoShow();
        progEstInfoShowAdd = new ProgEstInfoShow();
        progEstInfoShowUpd = new ProgEstInfoShow();
        progEstInfoShowDel = new ProgEstInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag="false";
    }

    public void resetActionForAdd(){
        progEstInfoShowAdd =new ProgEstInfoShow();
        strSubmitType="Add";
        rowSelectedFlag="false";
    }

    public void setMaxNoPlusOne(){
        try {
            Integer intTemp;
            String strMaxId= progEstInfoService.getStrMaxProgEstInfoId();
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLSta"+ esCommon.getStrToday()+"001";
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
                progEstInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                progEstInfoShowUpd.setId(strMaxId);
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
                    progEstInfoShowQry.setFlowStatusBegin("");
                    progEstInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                    break;
                case "MngPass":
                    break;
                case "MngFail":
                    progEstInfoShowQry.setFlowStatusBegin("");
                    progEstInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    progEstInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progEstInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    progEstInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    progEstInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    progEstInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    progEstInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "Print":
                    progEstInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    progEstInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.progEstInfoShowList.clear();
            List<ProgEstInfoShow> progEstInfoShowTemp =
                    progEstInfoService.getProgEstInfoListByFlowStatusBegin_End(progEstInfoShowQry);
            for(ProgEstInfoShow esISSOMPCUnit: progEstInfoShowTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getTkcttInfoPkid())){
                        progEstInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progEstInfoShowList.isEmpty()) {
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
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progEstInfoShowSelected.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInFlowStatuslist(progEstInfoShowSelected.getFlowStatus());
            progEstInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(progEstInfoShowSelected.getCreatedBy()));
            progEstInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(progEstInfoShowSelected.getUpdatedBy()));

            switch (strPowerTypePara){
                case "Qry":// ��ѯ
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                    break;
                case "Mng":// ά��
                    if(strSubmitTypePara.equals("Sel")){
                        progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
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
                            progEstInfoShowUpd =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                        }else if(strSubmitTypePara.equals("Del")){
                            rowSelectedFlag="false";
                            progEstInfoShowDel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                        }
                    }
                    break;
                case "Check": // Ȩ�޿���
                    rowSelectedFlag="true";
                    //�������̻���,��ʾ��ͬ���˻ص�״̬
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
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
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
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
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
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
    private boolean submitPreCheck(ProgEstInfoShow progEstInfoShow){
        if (StringUtils.isEmpty(progEstInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(progEstInfoShow.getTkcttInfoPkid())) {
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
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progEstInfoShowSel.setFlowStatus(null);
                    progEstInfoShowSel.setFlowStatusRemark(null);
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
                // ��ʼ
                strStatusFlagBegin=null;
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
                // ��ʼ
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS0.getCode();
                // ���
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
                    // ״̬��־������
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // ����д����ʵ��Խ���˻�
                    progEstInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
                // ���
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS1.getCode();
                // ����
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS2.getCode();
            } else if(strPowerType.contains("Approve")){// ��׼
                if(strPowerType.equals("ApprovePass")){
                    // ״̬��־����׼
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                }else if(strPowerType.equals("ApproveFail")){
                    // ����д����ʵ��Խ���˻�
                    progEstInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    updRecordAction(progEstInfoShowSel);

                    MessageUtil.addInfo("������׼δ����");
                }
                // ����
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS2.getCode();
                // ��׼
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS3.getCode();
            }
            // ���²�ѯ���Ѳ����Ľ��
            ProgEstInfoShow progEstInfoShowTemp =new ProgEstInfoShow();
                 progEstInfoShowTemp.setFlowStatusBegin(strStatusFlagBegin);
            progEstInfoShowTemp.setFlowStatusEnd(strStatusFlagEnd);

            this.progEstInfoShowList.clear();
            List<ProgEstInfoShow> progEstInfoShowListTemp =
                    progEstInfoService.getProgEstInfoListByFlowStatusBegin_End(progEstInfoShowTemp);

            for(ProgEstInfoShow esISSOMPCUnit: progEstInfoShowListTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getTkcttInfoPkid())){
                        progEstInfoShowList.add(esISSOMPCUnit);
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
            if(!submitPreCheck(progEstInfoShowAdd)){
                return;
            }
            List<ProgEstInfo> progInfoListTemp =
                    progEstInfoService.getProgEstInfoListByModel(progEstInfoShowAdd);
            if(progInfoListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            // ���ɴ������ڲ���Ϊ������
            String strStaQtyMaxStageNo = ToolUtil.getStrIgnoreNull(
                    progEstInfoService.getMaxStageNo(progEstInfoShowAdd.getTkcttInfoPkid()));
            String strStaQtyMaxStageNoStatus=ToolUtil.getStrIgnoreNull(
                    progEstInfoService.getFlowStatus(progEstInfoShowAdd.getTkcttInfoPkid(), strStaQtyMaxStageNo));

            if (progEstInfoShowAdd.getStageNo().compareTo(strStaQtyMaxStageNo)<=0){ //���Ⱥ�����Ƚ��ںŴ�С�����������С���ߵ�������¼��
                MessageUtil.addError("Ӧ¼�����[" + strStaQtyMaxStageNo + "]�ڵ��ܰ�ͳ������!");
                return;
            }else {
                if (!strStaQtyMaxStageNo.equals("")&&(EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strStaQtyMaxStageNoStatus) > 0||"".equals(strStaQtyMaxStageNoStatus))) {//�ж��Ƿ��з���׼״̬�����ݴ��ڣ�����в���¼��
                    MessageUtil.addError("�ܰ�ͳ�ƽ����[" + strStaQtyMaxStageNo + "]�����ݻ�δ��׼ͨ��������¼�������ݣ�");
                    return;
                }
            }
            addRecordAction(progEstInfoShowAdd);
        }
        else if(strSubmitType.equals("Upd")){
            if(!submitPreCheck(progEstInfoShowUpd)){
                return;
            }
            updRecordAction(progEstInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            delRecordAction(progEstInfoShowDel);
        }
        ProgEstInfoShow progEstInfoShowTemp =new ProgEstInfoShow();
        this.progEstInfoShowList =
                progEstInfoService.getProgEstInfoListByFlowStatusBegin_End(progEstInfoShowTemp);
    }

    private void addRecordAction(ProgEstInfoShow progEstInfoShowPara){
        try {
            progEstInfoService.insertRecord(progEstInfoShowPara) ;
            // ����׼�˵���һ�׶ε������õ���һ�׶��У���Ϊ��ʼ�ۼ���
            progEstItemService.setFromLastStageApproveDataToThisStageBeginData(progEstInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgEstInfoShow progEstInfoShowPara){
        try {
            progEstInfoService.updateRecord(progEstInfoShowPara) ;
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgEstInfoShow progEstInfoShowPara){
        try {
            // ɾ����ϸ����
            int deleteItemsByInitStlTkcttEngNum= progEstItemService.deleteItemsByInitStlTkcttEng(
                    progEstInfoShowPara.getPkid());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum= progEstInfoService.deleteRecord(progEstInfoShowPara.getPkid()) ;
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

    public ProgEstInfoService getProgEstInfoService() {
        return progEstInfoService;
    }

    public void setProgEstInfoService(ProgEstInfoService progEstInfoService) {
        this.progEstInfoService = progEstInfoService;
    }

    public ProgEstItemService getProgEstItemService() {
        return progEstItemService;
    }

    public void setProgEstItemService(ProgEstItemService progEstItemService) {
        this.progEstItemService = progEstItemService;
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

    public ProgEstInfoShow getProgEstInfoShowQry() {
        return progEstInfoShowQry;
    }

    public void setProgEstInfoShowQry(ProgEstInfoShow progEstInfoShowQry) {
        this.progEstInfoShowQry = progEstInfoShowQry;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public ProgEstInfoShow getProgEstInfoShowSelected() {
        return progEstInfoShowSelected;
    }

    public void setProgEstInfoShowSelected(ProgEstInfoShow progEstInfoShowSelected) {
        this.progEstInfoShowSelected = progEstInfoShowSelected;
    }

    public ProgEstInfoShow getProgEstInfoShowSel() {
        return progEstInfoShowSel;
    }

    public void setProgEstInfoShowSel(ProgEstInfoShow progEstInfoShowSel) {
        this.progEstInfoShowSel = progEstInfoShowSel;
    }

    public ProgEstInfoShow getProgEstInfoShowAdd() {
        return progEstInfoShowAdd;
    }

    public void setProgEstInfoShowAdd(ProgEstInfoShow progEstInfoShowAdd) {
        this.progEstInfoShowAdd = progEstInfoShowAdd;
    }

    public ProgEstInfoShow getProgEstInfoShowUpd() {
        return progEstInfoShowUpd;
    }

    public void setProgEstInfoShowUpd(ProgEstInfoShow progEstInfoShowUpd) {
        this.progEstInfoShowUpd = progEstInfoShowUpd;
    }

    public ProgEstInfoShow getProgEstInfoShowDel() {
        return progEstInfoShowDel;
    }

    public void setProgEstInfoShowDel(ProgEstInfoShow progEstInfoShowDel) {
        this.progEstInfoShowDel = progEstInfoShowDel;
    }

    public List<ProgEstInfoShow> getProgEstInfoShowList() {
        return progEstInfoShowList;
    }

    public void setProgEstInfoShowList(List<ProgEstInfoShow> progEstInfoShowList) {
        this.progEstInfoShowList = progEstInfoShowList;
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
