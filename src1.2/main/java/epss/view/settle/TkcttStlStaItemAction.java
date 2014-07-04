package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CommStlSubcttEngH;
import epss.repository.model.model_show.ProgEstStaItemShow;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.*;
import epss.common.utils.JxlsManager;
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
public class TkcttStlStaItemAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttStlStaItemAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esInitCustService}")
    private EsInitCustService esInitCustService;
    @ManagedProperty(value = "#{esInitStlService}")
    private EsInitStlService esInitStlService;
    @ManagedProperty(value = "#{esItemStlTkcttEngStaService}")
    private EsItemStlTkcttEngStaService esItemStlTkcttEngStaService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<ProgEstStaItemShow> progEstStaItemShowList;
    private ProgEstStaItemShow progEstStaItemShow;
    private ProgEstStaItemShow progEstStaItemShowUpd;
    private ProgEstStaItemShow progEstStaItemShowDel;
    private ProgEstStaItemShow progEstStaItemShowSelected;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private CommStlSubcttEngH commStlSubcttEngH;

    /*������*/
    private String strEsInitStlSubcttEng;
    private String strTkcttPkid;
    private String strLatestApprovedPeriodNo;
    private EsInitStl esInitStl;

    private String strSubmitType;
    private String strMngNotFinishFlag;

    private Map beansMap;
    // �����Ͽؼ�����ʾ����
    private String strExportToExcelRendered;
    private List<ProgEstStaItemShow> progEstItemShowListForExcel;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strEsInitStlSubcttEng")){
            strEsInitStlSubcttEng=parammap.get("strEsInitStlSubcttEng").toString();
            this.esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEng);
            strTkcttPkid= this.esInitStl.getStlPkid();
        }

        strLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNo(ESEnum.ITEMTYPE6.getCode(),strTkcttPkid));

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

        /*�ְ���ͬ����*/
        // From StlPkid To SubcttPkid
        EsInitStl esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEng);
        commStlSubcttEngH.setStrSubcttPkid(esInitStl.getStlPkid());
        commStlSubcttEngH.setStrStlId(esInitStl.getId());
        // From SubcttPkid To CstplPkid
        EsCttInfo esCttInfo_Subctt=esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrSubcttPkid());
        commStlSubcttEngH.setStrCstplPkid(esCttInfo_Subctt.getParentPkid());
        commStlSubcttEngH.setStrSubcttId(esCttInfo_Subctt.getId());
        commStlSubcttEngH.setStrSubcttName(esCttInfo_Subctt.getName());
        commStlSubcttEngH.setStrSignPartPkid(esCttInfo_Subctt.getSignPartB());
        commStlSubcttEngH.setStrSignPartName(esInitCustService.getEsInitCustByPkid(
                commStlSubcttEngH.getStrSignPartPkid()).getName());

        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
    }
    /*����*/
    public void resetAction(){
        progEstStaItemShow =new ProgEstStaItemShow();
        progEstStaItemShowUpd =new ProgEstStaItemShow();
        progEstStaItemShowDel =new ProgEstStaItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progEstStaItemShowUpd.setEng_PeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaListTemp =
                        esItemStlTkcttEngStaService.isExistInDb(progEstStaItemShowUpd);
                if (esItemStlTkcttEngStaListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (esItemStlTkcttEngStaListTemp.size() == 1) {
                    progEstStaItemShowUpd.setEng_Pkid(esItemStlTkcttEngStaListTemp.get(0).getPkid());
                    esItemStlTkcttEngStaService.updateRecord(progEstStaItemShowUpd);
                }
                if (esItemStlTkcttEngStaListTemp.size()==0){
				    progEstStaItemShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progEstStaItemShowUpd.setEng_TkcttItemPkid(progEstStaItemShowUpd.getTkctt_Pkid());
                    esItemStlTkcttEngStaService.insertRecord(progEstStaItemShowUpd);
                }
                MessageUtil.addInfo("����������ɡ�");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progEstStaItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("�ύ����ʧ�ܣ�",e);
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progEstStaItemShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progEstStaItemShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "����");
                    return false;
                }
                progEstStaItemShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_BeginToCurrentPeriodEQty());

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

    private void delRecordAction(ProgEstStaItemShow progEstStaItemShowPara){
        try {
            if(progEstStaItemShowPara.getEng_Pkid()==null||
                    progEstStaItemShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
                int deleteRecordNum=esItemStlTkcttEngStaService.deleteRecord(progEstStaItemShowPara.getEng_Pkid()) ;
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
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){                
                progEstStaItemShow =(ProgEstStaItemShow)BeanUtils.cloneBean(progEstStaItemShowSelected) ;
                progEstStaItemShow.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstStaItemShow.getTkctt_StrNo()));
            }            
            String strTkctt_Unit= progEstStaItemShowSelected.getTkctt_Unit();
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
                progEstStaItemShowUpd =(ProgEstStaItemShow) BeanUtils.cloneBean(progEstStaItemShowSelected) ;
                progEstStaItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstStaItemShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progEstStaItemShowUpd.getEng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progEstStaItemShowDel =(ProgEstStaItemShow) BeanUtils.cloneBean(progEstStaItemShowSelected) ;
                progEstStaItemShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstStaItemShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*��ʼ������*/
    private void initData() {
        /*�ְ���ͬ*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        progEstItemShowListForExcel=new ArrayList<ProgEstStaItemShow>();
        esCttItemList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(), strTkcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progEstStaItemShowList =new ArrayList<ProgEstStaItemShow>();
        recursiveDataTable("root", esCttItemList, progEstStaItemShowList);
        progEstStaItemShowList =getItemStlTkcttEngSMList_DoFromatNo(progEstStaItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progEstItemShowListForExcel", progEstItemShowListForExcel);
        // �������趨
        if(progEstStaItemShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*�������ݿ��в㼶��ϵ�����б��õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<ProgEstStaItemShow> sprogEstStaItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgEstStaItemShow progEstStaItemShowTemp = new ProgEstStaItemShow();
            progEstStaItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progEstStaItemShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progEstStaItemShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progEstStaItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progEstStaItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progEstStaItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progEstStaItemShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progEstStaItemShowTemp.setTkctt_Name(itemUnit.getName());
            progEstStaItemShowTemp.setTkctt_Note(itemUnit.getNote());
            progEstStaItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progEstStaItemShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progEstStaItemShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progEstStaItemShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            EsItemStlTkcttEngSta esItemStlTkcttEngSta=new EsItemStlTkcttEngSta();
            esItemStlTkcttEngSta.setTkcttPkid(strTkcttPkid);
            esItemStlTkcttEngSta.setTkcttItemPkid(progEstStaItemShowTemp.getTkctt_Pkid());
            esItemStlTkcttEngSta.setPeriodNo(esInitStl.getPeriodNo());
            List<EsItemStlTkcttEngSta> esItemStlTkcttEngStaList =
                    esItemStlTkcttEngStaService.selectRecordsByExample(esItemStlTkcttEngSta);
            if(esItemStlTkcttEngStaList.size()>0){
                esItemStlTkcttEngSta= esItemStlTkcttEngStaList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(esItemStlTkcttEngSta.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(esItemStlTkcttEngSta.getLastUpdBy());
                progEstStaItemShowTemp.setEng_Pkid(esItemStlTkcttEngSta.getPkid());
                progEstStaItemShowTemp.setEng_PeriodNo(esItemStlTkcttEngSta.getPeriodNo());
                progEstStaItemShowTemp.setEng_TkcttPkid(esItemStlTkcttEngSta.getTkcttPkid());
                progEstStaItemShowTemp.setEng_TkcttItemPkid(esItemStlTkcttEngSta.getTkcttItemPkid());
                progEstStaItemShowTemp.setEng_CurrentPeriodEQty(esItemStlTkcttEngSta.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngSta.getCurrentPeriodQty());
                progEstStaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(esItemStlTkcttEngSta.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngSta.getBeginToCurrentPeriodQty());
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progEstStaItemShowTemp.setEng_DeletedFlag(esItemStlTkcttEngSta.getDeleteFlag());
                progEstStaItemShowTemp.setEng_CreatedBy(esItemStlTkcttEngSta.getCreatedBy());
                progEstStaItemShowTemp.setEng_CreatedByName(strCreatedByName);
                progEstStaItemShowTemp.setEng_CreatedDate(esItemStlTkcttEngSta.getCreatedDate());
                progEstStaItemShowTemp.setEng_LastUpdBy(esItemStlTkcttEngSta.getLastUpdBy());
                progEstStaItemShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progEstStaItemShowTemp.setEng_LastUpdDate(esItemStlTkcttEngSta.getLastUpdDate());
                progEstStaItemShowTemp.setEng_ModificationNum(esItemStlTkcttEngSta.getModificationNum());
            }
            sprogEstStaItemShowListPara.add(progEstStaItemShowTemp) ;
            recursiveDataTable(progEstStaItemShowTemp.getTkctt_Pkid(), esCttItemListPara, sprogEstStaItemShowListPara);
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgEstStaItemShow> progEstStaItemShowListTemp =new ArrayList<ProgEstStaItemShow>();
        progEstStaItemShowListTemp.addAll(progEstStaItemShowList);

        progEstStaItemShowList.clear();
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

        ProgEstStaItemShow itemUnit=new ProgEstStaItemShow();
        ProgEstStaItemShow itemUnitNext=new ProgEstStaItemShow();

        for(int i=0;i< progEstStaItemShowListTemp.size();i++){
            itemUnit = progEstStaItemShowListTemp.get(i);
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

            progEstStaItemShowList.add(itemUnit);

            if(i+1< progEstStaItemShowListTemp.size()){
                itemUnitNext = progEstStaItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgEstStaItemShow itemOfEsItemHieRelapTemp=new ProgEstStaItemShow();
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
                    progEstStaItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progEstStaItemShowListTemp.size()){
                itemUnitNext = progEstStaItemShowListTemp.get(i);
                ProgEstStaItemShow progEstStaItemShowTemp =new ProgEstStaItemShow();
                progEstStaItemShowTemp.setTkctt_Name("�ϼ�");
                progEstStaItemShowTemp.setTkctt_Pkid("total"+i);
                progEstStaItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progEstStaItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progEstStaItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progEstStaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progEstStaItemShowList.add(progEstStaItemShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // �ܺϼ�
                progEstStaItemShowTemp =new ProgEstStaItemShow();
                progEstStaItemShowTemp.setTkctt_Name("�ܺϼ�");
                progEstStaItemShowTemp.setTkctt_Pkid("total_all"+i);
                progEstStaItemShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progEstStaItemShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progEstStaItemShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progEstStaItemShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progEstStaItemShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progEstStaItemShowList.add(progEstStaItemShowTemp);
            }
        }
    }

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgEstStaItemShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgEstStaItemShow> progEstStaItemShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgEstStaItemShow itemUnit: progEstStaItemShowListPara){
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

                ProgEstStaItemShow itemUnitTemp= (ProgEstStaItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progEstItemShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
        return progEstStaItemShowListPara;
    }


    public String onExportExcel()throws IOException, WriteException {
        if (this.progEstStaItemShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ�����ͳ������-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlTkcttEngSta.xls");
            // ����״̬��Ʊ����Ҫ����ʱ���޸ĵ����ļ���
        }
        return null;
    }

    /*�������ݿ��в㼶��ϵ�����б��õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<EsCttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
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

    public EsItemStlTkcttEngStaService getEsItemStlTkcttEngStaService() {
        return esItemStlTkcttEngStaService;
    }

    public void setEsItemStlTkcttEngStaService(EsItemStlTkcttEngStaService EsItemStlTkcttEngStaService) {
        this.esItemStlTkcttEngStaService = EsItemStlTkcttEngStaService;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsInitStlService getEsInitStlService() {
        return esInitStlService;
    }

    public void setEsInitStlService(EsInitStlService esInitStlService) {
        this.esInitStlService = esInitStlService;
    }
    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public CommStlSubcttEngH getCommStlSubcttEngH() {
        return commStlSubcttEngH;
    }

    public void setCommStlSubcttEngH(CommStlSubcttEngH commStlSubcttEngH) {
        this.commStlSubcttEngH = commStlSubcttEngH;
    }

    public EsInitCustService getEsInitCustService() {
        return esInitCustService;
    }

    public void setEsInitCustService(EsInitCustService esInitCustService) {
        this.esInitCustService = esInitCustService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }
    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public BigDecimal getbDEng_BeginToCurrentPeriodEQtyInDB() {
        return bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public void setbDEng_BeginToCurrentPeriodEQtyInDB(BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB) {
        this.bDEng_BeginToCurrentPeriodEQtyInDB = bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public ProgEstStaItemShow getProgEstStaItemShow() {
        return progEstStaItemShow;
    }

    public void setProgEstStaItemShow(ProgEstStaItemShow progEstStaItemShow) {
        this.progEstStaItemShow = progEstStaItemShow;
    }

    public ProgEstStaItemShow getProgEstStaItemShowDel() {
        return progEstStaItemShowDel;
    }

    public void setProgEstStaItemShowDel(ProgEstStaItemShow progEstStaItemShowDel) {
        this.progEstStaItemShowDel = progEstStaItemShowDel;
    }

    public ProgEstStaItemShow getProgEstStaItemShowSelected() {
        return progEstStaItemShowSelected;
    }

    public void setProgEstStaItemShowSelected(ProgEstStaItemShow progEstStaItemShowSelected) {
        this.progEstStaItemShowSelected = progEstStaItemShowSelected;
    }

    public ProgEstStaItemShow getProgEstStaItemShowUpd() {
        return progEstStaItemShowUpd;
    }

    public void setProgEstStaItemShowUpd(ProgEstStaItemShow progEstStaItemShowUpd) {
        this.progEstStaItemShowUpd = progEstStaItemShowUpd;
    }

    public List<ProgEstStaItemShow> getProgEstStaItemShowList() {
        return progEstStaItemShowList;
    }

    public void setProgEstStaItemShowList(List<ProgEstStaItemShow> progEstStaItemShowList) {
        this.progEstStaItemShowList = progEstStaItemShowList;
    }

    public List<ProgEstStaItemShow> getProgEstItemShowListForExcel() {
        return progEstItemShowListForExcel;
    }

    public void setProgEstItemShowListForExcel(List<ProgEstStaItemShow> progEstItemShowListForExcel) {
        this.progEstItemShowListForExcel = progEstItemShowListForExcel;
    }

    /*�����ֶ�End*/
}