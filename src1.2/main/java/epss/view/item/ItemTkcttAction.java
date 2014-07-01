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
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.CttItemShow;
import epss.repository.model.EsInitPower;
import epss.service.*;
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
public class ItemTkcttAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemTkcttAction.class);
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esInitCustService}")
    private EsInitCustService esInitCustService;

    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    private List<EsCttItem> esCttItemList;

    private CttItemShow cttItemShowCopy;
    private CttItemShow cttItemShowPaste;
    private List<CttItemShow> cttItemShowList;
    /*列表中选择一行*/
    private CttItemShow cttItemShowSelected;

    /*所属类型*/
    private String strBelongToType;
    /*所属号*/
    private String strBelongToPkid;

    /*提交类型*/
    private String strSubmitType;
    private String strPasteType;

    /*控制控件在画面上的可用与现实Start*/
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    //显示的控制
    private String strPasteBtnRendered;
    private String strMngNotFinishFlag;
    private String strStickyHeaderFlag;
    /*控制控件在画面上的可用与现实End*/

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        strBelongToType=ESEnum.ITEMTYPE0.getCode();
        strBelongToPkid=parammap.get("strTkCttPkid").toString();

        List<EsInitPower> esInitPowerList=
                esInitPowerService.selectListByModel(strBelongToType, strBelongToPkid, "NULL");
        strMngNotFinishFlag="true";
        if(esInitPowerList.size()>0){
            if(esInitPowerList.get(0).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                strMngNotFinishFlag="false";
            }
        }
        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
        /*形成关系树*/
        esCttItemList =new ArrayList<EsCttItem>();
        cttItemShowList =new ArrayList<CttItemShow>();
        esCttItemList = esCttItemService.getEsItemList(
                strBelongToType, strBelongToPkid);
        recursiveDataTable("root", esCttItemList);
        cttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,List<EsCttItem> esCttItemListPara){
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
                    itemUnit.getDeletedFlag(),
                    itemUnit.getOriginFlag(),
                    itemUnit.getCreatedBy(),
                    strCreatedByName,
                    itemUnit.getCreatedDate(),
                    itemUnit.getLastUpdBy(),
                    strLastUpdByName,
                    itemUnit.getLastUpdDate(),
                    itemUnit.getModificationNum(),
                    itemUnit.getNote(),
                    itemUnit.getCorrespondingPkid(),
                    "",
                    ""
                );
            cttItemShowList.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara);
        }
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
    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<CttItemShow> cttItemShowListTemp =new ArrayList<CttItemShow>();
        cttItemShowListTemp.addAll(cttItemShowList);

        cttItemShowList.clear();
        // 小计
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);
        CttItemShow itemUnit=new CttItemShow();
        CttItemShow itemUnitNext=new CttItemShow();
        for(int i=0;i< cttItemShowListTemp.size();i++){
            itemUnit = cttItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            cttItemShowList.add(itemUnit);

            if(i+1< cttItemShowListTemp.size()){
                itemUnitNext = cttItemShowListTemp.get(i+1);
                if(itemUnitNext.getParentPkid().equals("root")){
                    CttItemShow cttItemShowTemp =new CttItemShow();
                    cttItemShowTemp.setName("合计");
                    cttItemShowTemp.setPkid("total"+i);
                    cttItemShowTemp.setContractAmount(bdTotal);
                    cttItemShowList.add(cttItemShowTemp);
                    bdTotal=new BigDecimal(0);
                }
            } else if(i+1== cttItemShowListTemp.size()){
                itemUnitNext = cttItemShowListTemp.get(i);
                CttItemShow cttItemShowTemp =new CttItemShow();
                cttItemShowTemp.setName("合计");
                cttItemShowTemp.setPkid("total"+i);
                cttItemShowTemp.setContractAmount(bdTotal);
                cttItemShowList.add(cttItemShowTemp);
                bdTotal=new BigDecimal(0);

                // 总合计
                cttItemShowTemp =new CttItemShow();
                cttItemShowTemp.setName("总合计");
                cttItemShowTemp.setPkid("total_all"+i);
                cttItemShowTemp.setContractAmount(bdAllTotal);
                cttItemShowList.add(cttItemShowTemp);
            }
        }
    }
    /*重置*/
    public void resetAction(){
        strStickyHeaderFlag="true";
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strPasteBtnRendered="false";
        strPasteType="";
        cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strBelongToPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
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
        if ((cttItemShowTemp.getContractUnitPrice()!=null&&
                cttItemShowTemp.getContractUnitPrice().compareTo(BigDecimal.ZERO)!=0) ||
                (cttItemShowTemp.getContractQuantity()!=null&&
                        cttItemShowTemp.getContractQuantity().compareTo(BigDecimal.ZERO)!=0)){
            //||item_TkcttCstpl.getContractAmount()!=null){
            /*绑定前台控件,可输入的BigDecimal类型本来为null的，自动转换为0，不可输入的，还是null*/
            if (StringUtils.isEmpty(cttItemShowTemp.getUnit())) {
                MessageUtil.addError("请输入单位！");
                return false;
            }
        }
        return true;
    }

    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")){
            if(cttItemShowAdd.getContractUnitPrice()==null|| cttItemShowAdd.getContractQuantity()==null){
                bigDecimal=null;
            }else{
                bigDecimal = cttItemShowAdd.getContractUnitPrice().multiply(cttItemShowAdd.getContractQuantity());
            }
            cttItemShowAdd.setContractAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")){
            if(cttItemShowUpd.getContractUnitPrice()==null|| cttItemShowUpd.getContractQuantity()==null){
                bigDecimal=null;
            }else{
                bigDecimal = cttItemShowUpd.getContractUnitPrice().multiply(cttItemShowUpd.getContractQuantity());
            }
            cttItemShowUpd.setContractAmount(bigDecimal);
        }
    }

    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            if(strSubmitTypePara.equals("Add")) {
                return;
            }
            if(cttItemShowSelected.getStrNo()==null){
                MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                return;
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                cttItemShowSel = (CttItemShow) BeanUtils.cloneBean(cttItemShowSelected);
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                strPasteBtnRendered="false";
                cttItemShowUpd =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo())) ;
                cttItemShowCopy =null;
                cttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Del")){
                strPasteBtnRendered="false";
                cttItemShowDel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowCopy =null;
                cttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Copy")){
                strPasteBtnRendered="true";
                cttItemShowCopy =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowSel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                strPasteType="Copy";
                cttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Cut")){
                strPasteBtnRendered="true";
                cttItemShowCopy =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowSel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                strPasteType="Cut";
                cttItemShowPaste =null;
            }else if(strSubmitTypePara.contains("Paste")){
                strPasteBtnRendered="false";
                cttItemShowPaste = (CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
                pasteAction();
            }
            strStickyHeaderFlag="false";
        } catch (Exception e) {
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*删除*/
    public void delThisRecordAction(){
        try {
            int deleteRecordNum=esCttItemService.deleteRecord(cttItemShowDel.getPkid()) ;
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            esCttItemService.setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(
                    cttItemShowDel.getBelongToType(),
                    cttItemShowDel.getBelongToPkid(),
                    cttItemShowDel.getParentPkid(),
                    cttItemShowDel.getGrade(),
                    cttItemShowDel.getOrderid());
            MessageUtil.addInfo("删除数据完成。");
            initData();
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrNoToGradeAndOrderidAction(){
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
            MessageUtil.addError("请确认输入的编码，编码"+strIgnoreSpaceOfStr+"格式不正确！");
            return strNoBlurFalse();
        }

        //该编码已经存在
        if(!strSubmitType.equals("Upd")){
            if(getItemOfEsItemHieRelapByStrNo(strIgnoreSpaceOfStr, cttItemShowList)!=null){
            }
            else{ //该编码不存在
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<EsCttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid("root", esCttItemList);

            if(itemHieRelapListSubTemp .size() ==0){
                if(!strIgnoreSpaceOfStr.equals("1") ){
                    MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入1！");
                    return strNoBlurFalse();
                }
            }
            else{
                if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strIgnoreSpaceOfStr)){
                    MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入"+(itemHieRelapListSubTemp.size() +1)+"！");
                    return strNoBlurFalse();
                }
            }
            cttItemShowTemp.setGrade(1) ;
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            CttItemShow cttItemShowTemp1 =new CttItemShow();
            cttItemShowTemp1 =getItemOfEsItemHieRelapByStrNo(strParentNo, cttItemShowList);
            if(cttItemShowTemp1 ==null|| cttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("请确认输入的编码！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            }
            else{
                List<EsCttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid(
                        cttItemShowTemp1.getPkid(),
                        esCttItemList);
                if(itemHieRelapListSubTemp .size() ==0){
                    if(!cttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入"+strParentNo+".1！");
                        return strNoBlurFalse();
                    }
                }
                else{
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入"+strParentNo+"."+
                                (itemHieRelapListSubTemp.size() +1)+"！");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[]= strIgnoreSpaceOfStr.split("\\.");
                cttItemShowTemp.setGrade(strTemps.length) ;
                cttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                cttItemShowTemp.setParentPkid(cttItemShowTemp1.getPkid()) ;
            }
        }
        return true ;
    }
    public void submitThisRecordAction(){
        try{
            /*提交前的检查*/
            if(strSubmitType .equals("Del")) {
                delThisRecordAction();
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct的grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderidAction()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
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
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，"+e.getMessage() );
        }
    }

    /*粘贴*/
    private void pasteAction(){
        try{
            CttItemShow cttItemShowCopyTemp =new CttItemShow() ;
            if(strPasteType.equals("Copy")){
                /*复制的对象*/
                cttItemShowCopyTemp = (CttItemShow) BeanUtils.cloneBean(cttItemShowCopy);
                /*复制的目标位置*/
                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                if(strSubmitType .equals("Paste_brother_up")){
                    cttItemShowCopyTemp.setParentPkid(cttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= cttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= cttItemShowPaste.getOrderid();
                }else if(strSubmitType.equals("Paste_brother_down")){
                    cttItemShowCopyTemp.setParentPkid(cttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= cttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= cttItemShowPaste.getOrderid()+1;
                }else if(strSubmitType.equals("Paste_children")){
                    cttItemShowCopyTemp.setParentPkid(cttItemShowPaste.getPkid());
                    intCutPasteActionGrade= cttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= esCttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    cttItemShowCopyTemp.getBelongToType(),
                                    cttItemShowCopyTemp.getBelongToPkid(),
                                    cttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                cttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                cttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*更新复制的目标位置以后的数据*/
                esCttItemService.setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(
                        cttItemShowCopyTemp.getBelongToType(),
                        cttItemShowCopyTemp.getBelongToPkid(),
                        cttItemShowCopyTemp.getParentPkid(),
                        cttItemShowCopyTemp.getGrade(),
                        cttItemShowCopyTemp.getOrderid());
                /*在复制的目标位置处插入复制的对象：esItemHieRelapTemp中的Pkid随机生成不同于以前的了*/
                esCttItemService.insertRecord(cttItemShowCopyTemp);

                /*剪切前后层级变化的数*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= cttItemShowPaste.getGrade() - cttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (cttItemShowPaste.getGrade()+1) - cttItemShowCopy.getGrade() ;
                }

                /*遍历复制对象的子节点数据Start*/
                 /*记录新插入的当前条，因为Pkid随着自动生成已经变化了*/
                CttItemShow cttItemShowTemp =(CttItemShow) BeanUtils.cloneBean(cttItemShowCopy);
                /*复制总包合同列表数据用*/
                List<CttItemShow> cttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTemp为排好序的总包合同信息列表*/
                cttItemShowListTemp.addAll(cttItemShowList);
                /*记录之前节点用*/
                CttItemShow cttItemShowBefore =new CttItemShow();
                Boolean isBegin=false;
                for(CttItemShow itemUnit: cttItemShowListTemp){
                    CttItemShow cttItemShowNewInsert =(CttItemShow) BeanUtils.cloneBean(itemUnit);
                     /*找到剪切对象后，寻找该对象其子节点（层级号大于该对象的层级号的数据）并进行复制*/
                    if(isBegin.equals(true) &&
                            ToolUtil.getIntIgnoreNull(cttItemShowNewInsert.getGrade())<= cttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        /*同层的情况下*/
                        if(cttItemShowNewInsert.getGrade().equals(cttItemShowBefore.getGrade())){
                            /*设置本条数据的父亲节点号为刚才插入数据的父亲节点号*/
                            cttItemShowNewInsert.setParentPkid(cttItemShowTemp.getParentPkid());
                        }
                        /*子层层的情况下*/
                        else{
                            /*设置本条数据的父亲节点号为刚才插入数据的节点号*/
                            cttItemShowNewInsert.setParentPkid(cttItemShowTemp.getPkid());
                        }
                        /*设置本条数据的层级号为原先的层级号加变换前后的层级差*/
                        cttItemShowNewInsert.setGrade(cttItemShowNewInsert.getGrade()+intGradeGap);
                        /*插入新数据*/
                        esCttItemService.insertRecord(cttItemShowNewInsert);
                        /*记录新插入的当前条，因为Pkid随着自动生成已经变化了*/
                        cttItemShowTemp =(CttItemShow) BeanUtils.cloneBean(cttItemShowNewInsert);
                        /*记录当前条数据*/
                        cttItemShowBefore =itemUnit;
                    }
                    /*找到复制对象*/
                    if(cttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }
                /*遍历复制对象的子节点数据End*/
            }
            else if (strPasteType.equals("Cut")){
                /*粘贴的操作*/
                /*修改目标节点的父子关系及层级号和序号*/
                cttItemShowCopyTemp = (CttItemShow) BeanUtils.cloneBean(cttItemShowCopy);

                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                if(strSubmitType .equals("Paste_brother_up")){
                    cttItemShowCopyTemp.setParentPkid(cttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= cttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= cttItemShowPaste.getOrderid();
                }else if(strSubmitType.equals("Paste_brother_down")){
                    cttItemShowCopyTemp.setParentPkid(cttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= cttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= cttItemShowPaste.getOrderid()+1;
                }else if(strSubmitType.equals("Paste_children")){
                    cttItemShowCopyTemp.setParentPkid(cttItemShowPaste.getPkid());
                    intCutPasteActionGrade= cttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= esCttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    cttItemShowCopyTemp.getBelongToType(),
                                    cttItemShowCopyTemp.getBelongToPkid(),
                                    cttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                cttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                cttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*1更新剪切的目标位置以后的数据*/
                esCttItemService.setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(
                        cttItemShowCopyTemp.getBelongToType(),
                        cttItemShowCopyTemp.getBelongToPkid(),
                        cttItemShowCopyTemp.getParentPkid(),
                        cttItemShowCopyTemp.getGrade(),
                        cttItemShowCopyTemp.getOrderid());
                /*2修改本条数据的ParentPkid，Grade,Orderid*/
                esCttItemService.updateRecord(cttItemShowCopyTemp) ;
                /*3修改子节点的层级关系*/
                /*剪切前后层级变化的数*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= cttItemShowPaste.getGrade() - cttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (cttItemShowPaste.getGrade()+1) - cttItemShowCopy.getGrade() ;
                }

                /*复制总包合同列表数据用*/
                List<CttItemShow> cttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTemp为排好序的总包合同信息列表*/
                cttItemShowListTemp.addAll(cttItemShowList);
                Boolean isBegin=false;
                for(CttItemShow itemUnit: cttItemShowListTemp){
                    /*找到剪切对象后，寻找该对象其子节点（层级号大于该对象的层级号的数据）并进行复制*/
                    if(isBegin.equals(true) && itemUnit.getGrade()<= cttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        itemUnit.setGrade(itemUnit.getGrade()+intGradeGap);
                        esCttItemService.updateRecord(itemUnit) ;
                    }
                    /*找到剪切对象*/
                    if(cttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }

                /*剪切的操作*/
                esCttItemService.setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(
                        cttItemShowCopyTemp.getBelongToType(),
                        cttItemShowCopyTemp.getBelongToPkid(),
                        cttItemShowCopyTemp.getParentPkid(),
                        cttItemShowCopyTemp.getGrade(),
                        cttItemShowCopyTemp.getOrderid());
            }
            MessageUtil.addInfo("粘贴成功!");
            initData();
        }
        catch (Exception e){
            logger.error("粘贴数据失败，", e);
            MessageUtil.addError(e.getMessage());
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
    /*层级关系列表中通过Pkid的该项*/
    private CttItemShow getItemOfEsItemHieRelapByPkid(String strPkid,
              List<CttItemShow> cttItemShowListPara){
        CttItemShow cttItemShowTemp =new CttItemShow();
        try{
            for(CttItemShow itemUnit: cttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkid()).equals(strPkid)) {
                    cttItemShowTemp =(CttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cttItemShowTemp;
    }
    /*在总包合同列表中根据编号找到项*/
    private CttItemShow getItemOfEsItemHieRelapByStrNo(
             String strNo,
             List<CttItemShow> cttItemShowListPara){
        CttItemShow cttItemShowTemp =null;
        try{
            for(CttItemShow itemUnit: cttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    cttItemShowTemp =(CttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cttItemShowTemp;
    }
    private Boolean strNoBlurFalse(){
        cttItemShowSel.setPkid("") ;
        cttItemShowSel.setParentPkid("");
        cttItemShowSel.setGrade(null);
        cttItemShowSel.setOrderid(null);
        return false;
    }

    /*智能字段Start*/

    public String getStrStickyHeaderFlag() {
        return strStickyHeaderFlag;
    }

    public void setStrStickyHeaderFlag(String strStickyHeaderFlag) {
        this.strStickyHeaderFlag = strStickyHeaderFlag;
    }

    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }
    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }
    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }
    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsInitCustService getEsInitCustService() {
        return esInitCustService;
    }

    public void setEsInitCustService(EsInitCustService esInitCustService) {
        this.esInitCustService = esInitCustService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }
    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttItemShow getCttItemShowSel() {
        return cttItemShowSel;
    }

    public void setCttItemShowSel(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
    }

    public CttItemShow getCttItemShowSelected() {
        return cttItemShowSelected;
    }
    public void setCttItemShowSelected(CttItemShow cttItemShowSelected) {
        this.cttItemShowSelected = cttItemShowSelected;
    }
    public List<CttItemShow> getCttItemShowList() {
        return cttItemShowList;
    }
    public void setCttItemShowList(List<CttItemShow> cttItemShowList) {
        this.cttItemShowList = cttItemShowList;
    }
    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }
    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
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
    public String getStrPasteBtnRendered() {
        return strPasteBtnRendered;
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
    /*智能字段End*/
}