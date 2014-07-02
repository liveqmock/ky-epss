package epss.view.item;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgMatQtyItemShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
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
public class ItemSubcttEngMStlAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemSubcttEngMStlAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    @ManagedProperty(value = "#{esInitStlService}")
    private EsInitStlService esInitStlService;
    @ManagedProperty(value = "#{esItemStlSubcttEngMService}")
    private EsItemStlSubcttEngMService esItemStlSubcttEngMService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgMatQtyItemShow> progMatQtyItemShowList;
    private ProgMatQtyItemShow progMatQtyItemShow;
    private ProgMatQtyItemShow progMatQtyItemShowUpd;
    private ProgMatQtyItemShow progMatQtyItemShowDel;
    private ProgMatQtyItemShow progMatQtyItemShowSelected;
    private BigDecimal bDEngMMng_BeginToCurrentPeriodMQtyInDB;
    private BigDecimal bDEngMMng_CurrentPeriodMQtyInDB;

    private String strSubmitType;
    private String strSubcttPkid;
    private String strLatestApprovedPeriodNo;
    private EsInitStl esInitStl;

    /*����ά������㼶���ֵ���ʾ*/
    private String strMngNotFinishFlag;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strEsInitStlSubcttEng")){
            String strEsInitStlSubcttEngTemp=parammap.get("strEsInitStlSubcttEng").toString();
            esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEngTemp);
            strSubcttPkid= esInitStl.getStlPkid();
        }

        strLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(ESEnum.ITEMTYPE4.getCode(),strSubcttPkid));
        
        List<EsInitPower> esInitPowerList=
                esInitPowerService.selectListByModel(esInitStl.getStlType(),esInitStl.getStlPkid(),esInitStl.getPeriodNo());
        strMngNotFinishFlag="true";
        if(esInitPowerList.size()>0){
            if(esInitPowerList.get(0).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                strMngNotFinishFlag="false";
            }
        }
        resetAction();
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        /*�ְ���ͬ*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        esCttItemList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE2.getCode(), strSubcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progMatQtyItemShowList =new ArrayList<ProgMatQtyItemShow>();
        recursiveDataTable("root", esCttItemList, progMatQtyItemShowList);
        progMatQtyItemShowList =getStlSubCttEngMMngConstructList_DoFromatNo(progMatQtyItemShowList);
        List<EsInitPower> esInitPowerList= esInitPowerService.selectListByModel(ESEnumPower.POWER_TYPE3.getCode(),
                strSubcttPkid, esCommon.getStrDateThisPeriod());
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgMatQtyItemShow> sProgMatQtyItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgMatQtyItemShow progMatQtyItemShowTemp = new ProgMatQtyItemShow();
            progMatQtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progMatQtyItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progMatQtyItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progMatQtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progMatQtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progMatQtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());

            progMatQtyItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());

            progMatQtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progMatQtyItemShowTemp.setSubctt_Note(itemUnit.getNote());

            progMatQtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progMatQtyItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progMatQtyItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progMatQtyItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progMatQtyItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            EsItemStlSubcttEngM esItemStlSubcttEngM=new EsItemStlSubcttEngM();
            esItemStlSubcttEngM.setSubcttPkid(strSubcttPkid);
            esItemStlSubcttEngM.setSubcttItemPkid(itemUnit.getPkid());
            esItemStlSubcttEngM.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlSubcttEngM> esItemStlSubcttEngMList =
                    esItemStlSubcttEngMService.selectRecordsByExample(esItemStlSubcttEngM);
            if(esItemStlSubcttEngMList.size()>0){
                esItemStlSubcttEngM= esItemStlSubcttEngMList.get(0);
                progMatQtyItemShowTemp.setEngMMng_Pkid(esItemStlSubcttEngM.getPkid());
                progMatQtyItemShowTemp.setEngMMng_PeriodNo(esItemStlSubcttEngM.getPeriodNo());
                progMatQtyItemShowTemp.setEngMMng_SubcttPkid(esItemStlSubcttEngM.getSubcttPkid());
                progMatQtyItemShowTemp.setEngMMng_SubcttItemPkid(esItemStlSubcttEngM.getSubcttItemPkid());
                progMatQtyItemShowTemp.setEngMMng_BeginToCurrentPeriodMQty(esItemStlSubcttEngM.getBeginToCurrentPeriodMQty());
                progMatQtyItemShowTemp.setEngMMng_CurrentPeriodMQty(esItemStlSubcttEngM.getCurrentPeriodMQty());
                progMatQtyItemShowTemp.setEngMMng_MPurchaseUnitPrice(esItemStlSubcttEngM.getmPurchaseUnitPrice());
                progMatQtyItemShowTemp.setEngMMng_DeletedFlag(esItemStlSubcttEngM.getDeleteFlag());
                progMatQtyItemShowTemp.setEngMMng_CreatedBy(esItemStlSubcttEngM.getCreatedBy());
                progMatQtyItemShowTemp.setEngMMng_CreatedDate(esItemStlSubcttEngM.getCreatedDate());
                progMatQtyItemShowTemp.setEngMMng_LastUpdBy(esItemStlSubcttEngM.getLastUpdBy());
                progMatQtyItemShowTemp.setEngMMng_LastUpdDate(esItemStlSubcttEngM.getLastUpdDate());
                progMatQtyItemShowTemp.setEngMMng_ModificationNum(esItemStlSubcttEngM.getModificationNum());
            }
            sProgMatQtyItemShowListPara.add(progMatQtyItemShowTemp) ;
            recursiveDataTable(progMatQtyItemShowTemp.getSubctt_Pkid(), esCttItemListPara, sProgMatQtyItemShowListPara);
        }
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgMatQtyItemShow> getStlSubCttEngMMngConstructList_DoFromatNo(
            List<ProgMatQtyItemShow> progMatQtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgMatQtyItemShow itemUnit: progMatQtyItemShowListPara){
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
        return progMatQtyItemShowListPara;
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<EsCttItem> getEsCttItemListByParentPkid(
            String strLevelParentPkid,
            List<EsCttItem> esCttItemListPara) {
        List<EsCttItem> tempEsCttItemList =new ArrayList<EsCttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(EsCttItem itemUnit: esCttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempEsCttItemList.add(itemUnit);
            }
        }
        return tempEsCttItemList;
    }

    /*����*/
    public void resetAction(){
        progMatQtyItemShow =new ProgMatQtyItemShow();
        progMatQtyItemShowUpd =new ProgMatQtyItemShow();
        progMatQtyItemShowDel =new ProgMatQtyItemShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEngMMng_CurrentPeriodMQty("submit")){
                    return;
                }
                ProgMatQtyItemShow progMatQtyItemShowTemp=new ProgMatQtyItemShow();
                progMatQtyItemShowTemp.setEngMMng_SubcttPkid(strSubcttPkid);
                progMatQtyItemShowTemp.setEngMMng_PeriodNo(esInitStl.getPeriodNo());
                progMatQtyItemShowTemp.setEngMMng_SubcttItemPkid(progMatQtyItemShowUpd.getSubctt_Pkid());
                List<EsItemStlSubcttEngM> esItemStlSubcttEngMListTemp =
                        esItemStlSubcttEngMService.isExistInDb(progMatQtyItemShowTemp);
                if (esItemStlSubcttEngMListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (esItemStlSubcttEngMListTemp.size() == 1) {
                    progMatQtyItemShowUpd.setEngMMng_Pkid (esItemStlSubcttEngMListTemp.get(0).getPkid());
                    updRecordAction(progMatQtyItemShowUpd);
                } else if (esItemStlSubcttEngMListTemp.size()==0){
                    progMatQtyItemShowUpd.setEngMMng_SubcttPkid(strSubcttPkid);
                    progMatQtyItemShowUpd.setEngMMng_PeriodNo(esInitStl.getPeriodNo());
                    progMatQtyItemShowUpd.setEngMMng_SubcttItemPkid(progMatQtyItemShowUpd.getSubctt_Pkid());
                    addRecordAction(progMatQtyItemShowUpd);
                }
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progMatQtyItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
            logger.error("��������ʧ�ܣ�", e);
        }
    }

    public boolean blurEngMMng_CurrentPeriodMQty(String strBlurOrSubmitFlag){

        if(ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getSubctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty().toString().matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDEngQMng_CurrentPeriodMQtyTemp= 
			ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty());

            BigDecimal bigDecimalTemp= 
			         bDEngMMng_BeginToCurrentPeriodMQtyInDB.add(bDEngQMng_CurrentPeriodMQtyTemp).subtract(bDEngMMng_CurrentPeriodMQtyInDB);

            BigDecimal bDSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getSubctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                    MessageUtil.addInfo("���ڲ����ۼƹ�Ӧ����+���ڲ��Ϲ�Ӧ����>��ͬ������ȷ������ı��ڲ��Ϲ�Ӧ������"
                            + bDEngQMng_CurrentPeriodMQtyTemp.toString() + "���Ƿ���ȷ��");
                 }
                progMatQtyItemShowUpd.setEngMMng_BeginToCurrentPeriodMQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodMQtyTemp=
                        ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
                if(bDEngQMng_BeginToCurrentPeriodMQtyTemp.compareTo(bDEngMMng_BeginToCurrentPeriodMQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                        MessageUtil.addInfo("���ڲ����ۼƹ�Ӧ����+���ڲ��Ϲ�Ӧ����>��ͬ������ȷ������ı��ڲ��Ϲ�Ӧ������"
                                + bDEngQMng_CurrentPeriodMQtyTemp.toString() + "���Ƿ���ȷ��");
                    }
                }
            }
        }
        return true;
    }

    private void addRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            esItemStlSubcttEngMService.insertRecord(progMatQtyItemShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            esItemStlSubcttEngMService.updateRecord(progMatQtyItemShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void delRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            if(progMatQtyItemShowPara.getEngMMng_Pkid()==null||
                    progMatQtyItemShowDel.getEngMMng_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
                int deleteRecordNum = esItemStlSubcttEngMService.deleteRecord(
                        progMatQtyItemShowDel.getEngMMng_Pkid());
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
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            BigDecimal bdSubctt_SignPartAPrice= ToolUtil.getBdIgnoreNull(
                    progMatQtyItemShowSelected.getSubctt_SignPartAPrice());
            BigDecimal bdSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(
                    progMatQtyItemShowSelected.getSubctt_ContractQuantity());
            if(strSubmitTypePara.equals("Sel")){
                progMatQtyItemShow =(ProgMatQtyItemShow)BeanUtils.cloneBean(progMatQtyItemShowSelected) ;
                progMatQtyItemShow.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShow.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�����");
                    return;
                }
                progMatQtyItemShowUpd =(ProgMatQtyItemShow) BeanUtils.cloneBean(progMatQtyItemShowSelected) ;
                progMatQtyItemShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShowUpd.getSubctt_StrNo()));

                bDEngMMng_CurrentPeriodMQtyInDB=ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_CurrentPeriodMQty());
                bDEngMMng_BeginToCurrentPeriodMQtyInDB=
                        ToolUtil.getBdIgnoreNull(progMatQtyItemShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
            }else
            if(strSubmitTypePara.equals("Del")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�ɾ��");
                    return;
                }
                progMatQtyItemShowDel =(ProgMatQtyItemShow) BeanUtils.cloneBean(progMatQtyItemShowSelected) ;
                progMatQtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /* �����ֶ�Start*/
    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShow() {
        return progMatQtyItemShow;
    }

    public void setProgMatQtyItemShow(ProgMatQtyItemShow progMatQtyItemShow) {
        this.progMatQtyItemShow = progMatQtyItemShow;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShowSelected() {
        return progMatQtyItemShowSelected;
    }

    public void setProgMatQtyItemShowSelected(ProgMatQtyItemShow progMatQtyItemShowSelected) {
        this.progMatQtyItemShowSelected = progMatQtyItemShowSelected;
    }

    public List<ProgMatQtyItemShow> getProgMatQtyItemShowList() {
        return progMatQtyItemShowList;
    }

    public void setProgMatQtyItemShowList(List<ProgMatQtyItemShow> progMatQtyItemShowList) {
        this.progMatQtyItemShowList = progMatQtyItemShowList;
    }

    public EsItemStlSubcttEngMService getEsItemStlSubcttEngMService() {
        return esItemStlSubcttEngMService;
    }

    public void setEsItemStlSubcttEngMService(EsItemStlSubcttEngMService esItemStlSubcttEngMService) {
        this.esItemStlSubcttEngMService = esItemStlSubcttEngMService;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsInitStlService getEsInitStlService() {
        return esInitStlService;
    }

    public void setEsInitStlService(EsInitStlService esInitStlService) {
        this.esInitStlService = esInitStlService;
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

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShowDel() {
        return progMatQtyItemShowDel;
    }

    public void setProgMatQtyItemShowDel(ProgMatQtyItemShow progMatQtyItemShowDel) {
        this.progMatQtyItemShowDel = progMatQtyItemShowDel;
    }

    public ProgMatQtyItemShow getProgMatQtyItemShowUpd() {
        return progMatQtyItemShowUpd;
    }

    public void setProgMatQtyItemShowUpd(ProgMatQtyItemShow progMatQtyItemShowUpd) {
        this.progMatQtyItemShowUpd = progMatQtyItemShowUpd;
    }
    /*�����ֶ�End*/
}
