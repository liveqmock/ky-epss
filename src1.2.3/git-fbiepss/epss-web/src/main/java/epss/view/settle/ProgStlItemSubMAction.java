package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubMShow;
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
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;

    private List<ProgStlItemSubMShow> progStlItemSubMShowList;
    private ProgStlItemSubMShow progStlItemSubMShowSel;
    private ProgStlItemSubMShow progStlItemSubMShowUpd;
    private ProgStlItemSubMShow progStlItemSubMShowDel;
    private BigDecimal bDEngMMng_BeginToCurrentPeriodMQtyInDB;
    private BigDecimal bDEngMMng_CurrentPeriodMQtyInDB;

    private String strSubmitType;
    private ProgStlInfo progStlInfo;

    /*����ά������㼶���ֵ���ʾ*/
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgStlItemSubMShow> progStlItemSubMShowListExcel;
    private Map beansMap;
    private ProgStlInfoShow progStlInfoShow;
    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        beansMap = new HashMap();
        if(parammap.containsKey("strFlowType")){
            strFlowType=parammap.get("strFlowType").toString();
        }
        if(parammap.containsKey("strStlInfoPkid")){
            String strStlInfoPkidTemp=parammap.get("strStlInfoPkid").toString();
            progStlInfo = progStlInfoService.selectRecordsByPrimaryKey(strStlInfoPkidTemp);
        }

        strPassFlag="true";
        if("Mng".equals(strFlowType)&&
                EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfo.getFlowStatus())) {
            strPassFlag="false";
        }
        resetAction();
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        try {
            // ��ϸҳͷ
            CttInfo cttInfoTemp = cttInfoService.getCttInfoByPkId(progStlInfo.getStlPkid());
            progStlInfoShow =progStlInfoService.fromModelToModelShow(progStlInfo);
            progStlInfoShow.setStlId(cttInfoTemp.getId());
            progStlInfoShow.setStlName(cttInfoTemp.getName());
            progStlInfoShow.setSignPartBName(signPartService.getEsInitCustByPkid(cttInfoTemp.getSignPartB()).getName());
            beansMap.put("progStlInfoShow", progStlInfoShow);

            /*�ְ���ͬ*/
            List<CttItem> cttItemList =new ArrayList<CttItem>();
            cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE2.getCode(), progStlInfo.getStlPkid());
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemSubMShowList =new ArrayList<ProgStlItemSubMShow>();
            recursiveDataTable("root", cttItemList, progStlItemSubMShowList);
            progStlItemSubMShowList =getStlSubCttEngMMngConstructList_DoFromatNo(progStlItemSubMShowList);
            progStlItemSubMShowListExcel =new ArrayList<ProgStlItemSubMShow>();
            for(ProgStlItemSubMShow itemUnit: progStlItemSubMShowList){
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
    /*�������ݿ��в㼶��ϵ�����б��õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubMShow> sProgStlItemSubMShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
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
            progStlItemSubMShowTemp.setSubctt_Note(itemUnit.getNote());
            progStlItemSubMShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubMShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubMShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubMShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progStlItemSubMShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            ProgStlItemSubM progStlItemSubM =new ProgStlItemSubM();
            progStlItemSubM.setSubcttPkid(progStlInfo.getStlPkid());
            progStlItemSubM.setSubcttItemPkid(itemUnit.getPkid());
            progStlItemSubM.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemSubM> progStlItemSubMList =
                    progMatqtyItemService.selectRecordsByExample(progStlItemSubM);
            if(progStlItemSubMList.size()>0){
                progStlItemSubM = progStlItemSubMList.get(0);
                progStlItemSubMShowTemp.setEngMMng_Pkid(progStlItemSubM.getPkid());
                progStlItemSubMShowTemp.setEngMMng_PeriodNo(progStlItemSubM.getPeriodNo());
                progStlItemSubMShowTemp.setEngMMng_SubcttPkid(progStlItemSubM.getSubcttPkid());
                progStlItemSubMShowTemp.setEngMMng_SubcttItemPkid(progStlItemSubM.getSubcttItemPkid());
                progStlItemSubMShowTemp.setEngMMng_BeginToCurrentPeriodMQty(progStlItemSubM.getBeginToCurrentPeriodMQty());
                progStlItemSubMShowTemp.setEngMMng_CurrentPeriodMQty(progStlItemSubM.getCurrentPeriodMQty());
                progStlItemSubMShowTemp.setEngMMng_MPurchaseUnitPrice(progStlItemSubM.getmPurchaseUnitPrice());
                progStlItemSubMShowTemp.setEngMMng_DeletedFlag(progStlItemSubM.getArchivedFlag());
                progStlItemSubMShowTemp.setEngMMng_CreatedBy(progStlItemSubM.getCreatedBy());
                progStlItemSubMShowTemp.setEngMMng_CreatedTime(progStlItemSubM.getCreatedTime());
                progStlItemSubMShowTemp.setEngMMng_LastUpdBy(progStlItemSubM.getLastUpdBy());
                progStlItemSubMShowTemp.setEngMMng_LastUpdTime(progStlItemSubM.getLastUpdTime());
                progStlItemSubMShowTemp.setEngMMng_ModificationNum(progStlItemSubM.getModificationNum());
            }
            sProgStlItemSubMShowListPara.add(progStlItemSubMShowTemp) ;
            recursiveDataTable(progStlItemSubMShowTemp.getSubctt_Pkid(), cttItemListPara, sProgStlItemSubMShowListPara);
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
    /*�������ݿ��в㼶��ϵ�����б��õ�ĳһ�ڵ��µ��ӽڵ�*/
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
                if(!blurEngMMng_CurrentPeriodMQty("submit")){
                    return;
                }
                ProgStlItemSubMShow progStlItemSubMShowTemp =new ProgStlItemSubMShow();
                progStlItemSubMShowTemp.setEngMMng_SubcttPkid(progStlInfo.getStlPkid());
                progStlItemSubMShowTemp.setEngMMng_PeriodNo(progStlInfo.getPeriodNo());
                progStlItemSubMShowTemp.setEngMMng_SubcttItemPkid(progStlItemSubMShowUpd.getSubctt_Pkid());
                List<ProgStlItemSubM> progStlItemSubMListTemp =
                        progMatqtyItemService.isExistInDb(progStlItemSubMShowTemp);
                if (progStlItemSubMListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (progStlItemSubMListTemp.size() == 1) {
                    progStlItemSubMShowUpd.setEngMMng_Pkid (progStlItemSubMListTemp.get(0).getPkid());
                    updRecordAction(progStlItemSubMShowUpd);
                } else if (progStlItemSubMListTemp.size()==0){
                    progStlItemSubMShowUpd.setEngMMng_SubcttPkid(progStlInfo.getStlPkid());
                    progStlItemSubMShowUpd.setEngMMng_PeriodNo(progStlInfo.getPeriodNo());
                    progStlItemSubMShowUpd.setEngMMng_SubcttItemPkid(progStlItemSubMShowUpd.getSubctt_Pkid());
                    addRecordAction(progStlItemSubMShowUpd);
                }
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemSubMShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
            logger.error("��������ʧ�ܣ�", e);
        }
    }

    public boolean blurEngMMng_CurrentPeriodMQty(String strBlurOrSubmitFlag){

        if(ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getSubctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty().toString().matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDEngQMng_CurrentPeriodMQtyTemp= 
			ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty());

            BigDecimal bigDecimalTemp= 
			         bDEngMMng_BeginToCurrentPeriodMQtyInDB.add(bDEngQMng_CurrentPeriodMQtyTemp).subtract(bDEngMMng_CurrentPeriodMQtyInDB);

            BigDecimal bDSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getSubctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                    MessageUtil.addInfo("���ڲ����ۼƹ�Ӧ����+���ڲ��Ϲ�Ӧ����>��ͬ������ȷ������ı��ڲ��Ϲ�Ӧ������"
                            + bDEngQMng_CurrentPeriodMQtyTemp.toString() + "���Ƿ���ȷ��");
                 }
                progStlItemSubMShowUpd.setEngMMng_BeginToCurrentPeriodMQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodMQtyTemp=
                        ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
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

    private void addRecordAction(ProgStlItemSubMShow progStlItemSubMShowPara){
        try {
            progMatqtyItemService.insertRecord(progStlItemSubMShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlItemSubMShow progStlItemSubMShowPara){
        try {
            progMatqtyItemService.updateRecord(progStlItemSubMShowPara);
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
                int deleteRecordNum = progMatqtyItemService.deleteRecord(
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
            BigDecimal bdSubctt_SignPartAPrice= ToolUtil.getBdIgnoreNull(
                    progStlItemSubMShowPara.getSubctt_SignPartAPrice());
            BigDecimal bdSubctt_ContractQuantity= ToolUtil.getBdIgnoreNull(
                    progStlItemSubMShowPara.getSubctt_ContractQuantity());
            if(strSubmitTypePara.equals("Sel")){
                progStlItemSubMShowSel =(ProgStlItemSubMShow)BeanUtils.cloneBean(progStlItemSubMShowPara) ;
                progStlItemSubMShowSel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubMShowSel.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�����");
                    return;
                }
                progStlItemSubMShowUpd =(ProgStlItemSubMShow) BeanUtils.cloneBean(progStlItemSubMShowPara) ;
                progStlItemSubMShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubMShowUpd.getSubctt_StrNo()));

                bDEngMMng_CurrentPeriodMQtyInDB=ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_CurrentPeriodMQty());
                bDEngMMng_BeginToCurrentPeriodMQtyInDB=
                        ToolUtil.getBdIgnoreNull(progStlItemSubMShowUpd.getEngMMng_BeginToCurrentPeriodMQty());
            }else
            if(strSubmitTypePara.equals("Del")){
                if(bdSubctt_SignPartAPrice.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_ContractQuantity.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�ɾ��");
                    return;
                }
                progStlItemSubMShowDel =(ProgStlItemSubMShow) BeanUtils.cloneBean(progStlItemSubMShowPara) ;
                progStlItemSubMShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemSubMShowDel.getSubctt_StrNo()));
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
                    CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId(progStlInfo.getStlPkid());
                    if (("3").equals(cttInfoTemp.getType())||("6").equals(cttInfoTemp.getType())){
                        ProgStlInfoShow progStlInfoShowQryQ =new ProgStlInfoShow();
                        progStlInfoShowQryQ.setStlPkid(progStlInfo.getStlPkid());
                        progStlInfoShowQryQ.setStlType("3");
                        progStlInfoShowQryQ.setPeriodNo(progStlInfo.getPeriodNo());
                        List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                                progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQryQ);
                        if (progStlInfoShowConstructsTemp.size()==0){
                            progStlInfo.setAutoLinkAdd("0");
                            progStlInfoService.updateRecord(progStlInfo);
                            progStlInfoShowQryQ.setAutoLinkAdd("1");
                            progStlInfoShowQryQ.setId(getMaxId(progStlInfoShowQryQ.getStlType()));
                            progStlInfoService.insertRecord(progStlInfoShowQryQ);
                        }else{
                            for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                                if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))){
                                    progStlInfo.setAutoLinkAdd("0");
                                    progStlInfoService.updateRecord(progStlInfo);
                                    progStlInfoShowQryQ.setAutoLinkAdd("1");
                                    progStlInfoService.updateRecord(progStlInfoShowQryQ);
                                }else{
                                    if(("1").equals(esISSOMPCUnit.getAutoLinkAdd())){
                                        progStlInfo.setAutoLinkAdd("0");
                                    }else{
                                        progStlInfo.setAutoLinkAdd("1");
                                    }
                                    progStlInfoService.updateRecord(progStlInfo);
                                }
                            }
                        }
                    }
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ��¼�����
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfoService.updateRecord(progStlInfo);
                    strPassFlag="false";
                    MessageUtil.addInfo("����¼����ɣ�");
                }else if(strPowerType.equals("MngFail")){
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfoService.updateRecord(progStlInfo);
                    strPassFlag="true";
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// ���
                if(strPowerType.equals("CheckPass")){
                    // ״̬��־�����
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfoService.updateRecord(progStlInfo);
                    MessageUtil.addInfo("�������ͨ����");
                }else if(strPowerType.equals("CheckFail")){
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfoService.updateRecord(progStlInfo);
                    MessageUtil.addInfo("�������δ����");
                }
            }else if(strPowerType.contains("DoubleCheck")){// ����
                if(strPowerType.equals("DoubleCheckPass")){
                    try {
                        // ״̬��־������
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                        // ԭ�򣺸���ͨ��
                        progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                        progStlInfoService.updateRecord(progStlInfo);
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
                            if (!(EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(SubcttStlPStatus) < 0)) {
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
                        progStlInfoService.updateRecord(progStlInfo);
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
                    progStlInfoService.updateRecord(progStlInfo);
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
                    progStlInfoService.updateRecord(progStlInfo);

                    MessageUtil.addInfo("������׼δ����");
                }
            }
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public String onExportExcel()throws IOException, WriteException {
        if (this.progStlItemSubMShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ����Ͻ���-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progMatqty.xls");
            // ����״̬��Ʊ����Ҫ����ʱ���޸ĵ����ļ���
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

    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
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

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }
    /*�����ֶ�End*/
}