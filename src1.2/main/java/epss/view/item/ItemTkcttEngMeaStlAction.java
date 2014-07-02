package epss.view.item;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CommStlSubcttEngH;
import epss.repository.model.model_show.ProgEstMeaItemShow;
import epss.common.utils.StyleModel;
import epss.common.utils.MessageUtil;
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
public class ItemTkcttEngMeaStlAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemTkcttEngMeaStlAction.class);
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
    @ManagedProperty(value = "#{esItemStlTkcttEngMeaService}")
    private EsItemStlTkcttEngMeaService esItemStlTkcttEngMeaService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgEstMeaItemShow> progEstMeaItemShowList;
    private ProgEstMeaItemShow progEstMeaItemShow;
    private ProgEstMeaItemShow progEstMeaItemShowUpd;
    private ProgEstMeaItemShow progEstMeaItemShowDel;
    private ProgEstMeaItemShow progEstMeaItemShowSelected;

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
    private List<ProgEstMeaItemShow> progMeaItemShowListForExcel;
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
                esFlowService.getLatestApprovedPeriodNo(ESEnum.ITEMTYPE7.getCode(),strTkcttPkid));

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
        progEstMeaItemShow =new ProgEstMeaItemShow();
        progEstMeaItemShowUpd =new ProgEstMeaItemShow();
        progEstMeaItemShowDel =new ProgEstMeaItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progEstMeaItemShowUpd.setEng_PeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaListTemp =
                        esItemStlTkcttEngMeaService.isExistInDb(progEstMeaItemShowUpd);
                if (esItemStlTkcttEngMeaListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (esItemStlTkcttEngMeaListTemp.size() == 1) {
                    progEstMeaItemShowUpd.setEng_Pkid(esItemStlTkcttEngMeaListTemp.get(0).getPkid());
                    esItemStlTkcttEngMeaService.updateRecord(progEstMeaItemShowUpd);
                }
                if (esItemStlTkcttEngMeaListTemp.size()==0){
                    progEstMeaItemShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progEstMeaItemShowUpd.setEng_TkcttItemPkid(progEstMeaItemShowUpd.getTkctt_Pkid());
                    esItemStlTkcttEngMeaService.insertRecord(progEstMeaItemShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progEstMeaItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("提交数据失败，",e);
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progEstMeaItemShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progEstMeaItemShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progEstMeaItemShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progEstMeaItemShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progEstMeaItemShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progEstMeaItemShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progEstMeaItemShowUpd.getEng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                        MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
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

    private void delRecordAction(ProgEstMeaItemShow progEstMeaItemShowPara){
        try {
            if(progEstMeaItemShowPara.getEng_Pkid()==null||
                    progEstMeaItemShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum=esItemStlTkcttEngMeaService.deleteRecord(progEstMeaItemShowPara.getEng_Pkid()) ;
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
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){
                progEstMeaItemShow =(ProgEstMeaItemShow)BeanUtils.cloneBean(progEstMeaItemShowSelected) ;
                progEstMeaItemShow.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstMeaItemShow.getTkctt_StrNo()));
            }
            String strTkctt_Unit= progEstMeaItemShowSelected.getTkctt_Unit();
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
                progEstMeaItemShowUpd =(ProgEstMeaItemShow) BeanUtils.cloneBean(progEstMeaItemShowSelected) ;
                progEstMeaItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstMeaItemShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progEstMeaItemShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progEstMeaItemShowUpd.getEng_BeginToCurrentPeriodEQty());
               }
            else if(strSubmitTypePara.equals("Del")){
                progEstMeaItemShowDel =(ProgEstMeaItemShow) BeanUtils.cloneBean(progEstMeaItemShowSelected) ;
                progEstMeaItemShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstMeaItemShowDel.getTkctt_StrNo()));
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
        progMeaItemShowListForExcel=new ArrayList<ProgEstMeaItemShow>();
        esCttItemList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(), strTkcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progEstMeaItemShowList =new ArrayList<ProgEstMeaItemShow>();
        recursiveDataTable("root", esCttItemList, progEstMeaItemShowList);
        progEstMeaItemShowList =getItemStlTkcttEngSMList_DoFromatNo(progEstMeaItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progMeaItemShowListForExcel", progMeaItemShowListForExcel);
        // 表内容设定
        if(progEstMeaItemShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<ProgEstMeaItemShow> sProgEstMeaItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgEstMeaItemShow progEstMeaItemShowTemp = new ProgEstMeaItemShow();
            progEstMeaItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progEstMeaItemShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progEstMeaItemShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progEstMeaItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progEstMeaItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progEstMeaItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progEstMeaItemShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progEstMeaItemShowTemp.setTkctt_Name(itemUnit.getName());
            progEstMeaItemShowTemp.setTkctt_Note(itemUnit.getNote());
            progEstMeaItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progEstMeaItemShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progEstMeaItemShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progEstMeaItemShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            EsItemStlTkcttEngMea esItemStlTkcttEngMea=new EsItemStlTkcttEngMea();
            esItemStlTkcttEngMea.setTkcttPkid(strTkcttPkid);
            esItemStlTkcttEngMea.setTkcttItemPkid(progEstMeaItemShowTemp.getTkctt_Pkid());
            esItemStlTkcttEngMea.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList =
                    esItemStlTkcttEngMeaService.selectRecordsByKeyExample(esItemStlTkcttEngMea);
            if(esItemStlTkcttEngMeaList.size()>0){
                esItemStlTkcttEngMea= esItemStlTkcttEngMeaList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(esItemStlTkcttEngMea.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(esItemStlTkcttEngMea.getLastUpdBy());
                progEstMeaItemShowTemp.setEng_Pkid(esItemStlTkcttEngMea.getPkid());
                progEstMeaItemShowTemp.setEng_PeriodNo(esItemStlTkcttEngMea.getPeriodNo());
                progEstMeaItemShowTemp.setEng_TkcttPkid(esItemStlTkcttEngMea.getTkcttPkid());
                progEstMeaItemShowTemp.setEng_TkcttItemPkid (esItemStlTkcttEngMea.getTkcttItemPkid());
                progEstMeaItemShowTemp.setEng_CurrentPeriodEQty(esItemStlTkcttEngMea.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getCurrentPeriodQty());
                progEstMeaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progEstMeaItemShowTemp.setEng_BeginToCurrentPeriodEQty(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                progEstMeaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progEstMeaItemShowTemp.setEng_DeletedFlag(esItemStlTkcttEngMea.getDeleteFlag());
                progEstMeaItemShowTemp.setEng_CreatedBy(esItemStlTkcttEngMea.getCreatedBy());
                progEstMeaItemShowTemp.setEng_CreatedByName(strCreatedByName);
                progEstMeaItemShowTemp.setEng_CreatedDate(esItemStlTkcttEngMea.getCreatedDate());
                progEstMeaItemShowTemp.setEng_LastUpdBy(esItemStlTkcttEngMea.getLastUpdBy());
                progEstMeaItemShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progEstMeaItemShowTemp.setEng_LastUpdDate(esItemStlTkcttEngMea.getLastUpdDate());
                progEstMeaItemShowTemp.setEng_ModificationNum(esItemStlTkcttEngMea.getModificationNum());
            }
            sProgEstMeaItemShowListPara.add(progEstMeaItemShowTemp) ;
            recursiveDataTable(progEstMeaItemShowTemp.getTkctt_Pkid(), esCttItemListPara, sProgEstMeaItemShowListPara);
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgEstMeaItemShow> progEstMeaItemShowListTemp =new ArrayList<ProgEstMeaItemShow>();
        progEstMeaItemShowListTemp.addAll(progEstMeaItemShowList);

        progEstMeaItemShowList.clear();
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

        ProgEstMeaItemShow itemUnit=new ProgEstMeaItemShow();
        ProgEstMeaItemShow itemUnitNext=new ProgEstMeaItemShow();

        for(int i=0;i< progEstMeaItemShowListTemp.size();i++){
            itemUnit = progEstMeaItemShowListTemp.get(i);
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

            progEstMeaItemShowList.add(itemUnit);

            if(i+1< progEstMeaItemShowListTemp.size()){
                itemUnitNext = progEstMeaItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgEstMeaItemShow itemOfEsItemHieRelapTemp=new ProgEstMeaItemShow();
                    itemOfEsItemHieRelapTemp.setTkctt_Name("合计");
                    itemOfEsItemHieRelapTemp.setTkctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setTkctt_ContractQuantity(
                            ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                    itemOfEsItemHieRelapTemp.setTkctt_ContractAmount(
                            ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEAmount(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEAmount(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                    progEstMeaItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progEstMeaItemShowListTemp.size()){
                itemUnitNext = progEstMeaItemShowListTemp.get(i);
                ProgEstMeaItemShow progEstMeaItemShowTemp =new ProgEstMeaItemShow();
                progEstMeaItemShowTemp.setTkctt_Name("合计");
                progEstMeaItemShowTemp.setTkctt_Pkid("total"+i);
                progEstMeaItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progEstMeaItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progEstMeaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progEstMeaItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progEstMeaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progEstMeaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progEstMeaItemShowList.add(progEstMeaItemShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // 总合计
                progEstMeaItemShowTemp =new ProgEstMeaItemShow();
                progEstMeaItemShowTemp.setTkctt_Name("总合计");
                progEstMeaItemShowTemp.setTkctt_Pkid("total_all"+i);
                progEstMeaItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progEstMeaItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progEstMeaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progEstMeaItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progEstMeaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progEstMeaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progEstMeaItemShowList.add(progEstMeaItemShowTemp);
            }
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgEstMeaItemShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgEstMeaItemShow> progEstMeaItemShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgEstMeaItemShow itemUnit: progEstMeaItemShowListPara){
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

                ProgEstMeaItemShow itemUnitTemp= (ProgEstMeaItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progMeaItemShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return progEstMeaItemShowListPara;
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
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

    public ProgEstMeaItemShow getProgEstMeaItemShow() {
        return progEstMeaItemShow;
    }

    public void setProgEstMeaItemShow(ProgEstMeaItemShow progEstMeaItemShow) {
        this.progEstMeaItemShow = progEstMeaItemShow;
    }

    public ProgEstMeaItemShow getProgEstMeaItemShowSelected() {
        return progEstMeaItemShowSelected;
    }

    public void setProgEstMeaItemShowSelected(ProgEstMeaItemShow progEstMeaItemShowSelected) {
        this.progEstMeaItemShowSelected = progEstMeaItemShowSelected;
    }

    public List<ProgEstMeaItemShow> getProgEstMeaItemShowList() {
        return progEstMeaItemShowList;
    }

    public void setProgEstMeaItemShowList(List<ProgEstMeaItemShow> progEstMeaItemShowList) {
        this.progEstMeaItemShowList = progEstMeaItemShowList;
    }

    public EsItemStlTkcttEngMeaService getEsItemStlTkcttEngMeaService() {
        return esItemStlTkcttEngMeaService;
    }

    public void setEsItemStlTkcttEngMeaService(EsItemStlTkcttEngMeaService EsItemStlTkcttEngMeaService) {
        this.esItemStlTkcttEngMeaService = EsItemStlTkcttEngMeaService;
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

    public ProgEstMeaItemShow getProgEstMeaItemShowDel() {
        return progEstMeaItemShowDel;
    }

    public void setProgEstMeaItemShowDel(ProgEstMeaItemShow progEstMeaItemShowDel) {
        this.progEstMeaItemShowDel = progEstMeaItemShowDel;
    }

    public ProgEstMeaItemShow getProgEstMeaItemShowUpd() {
        return progEstMeaItemShowUpd;
    }

    public void setProgEstMeaItemShowUpd(ProgEstMeaItemShow progEstMeaItemShowUpd) {
        this.progEstMeaItemShowUpd = progEstMeaItemShowUpd;
    }
    /*智能字段End*/
}
