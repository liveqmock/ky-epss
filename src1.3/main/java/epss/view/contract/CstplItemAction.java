package epss.view.contract;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.common.enums.*;
import epss.repository.model.CstplInfo;
import epss.repository.model.CstplItem;
import epss.repository.model.TkcttItem;
import epss.repository.model.model_show.CstplItemShow;
import epss.service.*;
import epss.service.CstplInfoService;
import epss.view.common.EsCommon;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.*;

@ManagedBean
@ViewScoped
public class CstplItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplItemAction.class);
    @ManagedProperty(value = "#{cstplInfoService}")
    private CstplInfoService cstplInfoService;
    @ManagedProperty(value = "#{cstplItemService}")
    private CstplItemService cstplItemService;
    @ManagedProperty(value = "#{tkcttItemService}")
    private TkcttItemService tkcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    /*查询，提交用*/
    private CstplItemShow cstplItemShowSel;
    private CstplItemShow cstplItemShowAdd;
    private CstplItemShow cstplItemShowUpd;
    private CstplItemShow cstplItemShowDel;
    /*列表中选择一行*/
    private CstplItemShow cstplItemShowSelected;
    /*列表显示用*/
    private List<CstplItemShow> cstplItemShowList;

    /*所属号*/
    private String strCstplInfoPkid;

    /*提交类型*/
    private String strSubmitType;
    private String strMngNotFinishFlag;

    /*控制控件在画面上的可用与现实Start*/
    //显示的控制
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    /*控制控件在画面上的可用与现实End*/
    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        strCstplInfoPkid=parammap.get("strCstplInfoPkid").toString();
        resetAction();
        initData() ;
    }
    private void initData() {
        try{
            /*总包合同列表*/
            List<TkcttItem> tkcttItemList =new ArrayList<TkcttItem>();
            // 查找该成本计划对应的总包合同
            CstplInfo cstplInfoTemp = cstplInfoService.getCstplInfoByPkid(strCstplInfoPkid);
            strMngNotFinishFlag="true";
            if(cstplInfoTemp!=null&& EnumFlowStatus.FLOW_STATUS0.getCode().equals(cstplInfoTemp.getFlowStatus())) {
                strMngNotFinishFlag="false";
            }
            String strTkcttInfoPkidInCstplInfo= cstplInfoTemp.getTkcttInfoPkid();
            // 查找该成本计划对应的总包合同
            tkcttItemList = tkcttItemService.getTkcttItemListByTkcttInfoPkid(strTkcttInfoPkidInCstplInfo);
            List<CstplItemShow> cstplItemShowListTkctt =new ArrayList<>();
            recursiveByTkcttItemList("root", tkcttItemList, cstplItemShowListTkctt);
            cstplItemShowListTkctt =getItemOfEsItemHieRelapList_DoFromatNo(cstplItemShowListTkctt);

            /*成本计划列表*/
            List<CstplItem> cstplItemListCstpl = cstplItemService.getCstplListByCstplInfoPkid(strCstplInfoPkid);

            List<CstplItemShow> cstplItemShowListCstpl =new ArrayList<>();
            recursiveByCstplItemList("root", cstplItemListCstpl, cstplItemShowListCstpl);
            cstplItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(
                    cstplItemShowListCstpl) ;

            /*拼装列表*/
            List<CstplItemShow> cstplItemShowList_ForSort =new ArrayList<>();
            int i=0;
            for(CstplItemShow itemTkctt: cstplItemShowListTkctt){
                i++;
                Boolean insertedFlag=false ;
                String strFrontNoAndName="";
                CstplItemShow itemTkcttInsertItem;
                for(CstplItemShow itemCstpl: cstplItemShowListCstpl){
                    itemTkcttInsertItem=new CstplItemShow();
                    if(itemTkctt.getTkctt_Pkid().equals(itemCstpl.getCstpl_TkcttItemPkid())){
                        //总包合同
                        if(strFrontNoAndName.equals(itemTkctt.getTkctt_StrNo()+itemTkctt .getTkctt_Name())){
                            itemTkcttInsertItem.setTkctt_StrNo("") ;
                            itemTkcttInsertItem.setTkctt_Name("") ;
                            itemTkcttInsertItem.setTkctt_Remark("");
                            itemTkcttInsertItem.setTkctt_Pkid(cstplItemShowList_ForSort.size() +";"+itemTkctt.getTkctt_Pkid());
                            itemTkcttInsertItem.setTkctt_Unit("");
                            itemTkcttInsertItem.setTkctt_UnitPrice(null) ;
                            itemTkcttInsertItem.setTkctt_Qty(null) ;
                            itemTkcttInsertItem.setTkctt_Amt(null) ;
                        }else{
                            strFrontNoAndName=itemTkctt.getTkctt_StrNo()+itemTkctt .getTkctt_Name();
                            itemTkcttInsertItem.setTkctt_StrNo(itemTkctt.getTkctt_StrNo());
                            itemTkcttInsertItem.setTkctt_Name(itemTkctt.getTkctt_Name());
                            itemTkcttInsertItem.setTkctt_Remark(itemTkctt.getTkctt_Remark());
                            itemTkcttInsertItem.setTkctt_Pkid(itemTkctt.getTkctt_Pkid());
                            itemTkcttInsertItem.setTkctt_Unit(itemTkctt.getTkctt_Unit());
                            itemTkcttInsertItem.setTkctt_UnitPrice(itemTkctt.getTkctt_UnitPrice()) ;
                            itemTkcttInsertItem.setTkctt_Qty(itemTkctt.getTkctt_Qty()) ;
                            itemTkcttInsertItem.setTkctt_Amt(itemTkctt.getTkctt_Amt()) ;
                        }
                        itemTkcttInsertItem.setTkctt_Grade(itemTkctt.getTkctt_Grade());
                        itemTkcttInsertItem.setTkctt_Orderid(itemTkctt.getTkctt_Orderid()) ;
                        itemTkcttInsertItem.setTkctt_ArchivedFlag(itemTkctt.getTkctt_ArchivedFlag());
                        itemTkcttInsertItem.setTkctt_OriginFlag(itemTkctt.getTkctt_OriginFlag()) ;
                        itemTkcttInsertItem.setTkctt_CreatedBy(itemTkctt.getTkctt_CreatedBy());
                        itemTkcttInsertItem.setTkctt_CreatedTime(itemTkctt.getTkctt_CreatedTime());
                        itemTkcttInsertItem.setTkctt_UpdatedBy(itemTkctt.getTkctt_UpdatedBy());
                        itemTkcttInsertItem.setTkctt_UpdatedTime(itemTkctt.getTkctt_UpdatedTime());
                        itemTkcttInsertItem.setTkctt_RecVersion(itemTkctt.getTkctt_RecVersion());
                        //成本计划
                        itemTkcttInsertItem.setCstpl_StrNo(itemCstpl.getCstpl_StrNo());
                        itemTkcttInsertItem.setCstpl_Pkid(itemCstpl.getCstpl_Pkid());
                        itemTkcttInsertItem.setCstpl_CstplInfoPkid(itemCstpl.getCstpl_CstplInfoPkid());
                        itemTkcttInsertItem.setCstpl_ParentPkid(itemCstpl.getCstpl_ParentPkid());
                        itemTkcttInsertItem.setCstpl_Grade(itemCstpl.getCstpl_Grade());
                        itemTkcttInsertItem.setCstpl_Orderid(itemCstpl.getCstpl_Orderid());
                        itemTkcttInsertItem.setCstpl_Name(itemCstpl.getCstpl_Name());
                         itemTkcttInsertItem.setCstpl_Unit(itemCstpl.getCstpl_Unit());
                        itemTkcttInsertItem.setCstpl_UnitPrice(itemCstpl.getCstpl_UnitPrice());
                        itemTkcttInsertItem.setCstpl_Qty(itemCstpl.getCstpl_Qty());
                        itemTkcttInsertItem.setCstpl_Amt(itemCstpl.getCstpl_Amt());
                        itemTkcttInsertItem.setCstpl_ArchivedFlag(itemCstpl.getCstpl_ArchivedFlag());
                        itemTkcttInsertItem.setCstpl_OriginFlag(itemCstpl.getCstpl_OriginFlag());
                        itemTkcttInsertItem.setCstpl_CreatedBy(itemCstpl.getCstpl_CreatedBy());
                        itemTkcttInsertItem.setCstpl_CreatedTime(itemCstpl.getCstpl_CreatedTime());
                        itemTkcttInsertItem.setCstpl_UpdatedBy(itemCstpl.getCstpl_UpdatedBy());
                        itemTkcttInsertItem.setCstpl_UpdatedTime(itemCstpl.getCstpl_UpdatedTime());
                        itemTkcttInsertItem.setCstpl_RecVersion(itemCstpl.getCstpl_RecVersion());
                        itemTkcttInsertItem.setCstpl_TkcttItemPkid(itemCstpl.getCstpl_TkcttItemPkid());
                        itemTkcttInsertItem.setCstpl_Remark(itemCstpl.getCstpl_Remark());
                        if(ToolUtil.getStrIgnoreNull(itemTkcttInsertItem.getCstpl_Pkid()).equals("")){
                            itemTkcttInsertItem.setCstpl_Pkid(cstplItemShowList_ForSort.size() +"");
                        }
                        insertedFlag=true ;
                        cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                    }
                }
                if (insertedFlag.equals(false)){
                    itemTkcttInsertItem=(CstplItemShow)BeanUtils.cloneBean(itemTkctt);
                    cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                }
            }

            for(CstplItemShow itemCstpl: cstplItemShowListCstpl){
                Boolean insertedFlag=false ;
                for(CstplItemShow itemTkctt: cstplItemShowListTkctt){
                    if(itemTkctt.getTkctt_Pkid().equals(itemCstpl.getCstpl_TkcttItemPkid())){
                        insertedFlag=true;
                        break;
                    }
                }
                if(insertedFlag.equals(false)){
                    CstplItemShow itemTkcttInsertItem=(CstplItemShow)BeanUtils.cloneBean(itemCstpl);
                    if (ToolUtil.getStrIgnoreNull(itemTkcttInsertItem.getTkctt_Pkid()).equals("")){
                        itemTkcttInsertItem.setTkctt_Pkid(cstplItemShowList_ForSort.size() +"");
                    }
                    cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                }
            }

            for(CstplItemShow itemUnit: cstplItemShowList_ForSort){
                if(itemUnit.getCstpl_StrNo()!=null){
                    String strCstplTkcttItemNoTemp=ToolUtil.getStrIgnoreNull(itemUnit.getCstpl_TkcttItemNo());
                    CstplItemShow cstplItemShowTemp =
                            getItemOfTkcttAndCstplByPkid(strCstplTkcttItemNoTemp, cstplItemShowList_ForSort, "Tkctt");
                    if(cstplItemShowTemp !=null){
                        itemUnit.setCstpl_TkcttItemNo(cstplItemShowTemp.getCstpl_StrNo());
                    }
                } else{
                    itemUnit.setCstpl_TkcttItemNo(itemUnit.getCstpl_StrNo());
                }
            }

            cstplItemShowList =new ArrayList<CstplItemShow>();
            cstplItemShowList =getItemOfTkcttAndCstplListSorted(cstplItemShowList_ForSort,0);
            // 添加合计
            setItemOfTkcttAndCstplList_AddTotal();
        } catch (Exception e) {
            logger.error("初始化数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void resetAction(){
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cstplItemShowSel =new CstplItemShow();
        cstplItemShowSel.setCstpl_CstplInfoPkid(strCstplInfoPkid);
        cstplItemShowAdd =new CstplItemShow();
        cstplItemShowAdd.setCstpl_CstplInfoPkid(strCstplInfoPkid);
        cstplItemShowUpd =new CstplItemShow();
        cstplItemShowUpd.setCstpl_CstplInfoPkid(strCstplInfoPkid);
        cstplItemShowDel =new CstplItemShow();
        cstplItemShowDel.setCstpl_CstplInfoPkid(strCstplInfoPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cstplItemShowAdd =new CstplItemShow();
        cstplItemShowAdd.setCstpl_CstplInfoPkid(strCstplInfoPkid);
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cstplItemShowSel.getCstpl_Name()).equals("")){
            if(ToolUtil.getStrIgnoreNull(strSubmitType).equals("Upd")) {
                styleModelNo.setDisabled_Flag("true");
            }else{
                styleModelNo.setDisabled_Flag("false");
            }
        }
        return true;
    }
    public void selectRecordAction(String strSubmitTypePara){
        try {
            String  strSubmitTypeBefore= strSubmitType;
            strSubmitType=strSubmitTypePara;
            switch (strSubmitTypePara) {
                case "Sel" :
                    cstplItemShowSel =(CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
                    cstplItemShowSel.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSel.getCstpl_StrNo())) ;
                    cstplItemShowSel.setCstpl_TkcttItemNo(
                            ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSel.getCstpl_TkcttItemNo()));
                    break;
                case "Upd":
                    cstplItemShowUpd = (CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
                    cstplItemShowUpd.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowUpd.getCstpl_StrNo()));
                    cstplItemShowUpd.setCstpl_TkcttItemNo(
                            ToolUtil.getIgnoreSpaceOfStr(cstplItemShowUpd.getCstpl_TkcttItemNo()));
                    break;
                case "Del":
                    if(cstplItemShowSelected.getCstpl_StrNo()==null) {
                        MessageUtil.addInfo("没有可删除的数据！");
                        cstplItemShowDel =new CstplItemShow();
                        return;
                    }
                    else{
                        cstplItemShowDel =(CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
                    }
                    cstplItemShowDel.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowDel.getCstpl_StrNo())); ;
                    cstplItemShowDel.setCstpl_TkcttItemNo(
                            ToolUtil.getIgnoreSpaceOfStr(cstplItemShowDel.getCstpl_TkcttItemNo()));
                    break;
                case "From_tkctt_to_cstpl":
                    if(!fromTkcttToCstplAction()){
                        strSubmitType=strSubmitTypeBefore;
                    }
            }
        } catch (Exception e) {
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")) {
            if (cstplItemShowAdd.getCstpl_UnitPrice() == null || cstplItemShowAdd.getCstpl_Qty() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cstplItemShowAdd.getCstpl_UnitPrice().multiply(cstplItemShowAdd.getCstpl_Qty());
            }
            cstplItemShowAdd.setCstpl_Amt(bigDecimal);
        }
        if (strSubmitType.equals("Upd")) {
            if (cstplItemShowUpd.getCstpl_UnitPrice() == null || cstplItemShowUpd.getCstpl_Qty() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cstplItemShowUpd.getCstpl_UnitPrice().multiply(cstplItemShowUpd.getCstpl_Qty());
            }
            cstplItemShowUpd.setCstpl_Amt(bigDecimal);
        }
    }
    public Boolean blurStrNoToGradeAndOrderid(String strIsBlur){
        CstplItemShow cstplItemShowTemp =new CstplItemShow(strCstplInfoPkid);
        if (strSubmitType.equals("Add")){
            cstplItemShowTemp = cstplItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cstplItemShowTemp = cstplItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCstpl_StrNo());
        if(StringUtils .isEmpty(strIgnoreSpaceOfStr)){
            return true;
        }
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (!strIgnoreSpaceOfStr.matches(strRegex) ){
            MessageUtil.addError("请确认输入的编码，编码" + strIgnoreSpaceOfStr + "格式不正确！");
            return strNoBlurFalse();
        }

        //该编码已经存在
        if(!strSubmitType.equals("Upd")){
            if(getItemOfTkcttAndCstplByStrNo(strIgnoreSpaceOfStr, cstplItemShowList, "Cstpl")!=null){
                cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(
                        strIgnoreSpaceOfStr, cstplItemShowList, "Cstpl");

                String correspondingItemNoContrast= ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCstpl_TkcttItemNo());
                if(!ToolUtil.getStrIgnoreNull(cstplItemShowTemp.getCstpl_TkcttItemNo())
                        .equals(cstplItemShowTemp.getCstpl_TkcttItemNo())){
                    MessageUtil.addError("该编码(" + strIgnoreSpaceOfStr + ")，对应编码(" + correspondingItemNoContrast
                            + ")已经存在,您输入的对应编码（" + cstplItemShowTemp.getCstpl_TkcttItemNo() +
                            ")执行插入操作时会出现逻辑错误，将无法插入！");
                    return strNoBlurFalse();
                }
            }
        }

        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<CstplItemShow> cstplItemShowSubTemp =new ArrayList<CstplItemShow>();
            cstplItemShowSubTemp =getItemOfTkcttAndCstplByLevelParentPkid(
                    "root",
                    cstplItemShowList,"Cstpl");
            if(cstplItemShowSubTemp.size() ==0){
                if(!strIgnoreSpaceOfStr.equals("1") ){
                    MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入1！");
                    return strNoBlurFalse();
                }
            }
            else{
                if(cstplItemShowSubTemp.size() +1<Integer.parseInt(strIgnoreSpaceOfStr)){
                    MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + (cstplItemShowSubTemp.size() + 1) + "！");
                    return strNoBlurFalse();
                }
            }
            cstplItemShowTemp.setCstpl_Grade(1);
            cstplItemShowTemp.setCstpl_Orderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cstplItemShowTemp.setCstpl_ParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            cstplItemShowTemp =new CstplItemShow();
            cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList, "Cstpl");
            if(cstplItemShowTemp ==null|| cstplItemShowTemp.getTkctt_Pkid()==null){
                MessageUtil.addError("请确认输入的编码！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            }
            else{
                List<CstplItemShow> cstplItemShowSubTemp =new ArrayList<CstplItemShow>();
                cstplItemShowSubTemp =getItemOfTkcttAndCstplByLevelParentPkid(
                        cstplItemShowTemp.getCstpl_Pkid(),
                        cstplItemShowList, "Cstpl");
                if(cstplItemShowSubTemp.size() ==0){
                    if(!cstplItemShowTemp.getCstpl_StrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + strParentNo + ".1！");
                        return strNoBlurFalse();
                    }
                }
                else{
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(cstplItemShowSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + strParentNo + "." +
                                (cstplItemShowSubTemp.size() + 1) + "！");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[]= strIgnoreSpaceOfStr.split("\\.");
                cstplItemShowTemp.setCstpl_Grade(strTemps.length) ;
                cstplItemShowTemp.setCstpl_Orderid(Integer.parseInt(strTemps[strTemps.length - 1]));
                cstplItemShowTemp.setCstpl_ParentPkid(cstplItemShowTemp.getCstpl_TkcttItemPkid()) ;
            }
        }
        if(strIsBlur.equals("true")){
            cstplItemShowTemp =
                    getItemOfTkcttAndCstplByStrNo(strIgnoreSpaceOfStr, cstplItemShowList,"Tkctt");
            if(cstplItemShowTemp !=null){
                cstplItemShowTemp.setCstpl_TkcttItemNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCstpl_StrNo())) ;
                cstplItemShowTemp.setCstpl_TkcttItemPkid(cstplItemShowTemp.getCstpl_Pkid());
            }else{
                cstplItemShowTemp.setCstpl_TkcttItemNo(null);
                cstplItemShowTemp.setCstpl_TkcttItemPkid(null);
            }
        }
        return true ;
    }

    public Boolean blurCorrespondingPkid(){
        CstplItemShow cstplItemShowTemp =new CstplItemShow(strCstplInfoPkid );
        if (strSubmitType.equals("Add")){
            cstplItemShowTemp = cstplItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cstplItemShowTemp = cstplItemShowUpd;
        }
        cstplItemShowTemp =
                getItemOfTkcttAndCstplByStrNo(cstplItemShowTemp.getCstpl_TkcttItemNo(), cstplItemShowList, "Tkctt");
        if(cstplItemShowTemp !=null && cstplItemShowTemp.getCstpl_StrNo() !=null){

            String strNo= cstplItemShowTemp.getCstpl_StrNo();
            if(strNo!=null&&!strNo.equals("")){
                CstplItemShow cstplTempForCstplItemShow =getItemOfTkcttAndCstplByStrNo(
                        strNo, cstplItemShowList, "Cstpl");
                if(cstplTempForCstplItemShow !=null) {
                    String correspondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(cstplTempForCstplItemShow.getCstpl_TkcttItemNo());
                    if(cstplItemShowTemp.getCstpl_TkcttItemNo()!=null&&
                            !correspondingItemNoContrast.equals(cstplItemShowTemp.getCstpl_TkcttItemNo())){
                        MessageUtil.addError("该编码(" + strNo + ")，对应编码(" + correspondingItemNoContrast
                                + ")已经存在,您输入的对应编码（" + cstplItemShowTemp.getCstpl_TkcttItemNo() +
                                ")执行插入操作时会出现逻辑错误，将无法插入！");
                        return strNoBlurFalse();
                    }
                }
            }
            cstplItemShowTemp.setCstpl_TkcttItemNo(cstplItemShowTemp.getCstpl_Pkid());
        }
        return true ;
    }
    public void delThisRecordAction() {
        try {
            Integer intItemUnitConstructSelectedGrade=-1;
            List<CstplItemShow> cstplItemShowListTemp =new ArrayList<>();
            cstplItemShowListTemp.addAll(cstplItemShowList);
            for(CstplItemShow itemUnitConstructItem: cstplItemShowListTemp){
                if(intItemUnitConstructSelectedGrade>-1){
                    if(ToolUtil.getIntIgnoreNull(itemUnitConstructItem .getCstpl_Grade())
                            <=intItemUnitConstructSelectedGrade){
                        break;
                    }
                    else{
                        cstplItemService.deleteRecord(itemUnitConstructItem.getCstpl_Pkid()) ;
                    }
                }
                if(intItemUnitConstructSelectedGrade==-1){
                    if(itemUnitConstructItem.equals(cstplItemShowSelected) ){
                        intItemUnitConstructSelectedGrade=itemUnitConstructItem.getCstpl_Grade();
                        int deleteRecordNumOfSelf= cstplItemService.deleteRecord(itemUnitConstructItem.getCstpl_Pkid()) ;
                        if (deleteRecordNumOfSelf<=0){
                            MessageUtil.addInfo("该记录已删除。");
                            return;
                        }
                    }
                }
            }
            cstplItemService.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                    cstplItemShowSelected.getCstpl_CstplInfoPkid(),
                    cstplItemShowSelected.getCstpl_ParentPkid(),
                    cstplItemShowSelected.getCstpl_Grade());
            initData();
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void submitThisRecordAction(){
        try{
            /*提交前的检查*/
            if(strSubmitType .equals("Del")) {
                delThisRecordAction();
                return;
            }
            if(!subMitActionPreCheck()){
                return ;
            }
            /*编码验证*/
            if(!blurStrNoToGradeAndOrderid("false")){
                return ;
            }
            /*对应编码验证*/
            if(!blurCorrespondingPkid()){
                return ;
            }
            if(strSubmitType .equals("Upd")) {
                Integer intRecVersion=ToolUtil.getIntIgnoreNull(cstplItemShowUpd.getCstpl_RecVersion());
                cstplItemShowUpd.setCstpl_RecVersion(intRecVersion + 1);
                cstplItemService.updateRecord(cstplItemShowUpd) ;
            }
            else if(strSubmitType .equals("Add")) {
                CstplItem cstplItemTemp = cstplItemService.fromShowModelToModel(cstplItemShowAdd);
                if (cstplItemService.isExistSameRecordInDb(cstplItemTemp)){
                    MessageUtil.addInfo("该编号对应记录已存在，请重新录入。");
                    return;
                }
                cstplItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        cstplItemShowAdd.getCstpl_CstplInfoPkid(),
                        cstplItemShowAdd.getCstpl_ParentPkid(),
                        cstplItemShowAdd.getCstpl_Grade());
                cstplItemService.insertRecord(cstplItemShowAdd);
            }
            MessageUtil.addInfo("提交数据完成。");
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }
    /*提交前的检查：必须项的输入*/
    private Boolean subMitActionPreCheck(){
        CstplItemShow cstplItemShowTemp =new CstplItemShow(strCstplInfoPkid);
        if (strSubmitType.equals("Add")){
            cstplItemShowTemp = cstplItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cstplItemShowTemp = cstplItemShowUpd;
        }
        if (StringUtils.isEmpty(cstplItemShowTemp.getCstpl_StrNo())) {

            MessageUtil.addError("请输入编号！");
            return false;
        }
        if (StringUtils.isEmpty(cstplItemShowTemp.getCstpl_Name())) {
            MessageUtil.addError("请输入名称！");
            return false;
        }
        return true;
    }

    private void setItemOfTkcttAndCstplList_AddTotal(){
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        cstplItemShowListTemp.addAll(cstplItemShowList);

        cstplItemShowList.clear();
        // 小计
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        BigDecimal bdTotalCstpl=new BigDecimal(0);
        BigDecimal bdAllTotalCstpl=new BigDecimal(0);

        CstplItemShow itemUnit=new CstplItemShow();
        CstplItemShow itemUnitNext=new CstplItemShow();
        for(int i=0;i< cstplItemShowListTemp.size();i++){
            itemUnit = cstplItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));
            // 对照
            bdTotalCstpl=bdTotalCstpl.add(ToolUtil.getBdIgnoreNull(itemUnit.getCstpl_Amt()));
            bdAllTotalCstpl=bdAllTotalCstpl.add(ToolUtil.getBdIgnoreNull(itemUnit.getCstpl_Amt()));
            cstplItemShowList.add(itemUnit);

            if(i+1< cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i+1);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                Boolean isRoot=false;
                if(ToolUtil.getStrIgnoreNull(itemUnitNext.getCstpl_ParentPkid()).equals("root")){
                    cstplItemShowTemp.setTkctt_Name("合计");
                    cstplItemShowTemp.setTkctt_Pkid("total"+i);
                    cstplItemShowTemp.setTkctt_Amt(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

                if(ToolUtil.getStrIgnoreNull(itemUnitNext.getCstpl_ParentPkid()).equals("root")){
                    cstplItemShowTemp.setTkctt_Pkid("total"+i);
                    cstplItemShowTemp.setCstpl_Name("合计");
                    cstplItemShowTemp.setCstpl_Pkid("total_contrast"+i);
                    cstplItemShowTemp.setCstpl_Amt(bdTotalCstpl);
                    bdTotalCstpl = new BigDecimal(0);
                    isRoot=true;
                }

                if(isRoot.equals(true)){
                    cstplItemShowList.add(cstplItemShowTemp);
                }
            } else if(i+1== cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setTkctt_Name("合计");
                cstplItemShowTemp.setTkctt_Pkid("total"+i);
                cstplItemShowTemp.setTkctt_Amt(bdTotal);

                cstplItemShowTemp.setCstpl_Name("合计");
                cstplItemShowTemp.setCstpl_Pkid("total_contrast"+i);
                cstplItemShowTemp.setCstpl_Amt(bdTotalCstpl);
                cstplItemShowList.add(cstplItemShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalCstpl = new BigDecimal(0);

                // 总合计
                cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setTkctt_Name("总合计");
                cstplItemShowTemp.setTkctt_Pkid("total_all"+i);
                cstplItemShowTemp.setTkctt_Amt(bdAllTotal);

                cstplItemShowTemp.setCstpl_Name("总合计");
                cstplItemShowTemp.setCstpl_Pkid("total_all_contrast"+i);
                cstplItemShowTemp.setCstpl_Amt(bdAllTotalCstpl);
                cstplItemShowList.add(cstplItemShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalCstpl = new BigDecimal(0);
            }
        }
    }
    private boolean fromTkcttToCstplAction(){
        try{
            if(cstplItemShowSelected.getCstpl_StrNo() ==null||
                    cstplItemShowSelected.getCstpl_StrNo().equals("")) {
                MessageUtil.addInfo("无可复制！");
                return false;
            }
            if(!ToolUtil.getStrIgnoreNull(cstplItemShowSelected.getCstpl_StrNo()).equals("")) {
                MessageUtil.addInfo("成本计划项不为空，总包合同到成本计划的项无法复制，如需复制，请先删除该成本计划项！");
                return false;
            }
            String strIgnoreSpaceOfStr=ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSelected.getCstpl_StrNo());
            String strNoSplited[]  = strIgnoreSpaceOfStr.split("\\.") ;
            String strTemp="";
            for(int i=0;i<strNoSplited.length-1 ;i++) {
                if(i==0) {
                    strTemp+=strNoSplited[i] ;
                }
                else{
                    strTemp+="."+ strNoSplited[i] ;
                }
                CstplItemShow cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strTemp, cstplItemShowList,"Cstpl");
                if(cstplItemShowTemp ==null){
                    MessageUtil.addInfo("编码（" + strTemp + "）数据不存在，请先建立（" + strTemp + "）的数据！");
                    return false;
                }
            }
            cstplItemShowSel = (CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
            cstplItemShowSel.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSel.getCstpl_StrNo())) ;
            cstplItemShowSel.setCstpl_CstplInfoPkid(strCstplInfoPkid);
            Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");
            if(intLastIndexof>0){
                String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
                CstplItemShow cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList,"Cstpl");
                cstplItemShowSel.setCstpl_ParentPkid(cstplItemShowTemp.getCstpl_Pkid());
                cstplItemShowSel.setCstpl_Grade(cstplItemShowTemp.getCstpl_Grade() + 1);
            }else{
                cstplItemShowSel.setCstpl_ParentPkid("root");
            }

            cstplItemShowSel.setCstpl_TkcttItemPkid(cstplItemShowSel.getCstpl_Pkid());
            cstplItemShowSel.setCstpl_TkcttItemNo(cstplItemShowSel.getCstpl_StrNo()) ;
            submitThisRecordAction();
            initData();
            return true;
        }catch (Exception e){
            logger.error("总包合同到成本计划的项复制失败，", e);
            MessageUtil.addError("总包合同到成本计划的项复制失败，" + e.getMessage());
            return false ;
        }
    }
    private List<CstplItemShow> getItemOfTkcttAndCstplListSorted(List<CstplItemShow> listPara,int i){
        List<CstplItemShow> modelList=new ArrayList<CstplItemShow>();
        modelList.addAll(listPara);
        //ct用于统计当前外层循环时元素的数量;ct1用于统计当前内层循环中符合外层循环中对应元素的数量;
        String strThisCorrespondingItemNoContrast;
        String strThisCorrespondingItemNoContrastAfter;
        for(;i<modelList.size();i++){//外层循环实现对整个list的遍历
            int startI=i;            //保存当前元素的初始位置i,因为在内层循环中会改变i的值
            int countParent=0;       //统计和当前元素（即父元素）相同的元素个数，父元素只能是顺序相同
            int countChild=0;        //统计匹配父元素的孩子元素的个数，孩子元素也是顺序出现
            int positionOfChild=0;   //记录下和父元素匹配的第一个孩子的位置
            strThisCorrespondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(modelList.get(i).getCstpl_TkcttItemNo());
            for(int j=i+1; j<modelList.size();j++){
                strThisCorrespondingItemNoContrastAfter=ToolUtil.getIgnoreSpaceOfStr(modelList.get(j).getCstpl_TkcttItemNo());
                if(strThisCorrespondingItemNoContrast.equals(strThisCorrespondingItemNoContrastAfter)){
                //判断内层循环中是否有和外层循环中的元素相同的元素，并记录下来保存在countParent
                    if(countParent==0){
                    //如果内层循环中存在和外层中相同的元素，并且是第一次遇到，由于countParent初值设置为0，
                        countParent+=2;
                        //故首次碰到时内层中需要将countParent+2
                        continue;
                    }
                    ++countParent;
                }
                else{//else下分为三种情况
                    if(countParent==0||!strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast))
                        break;
                        //1.内层中没有和外层中元素相同的元素，由已知条件的list知，这种情况直接退出内层循环即可；
                        //2.内层中有和外层中元素相同的元素，但是内层中没有其孩子，由已知条件的list知，这种情况直接退出内层循环即可
                    if((strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast)&&
                            strThisCorrespondingItemNoContrastAfter.compareTo(strThisCorrespondingItemNoContrast)>0)){
                        if(positionOfChild==0){
                            //内层中有和外层中元素相同的元素，
                            positionOfChild=j;
                            //记录下第一个孩子的位置保存在positionOfChild中。
                            countChild+=1;
                            //内层中有和外层中元素相同的元素，并且还有其孩子，则统计其孩子的个数，保存在countChild，由于countParent初值设置为0，故内层中需要将countParent+1
                            continue;
                        }
                        ++countChild;
                    }
                    if(countChild!=0)//对孩子采用递归思想
                        modelList=getItemOfTkcttAndCstplListSorted(modelList, positionOfChild);
                        //递归后得到，匹配父元素的孩子的正确排序
                }
            }
            if(countParent!=0&&countChild!=0){//根据之前记录下来的数据交换排序
                for(int x=1;x<=countChild;x++){
                    CstplItemShow m=modelList.get(positionOfChild);//保存要移动的孩子元素的值
                    modelList.remove(positionOfChild);     //移除孩子元素，
                    modelList.add(i+1, m);                 //将孩子插入父元素下
                    ++i;                                   //下一个要插入的孩子的位置
                    ++positionOfChild;
                }
                --countParent;
            }
            i=startI+countParent+countChild;//如果进行过排序后，则外层循环中i的值必定发生变化，
        }                                   //变换后的i的值=开始时用变量startI记录下来的最初i值+父元素个数+匹配父元素的子元素个数
        return modelList;
    }
    /*递归排序*/
    private void recursiveByTkcttItemList(String strLevelParentId,
                                            List<TkcttItem> tkcttItemListPara,
                                            List<CstplItemShow> cstplItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<TkcttItem> tkcttItemListTemp =new ArrayList<TkcttItem>();
        // 通过父层id查找它的孩子
        tkcttItemListTemp =getTkcttItemListByLevelParentPkid(strLevelParentId, tkcttItemListPara);
        for(TkcttItem itemUnit: tkcttItemListTemp){
            CstplItemShow cstplItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strUpdatedByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
            // 层级项
            cstplItemShowTemp = new CstplItemShow(
                itemUnit.getPkid(),
                itemUnit.getTkcttInfoPkid(),
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
                strUpdatedByName,
                itemUnit.getUpdatedTime(),
                itemUnit.getRecVersion(),
                itemUnit.getRemark(),
                itemUnit.getTid()
            );
            cstplItemShowListPara.add(cstplItemShowTemp) ;
            recursiveByTkcttItemList(cstplItemShowTemp.getCstpl_Pkid(), tkcttItemListPara, cstplItemShowListPara);
        }
    }
    private void recursiveByCstplItemList(
            String strLevelParentId,
            List<CstplItem> cstplItemListPara,
            List<CstplItemShow> cstplItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CstplItem> cstplItemList =new ArrayList<CstplItem>();
        // 通过父层id查找它的孩子
        cstplItemList =getCstplItemListByLevelParentPkid(strLevelParentId, cstplItemListPara);
        for(CstplItem itemUnit: cstplItemList){
            CstplItemShow cstplItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strUpdatedByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
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
                    strUpdatedByName,
                    itemUnit.getUpdatedTime(),
                    itemUnit.getRecVersion(),
                    itemUnit.getRemark(),
                    itemUnit.getTkcttItemPkid(),
                    "",
                    itemUnit.getTid()
            );
            cstplItemShowListPara.add(cstplItemShowTemp) ;
            recursiveByCstplItemList(cstplItemShowTemp.getCstpl_Pkid(), cstplItemListPara, cstplItemShowListPara);
        }
    }
    /*不符合规范的编码输入并离开时,清空相应的对象*/
    private Boolean strNoBlurFalse(){
        cstplItemShowSel.setCstpl_Pkid("") ;
        cstplItemShowSel.setCstpl_ParentPkid("");
        cstplItemShowSel.setCstpl_Grade(null);
        cstplItemShowSel.setCstpl_Orderid(null);
        return false;
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<TkcttItem> getTkcttItemListByLevelParentPkid(
            String strLevelParentPkid,
            List<TkcttItem> tkcttItemListPara) {
        List<TkcttItem> tkcttItemListTemp =new ArrayList<TkcttItem>();
        /*避开重复链接数据库*/
        for(TkcttItem itemUnit: tkcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tkcttItemListTemp.add(itemUnit);
            }
        }
        return tkcttItemListTemp;
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CstplItem> getCstplItemListByLevelParentPkid(
            String strLevelParentPkid,
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
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CstplItemShow> getItemOfTkcttAndCstplByLevelParentPkid(
            String strLevelParentPkid,
            List<CstplItemShow> esCstplItemShowListPara
            ,String strTkcttOrCstpl) {
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        /*避开重复链接数据库*/
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getCstpl_ParentPkid()).equalsIgnoreCase(strLevelParentPkid)){
                        cstplItemShowListTemp.add(itemUnit);
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getCstpl_ParentPkid()).equalsIgnoreCase(strLevelParentPkid)){
                        cstplItemShowListTemp.add(itemUnit);
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cstplItemShowListTemp;
    }
    /*通过编号得到该项*/
    private CstplItemShow getItemOfTkcttAndCstplByStrNo(
            String strNo,
            List<CstplItemShow> cstplItemShowListPara,
            String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getCstpl_StrNo()).equals(strNo)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getCstpl_StrNo()).equals(strNo)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cstplItemShowTemp;
    }
    /*通过Pkid得到该项*/
    private CstplItemShow getItemOfTkcttAndCstplByPkid(
            String strPkid,
            List<CstplItemShow> cstplItemShowListPara,
            String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getTkctt_Pkid()).equals(strPkid)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getCstpl_Pkid()).equals(strPkid)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cstplItemShowTemp;
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<CstplItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CstplItemShow> cstplItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CstplItemShow itemUnit: cstplItemShowListPara){
            if(itemUnit.getCstpl_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0){
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

    /*智能字段Start*/
    public CstplItemService getCstplItemService() {
        return cstplItemService;
    }

    public void setCstplItemService(CstplItemService cstplItemService) {
        this.cstplItemService = cstplItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CstplInfoService getCstplInfoService() {
        return cstplInfoService;
    }

    public void setCstplInfoService(CstplInfoService cstplInfoService) {
        this.cstplInfoService = cstplInfoService;
    }

    public List<CstplItemShow> getCstplItemShowList() {
        return cstplItemShowList;
    }

    public void setCstplItemShowList(List<CstplItemShow> cstplItemShowList) {
        this.cstplItemShowList = cstplItemShowList;
    }

    public CstplItemShow getCstplItemShowSelected() {
        return cstplItemShowSelected;
    }

    public void setCstplItemShowSelected(CstplItemShow cstplItemShowSelected) {
        this.cstplItemShowSelected = cstplItemShowSelected;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }
    public StyleModel getStyleModelNo() {
        return styleModelNo;
    }
    public StyleModel getStyleModel() {
        return styleModel;
    }
    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public CstplItemShow getCstplItemShowAdd() {
        return cstplItemShowAdd;
    }

    public void setCstplItemShowAdd(CstplItemShow cstplItemShowAdd) {
        this.cstplItemShowAdd = cstplItemShowAdd;
    }

    public CstplItemShow getCstplItemShowDel() {
        return cstplItemShowDel;
    }

    public void setCstplItemShowDel(CstplItemShow cstplItemShowDel) {
        this.cstplItemShowDel = cstplItemShowDel;
    }

    public CstplItemShow getCstplItemShowUpd() {
        return cstplItemShowUpd;
    }

    public void setCstplItemShowUpd(CstplItemShow cstplItemShowUpd) {
        this.cstplItemShowUpd = cstplItemShowUpd;
    }

    public CstplItemShow getCstplItemShowSel() {
        return cstplItemShowSel;
    }

    public void setCstplItemShowSel(CstplItemShow cstplItemShowSel) {
        this.cstplItemShowSel = cstplItemShowSel;
    }

    public TkcttItemService getTkcttItemService() {
        return tkcttItemService;
    }

    public void setTkcttItemService(TkcttItemService tkcttItemService) {
        this.tkcttItemService = tkcttItemService;
    }
/*智能字段End*/
}