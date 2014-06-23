package epss.view.item;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.JxlsManager;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.*;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsItemStlTkcttEngMea;
import epss.service.*;
import epss.service.common.EsFlowService;
import epss.service.common.EsQueryService;
import epss.view.common.EsCommon;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ItemQryByTkMeaCSubStlQAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemQryByTkMeaCSubStlQAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{esItemStlTkcttEngMeaService}")
    private EsItemStlTkcttEngMeaService esItemStlTkcttEngMeaService;

    /*�б���ʾ��*/
    private List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowList;
    private List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListForExcel;

    private List<SelectItem> tkcttList;

    private String strTkcttPkid;
    private String strPeriodNo;

    // �����Ͽؼ�����ʾ����
    private CommStlSubcttEngH commStlSubcttEngH;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        // ��ȡ�Ѿ���׼�˵��ܰ���ͬ�б�
        List<CttInfoShow> cttInfoShowList =
                esCttInfoService.getCttInfoListByCttType_Status(
                        ESEnum.ITEMTYPE0.getCode()
                       ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
        tkcttList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","");
            tkcttList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                tkcttList.add(selectItem);
            }
        }
        strPeriodNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.qryTkMeaCSStlQShowListForExcel.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ܰ������ɱ��ƻ��ְ����������Ƚ�-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryTkMeaCSStlQ.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    private void initData(String strTkcttInfoPkid) {
        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        // 1���ܰ���ͬ��Ϣ
        // 1��1��ȡ���ܰ���ͬ��Ϣ
        EsCttInfo esCttInfoTkctt= esCttInfoService.getCttInfoByPkId(strTkcttInfoPkid);
        commStlSubcttEngH.setStrTkcttId(esCttInfoTkctt.getId());
        commStlSubcttEngH.setStrTkcttName(esCttInfoTkctt.getName());
        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
        // 1��2����ȡ��Ӧ�ܰ���ͬ����ϸ����
        List<EsCttItem> esCttItemOfTkcttList =new ArrayList<EsCttItem>();
        esCttItemOfTkcttList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(),
                strTkcttPkid);
        // �����ܰ���ͬ���ݵ���Ϣ��ƴ�ɺ�ͬԭ��
        List<CttItemShow> tkcttItemShowList =new ArrayList<>();
        recursiveDataTable("root", esCttItemOfTkcttList, tkcttItemShowList);
        tkcttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(tkcttItemShowList);

        // 2���ɱ��ƻ���Ϣ
        List<EsCttInfo> esCstplInfoList=esCttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                ESEnum.ITEMTYPE1.getCode(),esCttInfoTkctt.getPkid());
        if(esCstplInfoList.size()==0){
            return;
        }
        EsCttInfo esCstplInfo =esCstplInfoList.get(0);
        List<CttItemShow> cstplItemListTemp=
                esQueryService.getEsCstplItemList(strTkcttInfoPkid);

        // 3���ܰ���ͬ�����׼�˵��ܰ���������
        // С�ڵ�����ѡ���������Ѿ���׼�˵ļ�������
        String strMeaLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNoByEndPeriod(
                        ESEnum.ITEMTYPE7.getCode(),strTkcttInfoPkid,strPeriodNo));
        List<EsItemStlTkcttEngMea> esItemStlTkcttEngMeaList=new ArrayList<EsItemStlTkcttEngMea>();
        if(!ToolUtil.getStrIgnoreNull(strMeaLatestApprovedPeriodNo).equals("")){
            EsItemStlTkcttEngMea esItemStlTkcttEngMea=new EsItemStlTkcttEngMea();
            esItemStlTkcttEngMea.setTkcttPkid(strTkcttInfoPkid);
            esItemStlTkcttEngMea.setPeriodNo(strMeaLatestApprovedPeriodNo);
            esItemStlTkcttEngMeaList=esItemStlTkcttEngMeaService.selectRecordsByPkidPeriodNoExample(esItemStlTkcttEngMea);
        }

        // 4���ְ�����
        List<QryShow> subcttStlQBySignPartList=esQueryService.getCSStlQBySignPartList(esCstplInfo.getPkid(), strPeriodNo);

        /*ƴװ�б�*/
        try {
            qryTkMeaCSStlQShowList =new ArrayList<QryTkMeaCSStlQShow>();
            for(CttItemShow cttItemShowTkctt : tkcttItemShowList){
                QryTkMeaCSStlQShow qryTkMeaCSStlQShowNew =new QryTkMeaCSStlQShow();
                // �ܰ���ͬ����
                BigDecimal bdTkcttContractUnitPrice=new BigDecimal(0);
                // ���ڼ���
                BigDecimal bdTkcttStlCMeaQuantity=new BigDecimal(0);
                BigDecimal bdTkcttStlCMeaAmount=new BigDecimal(0);
                // ���ۼ���
                BigDecimal bdTkcttStlBToCMeaQuantity=new BigDecimal(0);
                BigDecimal bdTkcttStlBToCMeaAmount=new BigDecimal(0);
                // �ܰ���ͬ
                qryTkMeaCSStlQShowNew.setStrTkctt_Pkid(cttItemShowTkctt.getPkid());
                qryTkMeaCSStlQShowNew.setStrTkctt_ParentPkid(cttItemShowTkctt.getParentPkid());
                qryTkMeaCSStlQShowNew.setStrTkctt_No(cttItemShowTkctt.getStrNo());
                qryTkMeaCSStlQShowNew.setStrTkctt_Name(cttItemShowTkctt.getName());
                qryTkMeaCSStlQShowNew.setStrTkctt_Unit(cttItemShowTkctt.getUnit());
                qryTkMeaCSStlQShowNew.setBdTkctt_ContractUnitPrice(cttItemShowTkctt.getContractUnitPrice());
                qryTkMeaCSStlQShowNew.setBdTkctt_ContractQuantity(cttItemShowTkctt.getContractQuantity());
                if(cttItemShowTkctt.getContractUnitPrice()!=null&&
                        cttItemShowTkctt.getContractQuantity()!=null) {
                    qryTkMeaCSStlQShowNew.setBdTkctt_ContractAmount(
                            cttItemShowTkctt.getContractUnitPrice().multiply(cttItemShowTkctt.getContractQuantity()));
                }
                // ����
                for(EsItemStlTkcttEngMea esItemStlTkcttEngMea:esItemStlTkcttEngMeaList){
                    if(ToolUtil.getStrIgnoreNull(cttItemShowTkctt.getPkid()).equals(esItemStlTkcttEngMea.getTkcttItemPkid())){
                        bdTkcttContractUnitPrice=ToolUtil.getBdIgnoreNull(cttItemShowTkctt.getContractUnitPrice());
                        bdTkcttStlCMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getCurrentPeriodQty());
                        bdTkcttStlCMeaAmount=bdTkcttStlCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        bdTkcttStlBToCMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        bdTkcttStlBToCMeaAmount=bdTkcttStlBToCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        // ���ڼ��������ͽ��
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_CurrentPeriodMeaQuantity(bdTkcttStlCMeaQuantity);
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_CurrentPeriodMeaAmount(bdTkcttStlCMeaAmount);
                        // ���ۼ��������ͽ��
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_BeginToCurrentPeriodMeaQuantity(bdTkcttStlBToCMeaQuantity);
                        qryTkMeaCSStlQShowNew.setBdTkcttStl_BeginToCurrentPeriodMeaAmount(bdTkcttStlBToCMeaAmount);
                        break;
                    }
                }

                // �ɱ��ƻ�
                String strItemOfEsItemHieRelapTkctt_Pkid=ToolUtil.getStrIgnoreNull(qryTkMeaCSStlQShowNew.getStrTkctt_Pkid());
                String strItemOfEsItemHieRelapCstpl_CorrespondingPkid;
                String strItemOfEsItemHieRelapCstpl_Pkid="";
                for(CttItemShow cstplItemShowUnit : cstplItemListTemp){
                    strItemOfEsItemHieRelapCstpl_CorrespondingPkid=ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getCorrespondingPkid());
                    if(strItemOfEsItemHieRelapTkctt_Pkid.equals(strItemOfEsItemHieRelapCstpl_CorrespondingPkid)) {
                        strItemOfEsItemHieRelapCstpl_Pkid=ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getPkid());
                        qryTkMeaCSStlQShowNew.setStrCstpl_Pkid(strItemOfEsItemHieRelapCstpl_Pkid);
                        qryTkMeaCSStlQShowNew.setBdCstpl_ContractUnitPrice(cstplItemShowUnit.getContractUnitPrice());
                        qryTkMeaCSStlQShowNew.setBdCstpl_ContractQuantity(cstplItemShowUnit.getContractQuantity());
                        if(cstplItemShowUnit.getContractUnitPrice()!=null&&
                                cstplItemShowUnit.getContractQuantity()!=null) {
                            qryTkMeaCSStlQShowNew.setBdCstpl_ContractAmount(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                        break;
                    }
                }

                Integer intGroup=0;
                Boolean isInThisCirculateHasSame=false;
                BigDecimal bdSubcttContractQuantityTotal=new BigDecimal(0);
                BigDecimal bdSubcttContractAmountTotal=new BigDecimal(0);
                // ���ݳɱ��ƻ����ӷְ���ͬ��
                for(int i=0;i<subcttStlQBySignPartList.size();i++) {
                    // �ɱ��ƻ�������Ŀ��ְ���ͬ��
                    if(strItemOfEsItemHieRelapCstpl_Pkid.equals(subcttStlQBySignPartList.get(i).getStrCorrespondingPkid())) {
                        // Ŀ��ְ���ͬ��ĺ�ͬ����
                        BigDecimal bdSubcttContractUnitPrice=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdUnitPrice());
                        isInThisCirculateHasSame=true;
                        intGroup++;
                        // ��¡Ŀ����д������
                        QryTkMeaCSStlQShow qryTkMeaCSStlQShowNewInsert =(QryTkMeaCSStlQShow)BeanUtils.cloneBean(qryTkMeaCSStlQShowNew);
                        // Ŀ��ְ���ͬ��ĵ������������ڽ��
                        BigDecimal bdSubcttCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdCurrentPeriodQuantity());
                        BigDecimal bdSubcttCurrentPeriodAmount=bdSubcttCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);

                        BigDecimal bdSubcttBeginToCurrentPeriodQuantity=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getBdBeginToCurrentPeriodQuantity());
                        BigDecimal bdSubcttBeginToCurrentPeriodAmount=bdSubcttBeginToCurrentPeriodQuantity.multiply(bdSubcttContractUnitPrice);

                        // �ۼ�Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        bdSubcttContractQuantityTotal=bdSubcttContractQuantityTotal.add(bdSubcttBeginToCurrentPeriodQuantity);
                        bdSubcttContractAmountTotal=bdSubcttContractAmountTotal.add(bdSubcttBeginToCurrentPeriodAmount);

                        // �ְ���ͬ
                        qryTkMeaCSStlQShowNewInsert.setStrSubctt_SignPartName(subcttStlQBySignPartList.get(i).getStrName());
                        qryTkMeaCSStlQShowNewInsert.setStrTkctt_Pkid(subcttStlQBySignPartList.get(i).getStrCorrespondingPkid()
                                + "/" + intGroup.toString());

                        // �ְ�����
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_CurrentPeriodQQty(bdSubcttCurrentPeriodQuantity);
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_CurrentPeriodAmount(bdSubcttCurrentPeriodAmount);
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodQQty(bdSubcttBeginToCurrentPeriodQuantity);
                        qryTkMeaCSStlQShowNewInsert.setBdSubcttStl_BeginToCurrentPeriodAmount(bdSubcttBeginToCurrentPeriodAmount);

                        if(intGroup>1){
                            qryTkMeaCSStlQShowNewInsert.setStrTkctt_No("");
                            qryTkMeaCSStlQShowNewInsert.setStrTkctt_Name("");
                            qryTkMeaCSStlQShowNewInsert.setStrTkctt_Unit("");
                        }

                        // ���һ��֮ǰ����
                        if(i<subcttStlQBySignPartList.size()-1){
                            // ��һ������Ŀ��ְ���ͬ��
                            if(cttItemShowTkctt.getPkid().equals(subcttStlQBySignPartList.get(i+1).getStrCorrespondingPkid())){
                                // �ɱ��ƻ����趨
                                qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNewInsert);
                            }// ��һ���Ŀ��ְ���ͬ��
                            else{
                                qryTkMeaCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaQuantity).subtract(bdSubcttContractQuantityTotal));
                                qryTkMeaCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodAmount(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaAmount).subtract(bdSubcttContractAmountTotal));
                                 qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNewInsert);
                                break;
                            }
                        }else{
                            // �ܰ�������ְ�����ֵ��
                            qryTkMeaCSStlQShowNewInsert.setBdMeaS_BeginToCurrentPeriodQQty(
                                    bdTkcttStlBToCMeaQuantity.subtract(bdSubcttContractQuantityTotal));
                            qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNewInsert);
                        }
                    }
                }
                if(isInThisCirculateHasSame.equals(false)){
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowNew);
                }
            }
            qryTkMeaCSStlQShowListForExcel =new ArrayList<QryTkMeaCSStlQShow>();
            for(QryTkMeaCSStlQShow itemOfEsItemHieRelapTkctt: qryTkMeaCSStlQShowList){
                QryTkMeaCSStlQShow itemOfEsItemHieRelapTkcttTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(itemOfEsItemHieRelapTkctt);
                itemOfEsItemHieRelapTkcttTemp.setStrTkctt_No(ToolUtil.getIgnoreSpaceOfStr(itemOfEsItemHieRelapTkcttTemp.getStrTkctt_No()));
                qryTkMeaCSStlQShowListForExcel.add(itemOfEsItemHieRelapTkcttTemp);
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
        if(qryTkMeaCSStlQShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("qryTkMeaCSStlQShowListForExcel", qryTkMeaCSStlQShowListForExcel);
    }

    /*�ݹ�����*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<EsCttItem> esCttItemListPara,
                                      List<CttItemShow> cttItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        EsCttItem esCttItem =null;
        for(EsCttItem itemUnit: subEsCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getLastUpdBy());
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
            if(!ToolUtil.getStrIgnoreNull(cttItemShowTemp.getCorrespondingPkid()).equals("")) {
                esCttItem = esCttItemService.getEsItemHieRelapByPkId(cttItemShowTemp.getCorrespondingPkid());
                cttItemShowTemp.setStrCorrespondingItemPkid(esCttItem.getPkid());
            }
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara, cttItemShowListPara);
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<EsCttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
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
            if(itemUnit.getGrade().equals(intBeforeGrade)){
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
            if(ToolUtil.getStrIgnoreNull(strTkcttPkid).equals("")){
                MessageUtil.addWarn("��ѡ��ɱ��ƻ��");
                return;
            }
            initData(strTkcttPkid);
            // StickyHeader��ƴװ��ͷƽ�ֿ�ȣ������趨��ȣ������Բ��ã���ʱͣ��
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    /*�����ֶ�Start*/
    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public List<QryTkMeaCSStlQShow> getQryTkMeaCSStlQShowList() {
        return qryTkMeaCSStlQShowList;
    }

    public void setQryTkMeaCSStlQShowList(List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowList) {
        this.qryTkMeaCSStlQShowList = qryTkMeaCSStlQShowList;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public String getStrTkcttPkid() {
        return strTkcttPkid;
    }

    public void setStrTkcttPkid(String strTkcttPkid) {
        this.strTkcttPkid = strTkcttPkid;
    }

    public String getStrPeriodNo() {
        return strPeriodNo;
    }

    public void setStrPeriodNo(String strPeriodNo) {
        this.strPeriodNo = strPeriodNo;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public List<SelectItem> getTkcttList() {
        return tkcttList;
    }

    public void setTkcttList(List<SelectItem> tkcttList) {
        this.tkcttList = tkcttList;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public CommStlSubcttEngH getCommStlSubcttEngH() {
        return commStlSubcttEngH;
    }

    public void setCommStlSubcttEngH(CommStlSubcttEngH commStlSubcttEngH) {
        this.commStlSubcttEngH = commStlSubcttEngH;
    }

    public List<QryTkMeaCSStlQShow> getQryTkMeaCSStlQShowListForExcel() {
        return qryTkMeaCSStlQShowListForExcel;
    }

    public void setQryTkMeaCSStlQShowListForExcel(List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListForExcel) {
        this.qryTkMeaCSStlQShowListForExcel = qryTkMeaCSStlQShowListForExcel;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public EsItemStlTkcttEngMeaService getEsItemStlTkcttEngMeaService() {
        return esItemStlTkcttEngMeaService;
    }

    public void setEsItemStlTkcttEngMeaService(EsItemStlTkcttEngMeaService esItemStlTkcttEngMeaService) {
        this.esItemStlTkcttEngMeaService = esItemStlTkcttEngMeaService;
    }
    /*�����ֶ�End*/
}
