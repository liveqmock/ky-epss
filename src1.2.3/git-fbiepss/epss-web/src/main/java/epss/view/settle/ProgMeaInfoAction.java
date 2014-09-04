package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgInfoShow;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.repository.model.EsInitStl;
import epss.service.*;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;;
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
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgInfoShow progInfoShowQry;
    private ProgInfoShow progInfoShowSelected;
    private ProgInfoShow progInfoShowSel;
    private ProgInfoShow progInfoShowAdd;
    private ProgInfoShow progInfoShowUpd;
    private ProgInfoShow progInfoShowDel;

    private List<ProgInfoShow> progInfoShowList;
    private List<SelectItem> tkcttList;

    private String strSubmitType;
    private String strStlType;
    private String strCstplPkid;
    /*����ά������㼶���ֵ���ʾ*/
    private String strCstplPkidRendered;
    private String strControlsRenderedForCheck;

    private String strCallPageNameFlag;
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progInfoShowList = new ArrayList<ProgInfoShow>();
        strStlType=ESEnum.ITEMTYPE7.getCode();
        resetAction();

        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_Status(
                        ESEnum.ITEMTYPE0.getCode()
                        ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
        tkcttList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","ȫ��");
            tkcttList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                tkcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progInfoShowQry =new ProgInfoShow();
        progInfoShowSel =new ProgInfoShow();
        progInfoShowAdd =new ProgInfoShow();
        progInfoShowUpd =new ProgInfoShow();
        progInfoShowDel =new ProgInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.getBackToStatusFlagList("Qry");
    }

    public void resetActionForAdd(){
        progInfoShowAdd =new ProgInfoShow();
        strSubmitType="Add";
    }

    public void setMaxNoPlusOne(String strQryTypePara){
        try {
            Integer intTemp;
            String strMaxId= progStlInfoService.getStrMaxStlId(strStlType);
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLMea"+ ToolUtil.getStrToday()+"001";
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
            if (strQryTypePara.equals("Qry")) {
                progInfoShowQry.setId(strMaxId);
            }else if (strQryTypePara.equals("Add")) {
                progInfoShowAdd.setId(strMaxId);
            }else if (strQryTypePara.equals("Upd")) {
                progInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    public void onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progInfoShowQry.setStlType(strStlType);
            if(strQryFlag.equals("Qry")){
                progInfoShowQry.setStrStatusFlagBegin(progInfoShowQry.getStatusFlag());
                progInfoShowQry.setStrStatusFlagEnd(progInfoShowQry.getStatusFlag());
            }else if(strQryFlag.equals("Mng")){
                progInfoShowQry.setStrStatusFlagBegin(null);
                progInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
            }else if(strQryFlag.equals("Check")){
                progInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                progInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG1.getCode());
            }else if(strQryFlag.equals("DoubleCheck")){
                progInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                progInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG2.getCode());
            }else if(strQryFlag.equals("Approve")){
                progInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                progInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
            }else if(strQryFlag.equals("Print")){
                progInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                progInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
            }
            this.progInfoShowList.clear();
            List<ProgInfoShow> progInfoShowConstructsTemp =
                    esFlowService.selectTkcttStlSMByStatusFlagBegin_End(progInfoShowQry);
            for(ProgInfoShow esISSOMPCUnit: progInfoShowConstructsTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())){
                        progInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
            resetAction();
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   ProgInfoShow progInfoShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progInfoShowPara.getStatusFlag());
            String strStatusFlagName= esFlowControl.getLabelByValueInStatusFlaglist(progInfoShowPara.getStatusFlag());
            progInfoShowPara.setCreatedByName(ToolUtil.getUserName(progInfoShowPara.getCreatedBy()));
            progInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progInfoShowPara.getLastUpdBy()));
            // ��ѯ
            if(strPowerTypePara.equals("Qry")){
                progInfoShowSel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
            }else if(strPowerTypePara.equals("Mng")){// ά��
                if(strSubmitTypePara.equals("Sel")){
                    progInfoShowSel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                }else if(strSubmitTypePara.equals("Add")){
                }else{
                    if(!strStatusFlagCode.equals("")&&
                            !strStatusFlagCode.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        MessageUtil.addInfo("�����Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                        return;
                    }
                    if(strSubmitTypePara.equals("Upd")){
                        progInfoShowUpd =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                    }else if(strSubmitTypePara.equals("Del")){
                        progInfoShowDel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                    }
                }
            }else{// Ȩ�޿���
                //�������̻���,��ʾ��ͬ���˻ص�״̬
                esFlowControl.getBackToStatusFlagList(strPowerTypePara) ;
                progInfoShowSel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                if(strPowerTypePara.equals("Check")){
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("�������ݻ�δ¼����ϣ�����ʱ���ܽ�����˲�����");
                    }else if(!strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG0.getCode())&&!
                            strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG1.getCode())){
                        MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                    }
                }else if(strPowerTypePara.equals("DoubleCheck")){
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("�������ݻ�δ¼����ϣ�����ʱ���ܽ��и��˲�����");
                    }else if(strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG0.getCode())){
                        MessageUtil.addInfo("�������ݸո�¼�룬��δ��ˣ�����ʱ���ܽ��и��ˣ�");
                    }else if(!strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG1.getCode())&&!
                            strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG2.getCode())){
                        MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                    }
                }else if(strPowerTypePara.equals("Approve")){
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("�������ݻ�δ¼����ϣ�����ʱ���ܽ�����׼������");
                    }else if(strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG0.getCode())){
                        MessageUtil.addInfo("�������ݸո�¼�룬��δ��ˣ�����ʱ���ܽ�����׼��");
                    }else if(!strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG2.getCode())&&!
                            strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG3.getCode())){
                        MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"������Ȩ���б༭������");
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
    */
    private boolean submitPreCheck(ProgInfoShow progInfoShow){
        if (StringUtils.isEmpty(progInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(progInfoShow.getStlPkid())) {
            MessageUtil.addError("�������ܰ���ͬ��");
            return false;
        }
        return true ;
    }

    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction(){
        if(strSubmitType.equals("Add")){
            progInfoShowAdd.setStlType(strStlType);
            if(!submitPreCheck(progInfoShowAdd)){
                return;
            }
            List<EsInitStl> esInitStlListTemp =
                    progStlInfoService.getInitStlListByModelShow(progInfoShowAdd);
            if(esInitStlListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp=esFlowService.subCttStlCheckForMng(
                    ESEnum.ITEMTYPE7.getCode(),
                    progInfoShowAdd.getStlPkid(),
                    progInfoShowAdd.getPeriodNo());
            if(!"".equals(strTemp)){
                MessageUtil.addError(strTemp);
                return;
            }else{
                addRecordAction(progInfoShowAdd);
                resetActionForAdd();
            }
        }
        else if(strSubmitType.equals("Upd")){
            progInfoShowUpd.setStlType(strStlType);
            if(!submitPreCheck(progInfoShowUpd)){
                return;
            }
            updRecordAction(progInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            progInfoShowDel.setStlType(strStlType);
            delRecordAction(progInfoShowDel);
        }
        ProgInfoShow progInfoShowTemp =new ProgInfoShow();
        progInfoShowTemp.setStlType(strStlType);
        progInfoShowTemp.setStrStatusFlagBegin(null);
        progInfoShowTemp.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
        this.progInfoShowList =
                esFlowService.selectTkcttStlSMByStatusFlagBegin_End(progInfoShowTemp);
    }

    private void addRecordAction(ProgInfoShow progInfoShowPara){
        try {
            progStlInfoService.insertRecord(progInfoShowPara) ;
            // ����׼�˵���һ�׶ε������õ���һ�׶��У���Ϊ��ʼ�ۼ���
            progMeaItemService.setFromLastStageApproveDataToThisStageBeginData(progInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgInfoShow progInfoShowPara){
        try {
            progStlInfoService.updateRecord(progInfoShowPara) ;
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgInfoShow progInfoShowPara){
        try {
            // ɾ����ϸ����
            int deleteItemsByInitStlTkcttEngNum= progMeaItemService.deleteItemsByInitStlTkcttEng(
                    progInfoShowPara.getStlPkid(),
                    progInfoShowPara.getPeriodNo());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum= progStlInfoService.deleteRecord(progInfoShowDel.getPkid()) ;
            // ɾ��Ȩ������
            int deleteRecordOfPowerNum= flowCtrlService.deleteRecord(
                    progInfoShowPara.getStlType(),
                    progInfoShowPara.getStlPkid(),
                    progInfoShowPara.getPeriodNo());
            if (deleteItemsByInitStlTkcttEngNum<=0&&deleteRecordOfRegistNum<=0&&deleteRecordOfPowerNum<=0){
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
    public ProgInfoShow getProgInfoShowQry() {
        return progInfoShowQry;
    }

    public void setProgInfoShowQry(ProgInfoShow progInfoShowQry) {
        this.progInfoShowQry = progInfoShowQry;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService progMeaItemService) {
        this.progMeaItemService = progMeaItemService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public List<ProgInfoShow> getProgInfoShowList() {
        return progInfoShowList;
    }

    public void setProgInfoShowList(List<ProgInfoShow> progInfoShowList) {
        this.progInfoShowList = progInfoShowList;
    }

    public ProgInfoShow getProgInfoShowSelected() {
        return progInfoShowSelected;
    }

    public void setProgInfoShowSelected(ProgInfoShow progInfoShowSelected) {
        this.progInfoShowSelected = progInfoShowSelected;
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

    public String getStrStlType() {
        return strStlType;
    }

    public void setStrStlType(String strStlType) {
        this.strStlType = strStlType;
    }

    public String getStrCstplPkid() {
        return strCstplPkid;
    }

    public void setStrCstplPkid(String strCstplPkid) {
        this.strCstplPkid = strCstplPkid;
    }

    public String getStrCstplPkidRendered() {
        return strCstplPkidRendered;
    }

    public void setStrCstplPkidRendered(String strCstplPkidRendered) {
        this.strCstplPkidRendered = strCstplPkidRendered;
    }

    public String getStrControlsRenderedForCheck() {
        return strControlsRenderedForCheck;
    }

    public void setStrControlsRenderedForCheck(String strControlsRenderedForCheck) {
        this.strControlsRenderedForCheck = strControlsRenderedForCheck;
    }

    public String getStrCallPageNameFlag() {
        return strCallPageNameFlag;
    }

    public void setStrCallPageNameFlag(String strCallPageNameFlag) {
        this.strCallPageNameFlag = strCallPageNameFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public ProgInfoShow getProgInfoShowAdd() {
        return progInfoShowAdd;
    }

    public void setProgInfoShowAdd(ProgInfoShow progInfoShowAdd) {
        this.progInfoShowAdd = progInfoShowAdd;
    }

    public ProgInfoShow getProgInfoShowDel() {
        return progInfoShowDel;
    }

    public void setProgInfoShowDel(ProgInfoShow progInfoShowDel) {
        this.progInfoShowDel = progInfoShowDel;
    }

    public ProgInfoShow getProgInfoShowUpd() {
        return progInfoShowUpd;
    }

    public void setProgInfoShowUpd(ProgInfoShow progInfoShowUpd) {
        this.progInfoShowUpd = progInfoShowUpd;
    }

    public ProgInfoShow getProgInfoShowSel() {
        return progInfoShowSel;
    }

    public void setProgInfoShowSel(ProgInfoShow progInfoShowSel) {
        this.progInfoShowSel = progInfoShowSel;
    }

    /*�����ֶ�End*/
}
