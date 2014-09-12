package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.JxlsManager;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.EsItemStlTkcttEngMea;
import epss.service.*;
import epss.service.EsFlowService;
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
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;

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
                        ESEnum.ITEMTYPE1.getCode()
                       ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
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
        List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList=new ArrayList<EsItemStlTkcttEngMea>();

        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        EsCttInfo esInitCttCstpl= cttInfoService.getCttInfoByPkId(strBelongToPkid);
        reportHeader.setStrCstplId(esInitCttCstpl.getId());
        reportHeader.setStrCstplName(esInitCttCstpl.getName());
        beansMap.put("reportHeader", reportHeader);
        /*成本计划列表*/
        List<EsCttItem> esCttItemListCstpl =new ArrayList<EsCttItem>();
        esCttItemListCstpl = cttItemService.getEsItemList(
                ESEnum.ITEMTYPE1.getCode(),
                strCstplPkid);
        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", esCttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListCstpl);

        String strMeaLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNoByEndPeriod(
                        ESEnum.ITEMTYPE7.getCode(),
                        esInitCttCstpl.getParentPkid(),
                        strPeriodNo));

        if(!ToolUtil.getStrIgnoreNull(strMeaLatestApprovedPeriodNo).equals("")){
            EsItemStlTkcttEngMea esItemStlTkcttEngMea=new EsItemStlTkcttEngMea();
            esItemStlTkcttEngMea.setTkcttPkid(esInitCttCstpl.getParentPkid());
            esItemStlTkcttEngMea.setPeriodNo(strMeaLatestApprovedPeriodNo);
            esItemStlTkcttEngMeaList= progMeaItemService.selectRecordsByPkidPeriodNoExample(esItemStlTkcttEngMea);
        }

        List<QryShow> qryShowList =esQueryService.getCSStlQList(strBelongToPkid, strPeriodNo);

        /*拼装列表*/
        try {
            qryCSMeaSubQShowList =new ArrayList<QryCSStlQShow>();
            QryCSStlQShow itemCstplInsertItem;

            BigDecimal bdCstplContractUnitPrice=new BigDecimal(0);
            BigDecimal bdTkcttStlMeaQuantity=new BigDecimal(0);
            BigDecimal bdTkcttStlMeaAmount=new BigDecimal(0);
            BigDecimal bdCstplContractAmount=new BigDecimal(0);

            for(CttItemShow itemUnit: cttItemShowListCstpl){
                if(itemUnit.getUnit()!=null){
                    bdCstplContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
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
                for(EsItemStlTkcttEngMea esItemStlTkcttEngMea:esItemStlTkcttEngMeaList){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getStrCorrespondingItemPkid()).equals(
                            esItemStlTkcttEngMea.getTkcttItemPkid())){
                        itemCstplInsertItem.setBdTkcttStl_MeaQuantity(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        bdTkcttStlMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
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
                        BigDecimal bdSubcttBeginToCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdUnitPrice());
                        BigDecimal bdSubcttBeginToCurrentPeriodAmount=bdSubcttBeginToCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);
                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttBeginToCurrentPeriodQuantity);
                        bdSubcttContractUnitPriceTotal=bdSubcttContractUnitPriceTotal.add(bdSubcttContractUnitPrice);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttBeginToCurrentPeriodAmount);

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
                            qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(bdTkcttStlMeaQuantity.subtract(bdSubcttContractQuantityTotal));
                            qryCSStlQShowNewInsert.setBdMeaS_ContractUnitPrice(bdCstplContractUnitPrice.subtract(bdSubcttContractUnitPriceTotal));
                            qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodMAmount(bdCstplContractAmount.subtract(bdSubcttContractAmountTotal));
                            qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryCSMeaSubQShowList.add(itemCstplInsertItem);
                }
            }
            qryCSMeaSubQShowListForExcel =new ArrayList<QryCSStlQShow>();
            for(QryCSStlQShow itemUnit: qryCSMeaSubQShowList){
                QryCSStlQShow itemUnitTemp= (QryCSStlQShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrCstpl_No(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCstpl_No()));
                qryCSMeaSubQShowListForExcel.add(itemUnitTemp);
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }

        if(qryCSMeaSubQShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryCSMeaSubQShowListForExcel", qryCSMeaSubQShowListForExcel);
    }

    /*递归排序*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<CttItemShow> cttItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        EsCttItem esCttItem =null;
        for(EsCttItem itemUnit: subEsCttItemList){
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
                    itemUnit.getDeletedFlag() ,
                    itemUnit.getOriginFlag() ,
                    itemUnit.getCreatedBy() ,
                    strCreatedByName,
                    itemUnit.getCreatedDate() ,
                    itemUnit.getLastUpdBy() ,
                    strLastUpdByName,
                    itemUnit.getLastUpdDate() ,
                    itemUnit.getModificationNum(),
                    itemUnit.getNote(),
                    itemUnit.getCorrespondingPkid(),
                    "",
                    ""
                );
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara, cttItemShowListPara);
        }
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

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService progMeaItemService) {
        this.progMeaItemService = progMeaItemService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    /*智能字段End*/
}