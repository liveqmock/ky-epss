package epss.view.contract;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.SubcttInfo;
import epss.repository.model.SubcttItem;
import epss.repository.model.model_show.SubcttItemShow;
import epss.service.*;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class SubcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttItemAction.class);
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{subcttItemService}")
    private SubcttItemService subcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;

    private SubcttItemShow subcttItemShowSel;
    private SubcttItemShow subcttItemShowAdd;
    private SubcttItemShow subcttItemShowUpd;
    private SubcttItemShow subcttItemShowDel;
    private List<SubcttItem> subcttItemList;
	private SubcttItemShow subcttItemShowCopy;
    private SubcttItemShow subcttItemShowPaste;
    /*列表中选择一行*/
    private SubcttItemShow subcttItemShowSelected;
	/*列表显示用*/
    private List<SubcttItemShow> subcttItemShowList;
    /*所属号*/
    private String strSubcttInfoPkid;

    /*提交类型*/
    private String strSubmitType;
    private String strMngNotFinishFlag;
    private String strPasteType;

    /*控制控件在画面上的可用与现实Start*/
    //显示的控制
	private StyleModel styleModelNo;
    private StyleModel styleModel;
    // 固定项输入时控制控件的可用
    private StyleModel styleModelCttQty;
    private StyleModel styleModelCttAmount;
    private String strPasteBtnRendered;
    /*控制控件在画面上的可用与现实End*/

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strSubcttInfoPkid")) {
            strSubcttInfoPkid=parammap.get("strSubcttInfoPkid").toString();
        }else{
            strSubcttInfoPkid="";
        }
        

        SubcttInfo subcttInfoTemp =
                subcttInfoService.getSubcttInfoByPkid(strSubcttInfoPkid);
        strMngNotFinishFlag="true";
        if(subcttInfoTemp!=null&& EnumFlowStatus.FLOW_STATUS0.getCode().equals(subcttInfoTemp.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }
        resetAction();
        initData() ;
    }

    /*初始化操作*/
    private void initData() {
        /*成本计划*/
        subcttItemShowList =new ArrayList<SubcttItemShow>();
        subcttItemList = subcttItemService.getSubcttItemListBySubcttInfoPkid(strSubcttInfoPkid);
        recursiveDataTable("root", subcttItemList, subcttItemShowList);
        subcttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(subcttItemShowList);
        /*分包合同*/
        subcttItemList =new ArrayList<SubcttItem>();
        subcttItemShowList =new ArrayList<SubcttItemShow>();
        subcttItemList = subcttItemService.getSubcttItemListBySubcttInfoPkid(strSubcttInfoPkid);
        subcttItemShowList.clear();
        recursiveDataTable("root", subcttItemList, subcttItemShowList);
        subcttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(subcttItemShowList);
        /*分包合同对应成本计划中的项*/
        for(SubcttItemShow itemUnit: subcttItemShowList){
            for(SubcttItemShow itemUnitCstpl: subcttItemShowList){
                if(ToolUtil.getStrIgnoreNull(itemUnit.getCstplItemPkid()).equals(itemUnitCstpl .getPkid())){
                    itemUnit.setCstplItemNo(itemUnitCstpl.getStrNo());
                    itemUnit.setCstplItemName(itemUnitCstpl .getName());
                }
            }
        }
        // 添加合计
        setItemOfCstplAndSubcttList_AddTotal();
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<SubcttItem> subcttItemListPara,
                                    List<SubcttItemShow> subcttItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<SubcttItem> subSubcttItemList =new ArrayList<SubcttItem>();
        // 通过父层id查找它的孩子
        subSubcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, subcttItemListPara);
        for(SubcttItem itemUnit: subSubcttItemList){
            SubcttItemShow subcttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
            subcttItemShowTemp = new SubcttItemShow(
                     itemUnit.getPkid(),
                     "",
                     itemUnit.getSubcttInfoPkid(),
                     itemUnit.getParentPkid(),
                     itemUnit.getGrade(),
                     itemUnit.getOrderid(),
                     itemUnit.getName(),
                     itemUnit.getUnit(),
                     itemUnit.getUnitPrice(),
                     itemUnit.getQty(),
                     itemUnit.getAmt(),
                     itemUnit.getSignPartPriceA(),
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
                     itemUnit.getCstplItemPkid(),
                     "",
                     "",
                     itemUnit.getTid()
            );
            subcttItemShowListPara.add(subcttItemShowTemp) ;
            recursiveDataTable(subcttItemShowTemp.getPkid(), subcttItemListPara, subcttItemShowListPara);
        }
    }
    private void setItemOfCstplAndSubcttList_AddTotal(){
        List<SubcttItemShow> subcttItemShowListTemp =new ArrayList<SubcttItemShow>();
        subcttItemShowListTemp.addAll(subcttItemShowList);

        subcttItemShowList.clear();
        // 小计
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        SubcttItemShow itemUnit=new SubcttItemShow();
        SubcttItemShow itemUnitNext=new SubcttItemShow();
        for(int i=0;i< subcttItemShowListTemp.size();i++){
            itemUnit = subcttItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmt()));
            if(itemUnit.getUnit()!=null&&!itemUnit.getUnit().equals("")){
                bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmt()));
            }
            subcttItemShowList.add(itemUnit);

            if(i+1< subcttItemShowListTemp.size()){
                itemUnitNext = subcttItemShowListTemp.get(i+1);
                SubcttItemShow subcttItemShowTemp =new SubcttItemShow();
                Boolean isRoot=false;
                if(itemUnitNext.getParentPkid()!=null&&itemUnitNext.getParentPkid().equals("root")){
                    subcttItemShowTemp.setName("合计");
                    subcttItemShowTemp.setPkid("total"+i);
                    subcttItemShowTemp.setAmt(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }
                if(isRoot.equals(true)){
                    subcttItemShowList.add(subcttItemShowTemp);
                }
            } else if(i+1== subcttItemShowListTemp.size()){
                itemUnitNext = subcttItemShowListTemp.get(i);
                SubcttItemShow subcttItemShowTemp =new SubcttItemShow();
                subcttItemShowTemp.setName("合计");
                subcttItemShowTemp.setPkid("total"+i);
                subcttItemShowTemp.setAmt(bdTotal);
                bdTotal=new BigDecimal(0);
                subcttItemShowList.add(subcttItemShowTemp);
                // 总合计
                subcttItemShowTemp =new SubcttItemShow();
                subcttItemShowTemp.setName("总合计");
                subcttItemShowTemp.setPkid("total_all"+i);
                subcttItemShowTemp.setAmt(bdAllTotal);
                subcttItemShowList.add(subcttItemShowTemp);
                bdAllTotal=new BigDecimal(0);
            }
        }
    }

    /*重置*/
    public void resetAction(){
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        styleModelCttQty=new StyleModel();
        styleModelCttQty.setDisabled_Flag("false");
        styleModelCttAmount=new StyleModel();
        styleModelCttAmount.setDisabled_Flag("true");
        strPasteType="";
        subcttItemShowSel =new SubcttItemShow(strSubcttInfoPkid);
        subcttItemShowAdd =new SubcttItemShow(strSubcttInfoPkid);
        subcttItemShowUpd =new SubcttItemShow(strSubcttInfoPkid);
        subcttItemShowDel =new SubcttItemShow(strSubcttInfoPkid);
    }
    public void resetActionForAdd(){
        strSubmitType="Add";
        subcttItemShowAdd =new SubcttItemShow(strSubcttInfoPkid);
    }
    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            if(!strSubmitTypePara.equals("Add")){
                if(subcttItemShowSelected.getStrNo()==null){
                    MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                    return;
                }
            }
            if(strSubmitTypePara.equals("Sel")){
                subcttItemShowSel =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(subcttItemShowSel.getStrNo())) ;
                subcttItemShowSel.setCstplItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(subcttItemShowSel.getCstplItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                subcttItemShowUpd =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(subcttItemShowUpd.getStrNo())) ;
                subcttItemShowUpd.setCstplItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(subcttItemShowUpd.getCstplItemNo()));
                subcttItemShowCopy =null;
                subcttItemShowPaste =null;
                esCommon.getIndexOfSubcttItemNamelist(subcttItemShowUpd.getName());
                strPasteBtnRendered="false";
            }
            else if(strSubmitTypePara.equals("Del")){
                subcttItemShowDel =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(subcttItemShowDel.getStrNo())) ;
                subcttItemShowDel.setCstplItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(subcttItemShowDel.getCstplItemNo()));
                strPasteBtnRendered="false";
                subcttItemShowCopy =null;
                subcttItemShowPaste =null;
            }
            else if(strSubmitTypePara.equals("Copy")){
                strPasteBtnRendered="true";
                subcttItemShowCopy =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;

                subcttItemShowSel =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(subcttItemShowSel.getStrNo()));
                subcttItemShowSel.setCstplItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(subcttItemShowSel.getCstplItemNo()));

                strPasteType="Copy";
                subcttItemShowPaste =null;

                styleModelNo.setDisabled_Flag("true");
                styleModel.setDisabled_Flag("true");
                styleModelCttQty.setDisabled_Flag("true");
                styleModelCttAmount.setDisabled_Flag("true");
            }
            else if(strSubmitTypePara.equals("Cut")){
                strPasteBtnRendered="true";
                subcttItemShowCopy =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowSel =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(subcttItemShowSel.getStrNo())) ;
                subcttItemShowSel.setCstplItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(subcttItemShowSel.getCstplItemNo()));
                strPasteType="Cut";
                subcttItemShowPaste =null;

                styleModelNo.setDisabled_Flag("true");
                styleModel.setDisabled_Flag("true");
                styleModelCttQty.setDisabled_Flag("true");
                styleModelCttAmount.setDisabled_Flag("true");
            }
            else if(strSubmitTypePara.contains("Paste")){
                strPasteBtnRendered="false";
                subcttItemShowPaste = (SubcttItemShow) BeanUtils.cloneBean(subcttItemShowSelected) ;
                subcttItemShowSel =new SubcttItemShow(strSubcttInfoPkid);
                pasteAction();

                styleModelNo.setDisabled_Flag("true");
                styleModel.setDisabled_Flag("true");
                styleModelCttQty.setDisabled_Flag("true");
                styleModelCttAmount.setDisabled_Flag("true");
            }
        } catch (Exception e) {
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(subcttItemShowSel.getName()).equals("")){
            styleModelCttQty.setDisabled_Flag("false");
            styleModelCttAmount.setDisabled_Flag("true");
            if(strSubmitType!=null && strSubmitType.equals("Upd")) {
                styleModelNo.setDisabled_Flag("true");
            }else{
                styleModelNo.setDisabled_Flag("false");
            }
        }
        return true;
    }
    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")){
            if(subcttItemShowAdd.getUnitPrice()==null|| subcttItemShowAdd.getQty()==null){
                bigDecimal=null;
            }
            else{
                bigDecimal = subcttItemShowAdd.getUnitPrice().multiply(subcttItemShowAdd.getQty());
            }
            subcttItemShowAdd.setAmt(bigDecimal);
        }
        if (strSubmitType.equals("Upd")){
            if(subcttItemShowUpd.getUnitPrice()==null|| subcttItemShowUpd.getQty()==null){
                bigDecimal=null;
            }
            else{
                bigDecimal = subcttItemShowUpd.getUnitPrice().multiply(subcttItemShowUpd.getQty());
            }
            subcttItemShowUpd.setAmt(bigDecimal);
        }

    }
    public Boolean blurStrNoToGradeAndOrderid(){
        SubcttItemShow subcttItemShowTemp =new SubcttItemShow(strSubcttInfoPkid);
        if (strSubmitType.equals("Add")){
            subcttItemShowTemp = subcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            subcttItemShowTemp = subcttItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(subcttItemShowTemp.getStrNo());
        if(StringUtils .isEmpty(strIgnoreSpaceOfStr)){
            return true;
        }
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (!strIgnoreSpaceOfStr.matches(strRegex) ){
            MessageUtil.addError("请确认输入的编码，编码" + strIgnoreSpaceOfStr + "格式不正确！");
            return strNoBlurFalse();
        }

        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<SubcttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid("root", subcttItemList);

            if(itemHieRelapListSubTemp .size() ==0){
                if(!strIgnoreSpaceOfStr.equals("1") ){
                    MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入1！");
                    return strNoBlurFalse();
                }
            }
            else{
                if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strIgnoreSpaceOfStr)){
                    MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" +
                            (itemHieRelapListSubTemp.size() + 1) + "！");
                    return strNoBlurFalse();
                }
            }
            subcttItemShowTemp.setGrade(1);
            subcttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            subcttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            SubcttItemShow subcttItemShowTemp1 =new SubcttItemShow();
            subcttItemShowTemp1 =getItemOfEsItemHieRelapByStrNo(
                    strParentNo,
                    subcttItemShowList);
            if(subcttItemShowTemp1 ==null|| subcttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("请确认输入的编码！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            }
            else{
                List<SubcttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid(
                        subcttItemShowTemp1.getPkid(),
                        subcttItemList);
                if(itemHieRelapListSubTemp .size() ==0){
                    if(!subcttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + strParentNo + ".1！");
                        return strNoBlurFalse();
                    }
                }
                else{
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + strParentNo +
                                "." + (itemHieRelapListSubTemp.size() + 1) + "！");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[]= strIgnoreSpaceOfStr.split("\\.");
                subcttItemShowTemp.setGrade(strTemps.length) ;
                subcttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                subcttItemShowTemp.setParentPkid(subcttItemShowTemp1.getPkid()) ;
            }
        }
        return true ;
    }
    public Boolean blurCorrespondingPkid(){
        SubcttItemShow subcttItemShowTemp =new SubcttItemShow(strSubcttInfoPkid);
        if (strSubmitType.equals("Add")){
            subcttItemShowTemp = subcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            subcttItemShowTemp = subcttItemShowUpd;
        }
        if(subcttItemShowTemp.getCstplItemPkid()==null||
                subcttItemShowTemp.getCstplItemNo().equals("")){
            subcttItemShowTemp.setCstplItemPkid(null);
            subcttItemShowTemp.setCstplItemName(null);
            return true;
        }
        SubcttItemShow subcttItemShowTemp1 =
                getItemOfEsItemHieRelapByStrNo(
                        subcttItemShowTemp.getCstplItemNo(),
                        subcttItemShowList);
        if(subcttItemShowTemp1 !=null && subcttItemShowTemp1.getStrNo() !=null){
            subcttItemShowTemp.setCstplItemPkid(subcttItemShowTemp1.getPkid());
            subcttItemShowTemp.setCstplItemName(subcttItemShowTemp1.getName());
            return true ;
        }
        else{
            subcttItemShowTemp.setCstplItemPkid(null);
            subcttItemShowTemp.setCstplItemName(null);
            MessageUtil.addError("请确认输入的对应编码，该编码不是正确的成本计划项编码！");
            return strNoBlurFalse();
        }
    }
    private Boolean strNoBlurFalse(){
        subcttItemShowSel.setPkid("") ;
        subcttItemShowSel.setParentPkid("");
        subcttItemShowSel.setGrade(null);
        subcttItemShowSel.setOrderid(null);
        return false;
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Del")){
                int delRecordNum=delRecordAction(subcttItemShowDel);
                if (delRecordNum<=0){
                    MessageUtil.addInfo("该记录已删除。");
                    return;
                }
            }
            else {
                 /*提交前的检查*/
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct的grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderid()){
                    return ;
                }
                /*对应编码验证*/
                if(!blurCorrespondingPkid ()){
                    return ;
                }

                if(strSubmitType.equals("Add")){
                    addRecordAction(subcttItemShowAdd);
                }else if(strSubmitType.equals("Upd")){
                    Integer intRecVersion= subcttItemShowUpd.getRecVersion()==null?0: subcttItemShowUpd.getRecVersion();
                    subcttItemShowUpd.setRecVersion(intRecVersion + 1);
                    updRecordAction(subcttItemShowUpd);
                }
            }
            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("提交数据完成。");
                case "Upd" : MessageUtil.addInfo("更新数据完成。");
                case "Del" : MessageUtil.addInfo("删除数据完成。");
            }
            initData();
            strPasteBtnRendered="false";
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }
    private void addRecordAction(SubcttItemShow subcttItemShowPara){
        subcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                subcttItemShowPara.getSubcttInfoPkid(),
                subcttItemShowPara.getParentPkid(),
                subcttItemShowPara.getGrade());
        subcttItemService.insertRecord(subcttItemShowPara);
    }
    private void updRecordAction(SubcttItemShow subcttItemShowPara){
        subcttItemService.updateRecord(subcttItemShowPara) ;
    }
    private int delRecordAction(SubcttItemShow subcttItemShowPara) {
        int deleteRecordNum= subcttItemService.deleteRecord(subcttItemShowPara.getPkid()) ;
        subcttItemService.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                subcttItemShowPara.getSubcttInfoPkid(),
                subcttItemShowPara.getParentPkid(),
                subcttItemShowPara.getGrade());
        return deleteRecordNum;
    }

    /*提交前的检查：必须项的输入*/
	private Boolean subMitActionPreCheck(){
        SubcttItemShow subcttItemShowTemp =new SubcttItemShow(strSubcttInfoPkid);
        if (strSubmitType.equals("Add")){
            subcttItemShowTemp = subcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            subcttItemShowTemp = subcttItemShowUpd;
        }
        if (StringUtils.isEmpty(subcttItemShowTemp.getStrNo())) {
            MessageUtil.addError("请输入编号！");
            return false;
        }
        if (StringUtils.isEmpty(subcttItemShowTemp.getName())) {
            MessageUtil.addError("请输入名称！");
            return false;
        }

        if ((subcttItemShowTemp.getUnitPrice()!=null&&
                subcttItemShowTemp.getUnitPrice().compareTo(BigDecimal.ZERO)!=0) ||
                (subcttItemShowTemp.getQty()!=null&&
                        subcttItemShowTemp.getQty().compareTo(BigDecimal.ZERO)!=0)){
            //||item_TkcttCstpl.getAmt()!=null){
            /*绑定前台控件,可输入的BigDecimal类型本来为null的，自动转换为0，不可输入的，还是null*/
            if (StringUtils.isEmpty(subcttItemShowTemp.getUnit())) {
                MessageUtil.addError("请输入单位！");
                return false;
            }
        }
        return true;
    }

    public void submitAction_Cstpl(){
        try{
            subcttItemShowSelected.setCstplItemPkid(subcttItemShowSelected.getPkid());
            subcttItemService.updateRecord(subcttItemShowSelected) ;
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<SubcttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
             List<SubcttItemShow> subcttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(SubcttItemShow itemUnit: subcttItemShowListPara){
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
        return subcttItemShowListPara;
    }
    /*粘贴*/
    private void pasteAction(){
        try{
            SubcttItemShow subcttItemShowCopyTemp =new SubcttItemShow() ;
            if(strPasteType.equals("Copy")){
                /*复制的对象*/
                subcttItemShowCopyTemp = (SubcttItemShow) BeanUtils.cloneBean(subcttItemShowCopy);
                /*复制的目标位置*/
                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                switch (strSubmitType){
                    case "Paste_brother_up":
                    subcttItemShowCopyTemp.setParentPkid(subcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= subcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= subcttItemShowPaste.getOrderid();break;
                    case "Paste_brother_down":
                    subcttItemShowCopyTemp.setParentPkid(subcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= subcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= subcttItemShowPaste.getOrderid()+1;break;
                    case "Paste_children":
                    subcttItemShowCopyTemp.setParentPkid(subcttItemShowPaste.getPkid());
                    intCutPasteActionGrade= subcttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= subcttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    subcttItemShowCopyTemp.getSubcttInfoPkid(),
                                    subcttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                subcttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                subcttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*更新复制的目标位置以后的数据*/
                subcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        subcttItemShowCopyTemp.getSubcttInfoPkid(),
                        subcttItemShowCopyTemp.getParentPkid(),
                        subcttItemShowCopyTemp.getGrade());
                /*在复制的目标位置处插入复制的对象：esItemHieRelapTemp中的Pkid随机生成不同于以前的了*/
                subcttItemService.insertRecord(subcttItemShowCopyTemp);

                /*剪切前后层级变化的数*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= subcttItemShowPaste.getGrade() - subcttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (subcttItemShowPaste.getGrade()+1) - subcttItemShowCopy.getGrade() ;
                }

                /*遍历复制对象的子节点数据Start*/
                 /*记录新插入的当前条，因为Pkid随着自动生成已经变化了*/
                SubcttItemShow subcttItemShowTemp =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowCopy);
                /*复制总包合同列表数据用*/
                List<SubcttItemShow> subcttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTemp为排好序的总包合同信息列表*/
                subcttItemShowListTemp.addAll(subcttItemShowList);
                /*记录之前节点用*/
                SubcttItemShow subcttItemShowBefore =new SubcttItemShow();
                Boolean isBegin=false;
                for(SubcttItemShow itemUnit: subcttItemShowListTemp){
                    SubcttItemShow subcttItemShowNewInsert =(SubcttItemShow) BeanUtils.cloneBean(itemUnit);
                     /*找到剪切对象后，寻找该对象其子节点（层级号大于该对象的层级号的数据）并进行复制*/
                    if(isBegin.equals(true) &&
                            ToolUtil.getIntIgnoreNull(subcttItemShowNewInsert.getGrade())<= subcttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        /*同层的情况下*/
                        if(subcttItemShowNewInsert.getGrade().equals(subcttItemShowBefore.getGrade())){
                            /*设置本条数据的父亲节点号为刚才插入数据的父亲节点号*/
                            subcttItemShowNewInsert.setParentPkid(subcttItemShowTemp.getParentPkid());
                        }
                        /*子层层的情况下*/
                        else{
                            /*设置本条数据的父亲节点号为刚才插入数据的节点号*/
                            subcttItemShowNewInsert.setParentPkid(subcttItemShowTemp.getPkid());
                        }
                        /*设置本条数据的层级号为原先的层级号加变换前后的层级差*/
                        subcttItemShowNewInsert.setGrade(subcttItemShowNewInsert.getGrade()+intGradeGap);
                        /*插入新数据*/
                        subcttItemService.insertRecord(subcttItemShowNewInsert);
                        /*记录新插入的当前条，因为Pkid随着自动生成已经变化了*/
                        subcttItemShowTemp =(SubcttItemShow) BeanUtils.cloneBean(subcttItemShowNewInsert);
                        /*记录当前条数据*/
                        subcttItemShowBefore =itemUnit;
                    }
                    /*找到复制对象*/
                    if(subcttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }
                /*遍历复制对象的子节点数据End*/
            }
            else if (strPasteType.equals("Cut")){
                /*粘贴的操作*/
                /*修改目标节点的父子关系及层级号和序号*/
                subcttItemShowCopyTemp = (SubcttItemShow) BeanUtils.cloneBean(subcttItemShowCopy);

                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                switch (strSubmitType){
                    case "Paste_brother_up":
                    subcttItemShowCopyTemp.setParentPkid(subcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= subcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= subcttItemShowPaste.getOrderid();break;
                    case "Paste_brother_down":
                    subcttItemShowCopyTemp.setParentPkid(subcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= subcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= subcttItemShowPaste.getOrderid()+1;break;
                    case "Paste_children":
                    subcttItemShowCopyTemp.setParentPkid(subcttItemShowPaste.getPkid());
                    intCutPasteActionGrade= subcttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= subcttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    subcttItemShowCopyTemp.getSubcttInfoPkid(),
                                    subcttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                subcttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                subcttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*1更新剪切的目标位置以后的数据*/
                subcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        subcttItemShowCopyTemp.getSubcttInfoPkid(),
                        subcttItemShowCopyTemp.getParentPkid(),
                        subcttItemShowCopyTemp.getGrade());
                /*2修改本条数据的ParentPkid，Grade,Orderid*/
                subcttItemService.updateRecord(subcttItemShowCopyTemp) ;
                /*3修改子节点的层级关系*/
                /*剪切前后层级变化的数*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= subcttItemShowPaste.getGrade() - subcttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (subcttItemShowPaste.getGrade()+1) - subcttItemShowCopy.getGrade() ;
                }

                /*复制总包合同列表数据用*/
                List<SubcttItemShow> subcttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTemp为排好序的总包合同信息列表*/
                subcttItemShowListTemp.addAll(subcttItemShowList);
                Boolean isBegin=false;
                for(SubcttItemShow itemUnit: subcttItemShowListTemp){
                    /*找到剪切对象后，寻找该对象其子节点（层级号大于该对象的层级号的数据）并进行复制*/
                    if(isBegin.equals(true) && itemUnit.getGrade()<= subcttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        itemUnit.setGrade(itemUnit.getGrade()+intGradeGap);
                        subcttItemService.updateRecord(itemUnit) ;
                    }
                    /*找到剪切对象*/
                    if(subcttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }

                /*剪切的操作*/
                subcttItemService.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                        subcttItemShowCopyTemp.getSubcttInfoPkid(),
                        subcttItemShowCopyTemp.getParentPkid(),
                        subcttItemShowCopyTemp.getGrade());
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
    private List<SubcttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
             List<SubcttItem> subcttItemListPara) {
        List<SubcttItem> tempSubcttItemList =new ArrayList<SubcttItem>();
        /*避开重复链接数据库*/
        for(SubcttItem itemUnit: subcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempSubcttItemList.add(itemUnit);
            }
        }
        return tempSubcttItemList;
    }
    /*层级关系列表中通过Pkid的该项*/
    private SubcttItemShow getItemOfEsItemHieRelapByPkid(String strPkid,
              List<SubcttItemShow> subcttItemShowListPara){
        SubcttItemShow subcttItemShowTemp =new SubcttItemShow();
        try{
            for(SubcttItemShow itemUnit: subcttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkid()).equals(strPkid)) {
                    subcttItemShowTemp =(SubcttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return subcttItemShowTemp;
    }
    /*在总包合同列表中根据编号找到项*/
    private SubcttItemShow getItemOfEsItemHieRelapByStrNo(
             String strNo,
             List<SubcttItemShow> subcttItemShowListPara){
        SubcttItemShow subcttItemShowTemp =null;
        try{
            for(SubcttItemShow itemUnit: subcttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    subcttItemShowTemp =(SubcttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return subcttItemShowTemp;
    }

    /*智能字段Start*/
    public SubcttItemService getSubcttItemService() {
        return subcttItemService;
    }
    public void setSubcttItemService(SubcttItemService subcttItemService) {
        this.subcttItemService = subcttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public SubcttItemShow getSubcttItemShowSel() {
        return subcttItemShowSel;
    }

    public void setSubcttItemShowSel(SubcttItemShow subcttItemShowSel) {
        this.subcttItemShowSel = subcttItemShowSel;
    }

    public SubcttItemShow getSubcttItemShow() {
        return subcttItemShowSel;
    }

    public void setSubcttItemShow(SubcttItemShow subcttItemShowSel) {
        this.subcttItemShowSel = subcttItemShowSel;
    }

    public SubcttItemShow getSubcttItemShowSelected() {
        return subcttItemShowSelected;
    }

    public void setSubcttItemShowSelected(SubcttItemShow subcttItemShowSelected) {
        this.subcttItemShowSelected = subcttItemShowSelected;
    }

    public List<SubcttItemShow> getSubcttItemShowList() {
        return subcttItemShowList;
    }

    public void setSubcttItemShowList(List<SubcttItemShow> subcttItemShowList) {
        this.subcttItemShowList = subcttItemShowList;
    }

    public String getStrBelongToPkid() {
        return strSubcttInfoPkid;
    }

    public void setStrBelongToPkid(String strSubcttInfoPkid) {
        this.strSubcttInfoPkid = strSubcttInfoPkid;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getStrPasteBtnRendered() {
        return strPasteBtnRendered;
    }

    public void setStrPasteBtnRendered(String strPasteBtnRendered) {
        this.strPasteBtnRendered = strPasteBtnRendered;
    }

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public StyleModel getStyleModelNo() {
        return styleModelNo;
    }

    public StyleModel getStyleModelCttAmount() {
        return styleModelCttAmount;
    }

    public StyleModel getStyleModelCttQty() {
        return styleModelCttQty;
    }

    public SubcttItemShow getSubcttItemShowAdd() {
        return subcttItemShowAdd;
    }

    public void setSubcttItemShowAdd(SubcttItemShow subcttItemShowAdd) {
        this.subcttItemShowAdd = subcttItemShowAdd;
    }

    public SubcttItemShow getSubcttItemShowUpd() {
        return subcttItemShowUpd;
    }

    public void setSubcttItemShowUpd(SubcttItemShow subcttItemShowUpd) {
        this.subcttItemShowUpd = subcttItemShowUpd;
    }


    public SubcttItemShow getSubcttItemShowDel() {
        return subcttItemShowDel;
    }

    public void setSubcttItemShowDel(SubcttItemShow subcttItemShowDel) {
        this.subcttItemShowDel = subcttItemShowDel;
    }

    /*智能字段End*/
}