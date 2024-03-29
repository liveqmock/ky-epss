package epss.view.item;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CommStlSubcttEngH;
import epss.common.utils.JxlsManager;
import epss.common.utils.StyleModel;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgEstStaItemShow;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
public class ItemTkcttEngStaStlAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemTkcttEngStaStlAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esInitCustService}")
    private EsInitCustService esInitCustService;
    @ManagedProperty(value = "#{esInitStlService}")
    private EsInitStlService esInitStlService;
    @ManagedProperty(value = "#{esItemStlTkcttEngStaService}")
    private EsItemStlTkcttEngStaService esItemStlTkcttEngStaService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgEstStaItemShow> progEstStaItemShowList;
    private ProgEstStaItemShow progEstStaItemShow;
    private ProgEstStaItemShow progEstStaItemShowUpd;
    private ProgEstStaItemShow progEstStaItemShowDel;
    private ProgEstStaItemShow progEstStaItemShowSelected;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private CommStlSubcttEngH commStlSubcttEngH;

    /*所属号*/
    private String strEsInitStlSubcttEng;
    private String strTkcttPkid;
    private String strLatestApprovedPeriodNo;
    private EsInitStl esInitStl;

    private String strSubmitType;
    private String strMngNotFinishFlag;

    private Map beansMap;
    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    private List<ProgEstStaItemShow> progEstItemShowListForExcel;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strEsInitStlSubcttEng")){
            strEsInitStlSubcttEng=parammap.get("strEsInitStlSubcttEng").toString();
            this.esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEng);
            strTkcttPkid= this.esInitStl.getStlPkid();
        }

        strLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(ESEnum.ITEMTYPE6.getCode(),strTkcttPkid));

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

        /*分包合同数据*/
        // From StlPkid To SubcttPkid
        EsInitStl esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEng);
        commStlSubcttEngH.setStrSubcttPkid(esInitStl.getStlPkid());
        commStlSubcttEngH.setStrStlId(esInitStl.getId());
        // From SubcttPkid To CstplPkid
        EsCttInfo esCttInfo_Subctt=esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrSubcttPkid());
        commStlSubcttEngH.setStrCstplPkid(esCttInfo_Subctt.getParentPkid());
        commStlSubcttEngH.setStrSubcttId(esCttInfo_Subctt.getId());
        commStlSubcttEngH.setStrSubcttName(esCttInfo_Subctt.getName());
        commStlSubcttEngH.setStrSignPartPkid(esCttInfo_Subctt.getSignPartB());
        commStlSubcttEngH.setStrSignPartName(esInitCustService.getEsInitCustByPkid(
                commStlSubcttEngH.getStrSignPartPkid()).getName());

        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
    }
    /*重置*/
    public void resetAction(){
        progEstStaItemShow =new ProgEstStaItemShow();
        progEstStaItemShowUpd =new ProgEstStaItemShow();
        progEstStaItemShowDel =new ProgEstStaItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if (!blurEng_CurrentPeriodEQty("submit")) {
                    return;
                }
                progEstStaItemShowUpd.setEng_PeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaListTemp =
                        esItemStlTkcttEngStaService.isExistInDb(progEstStaItemShowUpd);
                if (esItemStlTkcttEngStaListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (esItemStlTkcttEngStaListTemp.size() == 1) {
                    progEstStaItemShowUpd.setEng_Pkid(esItemStlTkcttEngStaListTemp.get(0).getPkid());
                    esItemStlTkcttEngStaService.updateRecord(progEstStaItemShowUpd);
                }
                if (esItemStlTkcttEngStaListTemp.size()==0){
                    progEstStaItemShowUpd.setEng_TkcttItemPkid(progEstStaItemShowSelected.getTkctt_Pkid());
                    progEstStaItemShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    esItemStlTkcttEngStaService.insertRecord(progEstStaItemShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progEstStaItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progEstStaItemShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progEstStaItemShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progEstStaItemShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                        MessageUtil.addError("上期材料累计供应数量+本期材料供应数量>合同数量，请确认您输入的本期材料供应数量（"
                                + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }

    private void delRecordAction(ProgEstStaItemShow progEstStaItemShowPara){
        try {
            if(progEstStaItemShowPara.getEng_Pkid()==null||
                    progEstStaItemShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum=esItemStlTkcttEngStaService.deleteRecord(progEstStaItemShowPara.getEng_Pkid()) ;
                if (deleteRecordNum<=0){
                    MessageUtil.addInfo("该记录已删除。");
                }else {
                    MessageUtil.addInfo("删除数据完成。");
                }
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            if (strSubmitTypePara.equals("Sel")){                
                progEstStaItemShow =(ProgEstStaItemShow)BeanUtils.cloneBean(progEstStaItemShowSelected) ;
                progEstStaItemShow.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstStaItemShow.getTkctt_StrNo()));
            }
            strSubmitType=strSubmitTypePara;
            String strTkctt_Unit= progEstStaItemShowSelected.getTkctt_Unit();
            if(strTkctt_Unit==null) {
                if(strSubmitTypePara.equals("Upd")){
                    MessageUtil.addInfo("该数据不是项数据，无法更新");
                }
                else if(strSubmitTypePara.equals("Del")){
                    MessageUtil.addInfo("该数据不是项数据，无法删除");
                }
                resetAction();
                return;
            }
            if(strSubmitTypePara.equals("Upd")){
                progEstStaItemShowUpd =(ProgEstStaItemShow) BeanUtils.cloneBean(progEstStaItemShowSelected) ;
                progEstStaItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstStaItemShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progEstStaItemShowDel =(ProgEstStaItemShow) BeanUtils.cloneBean(progEstStaItemShowSelected) ;
                progEstStaItemShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstStaItemShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*初始化操作*/
    private void initData() {
        /*分包合同*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        progEstItemShowListForExcel=new ArrayList<ProgEstStaItemShow>();
        esCttItemList = esCttItemService.getEsItemHieRelapListByTypeAndPkid(
                ESEnum.ITEMTYPE0.getCode(), strTkcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progEstStaItemShowList =new ArrayList<ProgEstStaItemShow>();
        recursiveDataTable("root", esCttItemList, progEstStaItemShowList);
        progEstStaItemShowList =getItemStlTkcttEngSMList_DoFromatNo(progEstStaItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progEstItemShowListForExcel", progEstItemShowListForExcel);
        // 表内容设定
        if(progEstStaItemShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<ProgEstStaItemShow> sprogEstStaItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgEstStaItemShow progEstStaItemShowTemp = new ProgEstStaItemShow();
            progEstStaItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progEstStaItemShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progEstStaItemShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progEstStaItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progEstStaItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progEstStaItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progEstStaItemShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progEstStaItemShowTemp.setTkctt_Name(itemUnit.getName());
            progEstStaItemShowTemp.setTkctt_Note(itemUnit.getNote());
            progEstStaItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progEstStaItemShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progEstStaItemShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progEstStaItemShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            EsItemStlTkcttEngSta esItemStlTkcttEngSta=new EsItemStlTkcttEngSta();
            esItemStlTkcttEngSta.setTkcttPkid(strTkcttPkid);
            esItemStlTkcttEngSta.setTkcttItemPkid(progEstStaItemShowTemp.getTkctt_Pkid());
            esItemStlTkcttEngSta.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaList =
                    esItemStlTkcttEngStaService.selectRecordsByExample(esItemStlTkcttEngSta);
            if(esItemStlTkcttEngStaList.size()>0){
                esItemStlTkcttEngSta= esItemStlTkcttEngStaList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(esItemStlTkcttEngSta.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(esItemStlTkcttEngSta.getLastUpdBy());
                progEstStaItemShowTemp.setEng_Pkid(esItemStlTkcttEngSta.getPkid());
                progEstStaItemShowTemp.setEng_PeriodNo(esItemStlTkcttEngSta.getPeriodNo());
                progEstStaItemShowTemp.setEng_TkcttPkid(esItemStlTkcttEngSta.getTkcttPkid());
                progEstStaItemShowTemp.setEng_TkcttItemPkid(esItemStlTkcttEngSta.getTkcttItemPkid());
                progEstStaItemShowTemp.setEng_CurrentPeriodEQty(esItemStlTkcttEngSta.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngSta.getCurrentPeriodQty());
                progEstStaItemShowTemp.setEng_CurrentPeriodEAmount(bdContractUnitPrice.multiply(bdCurrentPeriodQty));

                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(esItemStlTkcttEngSta.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngSta.getBeginToCurrentPeriodQty());
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty));

                progEstStaItemShowTemp.setEng_DeletedFlag(esItemStlTkcttEngSta.getDeleteFlag());
                progEstStaItemShowTemp.setEng_CreatedBy(esItemStlTkcttEngSta.getCreatedBy());
                progEstStaItemShowTemp.setEng_CreatedByName(strCreatedByName);
                progEstStaItemShowTemp.setEng_CreatedDate(esItemStlTkcttEngSta.getCreatedDate());
                progEstStaItemShowTemp.setEng_LastUpdBy(esItemStlTkcttEngSta.getLastUpdBy());
                progEstStaItemShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progEstStaItemShowTemp.setEng_LastUpdDate(esItemStlTkcttEngSta.getLastUpdDate());
                progEstStaItemShowTemp.setEng_ModificationNum(esItemStlTkcttEngSta.getModificationNum());
            }
            if(strLatestApprovedPeriodNo!=null && progEstStaItemShowTemp.getEng_BeginToCurrentPeriodEQty()==null){
                EsItemStlTkcttEngSta esItemStlTkcttEngStaLatestApprovedPeriodNo=new EsItemStlTkcttEngSta();
                esItemStlTkcttEngStaLatestApprovedPeriodNo.setTkcttPkid(strTkcttPkid);
                esItemStlTkcttEngStaLatestApprovedPeriodNo.setTkcttItemPkid(esItemStlTkcttEngSta.getTkcttItemPkid());
                esItemStlTkcttEngStaLatestApprovedPeriodNo.setPeriodNo(strLatestApprovedPeriodNo);
                List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaListLatestApprovedPeriodNo =
                        esItemStlTkcttEngStaService.selectRecordsByExample(esItemStlTkcttEngStaLatestApprovedPeriodNo);
                if(esItemStlTkcttEngStaListLatestApprovedPeriodNo.size()>0){
                    esItemStlTkcttEngStaLatestApprovedPeriodNo= esItemStlTkcttEngStaListLatestApprovedPeriodNo.get(0);
                    progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                            esItemStlTkcttEngStaLatestApprovedPeriodNo.getBeginToCurrentPeriodQty());
                    bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progEstStaItemShowTemp.getEng_BeginToCurrentPeriodEQty());
                    progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty));
                }
            }
            sprogEstStaItemShowListPara.add(progEstStaItemShowTemp) ;
            recursiveDataTable(progEstStaItemShowTemp.getTkctt_Pkid(), esCttItemListPara, sprogEstStaItemShowListPara);
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgEstStaItemShow> progEstStaItemShowListTemp =new ArrayList<ProgEstStaItemShow>();
        progEstStaItemShowListTemp.addAll(progEstStaItemShowList);

        progEstStaItemShowList.clear();
        // 合同数量小计
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // 合同数量大计
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // 合同金额小计
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // 合同金额大计
        BigDecimal bdAmountAllTotal=new BigDecimal(0);

        // 开累数量小计
        BigDecimal bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
        // 开累数量大计
        BigDecimal bdBeginToCurrentPeriodEQtyAllTotal=new BigDecimal(0);
        // 当期数量小计
        BigDecimal bdCurrentPeriodEQtyTotal=new BigDecimal(0);
        // 当期数量大计
        BigDecimal bdCurrentPeriodEQtyAllTotal=new BigDecimal(0);

        // 开累金额小计
        BigDecimal bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
        // 开累金额大计
        BigDecimal bdBeginToCurrentPeriodEAmountAllTotal=new BigDecimal(0);
        // 当期金额小计
        BigDecimal bdCurrentPeriodEAmountTotal=new BigDecimal(0);
        // 当期金额大计
        BigDecimal bdCurrentPeriodEAmountAllTotal=new BigDecimal(0);

        ProgEstStaItemShow itemUnit=new ProgEstStaItemShow();
        ProgEstStaItemShow itemUnitNext=new ProgEstStaItemShow();

        for(int i=0;i< progEstStaItemShowListTemp.size();i++){
            itemUnit = progEstStaItemShowListTemp.get(i);
            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractQuantity()));
            bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractAmount()));
            bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractAmount()));

            bdBeginToCurrentPeriodEQtyTotal=
                    bdBeginToCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
            bdBeginToCurrentPeriodEQtyAllTotal=
                    bdBeginToCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
            bdCurrentPeriodEQtyTotal=
                    bdCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEQty()));
            bdCurrentPeriodEQtyAllTotal=
                    bdCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEQty()));

            bdBeginToCurrentPeriodEAmountTotal=
                    bdBeginToCurrentPeriodEAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEAmount()));
            bdBeginToCurrentPeriodEAmountAllTotal=
                    bdBeginToCurrentPeriodEAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEAmount()));
            bdCurrentPeriodEAmountTotal=
                    bdCurrentPeriodEAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEAmount()));
            bdCurrentPeriodEAmountAllTotal=
                    bdCurrentPeriodEAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEAmount()));

            progEstStaItemShowList.add(itemUnit);

            if(i+1< progEstStaItemShowListTemp.size()){
                itemUnitNext = progEstStaItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgEstStaItemShow itemOfEsItemHieRelapTemp=new ProgEstStaItemShow();
                    itemOfEsItemHieRelapTemp.setTkctt_Name("合计");
                    itemOfEsItemHieRelapTemp.setTkctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setTkctt_ContractQuantity(bdQuantityTotal);
                    itemOfEsItemHieRelapTemp.setTkctt_ContractAmount(bdAmountTotal);

                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEQty(bdBeginToCurrentPeriodEQtyTotal);
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEQty(bdCurrentPeriodEQtyTotal);
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEAmount(bdBeginToCurrentPeriodEAmountTotal);
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEAmount(bdCurrentPeriodEAmountTotal);
                    progEstStaItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progEstStaItemShowListTemp.size()){
                itemUnitNext = progEstStaItemShowListTemp.get(i);
                ProgEstStaItemShow progEstStaItemShowTemp =new ProgEstStaItemShow();
                progEstStaItemShowTemp.setTkctt_Name("合计");
                progEstStaItemShowTemp.setTkctt_Pkid("total"+i);
                progEstStaItemShowTemp.setTkctt_ContractQuantity(bdQuantityTotal);
                progEstStaItemShowTemp.setTkctt_ContractAmount(bdAmountTotal);

                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(bdBeginToCurrentPeriodEQtyTotal);
                progEstStaItemShowTemp.setEng_CurrentPeriodEQty(bdCurrentPeriodEQtyTotal);
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(bdBeginToCurrentPeriodEAmountTotal);
                progEstStaItemShowTemp.setEng_CurrentPeriodEAmount(bdCurrentPeriodEAmountTotal);
                progEstStaItemShowList.add(progEstStaItemShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // 总合计
                progEstStaItemShowTemp =new ProgEstStaItemShow();
                progEstStaItemShowTemp.setTkctt_Name("总合计");
                progEstStaItemShowTemp.setTkctt_Pkid("total_all"+i);
                progEstStaItemShowTemp.setTkctt_ContractQuantity(bdQuantityAllTotal);
                progEstStaItemShowTemp.setTkctt_ContractAmount(bdAmountAllTotal);

                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(bdBeginToCurrentPeriodEQtyAllTotal);
                progEstStaItemShowTemp.setEng_CurrentPeriodEQty(bdCurrentPeriodEQtyAllTotal);
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(bdBeginToCurrentPeriodEAmountAllTotal);
                progEstStaItemShowTemp.setEng_CurrentPeriodEAmount(bdCurrentPeriodEAmountAllTotal);
                progEstStaItemShowList.add(progEstStaItemShowTemp);
            }
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgEstStaItemShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgEstStaItemShow> progEstStaItemShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgEstStaItemShow itemUnit: progEstStaItemShowListPara){
                if(itemUnit.getTkctt_Grade().equals(intBeforeGrade)){
                    if(strTemp.lastIndexOf(".")<0) {
                        strTemp=itemUnit.getTkctt_Orderid().toString();
                    }
                    else{
                        strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getTkctt_Orderid().toString();
                    }
                }
                else{
                    if(itemUnit.getTkctt_Grade()==1){
                        strTemp=itemUnit.getTkctt_Orderid().toString() ;
                    }
                    else {
                        if (!itemUnit.getTkctt_Grade().equals(intBeforeGrade)) {
                            if (itemUnit.getTkctt_Grade().compareTo(intBeforeGrade)>0) {
                                strTemp = strTemp + "." + itemUnit.getTkctt_Orderid().toString();
                            } else {
                                Integer intTemp=strTemp.indexOf(".",itemUnit.getTkctt_Grade()-1);
                                strTemp = strTemp .substring(0, intTemp);
                                strTemp = strTemp+"."+itemUnit.getTkctt_Orderid().toString();
                            }
                        }
                    }
                }
                intBeforeGrade=itemUnit.getTkctt_Grade() ;
                itemUnit.setTkctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getTkctt_Grade(), strTemp)) ;

                ProgEstStaItemShow itemUnitTemp= (ProgEstStaItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progEstItemShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
        return progEstStaItemShowListPara;
    }


    public String onExportExcel()throws IOException, WriteException {
        if (this.progEstStaItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "分包工程统计数量-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlTkcttEngSta.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
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

    public EsItemStlTkcttEngStaService getEsItemStlTkcttEngStaService() {
        return esItemStlTkcttEngStaService;
    }

    public void setEsItemStlTkcttEngStaService(EsItemStlTkcttEngStaService EsItemStlTkcttEngStaService) {
        this.esItemStlTkcttEngStaService = EsItemStlTkcttEngStaService;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsInitStlService getEsInitStlService() {
        return esInitStlService;
    }

    public void setEsInitStlService(EsInitStlService esInitStlService) {
        this.esInitStlService = esInitStlService;
    }
    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public CommStlSubcttEngH getCommStlSubcttEngH() {
        return commStlSubcttEngH;
    }

    public void setCommStlSubcttEngH(CommStlSubcttEngH commStlSubcttEngH) {
        this.commStlSubcttEngH = commStlSubcttEngH;
    }

    public EsInitCustService getEsInitCustService() {
        return esInitCustService;
    }

    public void setEsInitCustService(EsInitCustService esInitCustService) {
        this.esInitCustService = esInitCustService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }
    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public BigDecimal getbDEng_BeginToCurrentPeriodEQtyInDB() {
        return bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public void setbDEng_BeginToCurrentPeriodEQtyInDB(BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB) {
        this.bDEng_BeginToCurrentPeriodEQtyInDB = bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public ProgEstStaItemShow getProgEstStaItemShow() {
        return progEstStaItemShow;
    }

    public void setProgEstStaItemShow(ProgEstStaItemShow progEstStaItemShow) {
        this.progEstStaItemShow = progEstStaItemShow;
    }

    public ProgEstStaItemShow getProgEstStaItemShowDel() {
        return progEstStaItemShowDel;
    }

    public void setProgEstStaItemShowDel(ProgEstStaItemShow progEstStaItemShowDel) {
        this.progEstStaItemShowDel = progEstStaItemShowDel;
    }

    public ProgEstStaItemShow getProgEstStaItemShowSelected() {
        return progEstStaItemShowSelected;
    }

    public void setProgEstStaItemShowSelected(ProgEstStaItemShow progEstStaItemShowSelected) {
        this.progEstStaItemShowSelected = progEstStaItemShowSelected;
    }

    public ProgEstStaItemShow getProgEstStaItemShowUpd() {
        return progEstStaItemShowUpd;
    }

    public void setProgEstStaItemShowUpd(ProgEstStaItemShow progEstStaItemShowUpd) {
        this.progEstStaItemShowUpd = progEstStaItemShowUpd;
    }

    public List<ProgEstStaItemShow> getProgEstStaItemShowList() {
        return progEstStaItemShowList;
    }

    public void setProgEstStaItemShowList(List<ProgEstStaItemShow> progEstStaItemShowList) {
        this.progEstStaItemShowList = progEstStaItemShowList;
    }

    public List<ProgEstStaItemShow> getProgEstItemShowListForExcel() {
        return progEstItemShowListForExcel;
    }

    public void setProgEstItemShowListForExcel(List<ProgEstStaItemShow> progEstItemShowListForExcel) {
        this.progEstItemShowListForExcel = progEstItemShowListForExcel;
    }

    /*智能字段End*/
}
