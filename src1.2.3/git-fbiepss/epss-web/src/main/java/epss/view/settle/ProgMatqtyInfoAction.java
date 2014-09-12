package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.OperResShow;
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
import skyline.util.MessageUtil;
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
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgInfoShow progInfoShowQry;
    private ProgInfoShow progInfoShowSel;
    private ProgInfoShow progInfoShowAdd;
    private ProgInfoShow progInfoShowUpd;
    private ProgInfoShow progInfoShowDel;

    private List<ProgInfoShow> progInfoShowList;
    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String strStlType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progInfoShowList = new ArrayList<ProgInfoShow>();
        String strCttInfoPkid="";
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strCttInfoPkid")){
            strCttInfoPkid=parammap.get("strCttInfoPkid").toString();
        }
        strStlType =ESEnum.ITEMTYPE4.getCode();

        resetAction();
        //��ĳһ�ɱ��ƻ��µķְ���ͬ
        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                        ESEnum.ITEMTYPE2.getCode()
                        , strCttInfoPkid
                        , ESEnumStatusFlag.STATUS_FLAG3.getCode());
        subcttList = new ArrayList<SelectItem>();
        if (cttInfoShowList.size() > 0) {
            SelectItem selectItem = new SelectItem("", "ȫ��");
            subcttList.add(selectItem);
            for (CttInfoShow itemUnit : cttInfoShowList) {
                selectItem = new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progInfoShowQry = new ProgInfoShow();
        progInfoShowSel = new ProgInfoShow();
        progInfoShowAdd = new ProgInfoShow();
        progInfoShowUpd = new ProgInfoShow();
        progInfoShowDel = new ProgInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
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
                strMaxId="STLM"+ ToolUtil.getStrToday()+"001";
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
                progInfoShowQry.setStrStatusFlagBegin(progInfoShowQry.getFlowStatus());
                progInfoShowQry.setStrStatusFlagEnd(progInfoShowQry.getFlowStatus());
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
                    esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowQry);
            for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                for (SelectItem itemUnit:subcttList) {
                    if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                        progInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
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
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progInfoShowPara.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInStatusFlaglist(progInfoShowPara.getFlowStatus());
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
            MessageUtil.addError("������ְ���ͬ��");
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
                    ESEnum.ITEMTYPE4.getCode(),
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
            //�ж��Ƿ��ѹ��������˷ְ���������
            EsCttInfo esCttInfoTemp=cttInfoService.getCttInfoByPkId(progInfoShowDel.getStlPkid());
            if ((ESEnumSubcttType.TYPE3.getCode()).equals(esCttInfoTemp.getType())||
                 (ESEnumSubcttType.TYPE6.getCode()).equals(esCttInfoTemp.getType())){
                ProgInfoShow progInfoShowQryQ=new ProgInfoShow();
                progInfoShowQryQ.setStlPkid(progInfoShowDel.getStlPkid());
                progInfoShowQryQ.setStlType(ESEnum.ITEMTYPE3.getCode());
                progInfoShowQryQ.setPeriodNo(progInfoShowDel.getPeriodNo());
                List<ProgInfoShow> progInfoShowConstructsTemp =
                        esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowQryQ);
                if (progInfoShowConstructsTemp.size()!=0){
                    for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                        if((!("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus())))
                                &&(progInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))){
                            MessageUtil.addInfo("�ü�¼�ѹ����ְ��������㣬����ɾ����");
                            return;
                        }else if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))
                                &&(progInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))){
                            delQtyRecordAction(esISSOMPCUnit);
                        }
                    }
                }
            }
            delRecordAction(progInfoShowDel);
        }

        ProgInfoShow progInfoShowTemp =new ProgInfoShow();
        progInfoShowTemp.setStlType(strStlType);
        progInfoShowTemp.setStrStatusFlagBegin(null);
        progInfoShowTemp.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
        this.progInfoShowList.clear();
        List<ProgInfoShow> progInfoShowConstructsTemp =
                esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowTemp);

        for(ProgInfoShow esISSOMPCUnit: progInfoShowConstructsTemp){
            for(SelectItem itemUnit:subcttList){
                if(itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())){
                    progInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }
    private void addRecordAction(ProgInfoShow progInfoShowPara){
        try {
            progStlInfoService.insertRecord(progInfoShowPara) ;
            progMatqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progInfoShowPara);
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
            int deleteItemsByInitStlTkcttEngNum=
                    progMatqtyItemService.deleteItemsByInitStlSubcttEng(
                    progInfoShowPara.getStlPkid(),
                    progInfoShowPara.getPeriodNo());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum= progStlInfoService.deleteRecord(progInfoShowPara.getPkid()) ;
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
    private void delQtyRecordAction(ProgInfoShow progInfoShowPara) {
        try {
            // ɾ����ϸ����
            int deleteItemsByInitStlTkcttEngNum =
                    progWorkqtyItemService.deleteItemsByInitStlSubcttEng(
                            progInfoShowPara.getStlPkid(),
                            progInfoShowPara.getPeriodNo());
            // ɾ���Ǽ�����
            int deleteRecordOfRegistNum = progStlInfoService.deleteRecord(progInfoShowPara.getPkid());
            if (deleteItemsByInitStlTkcttEngNum <= 0 && deleteRecordOfRegistNum <= 0) {
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

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public List<ProgInfoShow> getProgInfoShowList() {
        return progInfoShowList;
    }

    public void setProgInfoShowList(List<ProgInfoShow> progInfoShowList) {
        this.progInfoShowList = progInfoShowList;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgInfoShow getProgInfoShowQry() {
        return progInfoShowQry;
    }

    public void setProgInfoShowQry(ProgInfoShow progInfoShowQry) {
        this.progInfoShowQry = progInfoShowQry;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public ProgInfoShow getProgInfoShowSel() {
        return progInfoShowSel;
    }

    public void setProgInfoShowSel(ProgInfoShow progInfoShowSel) {
        this.progInfoShowSel = progInfoShowSel;
    }

    public ProgInfoShow getProgInfoShowAdd() {
        return progInfoShowAdd;
    }

    public void setProgInfoShowAdd(ProgInfoShow progInfoShowAdd) {
        this.progInfoShowAdd = progInfoShowAdd;
    }

    public ProgInfoShow getProgInfoShowUpd() {
        return progInfoShowUpd;
    }

    public void setProgInfoShowUpd(ProgInfoShow progInfoShowUpd) {
        this.progInfoShowUpd = progInfoShowUpd;
    }

    public ProgInfoShow getProgInfoShowDel() {
        return progInfoShowDel;
    }

    public void setProgInfoShowDel(ProgInfoShow progInfoShowDel) {
        this.progInfoShowDel = progInfoShowDel;
    }
    /*�����ֶ�End*/

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }
}
