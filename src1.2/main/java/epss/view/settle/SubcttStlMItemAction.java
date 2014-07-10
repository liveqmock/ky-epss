package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.model.model_show.ProgMatQtyItemShow;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class SubcttStlMItemAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttStlMItemAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    @ManagedProperty(value = "#{esInitStlService}")
    private EsInitStlService esInitStlService;
    @ManagedProperty(value = "#{esItemStlSubcttEngMService}")
    private EsItemStlSubcttEngMService esItemStlSubcttEngMService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgMatQtyItemShow> progMatQtyItemShowList;
    private ProgMatQtyItemShow progMatQtyItemShow;
    private ProgMatQtyItemShow progMatQtyItemShowUpd;
    private ProgMatQtyItemShow progMatQtyItemShowDel;
    private BigDecimal bDEngMMng_BeginToCurrentPeriodMQtyInDB;
    private BigDecimal bDEngMMng_CurrentPeriodMQtyInDB;

    private String strSubmitType;
    private String strSubcttPkid;
    private EsInitStl esInitStl;

    /*控制维护画面层级部分的显示*/
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strFlowType")){
            strFlowType=parammap.get("strFlowType").toString();
        }
        if(parammap.containsKey("strEsInitStlSubcttEng")){
            String strEsInitStlSubcttEngTemp=parammap.get("strEsInitStlSubcttEng").toString();
            esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEngTemp);
            strSubcttPkid= esInitStl.getStlPkid();
        }

        List<EsInitPower> esInitPowerList=
                esInitPowerService.selectListByModel(esInitStl.getStlType(),esInitStl.getStlPkid(),esInitStl.getPeriodNo());
        strPassFlag="true";
        if(esInitPowerList.size()>0){
            if("Mng".equals(strFlowType)&&ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(esInitPowerList.get(0).getStatusFlag())) {
                strPassFlag="false";
            }
        }
        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
        /*分包合同*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        esCttItemList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE2.getCode(), strSubcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progMatQtyItemShowList =new ArrayList<ProgMatQtyItemShow>();
        recursiveDataTable("root", esCttItemList, progMatQtyItemShowList);
        progMatQtyItemShowList =getStlSubCttEngMMngConstructList_DoFromatNo(progMatQtyItemShowList);
        List<EsInitPower> esInitPowerList= esInitPowerService.selectListByModel(ESEnumPower.POWER_TYPE3.getCode(),
                strSubcttPkid, esCommon.getStrDateThisPeriod());
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgMatQtyItemShow> sProgMatQtyItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgMatQtyItemShow progMatQtyItemShowTemp = new ProgMatQtyItemShow();
            progMatQtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progMatQtyItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progMatQtyItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progMatQtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progMatQtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progMatQtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());

            progMatQtyItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());

            progMatQtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progMatQtyItemShowTemp.setSubctt_Note(itemUnit.getNote());

            progMatQtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progMatQtyItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progMatQtyItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progMatQtyItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progMatQtyItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            EsItemStlSubcttEngM esItemStlSubcttEngM=new EsItemStlSubcttEngM();
            esItemStlSubcttEngM.setSubcttPkid(strSubcttPkid);
            esItemStlSubcttEngM.setSubcttItemPkid(itemUnit.getPkid());
            esItemStlSubcttEngM.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlSubcttEngM> esItemStlSubcttEngMList =
                    esItemStlSubcttEngMService.selectRecordsByExample(esItemStlSubcttEngM);
            if(esItemStlSubcttEngMList.size()>0){
                esItemStlSubcttEngM= esItemStlSubcttEngMList.get(0);
                progMatQtyItemShowTemp.setEngMMng_Pkid(esItemStlSubcttEngM.getPkid());
                progMatQtyItemShowTemp.setEngMMng_PeriodNo(esItemStlSubcttEngM.getPeriodNo());
                progMatQtyItemShowTemp.setEngMMng_SubcttPkid(esItemStlSubcttEngM.getSubcttPkid());
                progMatQtyItemShowTemp.setEngMMng_SubcttItemPkid(esItemStlSubcttEngM.getSubcttItemPkid());
                progMatQtyItemShowTemp.setEngMMng_BeginToCurrentPeriodMQty(esItemStlSubcttEngM.getBeginToCurrentPeriodMQty());
                progMatQtyItemShowTemp.setEngMMng_CurrentPeriodMQty(esItemStlSubcttEngM.getCurrentPeriodMQty());
                progMatQtyItemShowTemp.setEngMMng_MPurchaseUnitPrice(esItemStlSubcttEngM.getmPurchaseUnitPrice());
                progMatQtyItemShowTemp.setEngMMng_DeletedFlag(esItemStlSubcttEngM.getDeleteFlag());
                progMatQtyItemShowTemp.setEngMMng_CreatedBy(esItemStlSubcttEngM.getCreatedBy());
                progMatQtyItemShowTemp.setEngMMng_CreatedDate(esItemStlSubcttEngM.getCreatedDate());
                progMatQtyItemShowTemp.setEngMMng_LastUpdBy(esItemStlSubcttEngM.getLastUpdBy());
                progMatQtyItemShowTemp.setEngMMng_LastUpdDate(esItemStlSubcttEngM.getLastUpdDate());
                progMatQtyItemShowTemp.setEngMMng_ModificationNum(esItemStlSubcttEngM.getModificationNum());
            }
            sProgMatQtyItemShowListPara.add(progMatQtyItemShowTemp) ;
            recursiveDataTable(progMatQtyItemShowTemp.getSubctt_Pkid(), esCttItemListPara, sProgMatQtyItemShowListPara);
        }
    }
    /*根据group和orderid临时编制编码strNo*/
    private List<ProgMatQtyItemShow> getStlSubCttEngMMngConstructList_DoFromatNo(
            List<ProgMatQtyItemShow> progMatQtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgMatQtyItemShow itemUnit: progMatQtyItemShowListPara){
            if(itemUnit.getSubctt_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getSubctt_Orderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getSubctt_Orderid().toString();
                }
            }
            else{
                if(itemUnit.getSubctt_Grade()==1){
                    strTemp=itemUnit.getSubctt_Orderid().toString() ;
                }
                else {
                    if (!itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getSubctt_Grade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        } else {
                            Integer intTemp=strTemp.indexOf(".",itemUnit.getSubctt_Grade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getSubctt_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getSubctt_Grade() ;
            itemUnit.setSubctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getSubctt_Grade(), strTemp)) ;
        }
        return progMatQtyItemShowListPara;
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsCttItemListByParentPkid(
            String strLevelParentPkid,
            List<EsCttItem> esCttItemListPara) {
        List<EsCttItem> tempEsCttItemList =new ArrayList<EsCttItem>();
        /*避开重复链接数据库*/
        for(EsCttItem itemUnit: esCttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempEsCttItemList.add(itemUnit);
            }
        }
        return tempEsCttItemList;
    }

    /*重置*/
    public void resetAction(){
        progMatQtyItemShow =new ProgMatQtyItemShow();
        progMatQtyItemShowUpd =new ProgMatQtyItemShow();
        progMatQtyItemShowDel =new ProgMatQtyItemShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEngMMng_CurrentPeriodMQty("submit")){
                    return;
                }
                ProgMatQtyItemShow progMatQtyItemShowTemp=new ProgMatQtyItemShow();
                progMatQtyItemShowTemp.setEngMMng_SubcttPkid(strSubcttPkid);
                progMatQtyItemShowTemp.setEngMMng_PeriodNo(esInitStl.getPeriodNo());
                progMatQtyItemShowTemp.setEngMMng_SubcttItemPkid(progMatQtyItemShowUpd.getSubctt_Pkid());
                List<EsItemStlSubcttEngM> esItemStlSubcttEngMListTemp =
                        esItemStlSubcttEngMService.isExistInDb(progMatQtyItemShowTemp);
                if (esItemStlSubcttEngMListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (esItemStlSubcttEngMListTemp.size() == 1) {
                    progMatQtyItemShowUpd.setEngMMng_Pkid (esItemStlSubcttEngMListTemp.get(0).getPkid());
                    updRecordAction(progMatQtyItemShowUpd);
                } else if (esItemStlSubcttEngMListTemp.size()==0){
                    progMatQtyItemShowUpd.setEngMMng_SubcttPkid(strSubcttPkid);
                    progMatQtyItemShowUpd.setEngMMng_PeriodNo(esInitStl.getPeriodNo());
                    progMatQtyItemShowUpd.setEngMMng_SubcttItemPkid(progMatQtyItemShowUpd.getSubctt_Pkid());
                    addRecordAction(progMatQtyItemShowUpd);
                }
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progMatQtyItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
            logger.error("增加数据失败，", e);
        }
    }

    public boolean blurEngMMng_CurrentPeriodMQty(String strBlurOrSubmitFlag){

        if(ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getSubctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEngQMng_CurrentPeriodMQtyTemp= 
			ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty());

            BigDecimal bigDecimalTemp= 
			         bDEngMMng_BeginToCurrentPeriodMQtyInDB.add(bDEngQMng_CurrentPeriodMQtyTemp).subtract(bDEngMMng_CurrentPeriodMQtyInDB);

            BigDecimal bDSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getSubctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                    MessageUtil.addInfo("上期材料累计供应数量+本期材料供应数量>合同数量，确认输入的本期材料供应数量（"
                            + bDEngQMng_CurrentPeriodMQtyTemp.toString() + "）是否正确！");
                 }
                progMatQtyItemShowUpd.setEngMMng_BeginToCurrentPeriodMQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodMQtyTemp=
                        ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
                if(bDEngQMng_BeginToCurrentPeriodMQtyTemp.compareTo(bDEngMMng_BeginToCurrentPeriodMQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                        MessageUtil.addInfo("上期材料累计供应数量+本期材料供应数量>合同数量，确认输入的本期材料供应数量（"
                                + bDEngQMng_CurrentPeriodMQtyTemp.toString() + "）是否正确！");
                    }
                }
            }
        }
        return true;
    }

    private void addRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            esItemStlSubcttEngMService.insertRecord(progMatQtyItemShowPara);
            MessageUtil.addInfo("增加数据完成。");
        } catch (Exception e) {
            logger.error("增加数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            esItemStlSubcttEngMService.updateRecord(progMatQtyItemShowPara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void delRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            if(progMatQtyItemShowPara.getEngMMng_Pkid()==null||
                    progMatQtyItemShowDel.getEngMMng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum = esItemStlSubcttEngMService.deleteRecord(
                        progMatQtyItemShowDel.getEngMMng_Pkid());
                if (deleteRecordNum <= 0) {
                    MessageUtil.addInfo("该记录已删除。");
                } else {
                    MessageUtil.addInfo("删除数据完成。");
                }
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara,ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            BigDecimal bdSubctt_SignPartAPrice= ToolUtil.getBdIgnoreNull(
                    progMatQtyItemShowPara.getSubctt_SignPartAPrice());
            BigDecimal bdSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(
                    progMatQtyItemShowPara.getSubctt_ContractQuantity());
            if(strSubmitTypePara.equals("Sel")){
                progMatQtyItemShow =(ProgMatQtyItemShow)BeanUtils.cloneBean(progMatQtyItemShowPara) ;
                progMatQtyItemShow.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShow.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("该数据不是工程材料数据，无法更新");
                    return;
                }
                progMatQtyItemShowUpd =(ProgMatQtyItemShow) BeanUtils.cloneBean(progMatQtyItemShowPara) ;
                progMatQtyItemShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShowUpd.getSubctt_StrNo()));

                bDEngMMng_CurrentPeriodMQtyInDB=ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty());
                bDEngMMng_BeginToCurrentPeriodMQtyInDB=
                        ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
            }else
            if(strSubmitTypePara.equals("Del")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("该数据不是工程材料数据，无法删除");
                    return;
                }
                progMatQtyItemShowDel =(ProgMatQtyItemShow) BeanUtils.cloneBean(progMatQtyItemShowPara) ;
                progMatQtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            strPowerType=strFlowType+strPowerType;
            ProgInfoShow progInfoShowSel=new ProgInfoShow();
            progInfoShowSel.setStlType(esInitStl.getStlType());
            progInfoShowSel.setStlPkid(esInitStl.getStlPkid());
            progInfoShowSel.setPeriodNo(esInitStl.getPeriodNo());
            progInfoShowSel.setPowerType(esInitStl.getStlType());
            progInfoShowSel.setPowerPkid(esInitStl.getStlPkid());
            progInfoShowSel.setPeriodNo(esInitStl.getPeriodNo());

            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    esFlowControl.mngFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    strPassFlag="false";
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    esFlowControl.mngNotFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    strPassFlag="true";
                    MessageUtil.addInfo("数据录入未完！");
                }
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
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    // 状态标志：复核
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);

                    //todo
                    String SubcttStlQStatus = ToolUtil.getStrIgnoreNull(
                            esFlowService.getStatusFlag(ESEnum.ITEMTYPE3.getCode(), progInfoShowSel.getStlPkid(), progInfoShowSel.getPeriodNo()));
                    if (!("".equals(SubcttStlQStatus)) && ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(SubcttStlQStatus) == 0) {
                        EsInitStl esInitStlTemp = new EsInitStl();
                        esInitStlTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
                        esInitStlTemp.setStlPkid(progInfoShowSel.getStlPkid());
                        esInitStlTemp.setPeriodNo(progInfoShowSel.getPeriodNo());
                        esInitStlTemp.setNote("");
                        // 结算登记表和流程控制表登记
                        esInitStlService.insertStlAndPowerRecord(esInitStlTemp);
                    }
                    MessageUtil.addInfo("数据复核通过！");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    String SubcttStlPStatus = ToolUtil.getStrIgnoreNull(
                            esFlowService.getStatusFlag(ESEnum.ITEMTYPE5.getCode(), progInfoShowSel.getStlPkid(), progInfoShowSel.getPeriodNo()));
                    String SubcttStlQStatus = ToolUtil.getStrIgnoreNull(
                            esFlowService.getStatusFlag(ESEnum.ITEMTYPE3.getCode(), progInfoShowSel.getStlPkid(), progInfoShowSel.getPeriodNo()));
                    if (!("".equals(SubcttStlPStatus))&&ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(SubcttStlPStatus) < 0) {
                        MessageUtil.addInfo("该数据已被分包价格结算批准，您无权进行操作！");
                        return;
                    } else {
                        if (!("".equals(SubcttStlQStatus)) && ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(SubcttStlQStatus) == 0) {
                            try {
                                ProgInfoShow progInfoShowTemp = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowSel);
                                progInfoShowTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
                                esInitStlService.deleteRecord(progInfoShowTemp);
                            } catch (Exception e) {
                                logger.error("删除数据失败,复核未过操作失败。", e);
                                MessageUtil.addError(e.getMessage());
                                return;
                            }
                        }
                    }
                    // 这样写可以实现越级退回
                    progInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // 原因：复核未过
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    esInitPowerService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("数据复核未过！");
                }
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
            }
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /* 智能字段Start*/
    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShow() {
        return progMatQtyItemShow;
    }

    public void setProgMatQtyItemShow(ProgMatQtyItemShow progMatQtyItemShow) {
        this.progMatQtyItemShow = progMatQtyItemShow;
    }

    public List<ProgMatQtyItemShow> getProgMatQtyItemShowList() {
        return progMatQtyItemShowList;
    }

    public void setProgMatQtyItemShowList(List<ProgMatQtyItemShow> progMatQtyItemShowList) {
        this.progMatQtyItemShowList = progMatQtyItemShowList;
    }

    public EsItemStlSubcttEngMService getEsItemStlSubcttEngMService() {
        return esItemStlSubcttEngMService;
    }

    public void setEsItemStlSubcttEngMService(EsItemStlSubcttEngMService esItemStlSubcttEngMService) {
        this.esItemStlSubcttEngMService = esItemStlSubcttEngMService;
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

    public EsInitStlService getEsInitStlService() {
        return esInitStlService;
    }

    public void setEsInitStlService(EsInitStlService esInitStlService) {
        this.esInitStlService = esInitStlService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShowDel() {
        return progMatQtyItemShowDel;
    }

    public void setProgMatQtyItemShowDel(ProgMatQtyItemShow progMatQtyItemShowDel) {
        this.progMatQtyItemShowDel = progMatQtyItemShowDel;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShowUpd() {
        return progMatQtyItemShowUpd;
    }

    public void setProgMatQtyItemShowUpd(ProgMatQtyItemShow progMatQtyItemShowUpd) {
        this.progMatQtyItemShowUpd = progMatQtyItemShowUpd;
    }

    public String getStrPassFlag() {
        return strPassFlag;
    }

    public void setStrPassFlag(String strPassFlag) {
        this.strPassFlag = strPassFlag;
    }

    public String getStrFlowType() {
        return strFlowType;
    }

    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }
/*智能字段End*/
}
