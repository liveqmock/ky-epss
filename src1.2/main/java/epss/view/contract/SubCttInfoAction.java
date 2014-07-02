package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttItem;
import epss.repository.model.EsInitStl;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class SubCttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(SubCttInfoAction.class);
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
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;
    private List<CttInfoShow> cttInfoShowList;
    private CttInfoShow cttInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;

    // 画面之间传递过来的参数
    private String strBelongToPkid;

    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;
    //实现甲供材情况
    private CttItemShow cttItemShow;
    //验证分包合同编号和名称是否重复的提示信息
    String strWarnMsg;


    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // 从成本计划传递过来的成本计划号
        if (parammap.containsKey("strCstplPkid")) {
            strBelongToPkid = parammap.get("strCstplPkid").toString();
        } else {// 总包合同页面上
            strBelongToPkid = null;
        }
        initData();
    }

    public void initData() {
        this.cttInfoShowList = new ArrayList<CttInfoShow>();
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(ESEnum.ITEMTYPE2.getCode());
        cttInfoShowQry.setParentPkid(strBelongToPkid);
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE2.getCode());
        cttInfoShowAdd.setParentPkid(strBelongToPkid);
        cttInfoShowSel = new CttInfoShow();
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowDel = new CttInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setStatusFlagListByPower("Qry");
        rowSelectedFlag = "false";
    }

    //分包合同录入时，验证分包合同编号是否合法重复
    public void validSubCttId() {
        strWarnMsg = "";
        String subCttIdFromPage = cttInfoShowAdd.getId();
        if (!(subCttIdFromPage.matches("^[a-zA-Z0-9]+$"))) {
            strWarnMsg = "合同编号应以字母数字开头，请重新输入。";
        } else {
            if (esCttInfoService.IdisExistInDb(cttInfoShowAdd)) {
                strWarnMsg = "该合同编号已存在，请重新输入。";

            }
        }
    }

    //分包合同录入时，验证分包合同编号是否合法重复
    public void validSubCttName() {
        strWarnMsg = "";

        if (esCttInfoService.NameisExistInDb(cttInfoShowAdd)) {
            strWarnMsg = "该合同编号已存在，请重新输入。";

        }

    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = esCttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE2.getCode());
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

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE2.getCode());
        cttInfoShowAdd.setParentPkid(strBelongToPkid);
        rowSelectedFlag = "false";
        strWarnMsg = "";
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
            } else if (strPowerTypePara.equals("Mng")) { // 维护
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
    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if (StringUtils.isEmpty(cttInfoShowPara.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        if (StringUtils.isEmpty(cttInfoShowPara.getSignPartA())) {
            MessageUtil.addError("请输入签订甲方！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignPartB())) {
            MessageUtil.addError("请输入签订乙方！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttStartDate())) {
            MessageUtil.addError("请输入合同开始时间！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttEndDate())) {
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
                    List<EsCttItem> esCttItemList = esCttItemService.getEsItemList(cttInfoShowSel.getCttType(), cttInfoShowSel.getPkid());
                    if (esCttItemList.isEmpty()) {
                        MessageUtil.addInfo("无详细内容！");
                        rowSelectedFlag = "false";
                        return;
                    }
                    int checkPriceZero = 0;
                    for (EsCttItem esCttItemTemp : esCttItemList) {
                        if (!(esCttItemTemp.getSignPartAPrice() == null)) {
                            if (!(esCttItemTemp.getSignPartAPrice().equals(new BigDecimal(0)))) {
                                checkPriceZero += 1;
                                break;
                            }
                        }
                    }
                    if (checkPriceZero == 0) {
                        cttInfoShowSel.setType("0");
                        cttInfoShowSel.setPkid(cttInfoShowSel.getPkid());
                        esCttInfoService.updateByPKid(cttInfoShowSel);
                    } else {
                        cttInfoShowSel.setType("2");
                        cttInfoShowSel.setPkid(cttInfoShowSel.getPkid());
                        esCttInfoService.updateByPKid(cttInfoShowSel);
                    }
                    esFlowControl.mngFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfoShowSel.setType("");
                    cttInfoShowSel.setPkid(cttInfoShowSel.getPkid());
                    esCttInfoService.updateByPKid(cttInfoShowSel);
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
     *
     * @param
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Add")) {
            if (!submitPreCheck(cttInfoShowAdd)) {
                return;
            }
            if (esCttInfoService.isExistInDb(cttInfoShowAdd)) {
                MessageUtil.addError("该记录已存在，请重新录入！");
            } else {
                addRecordAction(cttInfoShowAdd);
            }
        } else if (strSubmitType.equals("Upd")) {
            updRecordAction(cttInfoShowUpd);
        } else if (strSubmitType.equals("Del")) {
            deleteRecordAction(cttInfoShowDel);
        }
        onQueryAction("Mng","false");
    }

    private void addRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE2.getCode());
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
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE2.getCode());
            esCttInfoService.updateRecord(cttInfoShowPara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE2.getCode());
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

    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
    }

    public CttItemShow getCttItemShow() {
        return cttItemShow;
    }

    public void setCttItemShow(CttItemShow cttItemShow) {
        this.cttItemShow = cttItemShow;
    }

    public String getStrWarnMsg() {
        return strWarnMsg;
    }

    public void setStrWarnMsg(String strWarnMsg) {
        this.strWarnMsg = strWarnMsg;
    }
}
