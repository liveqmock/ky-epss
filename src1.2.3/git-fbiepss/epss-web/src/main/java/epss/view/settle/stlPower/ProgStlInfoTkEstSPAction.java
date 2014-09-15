package epss.view.settle.stlPower;

import epss.common.enums.*;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlInfoTkEstSPAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoTkEstSPAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progEstItemService}")
    private ProgEstItemService progEstItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
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
    /*控制维护画面层级部分的显示*/
    private String strCstplPkidRendered;
    private String strControlsRenderedForCheck;

    private String strCallPageNameFlag;
    private StyleModel styleModel;

    private OperRes operRes;

    @PostConstruct
    public void init() {
        this.progStlInfoShowList = new ArrayList<ProgStlInfoShow>();
        strStlType= EnumResType.RES_TYPE6.getCode();
        resetAction();

        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String strResPkidTemp = parammap.get("strResPkid").toString();
        operRes=operResService.getOperResByPkid(strResPkidTemp);

        //自己拥有权限的分包合同
        List<OperResShow> operResShowListTemp =
                operResService.getInfoListByOperFlowPkid(
                        strStlType,
                        EnumFlowStatus.FLOW_STATUS0.getCode());
        tkcttList = new ArrayList<SelectItem>();
        if (operResShowListTemp.size() > 0) {
            SelectItem selectItem = new SelectItem("", "全部");
            tkcttList.add(selectItem);
            for (OperResShow operResShowUnit : operResShowListTemp) {
                selectItem = new SelectItem();
                selectItem.setValue(operResShowUnit.getInfoPkid());
                selectItem.setLabel(operResShowUnit.getInfoPkidName());
                tkcttList.add(selectItem);
            }
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
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
        }
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            progStlInfoShowQry.setStlType(strStlType);
            progStlInfoShowQry.setStrStatusFlagBegin(null);
            progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
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
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public void selectRecordAction(String strSubmitTypePara,
                                     ProgStlInfoShow progStlInfoShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progStlInfoShowPara.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInStatusFlaglist(progStlInfoShowPara.getFlowStatus());
            progStlInfoShowPara.setCreatedByName(ToolUtil.getUserName(progStlInfoShowPara.getCreatedBy()));
            progStlInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progStlInfoShowPara.getLastUpdBy()));
            if(strSubmitTypePara.equals("Sel")){
                progStlInfoShowSel =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }else if(strSubmitTypePara.equals("Add")){
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
     * 必须输入项目检查
    */
    private boolean submitPreCheck(ProgStlInfoShow progStlInfoShow){
        if (StringUtils.isEmpty(progStlInfoShow.getId())){
            MessageUtil.addError("请输入结算编号！");
            return false;
        }
        else if (StringUtils.isEmpty(progStlInfoShow.getStlPkid())) {
            MessageUtil.addError("请输入总包合同！");
            return false;
        }else if (StringUtils.isEmpty(progStlInfoShow.getPeriodNo())){
            MessageUtil.addError("请输入期数编码！");
            return false;
        }
        return true ;
    }

    /**
     * 提交维护权限
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
                MessageUtil.addError("该记录已存在，请重新录入！");
                return;
            }
            String strTemp=progStlInfoService.subCttStlCheckForMng(
                    EnumResType.RES_TYPE6.getCode(),
                    progStlInfoShowAdd.getStlPkid(),
                    progStlInfoShowAdd.getPeriodNo());
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
            delRecordAction(progStlInfoShowDel);
        }
        ProgStlInfoShow progStlInfoShowTemp =new ProgStlInfoShow();
        progStlInfoShowTemp.setStlType(strStlType);
        progStlInfoShowTemp.setStrStatusFlagBegin(null);
        progStlInfoShowTemp.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
        this.progStlInfoShowList.clear();
        List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                progStlInfoService.selectTkcttStlSMByStatusFlagBegin_End(progStlInfoShowTemp);
        for (SelectItem itemUnit : tkcttList) {
            for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                    progStlInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }

    private void addRecordAction(ProgStlInfoShow progStlInfoShowPara){
        try {
            progStlInfoService.insertRecord(progStlInfoShowPara) ;
            // 从批准了的上一阶段的数据拿到这一阶段中，作为开始累计数
            progEstItemService.setFromLastStageApproveDataToThisStageBeginData(progStlInfoShowPara);
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlInfoShow progStlInfoShowPara){
        try {
            progStlInfoService.updateRecord(progStlInfoShowPara) ;
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgStlInfoShow progStlInfoShowPara){
        try {
            // 删除详细数据
            int deleteItemsByInitStlTkcttEngNum= progEstItemService.deleteItemsByInitStlTkcttEng(
                    progStlInfoShowPara.getStlPkid(),
                    progStlInfoShowPara.getPeriodNo());
            // 删除登记数据
            int deleteRecordOfRegistNum= progStlInfoService.deleteRecord(progStlInfoShowPara.getPkid()) ;
            if (deleteItemsByInitStlTkcttEngNum<=0&&deleteRecordOfRegistNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段Start*/
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

    public ProgEstItemService getProgEstItemService() {
        return progEstItemService;
    }

    public void setProgEstItemService(ProgEstItemService progEstItemService) {
        this.progEstItemService = progEstItemService;
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

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
    /*智能字段End*/
}
