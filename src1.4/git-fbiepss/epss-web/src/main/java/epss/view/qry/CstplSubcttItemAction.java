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
import skyline.util.JxlsManager;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.CttInfo;
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
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class CstplSubcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplSubcttItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    /*列表显示用*/
    private List<QryCSShow> qryCSShowList;
    private List<QryCSShow> qryCSShowListForExcel;

    private List<SelectItem> cstplList;

    private CttItem cttItem;

    // 画面上控件的显示控制
    private ReportHeader reportHeader;
    private String strExportToExcelRendered;
    private Map beansMap;
    @PostConstruct
    public void init() {
        try {
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
            cttItem =new CttItem();
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.qryCSShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "成分比较-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCS.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }
    private void initData(String strBelongToPkid) {
        CttInfo cttInfo = cttInfoService.getCttInfoByPkId(strBelongToPkid);
        reportHeader.setStrCstplId(cttInfo.getId());
        reportHeader.setStrCstplName(cttInfo.getName());
        beansMap.put("reportHeader", reportHeader);
        /*成本计划列表*/
        List<CttItem> cttItemListCstpl =new ArrayList<CttItem>();
        cttItemListCstpl = cttItemService.getEsItemList(
                EnumResType.RES_TYPE1.getCode(),
                cttItem.getBelongToPkid());
        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", cttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListCstpl);

        List<QryShow> qryShowList =esQueryService.getCSList("2",strBelongToPkid);

        /*拼装列表*/
        try {
            qryCSShowList =new ArrayList<QryCSShow>();
            QryCSShow itemCstplInsertItem;
            BigDecimal bdCstplContractQuantity=null;
            BigDecimal bdCstplContractUnitPrice=null;
            BigDecimal bdCstplContractAmount=null;
            for(CttItemShow itemUnit: cttItemShowListCstpl){
                bdCstplContractQuantity=itemUnit.getContractQuantity();
                bdCstplContractUnitPrice=itemUnit.getContractUnitPrice();
                bdCstplContractAmount=itemUnit.getContractAmount();
                itemCstplInsertItem=new QryCSShow();
                itemCstplInsertItem.setStrCstpl_Pkid(itemUnit.getPkid());
                itemCstplInsertItem.setStrCstpl_ParentPkid(itemUnit.getParentPkid());
                itemCstplInsertItem.setStrCstpl_No(itemUnit.getStrNo());
                itemCstplInsertItem.setStrCstpl_Name(itemUnit.getName());
                itemCstplInsertItem.setStrCstpl_Unit(itemUnit.getUnit());
                itemCstplInsertItem.setBdCstpl_ContractQuantity(bdCstplContractQuantity);
                itemCstplInsertItem.setBdCstpl_ContractUnitPrice(bdCstplContractUnitPrice);
                itemCstplInsertItem.setBdCstpl_ContractAmount(bdCstplContractAmount);
                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttContractQuantityTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractUnitPriceTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractAmountTotal=new BigDecimal(0);
                // 根据成本计划项插接分包合同项
                for(int i=0;i< qryShowList.size();i++) {
                    // 成本计划项遇到目标分包合同项
                    QryShow qryShowTemp=qryShowList.get(i);
                    if(itemUnit.getPkid().equals(qryShowTemp.getStrCorrespondingPkid())) {
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // 克隆目标进行处理后插接
                        QryCSShow qryCSShowNewInsert =(QryCSShow)BeanUtils.cloneBean(itemCstplInsertItem);
                        // 目标分包合同项的合同数量，合同单价，合同金额
                        BigDecimal bdSubcttContractQuantity=ToolUtil.getBdIgnoreNull(qryShowTemp.getBdQuantity());
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(qryShowTemp.getBdUnitPrice());
                        BigDecimal bdSubcttContractAmount=ToolUtil.getBdIgnoreNull(qryShowTemp.getBdAmount());
                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttContractQuantity);
                        bdSubcttContractUnitPriceTotal=bdSubcttContractUnitPriceTotal.add(bdSubcttContractUnitPrice);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttContractAmount);

                        // 分包合同
                        qryCSShowNewInsert.setBdSubctt_ContractQuantity(bdSubcttContractQuantity);
                        qryCSShowNewInsert.setBdSubctt_ContractUnitPrice(bdSubcttContractUnitPrice);
                        qryCSShowNewInsert.setBdSubctt_ContractAmount(bdSubcttContractAmount);
                        qryCSShowNewInsert.setStrSubctt_Name(qryShowTemp.getStrName());
                        qryCSShowNewInsert.setStrSubctt_SignPartName(qryShowTemp.getStrSignPartName());
                        qryCSShowNewInsert.setStrCstpl_Pkid(qryShowTemp.getStrCorrespondingPkid() + "/" + intGroup.toString());
                        qryCSShowNewInsert.setStrCstpl_ParentPkid(itemUnit.getParentPkid());

                        if(intGroup>1){
                            qryCSShowNewInsert.setStrCstpl_No("");
                            qryCSShowNewInsert.setStrCstpl_Name("");
                            qryCSShowNewInsert.setStrCstpl_Unit("");
                            qryCSShowNewInsert.setBdCstpl_ContractQuantity(null);
                            qryCSShowNewInsert.setBdCstpl_ContractUnitPrice(null);
                            qryCSShowNewInsert.setBdCstpl_ContractAmount(null);
                        }

                        // 最后一项之前的项
                        if(i< qryShowList.size()-1){
                            // 下一项仍是目标分包合同项
                            if(itemUnit.getPkid().equals(qryShowList.get(i+1).getStrCorrespondingPkid())){
                                // 成本计划再设定
                                qryCSShowList.add(qryCSShowNewInsert);
                            }// 下一项不是目标分包合同项
                            else{
                                // 成分值差
                                qryCSShowNewInsert.setBdC_S_ContractQuantity(
                                        ToolUtil.getBdIgnoreNull(bdCstplContractQuantity).subtract(bdSubcttContractQuantityTotal));
                                if(intGroup==1){
                                    qryCSShowNewInsert.setBdC_S_ContractUnitPrice(
                                            ToolUtil.getBdIgnoreNull(bdCstplContractUnitPrice).subtract(bdSubcttContractUnitPriceTotal));
                                }
                                if(bdCstplContractAmount!=null) {
                                qryCSShowNewInsert.setBdC_S_ContractAmount(
                                        ToolUtil.getBdIgnoreNull(bdCstplContractAmount.subtract(bdSubcttContractAmountTotal)));
                                }
                                qryCSShowList.add(qryCSShowNewInsert);
                                break;
                            }
                        }else{
                            // 成分值差
                            qryCSShowNewInsert.setBdC_S_ContractQuantity(bdCstplContractQuantity.subtract(bdSubcttContractQuantityTotal));
                            if(intGroup==1) {
                                qryCSShowNewInsert.setBdC_S_ContractUnitPrice(bdCstplContractUnitPrice.subtract(bdSubcttContractUnitPriceTotal));
                            }
                            if(bdCstplContractAmount!=null) {
                                qryCSShowNewInsert.setBdC_S_ContractAmount(bdCstplContractAmount.subtract(bdSubcttContractAmountTotal));
                            }
                            qryCSShowList.add(qryCSShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryCSShowList.add(itemCstplInsertItem);
                }
            }

            // 添加合计
            setItemOfCSForQueryList_AddTotal();

            if(qryCSShowList.size()>0){
                strExportToExcelRendered="true";
            }else{
                strExportToExcelRendered="false";
            }

            beansMap.put("qryCSShowList", qryCSShowList);

            qryCSShowListForExcel =new ArrayList<QryCSShow>();
            for(QryCSShow itemUnit: qryCSShowList){
                QryCSShow itemUnitTemp= (QryCSShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrCstpl_No(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCstpl_No()));
                qryCSShowListForExcel.add(itemUnitTemp);
            }
            beansMap.put("qryCSShowListForExcel", qryCSShowListForExcel);
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }
    private void setItemOfCSForQueryList_AddTotal(){
        List<QryCSShow> qryCSShowListTemp =new ArrayList<QryCSShow>();
        qryCSShowListTemp.addAll(qryCSShowList);

        qryCSShowList.clear();
        // 小计
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        BigDecimal bdTotalContrast=new BigDecimal(0);
        BigDecimal bdAllTotalContrast=new BigDecimal(0);

        QryCSShow itemUnit=new QryCSShow();
        QryCSShow itemUnitNext=new QryCSShow();
        for(int i=0;i< qryCSShowListTemp.size();i++){
            itemUnit = qryCSShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getBdCstpl_ContractAmount()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getBdCstpl_ContractAmount()));
            // 对照
           /* bdTotalContrast=bdTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));
            bdAllTotalContrast=bdAllTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));*/
            qryCSShowList.add(itemUnit);

            if(i+1< qryCSShowListTemp.size()){
                itemUnitNext = qryCSShowListTemp.get(i+1);
                QryCSShow qryCSShowTemp =new QryCSShow();
                Boolean isRoot=false;
                if(!("".equals(itemUnitNext.getStrCstpl_No()))&&itemUnitNext.getStrCstpl_ParentPkid().equals("root")){
                    qryCSShowTemp.setStrCstpl_Name("合计");
                    qryCSShowTemp.setStrCstpl_Pkid("total"+i);
                    qryCSShowTemp.setBdCstpl_ContractAmount(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

               /* if(itemUnitNext.getParentPkidContrast()!=null && itemUnitNext.getParentPkidContrast().equals("root")){
                    qryCSShowTemp.setStrPkid("total"+i);
                    qryCSShowTemp.setStrNameContrast("合计");
                    qryCSShowTemp.setPkidContrast("total_contrast"+i);
                    qryCSShowTemp.setContractAmountContrast(bdTotalContrast);
                    bdTotalContrast = new BigDecimal(0);
                    isRoot=true;
                }*/

                if(isRoot.equals(true)){
                    qryCSShowList.add(qryCSShowTemp);
                }

            } else if(i+1== qryCSShowListTemp.size()){
                itemUnitNext = qryCSShowListTemp.get(i);
                QryCSShow qryCSShowTemp =new QryCSShow();
                qryCSShowTemp.setStrCstpl_Name("合计");
                qryCSShowTemp.setStrCstpl_Pkid("total" + i);
                qryCSShowTemp.setBdCstpl_ContractAmount(bdTotal);

               /* qryCSShowTemp.setNameContrast("合计");
                qryCSShowTemp.setPkidContrast("total_contrast"+i);
                qryCSShowTemp.setContractAmountContrast(bdTotalContrast);*/
                qryCSShowList.add(qryCSShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalContrast = new BigDecimal(0);

                // 总合计
                qryCSShowTemp =new QryCSShow();
                qryCSShowTemp.setStrCstpl_Name("总合计");
                qryCSShowTemp.setStrCstpl_Pkid("total_all" + i);
                qryCSShowTemp.setBdCstpl_ContractAmount(bdAllTotal);

              /*  qryCSShowTemp.setNameContrast("总合计");
                qryCSShowTemp.setPkidContrast("total_all_contrast"+i);
                qryCSShowTemp.setContractAmountContrast(bdAllTotalContrast);*/
                qryCSShowList.add(qryCSShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalContrast = new BigDecimal(0);
            }
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
                // 层级项
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
            if(ToolUtil.getStrIgnoreNull(cttItem.getBelongToPkid()).equals("")){
                MessageUtil.addWarn("请选择成本计划项。");
                return;
            }
            initData(cttItem.getBelongToPkid());
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*智能字段Start*/
    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
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

    public List<QryCSShow> getQryCSShowList() {
        return qryCSShowList;
    }

    public void setQryCSShowList(List<QryCSShow> qryCSShowList) {
        this.qryCSShowList = qryCSShowList;
    }

    public List<QryCSShow> getQryCSShowListForExcel() {
        return qryCSShowListForExcel;
    }

    public void setQryCSShowListForExcel(List<QryCSShow> qryCSShowListForExcel) {
        this.qryCSShowListForExcel = qryCSShowListForExcel;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public CttItem getCttItem() {
        return cttItem;
    }

    public void setCttItem(CttItem cttItem) {
        this.cttItem = cttItem;
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

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    /*智能字段End*/
}