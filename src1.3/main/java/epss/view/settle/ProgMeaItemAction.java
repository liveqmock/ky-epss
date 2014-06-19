package epss.view.settle;

import epss.common.enums.EnumFlowStatus;
import epss.repository.model.model_show.CttInfoShow;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgMeaItemShow;
import epss.service.*;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
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
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{tkcttItemService}")
    private TkcttItemService tkcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progMeaInfoService}")
    private ProgMeaInfoService progMeaInfoService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;

    private List<ProgMeaItemShow> progMeaItemShowList;
    private ProgMeaItemShow progMeaItemShow;
    private ProgMeaItemShow progMeaItemShowUpd;
    private ProgMeaItemShow progMeaItemShowDel;
    private ProgMeaItemShow progMeaItemShowSelected;

    private BigDecimal bdProgMea_AddUpQtyInDB;
    private BigDecimal bdProgMea_ThisStageQtyInDB;

    private CttInfoShow cttInfoShow;

    /*所属号*/
    private String strProgMeaInfoPkid;
    private ProgMeaInfo progMeaInfo;
    private String strSubmitType;
    private String strMngNotFinishFlag;

    private Map beansMap;
    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    private List<ProgMeaItemShow> progMeaItemShowListForExcel;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cttInfoShow =new CttInfoShow();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strProgMeaInfoPkid")){
            strProgMeaInfoPkid=parammap.get("strProgMeaInfoPkid").toString();
            this.progMeaInfo = progMeaInfoService.selectRecordsByPrimaryKey(strProgMeaInfoPkid);
        }

        strMngNotFinishFlag="true";
        if(progMeaInfo!=null&&EnumFlowStatus.FLOW_STATUS0.getCode().equals(progMeaInfo.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }

        resetAction();
        initData();

        /*总包合同数据*/
        ProgMeaInfo progMeaInfo = progMeaInfoService.selectRecordsByPrimaryKey(strProgMeaInfoPkid);
        TkcttInfo tkcttInfoTemp=tkcttInfoService.getTkcttInfoByPkid(progMeaInfo.getTkcttInfoPkid()) ;
        cttInfoShow.setStrTkcttInfoId(tkcttInfoTemp.getId());
        cttInfoShow.setStrTkcttInfoName(tkcttInfoTemp.getName());
        cttInfoShow.setStrStlId(progMeaInfo.getId());
        String strTkcttSignPartName=signPartService.getEsInitCustByPkid(tkcttInfoTemp.getSignPartPkidB()).getName();
        cttInfoShow.setStrTkcttSignPartNameB(strTkcttSignPartName);
        beansMap.put("cttInfoShow", cttInfoShow);
    }
    /*重置*/
    public void resetAction(){
        progMeaItemShow =new ProgMeaItemShow();
        progMeaItemShowUpd =new ProgMeaItemShow();
        progMeaItemShowDel =new ProgMeaItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurProgMea_ThisStageQty("submit")){
                    MessageUtil.addInfo("请录入本期工程量。");
                    return;
                }
                List<ProgMeaItem> progMeaItemListTemp =
                        progMeaItemService.isExistInDb(progMeaItemShowUpd);
                if (progMeaItemListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (progMeaItemListTemp.size() == 1) {
                    progMeaItemShowUpd.setProgMea_Pkid(progMeaItemListTemp.get(0).getPkid());
                    progMeaItemService.updateRecord(progMeaItemShowUpd);
                }
                if (progMeaItemListTemp.size()==0){
                    progMeaItemShowUpd.setProgMea_TkcttItemPkid(progMeaItemShowUpd.getTkctt_Pkid());
                    progMeaItemShowUpd.setProgMea_PorgMeaInfoPkid(progMeaItemShowUpd.getProgMea_PorgMeaInfoPkid());
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

    public boolean blurProgMea_ThisStageQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getTkctt_Qty()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            BigDecimal bdProgMea_ThisStageQtyTemp=
                    ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getProgMea_ThisStageQty());
            BigDecimal bigDecimalTemp=
                    bdProgMea_AddUpQtyInDB.add(bdProgMea_ThisStageQtyTemp).subtract(bdProgMea_ThisStageQtyInDB);
            BigDecimal bdTkctt_Qty=
                    ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getTkctt_Qty());

            String strTemp=bdProgMea_ThisStageQtyTemp.toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!bdProgMea_ThisStageQtyTemp.toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bdTkctt_Qty)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bdProgMea_ThisStageQtyTemp.toString() + "）！");
                    return false;
                }
                if(!ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getProgMea_ThisStageQty()).equals( new BigDecimal(0))){
                    progMeaItemShowUpd.setProgMea_AddUpQty(bigDecimalTemp);
                }
                progMeaItemShowUpd.setProgMea_AddUpQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                if(ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getProgMea_ThisStageQty()).equals( new BigDecimal(0))){
                    return false;
                }
                BigDecimal bdProgMeaQMng_AddUpQtyTemp=
                        ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getProgMea_AddUpQty());

                if(bdProgMeaQMng_AddUpQtyTemp.compareTo(bdProgMea_AddUpQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bdTkctt_Qty)>0){
                        MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                                + bdProgMea_ThisStageQtyTemp.toString() + "）！");
                        return false;
                    }
                }
                progMeaItemShowUpd.setProgMea_ThisStageAmt(progMeaItemShowUpd.getTkctt_UnitPrice().multiply(bdProgMea_ThisStageQtyTemp));
                progMeaItemShowUpd.setProgMea_AddUpAmt(progMeaItemShowUpd.getTkctt_UnitPrice().multiply(bigDecimalTemp));
            }
        }
        return true;
    }

    private void delRecordAction(ProgMeaItemShow progMeaItemShowPara){
        try {
            if(progMeaItemShowPara.getProgMea_Pkid()==null||
                    progMeaItemShowPara.getProgMea_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum= progMeaItemService.deleteRecord(progMeaItemShowPara.getProgMea_Pkid()) ;
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
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){
                progMeaItemShow =(ProgMeaItemShow)BeanUtils.cloneBean(progMeaItemShowSelected) ;
                progMeaItemShow.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMeaItemShow.getTkctt_StrNo()));
            }
            String strTkctt_Unit= progMeaItemShowSelected.getTkctt_Unit();
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
                progMeaItemShowUpd =(ProgMeaItemShow) BeanUtils.cloneBean(progMeaItemShowSelected) ;
                progMeaItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMeaItemShowUpd.getTkctt_StrNo()));
                progMeaItemShowUpd.setProgMea_PorgMeaInfoPkid(strProgMeaInfoPkid);
                progMeaItemShowUpd.setProgMea_TkcttItemPkid(progMeaItemShowUpd.getTkctt_Pkid());
                bdProgMea_ThisStageQtyInDB=ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getProgMea_ThisStageQty());
                bdProgMea_AddUpQtyInDB=
                        ToolUtil.getBdIgnoreNull(progMeaItemShowUpd.getProgMea_AddUpQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progMeaItemShowDel =(ProgMeaItemShow) BeanUtils.cloneBean(progMeaItemShowSelected) ;
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
        List<TkcttItem> tkcttItemListTemp =new ArrayList<TkcttItem>();
        progMeaItemShowListForExcel=new ArrayList<ProgMeaItemShow>();
        tkcttItemListTemp = tkcttItemService.getTkcttItemListByTkcttInfoPkid(progMeaInfo.getTkcttInfoPkid());
        if(tkcttItemListTemp.size()<=0){
            return;
        }
        progMeaItemShowList =new ArrayList<ProgMeaItemShow>();
        recursiveDataTable("root", tkcttItemListTemp, progMeaItemShowList);
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
                                      List<TkcttItem> cttItemListPara,
                                      List<ProgMeaItemShow> sProgMeaItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<TkcttItem> subTkcttItemList =new ArrayList<TkcttItem>();
        // 通过父层id查找它的孩子
        subTkcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, cttItemListPara);
        BigDecimal bdUnitPrice=new BigDecimal(0);
        BigDecimal bdThisStageQty=new BigDecimal(0);
        BigDecimal bdAddUpQty=new BigDecimal(0);
        for(TkcttItem itemUnit: subTkcttItemList){
            ProgMeaItemShow progMeaItemShowTemp = new ProgMeaItemShow();
            progMeaItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progMeaItemShowTemp.setTkctt_TkcttInfoPkid(itemUnit.getTkcttInfoPkid());
            progMeaItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progMeaItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progMeaItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progMeaItemShowTemp.setTkctt_Name(itemUnit.getName());
            progMeaItemShowTemp.setTkctt_Remark(itemUnit.getRemark());
            progMeaItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progMeaItemShowTemp.setTkctt_UnitPrice(itemUnit.getUnitPrice());
            bdUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getUnitPrice());
            progMeaItemShowTemp.setTkctt_Qty(itemUnit.getQty());
            progMeaItemShowTemp.setTkctt_Amt(itemUnit.getAmt());

            ProgMeaItem progMeaItem =new ProgMeaItem();
            progMeaItem.setProgMeaInfoPkid(progMeaInfo.getPkid());
            progMeaItem.setTkcttItemPkid(progMeaItemShowTemp.getTkctt_Pkid());

            List<ProgMeaItem> progMeaItemList =
                    progMeaItemService.getProgMeaItemListByModel(progMeaItem);
            if(progMeaItemList.size()>0){
                progMeaItem = progMeaItemList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(progMeaItem.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(progMeaItem.getUpdatedBy());
                progMeaItemShowTemp.setProgMea_Pkid(progMeaItem.getPkid());
                progMeaItemShowTemp.setProgMea_PorgMeaInfoPkid(progMeaItem.getProgMeaInfoPkid());
                progMeaItemShowTemp.setProgMea_TkcttItemPkid(progMeaItem.getTkcttItemPkid());
                progMeaItemShowTemp.setProgMea_ThisStageQty(progMeaItem.getThisStageQty());
                bdThisStageQty=ToolUtil.getBdIgnoreNull(progMeaItem.getThisStageQty());
                progMeaItemShowTemp.setProgMea_ThisStageAmt(bdUnitPrice.multiply(bdThisStageQty));
                progMeaItemShowTemp.setProgMea_AddUpQty(progMeaItem.getAddUpQty());
                bdAddUpQty=ToolUtil.getBdIgnoreNull(progMeaItem.getAddUpQty());
                progMeaItemShowTemp.setProgMea_AddUpAmt(bdUnitPrice.multiply(bdAddUpQty));
                progMeaItemShowTemp.setProgMea_ArchivedFlag(progMeaItem.getArchivedFlag());
                progMeaItemShowTemp.setProgMea_CreatedBy(progMeaItem.getCreatedBy());
                progMeaItemShowTemp.setProgMea_CreatedByName(strCreatedByName);
                progMeaItemShowTemp.setProgMea_CreatedTime(progMeaItem.getCreatedTime());
                progMeaItemShowTemp.setProgMea_UpdatedBy(progMeaItem.getUpdatedBy());
                progMeaItemShowTemp.setProgMea_UpdatedByName(strLastUpdByName);
                progMeaItemShowTemp.setProgMea_UpdatedTime(progMeaItem.getUpdatedTime());
                progMeaItemShowTemp.setProgMea_RecVersion(progMeaItem.getRecVersion());
            }
            sProgMeaItemShowListPara.add(progMeaItemShowTemp) ;
            recursiveDataTable(progMeaItemShowTemp.getTkctt_Pkid(), cttItemListPara, sProgMeaItemShowListPara);
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
        BigDecimal bdAddUpQtyTotal=new BigDecimal(0);
        // 开累数量大计
        BigDecimal bdAddUpQtyAllTotal=new BigDecimal(0);
        // 当期数量小计
        BigDecimal bdThisStageQtyTotal=new BigDecimal(0);
        // 当期数量大计
        BigDecimal bdThisStageQtyAllTotal=new BigDecimal(0);

        // 开累金额小计
        BigDecimal bdAddUpAmtTotal=new BigDecimal(0);
        // 开累金额大计
        BigDecimal bdAddUpAmtAllTotal=new BigDecimal(0);
        // 当期金额小计
        BigDecimal bdThisStageAmtTotal=new BigDecimal(0);
        // 当期金额大计
        BigDecimal bdThisStageAmtAllTotal=new BigDecimal(0);

        ProgMeaItemShow itemUnit=new ProgMeaItemShow();
        ProgMeaItemShow itemUnitNext=new ProgMeaItemShow();

        for(int i=0;i< progMeaItemShowListTemp.size();i++){
            itemUnit = progMeaItemShowListTemp.get(i);
            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Qty()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Qty()));
            bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));
            bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));

            bdAddUpQtyTotal=
                    bdAddUpQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_AddUpQty()));
            bdAddUpQtyAllTotal=
                    bdAddUpQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_AddUpQty()));
            bdThisStageQtyTotal=
                    bdThisStageQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_ThisStageQty()));
            bdThisStageQtyAllTotal=
                    bdThisStageQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_ThisStageQty()));

            bdAddUpAmtTotal=
                    bdAddUpAmtTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_AddUpAmt()));
            bdAddUpAmtAllTotal=
                    bdAddUpAmtAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_AddUpAmt()));
            bdThisStageAmtTotal=
                    bdThisStageAmtTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_ThisStageAmt()));
            bdThisStageAmtAllTotal=
                    bdThisStageAmtAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgMea_ThisStageAmt()));

            progMeaItemShowList.add(itemUnit);

            if(i+1< progMeaItemShowListTemp.size()){
                itemUnitNext = progMeaItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgMeaItemShow itemOfEsItemHieRelapTemp=new ProgMeaItemShow();
                    itemOfEsItemHieRelapTemp.setTkctt_Name("合计");
                    itemOfEsItemHieRelapTemp.setTkctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setTkctt_Qty(bdQuantityTotal);
                    itemOfEsItemHieRelapTemp.setTkctt_Amt(bdAmountTotal);
                    itemOfEsItemHieRelapTemp.setProgMea_AddUpQty(bdAddUpQtyTotal);
                    itemOfEsItemHieRelapTemp.setProgMea_ThisStageQty(bdThisStageQtyTotal);
                    itemOfEsItemHieRelapTemp.setProgMea_AddUpAmt(bdAddUpAmtTotal);
                    itemOfEsItemHieRelapTemp.setProgMea_ThisStageAmt(bdThisStageAmtTotal);
                    progMeaItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdAddUpQtyTotal=new BigDecimal(0);
                    bdThisStageQtyTotal=new BigDecimal(0);
                    bdAddUpAmtTotal=new BigDecimal(0);
                    bdThisStageAmtTotal=new BigDecimal(0);
                }
            } else if(i+1== progMeaItemShowListTemp.size()){
                itemUnitNext = progMeaItemShowListTemp.get(i);
                ProgMeaItemShow progMeaItemShowTemp =new ProgMeaItemShow();
                progMeaItemShowTemp.setTkctt_Name("合计");
                progMeaItemShowTemp.setTkctt_Pkid("total"+i);
                progMeaItemShowTemp.setTkctt_Qty(bdQuantityTotal);
                progMeaItemShowTemp.setTkctt_Amt(bdAmountTotal);

                progMeaItemShowTemp.setProgMea_AddUpQty(bdAddUpQtyTotal);
                progMeaItemShowTemp.setProgMea_ThisStageQty(bdThisStageQtyTotal);
                progMeaItemShowTemp.setProgMea_AddUpAmt(bdAddUpAmtTotal);
                progMeaItemShowTemp.setProgMea_ThisStageAmt(bdThisStageAmtTotal);
                progMeaItemShowList.add(progMeaItemShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdAddUpQtyTotal=new BigDecimal(0);
                bdThisStageQtyTotal=new BigDecimal(0);
                bdAddUpAmtTotal=new BigDecimal(0);
                bdThisStageAmtTotal=new BigDecimal(0);

                // 总合计
                progMeaItemShowTemp =new ProgMeaItemShow();
                progMeaItemShowTemp.setTkctt_Name("总合计");
                progMeaItemShowTemp.setTkctt_Pkid("total_all"+i);
                progMeaItemShowTemp.setTkctt_Qty(bdQuantityAllTotal);
                progMeaItemShowTemp.setTkctt_Amt(bdAmountAllTotal);

                progMeaItemShowTemp.setProgMea_AddUpQty(bdAddUpQtyAllTotal);
                progMeaItemShowTemp.setProgMea_ThisStageQty(bdThisStageQtyAllTotal);
                progMeaItemShowTemp.setProgMea_AddUpAmt(bdAddUpAmtAllTotal);
                progMeaItemShowTemp.setProgMea_ThisStageAmt(bdThisStageAmtAllTotal);
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

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<TkcttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
             List<TkcttItem> cttItemListPara) {
        List<TkcttItem> tempTkcttItemList =new ArrayList<TkcttItem>();
        /*避开重复链接数据库*/
        for(TkcttItem itemUnit: cttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempTkcttItemList.add(itemUnit);
            }
        }
        return tempTkcttItemList;
    }

    /* 智能字段Start*/

    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
    }

    public TkcttItemService getTkcttItemService() {
        return tkcttItemService;
    }

    public void setTkcttItemService(TkcttItemService tkcttItemService) {
        this.tkcttItemService = tkcttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public ProgMeaInfoService getProgMeaInfoService() {
        return progMeaInfoService;
    }

    public void setProgMeaInfoService(ProgMeaInfoService progMeaInfoService) {
        this.progMeaInfoService = progMeaInfoService;
    }

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService progMeaItemService) {
        this.progMeaItemService = progMeaItemService;
    }

    public List<ProgMeaItemShow> getProgMeaItemShowList() {
        return progMeaItemShowList;
    }

    public void setProgMeaItemShowList(List<ProgMeaItemShow> progMeaItemShowList) {
        this.progMeaItemShowList = progMeaItemShowList;
    }

    public ProgMeaItemShow getProgMeaItemShow() {
        return progMeaItemShow;
    }

    public void setProgMeaItemShow(ProgMeaItemShow progMeaItemShow) {
        this.progMeaItemShow = progMeaItemShow;
    }

    public ProgMeaItemShow getProgMeaItemShowUpd() {
        return progMeaItemShowUpd;
    }

    public void setProgMeaItemShowUpd(ProgMeaItemShow progMeaItemShowUpd) {
        this.progMeaItemShowUpd = progMeaItemShowUpd;
    }

    public ProgMeaItemShow getProgMeaItemShowDel() {
        return progMeaItemShowDel;
    }

    public void setProgMeaItemShowDel(ProgMeaItemShow progMeaItemShowDel) {
        this.progMeaItemShowDel = progMeaItemShowDel;
    }

    public ProgMeaItemShow getProgMeaItemShowSelected() {
        return progMeaItemShowSelected;
    }

    public void setProgMeaItemShowSelected(ProgMeaItemShow progMeaItemShowSelected) {
        this.progMeaItemShowSelected = progMeaItemShowSelected;
    }

    public BigDecimal getBdProgMea_AddUpQtyInDB() {
        return bdProgMea_AddUpQtyInDB;
    }

    public void setBdProgMea_AddUpQtyInDB(BigDecimal bdProgMea_AddUpQtyInDB) {
        this.bdProgMea_AddUpQtyInDB = bdProgMea_AddUpQtyInDB;
    }

    public BigDecimal getBdProgMea_ThisStageQtyInDB() {
        return bdProgMea_ThisStageQtyInDB;
    }

    public void setBdProgMea_ThisStageQtyInDB(BigDecimal bdProgMea_ThisStageQtyInDB) {
        this.bdProgMea_ThisStageQtyInDB = bdProgMea_ThisStageQtyInDB;
    }

    public CttInfoShow getCttInfoShow() {
        return cttInfoShow;
    }

    public void setCttInfoShow(CttInfoShow cttInfoShow) {
        this.cttInfoShow = cttInfoShow;
    }

    public String getStrProgMeaInfoPkid() {
        return strProgMeaInfoPkid;
    }

    public void setStrProgMeaInfoPkid(String strProgMeaInfoPkid) {
        this.strProgMeaInfoPkid = strProgMeaInfoPkid;
    }

    public ProgMeaInfo getProgMeaInfo() {
        return progMeaInfo;
    }

    public void setProgMeaInfo(ProgMeaInfo progMeaInfo) {
        this.progMeaInfo = progMeaInfo;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public void setStrMngNotFinishFlag(String strMngNotFinishFlag) {
        this.strMngNotFinishFlag = strMngNotFinishFlag;
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

    public List<ProgMeaItemShow> getProgMeaItemShowListForExcel() {
        return progMeaItemShowListForExcel;
    }

    public void setProgMeaItemShowListForExcel(List<ProgMeaItemShow> progMeaItemShowListForExcel) {
        this.progMeaItemShowListForExcel = progMeaItemShowListForExcel;
    }
 /*智能字段End*/
}
