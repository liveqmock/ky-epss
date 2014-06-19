package epss.view.contract;

import epss.common.enums.EnumFlowStatusRemark;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.TkcttInfoShow;
import epss.repository.model.TkcttInfo;

import epss.service.TkcttInfoService;
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
public class TkcttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttInfoAction.class);
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private TkcttInfoShow tkcttInfoShowQry;
    private String strNotPassToStatus;
    private TkcttInfoShow tkcttInfoShowSel;
    private TkcttInfoShow tkcttInfoShowAdd;
    private TkcttInfoShow tkcttInfoShowUpd;
    private TkcttInfoShow tkcttInfoShowDel;
    private List<TkcttInfoShow> tkcttInfoShowList;
    private TkcttInfoShow tkcttInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        initData();
    }
    public void initData() {
        this.tkcttInfoShowList = new ArrayList<TkcttInfoShow>();
        tkcttInfoShowQry = new TkcttInfoShow();
        tkcttInfoShowSel = new TkcttInfoShow();
        tkcttInfoShowAdd = new TkcttInfoShow();
        tkcttInfoShowUpd = new TkcttInfoShow();
        tkcttInfoShowDel = new TkcttInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = tkcttInfoService.getStrMaxTkcttInfoId();
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "TKCTT" + esCommon.getStrToday() + "001";
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
                tkcttInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                tkcttInfoShowUpd.setId(strMaxId);
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
                    tkcttInfoShowQry.setFlowStatusBegin("");
                    tkcttInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                case "MngPass":
                case "MngFail":
                    tkcttInfoShowQry.setFlowStatusBegin("");
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "ApprovedQry":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.tkcttInfoShowList.clear();
            tkcttInfoShowList = tkcttInfoService.getTkcttInfoListByFlowStatusBegin_End(tkcttInfoShowQry);
            if(strQryMsgOutPara.equals("true"))  {
                if (tkcttInfoShowList.isEmpty()) {
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

    public TkcttInfo getCttInfoByPkId(String strPkid) {
        return tkcttInfoService.getTkcttInfoByPkid(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        tkcttInfoShowAdd = new TkcttInfoShow();
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
            // 查询
            tkcttInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(tkcttInfoShowSelected.getCreatedBy()));
            tkcttInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(tkcttInfoShowSelected.getUpdatedBy()));
            switch (strPowerTypePara){
                case "Qry":
                    tkcttInfoShowSel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                    break;
                case "Mng":// 维护
                    if (strSubmitTypePara.equals("Sel")) {
                        tkcttInfoShowSel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                        rowSelectedFlag = "true";
                    } else if (strSubmitTypePara.equals("Add")) {
                        tkcttInfoShowAdd = new TkcttInfoShow();
                        rowSelectedFlag = "false";
                    } else {
                        if (strSubmitTypePara.equals("Upd")) {
                            tkcttInfoShowUpd = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        } else if (strSubmitTypePara.equals("Del")) {
                            tkcttInfoShowDel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        }
                    }
                    break;
                default:
                    tkcttInfoShowSel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
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
    private boolean submitPreCheck(TkcttInfoShow tkcttInfoShowPara) {
        if (StringUtils.isEmpty(tkcttInfoShowPara.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        if (StringUtils.isEmpty(tkcttInfoShowPara.getSignPartPkidA())) {
            MessageUtil.addError("请输入签订甲方！");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getSignPartPkidB())) {
            MessageUtil.addError("请输入签订乙方！");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getStartDate())) {
            MessageUtil.addError("请输入合同开始时间！");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getEndDate())) {
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
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    tkcttInfoShowSel.setFlowStatus(null);
                    tkcttInfoShowSel.setFlowStatusRemark(null);
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：审核未过
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    tkcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：复核未过
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");
                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // 这样写可以实现越级退回
                    tkcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：批准未过
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    /*List<ProgInfo> progInfoListTemp =
                            esFlowService.selectIsUsedInQMPByTkcttInfoPkid(tkcttInfoShowSel.getPkid());
                    if (progInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("该数据已经被["
                                + ESEnum.valueOfAlias(progInfoListTemp.get(0).getInfoType()).getTitle()
                                + "]使用，数据批准未过,请慎重编辑！");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                tkcttInfoShowSel.getPkid()) > 0) {
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
                    if (!submitPreCheck(tkcttInfoShowAdd)) {
                        return;
                    }
                    if (tkcttInfoService.isExistInDb(tkcttInfoShowAdd)) {
                        MessageUtil.addError("该记录已存在，请重新录入！");
                    } else {
                        tkcttInfoService.insertRecord(tkcttInfoShowAdd);
                        MessageUtil.addInfo("增加数据成功。");
                    }
                    break;
                case "Upd":
                    tkcttInfoService.updateRecord(tkcttInfoShowUpd);
                    MessageUtil.addInfo("更新数据成功。");
                    break;
                case "Del":
                    int deleteRecordNumOfCtt= tkcttInfoService.deleteRecord(tkcttInfoShowDel.getPkid());
                    if (deleteRecordNumOfCtt<=0){
                        MessageUtil.addInfo("该记录已删除。");
                    }else {
                        MessageUtil.addInfo("删除数据成功。");
                    }
            }
            onQueryAction("Mng","false");
        } catch (Exception e) {
            logger.error("提交数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段 Start*/
    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
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

    public TkcttInfoShow getTkcttInfoShowSelected() {
        return tkcttInfoShowSelected;
    }

    public void setTkcttInfoShowSelected(TkcttInfoShow tkcttInfoShowSelected) {
        this.tkcttInfoShowSelected = tkcttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public TkcttInfoShow getTkcttInfoShowQry() {
        return tkcttInfoShowQry;
    }

    public void setTkcttInfoShowQry(TkcttInfoShow tkcttInfoShowQry) {
        this.tkcttInfoShowQry = tkcttInfoShowQry;
    }

    public List<TkcttInfoShow> getTkcttInfoShowList() {
        return tkcttInfoShowList;
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

    public TkcttInfoShow getTkcttInfoShowUpd() {
        return tkcttInfoShowUpd;
    }

    public void setTkcttInfoShowUpd(TkcttInfoShow tkcttInfoShowUpd) {
        this.tkcttInfoShowUpd = tkcttInfoShowUpd;
    }

    public TkcttInfoShow getTkcttInfoShowAdd() {
        return tkcttInfoShowAdd;
    }

    public void setTkcttInfoShowAdd(TkcttInfoShow tkcttInfoShowAdd) {
        this.tkcttInfoShowAdd = tkcttInfoShowAdd;
    }

    public TkcttInfoShow getTkcttInfoShowDel() {
        return tkcttInfoShowDel;
    }

    public void setTkcttInfoShowDel(TkcttInfoShow tkcttInfoShowDel) {
        this.tkcttInfoShowDel = tkcttInfoShowDel;
    }

    public TkcttInfoShow getTkcttInfoShowSel() {
        return tkcttInfoShowSel;
    }

    public void setTkcttInfoShowSel(TkcttInfoShow tkcttInfoShowSel) {
        this.tkcttInfoShowSel = tkcttInfoShowSel;
    }

    /*智能字段 End*/
}
