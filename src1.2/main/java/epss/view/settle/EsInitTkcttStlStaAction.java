package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgInfoShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsInitStl;
import epss.service.*;
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
public class EsInitTkcttStlStaAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitTkcttStlStaAction.class);
    @ManagedProperty(value = "#{esInitStlService}")
    private EsInitStlService esInitStlService;
    @ManagedProperty(value = "#{esItemStlTkcttEngStaService}")
    private EsItemStlTkcttEngStaService esItemStlTkcttEngStaService;
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgInfoShow progInfoShowQry;
    private String strNotPassToStatus;
    private ProgInfoShow progInfoShowSelected;
    private ProgInfoShow progInfoShowSel;
    private ProgInfoShow progInfoShowAdd;
    private ProgInfoShow progInfoShowUpd;
    private ProgInfoShow progInfoShowDel;

    private List<ProgInfoShow> progInfoShowList;

    private List<SelectItem> tkcttList;

    private String strSubmitType;
    private String rowSelectedFlag;
    private String strStlType;
    private String strCstplPkid;
    /*控制维护画面层级部分的显示*/
    private String strCstplPkidRendered;
    private String strControlsRenderedForCheck;

    private String strCallPageNameFlag;
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progInfoShowList = new ArrayList<ProgInfoShow>();
        strStlType=ESEnum.ITEMTYPE6.getCode();
        resetAction();

        List<CttInfoShow> cttInfoShowList =
                esCttInfoService.getCttInfoListByCttType_Status(
                        ESEnum.ITEMTYPE0.getCode()
                        ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
        tkcttList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","全部");
            tkcttList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                tkcttList.add(selectItem);
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
        esFlowControl.setStatusFlagListByPower("Qry");
        rowSelectedFlag="false";
    }

    public void resetActionForAdd(){
        progInfoShowAdd =new ProgInfoShow();
        strSubmitType="Add";
        rowSelectedFlag="false";
    }

    public void setMaxNoPlusOne(){
        try {
            Integer intTemp;
            String strMaxId= esInitStlService.getStrMaxStlId(strStlType);
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
            progInfoShowAdd.setId(strMaxId);
            progInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
        }
    }

    public void onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progInfoShowQry.setStlType(strStlType);
            if(strQryFlag.equals("Qry")){
                progInfoShowQry.setStrStatusFlagBegin(progInfoShowQry.getStatusFlag());
                progInfoShowQry.setStrStatusFlagEnd(progInfoShowQry.getStatusFlag());
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
                    esFlowService.selectTkcttStlSMByStatusFlagBegin_End(progInfoShowQry);
            for(ProgInfoShow esISSOMPCUnit: progInfoShowConstructsTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())){
                        progInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if(strQryMsgOutPara.equals("true")) {
                if (progInfoShowList.isEmpty()) {
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
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progInfoShowSelected.getStatusFlag());
            String strStatusFlagName= esFlowControl.getLabelByValueInStatusFlaglist(progInfoShowSelected.getStatusFlag());
            progInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(progInfoShowSelected.getCreatedBy()));
            progInfoShowSelected.setLastUpdByName(esCommon.getOperNameByOperId(progInfoShowSelected.getLastUpdBy()));
            // 查询
            if(strPowerTypePara.equals("Qry")){
                progInfoShowSel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowSelected);
            }else if(strPowerTypePara.equals("Mng")){// 维护
                if(strSubmitTypePara.equals("Sel")){
                    progInfoShowSel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowSelected);
                    rowSelectedFlag="true";
                }else if(strSubmitTypePara.equals("Add")){
                }else{
                    if(!strStatusFlagCode.equals("")&&
                            !strStatusFlagCode.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        MessageUtil.addInfo("数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                        return;
                    }
                    if(strSubmitTypePara.equals("Upd")){
                        rowSelectedFlag="false";
                        progInfoShowUpd =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowSelected);
                    }else if(strSubmitTypePara.equals("Del")){
                        rowSelectedFlag="false";
                        progInfoShowDel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowSelected);
                    }
                }
            }else{// 权限控制
                rowSelectedFlag="true";
                //根据流程环节,显示不同的退回的状态
                esFlowControl.setStatusFlagListByPower(strPowerTypePara) ;
                progInfoShowSel =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowSelected);
                if(strPowerTypePara.equals("Check")){
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("本期数据还未录入完毕，您暂时不能进行审核操作！");
                    }else if(!strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG0.getCode())&&!
                            strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG1.getCode())){
                        MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                    }
                }else if(strPowerTypePara.equals("DoubleCheck")){
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("本期数据还未录入完毕，您暂时不能进行复核操作！");
                    }else if(strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG0.getCode())){
                        MessageUtil.addInfo("本期数据刚刚录入，还未审核，您暂时不能进行复核！");
                    }else if(!strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG1.getCode())&&!
                            strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG2.getCode())){
                        MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                    }
                }else if(strPowerTypePara.equals("Approve")){
                    if(strStatusFlagCode.equals("")){
                        MessageUtil.addInfo("本期数据还未录入完毕，您暂时不能进行批准操作！");
                    }else if(strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG0.getCode())){
                        MessageUtil.addInfo("本期数据刚刚录入，还未审核，您暂时不能进行批准！");
                    }else if(!strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG2.getCode())&&!
                            strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG3.getCode())){
                        MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您无权进行编辑操作！");
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
    */
    private boolean submitPreCheck(ProgInfoShow progInfoShow){
        if (StringUtils.isEmpty(progInfoShow.getId())){
            MessageUtil.addError("请输入结算编号！");
            return false;
        }
        else if (StringUtils.isEmpty(progInfoShow.getStlPkid())) {
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
                    esFlowControl.mngFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    esFlowControl.mngNotFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    MessageUtil.addInfo("数据录入未完！");
                }
                // 初始
                strStatusFlagBegin=null;
                // 审核
                strStatusFlagEnd=ESEnumStatusFlag.STATUS_FLAG0.getCode();
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// 审核
                if(strPowerType.equals("CheckPass")){
                    // 状态标志：审核
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // 原因：审核通过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("数据审核通过！");
                }else if(strPowerType.equals("CheckFail")){
                    // 状态标志：初始
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // 原因：审核未过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("数据审核未过！");
                }
                // 初始
                strStatusFlagBegin=ESEnumStatusFlag.STATUS_FLAG0.getCode();
                // 审核
                strStatusFlagEnd=ESEnumStatusFlag.STATUS_FLAG1.getCode();
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    // 状态标志：复核
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("数据复核通过！");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // 这样写可以实现越级退回
                    progInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // 原因：复核未过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
                // 审核
                strStatusFlagBegin=ESEnumStatusFlag.STATUS_FLAG1.getCode();
                // 复核
                strStatusFlagEnd=ESEnumStatusFlag.STATUS_FLAG2.getCode();
            } else if(strPowerType.contains("Approve")){// 批准
                if(strPowerType.equals("ApprovePass")){
                    // 状态标志：批准
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // 原因：批准通过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("数据批准通过！");
                }else if(strPowerType.equals("ApproveFail")){
                    // 这样写可以实现越级退回
                    progInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // 原因：批准未过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);

                    MessageUtil.addInfo("数据批准未过！");
                }
                // 复核
                strStatusFlagBegin=ESEnumStatusFlag.STATUS_FLAG2.getCode();
                // 批准
                strStatusFlagEnd=ESEnumStatusFlag.STATUS_FLAG3.getCode();
            }
            // 重新查询，已操作的结果
            ProgInfoShow progInfoShowTemp =new ProgInfoShow();
            progInfoShowTemp.setStlType(strStlType);
            progInfoShowTemp.setStrStatusFlagBegin(strStatusFlagBegin);
            progInfoShowTemp.setStrStatusFlagEnd(strStatusFlagEnd);

            this.progInfoShowList.clear();
            List<ProgInfoShow> progInfoShowConstructsTemp =
                    esFlowService.selectTkcttStlSMByStatusFlagBegin_End(progInfoShowTemp);

            for(ProgInfoShow esISSOMPCUnit: progInfoShowConstructsTemp){
                for(SelectItem itemUnit:tkcttList){
                    if(itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())){
                        progInfoShowList.add(esISSOMPCUnit);
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
            progInfoShowAdd.setStlType(strStlType);
            if(!submitPreCheck(progInfoShowAdd)){
                return;
            }
            List<EsInitStl> esInitStlListTemp =
                    esInitStlService.getExistedEsInitStlSubcttEngInDb(progInfoShowAdd);
            if(esInitStlListTemp.size()>0) {
                MessageUtil.addError("该记录已存在，请重新录入！");
                return;
            }
            String strTemp=esFlowService.subCttStlCheckForMng(
                    ESEnum.ITEMTYPE6.getCode(),
                    progInfoShowAdd.getStlPkid(),
                    progInfoShowAdd.getPeriodNo());
            if(!"".equals(strTemp)){
                MessageUtil.addError(strTemp);
                return;
            }
            addRecordAction(progInfoShowAdd);
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
            delRecordAction(progInfoShowDel);
        }
        ProgInfoShow progInfoShowTemp =new ProgInfoShow();
        progInfoShowTemp.setStlType(strStlType);
        progInfoShowTemp.setStrStatusFlagBegin(null);
        progInfoShowTemp.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
        this.progInfoShowList =
                esFlowService.selectTkcttStlSMByStatusFlagBegin_End(progInfoShowTemp);
    }

    private void addRecordAction(ProgInfoShow progInfoShowPara){
        try {
            esInitStlService.insertRecord(progInfoShowPara) ;
            // 从批准了的上一阶段的数据拿到这一阶段中，作为开始累计数
            esItemStlTkcttEngStaService.setFromLastStageApproveDataToThisStageBeginData(progInfoShowPara);
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgInfoShow progInfoShowPara){
        try {
            esInitStlService.updateRecord(progInfoShowPara) ;
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgInfoShow progInfoShowPara){
        try {
            // 删除详细数据
            int deleteItemsByInitStlTkcttEngNum=esItemStlTkcttEngStaService.deleteItemsByInitStlTkcttEng(
                    progInfoShowPara.getStlPkid(),
                    progInfoShowPara.getPeriodNo());
            // 删除登记数据
            int deleteRecordOfRegistNum=esInitStlService.deleteRecord(progInfoShowPara.getPkid()) ;
            // 删除权限数据
            int deleteRecordOfPowerNum=esInitPowerService.deleteRecord(
                    progInfoShowPara.getStlType(),
                    progInfoShowPara.getStlPkid(),
                    progInfoShowPara.getPeriodNo());
            if (deleteItemsByInitStlTkcttEngNum<=0&&deleteRecordOfRegistNum<=0&&deleteRecordOfPowerNum<=0){
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
    public ProgInfoShow getProgInfoShowQry() {
        return progInfoShowQry;
    }

    public void setProgInfoShowQry(ProgInfoShow progInfoShowQry) {
        this.progInfoShowQry = progInfoShowQry;
    }

    public EsInitStlService getEsInitStlService() {
        return esInitStlService;
    }

    public void setEsInitStlService(EsInitStlService esInitStlService) {
        this.esInitStlService = esInitStlService;
    }

    public EsItemStlTkcttEngStaService getEsItemStlTkcttEngStaService() {
        return esItemStlTkcttEngStaService;
    }

    public void setEsItemStlTkcttEngStaService(EsItemStlTkcttEngStaService esItemStlTkcttEngStaService) {
        this.esItemStlTkcttEngStaService = esItemStlTkcttEngStaService;
    }

    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
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

    public List<ProgInfoShow> getProgInfoShowList() {
        return progInfoShowList;
    }

    public void setProgInfoShowList(List<ProgInfoShow> progInfoShowList) {
        this.progInfoShowList = progInfoShowList;
    }

    public ProgInfoShow getProgInfoShowSelected() {
        return progInfoShowSelected;
    }

    public void setProgInfoShowSelected(ProgInfoShow progInfoShowSelected) {
        this.progInfoShowSelected = progInfoShowSelected;
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

    public ProgInfoShow getProgInfoShowAdd() {
        return progInfoShowAdd;
    }

    public void setProgInfoShowAdd(ProgInfoShow progInfoShowAdd) {
        this.progInfoShowAdd = progInfoShowAdd;
    }

    public ProgInfoShow getProgInfoShowDel() {
        return progInfoShowDel;
    }

    public void setProgInfoShowDel(ProgInfoShow progInfoShowDel) {
        this.progInfoShowDel = progInfoShowDel;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public ProgInfoShow getProgInfoShowUpd() {
        return progInfoShowUpd;
    }

    public void setProgInfoShowUpd(ProgInfoShow progInfoShowUpd) {
        this.progInfoShowUpd = progInfoShowUpd;
    }

    public ProgInfoShow getProgInfoShowSel() {
        return progInfoShowSel;
    }

    public void setProgInfoShowSel(ProgInfoShow progInfoShowSel) {
        this.progInfoShowSel = progInfoShowSel;
    }

    /*智能字段End*/
}
