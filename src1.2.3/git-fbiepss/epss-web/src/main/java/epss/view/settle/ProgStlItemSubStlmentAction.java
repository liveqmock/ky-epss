package epss.view.settle;

import epss.common.enums.EnumFlowStatusReason;
import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumSubcttType;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.ProgStlItemSubStlmentShow;
import epss.repository.model.model_show.ReportHeader;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import skyline.util.JxlsManager;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.flow.EsCommon;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
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
public class ProgStlItemSubStlmentAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemSubStlmentAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemSubStlmentService}")
    private ProgStlItemSubStlmentService progStlItemSubStlmentService;
    @ManagedProperty(value = "#{progStlItemSubMService}")
    private ProgStlItemSubMService progStlItemSubMService;
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;
    @ManagedProperty(value = "#{flowCtrlHisService}")
    private FlowCtrlHisService flowCtrlHisService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;

    private List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowList;
    private List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListForApprove;
    private List<ProgStlItemSubStlment> progSubstlItemShowListForAccountAndQry;
    private ReportHeader reportHeader;

    private Map beansMap;
    private ProgStlInfo progStlInfo;

    /*所属号*/
    private String strStlInfoPkid;

    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    // 控制记账按钮的显示
    private String strAccountBtnRendered;
    private String strApproveBtnRendered;
    private String strApprovedNotBtnRenderedForStlQ;
    private String strApprovedNotBtnRenderedForStlM;
    private String strFlowType;

    //附件
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //上传下载文件
    private StreamedContent downloadFile;

    @PostConstruct
    public void init() {
        this.attachmentList=new ArrayList<>();
        beansMap = new HashMap();
        reportHeader = new ReportHeader();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strStlInfoPkid")) {
            strStlInfoPkid = parammap.get("strStlInfoPkid").toString();
        }
        if (parammap.containsKey("strFlowType")) {
            strFlowType = parammap.get("strFlowType").toString();
        }
        initData();
    }

    /*初始化操作*/
    private void initData() {
        progStlItemSubStlmentShowList = new ArrayList<>();
        if(strStlInfoPkid == null){
            return;
        }
        progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
        initHeadMsg();
        CttInfo cttInfo =cttInfoService.getCttInfoByPkId(progStlInfo.getStlPkid());
        // 附件记录变成List
        attachmentList = ToolUtil.getListAttachmentByStrAttachment(cttInfo.getAttachment());
        if ("".equals(ToolUtil.getStrIgnoreNull(cttInfo.getType()))){
            strApprovedNotBtnRenderedForStlQ="true";
            strApprovedNotBtnRenderedForStlM="true";
        }else if (EnumSubcttType.TYPE0.getCode().equals(cttInfo.getType())){
            strApprovedNotBtnRenderedForStlQ="true";
            strApprovedNotBtnRenderedForStlM="false";
        } else if (EnumSubcttType.TYPE1.getCode().equals(cttInfo.getType())){
            strApprovedNotBtnRenderedForStlQ="false";
            strApprovedNotBtnRenderedForStlM="true";
        }else if (EnumSubcttType.TYPE2.getCode().equals(cttInfo.getType())){

        }else if (EnumSubcttType.TYPE3.getCode().equals(cttInfo.getType())){
            strApprovedNotBtnRenderedForStlQ="true";
            strApprovedNotBtnRenderedForStlM="true";
        }else if (EnumSubcttType.TYPE4.getCode().equals(cttInfo.getType())){

        }else if (EnumSubcttType.TYPE5.getCode().equals(cttInfo.getType())){

        }else if (EnumSubcttType.TYPE6.getCode().equals(cttInfo.getType())){

        }

        if (EnumFlowStatus.FLOW_STATUS3.getCode().compareTo(
                ToolUtil.getStrIgnoreNull(progStlInfo.getFlowStatus())) <= 0) {
            // 批准之后，在抽明细数据就从明细表中抽出了，将不再拼数据，为了报表的输出
            // 需要重新生成结算项stl2,stl3,stl4,stl5,stl6
            initMsgForExcel();
            if ("Account".equals(strFlowType) || "Qry".equals(strFlowType)) {
                if (EnumFlowStatus.FLOW_STATUS4.getCode().equals(progStlInfo.getFlowStatus())) {
                    strAccountBtnRendered = "false";
                } else {
                    strAccountBtnRendered = "true";
                }
                /*表内容设定,查询和记账，查P表*/
                progSubstlItemShowListForAccountAndQry = new ArrayList<ProgStlItemSubStlment>();
                progSubstlItemShowListForAccountAndQry =
                        progStlItemSubStlmentService.getProgStlItemSubStlmentListAccount(
                                progStlInfo.getStlPkid(), progStlInfo.getPeriodNo());
            }else
            if ("Approve".equals(strFlowType)) {
                strExportToExcelRendered = "true";
                strApproveBtnRendered = "false";
                List<ProgStlItemSubStlment> progStlItemSubStlmentList =
                        progStlItemSubStlmentService.getProgStlItemSubStlmentListAccount(
                                progStlInfo.getStlPkid(), progStlInfo.getPeriodNo());
                /*表内容设定，批准，查P表*/
                for (ProgStlItemSubStlment progStlItemSubStlment : progStlItemSubStlmentList) {
                    progStlItemSubStlmentShowList.add(
                            progStlItemSubStlmentService.fromModelToShow(progStlItemSubStlment));
                }
            }
        } else {
            strApproveBtnRendered = "true";
            List<CttItem> cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE2.getCode(), progStlInfo.getStlPkid());
            if (cttItemList.size() > 0) {
                recursiveDataTable("root", cttItemList, progStlItemSubStlmentShowList);
                progStlItemSubStlmentShowList =
                        getStlSubCttEngPMngConstructList_DoFromatNo(progStlItemSubStlmentShowList);
                /*表内容设定,复核，查询拼装*/
                progStlItemSubStlmentShowListForApprove = new ArrayList<>();
                setStlSubCttEngPMngConstructList_AddSettle();
            } else {
                strExportToExcelRendered = "false";
            }
        }
    }

    private void initHeadMsg() {
        // Excel中的头信息
        reportHeader.setStrDate(ToolUtil.getStrLastUpdDate());
        reportHeader.setStrSubcttPkid(progStlInfo.getStlPkid());
        reportHeader.setStrStlId(progStlInfo.getId());
        // From SubcttPkid To CstplPkid
        CttInfo cttInfo_Subctt = cttInfoService.getCttInfoByPkId(reportHeader.getStrSubcttPkid());
        reportHeader.setStrCstplPkid(cttInfo_Subctt.getParentPkid());
        reportHeader.setStrSubcttId(cttInfo_Subctt.getId());
        reportHeader.setStrSubcttName(cttInfo_Subctt.getName());
        reportHeader.setStrSignPartPkid(cttInfo_Subctt.getSignPartB());
        reportHeader.setStrSignPartName(signPartService.getEsInitCustByPkid(
                reportHeader.getStrSignPartPkid()).getName());
        // From CstplPkid To TkcttPkid
        CttInfo cttInfo_Cstpl = cttInfoService.getCttInfoByPkId(reportHeader.getStrCstplPkid());
        reportHeader.setStrTkcttPkid(cttInfo_Cstpl.getParentPkid());
        reportHeader.setStrCstplId(cttInfo_Cstpl.getId());
        reportHeader.setStrCstplName(cttInfo_Cstpl.getName());

        CttInfo cttInfo_Tkctt = cttInfoService.getCttInfoByPkId(reportHeader.getStrTkcttPkid());
        reportHeader.setStrTkcttId(cttInfo_Tkctt.getId());
        reportHeader.setStrTkcttName(cttInfo_Tkctt.getName());
        beansMap.put("reportHeader", reportHeader);
    }
    private void initMsgForExcel() {
        List<ProgStlItemSubStlmentShow> records0 = new ArrayList<ProgStlItemSubStlmentShow>();
        List<ProgStlItemSubStlmentShow> records1 = new ArrayList<ProgStlItemSubStlmentShow>();
        List<ProgStlItemSubStlment> progSubstlItemShowListForApproveTemp =
                progStlItemSubStlmentService.getProgStlItemSubStlmentListAccount(
                        progStlInfo.getStlPkid(), progStlInfo.getPeriodNo());
        for (ProgStlItemSubStlment progStlItemSubStlment : progSubstlItemShowListForApproveTemp) {
            ProgStlItemSubStlmentShow progStlItemSubStlmentShowTemp =
                    progStlItemSubStlmentService.fromModelToShow(progStlItemSubStlment);
            if (progStlItemSubStlmentShowTemp.getSubctt_ItemPkid().contains("stl")) {
                beansMap.put(progStlItemSubStlmentShowTemp.getSubctt_ItemPkid(), progStlItemSubStlmentShowTemp);
            }else{
                if (EnumResType.RES_TYPE3.getCode().equals(progStlItemSubStlmentShowTemp.getEngPMng_SubStlType())) {
                    records0.add(progStlItemSubStlmentShowTemp);
                }
                if (EnumResType.RES_TYPE4.getCode().equals(progStlItemSubStlmentShowTemp.getEngPMng_SubStlType())) {
                    records1.add(progStlItemSubStlmentShowTemp);
                }
            }
        }
        beansMap.put("records0", records0);
        beansMap.put("records1", records1);

        List<FlowCtrlHis> flowCtrlHisForSubcttStlList =
                flowCtrlHisService.getSubStlListByFlowCtrlHis(progStlInfo.getStlPkid(), progStlInfo.getPeriodNo());
        for (FlowCtrlHis flowCtrlHisTemp : flowCtrlHisForSubcttStlList) {
            if (EnumResType.RES_TYPE3.getCode().equals(flowCtrlHisTemp.getInfoType())) {
                if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlQMng", flowCtrlHisTemp);
                } else if (EnumFlowStatus.FLOW_STATUS1.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlQCheck", flowCtrlHisTemp);
                } else if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlQDoubleCheck", flowCtrlHisTemp);
                }
            } else if (EnumResType.RES_TYPE4.getCode().equals(flowCtrlHisTemp.getInfoType())) {
                if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlMMng", flowCtrlHisTemp);
                } else if (EnumFlowStatus.FLOW_STATUS1.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlMCheck", flowCtrlHisTemp);
                } else if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlMDoubleCheck", flowCtrlHisTemp);
                }
            } else if (("Account".equals(strFlowType) || "Qry".equals(strFlowType)) &&
                    EnumResType.RES_TYPE5.getCode().equals(flowCtrlHisTemp.getInfoType())) {
                if (EnumFlowStatus.FLOW_STATUS3.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlPApprove", flowCtrlHisTemp);
                } else if (EnumFlowStatus.FLOW_STATUS4.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlPAct", flowCtrlHisTemp);
                } else if (EnumFlowStatus.FLOW_STATUS5.getCode().equals(flowCtrlHisTemp.getFlowStatus())) {
                    beansMap.put("esInitPowerHisForSubcttStlPFile", flowCtrlHisTemp);
                }
            }
        }
    }

    private void setStlSubCttEngPMngConstructList_AddSettle() {
        try {
            BigDecimal bd0 = new BigDecimal(0);
            BigDecimal bdCurrentPeriodTotalAmtTemp = new BigDecimal(0);
            BigDecimal bdBeginToCurrentPeriodTotalAmtTemp = new BigDecimal(0);
            // 0:稅金率；1:质保金率；2:付款率；3:安全施工措施费率
            BigDecimal[] bdRates = {bd0, bd0, bd0, bd0};

            List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListForSubcttEngQ = new ArrayList<ProgStlItemSubStlmentShow>();
            List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListForSubcttEngM = new ArrayList<ProgStlItemSubStlmentShow>();
            for (ProgStlItemSubStlmentShow itemUnit : progStlItemSubStlmentShowList) {
                ProgStlItemSubStlmentShow progStlItemSubStlmentShowForSubcttEngQ =
                        (ProgStlItemSubStlmentShow) BeanUtils.cloneBean(itemUnit);
                ProgStlItemSubStlmentShow progStlItemSubStlmentShowForSubcttEngM =
                        (ProgStlItemSubStlmentShow) BeanUtils.cloneBean(itemUnit);
                progStlItemSubStlmentShowListForSubcttEngQ.add(progStlItemSubStlmentShowForSubcttEngQ);
                progStlItemSubStlmentShowListForSubcttEngM.add(progStlItemSubStlmentShowForSubcttEngM);
            }
            progStlItemSubStlmentShowList.clear();
            ProgStlItemSubStlmentShow stl1 = new ProgStlItemSubStlmentShow();
            stl1.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl1.setSubctt_ItemPkid("stl1");
            stl1.setSubctt_ItemName("造价计算");
            stl1.setEngPMng_SubStlType(EnumResType.RES_TYPE3.getCode());
            stl1.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            progStlItemSubStlmentShowList.add(stl1);
            progStlItemSubStlmentShowListForApprove.add(stl1);

            for (ProgStlItemSubStlmentShow itemUnit : progStlItemSubStlmentShowListForSubcttEngQ) {
                // 从工程数量结算中抽取结算数据
                ProgStlItemSubQ progStlItemSubQTemp = new ProgStlItemSubQ();
                progStlItemSubQTemp.setSubcttPkid(reportHeader.getStrSubcttPkid());
                progStlItemSubQTemp.setSubcttItemPkid(itemUnit.getSubctt_ItemPkid());
                progStlItemSubQTemp.setPeriodNo(progStlInfo.getPeriodNo());
                List<ProgStlItemSubQ> progStlItemSubQList =
                        progStlItemSubQService.selectRecordsByExample(progStlItemSubQTemp);
                itemUnit.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
                itemUnit.setEngPMng_SubStlType(EnumResType.RES_TYPE3.getCode());
                if (progStlItemSubQList.size() <= 0) {
                    if (itemUnit.getSubctt_SpareField() != null) {
                        //税金率
                        if (ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("0")) {
                            bdRates[0] = ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                        }
                        //质保金率
                        if (ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("1")) {
                            bdRates[1] = ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                        }
                    } else {
                        if ("Approve".equals(strFlowType)) {
                            progStlItemSubStlmentShowListForApprove.add(itemUnit);
                        }
                    }
                } else {
                    progStlItemSubQTemp = progStlItemSubQList.get(0);
                    itemUnit.setEngPMng_BeginToCurrentPeriodEQty(progStlItemSubQTemp.getBeginToCurrentPeriodEQty());
                    itemUnit.setEngPMng_CurrentPeriodEQty(progStlItemSubQTemp.getCurrentPeriodEQty());

                    BigDecimal bdContractUnitPriceInSubctt = itemUnit.getSubctt_ContractUnitPrice() == null ?
                            new BigDecimal(0) : itemUnit.getSubctt_ContractUnitPrice();

                    if (itemUnit.getEngPMng_BeginToCurrentPeriodEQty() == null) {
                        itemUnit.setEngPMng_BeginToCurrentPeriodAmt(null);
                    } else {
                        BigDecimal bdBeginToCurrentPeriodEQty = itemUnit.getEngPMng_BeginToCurrentPeriodEQty();
                        itemUnit.setEngPMng_BeginToCurrentPeriodAmt(bdContractUnitPriceInSubctt.multiply(bdBeginToCurrentPeriodEQty));
                    }

                    if (itemUnit.getEngPMng_CurrentPeriodEQty() == null) {
                        itemUnit.setEngPMng_CurrentPeriodAmt(null);
                    } else {
                        BigDecimal bdCurrentPeriodEQty = itemUnit.getEngPMng_CurrentPeriodEQty();
                        itemUnit.setEngPMng_CurrentPeriodAmt(bdContractUnitPriceInSubctt.multiply(bdCurrentPeriodEQty));
                    }
                    bdCurrentPeriodTotalAmtTemp = bdCurrentPeriodTotalAmtTemp.add(
                            getBdIgnoreNull(itemUnit.getEngPMng_CurrentPeriodAmt()));
                    bdBeginToCurrentPeriodTotalAmtTemp = bdBeginToCurrentPeriodTotalAmtTemp.add(
                            getBdIgnoreNull(itemUnit.getEngPMng_BeginToCurrentPeriodAmt()));
                    progStlItemSubStlmentShowList.add(itemUnit);
                    progStlItemSubStlmentShowListForApprove.add(itemUnit);
                }
            }

            List<ProgStlItemSubStlmentShow> records0 = new ArrayList<ProgStlItemSubStlmentShow>();
            records0.addAll(progStlItemSubStlmentShowList);
            beansMap.put("records0", records0);

            //1小计
            ProgStlItemSubStlmentShow stl2 = new ProgStlItemSubStlmentShow();
            stl2.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl2.setSubctt_ItemPkid("stl2");
            stl2.setSubctt_ItemName("小计");
            stl2.setEngPMng_SubStlType(EnumResType.RES_TYPE3.getCode());
            stl2.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl2.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp);
            stl2.setEngPMng_BeginToCurrentPeriodAmt(bdBeginToCurrentPeriodTotalAmtTemp);
            progStlItemSubStlmentShowList.add(stl2);
            progStlItemSubStlmentShowListForApprove.add(stl2);

            //3扣款
            ProgStlItemSubStlmentShow stl3 = new ProgStlItemSubStlmentShow();
            stl3.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl3.setSubctt_ItemPkid("stl3");
            stl3.setSubctt_ItemName("扣款(材料)");
            stl3.setEngPMng_SubStlType(EnumResType.RES_TYPE4.getCode());
            stl3.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            progStlItemSubStlmentShowList.add(stl3);
            progStlItemSubStlmentShowListForApprove.add(stl3);

            BigDecimal bdTotalAmt = new BigDecimal(0);
            BigDecimal bdTotalAllAmt = new BigDecimal(0);
            List<ProgStlItemSubStlmentShow> records1 = new ArrayList<ProgStlItemSubStlmentShow>();

            for (ProgStlItemSubStlmentShow itemUnit : progStlItemSubStlmentShowListForSubcttEngM) {
                // 从工程材料结算中抽取结算数据
                ProgStlItemSubM progStlItemSubM = new ProgStlItemSubM();
                progStlItemSubM.setSubcttPkid(reportHeader.getStrSubcttPkid());
                progStlItemSubM.setSubcttItemPkid(itemUnit.getSubctt_ItemPkid());
                progStlItemSubM.setPeriodNo(progStlInfo.getPeriodNo());
                List<ProgStlItemSubM> progStlItemSubMList =
                        progStlItemSubMService.selectRecordsByExample(progStlItemSubM);
                itemUnit.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
                itemUnit.setEngPMng_SubStlType(EnumResType.RES_TYPE4.getCode());
                if (progStlItemSubMList.size() <= 0) {
                    if ("Approve".equals(strFlowType)) {
                        progStlItemSubStlmentShowListForApprove.add(itemUnit);
                    }
                } else {
                    progStlItemSubM = progStlItemSubMList.get(0);
                    itemUnit.setSubctt_ContractUnitPrice(itemUnit.getSubctt_SignPartAPrice());
                    itemUnit.setEngPMng_BeginToCurrentPeriodEQty(progStlItemSubM.getBeginToCurrentPeriodMQty());
                    itemUnit.setEngPMng_CurrentPeriodEQty(progStlItemSubM.getCurrentPeriodMQty());

                    BigDecimal bdSubctt_ContractUnitPrice = ToolUtil.getBdIgnoreNull(
                            itemUnit.getSubctt_ContractUnitPrice());
                    BigDecimal bdEngPMng_CurrentPeriodEQty = ToolUtil.getBdIgnoreNull(
                            itemUnit.getEngPMng_CurrentPeriodEQty());
                    BigDecimal bdEngPMng_BeginToCurrentPeriodEQty = ToolUtil.getBdIgnoreNull(
                            itemUnit.getEngPMng_BeginToCurrentPeriodEQty());

                    BigDecimal bdEngPMng_CurrentPeriodAmtTemp = bdSubctt_ContractUnitPrice.multiply(bdEngPMng_CurrentPeriodEQty);
                    BigDecimal bdEngPMng_BeginToCurrentPeriodAmtTemp = bdSubctt_ContractUnitPrice.multiply(bdEngPMng_BeginToCurrentPeriodEQty);

                    bdTotalAmt = bdTotalAmt.add(bdEngPMng_CurrentPeriodAmtTemp);
                    bdTotalAllAmt = bdTotalAllAmt.add(bdSubctt_ContractUnitPrice.multiply(bdEngPMng_BeginToCurrentPeriodEQty));

                    itemUnit.setEngPMng_CurrentPeriodAmt(
                            ToolUtil.getBdFrom0ToNull(bdEngPMng_CurrentPeriodAmtTemp));
                    itemUnit.setEngPMng_BeginToCurrentPeriodAmt(
                            ToolUtil.getBdFrom0ToNull(bdEngPMng_BeginToCurrentPeriodAmtTemp));
                    progStlItemSubStlmentShowList.add(itemUnit);
                    progStlItemSubStlmentShowListForApprove.add(itemUnit);
                    records1.add(itemUnit);
                }
            }

            beansMap.put("records1", records1);

            ProgStlItemSubStlmentShow stl4 = new ProgStlItemSubStlmentShow();
            stl4.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl4.setSubctt_ItemPkid("stl4");
            stl4.setSubctt_ItemName("扣款(税)");
            stl4.setEngPMng_SubStlType(EnumResType.RES_TYPE4.getCode());
            stl4.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl4.setSubctt_ContractUnitPrice(bdRates[0]);
            stl4.setEngPMng_CurrentPeriodAmt(stl2.getEngPMng_CurrentPeriodAmt().multiply(bdRates[0]));
            stl4.setEngPMng_BeginToCurrentPeriodAmt(stl2.getEngPMng_CurrentPeriodAmt().multiply(bdRates[0]));
            progStlItemSubStlmentShowList.add(stl4);
            progStlItemSubStlmentShowListForApprove.add(stl4);

            ProgStlItemSubStlmentShow stl5 = new ProgStlItemSubStlmentShow();
            stl5.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl5.setSubctt_ItemPkid("stl5");
            stl5.setSubctt_ItemName("小计");
            stl5.setEngPMng_SubStlType(EnumResType.RES_TYPE4.getCode());
            stl5.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl5.setEngPMng_CurrentPeriodAmt(bdTotalAmt.add(stl4.getEngPMng_CurrentPeriodAmt()));
            stl5.setEngPMng_BeginToCurrentPeriodAmt(bdTotalAllAmt.add(stl4.getEngPMng_BeginToCurrentPeriodAmt()));
            progStlItemSubStlmentShowList.add(stl5);
            progStlItemSubStlmentShowListForApprove.add(stl5);

            ProgStlItemSubStlmentShow stl6 = new ProgStlItemSubStlmentShow();
            stl6.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl6.setSubctt_ItemPkid("stl6");
            stl6.setSubctt_ItemName("本期净结算额");
            stl6.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl6.setEngPMng_CurrentPeriodAmt(
                    stl2.getEngPMng_CurrentPeriodAmt().subtract(stl5.getEngPMng_CurrentPeriodAmt()));
            stl6.setEngPMng_BeginToCurrentPeriodAmt(
                    stl2.getEngPMng_BeginToCurrentPeriodAmt().subtract(stl5.getEngPMng_BeginToCurrentPeriodAmt()));
            progStlItemSubStlmentShowList.add(stl6);
            progStlItemSubStlmentShowListForApprove.add(stl6);

            ProgStlItemSubStlmentShow stl7 = new ProgStlItemSubStlmentShow();
            stl7.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl7.setSubctt_ItemPkid("stl7");
            stl7.setSubctt_ItemName("其它(质保金)");
            stl7.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl7.setSubctt_ContractUnitPrice(bdRates[1]);
            stl7.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            stl7.setEngPMng_BeginToCurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            progStlItemSubStlmentShowList.add(stl7);
            progStlItemSubStlmentShowListForApprove.add(stl7);

            ProgStlItemSubStlmentShow stl8 = new ProgStlItemSubStlmentShow();
            stl8.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl8.setSubctt_ItemPkid("stl8");
            stl8.setSubctt_ItemName("小计");
            stl8.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl8.setEngPMng_CurrentPeriodAmt(stl7.getEngPMng_CurrentPeriodAmt());
            stl8.setEngPMng_BeginToCurrentPeriodAmt(stl7.getEngPMng_BeginToCurrentPeriodAmt());
            progStlItemSubStlmentShowList.add(stl8);
            progStlItemSubStlmentShowListForApprove.add(stl8);

            ProgStlItemSubStlmentShow stl9 = new ProgStlItemSubStlmentShow();
            stl9.setSubctt_Pkid(reportHeader.getStrSubcttPkid());
            stl9.setSubctt_ItemPkid("stl9");
            stl9.setSubctt_ItemName("合计(扣除其它栏款项后本期结算价值)");
            stl9.setEngPMng_PeriodNo(progStlInfo.getPeriodNo());
            stl9.setEngPMng_CurrentPeriodAmt(
                    stl6.getEngPMng_CurrentPeriodAmt().subtract(stl8.getEngPMng_CurrentPeriodAmt()));
            stl9.setEngPMng_BeginToCurrentPeriodAmt(
                    stl6.getEngPMng_BeginToCurrentPeriodAmt().subtract(stl8.getEngPMng_BeginToCurrentPeriodAmt()));
            progStlItemSubStlmentShowList.add(stl9);
            progStlItemSubStlmentShowListForApprove.add(stl9);
            beansMap.put("stl1", stl1);
            beansMap.put("stl2", stl2);
            beansMap.put("stl3", stl3);
            beansMap.put("stl4", stl4);
            beansMap.put("stl5", stl5);
            beansMap.put("stl6", stl6);
            beansMap.put("stl7", stl7);
            beansMap.put("stl8", stl8);
            beansMap.put("stl9", stl9);
        } catch (Exception e) {
            MessageUtil.addInfo("结算数据引取失败！" + e.getMessage());
        }
    }

    private BigDecimal getBdIgnoreNull(BigDecimal bigDecimalPara) {
        return bigDecimalPara == null ? new BigDecimal(0) : bigDecimalPara;
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<ProgStlItemSubStlmentShow> sProgStlItemSubStlmentShowListPara) {
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList = new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList = getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for (CttItem itemUnit : subCttItemList) {
            ProgStlItemSubStlmentShow progStlItemSubStlmentShowTemp = new ProgStlItemSubStlmentShow();
            progStlItemSubStlmentShowTemp.setSubctt_Pkid(itemUnit.getBelongToPkid());
            progStlItemSubStlmentShowTemp.setSubctt_Name(reportHeader.getStrSubcttName());
            progStlItemSubStlmentShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progStlItemSubStlmentShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemSubStlmentShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemSubStlmentShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progStlItemSubStlmentShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progStlItemSubStlmentShowTemp.setSubctt_ItemPkid(itemUnit.getPkid());
            progStlItemSubStlmentShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemSubStlmentShowTemp.setSubctt_ItemName(itemUnit.getName());
            progStlItemSubStlmentShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubStlmentShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubStlmentShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubStlmentShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progStlItemSubStlmentShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            progStlItemSubStlmentShowTemp.setSubctt_Remark(itemUnit.getRemark());
            progStlItemSubStlmentShowTemp.setSubctt_SpareField(itemUnit.getSpareField());
            sProgStlItemSubStlmentShowListPara.add(progStlItemSubStlmentShowTemp);
            recursiveDataTable(
                    progStlItemSubStlmentShowTemp.getSubctt_ItemPkid(),
                    cttItemListPara,
                    sProgStlItemSubStlmentShowListPara);
        }
    }

    public void onExportExcel() throws IOException, WriteException {
        String strExcelName;
        if (beansMap.get("esInitPowerHisForSubcttStlQMng") != null) {
            String qMngImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlQMng"));
            beansMap.put("qMngImagName",qMngImagName);
        }

        if (beansMap.get("esInitPowerHisForSubcttStlQCheck") != null) {
            String qCheckImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlQCheck"));
            beansMap.put("qCheckImagName",qCheckImagName);
        }

        if (beansMap.get("esInitPowerHisForSubcttStlQDoubleCheck") != null) {
            String qDoubleCheckImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlQDoubleCheck"));
            beansMap.put("qDoubleCheckImagName",qDoubleCheckImagName);
        }
        if (beansMap.get("esInitPowerHisForSubcttStlMMng") != null) {
            String mMngImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlMMng"));
            beansMap.put("mMngImagName",mMngImagName);
        }
        if (beansMap.get("esInitPowerHisForSubcttStlMCheck") != null) {
            String mCheckImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlMCheck"));
            beansMap.put("mCheckImagName",mCheckImagName);
        }
        if (beansMap.get("esInitPowerHisForSubcttStlMDoubleCheck") != null) {
            String mDoubleCheckImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlMDoubleCheck"));
            beansMap.put("mDoubleCheckImagName",mDoubleCheckImagName);
        }

        if (beansMap.get("esInitPowerHisForSubcttStlPApprove") != null) {
            String pApproveImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlPApprove"));
            beansMap.put("pApproveImagName",pApproveImagName);
        }

        if (beansMap.get("esInitPowerHisForSubcttStlPAct") != null) {
            String pActImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlPAct"));
            beansMap.put("pActImagName",pActImagName);
        }

        if (beansMap.get("esInitPowerHisForSubcttStlPFile") != null) {
            String pFileImagName =
                    getOperAttachment(beansMap.get("esInitPowerHisForSubcttStlPFile"));
            beansMap.put("pFileCheckImagName",pFileImagName);
        }
        if ("Account".equals(strFlowType) || "Qry".equals(strFlowType)) {
            if (this.progSubstlItemShowListForAccountAndQry.size() == 0) {
                MessageUtil.addWarn("记录为空...");
                return ;
            }else {
                String  stractSubstlNum=this.progSubstlItemShowListForAccountAndQry.size()+"";
                short actSubstlNum=Short.parseShort(stractSubstlNum);
                beansMap.put("actSubstlNum",actSubstlNum);
                strExcelName = "progStlItemSubStlmentAccount.xls";
            }
        } else {
            if (this.progStlItemSubStlmentShowList.size() == 0) {
                MessageUtil.addWarn("记录为空...");
                return ;
            }else {
                String strprogStlItemSubStlmentNum=this.progStlItemSubStlmentShowList.size()+"";
                short progStlItemSubStlmentNum=Short.parseShort(strprogStlItemSubStlmentNum);
                beansMap.put("progStlItemSubStlmentNum",progStlItemSubStlmentNum);
                strExcelName = "progStlItemSubStlmentApprove.xls";
            }
        }
        String excelFilename = "分包工程预结算单-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        String strTrue=jxls.exportList(excelFilename, beansMap, strExcelName);
        // 其他状态的票据需要添加时再修改导出文件名
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgStlItemSubStlmentShow> getStlSubCttEngPMngConstructList_DoFromatNo(
             List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowListPara) {
        String strTemp = "";
        Integer intBeforeGrade = -1;
        for (ProgStlItemSubStlmentShow itemUnit : progStlItemSubStlmentShowListPara) {
            if (itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                if (strTemp.lastIndexOf(".") < 0) {
                    strTemp = itemUnit.getSubctt_Orderid().toString();
                } else {
                    strTemp = strTemp.substring(0, strTemp.lastIndexOf(".")) + "." + itemUnit.getSubctt_Orderid().toString();
                }
            } else {
                if (itemUnit.getSubctt_Grade() == 1) {
                    strTemp = itemUnit.getSubctt_Orderid().toString();
                } else {
                    if (!itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getSubctt_Grade().compareTo(intBeforeGrade) > 0) {
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        } else {
                            Integer intTemp = strTemp.indexOf(".", itemUnit.getSubctt_Grade() - 1);
                            strTemp = strTemp.substring(0, intTemp);
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade = itemUnit.getSubctt_Grade();
            itemUnit.setSubctt_StrNo(ToolUtil.padLeftSpace_DoLevel(itemUnit.getSubctt_Grade(), strTemp));
        }
        return progStlItemSubStlmentShowListPara;
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
                                                        List<CttItem> cttItemListPara) {
        List<CttItem> tempCttItemList = new ArrayList<CttItem>();
        /*避开重复链接数据库*/
        for (CttItem itemUnit : cttItemListPara) {
            if (strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())) {
                tempCttItemList.add(itemUnit);
            }
        }
        return tempCttItemList;
    }

    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/" + attachmentModelPara.getCOLUMN_NAME());
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

    /**
     * 根据权限进行审核
     *
     * @param strPowerType
     */
    @Transactional
    public void onClickForPowerAction(String strPowerType) {
        try {
            // 批准
            if (strPowerType.contains("Approve")) {
                if (strPowerType.equals("ApprovePass")) {
                    ProgStlInfo progStlInfoTemp = (ProgStlInfo) BeanUtils.cloneBean(progStlInfo);
                    // 结算登记表和Power表更新,并将价格结算的完整数据插入至PROG_STL_ITEM_SUB_STLMENT表
                    progStlInfoTemp.setId(getMaxIdPlusOne());
                    progStlInfoService.updSubPApprovePass(
                            progStlInfoTemp, progStlItemSubStlmentShowListForApprove);
                    strApproveBtnRendered = "false";
                    strApprovedNotBtnRenderedForStlQ = "true";
                    strApprovedNotBtnRenderedForStlM = "true";
                } else if (strPowerType.equals("ApproveFailToQ")) {
                    ProgStlInfo progStlInfoTemp = (ProgStlInfo) BeanUtils.cloneBean(progStlInfo);
                    progStlInfoTemp.setStlType(EnumResType.RES_TYPE5.getCode());
                    progStlInfoService.delSubPApproveFailTo(progStlInfoTemp, EnumResType.RES_TYPE3.getCode());
                    strApproveBtnRendered = "false";
                    strApprovedNotBtnRenderedForStlQ = "false";
                    strApprovedNotBtnRenderedForStlM = "false";
                } else if (strPowerType.equals("ApproveFailToM")) {
                    ProgStlInfo progStlInfoTemp = (ProgStlInfo) BeanUtils.cloneBean(progStlInfo);
                    progStlInfoTemp.setStlType(EnumResType.RES_TYPE5.getCode());
                    progStlInfoService.delSubPApproveFailTo(progStlInfoTemp, EnumResType.RES_TYPE4.getCode());
                    strApproveBtnRendered = "false";
                    strApprovedNotBtnRenderedForStlQ = "false";
                    strApprovedNotBtnRenderedForStlM = "false";
                }
                MessageUtil.addInfo("批准数据完成。");
            }else if (strPowerType.contains("AccountPass")) {
                ProgStlInfo progStlInfoTemp = (ProgStlInfo) BeanUtils.cloneBean(progStlInfo);
                // 结算登记表和Power表更新,并将价格结算的完整数据插入至PROG_STL_ITEM_SUB_STLMENT表
                progStlInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS4.getCode());
                progStlInfoTemp.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON7.getCode());
                progStlInfoService.updateRecord(progStlInfoTemp);
                strAccountBtnRendered = "false";
                MessageUtil.addInfo("数据记账成功。");
            }
        } catch (Exception e) {
            logger.error("批准数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private String getMaxIdPlusOne() {
        try {
            Integer intTemp;
            String strMaxTkStlId = progStlInfoService.getStrMaxStlId(progStlInfo.getStlType());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxTkStlId))) {
                strMaxTkStlId = "STLP" + ToolUtil.getStrToday() + "001";
            } else {
                if (strMaxTkStlId.length() > 3) {
                    String strTemp = strMaxTkStlId.substring(strMaxTkStlId.length() - 3).replaceFirst("^0+", "");
                    if (ToolUtil.strIsDigit(strTemp)) {
                        intTemp = Integer.parseInt(strTemp);
                        intTemp = intTemp + 1;
                        strMaxTkStlId = strMaxTkStlId.substring(0, strMaxTkStlId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                    } else {
                        strMaxTkStlId += "001";
                    }
                }
            }
            return strMaxTkStlId;
        } catch (Exception e) {
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
            return "";
        }
    }

    private String getOperAttachment(Object obj) {
        if(((FlowCtrlHis) obj).getCreatedBy()!=null){
            return deptOperService.getOperByPkid(((FlowCtrlHis) obj).getCreatedBy()).getAttachment();
        }
        return null;
    }

    /* 智能字段Start*/
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

    public List<ProgStlItemSubStlmentShow> getProgStlItemSubStlmentShowList() {
        return progStlItemSubStlmentShowList;
    }

    public void setProgStlItemSubStlmentShowList(List<ProgStlItemSubStlmentShow> progStlItemSubStlmentShowList) {
        this.progStlItemSubStlmentShowList = progStlItemSubStlmentShowList;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public ProgStlItemSubStlmentService getProgStlItemSubStlmentService() {
        return progStlItemSubStlmentService;
    }

    public void setProgStlItemSubStlmentService(ProgStlItemSubStlmentService progStlItemSubStlmentService) {
        this.progStlItemSubStlmentService = progStlItemSubStlmentService;
    }

    public ProgStlItemSubMService getProgStlItemSubMService() {
        return progStlItemSubMService;
    }

    public void setProgStlItemSubMService(ProgStlItemSubMService progStlItemSubMService) {
        this.progStlItemSubMService = progStlItemSubMService;
    }

    public ProgStlItemSubQService getProgStlItemSubQService() {
        return progStlItemSubQService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public void setProgStlItemSubQService(ProgStlItemSubQService progStlItemSubQService) {
        this.progStlItemSubQService = progStlItemSubQService;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    public String getStrAccountBtnRendered() {
        return strAccountBtnRendered;
    }

    public void setStrAccountBtnRendered(String strAccountBtnRendered) {
        this.strAccountBtnRendered = strAccountBtnRendered;
    }

    public String getStrApproveBtnRendered() {
        return strApproveBtnRendered;
    }

    public void setStrApproveBtnRendered(String strApproveBtnRendered) {
        this.strApproveBtnRendered = strApproveBtnRendered;
    }

    public List<ProgStlItemSubStlment> getProgSubstlItemShowListForAccountAndQry() {
        return progSubstlItemShowListForAccountAndQry;
    }

    public void setProgSubstlItemShowListForAccountAndQry(List<ProgStlItemSubStlment> progSubstlItemShowListForAccountAndQry) {
        this.progSubstlItemShowListForAccountAndQry = progSubstlItemShowListForAccountAndQry;
    }

    public FlowCtrlHisService getFlowCtrlHisService() {
        return flowCtrlHisService;
    }

    public void setFlowCtrlHisService(FlowCtrlHisService flowCtrlHisService) {
        this.flowCtrlHisService = flowCtrlHisService;
    }

    public String getStrApprovedNotBtnRenderedForStlQ() {
        return strApprovedNotBtnRenderedForStlQ;
    }

    public void setStrApprovedNotBtnRenderedForStlQ(String strApprovedNotBtnRenderedForStlQ) {
        this.strApprovedNotBtnRenderedForStlQ = strApprovedNotBtnRenderedForStlQ;
    }

    public String getStrApprovedNotBtnRenderedForStlM() {
        return strApprovedNotBtnRenderedForStlM;
    }

    public void setStrApprovedNotBtnRenderedForStlM(String strApprovedNotBtnRenderedForStlM) {
        this.strApprovedNotBtnRenderedForStlM = strApprovedNotBtnRenderedForStlM;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
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
}
