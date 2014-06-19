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
import epss.repository.model.TkcttInfo;
import epss.repository.model.TkcttItem;
import epss.repository.model.model_show.TkcttItemShow;
import epss.service.SignPartService;
import epss.service.TkcttInfoService;
import epss.service.TkcttItemService;
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
public class TkcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttItemAction.class);
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

    private TkcttItemShow tkcttItemShowSel;
    private TkcttItemShow tkcttItemShowAdd;
    private TkcttItemShow tkcttItemShowUpd;
    private TkcttItemShow tkcttItemShowDel;
    private List<TkcttItem> tkcttItemList;

    private TkcttItemShow tkcttItemShowCopy;
    private TkcttItemShow tkcttItemShowPaste;
    private List<TkcttItemShow> tkcttItemShowList;
    /*列表中选择一行*/
    private TkcttItemShow tkcttItemShowSelected;
    /*所属号*/
    private String strTkcttInfoPkid;

    /*提交类型*/
    private String strSubmitType;
    private String strPasteType;

    /*控制控件在画面上的可用与现实Start*/
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    //显示的控制
    private String strPasteBtnRendered;
    private String strMngNotFinishFlag;
    /*控制控件在画面上的可用与现实End*/

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        strTkcttInfoPkid=parammap.get("strTkcttInfoPkid").toString();
        TkcttInfo tkcttInfoTemp =
                tkcttInfoService.getTkcttInfoByPkid(strTkcttInfoPkid);
        strMngNotFinishFlag="true";
        if(EnumFlowStatus.FLOW_STATUS0.getCode().equals(tkcttInfoTemp.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }
        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
        /*形成关系树*/
        tkcttItemList =new ArrayList<TkcttItem>();
        tkcttItemShowList =new ArrayList<TkcttItemShow>();
        tkcttItemList = tkcttItemService.getTkcttItemListByTkcttInfoPkid(strTkcttInfoPkid);
        recursiveDataTable("root", tkcttItemList);
        tkcttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(tkcttItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,List<TkcttItem> tkcttItemListPara){
        // 根据父层级号获得该父层级下的子节点
        List<TkcttItem> subTkcttItemList =new ArrayList<TkcttItem>();
        // 通过父层id查找它的孩子
        subTkcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, tkcttItemListPara);
        for(TkcttItem itemUnit: subTkcttItemList){
            TkcttItemShow tkcttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
            // 层级项
            tkcttItemShowTemp = new TkcttItemShow(
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
                strLastUpdByName,
                itemUnit.getUpdatedTime(),
                itemUnit.getRecVersion(),
                itemUnit.getRemark(),
                itemUnit.getTid()
            );
            tkcttItemShowList.add(tkcttItemShowTemp) ;
            recursiveDataTable(tkcttItemShowTemp.getPkid(), tkcttItemListPara);
        }
    }
    /*根据group和orderid临时编制编码strNo*/
    private List<TkcttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<TkcttItemShow> tkcttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(TkcttItemShow itemUnit: tkcttItemShowListPara){
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
        return tkcttItemShowListPara;
    }
    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<TkcttItemShow> tkcttItemShowListTemp =new ArrayList<TkcttItemShow>();
        tkcttItemShowListTemp.addAll(tkcttItemShowList);

        tkcttItemShowList.clear();
        // 小计
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);
        TkcttItemShow itemUnit=new TkcttItemShow();
        TkcttItemShow itemUnitNext=new TkcttItemShow();
        for(int i=0;i< tkcttItemShowListTemp.size();i++){
            itemUnit = tkcttItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmt()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmt()));
            tkcttItemShowList.add(itemUnit);

            if(i+1< tkcttItemShowListTemp.size()){
                itemUnitNext = tkcttItemShowListTemp.get(i+1);
                if(itemUnitNext.getParentPkid().equals("root")){
                    TkcttItemShow tkcttItemShowTemp =new TkcttItemShow();
                    tkcttItemShowTemp.setName("合计");
                    tkcttItemShowTemp.setPkid("total"+i);
                    tkcttItemShowTemp.setAmt(bdTotal);
                    tkcttItemShowList.add(tkcttItemShowTemp);
                    bdTotal=new BigDecimal(0);
                }
            } else if(i+1== tkcttItemShowListTemp.size()){
                itemUnitNext = tkcttItemShowListTemp.get(i);
                TkcttItemShow tkcttItemShowTemp =new TkcttItemShow();
                tkcttItemShowTemp.setName("合计");
                tkcttItemShowTemp.setPkid("total"+i);
                tkcttItemShowTemp.setAmt(bdTotal);
                tkcttItemShowList.add(tkcttItemShowTemp);
                bdTotal=new BigDecimal(0);

                // 总合计
                tkcttItemShowTemp =new TkcttItemShow();
                tkcttItemShowTemp.setName("总合计");
                tkcttItemShowTemp.setPkid("total_all"+i);
                tkcttItemShowTemp.setAmt(bdAllTotal);
                tkcttItemShowList.add(tkcttItemShowTemp);
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
        strPasteBtnRendered="false";
        strPasteType="";
        tkcttItemShowSel =new TkcttItemShow(strTkcttInfoPkid);
        tkcttItemShowAdd =new TkcttItemShow(strTkcttInfoPkid);
        tkcttItemShowUpd =new TkcttItemShow(strTkcttInfoPkid);
        tkcttItemShowDel =new TkcttItemShow(strTkcttInfoPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        tkcttItemShowAdd =new TkcttItemShow(strTkcttInfoPkid);
    }

    /*提交前的检查：必须项的输入*/
    private Boolean subMitActionPreCheck(){
        TkcttItemShow tkcttItemShowTemp =new TkcttItemShow(strTkcttInfoPkid);
        if (strSubmitType.equals("Add")){
            tkcttItemShowTemp = tkcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            tkcttItemShowTemp = tkcttItemShowUpd;
        }
        if (StringUtils.isEmpty(tkcttItemShowTemp.getStrNo())) {
            MessageUtil.addError("请输入编号！");
            return false;
        }
        if (StringUtils.isEmpty(tkcttItemShowTemp.getName())) {
            MessageUtil.addError("请输入名称！");
            return false;
        }
        return true;
    }

    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")){
            if(tkcttItemShowAdd.getUnitPrice()==null|| tkcttItemShowAdd.getQty()==null){
                bigDecimal=null;
            }else{
                bigDecimal = tkcttItemShowAdd.getUnitPrice().multiply(tkcttItemShowAdd.getQty());
            }
            tkcttItemShowAdd.setAmt(bigDecimal);
        }
        if (strSubmitType.equals("Upd")){
            if(tkcttItemShowUpd.getUnitPrice()==null|| tkcttItemShowUpd.getQty()==null){
                bigDecimal=null;
            }else{
                bigDecimal = tkcttItemShowUpd.getUnitPrice().multiply(tkcttItemShowUpd.getQty());
            }
            tkcttItemShowUpd.setAmt(bigDecimal);
        }
    }

    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            if(strSubmitTypePara.equals("Add")) {
                return;
            }
            if(tkcttItemShowSelected.getStrNo()==null){
                MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                return;
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                tkcttItemShowSel = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected);
                tkcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowSel.getStrNo())) ;
            }
            if(strSubmitTypePara.equals("Upd")){
                strPasteBtnRendered="false";
                tkcttItemShowUpd =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowUpd.getStrNo())) ;
                tkcttItemShowCopy =null;
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Del")){
                strPasteBtnRendered="false";
                tkcttItemShowDel =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowDel.getStrNo())) ;
                tkcttItemShowCopy =null;
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Copy")){
                strPasteBtnRendered="true";
                tkcttItemShowCopy =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowSel.getStrNo())) ;
                strPasteType="Copy";
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Cut")){
                strPasteBtnRendered="true";
                tkcttItemShowCopy =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowSel.getStrNo())) ;
                strPasteType="Cut";
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.contains("Paste")){
                strPasteBtnRendered="false";
                tkcttItemShowPaste = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel =new TkcttItemShow(strTkcttInfoPkid);
                pasteAction();
            }
        } catch (Exception e) {
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*删除*/
    public void delThisRecordAction(){
        try {
            int deleteRecordNum= tkcttItemService.deleteRecord(tkcttItemShowDel.getPkid()) ;
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            tkcttItemService.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                    tkcttItemShowDel.getTkcttInfoPkid(),
                    tkcttItemShowDel.getParentPkid(),
                    tkcttItemShowDel.getGrade());
            MessageUtil.addInfo("删除数据完成。");
            initData();
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrNoToGradeAndOrderidAction(){
        TkcttItemShow tkcttItemShowTemp =new TkcttItemShow(strTkcttInfoPkid);
        if (strSubmitType.equals("Add")){
            tkcttItemShowTemp = tkcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            tkcttItemShowTemp = tkcttItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowTemp.getStrNo());
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
            if(getItemOfEsItemHieRelapByStrNo(strIgnoreSpaceOfStr, tkcttItemShowList)!=null){
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<TkcttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid("root", tkcttItemList);

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
            tkcttItemShowTemp.setGrade(1) ;
            tkcttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            tkcttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            TkcttItemShow tkcttItemShowTemp1 =new TkcttItemShow();
            tkcttItemShowTemp1 =getItemOfEsItemHieRelapByStrNo(strParentNo, tkcttItemShowList);
            if(tkcttItemShowTemp1 ==null|| tkcttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("请确认输入的编码！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            }
            else{
                List<TkcttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid(
                        tkcttItemShowTemp1.getPkid(),
                        tkcttItemList);
                if(itemHieRelapListSubTemp .size() ==0){
                    if(!tkcttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
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
                tkcttItemShowTemp.setGrade(strTemps.length) ;
                tkcttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                tkcttItemShowTemp.setParentPkid(tkcttItemShowTemp1.getPkid()) ;
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
                    tkcttItemService.updateRecord(tkcttItemShowUpd) ;
                }
                else if(strSubmitType .equals("Add")) {
                    TkcttItem cttItemTemp = tkcttItemService.fromShowModelToModel(tkcttItemShowAdd);
                    if (tkcttItemService.isExistSameRecordInDb(cttItemTemp)){
                        MessageUtil.addInfo("该编号对应记录已存在，请重新录入。");
                        return;
                    }
                    tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                            tkcttItemShowAdd.getTkcttInfoPkid(),
                            tkcttItemShowAdd.getParentPkid(),
                            tkcttItemShowAdd.getGrade(),
                            tkcttItemShowAdd.getOrderid());
                    tkcttItemService.insertRecord(tkcttItemShowAdd);
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
            TkcttItemShow tkcttItemShowCopyTemp =new TkcttItemShow() ;
            if(strPasteType.equals("Copy")){
                /*复制的对象*/
                tkcttItemShowCopyTemp = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowCopy);
                /*复制的目标位置*/
                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                if(strSubmitType .equals("Paste_brother_up")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid();
                }else if(strSubmitType.equals("Paste_brother_down")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid()+1;
                }else if(strSubmitType.equals("Paste_children")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= tkcttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                                    tkcttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                tkcttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                tkcttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*更新复制的目标位置以后的数据*/
                tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                        tkcttItemShowCopyTemp.getParentPkid(),
                        tkcttItemShowCopyTemp.getGrade(),
                        tkcttItemShowCopyTemp.getOrderid());
                /*在复制的目标位置处插入复制的对象：esItemHieRelapTemp中的Pkid随机生成不同于以前的了*/
                tkcttItemService.insertRecord(tkcttItemShowCopyTemp);

                /*剪切前后层级变化的数*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= tkcttItemShowPaste.getGrade() - tkcttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (tkcttItemShowPaste.getGrade()+1) - tkcttItemShowCopy.getGrade() ;
                }

                /*遍历复制对象的子节点数据Start*/
                 /*记录新插入的当前条，因为Pkid随着自动生成已经变化了*/
                TkcttItemShow tkcttItemShowTemp =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowCopy);
                /*复制总包合同列表数据用*/
                List<TkcttItemShow> tkcttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTemp为排好序的总包合同信息列表*/
                tkcttItemShowListTemp.addAll(tkcttItemShowList);
                /*记录之前节点用*/
                TkcttItemShow tkcttItemShowBefore =new TkcttItemShow();
                Boolean isBegin=false;
                for(TkcttItemShow itemUnit: tkcttItemShowListTemp){
                    TkcttItemShow tkcttItemShowNewInsert =(TkcttItemShow) BeanUtils.cloneBean(itemUnit);
                     /*找到剪切对象后，寻找该对象其子节点（层级号大于该对象的层级号的数据）并进行复制*/
                    if(isBegin.equals(true) &&
                            ToolUtil.getIntIgnoreNull(tkcttItemShowNewInsert.getGrade())<= tkcttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        /*同层的情况下*/
                        if(tkcttItemShowNewInsert.getGrade().equals(tkcttItemShowBefore.getGrade())){
                            /*设置本条数据的父亲节点号为刚才插入数据的父亲节点号*/
                            tkcttItemShowNewInsert.setParentPkid(tkcttItemShowTemp.getParentPkid());
                        }
                        /*子层层的情况下*/
                        else{
                            /*设置本条数据的父亲节点号为刚才插入数据的节点号*/
                            tkcttItemShowNewInsert.setParentPkid(tkcttItemShowTemp.getPkid());
                        }
                        /*设置本条数据的层级号为原先的层级号加变换前后的层级差*/
                        tkcttItemShowNewInsert.setGrade(tkcttItemShowNewInsert.getGrade()+intGradeGap);
                        /*插入新数据*/
                        tkcttItemService.insertRecord(tkcttItemShowNewInsert);
                        /*记录新插入的当前条，因为Pkid随着自动生成已经变化了*/
                        tkcttItemShowTemp =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowNewInsert);
                        /*记录当前条数据*/
                        tkcttItemShowBefore =itemUnit;
                    }
                    /*找到复制对象*/
                    if(tkcttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }
                /*遍历复制对象的子节点数据End*/
            }
            else if (strPasteType.equals("Cut")){
                /*粘贴的操作*/
                /*修改目标节点的父子关系及层级号和序号*/
                tkcttItemShowCopyTemp = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowCopy);

                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                if(strSubmitType .equals("Paste_brother_up")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid();
                }else if(strSubmitType.equals("Paste_brother_down")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid()+1;
                }else if(strSubmitType.equals("Paste_children")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= tkcttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                                    tkcttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                tkcttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                tkcttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*1更新剪切的目标位置以后的数据*/
                tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                        tkcttItemShowCopyTemp.getParentPkid(),
                        tkcttItemShowCopyTemp.getGrade(),
                        tkcttItemShowCopyTemp.getOrderid());
                /*2修改本条数据的ParentPkid，Grade,Orderid*/
                tkcttItemService.updateRecord(tkcttItemShowCopyTemp) ;
                /*3修改子节点的层级关系*/
                /*剪切前后层级变化的数*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= tkcttItemShowPaste.getGrade() - tkcttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (tkcttItemShowPaste.getGrade()+1) - tkcttItemShowCopy.getGrade() ;
                }

                /*复制总包合同列表数据用*/
                List<TkcttItemShow> tkcttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTemp为排好序的总包合同信息列表*/
                tkcttItemShowListTemp.addAll(tkcttItemShowList);
                Boolean isBegin=false;
                for(TkcttItemShow itemUnit: tkcttItemShowListTemp){
                    /*找到剪切对象后，寻找该对象其子节点（层级号大于该对象的层级号的数据）并进行复制*/
                    if(isBegin.equals(true) && itemUnit.getGrade()<= tkcttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        itemUnit.setGrade(itemUnit.getGrade()+intGradeGap);
                        tkcttItemService.updateRecord(itemUnit) ;
                    }
                    /*找到剪切对象*/
                    if(tkcttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }

                /*剪切的操作*/
                tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                        tkcttItemShowCopyTemp.getParentPkid(),
                        tkcttItemShowCopyTemp.getGrade(),
                        tkcttItemShowCopyTemp.getOrderid());
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
    private List<TkcttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
             List<TkcttItem> tkcttItemListPara) {
        List<TkcttItem> tempTkcttItemList =new ArrayList<TkcttItem>();
        /*避开重复链接数据库*/
        for(TkcttItem itemUnit: tkcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempTkcttItemList.add(itemUnit);
            }
        }
        return tempTkcttItemList;
    }
    /*层级关系列表中通过Pkid的该项*/
    private TkcttItemShow getItemOfEsItemHieRelapByPkid(String strPkid,
              List<TkcttItemShow> tkcttItemShowListPara){
        TkcttItemShow tkcttItemShowTemp =new TkcttItemShow();
        try{
            for(TkcttItemShow itemUnit: tkcttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkid()).equals(strPkid)) {
                    tkcttItemShowTemp =(TkcttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return tkcttItemShowTemp;
    }
    /*在总包合同列表中根据编号找到项*/
    private TkcttItemShow getItemOfEsItemHieRelapByStrNo(
             String strNo,
             List<TkcttItemShow> tkcttItemShowListPara){
        TkcttItemShow tkcttItemShowTemp =null;
        try{
            for(TkcttItemShow itemUnit: tkcttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    tkcttItemShowTemp =(TkcttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return tkcttItemShowTemp;
    }
    private Boolean strNoBlurFalse(){
        tkcttItemShowSel.setPkid("") ;
        tkcttItemShowSel.setParentPkid("");
        tkcttItemShowSel.setGrade(null);
        tkcttItemShowSel.setOrderid(null);
        return false;
    }

    /*智能字段Start*/
    public TkcttItemService getTkcttItemService() {
        return tkcttItemService;
    }
    public void setTkcttItemService(TkcttItemService tkcttItemService) {
        this.tkcttItemService = tkcttItemService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }
    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public TkcttItemShow getTkcttItemShowSel() {
        return tkcttItemShowSel;
    }

    public void setTkcttItemShowSel(TkcttItemShow tkcttItemShowSel) {
        this.tkcttItemShowSel = tkcttItemShowSel;
    }

    public TkcttItemShow getTkcttItemShowSelected() {
        return tkcttItemShowSelected;
    }
    public void setTkcttItemShowSelected(TkcttItemShow tkcttItemShowSelected) {
        this.tkcttItemShowSelected = tkcttItemShowSelected;
    }
    public List<TkcttItemShow> getTkcttItemShowList() {
        return tkcttItemShowList;
    }
    public void setTkcttItemShowList(List<TkcttItemShow> tkcttItemShowList) {
        this.tkcttItemShowList = tkcttItemShowList;
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

    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
    }

    public TkcttItemShow getTkcttItemShowAdd() {
        return tkcttItemShowAdd;
    }

    public void setTkcttItemShowAdd(TkcttItemShow tkcttItemShowAdd) {
        this.tkcttItemShowAdd = tkcttItemShowAdd;
    }

    public TkcttItemShow getTkcttItemShowDel() {
        return tkcttItemShowDel;
    }

    public void setTkcttItemShowDel(TkcttItemShow tkcttItemShowDel) {
        this.tkcttItemShowDel = tkcttItemShowDel;
    }

    public TkcttItemShow getTkcttItemShowUpd() {
        return tkcttItemShowUpd;
    }

    public void setTkcttItemShowUpd(TkcttItemShow tkcttItemShowUpd) {
        this.tkcttItemShowUpd = tkcttItemShowUpd;
    }
    /*智能字段End*/
}