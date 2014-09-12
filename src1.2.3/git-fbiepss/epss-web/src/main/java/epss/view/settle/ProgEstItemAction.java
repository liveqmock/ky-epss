package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.model.model_show.ReportHeader;
import epss.repository.model.model_show.ProgEstItemShow;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.*;
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
public class ProgEstItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgEstItemAction.class);
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
    @ManagedProperty(value = "#{progEstItemService}")
    private ProgEstItemService progEstItemService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    private List<ProgEstItemShow> progEstItemShowList;
    private ProgEstItemShow progEstItemShowSel;
    private ProgEstItemShow progEstItemShowUpd;
    private ProgEstItemShow progEstItemShowDel;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private ReportHeader reportHeader;

    /*所属号*/
    private String strStlInfoPkid;
    private String strTkcttPkid;
    private EsInitStl esInitStl;

    private ProgInfoShow progInfoShow;

    private String strSubmitType;
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;

    private Map beansMap;
    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    private List<ProgEstItemShow> progEstItemShowListForExcel;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        reportHeader =new ReportHeader();
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
        if("Mng".equals(strFlowType) && ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(esInitStl.getFlowStatus())) {
            strPassFlag="false";
        }

        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
         /*分包合同数据*/
        // From StlPkid To SubcttPkid
        EsInitStl esInitStl = progStlInfoService.selectRecordsByPrimaryKey(strStlInfoPkid);
        reportHeader.setStrSubcttPkid(esInitStl.getStlPkid());
        reportHeader.setStrStlId(esInitStl.getId());
        // From SubcttPkid To CstplPkid
        EsCttInfo esCttInfoTemp= cttInfoService.getCttInfoByPkId(reportHeader.getStrSubcttPkid());
        reportHeader.setStrCstplPkid(esCttInfoTemp.getParentPkid());
        reportHeader.setStrSubcttId(esCttInfoTemp.getId());
        reportHeader.setStrSubcttName(esCttInfoTemp.getName());
        reportHeader.setStrSignPartPkid(esCttInfoTemp.getSignPartB());
        reportHeader.setStrSignPartName(signPartService.getEsInitCustByPkid(
                reportHeader.getStrSignPartPkid()).getName());

        beansMap.put("reportHeader", reportHeader);

        progInfoShow=progStlInfoService.fromModelToModelShow(esInitStl);
        progInfoShow.setStlName(esCttInfoTemp.getName());
        progInfoShow.setSignPartBName(reportHeader.getStrSignPartName());

        /*分包合同*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        progEstItemShowListForExcel=new ArrayList<ProgEstItemShow>();
        esCttItemList = cttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(), strTkcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progEstItemShowList =new ArrayList<ProgEstItemShow>();
        recursiveDataTable("root", esCttItemList, progEstItemShowList);
        progEstItemShowList =getItemStlTkcttEngSMList_DoFromatNo(progEstItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progEstItemShowListForExcel", progEstItemShowListForExcel);
        // 表内容设定
        if(progEstItemShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgEstItemShow> sprogEstItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgEstItemShow progEstItemShowTemp = new ProgEstItemShow();
            progEstItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progEstItemShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progEstItemShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progEstItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progEstItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progEstItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progEstItemShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progEstItemShowTemp.setTkctt_Name(itemUnit.getName());
            progEstItemShowTemp.setTkctt_Note(itemUnit.getNote());
            progEstItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progEstItemShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progEstItemShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progEstItemShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            EsItemStlTkcttEngSta esItemStlTkcttEngSta=new EsItemStlTkcttEngSta();
            esItemStlTkcttEngSta.setTkcttPkid(strTkcttPkid);
            esItemStlTkcttEngSta.setTkcttItemPkid(progEstItemShowTemp.getTkctt_Pkid());
            esItemStlTkcttEngSta.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaList =
                    progEstItemService.selectRecordsByExample(esItemStlTkcttEngSta);
            if(esItemStlTkcttEngStaList.size()>0){
                esItemStlTkcttEngSta= esItemStlTkcttEngStaList.get(0);
                String strCreatedByName= ToolUtil.getUserName(esItemStlTkcttEngSta.getCreatedBy());
                String strLastUpdByName= ToolUtil.getUserName(esItemStlTkcttEngSta.getLastUpdBy());
                progEstItemShowTemp.setEng_Pkid(esItemStlTkcttEngSta.getPkid());
                progEstItemShowTemp.setEng_PeriodNo(esItemStlTkcttEngSta.getPeriodNo());
                progEstItemShowTemp.setEng_TkcttPkid(esItemStlTkcttEngSta.getTkcttPkid());
                progEstItemShowTemp.setEng_TkcttItemPkid(esItemStlTkcttEngSta.getTkcttItemPkid());
                progEstItemShowTemp.setEng_CurrentPeriodEQty(esItemStlTkcttEngSta.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngSta.getCurrentPeriodQty());
                progEstItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progEstItemShowTemp.setEng_BeginToCurrentPeriodEQty(esItemStlTkcttEngSta.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngSta.getBeginToCurrentPeriodQty());
                progEstItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progEstItemShowTemp.setEng_DeletedFlag(esItemStlTkcttEngSta.getDeleteFlag());
                progEstItemShowTemp.setEng_CreatedBy(esItemStlTkcttEngSta.getCreatedBy());
                progEstItemShowTemp.setEng_CreatedByName(strCreatedByName);
                progEstItemShowTemp.setEng_CreatedDate(esItemStlTkcttEngSta.getCreatedDate());
                progEstItemShowTemp.setEng_LastUpdBy(esItemStlTkcttEngSta.getLastUpdBy());
                progEstItemShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progEstItemShowTemp.setEng_LastUpdDate(esItemStlTkcttEngSta.getLastUpdDate());
                progEstItemShowTemp.setEng_ModificationNum(esItemStlTkcttEngSta.getModificationNum());
            }
            sprogEstItemShowListPara.add(progEstItemShowTemp) ;
            recursiveDataTable(progEstItemShowTemp.getTkctt_Pkid(), esCttItemListPara, sprogEstItemShowListPara);
        }
    }

    /*重置*/
    public void resetAction(){
        progEstItemShowSel =new ProgEstItemShow();
        progEstItemShowUpd =new ProgEstItemShow();
        progEstItemShowDel =new ProgEstItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progEstItemShowUpd.setEng_PeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaListTemp =
                        progEstItemService.isExistInDb(progEstItemShowUpd);
                if (esItemStlTkcttEngStaListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (esItemStlTkcttEngStaListTemp.size() == 1) {
                    progEstItemShowUpd.setEng_Pkid(esItemStlTkcttEngStaListTemp.get(0).getPkid());
                    progEstItemService.updateRecord(progEstItemShowUpd);
                }
                if (esItemStlTkcttEngStaListTemp.size()==0){
				    progEstItemShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progEstItemShowUpd.setEng_TkcttItemPkid(progEstItemShowUpd.getTkctt_Pkid());
                    progEstItemService.insertRecord(progEstItemShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progEstItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("提交数据失败，",e);
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progEstItemShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progEstItemShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progEstItemShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getEng_BeginToCurrentPeriodEQty());

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

    private void delRecordAction(ProgEstItemShow progEstItemShowPara){
        try {
            if(progEstItemShowPara.getEng_Pkid()==null||
                    progEstItemShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum= progEstItemService.deleteRecord(progEstItemShowPara.getEng_Pkid()) ;
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
    public void selectRecordAction(String strSubmitTypePara,ProgEstItemShow progEstItemShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){                
                progEstItemShowSel =(ProgEstItemShow)BeanUtils.cloneBean(progEstItemShowPara) ;
                progEstItemShowSel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstItemShowSel.getTkctt_StrNo()));
            }            
            String strTkctt_Unit= progEstItemShowPara.getTkctt_Unit();
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
                progEstItemShowUpd =(ProgEstItemShow) BeanUtils.cloneBean(progEstItemShowPara) ;
                progEstItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstItemShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getEng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progEstItemShowDel =(ProgEstItemShow) BeanUtils.cloneBean(progEstItemShowPara) ;
                progEstItemShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstItemShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgEstItemShow> progEstItemShowListTemp =new ArrayList<ProgEstItemShow>();
        progEstItemShowListTemp.addAll(progEstItemShowList);

        progEstItemShowList.clear();
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

        ProgEstItemShow itemUnit=new ProgEstItemShow();
        ProgEstItemShow itemUnitNext=new ProgEstItemShow();

        for(int i=0;i< progEstItemShowListTemp.size();i++){
            itemUnit = progEstItemShowListTemp.get(i);
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

            progEstItemShowList.add(itemUnit);

            if(i+1< progEstItemShowListTemp.size()){
                itemUnitNext = progEstItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgEstItemShow itemOfEsItemHieRelapTemp=new ProgEstItemShow();
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
                    progEstItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progEstItemShowListTemp.size()){
                itemUnitNext = progEstItemShowListTemp.get(i);
                ProgEstItemShow progEstItemShowTemp =new ProgEstItemShow();
                progEstItemShowTemp.setTkctt_Name("合计");
                progEstItemShowTemp.setTkctt_Pkid("total"+i);
                progEstItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progEstItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progEstItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progEstItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progEstItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progEstItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progEstItemShowList.add(progEstItemShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // 总合计
                progEstItemShowTemp =new ProgEstItemShow();
                progEstItemShowTemp.setTkctt_Name("总合计");
                progEstItemShowTemp.setTkctt_Pkid("total_all"+i);
                progEstItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progEstItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progEstItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progEstItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progEstItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progEstItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progEstItemShowList.add(progEstItemShowTemp);
            }
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgEstItemShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgEstItemShow> progEstItemShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgEstItemShow itemUnit: progEstItemShowListPara){
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

                ProgEstItemShow itemUnitTemp= (ProgEstItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progEstItemShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
        return progEstItemShowListPara;
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.progEstItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "总包数量统计-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progEst.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
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

    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            strPowerType=strFlowType+strPowerType;
            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    // 状态标志：初始
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // 原因：录入完毕
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG0.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    strPassFlag="false";
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    esInitStl.setFlowStatus(null);
                    esInitStl.setFlowStatusReason(null);
                    progStlInfoService.updateRecord(esInitStl);
                    strPassFlag="true";
                    MessageUtil.addInfo("数据录入未完！");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// 审核
                if(strPowerType.equals("CheckPass")){
                    // 状态标志：审核
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // 原因：审核通过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据审核通过！");
                }else if(strPowerType.equals("CheckFail")){
                    // 状态标志：初始
                    esInitStl.setFlowStatus(null);
                    // 原因：审核未过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据审核未过！");
                }
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    // 状态标志：复核
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据复核通过！");
                }else if(strPowerType.equals("DoubleCheckFail")){
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
            } else if(strPowerType.contains("Approve")){// 批准
                if(strPowerType.equals("ApprovePass")){
                    // 状态标志：批准
                    esInitStl.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // 原因：批准通过
                    esInitStl.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    progStlInfoService.updateRecord(esInitStl);
                    MessageUtil.addInfo("数据批准通过！");
                }else if(strPowerType.equals("ApproveFail")){
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

    public ProgInfoShow getProgInfoShow() {
        return progInfoShow;
    }

    public void setProgInfoShow(ProgInfoShow progInfoShow) {
        this.progInfoShow = progInfoShow;
    }

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

    public ProgEstItemService getProgEstItemService() {
        return progEstItemService;
    }

    public void setProgEstItemService(ProgEstItemService ProgEstItemService) {
        this.progEstItemService = ProgEstItemService;
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

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
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
    public String getStrMngNotFinishFlag() {
        return strPassFlag;
    }

    public BigDecimal getbDEng_BeginToCurrentPeriodEQtyInDB() {
        return bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public void setbDEng_BeginToCurrentPeriodEQtyInDB(BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB) {
        this.bDEng_BeginToCurrentPeriodEQtyInDB = bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public ProgEstItemShow getProgEstItemShowSel() {
        return progEstItemShowSel;
    }

    public void setProgEstItemShowSel(ProgEstItemShow progEstItemShowSel) {
        this.progEstItemShowSel = progEstItemShowSel;
    }

    public ProgEstItemShow getProgEstItemShowDel() {
        return progEstItemShowDel;
    }

    public void setProgEstItemShowDel(ProgEstItemShow progEstItemShowDel) {
        this.progEstItemShowDel = progEstItemShowDel;
    }

    public ProgEstItemShow getProgEstItemShowUpd() {
        return progEstItemShowUpd;
    }

    public void setProgEstItemShowUpd(ProgEstItemShow progEstItemShowUpd) {
        this.progEstItemShowUpd = progEstItemShowUpd;
    }

    public List<ProgEstItemShow> getProgEstItemShowList() {
        return progEstItemShowList;
    }

    public void setProgEstItemShowList(List<ProgEstItemShow> progEstItemShowList) {
        this.progEstItemShowList = progEstItemShowList;
    }

    public List<ProgEstItemShow> getProgEstItemShowListForExcel() {
        return progEstItemShowListForExcel;
    }

    public void setProgEstItemShowListForExcel(List<ProgEstItemShow> progEstItemShowListForExcel) {
        this.progEstItemShowListForExcel = progEstItemShowListForExcel;
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
