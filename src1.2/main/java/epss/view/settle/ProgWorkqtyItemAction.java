package epss.view.settle;

import epss.common.enums.*;
import epss.common.utils.JxlsManager;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.model.model_show.ProgWorkqtyItemShow;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
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
public class ProgWorkqtyItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgWorkqtyItemAction.class);
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
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;

    private List<ProgWorkqtyItemShow> progWorkqtyItemShowList;
    private ProgWorkqtyItemShow progWorkqtyItemShowSel;
    private ProgWorkqtyItemShow progWorkqtyItemShowUpd;
    private ProgWorkqtyItemShow progWorkqtyItemShowDel;
    private BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEngQMng_CurrentPeriodEQtyInDB;

    private String strSubmitType;
    private String strSubcttPkid;
    private EsInitStl progWorkqtyInfo;
    private ProgInfoShow progWorkqtyInfoShowH;

    /*����ά������㼶���ֵ���ʾ*/
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgWorkqtyItemShow> progWorkqtyItemShowListExcel;
    private Map beansMap;
    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        beansMap = new HashMap();
        if(parammap.containsKey("strFlowType")){
            strFlowType=parammap.get("strFlowType").toString();
        }
        if(parammap.containsKey("strProgWorkqtyInfoPkid")){
            String strProgWorkqtyInfoPkidTemp=parammap.get("strProgWorkqtyInfoPkid").toString();
            progWorkqtyInfo = progStlInfoService.selectRecordsByPrimaryKey(strProgWorkqtyInfoPkidTemp);
            strSubcttPkid= progWorkqtyInfo.getStlPkid();
        }

        List<EsInitPower> esInitPowerList=
                flowCtrlService.selectListByModel(progWorkqtyInfo.getStlType(),progWorkqtyInfo.getStlPkid(),progWorkqtyInfo.getPeriodNo());
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
            EsCttInfo esCttInfo_Subctt= cttInfoService.getCttInfoByPkId(progWorkqtyInfo.getStlPkid());
            progWorkqtyInfoShowH.setId(progWorkqtyInfo.getId());
            progWorkqtyInfoShowH.setStlName(cttInfoService.getCttInfoByPkId(progWorkqtyInfo.getStlPkid()).getName());
            progWorkqtyInfoShowH.setPeriodNo(progWorkqtyInfo.getPeriodNo());
            progWorkqtyInfoShowH.setSignPartBName(signPartService.getEsInitCustByPkid(esCttInfo_Subctt.getSignPartB()).getName());
            beansMap.put("progWorkqtyInfoShowH", progWorkqtyInfoShowH);
        /*�ְ���ͬ*/
            List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
            esCttItemList = cttItemService.getEsItemList(
                    ESEnum.ITEMTYPE2.getCode(), strSubcttPkid);
            if(esCttItemList.size()<=0){
                return;
            }
            progWorkqtyItemShowList =new ArrayList<ProgWorkqtyItemShow>();
            recursiveDataTable("root", esCttItemList, progWorkqtyItemShowList);
            progWorkqtyItemShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progWorkqtyItemShowList);
            List<EsInitPower> esInitPowerList= flowCtrlService.selectListByModel(ESEnumPower.POWER_TYPE3.getCode(),
                    strSubcttPkid, esCommon.getStrDateThisPeriod());
            progWorkqtyItemShowListExcel =new ArrayList<ProgWorkqtyItemShow>();
            for(ProgWorkqtyItemShow itemUnit: progWorkqtyItemShowList){
                ProgWorkqtyItemShow itemUnitTemp= (ProgWorkqtyItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                progWorkqtyItemShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progWorkqtyItemShowListExcel", progWorkqtyItemShowListExcel);
            beansMap.put("progWorkqtyItemShowList", progWorkqtyItemShowList);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }

    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgWorkqtyItemShow> sProgWorkqtyItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgWorkqtyItemShow progWorkqtyItemShowTemp = new ProgWorkqtyItemShow();
            progWorkqtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progWorkqtyItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progWorkqtyItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progWorkqtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progWorkqtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progWorkqtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progWorkqtyItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());

            progWorkqtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progWorkqtyItemShowTemp.setSubctt_Note(itemUnit.getNote());

            progWorkqtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progWorkqtyItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progWorkqtyItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progWorkqtyItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progWorkqtyItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            EsItemStlSubcttEngQ esItemStlSubcttEngQ=new EsItemStlSubcttEngQ();
            esItemStlSubcttEngQ.setSubcttPkid(strSubcttPkid);
            esItemStlSubcttEngQ.setSubcttItemPkid(itemUnit.getPkid());
            esItemStlSubcttEngQ.setPeriodNo(progWorkqtyInfo.getPeriodNo());
            List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList =
                    progWorkqtyItemService.selectRecordsByExample(esItemStlSubcttEngQ);
            if(esItemStlSubcttEngQList.size()>0){
                esItemStlSubcttEngQ= esItemStlSubcttEngQList.get(0);
                progWorkqtyItemShowTemp.setEngQMng_Pkid(esItemStlSubcttEngQ.getPkid());
                progWorkqtyItemShowTemp.setEngQMng_PeriodNo(esItemStlSubcttEngQ.getPeriodNo());
                progWorkqtyItemShowTemp.setEngQMng_SubcttPkid(esItemStlSubcttEngQ.getSubcttPkid());

                progWorkqtyItemShowTemp.setEngQMng_SubcttItemPkid(esItemStlSubcttEngQ.getSubcttItemPkid());

                progWorkqtyItemShowTemp.setEngQMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngQ.getBeginToCurrentPeriodEQty());
                progWorkqtyItemShowTemp.setEngQMng_CurrentPeriodEQty(esItemStlSubcttEngQ.getCurrentPeriodEQty());
                progWorkqtyItemShowTemp.setEngQMng_DeletedFlag(esItemStlSubcttEngQ.getDeleteFlag());
                progWorkqtyItemShowTemp.setEngQMng_CreatedBy(esItemStlSubcttEngQ.getCreatedBy());
                progWorkqtyItemShowTemp.setEngQMng_CreatedDate(esItemStlSubcttEngQ.getCreatedDate());
                progWorkqtyItemShowTemp.setEngQMng_LastUpdBy(esItemStlSubcttEngQ.getLastUpdBy());
                progWorkqtyItemShowTemp.setEngQMng_LastUpdDate(esItemStlSubcttEngQ.getLastUpdDate());
                progWorkqtyItemShowTemp.setEngQMng_ModificationNum(esItemStlSubcttEngQ.getModificationNum());
            }
            sProgWorkqtyItemShowListPara.add(progWorkqtyItemShowTemp) ;
            recursiveDataTable(progWorkqtyItemShowTemp.getSubctt_Pkid(), esCttItemListPara, sProgWorkqtyItemShowListPara);
        }
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgWorkqtyItemShow> getStlSubCttEngQMngConstructList_DoFromatNo(
            List<ProgWorkqtyItemShow> progWorkqtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgWorkqtyItemShow itemUnit: progWorkqtyItemShowListPara){
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
        return progWorkqtyItemShowListPara;
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
        progWorkqtyInfoShowH=new ProgInfoShow();
        progWorkqtyItemShowSel =new ProgWorkqtyItemShow();
        progWorkqtyItemShowUpd =new ProgWorkqtyItemShow();
        progWorkqtyItemShowDel =new ProgWorkqtyItemShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEngQMng_CurrentPeriodMQty("submit")){
                    return;
                }
                progWorkqtyItemShowUpd.setEngQMng_PeriodNo(progWorkqtyInfo.getPeriodNo());
                List<EsItemStlSubcttEngQ> esItemStlSubcttEngQListTemp =
                        progWorkqtyItemService.isExistInDb(progWorkqtyItemShowUpd);
                if (esItemStlSubcttEngQListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (esItemStlSubcttEngQListTemp.size() == 1) {
                    progWorkqtyItemShowUpd.setEngQMng_Pkid (esItemStlSubcttEngQListTemp.get(0).getPkid());
                    progWorkqtyItemService.updateRecord(progWorkqtyItemShowUpd);
                }
                if (esItemStlSubcttEngQListTemp.size()==0){
                    progWorkqtyItemShowUpd.setEngQMng_SubcttPkid(strSubcttPkid);
                    progWorkqtyItemShowUpd.setEngQMng_SubcttItemPkid(progWorkqtyItemShowUpd.getSubctt_Pkid());
                    progWorkqtyItemService.insertRecord(progWorkqtyItemShowUpd);
                }
                MessageUtil.addInfo("����������ɡ�");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progWorkqtyItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }

    public boolean blurEngQMng_CurrentPeriodMQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getSubctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progWorkqtyItemShowUpd.getEngQMng_CurrentPeriodEQty().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDEngQMng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEngQMng_BeginToCurrentPeriodEQtyInDB.add(bDEngQMng_CurrentPeriodEQtyTemp).subtract(bDEngQMng_CurrentPeriodEQtyInDB);

            BigDecimal bDSubctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getSubctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_ContractQuantity)>0){
                    MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                            + bDEngQMng_CurrentPeriodEQtyTemp.toString() + "����");
                    return false;
                }
                progWorkqtyItemShowUpd.setEngQMng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_BeginToCurrentPeriodEQty());

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
    private void delRecordAction(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        try {
            int deleteRecordNum= progWorkqtyItemService.deleteRecord(progWorkqtyItemShowPara.getEngQMng_Pkid());
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
    public void selectRecordAction(String strSubmitTypePara,ProgWorkqtyItemShow progWorkqtyItemShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                progWorkqtyItemShowSel =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowPara) ;
                progWorkqtyItemShowSel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowSel.getSubctt_StrNo()));
            }
            else if(strSubmitTypePara.equals("Upd")){
                progWorkqtyItemShowUpd =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowPara) ;
                progWorkqtyItemShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowUpd.getSubctt_StrNo()));
                bDEngQMng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_CurrentPeriodEQty());
                bDEngQMng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getEngQMng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progWorkqtyItemShowDel =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowPara) ;
                progWorkqtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowDel.getSubctt_StrNo()));
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
            ProgInfoShow progInfoShowSel=new ProgInfoShow();
            progInfoShowSel.setStlType(progWorkqtyInfo.getStlType());
            progInfoShowSel.setStlPkid(progWorkqtyInfo.getStlPkid());
            progInfoShowSel.setPeriodNo(progWorkqtyInfo.getPeriodNo());
            progInfoShowSel.setPowerType(progWorkqtyInfo.getStlType());
            progInfoShowSel.setPowerPkid(progWorkqtyInfo.getStlPkid());
            progInfoShowSel.setPeriodNo(progWorkqtyInfo.getPeriodNo());

            if (strPowerType.contains("Mng")) {
                if (strPowerType.equals("MngPass")) {
                    esFlowControl.mngFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    strPassFlag="false";
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerType.equals("MngFail")) {
                    esFlowControl.mngNotFinishAction(
                            progInfoShowSel.getStlType(),
                            progInfoShowSel.getStlPkid(),
                            progInfoShowSel.getPeriodNo());
                    strPassFlag="true";
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            } else if (strPowerType.contains("Check") && !strPowerType.contains("DoubleCheck")) {// ���
                if (strPowerType.equals("CheckPass")) {
                    // ״̬��־�����
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // ԭ�����ͨ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerType.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // ԭ�����δ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
            } else if (strPowerType.contains("DoubleCheck")) {// ����
                if (strPowerType.equals("DoubleCheckPass")) {
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
                } else if (strPowerType.equals("DoubleCheckFail")) {
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
            } else if (strPowerType.contains("Approve")) {// ��׼
                if (strPowerType.equals("ApprovePass")) {
                    // ״̬��־����׼
                    progInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // ԭ����׼ͨ��
                    progInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    flowCtrlService.updateRecordByStl(progInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                } else if (strPowerType.equals("ApproveFail")) {
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
        if (this.progWorkqtyItemShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ���������-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlSubCttEngQ.xls");
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

    public ProgInfoShow getProgWorkqtyInfoShowH() {
        return progWorkqtyInfoShowH;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowSel() {
        return progWorkqtyItemShowSel;
    }

    public void setProgWorkqtyItemShowSel(ProgWorkqtyItemShow progWorkqtyItemShowSel) {
        this.progWorkqtyItemShowSel = progWorkqtyItemShowSel;
    }

    public List<ProgWorkqtyItemShow> getProgWorkqtyItemShowList() {
        return progWorkqtyItemShowList;
    }

    public void setProgWorkqtyItemShowList(List<ProgWorkqtyItemShow> progWorkqtyItemShowList) {
        this.progWorkqtyItemShowList = progWorkqtyItemShowList;
    }

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
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

    public ProgWorkqtyItemShow getProgWorkqtyItemShowDel() {
        return progWorkqtyItemShowDel;
    }

    public void setProgWorkqtyItemShowDel(ProgWorkqtyItemShow progWorkqtyItemShowDel) {
        this.progWorkqtyItemShowDel = progWorkqtyItemShowDel;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowUpd() {
        return progWorkqtyItemShowUpd;
    }

    public void setProgWorkqtyItemShowUpd(ProgWorkqtyItemShow progWorkqtyItemShowUpd) {
        this.progWorkqtyItemShowUpd = progWorkqtyItemShowUpd;
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

    public List<ProgWorkqtyItemShow> getProgWorkqtyItemShowListExcel() {
        return progWorkqtyItemShowListExcel;
    }

    public void setProgWorkqtyItemShowListExcel(List<ProgWorkqtyItemShow> progWorkqtyItemShowListExcel) {
        this.progWorkqtyItemShowListExcel = progWorkqtyItemShowListExcel;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }
}

