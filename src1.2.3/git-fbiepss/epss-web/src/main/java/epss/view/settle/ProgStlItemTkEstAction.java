package epss.view.settle;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ReportHeader;
import epss.repository.model.model_show.ProgStlItemTkEstShow;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import skyline.util.JxlsManager;
import java.text.SimpleDateFormat;
import java.io.IOException;
import jxl.write.WriteException;
/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ����9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlItemTkEstAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemTkEstAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemTkEstService}")
    private ProgStlItemTkEstService progStlItemTkEstService;
    private List<ProgStlItemTkEstShow> progStlItemTkEstShowList;
    private ProgStlItemTkEstShow progStlItemTkEstShowSel;
    private ProgStlItemTkEstShow progStlItemTkEstShowUpd;
    private ProgStlItemTkEstShow progStlItemTkEstShowDel;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private ReportHeader reportHeader;

    /*������*/
    private String strStlInfoPkid;
    private String strTkcttPkid;
    private ProgStlInfo progStlInfo;

    private ProgStlInfoShow progStlInfoShow;

    private String strSubmitType;
    private String strPassVisible;
    private String strPassFailVisible;
    private String strFlowType;
    private String strNotPassToStatus;

    private Map beansMap;
    // �����Ͽؼ�����ʾ����
    private String strExportToExcelRendered;
    private List<ProgStlItemTkEstShow> progStlItemTkEstShowListForExcel;

    // ���̱�ע����
    private String strFlowStatusRemark;

    @PostConstruct
    public void init() {
        try {
            beansMap = new HashMap();
            reportHeader =new ReportHeader();
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            if(parammap.containsKey("strFlowType")){
                strFlowType=parammap.get("strFlowType").toString();
            }
            if(parammap.containsKey("strStlInfoPkid")){
                strStlInfoPkid=parammap.get("strStlInfoPkid").toString();
                this.progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
                strTkcttPkid= this.progStlInfo.getStlPkid();

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
                            ||("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfo.getFlowStatus()))
                            ||("Approve".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS3.getCode().equals(progStlInfo.getFlowStatus()))){
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
        try{
             /*�ְ���ͬ����*/
            // From StlPkid To SubcttPkid
            ProgStlInfo progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
            reportHeader.setStrSubcttPkid(progStlInfo.getStlPkid());
            reportHeader.setStrStlId(progStlInfo.getId());
            // From SubcttPkid To CstplPkid
            CttInfo cttInfoTemp = cttInfoService.getCttInfoByPkId(reportHeader.getStrSubcttPkid());
            reportHeader.setStrCstplPkid(cttInfoTemp.getParentPkid());
            reportHeader.setStrSubcttId(cttInfoTemp.getId());
            reportHeader.setStrSubcttName(cttInfoTemp.getName());
            reportHeader.setStrSignPartPkid(cttInfoTemp.getSignPartB());
            SignPart signPartTemp=signPartService.getEsInitCustByPkid(
                    reportHeader.getStrSignPartPkid());
            if(signPartTemp!=null) {
                reportHeader.setStrSignPartName(signPartTemp.getName());
            }

            beansMap.put("reportHeader", reportHeader);

            progStlInfoShow =progStlInfoService.fromModelToModelShow(progStlInfo);
            progStlInfoShow.setStlId(cttInfoTemp.getId());
            progStlInfoShow.setStlName(cttInfoTemp.getName());
            progStlInfoShow.setSignPartBName(reportHeader.getStrSignPartName());

            /*�ְ���ͬ*/
            List<CttItem> cttItemList =new ArrayList<>();
            progStlItemTkEstShowListForExcel =new ArrayList<>();
            cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE0.getCode(), strTkcttPkid);
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemTkEstShowList =new ArrayList<>();
            recursiveDataTable("root", cttItemList, progStlItemTkEstShowList);
            progStlItemTkEstShowList =getItemStlTkcttEngSMList_DoFromatNo(progStlItemTkEstShowList);
            setItemOfEsItemHieRelapList_AddTotal();
            // Excel�����γ�
            progStlItemTkEstShowListForExcel =new ArrayList<>();
            for(ProgStlItemTkEstShow itemUnit: progStlItemTkEstShowList){
                // �ְ���ͬ
                itemUnit.setTkctt_ContractUnitPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getTkctt_ContractUnitPrice()));
                itemUnit.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getTkctt_ContractQuantity()));
                itemUnit.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getTkctt_ContractAmount()));
                // �ܰ����ȹ�����ͳ�ƽ���
                itemUnit.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
                itemUnit.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));

                ProgStlItemTkEstShow itemUnitTemp= null;
                itemUnitTemp = (ProgStlItemTkEstShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progStlItemTkEstShowListForExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemTkEstShowListForExcel", progStlItemTkEstShowListForExcel);
            // �������趨
            if(progStlItemTkEstShowList.size()>0){
                strExportToExcelRendered="true";
            }else{
                strExportToExcelRendered="false";
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<ProgStlItemTkEstShow> sprogStlItemTkEstShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // ͨ������id�������ĺ���
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemTkEstShow progStlItemTkEstShowTemp = new ProgStlItemTkEstShow();
            progStlItemTkEstShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progStlItemTkEstShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progStlItemTkEstShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemTkEstShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemTkEstShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progStlItemTkEstShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progStlItemTkEstShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemTkEstShowTemp.setTkctt_Name(itemUnit.getName());
            progStlItemTkEstShowTemp.setTkctt_Remark(itemUnit.getRemark());
            progStlItemTkEstShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progStlItemTkEstShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progStlItemTkEstShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemTkEstShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            ProgStlItemTkEst progStlItemTkEst =new ProgStlItemTkEst();
            progStlItemTkEst.setTkcttPkid(strTkcttPkid);
            progStlItemTkEst.setTkcttItemPkid(progStlItemTkEstShowTemp.getTkctt_Pkid());
            progStlItemTkEst.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemTkEst> progStlItemTkEstList =
                    progStlItemTkEstService.selectRecordsByExample(progStlItemTkEst);
            if(progStlItemTkEstList.size()>0){
                progStlItemTkEst = progStlItemTkEstList.get(0);
                String strCreatedByName= ToolUtil.getUserName(progStlItemTkEst.getCreatedBy());
                String strLastUpdByName= ToolUtil.getUserName(progStlItemTkEst.getLastUpdBy());
                progStlItemTkEstShowTemp.setEng_Pkid(progStlItemTkEst.getPkid());
                progStlItemTkEstShowTemp.setEng_PeriodNo(progStlItemTkEst.getPeriodNo());
                progStlItemTkEstShowTemp.setEng_TkcttPkid(progStlItemTkEst.getTkcttPkid());
                progStlItemTkEstShowTemp.setEng_TkcttItemPkid(progStlItemTkEst.getTkcttItemPkid());
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEQty(progStlItemTkEst.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progStlItemTkEst.getCurrentPeriodQty());
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEQty(progStlItemTkEst.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progStlItemTkEst.getBeginToCurrentPeriodQty());
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progStlItemTkEstShowTemp.setEng_ArchivedFlag(progStlItemTkEst.getArchivedFlag());
                progStlItemTkEstShowTemp.setEng_CreatedBy(progStlItemTkEst.getCreatedBy());
                progStlItemTkEstShowTemp.setEng_CreatedByName(strCreatedByName);
                progStlItemTkEstShowTemp.setEng_CreatedTime(progStlItemTkEst.getCreatedTime());
                progStlItemTkEstShowTemp.setEng_LastUpdBy(progStlItemTkEst.getLastUpdBy());
                progStlItemTkEstShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progStlItemTkEstShowTemp.setEng_LastUpdTime(progStlItemTkEst.getLastUpdTime());
                progStlItemTkEstShowTemp.setEng_RecVersion(progStlItemTkEst.getRecVersion());
                if (ToolUtil.getBdIgnoreNull(progStlItemTkEstShowTemp.getEng_BeginToCurrentPeriodEQty())
                        .compareTo(progStlItemTkEstShowTemp.getTkctt_ContractQuantity())==0){
                    progStlItemTkEstShowTemp.setIsUptoCttQtyFlag(true);
                }
            }
            sprogStlItemTkEstShowListPara.add(progStlItemTkEstShowTemp) ;
            recursiveDataTable(progStlItemTkEstShowTemp.getTkctt_Pkid(), cttItemListPara, sprogStlItemTkEstShowListPara);
        }
    }

    /*����*/
    public void resetAction(){
        progStlItemTkEstShowSel =new ProgStlItemTkEstShow();
        progStlItemTkEstShowUpd =new ProgStlItemTkEstShow();
        progStlItemTkEstShowDel =new ProgStlItemTkEstShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progStlItemTkEstShowUpd.setEng_PeriodNo(progStlInfo.getPeriodNo());
                List<ProgStlItemTkEst> progStlItemTkEstListTemp =
                        progStlItemTkEstService.isExistInDb(progStlItemTkEstShowUpd);
                if (progStlItemTkEstListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (progStlItemTkEstListTemp.size() == 1) {
                    progStlItemTkEstShowUpd.setEng_Pkid(progStlItemTkEstListTemp.get(0).getPkid());
                    progStlItemTkEstService.updateRecord(progStlItemTkEstShowUpd);
                }
                if (progStlItemTkEstListTemp.size()==0){
				    progStlItemTkEstShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progStlItemTkEstShowUpd.setEng_TkcttItemPkid(progStlItemTkEstShowUpd.getTkctt_Pkid());
                    progStlItemTkEstService.insertRecord(progStlItemTkEstShowUpd);
                }
                MessageUtil.addInfo("����������ɡ�");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemTkEstShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("�ύ����ʧ�ܣ�",e);
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "����");
                    return false;
                }
                progStlItemTkEstShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                        MessageUtil.addError("���ڲ����ۼƹ�Ӧ����+���ڲ��Ϲ�Ӧ����>��ͬ��������ȷ��������ı��ڲ��Ϲ�Ӧ������"
                                + bDEng_CurrentPeriodEQtyTemp.toString() + "����");
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }

    private void delRecordAction(ProgStlItemTkEstShow progStlItemTkEstShowPara){
        try {
            if(progStlItemTkEstShowPara.getEng_Pkid()==null||
                    progStlItemTkEstShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
                int deleteRecordNum= progStlItemTkEstService.deleteRecord(progStlItemTkEstShowPara.getEng_Pkid()) ;
                if (deleteRecordNum<=0){
                    MessageUtil.addInfo("�ü�¼��ɾ����");
                }else {
                    MessageUtil.addInfo("ɾ��������ɡ�");
                }
            }
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara,ProgStlItemTkEstShow progStlItemTkEstShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){                
                progStlItemTkEstShowSel =(ProgStlItemTkEstShow)BeanUtils.cloneBean(progStlItemTkEstShowPara) ;
                progStlItemTkEstShowSel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkEstShowSel.getTkctt_StrNo()));
            }            
            String strTkctt_Unit= progStlItemTkEstShowPara.getTkctt_Unit();
            if(strTkctt_Unit==null) {
                if(strSubmitTypePara.equals("Upd")){
                    MessageUtil.addInfo("�����ݲ��������ݣ��޷�����");
                }
                else if(strSubmitTypePara.equals("Del")){
                    MessageUtil.addInfo("�����ݲ��������ݣ��޷�ɾ��");
                }
                resetAction();
                return;
            }
            if(strSubmitTypePara.equals("Upd")){
                progStlItemTkEstShowUpd =(ProgStlItemTkEstShow) BeanUtils.cloneBean(progStlItemTkEstShowPara) ;
                progStlItemTkEstShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkEstShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progStlItemTkEstShowDel =(ProgStlItemTkEstShow) BeanUtils.cloneBean(progStlItemTkEstShowPara) ;
                progStlItemTkEstShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkEstShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgStlItemTkEstShow> progStlItemTkEstShowListTemp =new ArrayList<ProgStlItemTkEstShow>();
        progStlItemTkEstShowListTemp.addAll(progStlItemTkEstShowList);

        progStlItemTkEstShowList.clear();
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

        // ���۽��С��
        BigDecimal bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
        // ���۽����
        BigDecimal bdBeginToCurrentPeriodEAmountAllTotal=new BigDecimal(0);
        // ���ڽ��С��
        BigDecimal bdCurrentPeriodEAmountTotal=new BigDecimal(0);
        // ���ڽ����
        BigDecimal bdCurrentPeriodEAmountAllTotal=new BigDecimal(0);

        ProgStlItemTkEstShow itemUnit=new ProgStlItemTkEstShow();
        ProgStlItemTkEstShow itemUnitNext=new ProgStlItemTkEstShow();

        for(int i=0;i< progStlItemTkEstShowListTemp.size();i++){
            itemUnit = progStlItemTkEstShowListTemp.get(i);
            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractQuantity()));
            bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractAmount()));
            bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractAmount()));

            bdBeginToCurrentPeriodEQtyTotal=
                    bdBeginToCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
            bdBeginToCurrentPeriodEQtyAllTotal=
                    bdBeginToCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
            bdCurrentPeriodEQtyTotal=
                    bdCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEQty()));
            bdCurrentPeriodEQtyAllTotal=
                    bdCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEQty()));

            bdBeginToCurrentPeriodEAmountTotal=
                    bdBeginToCurrentPeriodEAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEAmount()));
            bdBeginToCurrentPeriodEAmountAllTotal=
                    bdBeginToCurrentPeriodEAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEAmount()));
            bdCurrentPeriodEAmountTotal=
                    bdCurrentPeriodEAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEAmount()));
            bdCurrentPeriodEAmountAllTotal=
                    bdCurrentPeriodEAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEAmount()));

            progStlItemTkEstShowList.add(itemUnit);

            if(i+1< progStlItemTkEstShowListTemp.size()){
                itemUnitNext = progStlItemTkEstShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgStlItemTkEstShow itemOfEsItemHieRelapTemp=new ProgStlItemTkEstShow();
                    itemOfEsItemHieRelapTemp.setTkctt_Name("�ϼ�");
                    itemOfEsItemHieRelapTemp.setTkctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setTkctt_ContractQuantity(
                            ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                    itemOfEsItemHieRelapTemp.setTkctt_ContractAmount(
                            ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEAmount(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEAmount(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                    progStlItemTkEstShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progStlItemTkEstShowListTemp.size()){
                itemUnitNext = progStlItemTkEstShowListTemp.get(i);
                ProgStlItemTkEstShow progStlItemTkEstShowTemp =new ProgStlItemTkEstShow();
                progStlItemTkEstShowTemp.setTkctt_Name("�ϼ�");
                progStlItemTkEstShowTemp.setTkctt_Pkid("total"+i);
                progStlItemTkEstShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progStlItemTkEstShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progStlItemTkEstShowList.add(progStlItemTkEstShowTemp);
                progStlItemTkEstShowListForExcel.add(progStlItemTkEstShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // �ܺϼ�
                progStlItemTkEstShowTemp =new ProgStlItemTkEstShow();
                progStlItemTkEstShowTemp.setTkctt_Name("�ܺϼ�");
                progStlItemTkEstShowTemp.setTkctt_Pkid("total_all"+i);
                progStlItemTkEstShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progStlItemTkEstShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progStlItemTkEstShowList.add(progStlItemTkEstShowTemp);
                progStlItemTkEstShowListForExcel.add(progStlItemTkEstShowTemp);
            }
        }
    }

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgStlItemTkEstShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgStlItemTkEstShow> progStlItemTkEstShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgStlItemTkEstShow itemUnit: progStlItemTkEstShowListPara){
                if(itemUnit.getTkctt_Grade().equals(intBeforeGrade)){
                    if(strTemp.lastIndexOf(".")<0) {
                        strTemp=itemUnit.getTkctt_Orderid().toString();
                    }
                    else{
                        strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getTkctt_Orderid().toString();
                    }
                }
                else{
                    if(itemUnit.getTkctt_Grade()==1){
                        strTemp=itemUnit.getTkctt_Orderid().toString() ;
                    }
                    else {
                        if (!itemUnit.getTkctt_Grade().equals(intBeforeGrade)) {
                            if (itemUnit.getTkctt_Grade().compareTo(intBeforeGrade)>0) {
                                strTemp = strTemp + "." + itemUnit.getTkctt_Orderid().toString();
                            } else {
                                Integer intTemp=strTemp.indexOf(".",itemUnit.getTkctt_Grade()-1);
                                strTemp = strTemp .substring(0, intTemp);
                                strTemp = strTemp+"."+itemUnit.getTkctt_Orderid().toString();
                            }
                        }
                    }
                }
                intBeforeGrade=itemUnit.getTkctt_Grade() ;
                itemUnit.setTkctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getTkctt_Grade(), strTemp)) ;
            }
        }
        catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
        return progStlItemTkEstShowListPara;
    }

    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "�ܰ�����ͳ��-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"progStlItemTkEst.xls");
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
                    // ״̬��־������
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                }else if(strPowerType.equals("DoubleCheckFail")){
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
                    String strTemp = progStlInfoService.progStlInfoAppFailPreCheck(
                            EnumResType.RES_TYPE6.getCode(),
                            progStlInfo.getStlPkid(),
                            progStlInfo.getPeriodNo());
                    if (!"".equals(strTemp)) {
                        MessageUtil.addError(strTemp);
                        return;
                    }else{
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
            }
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /* �����ֶ�Start*/

    public ProgStlInfoShow getProgStlInfoShow() {
        return progStlInfoShow;
    }

    public void setProgStlInfoShow(ProgStlInfoShow progStlInfoShow) {
        this.progStlInfoShow = progStlInfoShow;
    }

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

    public ProgStlItemTkEstService getProgStlItemTkEstService() {
        return progStlItemTkEstService;
    }

    public void setProgStlItemTkEstService(ProgStlItemTkEstService ProgStlItemTkEstService) {
        this.progStlItemTkEstService = ProgStlItemTkEstService;
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

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public BigDecimal getbDEng_BeginToCurrentPeriodEQtyInDB() {
        return bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public void setbDEng_BeginToCurrentPeriodEQtyInDB(BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB) {
        this.bDEng_BeginToCurrentPeriodEQtyInDB = bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public ProgStlItemTkEstShow getProgStlItemTkEstShowSel() {
        return progStlItemTkEstShowSel;
    }

    public void setProgStlItemTkEstShowSel(ProgStlItemTkEstShow progStlItemTkEstShowSel) {
        this.progStlItemTkEstShowSel = progStlItemTkEstShowSel;
    }

    public ProgStlItemTkEstShow getProgStlItemTkEstShowDel() {
        return progStlItemTkEstShowDel;
    }

    public void setProgStlItemTkEstShowDel(ProgStlItemTkEstShow progStlItemTkEstShowDel) {
        this.progStlItemTkEstShowDel = progStlItemTkEstShowDel;
    }

    public ProgStlItemTkEstShow getProgStlItemTkEstShowUpd() {
        return progStlItemTkEstShowUpd;
    }

    public void setProgStlItemTkEstShowUpd(ProgStlItemTkEstShow progStlItemTkEstShowUpd) {
        this.progStlItemTkEstShowUpd = progStlItemTkEstShowUpd;
    }

    public List<ProgStlItemTkEstShow> getProgStlItemTkEstShowList() {
        return progStlItemTkEstShowList;
    }

    public void setProgStlItemTkEstShowList(List<ProgStlItemTkEstShow> progStlItemTkEstShowList) {
        this.progStlItemTkEstShowList = progStlItemTkEstShowList;
    }

    public List<ProgStlItemTkEstShow> getProgStlItemTkEstShowListForExcel() {
        return progStlItemTkEstShowListForExcel;
    }

    public void setProgStlItemTkEstShowListForExcel(List<ProgStlItemTkEstShow> progStlItemTkEstShowListForExcel) {
        this.progStlItemTkEstShowListForExcel = progStlItemTkEstShowListForExcel;
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
    /*�����ֶ�End*/
}
