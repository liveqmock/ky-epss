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
 * Time: ����9:30
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

    /*����ά������㼶���ֵ���ʾ*/
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
            progStlItemSubFShowList =new ArrayList<>();
            progStlItemSubFShowListExcel =new ArrayList<>();
            attachmentList=new ArrayList<>();
            attachmentList=ToolUtil.getListAttachmentByStrAttachment(progStlInfoShow.getAttachment());
            recursiveDataTable("root", cttItemList, progStlItemSubFShowList);
            progStlItemSubFShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progStlItemSubFShowList);
            setItemOfEsItemHieRelapList_AddTotal();
            // Excel�����γ�
            progStlItemSubFShowListExcel =new ArrayList<>();
            for(ProgStlItemSubFShow itemUnit: progStlItemSubFShowList){
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
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
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
        // ��ͬ����С��
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // ��ͬ�������
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // ��ͬ���С��
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // ��ͬ�����
        BigDecimal bdAmountAllTotal=new BigDecimal(0);
        // ���۲�����С��
        BigDecimal bdBeginToCurrentPeriodFQtyTotal=new BigDecimal(0);
        // ���۲��������
        BigDecimal bdBeginToCurrentPeriodFQtyAllTotal=new BigDecimal(0);
        // ���ڲ�����С��
        BigDecimal bdCurrentPeriodFQtyTotal=new BigDecimal(0);
        // ���ڲ��������
        BigDecimal bdCurrentPeriodMQtyAllTotal=new BigDecimal(0);
        ProgStlItemSubFShow itemUnit=new ProgStlItemSubFShow();
        ProgStlItemSubFShow itemUnitNext=new ProgStlItemSubFShow();
        for(int i=0;i< progStlItemSubMShowListTemp.size();i++){
            itemUnit = progStlItemSubMShowListTemp.get(i);
            if ( itemUnit.getSubctt_SpareField()!=null && itemUnit.getSubctt_SpareField().equals("F1")){
            // �˴������������������Ƚ�   ���������ͬ���� �˴�����������һ��
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
            //��˰�ʽ�����С�ƣ���˰��Ϊ����ʱ������ǰ�Ϳ��۲�������
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
                    itemOfEsItemHieRelapTemp.setSubctt_Name("�ϼ�");
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
                progStlItemSubFShowTemp.setSubctt_Name("�ϼ�");
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
                // �ܺϼ�
                progStlItemSubFShowTemp = new ProgStlItemSubFShow();
                progStlItemSubFShowTemp.setSubctt_Name("�ܺϼ�");
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
    
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubFShow> ProgStlItemSubFShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // ͨ������id�������ĺ���
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
    /*����group��orderid��ʱ���Ʊ���strNo*/
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
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
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
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
            logger.error("��������ʧ�ܣ�", e);
        }
    }

    public boolean blurengFMng_CurrentPeriodFAmt(String strBlurOrSubmitFlag){
            String strTemp= progStlItemSubFShowUpd.getEngSMng_ThisStageAmt().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }
            BigDecimal bDEngSMng_ThisStageAmtTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getEngSMng_ThisStageAmt());
            //����������,
            BigDecimal bigDecimalTemp=
                    bDEngSMng_AddUpToAmt.add(bDEngSMng_ThisStageAmtTemp).subtract(bDEngSMng_ThisStageAmt);

            BigDecimal bDSubctt_ContracttAmount=
                    ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getSubctt_ContractAmount());

//            if(strBlurOrSubmitFlag.equals("blur")) {
            if(bigDecimalTemp.compareTo(bDSubctt_ContracttAmount)>0){
                MessageUtil.addError("���ڿ��۰�ȫ��ʩ��+���ڰ�ȫ��ʩ��>��ͬ��������ȷ��������� ��"
                        + bDEngSMng_ThisStageAmtTemp.toString() + "����");
                return false;
            }
            progStlItemSubFShowUpd.setEngSMng_AddUpToAmt(bigDecimalTemp);

        return true;
    }

    private void addRecordAction(ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            progStlItemSubFService.insertRecord(progStlItemSubFShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            progStlItemSubFService.updateRecord(progStlItemSubFShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void delRecordAction(ProgStlItemSubFShow progStlItemSubFShowPara){
        try {
            if(progStlItemSubFShowPara.getEngSMng_Pkid()==null||
                    progStlItemSubFShowDel.getEngSMng_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
            }
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*�ҵ����¼�*/
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
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    strPassFlag="false";
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    strPassFlag="true";
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
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
    public String onExportExcel()throws IOException, WriteException {
        if (this.progStlItemSubFShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ���ȫ��ʩ����-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progStlItemSubF.xls");
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
    // ����
    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/stl/SubQ/" + attachmentModelPara.getCOLUMN_NAME());
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

    /*�����ֶ�End*/
}
