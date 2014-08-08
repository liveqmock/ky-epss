package epss.view.settle;

import epss.common.enums.*;
import epss.common.utils.JxlsManager;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.model.model_show.ProgMatQtyItemShow;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
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
public class ProgMatqtyItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMatqtyItemAction.class);
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
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;

    private List<ProgMatQtyItemShow> progMatQtyItemShowList;
    private ProgMatQtyItemShow progMatQtyItemShowSel;
    private ProgMatQtyItemShow progMatQtyItemShowUpd;
    private ProgMatQtyItemShow progMatQtyItemShowDel;
    private BigDecimal bDEngMMng_BeginToCurrentPeriodMQtyInDB;
    private BigDecimal bDEngMMng_CurrentPeriodMQtyInDB;

    private String strSubmitType;
    private String strSubcttPkid;
    private EsInitStl esInitStl;

    /*����ά������㼶���ֵ���ʾ*/
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgMatQtyItemShow> progMatQtyItemShowListExcel;
    private Map beansMap;
    private ProgInfoShow progMatqtyInfoShowH;
    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        beansMap = new HashMap();
        if(parammap.containsKey("strFlowType")){
            strFlowType=parammap.get("strFlowType").toString();
        }
        if(parammap.containsKey("strProgMatqtyInfoPkid")){
            String strProgMatqtyInfoPkidTemp=parammap.get("strProgMatqtyInfoPkid").toString();
            esInitStl = progStlInfoService.selectRecordsByPrimaryKey(strProgMatqtyInfoPkidTemp);
            beansMap.put("esInitStl", esInitStl);
            strSubcttPkid= esInitStl.getStlPkid();
        }

        List<EsInitPower> esInitPowerList=
                flowCtrlService.selectListByModel(esInitStl.getStlType(),esInitStl.getStlPkid(),esInitStl.getPeriodNo());
        strPassFlag="true";
        if(esInitPowerList.size()>0){
            if("Mng".equals(strFlowType)&&ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(esInitPowerList.get(0).getStatusFlag())) {
                strPassFlag="false";
            }
        }
        resetAction();
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        try {
            // ��ϸҳͷ
            EsCttInfo esCttInfo_Subctt= cttInfoService.getCttInfoByPkId(esInitStl.getStlPkid());
            progMatqtyInfoShowH.setStlName(esCttInfo_Subctt.getName());
            progMatqtyInfoShowH.setSignPartBName(signPartService.getEsInitCustByPkid(esCttInfo_Subctt.getSignPartB()).getName());
            beansMap.put("progMatqtyInfoShowH", progMatqtyInfoShowH);
            /*�ְ���ͬ*/
            List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
            esCttItemList = cttItemService.getEsItemList(
                    ESEnum.ITEMTYPE2.getCode(), strSubcttPkid);
            if(esCttItemList.size()<=0){
                return;
            }
            progMatQtyItemShowList =new ArrayList<ProgMatQtyItemShow>();
            recursiveDataTable("root", esCttItemList, progMatQtyItemShowList);
            progMatQtyItemShowList =getStlSubCttEngMMngConstructList_DoFromatNo(progMatQtyItemShowList);
            List<EsInitPower> esInitPowerList= flowCtrlService.selectListByModel(ESEnumPower.POWER_TYPE3.getCode(),
                    strSubcttPkid, esCommon.getStrDateThisPeriod());
            progMatQtyItemShowListExcel =new ArrayList<ProgMatQtyItemShow>();
            for(ProgMatQtyItemShow itemUnit: progMatQtyItemShowList){
                ProgMatQtyItemShow itemUnitTemp= (ProgMatQtyItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                progMatQtyItemShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progMatQtyItemShowListExcel", progMatQtyItemShowListExcel);
            beansMap.put("progMatQtyItemShowList", progMatQtyItemShowList);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
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
                    progMatqtyItemService.selectRecordsByExample(esItemStlSubcttEngM);
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
        progMatqtyInfoShowH=new ProgInfoShow();
        progMatQtyItemShowSel =new ProgMatQtyItemShow();
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
                        progMatqtyItemService.isExistInDb(progMatQtyItemShowTemp);
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
            progMatqtyItemService.insertRecord(progMatQtyItemShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            progMatqtyItemService.updateRecord(progMatQtyItemShowPara);
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
                int deleteRecordNum = progMatqtyItemService.deleteRecord(
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
    public void selectRecordAction(String strSubmitTypePara,ProgMatQtyItemShow progMatQtyItemShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            BigDecimal bdSubctt_SignPartAPrice= ToolUtil.getBdIgnoreNull(
                    progMatQtyItemShowPara.getSubctt_SignPartAPrice());
            BigDecimal bdSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(
                    progMatQtyItemShowPara.getSubctt_ContractQuantity());
            if(strSubmitTypePara.equals("Sel")){
                progMatQtyItemShowSel =(ProgMatQtyItemShow)BeanUtils.cloneBean(progMatQtyItemShowPara) ;
                progMatQtyItemShowSel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShowSel.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�����");
                    return;
                }
                progMatQtyItemShowUpd =(ProgMatQtyItemShow) BeanUtils.cloneBean(progMatQtyItemShowPara) ;
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
                progMatQtyItemShowDel =(ProgMatQtyItemShow) BeanUtils.cloneBean(progMatQtyItemShowPara) ;
                progMatQtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatQtyItemShowDel.getSubctt_StrNo()));
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
            ProgInfoShow progInfoShowSel=new ProgInfoShow();
            progInfoShowSel.setStlType(esInitStl.getStlType());
            progInfoShowSel.setStlPkid(esInitStl.getStlPkid());
            progInfoShowSel.setPeriodNo(esInitStl.getPeriodNo());
            progInfoShowSel.setPowerType(esInitStl.getStlType());
            progInfoShowSel.setPowerPkid(esInitStl.getStlPkid());
            progInfoShowSel.setPeriodNo(esInitStl.getPeriodNo());

            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    EsCttInfo esCttInfoTemp=cttInfoService.getCttInfoByPkId(progInfoShowSel.getStlPkid());
                    if (("3").equals(esCttInfoTemp.getType())||("6").equals(esCttInfoTemp.getType())){
                        ProgInfoShow progInfoShowQryQ=new ProgInfoShow();
                        progInfoShowQryQ.setStlPkid(progInfoShowSel.getStlPkid());
                        progInfoShowQryQ.setStlType("3");
                        progInfoShowQryQ.setPeriodNo(progInfoShowSel.getPeriodNo());
                        List<ProgInfoShow> progInfoShowConstructsTemp =
                                esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowQryQ);
                        if (progInfoShowConstructsTemp.size()==0){
                            esInitStl.setAutoLinkAdd("0");
                            progStlInfoService.updateRecord(esInitStl);
                            progInfoShowQryQ.setAutoLinkAdd("1");
                            progInfoShowQryQ.setId(getMaxId(progInfoShowQryQ.getStlType()));
                            progStlInfoService.insertRecord(progInfoShowQryQ);
                        }else{
                            for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                                if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getStatusFlag()))){
                                    progInfoShowSel.setAutoLinkAdd("0");
                                    progStlInfoService.updateRecord(esInitStl);
                                    progInfoShowQryQ.setAutoLinkAdd("1");
                                    progStlInfoService.insertRecord(progInfoShowQryQ);
                                }
                            }
                        }
                    }
                    esFlowControl.mngFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    strPassFlag="false";
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    esInitStl.setAutoLinkAdd("");
                    progStlInfoService.updateRecord(esInitStl);
                    esFlowControl.mngNotFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    strPassFlag="true";
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // ԭ�����ͨ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // ԭ�����δ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
                    try {
                        // ״̬��־������
                        progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                        // ԭ�򣺸���ͨ��
                        progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                        flowCtrlService.updateRecordByStl(progInfoShowSel,strPowerType);
                        MessageUtil.addInfo("���ݸ���ͨ����");
                    }catch (Exception e) {
                        logger.error("����ͨ������ʧ�ܡ�", e);
                        MessageUtil.addError("����ͨ������ʧ�ܡ�");
                        return;
                    }
                }else if(strPowerType.equals("DoubleCheckFail")){
                    try {
                        String SubcttStlPStatus = ToolUtil.getStrIgnoreNull(
                                esFlowService.getStatusFlag(ESEnum.ITEMTYPE5.getCode(), progInfoShowSel.getStlPkid(), progInfoShowSel.getPeriodNo()));
                        if (!("".equals(SubcttStlPStatus)) && ESEnumStatusFlag.STATUS_FLAG2.getCode().compareTo(SubcttStlPStatus) < 0) {
                            MessageUtil.addInfo("�������ѱ��ְ��۸������׼������Ȩ���в�����");
                            return;
                        }
                        // ����д����ʵ��Խ���˻�
                        progInfoShowSel.setStatusFlag(strNotPassToStatus);
                        // ԭ�򣺸���δ��
                        progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                        flowCtrlService.updateRecordByStl(progInfoShowSel,strPowerType);
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
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // ԭ����׼ͨ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                }else if(strPowerType.equals("ApproveFail")){
                    // ����д����ʵ��Խ���˻�
                    progInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // ԭ����׼δ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);

                    MessageUtil.addInfo("������׼δ����");
                }
            }
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public String onExportExcel()throws IOException, WriteException {
        if (this.progMatQtyItemShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ����Ͻ���-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlSubCttEngM.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    public String getMaxId(String strStlType) {
        Integer intTemp;
        String strMaxId = progStlInfoService.getStrMaxStlId(strStlType);
        if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
            strMaxId = "STLQ" + esCommon.getStrToday() + "001";
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

    public ProgMatQtyItemShow getProgMatQtyItemShowSel() {
        return progMatQtyItemShowSel;
    }

    public void setProgMatQtyItemShowSel(ProgMatQtyItemShow progMatQtyItemShowSel) {
        this.progMatQtyItemShowSel = progMatQtyItemShowSel;
    }

    public List<ProgMatQtyItemShow> getProgMatQtyItemShowList() {
        return progMatQtyItemShowList;
    }

    public void setProgMatQtyItemShowList(List<ProgMatQtyItemShow> progMatQtyItemShowList) {
        this.progMatQtyItemShowList = progMatQtyItemShowList;
    }

    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public List<ProgMatQtyItemShow> getProgMatQtyItemShowListExcel() {
        return progMatQtyItemShowListExcel;
    }

    public void setProgMatQtyItemShowListExcel(List<ProgMatQtyItemShow> progMatQtyItemShowListExcel) {
        this.progMatQtyItemShowListExcel = progMatQtyItemShowListExcel;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public ProgInfoShow getProgMatqtyInfoShowH() {
        return progMatqtyInfoShowH;
    }

    public void setProgMatqtyInfoShowH(ProgInfoShow progMatqtyInfoShowH) {
        this.progMatqtyInfoShowH = progMatqtyInfoShowH;
    }

    public EsInitStl getEsInitStl() {
        return esInitStl;
    }

    public void setEsInitStl(EsInitStl esInitStl) {
        this.esInitStl = esInitStl;
    }
	/*�����ֶ�End*/

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }
}
