package epss.view.settle;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.repository.model.ProgStlInfo;
import epss.service.*;
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

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlInfoTkEstAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoTkEstAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemTkEstService}")
    private ProgStlItemTkEstService progStlItemTkEstService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgStlInfoShow progStlInfoShowQry;
    private ProgStlInfoShow progStlInfoShowSelected;
    private ProgStlInfoShow progStlInfoShowSel;
    private ProgStlInfoShow progStlInfoShowAdd;
    private ProgStlInfoShow progStlInfoShowUpd;
    private ProgStlInfoShow progStlInfoShowDel;

    private List<ProgStlInfoShow> progStlInfoShowList;

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
        try {
            this.progStlInfoShowList = new ArrayList<ProgStlInfoShow>();
            strStlType= EnumResType.RES_TYPE6.getCode();
            resetAction();

            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getCttInfoListByCttType_Status(
                            EnumResType.RES_TYPE0.getCode()
                            , EnumFlowStatus.FLOW_STATUS3.getCode());
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
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public void resetAction(){
        progStlInfoShowQry = new ProgStlInfoShow();
        progStlInfoShowSel = new ProgStlInfoShow();
        progStlInfoShowAdd = new ProgStlInfoShow();
        progStlInfoShowUpd = new ProgStlInfoShow();
        progStlInfoShowDel = new ProgStlInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
    }

    public void resetActionForAdd(){
        progStlInfoShowAdd =new ProgStlInfoShow();
        strSubmitType="Add";
    }

    public void setMaxNoPlusOne(String strQryTypePara){
        try {
            Integer intTemp;
            String strMaxId= progStlInfoService.getStrMaxStlId(strStlType);
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLSta"+ ToolUtil.getStrToday()+"001";
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
                progStlInfoShowQry.setId(strMaxId);
            }else if (strQryTypePara.equals("Add")) {
                progStlInfoShowAdd.setId(strMaxId);
            }else if (strQryTypePara.equals("Upd")) {
                progStlInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    public void onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progStlInfoShowQry.setStlType(strStlType);
            if(strQryFlag.equals("Qry")){
                progStlInfoShowQry.setStrStatusFlagBegin(progStlInfoShowQry.getFlowStatus());
                progStlInfoShowQry.setStrStatusFlagEnd(progStlInfoShowQry.getFlowStatus());
            }else if(strQryFlag.equals("Mng")){
                progStlInfoShowQry.setStrStatusFlagBegin(null);
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
            }else if(strQryFlag.equals("Check")){
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
            }else if(strQryFlag.equals("DoubleCheck")){
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
            }else if(strQryFlag.equals("Approve")){
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }else if(strQryFlag.equals("Print")){
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.progStlInfoShowList.clear();
            List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                    progStlInfoService.selectTkcttStlSMByStatusFlagBegin_End(progStlInfoShowQry);
            for(ProgStlInfoShow esISSOMPCUnit: progStlInfoShowConstructsTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())){
                        progStlInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progStlInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(String strSubmitTypePara,
                                     ProgStlInfoShow progStlInfoShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            progStlInfoShowPara.setCreatedByName(cttInfoService.getUserName(progStlInfoShowPara.getCreatedBy()));
            progStlInfoShowPara.setLastUpdByName(cttInfoService.getUserName(progStlInfoShowPara.getLastUpdBy()));
            // ��ѯ
            if(strSubmitTypePara.equals("Sel")){
                progStlInfoShowSel =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }else if(strSubmitTypePara.equals("Add")){
                progStlInfoShowAdd =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }else if(strSubmitTypePara.equals("Upd")){
                progStlInfoShowUpd =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }else if(strSubmitTypePara.equals("Del")){
                progStlInfoShowDel =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
    */
    private boolean submitPreCheck(ProgStlInfoShow progStlInfoShow){
        if (StringUtils.isEmpty(progStlInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(progStlInfoShow.getStlPkid())) {
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
            progStlInfoShowAdd.setStlType(strStlType);
            if(!submitPreCheck(progStlInfoShowAdd)){
                return;
            }
            List<ProgStlInfo> progStlInfoListTemp =
                    progStlInfoService.getInitStlListByModelShow(progStlInfoShowAdd);
            if(progStlInfoListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp=progStlInfoService.progStlInfoMngPreCheck(progStlInfoShowAdd);
            if(!"".equals(strTemp)){
                MessageUtil.addError(strTemp);
                return;
            }else{
                addRecordAction(progStlInfoShowAdd);
                resetActionForAdd();
            }

        }
        else if(strSubmitType.equals("Upd")){
            progStlInfoShowUpd.setStlType(strStlType);
            if(!submitPreCheck(progStlInfoShowUpd)){
                return;
            }
            updRecordAction(progStlInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            progStlInfoShowDel.setStlType(strStlType);
            progStlInfoService.delTkEstStlInfoAndItem(progStlInfoShowDel);
        }
        ProgStlInfoShow progStlInfoShowTemp =new ProgStlInfoShow();
        progStlInfoShowTemp.setStlType(strStlType);
        progStlInfoShowTemp.setStrStatusFlagBegin(null);
        progStlInfoShowTemp.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
        this.progStlInfoShowList =
                progStlInfoService.selectTkcttStlSMByStatusFlagBegin_End(progStlInfoShowTemp);
    }

    private void addRecordAction(ProgStlInfoShow progStlInfoShowPara){
        try {
            progStlInfoService.insertRecord(progStlInfoShowPara) ;
            // ����׼�˵���һ�׶ε������õ���һ�׶��У���Ϊ��ʼ�ۼ���
            progStlItemTkEstService.setFromLastStageAddUpToDataToThisStageBeginData(progStlInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlInfoShow progStlInfoShowPara){
        try {
            progStlInfoService.updateRecord(progStlInfoShowPara) ;
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ�Start*/
    public ProgStlInfoShow getProgStlInfoShowQry() {
        return progStlInfoShowQry;
    }

    public void setProgStlInfoShowQry(ProgStlInfoShow progStlInfoShowQry) {
        this.progStlInfoShowQry = progStlInfoShowQry;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public ProgStlItemTkEstService getProgStlItemTkEstService() {
        return progStlItemTkEstService;
    }

    public void setProgStlItemTkEstService(ProgStlItemTkEstService progStlItemTkEstService) {
        this.progStlItemTkEstService = progStlItemTkEstService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public List<ProgStlInfoShow> getProgStlInfoShowList() {
        return progStlInfoShowList;
    }

    public void setProgStlInfoShowList(List<ProgStlInfoShow> progStlInfoShowList) {
        this.progStlInfoShowList = progStlInfoShowList;
    }

    public ProgStlInfoShow getProgStlInfoShowSelected() {
        return progStlInfoShowSelected;
    }

    public void setProgStlInfoShowSelected(ProgStlInfoShow progStlInfoShowSelected) {
        this.progStlInfoShowSelected = progStlInfoShowSelected;
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

    public ProgStlInfoShow getProgStlInfoShowAdd() {
        return progStlInfoShowAdd;
    }

    public void setProgStlInfoShowAdd(ProgStlInfoShow progStlInfoShowAdd) {
        this.progStlInfoShowAdd = progStlInfoShowAdd;
    }

    public ProgStlInfoShow getProgStlInfoShowDel() {
        return progStlInfoShowDel;
    }

    public void setProgStlInfoShowDel(ProgStlInfoShow progStlInfoShowDel) {
        this.progStlInfoShowDel = progStlInfoShowDel;
    }

    public ProgStlInfoShow getProgStlInfoShowUpd() {
        return progStlInfoShowUpd;
    }

    public void setProgStlInfoShowUpd(ProgStlInfoShow progStlInfoShowUpd) {
        this.progStlInfoShowUpd = progStlInfoShowUpd;
    }

    public ProgStlInfoShow getProgStlInfoShowSel() {
        return progStlInfoShowSel;
    }

    public void setProgStlInfoShowSel(ProgStlInfoShow progStlInfoShowSel) {
        this.progStlInfoShowSel = progStlInfoShowSel;
    }

    /*�����ֶ�End*/
}
