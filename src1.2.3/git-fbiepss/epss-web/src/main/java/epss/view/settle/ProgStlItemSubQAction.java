package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubQShow;
import jxl.write.WriteException;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
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
public class ProgStlItemSubQAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemSubQAction.class);
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
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    private List<ProgStlItemSubQShow> progStlItemSubQShowList;
    private ProgStlItemSubQShow progStlItemSubQShowSel;
    private ProgStlItemSubQShow progStlItemSubQShowUpd;
    private ProgStlItemSubQShow progStlItemSubQShowDel;
    private BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEngQMng_CurrentPeriodEQtyInDB;

    private String strSubmitType;
    private String strSubcttPkid;
    private ProgStlInfo progStlInfo;
    private ProgStlInfoShow progStlInfoShow;

    /*控制维护画面层级部分的显示*/
    private String strPassVisible;
    private String strPassFailVisible;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgStlItemSubQShow> progStlItemSubQShowListExcel;
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
            if(parammap.containsKey("strFlowType")){
                strFlowType=parammap.get("strFlowType").toString();
            }
            if(parammap.containsKey("strStlInfoPkid")){
                String strStlInfoPkidTemp=parammap.get("strStlInfoPkid").toString();
                progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkidTemp);
                strSubcttPkid= progStlInfo.getStlPkid();

                strPassVisible = "true";
                strPassFailVisible = "true";
                if ("Mng".equals(strFlowType)) {
                    if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfo.getFlowStatus())){
                        strPassVisible = "false";
                    }else {
                        strPassFailVisible = "false";
                    }
                }else {
                    if (("Check".equals(strFlowType)&&EnumFlowStatus.FLOW_STATUS1.getCode().equals(progStlInfo.getFlowStatus()))
                            ||("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfo.getFlowStatus()))){
                        strPassVisible = "false";
                    }
                }
                resetAction();
                initData();
            }
        }catch (Exception e){
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
            progStlItemSubQShowList =new ArrayList<>();
            progStlItemSubQShowListExcel =new ArrayList<>();
            attachmentList=new ArrayList<>();
            attachmentList=ToolUtil.getListAttachmentByStrAttachment(progStlInfoShow.getAttachment());
            recursiveDataTable("root", cttItemList, progStlItemSubQShowList);
            progStlItemSubQShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progStlItemSubQShowList);
            setItemOfEsItemHieRelapList_AddTotal();
            // Excel报表形成
            progStlItemSubQShowListExcel =new ArrayList<>();
            for(ProgStlItemSubQShow itemUnit: progStlItemSubQShowList){
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
                itemUnit.setEngQMng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngQMng_CurrentPeriodEQty()));
                itemUnit.setEngQMng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()));

                ProgStlItemSubQShow itemUnitTemp= (ProgStlItemSubQShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                if(itemUnitTemp.getSubctt_SpareField()==null){
                    itemUnitTemp.setSubctt_ContractAmountForExcel(
                            ToolUtil.getStrFromBdIgnoreZeroNull(itemUnitTemp.getSubctt_ContractAmount()));
                    // 分包工程量结算
                    itemUnitTemp.setEngQMng_CurrentPeriodEQtyForExcel(
                            ToolUtil.getStrFromBdIgnoreZeroNull(itemUnitTemp.getEngQMng_CurrentPeriodEQty()));
                    itemUnitTemp.setEngQMng_BeginToCurrentPeriodEQtyForExcel(
                            ToolUtil.getStrFromBdIgnoreZeroNull(itemUnitTemp.getEngQMng_BeginToCurrentPeriodEQty()));
                }else{
                    String strSubctt_ContractAmountInPercent=ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(itemUnitTemp.getSubctt_ContractAmount()));
                    itemUnitTemp.setSubctt_ContractAmountForExcel(strSubctt_ContractAmountInPercent);
                    String strEngQMng_BeginToCurrentPeriodEQtyInPercent=ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(itemUnitTemp.getEngQMng_BeginToCurrentPeriodEQty()));
                    itemUnitTemp.setEngQMng_BeginToCurrentPeriodEQtyForExcel(strEngQMng_BeginToCurrentPeriodEQtyInPercent);
                    String strEengQMng_CurrentPeriodEQtyInPercentInPercent=ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(itemUnitTemp.getEngQMng_CurrentPeriodEQty()));
                    itemUnitTemp.setEngQMng_CurrentPeriodEQtyForExcel(strEengQMng_CurrentPeriodEQtyInPercentInPercent);
                }
                progStlItemSubQShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemSubQShowListExcel", progStlItemSubQShowListExcel);
        }catch (Exception e){
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<ProgStlItemSubQShow> progStlItemSubQShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemSubQShow progStlItemSubQShowTemp = new ProgStlItemSubQShow();
            progStlItemSubQShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progStlItemSubQShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progStlItemSubQShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemSubQShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemSubQShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progStlItemSubQShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progStlItemSubQShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());

            progStlItemSubQShowTemp.setSubctt_Name(itemUnit.getName());
            progStlItemSubQShowTemp.setSubctt_Remark(itemUnit.getRemark());

            progStlItemSubQShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubQShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubQShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubQShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            String strSubctt_ContractAmountInPercent=ToolUtil.getCttAmtInPercent(itemUnit.getContractAmount());
            progStlItemSubQShowTemp.setSubctt_ContractAmountInPercent(strSubctt_ContractAmountInPercent);
            progStlItemSubQShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            progStlItemSubQShowTemp.setSubctt_SpareField(itemUnit.getSpareField());

            ProgStlItemSubQ progStlItemSubQ =new ProgStlItemSubQ();
            progStlItemSubQ.setSubcttPkid(strSubcttPkid);
            progStlItemSubQ.setSubcttItemPkid(itemUnit.getPkid());
            progStlItemSubQ.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemSubQ> progStlItemSubQList =
                    progStlItemSubQService.selectRecordsByExample(progStlItemSubQ);
            if(progStlItemSubQList.size()>0){
                progStlItemSubQ = progStlItemSubQList.get(0);
                progStlItemSubQShowTemp.setEngQMng_Pkid(progStlItemSubQ.getPkid());
                progStlItemSubQShowTemp.setEngQMng_PeriodNo(progStlItemSubQ.getPeriodNo());
                progStlItemSubQShowTemp.setEngQMng_SubcttPkid(progStlItemSubQ.getSubcttPkid());
                progStlItemSubQShowTemp.setEngQMng_SubcttItemPkid(progStlItemSubQ.getSubcttItemPkid());
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQty(progStlItemSubQ.getBeginToCurrentPeriodEQty());
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQty(progStlItemSubQ.getCurrentPeriodEQty());
                String strEngQMng_BeginToCurrentPeriodEQtyInPercent=ToolUtil.getCttAmtInPercent(progStlItemSubQ.getBeginToCurrentPeriodEQty());
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQtyInPercent(strEngQMng_BeginToCurrentPeriodEQtyInPercent);
                String strEengQMng_CurrentPeriodEQtyInPercentInPercent=ToolUtil.getCttAmtInPercent(progStlItemSubQ.getCurrentPeriodEQty());
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQtyInPercent(strEengQMng_CurrentPeriodEQtyInPercentInPercent);
                progStlItemSubQShowTemp.setEngQMng_ArchivedFlag(progStlItemSubQ.getArchivedFlag());
                progStlItemSubQShowTemp.setEngQMng_CreatedBy(progStlItemSubQ.getCreatedBy());
                String strCreatedByNameTemp=ToolUtil.getUserName(progStlItemSubQ.getCreatedBy());
                progStlItemSubQShowTemp.setEngQMng_CreatedByName(strCreatedByNameTemp);
                progStlItemSubQShowTemp.setEngQMng_CreatedTime(progStlItemSubQ.getCreatedTime());
                progStlItemSubQShowTemp.setEngQMng_LastUpdBy(progStlItemSubQ.getLastUpdBy());
                String strLastUpdByNameTemp=ToolUtil.getUserName(progStlItemSubQ.getLastUpdBy());
                progStlItemSubQShowTemp.setEngQMng_LastUpdByName(strLastUpdByNameTemp);
                progStlItemSubQShowTemp.setEngQMng_LastUpdTime(progStlItemSubQ.getLastUpdTime());
                progStlItemSubQShowTemp.setEngQMng_RecVersion(progStlItemSubQ.getRecVersion());
                progStlItemSubQShowTemp.setEngQMng_Remark(progStlItemSubQ.getRemark());
            }
            progStlItemSubQShowListPara.add(progStlItemSubQShowTemp) ;
            recursiveDataTable(progStlItemSubQShowTemp.getSubctt_Pkid(), cttItemListPara, progStlItemSubQShowListPara);
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgStlItemSubQShow> progStlItemSubQShowListTemp =new ArrayList<ProgStlItemSubQShow>();
        progStlItemSubQShowListTemp.addAll(progStlItemSubQShowList);
        progStlItemSubQShowList.clear();
        // 合同数量小计
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // 合同数量大计
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // 合同金额小计
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // 合同金额大计
        BigDecimal bdAmountAllTotal=new BigDecimal(0);
        // 开累数量小计
        BigDecimal bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
        // 开累数量大计
        BigDecimal bdBeginToCurrentPeriodEQtyAllTotal=new BigDecimal(0);
        // 当期数量小计
        BigDecimal bdCurrentPeriodEQtyTotal=new BigDecimal(0);
        // 当期数量大计
        BigDecimal bdCurrentPeriodEQtyAllTotal=new BigDecimal(0);
        ProgStlItemSubQShow itemUnit=new ProgStlItemSubQShow();
        ProgStlItemSubQShow itemUnitNext=new ProgStlItemSubQShow();
        for(int i=0;i< progStlItemSubQShowListTemp.size();i++){
            itemUnit = progStlItemSubQShowListTemp.get(i);
            // 控制画面上的按钮显示
            // 累积数已达到合同数量
            if(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()!=null) {
                if (itemUnit.getEngQMng_BeginToCurrentPeriodEQty()
                        .compareTo(itemUnit.getSubctt_ContractQuantity())>=0) {
                    itemUnit.setIsUptoCttQtyFlag(true);
                }
            }
            // 框架项
            if(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()).compareTo(ToolUtil.bigDecimal0)==0){
                itemUnit.setIsRenderedFlag(false);
            }

            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdBeginToCurrentPeriodEQtyTotal=
                    bdBeginToCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()));
            bdCurrentPeriodEQtyTotal=
                    bdCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngQMng_CurrentPeriodEQty()));
            //费税率金额不计入小计（费税率为子项时），当前和开累不计入大计
            if(itemUnit.getSubctt_SpareField()!=null&&itemUnit.getSubctt_Grade()>1){
                bdAmountTotal=bdAmountTotal.add(
                        ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount())).subtract(
                        ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }else{
                bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }
            if(itemUnit.getSubctt_SpareField()==null){
                bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
                bdBeginToCurrentPeriodEQtyAllTotal=
                        bdBeginToCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()));
                bdCurrentPeriodEQtyAllTotal=
                        bdCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngQMng_CurrentPeriodEQty()));
            }
            progStlItemSubQShowList.add(itemUnit);
            if(i+1< progStlItemSubQShowListTemp.size()){
                itemUnitNext = progStlItemSubQShowListTemp.get(i+1);
                if(itemUnitNext.getSubctt_ParentPkid().equals("root")){
                    ProgStlItemSubQShow itemOfEsItemHieRelapTemp=new ProgStlItemSubQShow();
                    itemOfEsItemHieRelapTemp.setSubctt_Name("合计");
                    itemOfEsItemHieRelapTemp.setSubctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setSubctt_ContractQuantity(
                            ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                    itemOfEsItemHieRelapTemp.setSubctt_ContractAmount(
                            ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                    itemOfEsItemHieRelapTemp.setEngQMng_BeginToCurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEngQMng_CurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                    String strSubctt_ContractAmountInPercent = ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(itemOfEsItemHieRelapTemp.getSubctt_ContractAmount()));
                    itemOfEsItemHieRelapTemp.setSubctt_ContractAmountForExcel(strSubctt_ContractAmountInPercent);
                    String strEngQMng_BeginToCurrentPeriodEQtyInPercent = ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(itemOfEsItemHieRelapTemp.getEngQMng_BeginToCurrentPeriodEQty()));
                    itemOfEsItemHieRelapTemp.setEngQMng_BeginToCurrentPeriodEQtyForExcel(strEngQMng_BeginToCurrentPeriodEQtyInPercent);
                    String strEengQMng_CurrentPeriodEQtyInPercentInPercent = ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal)));
                    itemOfEsItemHieRelapTemp.setEngQMng_CurrentPeriodEQtyForExcel(strEengQMng_CurrentPeriodEQtyInPercentInPercent);
                    itemOfEsItemHieRelapTemp.setIsRenderedFlag(false);
                    progStlItemSubQShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                }
            } else if(i+1== progStlItemSubQShowListTemp.size()){
                itemUnitNext = progStlItemSubQShowListTemp.get(i);
                ProgStlItemSubQShow progStlItemSubQShowTemp = new ProgStlItemSubQShow();
                progStlItemSubQShowTemp.setSubctt_Name("合计");
                progStlItemSubQShowTemp.setSubctt_Pkid("total" + i);
                progStlItemSubQShowTemp.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progStlItemSubQShowTemp.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                String strSubctt_ContractAmountInPercent = ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(progStlItemSubQShowTemp.getSubctt_ContractAmount()));
                String strEngQMng_BeginToCurrentPeriodEQtyInPercent = ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(progStlItemSubQShowTemp.getEngQMng_BeginToCurrentPeriodEQty()));
                String strEengQMng_CurrentPeriodEQtyInPercentInPercent = ToolUtil.getStrIgnoreNull(ToolUtil.getCttAmtInPercent(ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal)));
                progStlItemSubQShowTemp.setIsRenderedFlag(false);
                progStlItemSubQShowList.add(progStlItemSubQShowTemp);
                progStlItemSubQShowTemp.setSubctt_ContractAmountForExcel(strSubctt_ContractAmountInPercent);
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQtyForExcel(strEngQMng_BeginToCurrentPeriodEQtyInPercent);
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQtyForExcel(strEengQMng_CurrentPeriodEQtyInPercentInPercent);
                progStlItemSubQShowListExcel.add(progStlItemSubQShowTemp);
                bdQuantityTotal = new BigDecimal(0);
                bdAmountTotal = new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal = new BigDecimal(0);
                bdCurrentPeriodEQtyTotal = new BigDecimal(0);
                // 总合计
                progStlItemSubQShowTemp = new ProgStlItemSubQShow();
                progStlItemSubQShowTemp.setSubctt_Name("总合计");
                progStlItemSubQShowTemp.setSubctt_Pkid("total_all" + i);
                progStlItemSubQShowTemp.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progStlItemSubQShowTemp.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progStlItemSubQShowTemp.setIsRenderedFlag(false);
                progStlItemSubQShowList.add(progStlItemSubQShowTemp);
                progStlItemSubQShowTemp.setSubctt_ContractAmountForExcel(ToolUtil.getStrFromBdIgnoreZeroNull(progStlItemSubQShowTemp.getSubctt_ContractAmount()));
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQtyForExcel(ToolUtil.getStrFromBdIgnoreZeroNull(progStlItemSubQShowTemp.getEngQMng_BeginToCurrentPeriodEQty()));
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQtyForExcel(ToolUtil.getStrFromBdIgnoreZeroNull(progStlItemSubQShowTemp.getEngQMng_CurrentPeriodEQty()));
                progStlItemSubQShowListExcel.add(progStlItemSubQShowTemp);
            }
        }
    }
    /*根据group和orderid临时编制编码strNo*/
    private List<ProgStlItemSubQShow> getStlSubCttEngQMngConstructList_DoFromatNo(
            List<ProgStlItemSubQShow> progStlItemSubQShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgStlItemSubQShow itemUnit: progStlItemSubQShowListPara){
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
        progStlItemSubQShowSel =new ProgStlItemSubQShow();
        progStlItemSubQShowUpd =new ProgStlItemSubQShow();
        progStlItemSubQShowDel =new ProgStlItemSubQShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEngQMng_CurrentPeriodMQty("submit")){
                    return;
                }
                progStlItemSubQShowUpd.setEngQMng_PeriodNo(progStlInfo.getPeriodNo());
                List<ProgStlItemSubQ> progStlItemSubQListTemp =
                        progStlItemSubQService.isExistInDb(progStlItemSubQShowUpd);
                if (progStlItemSubQListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (progStlItemSubQListTemp.size() == 1) {
                    progStlItemSubQShowUpd.setEngQMng_Pkid (progStlItemSubQListTemp.get(0).getPkid());
                    progStlItemSubQService.updateRecord(progStlItemSubQShowUpd);
                }
                if (progStlItemSubQListTemp.size()==0){
                    progStlItemSubQShowUpd.setEngQMng_SubcttPkid(strSubcttPkid);
                    progStlItemSubQShowUpd.setEngQMng_SubcttItemPkid(progStlItemSubQShowUpd.getSubctt_Pkid());
                    progStlItemSubQService.insertRecord(progStlItemSubQShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemSubQShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEngQMng_CurrentPeriodMQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getSubctt_ContractQuantity()).
                compareTo(ToolUtil.bigDecimal0)==0){
            return true;
        }
        else{
            String strTemp= progStlItemSubQShowUpd.getEngQMng_CurrentPeriodEQty().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEngQMng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getEngQMng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEngQMng_BeginToCurrentPeriodEQtyInDB.add(bDEngQMng_CurrentPeriodEQtyTemp).subtract(bDEngQMng_CurrentPeriodEQtyInDB);

            BigDecimal bDSubctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getSubctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bDSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)<0){
                    bDSubctt_ContractQuantity=ToolUtil.bigDecimal0.divide(bDSubctt_ContractQuantity);
                }
                if(bigDecimalTemp.compareTo(ToolUtil.bigDecimal0)<0){
                    bigDecimalTemp=ToolUtil.bigDecimal0.divide(bigDecimalTemp);
                }
                if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progStlItemSubQShowUpd.setEngQMng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getEngQMng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEngQMng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                        MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                                + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "）！");
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }
    private void delRecordAction(ProgStlItemSubQShow progStlItemSubQShowPara){
        try {
            int deleteRecordNum= progStlItemSubQService.deleteRecord(progStlItemSubQShowPara.getEngQMng_Pkid());
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara,ProgStlItemSubQShow progStlItemSubQShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                progStlItemSubQShowSel =(ProgStlItemSubQShow) BeanUtils.cloneBean(progStlItemSubQShowPara) ;
                progStlItemSubQShowSel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubQShowSel.getSubctt_StrNo()));
            }
            else if(strSubmitTypePara.equals("Upd")){
                progStlItemSubQShowUpd =(ProgStlItemSubQShow) BeanUtils.cloneBean(progStlItemSubQShowPara) ;
                progStlItemSubQShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubQShowUpd.getSubctt_StrNo()));
                bDEngQMng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getEngQMng_CurrentPeriodEQty());
                bDEngQMng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getEngQMng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progStlItemSubQShowDel =(ProgStlItemSubQShow) BeanUtils.cloneBean(progStlItemSubQShowPara) ;
                progStlItemSubQShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubQShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * 根据权限进行审核
     *
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType) {
        try {
            strPowerType=strFlowType+strPowerType;
            if (strPowerType.contains("Mng")) {
                if (strPowerType.equals("MngPass")) {
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：录入完毕
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据录入完成！");
                } else if (strPowerType.equals("MngFail")) {
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据录入未完！");
                }
            } else if (strPowerType.contains("Check") && !strPowerType.contains("DoubleCheck")) {// 审核
                if (strPowerType.equals("CheckPass")) {
                    // 状态标志：审核
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核通过！");
                } else if (strPowerType.equals("CheckFail")) {
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(null);
                    // 原因：审核未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核未过！");
                }
            } else if (strPowerType.contains("DoubleCheck")) {// 复核
                if (strPowerType.equals("DoubleCheckPass")) {
                    try {
                        // 状态标志：复核
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        // 原因：复核通过
                        progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                        progStlInfoService.updAutoLinkTask(progStlInfo);
                        MessageUtil.addInfo("数据复核通过！");
                    }catch (Exception e) {
                        logger.error("复核通过操作失败。", e);
                        MessageUtil.addError("复核通过操作失败。");
                        return;
                    }
                } else if (strPowerType.equals("DoubleCheckFail")) {
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
                        progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                        progStlInfoService.updAutoLinkTask(progStlInfo);
                        MessageUtil.addInfo("数据复核未过！");
                    }catch (Exception e) {
                        logger.error("删除数据失败,复核未过操作失败。", e);
                        MessageUtil.addError("复核未过操作失败。");
                    }
                }
            } else if (strPowerType.contains("Approve")) {// 批准
                if (strPowerType.equals("ApprovePass")) {
                    // 状态标志：批准
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据批准通过！");
                } else if (strPowerType.equals("ApproveFail")) {
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
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
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
    // 附件
    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/stl/SubQ/" + attachmentModelPara.getCOLUMN_NAME());
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
            progStlInfoShow.setAttachment(sbTemp.toString());
            progStlInfoService.updateRecord(progStlInfoShow);
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
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
	public String onExportExcel()throws IOException, WriteException {
        if (this.progStlItemSubQShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "分包数量结算-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progStlItemSubQ.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
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

    public ProgStlInfoShow getProgStlInfoShow() {
        return progStlInfoShow;
    }

    public ProgStlItemSubQShow getProgStlItemSubQShowSel() {
        return progStlItemSubQShowSel;
    }

    public void setProgStlItemSubQShowSel(ProgStlItemSubQShow progStlItemSubQShowSel) {
        this.progStlItemSubQShowSel = progStlItemSubQShowSel;
    }

    public List<ProgStlItemSubQShow> getProgStlItemSubQShowList() {
        return progStlItemSubQShowList;
    }

    public void setProgStlItemSubQShowList(List<ProgStlItemSubQShow> progStlItemSubQShowList) {
        this.progStlItemSubQShowList = progStlItemSubQShowList;
    }

    public ProgStlItemSubQService getProgStlItemSubQService() {
        return progStlItemSubQService;
    }

    public void setProgStlItemSubQService(ProgStlItemSubQService progStlItemSubQService) {
        this.progStlItemSubQService = progStlItemSubQService;
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

    public ProgStlItemSubQShow getProgStlItemSubQShowDel() {
        return progStlItemSubQShowDel;
    }

    public void setProgStlItemSubQShowDel(ProgStlItemSubQShow progStlItemSubQShowDel) {
        this.progStlItemSubQShowDel = progStlItemSubQShowDel;
    }

    public ProgStlItemSubQShow getProgStlItemSubQShowUpd() {
        return progStlItemSubQShowUpd;
    }

    public void setProgStlItemSubQShowUpd(ProgStlItemSubQShow progStlItemSubQShowUpd) {
        this.progStlItemSubQShowUpd = progStlItemSubQShowUpd;
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

    public List<ProgStlItemSubQShow> getProgStlItemSubQShowListExcel() {
        return progStlItemSubQShowListExcel;
    }

    public void setProgStlItemSubQShowListExcel(List<ProgStlItemSubQShow> progStlItemSubQShowListExcel) {
        this.progStlItemSubQShowListExcel = progStlItemSubQShowListExcel;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

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

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }

    /*智能字段End*/
}

