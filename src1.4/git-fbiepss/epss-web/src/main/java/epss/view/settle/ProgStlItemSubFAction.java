package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubFShow;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.EsCommon;
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

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlItemSubFAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemSubFAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemSubFService}")
    private ProgStlItemSubFService progStlItemSubFService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;

    private List<ProgStlItemSubFShow> progStlItemSubFShowList;
    private ProgStlItemSubFShow progStlItemSubFShowSel;
    private ProgStlItemSubFShow progStlItemSubFShowUpd;
    private ProgStlItemSubFShow progStlItemSubFShowDel;
//    private BigDecimal bDEngSMng_BeginToCurrentPeriodSEXPInDB;
//    private BigDecimal bDEngSMng_CurrentPeriodSEXPInDB;
    private BigDecimal bDEngSMng_AddUpToAmt;
    private BigDecimal bDEngSMng_ThisStageAmt;


    private String strSubcttPkid;
    private String strSubmitType;
    private ProgStlInfo progStlInfo;

    /*控制维护画面层级部分的显示*/
    private String strPassFlag;
    private List<ProgStlItemSubFShow> progStlItemSubFShowListExcel;
    private ProgStlInfoShow progStlInfoShow;
    private String strPassVisible;
    private String strPassFailVisible;
    private String strFlowType;
    private String strNotPassToStatus;
    private Map beansMap;
    private String strFlowStatusRemark;
    private HtmlGraphicImage image;
    private StreamedContent downloadFile;
    private List<AttachmentModel> attachmentList;

    @PostConstruct
    public void init() {
        try {
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            beansMap = new HashMap();
            if (parammap.containsKey("strFlowType")) {
                strFlowType = parammap.get("strFlowType").toString();
            }
            if (parammap.containsKey("strStlInfoPkid")) {
                String strStlInfoPkidTemp = parammap.get("strStlInfoPkid").toString();
                progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkidTemp);
                strSubcttPkid = progStlInfo.getStlPkid();

                strPassVisible = "true";
                strPassFailVisible = "true";
                if ("Mng".equals(strFlowType)) {
                    if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfo.getFlowStatus())) {
                        strPassVisible = "false";
                    } else {
                        strPassFailVisible = "false";
                    }
                } else {
                    if (("Check".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS1.getCode().equals(progStlInfo.getFlowStatus()))
                            || ("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfo.getFlowStatus()))) {
                        strPassVisible = "false";
                    }
                }
                resetAction();
                initData();
            }
        } catch (Exception e) {
            logger.error("初始化失败", e);
        }
    }


    /*初始化操作*/
    private void initData() {
        try {
            // 详细页头
            CttInfo cttInfoTemp = cttInfoService.getCttInfoByPkId(progStlInfo.getStlPkid());
            progStlInfoShow =progStlInfoService.fromModelToModelShow(progStlInfo);
            progStlInfoShow.setStlId(cttInfoTemp.getId());
            progStlInfoShow.setStlName(cttInfoTemp.getName());
            SignPart signPartTemp=signPartService.getEsInitCustByPkid(cttInfoTemp.getSignPartB());
            if (signPartTemp!=null){
                progStlInfoShow.setSignPartBName(signPartTemp.getName());
            }
            EnumSubcttType subcttTypeTemp=EnumSubcttType.getValueByKey(cttInfoTemp.getType());
            if (subcttTypeTemp!=null){
                progStlInfoShow.setType(subcttTypeTemp.getTitle());
            }
            beansMap.put("progStlInfoShow", progStlInfoShow);

            /*分包合同*/
            List<CttItem> cttItemList =new ArrayList<>();
            cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE2.getCode(), strSubcttPkid);
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemSubFShowList =new ArrayList<>();
            progStlItemSubFShowListExcel =new ArrayList<>();
            attachmentList=new ArrayList<>();
            attachmentList=ToolUtil.getListAttachmentByStrAttachment(progStlInfoShow.getAttachment());
            recursiveDataTable("root", cttItemList, progStlItemSubFShowList);
            progStlItemSubFShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progStlItemSubFShowList);
            setItemOfEsItemHieRelapList_AddTotal();
            // Excel报表形成
            progStlItemSubFShowListExcel =new ArrayList<>();
            for(ProgStlItemSubFShow itemUnit: progStlItemSubFShowList){
                // 分包合同
                itemUnit.setSubctt_ContractUnitPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractUnitPrice()));
                itemUnit.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractQuantity()));
                itemUnit.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractAmount()));
                itemUnit.setSubctt_SignPartAPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_SignPartAPrice()));
                // 分包工程量结算
                itemUnit.setEngSMng_ThisStageAmt(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngSMng_ThisStageAmt()));
                itemUnit.setEngSMng_AddUpToAmt(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngSMng_AddUpToAmt()));
                ProgStlItemSubFShow itemUnitTemp= (ProgStlItemSubFShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                progStlItemSubFShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemSubFShowListExcel", progStlItemSubFShowListExcel);
        }catch (Exception e){
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }


    private List<ProgStlItemSubFShow> getStlSubCttEngQMngConstructList_DoFromatNo(
            List<ProgStlItemSubFShow> progStlItemSubQShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgStlItemSubFShow itemUnit: progStlItemSubQShowListPara){
            if(itemUnit.getSubctt_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getSubctt_Orderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getSubctt_Orderid().toString();
                }
            }
            else{
                if(itemUnit.getSubctt_Grade()==1){
                    strTemp=itemUnit.getSubctt_Orderid().toString() ;
                }
                else {
                    if (!itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getSubctt_Grade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        } else {
                            Integer intTemp=strTemp.indexOf(".",itemUnit.getSubctt_Grade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getSubctt_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getSubctt_Grade() ;
            itemUnit.setSubctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getSubctt_Grade(), strTemp)) ;
        }
        return progStlItemSubQShowListPara;
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgStlItemSubFShow> progStlItemSubMShowListTemp =new ArrayList<ProgStlItemSubFShow>();
        progStlItemSubMShowListTemp.addAll(progStlItemSubFShowList);
        progStlItemSubFShowList.clear();
        // 合同数量小计
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // 合同数量大计
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // 合同金额小计
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // 合同金额大计
        BigDecimal bdAmountAllTotal=new BigDecimal(0);
        // 开累材料量小计
        BigDecimal bdBeginToCurrentPeriodFQtyTotal=new BigDecimal(0);
        // 开累材料量大计
        BigDecimal bdBeginToCurrentPeriodFQtyAllTotal=new BigDecimal(0);
        // 当期材料量小计
        BigDecimal bdCurrentPeriodFQtyTotal=new BigDecimal(0);
        // 当期材料量大计
        BigDecimal bdCurrentPeriodMQtyAllTotal=new BigDecimal(0);
        ProgStlItemSubFShow itemUnit=new ProgStlItemSubFShow();
        ProgStlItemSubFShow itemUnitNext=new ProgStlItemSubFShow();
        for(int i=0;i< progStlItemSubMShowListTemp.size();i++){
            itemUnit = progStlItemSubMShowListTemp.get(i);
            if ( itemUnit.getSubctt_SpareField()!=null && itemUnit.getSubctt_SpareField().equals("F1")){
            // 此处用数字类型数据作比较   开累量与合同金额比 此处和数量单不一样
                if (ToolUtil.getBdFromStrOrBdIgnoreNull(itemUnit.getEngSMng_AddUpToAmt())
                        .compareTo(  ToolUtil.getBdFromStrOrBdIgnoreNull(itemUnit.getSubctt_ContractAmount()))>=0) {
                    itemUnit.setIsUptoCttQtyFlag(true);
                }
            }
            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdBeginToCurrentPeriodFQtyTotal=
                    bdBeginToCurrentPeriodFQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngSMng_AddUpToAmt()));
            bdCurrentPeriodFQtyTotal=
                    bdCurrentPeriodFQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngSMng_ThisStageAmt()));
            //费税率金额不计入小计（费税率为子项时），当前和开累不计入大计
            if(itemUnit.getSubctt_SpareField()!=null&&itemUnit.getSubctt_Grade()>1&&(!ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("F1"))){
                bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount())).subtract(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }else{
                bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }
            if(itemUnit.getSubctt_SpareField()==null||ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("F1")){
                bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }
            bdBeginToCurrentPeriodFQtyAllTotal=
                    bdBeginToCurrentPeriodFQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngSMng_AddUpToAmt()));
            bdCurrentPeriodMQtyAllTotal=
                    bdCurrentPeriodMQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngSMng_ThisStageAmt()));
            progStlItemSubFShowList.add(itemUnit);
            if(i+1< progStlItemSubMShowListTemp.size()){
                itemUnitNext = progStlItemSubMShowListTemp.get(i+1);
                if(itemUnitNext.getSubctt_ParentPkid().equals("root")){
                    ProgStlItemSubFShow itemOfEsItemHieRelapTemp=new ProgStlItemSubFShow();
                    itemOfEsItemHieRelapTemp.setSubctt_Name("合计");
                    itemOfEsItemHieRelapTemp.setSubctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setSubctt_ContractQuantity(
                            ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                    itemOfEsItemHieRelapTemp.setSubctt_ContractAmount(
                            ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                    itemOfEsItemHieRelapTemp.setEngSMng_AddUpToAmt(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodFQtyTotal));
                    itemOfEsItemHieRelapTemp.setEngSMng_ThisStageAmt(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodFQtyTotal));
                    progStlItemSubFShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodFQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodFQtyTotal=new BigDecimal(0);
                }
            } else if(i+1== progStlItemSubMShowListTemp.size()){
                itemUnitNext = progStlItemSubMShowListTemp.get(i);
                ProgStlItemSubFShow progStlItemSubFShowTemp = new ProgStlItemSubFShow();
                progStlItemSubFShowTemp.setSubctt_Name("合计");
                progStlItemSubFShowTemp.setSubctt_Pkid("total" + i);
                progStlItemSubFShowTemp.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progStlItemSubFShowTemp.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progStlItemSubFShowTemp.setEngSMng_AddUpToAmt(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodFQtyTotal));
                progStlItemSubFShowTemp.setEngSMng_ThisStageAmt(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodFQtyTotal));
                progStlItemSubFShowList.add(progStlItemSubFShowTemp);
                progStlItemSubFShowListExcel.add(progStlItemSubFShowTemp);
                bdQuantityTotal = new BigDecimal(0);
                bdAmountTotal = new BigDecimal(0);
                bdBeginToCurrentPeriodFQtyTotal = new BigDecimal(0);
                bdCurrentPeriodFQtyTotal = new BigDecimal(0);
                // 总合计
                progStlItemSubFShowTemp = new ProgStlItemSubFShow();
                progStlItemSubFShowTemp.setSubctt_Name("总合计");
                progStlItemSubFShowTemp.setSubctt_Pkid("total_all" + i);
                progStlItemSubFShowTemp.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progStlItemSubFShowTemp.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));
                progStlItemSubFShowTemp.setEngSMng_AddUpToAmt(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodFQtyAllTotal));
                progStlItemSubFShowTemp.setEngSMng_ThisStageAmt(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodMQtyAllTotal));
                progStlItemSubFShowList.add(progStlItemSubFShowTemp);
                progStlItemSubFShowListExcel.add(progStlItemSubFShowTemp);
            }
        }
    }
    
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubFShow> ProgStlItemSubFShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemSubFShow ProgStlItemSubFShowTemp = new ProgStlItemSubFShow();
            ProgStlItemSubFShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            ProgStlItemSubFShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            ProgStlItemSubFShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            ProgStlItemSubFShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            ProgStlItemSubFShowTemp.setSubctt_Grade(itemUnit.getGrade());
            ProgStlItemSubFShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            ProgStlItemSubFShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            ProgStlItemSubFShowTemp.setSubctt_Name(itemUnit.getName());
            ProgStlItemSubFShowTemp.setSubctt_Remark(itemUnit.getRemark());
            ProgStlItemSubFShowTemp.setSubctt_Unit(itemUnit.getUnit());
            ProgStlItemSubFShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            ProgStlItemSubFShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            ProgStlItemSubFShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());

            ProgStlItemSubFShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            ProgStlItemSubFShowTemp.setSubctt_SpareField(itemUnit.getSpareField());

            ProgStlItemSubF progStlItemSubF =new ProgStlItemSubF();
            progStlItemSubF.setSubcttPkid(strSubcttPkid);
            progStlItemSubF.setSubcttItemPkid(itemUnit.getPkid());
            progStlItemSubF.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemSubF> progStlItemSubFList =
                    progStlItemSubFService.selectRecordsByExample(progStlItemSubF);
            if(progStlItemSubFList.size()>0){
                progStlItemSubF = progStlItemSubFList.get(0);
                ProgStlItemSubFShowTemp.setEngSMng_Pkid(progStlItemSubF.getPkid());
                ProgStlItemSubFShowTemp.setEngSMng_PeriodNo(progStlItemSubF.getPeriodNo());
                ProgStlItemSubFShowTemp.setEngSMng_SubcttPkid(progStlItemSubF.getSubcttPkid());
                ProgStlItemSubFShowTemp.setEngSMng_SubcttItemPkid(progStlItemSubF.getSubcttItemPkid());
                ProgStlItemSubFShowTemp.setEngSMng_AddUpToAmt( progStlItemSubF.getAddUpToAmt());
                ProgStlItemSubFShowTemp.setEngSMng_ThisStageAmt( progStlItemSubF.getThisStageAmt());
                ProgStlItemSubFShowTemp.setEngSMng_ArchivedFlag(progStlItemSubF.getArchivedFlag());
                ProgStlItemSubFShowTemp.setEngSMng_CreatedBy(progStlItemSubF.getCreatedBy());
                String strCreatedByNameTemp=cttInfoService.getUserName(progStlItemSubF.getCreatedBy());
                ProgStlItemSubFShowTemp.setEngSMng_CreatedByName(strCreatedByNameTemp);
                ProgStlItemSubFShowTemp.setEngSMng_CreatedTime(progStlItemSubF.getCreatedTime());
                ProgStlItemSubFShowTemp.setEngSMng_LastUpdBy(progStlItemSubF.getLastUpdBy());
                String strLastUpdByNameTemp=cttInfoService.getUserName(progStlItemSubF.getLastUpdBy());
                ProgStlItemSubFShowTemp.setEngSMng_LastUpdByName(strLastUpdByNameTemp);
                ProgStlItemSubFShowTemp.setEngSMng_LastUpdTime(progStlItemSubF.getLastUpdTime());
                ProgStlItemSubFShowTemp.setEngSMng_RecVersion(progStlItemSubF.getRecVersion());
                ProgStlItemSubFShowTemp.setEngSMng_Remark(progStlItemSubF.getRemark());
                if(ProgStlItemSubFShowTemp.getEngSMng_AddUpToAmt()!=null) {
                    if (ProgStlItemSubFShowTemp.getEngSMng_AddUpToAmt()
                            .equals(ProgStlItemSubFShowTemp.getSubctt_ContractAmount())) {
                        ProgStlItemSubFShowTemp.setIsUptoCttQtyFlag(true);
                    }
                }
            }
            ProgStlItemSubFShowListPara.add(ProgStlItemSubFShowTemp) ;
            recursiveDataTable(ProgStlItemSubFShowTemp.getSubctt_Pkid(), cttItemListPara, ProgStlItemSubFShowListPara);
        }
    }
    /*根据group和orderid临时编制编码strNo*/
    private List<ProgStlItemSubFShow> getStlSubCttEngSMngConstructList_DoFromatNo(
            List<ProgStlItemSubFShow> progStlItemSubFShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgStlItemSubFShow itemUnit: progStlItemSubFShowListPara){
            if(itemUnit.getSubctt_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getSubctt_Orderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getSubctt_Orderid().toString();
                }
            }
            else{
                if(itemUnit.getSubctt_Grade()==1){
                    strTemp=itemUnit.getSubctt_Orderid().toString() ;
                }
                else {
                    if (!itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getSubctt_Grade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        } else {
                            Integer intTemp=strTemp.indexOf(".",itemUnit.getSubctt_Grade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getSubctt_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getSubctt_Grade() ;
            itemUnit.setSubctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getSubctt_Grade(), strTemp)) ;
        }
        return progStlItemSubFShowListPara;
    }
    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsCttItemListByParentPkid(
            String strLevelParentPkid,
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

    /*重置*/
    public void resetAction(){
        progStlInfoShow =new ProgStlInfoShow();
        progStlItemSubFShowSel =new ProgStlItemSubFShow();
        progStlItemSubFShowUpd =new ProgStlItemSubFShow();
        progStlItemSubFShowDel =new ProgStlItemSubFShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurengFMng_CurrentPeriodFAmt("submit")){
                    return;
                }
                ProgStlItemSubFShow progStlItemSubFShowTemp =new ProgStlItemSubFShow();
                progStlItemSubFShowTemp.setEngSMng_SubcttPkid(progStlInfo.getStlPkid());
                progStlItemSubFShowTemp.setEngSMng_PeriodNo(progStlInfo.getPeriodNo());
                progStlItemSubFShowTemp.setEngSMng_SubcttItemPkid(progStlItemSubFShowUpd.getSubctt_Pkid());
                List<ProgStlItemSubF> progStlItemSubFListTemp =
                        progStlItemSubFService.isExistInDb(progStlItemSubFShowTemp);
                if (progStlItemSubFListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (progStlItemSubFListTemp.size() == 1) {
                    progStlItemSubFShowUpd.setEngSMng_Pkid (progStlItemSubFListTemp.get(0).getPkid());
                    updRecordAction(progStlItemSubFShowUpd);
                } else if (progStlItemSubFListTemp.size()==0){
                    progStlItemSubFShowUpd.setEngSMng_SubcttPkid(progStlInfo.getStlPkid());
                    progStlItemSubFShowUpd.setEngSMng_PeriodNo(progStlInfo.getPeriodNo());
                    progStlItemSubFShowUpd.setEngSMng_SubcttItemPkid(progStlItemSubFShowUpd.getSubctt_Pkid());
                    addRecordAction(progStlItemSubFShowUpd);
                }
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemSubFShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
            logger.error("增加数据失败，", e);
        }
    }

    public boolean blurengFMng_CurrentPeriodFAmt(String strBlurOrSubmitFlag){
            String strTemp= progStlItemSubFShowUpd.getEngSMng_ThisStageAmt().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }
            BigDecimal bDEngSMng_ThisStageAmtTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getEngSMng_ThisStageAmt());
            //开累量叠加,
            BigDecimal bigDecimalTemp=
                    bDEngSMng_AddUpToAmt.add(bDEngSMng_ThisStageAmtTemp).subtract(bDEngSMng_ThisStageAmt);

            BigDecimal bDSubctt_ContracttAmount=
                    ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getSubctt_ContractAmount());

//            if(strBlurOrSubmitFlag.equals("blur")) {
            if(bigDecimalTemp.compareTo(bDSubctt_ContracttAmount)>0){
                MessageUtil.addError("上期开累安全措施费+本期安全措施费>合同数量，请确认您输入的 （"
                        + bDEngSMng_ThisStageAmtTemp.toString() + "）！");
                return false;
            }
            progStlItemSubFShowUpd.setEngSMng_AddUpToAmt(bigDecimalTemp);

        return true;
    }

    private void addRecordAction(ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            progStlItemSubFService.insertRecord(progStlItemSubFShowPara);
            MessageUtil.addInfo("增加数据完成。");
        } catch (Exception e) {
            logger.error("增加数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            progStlItemSubFService.updateRecord(progStlItemSubFShowPara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void delRecordAction(ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            if(progStlItemSubFShowPara.getEngSMng_Pkid()==null||
                    progStlItemSubFShowDel.getEngSMng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara,ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                progStlItemSubFShowSel =(ProgStlItemSubFShow)BeanUtils.cloneBean(progStlItemSubFShowPara) ;
                progStlItemSubFShowSel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubFShowSel.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                progStlItemSubFShowUpd =(ProgStlItemSubFShow) BeanUtils.cloneBean(progStlItemSubFShowPara) ;
                progStlItemSubFShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubFShowUpd.getSubctt_StrNo()));

                bDEngSMng_ThisStageAmt=ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getEngSMng_ThisStageAmt());
                bDEngSMng_AddUpToAmt=
                        ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getEngSMng_AddUpToAmt());
            }else
            if(strSubmitTypePara.equals("Del")){
                progStlItemSubFShowDel =(ProgStlItemSubFShow) BeanUtils.cloneBean(progStlItemSubFShowPara) ;
                progStlItemSubFShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubFShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            strPowerType=strFlowType+strPowerType;
            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：录入完毕
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    strPassFlag="false";
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    strPassFlag="true";
                    MessageUtil.addInfo("数据录入未完！");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// 审核
                if(strPowerType.equals("CheckPass")){
                    // 状态标志：审核
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核通过！");
                }else if(strPowerType.equals("CheckFail")){
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(null);
                    // 原因：审核未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核未过！");
                }
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    try {
                        // 状态标志：复核
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        // 原因：复核通过
                        progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        progStlInfoService.updAutoLinkTask(progStlInfo);
                        MessageUtil.addInfo("数据复核通过！");
                    }catch (Exception e) {
                        logger.error("复核通过操作失败。", e);
                        MessageUtil.addError("复核通过操作失败。");
                        return;
                    }
                }else if(strPowerType.equals("DoubleCheckFail")){
                    try {
                        ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                        progStlInfoTemp.setStlType(EnumResType.RES_TYPE5.getCode());
                        progStlInfoTemp.setStlPkid(progStlInfo.getStlPkid());
                        progStlInfoTemp.setPeriodNo(progStlInfo.getPeriodNo());
                        List<ProgStlInfo> progStlInfoListTemp=progStlInfoService.getInitStlListByModel(progStlInfoTemp);
                        if(progStlInfoListTemp.size()>0) {
                            String SubcttStlPStatus = ToolUtil.getStrIgnoreNull(progStlInfoListTemp.get(0).getFlowStatus());
                            if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(SubcttStlPStatus) < 0) {
                                MessageUtil.addInfo("该数据已被分包价格结算批准，您无权进行操作！");
                                return;
                            }
                        }

                        // 这样写可以实现越级退回
                        if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                            progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                        }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                            progStlInfo.setFlowStatus(null);
                        }

                        // 原因：复核未过
                        progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                        progStlInfoService.updAutoLinkTask(progStlInfo);
                        MessageUtil.addInfo("数据复核未过！");
                    }catch (Exception e) {
                        logger.error("删除数据失败,复核未过操作失败。", e);
                        MessageUtil.addError("复核未过操作失败。");
                        return;
                    }
                }
            } else if(strPowerType.contains("Approve")){// 批准
                if(strPowerType.equals("ApprovePass")){
                    // 状态标志：批准
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据批准通过！");
                }else if(strPowerType.equals("ApproveFail")){
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        progStlInfo.setFlowStatus(null);
                    }
                    // 原因：批准未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);

                    MessageUtil.addInfo("数据批准未过！");
                }
            }
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public String onExportExcel()throws IOException, WriteException {
        if (this.progStlItemSubFShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "分包安全措施结算-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progStlItemSubF.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }
    public String getMaxId(String strStlType) {
        Integer intTemp;
        String strMaxId = progStlInfoService.getStrMaxStlId(strStlType);
        if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
            strMaxId = "STLQ" + ToolUtil.getStrToday() + "001";
        } else {
            if (strMaxId.length() > 3) {
                String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                if (ToolUtil.strIsDigit(strTemp)) {
                    intTemp = Integer.parseInt(strTemp);
                    intTemp = intTemp + 1;
                    strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                } else {
                    strMaxId += "001";
                }
            }
        }
        return strMaxId;
    }

    public void download(AttachmentModel attachmentModelPara){
        String strAttachment=attachmentModelPara.getCOLUMN_NAME();
        try{
            if(StringUtils.isEmpty(strAttachment) ){
                MessageUtil.addError("路径为空，无法下载！");
                logger.error("路径为空，无法下载！");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/stl/SubQ")+"/"+strAttachment;
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
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/stl/SubQ");
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
            progStlInfoShow.setAttachment(sb.toString());
            progStlInfoService.updateRecord(progStlInfoShow);
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
    // 附件
    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/stl/SubQ/" + attachmentModelPara.getCOLUMN_NAME());
    }



    /* 智能字段Start*/
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }


    public ProgStlItemSubFShow getProgStlItemSubFShowSel() {
        return progStlItemSubFShowSel;
    }

    public void setProgStlItemSubFShowSel(ProgStlItemSubFShow progStlItemSubFShowSel) {
        this.progStlItemSubFShowSel = progStlItemSubFShowSel;
    }

    public List<ProgStlItemSubFShow> getProgStlItemSubFShowList() {
        return progStlItemSubFShowList;
    }

    public void setProgStlItemSubFShowList(List<ProgStlItemSubFShow> progStlItemSubFShowList) {
        this.progStlItemSubFShowList = progStlItemSubFShowList;
    }

    public ProgStlItemSubFService getProgStlItemSubFService() {
        return progStlItemSubFService;
    }

    public void setProgStlItemSubFService(ProgStlItemSubFService progStlItemSubFService) {
        this.progStlItemSubFService = progStlItemSubFService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
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

    public ProgStlItemSubFShow getProgStlItemSubFShowDel() {
        return progStlItemSubFShowDel;
    }

    public void setProgStlItemSubFShowDel(ProgStlItemSubFShow progStlItemSubFShowDel) {
        this.progStlItemSubFShowDel = progStlItemSubFShowDel;
    }

    public ProgStlItemSubFShow getProgStlItemSubFShowUpd() {
        return progStlItemSubFShowUpd;
    }

    public void setProgStlItemSubFShowUpd(ProgStlItemSubFShow progStlItemSubFShowUpd) {
        this.progStlItemSubFShowUpd = progStlItemSubFShowUpd;
    }

    public String getStrPassFlag() {
        return strPassFlag;
    }

    public void setStrPassFlag(String strPassFlag) {
        this.strPassFlag = strPassFlag;
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

    public List<ProgStlItemSubFShow> getProgStlItemSubFShowListExcel() {
        return progStlItemSubFShowListExcel;
    }

    public void setProgStlItemSubFShowListExcel(List<ProgStlItemSubFShow> progStlItemSubFShowListExcel) {
        this.progStlItemSubFShowListExcel = progStlItemSubFShowListExcel;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public ProgStlInfoShow getProgStlInfoShow() {
        return progStlInfoShow;
    }

    public void setProgStlInfoShow(ProgStlInfoShow progStlInfoShow) {
        this.progStlInfoShow = progStlInfoShow;
    }

    public ProgStlInfo getProgStlInfo() {
        return progStlInfo;
    }

    public void setProgStlInfo(ProgStlInfo progStlInfo) {
        this.progStlInfo = progStlInfo;
    }

    public ProgStlItemSubQService getProgStlItemSubQService() {
        return progStlItemSubQService;
    }

    public void setProgStlItemSubQService(ProgStlItemSubQService progStlItemSubQService) {
        this.progStlItemSubQService = progStlItemSubQService;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }

    public String getStrPassVisible() {
        return strPassVisible;
    }

    public void setStrPassVisible(String strPassVisible) {
        this.strPassVisible = strPassVisible;
    }

    public String getStrPassFailVisible() {
        return strPassFailVisible;
    }

    public void setStrPassFailVisible(String strPassFailVisible) {
        this.strPassFailVisible = strPassFailVisible;
    }

    public String getStrSubcttPkid() {
        return strSubcttPkid;
    }

    public void setStrSubcttPkid(String strSubcttPkid) {
        this.strSubcttPkid = strSubcttPkid;
    }

    public String getStrFlowStatusRemark() {
        return strFlowStatusRemark;
    }

    public void setStrFlowStatusRemark(String strFlowStatusRemark) {
        this.strFlowStatusRemark = strFlowStatusRemark;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public BigDecimal getbDEngSMng_ThisStageAmt() {
        return bDEngSMng_ThisStageAmt;
    }

    public void setbDEngSMng_ThisStageAmt(BigDecimal bDEngSMng_ThisStageAmt) {
        this.bDEngSMng_ThisStageAmt = bDEngSMng_ThisStageAmt;
    }

    public BigDecimal getbDEngSMng_AddUpToAmt() {
        return bDEngSMng_AddUpToAmt;
    }

    public void setbDEngSMng_AddUpToAmt(BigDecimal bDEngSMng_AddUpToAmt) {
        this.bDEngSMng_AddUpToAmt = bDEngSMng_AddUpToAmt;
    }

    /*智能字段End*/
}
