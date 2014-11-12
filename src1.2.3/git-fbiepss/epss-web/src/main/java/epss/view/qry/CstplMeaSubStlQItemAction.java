package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.CttItem;
import epss.repository.model.ProgStlItemTkMea;
import skyline.util.JxlsManager;
import epss.repository.model.CttInfo;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.service.*;
import epss.service.EsQueryService;
import epss.view.flow.EsCommon;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class CstplMeaSubStlQItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplMeaSubStlQItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progStlItemTkMeaService}")
    private ProgStlItemTkMeaService progStlItemTkMeaService;

    /*列表显示用*/
    private List<QryCSStlQShow> qryCSMeaSubQShowList;
    private List<QryCSStlQShow> qryCSMeaSubQShowListForExcel;

    private List<SelectItem> cstplList;

    private String strCstplPkid;
    private String strPeriodNo;

    // 画面上控件的显示控制
    private ReportHeader reportHeader;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        reportHeader =new ReportHeader();
        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_Status(
                        EnumResType.RES_TYPE1.getCode()
                      , EnumFlowStatus.FLOW_STATUS3.getCode());
        cstplList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","");
            cstplList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                cstplList.add(selectItem);
            }
        }
        strPeriodNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException,WriteException {
        if (this.qryCSMeaSubQShowListForExcel.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "成分计量数量结算比较-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCSMeaSubQ.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }
    private void initData(String strBelongToPkid) {
        List<ProgStlItemTkMea> progStlItemTkMeaList =new ArrayList<ProgStlItemTkMea>();

        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        CttInfo esInitCttCstpl= cttInfoService.getCttInfoByPkId(strBelongToPkid);
        reportHeader.setStrCstplId(esInitCttCstpl.getId());
        reportHeader.setStrCstplName(esInitCttCstpl.getName());
        beansMap.put("reportHeader", reportHeader);
        /*成本计划列表*/
        List<CttItem> cttItemListCstpl =new ArrayList<CttItem>();
        cttItemListCstpl = cttItemService.getEsItemList(
                EnumResType.RES_TYPE1.getCode(),
                strCstplPkid);
        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", cttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListCstpl);

        String strMeaLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestApprovedPeriodNoByEndPeriod(
                        EnumResType.RES_TYPE7.getCode(),
                        esInitCttCstpl.getParentPkid(),
                        strPeriodNo));

        if(!ToolUtil.getStrIgnoreNull(strMeaLatestApprovedPeriodNo).equals("")){
            ProgStlItemTkMea progStlItemTkMea =new ProgStlItemTkMea();
            progStlItemTkMea.setTkcttPkid(esInitCttCstpl.getParentPkid());
            progStlItemTkMea.setPeriodNo(strMeaLatestApprovedPeriodNo);
            progStlItemTkMeaList =
                    progStlItemTkMeaService.selectRecordsByPkidPeriodNoExample(progStlItemTkMea);
        }

        List<QryShow> qryShowList =esQueryService.getCSStlQList(strBelongToPkid, strPeriodNo);

        /*拼装列表*/
        try {
            qryCSMeaSubQShowList =new ArrayList<QryCSStlQShow>();
            QryCSStlQShow itemCstplInsertItem;

            for(CttItemShow itemUnit: cttItemShowListCstpl){
                // 总包计量
                BigDecimal bdTkcttStlMeaQuantity=new BigDecimal(0);
                BigDecimal bdTkcttStlMeaAmount=new BigDecimal(0);
                // 成本计划
                BigDecimal bdCstplContractUnitPrice=new BigDecimal(0);

                if(itemUnit.getUnit()!=null){
                    bdCstplContractUnitPrice=
                            ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
                }
                else{
                    bdCstplContractUnitPrice=itemUnit.getContractUnitPrice();
                }

                itemCstplInsertItem=new QryCSStlQShow();
                // 成本计划
                itemCstplInsertItem.setStrCstpl_Pkid(itemUnit.getPkid());
                itemCstplInsertItem.setStrCstpl_ParentPkid(itemUnit.getParentPkid());
                itemCstplInsertItem.setStrCstpl_No(itemUnit.getStrNo());
                itemCstplInsertItem.setStrCstpl_Name(itemUnit.getName());
                itemCstplInsertItem.setStrCstpl_Unit(itemUnit.getUnit());
                itemCstplInsertItem.setBdCstpl_ContractUnitPrice(bdCstplContractUnitPrice);

                // 统计计量
                for(ProgStlItemTkMea progStlItemTkMeaUnit : progStlItemTkMeaList){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getCorrespondingPkid()).equals(
                            progStlItemTkMeaUnit.getTkcttItemPkid())){
                        itemCstplInsertItem.setBdTkcttStl_MeaQuantity(progStlItemTkMeaUnit.getBeginToCurrentPeriodQty());

                        bdTkcttStlMeaQuantity=ToolUtil.getBdIgnoreNull(progStlItemTkMeaUnit.getBeginToCurrentPeriodQty());
                        bdTkcttStlMeaAmount=bdTkcttStlMeaQuantity.multiply(bdCstplContractUnitPrice);
                        itemCstplInsertItem.setBdTkcttStl_MeaAmount(bdTkcttStlMeaAmount);
                        break;
                    }
                }

                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttContractQuantityTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractUnitPriceTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractAmountTotal=new BigDecimal(0);
                // 根据成本计划项插接分包合同项
                for(int i=0;i< qryShowList.size();i++) {
                    // 成本计划项遇到目标分包合同项
                    if(itemUnit.getPkid().equals(qryShowList.get(i).getStrCorrespondingPkid())) {
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // 克隆目标进行处理后插接
                        QryCSStlQShow qryCSStlQShowNewInsert =(QryCSStlQShow)BeanUtils.cloneBean(itemCstplInsertItem);
                        // 目标分包合同项的合同数量，合同单价，合同金额
                        BigDecimal bdSubcttBeginToCurrentPeriodQuantity=
                                ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdSubcttContractUnitPrice=
                                ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdUnitPrice());
                        BigDecimal bdSubcttBeginToCurrentPeriodAmount=
                                bdSubcttBeginToCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);
                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttContractQuantityTotal=
                                bdSubcttContractQuantityTotal.add(bdSubcttBeginToCurrentPeriodQuantity);
                        bdSubcttContractUnitPriceTotal=
                                bdSubcttContractUnitPriceTotal.add(bdSubcttContractUnitPrice);
                        bdSubcttContractAmountTotal=
                                bdSubcttContractAmountTotal.add(bdSubcttBeginToCurrentPeriodAmount);

                        // 分包合同
                        qryCSStlQShowNewInsert.setStrSubctt_SignPartName(qryShowList.get(i).getStrName());
                        qryCSStlQShowNewInsert.setStrCstpl_Pkid(qryShowList.get(i).getStrCorrespondingPkid()
                                + "/" + intGroup.toString());
                        //qryCSStlQShowNewInsert.setStrCstpl_ParentPkid(itemUnit.getParentPkid());
                        qryCSStlQShowNewInsert.setBdSubctt_ContractUnitPrice(bdSubcttContractUnitPrice);

                        // 分包结算
                        qryCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodQQty(bdSubcttBeginToCurrentPeriodQuantity);
                        qryCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodMAmount(bdSubcttBeginToCurrentPeriodAmount);

                        if(intGroup>1){
                            qryCSStlQShowNewInsert.setStrCstpl_No("");
                            qryCSStlQShowNewInsert.setStrCstpl_Name("");
                            qryCSStlQShowNewInsert.setStrCstpl_Unit("");
                            qryCSStlQShowNewInsert.setBdCstpl_ContractUnitPrice(null);
                            qryCSStlQShowNewInsert.setBdTkcttStl_MeaQuantity(null);
                            qryCSStlQShowNewInsert.setBdTkcttStl_MeaAmount(null);
                        }

                        // 最后一项之前的项
                        if(i< qryShowList.size()-1){
                            // 下一项仍是目标分包合同项
                            if(itemUnit.getPkid().equals(qryShowList.get(i+1).getStrCorrespondingPkid())){
                                // 成本计划再设定
                                qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                            }// 下一项不是目标分包合同项
                            else{
                                // 总包计量与分包结算值差
                                qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlMeaQuantity).subtract(bdSubcttContractQuantityTotal));
                                if(intGroup==1){
                                    qryCSStlQShowNewInsert.setBdMeaS_ContractUnitPrice(
                                            ToolUtil.getBdIgnoreNull(bdCstplContractUnitPrice).subtract(bdSubcttContractUnitPriceTotal));
                                }
                                qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodMAmount(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlMeaAmount.subtract(bdSubcttContractAmountTotal)));
                                qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                                break;
                            }
                        }else{
                            // 总包计量与分包结算值差
                            qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                    bdTkcttStlMeaQuantity.subtract(bdSubcttContractQuantityTotal));
                            qryCSStlQShowNewInsert.setBdMeaS_ContractUnitPrice(
                                    bdCstplContractUnitPrice.subtract(bdSubcttContractUnitPriceTotal));
                            qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodMAmount(
                                    bdTkcttStlMeaAmount.subtract(bdSubcttContractAmountTotal));
                            qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    itemCstplInsertItem.setBdMeaS_BeginToCurrentPeriodQQty(
                            itemCstplInsertItem.getBdTkcttStl_MeaQuantity());
                    itemCstplInsertItem.setBdMeaS_ContractUnitPrice(
                            itemCstplInsertItem.getBdCstpl_ContractUnitPrice());
                    itemCstplInsertItem.setBdMeaS_BeginToCurrentPeriodMAmount(
                            itemCstplInsertItem.getBdTkcttStl_MeaAmount());
                    qryCSMeaSubQShowList.add(itemCstplInsertItem);
                }
            }

            qryCSMeaSubQShowListForExcel =new ArrayList<>();
            for(QryCSStlQShow itemUnit: qryCSMeaSubQShowList){
                QryCSStlQShow itemUnitTemp= (QryCSStlQShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrCstpl_No(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCstpl_No()));
                qryCSMeaSubQShowListForExcel.add(itemUnitTemp);
            }
        if(qryCSMeaSubQShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryCSMeaSubQShowListForExcel", qryCSMeaSubQShowListForExcel);

        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*递归排序*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<CttItemShow> cttItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= ToolUtil.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= ToolUtil.getUserName(itemUnit.getLastUpdBy());
            cttItemShowTemp = new CttItemShow(
                    itemUnit.getPkid(),
                    itemUnit.getBelongToType(),
                    itemUnit.getBelongToPkid(),
                    itemUnit.getParentPkid(),
                    itemUnit.getGrade(),
                    itemUnit.getOrderid(),
                    itemUnit.getName(),
                    itemUnit.getUnit(),
                    itemUnit.getContractUnitPrice(),
                    itemUnit.getContractQuantity(),
                    itemUnit.getContractAmount(),
                    itemUnit.getSignPartAPrice(),
                    itemUnit.getArchivedFlag() ,
                    itemUnit.getOriginFlag() ,
                    itemUnit.getCreatedBy() ,
                    strCreatedByName,
                    itemUnit.getCreatedTime() ,
                    itemUnit.getLastUpdBy() ,
                    strLastUpdByName,
                    itemUnit.getLastUpdTime() ,
                    itemUnit.getRecVersion(),
                    itemUnit.getRemark(),
                    itemUnit.getCorrespondingPkid(),
                    "",
                    ""
                );
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), cttItemListPara, cttItemShowListPara);
        }
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

    /*根据group和orderid临时编制编码strNo*/
    private List<CttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CttItemShow itemUnit: cttItemShowListPara){
            if(itemUnit.getGrade()==intBeforeGrade){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getOrderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getOrderid().toString();
                }
            }
            else{
                if(itemUnit.getGrade()==1){
                    strTemp=itemUnit.getOrderid().toString() ;
                }
                else {
                    if (!itemUnit.getGrade().equals(intBeforeGrade)) {
                        if (itemUnit.getGrade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getOrderid().toString();
                        } else {
                            Integer intTemp=ToolUtil.lookIndex(strTemp,'.',itemUnit.getGrade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getOrderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getGrade() ;
            itemUnit.setStrNo(ToolUtil.padLeft_DoLevel(itemUnit.getGrade(), strTemp)) ;
        }
        return cttItemShowListPara;
    }

    public void onQueryAction() {
        try {
            if(ToolUtil.getStrIgnoreNull(strCstplPkid).equals("")){
                MessageUtil.addWarn("请选择成本计划项。");
                return;
            }
            // StickyHeader对拼装列头平分宽度，不能设定宽度，兼容性不好，暂时停用
            initData(strCstplPkid);
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*智能字段Start*/

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public List<QryCSStlQShow> getQryCSMeaSubQShowListForExcel() {
        return qryCSMeaSubQShowListForExcel;
    }

    public void setQryCSMeaSubQShowListForExcel(List<QryCSStlQShow> qryCSMeaSubQShowListForExcel) {
        this.qryCSMeaSubQShowListForExcel = qryCSMeaSubQShowListForExcel;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public String getStrCstplPkid() {
        return strCstplPkid;
    }

    public void setStrCstplPkid(String strCstplPkid) {
        this.strCstplPkid = strCstplPkid;
    }

    public String getStrPeriodNo() {
        return strPeriodNo;
    }

    public void setStrPeriodNo(String strPeriodNo) {
        this.strPeriodNo = strPeriodNo;
    }

    public List<SelectItem> getCstplList() {
        return cstplList;
    }

    public void setCstplList(List<SelectItem> cstplList) {
        this.cstplList = cstplList;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    public List<QryCSStlQShow> getQryCSMeaSubQShowList() {
        return qryCSMeaSubQShowList;
    }

    public void setQryCSMeaSubQShowList(List<QryCSStlQShow> qryCSMeaSubQShowList) {
        this.qryCSMeaSubQShowList = qryCSMeaSubQShowList;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public ProgStlItemTkMeaService getProgStlItemTkMeaService() {
        return progStlItemTkMeaService;
    }

    public void setProgStlItemTkMeaService(ProgStlItemTkMeaService progStlItemTkMeaService) {
        this.progStlItemTkMeaService = progStlItemTkMeaService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }
/*智能字段End*/
}