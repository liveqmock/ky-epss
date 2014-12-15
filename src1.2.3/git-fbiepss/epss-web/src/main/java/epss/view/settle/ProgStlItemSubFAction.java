package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ProgStlItemSubFShow;
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
    private BigDecimal bDEngSMng_BeginToCurrentPeriodSEXPInDB;
    private BigDecimal bDEngSMng_CurrentPeriodSEXPInDB;

    private String strSubmitType;
    private ProgStlInfo progStlInfo;

    /*����ά������㼶���ֵ���ʾ*/
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;
    private List<ProgStlItemSubFShow> progStlItemSubFShowListExcel;
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
            progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkidTemp);
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
            progStlInfoShow.setType(EnumSubcttType.getValueByKey(cttInfoTemp.getType()).getTitle());
            beansMap.put("progStlInfoShow", progStlInfoShow);

            /*�ְ���ͬ*/
            List<CttItem> cttItemList =new ArrayList<CttItem>();
            cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE2.getCode(), progStlInfo.getStlPkid());
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemSubFShowList =new ArrayList<ProgStlItemSubFShow>();
            recursiveDataTable("root", cttItemList, progStlItemSubFShowList);
            progStlItemSubFShowList =getStlSubCttEngSMngConstructList_DoFromatNo(progStlItemSubFShowList);
            progStlItemSubFShowListExcel =new ArrayList<ProgStlItemSubFShow>();
            for(ProgStlItemSubFShow itemUnit: progStlItemSubFShowList){
                ProgStlItemSubFShow itemUnitTemp= (ProgStlItemSubFShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getSubctt_StrNo()));
                progStlItemSubFShowListExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemSubFShowListExcel", progStlItemSubFShowListExcel);
            beansMap.put("progStlItemSubFShowList", progStlItemSubFShowList);
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubFShow> sProgStlItemSubFShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<>();
        // ͨ������id�������ĺ���
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
            for(CttItem itemUnit: subCttItemList){
            ProgStlItemSubFShow progStlItemSubFShowTemp = new ProgStlItemSubFShow();
            progStlItemSubFShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progStlItemSubFShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progStlItemSubFShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemSubFShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemSubFShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progStlItemSubFShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progStlItemSubFShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemSubFShowTemp.setSubctt_Name(itemUnit.getName());
            progStlItemSubFShowTemp.setSubctt_Remark(itemUnit.getRemark());
            progStlItemSubFShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubFShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubFShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubFShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progStlItemSubFShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());

            ProgStlItemSubF progStlItemSubF =new ProgStlItemSubF();
            progStlItemSubF.setSubcttPkid(progStlInfo.getStlPkid());
            progStlItemSubF.setSubcttItemPkid(itemUnit.getPkid());
            progStlItemSubF.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemSubF> progStlItemSubFList =
                    progStlItemSubFService.selectRecordsByExample(progStlItemSubF);
            if(progStlItemSubFList.size()>0) {
                progStlItemSubF = progStlItemSubFList.get(0);
                progStlItemSubFShowTemp.setEngSMng_Pkid(progStlItemSubF.getPkid());
                progStlItemSubFShowTemp.setEngSMng_PeriodNo(progStlItemSubF.getPeriodNo());
                progStlItemSubFShowTemp.setEngSMng_SubcttPkid(progStlItemSubF.getSubcttPkid());
                progStlItemSubFShowTemp.setEngSMng_SubcttItemPkid(progStlItemSubF.getSubcttItemPkid());
                progStlItemSubFShowTemp.setEngSMng_AddUpToAmt(progStlItemSubF.getAddUpToAmt());
                progStlItemSubFShowTemp.setEngSMng_ThisStageAmt(progStlItemSubF.getThisStageAmt());
                progStlItemSubFShowTemp.setEngSMng_ArchivedFlag(progStlItemSubF.getArchivedFlag());
                progStlItemSubFShowTemp.setEngSMng_CreatedBy(progStlItemSubF.getCreatedBy());
                progStlItemSubFShowTemp.setEngSMng_CreatedTime(progStlItemSubF.getCreatedTime());
                progStlItemSubFShowTemp.setEngSMng_LastUpdBy(progStlItemSubF.getLastUpdBy());
                progStlItemSubFShowTemp.setEngSMng_LastUpdTime(progStlItemSubF.getLastUpdTime());
                progStlItemSubFShowTemp.setEngSMng_RecVersion(progStlItemSubF.getRecVersion());
                if (progStlItemSubFShowTemp.getEngSMng_AddUpToAmt() != null) {
                    if (progStlItemSubFShowTemp.getEngSMng_AddUpToAmt()
                            .equals(progStlItemSubFShowTemp.getSubctt_ContractAmount())) {
                        progStlItemSubFShowTemp.setIsUptoCttAmtFlag(true);
                    }
                }
            }
            sProgStlItemSubFShowListPara.add(progStlItemSubFShowTemp) ;
            recursiveDataTable(progStlItemSubFShowTemp.getSubctt_Pkid(), cttItemListPara, sProgStlItemSubFShowListPara);
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

    public boolean blurEngSMng_CurrentPeriodSEXP() {

        if(ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getSubctt_ContractAmount()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progStlItemSubFShowUpd.getEngSMng_ThisStageAmt().toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDEngSMng_CurrentPeriodSSEXPTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getEngSMng_ThisStageAmt()  );
            //����������,
            BigDecimal bigDecimalTemp=
                    bDEngSMng_BeginToCurrentPeriodSEXPInDB.add(bDEngSMng_CurrentPeriodSSEXPTemp).subtract(bDEngSMng_CurrentPeriodSEXPInDB );

            BigDecimal bDSubctt_ContracttAmount=
                    ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getSubctt_ContractAmount());
                if(bigDecimalTemp.compareTo(bDSubctt_ContracttAmount)>0){
                    MessageUtil.addError("���ڿ��۰�ȫ��ʩ��+���ڰ�ȫ��ʩ��>��ͬ��������ȷ��������� ��"
                            + bDEngSMng_CurrentPeriodSSEXPTemp.toString() + "����");
                    return false;
                }
            progStlItemSubFShowUpd.setEngSMng_AddUpToAmt (bigDecimalTemp);
        }
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

                bDEngSMng_CurrentPeriodSEXPInDB=ToolUtil.getBdIgnoreNull(progStlItemSubFShowUpd.getEngSMng_ThisStageAmt());
                bDEngSMng_BeginToCurrentPeriodSEXPInDB=
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
    /*�����ֶ�End*/
}
