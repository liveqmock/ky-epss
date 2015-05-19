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
import epss.repository.model.ProgStlItemTkMea;
import skyline.util.JxlsManager;
import epss.repository.model.CttItem;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.CttInfo;
import epss.service.*;
import epss.service.EsQueryService;
import epss.view.EsCommon;
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
    private List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowList;
    private List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListForExcel;

    private List<SelectItem> tkcttList;

    private String strTkcttPkid;
    private String strPeriodNo;

    // 画面上控件的显示控制
    private ReportHeader reportHeader;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        try {
            beansMap = new HashMap();
            reportHeader =new ReportHeader();
            // 获取已经批准了的总包合同列表
            CttInfoShow cttInfoShowPara=new CttInfoShow();
            cttInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            cttInfoShowPara.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getListShowByModelShow(cttInfoShowPara);
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
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
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
    private void initData(String strCttInfoPkid) {
        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        // 1。总包合同信息
        // 1。1。取出总包合同信息
        CttInfo esTkcttInfo= cttInfoService.getCttInfoByPkId(strCttInfoPkid);
        reportHeader.setStrTkcttId(esTkcttInfo.getId());
        reportHeader.setStrTkcttName(esTkcttInfo.getName());
        beansMap.put("reportHeader", reportHeader);
        // 1。2。抽取相应总包合同的详细内容
        List<CttItem> cttItemOfTkcttList = cttItemService.getEsItemList(
                EnumResType.RES_TYPE0.getCode(),
                strTkcttPkid);
        // 根据总包合同内容的信息，拼成合同原稿
        List<CttItemShow> tkcttItemShowList =new ArrayList<>();
        recursiveDataTable("root", cttItemOfTkcttList, tkcttItemShowList);
        tkcttItemShowList =getItemList_DoFromatNo(tkcttItemShowList);

        // 2。成本计划信息
        List<CttInfo> esCstplInfoList= cttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                EnumResType.RES_TYPE1.getCode(),esTkcttInfo.getPkid());
        if(esCstplInfoList.size()==0){
            return;
        }
        CttInfo esCstplInfo =esCstplInfoList.get(0);
        List<CttItem> cstplItemListTemp=
                cttItemService.getEsItemList(EnumResType.RES_TYPE1.getCode(),esCstplInfo.getPkid());
        List<CttItemShow> cstplItemShowListTemp =new ArrayList<>();
        recursiveDataTable("root", cstplItemListTemp, cstplItemShowListTemp);
        // 成本计划排版
        cstplItemShowListTemp =getItemList_DoFromatNo(cstplItemShowListTemp) ;

        // 3。总包合同最近批准了的总包计量数据
        // 小于等于所选期码的最近已经批准了的计量期码
        String strMeaLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestApprovedPeriodNoByEndPeriod(
                        EnumResType.RES_TYPE7.getCode(),strCttInfoPkid,strPeriodNo));
        List<ProgStlItemTkMea> progStlItemTkMeaList =new ArrayList<ProgStlItemTkMea>();
        if(!ToolUtil.getStrIgnoreNull(strMeaLatestApprovedPeriodNo).equals("")){
            ProgStlItemTkMea progStlItemTkMea =new ProgStlItemTkMea();
            progStlItemTkMea.setTkcttPkid(strCttInfoPkid);
            progStlItemTkMea.setPeriodNo(strMeaLatestApprovedPeriodNo);
            progStlItemTkMeaList = progStlItemTkMeaService.selectRecordsByPkidPeriodNoExample(progStlItemTkMea);
        }

        /*拼装列表*/
        try {
            qryTkMeaCSStlQShowList = new ArrayList<QryTkMeaCSStlQShow>();
            QryTkMeaCSStlQShow qryTkMeaCSStlQShowForTotalAmtOfAllItem = new QryTkMeaCSStlQShow();
            BigDecimal tkcttItem_CttAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal tkcttStlItem_ThisStageAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal tkcttStlItem_AddUpAmt__TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal cstplItem_Amt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal cstplTkcttItem_TotalAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal tkcttStlCstplItem_ThisStageAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal tkcttStlCstplItem_AddUpAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal subcttStlItem_ThisStageAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal subcttStlItem_AddUpAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal meaSItem_AddUpAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal meaSCstplItem_AddUpAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal subcttStlItem_ThisStageMAmt_TotalAmtOfAllItem = new BigDecimal(0);
            BigDecimal subcttStlItem_AddUpMAmt_TotalAmtOfAllItem = new BigDecimal(0);

            for (CttItemShow tkcttItemShowUnit : tkcttItemShowList) {
                Boolean insertedFlag = false;
                QryTkMeaCSStlQShow qryTkMeaCSStlQShowTemp = new QryTkMeaCSStlQShow();

                 // 总包合同
                qryTkMeaCSStlQShowTemp.setTkcttItem_Pkid(tkcttItemShowUnit.getPkid());
                qryTkMeaCSStlQShowTemp.setTkcttItem_ParentPkid(tkcttItemShowUnit.getParentPkid());
                qryTkMeaCSStlQShowTemp.setTkcttItem_No(tkcttItemShowUnit.getStrNo());
                qryTkMeaCSStlQShowTemp.setTkcttItem_Name(tkcttItemShowUnit.getName());
                qryTkMeaCSStlQShowTemp.setTkcttItem_Unit(tkcttItemShowUnit.getUnit());
                qryTkMeaCSStlQShowTemp.setTkcttItem_CttUnitPrice(ToolUtil.getBdFrom0ToNull(tkcttItemShowUnit.getContractUnitPrice()));
                qryTkMeaCSStlQShowTemp.setTkcttItem_CttQty(ToolUtil.getBdFrom0ToNull(tkcttItemShowUnit.getContractQuantity()));
                if(!ToolUtil.Is0Null(qryTkMeaCSStlQShowTemp.getTkcttItem_CttUnitPrice())&&
                        !ToolUtil.Is0Null(qryTkMeaCSStlQShowTemp.getTkcttItem_CttQty())) {
                    qryTkMeaCSStlQShowTemp.setTkcttItem_CttAmt(
                            tkcttItemShowUnit.getContractUnitPrice().multiply(tkcttItemShowUnit.getContractQuantity()));
                    tkcttItem_CttAmt_TotalAmtOfAllItem=tkcttItem_CttAmt_TotalAmtOfAllItem.add(
                            ToolUtil.getBdIgnoreNull(qryTkMeaCSStlQShowTemp.getTkcttItem_CttAmt()));
                }
                // 计量
                for(ProgStlItemTkMea progStlItemTkMea : progStlItemTkMeaList){
                    if(ToolUtil.getStrIgnoreNull(tkcttItemShowUnit.getPkid()).equals(progStlItemTkMea.getTkcttItemPkid())){
                        // 总包合同单价
                        BigDecimal bdTkcttContractUnitPrice=ToolUtil.getBdIgnoreNull(tkcttItemShowUnit.getContractUnitPrice());
                        BigDecimal bdTkcttStlCMeaQty=ToolUtil.getBdIgnoreNull(progStlItemTkMea.getCurrentPeriodQty());
                        BigDecimal bdTkcttStlCMeaAmount=bdTkcttStlCMeaQty.multiply(bdTkcttContractUnitPrice);
                        // 开累计量
                        BigDecimal bdTkcttStlBToCMeaQuantity=ToolUtil.getBdIgnoreNull(progStlItemTkMea.getBeginToCurrentPeriodQty());
                        BigDecimal bdTkcttStlBToCMeaAmount=bdTkcttStlBToCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        // 当期计量数量和金额
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_ThisStageQty(bdTkcttStlCMeaQty);
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_ThisStageAmt(bdTkcttStlCMeaAmount);
                        tkcttStlItem_ThisStageAmt_TotalAmtOfAllItem=tkcttStlItem_ThisStageAmt_TotalAmtOfAllItem.add(
                                ToolUtil.getBdIgnoreNull(bdTkcttStlCMeaAmount));
                        // 开累计量数量和金额
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_AddUpQty(bdTkcttStlBToCMeaQuantity);
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_AddUpAmt(bdTkcttStlBToCMeaAmount);
                        tkcttStlItem_AddUpAmt__TotalAmtOfAllItem=tkcttStlItem_AddUpAmt__TotalAmtOfAllItem.add(
                                ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaAmount));
                        break;
                    }
                }

                // 成本计划
                BigDecimal bdCstplTkcttItem_TotalAmt=new BigDecimal(0);
                for(CttItemShow cstplItemShowUnit : cstplItemShowListTemp){
                    QryTkMeaCSStlQShow tkMeaCstplUnitTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(qryTkMeaCSStlQShowTemp);
                    if(tkcttItemShowUnit.getPkid().equals(cstplItemShowUnit.getCorrespondingPkid())) {
                        if(insertedFlag.equals(true)){
                            tkMeaCstplUnitTemp.setTkcttItem_Pkid(null);
                            tkMeaCstplUnitTemp.setTkcttItem_No(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Name(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Unit(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttUnitPrice(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttQty(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpAmt(null);
                        }
                        insertedFlag=true ;
                        tkMeaCstplUnitTemp.setCstplItem_Pkid(cstplItemShowUnit.getPkid());
                        tkMeaCstplUnitTemp.setCstplItem_No(cstplItemShowUnit.getStrNo());
                        tkMeaCstplUnitTemp.setCstplItem_Name(cstplItemShowUnit.getName());
                        tkMeaCstplUnitTemp.setCstplItem_UnitPrice(ToolUtil.getBdFrom0ToNull(cstplItemShowUnit.getContractUnitPrice()));
                        tkMeaCstplUnitTemp.setCstplItem_Qty(ToolUtil.getBdFrom0ToNull(cstplItemShowUnit.getContractQuantity()));
                        if(!ToolUtil.Is0Null(cstplItemShowUnit.getContractUnitPrice())&&
                           !ToolUtil.Is0Null(cstplItemShowUnit.getContractQuantity())) {
                            tkMeaCstplUnitTemp.setCstplItem_Amt(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                        if(tkMeaCstplUnitTemp.getCstplItem_Amt()!=null) {
                            bdCstplTkcttItem_TotalAmt=bdCstplTkcttItem_TotalAmt.add(tkMeaCstplUnitTemp.getCstplItem_Amt()) ;
                            cstplItem_Amt_TotalAmtOfAllItem=cstplItem_Amt_TotalAmtOfAllItem.add(
                                    ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getCstplItem_Amt()));
                        }
                        qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                    }
                }

                if (insertedFlag.equals(false)){
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTemp);
                }

                if(bdCstplTkcttItem_TotalAmt.compareTo(new BigDecimal(0))>0) {
                    qryTkMeaCSStlQShowList.get(qryTkMeaCSStlQShowList.size()-1).setCstplTkcttItem_TotalAmt(bdCstplTkcttItem_TotalAmt);
                    cstplTkcttItem_TotalAmt_TotalAmtOfAllItem=cstplTkcttItem_TotalAmt_TotalAmtOfAllItem.add(
                            ToolUtil.getBdIgnoreNull(qryTkMeaCSStlQShowList.get(qryTkMeaCSStlQShowList.size()-1).getCstplTkcttItem_TotalAmt()));
                    if(ToolUtil.getBdIgnoreNull(tkcttItemShowUnit.getContractQuantity()).compareTo(ToolUtil.bigDecimal0)>0) {
                        // 算出计量产值单价
                        BigDecimal bdCstplTkcttItem_TotalUnitPrice=
                                bdCstplTkcttItem_TotalAmt.divide(tkcttItemShowUnit.getContractQuantity(),6,BigDecimal.ROUND_HALF_UP);
                        qryTkMeaCSStlQShowList.get(qryTkMeaCSStlQShowList.size()-1).setCstplTkcttItem_TotalUnitPrice(bdCstplTkcttItem_TotalUnitPrice);

                        for(int i=qryTkMeaCSStlQShowList.size()-1;i>=0;i--){
                            if(qryTkMeaCSStlQShowList.get(i).getTkcttItem_Pkid()!=null) {
                                if(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_ThisStageQty()!=null) {
                                    qryTkMeaCSStlQShowList.get(i).setTkcttStlCstplItem_ThisStageAmt(
                                            bdCstplTkcttItem_TotalUnitPrice.multiply(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_ThisStageQty()));
                                    tkcttStlCstplItem_ThisStageAmt_TotalAmtOfAllItem=tkcttStlCstplItem_ThisStageAmt_TotalAmtOfAllItem.add(
                                            ToolUtil.getBdIgnoreNull(qryTkMeaCSStlQShowList.get(i).getTkcttStlCstplItem_ThisStageAmt()));
                                }
                                if(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_AddUpQty()!=null) {
                                    qryTkMeaCSStlQShowList.get(i).setTkcttStlCstplItem_AddUpAmt(
                                            bdCstplTkcttItem_TotalUnitPrice.multiply(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_AddUpQty()));
                                    tkcttStlCstplItem_AddUpAmt_TotalAmtOfAllItem=tkcttStlCstplItem_AddUpAmt_TotalAmtOfAllItem.add(
                                            ToolUtil.getBdIgnoreNull(qryTkMeaCSStlQShowList.get(i).getTkcttStlCstplItem_AddUpAmt()));
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // 成本计划中空头项需要在最后一一列出
            for(CttItemShow cstplItemShowUnit : cstplItemShowListTemp){
                if(ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getCorrespondingPkid()).length()<=0){
                    QryTkMeaCSStlQShow qryTkMeaCSStlQShowTempRe =new QryTkMeaCSStlQShow();
                    // 列表主键
                    qryTkMeaCSStlQShowTempRe.setTkcttItem_Pkid(qryTkMeaCSStlQShowList.size()+":");
                    qryTkMeaCSStlQShowTempRe.setTkcttItem_Name("成本计划：（"+cstplItemShowUnit.getName()+")");
                    // 成本计划内容
                    qryTkMeaCSStlQShowTempRe.setCstplItem_Pkid(cstplItemShowUnit.getPkid());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_No(cstplItemShowUnit.getStrNo());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_Name(cstplItemShowUnit.getName());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_UnitPrice(cstplItemShowUnit.getContractUnitPrice());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_Qty(cstplItemShowUnit.getContractQuantity());
                    if(!ToolUtil.Is0Null(cstplItemShowUnit.getContractAmount())) {
                        qryTkMeaCSStlQShowTempRe.setCstplItem_Amt(cstplItemShowUnit.getContractAmount());
                    } else{
                        if(!ToolUtil.Is0Null(cstplItemShowUnit.getContractUnitPrice())&&
                                !ToolUtil.Is0Null(cstplItemShowUnit.getContractQuantity())) {
                            qryTkMeaCSStlQShowTempRe.setCstplItem_Amt(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                    }
                    cstplItem_Amt_TotalAmtOfAllItem=cstplItem_Amt_TotalAmtOfAllItem.add(
                            ToolUtil.getBdIgnoreNull(qryTkMeaCSStlQShowTempRe.getCstplItem_Amt()));
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTempRe);
                }
            }

            // 4。分包结算
            List<QryTkMeaCSStlQShow> subcttStlQBySignPartList =
                    esQueryService.getCSStlQBySignPartList(esCstplInfo.getPkid(), strPeriodNo);

            List<QryTkMeaCSStlQShow> subcttStlMBySignPartList =
                    esQueryService.getCSStlMBySignPartList(esCstplInfo.getPkid(), strPeriodNo);

            // 根据成本计划项插接分包合同项
            List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListTemp=new ArrayList<>();
            qryTkMeaCSStlQShowListTemp.addAll(qryTkMeaCSStlQShowList);
            qryTkMeaCSStlQShowList.clear();
            for(QryTkMeaCSStlQShow tkMeaCstplUnit:qryTkMeaCSStlQShowListTemp) {
                Boolean insertedFlag=false ;
                BigDecimal bdSubcttCttQtyTotal=new BigDecimal(0);
                BigDecimal bdSubcttCttAmtTotal=new BigDecimal(0);
                // 开累计量
                BigDecimal bdTkcttStlBToCMeaQty=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStlItem_AddUpQty());
                BigDecimal bdTkcttStlBToCMeaAmt=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStlItem_AddUpAmt());
                BigDecimal bdTkcttStlCstplBToCMeaAmt=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStlCstplItem_AddUpAmt());
                for(int i=0;i<subcttStlQBySignPartList.size();i++) {
                    QryTkMeaCSStlQShow tkMeaCstplUnitTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(tkMeaCstplUnit);
                    // 成本计划项遇到目标分包合同项
                    if(ToolUtil.getStrIgnoreNull(tkMeaCstplUnitTemp.getCstplItem_Pkid()).equals(
                            subcttStlQBySignPartList.get(i).getSubcttItem_CorrPkid())) {
                        // 目标分包合同项的合同单价
                        BigDecimal bdSubcttCttUnitPrice=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getSubcttItem_UnitPrice());
                        // 目标分包合同项的当期数量，当期金额
                        BigDecimal bdThisStageQty=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getSubcttStlItem_ThisStageQty());
                        BigDecimal bdThisStageAmt=bdThisStageQty.multiply(bdSubcttCttUnitPrice);
                        // 目标分包合同项的开累数量，开累金额
                        BigDecimal bdAddUpToQty=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getSubcttStlItem_AddUpQty());
                        BigDecimal bdAddUpToAmt=bdAddUpToQty.multiply(bdSubcttCttUnitPrice);

                        // 累计目标分包合同项的合同数量，合同单价，合同金额
                        bdSubcttCttQtyTotal=bdSubcttCttQtyTotal.add(bdAddUpToQty);
                        bdSubcttCttAmtTotal=bdSubcttCttAmtTotal.add(bdAddUpToAmt);

                        //总包合同
                        if(insertedFlag.equals(true)){
                            tkMeaCstplUnitTemp.setTkcttItem_Pkid(null);
                            tkMeaCstplUnitTemp.setTkcttItem_No(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Name(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Unit(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttUnitPrice(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttQty(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlCstplItem_ThisStageAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlCstplItem_AddUpAmt(null);
                            tkMeaCstplUnitTemp.setCstplItem_Name(null);
                            tkMeaCstplUnitTemp.setCstplItem_UnitPrice(null);
                            tkMeaCstplUnitTemp.setCstplItem_Qty(null);
                            tkMeaCstplUnitTemp.setCstplItem_Amt(null);
                            tkMeaCstplUnitTemp.setCstplTkcttItem_TotalAmt(null);
                            tkMeaCstplUnitTemp.setCstplTkcttItem_TotalUnitPrice(null);
                        }

                        insertedFlag=true ;
                        // 分包合同
                        tkMeaCstplUnitTemp.setSubcttItem_Name(subcttStlQBySignPartList.get(i).getSubcttItem_Name());
                        tkMeaCstplUnitTemp.setSubcttItem_CorrPkid(subcttStlQBySignPartList.get(i).getSubcttItem_CorrPkid());
                        tkMeaCstplUnitTemp.setSubcttItem_SignPartName(subcttStlQBySignPartList.get(i).getSubcttItem_SignPartName());

                        // 分包结算
                        tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageQty(bdThisStageQty);
                        tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageAmt(bdThisStageAmt);
                        subcttStlItem_ThisStageAmt_TotalAmtOfAllItem=subcttStlItem_ThisStageAmt_TotalAmtOfAllItem.add(
                                ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getSubcttStlItem_ThisStageAmt()));
                        tkMeaCstplUnitTemp.setSubcttStlItem_AddUpQty(bdAddUpToQty);
                        tkMeaCstplUnitTemp.setSubcttStlItem_AddUpAmt(bdAddUpToAmt);
                        subcttStlItem_AddUpAmt_TotalAmtOfAllItem=subcttStlItem_AddUpAmt_TotalAmtOfAllItem.add(
                                ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getSubcttStlItem_AddUpAmt()));

                        // 最后一项之前的项
                        if(i<subcttStlQBySignPartList.size()-1){
                            // 下一项仍是目标分包合同项
                            if(tkMeaCstplUnitTemp.getCstplItem_Pkid().equals(
                                    subcttStlQBySignPartList.get(i+1).getSubcttItem_CorrPkid())){
                                // 成本计划再设定
                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                            }// 下一项不是目标分包合同项
                            else{
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaQty).subtract(bdSubcttCttQtyTotal));
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaAmt).subtract(bdSubcttCttAmtTotal));
                                tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlCstplBToCMeaAmt).subtract(bdSubcttCttAmtTotal));
                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                                meaSItem_AddUpAmt_TotalAmtOfAllItem=meaSItem_AddUpAmt_TotalAmtOfAllItem.add(
                                        ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getMeaSItem_AddUpAmt()));
                                meaSCstplItem_AddUpAmt_TotalAmtOfAllItem=meaSCstplItem_AddUpAmt_TotalAmtOfAllItem.add(
                                        ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getMeaSCstplItem_AddUpAmt()));
                                break;
                            }
                        }else{
                            // 总包计量与分包结算值差
                            tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(
                                    bdTkcttStlBToCMeaQty.subtract(bdSubcttCttQtyTotal));
                            tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(
                                    bdTkcttStlBToCMeaAmt.subtract(bdSubcttCttAmtTotal));
                            tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(
                                    bdTkcttStlCstplBToCMeaAmt.subtract(bdSubcttCttAmtTotal));
                            qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                            meaSItem_AddUpAmt_TotalAmtOfAllItem=meaSItem_AddUpAmt_TotalAmtOfAllItem.add(
                                    ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getMeaSItem_AddUpAmt()));
                            meaSCstplItem_AddUpAmt_TotalAmtOfAllItem=meaSCstplItem_AddUpAmt_TotalAmtOfAllItem.add(
                                    ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getMeaSCstplItem_AddUpAmt()));
                        }
                    }
                }

                if(insertedFlag.equals(false)){
                    tkMeaCstplUnit.setMeaSItem_AddUpQty(tkMeaCstplUnit.getTkcttStlItem_AddUpQty());
                    tkMeaCstplUnit.setMeaSItem_AddUpAmt(tkMeaCstplUnit.getTkcttStlItem_AddUpAmt());
                    tkMeaCstplUnit.setMeaSCstplItem_AddUpAmt(tkMeaCstplUnit.getTkcttStlCstplItem_AddUpAmt());
                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                    meaSItem_AddUpAmt_TotalAmtOfAllItem=meaSItem_AddUpAmt_TotalAmtOfAllItem.add(
                            ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getMeaSItem_AddUpAmt()));
                    meaSCstplItem_AddUpAmt_TotalAmtOfAllItem=meaSCstplItem_AddUpAmt_TotalAmtOfAllItem.add(
                            ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getMeaSCstplItem_AddUpAmt()));
                }
            }

            // 材料
            List<QryTkMeaCSStlQShow> qryTkMeaCSStlMShowListTemp = new ArrayList<>();
            qryTkMeaCSStlMShowListTemp.addAll(qryTkMeaCSStlQShowList);
            qryTkMeaCSStlQShowList.clear();
            for (int j = 0; j < qryTkMeaCSStlMShowListTemp.size(); j++) {
                QryTkMeaCSStlQShow tkMeaCstplUnit = new QryTkMeaCSStlQShow();
                tkMeaCstplUnit = (QryTkMeaCSStlQShow) BeanUtils.cloneBean(qryTkMeaCSStlMShowListTemp.get(j));
                Boolean insertedFlag = false;
                String CstplItem_Pkidtemp1 = "";
                String CstplItem_Pkidtemp2 = "";
                BigDecimal bdSubcttCttAmtTotal = new BigDecimal(0);
                //获取这次和下次成本计划pkid ,最后判断两次pkid是否相等,相等这次数据不使用,不相等使用,只用一次成本计划pkid,对应项与用这次pkid进行对应
                if (j < qryTkMeaCSStlMShowListTemp.size() - 1) {
                    CstplItem_Pkidtemp1 = ToolUtil.getStrIgnoreNull((qryTkMeaCSStlMShowListTemp.get(j).getCstplItem_Pkid()));
                    CstplItem_Pkidtemp2 = ToolUtil.getStrIgnoreNull((qryTkMeaCSStlMShowListTemp.get(j + 1).getCstplItem_Pkid()));
                } else {
                    CstplItem_Pkidtemp1 = ToolUtil.getStrIgnoreNull((qryTkMeaCSStlMShowListTemp.get(j).getCstplItem_Pkid()));
                    CstplItem_Pkidtemp2 = ToolUtil.getStrIgnoreNull((qryTkMeaCSStlMShowListTemp.get(j).getCstplItem_Pkid())) + "error";
                }
                String fgtemp = "Y";  //   N 下条pkid 不等; Y 下条pkid 相等
                if (!CstplItem_Pkidtemp1.
                        equals(CstplItem_Pkidtemp2)) {
                    fgtemp = "N";
                    // 数量差
                    // 计量分包结算金额差
                    // 计量成本分包结算金额差
                    BigDecimal meaSItem_AddUpQty = tkMeaCstplUnit.getMeaSItem_AddUpQty();
                    BigDecimal meaSItem_AddUpAmtTemp = tkMeaCstplUnit.getMeaSItem_AddUpAmt();
                    BigDecimal meaSCstplItem_AddUpAmtTemp = tkMeaCstplUnit.getMeaSCstplItem_AddUpAmt();

                    for (int i = 0; i < subcttStlMBySignPartList.size(); i++) {
                        QryTkMeaCSStlQShow tkMeaCstplUnitTemp = (QryTkMeaCSStlQShow) BeanUtils.cloneBean(tkMeaCstplUnit);
                        // 成本计划项遇到目标分包合同项
                        // 分包信息和成本信息多对一,会有第二次循环,这样insertedFlag在第二次为true这样第二次显示时去掉总包信息你
                        if (ToolUtil.getStrIgnoreNull(tkMeaCstplUnitTemp.getCstplItem_Pkid()).equals(
                                subcttStlMBySignPartList.get(i).getSubcttItem_CorrPkid())) {

                            // 目标分包合同项的合同单价
                            BigDecimal bdSubcttCttUnitPrice = ToolUtil.getBdIgnoreNull(subcttStlMBySignPartList.get(i).getSubcttItem_UnitPrice());
                            // 目标分包合同项的当期数量，当期金额
                            BigDecimal bdThisStageMQty = ToolUtil.getBdIgnoreNull(subcttStlMBySignPartList.get(i).getSubcttStlItem_ThisStageMQty());
                            BigDecimal bdThisStageMAmt = bdThisStageMQty.multiply(bdSubcttCttUnitPrice);
                            // 目标分包合同项的开累数量，开累金额
                            BigDecimal bdAddUpToMQty = ToolUtil.getBdIgnoreNull(subcttStlMBySignPartList.get(i).getSubcttStlItem_AddUpMQty());
                            BigDecimal bdAddUpToMAmt = bdAddUpToMQty.multiply(bdSubcttCttUnitPrice);

                            // 累计目标分包合同项的合同数量，合同单价，合同金额
//                            bdSubcttCttQtyTotal = bdSubcttCttQtyTotal.add(bdAddUpToMQty);
                            bdSubcttCttAmtTotal = bdSubcttCttAmtTotal.add(bdAddUpToMAmt);  // 本次的金额

                            //总包合同
                            if (insertedFlag.equals(true)) {
                                tkMeaCstplUnitTemp.setTkcttItem_Pkid(null);
                                tkMeaCstplUnitTemp.setTkcttItem_No(null);
                                tkMeaCstplUnitTemp.setTkcttItem_Name(null);
                                tkMeaCstplUnitTemp.setTkcttItem_Unit(null);
                                tkMeaCstplUnitTemp.setTkcttItem_CttUnitPrice(null);
                                tkMeaCstplUnitTemp.setTkcttItem_CttQty(null);
                                tkMeaCstplUnitTemp.setTkcttItem_CttAmt(null);
                                tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageQty(null);
                                tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageAmt(null);
                                tkMeaCstplUnitTemp.setTkcttStlCstplItem_ThisStageAmt(null);
                                tkMeaCstplUnitTemp.setTkcttStlItem_AddUpQty(null);
                                tkMeaCstplUnitTemp.setTkcttStlItem_AddUpAmt(null);
                                tkMeaCstplUnitTemp.setTkcttStlCstplItem_AddUpAmt(null);
                                tkMeaCstplUnitTemp.setCstplItem_Name(null);
                                tkMeaCstplUnitTemp.setCstplItem_UnitPrice(null);
                                tkMeaCstplUnitTemp.setCstplItem_Qty(null);
                                tkMeaCstplUnitTemp.setCstplItem_Amt(null);
                                tkMeaCstplUnitTemp.setCstplTkcttItem_TotalAmt(null);
                                tkMeaCstplUnitTemp.setCstplTkcttItem_TotalUnitPrice(null);

                                tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageQty(null);
                                tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageAmt(null);
                                tkMeaCstplUnitTemp.setSubcttStlItem_AddUpQty(null);
                                tkMeaCstplUnitTemp.setSubcttStlItem_AddUpAmt(null);
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(null);  // 计量分包比较-数量
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(null);
                                tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(null);

                            }

                            insertedFlag = true;
                            // 分包合同
                            tkMeaCstplUnitTemp.setSubcttItem_Name(subcttStlMBySignPartList.get(i).getSubcttItem_Name());
                            tkMeaCstplUnitTemp.setSubcttItem_CorrPkid(subcttStlMBySignPartList.get(i).getSubcttItem_CorrPkid());
                            tkMeaCstplUnitTemp.setSubcttItem_SignPartName(subcttStlMBySignPartList.get(i).getSubcttItem_SignPartName());

                            // 分包结算 材料本期合计
                            tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageMQty(bdThisStageMQty);
                            tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageMAmt(bdThisStageMAmt);
                            subcttStlItem_ThisStageMAmt_TotalAmtOfAllItem = subcttStlItem_ThisStageMAmt_TotalAmtOfAllItem.add(
                                    ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getSubcttStlItem_ThisStageMAmt()));
                            // 材料总合计
                            tkMeaCstplUnitTemp.setSubcttStlItem_AddUpMQty(bdAddUpToMQty);
                            tkMeaCstplUnitTemp.setSubcttStlItem_AddUpMAmt(bdAddUpToMAmt);
                            subcttStlItem_AddUpMAmt_TotalAmtOfAllItem = subcttStlItem_AddUpMAmt_TotalAmtOfAllItem.add(
                                    ToolUtil.getBdIgnoreNull(tkMeaCstplUnitTemp.getSubcttStlItem_AddUpMAmt()));

                            // 最后一项之前的项
                            if (i < subcttStlMBySignPartList.size() - 1) {    // 材料列表的最后一行之前
                                // 下一项仍是目标分包合同项
                                //  成本pkid 和对应下一项分包,把本条信息写入list,继续下次循环,这次成本不能对应下个分包合同, 则需要记下此次的计量数据,最后进行合计
                                if (tkMeaCstplUnitTemp.getCstplItem_Pkid().equals(
                                        subcttStlMBySignPartList.get(i + 1).getSubcttItem_CorrPkid())) {
                                    // 成本计划再设定
                                    tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(null);
                                    tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(null);
                                    tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(null);
                                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                                }// 下一项不是目标分包合同项
                                else {   // 对 当前分包数据进行合计

//                                    tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(
//                                            ToolUtil.getBdIgnoreNull(meaSItem_AddUpQty) );
                                    //  用已有的计量比较差 再除去本次的材料费
                                    tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(    //计量分包结算金额差
                                            ToolUtil.getBdIgnoreNull(meaSItem_AddUpAmtTemp).add(bdSubcttCttAmtTotal));
                                    tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(   // 计量成本分包结算金额差
                                            ToolUtil.getBdIgnoreNull(meaSCstplItem_AddUpAmtTemp).add(bdSubcttCttAmtTotal));
                                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                                    // 合计 用外循环的计量比较值 和本次  此处和数量的写法不一样  原有的比较值踢出这次的材料合计值
                                    meaSItem_AddUpAmt_TotalAmtOfAllItem = meaSItem_AddUpAmt_TotalAmtOfAllItem.subtract(
                                            ToolUtil.getBdIgnoreNull(bdSubcttCttAmtTotal));
                                    meaSCstplItem_AddUpAmt_TotalAmtOfAllItem = meaSCstplItem_AddUpAmt_TotalAmtOfAllItem.subtract(
                                            ToolUtil.getBdIgnoreNull(bdSubcttCttAmtTotal));
//                                    break;
                                }
                            } else {
                                // 总包计量与分包结算值差
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(meaSItem_AddUpQty);
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(                    // 计量分包结算金额差
                                        meaSCstplItem_AddUpAmtTemp.add(bdSubcttCttAmtTotal));
                                tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(               // 计量成本分包结算金额差
                                        meaSCstplItem_AddUpAmtTemp.add(bdSubcttCttAmtTotal));

                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                                meaSItem_AddUpAmt_TotalAmtOfAllItem = meaSItem_AddUpAmt_TotalAmtOfAllItem.subtract(
                                        ToolUtil.getBdIgnoreNull(bdSubcttCttAmtTotal));
                                meaSCstplItem_AddUpAmt_TotalAmtOfAllItem = meaSCstplItem_AddUpAmt_TotalAmtOfAllItem.subtract(
                                        ToolUtil.getBdIgnoreNull(bdSubcttCttAmtTotal));
                            }
                        }
                    }
                } else {
                    fgtemp = "Y";

                }
                //  insertedFlag -false 说明  没有对应材料    fgtemp 没有本条与下条pkid不一致
                //insertedFlag -false  +fgtemp N
                //  没有匹配的材料数据,直接写入showlist
                if (insertedFlag.equals(false) && fgtemp.equals("N")) {
                    tkMeaCstplUnit.setMeaSItem_AddUpQty(tkMeaCstplUnit.getTkcttStlItem_AddUpQty());
                    tkMeaCstplUnit.setMeaSItem_AddUpAmt(tkMeaCstplUnit.getTkcttStlItem_AddUpAmt());
                    tkMeaCstplUnit.setMeaSCstplItem_AddUpAmt(tkMeaCstplUnit.getTkcttStlCstplItem_AddUpAmt());
                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                    // 不需要总合计值
                }
                //  重复的pkid ,最后一项之前数据置空  总合计使用的数据进行合计
                if (insertedFlag.equals(false) && fgtemp.equals("Y")) {
                    tkMeaCstplUnit.setMeaSItem_AddUpQty(null);
                    tkMeaCstplUnit.setMeaSItem_AddUpAmt(null);
                    tkMeaCstplUnit.setMeaSCstplItem_AddUpAmt(null);
                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                    // 总合计不需要
                }

            }
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setTkcttItem_Name("合计");
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setTkcttItem_CttAmt(tkcttItem_CttAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setTkcttStlItem_ThisStageAmt(tkcttStlItem_ThisStageAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setTkcttStlItem_AddUpAmt(tkcttStlItem_AddUpAmt__TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setCstplItem_Amt(cstplItem_Amt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setCstplTkcttItem_TotalAmt(cstplTkcttItem_TotalAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setTkcttStlCstplItem_ThisStageAmt(tkcttStlCstplItem_ThisStageAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setTkcttStlCstplItem_AddUpAmt(tkcttStlCstplItem_AddUpAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setSubcttStlItem_ThisStageAmt(subcttStlItem_ThisStageAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setSubcttStlItem_AddUpAmt(subcttStlItem_AddUpAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setMeaSItem_AddUpAmt(meaSItem_AddUpAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setMeaSCstplItem_AddUpAmt(meaSCstplItem_AddUpAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setSubcttStlItem_ThisStageMAmt(subcttStlItem_ThisStageMAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowForTotalAmtOfAllItem.setSubcttStlItem_AddUpMAmt(subcttStlItem_AddUpMAmt_TotalAmtOfAllItem);
            qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowForTotalAmtOfAllItem);
            // 将分析活动数据装填到Excel中
            qryTkMeaCSStlQShowListForExcel =new ArrayList<QryTkMeaCSStlQShow>();
            for(QryTkMeaCSStlQShow itemOfEsItemHieRelapTkctt: qryTkMeaCSStlQShowList){
                QryTkMeaCSStlQShow itemOfEsItemHieRelapTkcttTemp=
                        (QryTkMeaCSStlQShow) BeanUtils.cloneBean(itemOfEsItemHieRelapTkctt);
                itemOfEsItemHieRelapTkcttTemp.setTkcttItem_No(
                        ToolUtil.getIgnoreSpaceOfStr(itemOfEsItemHieRelapTkcttTemp.getTkcttItem_No()));
                qryTkMeaCSStlQShowListForExcel.add(itemOfEsItemHieRelapTkcttTemp);
            }
        if(qryTkMeaCSStlQShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryTkMeaCSStlQShowListForExcel", qryTkMeaCSStlQShowListForExcel);
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
        // 通过父层id查找它的孩子
        List<CttItem> subCttItemList =getEsItemListByLevelParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            String strCreatedByName= cttInfoService.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= cttInfoService.getUserName(itemUnit.getLastUpdBy());
            CttItemShow cttItemShowTemp = new CttItemShow(
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
                "",
                itemUnit.getSpareField()
            );
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), cttItemListPara, cttItemShowListPara);
        }
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsItemListByLevelParentPkid(String strLevelParentPkid,
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
    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
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

    public ProgStlItemTkMeaService getProgStlItemTkMeaService() {
        return progStlItemTkMeaService;
    }

    public void setProgStlItemTkMeaService(ProgStlItemTkMeaService progStlItemTkMeaService) {
        this.progStlItemTkMeaService = progStlItemTkMeaService;
    }
    /*智能字段End*/
}
