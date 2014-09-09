package epss.view.contract;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.repository.model.model_show.AttachmentModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.common.enums.*;
import epss.repository.model.*;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
import epss.service.*;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class TkcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    private EsCttInfo cttInfo;
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    private List<EsCttItem> esCttItemList;
    private List<CttItemShow> cttItemShowList;

    //附件
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;
    private UploadedFile uploadedFile;

    /*所属类型*/
    private String strBelongToType;
    /*所属号*/
    private String strTkcttInfoPkid;

    /*提交类型*/
    private String strSubmitType;

    /*控制控件在画面上的可用与现实Start*/
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    //显示的控制
    private String strPassFlag;
    private String strNotPassToStatus;
    private String strFlowType;
    private Boolean checkForUpd;
    private List<CttItemShow> cttItemShowListExcel;
    private Map beansMap;
    @PostConstruct
    public void init() {
        esCttItemList =new ArrayList<>();
        cttItemShowList =new ArrayList<>();
        attachmentList=new ArrayList<>();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        beansMap = new HashMap();
        strBelongToType = ESEnum.ITEMTYPE0.getCode();
        if (parammap.containsKey("strTkcttInfoPkid")) {
            strTkcttInfoPkid = parammap.get("strTkcttInfoPkid").toString();
            cttInfo = cttInfoService.getCttInfoByPkId(strTkcttInfoPkid);
        }
        if (parammap.containsKey("strFlowType")) {
            strFlowType = parammap.get("strFlowType").toString();
        }

        strPassFlag = "true";
        if ("Mng".equals(strFlowType) && ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(cttInfo.getFlowStatus())) {
            strPassFlag = "false";
        }
        resetAction();
        initData();
    }

    /*初始化操作*/
    private void initData() {
        /*形成关系树*/
        try {
        /*初始化流程状态列表*/
            if(ToolUtil.getStrIgnoreNull(strFlowType).length()!=0&&
                    ToolUtil.getStrIgnoreNull(strTkcttInfoPkid).length()!=0) {
                esFlowControl.getBackToStatusFlagList(strFlowType);
                // 附件记录变成List
                attachmentList=attachmentStrToList(cttInfo.getAttachment());
                // 输出Excel表头
                beansMap.put("tkcttInfo", cttInfo);
                esCttItemList = cttItemService.getEsItemList(
                        strBelongToType, strTkcttInfoPkid);
                recursiveDataTable("root", esCttItemList);
                cttItemShowList = getTkcttItemList_DoFromatNo(cttItemShowList);
                cttItemShowListExcel = new ArrayList<CttItemShow>();
                for (CttItemShow itemUnit : cttItemShowList) {
                    CttItemShow itemUnitTemp = (CttItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    cttItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cttItemShowListExcel", cttItemShowListExcel);
                setTkcttItemList_AddTotal();
                beansMap.put("cttItemShowList", cttItemShowList);
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId, List<EsCttItem> esCttItemListPara) {
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList = new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList = getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        for (EsCttItem itemUnit : subEsCttItemList) {
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName = ToolUtil.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName = ToolUtil.getUserName(itemUnit.getLastUpdBy());
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
            cttItemShowList.add(cttItemShowTemp);
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara);
        }
    }

    /*根据group和orderid临时编制编号strNo*/
    private List<CttItemShow> getTkcttItemList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara) {
        String strTemp = "";
        Integer intBeforeGrade = -1;
        for (CttItemShow itemUnit : cttItemShowListPara) {
            if (itemUnit.getGrade().equals(intBeforeGrade)) {
                if (strTemp.lastIndexOf(".") < 0) {
                    strTemp = itemUnit.getOrderid().toString();
                } else {
                    strTemp = strTemp.substring(0, strTemp.lastIndexOf(".")) + "." + itemUnit.getOrderid().toString();
                }
            } else {
                if (itemUnit.getGrade() == 1) {
                    strTemp = itemUnit.getOrderid().toString();
                } else {
                    if (!itemUnit.getGrade().equals(intBeforeGrade)) {
                        if (itemUnit.getGrade().compareTo(intBeforeGrade) > 0) {
                            strTemp = strTemp + "." + itemUnit.getOrderid().toString();
                        } else {
                            Integer intTemp = ToolUtil.lookIndex(strTemp, '.', itemUnit.getGrade() - 1);
                            strTemp = strTemp.substring(0, intTemp);
                            strTemp = strTemp + "." + itemUnit.getOrderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade = itemUnit.getGrade();
            itemUnit.setStrNo(ToolUtil.padLeft_DoLevel(itemUnit.getGrade(), strTemp));
        }
        return cttItemShowListPara;
    }

    private void setTkcttItemList_AddTotal() {
        List<CttItemShow> cttItemShowListTemp = new ArrayList<CttItemShow>();
        cttItemShowListTemp.addAll(cttItemShowList);

        cttItemShowList.clear();
        // 小计
        BigDecimal bdTotal = new BigDecimal(0);
        BigDecimal bdAllTotal = new BigDecimal(0);
        CttItemShow itemUnit = new CttItemShow();
        CttItemShow itemUnitNext = new CttItemShow();
        for (int i = 0; i < cttItemShowListTemp.size(); i++) {
            itemUnit = cttItemShowListTemp.get(i);
            bdTotal = bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            bdAllTotal = bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            cttItemShowList.add(itemUnit);

            if (i + 1 < cttItemShowListTemp.size()) {
                itemUnitNext = cttItemShowListTemp.get(i + 1);
                if (itemUnitNext.getParentPkid().equals("root")) {
                    CttItemShow cttItemShowTemp = new CttItemShow();
                    cttItemShowTemp.setName("合计");
                    cttItemShowTemp.setPkid("total" + i);
                    cttItemShowTemp.setContractAmount(bdTotal);
                    cttItemShowList.add(cttItemShowTemp);
                    bdTotal = new BigDecimal(0);
                }
            } else if (i + 1 == cttItemShowListTemp.size()) {
                itemUnitNext = cttItemShowListTemp.get(i);
                CttItemShow cttItemShowTemp = new CttItemShow();
                cttItemShowTemp.setName("合计");
                cttItemShowTemp.setPkid("total" + i);
                cttItemShowTemp.setContractAmount(bdTotal);
                cttItemShowList.add(cttItemShowTemp);
                bdTotal = new BigDecimal(0);

                // 总合计
                cttItemShowTemp = new CttItemShow();
                cttItemShowTemp.setName("总合计");
                cttItemShowTemp.setPkid("total_all" + i);
                cttItemShowTemp.setContractAmount(bdAllTotal);
                cttItemShowList.add(cttItemShowTemp);
            }
        }
    }

    /*重置*/
    public void resetAction() {
        strSubmitType = "Add";
        styleModelNo = new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel = new CttItemShow(strBelongToType, strTkcttInfoPkid);
        cttItemShowAdd = new CttItemShow(strBelongToType, strTkcttInfoPkid);
        cttItemShowUpd = new CttItemShow(strBelongToType, strTkcttInfoPkid);
        cttItemShowDel = new CttItemShow(strBelongToType, strTkcttInfoPkid);
    }

    public void resetActionForAdd() {
        strSubmitType = "Add";
        cttItemShowAdd = new CttItemShow(strBelongToType, strTkcttInfoPkid);
    }

    /*提交前的检查：必须项的输入*/
    private Boolean subMitActionPreCheck() {
        CttItemShow cttItemShowTemp = new CttItemShow(strBelongToType, strTkcttInfoPkid);
        if (strSubmitType.equals("Add")) {
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
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
        if ((cttItemShowTemp.getContractUnitPrice() != null &&
                cttItemShowTemp.getContractUnitPrice().compareTo(BigDecimal.ZERO) != 0) ||
                (cttItemShowTemp.getContractQuantity() != null &&
                        cttItemShowTemp.getContractQuantity().compareTo(BigDecimal.ZERO) != 0)) {
            /*绑定前台控件,可输入的BigDecimal类型本来为null的，自动转换为0，不可输入的，还是null*/
            if (StringUtils.isEmpty(cttItemShowTemp.getUnit())) {
                MessageUtil.addError("请输入单位！");
                return false;
            }
        }
        return true;
    }

    public void blurCalculateAmountAction() {
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

    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara, CttItemShow cttItemShowPara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                return;
            }
            strSubmitType = strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")) {
                cttItemShowSel = (CttItemShow) BeanUtils.cloneBean(cttItemShowPara);
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo()));
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if (strSubmitTypePara.equals("Upd")) {
                cttItemShowUpd = (CttItemShow) BeanUtils.cloneBean(cttItemShowPara);
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo()));
            } else if (strSubmitTypePara.equals("Del")) {
                cttItemShowDel = (CttItemShow) BeanUtils.cloneBean(cttItemShowPara);
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo()));
            }
        } catch (Exception e) {
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*删除*/
    public void delThisRecordAction(CttItemShow cttItemShowPara) {
        try {
            int deleteRecordNum = cttItemService.deleteRecord(cttItemShowPara.getPkid());
            if (deleteRecordNum <= 0) {
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            cttItemService.setAfterThisOrderidSubOneByNode(
                    cttItemShowPara.getBelongToType(),
                    cttItemShowPara.getBelongToPkid(),
                    cttItemShowPara.getParentPkid(),
                    cttItemShowPara.getGrade(),
                    cttItemShowPara.getOrderid());
            MessageUtil.addInfo("删除数据完成。");
            initData();
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrNoToGradeAndOrderidAction() {
        CttItemShow cttItemShowTemp = new CttItemShow(strBelongToType, strTkcttInfoPkid);
        if (strSubmitType.equals("Add")) {
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")) {
            cttItemShowTemp = cttItemShowUpd;
        }
        String strIgnoreSpaceOfStr = ToolUtil.getIgnoreSpaceOfStr(cttItemShowTemp.getStrNo());
        if (StringUtils.isEmpty(strIgnoreSpaceOfStr)) {
            return true;
        }
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (!strIgnoreSpaceOfStr.matches(strRegex)) {
            MessageUtil.addError("请确认输入的编号，编号" + strIgnoreSpaceOfStr + "格式不正确！");
            return strNoBlurFalse();
        }

        //该编码已经存在
        if(!strSubmitType.equals("Upd")){
            if(getEsCttItemByStrNo(strIgnoreSpaceOfStr, cttItemShowList)!=null){
            }
            else{ //该编码不存在
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if (intLastIndexof < 0) {
            List<EsCttItem> itemHieRelapListSubTemp = new ArrayList<>();
            itemHieRelapListSubTemp = getEsCttItemListByParentPkid("root", esCttItemList);

            if (itemHieRelapListSubTemp.size() == 0) {
                if (!strIgnoreSpaceOfStr.equals("1")) {
                    MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入1！");
                    return strNoBlurFalse();
                }
            } else {
                if (itemHieRelapListSubTemp.size() + 1 < Integer.parseInt(strIgnoreSpaceOfStr)) {
                    MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入" + (itemHieRelapListSubTemp.size() + 1) + "！");
                    return strNoBlurFalse();
                }
            }
            cttItemShowTemp.setGrade(1) ;
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        } else {
            String strParentNo = strIgnoreSpaceOfStr.substring(0, intLastIndexof);
            CttItemShow cttItemShowTemp1 = new CttItemShow();
            cttItemShowTemp1 = getEsCttItemByStrNo(strParentNo, cttItemShowList);
            if (cttItemShowTemp1 == null || cttItemShowTemp1.getPkid() == null) {
                MessageUtil.addError("请确认输入的编号！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            } else {
                List<EsCttItem> itemHieRelapListSubTemp = new ArrayList<>();
                itemHieRelapListSubTemp = getEsCttItemListByParentPkid(
                        cttItemShowTemp1.getPkid(),
                        esCttItemList);
                if (itemHieRelapListSubTemp.size() == 0) {
                    if (!cttItemShowTemp.getStrNo().equals(strParentNo + ".1")) {
                        MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入" + strParentNo + ".1！");
                        return strNoBlurFalse();
                    }
                } else {
                    String strOrderid = strIgnoreSpaceOfStr.substring(intLastIndexof + 1);
                    if (itemHieRelapListSubTemp.size() + 1 < Integer.parseInt(strOrderid)) {
                        MessageUtil.addError("请确认输入的编号！该编号不符合规范，应输入" + strParentNo + "." +
                                (itemHieRelapListSubTemp.size() + 1) + "！");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[] = strIgnoreSpaceOfStr.split("\\.");
                cttItemShowTemp.setGrade(strTemps.length);
                cttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length - 1]));
                cttItemShowTemp.setParentPkid(cttItemShowTemp1.getPkid());
            }
        }
        return true ;
    }
    public void submitThisRecordAction(){
        try{
            /*提交前的检查*/
            if(strSubmitType .equals("Del")) {
                if(ToolUtil.getStrIgnoreNull(cttItemShowDel.getStrNo()).length()==0){
                    MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                    return;
                }
                delThisRecordAction(cttItemShowDel);
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct的grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderidAction()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
                    if(ToolUtil.getStrIgnoreNull(cttItemShowUpd.getStrNo()).length()==0){
                        MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                        return;
                    }
                    cttItemService.updateRecord(cttItemShowUpd) ;
                    checkForUpd = true;
                }
                else if(strSubmitType .equals("Add")) {
                     EsCttItem esCttItemTemp= cttItemService.fromModelShowToModel(cttItemShowAdd);
                    if (cttItemService.isExistSameRecordInDb(esCttItemTemp)){
                        MessageUtil.addInfo("该编号对应记录已存在，请重新录入。");
                        return;
                    }
                    cttItemService.setAfterThisOrderidPlusOneByNode(
                            cttItemShowAdd.getBelongToType(),
                            cttItemShowAdd.getBelongToPkid(),
                            cttItemShowAdd.getParentPkid(),
                            cttItemShowAdd.getGrade(),
                            cttItemShowAdd.getOrderid());
                    cttItemService.insertRecord(cttItemShowAdd);
                    resetAction();
                }
                MessageUtil.addInfo("提交数据完成。");
                initData();
            }
        } catch (Exception e) {
            checkForUpd = false;
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
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
    /*在总包合同列表中根据编号找到项*/
    private CttItemShow getEsCttItemByStrNo(
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

    /**
     * 根据权限进行审核
     *
     * @param strPowerTypePara
     */
    public void onClickForPowerAction(String strPowerTypePara) {
        try {
            strPowerTypePara=strFlowType+strPowerTypePara;
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    // 状态标志：初始
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // 原因：录入完毕
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG0.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfo.setFlowStatus(null);
                    cttInfo.setFlowStatusReason(null);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // 原因：审核通过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    cttInfo.setFlowStatus(null);
                    // 原因：审核未过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                        cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    }else if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }
                    // 原因：复核未过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据复核未过！");
                }
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // 原因：批准通过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据批准通过！");
                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // 检查是否被使用
                    String strCttTypeTemp = "";
                    if (cttInfo.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE1.getCode();
                    } else if (cttInfo.getCttType().equals(ESEnum.ITEMTYPE1.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE2.getCode();
                    }

                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())) {
                        cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    }else if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())) {
                        cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    }else if(strNotPassToStatus.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }
                    // 原因：批准未过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());

                    cttInfoService.updateRecord(cttInfo);

                    List<EsInitStl> esInitStlListTemp =
                            esFlowService.selectIsUsedInQMPBySubcttPkid(cttInfo.getPkid());
                    if (esInitStlListTemp.size() > 0) {
                        MessageUtil.addInfo("该数据已经被["
                                + ESEnum.getValueByKey(esInitStlListTemp.get(0).getStlType()).getTitle()
                                + "]使用，数据批准未过,请慎重编辑！");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cttInfo.getPkid()) > 0) {
                            MessageUtil.addInfo("该数据已经被[" + ESEnum.getValueByKey(strCttTypeTemp).getTitle()
                                    + "]使用，数据批准未过,请慎重编辑！");
                        } else {
                            MessageUtil.addInfo("数据批准未过！");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public String onExportExcel()throws IOException, WriteException {
        if (this.cttItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "总包合同-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"tkctt.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }

    // 附件
    public List<AttachmentModel> attachmentStrToList(String strAttachmentPara){
        List<AttachmentModel> attachmentListTemp=new ArrayList<>();
        if(strAttachmentPara!=null){
            attachmentListTemp.clear();
            if (!StringUtils.isEmpty(strAttachmentPara)) {
                String strTemps[] = strAttachmentPara.split(";");
                for (int i = 0; i < strTemps.length; i++) {
                    AttachmentModel attachmentModelTemp = new AttachmentModel(i + "", strTemps[i], strTemps[i]);
                    attachmentListTemp.add(attachmentModelTemp);
                }
            }
        }
        return attachmentListTemp;
    }

    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/" + attachmentModelPara.getCOLUMN_NAME());
    }

    public void delAttachmentRecordAction(AttachmentModel attachmentModelPara){
        try {
            File deletingFile = new File(attachmentModelPara.getCOLUMN_PATH());
            deletingFile.delete();
            attachmentList.remove(attachmentModelPara) ;
            StringBuffer sbTemp = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sbTemp.append(item.getCOLUMN_PATH() + ";");
            }
            cttInfo.setAttachment(sbTemp.toString());
            cttInfoService.updateRecord(cttInfo);
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void download(String strAttachment){
        try{
            if(StringUtils .isEmpty(strAttachment) ){
                MessageUtil.addError("路径为空，无法下载！");
                logger.error("路径为空，无法下载！");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("下载文件失败", e);
            MessageUtil.addError("下载文件失败,"+e.getMessage()+strAttachment);
        }
    }

    public void upload(FileUploadEvent event) {
        BufferedInputStream inStream = null;
        FileOutputStream fileOutputStream = null;
        UploadedFile uploadedFile = event.getFile();
        AttachmentModel attachmentModel = new AttachmentModel();
        if (uploadedFile != null) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload");
            File superFile = new File(path);
            if (!superFile.exists()) {
                superFile.mkdirs();
            }
            File descFile = new File(superFile, uploadedFile.getFileName());
            attachmentModel.setCOLUMN_ID(ToolUtil.getIntIgnoreNull(attachmentList.size()) + "");
            attachmentModel.setCOLUMN_NAME(uploadedFile.getFileName());
            attachmentModel.setCOLUMN_PATH(descFile.getAbsolutePath());
            for (AttachmentModel item : attachmentList){
                if (item.getCOLUMN_NAME().equals(attachmentModel.getCOLUMN_NAME())) {
                    MessageUtil.addError("附件已存在！");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("附件路径("+sb.toString()+")长度已超过最大允许值4000，不能入库，请联系系统管理员！");
                return;
            }
            cttInfo.setAttachment(sb.toString());
            cttInfoService.updateRecord(cttInfo);
            try {
                inStream = new BufferedInputStream(uploadedFile.getInputstream());
                fileOutputStream = new FileOutputStream(descFile);
                byte[] buf = new byte[1024];
                int num;
                while ((num = inStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /*智能字段Start*/
    public CttItemService getCttItemService() {
        return cttItemService;
    }
    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
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
    public List<CttItemShow> getCttItemShowList() {
        return cttItemShowList;
    }
    public void setCttItemShowList(List<CttItemShow> cttItemShowList) {
        this.cttItemShowList = cttItemShowList;
    }
    public String getStrBelongToPkid() {
        return strTkcttInfoPkid;
    }
    public void setStrBelongToPkid(String strTkcttInfoPkid) {
        this.strTkcttInfoPkid = strTkcttInfoPkid;
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
    public String getStrPassFlag() {
        return strPassFlag;
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
    public EsFlowService getEsFlowService() {
        return esFlowService;
    }
    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }
    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }
    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }
    public String getStrFlowType() {
        return strFlowType;
    }
    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }
    public EsCttInfo getCttInfo() {
        return cttInfo;
    }
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }
    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }
    public Boolean getCheckForUpd() {
        return checkForUpd;
    }
    public List<CttItemShow> getCttItemShowListExcel() {
        return cttItemShowListExcel;
    }
    public void setCttItemShowListExcel(List<CttItemShow> cttItemShowListExcel) {
        this.cttItemShowListExcel = cttItemShowListExcel;
    }
    //文件
    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }
    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }
    public HtmlGraphicImage getImage() {
        return image;
    }
    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }
    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    /*智能字段End*/
}