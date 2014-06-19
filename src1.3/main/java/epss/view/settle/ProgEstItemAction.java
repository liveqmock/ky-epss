package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.repository.model.model_show.CttInfoShow;

import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.repository.model.model_show.ProgEstItemShow;
import epss.service.*;
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
import java.io.IOException;
import jxl.write.WriteException;
import java.text.SimpleDateFormat;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.JxlsManager;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ����9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgEstItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgEstItemAction.class);
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{tkcttItemService}")
    private TkcttItemService tkcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progEstInfoService}")
    private ProgEstInfoService progEstInfoService;
    @ManagedProperty(value = "#{progEstItemService}")
    private ProgEstItemService progEstItemService;

    private List<ProgEstItemShow> progEstItemShowList;
    private ProgEstItemShow progEstItemShow;
    private ProgEstItemShow progEstItemShowUpd;
    private ProgEstItemShow progEstItemShowDel;
    private ProgEstItemShow progEstItemShowSelected;

    private BigDecimal bDProgEst_AddUpQtyInDB;
    private BigDecimal bDProgEst_ThisStageQtyInDB;

    private CttInfoShow cttInfoShow;

    /*������*/
    private String strProgEstInfoPkid;
    private ProgEstInfo progEstInfo;
    private String strSubmitType;
    private String strMngNotFinishFlag;

    private Map beansMap;
    // �����Ͽؼ�����ʾ����
    private String strExportToExcelRendered;
    private List<ProgEstItemShow> progEstItemShowListForExcel;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cttInfoShow =new CttInfoShow();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strProgEstInfoPkid")){
            strProgEstInfoPkid=parammap.get("strProgEstInfoPkid").toString();
            this.progEstInfo = progEstInfoService.selectRecordsByPrimaryKey(strProgEstInfoPkid);
        }

        strMngNotFinishFlag="true";
        if(progEstInfo!=null&&EnumFlowStatus.FLOW_STATUS0.getCode().equals(progEstInfo.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }

        resetAction();
        initData();

        /*�ܰ���ͬ����*/
        ProgEstInfo progEstInfo = progEstInfoService.selectRecordsByPrimaryKey(strProgEstInfoPkid);
        TkcttInfo tkcttInfoTemp=tkcttInfoService.getTkcttInfoByPkid(progEstInfo.getTkcttInfoPkid()) ;
        cttInfoShow.setStrTkcttInfoId(tkcttInfoTemp.getId());
        cttInfoShow.setStrTkcttInfoName(tkcttInfoTemp.getName());
        cttInfoShow.setStrStlId(progEstInfo.getId());
        String strTkcttSignPartNameB=tkcttInfoTemp.getSignPartPkidB();
        cttInfoShow.setStrTkcttSignPartNameB(strTkcttSignPartNameB);
        beansMap.put("cttInfoShow", cttInfoShow);
    }
    /*����*/
    public void resetAction(){
        progEstItemShow =new ProgEstItemShow();
        progEstItemShowUpd =new ProgEstItemShow();
        progEstItemShowDel =new ProgEstItemShow();
        progEstItemShowSelected=new ProgEstItemShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if (!blurProgEst_ThisStageQty("submit")) {
                    MessageUtil.addInfo("��¼�뱾�ڹ�������");
                    return;
                }
                List<ProgEstItem> progEstItemListTemp =
                        progEstItemService.isExistInDb(progEstItemShowUpd);
                if (progEstItemListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (progEstItemListTemp.size() == 1) {
                    progEstItemShowUpd.setProgEst_Pkid(progEstItemListTemp.get(0).getPkid());
                    progEstItemService.updateRecord(progEstItemShowUpd);
                }
                if (progEstItemListTemp.size()==0){
                    progEstItemShowUpd.setProgEst_TkcttItemPkid(progEstItemShowUpd.getTkctt_Pkid());
                    progEstItemService.insertRecord(progEstItemShowUpd);
                }
                MessageUtil.addInfo("����������ɡ�");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progEstItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("�ύ����ʧ�ܣ�",e);
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }

    public boolean blurProgEst_ThisStageQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getTkctt_Qty()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            BigDecimal bDProgEst_ThisStageQtyTemp=
                    ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getProgEst_ThisStageQty());
            BigDecimal bigDecimalTemp=
                    bDProgEst_AddUpQtyInDB.add(bDProgEst_ThisStageQtyTemp).subtract(bDProgEst_ThisStageQtyInDB);
            BigDecimal bDTkctt_Qty=
                    ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getTkctt_Qty());

            String strTemp= bDProgEst_ThisStageQtyTemp.toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!bDProgEst_ThisStageQtyTemp.toString().matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_Qty)>0){
                    MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                            + bDProgEst_ThisStageQtyTemp.toString() + "����");
                    return false;
                }
                if(!ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getProgEst_ThisStageQty()).equals( new BigDecimal(0))){
                    progEstItemShowUpd.setProgEst_AddUpQty(bigDecimalTemp);
                }
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                if(ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getProgEst_ThisStageQty()).equals( new BigDecimal(0))){
                    return false;
                }
                BigDecimal bDEngQMng_AddUpQtyTemp=
                        ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getProgEst_AddUpQty());

                if(bDEngQMng_AddUpQtyTemp.compareTo(bDProgEst_AddUpQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDTkctt_Qty)>0){
                        MessageUtil.addError("���ڲ����ۼƹ�Ӧ����+���ڲ��Ϲ�Ӧ����>��ͬ��������ȷ��������ı��ڲ��Ϲ�Ӧ������"
                                + bDProgEst_ThisStageQtyTemp.toString() + "����");
                        return false;
                    }
                }
                progEstItemShowUpd.setProgEst_ThisStageAmt(progEstItemShowUpd.getTkctt_UnitPrice().multiply(bDProgEst_ThisStageQtyTemp));
                progEstItemShowUpd.setProgEst_AddUpAmt(progEstItemShowUpd.getTkctt_UnitPrice().multiply(bigDecimalTemp));
            }
        }
        return true;
    }

    private void delRecordAction(ProgEstItemShow progEstItemShowPara){
        try {
            if(progEstItemShowPara.getProgEst_Pkid()==null||
                    progEstItemShowPara.getProgEst_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
                int deleteRecordNum= progEstItemService.deleteRecord(progEstItemShowPara.getProgEst_Pkid()) ;
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
                progEstItemShow =(ProgEstItemShow)BeanUtils.cloneBean(progEstItemShowSelected) ;
                progEstItemShow.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstItemShow.getTkctt_StrNo()));
            }
            String strTkctt_Unit= progEstItemShowSelected.getTkctt_Unit();
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
                progEstItemShowUpd =(ProgEstItemShow) BeanUtils.cloneBean(progEstItemShowSelected) ;
                progEstItemShowUpd.setProgEst_PorgEstInfoPkid(strProgEstInfoPkid);
                progEstItemShowUpd.setProgEst_TkcttItemPkid(progEstItemShowUpd.getTkctt_Pkid());
                progEstItemShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstItemShowUpd.getTkctt_StrNo()));
                bDProgEst_ThisStageQtyInDB=ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getProgEst_ThisStageQty());
                bDProgEst_AddUpQtyInDB=
                        ToolUtil.getBdIgnoreNull(progEstItemShowUpd.getProgEst_AddUpQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progEstItemShowDel =(ProgEstItemShow) BeanUtils.cloneBean(progEstItemShowSelected) ;
                progEstItemShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progEstItemShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*��ʼ������*/
    private void initData() {
        /*�ְ���ͬ*/
        List<TkcttItem> tkcttItemListTemp =new ArrayList<TkcttItem>();
        progEstItemShowListForExcel=new ArrayList<ProgEstItemShow>();
        tkcttItemListTemp = tkcttItemService.getTkcttItemListByTkcttInfoPkid(progEstInfo.getTkcttInfoPkid());
        if(tkcttItemListTemp.size()<=0){
            return;
        }
        progEstItemShowList =new ArrayList<ProgEstItemShow>();
        recursiveDataTable("root", tkcttItemListTemp, progEstItemShowList);
        progEstItemShowList =getItemStlTkcttEngSMList_DoFromatNo(progEstItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
        beansMap.put("progEstItemShowListForExcel", progEstItemShowListForExcel);
        // �������趨
        if(progEstItemShowList.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<TkcttItem> tkcttItemListPara,
                                      List<ProgEstItemShow> sprogEstItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<TkcttItem> tkcttItemList =new ArrayList<TkcttItem>();
        // ͨ������id�������ĺ���
        tkcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, tkcttItemListPara);
        BigDecimal bdUnitPrice=new BigDecimal(0);
        BigDecimal bdThisStageQty=new BigDecimal(0);
        BigDecimal bdAddUpQty=new BigDecimal(0);
        for(TkcttItem itemUnit: tkcttItemList){
            ProgEstItemShow progEstItemShowTemp = new ProgEstItemShow();
            progEstItemShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progEstItemShowTemp.setTkctt_TkcttInfoPkid(itemUnit.getTkcttInfoPkid());
            progEstItemShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progEstItemShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progEstItemShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progEstItemShowTemp.setTkctt_Name(itemUnit.getName());
            progEstItemShowTemp.setTkctt_Remark(itemUnit.getRemark());
            progEstItemShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progEstItemShowTemp.setTkctt_UnitPrice(itemUnit.getUnitPrice());
            bdUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getUnitPrice());
            progEstItemShowTemp.setTkctt_Qty(itemUnit.getQty());
            progEstItemShowTemp.setTkctt_Amt(itemUnit.getAmt());

            ProgEstItem progEstItem =new ProgEstItem();
            progEstItem.setTkcttItemPkid(progEstItemShowTemp.getTkctt_Pkid());

            progEstItem.setProgEstInfoPkid(progEstInfo.getPkid());

            List<ProgEstItem> progEstItemList =
                    progEstItemService.getProgEstItemListByModel(progEstItem);
            if(progEstItemList.size()>0){
                progEstItem = progEstItemList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(progEstItem.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(progEstItem.getUpdatedBy());
                progEstItemShowTemp.setProgEst_Pkid(progEstItem.getPkid());
                progEstItemShowTemp.setProgEst_PorgEstInfoPkid(progEstItem.getProgEstInfoPkid());
                progEstItemShowTemp.setProgEst_TkcttItemPkid(progEstItem.getTkcttItemPkid());
                progEstItemShowTemp.setProgEst_ThisStageQty(progEstItem.getThisStageQty());
                bdThisStageQty=ToolUtil.getBdIgnoreNull(progEstItem.getThisStageQty());
                progEstItemShowTemp.setProgEst_ThisStageAmt(bdUnitPrice.multiply(bdThisStageQty));
                progEstItemShowTemp.setProgEst_AddUpQty(progEstItem.getAddUpQty());
                bdAddUpQty=ToolUtil.getBdIgnoreNull(progEstItem.getAddUpQty());
                progEstItemShowTemp.setProgEst_AddUpAmt(bdUnitPrice.multiply(bdAddUpQty));
                progEstItemShowTemp.setProgEst_ArchivedFlag(progEstItem.getArchivedFlag());
                progEstItemShowTemp.setProgEst_CreatedBy(progEstItem.getCreatedBy());
                progEstItemShowTemp.setProgEst_CreatedByName(strCreatedByName);
                progEstItemShowTemp.setProgEst_CreatedTime(progEstItem.getCreatedTime());
                progEstItemShowTemp.setProgEst_UpdatedBy(progEstItem.getUpdatedBy());
                progEstItemShowTemp.setProgEst_UpdatedByName(strLastUpdByName);
                progEstItemShowTemp.setProgEst_UpdatedTime(progEstItem.getUpdatedTime());
                progEstItemShowTemp.setProgEst_RecVersion(progEstItem.getRecVersion());
            }
            sprogEstItemShowListPara.add(progEstItemShowTemp) ;
            recursiveDataTable(progEstItemShowTemp.getTkctt_Pkid(), tkcttItemListPara, sprogEstItemShowListPara);
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgEstItemShow> progEstItemShowListTemp =new ArrayList<ProgEstItemShow>();
        progEstItemShowListTemp.addAll(progEstItemShowList);

        progEstItemShowList.clear();
        // ��ͬ����С��
        BigDecimal bdQtyTotal=new BigDecimal(0);
        // ��ͬ�������
        BigDecimal bdQtyAllTotal=new BigDecimal(0);
        // ��ͬ���С��
        BigDecimal bdAmtTotal=new BigDecimal(0);
        // ��ͬ�����
        BigDecimal bdAmtAllTotal=new BigDecimal(0);

        // ��������С��
        BigDecimal bdAddUpQtyTotal=new BigDecimal(0);
        // �����������
        BigDecimal bdAddUpQtyAllTotal=new BigDecimal(0);
        // ��������С��
        BigDecimal bdThisStageQtyTotal=new BigDecimal(0);
        // �����������
        BigDecimal bdThisStageQtyAllTotal=new BigDecimal(0);

        // ���۽��С��
        BigDecimal bdAddUpAmtTotal=new BigDecimal(0);
        // ���۽����
        BigDecimal bdAddUpAmtAllTotal=new BigDecimal(0);
        // ���ڽ��С��
        BigDecimal bdThisStageAmtTotal=new BigDecimal(0);
        // ���ڽ����
        BigDecimal bdThisStageAmtAllTotal=new BigDecimal(0);

        ProgEstItemShow itemUnit=new ProgEstItemShow();
        ProgEstItemShow itemUnitNext=new ProgEstItemShow();

        for(int i=0;i< progEstItemShowListTemp.size();i++){
            itemUnit = progEstItemShowListTemp.get(i);
            bdQtyTotal=bdQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Qty()));
            bdQtyAllTotal=bdQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Qty()));
            bdAmtTotal=bdAmtTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));
            bdAmtAllTotal=bdAmtAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));

            bdAddUpQtyTotal=
                    bdAddUpQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_AddUpQty()));
            bdAddUpQtyAllTotal=
                    bdAddUpQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_AddUpQty()));
            bdThisStageQtyTotal=
                    bdThisStageQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_ThisStageQty()));
            bdThisStageQtyAllTotal=
                    bdThisStageQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_ThisStageQty()));

            bdAddUpAmtTotal=
                    bdAddUpAmtTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_AddUpAmt()));
            bdAddUpAmtAllTotal=
                    bdAddUpAmtAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_AddUpAmt()));
            bdThisStageAmtTotal=
                    bdThisStageAmtTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_ThisStageAmt()));
            bdThisStageAmtAllTotal=
                    bdThisStageAmtAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getProgEst_ThisStageAmt()));

            progEstItemShowList.add(itemUnit);

            if(i+1< progEstItemShowListTemp.size()){
                itemUnitNext = progEstItemShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgEstItemShow itemOfEsItemHieRelapTemp=new ProgEstItemShow();
                    itemOfEsItemHieRelapTemp.setTkctt_Name("�ϼ�");
                    itemOfEsItemHieRelapTemp.setTkctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setTkctt_Qty(bdQtyTotal);
                    itemOfEsItemHieRelapTemp.setTkctt_Amt(bdAmtTotal);
                    itemOfEsItemHieRelapTemp.setProgEst_AddUpQty(bdAddUpQtyTotal);
                    itemOfEsItemHieRelapTemp.setProgEst_ThisStageQty(bdThisStageQtyTotal);
                    itemOfEsItemHieRelapTemp.setProgEst_AddUpAmt(bdAddUpAmtTotal);
                    itemOfEsItemHieRelapTemp.setProgEst_ThisStageAmt(bdThisStageAmtTotal);
                    progEstItemShowList.add(itemOfEsItemHieRelapTemp);
                    bdQtyTotal=new BigDecimal(0);
                    bdAmtTotal=new BigDecimal(0);
                    bdAddUpQtyTotal=new BigDecimal(0);
                    bdThisStageQtyTotal=new BigDecimal(0);
                    bdAddUpAmtTotal=new BigDecimal(0);
                    bdThisStageAmtTotal=new BigDecimal(0);
                }
            } else if(i+1== progEstItemShowListTemp.size()){
                itemUnitNext = progEstItemShowListTemp.get(i);
                ProgEstItemShow progEstItemShowTemp =new ProgEstItemShow();
                progEstItemShowTemp.setTkctt_Name("�ϼ�");
                progEstItemShowTemp.setTkctt_Pkid("total"+i);
                progEstItemShowTemp.setTkctt_Qty(bdQtyTotal);
                progEstItemShowTemp.setTkctt_Amt(bdAmtTotal);

                progEstItemShowTemp.setProgEst_AddUpQty(bdAddUpQtyTotal);
                progEstItemShowTemp.setProgEst_ThisStageQty(bdThisStageQtyTotal);
                progEstItemShowTemp.setProgEst_AddUpAmt(bdAddUpAmtTotal);
                progEstItemShowTemp.setProgEst_ThisStageAmt(bdThisStageAmtTotal);
                progEstItemShowList.add(progEstItemShowTemp);
                bdQtyTotal=new BigDecimal(0);
                bdAmtTotal=new BigDecimal(0);
                bdAddUpQtyTotal=new BigDecimal(0);
                bdThisStageQtyTotal=new BigDecimal(0);
                bdAddUpAmtTotal=new BigDecimal(0);
                bdThisStageAmtTotal=new BigDecimal(0);

                // �ܺϼ�
                progEstItemShowTemp =new ProgEstItemShow();
                progEstItemShowTemp.setTkctt_Name("�ܺϼ�");
                progEstItemShowTemp.setTkctt_Pkid("total_all"+i);
                progEstItemShowTemp.setTkctt_Qty(bdQtyAllTotal);
                progEstItemShowTemp.setTkctt_Amt(bdAmtAllTotal);

                progEstItemShowTemp.setProgEst_AddUpQty(bdAddUpQtyAllTotal);
                progEstItemShowTemp.setProgEst_ThisStageQty(bdThisStageQtyAllTotal);
                progEstItemShowTemp.setProgEst_AddUpAmt(bdAddUpAmtAllTotal);
                progEstItemShowTemp.setProgEst_ThisStageAmt(bdThisStageAmtAllTotal);
                progEstItemShowList.add(progEstItemShowTemp);
            }
        }
    }

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgEstItemShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgEstItemShow> progEstItemShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgEstItemShow itemUnit: progEstItemShowListPara){
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

                ProgEstItemShow itemUnitTemp= (ProgEstItemShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progEstItemShowListForExcel.add(itemUnitTemp);
            }
        }
        catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
        return progEstItemShowListPara;
    }


    public String onExportExcel()throws IOException, WriteException {
        if (this.progEstItemShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ�����ͳ������-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlTkcttEst.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }

    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<TkcttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
             List<TkcttItem> tkcttItemListPara) {
        List<TkcttItem> tempTkcttItemList =new ArrayList<TkcttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(TkcttItem itemUnit: tkcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempTkcttItemList.add(itemUnit);
            }
        }
        return tempTkcttItemList;
    }

    /* �����ֶ�Start*/
    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public TkcttItemService getTkcttItemService() {
        return tkcttItemService;
    }

    public void setTkcttItemService(TkcttItemService tkcttItemService) {
        this.tkcttItemService = tkcttItemService;
    }    

    public ProgEstItemService getProgEstItemService() {
        return progEstItemService;
    }

    public void setProgEstItemService(ProgEstItemService ProgEstItemService) {
        this.progEstItemService = ProgEstItemService;
    }

    public ProgEstInfoService getProgEstInfoService() {
        return progEstInfoService;
    }

    public void setProgEstInfoService(ProgEstInfoService progEstInfoService) {
        this.progEstInfoService = progEstInfoService;
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

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }
    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public BigDecimal getbDProgEst_AddUpQtyInDB() {
        return bDProgEst_AddUpQtyInDB;
    }

    public void setbDProgEst_AddUpQtyInDB(BigDecimal bDProgEst_AddUpQtyInDB) {
        this.bDProgEst_AddUpQtyInDB = bDProgEst_AddUpQtyInDB;
    }

    public BigDecimal getbDProgEst_ThisStageQtyInDB() {
        return bDProgEst_ThisStageQtyInDB;
    }

    public void setbDProgEst_ThisStageQtyInDB(BigDecimal bDProgEst_ThisStageQtyInDB) {
        this.bDProgEst_ThisStageQtyInDB = bDProgEst_ThisStageQtyInDB;
    }

    public ProgEstItemShow getProgEstItemShow() {
        return progEstItemShow;
    }

    public void setProgEstItemShow(ProgEstItemShow progEstItemShow) {
        this.progEstItemShow = progEstItemShow;
    }

    public ProgEstItemShow getProgEstItemShowDel() {
        return progEstItemShowDel;
    }

    public void setProgEstItemShowDel(ProgEstItemShow progEstItemShowDel) {
        this.progEstItemShowDel = progEstItemShowDel;
    }

    public ProgEstItemShow getProgEstItemShowSelected() {
        return progEstItemShowSelected;
    }

    public void setProgEstItemShowSelected(ProgEstItemShow progEstItemShowSelected) {
        this.progEstItemShowSelected = progEstItemShowSelected;
    }

    public ProgEstItemShow getProgEstItemShowUpd() {
        return progEstItemShowUpd;
    }

    public void setProgEstItemShowUpd(ProgEstItemShow progEstItemShowUpd) {
        this.progEstItemShowUpd = progEstItemShowUpd;
    }

    public List<ProgEstItemShow> getProgEstItemShowList() {
        return progEstItemShowList;
    }

    public void setProgEstItemShowList(List<ProgEstItemShow> progEstItemShowList) {
        this.progEstItemShowList = progEstItemShowList;
    }

    public List<ProgEstItemShow> getProgEstItemShowListForExcel() {
        return progEstItemShowListForExcel;
    }

    public void setProgEstItemShowListForExcel(List<ProgEstItemShow> progEstItemShowListForExcel) {
        this.progEstItemShowListForExcel = progEstItemShowListForExcel;
    }

    /*�����ֶ�End*/
}
