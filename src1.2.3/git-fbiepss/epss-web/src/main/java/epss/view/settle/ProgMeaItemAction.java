package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CommStlSubcttEngH;
import epss.repository.model.model_show.ProgMeaItemShow;
import skyline.util.MessageUtil;;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgInfoShow;
import epss.service.*;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.*;
import skyline.util.JxlsManager;
import java.text.SimpleDateFormat;
import java.io.IOException;
import jxl.write.WriteException;
/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgMeaItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMeaItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgMeaItemShow> progMeaItemShowList;
    private ProgMeaItemShow progMeaItemShowSel;
    private ProgMeaItemShow progMeaItemShowUpd;
    private ProgMeaItemShow progMeaItemShowDel;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private CommStlSubcttEngH commStlSubcttEngH;

    /*所属号*/
    private String strStlInfoPkid;
    private String strTkcttPkid;
    private EsInitStl esInitStl;

    private String strSubmitType;
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;

    private Map beansMap;
    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    private List<ProgMeaItemShow> progMeaItemShowListForExcel;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strFlowType")){
            strFlowType=parammap.get("strFlowType").toString();
        }
        if(parammap.containsKey("strStlInfoPkid")){
            strStlInfoPkid=parammap.get("strStlInfoPkid").toString();
            this.esInitStl = progStlInfoService.selectRecordsByPrimaryKey(strStlInfoPkid);
            strTkcttPkid= this.esInitStl.getStlPkid();
        }

        strPassFlag="true";
        if("Mng".equals(strFlowType) &&ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(esInitStl.getFlowStatus())) {
            strPassFlag="false";
        }

        resetAction();
        initData();

        /*分包合同数据*/
        // From StlPkid To SubcttPkid
        EsInitStl esInitStl = progStlInfoService.selectRecordsByPrimaryKey(strStlInfoPkid);
        commStlSubcttEngH.setStrSubcttPkid(esInitStl.getStlPkid());
        commStlSubcttEngH.setStrStlId(esInitStl.getId());
        // From SubcttPkid To CstplPkid
        EsCttInfo esCttInfo_Subctt= cttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrSubcttPkid());
        commStlSubcttEngH.setStrCstplPkid(esCttInfo_Subctt.getParentPkid());
        commStlSubcttEngH.setStrSubcttId(esCttInfo_Subctt.getId());
        commStlSubcttEngH.setStrSubcttName(esCttInfo_Subctt.getName());
        commStlSubcttEngH.setStrSignPartPkid(esCttInfo_Subctt.getSignPartB());
        commStlSubcttEngH.setStrSignPartName(signPartService.getEsInitCustByPkid(
                commStlSubcttEngH.getStrSignPartPkid()).getName());

        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
    }
    /*重置*/
    public void resetAction(){
        progMeaItemShowSel =new ProgMeaItemShow();
        progMeaItemShowUpd =new ProgMeaItemShow();
        progMeaItemShowDel =new ProgMeaItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progMeaItemShowUpd.setEng_PeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaListTemp =
                        progMeaItemService.isExistInDb(progMeaItemShowUpd);
                if (esItemStlTkcttEngMeaListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (esItemStlTkcttEngMeaListTemp.size() == 1) {
                    progMeaItemShowUpd.setEng_Pkid(esItemStlTkcttEngMeaListTemp.get(0).getPkid());
                    progMeaItemService.updateRecord(progMeaItemShowUpd);
                }
                if (esItemStlTkcttEngMeaListTemp.size()==0){
                    progMeaItemShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progMeaItemShowUpd.setEng_TkcttItemPkid(progMeaItemShowUpd.getTkctt_Pkid());
                    progMeaItemService.insertRecord(progMeaItemShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progMeaItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("提交数据失败，",e);
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progMeaItemShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progMeaItemShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progMeaItemShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getEng_BeginToCurrentPeriodEQty());

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

    private void delRecordAction(ProgMeaItemShow progMeaItemShowPara){
        try {
            if(progMeaItemShowPara.getEng_Pkid()==null||
                    progMeaItemShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum= progMeaItemService.deleteRecord(progMeaItemShowPara.getEng_Pkid()) ;
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
    public void selectRecordAction(String strSubmitTypePara,ProgMeaItemShow progMeaItemShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){
                progMeaItemShowSel =(ProgMeaItemShow)BeanUtils.cloneBean(progMeaItemShowPara) ;
                progMeaItemShowSel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMeaItemShowSel.getTkctt_StrNo()));
            }
            String strTkctt_Unit= progMeaItemShowPara.getTkctt_Unit();
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
                progMeaItemShowUpd =(ProgMeaItemShow) BeanUtils.cloneBean(progMeaItemShowPara) ;
                progMeaItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMeaItemShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getEng_BeginToCurrentPeriodEQty());
               }
            else if(strSubmitTypePara.equals("Del")){
                progMeaItemShowDel =(ProgMeaItemShow) BeanUtils.cloneBean(progMeaItemShowPara) ;
                progMeaItemShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMeaItemShowDel.getTkctt_StrNo()));
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
        progMeaItemShowListForExcel=new ArrayList<ProgMeaItemShow>();
        esCttItemList = cttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(), strTkcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progMeaItemShowList =new ArrayList<ProgMeaItemShow>();
        recursiveDataTable("root", esCttItemList, progMeaItemShowList);
        progMeaItemShowList =getItemStlTkcttEngSMList_DoFromatNo(progMeaItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progMeaItemShowListForExcel", progMeaItemShowListForExcel);
        // 表内容设定
        if(progMeaItemShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<ProgMeaItemShow> sProgMeaItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgMeaItemShow progMeaItemShowTemp = new ProgMeaItemShow();
            progMeaItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progMeaItemShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progMeaItemShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progMeaItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progMeaItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progMeaItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progMeaItemShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progMeaItemShowTemp.setTkctt_Name(itemUnit.getName());
            progMeaItemShowTemp.setTkctt_Note(itemUnit.getNote());
            progMeaItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progMeaItemShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progMeaItemShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progMeaItemShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            EsItemStlTkcttEngMea esItemStlTkcttEngMea=new EsItemStlTkcttEngMea();
            esItemStlTkcttEngMea.setTkcttPkid(strTkcttPkid);
            esItemStlTkcttEngMea.setTkcttItemPkid(progMeaItemShowTemp.getTkctt_Pkid());
            esItemStlTkcttEngMea.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList =
                    progMeaItemService.selectRecordsByKeyExample(esItemStlTkcttEngMea);
            if(esItemStlTkcttEngMeaList.size()>0){
                esItemStlTkcttEngMea= esItemStlTkcttEngMeaList.get(0);
                String strCreatedByName= ToolUtil.getUserName(esItemStlTkcttEngMea.getCreatedBy());
                String strLastUpdByName= ToolUtil.getUserName(esItemStlTkcttEngMea.getLastUpdBy());
                progMeaItemShowTemp.setEng_Pkid(esItemStlTkcttEngMea.getPkid());
                progMeaItemShowTemp.setEng_PeriodNo(esItemStlTkcttEngMea.getPeriodNo());
                progMeaItemShowTemp.setEng_TkcttPkid(esItemStlTkcttEngMea.getTkcttPkid());
                progMeaItemShowTemp.setEng_TkcttItemPkid (esItemStlTkcttEngMea.getTkcttItemPkid());
                progMeaItemShowTemp.setEng_CurrentPeriodEQty(esItemStlTkcttEngMea.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getCurrentPeriodQty());
                progMeaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progMeaItemShowTemp.setEng_BeginToCurrentPeriodEQty(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                progMeaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progMeaItemShowTemp.setEng_DeletedFlag(esItemStlTkcttEngMea.getDeleteFlag());
                progMeaItemShowTemp.setEng_CreatedBy(esItemStlTkcttEngMea.getCreatedBy());
                progMeaItemShowTemp.setEng_CreatedByName(strCreatedByName);
                progMeaItemShowTemp.setEng_CreatedDate(esItemStlTkcttEngMea.getCreatedDate());
                progMeaItemShowTemp.setEng_LastUpdBy(esItemStlTkcttEngMea.getLastUpdBy());
                progMeaItemShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progMeaItemShowTemp.setEng_LastUpdDate(esItemStlTkcttEngMea.getLastUpdDate());
                progMeaItemShowTemp.setEng_ModificationNum(esItemStlTkcttEngMea.getModificationNum());
            }
            sProgMeaItemShowListPara.add(progMeaItemShowTemp) ;
            recursiveDataTable(progMeaItemShowTemp.getTkctt_Pkid(), esCttItemListPara, sProgMeaItemShowListPara);
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgMeaItemShow> progMeaItemShowListTemp =new ArrayList<ProgMeaItemShow>();
        progMeaItemShowListTemp.addAll(progMeaItemShowList);

        progMeaItemShowList.clear();
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

        ProgMeaItemShow itemUnit=new ProgMeaItemShow();
        ProgMeaItemShow itemUnitNext=new ProgMeaItemShow();

        for(int i=0;i< progMeaItemShowListTemp.size();i++){
            itemUnit = progMeaItemShowListTemp.get(i);
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

            progMeaItemShowList.add(itemUnit);

            if(i+1< progMeaItemShowListTemp.size()){
                itemUnitNext = progMeaItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgMeaItemShow itemOfEsItemHieRelapTemp=new ProgMeaItemShow();
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
                    progMeaItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progMeaItemShowListTemp.size()){
                itemUnitNext = progMeaItemShowListTemp.get(i);
                ProgMeaItemShow progMeaItemShowTemp =new ProgMeaItemShow();
                progMeaItemShowTemp.setTkctt_Name("合计");
                progMeaItemShowTemp.setTkctt_Pkid("total"+i);
                progMeaItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progMeaItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progMeaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progMeaItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progMeaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progMeaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progMeaItemShowList.add(progMeaItemShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // 总合计
                progMeaItemShowTemp =new ProgMeaItemShow();
                progMeaItemShowTemp.setTkctt_Name("总合计");
                progMeaItemShowTemp.setTkctt_Pkid("total_all"+i);
                progMeaItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progMeaItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progMeaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progMeaItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progMeaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progMeaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progMeaItemShowList.add(progMeaItemShowTemp);
            }
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgMeaItemShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgMeaItemShow> progMeaItemShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgMeaItemShow itemUnit: progMeaItemShowListPara){
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

                ProgMeaItemShow itemUnitTemp= (ProgMeaItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progMeaItemShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return progMeaItemShowListPara;
    }
    public String onExportExcel()throws IOException, WriteException {
        if (this.progMeaItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "总包数量计量-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlTkcttEngMea.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
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
    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            strPowerType=strFlowType+strPowerType;
            if (strPowerType.contains("Mng")) {
                if (strPowerType.equals("MngPass")) {
                    // 状态标志：初始
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // 原因：录入完毕
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG0.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    strPassFlag = "false";
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerType.equals("MngFail")) {
                    esInitStl.setFlowStatus(null);
                    esInitStl.setFlowStatusReason(null);
                    progStlInfoService.updateRecord(esInitStl);
                    strPassFlag = "true";
                    MessageUtil.addInfo("数据录入未完！");
                }
            } else if (strPowerType.contains("Check") && !strPowerType.contains("DoubleCheck")) {// 审核
                if (strPowerType.equals("CheckPass")) {
                    // 状态标志：审核
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // 原因：审核通过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerType.equals("CheckFail")) {
                    // 状态标志：初始
                    esInitStl.setFlowStatus(null);
                    // 原因：审核未过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } else if (strPowerType.contains("DoubleCheck")) {// 复核
                if (strPowerType.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerType.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                        esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    }else if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                        esInitStl.setFlowStatus(null);
                    }

                    // 原因：复核未过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据复核未过！");
                }
            } else if (strPowerType.contains("Approve")) {// 批准
                if (strPowerType.equals("ApprovePass")) {
                    // 状态标志：批准
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // 原因：批准通过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据批准通过！");
                } else if (strPowerType.equals("ApproveFail")) {
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())) {
                        esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    }else if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                        esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    }else if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                        esInitStl.setFlowStatus(null);
                    }

                    // 原因：批准未过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据批准未过！");
                }
            }
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /* 智能字段Start*/
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public ProgMeaItemShow getProgMeaItemShowSel() {
        return progMeaItemShowSel;
    }

    public void setProgMeaItemShowSel(ProgMeaItemShow progMeaItemShowSel) {
        this.progMeaItemShowSel = progMeaItemShowSel;
    }

    public List<ProgMeaItemShow> getProgMeaItemShowList() {
        return progMeaItemShowList;
    }

    public void setProgMeaItemShowList(List<ProgMeaItemShow> progMeaItemShowList) {
        this.progMeaItemShowList = progMeaItemShowList;
    }

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService ProgMeaItemService) {
        this.progMeaItemService = ProgMeaItemService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
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

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public ProgMeaItemShow getProgMeaItemShowDel() {
        return progMeaItemShowDel;
    }

    public void setProgMeaItemShowDel(ProgMeaItemShow progMeaItemShowDel) {
        this.progMeaItemShowDel = progMeaItemShowDel;
    }

    public ProgMeaItemShow getProgMeaItemShowUpd() {
        return progMeaItemShowUpd;
    }

    public void setProgMeaItemShowUpd(ProgMeaItemShow progMeaItemShowUpd) {
        this.progMeaItemShowUpd = progMeaItemShowUpd;
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

    public List<ProgMeaItemShow> getProgMeaItemShowListForExcel() {
        return progMeaItemShowListForExcel;
    }

    public void setProgMeaItemShowListForExcel(List<ProgMeaItemShow> progMeaItemShowListForExcel) {
        this.progMeaItemShowListForExcel = progMeaItemShowListForExcel;
    }
	
	/*智能字段End*/
}
