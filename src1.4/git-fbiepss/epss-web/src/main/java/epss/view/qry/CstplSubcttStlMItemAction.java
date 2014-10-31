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
import epss.repository.model.CttInfo;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.service.*;
import epss.service.EsQueryService;
import epss.view.flow.EsCommon;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
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
import java.util.*;

@ManagedBean
@ViewScoped
public class CstplSubcttStlMItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplSubcttStlMItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;

    /*�б���ʾ��*/
    private List<QryCSStlMShow> qryCSStlMShowList;
    private List<QryCSStlMShow> qryCSStlMShowListForExcel;

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
        strPeriodNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException, RowsExceededException, WriteException {
        if (this.qryCSStlMShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "���̷ְ���ͬ���Ϲ�Ӧ���-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCSStlM.xls");
            // ����״̬��Ʊ����Ҫ����ʱ���޸ĵ����ļ���
        }
        return null;
    }
    private void initData(String strCstplInfoPkidPara) {
        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        CttInfo cttInfo = cttInfoService.getCttInfoByPkId(strCstplInfoPkidPara);
        reportHeader.setStrCstplId(cttInfo.getId());
        reportHeader.setStrCstplName(cttInfo.getName());
        beansMap.put("reportHeader", reportHeader);
        /*�ɱ��ƻ��б�*/
        List<CttItem> cttItemListCstpl =new ArrayList<CttItem>();
        cttItemListCstpl = cttItemService.getEsItemList(
                EnumResType.RES_TYPE1.getCode(),
                strCstplPkid);
        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", cttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListCstpl);

        List<QryShow> qryShowList =esQueryService.getCSStlMList(strCstplInfoPkidPara, strPeriodNo);

        /*ƴװ�б�*/
        try {
            qryCSStlMShowList =new ArrayList<>();
            QryCSStlMShow itemCstplInsertItem;
            for(CttItemShow itemUnit: cttItemShowListCstpl){
                itemCstplInsertItem=new QryCSStlMShow();
                itemCstplInsertItem.setStrPkid(itemUnit.getPkid());
                itemCstplInsertItem.setStrParentPkid(itemUnit.getParentPkid());
                itemCstplInsertItem.setStrNo(itemUnit.getStrNo());
                itemCstplInsertItem.setStrName(itemUnit.getName());

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
                        // ��¡Ŀ����д�������
                        QryCSStlMShow qryCSStlMShowNewInsert =(QryCSStlMShow)BeanUtils.cloneBean(itemCstplInsertItem);
                        // Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdUnitPrice());
                        BigDecimal bdSubcttCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdQuantity());
                        BigDecimal bdSubcttBeginToCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(qryShowList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdSubcttCurrentPeriodAmount=bdSubcttCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);
                        BigDecimal bdSubcttBeginToCurrentPeriodAmount=bdSubcttBeginToCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);
                        // �ۼ�Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttCurrentPeriodQuantity);
                        bdSubcttContractUnitPriceTotal=bdSubcttContractUnitPriceTotal.add(bdSubcttContractUnitPrice);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttCurrentPeriodAmount);

                        // �ְ���ͬ
                        qryCSStlMShowNewInsert.setBdSubctt_BeginToCurrentPeriodMQty(bdSubcttBeginToCurrentPeriodQuantity);
                        qryCSStlMShowNewInsert.setBdSubctt_CurrentPeriodMQty(bdSubcttCurrentPeriodQuantity);

                        qryCSStlMShowNewInsert.setBdSubctt_BeginToCurrentPeriodMAmount(bdSubcttBeginToCurrentPeriodAmount);
                        qryCSStlMShowNewInsert.setBdSubctt_CurrentPeriodMAmount(bdSubcttCurrentPeriodAmount);

                        qryCSStlMShowNewInsert.setStrSubctt_SignPartName(qryShowList.get(i).getStrName());
                        qryCSStlMShowNewInsert.setStrPkid(qryShowList.get(i).getStrCorrespondingPkid()
                                + "/" + intGroup.toString());
                        qryCSStlMShowNewInsert.setStrParentPkid(itemUnit.getParentPkid());
                        qryCSStlMShowNewInsert.setStrSubctt_Unit(qryShowList.get(i).getStrUnit());
                        qryCSStlMShowNewInsert.setBdSubctt_ContractQuantity(qryShowList.get(i).getBdQuantity());
                        qryCSStlMShowNewInsert.setBdSubctt_ContractUnitPrice(bdSubcttContractUnitPrice);

                        if(intGroup>1){
                            qryCSStlMShowNewInsert.setStrNo("");
                            qryCSStlMShowNewInsert.setStrName("");
                        }

                        // ���һ��֮ǰ����
                        if(i< qryShowList.size()-1){
                            // ��һ������Ŀ��ְ���ͬ��
                            if(itemUnit.getPkid().equals(qryShowList.get(i+1).getStrCorrespondingPkid())){
                                // �ɱ��ƻ����趨
                                qryCSStlMShowList.add(qryCSStlMShowNewInsert);
                            }// ��һ���Ŀ��ְ���ͬ��
                            else{
                                // �ɷ�ֵ��
                                qryCSStlMShowList.add(qryCSStlMShowNewInsert);
                                break;
                            }
                        }else{
                            // �ɷ�ֵ��
                            qryCSStlMShowList.add(qryCSStlMShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryCSStlMShowList.add(itemCstplInsertItem);
                }
            }
            qryCSStlMShowListForExcel =new ArrayList<QryCSStlMShow>();
            for(QryCSStlMShow itemUnit: qryCSStlMShowList){
                QryCSStlMShow itemUnitTemp= (QryCSStlMShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                qryCSStlMShowListForExcel.add(itemUnitTemp);
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }

        if(qryCSStlMShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryCSStlMShowListForExcel", qryCSStlMShowListForExcel);
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
    /*�������ݿ��в㼶��ϵ�����б��õ�ĳһ�ڵ��µ��ӽڵ�*/
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
            if(ToolUtil.getStrIgnoreNull(strCstplPkid).equals("")){
                MessageUtil.addWarn("��ѡ��ɱ��ƻ��");
                return;
            }
            initData(strCstplPkid);
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

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public List<QryCSStlMShow> getQryCSStlMShowList() {
        return qryCSStlMShowList;
    }

    public void setQryCSStlMShowList(List<QryCSStlMShow> qryCSStlMShowList) {
        this.qryCSStlMShowList = qryCSStlMShowList;
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

    public List<QryCSStlMShow> getQryCSStlMShowListForExcel() {
        return qryCSStlMShowListForExcel;
    }

    public void setQryCSStlMShowListForExcel(List<QryCSStlMShow> qryCSStlMShowListForExcel) {
        this.qryCSStlMShowListForExcel = qryCSStlMShowListForExcel;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }
    /*�����ֶ�End*/
}