package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.CttInfoShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsInitStl;
import epss.service.EsCttInfoService;
import epss.service.EsCttItemService;
import epss.service.EsInitPowerService;
import epss.service.common.EsFlowService;
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
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private CttInfoShow cttInfoShowQry;
    private String strNotPassToStatus;
    private CttInfoShow cttInfoShowSelected;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;
    private List<CttInfoShow> cttInfoShowList;

    private String strSubmitType;
    private String rowSelectedFlag;

    // 画面之间传递过来的参数
    private String strBelongToPkid;

    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // 从总包合同传递过来的总包合同号
        if (parammap.containsKey("strTkCttPkid")) {
            strBelongToPkid = parammap.get("strTkCttPkid").toString();
        }
        initData();
    }
    public void initData() {
        this.cttInfoShowList = new ArrayList<CttInfoShow>();
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowQry.setParentPkid(strBelongToPkid);
        cttInfoShowSel = new CttInfoShow();
        cttInfoShowSel.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowSel.setParentPkid(strBelongToPkid);
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowAdd.setParentPkid(strBelongToPkid);
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowUpd.setParentPkid(strBelongToPkid);
        cttInfoShowDel = new CttInfoShow();
        cttInfoShowDel.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowDel.setParentPkid(strBelongToPkid);
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setStatusFlagListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = esCttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE1.getCode());
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
            cttInfoShowAdd.setId(strMaxId);
            cttInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            if (strQryFlag.equals("Qry")) {

            } else if (strQryFlag.contains("Mng")) {
                cttInfoShowQry.setStrStatusFlagBegin(null);
                cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
            } else if (strQryFlag.contains("Check")) {
                if (strQryFlag.contains("DoubleCheck")) {
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                }
            }  else if (strQryFlag.contains("Approve")) {
                if (strQryFlag.equals("ApprovedQry")) {
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                }
            }
            this.cttInfoShowList.clear();
            cttInfoShowQry.setParentPkid(strBelongToPkid);
            cttInfoShowList = esFlowService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (cttInfoShowList.isEmpty()) {
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

    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return esCttInfoService.getCttInfoByPkId(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE1.getCode());
        cttInfoShowAdd.setParentPkid(strBelongToPkid);
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setStatusFlagListByPower(strPowerTypePara);
            cttInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(cttInfoShowSelected.getCreatedBy()));
            cttInfoShowSelected.setLastUpdByName(esCommon.getOperNameByOperId(cttInfoShowSelected.getLastUpdBy()));
            // 查询
            if (strPowerTypePara.equals("Qry")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
            } else// 维护
            if (strPowerTypePara.equals("Mng")) {
                if (strSubmitTypePara.equals("Sel")) {
                    cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                    rowSelectedFlag = "true";
                } else if (strSubmitTypePara.equals("Add")) {
                    cttInfoShowAdd = new CttInfoShow();
                    cttInfoShowAdd.setParentPkid(strBelongToPkid);
                    rowSelectedFlag = "false";
                } else {
                    if (strSubmitTypePara.equals("Upd")) {
                        cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                        rowSelectedFlag = "false";
                    } else if (strSubmitTypePara.equals("Del")) {
                        cttInfoShowDel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                        rowSelectedFlag = "false";
                    }
                }
            } else {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                rowSelectedFlag = "true";
                //根据流程环节,显示不同的退回的状态
                esFlowControl.setStatusFlagListByPower(strPowerTypePara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShow) {
        if (StringUtils.isEmpty(cttInfoShow.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShow.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShow.getSignDate())) {
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
            String strPowerTypeTemp="";
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    esFlowControl.mngFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    esFlowControl.mngNotFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // 原因：审核通过
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // 原因：审核未过
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
                strPowerTypeTemp="Check";
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    cttInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // 原因：复核未过
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
                strPowerTypeTemp="DoubleCheck";
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // 原因：批准通过
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // 检查是否被使用
                    String strCttTypeTemp = "";
                    if (cttInfoShowSel.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE1.getCode();
                    } else if (cttInfoShowSel.getCttType().equals(ESEnum.ITEMTYPE1.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE2.getCode();
                    }

                    // 这样写可以实现越级退回
                    cttInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // 原因：批准未过
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());

                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);

                    List<EsInitStl> esInitStlListTemp =
                            esFlowService.selectIsUsedInQMPBySubcttPkid(cttInfoShowSel.getPkid());
                    if (esInitStlListTemp.size() > 0) {
                        MessageUtil.addInfo("该数据已经被["
                                + ESEnum.valueOfAlias(esInitStlListTemp.get(0).getStlType()).getTitle()
                                + "]使用，数据批准未过,请慎重编辑！");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cttInfoShowSel.getPkid()) > 0) {
                            MessageUtil.addInfo("该数据已经被[" + ESEnum.valueOfAlias(strCttTypeTemp).getTitle()
                                    + "]使用，数据批准未过,请慎重编辑！");
                        } else {
                            MessageUtil.addInfo("数据批准未过！");
                        }
                    }
                }
                strPowerTypeTemp="Approve";
            }
            // 重新查询，已操作的结果
            onQueryAction(strPowerTypeTemp,"false");
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Add")) {
            cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE1.getCode());
            if (!submitPreCheck(cttInfoShowAdd)) {
                return;
            }
            if (esCttInfoService.isExistInDb(cttInfoShowAdd)) {
                MessageUtil.addError("该记录已存在，请重新录入！");
            } else {
                addRecordAction(cttInfoShowAdd);
            }
        } else if (strSubmitType.equals("Upd")) {
            cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE1.getCode());
            updRecordAction(cttInfoShowUpd);
        } else if (strSubmitType.equals("Del")) {
            cttInfoShowDel.setCttType(ESEnum.ITEMTYPE1.getCode());
            deleteRecordAction(cttInfoShowDel);
        }
        onQueryAction("Mng","false");
    }

    private void addRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            if (cttInfoShowPara.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                cttInfoShowPara.setParentPkid("ROOT");
            }
            esCttInfoService.insertRecord(cttInfoShowPara);
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            esCttInfoService.updateRecord(cttInfoShowPara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE1.getCode());
            int deleteRecordNumOfCttItem=esCttItemService.deleteRecord(cttInfoShowPara);
            int deleteRecordNumOfCtt= esCttInfoService.deleteRecord(cttInfoShowPara.getPkid());
            int deleteRecordNumOfPower=esInitPowerService.deleteRecord(
                    cttInfoShowPara.getCttType(),
                    cttInfoShowPara.getPkid(),
                    "NULL");
            if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfPower<=0&&deleteRecordNumOfCttItem<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段 Start*/
    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public CttInfoShow getCttInfoShowSelected() {
        return cttInfoShowSelected;
    }

    public void setCttInfoShowSelected(CttInfoShow cttInfoShowSelected) {
        this.cttInfoShowSelected = cttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public CttInfoShow getCttInfoShowQry() {
        return cttInfoShowQry;
    }

    public void setCttInfoShowQry(CttInfoShow cttInfoShowQry) {
        this.cttInfoShowQry = cttInfoShowQry;
    }

    public List<CttInfoShow> getCttInfoShowList() {
        return cttInfoShowList;
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

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowAdd() {
        return cttInfoShowAdd;
    }

    public void setCttInfoShowAdd(CttInfoShow cttInfoShowAdd) {
        this.cttInfoShowAdd = cttInfoShowAdd;
    }

    public CttInfoShow getCttInfoShowDel() {
        return cttInfoShowDel;
    }

    public void setCttInfoShowDel(CttInfoShow cttInfoShowDel) {
        this.cttInfoShowDel = cttInfoShowDel;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }
    /*智能字段 End*/
}
