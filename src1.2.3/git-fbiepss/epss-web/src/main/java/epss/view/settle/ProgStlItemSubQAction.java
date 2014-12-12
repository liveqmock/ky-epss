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
 * Time: ����9:30
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

    /*����ά������㼶���ֵ���ʾ*/
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
            logger.error("��ʼ��ʧ��", e);
        }
    }

    /*��ʼ������*/
    private void initData() {
        try {
            // ��ϸҳͷ
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

            /*�ְ���ͬ*/
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
            // Excel�����γ�
            progStlItemSubQShowListExcel =new ArrayList<>();
            for(ProgStlItemSubQShow itemUnit: progStlItemSubQShowList){
                // �ְ���ͬ
                itemUnit.setSubctt_ContractUnitPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractUnitPrice()));
                itemUnit.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractQuantity()));
                itemUnit.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractAmount()));
                itemUnit.setSubctt_SignPartAPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_SignPartAPrice()));
                // �ְ�����������
                itemUnit.setEngQMng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngQMng_CurrentPeriodEQty()));
                itemUnit.setEngQMng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()));

                ProgStlItemSubQShow itemUnitTemp= (ProgStlItemSubQShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                if(itemUnitTemp.getSubctt_SpareField()==null){
                    itemUnitTemp.setSubctt_ContractAmountForExcel(
                            ToolUtil.getStrFromBdIgnoreZeroNull(itemUnitTemp.getSubctt_ContractAmount()));
                    // �ְ�����������
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
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<ProgStlItemSubQShow> progStlItemSubQShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // ͨ������id�������ĺ���
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
        // ��ͬ����С��
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // ��ͬ�������
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // ��ͬ���С��
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // ��ͬ�����
        BigDecimal bdAmountAllTotal=new BigDecimal(0);
        // ��������С��
        BigDecimal bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
        // �����������
        BigDecimal bdBeginToCurrentPeriodEQtyAllTotal=new BigDecimal(0);
        // ��������С��
        BigDecimal bdCurrentPeriodEQtyTotal=new BigDecimal(0);
        // �����������
        BigDecimal bdCurrentPeriodEQtyAllTotal=new BigDecimal(0);
        ProgStlItemSubQShow itemUnit=new ProgStlItemSubQShow();
        ProgStlItemSubQShow itemUnitNext=new ProgStlItemSubQShow();
        for(int i=0;i< progStlItemSubQShowListTemp.size();i++){
            itemUnit = progStlItemSubQShowListTemp.get(i);
            // ���ƻ����ϵİ�ť��ʾ
            // �ۻ����Ѵﵽ��ͬ����
            if(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()!=null) {
                if (itemUnit.getEngQMng_BeginToCurrentPeriodEQty()
                        .compareTo(itemUnit.getSubctt_ContractQuantity())>=0) {
                    itemUnit.setIsUptoCttQtyFlag(true);
                }
            }
            // �����
            if(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()).compareTo(ToolUtil.bigDecimal0)==0){
                itemUnit.setIsRenderedFlag(false);
            }

            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdBeginToCurrentPeriodEQtyTotal=
                    bdBeginToCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngQMng_BeginToCurrentPeriodEQty()));
            bdCurrentPeriodEQtyTotal=
                    bdCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngQMng_CurrentPeriodEQty()));
            //��˰�ʽ�����С�ƣ���˰��Ϊ����ʱ������ǰ�Ϳ��۲�������
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
                    itemOfEsItemHieRelapTemp.setSubctt_Name("�ϼ�");
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
                progStlItemSubQShowTemp.setSubctt_Name("�ϼ�");
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
                // �ܺϼ�
                progStlItemSubQShowTemp = new ProgStlItemSubQShow();
                progStlItemSubQShowTemp.setSubctt_Name("�ܺϼ�");
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
    /*����group��orderid��ʱ���Ʊ���strNo*/
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
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CttItem> getEsCttItemListByParentPkid(
            String strLevelParentPkid,
            List<CttItem> cttItemListPara) {
        List<CttItem> tempCttItemList =new ArrayList<CttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(CttItem itemUnit: cttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempCttItemList.add(itemUnit);
            }
        }
        return tempCttItemList;
    }

    /*����*/
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
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
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
                MessageUtil.addInfo("����������ɡ�");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemSubQShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
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
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
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
                    MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                            + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "����");
                    return false;
                }
                progStlItemSubQShowUpd.setEngQMng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getEngQMng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEngQMng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                        MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                                + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "����");
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
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*�ҵ����¼�*/
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
     * ����Ȩ�޽������
     *
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType) {
        try {
            strPowerType=strFlowType+strPowerType;
            if (strPowerType.contains("Mng")) {
                if (strPowerType.equals("MngPass")) {
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ��¼�����
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerType.equals("MngFail")) {
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            } else if (strPowerType.contains("Check") && !strPowerType.contains("DoubleCheck")) {// ���
                if (strPowerType.equals("CheckPass")) {
                    // ״̬��־�����
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerType.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("�������δ����");
                }
            } else if (strPowerType.contains("DoubleCheck")) {// ����
                if (strPowerType.equals("DoubleCheckPass")) {
                    try {
                        // ״̬��־������
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        // ԭ�򣺸���ͨ��
                        progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                        progStlInfoService.updAutoLinkTask(progStlInfo);
                        MessageUtil.addInfo("���ݸ���ͨ����");
                    }catch (Exception e) {
                        logger.error("����ͨ������ʧ�ܡ�", e);
                        MessageUtil.addError("����ͨ������ʧ�ܡ�");
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
                                MessageUtil.addInfo("�������ѱ��ְ��۸������׼������Ȩ���в�����");
                                return;
                            }
                        }

                        // ����д����ʵ��Խ���˻�
                        if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                            progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                        }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                            progStlInfo.setFlowStatus(null);
                        }

                        // ԭ�򣺸���δ��
                        progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                        progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                        progStlInfoService.updAutoLinkTask(progStlInfo);
                        MessageUtil.addInfo("���ݸ���δ����");
                    }catch (Exception e) {
                        logger.error("ɾ������ʧ��,����δ������ʧ�ܡ�", e);
                        MessageUtil.addError("����δ������ʧ�ܡ�");
                    }
                }
            } else if (strPowerType.contains("Approve")) {// ��׼
                if (strPowerType.equals("ApprovePass")) {
                    // ״̬��־����׼
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("������׼ͨ����");
                } else if (strPowerType.equals("ApproveFail")) {
                    // ����д����ʵ��Խ���˻�
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        progStlInfo.setFlowStatus(null);
                    }
                    // ԭ����׼δ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("������׼δ����");
                }
            }
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    // ����
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
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void download(AttachmentModel attachmentModelPara){
        String strAttachment=attachmentModelPara.getCOLUMN_NAME();
        try{
            if(StringUtils.isEmpty(strAttachment) ){
                MessageUtil.addError("·��Ϊ�գ��޷����أ�");
                logger.error("·��Ϊ�գ��޷����أ�");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/stl/SubQ")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("�����ļ�ʧ��", e);
            MessageUtil.addError("�����ļ�ʧ��,"+e.getMessage()+strAttachment);
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
                    MessageUtil.addError("�����Ѵ��ڣ�");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("����·��("+sb.toString()+")�����ѳ����������ֵ4000��������⣬����ϵϵͳ����Ա��");
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
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ���������-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progStlItemSubQ.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    /* �����ֶ�Start*/
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

    /*�����ֶ�End*/
}

