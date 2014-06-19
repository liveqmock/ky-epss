package epss.view.contract;

import epss.common.enums.EnumFlowStatusRemark;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.CstplInfo;
import epss.repository.model.model_show.CstplInfoShow;
import epss.service.CstplInfoService;
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
public class CstplInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplInfoAction.class);
    @ManagedProperty(value = "#{cstplInfoService}")
    private CstplInfoService cstplInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private CstplInfoShow cstplInfoShowQry;
    private String strNotPassToStatus;
    private CstplInfoShow cstplInfoShowSel;
    private CstplInfoShow cstplInfoShowAdd;
    private CstplInfoShow cstplInfoShowUpd;
    private CstplInfoShow cstplInfoShowDel;
    private List<CstplInfoShow> cstplInfoShowList;    
	private CstplInfoShow cstplInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;

    // 画面之间传递过来的参数
    private String strTkcttInfoPkid;

    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // 从总包合同传递过来的总包合同号
        if (parammap.containsKey("strTkcttInfoPkid")) {
            strTkcttInfoPkid = parammap.get("strTkcttInfoPkid").toString();
        }else{
            strTkcttInfoPkid="";
        }
        initData();
    }
    public void initData() {
        this.cstplInfoShowList = new ArrayList<CstplInfoShow>();
        cstplInfoShowQry = new CstplInfoShow();
        cstplInfoShowQry.setTkcttInfoPkid(strTkcttInfoPkid);
        cstplInfoShowSel = new CstplInfoShow();
        cstplInfoShowAdd = new CstplInfoShow();
        cstplInfoShowAdd.setTkcttInfoPkid(strTkcttInfoPkid);
        cstplInfoShowUpd = new CstplInfoShow();
        cstplInfoShowDel = new CstplInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cstplInfoService.getStrMaxCstplInfoId();
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "CSTPL" + esCommon.getStrToday() + "001";
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
                cstplInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                cstplInfoShowUpd.setId(strMaxId);
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
                    cstplInfoShowQry.setFlowStatusBegin("");
                    cstplInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                case "MngPass":
                case "MngFail":
                    cstplInfoShowQry.setFlowStatusBegin("");
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "ApprovedQry":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
            }
            this.cstplInfoShowList.clear();
            cstplInfoShowQry.setTkcttInfoPkid(strTkcttInfoPkid);
            cstplInfoShowList = cstplInfoService.getCstplInfoListByFlowStatusBegin_End(cstplInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (cstplInfoShowList.isEmpty()) {
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

    public CstplInfo getCttInfoByPkid(String strPkid) {
        return cstplInfoService.getCstplInfoByPkid(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cstplInfoShowAdd = new CstplInfoShow();
        cstplInfoShowAdd.setTkcttInfoPkid(strTkcttInfoPkid);
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                     String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
            cstplInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(cstplInfoShowSelected.getCreatedBy()));
            cstplInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(cstplInfoShowSelected.getUpdatedBy()));
            switch (strPowerTypePara){
                case "Qry":// 查询
                cstplInfoShowSel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected); break;
                case "Mng":// 维护
                    if (strSubmitTypePara.equals("Sel")) {
                        cstplInfoShowSel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
                        rowSelectedFlag = "true";
                    } else if (strSubmitTypePara.equals("Add")) {
                        cstplInfoShowAdd = new CstplInfoShow();
                        cstplInfoShowAdd.setTkcttInfoPkid(strTkcttInfoPkid);
                        rowSelectedFlag = "false";
                    } else {
                        if (strSubmitTypePara.equals("Upd")) {
                            cstplInfoShowUpd = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
                            rowSelectedFlag = "false";
                        } else if (strSubmitTypePara.equals("Del")) {
                            cstplInfoShowDel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
                            rowSelectedFlag = "false";
                        }
                    }
                    break;
                default:
                    cstplInfoShowSel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
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
    private boolean submitPreCheck(CstplInfoShow cstplInfoShow) {
        if (StringUtils.isEmpty(cstplInfoShow.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cstplInfoShow.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cstplInfoShow.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
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
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cstplInfoShowSel.setFlowStatus(null);
                    cstplInfoShowSel.setFlowStatusRemark(null);
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：审核未过
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    cstplInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：复核未过
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // 这样写可以实现越级退回
                    cstplInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // 原因：批准未过
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    /*List<ProgInfo> progInfoListTemp =
                            esFlowService.selectIsUsedInQMPByCstplInfoPkid(cstplInfoShowSel.getPkid());
                    if (progInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("该数据已经被["
                                + ESEnum.valueOfAlias(progInfoListTemp.get(0).getInfoType()).getTitle()
                                + "]使用，数据批准未过,请慎重编辑！");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cstplInfoShowSel.getPkid()) > 0) {
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
                    if (!submitPreCheck(cstplInfoShowAdd)) {
                        return;
                    }
                    if (cstplInfoService.isExistInDb(cstplInfoShowAdd)) {
                        MessageUtil.addError("该记录已存在，请重新录入！");
                    } else {
                        cstplInfoService.insertRecord(cstplInfoShowAdd);
                        MessageUtil.addInfo("增加数据成功。");
                    }
                    break;
                case "Upd":
                    cstplInfoService.updateRecord(cstplInfoShowUpd);
                    MessageUtil.addInfo("更新数据成功。");
                    break;
                case "Del":
                    int deleteRecordNumOfCtt= cstplInfoService.deleteRecord(cstplInfoShowDel.getPkid());
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
    public CstplInfoService getCstplInfoService() {
        return cstplInfoService;
    }

    public void setCstplInfoService(CstplInfoService cstplInfoService) {
        this.cstplInfoService = cstplInfoService;
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

    public CstplInfoShow getCstplInfoShowSelected() {
        return cstplInfoShowSelected;
    }

    public void setCstplInfoShowSelected(CstplInfoShow cstplInfoShowSelected) {
        this.cstplInfoShowSelected = cstplInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public CstplInfoShow getCstplInfoShowQry() {
        return cstplInfoShowQry;
    }

    public void setCstplInfoShowQry(CstplInfoShow cstplInfoShowQry) {
        this.cstplInfoShowQry = cstplInfoShowQry;
    }

    public List<CstplInfoShow> getCstplInfoShowList() {
        return cstplInfoShowList;
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

    public CstplInfoShow getCstplInfoShowUpd() {
        return cstplInfoShowUpd;
    }

    public void setCstplInfoShowUpd(CstplInfoShow cstplInfoShowUpd) {
        this.cstplInfoShowUpd = cstplInfoShowUpd;
    }

    public CstplInfoShow getCstplInfoShowAdd() {
        return cstplInfoShowAdd;
    }

    public void setCstplInfoShowAdd(CstplInfoShow cstplInfoShowAdd) {
        this.cstplInfoShowAdd = cstplInfoShowAdd;
    }

    public CstplInfoShow getCstplInfoShowDel() {
        return cstplInfoShowDel;
    }

    public void setCstplInfoShowDel(CstplInfoShow cstplInfoShowDel) {
        this.cstplInfoShowDel = cstplInfoShowDel;
    }

    public CstplInfoShow getCstplInfoShowSel() {
        return cstplInfoShowSel;
    }

    public void setCstplInfoShowSel(CstplInfoShow cstplInfoShowSel) {
        this.cstplInfoShowSel = cstplInfoShowSel;
    }
    /*智能字段 End*/
}
