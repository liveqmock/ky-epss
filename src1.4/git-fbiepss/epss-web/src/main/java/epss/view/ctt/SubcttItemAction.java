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
import epss.repository.model.*;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
import epss.service.*;
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
public class SubcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttItemAction.class);
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    /*打开的成本计划页面用*/
    private List<CttItemShow> cttItemShowList_Cstpl;
    private CttItemShow cttItemShowSelected_Cstpl;

    private CttInfo cttInfo;
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemQShowAdd;
    private CttItemShow cttItemQShowUpd;
    private CttItemShow cttItemQShowDel;
    private CttItemShow cttItemFShowAdd;
    private CttItemShow cttItemFShowUpd;
    private CttItemShow cttItemFShowDel;

    private List<CttItem> cttItemList;
    /*列表中选择一行*/
    private CttItemShow cttItemShowSelected;
	/*列表显示用*/
    private List<CttItemShow> cttItemShowList;

    /*所属类型*/
    private String strBelongToType;
    /*所属号*/
    private String strCttInfoPkid;

    /*提交类型*/
    private String strSubmitType;
    private String strPassVisible;
    private String strPassFailVisible;
    private String strFlowType;
    private String strNotPassToStatus;

    /*控制控件在画面上的可用与现实Start*/
    //显示的控制
    private StyleModel styleModel;
    /*控制控件在画面上的可用与现实End*/
    private Map beansMap;
    private List<CttItemShow> cttItemShowListExcel;

    //附件
    private CttInfoShow cttInfoShowAttachment;
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;
	// 录入备注
    private String strFlowStatusRemark;

    @PostConstruct
    public void init() {
        try {
            this.attachmentList=new ArrayList<>();
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            beansMap = new HashMap();
            strBelongToType= EnumResType.RES_TYPE2.getCode();
            if (parammap.containsKey("strCttInfoPkid")) {
                strCttInfoPkid = parammap.get("strCttInfoPkid").toString();
                cttInfo=cttInfoService.getCttInfoByPkId(strCttInfoPkid);
            }
            if (parammap.containsKey("strFlowType")) {
                strFlowType = parammap.get("strFlowType").toString();
            }
            strPassVisible = "true";
            strPassFailVisible = "true";
            if ("Mng".equals(strFlowType)) {
                if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(cttInfo.getFlowStatus())){
                    strPassVisible = "false";
                }else {
                    strPassFailVisible = "false";
                }
            }else {
                if (("Check".equals(strFlowType)&&EnumFlowStatus.FLOW_STATUS1.getCode().equals(cttInfo.getFlowStatus()))
                        ||("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(cttInfo.getFlowStatus()))
                        ||("Approve".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS3.getCode().equals(cttInfo.getFlowStatus()))){
                    strPassVisible = "false";
                }
            }
            resetAction("");
            initData() ;
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    /*初始化操作*/
    private void initData() {
        try{
            cttItemShowList_Cstpl =new ArrayList<>();
            if(ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
        /*分包合同*/
                cttInfo = cttInfoService.getCttInfoByPkId(strCttInfoPkid);
                // 附件记录变成List
                attachmentList = ToolUtil.getListAttachmentByStrAttachment(cttInfo.getAttachment());
                beansMap.put("cttInfo", cttInfo);
        /*成本计划*/
                String strCstplPkidInInitCtt = cttInfo.getParentPkid();
                cttItemList = cttItemService.getEsItemList(
                        EnumResType.RES_TYPE1.getCode(), strCstplPkidInInitCtt);
                recursiveDataTable("root", cttItemList, cttItemShowList_Cstpl);
                cttItemShowList_Cstpl = getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList_Cstpl);
        /*分包合同*/
                cttItemList = new ArrayList<>();
                cttItemShowList = new ArrayList<>();
                cttItemList = cttItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);
                cttItemShowList.clear();
                recursiveDataTable("root", cttItemList, cttItemShowList);
                cttItemShowList = getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList);
        /*分包合同对应成本计划中的项*/
                for (CttItemShow itemUnit : cttItemShowList) {
                    for (CttItemShow itemUnitCstpl : cttItemShowList_Cstpl) {
                        if (itemUnit.getCorrespondingPkid() != null &&
                                itemUnit.getCorrespondingPkid().equals(itemUnitCstpl.getPkid())) {
                            itemUnit.setStrCorrespondingItemNo(itemUnitCstpl.getStrNo());
                            itemUnit.setStrCorrespondingItemName(itemUnitCstpl.getName());
                        }
                    }
                }
                // 添加合计
                setItemOfCstplAndSubcttList_AddTotal();

                cttItemShowListExcel = new ArrayList<CttItemShow>();
                for (CttItemShow itemUnit : cttItemShowList) {
                    // 分包合同中合同单价，工程量，金额，甲供材单价
                    itemUnit.setContractUnitPrice(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractUnitPrice()));
                    itemUnit.setContractQuantity(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractQuantity()));
                    itemUnit.setContractAmount(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractAmount()));
                    itemUnit.setSignPartAPrice(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getSignPartAPrice()));
                    CttItemShow itemUnitTemp = (CttItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    itemUnitTemp.setStrCorrespondingItemNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCorrespondingItemNo()));
                    cttItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cttItemShowListExcel", cttItemShowListExcel);
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<CttItemShow> cttItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= cttInfoService.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= cttInfoService.getUserName(itemUnit.getLastUpdBy());
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
    private void setItemOfCstplAndSubcttList_AddTotal(){
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
            if (itemUnit.getSpareField()==null||itemUnit.getSpareField().equals("F1")){
                bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            }
            if(itemUnit.getUnit()!=null&&!itemUnit.getUnit().equals("")&&itemUnit.getSpareField()==null){
                bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            }
            if(itemUnit.getSpareField()!=null && itemUnit.getSpareField().equals("F1")){
                bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            }
            cttItemShowList.add(itemUnit);

            if(i+1< cttItemShowListTemp.size()){
                itemUnitNext = cttItemShowListTemp.get(i+1);
                CttItemShow cttItemShowTemp =new CttItemShow();
                Boolean isRoot=false;
                if(itemUnitNext.getParentPkid()!=null&&itemUnitNext.getParentPkid().equals("root")){
                    cttItemShowTemp.setName("合计");
                    cttItemShowTemp.setPkid("total"+i);
                    cttItemShowTemp.setContractAmount(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }
                if(isRoot.equals(true)){
                    cttItemShowList.add(cttItemShowTemp);
                }
            } else if(i+1== cttItemShowListTemp.size()){
                itemUnitNext = cttItemShowListTemp.get(i);
                CttItemShow cttItemShowTemp =new CttItemShow();
                cttItemShowTemp.setName("合计");
                cttItemShowTemp.setPkid("total"+i);
                cttItemShowTemp.setContractAmount(bdTotal);
                bdTotal=new BigDecimal(0);
                cttItemShowList.add(cttItemShowTemp);
                // 总合计
                cttItemShowTemp =new CttItemShow();
                cttItemShowTemp.setName("总合计");
                cttItemShowTemp.setPkid("total_all"+i);
                cttItemShowTemp.setContractAmount(bdAllTotal);
                cttItemShowList.add(cttItemShowTemp);
                bdAllTotal=new BigDecimal(0);
            }
        }
    }

    /*重置*/
    public void resetAction(String strMitType   ){
        strSubmitType="Add";
        if (ToolUtil.getStrIgnoreNull(strMitType).equals("FAdd")){
            strSubmitType="FAdd";
        }
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemQShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemQShowUpd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemQShowDel =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemFShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemFShowUpd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemFShowDel =new CttItemShow(strBelongToType ,strCttInfoPkid);
    }
    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara,CttItemShow cttItemShowSeledPara){
        try {
            strSubmitType=strSubmitTypePara;
            if(!strSubmitTypePara.equals("Add")){
                if(cttItemShowSeledPara.getStrNo()==null){
                    MessageUtil.addError("请确认选择的行，合计行不可编辑！");
                    return;
                }
            }
            if(strSubmitTypePara.equals("Sel")){
                cttItemShowSel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemQShowUpd =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemQShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemQShowUpd.getStrNo())) ;
                cttItemQShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemQShowUpd.getStrCorrespondingItemNo()));
                esCommon.getIndexOfSubcttItemNamelist(cttItemQShowUpd.getName());
            }
            if(strSubmitTypePara.equals("FUpd")){
                cttItemFShowUpd =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemFShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemFShowUpd.getStrNo())) ;
                cttItemFShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemFShowUpd.getStrCorrespondingItemNo()));
                esCommon.getIndexOfSubcttItemNamelist(cttItemFShowUpd.getName());
            }
            if(strSubmitTypePara.equals("Del")){
                cttItemQShowDel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemQShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemQShowDel.getStrNo())) ;
                cttItemQShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemQShowDel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("FDel")){
                cttItemFShowDel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemFShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemFShowDel.getStrNo())) ;
                cttItemFShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemFShowDel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("FromCstplToSubctt")){
                cttItemShowSelected =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
            }
        } catch (Exception e) {
            logger.error("选择数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
        if (strSubmitType.equals("Add")){
            if(cttItemQShowAdd.getContractUnitPrice()==null|| cttItemQShowAdd.getContractQuantity()==null){
                bigDecimal=null;
            }
            else{
                bigDecimal = cttItemQShowAdd.getContractUnitPrice().multiply(cttItemQShowAdd.getContractQuantity());
            }
            cttItemQShowAdd.setContractAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")){
            if(cttItemQShowUpd.getContractUnitPrice()==null|| cttItemQShowUpd.getContractQuantity()==null){
                bigDecimal=null;
            }
            else{
                bigDecimal = cttItemQShowUpd.getContractUnitPrice().multiply(cttItemQShowUpd.getContractQuantity());
            }
            cttItemQShowUpd.setContractAmount(bigDecimal);
        }

    }
    public Boolean blurStrNoToGradeAndOrderid(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemQShowAdd;
        }
        if (strSubmitType.equals("FAdd")){
            cttItemShowTemp = cttItemFShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemQShowUpd;
        }
        if (strSubmitType.equals("FUpd")){
            cttItemShowTemp = cttItemFShowUpd;
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

        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<CttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsCttItemListByParentPkid("root", cttItemList);

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
            cttItemShowTemp.setGrade(1);
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            CttItemShow cttItemShowTemp1 =new CttItemShow();
            cttItemShowTemp1 =getEsCttItemByStrNo(
                    strParentNo,
                    cttItemShowList);
            if(cttItemShowTemp1 ==null|| cttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("请确认输入的编码！父层" + strParentNo + "不存在！");
                return strNoBlurFalse();
            }
            else{

                List<CttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsCttItemListByParentPkid(
                        cttItemShowTemp1.getPkid(),
                        cttItemList);
                if(itemHieRelapListSubTemp .size() ==0){
                    if (cttItemShowTemp1.getSignPartAPrice()!= null ||cttItemShowTemp1.getContractQuantity() != null
                            ||cttItemShowTemp1.getContractUnitPrice() != null ){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，"+strParentNo+"不能有子项");
                        return strNoBlurFalse();
                    }
                    if(!cttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + strParentNo + ".1！");
                        return strNoBlurFalse();
                    }
                }
                else{
                    if (cttItemShowTemp1.getSignPartAPrice()!= null ||cttItemShowTemp1.getContractQuantity() != null
                         ||cttItemShowTemp1.getContractUnitPrice() != null ){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，"+strParentNo+"不能有子项");
                        return strNoBlurFalse();
                    }
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("请确认输入的编码！该编码不符合规范，应输入" + strParentNo +
                                "." + (itemHieRelapListSubTemp.size() + 1) + "！");
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
    public Boolean blurCorrespondingPkid(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemQShowAdd;
        }
        if (strSubmitType.equals("FAdd")){
            cttItemShowTemp = cttItemFShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemQShowUpd;
        }
        if (strSubmitType.equals("FUpd")){
            cttItemShowTemp = cttItemFShowUpd;
        }
        if(cttItemShowTemp.getStrCorrespondingItemNo()==null||
                cttItemShowTemp.getStrCorrespondingItemNo().equals("")){
            cttItemShowTemp.setCorrespondingPkid(null);
            cttItemShowTemp.setStrCorrespondingItemName(null);
            return true;
        }
        CttItemShow cttItemShowTemp1 =
                getEsCttItemByStrNo(
                        cttItemShowTemp.getStrCorrespondingItemNo(),
                        cttItemShowList_Cstpl);
        if(cttItemShowTemp1 !=null && cttItemShowTemp1.getStrNo() !=null){
            cttItemShowTemp.setCorrespondingPkid(cttItemShowTemp1.getPkid());
            cttItemShowTemp.setStrCorrespondingItemName(cttItemShowTemp1.getName());
            return true ;
        }
        else{
            cttItemShowTemp.setCorrespondingPkid(null);
            cttItemShowTemp.setStrCorrespondingItemName(null);
            MessageUtil.addError("请确认输入的对应编码，该编码不是正确的成本计划项编码！");
            return strNoBlurFalse();
        }
    }
    private Boolean strNoBlurFalse(){
        cttItemShowSel.setPkid("") ;
        cttItemShowSel.setParentPkid("");
        cttItemShowSel.setGrade(null);
        cttItemShowSel.setOrderid(null);
        return false;
    }

    public void submitThisRecordAction(){
        try{
                if(strSubmitType.equals("Del") ){
                cttItemService.setAfterThisOrderidSubOneByNode(cttItemQShowDel);
            }else if (strSubmitType.equals("FDel")){
                cttItemService.setAfterThisOrderidSubOneByNode(cttItemFShowDel);
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
                    cttItemService.setAfterThisOrderidPlusOneByNode(cttItemQShowAdd);
                    resetAction("");
                }
                else if(strSubmitType.equals("FAdd")){
                    if(!ToolUtil.getStrIgnoreNull(cttItemFShowAdd.getName()).equals("")){
                        String name= esCommon.getLabelSubcttItemNamelist(cttItemFShowAdd.getName());
                        if(!name.equals("-1")){
                            cttItemFShowAdd.setSpareField(cttItemFShowAdd.getName());
                            cttItemFShowAdd.setName(name);
                        }
                    }
                    cttItemService.setAfterThisOrderidPlusOneByNode(cttItemFShowAdd);
                    resetAction("FAdd");
                }
                else if(strSubmitType.equals("Upd")){
                    cttItemService.updateRecord(cttItemQShowUpd);
                }
                else if(strSubmitType.equals("FUpd")){
                    if(!ToolUtil.getStrIgnoreNull(cttItemFShowUpd.getName()).equals("")){
                        String name= esCommon.getLabelSubcttItemNamelist(cttItemFShowUpd.getName());
                        if(!name.equals("-1")){
                            cttItemFShowUpd.setSpareField(cttItemFShowUpd.getName());
                            cttItemFShowUpd.setName(name);
                        }
                    }
                    cttItemService.updateRecord(cttItemFShowUpd);
                }
            }
            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("增加数据完成。");
                    break;
                case "FAdd" : MessageUtil.addInfo("增加数据完成。");
                    break;
                case "Upd" : MessageUtil.addInfo("更新数据完成。");
                    break;
                case "FUpd" : MessageUtil.addInfo("更新数据完成。");
                    break;
                case "Del" : MessageUtil.addInfo("删除数据完成。");
                    break;
                case "FDel" : MessageUtil.addInfo("删除数据完成。");
            }
            initData();
        }
        catch (Exception e){
            switch (strSubmitType){
                case "Add" : MessageUtil.addError("增加数据失败，"+ e.getMessage());
                    break;
                case "FAdd" : MessageUtil.addInfo("增加数据失败,"+  e.getMessage());
                    break;
                case "Upd" : MessageUtil.addError("更新数据失败，"+ e.getMessage());
                    break;
                case "FUpd" : MessageUtil.addError("更新数据失败，"+ e.getMessage());
                    break;
                case "Del" : MessageUtil.addError("删除数据失败，"+ e.getMessage());
            }
        }
    }

    /*提交前的检查：必须项的输入*/
	private Boolean subMitActionPreCheck(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemQShowAdd;
        }
        if (strSubmitType.equals("FAdd")){
            cttItemShowTemp = cttItemFShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemQShowUpd;
        }
        if (strSubmitType.equals("FUpd")){
            cttItemShowTemp = cttItemFShowUpd;
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

    public void submitAction_Cstpl(){
        try{
            cttItemShowSelected.setCorrespondingPkid(cttItemShowSelected_Cstpl.getPkid());
            cttItemService.updateRecord(cttItemShowSelected) ;
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<CttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(List<CttItemShow> cttItemShowListPara){
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

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
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
    private boolean checkPreMng(CttInfo cttInfoPara) {
        if (StringUtils.isEmpty(cttInfoPara.getId())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getName())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getSignDate())) {
            return false;
        }
        if (StringUtils.isEmpty(cttInfoPara.getSignPartA())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getSignPartB())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getCttStartDate())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getCttEndDate())) {
            return false;
        }
        return true;
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
                    if (!strCttInfoPkid.equals("")) {
                        cttInfo=cttInfoService.getCttInfoByPkId(strCttInfoPkid);
                    }
                    if(!checkPreMng(cttInfo)){
                        MessageUtil.addError("合同信息未维护完整，无法录入完成！");
                        return ;
                    }
                    List<CttItem> cttItemList = cttItemService.getEsItemList(cttInfo.getCttType(), cttInfo.getPkid());
                    if (cttItemList.isEmpty()) {
                        MessageUtil.addInfo("无详细内容！");
                        return;
                    }
                    int checkQZero=0;
                    int checkMZero=0;
                    int checkSpare=0;//明细表判断是否是安全措施费字段
                    for (CttItem cttItemTemp : cttItemList) {
                        if (ToolUtil.bigDecimal0.compareTo(ToolUtil.getBdIgnoreNull(cttItemTemp.getSignPartAPrice()))!=0){
                            checkMZero=1;
                        }
                        if (ToolUtil.bigDecimal0.compareTo(ToolUtil.getBdIgnoreNull(cttItemTemp.getContractQuantity()))!=0){
                            checkQZero=1;
                        }
                        if (ToolUtil.getStrIgnoreNull(cttItemTemp.getSpareField()).equals("F1") ){
                            checkSpare=1;
                        }
                    }
                    if(checkSpare==0) {
                        if(checkQZero==1&&checkMZero==0){
                            cttInfo.setType(EnumSubcttType.TYPE0.getCode());
                        }
                        if(checkQZero==0&&checkMZero==1){
                            cttInfo.setType(EnumSubcttType.TYPE1.getCode());
                        }
                        if(checkQZero==1&&checkMZero==1){
                            cttInfo.setType(EnumSubcttType.TYPE3.getCode());
                        }
                    }else {
                        if (checkQZero == 0 && checkMZero == 0 ) {//安全措施
                            cttInfo.setType(EnumSubcttType.TYPE2.getCode());
                        }
                        if (checkQZero == 0 && checkMZero == 1 ) {
                            cttInfo.setType(EnumSubcttType.TYPE5.getCode());
                        }
                        if (checkQZero == 1 && checkMZero == 0) {
                            cttInfo.setType(EnumSubcttType.TYPE4.getCode());
                        }
                        if (checkQZero == 1 && checkMZero == 1 ) {
                            cttInfo.setType(EnumSubcttType.TYPE6.getCode());
                        }
                    }
                    // 状态标志：初始
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：录入完毕
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfo.setFlowStatus(null);
                    cttInfo.setFlowStatusReason(null);
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }// 审核
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // 状态标志：审核
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // 状态标志：初始
                    cttInfo.setFlowStatus(null);
                    // 原因：审核未过
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } // 复核
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // 状态标志：复核
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据复核通过！");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }
                    // 原因：复核未过
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据复核未过！");
                }
            }// 批准
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // 状态标志：批准
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("数据批准通过！");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // 检查是否被使用
                    String strCttTypeTemp = "";
                    if (cttInfo.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                        strCttTypeTemp = EnumResType.RES_TYPE1.getCode();
                    } else if (cttInfo.getCttType().equals(EnumResType.RES_TYPE1.getCode())) {
                        strCttTypeTemp = EnumResType.RES_TYPE2.getCode();
                    }

                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }

                    // 原因：批准未过
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);

                    List<ProgStlInfo> progStlInfoListTemp =
                            progStlInfoService.selectIsUsedInQMPBySubcttPkid(cttInfo.getPkid());
                    if (progStlInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("该数据已经被["
                                + EnumResType.getValueByKey(progStlInfoListTemp.get(0).getStlType()).getTitle()
                                + "]使用，数据批准未过,请慎重编辑！");
                    } else {
                        if (cttInfoService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cttInfo.getPkid()) > 0) {
                            MessageUtil.addInfo("该数据已经被[" + EnumResType.getValueByKey(strCttTypeTemp).getTitle()
                                    + "]使用，数据批准未过,请慎重编辑！");
                        } else {
                            MessageUtil.addInfo("数据批准未过！");
                        }
                    }
                }
            }
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "分包合同-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"oriSubctt.xls");
    }

    //附件相关方法
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
                downloadFile = new DefaultStreamedContent(
                        stream,
                        new MimetypesFileTypeMap().getContentType(file),
                        new String(strAttachment.getBytes("gbk"),"iso8859-1"));
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

    public CttItemShow getCttItemShow() {
        return cttItemShowSel;
    }

    public void setCttItemShow(CttItemShow cttItemShowSel) {
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

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public List<CttItemShow> getCttItemShowList_Cstpl() {
        return cttItemShowList_Cstpl;
    }

    public void setCttItemShowList_Cstpl(List<CttItemShow> cttItemShowList_Cstpl) {
        this.cttItemShowList_Cstpl = cttItemShowList_Cstpl;
    }

    public CttItemShow getCttItemShowSelected_Cstpl() {
        return cttItemShowSelected_Cstpl;
    }

    public void setCttItemShowSelected_Cstpl(CttItemShow cttItemShowSelected_Cstpl) {
        this.cttItemShowSelected_Cstpl = cttItemShowSelected_Cstpl;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public CttItemShow getCttItemQShowAdd() {
        return cttItemQShowAdd;
    }

    public void setCttItemQShowAdd(CttItemShow cttItemQShowAdd) {
        this.cttItemQShowAdd = cttItemQShowAdd;
    }

    public CttItemShow getCttItemQShowUpd() {
        return cttItemQShowUpd;
    }

    public void setCttItemQShowUpd(CttItemShow cttItemQShowUpd) {
        this.cttItemQShowUpd = cttItemQShowUpd;
    }

    public CttItemShow getCttItemQShowDel() {
        return cttItemQShowDel;
    }

    public void setCttItemQShowDel(CttItemShow cttItemQShowDel) {
        this.cttItemQShowDel = cttItemQShowDel;
    }

    public String getStrFlowType() {
        return strFlowType;
    }

    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public CttInfo getCttInfo() {
        return cttInfo;
    }

    public void setCttInfo(CttInfo cttInfo) {
        this.cttInfo = cttInfo;
    }

    public List<CttItemShow> getCttItemShowListExcel() {
        return cttItemShowListExcel;
    }

    public void setCttItemShowListExcel(List<CttItemShow> cttItemShowListExcel) {
        this.cttItemShowListExcel = cttItemShowListExcel;
    }

    public CttInfoShow getCttInfoShowAttachment() {
        return cttInfoShowAttachment;
    }

    public void setCttInfoShowAttachment(CttInfoShow cttInfoShowAttachment) {
        this.cttInfoShowAttachment = cttInfoShowAttachment;
    }

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }
    /*智能字段End*/

    public String getStrPassVisible() {
        return strPassVisible;
    }

    public String getStrPassFailVisible() {
        return strPassFailVisible;
    }

    public String getStrFlowStatusRemark() {
        return strFlowStatusRemark;
    }

    public void setStrFlowStatusRemark(String strFlowStatusRemark) {
        this.strFlowStatusRemark = strFlowStatusRemark;
    }

    public CttItemShow getCttItemFShowAdd() {
        return cttItemFShowAdd;
    }

    public void setCttItemFShowAdd(CttItemShow cttItemFShowAdd) {
        this.cttItemFShowAdd = cttItemFShowAdd;
    }

    public CttItemShow getCttItemFShowUpd() {
        return cttItemFShowUpd;
    }

    public void setCttItemFShowUpd(CttItemShow cttItemFShowUpd) {
        this.cttItemFShowUpd = cttItemFShowUpd;
    }

    public CttItemShow getCttItemFShowDel() {
        return cttItemFShowDel;
    }

    public void setCttItemFShowDel(CttItemShow cttItemFShowDel) {
        this.cttItemFShowDel = cttItemFShowDel;
    }

    /*智能字段End*/
}