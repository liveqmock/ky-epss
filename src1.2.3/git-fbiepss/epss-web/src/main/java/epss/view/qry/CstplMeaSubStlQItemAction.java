package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.JxlsManager;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.EsItemStlTkcttEngMea;
import epss.service.*;
import epss.service.EsFlowService;
import epss.service.EsQueryService;
import epss.view.flow.EsCommon;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class CstplMeaSubStlQItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplMeaSubStlQItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;

    /*�б���ʾ��*/
    private List<QryCSStlQShow> qryCSMeaSubQShowList;
    private List<QryCSStlQShow> qryCSMeaSubQShowListForExcel;

    private List<SelectItem> cstplList;

    private String strCstplPkid;
    private String strPeriodNo;

    // �����Ͽؼ�����ʾ����
    private ReportHeader reportHeader;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        reportHeader =new ReportHeader();
        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_Status(
                        ESEnum.ITEMTYPE1.getCode()
                       ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
        cstplList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","");
            cstplList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                cstplList.add(selectItem);
            }
        }
        strPeriodNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException,WriteException {
        if (this.qryCSMeaSubQShowListForExcel.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ɷּ�����������Ƚ�-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCSMeaSubQ.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    private void initData(String strBelongToPkid) {
        List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList=new ArrayList<EsItemStlTkcttEngMea>();

        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        EsCttInfo esInitCttCstpl= cttInfoService.getCttInfoByPkId(strBelongToPkid);
        reportHeader.setStrCstplId(esInitCttCstpl.getId());
        reportHeader.setStrCstplName(esInitCttCstpl.getName());
        beansMap.put("reportHeader", reportHeader);
        /*�ɱ��ƻ��б�*/
        List<EsCttItem> esCttItemListCstpl =new ArrayList<EsCttItem>();
        esCttItemListCstpl = cttItemService.getEsItemList(
                ESEnum.ITEMTYPE1.getCode(),
                strCstplPkid);
        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", esCttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListCstpl);

        String strMeaLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNoByEndPeriod(
                        ESEnum.ITEMTYPE7.getCode(),
                        esInitCttCstpl.getParentPkid(),
                        strPeriodNo));

        if(!ToolUtil.getStrIgnoreNull(strMeaLatestApprovedPeriodNo).equals("")){
            EsItemStlTkcttEngMea esItemStlTkcttEngMea=new EsItemStlTkcttEngMea();
            esItemStlTkcttEngMea.setTkcttPkid(esInitCttCstpl.getParentPkid());
            esItemStlTkcttEngMea.setPeriodNo(strMeaLatestApprovedPeriodNo);
            esItemStlTkcttEngMeaList= progMeaItemService.selectRecordsByPkidPeriodNoExample(esItemStlTkcttEngMea);
        }

        List<QryShow> qryShowList =esQueryService.getCSStlQList(strBelongToPkid, strPeriodNo);

        /*ƴװ�б�*/
        try {
            qryCSMeaSubQShowList =new ArrayList<QryCSStlQShow>();
            QryCSStlQShow itemCstplInsertItem;

            BigDecimal bdCstplContractUnitPrice=new BigDecimal(0);
            BigDecimal bdTkcttStlMeaQuantity=new BigDecimal(0);
            BigDecimal bdTkcttStlMeaAmount=new BigDecimal(0);
            BigDecimal bdCstplContractAmount=new BigDecimal(0);

            for(CttItemShow itemUnit: cttItemShowListCstpl){
                if(itemUnit.getUnit()!=null){
                    bdCstplContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
                }
                else{
                    bdCstplContractUnitPrice=itemUnit.getContractUnitPrice();
                }

                itemCstplInsertItem=new QryCSStlQShow();
                // �ɱ��ƻ�
                itemCstplInsertItem.setStrCstpl_Pkid(itemUnit.getPkid());
                itemCstplInsertItem.setStrCstpl_ParentPkid(itemUnit.getParentPkid());
                itemCstplInsertItem.setStrCstpl_No(itemUnit.getStrNo());
                itemCstplInsertItem.setStrCstpl_Name(itemUnit.getName());
                itemCstplInsertItem.setStrCstpl_Unit(itemUnit.getUnit());
                itemCstplInsertItem.setBdCstpl_ContractUnitPrice(bdCstplContractUnitPrice);

                // ͳ�Ƽ���
                for(EsItemStlTkcttEngMea esItemStlTkcttEngMea:esItemStlTkcttEngMeaList){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getStrCorrespondingItemPkid()).equals(
                            esItemStlTkcttEngMea.getTkcttItemPkid())){
                        itemCstplInsertItem.setBdTkcttStl_MeaQuantity(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        bdTkcttStlMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        bdTkcttStlMeaAmount=bdTkcttStlMeaQuantity.multiply(bdCstplContractUnitPrice);
                        itemCstplInsertItem.setBdTkcttStl_MeaAmount(bdTkcttStlMeaAmount);
                        break;
                    }
                }

                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttContractQuantityTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractUnitPriceTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractAmountTotal=new BigDecimal(0);
                // ���ݳɱ��ƻ����ӷְ���ͬ��
                for(int i=0;i< qryShowList.size();i++) {
                    // �ɱ��ƻ�������Ŀ��ְ���ͬ��
                    if(itemUnit.getPkid().equals(qryShowList.get(i).getStrCorrespondingPkid())) {
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // ��¡Ŀ����д������
                        QryCSStlQShow qryCSStlQShowNewInsert =(QryCSStlQShow)BeanUtils.cloneBean(itemCstplInsertItem);
                        // Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        BigDecimal bdSubcttBeginToCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdUnitPrice());
                        BigDecimal bdSubcttBeginToCurrentPeriodAmount=bdSubcttBeginToCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);
                        // �ۼ�Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttBeginToCurrentPeriodQuantity);
                        bdSubcttContractUnitPriceTotal=bdSubcttContractUnitPriceTotal.add(bdSubcttContractUnitPrice);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttBeginToCurrentPeriodAmount);

                        // �ְ���ͬ
                        qryCSStlQShowNewInsert.setStrSubctt_SignPartName(qryShowList.get(i).getStrName());
                        qryCSStlQShowNewInsert.setStrCstpl_Pkid(qryShowList.get(i).getStrCorrespondingPkid()
                                + "/" + intGroup.toString());
                        //qryCSStlQShowNewInsert.setStrCstpl_ParentPkid(itemUnit.getParentPkid());
                        qryCSStlQShowNewInsert.setBdSubctt_ContractUnitPrice(bdSubcttContractUnitPrice);

                        // �ְ�����
                        qryCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodQQty(bdSubcttBeginToCurrentPeriodQuantity);
                        qryCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodMAmount(bdSubcttBeginToCurrentPeriodAmount);

                        if(intGroup>1){
                            qryCSStlQShowNewInsert.setStrCstpl_No("");
                            qryCSStlQShowNewInsert.setStrCstpl_Name("");
                            qryCSStlQShowNewInsert.setStrCstpl_Unit("");
                            qryCSStlQShowNewInsert.setBdCstpl_ContractUnitPrice(null);
                            qryCSStlQShowNewInsert.setBdTkcttStl_MeaQuantity(null);
                            qryCSStlQShowNewInsert.setBdTkcttStl_MeaAmount(null);
                        }

                        // ���һ��֮ǰ����
                        if(i< qryShowList.size()-1){
                            // ��һ������Ŀ��ְ���ͬ��
                            if(itemUnit.getPkid().equals(qryShowList.get(i+1).getStrCorrespondingPkid())){
                                // �ɱ��ƻ����趨
                                qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                            }// ��һ���Ŀ��ְ���ͬ��
                            else{
                                // �ܰ�������ְ�����ֵ��
                                qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlMeaQuantity).subtract(bdSubcttContractQuantityTotal));
                                if(intGroup==1){
                                    qryCSStlQShowNewInsert.setBdMeaS_ContractUnitPrice(
                                            ToolUtil.getBdIgnoreNull(bdCstplContractUnitPrice).subtract(bdSubcttContractUnitPriceTotal));
                                }
                                qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodMAmount(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlMeaAmount.subtract(bdSubcttContractAmountTotal)));
                                qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                                break;
                            }
                        }else{
                            // �ܰ�������ְ�����ֵ��
                            qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(bdTkcttStlMeaQuantity.subtract(bdSubcttContractQuantityTotal));
                            qryCSStlQShowNewInsert.setBdMeaS_ContractUnitPrice(bdCstplContractUnitPrice.subtract(bdSubcttContractUnitPriceTotal));
                            qryCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodMAmount(bdCstplContractAmount.subtract(bdSubcttContractAmountTotal));
                            qryCSMeaSubQShowList.add(qryCSStlQShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryCSMeaSubQShowList.add(itemCstplInsertItem);
                }
            }
            qryCSMeaSubQShowListForExcel =new ArrayList<QryCSStlQShow>();
            for(QryCSStlQShow itemUnit: qryCSMeaSubQShowList){
                QryCSStlQShow itemUnitTemp= (QryCSStlQShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrCstpl_No(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCstpl_No()));
                qryCSMeaSubQShowListForExcel.add(itemUnitTemp);
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }

        if(qryCSMeaSubQShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryCSMeaSubQShowListForExcel", qryCSMeaSubQShowListForExcel);
    }

    /*�ݹ�����*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<CttItemShow> cttItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        EsCttItem esCttItem =null;
        for(EsCttItem itemUnit: subEsCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= ToolUtil.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= ToolUtil.getUserName(itemUnit.getLastUpdBy());
            cttItemShowTemp = new CttItemShow(
                    itemUnit.getPkid(),
                    itemUnit.getBelongToType(),
                    itemUnit.getBelongToPkid(),
                    itemUnit.getParentPkid(),
                    itemUnit.getGrade(),
                    itemUnit.getOrderid(),
                    itemUnit.getName(),
                    itemUnit.getUnit(),
                    itemUnit.getContractUnitPrice(),
                    itemUnit.getContractQuantity(),
                    itemUnit.getContractAmount(),
                    itemUnit.getSignPartAPrice(),
                    itemUnit.getDeletedFlag() ,
                    itemUnit.getOriginFlag() ,
                    itemUnit.getCreatedBy() ,
                    strCreatedByName,
                    itemUnit.getCreatedDate() ,
                    itemUnit.getLastUpdBy() ,
                    strLastUpdByName,
                    itemUnit.getLastUpdDate() ,
                    itemUnit.getModificationNum(),
                    itemUnit.getNote(),
                    itemUnit.getCorrespondingPkid(),
                    "",
                    ""
                );
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara, cttItemShowListPara);
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
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

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<CttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CttItemShow itemUnit: cttItemShowListPara){
            if(itemUnit.getGrade()==intBeforeGrade){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getOrderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getOrderid().toString();
                }
            }
            else{
                if(itemUnit.getGrade()==1){
                    strTemp=itemUnit.getOrderid().toString() ;
                }
                else {
                    if (!itemUnit.getGrade().equals(intBeforeGrade)) {
                        if (itemUnit.getGrade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getOrderid().toString();
                        } else {
                            Integer intTemp=ToolUtil.lookIndex(strTemp,'.',itemUnit.getGrade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getOrderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getGrade() ;
            itemUnit.setStrNo(ToolUtil.padLeft_DoLevel(itemUnit.getGrade(), strTemp)) ;
        }
        return cttItemShowListPara;
    }

    public void onQueryAction() {
        try {
            if(ToolUtil.getStrIgnoreNull(strCstplPkid).equals("")){
                MessageUtil.addWarn("��ѡ��ɱ��ƻ��");
                return;
            }
            // StickyHeader��ƴװ��ͷƽ�ֿ�ȣ������趨��ȣ������Բ��ã���ʱͣ��
            initData(strCstplPkid);
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    /*�����ֶ�Start*/

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public List<QryCSStlQShow> getQryCSMeaSubQShowListForExcel() {
        return qryCSMeaSubQShowListForExcel;
    }

    public void setQryCSMeaSubQShowListForExcel(List<QryCSStlQShow> qryCSMeaSubQShowListForExcel) {
        this.qryCSMeaSubQShowListForExcel = qryCSMeaSubQShowListForExcel;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public String getStrCstplPkid() {
        return strCstplPkid;
    }

    public void setStrCstplPkid(String strCstplPkid) {
        this.strCstplPkid = strCstplPkid;
    }

    public String getStrPeriodNo() {
        return strPeriodNo;
    }

    public void setStrPeriodNo(String strPeriodNo) {
        this.strPeriodNo = strPeriodNo;
    }

    public List<SelectItem> getCstplList() {
        return cstplList;
    }

    public void setCstplList(List<SelectItem> cstplList) {
        this.cstplList = cstplList;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    public List<QryCSStlQShow> getQryCSMeaSubQShowList() {
        return qryCSMeaSubQShowList;
    }

    public void setQryCSMeaSubQShowList(List<QryCSStlQShow> qryCSMeaSubQShowList) {
        this.qryCSMeaSubQShowList = qryCSMeaSubQShowList;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService progMeaItemService) {
        this.progMeaItemService = progMeaItemService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    /*�����ֶ�End*/
}