package epss.view.contract;

import epss.common.enums.EnumFlowStatusRemark;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.SubcttInfoShow;
import epss.service.ProgMatqtyInfoService;
import epss.service.ProgSubstlInfoService;
import epss.service.ProgWorkqtyInfoService;
import epss.service.SubcttInfoService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
public class SubcttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttInfoAction.class);
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progSubstlInfoService}")
    private ProgSubstlInfoService progSubstlInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private SubcttInfoShow subcttInfoShowQry;
    private String strNotPassToStatus;
    private SubcttInfoShow subcttInfoShowSel;
    private SubcttInfoShow subcttInfoShowAdd;
    private SubcttInfoShow subcttInfoShowUpd;
    private SubcttInfoShow subcttInfoShowDel;
    private List<SubcttInfoShow> subcttInfoShowList;
    private SubcttInfoShow subcttInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;

    // 画面之间传递过来的参数
    private String strCstplInfoPkid;

    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // 从成本计划传递过来的成本计划号
        if (parammap.containsKey("strCstplInfoPkid")) {
            strCstplInfoPkid = parammap.get("strCstplInfoPkid").toString();
        } else {// 总包合同页面上
            strCstplInfoPkid = "";
        }
        initData();
    }
    public void initData() {
        this.subcttInfoShowList = new ArrayList<SubcttInfoShow>();
        subcttInfoShowQry = new SubcttInfoShow();
        subcttInfoShowQry.setCstplInfoPkid(strCstplInfoPkid);
		subcttInfoShowSel = new SubcttInfoShow();
        subcttInfoShowAdd = new SubcttInfoShow();
        subcttInfoShowAdd.setCstplInfoPkid(strCstplInfoPkid);
        subcttInfoShowUpd = new SubcttInfoShow();
        subcttInfoShowDel = new SubcttInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = subcttInfoService.getStrMaxSubcttInfoId();
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "SUBCTT" + esCommon.getStrToday() + "001";
            } else {
                if (strMaxId.length() > 3) {
                    String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                    if (ToolUtil.strIsDigit(strTemp)) {
                        intTemp = Integer.parseInt(strTemp);
                        intTemp = intTemp + 1;
                        strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                    } else {
                        strMaxId += "001";
                    }
                }
            }
            if("Add".equals(strSubmitType)) {
                subcttInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                subcttInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            switch (strQryFlag){
                case "Qry":
                    subcttInfoShowQry.setFlowStatusBegin("");
                    subcttInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                case "MngPass":
                case "MngFail":
                    subcttInfoShowQry.setFlowStatusBegin("");
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "ApprovedQry":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.subcttInfoShowList.clear();
            subcttInfoShowQry.setCstplInfoPkid(strCstplInfoPkid);
            subcttInfoShowList = subcttInfoService.getSubcttInfoListByFlowStatusBegin_End(subcttInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (subcttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
            rowSelectedFlag = "false";
        } catch (Exception e) {
            logger.error("合同信息查询失败", e);
            MessageUtil.addError("合同信息查询失败");
        }
        return null;
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        subcttInfoShowAdd = new SubcttInfoShow();
        subcttInfoShowAdd.setCstplInfoPkid(strCstplInfoPkid);
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
            subcttInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(subcttInfoShowSelected.getCreatedBy()));
            subcttInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(subcttInfoShowSelected.getUpdatedBy()));
            switch (strPowerTypePara){
                case "Qry":// 查询
                    subcttInfoShowSel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected); break;
                case "Mng":// 维护
                    if (strSubmitTypePara.equals("Sel")) {
                        subcttInfoShowSel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                        rowSelectedFlag = "true";
                    } else if (strSubmitTypePara.equals("Add")) {
                        subcttInfoShowAdd = new SubcttInfoShow();
                        subcttInfoShowAdd.setCstplInfoPkid(strCstplInfoPkid);
                        rowSelectedFlag = "false";
                    } else {
                        if (strSubmitTypePara.equals("Upd")) {
                            subcttInfoShowUpd = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        } else if (strSubmitTypePara.equals("Del")) {
                            subcttInfoShowDel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        }
                    }
                    break;
                default:
                    subcttInfoShowSel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                    rowSelectedFlag = "true";
            }
            //根据流程环节,显示不同的退回的状态
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(SubcttInfoShow subcttInfoShowPara) {
        if (StringUtils.isEmpty(subcttInfoShowPara.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        if (StringUtils.isEmpty(subcttInfoShowPara.getSignPartPkidA())) {
            MessageUtil.addError("请输入签订甲方！");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getSignPartPkidB())) {
            MessageUtil.addError("请输入签订乙方！");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getStartDate())) {
            MessageUtil.addError("请输入合同开始时间！");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getEndDate())) {
            MessageUtil.addError("请输入合同截止时间！");
            return false;
        }
        return true;
    }

    /**
     * 根据权限进行审核
     *
     * @param strPowerTypePara
     */
    public void onClickForPowerAction(String strPowerTypePara) {
        try {
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    subcttInfoShowSel.setFlowStatus(null);
                    subcttInfoShowSel.setFlowStatusRemark(null);
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：审核未过
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    subcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：复核未过
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // 这样写可以实现越级退回
                    subcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：批准未过
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    /*List<ProgInfo> progInfoListTemp =
                            progWorkqtyInfoService.selectIsUsedInQMPBySubcttPkid(subcttInfoShowSel.getPkid());
                    if (progInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("该数据已经被["
                                + ESEnum.valueOfAlias(progInfoListTemp.get(0).getInfoType()).getTitle()
                                + "]使用，数据批准未过,请慎重编辑！");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                subcttInfoShowSel.getPkid()) > 0) {
                            MessageUtil.addInfo("该数据已经被[" + ESEnum.valueOfAlias(strCttTypeTemp).getTitle()
                                    + "]使用，数据批准未过,请慎重编辑！");
                        } else {
                            MessageUtil.addInfo("数据批准未过！");
                        }
                    }*/
                }
            }
            // 重新查询，已操作的结果
            onQueryAction(strPowerTypePara,"false");
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 提交维护权限
     */
    public void onClickForMngAction() {
        try {
            switch (strSubmitType){
                case "Add":
                    if (!submitPreCheck(subcttInfoShowAdd)) {
                        return;
                    }
                    if (subcttInfoService.isExistInDb(subcttInfoShowAdd)) {
                        MessageUtil.addError("该记录已存在，请重新录入！");
                    } else {
                        subcttInfoService.insertRecord(subcttInfoShowAdd);
                        MessageUtil.addInfo("增加数据成功。");
                    }
                    break;
                case "Upd":
                    subcttInfoService.updateRecord(subcttInfoShowUpd);
                    MessageUtil.addInfo("更新数据成功。");
                    break;
                case "Del":
                    int deleteRecordNumOfCtt= subcttInfoService.deleteRecord(subcttInfoShowDel.getPkid());
                    if (deleteRecordNumOfCtt<=0){
                        MessageUtil.addInfo("该记录已删除。");
                    }else {
                        MessageUtil.addInfo("删除数据成功。");
                    }
            }
            onQueryAction("Mng", "false");
        } catch (Exception e) {
            logger.error("提交数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段 Start*/
    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public SubcttInfoShow getSubcttInfoShowSelected() {
        return subcttInfoShowSelected;
    }

    public void setSubcttInfoShowSelected(SubcttInfoShow subcttInfoShowSelected) {
        this.subcttInfoShowSelected = subcttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public SubcttInfoShow getSubcttInfoShowQry() {
        return subcttInfoShowQry;
    }

    public void setSubcttInfoShowQry(SubcttInfoShow subcttInfoShowQry) {
        this.subcttInfoShowQry = subcttInfoShowQry;
    }

    public List<SubcttInfoShow> getSubcttInfoShowList() {
        return subcttInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public SubcttInfoShow getSubcttInfoShowUpd() {
        return subcttInfoShowUpd;
    }

    public void setSubcttInfoShowUpd(SubcttInfoShow subcttInfoShowUpd) {
        this.subcttInfoShowUpd = subcttInfoShowUpd;
    }

    public SubcttInfoShow getSubcttInfoShowAdd() {
        return subcttInfoShowAdd;
    }

    public void setSubcttInfoShowAdd(SubcttInfoShow subcttInfoShowAdd) {
        this.subcttInfoShowAdd = subcttInfoShowAdd;
    }

    public SubcttInfoShow getSubcttInfoShowDel() {
        return subcttInfoShowDel;
    }

    public void setSubcttInfoShowDel(SubcttInfoShow subcttInfoShowDel) {
        this.subcttInfoShowDel = subcttInfoShowDel;
    }

    public SubcttInfoShow getSubcttInfoShowSel() {
        return subcttInfoShowSel;
    }

    public void setSubcttInfoShowSel(SubcttInfoShow subcttInfoShowSel) {
        this.subcttInfoShowSel = subcttInfoShowSel;
    }

    public ProgWorkqtyInfoService getProgWorkqtyInfoService() {
        return progWorkqtyInfoService;
    }

    public void setProgWorkqtyInfoService(ProgWorkqtyInfoService progWorkqtyInfoService) {
        this.progWorkqtyInfoService = progWorkqtyInfoService;
    }

    public ProgMatqtyInfoService getProgMatqtyInfoService() {
        return progMatqtyInfoService;
    }

    public void setProgMatqtyInfoService(ProgMatqtyInfoService progMatqtyInfoService) {
        this.progMatqtyInfoService = progMatqtyInfoService;
    }

    public ProgSubstlInfoService getProgSubstlInfoService() {
        return progSubstlInfoService;
    }

    public void setProgSubstlInfoService(ProgSubstlInfoService progSubstlInfoService) {
        this.progSubstlInfoService = progSubstlInfoService;
    }
/*智能字段 End*/
}
