package epss.view.ctt;

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
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttItem;
import epss.repository.model.EsInitStl;
import epss.repository.model.model_show.CstplItemShow;
import epss.repository.model.model_show.CttItemShow;
import epss.service.*;
import epss.service.CttInfoService;
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
public class CstplItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplItemAction.class);
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;

    private EsCttInfo cttInfo;
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    /*列表显示用*/
    private List<CstplItemShow> cstplItemShowList;

    /*所属类型*/
    private String strBelongToType;
    /*所属号*/
    private String strCttInfoPkid;

    /*提交类型*/
    private String strSubmitType;
    private String strPassFlag;
    private String strNotPassToStatus;
    private String strFlowType;
    /*控制控件在画面上的可用与现实Start*/
    //显示的控制
    private StyleModel styleModel;
    /*控制控件在画面上的可用与现实End*/
    private List<CstplItemShow> cstplItemShowListExcel;
    private Map beansMap;

    //附件
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;
    private UploadedFile uploadedFile;

    @PostConstruct
    public void init() {
        this.attachmentList=new ArrayList<AttachmentModel>();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        beansMap = new HashMap();
        strBelongToType=ESEnum.ITEMTYPE1.getCode();
        if (parammap.containsKey("strCttInfoPkid")) {
            strCttInfoPkid = parammap.get("strCttInfoPkid").toString();
            cttInfo = cttInfoService.getCttInfoByPkId(strCttInfoPkid);
        }
        if (parammap.containsKey("strFlowType")) {
            strFlowType = parammap.get("strFlowType").toString();
        }

        strPassFlag = "true";
        if ("Mng".equals(strFlowType) && ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(cttInfo.getFlowStatus())) {
            strPassFlag = "false";
        }

        resetAction();
        initData() ;
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
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfo.setFlowStatus(null);
                    cttInfo.setFlowStatusReason(null);
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // 原因：审核通过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    cttInfo.setFlowStatus(null);
                    // 原因：审核未过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // 原因：复核通过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
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
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
                    MessageUtil.addInfo("数据复核未过！");
                }
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    cttInfo.setFlowStatus(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // 原因：批准通过
                    cttInfo.setFlowStatusReason(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);
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
                    cttInfoService.updateRecord(cttInfo,strPowerTypePara);

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

    public void resetAction(){
        strSubmitType="Add";
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strCttInfoPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttItemShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cttItemShowSel.getName()).equals("")){
            Integer intIndex= esCommon.getIndexOfCstplItemNamelist(cttItemShowSel.getName());
            if(intIndex>=0){
                cttItemShowSel.setUnit(null);
                cttItemShowSel.setContractUnitPrice(null);
                cttItemShowSel.setContractQuantity(null);
                cttItemShowSel.setStrCorrespondingItemNo(null);
                cttItemShowSel.setCorrespondingPkid(null);
            }
        }
        return true;
    }

    public void selectRecordAction(String strSubmitTypePara,
                                   CstplItemShow cstplItemShowPara) {
        try {
            String  strSubmitTypeBefore= strSubmitType;
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                if(cstplItemShowPara.getBelongToTypeContrast()==null) {
                    cttItemShowSel =new CttItemShow(strBelongToType ,strCttInfoPkid);
                }else{
                    cttItemShowSel =getItemOfEsItemHieRelapByItem(cstplItemShowPara,"Cstpl");
                }
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemShowUpd = getItemOfEsItemHieRelapByItem(cstplItemShowPara, "Cstpl");
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo()));
                cttItemShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrCorrespondingItemNo()));
            }
            else if(strSubmitTypePara.equals("Del")){
                if(cstplItemShowPara.getStrNoContrast()==null) {
                    MessageUtil.addInfo("没有可删除的数据！");
                    cttItemShowDel =new CttItemShow(strBelongToType ,strCttInfoPkid);
                    return;
                }
                else{
                    cttItemShowDel =getItemOfEsItemHieRelapByItem(cstplItemShowPara,"Cstpl");
                }
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrCorrespondingItemNo()));
            }
            else if(strSubmitTypePara.equals("From_tkctt_to_cstpl")){
                if(fromTkcttToCstplAction(cstplItemShowPara)){
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
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
    public void delThisRecordAction(CttItemShow cttItemShowPara) {
        try {
            cttItemService.deleteRecord(cttItemShowPara.getPkid()) ;
            cttItemService.setAfterThisOrderidSubOneByNode(
                    cttItemShowPara.getBelongToType(),
                    cttItemShowPara.getBelongToPkid(),
                    cttItemShowPara.getParentPkid(),
                    cttItemShowPara.getGrade(),
                    cttItemShowPara.getOrderid());
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
            if (strSubmitType.equals("Del")) {
                if (ToolUtil.getStrIgnoreNull(cttItemShowDel.getStrNo()).length() == 0) {
                    MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                    return;
                }
                delThisRecordAction(cttItemShowDel);
            } else {
                if (!subMitActionPreCheck()) {
                    return;
                }
            /*编码验证*/
                if (!blurStrNoToGradeAndOrderid("false")) {
                    return;
                }
            /*对应编码验证*/
                if (!blurCorrespondingPkid()) {
                    return;
                }
                if (strSubmitType.equals("Upd")) {
                    cttItemService.updateRecord(cttItemShowUpd);
                } else if (strSubmitType.equals("Add")) {
                    EsCttItem esCttItemTemp = cttItemService.fromModelShowToModel(cttItemShowAdd);
                    if (cttItemService.isExistSameRecordInDb(esCttItemTemp)) {
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
                }
                MessageUtil.addInfo("提交数据完成。");
                initData();
            }
        } catch (Exception e) {
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }
    /*提交前的检查：必须项的输入*/
    private Boolean subMitActionPreCheck(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
        try {
        /*总包合同列表*/
            List<EsCttItem> esCttItemListTkctt =new ArrayList<EsCttItem>();
            if(ToolUtil.getStrIgnoreNull(strFlowType).length()!=0&&
                    ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
                // 附件记录变成List
                attachmentList=attachmentStrToList(cttInfo.getAttachment());

                beansMap.put("cttInfo", cttInfo);
                String strTkcttPkidInCstpl = cttInfo.getParentPkid();
                esCttItemListTkctt =
                        cttItemService.getEsItemList(ESEnum.ITEMTYPE0.getCode(), strTkcttPkidInCstpl);
                List<CttItemShow> cttItemShowListTkctt = new ArrayList<>();
                recursiveDataTable("root", esCttItemListTkctt, cttItemShowListTkctt);
                cttItemShowListTkctt = getCstplItemList_DoFromatNo(cttItemShowListTkctt);

        /*成本计划列表*/
                List<EsCttItem> esCttItemListCstpl = cttItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);

                List<CttItemShow> cttItemShowListCstpl = new ArrayList<>();
                recursiveDataTable("root", esCttItemListCstpl, cttItemShowListCstpl);
                cttItemShowListCstpl = getCstplItemList_DoFromatNo(
                        cttItemShowListCstpl);

        /*拼装列表*/
                List<CstplItemShow> cstplItemShowList_ForSort = new ArrayList<>();

                for (CttItemShow itemTkctt : cttItemShowListTkctt) {
                    Boolean insertedFlag = false;
                    for (CttItemShow itemCstpl : cttItemShowListCstpl) {
                        CstplItemShow itemTkcttInsertItem = getCstplItem(itemTkctt, "Tkctt");
                        if (itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())) {
                            //总包合同
                            if (insertedFlag.equals(true)) {
                                itemTkcttInsertItem.setPkid(null);
                                itemTkcttInsertItem.setStrNo(null);
                                itemTkcttInsertItem.setName(null);
                                itemTkcttInsertItem.setNote(null);
                                itemTkcttInsertItem.setUnit(null);
                                itemTkcttInsertItem.setContractUnitPrice(null);
                                itemTkcttInsertItem.setContractQuantity(null);
                                itemTkcttInsertItem.setContractAmount(null);
                                itemTkcttInsertItem.setBelongToType(null);
                                itemTkcttInsertItem.setBelongToPkid(null);
                                itemTkcttInsertItem.setParentPkid(null);
                                itemTkcttInsertItem.setGrade(null);
                                itemTkcttInsertItem.setOrderid(null);
                                itemTkcttInsertItem.setSignPartAPrice(null);
                                itemTkcttInsertItem.setDeletedFlag(null);
                                itemTkcttInsertItem.setOriginFlag(null);
                                itemTkcttInsertItem.setCreatedBy(null);
                                itemTkcttInsertItem.setCreatedDate(null);
                                itemTkcttInsertItem.setLastUpdBy(null);
                                itemTkcttInsertItem.setLastUpdDate(null);
                                itemTkcttInsertItem.setModificationNum(null);
                                itemTkcttInsertItem.setCorrespondingPkid(null);
                            } else
                                //成本计划
                                itemTkcttInsertItem.setStrNoContrast(itemCstpl.getStrNo());
                            itemTkcttInsertItem.setPkidContrast(itemCstpl.getPkid());
                            itemTkcttInsertItem.setBelongToTypeContrast(itemCstpl.getBelongToType());
                            itemTkcttInsertItem.setBelongToPkidContrast(itemCstpl.getBelongToPkid());
                            itemTkcttInsertItem.setParentPkidContrast(itemCstpl.getParentPkid());
                            itemTkcttInsertItem.setGradeContrast(itemCstpl.getGrade());
                            itemTkcttInsertItem.setOrderidContrast(itemCstpl.getOrderid());
                            itemTkcttInsertItem.setNameContrast(itemCstpl.getName());
                            itemTkcttInsertItem.setNoteContrast(itemCstpl.getNote());
                            itemTkcttInsertItem.setUnitContrast(itemCstpl.getUnit());
                            itemTkcttInsertItem.setContractUnitPriceContrast(itemCstpl.getContractUnitPrice());
                            itemTkcttInsertItem.setContractQuantityContrast(itemCstpl.getContractQuantity());
                            itemTkcttInsertItem.setContractAmountContrast(itemCstpl.getContractAmount());
                            itemTkcttInsertItem.setSignPartAPriceContrast(itemCstpl.getSignPartAPrice());
                            itemTkcttInsertItem.setDeletedFlagContrast(itemCstpl.getDeletedFlag());
                            itemTkcttInsertItem.setOriginFlagContrast(itemCstpl.getOriginFlag());
                            itemTkcttInsertItem.setCreatedByContrast(itemCstpl.getCreatedBy());
                            itemTkcttInsertItem.setCreatedDateContrast(itemCstpl.getCreatedDate());
                            itemTkcttInsertItem.setLastUpdByContrast(itemCstpl.getLastUpdBy());
                            itemTkcttInsertItem.setLastUpdDateContrast(itemCstpl.getLastUpdDate());
                            itemTkcttInsertItem.setModificationNumContrast(itemCstpl.getModificationNum());
                            itemTkcttInsertItem.setCorrespondingPkidContrast(itemCstpl.getCorrespondingPkid());
                            if (itemTkcttInsertItem.getPkid() == null || itemTkcttInsertItem.getPkid().equals("")) {
                                itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() + "");
                            }
                            insertedFlag = true;
                            cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                        }
                    }
                    if (insertedFlag.equals(false)) {
                        CstplItemShow itemTkcttInsertItem = getCstplItem(itemTkctt, "Tkctt");
                        cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                    }
                }

                for (CttItemShow itemCstpl : cttItemShowListCstpl) {
                    Boolean insertedFlag = false;
                    for (CttItemShow itemTkctt : cttItemShowListTkctt) {
                        if (itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())) {
                            insertedFlag = true;
                            break;
                        }
                    }
                    if (insertedFlag.equals(false)) {
                        CstplItemShow itemTkcttInsertItem = getCstplItem(itemCstpl, "Cstpl");
                        if (itemTkcttInsertItem.getPkid() == null || itemTkcttInsertItem.getPkid().equals("")) {
                            itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() + "");
                        }
                        cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                    }
                }

                for (CstplItemShow itemUnit : cstplItemShowList_ForSort) {
                    if (itemUnit.getStrNoContrast() != null) {
                        String correspondingItemNoContrast = itemUnit.getCorrespondingPkidContrast() == null ? "" : itemUnit.getCorrespondingPkidContrast();
                        CstplItemShow cstplItemShowTemp =
                                getItemOfTkcttAndCstplByPkid(correspondingItemNoContrast, cstplItemShowList_ForSort, "Tkctt");
                        if (cstplItemShowTemp != null) {
                            itemUnit.setCorrespondingItemNoContrast(cstplItemShowTemp.getStrNo());
                        }
                    } else {
                        itemUnit.setCorrespondingItemNoContrast(itemUnit.getStrNo());
                    }
                }
                cstplItemShowList = new ArrayList<CstplItemShow>();
                cstplItemShowList.addAll(cstplItemShowList_ForSort);
                cstplItemShowListExcel = new ArrayList<CstplItemShow>();
                for (CstplItemShow itemUnit : cstplItemShowList) {
                    CstplItemShow itemUnitTemp = (CstplItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    itemUnitTemp.setStrNoContrast(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNoContrast()));
                    cstplItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cstplItemShowListExcel", cstplItemShowListExcel);
                // 添加合计
                setCstplItemList_AddTotal();
                beansMap.put("cstplItemShowList", cstplItemShowList);
                resetActionForAdd();
            }
        }catch (Exception e) {
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }
    private void setCstplItemList_AddTotal(){
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
    private boolean fromTkcttToCstplAction(CstplItemShow cstplItemShowPara){
        try{
            if(cstplItemShowPara.getStrNo() ==null||
                    cstplItemShowPara.getStrNo().equals("")) {
                MessageUtil.addInfo("无可复制！");
                return false;
            }
            if(!ToolUtil.getStrIgnoreNull(cstplItemShowPara.getStrNoContrast()).equals("")) {
                MessageUtil.addInfo("成本计划项不为空，总包合同到成本计划的项无法复制，如需复制，请先删除该成本计划项！");
                return false;
            }
            String strIgnoreSpaceOfStr=ToolUtil.getIgnoreSpaceOfStr(cstplItemShowPara.getStrNo());
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
            cttItemShowAdd = getItemOfEsItemHieRelapByItem(cstplItemShowPara,"Tkctt");
            cttItemShowAdd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowAdd.getStrNo())) ;
            cttItemShowAdd.setBelongToType(strBelongToType) ;
            cttItemShowAdd.setBelongToPkid(strCttInfoPkid) ;
            Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");
            if(intLastIndexof>0){
                String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList,"Cstpl");
                cttItemShowAdd.setParentPkid(cstplItemShowTemp.getPkid());
                cttItemShowAdd.setGrade(cstplItemShowTemp.getGrade() + 1) ;
            }else{
                cttItemShowAdd.setParentPkid("root");
            }

            cttItemShowAdd.setCorrespondingPkid(cttItemShowAdd.getPkid()) ;
            cttItemShowAdd.setStrCorrespondingItemNo(cttItemShowAdd.getStrNo()) ;
            strSubmitType="Add";
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
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= ToolUtil.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= ToolUtil.getUserName(itemUnit.getLastUpdBy());
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
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CstplItemShow> getItemOfTkcttAndCstplByLevelParentPkid(
            String strLevelParentPkid,
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
    private CstplItemShow getItemOfTkcttAndCstplByStrNo(
            String strNo,
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
    private CstplItemShow getItemOfTkcttAndCstplByPkid(
            String strPkid,
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
    private CstplItemShow getCstplItem(
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
    private List<CttItemShow> getCstplItemList_DoFromatNo(
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
    public String onExportExcel()throws IOException, WriteException {
        if (this.cstplItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "成本计划-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"oriCstpl.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }

    //附件相关方法
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
            cttInfoService.updateRecord(cttInfo,"AttachRemove");
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
            cttInfoService.updateRecord(cttInfo,"AttachAdd");
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
    public EsCttInfo getCttInfo() {
        return cttInfo;
    }

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

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public List<CstplItemShow> getCstplItemShowList() {
        return cstplItemShowList;
    }

    public void setCstplItemShowList(List<CstplItemShow> cstplItemShowList) {
        this.cstplItemShowList = cstplItemShowList;
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
    public StyleModel getStyleModel() {
        return styleModel;
    }
    public String getStrMngNotFinishFlag() {
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

    public CttItemShow getCttItemShowSel() {
        return cttItemShowSel;
    }

    public void setCttItemShowSel(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
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

    public String getStrPassFlag() {
        return strPassFlag;
    }

    public String getStrFlowType() {
        return strFlowType;
    }

    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }

    public List<CstplItemShow> getCstplItemShowListExcel() {
        return cstplItemShowListExcel;
    }

    public void setCstplItemShowListExcel(List<CstplItemShow> cstplItemShowListExcel) {
        this.cstplItemShowListExcel = cstplItemShowListExcel;
    }

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