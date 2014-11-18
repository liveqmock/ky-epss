package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.CttItem;
import skyline.util.JxlsManager;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.CttInfo;
import epss.service.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class CstplSubcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplSubcttItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    /*�б���ʾ��*/
    private List<QryCSShow> qryCSShowList;
    private List<QryCSShow> qryCSShowListForExcel;

    private List<SelectItem> cstplList;

    private CttItem cttItem;

    // �����Ͽؼ�����ʾ����
    private ReportHeader reportHeader;
    private String strExportToExcelRendered;
    private Map beansMap;
    @PostConstruct
    public void init() {
        try {
            beansMap = new HashMap();
            reportHeader =new ReportHeader();
            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getCttInfoListByCttType_Status(
                            EnumResType.RES_TYPE1.getCode()
                            , EnumFlowStatus.FLOW_STATUS3.getCode());
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
            cttItem =new CttItem();
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.qryCSShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ɷֱȽ�-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCS.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    private void initData(String strBelongToPkid) {
        CttInfo cttInfo = cttInfoService.getCttInfoByPkId(strBelongToPkid);
        reportHeader.setStrCstplId(cttInfo.getId());
        reportHeader.setStrCstplName(cttInfo.getName());
        beansMap.put("reportHeader", reportHeader);
        /*�ɱ��ƻ��б�*/
        List<CttItem> cttItemListCstpl =new ArrayList<CttItem>();
        cttItemListCstpl = cttItemService.getEsItemList(
                EnumResType.RES_TYPE1.getCode(),
                cttItem.getBelongToPkid());
        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", cttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListCstpl);

        List<QryShow> qryShowList =esQueryService.getCSList("2",strBelongToPkid);

        /*ƴװ�б�*/
        try {
            qryCSShowList =new ArrayList<QryCSShow>();
            QryCSShow itemCstplInsertItem;
            BigDecimal bdCstplContractQuantity=null;
            BigDecimal bdCstplContractUnitPrice=null;
            BigDecimal bdCstplContractAmount=null;
            for(CttItemShow itemUnit: cttItemShowListCstpl){
                bdCstplContractQuantity=itemUnit.getContractQuantity();
                bdCstplContractUnitPrice=itemUnit.getContractUnitPrice();
                bdCstplContractAmount=itemUnit.getContractAmount();
                itemCstplInsertItem=new QryCSShow();
                itemCstplInsertItem.setStrCstpl_Pkid(itemUnit.getPkid());
                itemCstplInsertItem.setStrCstpl_ParentPkid(itemUnit.getParentPkid());
                itemCstplInsertItem.setStrCstpl_No(itemUnit.getStrNo());
                itemCstplInsertItem.setStrCstpl_Name(itemUnit.getName());
                itemCstplInsertItem.setStrCstpl_Unit(itemUnit.getUnit());
                itemCstplInsertItem.setBdCstpl_ContractQuantity(bdCstplContractQuantity);
                itemCstplInsertItem.setBdCstpl_ContractUnitPrice(bdCstplContractUnitPrice);
                itemCstplInsertItem.setBdCstpl_ContractAmount(bdCstplContractAmount);
                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttContractQuantityTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractUnitPriceTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractAmountTotal=new BigDecimal(0);
                // ���ݳɱ��ƻ����ӷְ���ͬ��
                for(int i=0;i< qryShowList.size();i++) {
                    // �ɱ��ƻ�������Ŀ��ְ���ͬ��
                    QryShow qryShowTemp=qryShowList.get(i);
                    if(itemUnit.getPkid().equals(qryShowTemp.getStrCorrespondingPkid())) {
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // ��¡Ŀ����д������
                        QryCSShow qryCSShowNewInsert =(QryCSShow)BeanUtils.cloneBean(itemCstplInsertItem);
                        // Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        BigDecimal bdSubcttContractQuantity=ToolUtil.getBdIgnoreNull(qryShowTemp.getBdQuantity());
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(qryShowTemp.getBdUnitPrice());
                        BigDecimal bdSubcttContractAmount=ToolUtil.getBdIgnoreNull(qryShowTemp.getBdAmount());
                        // �ۼ�Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttContractQuantity);
                        bdSubcttContractUnitPriceTotal=bdSubcttContractUnitPriceTotal.add(bdSubcttContractUnitPrice);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttContractAmount);

                        // �ְ���ͬ
                        qryCSShowNewInsert.setBdSubctt_ContractQuantity(bdSubcttContractQuantity);
                        qryCSShowNewInsert.setBdSubctt_ContractUnitPrice(bdSubcttContractUnitPrice);
                        qryCSShowNewInsert.setBdSubctt_ContractAmount(bdSubcttContractAmount);
                        qryCSShowNewInsert.setStrSubctt_Name(qryShowTemp.getStrName());
                        qryCSShowNewInsert.setStrSubctt_SignPartName(qryShowTemp.getStrSignPartName());
                        qryCSShowNewInsert.setStrCstpl_Pkid(qryShowTemp.getStrCorrespondingPkid() + "/" + intGroup.toString());
                        qryCSShowNewInsert.setStrCstpl_ParentPkid(itemUnit.getParentPkid());

                        if(intGroup>1){
                            qryCSShowNewInsert.setStrCstpl_No("");
                            qryCSShowNewInsert.setStrCstpl_Name("");
                            qryCSShowNewInsert.setStrCstpl_Unit("");
                            qryCSShowNewInsert.setBdCstpl_ContractQuantity(null);
                            qryCSShowNewInsert.setBdCstpl_ContractUnitPrice(null);
                            qryCSShowNewInsert.setBdCstpl_ContractAmount(null);
                        }

                        // ���һ��֮ǰ����
                        if(i< qryShowList.size()-1){
                            // ��һ������Ŀ��ְ���ͬ��
                            if(itemUnit.getPkid().equals(qryShowList.get(i+1).getStrCorrespondingPkid())){
                                // �ɱ��ƻ����趨
                                qryCSShowList.add(qryCSShowNewInsert);
                            }// ��һ���Ŀ��ְ���ͬ��
                            else{
                                // �ɷ�ֵ��
                                qryCSShowNewInsert.setBdC_S_ContractQuantity(
                                        ToolUtil.getBdIgnoreNull(bdCstplContractQuantity).subtract(bdSubcttContractQuantityTotal));
                                if(intGroup==1){
                                    qryCSShowNewInsert.setBdC_S_ContractUnitPrice(
                                            ToolUtil.getBdIgnoreNull(bdCstplContractUnitPrice).subtract(bdSubcttContractUnitPriceTotal));
                                }
                                if(bdCstplContractAmount!=null) {
                                qryCSShowNewInsert.setBdC_S_ContractAmount(
                                        ToolUtil.getBdIgnoreNull(bdCstplContractAmount.subtract(bdSubcttContractAmountTotal)));
                                }
                                qryCSShowList.add(qryCSShowNewInsert);
                                break;
                            }
                        }else{
                            // �ɷ�ֵ��
                            qryCSShowNewInsert.setBdC_S_ContractQuantity(bdCstplContractQuantity.subtract(bdSubcttContractQuantityTotal));
                            if(intGroup==1) {
                                qryCSShowNewInsert.setBdC_S_ContractUnitPrice(bdCstplContractUnitPrice.subtract(bdSubcttContractUnitPriceTotal));
                            }
                            if(bdCstplContractAmount!=null) {
                                qryCSShowNewInsert.setBdC_S_ContractAmount(bdCstplContractAmount.subtract(bdSubcttContractAmountTotal));
                            }
                            qryCSShowList.add(qryCSShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryCSShowList.add(itemCstplInsertItem);
                }
            }

            // ��Ӻϼ�
            setItemOfCSForQueryList_AddTotal();

            if(qryCSShowList.size()>0){
                strExportToExcelRendered="true";
            }else{
                strExportToExcelRendered="false";
            }

            beansMap.put("qryCSShowList", qryCSShowList);

            qryCSShowListForExcel =new ArrayList<QryCSShow>();
            for(QryCSShow itemUnit: qryCSShowList){
                QryCSShow itemUnitTemp= (QryCSShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrCstpl_No(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCstpl_No()));
                qryCSShowListForExcel.add(itemUnitTemp);
            }
            beansMap.put("qryCSShowListForExcel", qryCSShowListForExcel);
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }
    private void setItemOfCSForQueryList_AddTotal(){
        List<QryCSShow> qryCSShowListTemp =new ArrayList<QryCSShow>();
        qryCSShowListTemp.addAll(qryCSShowList);

        qryCSShowList.clear();
        // С��
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        BigDecimal bdTotalContrast=new BigDecimal(0);
        BigDecimal bdAllTotalContrast=new BigDecimal(0);

        QryCSShow itemUnit=new QryCSShow();
        QryCSShow itemUnitNext=new QryCSShow();
        for(int i=0;i< qryCSShowListTemp.size();i++){
            itemUnit = qryCSShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getBdCstpl_ContractAmount()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getBdCstpl_ContractAmount()));
            // ����
           /* bdTotalContrast=bdTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));
            bdAllTotalContrast=bdAllTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));*/
            qryCSShowList.add(itemUnit);

            if(i+1< qryCSShowListTemp.size()){
                itemUnitNext = qryCSShowListTemp.get(i+1);
                QryCSShow qryCSShowTemp =new QryCSShow();
                Boolean isRoot=false;
                if(!("".equals(itemUnitNext.getStrCstpl_No()))&&itemUnitNext.getStrCstpl_ParentPkid().equals("root")){
                    qryCSShowTemp.setStrCstpl_Name("�ϼ�");
                    qryCSShowTemp.setStrCstpl_Pkid("total"+i);
                    qryCSShowTemp.setBdCstpl_ContractAmount(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

               /* if(itemUnitNext.getParentPkidContrast()!=null && itemUnitNext.getParentPkidContrast().equals("root")){
                    qryCSShowTemp.setStrPkid("total"+i);
                    qryCSShowTemp.setStrNameContrast("�ϼ�");
                    qryCSShowTemp.setPkidContrast("total_contrast"+i);
                    qryCSShowTemp.setContractAmountContrast(bdTotalContrast);
                    bdTotalContrast = new BigDecimal(0);
                    isRoot=true;
                }*/

                if(isRoot.equals(true)){
                    qryCSShowList.add(qryCSShowTemp);
                }

            } else if(i+1== qryCSShowListTemp.size()){
                itemUnitNext = qryCSShowListTemp.get(i);
                QryCSShow qryCSShowTemp =new QryCSShow();
                qryCSShowTemp.setStrCstpl_Name("�ϼ�");
                qryCSShowTemp.setStrCstpl_Pkid("total" + i);
                qryCSShowTemp.setBdCstpl_ContractAmount(bdTotal);

               /* qryCSShowTemp.setNameContrast("�ϼ�");
                qryCSShowTemp.setPkidContrast("total_contrast"+i);
                qryCSShowTemp.setContractAmountContrast(bdTotalContrast);*/
                qryCSShowList.add(qryCSShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalContrast = new BigDecimal(0);

                // �ܺϼ�
                qryCSShowTemp =new QryCSShow();
                qryCSShowTemp.setStrCstpl_Name("�ܺϼ�");
                qryCSShowTemp.setStrCstpl_Pkid("total_all" + i);
                qryCSShowTemp.setBdCstpl_ContractAmount(bdAllTotal);

              /*  qryCSShowTemp.setNameContrast("�ܺϼ�");
                qryCSShowTemp.setPkidContrast("total_all_contrast"+i);
                qryCSShowTemp.setContractAmountContrast(bdAllTotalContrast);*/
                qryCSShowList.add(qryCSShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalContrast = new BigDecimal(0);
            }
        }
    }

    /*�ݹ�����*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<CttItemShow> cttItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // ͨ������id�������ĺ���
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= ToolUtil.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= ToolUtil.getUserName(itemUnit.getLastUpdBy());
                // �㼶��
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
                    itemUnit.getArchivedFlag() ,
                    itemUnit.getOriginFlag() ,
                    itemUnit.getCreatedBy() ,
                    strCreatedByName,
                    itemUnit.getCreatedTime() ,
                    itemUnit.getLastUpdBy() ,
                    strLastUpdByName,
                    itemUnit.getLastUpdTime() ,
                    itemUnit.getRecVersion(),
                    itemUnit.getRemark(),
                    itemUnit.getCorrespondingPkid(),
                    "",
                    ""
                );
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), cttItemListPara, cttItemShowListPara);
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
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
            if(ToolUtil.getStrIgnoreNull(cttItem.getBelongToPkid()).equals("")){
                MessageUtil.addWarn("��ѡ��ɱ��ƻ��");
                return;
            }
            initData(cttItem.getBelongToPkid());
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    /*�����ֶ�Start*/
    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
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

    public List<QryCSShow> getQryCSShowList() {
        return qryCSShowList;
    }

    public void setQryCSShowList(List<QryCSShow> qryCSShowList) {
        this.qryCSShowList = qryCSShowList;
    }

    public List<QryCSShow> getQryCSShowListForExcel() {
        return qryCSShowListForExcel;
    }

    public void setQryCSShowListForExcel(List<QryCSShow> qryCSShowListForExcel) {
        this.qryCSShowListForExcel = qryCSShowListForExcel;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public CttItem getCttItem() {
        return cttItem;
    }

    public void setCttItem(CttItem cttItem) {
        this.cttItem = cttItem;
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

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    /*�����ֶ�End*/
}