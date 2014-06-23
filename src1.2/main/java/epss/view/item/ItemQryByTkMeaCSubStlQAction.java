package epss.view.item;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.JxlsManager;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.*;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsItemStlTkcttEngMea;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.service.common.EsQueryService;
import epss.view.common.EsCommon;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
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
public class ItemQryByTkMeaCSubStlQAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemQryByTkMeaCSubStlQAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{esItemStlTkcttEngMeaService}")
    private EsItemStlTkcttEngMeaService esItemStlTkcttEngMeaService;

    /*列表显示用*/
    private List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowList;
    private List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListForExcel;

    private List<SelectItem> tkcttList;

    private String strTkcttPkid;
    private String strPeriodNo;

    // 画面上控件的显示控制
    private CommStlSubcttEngH commStlSubcttEngH;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        // 获取已经批准了的总包合同列表
        List<CttInfoShow> cttInfoShowList =
                esCttInfoService.getCttInfoListByCttType_Status(
                        ESEnum.ITEMTYPE0.getCode()
                       ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
        tkcttList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","");
            tkcttList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                tkcttList.add(selectItem);
            }
        }
        strPeriodNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.qryTkMeaCSStlQShowListForExcel.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "总包计量成本计划分包结算数量比较-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryTkMeaCSStlQ.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }
    private void initData(String strTkcttInfoPkid) {
        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        // 1。总包合同信息
        // 1。1。取出总包合同信息
        EsCttInfo esCttInfoTkctt= esCttInfoService.getCttInfoByPkId(strTkcttInfoPkid);
        commStlSubcttEngH.setStrTkcttId(esCttInfoTkctt.getId());
        commStlSubcttEngH.setStrTkcttName(esCttInfoTkctt.getName());
        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
        // 1。2。抽取相应总包合同的详细内容
        List<EsCttItem> esCttItemOfTkcttList =new ArrayList<EsCttItem>();
        esCttItemOfTkcttList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(),
                strTkcttPkid);
        // 根据总包合同内容的信息，拼成合同原稿
        List<CttItemShow> tkcttItemShowList =new ArrayList<>();
        recursiveDataTable("root", esCttItemOfTkcttList, tkcttItemShowList);
        tkcttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(tkcttItemShowList);

        // 2。成本计划信息
        List<EsCttInfo> esCstplInfoList=esCttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                ESEnum.ITEMTYPE1.getCode(),esCttInfoTkctt.getPkid());
        if(esCstplInfoList.size()==0){
            return;
        }
        EsCttInfo esCstplInfo =esCstplInfoList.get(0);
        List<CttItemShow> cstplItemListTemp=
                esQueryService.getEsCstplItemList(strTkcttInfoPkid);

        // 3。总包合同最近批准了的总包计量数据
        // 小于等于所选期码的最近已经批准了的计量期码
        String strMeaLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNoByEndPeriod(
                        ESEnum.ITEMTYPE7.getCode(),strTkcttInfoPkid,strPeriodNo));
        List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList=new ArrayList<EsItemStlTkcttEngMea>();
        if(!ToolUtil.getStrIgnoreNull(strMeaLatestApprovedPeriodNo).equals("")){
            EsItemStlTkcttEngMea esItemStlTkcttEngMea=new EsItemStlTkcttEngMea();
            esItemStlTkcttEngMea.setTkcttPkid(strTkcttInfoPkid);
            esItemStlTkcttEngMea.setPeriodNo(strMeaLatestApprovedPeriodNo);
            esItemStlTkcttEngMeaList=esItemStlTkcttEngMeaService.selectRecordsByPkidPeriodNoExample(esItemStlTkcttEngMea);
        }

        // 4。分包结算
        List<QryShow> subcttStlQBySignPartList=esQueryService.getCSStlQBySignPartList(esCstplInfo.getPkid(), strPeriodNo);

        /*拼装列表*/
        try {
            qryTkMeaCSStlQShowList =new ArrayList<QryTkMeaCSStlQShow>();
            for(CttItemShow cttItemShowTkctt : tkcttItemShowList){
                QryTkMeaCSStlQShow qryTkMeaCSStlQShowNew =new QryTkMeaCSStlQShow();
                // 总包合同单价
                BigDecimal bdTkcttContractUnitPrice=new BigDecimal(0);
                // 当期计量
                BigDecimal bdTkcttStlCMeaQuantity=new BigDecimal(0);
                BigDecimal bdTkcttStlCMeaAmount=new BigDecimal(0);
                // 开累计量
                BigDecimal bdTkcttStlBToCMeaQuantity=new BigDecimal(0);
                BigDecimal bdTkcttStlBToCMeaAmount=new BigDecimal(0);
                // 总包合同
                qryTkMeaCSStlQShowNew.setStrTkctt_Pkid(cttItemShowTkctt.getPkid());
                qryTkMeaCSStlQShowNew.setStrTkctt_ParentPkid(cttItemShowTkctt.getParentPkid());
                qryTkMeaCSStlQShowNew.setStrTkctt_No(cttItemShowTkctt.getStrNo());
                qryTkMeaCSStlQShowNew.setStrTkctt_Name(cttItemShowTkctt.getName());
                qryTkMeaCSStlQShowNew.setStrTkctt_Unit(cttItemShowTkctt.getUnit());
                qryTkMeaCSStlQShowNew.setBdTkctt_ContractUnitPrice(cttItemShowTkctt.getContractUnitPrice());
                qryTkMeaCSStlQShowNew.setBdTkctt_ContractQuantity(cttItemShowTkctt.getContractQuantity());
                if(cttItemShowTkctt.getContractUnitPrice()!=null&&
                        cttItemShowTkctt.getContractQuantity()!=null) {
                    qryTkMeaCSStlQShowNew.setBdTkctt_ContractAmount(
                            cttItemShowTkctt.getContractUnitPrice().multiply(cttItemShowTkctt.getContractQuantity()));
                }
                // 计量
                for(EsItemStlTkcttEngMea esItemStlTkcttEngMea:esItemStlTkcttEngMeaList){
                    if(ToolUtil.getStrIgnoreNull(cttItemShowTkctt.getPkid()).equals(esItemStlTkcttEngMea.getTkcttItemPkid())){
                        bdTkcttContractUnitPrice=ToolUtil.getBdIgnoreNull(cttItemShowTkctt.getContractUnitPrice());
                        bdTkcttStlCMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getCurrentPeriodQty());
                        bdTkcttStlCMeaAmount=bdTkcttStlCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        bdTkcttStlBToCMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        bdTkcttStlBToCMeaAmount=bdTkcttStlBToCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        // 当期计量数量和金额
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_CurrentPeriodMeaQuantity(bdTkcttStlCMeaQuantity);
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_CurrentPeriodMeaAmount(bdTkcttStlCMeaAmount);
                        // 开累计量数量和金额
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_BeginToCurrentPeriodMeaQuantity(bdTkcttStlBToCMeaQuantity);
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_BeginToCurrentPeriodMeaAmount(bdTkcttStlBToCMeaAmount);
                        break;
                    }
                }

                // 成本计划
                String strItemOfEsItemHieRelapTkctt_Pkid=ToolUtil.getStrIgnoreNull(qryTkMeaCSStlQShowNew.getStrTkctt_Pkid());
                String strItemOfEsItemHieRelapCstpl_CorrespondingPkid;
                String strItemOfEsItemHieRelapCstpl_Pkid="";
                for(CttItemShow cstplItemShowUnit : cstplItemListTemp){
                    strItemOfEsItemHieRelapCstpl_CorrespondingPkid=ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getCorrespondingPkid());
                    if(strItemOfEsItemHieRelapTkctt_Pkid.equals(strItemOfEsItemHieRelapCstpl_CorrespondingPkid)) {
                        strItemOfEsItemHieRelapCstpl_Pkid=ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getPkid());
                        qryTkMeaCSStlQShowNew.setStrCstpl_Pkid(strItemOfEsItemHieRelapCstpl_Pkid);
                        qryTkMeaCSStlQShowNew.setBdCstpl_ContractUnitPrice(cstplItemShowUnit.getContractUnitPrice());
                        qryTkMeaCSStlQShowNew.setBdCstpl_ContractQuantity(cstplItemShowUnit.getContractQuantity());
                        if(cstplItemShowUnit.getContractUnitPrice()!=null&&
                                cstplItemShowUnit.getContractQuantity()!=null) {
                            qryTkMeaCSStlQShowNew.setBdCstpl_ContractAmount(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                        break;
                    }
                }

                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttContractQuantityTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractAmountTotal=new BigDecimal(0);
                // 根据成本计划项插接分包合同项
                for(int i=0;i<subcttStlQBySignPartList.size();i++) {
                    // 成本计划项遇到目标分包合同项
                    if(strItemOfEsItemHieRelapCstpl_Pkid.equals(subcttStlQBySignPartList.get(i).getStrCorrespondingPkid())) {
                        // 目标分包合同项的合同单价
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdUnitPrice());
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // 克隆目标进行处理后插接
                        QryTkMeaCSStlQShow qryTkMeaCSStlQShowNewInsert =(QryTkMeaCSStlQShow)BeanUtils.cloneBean(qryTkMeaCSStlQShowNew);
                        // 目标分包合同项的当期数量，当期金额
                        BigDecimal bdSubcttCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdCurrentPeriodQuantity());
                        BigDecimal bdSubcttCurrentPeriodAmount=bdSubcttCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);

                        BigDecimal bdSubcttBeginToCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdSubcttBeginToCurrentPeriodAmount=bdSubcttBeginToCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);

                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttBeginToCurrentPeriodQuantity);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttBeginToCurrentPeriodAmount);

                        // 分包合同
                        qryTkMeaCSStlQShowNewInsert.setStrSubctt_SignPartName(subcttStlQBySignPartList.get(i).getStrName());
                        qryTkMeaCSStlQShowNewInsert.setStrTkctt_Pkid(subcttStlQBySignPartList.get(i).getStrCorrespondingPkid()
                                + "/" + intGroup.toString());

                        // 分包结算
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_CurrentPeriodQQty(bdSubcttCurrentPeriodQuantity);
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_CurrentPeriodAmount(bdSubcttCurrentPeriodAmount);
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodQQty(bdSubcttBeginToCurrentPeriodQuantity);
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodAmount(bdSubcttBeginToCurrentPeriodAmount);

                        if(intGroup>1){
                            qryTkMeaCSStlQShowNewInsert.setStrTkctt_No("");
                            qryTkMeaCSStlQShowNewInsert.setStrTkctt_Name("");
                            qryTkMeaCSStlQShowNewInsert.setStrTkctt_Unit("");
                        }

                        // 最后一项之前的项
                        if(i<subcttStlQBySignPartList.size()-1){
                            // 下一项仍是目标分包合同项
                            if(cttItemShowTkctt.getPkid().equals(subcttStlQBySignPartList.get(i+1).getStrCorrespondingPkid())){
                                // 成本计划再设定
                                qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNewInsert);
                            }// 下一项不是目标分包合同项
                            else{
                                qryTkMeaCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaQuantity).subtract(bdSubcttContractQuantityTotal));
                                qryTkMeaCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodAmount(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaAmount).subtract(bdSubcttContractAmountTotal));
                                 qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNewInsert);
                                break;
                            }
                        }else{
                            // 总包计量与分包结算值差
                            qryTkMeaCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                    bdTkcttStlBToCMeaQuantity.subtract(bdSubcttContractQuantityTotal));
                            qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNew);
                }
            }
            qryTkMeaCSStlQShowListForExcel =new ArrayList<QryTkMeaCSStlQShow>();
            for(QryTkMeaCSStlQShow itemOfEsItemHieRelapTkctt: qryTkMeaCSStlQShowList){
                QryTkMeaCSStlQShow itemOfEsItemHieRelapTkcttTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(itemOfEsItemHieRelapTkctt);
                itemOfEsItemHieRelapTkcttTemp.setStrTkctt_No(ToolUtil.getIgnoreSpaceOfStr(itemOfEsItemHieRelapTkcttTemp.getStrTkctt_No()));
                qryTkMeaCSStlQShowListForExcel.add(itemOfEsItemHieRelapTkcttTemp);
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
        if(qryTkMeaCSStlQShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryTkMeaCSStlQShowListForExcel", qryTkMeaCSStlQShowListForExcel);
    }

    /*递归排序*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<CttItemShow> cttItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        EsCttItem esCttItem =null;
        for(EsCttItem itemUnit: subEsCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getLastUpdBy());
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
            if(!ToolUtil.getStrIgnoreNull(cttItemShowTemp.getCorrespondingPkid()).equals("")) {
                esCttItem = esCttItemService.getEsItemHieRelapByPkId(cttItemShowTemp.getCorrespondingPkid());
                cttItemShowTemp.setStrCorrespondingItemPkid(esCttItem.getPkid());
            }
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara, cttItemShowListPara);
        }
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

    /*根据group和orderid临时编制编码strNo*/
    private List<CttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CttItemShow itemUnit: cttItemShowListPara){
            if(itemUnit.getGrade().equals(intBeforeGrade)){
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
            if(ToolUtil.getStrIgnoreNull(strTkcttPkid).equals("")){
                MessageUtil.addWarn("请选择成本计划项。");
                return;
            }
            initData(strTkcttPkid);
            // StickyHeader对拼装列头平分宽度，不能设定宽度，兼容性不好，暂时停用
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*智能字段Start*/
    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public List<QryTkMeaCSStlQShow> getQryTkMeaCSStlQShowList() {
        return qryTkMeaCSStlQShowList;
    }

    public void setQryTkMeaCSStlQShowList(List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowList) {
        this.qryTkMeaCSStlQShowList = qryTkMeaCSStlQShowList;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public String getStrTkcttPkid() {
        return strTkcttPkid;
    }

    public void setStrTkcttPkid(String strTkcttPkid) {
        this.strTkcttPkid = strTkcttPkid;
    }

    public String getStrPeriodNo() {
        return strPeriodNo;
    }

    public void setStrPeriodNo(String strPeriodNo) {
        this.strPeriodNo = strPeriodNo;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public List<SelectItem> getTkcttList() {
        return tkcttList;
    }

    public void setTkcttList(List<SelectItem> tkcttList) {
        this.tkcttList = tkcttList;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public CommStlSubcttEngH getCommStlSubcttEngH() {
        return commStlSubcttEngH;
    }

    public void setCommStlSubcttEngH(CommStlSubcttEngH commStlSubcttEngH) {
        this.commStlSubcttEngH = commStlSubcttEngH;
    }

    public List<QryTkMeaCSStlQShow> getQryTkMeaCSStlQShowListForExcel() {
        return qryTkMeaCSStlQShowListForExcel;
    }

    public void setQryTkMeaCSStlQShowListForExcel(List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListForExcel) {
        this.qryTkMeaCSStlQShowListForExcel = qryTkMeaCSStlQShowListForExcel;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public EsItemStlTkcttEngMeaService getEsItemStlTkcttEngMeaService() {
        return esItemStlTkcttEngMeaService;
    }

    public void setEsItemStlTkcttEngMeaService(EsItemStlTkcttEngMeaService esItemStlTkcttEngMeaService) {
        this.esItemStlTkcttEngMeaService = esItemStlTkcttEngMeaService;
    }
    /*智能字段End*/
}
