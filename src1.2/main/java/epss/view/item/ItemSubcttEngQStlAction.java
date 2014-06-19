package epss.view.item;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgWorkqtyItemShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
public class ItemSubcttEngQStlAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemSubcttEngQStlAction.class);
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
    @ManagedProperty(value = "#{esItemStlSubcttEngQService}")
    private EsItemStlSubcttEngQService esItemStlSubcttEngQService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgWorkqtyItemShow> progWorkqtyItemShowList;
    private ProgWorkqtyItemShow progWorkqtyItemShow;
    private ProgWorkqtyItemShow progWorkqtyItemShowUpd;
    private ProgWorkqtyItemShow progWorkqtyItemShowDel;
    private ProgWorkqtyItemShow progWorkqtyItemShowSelected;
    private BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEngQMng_CurrentPeriodEQtyInDB;

    private String strSubmitType;
    private String strSubcttPkid;
    private String strLatestApprovedPeriodNo;
    private EsInitStl esInitStl;

    /*控制维护画面层级部分的显示*/
    private String strMngNotFinishFlag;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strEsInitStlSubcttEng")){
            String strEsInitStlSubcttEngTemp=parammap.get("strEsInitStlSubcttEng").toString();
            esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEngTemp);
            strSubcttPkid= esInitStl.getStlPkid();
        }

        strLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(ESEnum.ITEMTYPE3.getCode(),strSubcttPkid));

        List<EsInitPower> esInitPowerList=
                esInitPowerService.selectListByModel(esInitStl.getStlType(),esInitStl.getStlPkid(),esInitStl.getPeriodNo());
        strMngNotFinishFlag="true";
        if(esInitPowerList.size()>0){
            if(esInitPowerList.get(0).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                strMngNotFinishFlag="false";
            }
        }
        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
        /*分包合同*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        esCttItemList = esCttItemService.getEsItemHieRelapListByTypeAndPkid(
                ESEnum.ITEMTYPE2.getCode(), strSubcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progWorkqtyItemShowList =new ArrayList<ProgWorkqtyItemShow>();
        recursiveDataTable("root", esCttItemList, progWorkqtyItemShowList);
        progWorkqtyItemShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progWorkqtyItemShowList);
        List<EsInitPower> esInitPowerList= esInitPowerService.selectListByModel(ESEnumPower.POWER_TYPE3.getCode(),
                strSubcttPkid, esCommon.getStrDateThisPeriod());
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgWorkqtyItemShow> sProgWorkqtyItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgWorkqtyItemShow progWorkqtyItemShowTemp = new ProgWorkqtyItemShow();
            progWorkqtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progWorkqtyItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progWorkqtyItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progWorkqtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progWorkqtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progWorkqtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progWorkqtyItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());

            progWorkqtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progWorkqtyItemShowTemp.setSubctt_Note(itemUnit.getNote());

            progWorkqtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progWorkqtyItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progWorkqtyItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progWorkqtyItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progWorkqtyItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            EsItemStlSubcttEngQ esItemStlSubcttEngQ=new EsItemStlSubcttEngQ();
            esItemStlSubcttEngQ.setSubcttPkid(strSubcttPkid);
            esItemStlSubcttEngQ.setSubcttItemPkid(itemUnit.getPkid());
            esItemStlSubcttEngQ.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList =
                    esItemStlSubcttEngQService.selectRecordsByExample(esItemStlSubcttEngQ);
            if(esItemStlSubcttEngQList.size()>0){
                esItemStlSubcttEngQ= esItemStlSubcttEngQList.get(0);
                progWorkqtyItemShowTemp.setEngQMng_Pkid(esItemStlSubcttEngQ.getPkid());
                progWorkqtyItemShowTemp.setEngQMng_PeriodNo(esItemStlSubcttEngQ.getPeriodNo());
                progWorkqtyItemShowTemp.setEngQMng_SubcttPkid(esItemStlSubcttEngQ.getSubcttPkid());

                progWorkqtyItemShowTemp.setEngQMng_SubcttItemPkid(esItemStlSubcttEngQ.getSubcttItemPkid());

                progWorkqtyItemShowTemp.setEngQMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngQ.getBeginToCurrentPeriodEQty());
                progWorkqtyItemShowTemp.setEngQMng_CurrentPeriodEQty(esItemStlSubcttEngQ.getCurrentPeriodEQty());
                progWorkqtyItemShowTemp.setEngQMng_DeletedFlag(esItemStlSubcttEngQ.getDeleteFlag());
                progWorkqtyItemShowTemp.setEngQMng_CreatedBy(esItemStlSubcttEngQ.getCreatedBy());
                progWorkqtyItemShowTemp.setEngQMng_CreatedDate(esItemStlSubcttEngQ.getCreatedDate());
                progWorkqtyItemShowTemp.setEngQMng_LastUpdBy(esItemStlSubcttEngQ.getLastUpdBy());
                progWorkqtyItemShowTemp.setEngQMng_LastUpdDate(esItemStlSubcttEngQ.getLastUpdDate());
                progWorkqtyItemShowTemp.setEngQMng_ModificationNum(esItemStlSubcttEngQ.getModificationNum());
            }
            sProgWorkqtyItemShowListPara.add(progWorkqtyItemShowTemp) ;
            recursiveDataTable(progWorkqtyItemShowTemp.getSubctt_Pkid(), esCttItemListPara, sProgWorkqtyItemShowListPara);
        }
    }
    /*根据group和orderid临时编制编码strNo*/
    private List<ProgWorkqtyItemShow> getStlSubCttEngQMngConstructList_DoFromatNo(
            List<ProgWorkqtyItemShow> progWorkqtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgWorkqtyItemShow itemUnit: progWorkqtyItemShowListPara){
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
        return progWorkqtyItemShowListPara;
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsItemHieRelapListByLevelParentPkid(
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
        progWorkqtyItemShow =new ProgWorkqtyItemShow();
        progWorkqtyItemShowUpd =new ProgWorkqtyItemShow();
        progWorkqtyItemShowDel =new ProgWorkqtyItemShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEngQMng_CurrentPeriodMQty("submit")){
                    return;
                }
                progWorkqtyItemShowUpd.setEngQMng_PeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlSubcttEngQ> esItemStlSubcttEngQListTemp =
                        esItemStlSubcttEngQService.isExistInDb(progWorkqtyItemShowUpd);
                if (esItemStlSubcttEngQListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (esItemStlSubcttEngQListTemp.size() == 1) {
                    progWorkqtyItemShowUpd.setEngQMng_Pkid (esItemStlSubcttEngQListTemp.get(0).getPkid());
                    esItemStlSubcttEngQService.updateRecord(progWorkqtyItemShowUpd);
                }
                if (esItemStlSubcttEngQListTemp.size()==0){
                    progWorkqtyItemShowUpd.setEngQMng_SubcttItemPkid(progWorkqtyItemShowSelected.getSubctt_Pkid());
                    progWorkqtyItemShowUpd.setEngQMng_SubcttPkid(strSubcttPkid);
                    esItemStlSubcttEngQService.insertRecord(progWorkqtyItemShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progWorkqtyItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEngQMng_CurrentPeriodMQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getSubctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progWorkqtyItemShowUpd.getEngQMng_CurrentPeriodEQty().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEngQMng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEngQMng_BeginToCurrentPeriodEQtyInDB.add(bDEngQMng_CurrentPeriodEQtyTemp).subtract(bDEngQMng_CurrentPeriodEQtyInDB);

            BigDecimal bDSubctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getSubctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progWorkqtyItemShowUpd.setEngQMng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEngQMng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                        MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                                + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "）！");
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }
    private void delRecordAction(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        try {
            int deleteRecordNum=esItemStlSubcttEngQService.deleteRecord(progWorkqtyItemShowPara.getEngQMng_Pkid());
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                progWorkqtyItemShow =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowSelected) ;
                progWorkqtyItemShow.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShow.getSubctt_StrNo()));
            }
            else if(strSubmitTypePara.equals("Upd")){
                progWorkqtyItemShowUpd =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowSelected) ;
                progWorkqtyItemShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowUpd.getSubctt_StrNo()));
                bDEngQMng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_CurrentPeriodEQty());
                bDEngQMng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progWorkqtyItemShowDel =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowSelected) ;
                progWorkqtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
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

    public ProgWorkqtyItemShow getProgWorkqtyItemShow() {
        return progWorkqtyItemShow;
    }

    public void setProgWorkqtyItemShow(ProgWorkqtyItemShow progWorkqtyItemShow) {
        this.progWorkqtyItemShow = progWorkqtyItemShow;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowSelected() {
        return progWorkqtyItemShowSelected;
    }

    public void setProgWorkqtyItemShowSelected(ProgWorkqtyItemShow progWorkqtyItemShowSelected) {
        this.progWorkqtyItemShowSelected = progWorkqtyItemShowSelected;
    }

    public List<ProgWorkqtyItemShow> getProgWorkqtyItemShowList() {
        return progWorkqtyItemShowList;
    }

    public void setProgWorkqtyItemShowList(List<ProgWorkqtyItemShow> progWorkqtyItemShowList) {
        this.progWorkqtyItemShowList = progWorkqtyItemShowList;
    }

    public EsItemStlSubcttEngQService getEsItemStlSubcttEngQService() {
        return esItemStlSubcttEngQService;
    }

    public void setEsItemStlSubcttEngQService(EsItemStlSubcttEngQService esItemStlSubcttEngQService) {
        this.esItemStlSubcttEngQService = esItemStlSubcttEngQService;
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

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowDel() {
        return progWorkqtyItemShowDel;
    }

    public void setProgWorkqtyItemShowDel(ProgWorkqtyItemShow progWorkqtyItemShowDel) {
        this.progWorkqtyItemShowDel = progWorkqtyItemShowDel;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowUpd() {
        return progWorkqtyItemShowUpd;
    }

    public void setProgWorkqtyItemShowUpd(ProgWorkqtyItemShow progWorkqtyItemShowUpd) {
        this.progWorkqtyItemShowUpd = progWorkqtyItemShowUpd;
    }

    /*智能字段End*/
}

