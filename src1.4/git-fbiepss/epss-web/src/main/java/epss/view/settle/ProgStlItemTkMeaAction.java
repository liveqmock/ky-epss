package epss.view.settle;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemTkMeaShow;
import epss.repository.model.model_show.ReportHeader;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
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
public class ProgStlItemTkMeaAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemTkMeaAction.class);
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
    @ManagedProperty(value = "#{progStlItemTkMeaService}")
    private ProgStlItemTkMeaService progStlItemTkMeaService;

    private List<ProgStlItemTkMeaShow> progStlItemTkMeaShowList;
    private ProgStlItemTkMeaShow progStlItemTkMeaShowSel;
    private ProgStlItemTkMeaShow progStlItemTkMeaShowUpd;
    private ProgStlItemTkMeaShow progStlItemTkMeaShowDel;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private ReportHeader reportHeader;

    /*所属号*/
    private String strStlInfoPkid;
    private String strTkcttPkid;
    private ProgStlInfo progStlInfo;

    private ProgStlInfoShow progStlInfoShow;

    private String strSubmitType;
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;

    private Map beansMap;
    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    private List<ProgStlItemTkMeaShow> progStlItemTkMeaShowListForExcel;

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
            this.progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
            strTkcttPkid= this.progStlInfo.getStlPkid();
        }

        strPassFlag="true";
        if("Mng".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfo.getFlowStatus())) {
            strPassFlag="false";
        }

        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
        /*分包合同数据*/
        // From StlPkid To SubcttPkid
        ProgStlInfo progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
        reportHeader.setStrSubcttPkid(progStlInfo.getStlPkid());
        reportHeader.setStrStlId(progStlInfo.getId());
        // From SubcttPkid To CstplPkid
        CttInfo cttInfoTemp = cttInfoService.getCttInfoByPkId(reportHeader.getStrSubcttPkid());
        reportHeader.setStrCstplPkid(cttInfoTemp.getParentPkid());
        reportHeader.setStrSubcttId(cttInfoTemp.getId());
        reportHeader.setStrSubcttName(cttInfoTemp.getName());
        reportHeader.setStrSignPartPkid(cttInfoTemp.getSignPartB());
        SignPart signPartTemp=signPartService.getEsInitCustByPkid(
                reportHeader.getStrSignPartPkid());
        if(signPartTemp!=null) {
            reportHeader.setStrSignPartName(signPartTemp.getName());
        }

        beansMap.put("reportHeader", reportHeader);

        progStlInfoShow =progStlInfoService.fromModelToModelShow(progStlInfo);
        progStlInfoShow.setStlId(cttInfoTemp.getId());
        progStlInfoShow.setStlName(cttInfoTemp.getName());
        progStlInfoShow.setSignPartBName(reportHeader.getStrSignPartName());

        /*分包合同*/
        List<CttItem> cttItemList =new ArrayList<CttItem>();
        progStlItemTkMeaShowListForExcel =new ArrayList<ProgStlItemTkMeaShow>();
        cttItemList = cttItemService.getEsItemList(
                EnumResType.RES_TYPE0.getCode(), strTkcttPkid);
        if(cttItemList.size()<=0){
            return;
        }
        progStlItemTkMeaShowList =new ArrayList<ProgStlItemTkMeaShow>();
        recursiveDataTable("root", cttItemList, progStlItemTkMeaShowList);
        progStlItemTkMeaShowList =getItemStlTkcttEngSMList_DoFromatNo(progStlItemTkMeaShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progStlItemTkMeaShowListForExcel", progStlItemTkMeaShowListForExcel);
        // 表内容设定
        if(progStlItemTkMeaShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemTkMeaShow> sProgStlItemTkMeaShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemTkMeaShow progStlItemTkMeaShowTemp = new ProgStlItemTkMeaShow();
            progStlItemTkMeaShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progStlItemTkMeaShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progStlItemTkMeaShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemTkMeaShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemTkMeaShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progStlItemTkMeaShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progStlItemTkMeaShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemTkMeaShowTemp.setTkctt_Name(itemUnit.getName());
            progStlItemTkMeaShowTemp.setTkctt_Remark(itemUnit.getRemark());
            progStlItemTkMeaShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progStlItemTkMeaShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progStlItemTkMeaShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemTkMeaShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            ProgStlItemTkMea progStlItemTkMea =new ProgStlItemTkMea();
            progStlItemTkMea.setTkcttPkid(strTkcttPkid);
            progStlItemTkMea.setTkcttItemPkid(progStlItemTkMeaShowTemp.getTkctt_Pkid());
            progStlItemTkMea.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemTkMea> progStlItemTkMeaList =
                    progStlItemTkMeaService.selectRecordsByKeyExample(progStlItemTkMea);
            if(progStlItemTkMeaList.size()>0){
                progStlItemTkMea = progStlItemTkMeaList.get(0);
                String strCreatedByName= ToolUtil.getUserName(progStlItemTkMea.getCreatedBy());
                String strLastUpdByName= ToolUtil.getUserName(progStlItemTkMea.getLastUpdBy());
                progStlItemTkMeaShowTemp.setEng_Pkid(progStlItemTkMea.getPkid());
                progStlItemTkMeaShowTemp.setEng_PeriodNo(progStlItemTkMea.getPeriodNo());
                progStlItemTkMeaShowTemp.setEng_TkcttPkid(progStlItemTkMea.getTkcttPkid());
                progStlItemTkMeaShowTemp.setEng_TkcttItemPkid (progStlItemTkMea.getTkcttItemPkid());
                progStlItemTkMeaShowTemp.setEng_CurrentPeriodEQty(progStlItemTkMea.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progStlItemTkMea.getCurrentPeriodQty());
                progStlItemTkMeaShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progStlItemTkMeaShowTemp.setEng_BeginToCurrentPeriodEQty(progStlItemTkMea.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progStlItemTkMea.getBeginToCurrentPeriodQty());
                progStlItemTkMeaShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progStlItemTkMeaShowTemp.setEng_ArchivedFlag(progStlItemTkMea.getArchivedFlag());
                progStlItemTkMeaShowTemp.setEng_CreatedBy(progStlItemTkMea.getCreatedBy());
                progStlItemTkMeaShowTemp.setEng_CreatedByName(strCreatedByName);
                progStlItemTkMeaShowTemp.setEng_CreatedTime(progStlItemTkMea.getCreatedTime());
                progStlItemTkMeaShowTemp.setEng_LastUpdBy(progStlItemTkMea.getLastUpdBy());
                progStlItemTkMeaShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progStlItemTkMeaShowTemp.setEng_LastUpdTime(progStlItemTkMea.getLastUpdTime());
                progStlItemTkMeaShowTemp.setEng_RecVersion(progStlItemTkMea.getRecVersion());
                if (progStlItemTkMeaShowTemp.getEng_BeginToCurrentPeriodEQty()
                        .equals(progStlItemTkMeaShowTemp.getTkctt_ContractQuantity())){
                    progStlItemTkMeaShowTemp.setIsUptoCttContentFlag(true);
                }
            }
            sProgStlItemTkMeaShowListPara.add(progStlItemTkMeaShowTemp) ;
            recursiveDataTable(progStlItemTkMeaShowTemp.getTkctt_Pkid(), cttItemListPara, sProgStlItemTkMeaShowListPara);
        }
    }

    /*重置*/
    public void resetAction(){
        progStlItemTkMeaShowSel =new ProgStlItemTkMeaShow();
        progStlItemTkMeaShowUpd =new ProgStlItemTkMeaShow();
        progStlItemTkMeaShowDel =new ProgStlItemTkMeaShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progStlItemTkMeaShowUpd.setEng_PeriodNo(progStlInfo.getPeriodNo());
                List<ProgStlItemTkMea> progStlItemTkMeaListTemp =
                        progStlItemTkMeaService.isExistInDb(progStlItemTkMeaShowUpd);
                if (progStlItemTkMeaListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (progStlItemTkMeaListTemp.size() == 1) {
                    progStlItemTkMeaShowUpd.setEng_Pkid(progStlItemTkMeaListTemp.get(0).getPkid());
                    progStlItemTkMeaService.updateRecord(progStlItemTkMeaShowUpd);
                }
                if (progStlItemTkMeaListTemp.size()==0){
                    progStlItemTkMeaShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progStlItemTkMeaShowUpd.setEng_TkcttItemPkid(progStlItemTkMeaShowUpd.getTkctt_Pkid());
                    progStlItemTkMeaService.insertRecord(progStlItemTkMeaShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemTkMeaShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("提交数据失败，",e);
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progStlItemTkMeaShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progStlItemTkMeaShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progStlItemTkMeaShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemTkMeaShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progStlItemTkMeaShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progStlItemTkMeaShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progStlItemTkMeaShowUpd.getEng_BeginToCurrentPeriodEQty());

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

    private void delRecordAction(ProgStlItemTkMeaShow progStlItemTkMeaShowPara){
        try {
            if(progStlItemTkMeaShowPara.getEng_Pkid()==null||
                    progStlItemTkMeaShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum= progStlItemTkMeaService.deleteRecord(progStlItemTkMeaShowPara.getEng_Pkid()) ;
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
    public void selectRecordAction(String strSubmitTypePara,ProgStlItemTkMeaShow progStlItemTkMeaShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){
                progStlItemTkMeaShowSel =(ProgStlItemTkMeaShow)BeanUtils.cloneBean(progStlItemTkMeaShowPara) ;
                progStlItemTkMeaShowSel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkMeaShowSel.getTkctt_StrNo()));
            }
            String strTkctt_Unit= progStlItemTkMeaShowPara.getTkctt_Unit();
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
                progStlItemTkMeaShowUpd =(ProgStlItemTkMeaShow) BeanUtils.cloneBean(progStlItemTkMeaShowPara) ;
                progStlItemTkMeaShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkMeaShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progStlItemTkMeaShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progStlItemTkMeaShowUpd.getEng_BeginToCurrentPeriodEQty());
               }
            else if(strSubmitTypePara.equals("Del")){
                progStlItemTkMeaShowDel =(ProgStlItemTkMeaShow) BeanUtils.cloneBean(progStlItemTkMeaShowPara) ;
                progStlItemTkMeaShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkMeaShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgStlItemTkMeaShow> progStlItemTkMeaShowListTemp =new ArrayList<ProgStlItemTkMeaShow>();
        progStlItemTkMeaShowListTemp.addAll(progStlItemTkMeaShowList);

        progStlItemTkMeaShowList.clear();
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

        ProgStlItemTkMeaShow itemUnit=new ProgStlItemTkMeaShow();
        ProgStlItemTkMeaShow itemUnitNext=new ProgStlItemTkMeaShow();

        for(int i=0;i< progStlItemTkMeaShowListTemp.size();i++){
            itemUnit = progStlItemTkMeaShowListTemp.get(i);
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

            progStlItemTkMeaShowList.add(itemUnit);

            if(i+1< progStlItemTkMeaShowListTemp.size()){
                itemUnitNext = progStlItemTkMeaShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgStlItemTkMeaShow itemOfEsItemHieRelapTemp=new ProgStlItemTkMeaShow();
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
                    progStlItemTkMeaShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progStlItemTkMeaShowListTemp.size()){
                itemUnitNext = progStlItemTkMeaShowListTemp.get(i);
                ProgStlItemTkMeaShow progStlItemTkMeaShowTemp =new ProgStlItemTkMeaShow();
                progStlItemTkMeaShowTemp.setTkctt_Name("合计");
                progStlItemTkMeaShowTemp.setTkctt_Pkid("total"+i);
                progStlItemTkMeaShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progStlItemTkMeaShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progStlItemTkMeaShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progStlItemTkMeaShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progStlItemTkMeaShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progStlItemTkMeaShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progStlItemTkMeaShowList.add(progStlItemTkMeaShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // 总合计
                progStlItemTkMeaShowTemp =new ProgStlItemTkMeaShow();
                progStlItemTkMeaShowTemp.setTkctt_Name("总合计");
                progStlItemTkMeaShowTemp.setTkctt_Pkid("total_all"+i);
                progStlItemTkMeaShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progStlItemTkMeaShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progStlItemTkMeaShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progStlItemTkMeaShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progStlItemTkMeaShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progStlItemTkMeaShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progStlItemTkMeaShowList.add(progStlItemTkMeaShowTemp);
            }
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgStlItemTkMeaShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgStlItemTkMeaShow> progStlItemTkMeaShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgStlItemTkMeaShow itemUnit: progStlItemTkMeaShowListPara){
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

                ProgStlItemTkMeaShow itemUnitTemp= (ProgStlItemTkMeaShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progStlItemTkMeaShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return progStlItemTkMeaShowListPara;
    }

    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "总包数量计量-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"progStlItemTkMea.xls");
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
             List<CttItem> cttItemListPara) {
        List<CttItem> tempCttItemList =new ArrayList<CttItem>();
        /*避开重复链接数据库*/
        for(CttItem itemUnit: cttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempCttItemList.add(itemUnit);
            }
        }
        return tempCttItemList;
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
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：录入完毕
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    strPassFlag = "false";
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerType.equals("MngFail")) {
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    strPassFlag = "true";
                    MessageUtil.addInfo("数据录入未完！");
                }
            } else if (strPowerType.contains("Check") && !strPowerType.contains("DoubleCheck")) {// 审核
                if (strPowerType.equals("CheckPass")) {
                    // 状态标志：审核
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerType.equals("CheckFail")) {
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(null);
                    // 原因：审核未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } else if (strPowerType.contains("DoubleCheck")) {// 复核
                if (strPowerType.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerType.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        progStlInfo.setFlowStatus(null);
                    }

                    // 原因：复核未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据复核未过！");
                }
            } else if (strPowerType.contains("Approve")) {// 批准
                if (strPowerType.equals("ApprovePass")) {
                    // 状态标志：批准
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据批准通过！");
                } else if (strPowerType.equals("ApproveFail")) {
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        progStlInfo.setFlowStatus(null);
                    }

                    // 原因：批准未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据批准未过！");
                }
            }
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /* 智能字段Start*/

    public ProgStlInfoShow getProgStlInfoShow() {
        return progStlInfoShow;
    }

    public void setProgStlInfoShow(ProgStlInfoShow progStlInfoShow) {
        this.progStlInfoShow = progStlInfoShow;
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

    public ProgStlItemTkMeaShow getProgStlItemTkMeaShowSel() {
        return progStlItemTkMeaShowSel;
    }

    public void setProgStlItemTkMeaShowSel(ProgStlItemTkMeaShow progStlItemTkMeaShowSel) {
        this.progStlItemTkMeaShowSel = progStlItemTkMeaShowSel;
    }

    public List<ProgStlItemTkMeaShow> getProgStlItemTkMeaShowList() {
        return progStlItemTkMeaShowList;
    }

    public void setProgStlItemTkMeaShowList(List<ProgStlItemTkMeaShow> progStlItemTkMeaShowList) {
        this.progStlItemTkMeaShowList = progStlItemTkMeaShowList;
    }

    public ProgStlItemTkMeaService getProgStlItemTkMeaService() {
        return progStlItemTkMeaService;
    }

    public void setProgStlItemTkMeaService(ProgStlItemTkMeaService ProgStlItemTkMeaService) {
        this.progStlItemTkMeaService = ProgStlItemTkMeaService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
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

    public ProgStlItemTkMeaShow getProgStlItemTkMeaShowDel() {
        return progStlItemTkMeaShowDel;
    }

    public void setProgStlItemTkMeaShowDel(ProgStlItemTkMeaShow progStlItemTkMeaShowDel) {
        this.progStlItemTkMeaShowDel = progStlItemTkMeaShowDel;
    }

    public ProgStlItemTkMeaShow getProgStlItemTkMeaShowUpd() {
        return progStlItemTkMeaShowUpd;
    }

    public void setProgStlItemTkMeaShowUpd(ProgStlItemTkMeaShow progStlItemTkMeaShowUpd) {
        this.progStlItemTkMeaShowUpd = progStlItemTkMeaShowUpd;
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

    public List<ProgStlItemTkMeaShow> getProgStlItemTkMeaShowListForExcel() {
        return progStlItemTkMeaShowListForExcel;
    }

    public void setProgStlItemTkMeaShowListForExcel(List<ProgStlItemTkMeaShow> progStlItemTkMeaShowListForExcel) {
        this.progStlItemTkMeaShowListForExcel = progStlItemTkMeaShowListForExcel;
    }
	
	/*智能字段End*/
}
