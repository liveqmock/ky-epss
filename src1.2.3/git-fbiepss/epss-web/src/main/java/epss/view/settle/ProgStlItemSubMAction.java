package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubMShow;
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
 * Time: ����9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlItemSubMAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemSubMAction.class);
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
    @ManagedProperty(value = "#{progStlItemSubMService}")
    private ProgStlItemSubMService progStlItemSubMService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;

    private List<ProgStlItemSubMShow> progStlItemSubMShowList;
    private ProgStlItemSubMShow progStlItemSubMShowSel;
    private ProgStlItemSubMShow progStlItemSubMShowUpd;
    private ProgStlItemSubMShow progStlItemSubMShowDel;
    private BigDecimal bDEngMMng_BeginToCurrentPeriodMQtyInDB;
    private BigDecimal bDEngMMng_CurrentPeriodMQtyInDB;

    private String strSubmitType;
    private ProgStlInfo progStlInfo;

    /*����ά������㼶���ֵ���ʾ*/
    private String strPassVisible;
    private String strPassFailVisible;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgStlItemSubMShow> progStlItemSubMShowListExcel;
    private Map beansMap;
    private ProgStlInfoShow progStlInfoShow;
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
            List<CttItem> cttItemList =new ArrayList<CttItem>();
            cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE2.getCode(), progStlInfo.getStlPkid());
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemSubMShowList =new ArrayList<ProgStlItemSubMShow>();
            progStlItemSubMShowListExcel =new ArrayList<>();
            attachmentList=new ArrayList<>();
            attachmentList=ToolUtil.getListAttachmentByStrAttachment(progStlInfoShow.getAttachment());
            recursiveDataTable("root", cttItemList, progStlItemSubMShowList);
            progStlItemSubMShowList =getStlSubCttEngMMngConstructList_DoFromatNo(progStlItemSubMShowList);
            setItemOfEsItemHieRelapList_AddTotal();
            // Excel�����γ�
            progStlItemSubMShowListExcel =new ArrayList<ProgStlItemSubMShow>();
            for(ProgStlItemSubMShow itemUnit: progStlItemSubMShowList){
                // �ְ���ͬ
                itemUnit.setSubctt_ContractUnitPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractUnitPrice()));
                itemUnit.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractQuantity()));
                itemUnit.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_ContractAmount()));
                itemUnit.setSubctt_SignPartAPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getSubctt_SignPartAPrice()));
                // �ְ����̲�������������
                itemUnit.setEngMMng_CurrentPeriodMQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngMMng_CurrentPeriodMQty()));
                itemUnit.setEngMMng_BeginToCurrentPeriodMQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEngMMng_BeginToCurrentPeriodMQty()));

                ProgStlItemSubMShow itemUnitTemp= (ProgStlItemSubMShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                progStlItemSubMShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemSubMShowListExcel", progStlItemSubMShowListExcel);
            beansMap.put("progStlItemSubMShowList", progStlItemSubMShowList);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubMShow> sProgStlItemSubMShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<>();
        // ͨ������id�������ĺ���
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemSubMShow progStlItemSubMShowTemp = new ProgStlItemSubMShow();
            progStlItemSubMShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progStlItemSubMShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progStlItemSubMShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemSubMShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemSubMShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progStlItemSubMShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progStlItemSubMShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemSubMShowTemp.setSubctt_Name(itemUnit.getName());
            progStlItemSubMShowTemp.setSubctt_Remark(itemUnit.getRemark());
            progStlItemSubMShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubMShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubMShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubMShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progStlItemSubMShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            progStlItemSubMShowTemp.setSubctt_SpareField(itemUnit.getSpareField());

            ProgStlItemSubM progStlItemSubM =new ProgStlItemSubM();
            progStlItemSubM.setSubcttPkid(progStlInfo.getStlPkid());
            progStlItemSubM.setSubcttItemPkid(itemUnit.getPkid());
            progStlItemSubM.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemSubM> progStlItemSubMList =
                    progStlItemSubMService.selectRecordsByExample(progStlItemSubM);
            if(progStlItemSubMList.size()>0){
                progStlItemSubM = progStlItemSubMList.get(0);
                progStlItemSubMShowTemp.setEngMMng_Pkid(progStlItemSubM.getPkid());
                progStlItemSubMShowTemp.setEngMMng_PeriodNo(progStlItemSubM.getPeriodNo());
                progStlItemSubMShowTemp.setEngMMng_SubcttPkid(progStlItemSubM.getSubcttPkid());
                progStlItemSubMShowTemp.setEngMMng_SubcttItemPkid(progStlItemSubM.getSubcttItemPkid());
                progStlItemSubMShowTemp.setEngMMng_BeginToCurrentPeriodMQty(progStlItemSubM.getBeginToCurrentPeriodMQty());
                progStlItemSubMShowTemp.setEngMMng_CurrentPeriodMQty(progStlItemSubM.getCurrentPeriodMQty());
                progStlItemSubMShowTemp.setEngMMng_MPurchaseUnitPrice(progStlItemSubM.getmPurchaseUnitPrice());
                progStlItemSubMShowTemp.setEngMMng_ArchivedFlag(progStlItemSubM.getArchivedFlag());
                progStlItemSubMShowTemp.setEngMMng_CreatedBy(progStlItemSubM.getCreatedBy());
                String strCreatedByNameTemp=ToolUtil.getUserName(progStlItemSubM.getCreatedBy());
                progStlItemSubMShowTemp.setEngMMng_CreatedByName(strCreatedByNameTemp);
                progStlItemSubMShowTemp.setEngMMng_CreatedTime(progStlItemSubM.getCreatedTime());
                progStlItemSubMShowTemp.setEngMMng_LastUpdBy(progStlItemSubM.getLastUpdBy());
                String strLastUpdByNameTemp=ToolUtil.getUserName(progStlItemSubM.getLastUpdBy());
                progStlItemSubMShowTemp.setEngMMng_LastUpdByName(strLastUpdByNameTemp);
                progStlItemSubMShowTemp.setEngMMng_LastUpdTime(progStlItemSubM.getLastUpdTime());
                progStlItemSubMShowTemp.setEngMMng_RecVersion(progStlItemSubM.getRecVersion());
                progStlItemSubMShowTemp.setEngMMng_Remark(progStlItemSubM.getRemark());
                if(progStlItemSubMShowTemp.getEngMMng_BeginToCurrentPeriodMQty()!=null) {
                    if (progStlItemSubMShowTemp.getEngMMng_BeginToCurrentPeriodMQty()
                            .equals(progStlItemSubMShowTemp.getSubctt_ContractQuantity())) {
                        progStlItemSubMShowTemp.setIsUptoCttQtyFlag(true);
                    }
                }
            }
            sProgStlItemSubMShowListPara.add(progStlItemSubMShowTemp) ;
            recursiveDataTable(progStlItemSubMShowTemp.getSubctt_Pkid(), cttItemListPara, sProgStlItemSubMShowListPara);
        }
    }
    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgStlItemSubMShow> progStlItemSubMShowListTemp =new ArrayList<ProgStlItemSubMShow>();
        progStlItemSubMShowListTemp.addAll(progStlItemSubMShowList);
        progStlItemSubMShowList.clear();
        // ��ͬ����С��
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // ��ͬ�������
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // ��ͬ���С��
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // ��ͬ�����
        BigDecimal bdAmountAllTotal=new BigDecimal(0);
        // ���۲�����С��
        BigDecimal bdBeginToCurrentPeriodMQtyTotal=new BigDecimal(0);
        // ���۲��������
        BigDecimal bdBeginToCurrentPeriodMQtyAllTotal=new BigDecimal(0);
        // ���ڲ�����С��
        BigDecimal bdCurrentPeriodMQtyTotal=new BigDecimal(0);
        // ���ڲ��������
        BigDecimal bdCurrentPeriodMQtyAllTotal=new BigDecimal(0);
        ProgStlItemSubMShow itemUnit=new ProgStlItemSubMShow();
        ProgStlItemSubMShow itemUnitNext=new ProgStlItemSubMShow();
        for(int i=0;i< progStlItemSubMShowListTemp.size();i++){
            itemUnit = progStlItemSubMShowListTemp.get(i);
            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractQuantity()));
            bdBeginToCurrentPeriodMQtyTotal=
                    bdBeginToCurrentPeriodMQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngMMng_BeginToCurrentPeriodMQty()));
            bdCurrentPeriodMQtyTotal=
                    bdCurrentPeriodMQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngMMng_CurrentPeriodMQty()));
            //��˰�ʽ�����С�ƣ���˰��Ϊ����ʱ������ǰ�Ϳ��۲�������
            if(itemUnit.getSubctt_SpareField()!=null&&itemUnit.getSubctt_Grade()>1){
                bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount())).subtract(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }else{
                bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }
            if(itemUnit.getSubctt_SpareField()==null){
                bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount()));
            }
            bdBeginToCurrentPeriodMQtyAllTotal=
                    bdBeginToCurrentPeriodMQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngMMng_BeginToCurrentPeriodMQty()));
            bdCurrentPeriodMQtyAllTotal=
                    bdCurrentPeriodMQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEngMMng_CurrentPeriodMQty()));
            progStlItemSubMShowList.add(itemUnit);
            if(i+1< progStlItemSubMShowListTemp.size()){
                itemUnitNext = progStlItemSubMShowListTemp.get(i+1);
                if(itemUnitNext.getSubctt_ParentPkid().equals("root")){
                    ProgStlItemSubMShow itemOfEsItemHieRelapTemp=new ProgStlItemSubMShow();
                    itemOfEsItemHieRelapTemp.setSubctt_Name("�ϼ�");
                    itemOfEsItemHieRelapTemp.setSubctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setSubctt_ContractQuantity(
                            ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                    itemOfEsItemHieRelapTemp.setSubctt_ContractAmount(
                            ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                    itemOfEsItemHieRelapTemp.setEngMMng_BeginToCurrentPeriodMQty(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodMQtyTotal));
                    itemOfEsItemHieRelapTemp.setEngMMng_CurrentPeriodMQty(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodMQtyTotal));
                    progStlItemSubMShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodMQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodMQtyTotal=new BigDecimal(0);
                }
            } else if(i+1== progStlItemSubMShowListTemp.size()){
                itemUnitNext = progStlItemSubMShowListTemp.get(i);
                ProgStlItemSubMShow progStlItemSubMShowTemp = new ProgStlItemSubMShow();
                progStlItemSubMShowTemp.setSubctt_Name("�ϼ�");
                progStlItemSubMShowTemp.setSubctt_Pkid("total" + i);
                progStlItemSubMShowTemp.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progStlItemSubMShowTemp.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progStlItemSubMShowTemp.setEngMMng_BeginToCurrentPeriodMQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodMQtyTotal));
                progStlItemSubMShowTemp.setEngMMng_CurrentPeriodMQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodMQtyTotal));
                progStlItemSubMShowList.add(progStlItemSubMShowTemp);
                progStlItemSubMShowListExcel.add(progStlItemSubMShowTemp);
                bdQuantityTotal = new BigDecimal(0);
                bdAmountTotal = new BigDecimal(0);
                bdBeginToCurrentPeriodMQtyTotal = new BigDecimal(0);
                bdCurrentPeriodMQtyTotal = new BigDecimal(0);
                // �ܺϼ�
                progStlItemSubMShowTemp = new ProgStlItemSubMShow();
                progStlItemSubMShowTemp.setSubctt_Name("�ܺϼ�");
                progStlItemSubMShowTemp.setSubctt_Pkid("total_all" + i);
                progStlItemSubMShowTemp.setSubctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progStlItemSubMShowTemp.setSubctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));
                progStlItemSubMShowTemp.setEngMMng_BeginToCurrentPeriodMQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodMQtyAllTotal));
                progStlItemSubMShowTemp.setEngMMng_CurrentPeriodMQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodMQtyAllTotal));
                progStlItemSubMShowList.add(progStlItemSubMShowTemp);
                progStlItemSubMShowListExcel.add(progStlItemSubMShowTemp);
            }
        }
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgStlItemSubMShow> getStlSubCttEngMMngConstructList_DoFromatNo(
            List<ProgStlItemSubMShow> progStlItemSubMShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgStlItemSubMShow itemUnit: progStlItemSubMShowListPara){
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
        return progStlItemSubMShowListPara;
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
        progStlItemSubMShowSel =new ProgStlItemSubMShow();
        progStlItemSubMShowUpd =new ProgStlItemSubMShow();
        progStlItemSubMShowDel =new ProgStlItemSubMShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                ProgStlItemSubMShow progStlItemSubMShowTemp =new ProgStlItemSubMShow();
                progStlItemSubMShowTemp.setEngMMng_SubcttPkid(progStlInfo.getStlPkid());
                progStlItemSubMShowTemp.setEngMMng_PeriodNo(progStlInfo.getPeriodNo());
                progStlItemSubMShowTemp.setEngMMng_SubcttItemPkid(progStlItemSubMShowUpd.getSubctt_Pkid());
                List<ProgStlItemSubM> progStlItemSubMListTemp =
                        progStlItemSubMService.isExistInDb(progStlItemSubMShowTemp);
                if (progStlItemSubMListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                BigDecimal bigDecimalTemp =bDEngMMng_BeginToCurrentPeriodMQtyInDB
                        .add(ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty()))
                        .subtract(bDEngMMng_CurrentPeriodMQtyInDB);
                if (progStlItemSubMListTemp.size() == 0) {
                    progStlItemSubMShowUpd.setEngMMng_SubcttPkid(progStlInfo.getStlPkid());
                    progStlItemSubMShowUpd.setEngMMng_PeriodNo(progStlInfo.getPeriodNo());
                    progStlItemSubMShowUpd.setEngMMng_SubcttItemPkid(progStlItemSubMShowUpd.getSubctt_Pkid());
                    progStlItemSubMShowUpd.setEngMMng_BeginToCurrentPeriodMQty(bigDecimalTemp);
                    progStlItemSubMService.insertRecord(progStlItemSubMShowUpd);
                }else
                if (progStlItemSubMListTemp.size() == 1) {
                    progStlItemSubMShowUpd.setEngMMng_Pkid (progStlItemSubMListTemp.get(0).getPkid());
                    progStlItemSubMShowUpd.setEngMMng_BeginToCurrentPeriodMQty(bigDecimalTemp);
                    updRecordAction(progStlItemSubMShowUpd);
                }
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
            logger.error("��������ʧ�ܣ�", e);
        }
    }

    public boolean blurEngMMng_CurrentPeriodMQty() {
        String strTemp = progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty().toString();
        String strRegex = "[-]?[0-9]+\\.?[0-9]*";
        if (!progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty().toString().matches(strRegex)) {
            MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
            return false;
        }
        BigDecimal bDEngQMng_CurrentPeriodMQtyTemp =
                ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty());
        BigDecimal bigDecimalTemp =
                bDEngMMng_BeginToCurrentPeriodMQtyInDB.add(bDEngQMng_CurrentPeriodMQtyTemp).subtract(bDEngMMng_CurrentPeriodMQtyInDB);
        progStlItemSubMShowUpd.setEngMMng_BeginToCurrentPeriodMQty(bigDecimalTemp);
        return true;
    }

    private void addRecordAction(ProgStlItemSubMShow progStlItemSubMShowPara){
        try {
            progStlItemSubMService.insertRecord(progStlItemSubMShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlItemSubMShow progStlItemSubMShowPara){
        try {
            progStlItemSubMService.updateRecord(progStlItemSubMShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void delRecordAction(ProgStlItemSubMShow progStlItemSubMShowPara){
        try {
            if(progStlItemSubMShowPara.getEngMMng_Pkid()==null||
                    progStlItemSubMShowDel.getEngMMng_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
                int deleteRecordNum = progStlItemSubMService.deleteRecord(
                        progStlItemSubMShowDel.getEngMMng_Pkid());
                if (deleteRecordNum <= 0) {
                    MessageUtil.addInfo("�ü�¼��ɾ����");
                } else {
                    MessageUtil.addInfo("ɾ��������ɡ�");
                }
            }
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara,ProgStlItemSubMShow progStlItemSubMShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                progStlItemSubMShowSel =(ProgStlItemSubMShow)BeanUtils.cloneBean(progStlItemSubMShowPara) ;
                progStlItemSubMShowSel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubMShowSel.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                progStlItemSubMShowUpd =(ProgStlItemSubMShow) BeanUtils.cloneBean(progStlItemSubMShowPara) ;
                progStlItemSubMShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubMShowUpd.getSubctt_StrNo()));

                bDEngMMng_CurrentPeriodMQtyInDB=ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty());
                bDEngMMng_BeginToCurrentPeriodMQtyInDB=
                        ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * ����Ȩ�޽������
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            strPowerType=strFlowType+strPowerType;
            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ��¼�����
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("�������δ����");
                }
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
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
                        return;
                    }
                }
            } else if(strPowerType.contains("Approve")){// ��׼
                if(strPowerType.equals("ApprovePass")){
                    // ״̬��־����׼
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("������׼ͨ����");
                }else if(strPowerType.equals("ApproveFail")){
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
        image.setValue("/upload/stl/SubM/" + attachmentModelPara.getCOLUMN_NAME());
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
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/stl/SubM")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(
                        stream,
                        new MimetypesFileTypeMap().getContentType(file),
                        new String(strAttachment.getBytes("gbk"),"iso8859-1"));
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
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/stl/SubM");
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
        if (this.progStlItemSubMShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ����Ͻ���-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progStlItemSubM.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
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

    public ProgStlItemSubMShow getProgStlItemSubMShowSel() {
        return progStlItemSubMShowSel;
    }

    public void setProgStlItemSubMShowSel(ProgStlItemSubMShow progStlItemSubMShowSel) {
        this.progStlItemSubMShowSel = progStlItemSubMShowSel;
    }

    public List<ProgStlItemSubMShow> getProgStlItemSubMShowList() {
        return progStlItemSubMShowList;
    }

    public void setProgStlItemSubMShowList(List<ProgStlItemSubMShow> progStlItemSubMShowList) {
        this.progStlItemSubMShowList = progStlItemSubMShowList;
    }

    public ProgStlItemSubMService getProgStlItemSubMService() {
        return progStlItemSubMService;
    }

    public void setProgStlItemSubMService(ProgStlItemSubMService progStlItemSubMService) {
        this.progStlItemSubMService = progStlItemSubMService;
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

    public ProgStlItemSubMShow getProgStlItemSubMShowDel() {
        return progStlItemSubMShowDel;
    }

    public void setProgStlItemSubMShowDel(ProgStlItemSubMShow progStlItemSubMShowDel) {
        this.progStlItemSubMShowDel = progStlItemSubMShowDel;
    }

    public ProgStlItemSubMShow getProgStlItemSubMShowUpd() {
        return progStlItemSubMShowUpd;
    }

    public void setProgStlItemSubMShowUpd(ProgStlItemSubMShow progStlItemSubMShowUpd) {
        this.progStlItemSubMShowUpd = progStlItemSubMShowUpd;
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

    public List<ProgStlItemSubMShow> getProgStlItemSubMShowListExcel() {
        return progStlItemSubMShowListExcel;
    }

    public void setProgStlItemSubMShowListExcel(List<ProgStlItemSubMShow> progStlItemSubMShowListExcel) {
        this.progStlItemSubMShowListExcel = progStlItemSubMShowListExcel;
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
    /*�����ֶ�End*/

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
}
