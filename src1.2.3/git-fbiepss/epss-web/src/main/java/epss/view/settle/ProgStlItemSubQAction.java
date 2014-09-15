package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubQShow;
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
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
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
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgStlItemSubQShow> progStlItemSubQShowListExcel;
    private Map beansMap;
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
            strSubcttPkid= progStlInfo.getStlPkid();
        }

        strPassFlag="true";
        if("Mng".equals(strFlowType)&& EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfo.getFlowStatus())) {
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
                    EnumResType.RES_TYPE2.getCode(), strSubcttPkid);
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemSubQShowList =new ArrayList<ProgStlItemSubQShow>();
            recursiveDataTable("root", cttItemList, progStlItemSubQShowList);
            progStlItemSubQShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progStlItemSubQShowList);
            progStlItemSubQShowListExcel =new ArrayList<ProgStlItemSubQShow>();
            for(ProgStlItemSubQShow itemUnit: progStlItemSubQShowList){
                ProgStlItemSubQShow itemUnitTemp= (ProgStlItemSubQShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                progStlItemSubQShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemSubQShowListExcel", progStlItemSubQShowListExcel);
            beansMap.put("progStlItemSubQShowList", progStlItemSubQShowList);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }

    }
    /*�������ݿ��в㼶��ϵ�����б��õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubQShow> sProgStlItemSubQShowListPara){
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
            progStlItemSubQShowTemp.setSubctt_Note(itemUnit.getNote());

            progStlItemSubQShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubQShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubQShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubQShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progStlItemSubQShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            ProgStlItemSubQ progStlItemSubQ =new ProgStlItemSubQ();
            progStlItemSubQ.setSubcttPkid(strSubcttPkid);
            progStlItemSubQ.setSubcttItemPkid(itemUnit.getPkid());
            progStlItemSubQ.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemSubQ> progStlItemSubQList =
                    progWorkqtyItemService.selectRecordsByExample(progStlItemSubQ);
            if(progStlItemSubQList.size()>0){
                progStlItemSubQ = progStlItemSubQList.get(0);
                progStlItemSubQShowTemp.setEngQMng_Pkid(progStlItemSubQ.getPkid());
                progStlItemSubQShowTemp.setEngQMng_PeriodNo(progStlItemSubQ.getPeriodNo());
                progStlItemSubQShowTemp.setEngQMng_SubcttPkid(progStlItemSubQ.getSubcttPkid());
                progStlItemSubQShowTemp.setEngQMng_SubcttItemPkid(progStlItemSubQ.getSubcttItemPkid());
                progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQty(progStlItemSubQ.getBeginToCurrentPeriodEQty());
                progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQty(progStlItemSubQ.getCurrentPeriodEQty());
                progStlItemSubQShowTemp.setEngQMng_DeletedFlag(progStlItemSubQ.getArchivedFlag());
                progStlItemSubQShowTemp.setEngQMng_CreatedBy(progStlItemSubQ.getCreatedBy());
                progStlItemSubQShowTemp.setEngQMng_CreatedTime(progStlItemSubQ.getCreatedTime());
                progStlItemSubQShowTemp.setEngQMng_LastUpdBy(progStlItemSubQ.getLastUpdBy());
                progStlItemSubQShowTemp.setEngQMng_LastUpdTime(progStlItemSubQ.getLastUpdTime());
                progStlItemSubQShowTemp.setEngQMng_ModificationNum(progStlItemSubQ.getModificationNum());
            }
            sProgStlItemSubQShowListPara.add(progStlItemSubQShowTemp) ;
            recursiveDataTable(progStlItemSubQShowTemp.getSubctt_Pkid(), cttItemListPara, sProgStlItemSubQShowListPara);
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
                        progWorkqtyItemService.isExistInDb(progStlItemSubQShowUpd);
                if (progStlItemSubQListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (progStlItemSubQListTemp.size() == 1) {
                    progStlItemSubQShowUpd.setEngQMng_Pkid (progStlItemSubQListTemp.get(0).getPkid());
                    progWorkqtyItemService.updateRecord(progStlItemSubQShowUpd);
                }
                if (progStlItemSubQListTemp.size()==0){
                    progStlItemSubQShowUpd.setEngQMng_SubcttPkid(strSubcttPkid);
                    progStlItemSubQShowUpd.setEngQMng_SubcttItemPkid(progStlItemSubQShowUpd.getSubctt_Pkid());
                    progWorkqtyItemService.insertRecord(progStlItemSubQShowUpd);
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
        if(ToolUtil.getBdIgnoreNull(progStlItemSubQShowUpd.getSubctt_ContractQuantity()).equals( new BigDecimal(0))){
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
            int deleteRecordNum= progWorkqtyItemService.deleteRecord(progStlItemSubQShowPara.getEngQMng_Pkid());
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
                        CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId(progStlInfo.getStlPkid());
                        if (("3").equals(cttInfoTemp.getType())||("6").equals(cttInfoTemp.getType())){
                            ProgStlInfoShow progStlInfoShowQryM =new ProgStlInfoShow();
                            progStlInfoShowQryM.setStlType("4");
                            progStlInfoShowQryM.setStlPkid(progStlInfo.getStlPkid());
                            progStlInfoShowQryM.setPeriodNo(progStlInfo.getPeriodNo());
                            List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                                    progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQryM);
                            if (progStlInfoShowConstructsTemp.size()==0){
                                progStlInfo.setAutoLinkAdd("0");
                                progStlInfoService.updateRecord(progStlInfo);
                                progStlInfoShowQryM.setAutoLinkAdd("1");
                                progStlInfoShowQryM.setId(getMaxId( progStlInfoShowQryM.getStlType()));
                                progStlInfoService.insertRecord(progStlInfoShowQryM);
                            }else{
                                for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                                    if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))){
                                        progStlInfo.setAutoLinkAdd("0");
                                        progStlInfoService.updateRecord(progStlInfo);
                                        progStlInfoShowQryM.setAutoLinkAdd("1");
                                        progStlInfoService.updateRecord(progStlInfoShowQryM);
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
                } else if (strPowerType.equals("MngFail")) {
                    progStlInfo.setAutoLinkAdd("");
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfoService.updateRecord(progStlInfo);
                    strPassFlag="true";
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            } else if (strPowerType.contains("Check") && !strPowerType.contains("DoubleCheck")) {// ���
                if (strPowerType.equals("CheckPass")) {
                    // ״̬��־�����
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfoService.updateRecord(progStlInfo);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerType.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    progStlInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfoService.updateRecord(progStlInfo);
                    MessageUtil.addInfo("�������δ����");
                }
            } else if (strPowerType.contains("DoubleCheck")) {// ����
                if (strPowerType.equals("DoubleCheckPass")) {
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
                } else if (strPowerType.equals("DoubleCheckFail")) {
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
                    }
                }
            } else if (strPowerType.contains("Approve")) {// ��׼
                if (strPowerType.equals("ApprovePass")) {
                    // ״̬��־����׼
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfoService.updateRecord(progStlInfo);
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
        if (this.progStlItemSubQShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ���������-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"progWorkqty.xls");
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

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
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

    public String getStrPassFlag() {
        return strPassFlag;
    }

    public void setStrPassFlag(String strPassFlag) {
        this.strPassFlag = strPassFlag;
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
/*�����ֶ�End*/

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
}
