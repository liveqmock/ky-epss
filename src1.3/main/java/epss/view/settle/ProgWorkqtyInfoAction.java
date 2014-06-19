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
 * Time: 下午4:53
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
            SelectItem selectItem=new SelectItem("","全部");
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
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
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
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progWorkqtyInfoShowSelected.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInFlowStatuslist(progWorkqtyInfoShowSelected.getFlowStatus());
            progWorkqtyInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(progWorkqtyInfoShowSelected.getCreatedBy()));
            progWorkqtyInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(progWorkqtyInfoShowSelected.getUpdatedBy()));

            switch (strPowerTypePara){
                case "Qry":// 查询
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                    break;
                case "Mng":// 维护
                    if(strSubmitTypePara.equals("Sel")){
                        progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
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
                            progWorkqtyInfoShowUpd =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                        }else if(strSubmitTypePara.equals("Del")){
                            rowSelectedFlag="false";
                            progWorkqtyInfoShowDel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
                        }
                    }
                    break;
                case "Check": // 权限控制
                    rowSelectedFlag="true";
                    //根据流程环节,显示不同的退回的状态
                    esFlowControl.setFlowStatusListByPower(strPowerTypePara) ;
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
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
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
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
                    progWorkqtyInfoShowSel =(ProgWorkqtyInfoShow) BeanUtils.cloneBean(progWorkqtyInfoShowSelected);
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
    private boolean submitPreCheck(ProgWorkqtyInfoShow progWorkqtyInfoShow){
        if (StringUtils.isEmpty(progWorkqtyInfoShow.getId())){
            MessageUtil.addError("请输入结算编号！");
            return false;
        }
        else if (StringUtils.isEmpty(progWorkqtyInfoShow.getSubcttInfoPkid())) {
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
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    progWorkqtyInfoShowSel.setFlowStatus(null);
                    progWorkqtyInfoShowSel.setFlowStatusRemark(null);
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据录入未完！");
                }
                // 初始
                strStatusFlagBegin=null;
                // 审核
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// 审核
                if(strPowerType.equals("CheckPass")){
                    // 状态标志：审核
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                }else if(strPowerType.equals("CheckFail")){
                    // 状态标志：初始
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：审核未过
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
                // 初始
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS0.getCode();
                // 审核
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    // 状态标志：复核
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // 这样写可以实现越级退回
                    progWorkqtyInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：复核未过
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
                // 审核
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS1.getCode();
                // 复核
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS2.getCode();
            } else if(strPowerType.contains("Approve")){// 批准
                if(strPowerType.equals("ApprovePass")){
                    // 状态标志：批准
                    progWorkqtyInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");
                }else if(strPowerType.equals("ApproveFail")){
                    // 这样写可以实现越级退回
                    progWorkqtyInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：批准未过
                    progWorkqtyInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    updRecordAction(progWorkqtyInfoShowSel);

                    MessageUtil.addInfo("数据批准未过！");
                }
                // 复核
                strStatusFlagBegin=EnumFlowStatus.FLOW_STATUS2.getCode();
                // 批准
                strStatusFlagEnd=EnumFlowStatus.FLOW_STATUS3.getCode();
            }
            // 重新查询，已操作的结果
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
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * 提交维护权限
     */
    public void onClickForMngAction(){
        if(strSubmitType.equals("Add")){
            if(!submitPreCheck(progWorkqtyInfoShowAdd)){
                return;
            }
            List<ProgWorkqtyInfo> progInfoListTemp =
                    progWorkqtyInfoService.getProgWorkqtyInfoListByModel(progWorkqtyInfoShowAdd);
            if(progInfoListTemp.size()>0) {
                MessageUtil.addError("该记录已存在，请重新录入！");
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
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgWorkqtyInfoShow progWorkqtyInfoShowPara){
        try {
            progWorkqtyInfoService.updateRecord(progWorkqtyInfoShowPara) ;
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgWorkqtyInfoShow progWorkqtyInfoShowPara){
        try {
            // 删除详细数据
            int deleteItemsByInitStlSubcttEngNum= progWorkqtyItemService.deleteItemsByInitStlSubcttEng(
                    progWorkqtyInfoShowPara.getPkid());
            // 删除登记数据
            int deleteRecordOfRegistNum= progWorkqtyInfoService.deleteRecord(progWorkqtyInfoShowPara.getPkid()) ;
            if (deleteItemsByInitStlSubcttEngNum<=0&&deleteRecordOfRegistNum<=0){
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
    /*智能字段End*/
}
