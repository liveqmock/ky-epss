package epss.view.item;

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
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.CstplItemShow;
import epss.repository.model.model_show.CttItemShow;
import epss.repository.model.EsInitPower;
import epss.service.*;
import epss.service.EsCttInfoService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
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
public class ItemCstplAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemCstplAction.class);
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;

    /*查询，提交用*/
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    /*列表中选择一行*/
    private CstplItemShow cstplItemShowSelected;
    /*列表显示用*/
    private List<CstplItemShow> cstplItemShowList;

    /*所属类型*/
    private String strBelongToType;
    /*所属号*/
    private String strBelongToPkid;

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
        if  (parammap.containsKey("strType")){
            strBelongToType=parammap.get("strType").toString();
        }

        if(strBelongToType==null){
            return ;
        }
        strBelongToPkid=parammap.get("strCstplPkid").toString();

        List<EsInitPower> esInitPowerList=
                esInitPowerService.selectListByModel(strBelongToType,strBelongToPkid,"NULL");
        strMngNotFinishFlag="true";
        if(esInitPowerList.size()>0){
            if(esInitPowerList.get(0).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                strMngNotFinishFlag="false";
            }
        }
        resetAction();
        initData() ;
    }

    public void resetAction(){
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strBelongToPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cttItemShowSel.getName()).equals("")){
            Integer intIndex= esCommon.getIndexOfCstplItemNamelist(cttItemShowSel.getName());
            if(intIndex>=0){
                if(ToolUtil.getStrIgnoreNull(strSubmitType).equals("Upd")) {
                    styleModelNo.setDisabled_Flag("true");
                }else{
                    styleModelNo.setDisabled_Flag("false");
                }
                cttItemShowSel.setUnit(null);
                cttItemShowSel.setContractUnitPrice(null);
                cttItemShowSel.setContractQuantity(null);
                cttItemShowSel.setStrCorrespondingItemNo(null);
                cttItemShowSel.setCorrespondingPkid(null);
            }else{
                if(ToolUtil.getStrIgnoreNull(strSubmitType).equals("Upd")) {
                    styleModelNo.setDisabled_Flag("true");
                }else{
                    styleModelNo.setDisabled_Flag("false");
                }
            }
        }
        return true;
    }
    public void selectRecordAction(String strSubmitTypePara){
        try {
            String  strSubmitTypeBefore= strSubmitType;
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                CstplItemShow esInitSubcttTemp= cstplItemShowSelected;
                if(esInitSubcttTemp.getBelongToTypeContrast()==null) {
                    cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
                }
                else{
                    cttItemShowSel =getItemOfEsItemHieRelapByItem(esInitSubcttTemp,"Cstpl");
                }
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemShowUpd = getItemOfEsItemHieRelapByItem(cstplItemShowSelected, "Cstpl");

                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo()));
                cttItemShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrCorrespondingItemNo()));
                Integer intIndex = esCommon.getIndexOfCstplItemNamelist(cttItemShowUpd.getName());
            }
            else if(strSubmitTypePara.equals("Del")){
                if(cstplItemShowSelected.getStrNoContrast()==null) {
                    MessageUtil.addInfo("没有可删除的数据！");
                    cttItemShowDel =new CttItemShow(strBelongToType ,strBelongToPkid);
                    return;
                }
                else{
                    cttItemShowDel =getItemOfEsItemHieRelapByItem(cstplItemShowSelected,"Cstpl");
                }
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrCorrespondingItemNo()));
            }
            else if(strSubmitTypePara.equals("From_tkctt_to_cstpl")){
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
            if (cttItemShowAdd.getContractUnitPrice() == null || cttItemShowAdd.getContractQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cttItemShowAdd.getContractUnitPrice().multiply(cttItemShowAdd.getContractQuantity());
            }
            cttItemShowAdd.setContractAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")) {
            if (cttItemShowUpd.getContractUnitPrice() == null || cttItemShowUpd.getContractQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cttItemShowUpd.getContractUnitPrice().multiply(cttItemShowUpd.getContractQuantity());
            }
            cttItemShowUpd.setContractAmount(bigDecimal);
        }
    }
    public Boolean blurStrNoToGradeAndOrderid(String strIsBlur){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(cttItemShowTemp.getStrNo());
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
                CstplItemShow cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(
                        strIgnoreSpaceOfStr, cstplItemShowList, "Cstpl");

                String correspondingItemNoContrast= ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCorrespondingItemNoContrast());
                if(cttItemShowTemp.getStrCorrespondingItemNo()!=null&&
                        !correspondingItemNoContrast.equals(cttItemShowTemp.getStrCorrespondingItemNo())){
                    MessageUtil.addError("该编码(" + strIgnoreSpaceOfStr + ")，对应编码(" + correspondingItemNoContrast
                            + ")已经存在,您输入的对应编码（" + cttItemShowTemp.getStrCorrespondingItemNo() +
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
            cttItemShowTemp.setGrade(1);
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            CstplItemShow cstplItemShowTemp =new CstplItemShow();
            cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList, "Cstpl");
            if(cstplItemShowTemp ==null|| cstplItemShowTemp.getPkid()==null){
                MessageUtil.addError("请确认输入的编码！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            }
            else{
                List<CstplItemShow> cstplItemShowSubTemp =new ArrayList<CstplItemShow>();
                cstplItemShowSubTemp =getItemOfTkcttAndCstplByLevelParentPkid(
                        cstplItemShowTemp.getPkidContrast(),
                        cstplItemShowList, "Cstpl");
                if(cstplItemShowSubTemp.size() ==0){
                    if(!cttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
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
                cttItemShowTemp.setGrade(strTemps.length) ;
                cttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                cttItemShowTemp.setParentPkid( cstplItemShowTemp.getPkidContrast()) ;
            }
        }
        if(strIsBlur.equals("true")){
            CstplItemShow cstplItemShowTemp =
                    getItemOfTkcttAndCstplByStrNo(strIgnoreSpaceOfStr, cstplItemShowList,"Tkctt");
            if(cstplItemShowTemp !=null){
                cttItemShowTemp.setStrCorrespondingItemNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getStrNo())) ;
                cttItemShowTemp.setCorrespondingPkid(cstplItemShowTemp.getPkid());
            }else{
                cttItemShowTemp.setStrCorrespondingItemNo(null) ;
                cttItemShowTemp.setCorrespondingPkid(null) ;
            }
        }
        return true ;
    }

    public Boolean blurCorrespondingPkid(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        CstplItemShow cstplItemShowTemp =
                getItemOfTkcttAndCstplByStrNo(cttItemShowTemp.getStrCorrespondingItemNo(), cstplItemShowList, "Tkctt");
        if(cstplItemShowTemp !=null && cstplItemShowTemp.getStrNo() !=null){

            String strNo= cttItemShowTemp.getStrNo();
            if(strNo!=null&&!strNo.equals("")){
                CstplItemShow cstplTempForCstplItemShow =getItemOfTkcttAndCstplByStrNo(
                        strNo, cstplItemShowList, "Cstpl");
                if(cstplTempForCstplItemShow !=null) {
                    String correspondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(cstplTempForCstplItemShow.getCorrespondingItemNoContrast());
                    if(cttItemShowTemp.getStrCorrespondingItemNo()!=null&&
                            !correspondingItemNoContrast.equals(cttItemShowTemp.getStrCorrespondingItemNo())){
                        MessageUtil.addError("该编码(" + strNo + ")，对应编码(" + correspondingItemNoContrast
                                + ")已经存在,您输入的对应编码（" + cttItemShowTemp.getStrCorrespondingItemNo() +
                                ")执行插入操作时会出现逻辑错误，将无法插入！");
                        return strNoBlurFalse();
                    }
                }
            }
            cttItemShowTemp.setCorrespondingPkid(cstplItemShowTemp.getPkid());
        }
        else{
            //MessageUtil.addError("请确认输入的对应编码，该编码不是正确的总包合同项编码！");
            //return strNoBlurFalse();
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
                    if((itemUnitConstructItem .getGradeContrast()==null?0:itemUnitConstructItem .getGradeContrast())
                            <=intItemUnitConstructSelectedGrade){
                        break;
                    }
                    else{
                        esCttItemService.deleteRecord(itemUnitConstructItem.getPkidContrast()) ;
                    }
                }
                if(intItemUnitConstructSelectedGrade==-1){
                    if(itemUnitConstructItem.equals(cstplItemShowSelected) ){
                        intItemUnitConstructSelectedGrade=itemUnitConstructItem.getGradeContrast();
                        int deleteRecordNumOfSelf=esCttItemService.deleteRecord(itemUnitConstructItem.getPkidContrast()) ;
                        if (deleteRecordNumOfSelf<=0){
                            MessageUtil.addInfo("该记录已删除。");
                            return;
                        }
                    }
                }
            }
            esCttItemService.setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(
                    cstplItemShowSelected.getBelongToTypeContrast(),
                    cstplItemShowSelected.getBelongToPkidContrast(),
                    cstplItemShowSelected.getParentPkidContrast(),
                    cstplItemShowSelected.getGradeContrast(),
                    cstplItemShowSelected.getOrderidContrast());
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
                Integer intModificationNum=
                        cttItemShowUpd.getModificationNum()==null?0: cttItemShowUpd.getModificationNum();
                cttItemShowUpd.setModificationNum(intModificationNum+1);
                esCttItemService.updateRecord(cttItemShowUpd) ;
            }
            else if(strSubmitType .equals("Add")) {
                EsCttItem esCttItemTemp=esCttItemService.fromConstructToModel(cttItemShowAdd);
                if (esCttItemService.isExistSameRecordInDb(esCttItemTemp)){
                    MessageUtil.addInfo("该编号对应记录已存在，请重新录入。");
                    return;
                }
                esCttItemService.setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(
                        cttItemShowAdd.getBelongToType(),
                        cttItemShowAdd.getBelongToPkid(),
                        cttItemShowAdd.getParentPkid(),
                        cttItemShowAdd.getGrade(),
                        cttItemShowAdd.getOrderid());
                esCttItemService.insertRecord(cttItemShowAdd);
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
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        if (StringUtils.isEmpty(cttItemShowTemp.getStrNo())) {

            MessageUtil.addError("请输入编号！");
            return false;
        }
        if (StringUtils.isEmpty(cttItemShowTemp.getName())) {
            MessageUtil.addError("请输入名称！");
            return false;
        }
        return true;
    }

    private void initData() {
        /*总包合同列表*/
        List<EsCttItem> esCttItemListTkctt =new ArrayList<EsCttItem>();
        EsCttInfo esCttInfo = esCttInfoService.getCttInfoByPkId(strBelongToPkid);
        String strTkcttPkidInCstpl= esCttInfo.getParentPkid();
        esCttItemListTkctt =
                esCttItemService.getEsItemHieRelapListByTypeAndPkid(ESEnum.ITEMTYPE0.getCode(), strTkcttPkidInCstpl);
        List<CttItemShow> cttItemShowListTkctt =new ArrayList<>();
        recursiveDataTable("root", esCttItemListTkctt, cttItemShowListTkctt);
        cttItemShowListTkctt =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListTkctt);

        /*成本计划列表*/
        List<EsCttItem> esCttItemListCstpl = esCttItemService.getEsItemHieRelapListByTypeAndPkid(
                strBelongToType, strBelongToPkid);

        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", esCttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(
                cttItemShowListCstpl) ;

        /*拼装列表*/
        List<CstplItemShow> cstplItemShowList_ForSort =new ArrayList<>();
        int i=0;
        for(CttItemShow itemTkctt: cttItemShowListTkctt){
            i++;
            Boolean insertedFlag=false ;
            String strFrontNoAndName="";
            CstplItemShow itemTkcttInsertItem;
            for(CttItemShow itemCstpl: cttItemShowListCstpl){
                itemTkcttInsertItem=new CstplItemShow();
                if(itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())){
                    //总包合同
                    if(strFrontNoAndName.equals(itemTkctt.getStrNo()+itemTkctt .getName())){
                        itemTkcttInsertItem.setStrNo("") ;
                        itemTkcttInsertItem.setName("") ;
                        itemTkcttInsertItem.setNote("") ;
                        itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() +";"+itemTkctt.getPkid());
                        itemTkcttInsertItem.setUnit("");
                        itemTkcttInsertItem.setContractUnitPrice(null) ;
                        itemTkcttInsertItem.setContractQuantity(null) ;
                        itemTkcttInsertItem.setContractAmount(null) ;
                    }else{
                        strFrontNoAndName=itemTkctt.getStrNo()+itemTkctt .getName();
                        itemTkcttInsertItem.setStrNo(itemTkctt.getStrNo());
                        itemTkcttInsertItem.setName(itemTkctt.getName());
                        itemTkcttInsertItem.setNote(itemTkctt.getNote());
                        itemTkcttInsertItem.setPkid(itemTkctt.getPkid());
                        itemTkcttInsertItem.setUnit(itemTkctt.getUnit());
                        itemTkcttInsertItem.setContractUnitPrice(itemTkctt.getContractUnitPrice()) ;
                        itemTkcttInsertItem.setContractQuantity(itemTkctt.getContractQuantity()) ;
                        itemTkcttInsertItem.setContractAmount(itemTkctt.getContractAmount()) ;
                    }
                    itemTkcttInsertItem.setBelongToType(itemTkctt.getBelongToType()) ;
                    itemTkcttInsertItem.setBelongToPkid(itemTkctt.getBelongToPkid()) ;
                    itemTkcttInsertItem.setParentPkid(itemTkctt.getParentPkid());
                    itemTkcttInsertItem.setGrade(itemTkctt.getGrade());
                    itemTkcttInsertItem.setOrderid(itemTkctt.getOrderid()) ;
                    itemTkcttInsertItem.setCorrespondingPkid(itemTkctt .getCorrespondingPkid());

                    itemTkcttInsertItem.setSignPartAPrice(itemTkctt.getSignPartAPrice()) ;
                    itemTkcttInsertItem.setDeletedFlag(itemTkctt.getDeletedFlag());
                    itemTkcttInsertItem.setOriginFlag(itemTkctt.getOriginFlag()) ;
                    itemTkcttInsertItem.setCreatedBy(itemTkctt.getCreatedBy());
                    itemTkcttInsertItem.setCreatedDate(itemTkctt.getCreatedDate());
                    itemTkcttInsertItem.setLastUpdBy(itemTkctt.getLastUpdBy());
                    itemTkcttInsertItem.setLastUpdDate(itemTkctt.getLastUpdDate());
                    itemTkcttInsertItem.setModificationNum(itemTkctt.getModificationNum());
                    //成本计划
                    itemTkcttInsertItem.setStrNoContrast(itemCstpl.getStrNo()) ;
                    itemTkcttInsertItem.setPkidContrast(itemCstpl.getPkid());
                    itemTkcttInsertItem.setBelongToTypeContrast(itemCstpl.getBelongToType());
                    itemTkcttInsertItem.setBelongToPkidContrast(itemCstpl.getBelongToPkid());
                    itemTkcttInsertItem.setParentPkidContrast(itemCstpl.getParentPkid());
                    itemTkcttInsertItem.setGradeContrast(itemCstpl.getGrade());
                    itemTkcttInsertItem.setOrderidContrast(itemCstpl.getOrderid()) ;
                    itemTkcttInsertItem.setNameContrast(itemCstpl.getName());
                    itemTkcttInsertItem.setNoteContrast(itemCstpl.getNote());
                    itemTkcttInsertItem.setUnitContrast(itemCstpl.getUnit());
                    itemTkcttInsertItem.setContractUnitPriceContrast(itemCstpl.getContractUnitPrice()) ;
                    itemTkcttInsertItem.setContractQuantityContrast(itemCstpl.getContractQuantity()) ;
                    itemTkcttInsertItem.setContractAmountContrast(itemCstpl.getContractAmount()) ;
                    itemTkcttInsertItem.setSignPartAPriceContrast(itemCstpl.getSignPartAPrice()) ;
                    itemTkcttInsertItem.setDeletedFlagContrast(itemCstpl.getDeletedFlag());
                    itemTkcttInsertItem.setOriginFlagContrast(itemCstpl.getOriginFlag()) ;
                    itemTkcttInsertItem.setCreatedByContrast(itemCstpl.getCreatedBy());
                    itemTkcttInsertItem.setCreatedDateContrast(itemCstpl.getCreatedDate());
                    itemTkcttInsertItem.setLastUpdByContrast(itemCstpl.getLastUpdBy());
                    itemTkcttInsertItem.setLastUpdDateContrast(itemCstpl.getLastUpdDate());
                    itemTkcttInsertItem.setModificationNumContrast(itemCstpl.getModificationNum());
                    itemTkcttInsertItem.setCorrespondingPkidContrast(itemCstpl.getCorrespondingPkid());
                    if(itemTkcttInsertItem.getPkid() ==null||itemTkcttInsertItem.getPkid().equals("")){
                        itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() +"");
                    }
                    insertedFlag=true ;
                    cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                }
            }
            if (insertedFlag.equals(false)){
                itemTkcttInsertItem=new CstplItemShow();
                itemTkcttInsertItem=getItemOfTkcttAndCstplByItemOfEsItemHieRelap(itemTkctt,"Tkctt");
                cstplItemShowList_ForSort.add(itemTkcttInsertItem);
            }
        }

        for(CttItemShow itemCstpl: cttItemShowListCstpl){
            Boolean insertedFlag=false ;
            for(CttItemShow itemTkctt: cttItemShowListTkctt){
                if(itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())){
                    insertedFlag=true;
                    break;
                }
            }
            if(insertedFlag.equals(false)){
                CstplItemShow itemTkcttInsertItem=getItemOfTkcttAndCstplByItemOfEsItemHieRelap(itemCstpl,"Cstpl");
                if (itemTkcttInsertItem.getPkid() ==null||itemTkcttInsertItem.getPkid().equals("")){
                    itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() +"");
                }
                cstplItemShowList_ForSort.add(itemTkcttInsertItem);
            }
        }

        for(CstplItemShow itemUnit: cstplItemShowList_ForSort){
            if(itemUnit.getStrNoContrast()!=null){
                String correspondingItemNoContrast=itemUnit.getCorrespondingPkidContrast()==null?"":itemUnit.getCorrespondingPkidContrast();
                CstplItemShow cstplItemShowTemp =
                        getItemOfTkcttAndCstplByPkid(correspondingItemNoContrast, cstplItemShowList_ForSort, "Tkctt");
                if(cstplItemShowTemp !=null){
                    itemUnit.setCorrespondingItemNoContrast(cstplItemShowTemp.getStrNo());
                }
            } else{
                itemUnit.setCorrespondingItemNoContrast(itemUnit.getStrNo());
            }
        }

        cstplItemShowList =new ArrayList<CstplItemShow>();
        cstplItemShowList =getItemOfTkcttAndCstplListSorted(cstplItemShowList_ForSort,0);
        // 添加合计
        setItemOfTkcttAndCstplList_AddTotal();
    }
    private void setItemOfTkcttAndCstplList_AddTotal(){
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        cstplItemShowListTemp.addAll(cstplItemShowList);

        cstplItemShowList.clear();
        // 小计
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        BigDecimal bdTotalContrast=new BigDecimal(0);
        BigDecimal bdAllTotalContrast=new BigDecimal(0);

        CstplItemShow itemUnit=new CstplItemShow();
        CstplItemShow itemUnitNext=new CstplItemShow();
        for(int i=0;i< cstplItemShowListTemp.size();i++){
            itemUnit = cstplItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            // 对照
            bdTotalContrast=bdTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));
            bdAllTotalContrast=bdAllTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));
            cstplItemShowList.add(itemUnit);

            if(i+1< cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i+1);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                Boolean isRoot=false;
                if(itemUnitNext.getParentPkid()!=null&&itemUnitNext.getParentPkid().equals("root")){
                    cstplItemShowTemp.setName("合计");
                    cstplItemShowTemp.setPkid("total"+i);
                    cstplItemShowTemp.setContractAmount(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

                if(itemUnitNext.getParentPkidContrast()!=null && itemUnitNext.getParentPkidContrast().equals("root")){
                    cstplItemShowTemp.setPkid("total"+i);
                    cstplItemShowTemp.setNameContrast("合计");
                    cstplItemShowTemp.setPkidContrast("total_contrast"+i);
                    cstplItemShowTemp.setContractAmountContrast(bdTotalContrast);
                    bdTotalContrast = new BigDecimal(0);
                    isRoot=true;
                }

                if(isRoot.equals(true)){
                    cstplItemShowList.add(cstplItemShowTemp);
                }
            } else if(i+1== cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setName("合计");
                cstplItemShowTemp.setPkid("total"+i);
                cstplItemShowTemp.setContractAmount(bdTotal);

                cstplItemShowTemp.setNameContrast("合计");
                cstplItemShowTemp.setPkidContrast("total_contrast"+i);
                cstplItemShowTemp.setContractAmountContrast(bdTotalContrast);
                cstplItemShowList.add(cstplItemShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalContrast = new BigDecimal(0);

                // 总合计
                cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setName("总合计");
                cstplItemShowTemp.setPkid("total_all"+i);
                cstplItemShowTemp.setContractAmount(bdAllTotal);

                cstplItemShowTemp.setNameContrast("总合计");
                cstplItemShowTemp.setPkidContrast("total_all_contrast"+i);
                cstplItemShowTemp.setContractAmountContrast(bdAllTotalContrast);
                cstplItemShowList.add(cstplItemShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalContrast = new BigDecimal(0);
            }
        }
    }
    private boolean fromTkcttToCstplAction(){
        try{
            if(cstplItemShowSelected.getStrNo() ==null||
                    cstplItemShowSelected.getStrNo().equals("")) {
                MessageUtil.addInfo("无可复制！");
                return false;
            }
            if(!ToolUtil.getStrIgnoreNull(cstplItemShowSelected.getStrNoContrast()).equals("")) {
                MessageUtil.addInfo("成本计划项不为空，总包合同到成本计划的项无法复制，如需复制，请先删除该成本计划项！");
                return false;
            }
            String strIgnoreSpaceOfStr=ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSelected.getStrNo());
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
            cttItemShowSel = getItemOfEsItemHieRelapByItem(cstplItemShowSelected,"Tkctt");
            cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
            cttItemShowSel.setBelongToType(strBelongToType) ;
            cttItemShowSel.setBelongToPkid(strBelongToPkid) ;
            Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");
            if(intLastIndexof>0){
                String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList,"Cstpl");
                cttItemShowSel.setParentPkid(cstplItemShowTemp.getPkid());
                cttItemShowSel.setGrade(cstplItemShowTemp.getGrade() + 1) ;
            }else{
                cttItemShowSel.setParentPkid("root");
            }

            cttItemShowSel.setCorrespondingPkid(cttItemShowSel.getPkid()) ;
            cttItemShowSel.setStrCorrespondingItemNo(cttItemShowSel.getStrNo()) ;
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
        for(;i<modelList.size();i++){      //外层循环实现对整个list的遍历
            int startI=i;                    //保存当前元素的初始位置i,因为在内层循环中会改变i的值
            int countParent=0;               //统计和当前元素（即父元素）相同的元素个数，父元素只能是顺序相同
            int countChild=0;                //统计匹配父元素的孩子元素的个数，孩子元素也是顺序出现
            int positionOfChild=0;           //记录下和父元素匹配的第一个孩子的位置
            strThisCorrespondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(modelList.get(i).getCorrespondingItemNoContrast());
            for(int j=i+1; j<modelList.size();j++){
                strThisCorrespondingItemNoContrastAfter=ToolUtil.getIgnoreSpaceOfStr(modelList.get(j).getCorrespondingItemNoContrast());
                if(strThisCorrespondingItemNoContrast.equals(strThisCorrespondingItemNoContrastAfter)){//判断内层循环中是否有和外层循环中的元素相同的元素，并记录下来保存在countParent
                    if(countParent==0){        //如果内层循环中存在和外层中相同的元素，并且是第一次遇到，由于countParent初值设置为0，
                        countParent+=2;          //故首次碰到时内层中需要将countParent+2
                        continue;
                    }
                    ++countParent;
                }
                else{                        //else下分为三种情况
                    if(countParent==0||!strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast))
                        break;//1.内层中没有和外层中元素相同的元素，由已知条件的list知，这种情况直接退出内层循环即可；||2.内层中有和外层中元素相同的元素，但是内层中没有其孩子，由已知条件的list知，这种情况直接退出内层循环即可
                    if((strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast)&&
                            strThisCorrespondingItemNoContrastAfter.compareTo(strThisCorrespondingItemNoContrast)>0)){
                        if(positionOfChild==0){//内层中有和外层中元素相同的元素，
                            positionOfChild=j;  //记录下第一个孩子的位置保存在positionOfChild中。
                            countChild+=1;      //内层中有和外层中元素相同的元素，并且还有其孩子，则统计其孩子的个数，保存在countChild，由于countParent初值设置为0，故内层中需要将countParent+1
                            continue;
                        }
                        ++countChild;
                    }
                    if(countChild!=0)         //对孩子采用递归思想
                        modelList=getItemOfTkcttAndCstplListSorted(modelList, positionOfChild);//递归后得到，匹配父元素的孩子的正确排序
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
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<CttItemShow> cttItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getLastUpdBy());
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
    /*不符合规范的编码输入并离开时,清空相应的对象*/
    private Boolean strNoBlurFalse(){
        cttItemShowSel.setPkid("") ;
        cttItemShowSel.setParentPkid("");
        cttItemShowSel.setGrade(null);
        cttItemShowSel.setOrderid(null);
        return false;
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
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CstplItemShow> getItemOfTkcttAndCstplByLevelParentPkid(String strLevelParentPkid,
                                                                        List<CstplItemShow> esCstplItemShowListPara,String strTkcttOrCstpl) {
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        /*避开重复链接数据库*/
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(itemUnit!=null&&itemUnit.getParentPkidContrast()!=null&&
                            strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                        cstplItemShowListTemp.add(itemUnit);
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(itemUnit!=null&&itemUnit.getParentPkidContrast()!=null&&
                            strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkidContrast())){
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
    private CstplItemShow getItemOfTkcttAndCstplByStrNo(String strNo,
                                                        List<CstplItemShow> cstplItemShowListPara,String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNoContrast()).equals(strNo)) {
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
    private CstplItemShow getItemOfTkcttAndCstplByPkid(String strPkid,
                                                       List<CstplItemShow> cstplItemShowListPara,String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkid()).equals(strPkid)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkidContrast()).equals(strPkid)) {
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

    /*总成关系获取到层级关系中*/
    private CttItemShow getItemOfEsItemHieRelapByItem(
            CstplItemShow cstplItemShowPara,String strTkcttOrCstpl){
        CttItemShow cttItemShowTemp =new CttItemShow() ;
        if(strTkcttOrCstpl .equals("Tkctt")){
            cttItemShowTemp.setStrNo(cstplItemShowPara.getStrNo());
            cttItemShowTemp.setPkid(cstplItemShowPara.getPkid()) ;
            cttItemShowTemp.setBelongToType(cstplItemShowPara.getBelongToType());
            cttItemShowTemp.setBelongToPkid(cstplItemShowPara.getBelongToPkid());
            cttItemShowTemp.setParentPkid(cstplItemShowPara.getParentPkid()) ;
            cttItemShowTemp.setGrade(cstplItemShowPara.getGrade()) ;
            cttItemShowTemp.setOrderid(cstplItemShowPara.getOrderid()) ;
            cttItemShowTemp.setName(cstplItemShowPara.getName()) ;
            cttItemShowTemp.setNote(cstplItemShowPara.getNote()) ;

            cttItemShowTemp.setUnit(cstplItemShowPara.getUnit());
            cttItemShowTemp.setContractUnitPrice(cstplItemShowPara.getContractUnitPrice()) ;
            cttItemShowTemp.setContractQuantity(cstplItemShowPara.getContractQuantity()) ;
            cttItemShowTemp.setContractAmount(cstplItemShowPara.getContractAmount()) ;
            cttItemShowTemp.setSignPartAPrice(cstplItemShowPara.getSignPartAPrice()) ;
            cttItemShowTemp.setDeletedFlag(cstplItemShowPara.getDeletedFlag());
            cttItemShowTemp.setOriginFlag(cstplItemShowPara.getOriginFlag()) ;
            cttItemShowTemp.setCreatedBy(cstplItemShowPara.getCreatedBy());
            cttItemShowTemp.setCreatedDate(cstplItemShowPara.getCreatedDate());
            cttItemShowTemp.setLastUpdBy(cstplItemShowPara.getLastUpdBy());
            cttItemShowTemp.setLastUpdDate(cstplItemShowPara.getLastUpdDate());
            cttItemShowTemp.setModificationNum(cstplItemShowPara.getModificationNum());
            cttItemShowTemp.setCorrespondingPkid(cstplItemShowPara.getCorrespondingPkid());
            cttItemShowTemp.setStrCorrespondingItemNo(null);
        }
        else if(strTkcttOrCstpl .equals("Cstpl")){
            cttItemShowTemp.setStrNo(cstplItemShowPara.getStrNoContrast());
            cttItemShowTemp.setPkid(cstplItemShowPara.getPkidContrast()) ;
            cttItemShowTemp.setBelongToType(cstplItemShowPara.getBelongToTypeContrast());
            cttItemShowTemp.setBelongToPkid(cstplItemShowPara.getBelongToPkidContrast());
            cttItemShowTemp.setParentPkid(cstplItemShowPara.getParentPkidContrast()) ;
            cttItemShowTemp.setGrade(cstplItemShowPara.getGradeContrast()) ;
            cttItemShowTemp.setOrderid(cstplItemShowPara.getOrderidContrast()) ;
            cttItemShowTemp.setName(cstplItemShowPara.getNameContrast()) ;
            cttItemShowTemp.setNote(cstplItemShowPara.getNoteContrast()) ;

            cttItemShowTemp.setUnit(cstplItemShowPara.getUnitContrast());
            cttItemShowTemp.setContractUnitPrice(cstplItemShowPara.getContractUnitPriceContrast()) ;
            cttItemShowTemp.setContractQuantity(cstplItemShowPara.getContractQuantityContrast()) ;
            cttItemShowTemp.setContractAmount(cstplItemShowPara.getContractAmountContrast()) ;
            cttItemShowTemp.setSignPartAPrice(cstplItemShowPara.getSignPartAPriceContrast()) ;
            cttItemShowTemp.setDeletedFlag(cstplItemShowPara.getDeletedFlagContrast());
            cttItemShowTemp.setOriginFlag(cstplItemShowPara.getOriginFlagContrast()) ;
            cttItemShowTemp.setCreatedBy(cstplItemShowPara.getCreatedByContrast());
            cttItemShowTemp.setCreatedDate(cstplItemShowPara.getCreatedDateContrast());
            cttItemShowTemp.setLastUpdBy(cstplItemShowPara.getLastUpdByContrast());
            cttItemShowTemp.setLastUpdDate(cstplItemShowPara.getLastUpdDateContrast());
            cttItemShowTemp.setModificationNum(cstplItemShowPara.getModificationNumContrast());

            String strCorrespondingPkid= cstplItemShowPara.getCorrespondingPkidContrast();
            cttItemShowTemp.setCorrespondingPkid(strCorrespondingPkid);

            if(getItemOfTkcttAndCstplByPkid(strCorrespondingPkid, cstplItemShowList,"Tkctt")!=null) {
                cttItemShowTemp.setStrCorrespondingItemNo(
                        getItemOfTkcttAndCstplByPkid(strCorrespondingPkid, cstplItemShowList,"Tkctt").getStrNo());
            }else{
                cttItemShowTemp.setStrCorrespondingItemNo(null);
            }
        }
        return cttItemShowTemp;
    }
    /*总包合同到成本计划*/
    private CstplItemShow getItemOfTkcttAndCstplByItemOfEsItemHieRelap(
            CttItemShow cttItemShowPara,String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =new CstplItemShow() ;
        if(strTkcttOrCstpl .equals("Tkctt")){
            cstplItemShowTemp.setStrNo(cttItemShowPara.getStrNo());
            cstplItemShowTemp.setPkid(cttItemShowPara.getPkid()) ;
            cstplItemShowTemp.setBelongToType(cttItemShowPara.getBelongToType()) ;
            cstplItemShowTemp.setBelongToPkid(cttItemShowPara.getBelongToPkid()) ;
            cstplItemShowTemp.setParentPkid(cttItemShowPara.getParentPkid()) ;
            cstplItemShowTemp.setGrade(cttItemShowPara.getGrade()) ;
            cstplItemShowTemp.setOrderid(cttItemShowPara.getOrderid()) ;
            cstplItemShowTemp.setName(cttItemShowPara.getName()) ;
            cstplItemShowTemp.setNote(cttItemShowPara.getNote()) ;
            cstplItemShowTemp.setCorrespondingPkid(cttItemShowPara.getCorrespondingPkid());

            cstplItemShowTemp.setUnit(cttItemShowPara.getUnit());
            cstplItemShowTemp.setContractUnitPrice(cttItemShowPara.getContractUnitPrice()) ;
            cstplItemShowTemp.setContractQuantity(cttItemShowPara.getContractQuantity()) ;
            cstplItemShowTemp.setContractAmount(cttItemShowPara.getContractAmount()) ;
            cstplItemShowTemp.setSignPartAPrice(cttItemShowPara.getSignPartAPrice()) ;
            cstplItemShowTemp.setDeletedFlag(cttItemShowPara.getDeletedFlag());
            cstplItemShowTemp.setOriginFlag(cttItemShowPara.getOriginFlag()) ;
            cstplItemShowTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
            cstplItemShowTemp.setCreatedDate(cttItemShowPara.getCreatedDate());
            cstplItemShowTemp.setLastUpdBy(cttItemShowPara.getLastUpdBy());
            cstplItemShowTemp.setLastUpdDate(cttItemShowPara.getLastUpdDate());
            cstplItemShowTemp.setModificationNum(cttItemShowPara.getModificationNum());
        }
        else if(strTkcttOrCstpl .equals("Cstpl")){
            cstplItemShowTemp.setStrNoContrast(cttItemShowPara.getStrNo());
            cstplItemShowTemp.setPkidContrast(cttItemShowPara.getPkid()) ;
            cstplItemShowTemp.setBelongToTypeContrast(cttItemShowPara.getBelongToType()) ;
            cstplItemShowTemp.setBelongToPkidContrast(cttItemShowPara.getBelongToPkid()) ;
            cstplItemShowTemp.setParentPkidContrast(cttItemShowPara.getParentPkid()) ;
            cstplItemShowTemp.setGradeContrast(cttItemShowPara.getGrade()) ;
            cstplItemShowTemp.setOrderidContrast(cttItemShowPara.getOrderid()) ;
            cstplItemShowTemp.setNameContrast(cttItemShowPara.getName()) ;
            cstplItemShowTemp.setNoteContrast(cttItemShowPara.getNote()) ;
            cstplItemShowTemp.setCorrespondingPkidContrast(cttItemShowPara.getCorrespondingPkid());

            cstplItemShowTemp.setUnitContrast(cttItemShowPara.getUnit());
            cstplItemShowTemp.setContractUnitPriceContrast(cttItemShowPara.getContractUnitPrice()) ;
            cstplItemShowTemp.setContractQuantityContrast(cttItemShowPara.getContractQuantity()) ;
            cstplItemShowTemp.setContractAmountContrast(cttItemShowPara.getContractAmount()) ;
            cstplItemShowTemp.setSignPartAPriceContrast(cttItemShowPara.getSignPartAPrice()) ;
            cstplItemShowTemp.setDeletedFlagContrast(cttItemShowPara.getDeletedFlag());
            cstplItemShowTemp.setOriginFlagContrast(cttItemShowPara.getOriginFlag()) ;
            cstplItemShowTemp.setCreatedByContrast(cttItemShowPara.getCreatedBy());
            cstplItemShowTemp.setCreatedDateContrast(cttItemShowPara.getCreatedDate());
            cstplItemShowTemp.setLastUpdByContrast(cttItemShowPara.getLastUpdBy());
            cstplItemShowTemp.setLastUpdDateContrast(cttItemShowPara.getLastUpdDate());
            cstplItemShowTemp.setModificationNumContrast(cttItemShowPara.getModificationNum());
        }
        return cstplItemShowTemp;
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<CttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CttItemShow itemUnit: cttItemShowListPara){
            if(itemUnit.getGrade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0){
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

    /*智能字段Start*/
    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
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

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
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

    public CttItemShow getCttItemShowAdd() {
        return cttItemShowAdd;
    }

    public void setCttItemShowAdd(CttItemShow cttItemShowAdd) {
        this.cttItemShowAdd = cttItemShowAdd;
    }

    public CttItemShow getCttItemShowDel() {
        return cttItemShowDel;
    }

    public void setCttItemShowDel(CttItemShow cttItemShowDel) {
        this.cttItemShowDel = cttItemShowDel;
    }

    public CttItemShow getCttItemShowUpd() {
        return cttItemShowUpd;
    }

    public void setCttItemShowUpd(CttItemShow cttItemShowUpd) {
        this.cttItemShowUpd = cttItemShowUpd;
    }

    public CttItemShow getCttItemShowSel() {
        return cttItemShowSel;
    }

    public void setCttItemShowSel(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
    }

    /*智能字段End*/
}