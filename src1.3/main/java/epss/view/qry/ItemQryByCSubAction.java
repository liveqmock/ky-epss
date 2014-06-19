package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.enums.ESEnum;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.JxlsManager;
import epss.repository.model.CstplInfo;
import epss.repository.model.CstplItem;
import epss.repository.model.model_show.*;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.service.*;
import epss.view.common.EsCommon;
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
public class ItemQryByCSubAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemQryByCSubAction.class);
    @ManagedProperty(value = "#{cstplInfoService}")
    private CstplInfoService cstplInfoService;
    @ManagedProperty(value = "#{cstplItemService}")
    private CstplItemService cstplItemService;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{subcttItemService}")
    private SubcttItemService subcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;

    /*列表显示用*/
    private List<QryCSShow> qryCSShowList;
    private List<QryCSShow> qryCSShowListForExcel;
    private List<SelectItem> cstplList;
    private CstplInfo cstplInfoQry;

    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cstplInfoQry =new CstplInfo();
        CstplInfo cstplInfoTemp=new CstplInfo();
        cstplInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        List<CstplInfo> cstplInfoShowListTemp =
                cstplInfoService.getCstplInfoListByModel(cstplInfoTemp);
        cstplList=new ArrayList<SelectItem>();
        if(cstplInfoShowListTemp.size()>0){
            SelectItem selectItem=new SelectItem("","");
            cstplList.add(selectItem);
            for(CstplInfo itemUnit: cstplInfoShowListTemp){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                cstplList.add(selectItem);
            }
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
    private void initData(String strCstplInfoByPkidPara) {
        CstplInfo cstplInfoTemp = cstplInfoService.getCstplInfoByPkid(strCstplInfoByPkidPara);
        beansMap.put("cstplInfoTemp", cstplInfoTemp);
        /*成本计划列表*/
        List<CstplItem> cstplItemList =new ArrayList<CstplItem>();
        cstplItemList = cstplItemService.getCstplListByCstplInfoPkid(cstplInfoTemp.getPkid());
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<>();
        recursiveDataTable("root", cstplItemList, cstplItemShowListTemp);
        cstplItemShowListTemp =getCstplItemShowList_DoFromatNo(cstplItemShowListTemp);

        List<QryShow> qryShowList =esQueryService.getCSList(strCstplInfoByPkidPara);

        /*拼装列表*/
        try {
            qryCSShowList =new ArrayList<QryCSShow>();
            QryCSShow qryCSShowTemp;
            BigDecimal bdCstplQty=null;
            BigDecimal bdCstplUnitPrice=null;
            BigDecimal bdCstplAmt=null;
            for(CstplItemShow itemUnit: cstplItemShowListTemp){
                bdCstplQty=itemUnit.getTkctt_Qty();
                bdCstplUnitPrice=itemUnit.getTkctt_UnitPrice();
                bdCstplAmt=itemUnit.getTkctt_Amt();
                qryCSShowTemp=new QryCSShow();
                qryCSShowTemp.setStrCstpl_Pkid(itemUnit.getCstpl_Pkid());
                qryCSShowTemp.setStrCstpl_ParentPkid(itemUnit.getCstpl_ParentPkid());
                qryCSShowTemp.setStrCstpl_No(itemUnit.getCstpl_StrNo());
                qryCSShowTemp.setStrCstpl_Name(itemUnit.getCstpl_Name());
                qryCSShowTemp.setStrCstpl_Unit(itemUnit.getCstpl_Unit());
                qryCSShowTemp.setBdCstpl_Qty(bdCstplQty);
                qryCSShowTemp.setBdCstpl_UnitPrice(bdCstplUnitPrice);
                qryCSShowTemp.setBdCstpl_Amt(bdCstplAmt);
                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttQtyTotal=new BigDecimal(0);
                BigDecimal bdSubcttUnitPriceTotal=new BigDecimal(0);
                BigDecimal bdSubcttAmtTotal=new BigDecimal(0);
                // 根据成本计划项插接分包合同项
                for(int i=0;i< qryShowList.size();i++) {
                    // 成本计划项遇到目标分包合同项
                    if(itemUnit.getCstpl_Pkid().equals(qryShowList.get(i).getStrCorrespondingPkid())) {
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // 克隆目标进行处理后插接
                        QryCSShow qryCSShowNewInsert =(QryCSShow)BeanUtils.cloneBean(qryCSShowTemp);
                        // 目标分包合同项的合同数量，合同单价，合同金额
                        BigDecimal bdSubcttQty=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdQuantity());
                        BigDecimal bdSubcttUnitPrice=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdUnitPrice());
                        BigDecimal bdSubcttAmt=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdAmount());
                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttQtyTotal=bdSubcttQtyTotal.add(bdSubcttQty);
                        bdSubcttUnitPriceTotal=bdSubcttUnitPriceTotal.add(bdSubcttUnitPrice);
                        bdSubcttAmtTotal=bdSubcttAmtTotal.add(bdSubcttAmt);

                        // 分包合同
                        qryCSShowNewInsert.setBdSubctt_Qty(bdSubcttQty);
                        qryCSShowNewInsert.setBdSubctt_UnitPrice(bdSubcttUnitPrice);
                        qryCSShowNewInsert.setBdSubctt_Amt(bdSubcttAmt);
                        qryCSShowNewInsert.setStrSubctt_SignPartName(qryShowList.get(i).getStrName());
                        qryCSShowNewInsert.setStrCstpl_Pkid(qryShowList.get(i).getStrCorrespondingPkid() + "/" + intGroup.toString());
                        qryCSShowNewInsert.setStrCstpl_ParentPkid(itemUnit.getCstpl_ParentPkid());

                        if(intGroup>1){
                            qryCSShowNewInsert.setStrCstpl_No("");
                            qryCSShowNewInsert.setStrCstpl_Name("");
                            qryCSShowNewInsert.setStrCstpl_Unit("");
                            qryCSShowNewInsert.setBdCstpl_Qty(null);
                            qryCSShowNewInsert.setBdCstpl_UnitPrice(null);
                            qryCSShowNewInsert.setBdCstpl_Amt(null);
                        }

                        // 最后一项之前的项
                        if(i< qryShowList.size()-1){
                            // 下一项仍是目标分包合同项
                            if(itemUnit.getCstpl_Pkid().equals(qryShowList.get(i+1).getStrCorrespondingPkid())){
                                // 成本计划再设定
                                qryCSShowList.add(qryCSShowNewInsert);
                            }// 下一项不是目标分包合同项
                            else{
                                // 成分值差
                                if(bdCstplQty!=null){
                                    qryCSShowNewInsert.setBdC_S_Qty(
                                            bdCstplQty.subtract(bdSubcttQtyTotal));
                                }

                                if(intGroup==1){
                                    qryCSShowNewInsert.setBdC_S_UnitPrice(
                                            ToolUtil.getBdIgnoreNull(bdCstplUnitPrice).subtract(bdSubcttUnitPriceTotal));
                                }
                                if(bdCstplAmt!=null) {
                                qryCSShowNewInsert.setBdC_S_Amt(
                                        ToolUtil.getBdIgnoreNull(bdCstplAmt.subtract(bdSubcttAmtTotal)));
                                }
                                qryCSShowList.add(qryCSShowNewInsert);
                                break;
                            }
                        }else{
                            // 成分值差
                            if(bdCstplQty!=null){
                                qryCSShowNewInsert.setBdC_S_Qty(bdCstplQty.subtract(bdSubcttQtyTotal));
                            }
                            qryCSShowNewInsert.setBdC_S_UnitPrice(bdCstplUnitPrice.subtract(bdSubcttUnitPriceTotal));
                            if(bdCstplAmt!=null) {
                                qryCSShowNewInsert.setBdC_S_Amt(bdCstplAmt.subtract(bdSubcttAmtTotal));
                            }
                            qryCSShowList.add(qryCSShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryCSShowList.add(qryCSShowTemp);
                }
            }

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
        // 添加合计
        setItemOfCSForQueryList_AddTotal();

        if(qryCSShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }

        beansMap.put("qryCSShowList", qryCSShowList);
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
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getBdCstpl_Amt()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getBdCstpl_Amt()));
            // 对照
           /* bdTotalContrast=bdTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmtContrast()));
            bdAllTotalContrast=bdAllTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmtContrast()));*/
            qryCSShowList.add(itemUnit);

            if(i+1< qryCSShowListTemp.size()){
                itemUnitNext = qryCSShowListTemp.get(i+1);
                QryCSShow qryCSShowTemp =new QryCSShow();
                Boolean isRoot=false;
                if(itemUnitNext.getStrCstpl_ParentPkid()!=null&&itemUnitNext.getStrCstpl_ParentPkid().equals("root")){
                    qryCSShowTemp.setStrCstpl_Name("合计");
                    qryCSShowTemp.setStrCstpl_Pkid("total"+i);
                    qryCSShowTemp.setBdCstpl_Amt(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

               /* if(itemUnitNext.getParentPkidContrast()!=null && itemUnitNext.getParentPkidContrast().equals("root")){
                    qryCSShowTemp.setStrPkid("total"+i);
                    qryCSShowTemp.setStrNameContrast("合计");
                    qryCSShowTemp.setPkidContrast("total_contrast"+i);
                    qryCSShowTemp.setAmtContrast(bdTotalContrast);
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
                qryCSShowTemp.setBdCstpl_Amt(bdTotal);

               /* qryCSShowTemp.setNameContrast("合计");
                qryCSShowTemp.setPkidContrast("total_contrast"+i);
                qryCSShowTemp.setAmtContrast(bdTotalContrast);*/
                qryCSShowList.add(qryCSShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalContrast = new BigDecimal(0);

                // 总合计
                qryCSShowTemp =new QryCSShow();
                qryCSShowTemp.setStrCstpl_Name("总合计");
                qryCSShowTemp.setStrCstpl_Pkid("total_all" + i);
                qryCSShowTemp.setBdCstpl_Amt(bdAllTotal);

              /*  qryCSShowTemp.setNameContrast("总合计");
                qryCSShowTemp.setPkidContrast("total_all_contrast"+i);
                qryCSShowTemp.setAmtContrast(bdAllTotalContrast);*/
                qryCSShowList.add(qryCSShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalContrast = new BigDecimal(0);
            }
        }
    }

    /*递归排序*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CstplItem> cstplItemListPara,
                                      List<CstplItemShow> cstplItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CstplItem> subCstplItemList =new ArrayList<CstplItem>();
        // 通过父层id查找它的孩子
        subCstplItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, cstplItemListPara);
        for(CstplItem itemUnit: subCstplItemList){
            CstplItemShow cstplItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
                // 层级项
            cstplItemShowTemp = new CstplItemShow(
                    itemUnit.getPkid(),
                    itemUnit.getCstplInfoPkid(),
                    itemUnit.getParentPkid(),
                    "",
                    itemUnit.getGrade(),
                    itemUnit.getOrderid(),
                    itemUnit.getName(),
                    itemUnit.getUnit(),
                    itemUnit.getUnitPrice(),
                    itemUnit.getQty(),
                    itemUnit.getAmt(),
                    itemUnit.getArchivedFlag(),
                    itemUnit.getOriginFlag(),
                    itemUnit.getCreatedBy(),
                    strCreatedByName,
                    itemUnit.getCreatedTime(),
                    itemUnit.getUpdatedBy(),
                    strLastUpdByName,
                    itemUnit.getUpdatedTime(),
                    itemUnit.getRecVersion(),
                    itemUnit.getRemark(),
                    itemUnit.getTkcttItemPkid(),
                    "",
                    itemUnit.getTid()
                );
            cstplItemShowListPara.add(cstplItemShowTemp) ;
            recursiveDataTable(cstplItemShowTemp.getCstpl_Pkid(),cstplItemListPara, cstplItemShowListPara);
        }
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CstplItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
             List<CstplItem> cstplItemListPara) {
        List<CstplItem> tempCstplItemList =new ArrayList<CstplItem>();
        /*避开重复链接数据库*/
        for(CstplItem itemUnit: cstplItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempCstplItemList.add(itemUnit);
            }
        }
        return tempCstplItemList;
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<CstplItemShow> getCstplItemShowList_DoFromatNo(List<CstplItemShow> cstplItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CstplItemShow itemUnit: cstplItemShowListPara){
            if(itemUnit.getCstpl_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getCstpl_Orderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getCstpl_Orderid().toString();
                }
            }
            else{
                if(itemUnit.getCstpl_Grade()==1){
                    strTemp=itemUnit.getCstpl_Orderid().toString() ;
                }
                else {
                    if (!itemUnit.getCstpl_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getCstpl_Grade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getCstpl_Orderid().toString();
                        } else {
                            Integer intTemp=ToolUtil.lookIndex(strTemp,'.',itemUnit.getCstpl_Grade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getCstpl_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getCstpl_Grade() ;
            itemUnit.setCstpl_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getCstpl_Grade(), strTemp)) ;
        }
        return cstplItemShowListPara;
    }

    public void onQueryAction() {
        try {
            if(ToolUtil.getStrIgnoreNull(cstplInfoQry.getPkid()).equals("")){
                MessageUtil.addWarn("请选择成本计划项。");
                return;
            }
            initData(cstplInfoQry.getPkid());
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*智能字段Start*/
    public SubcttItemService getSubcttItemService() {
        return subcttItemService;
    }

    public void setSubcttItemService(SubcttItemService subcttItemService) {
        this.subcttItemService = subcttItemService;
    }

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
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

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
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

    public CstplInfoService getCstplInfoService() {
        return cstplInfoService;
    }

    public void setCstplInfoService(CstplInfoService cstplInfoService) {
        this.cstplInfoService = cstplInfoService;
    }

    public CstplItemService getCstplItemService() {
        return cstplItemService;
    }

    public void setCstplItemService(CstplItemService cstplItemService) {
        this.cstplItemService = cstplItemService;
    }

    public CstplInfo getCstplInfoQry() {
        return cstplInfoQry;
    }

    public void setCstplInfoQry(CstplInfo cstplInfoQry) {
        this.cstplInfoQry = cstplInfoQry;
    }

    /*智能字段End*/
}