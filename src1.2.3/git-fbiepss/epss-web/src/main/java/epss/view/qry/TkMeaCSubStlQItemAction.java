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
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;;
import skyline.util.ToolUtil;
import epss.repository.model.EsCttInfo;
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
public class TkMeaCSubStlQItemAction {
    private static final Logger logger = LoggerFactory.getLogger(TkMeaCSubStlQItemAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progMeaItemService}")
    private ProgMeaItemService progMeaItemService;

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
                cttInfoService.getCttInfoListByCttType_Status(
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
            // ����״̬��Ʊ����Ҫ����ʱ���޸ĵ����ļ���
        }
        return null;
    }
    private void initData(String strTkcttInfoPkid) {
        beansMap.put("strThisMonth", ToolUtil.getStrThisMonth());
        // 1���ܰ���ͬ��Ϣ
        // 1��1��ȡ���ܰ���ͬ��Ϣ
        EsCttInfo esTkcttInfo= cttInfoService.getCttInfoByPkId(strTkcttInfoPkid);
        commStlSubcttEngH.setStrTkcttId(esTkcttInfo.getId());
        commStlSubcttEngH.setStrTkcttName(esTkcttInfo.getName());
        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);
        // 1��2����ȡ��Ӧ�ܰ���ͬ����ϸ����
        List<EsCttItem> esCttItemOfTkcttList = cttItemService.getEsItemList(
                ESEnum.ITEMTYPE0.getCode(),
                strTkcttPkid);
        // �����ܰ���ͬ���ݵ���Ϣ��ƴ�ɺ�ͬԭ��
        List<CttItemShow> tkcttItemShowList =new ArrayList<>();
        recursiveDataTable("root", esCttItemOfTkcttList, tkcttItemShowList);
        tkcttItemShowList =getItemList_DoFromatNo(tkcttItemShowList);

        // 2���ɱ��ƻ���Ϣ
        List<EsCttInfo> esCstplInfoList= cttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                ESEnum.ITEMTYPE1.getCode(),esTkcttInfo.getPkid());
        if(esCstplInfoList.size()==0){
            return;
        }
        EsCttInfo esCstplInfo =esCstplInfoList.get(0);
        List<EsCttItem> cstplItemListTemp=
                cttItemService.getEsItemList(ESEnum.ITEMTYPE1.getCode(),esCstplInfo.getPkid());
        List<CttItemShow> cstplItemShowListTemp =new ArrayList<>();
        recursiveDataTable("root", cstplItemListTemp, cstplItemShowListTemp);
        // �ɱ��ƻ��Ű�
        cstplItemShowListTemp =getItemList_DoFromatNo(cstplItemShowListTemp) ;

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
            esItemStlTkcttEngMeaList= progMeaItemService.selectRecordsByPkidPeriodNoExample(esItemStlTkcttEngMea);
        }

        /*ƴװ�б�*/
        try {
            qryTkMeaCSStlQShowList =new ArrayList<QryTkMeaCSStlQShow>();
            for(CttItemShow tkcttItemShowUnit : tkcttItemShowList){
                Boolean insertedFlag=false ;
                QryTkMeaCSStlQShow qryTkMeaCSStlQShowTemp =new QryTkMeaCSStlQShow();

                 // �ܰ���ͬ
                qryTkMeaCSStlQShowTemp.setTkcttItem_Pkid(tkcttItemShowUnit.getPkid());
                qryTkMeaCSStlQShowTemp.setTkcttItem_ParentPkid(tkcttItemShowUnit.getParentPkid());
                qryTkMeaCSStlQShowTemp.setTkcttItem_No(tkcttItemShowUnit.getStrNo());
                qryTkMeaCSStlQShowTemp.setTkcttItem_Name(tkcttItemShowUnit.getName());
                qryTkMeaCSStlQShowTemp.setTkcttItem_Unit(tkcttItemShowUnit.getUnit());
                qryTkMeaCSStlQShowTemp.setTkcttItem_CttUnitPrice(tkcttItemShowUnit.getContractUnitPrice());
                qryTkMeaCSStlQShowTemp.setTkcttItem_CttQty(tkcttItemShowUnit.getContractQuantity());
                if(tkcttItemShowUnit.getContractUnitPrice()!=null&&
                        tkcttItemShowUnit.getContractQuantity()!=null) {
                    qryTkMeaCSStlQShowTemp.setTkcttItem_CttAmt(
                            tkcttItemShowUnit.getContractUnitPrice().multiply(tkcttItemShowUnit.getContractQuantity()));
                }
                // ����
                for(EsItemStlTkcttEngMea esItemStlTkcttEngMea:esItemStlTkcttEngMeaList){
                    if(ToolUtil.getStrIgnoreNull(tkcttItemShowUnit.getPkid()).equals(esItemStlTkcttEngMea.getTkcttItemPkid())){
                        // �ܰ���ͬ����
                        BigDecimal bdTkcttContractUnitPrice=ToolUtil.getBdIgnoreNull(tkcttItemShowUnit.getContractUnitPrice());
                        BigDecimal bdTkcttStlCMeaQty=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getCurrentPeriodQty());
                        BigDecimal bdTkcttStlCMeaAmount=bdTkcttStlCMeaQty.multiply(bdTkcttContractUnitPrice);
                        // ���ۼ���
                        BigDecimal bdTkcttStlBToCMeaQuantity=ToolUtil.getBdIgnoreNull(esItemStlTkcttEngMea.getBeginToCurrentPeriodQty());
                        BigDecimal bdTkcttStlBToCMeaAmount=bdTkcttStlBToCMeaQuantity.multiply(bdTkcttContractUnitPrice);
                        // ���ڼ��������ͽ��
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_ThisStageQty(bdTkcttStlCMeaQty);
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_ThisStageAmt(bdTkcttStlCMeaAmount);
                        // ���ۼ��������ͽ��
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_AddUpQty(bdTkcttStlBToCMeaQuantity);
                        qryTkMeaCSStlQShowTemp.setTkcttStlItem_AddUpAmt(bdTkcttStlBToCMeaAmount);
                        break;
                    }
                }

                // �ɱ��ƻ�
                BigDecimal bdCstplTkcttItem_TotalAmt=new BigDecimal(0);
                for(CttItemShow cstplItemShowUnit : cstplItemShowListTemp){
                    QryTkMeaCSStlQShow tkMeaCstplUnitTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(qryTkMeaCSStlQShowTemp);
                    if(tkcttItemShowUnit.getPkid().equals(cstplItemShowUnit.getCorrespondingPkid())) {
                        if(insertedFlag.equals(true)){
                            tkMeaCstplUnitTemp.setTkcttItem_Pkid(null);
                            tkMeaCstplUnitTemp.setTkcttItem_No(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Name(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Unit(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttUnitPrice(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttQty(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpAmt(null);
                        }
                        insertedFlag=true ;
                        tkMeaCstplUnitTemp.setCstplItem_Pkid(cstplItemShowUnit.getPkid());
                        tkMeaCstplUnitTemp.setCstplItem_No(cstplItemShowUnit.getStrNo());
                        tkMeaCstplUnitTemp.setCstplItem_Name(cstplItemShowUnit.getName());
                        tkMeaCstplUnitTemp.setCstplItem_UnitPrice(cstplItemShowUnit.getContractUnitPrice());
                        tkMeaCstplUnitTemp.setCstplItem_Qty(cstplItemShowUnit.getContractQuantity());
                        if(ToolUtil.getBdIgnoreNull(cstplItemShowUnit.getContractUnitPrice()).compareTo(ToolUtil.bigDecimal0)>0&&
                           ToolUtil.getBdIgnoreNull(cstplItemShowUnit.getContractQuantity()).compareTo(ToolUtil.bigDecimal0)>0) {
                            tkMeaCstplUnitTemp.setCstplItem_Amt(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                        if(tkMeaCstplUnitTemp.getCstplItem_Amt()!=null) {
                            bdCstplTkcttItem_TotalAmt=bdCstplTkcttItem_TotalAmt.add(tkMeaCstplUnitTemp.getCstplItem_Amt()) ;
                        }
                        qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                    }
                }

                if (insertedFlag.equals(false)){
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTemp);
                }

                if(bdCstplTkcttItem_TotalAmt.compareTo(new BigDecimal(0))>0) {
                    qryTkMeaCSStlQShowList.get(qryTkMeaCSStlQShowList.size()-1).setCstplTkcttItem_TotalAmt(bdCstplTkcttItem_TotalAmt);
                    if(ToolUtil.getBdIgnoreNull(tkcttItemShowUnit.getContractQuantity()).compareTo(ToolUtil.bigDecimal0)>0) {
                        // ���������ֵ����
                        BigDecimal bdCstplTkcttItem_TotalUnitPrice=
                                bdCstplTkcttItem_TotalAmt.divide(tkcttItemShowUnit.getContractQuantity(),6,BigDecimal.ROUND_HALF_UP);
                        qryTkMeaCSStlQShowList.get(qryTkMeaCSStlQShowList.size()-1).setCstplTkcttItem_TotalUnitPrice(bdCstplTkcttItem_TotalUnitPrice);

                        for(int i=qryTkMeaCSStlQShowList.size()-1;i>=0;i--){
                            if(qryTkMeaCSStlQShowList.get(i).getTkcttItem_Pkid()!=null) {
                                if(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_ThisStageQty()!=null) {
                                    qryTkMeaCSStlQShowList.get(i).setTkcttStlCstplItem_ThisStageAmt(
                                            bdCstplTkcttItem_TotalUnitPrice.multiply(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_ThisStageQty()));
                                }
                                if(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_AddUpQty()!=null) {
                                    qryTkMeaCSStlQShowList.get(i).setTkcttStlCstplItem_AddUpAmt(
                                            bdCstplTkcttItem_TotalUnitPrice.multiply(qryTkMeaCSStlQShowList.get(i).getTkcttStlItem_AddUpQty()));
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // �ɱ��ƻ��п�ͷ����Ҫ�����һһ�г�
            for(CttItemShow cstplItemShowUnit : cstplItemShowListTemp){
                if(ToolUtil.getStrIgnoreNull(cstplItemShowUnit.getCorrespondingPkid()).length()<=0){
                    QryTkMeaCSStlQShow qryTkMeaCSStlQShowTempRe =new QryTkMeaCSStlQShow();
                    // �б�����
                    qryTkMeaCSStlQShowTempRe.setTkcttItem_Pkid(qryTkMeaCSStlQShowList.size()+":");
                    qryTkMeaCSStlQShowTempRe.setTkcttItem_Name("�ɱ��ƻ�����"+cstplItemShowUnit.getName()+")");
                    // �ɱ��ƻ�����
                    qryTkMeaCSStlQShowTempRe.setCstplItem_Pkid(cstplItemShowUnit.getPkid());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_No(cstplItemShowUnit.getStrNo());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_Name(cstplItemShowUnit.getName());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_UnitPrice(cstplItemShowUnit.getContractUnitPrice());
                    qryTkMeaCSStlQShowTempRe.setCstplItem_Qty(cstplItemShowUnit.getContractQuantity());
                    if(!ToolUtil.getBdIgnoreNull(cstplItemShowUnit.getContractAmount()).equals(ToolUtil.bigDecimal0)) {
                        qryTkMeaCSStlQShowTempRe.setCstplItem_Amt(cstplItemShowUnit.getContractAmount());
                    } else{
                        if(cstplItemShowUnit.getContractUnitPrice()!=null&&
                                cstplItemShowUnit.getContractQuantity()!=null) {
                            qryTkMeaCSStlQShowTempRe.setCstplItem_Amt(
                                    cstplItemShowUnit.getContractUnitPrice().multiply(cstplItemShowUnit.getContractQuantity()));
                        }
                    }
                    qryTkMeaCSStlQShowList.add(qryTkMeaCSStlQShowTempRe);
                }
            }

            // 4���ְ�����
            List<QryTkMeaCSStlQShow> subcttStlQBySignPartList=esQueryService.getCSStlQBySignPartList(esCstplInfo.getPkid(), strPeriodNo);

            // ���ݳɱ��ƻ����ӷְ���ͬ��
            List<QryTkMeaCSStlQShow> qryTkMeaCSStlQShowListTemp=new ArrayList<>();
            qryTkMeaCSStlQShowListTemp.addAll(qryTkMeaCSStlQShowList);
            qryTkMeaCSStlQShowList.clear();
            for(QryTkMeaCSStlQShow tkMeaCstplUnit:qryTkMeaCSStlQShowListTemp) {
                Boolean insertedFlag=false ;
                BigDecimal bdSubcttCttQtyTotal=new BigDecimal(0);
                BigDecimal bdSubcttCttAmtTotal=new BigDecimal(0);
                // ���ۼ���
                BigDecimal bdTkcttStlBToCMeaQty=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStlItem_AddUpQty());
                BigDecimal bdTkcttStlBToCMeaAmt=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStlItem_AddUpAmt());
                BigDecimal bdTkcttStlCstplBToCMeaAmt=ToolUtil.getBdIgnoreNull(tkMeaCstplUnit.getTkcttStlCstplItem_AddUpAmt());
                for(int i=0;i<subcttStlQBySignPartList.size();i++) {
                    QryTkMeaCSStlQShow tkMeaCstplUnitTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(tkMeaCstplUnit);
                    // �ɱ��ƻ�������Ŀ��ְ���ͬ��
                    if(ToolUtil.getStrIgnoreNull(tkMeaCstplUnitTemp.getCstplItem_Pkid()).equals(
                            subcttStlQBySignPartList.get(i).getSubcttItem_CorrPkid())) {
                        // Ŀ��ְ���ͬ��ĺ�ͬ����
                        BigDecimal bdSubcttCttUnitPrice=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getSubcttItem_UnitPrice());
                        // Ŀ��ְ���ͬ��ĵ������������ڽ��
                        BigDecimal bdThisStageQty=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getSubcttStlItem_ThisStageQty());
                        BigDecimal bdThisStageAmt=bdThisStageQty.multiply(bdSubcttCttUnitPrice);
                        // Ŀ��ְ���ͬ��Ŀ������������۽��
                        BigDecimal bdAddUpToQty=ToolUtil.getBdIgnoreNull(subcttStlQBySignPartList.get(i).getSubcttStlItem_AddUpQty());
                        BigDecimal bdAddUpToAmt=bdAddUpToQty.multiply(bdSubcttCttUnitPrice);

                        // �ۼ�Ŀ��ְ���ͬ��ĺ�ͬ��������ͬ���ۣ���ͬ���
                        bdSubcttCttQtyTotal=bdSubcttCttQtyTotal.add(bdAddUpToQty);
                        bdSubcttCttAmtTotal=bdSubcttCttAmtTotal.add(bdAddUpToAmt);

                        //�ܰ���ͬ
                        if(insertedFlag.equals(true)){
                            tkMeaCstplUnitTemp.setTkcttItem_Pkid(null);
                            tkMeaCstplUnitTemp.setTkcttItem_No(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Name(null);
                            tkMeaCstplUnitTemp.setTkcttItem_Unit(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttUnitPrice(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttQty(null);
                            tkMeaCstplUnitTemp.setTkcttItem_CttAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_ThisStageAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlCstplItem_ThisStageAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpQty(null);
                            tkMeaCstplUnitTemp.setTkcttStlItem_AddUpAmt(null);
                            tkMeaCstplUnitTemp.setTkcttStlCstplItem_AddUpAmt(null);
                            tkMeaCstplUnitTemp.setCstplItem_Name(null);
                            tkMeaCstplUnitTemp.setCstplItem_UnitPrice(null);
                            tkMeaCstplUnitTemp.setCstplItem_Qty(null);
                            tkMeaCstplUnitTemp.setCstplItem_Amt(null);
                            tkMeaCstplUnitTemp.setCstplTkcttItem_TotalAmt(null);
                            tkMeaCstplUnitTemp.setCstplTkcttItem_TotalUnitPrice(null);
                        }

                        insertedFlag=true ;
                        // �ְ���ͬ
                        tkMeaCstplUnitTemp.setSubcttItem_Name(subcttStlQBySignPartList.get(i).getSubcttItem_Name());
                        tkMeaCstplUnitTemp.setSubcttItem_CorrPkid(subcttStlQBySignPartList.get(i).getSubcttItem_CorrPkid());
                        tkMeaCstplUnitTemp.setSubcttItem_SignPartName(subcttStlQBySignPartList.get(i).getSubcttItem_SignPartName());

                        // �ְ�����
                        tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageQty(bdThisStageQty);
                        tkMeaCstplUnitTemp.setSubcttStlItem_ThisStageAmt(bdThisStageAmt);
                        tkMeaCstplUnitTemp.setSubcttStlItem_AddUpQty(bdAddUpToQty);
                        tkMeaCstplUnitTemp.setSubcttStlItem_AddUpAmt(bdAddUpToAmt);

                        // ���һ��֮ǰ����
                        if(i<subcttStlQBySignPartList.size()-1){
                            // ��һ������Ŀ��ְ���ͬ��
                            if(tkMeaCstplUnitTemp.getCstplItem_Pkid().equals(
                                    subcttStlQBySignPartList.get(i+1).getSubcttItem_CorrPkid())){
                                // �ɱ��ƻ����趨
                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                            }// ��һ���Ŀ��ְ���ͬ��
                            else{
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaQty).subtract(bdSubcttCttQtyTotal));
                                tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlBToCMeaAmt).subtract(bdSubcttCttAmtTotal));
                                tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(
                                        ToolUtil.getBdIgnoreNull(bdTkcttStlCstplBToCMeaAmt).subtract(bdSubcttCttAmtTotal));
                                qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                                break;
                            }
                        }else{
                            // �ܰ�������ְ�����ֵ��
                            tkMeaCstplUnitTemp.setMeaSItem_AddUpQty(
                                    bdTkcttStlBToCMeaQty.subtract(bdSubcttCttQtyTotal));
                            tkMeaCstplUnitTemp.setMeaSItem_AddUpAmt(
                                    bdTkcttStlBToCMeaAmt.subtract(bdSubcttCttAmtTotal));
                            tkMeaCstplUnitTemp.setMeaSCstplItem_AddUpAmt(
                                    bdTkcttStlCstplBToCMeaAmt.subtract(bdSubcttCttAmtTotal));
                            qryTkMeaCSStlQShowList.add(tkMeaCstplUnitTemp);
                        }
                    }
                }

                if(insertedFlag.equals(false)){
                    tkMeaCstplUnit.setMeaSItem_AddUpQty(tkMeaCstplUnit.getTkcttStlItem_AddUpQty());
                    tkMeaCstplUnit.setMeaSItem_AddUpAmt(tkMeaCstplUnit.getTkcttStlItem_AddUpAmt());
                    tkMeaCstplUnit.setMeaSCstplItem_AddUpAmt(tkMeaCstplUnit.getTkcttStlCstplItem_AddUpAmt());
                    qryTkMeaCSStlQShowList.add(tkMeaCstplUnit);
                }
            }
            // �����������װ�Excel��
            qryTkMeaCSStlQShowListForExcel =new ArrayList<QryTkMeaCSStlQShow>();
            for(QryTkMeaCSStlQShow itemOfEsItemHieRelapTkctt: qryTkMeaCSStlQShowList){
                QryTkMeaCSStlQShow itemOfEsItemHieRelapTkcttTemp= (QryTkMeaCSStlQShow) BeanUtils.cloneBean(itemOfEsItemHieRelapTkctt);
                itemOfEsItemHieRelapTkcttTemp.setTkcttItem_No(ToolUtil.getIgnoreSpaceOfStr(itemOfEsItemHieRelapTkcttTemp.getTkcttItem_No()));
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
        // ͨ������id�������ĺ���
        List<EsCttItem> subEsCttItemList =getEsItemListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            String strCreatedByName= ToolUtil.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= ToolUtil.getUserName(itemUnit.getLastUpdBy());
            CttItemShow cttItemShowTemp = new CttItemShow(
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
    /*�������ݿ��в㼶��ϵ�����б��õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<EsCttItem> getEsItemListByLevelParentPkid(String strLevelParentPkid,
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
    private List<CttItemShow> getItemList_DoFromatNo(
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
            // StickyHeader��ƴװ��ͷƽ�ֿ��ȣ������趨���ȣ������Բ��ã���ʱͣ��
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

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
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

    public ProgMeaItemService getProgMeaItemService() {
        return progMeaItemService;
    }

    public void setProgMeaItemService(ProgMeaItemService progMeaItemService) {
        this.progMeaItemService = progMeaItemService;
    }
    /*�����ֶ�End*/
}