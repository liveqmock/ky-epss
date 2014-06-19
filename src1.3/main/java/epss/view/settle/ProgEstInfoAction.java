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
 * Time: 下午4:53
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
            SelectItem selectItem=new SelectItem("","全部");
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
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
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
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
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
                case "Qry":// 查询
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                    break;
                case "Mng":// 维护
                    if(strSubmitTypePara.equals("Sel")){
                        progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                        rowSelectedFlag="true";
                    }else if(strSubmitTypePara.equals("Add")){
                    }else{
                        if(!strStatusFlagCode.equals("")&&
                                !strStatusFlagCode.equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                            MessageUtil.addInfo("数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
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
                case "Check": // 权限控制
                    rowSelectedFlag="true";
                    //根据流程环节,显示不同的退回的状态
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("本期数据还未录入完毕，您暂时不能进行审核操作！");
                    }else if(!strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS0.getCode())&&!
                            strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS1.getCode())){
                        MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                    }
                    break;
                case "DoubleCheck":
                    rowSelectedFlag="true";
                    //根据流程环节,显示不同的退回的状态
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("本期数据还未录入完毕，您暂时不能进行复核操作！");
                    }else if(strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS0.getCode())){
                        MessageUtil.addInfo("本期数据刚刚录入，还未审核，您暂时不能进行复核！");
                    }else if(!strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS1.getCode())&&!
                            strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS2.getCode())){
                        MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                    }
                    break;
                case "Approve":
                    rowSelectedFlag="true";
                    //根据流程环节,显示不同的退回的状态
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progEstInfoShowSel =(ProgEstInfoShow) BeanUtils.cloneBean(progEstInfoShowSelected);
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("本期数据还未录入完毕，您暂时不能进行批准操作！");
                    }else if(strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS0.getCode())){
                        MessageUtil.addInfo("本期数据刚刚录入，还未审核，您暂时不能进行批准！");
                    }else if(!strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS2.getCode())&&!
                            strStatusFlagCode.equals(EnumFlowStatus .FLOW_STATUS3.getCode())){
                        MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                    }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
    */
    private boolean submitPreCheck(ProgEstInfoShow progEstInfoShow){
        if (StringUtils.isEmpty(progEstInfoShow.getId())){
            MessageUtil.addError("请输入结算编号！");
            return false;
        }
        else if (StringUtils.isEmpty(progEstInfoShow.getTkcttInfoPkid())) {
            MessageUtil.addError("请输入总包合同！");
            return false;
        }
        return true ;
    }

    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            // 查询操作结果用
            String strStatusFlagBegin="";
            String strStatusFlagEnd="";
            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    progEstInfoShowSel.setFlowStatus(null);
                    progEstInfoShowSel.setFlowStatusRemark(null);
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据录入未完！");
                }
                // 初始
                strStatusFlagBegin=null;
                // 审核
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// 审核
                if(strPowerType.equals("CheckPass")){
                    // 状态标志：审核
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                }else if(strPowerType.equals("CheckFail")){
                    // 状态标志：初始
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：审核未过
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
                // 初始
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS0.getCode();
                // 审核
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    // 状态标志：复核
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // 这样写可以实现越级退回
                    progEstInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：复核未过
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
                // 审核
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS1.getCode();
                // 复核
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS2.getCode();
            } else if(strPowerType.contains("Approve")){// 批准
                if(strPowerType.equals("ApprovePass")){
                    // 状态标志：批准
                    progEstInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    updRecordAction(progEstInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");
                }else if(strPowerType.equals("ApproveFail")){
                    // 这样写可以实现越级退回
                    progEstInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：批准未过
                    progEstInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    updRecordAction(progEstInfoShowSel);

                    MessageUtil.addInfo("数据批准未过！");
                }
                // 复核
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS2.getCode();
                // 批准
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS3.getCode();
            }
            // 重新查询，已操作的结果
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
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * 提交维护权限
     */
    public void onClickForMngAction(){
        if(strSubmitType.equals("Add")){
            if(!submitPreCheck(progEstInfoShowAdd)){
                return;
            }
            List<ProgEstInfo> progInfoListTemp =
                    progEstInfoService.getProgEstInfoListByModel(progEstInfoShowAdd);
            if(progInfoListTemp.size()>0) {
                MessageUtil.addError("该记录已存在，请重新录入！");
                return;
            }
            // 不可存在上期不作为的数据
            String strStaQtyMaxStageNo = ToolUtil.getStrIgnoreNull(
                    progEstInfoService.getMaxStageNo(progEstInfoShowAdd.getTkcttInfoPkid()));
            String strStaQtyMaxStageNoStatus=ToolUtil.getStrIgnoreNull(
                    progEstInfoService.getFlowStatus(progEstInfoShowAdd.getTkcttInfoPkid(), strStaQtyMaxStageNo));

            if (progEstInfoShowAdd.getStageNo().compareTo(strStaQtyMaxStageNo)<=0){ //首先和自身比较期号大小，如果比自身小或者等于则不能录入
                MessageUtil.addError("应录入大于[" + strStaQtyMaxStageNo + "]期的总包统计数据!");
                return;
            }else {
                if (!strStaQtyMaxStageNo.equals("")&&(EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(strStaQtyMaxStageNoStatus) > 0||"".equals(strStaQtyMaxStageNoStatus))) {//判断是否有非批准状态的数据存在，如果有不能录入
                    MessageUtil.addError("总包统计结算第[" + strStaQtyMaxStageNo + "]期数据还未批准通过，不能录入新数据！");
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
            // 从批准了的上一阶段的数据拿到这一阶段中，作为开始累计数
            progEstItemService.setFromLastStageApproveDataToThisStageBeginData(progEstInfoShowPara);
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgEstInfoShow progEstInfoShowPara){
        try {
            progEstInfoService.updateRecord(progEstInfoShowPara) ;
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgEstInfoShow progEstInfoShowPara){
        try {
            // 删除详细数据
            int deleteItemsByInitStlTkcttEngNum= progEstItemService.deleteItemsByInitStlTkcttEng(
                    progEstInfoShowPara.getPkid());
            // 删除登记数据
            int deleteRecordOfRegistNum= progEstInfoService.deleteRecord(progEstInfoShowPara.getPkid()) ;
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
/*智能字段End*/
}
