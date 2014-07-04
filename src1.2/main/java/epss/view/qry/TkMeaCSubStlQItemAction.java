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
public class TkMeaCSubStlQItemAction {
    private static final Logger logger = LoggerFactory.getLogger(TkMeaCSubStlQItemAction.class);
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
        EsCttInfo esTkcttInfo= esCttInfoService.getCttInfoByPkId(strTkcttInfoPkid);
        commStlSubcttEngH.setStrTkcttId(esTkcttInfo.getId());
        commStlSubcttEngH.setStrTkcttName(esTkcttInfo.getName());
        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
        // 1。2。抽取相应总包合同的详细内容
        List<EsCttItem> esCttItemOfTkcttList =new ArrayList<EsCttItem>();
        esCttItemOfTkcttList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(),
                strTkcttPkid);
        // 根据总包合同内容的信息，拼成合同原稿
        List<CttItemShow> tkcttItemShowList =new ArrayList<>();
        recursiveDataTable("root", esCttItemOfTkcttList, tkcttItemShowList);
        tkcttItemShowList =getItemList_DoFromatNo(tkcttItemShowList);

        // 2。成本计划信息
        List<EsCttInfo> esCstplInfoList=esCttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                ESEnum.ITEMTYPE1.getCode(),esTkcttInfo.getPkid());
        if(esCstplInfoList.size()==0){
            return;
        }
        EsCttInfo esCstplInfo =esCstplInfoList.get(0);
        List<EsCttItem> cstplItemListTemp=
                esCttItemService.getEsItemList(ESEnum.ITEMTYPE1.getCode(),esCstplInfo.getPkid());
        List<CttItemShow> cstplItemShowListTemp =new ArrayList<>();
        recursiveDataTable("root", cstplItemListTemp, cstplItemShowListTemp);
        // 成本计划排版
        cstplItemShowListTemp =getItemList_DoFromatNo(cstplItemShowListTemp) ;

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

        /*拼装列表*/
        try {
            qryTkMeaCSStlQShowList =new ArrayList<QryTkMeaCSStlQShow>();
            String strFrontNoAndName="";
            for(CttItemShow tkcttItemShowUnit : tkcttItemShowList){
                Boolean insertedFlag=false ;
                QryTkMeaCSStlQShow qryTkMeaCSStlQShowTemp =new QryTkMeaCSStlQShow();

                 // 总包合同
                qryTkMeaCSStlQShowTemp.setTkctt_Pkid(tkcttItemShowUnit.getPkid());
                qryTkMeaCSStlQShowTemp.setTkctt_ParentPkid(tkcttItemShowUnit.getParentPkid());
                qryTkMeaCSStlQShowTemp.setTkctt_No(tkcttItemShowUnit.getStrNo());
                qryTkMeaCSStlQShowTemp.setTkctt_Name(tkcttItemShowUnit.getName());
                qryTkMeaCSStlQShowTemp.setTkctt_Unit(tkcttItemShowUnit.getUnit());
                qryTkMeaCSStlQShowTemp.setTkctt_CttUnitPrice(tkcttItemShowUnit.getContractUnitPrice());
                qryTkMeaCSStlQShowTemp.setTkctt_CttQty(tkcttItemShowUnit.getContractQuantity());
                if(tkcttItemShowUnit.getContractUnitPrice()!=null&&
                        tkcttItemShowUnit.getContractQuantity()!=null) {
                    qryTkMeaCSStlQShowTemp.setTkctt_CttAmt(
                            tkcttItemShowUnit.getContractUnitPrice().multiply(tkcttItemShowUnit.getContractQuantity()));
                }
                // 计量
                for(EsItemStlTkcttEngMea esItemStlTkcttEngMea:esItemStlTkcttEngMeaList){
                    if(ToolUtil.getStrIgnoreNull(tkcttItemShowUnit.getPkid()).equals(esItemStlTkcttEngMea.getTkcttItemPkid())){
                        // 总包合同单价
                        BigDecimal bdTkcttContractUnitPrice=ToolUtil.getBdIgnoreNull(tkcttItemShowUnit.getContractUnitPrice());
                        BigDecimal bdTkcttStlCMeaQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getCurrentPeriodQty());
                        BigDecimal bdTkcttStlCMeaAmount=bdTkcttStlCMeaQty.multiply(bdTkcttContractUnitPrice);
                        // 开累计量
                        BigDecimal bdTkcttStlBToCMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        BigDecimal bdTkcttStlBToCMeaAmount=bdTkcttStlBToCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        // 当期计量数量和金额
                        qryTkMeaCSStlQShowTemp.setTkcttStl_ThisStageMeaQty(bdTkcttStlCMeaQty);
                        qryTkMeaCSStlQShowTemp.setTkcttStl_ThisStageMeaAmt(bdTkcttStlCMeaAmount);
                        // 开累计量数量和金额
                        qryTkMeaCSStlQShowTemp.setTkcttStl_AddUpMeaQty(bdTkcttStlBToCMeaQuantity);
                        qryTkMeaCSStlQShowTemp.setTkcttStl_AddUpMeaAmt(bdTkcttStlBToCMeaAmount);
                        break;
                    }
                }

                // 成本计划
                for(CttItemShow cstplItemShowUnit : cstplItemShowListTemp){
                    if(tkcttItemShowUnit.getPkid().equals(cstplItemShowUnit.getCorrespondingPkid())) {
                        insertedFlag=true ;
                        //总包合同
                        if(strFrontNoAndName.equals(tkcttItemShowUnit.getStrNo()+tkcttItemShowUnit .getName())){
                            QryTkMeaCSStlQShow qryTkMeaCSStlQShowTempRe =new QryTkMeaCSStlQShow();
                            // 列表主键
                            qryTkMeaCSStlQShowTempRe.setTkctt_Pkid(
                                    qryTkMeaCSStlQShowList.size() +";"+tkcttItemShowUnit.getPkid());
                            // 成本计划内容
                            qryTkMeaCSStlQShowTempRe.setCstpl_No(cstplItemShowUnit.getStrNo());
                            qryTkMeaCSStlQShowTempRe.setCstpl_Pkid(cstplItemShowUnit.getPkid());
                            qryTkMeaCSStlQShowTempRe.setCstpl_CttUnitPrice(cstplItemShowUnit.getContractUnitPrice());
                            qryTkMeaCSStlQShowTempRe.setCstpl_CttQty(cstplItemShowUnit.getContractQuantity());
                            if(cstplItemShowUnit.getContractUnitPrice()!=null&&
                                    cstplItemShowUnit.getContractQuantity()!=null) {
                                qryTkMeaCSStlQShowTempRe.setCstpl_CttAmt(
                                        cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                            }
                            qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTempRe);
                        }else{
                            strFrontNoAndName=tkcttItemShowUnit.getStrNo()+tkcttItemShowUnit .getName();
                            qryTkMeaCSStlQShowTemp.setCstpl_Pkid(cstplItemShowUnit.getPkid());
                            qryTkMeaCSStlQShowTemp.setCstpl_No(cstplItemShowUnit.getStrNo());
                            qryTkMeaCSStlQShowTemp.setCstpl_CttUnitPrice(cstplItemShowUnit.getContractUnitPrice());
                            qryTkMeaCSStlQShowTemp.setCstpl_CttQty(cstplItemShowUnit.getContractQuantity());
                            if(cstplItemShowUnit.getContractUnitPrice()!=null&&
                                    cstplItemShowUnit.getContractQuantity()!=null) {
                                qryTkMeaCSStlQShowTemp.setCstpl_CttAmt(
                                        cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                            }
                            qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTemp);
                        }
                    }
                }
                if (insertedFlag.equals(false)){
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTemp);
                }
            }
            // 成本计划
            for(CttItemShow cstplItemShowUnit : cstplItemShowListTemp){
                if(ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getCorrespondingPkid()).length()<=0){
                    QryTkMeaCSStlQShow qryTkMeaCSStlQShowTempRe =new QryTkMeaCSStlQShow();
                    // 列表主键
                    qryTkMeaCSStlQShowTempRe.setTkctt_Pkid(qryTkMeaCSStlQShowList.size()+":");
                    qryTkMeaCSStlQShowTempRe.setTkctt_Name("成本计划：（"+cstplItemShowUnit.getName()+")");
                    // 成本计划内容
                    qryTkMeaCSStlQShowTempRe.setCstpl_No(cstplItemShowUnit.getStrNo());
                    qryTkMeaCSStlQShowTempRe.setCstpl_Pkid(cstplItemShowUnit.getPkid());
                    qryTkMeaCSStlQShowTempRe.setCstpl_CttUnitPrice(cstplItemShowUnit.getContractUnitPrice());
                    qryTkMeaCSStlQShowTempRe.setCstpl_CttQty(cstplItemShowUnit.getContractQuantity());
                    if(!ToolUtil.getBdIgnoreNull(cstplItemShowUnit.getContractAmount()).equals(ToolUtil.bigDecimal0)) {
                        qryTkMeaCSStlQShowTempRe.setCstpl_CttAmt(cstplItemShowUnit.getContractAmount());
                    } else{
                        if(cstplItemShowUnit.getContractUnitPrice()!=null&&
                                cstplItemShowUnit.getContractQuantity()!=null) {
                            qryTkMeaCSStlQShowTempRe.setCstpl_CttAmt(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                    }
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTempRe);
                }
            }

            // 4。分包结算
            List<QryShow> subcttStlQBySignPartList=esQueryService.getCSStlQBySignPartList(esCstplInfo.getPkid(), strPeriodNo);

            BigDecimal bdSubcttCttQtyTotal=new BigDecimal(0);
            BigDecimal bdSubcttCttAmtTotal=new BigDecimal(0);

            // 根据成本计划项插接分包合同项
            List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListTemp=new ArrayList<>();
            qryTkMeaCSStlQShowListTemp.addAll(qryTkMeaCSStlQShowList);
            qryTkMeaCSStlQShowList.clear();
            strFrontNoAndName="";
            for(QryTkMeaCSStlQShow tkMeaCstplUnit:qryTkMeaCSStlQShowListTemp) {
                Boolean insertedFlag=false ;
                // 总包合同单价
                BigDecimal bdTkcttContractUnitPrice=tkMeaCstplUnit.getTkctt_CttUnitPrice();
                // 当期计量
                BigDecimal bdTkcttStlCMeaQty=tkMeaCstplUnit.getTkcttStl_ThisStageMeaQty();
                BigDecimal bdTkcttStlCMeaAmount=tkMeaCstplUnit.getTkcttStl_ThisStageMeaAmt();
                // 开累计量
                BigDecimal bdTkcttStlBToCMeaQty=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStl_AddUpMeaQty());
                BigDecimal bdTkcttStlBToCMeaAmount=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStl_AddUpMeaAmt());
                for(int i=0;i<subcttStlQBySignPartList.size();i++) {
                    // 成本计划项遇到目标分包合同项
                    if(ToolUtil.getStrIgnoreNull(tkMeaCstplUnit.getCstpl_Pkid()).equals(subcttStlQBySignPartList.get(i).getStrCorrespondingPkid())) {
                        insertedFlag=true ;
                        // 目标分包合同项的合同单价
                        BigDecimal bdSubcttCttUnitPrice=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdUnitPrice());
                        // 目标分包合同项的当期数量，当期金额
                        BigDecimal bdThisStageQty=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdCurrentPeriodQuantity());
                        BigDecimal bdThisStageAmt=bdThisStageQty.multiply(bdSubcttCttUnitPrice);
                        // 目标分包合同项的开累数量，开累金额
                        BigDecimal bdAddUpToQty=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdAddUpToAmt=bdAddUpToQty.multiply(bdSubcttCttUnitPrice);

                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttCttQtyTotal=bdSubcttCttQtyTotal.add(bdAddUpToQty);
                        bdSubcttCttAmtTotal=bdSubcttCttAmtTotal.add(bdAddUpToAmt);

                        //总包合同
                        tkMeaCstplUnit.setTkctt_Pkid(qryTkMeaCSStlQShowList.size() +";");

                        // 分包合同
                        tkMeaCstplUnit.setSubctt_CorrespondingPkid(subcttStlQBySignPartList.get(i).getStrCorrespondingPkid());
                        tkMeaCstplUnit.setSubctt_SignPartName(subcttStlQBySignPartList.get(i).getStrName());

                        // 分包结算
                        tkMeaCstplUnit.setSubcttStl_ThisStageQty(bdThisStageQty);
                        tkMeaCstplUnit.setSubcttStl_ThisStageAmt(bdThisStageAmt);
                        tkMeaCstplUnit.setSubcttStl_AddUpQty(bdAddUpToQty);
                        tkMeaCstplUnit.setSubcttStl_AddUpAmt(bdAddUpToAmt);

                        // 最后一项之前的项
                        if(i<subcttStlQBySignPartList.size()-1){
                            // 下一项仍是目标分包合同项
                            if(tkMeaCstplUnit.getCstpl_Pkid().equals(subcttStlQBySignPartList.get(i+1).getStrCorrespondingPkid())){
                                // 成本计划再设定
                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                            }// 下一项不是目标分包合同项
                            else{
                                tkMeaCstplUnit.setMeaS_AddUpQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaQty).subtract(bdSubcttCttQtyTotal));
                                tkMeaCstplUnit.setMeaS_AddUpAmt(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaAmount).subtract(bdSubcttCttAmtTotal));
                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                                break;
                            }
                        }else{
                            // 总包计量与分包结算值差
                            tkMeaCstplUnit.setMeaS_AddUpQty(
                                    bdTkcttStlBToCMeaQty.subtract(bdSubcttCttQtyTotal));
                            qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                        }
                    }
                }
                if(insertedFlag.equals(false)){
                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                }
            }
            // 将分析活动数据装填到Excel中
            qryTkMeaCSStlQShowListForExcel =new ArrayList<QryTkMeaCSStlQShow>();
            for(QryTkMeaCSStlQShow itemOfEsItemHieRelapTkctt: qryTkMeaCSStlQShowList){
                QryTkMeaCSStlQShow itemOfEsItemHieRelapTkcttTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(itemOfEsItemHieRelapTkctt);
                itemOfEsItemHieRelapTkcttTemp.setTkctt_No(ToolUtil.getIgnoreSpaceOfStr(itemOfEsItemHieRelapTkcttTemp.getTkctt_No()));
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
        subEsCttItemList =getEsItemListByLevelParentPkid(strLevelParentId, esCttItemListPara);
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
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara, cttItemShowListPara);
        }
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsItemListByLevelParentPkid(String strLevelParentPkid,
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
    private List<CttItemShow> getItemList_DoFromatNo(
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
