package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgMatqtyInfoShow;
import epss.repository.model.model_show.ProgMatqtyItemShow;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
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
public class ProgMatqtyItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMatqtyItemAction.class);
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{subcttItemService}")
    private SubcttItemService subcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;

    private List<ProgMatqtyItemShow> progMatqtyItemShowList;
    private ProgMatqtyItemShow progMatqtyItemShow;
    private ProgMatqtyItemShow progMatqtyItemShowUpd;
    private ProgMatqtyItemShow progMatqtyItemShowDel;
    private ProgMatqtyItemShow progMatqtyItemShowSelected;
    private BigDecimal bDProgMat_AddUpQtyInDB;
    private BigDecimal bDProgMat_ThisStageQtyInDB;

    private String strSubmitType;
    private ProgMatqtyInfo progMatqtyInfo;
    private String strProgMatqtyInfoPkid;

    /*����ά������㼶���ֵ���ʾ*/
    private String strMngNotFinishFlag;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strProgMatqtyInfoPkid")){
            strProgMatqtyInfoPkid=parammap.get("strProgMatqtyInfoPkid").toString();
            progMatqtyInfo = progMatqtyInfoService.selectRecordsByPrimaryKey(strProgMatqtyInfoPkid);
        }
        strMngNotFinishFlag="true";
        if(progMatqtyInfo!=null&&EnumFlowStatus.FLOW_STATUS0.getCode().equals(progMatqtyInfo.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }
        resetAction();
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        /*�ְ���ͬ*/
        List<SubcttItem> cttItemList =new ArrayList<SubcttItem>();
        cttItemList = subcttItemService.getSubcttItemListBySubcttInfoPkid(progMatqtyInfo.getSubcttInfoPkid());
        if(cttItemList.size()<=0){
            return;
        }
        progMatqtyItemShowList =new ArrayList<ProgMatqtyItemShow>();
        recursiveDataTable("root", cttItemList, progMatqtyItemShowList);
        progMatqtyItemShowList =getStlSubCttProgMatConstructList_DoFromatNo(progMatqtyItemShowList);
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<SubcttItem> subcttItemListPara,
                                    List<ProgMatqtyItemShow> progMatqtyItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<SubcttItem> subSubcttItemList =new ArrayList<SubcttItem>();
        // ͨ������id�������ĺ���
        subSubcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, subcttItemListPara);
        for(SubcttItem itemUnit: subSubcttItemList){
            ProgMatqtyItemShow progMatqtyItemShowTemp = new ProgMatqtyItemShow();
            progMatqtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progMatqtyItemShowTemp.setSubctt_SubcttInfoPkid(itemUnit.getSubcttInfoPkid());
            progMatqtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progMatqtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progMatqtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progMatqtyItemShowTemp.setSubctt_CstplItemPkid(itemUnit.getCstplItemPkid());
            progMatqtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progMatqtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progMatqtyItemShowTemp.setSubctt_UnitPrice(itemUnit.getUnitPrice());
            progMatqtyItemShowTemp.setSubctt_Qty(itemUnit.getQty());
            progMatqtyItemShowTemp.setSubctt_Amt(itemUnit.getAmt());
            progMatqtyItemShowTemp.setSubctt_SignPartPriceA(itemUnit.getSignPartPriceA());
            progMatqtyItemShowTemp.setSubctt_Remark(itemUnit.getRemark());
            ProgMatqtyItem progMatqtyItem =new ProgMatqtyItem();
            progMatqtyItem.setProgMatqtyInfoPkid(progMatqtyInfo.getPkid());
            progMatqtyItem.setSubcttItemPkid(itemUnit.getPkid());
            List<ProgMatqtyItem> progMatqtyItemList =
                    progMatqtyItemService.selectRecordsByExample(progMatqtyItem);
            if(progMatqtyItemList.size()>0){
                progMatqtyItem = progMatqtyItemList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(progMatqtyItem.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(progMatqtyItem.getUpdatedBy());
                progMatqtyItemShowTemp.setProgMat_Pkid(progMatqtyItem.getPkid());
                progMatqtyItemShowTemp.setProgMat_ProgMatqtyInfoPkid(progMatqtyItem.getProgMatqtyInfoPkid());
                progMatqtyItemShowTemp.setProgMat_SubcttItemPkid(progMatqtyItem.getSubcttItemPkid());
                progMatqtyItemShowTemp.setProgMat_AddUpQty(progMatqtyItem.getAddUpQty());
                progMatqtyItemShowTemp.setProgMat_ThisStageQty(progMatqtyItem.getThisStageQty());
                progMatqtyItemShowTemp.setProgMat_MPurchaseUnitPrice(progMatqtyItem.getmPurchaseUnitPrice());
                progMatqtyItemShowTemp.setProgMat_ArchivedFlag(progMatqtyItem.getArchivedFlag());
                progMatqtyItemShowTemp.setProgMat_CreatedBy(progMatqtyItem.getCreatedBy());
                progMatqtyItemShowTemp.setProgMat_CreatedByName(strCreatedByName);
                progMatqtyItemShowTemp.setProgMat_CreatedTime(progMatqtyItem.getCreatedTime());
                progMatqtyItemShowTemp.setProgMat_UpdatedBy(progMatqtyItem.getUpdatedBy());
                progMatqtyItemShowTemp.setProgMat_UpdatedByName(strLastUpdByName);
                progMatqtyItemShowTemp.setProgMat_UpdatedTime(progMatqtyItem.getUpdatedTime());
                progMatqtyItemShowTemp.setProgMat_RecVersion(progMatqtyItem.getRecVersion());
            }
            progMatqtyItemShowListPara.add(progMatqtyItemShowTemp) ;
            recursiveDataTable(progMatqtyItemShowTemp.getSubctt_Pkid(), subcttItemListPara, progMatqtyItemShowListPara);
        }
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgMatqtyItemShow> getStlSubCttProgMatConstructList_DoFromatNo(
            List<ProgMatqtyItemShow> progMatqtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgMatqtyItemShow itemUnit: progMatqtyItemShowListPara){
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
        return progMatqtyItemShowListPara;
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<SubcttItem> getEsItemHieRelapListByLevelParentPkid(
            String strLevelParentPkid,
            List<SubcttItem> cttItemListPara) {
        List<SubcttItem> tempSubcttItemList =new ArrayList<SubcttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(SubcttItem itemUnit: cttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempSubcttItemList.add(itemUnit);
            }
        }
        return tempSubcttItemList;
    }

    /*����*/
    public void resetAction(){
        progMatqtyItemShow =new ProgMatqtyItemShow();
        progMatqtyItemShowUpd =new ProgMatqtyItemShow();
        progMatqtyItemShowDel =new ProgMatqtyItemShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurProgMat_ThisStageQty("submit")){
                    MessageUtil.addInfo("��¼�뱾�ڹ�������");
                    return;
                }
                List<ProgMatqtyItem> progMatqtyItemListTemp =
                        progMatqtyItemService.isExistInDb(progMatqtyItemShowUpd);
                if (progMatqtyItemListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (progMatqtyItemListTemp.size() == 1) {
                    progMatqtyItemShowUpd.setProgMat_Pkid (progMatqtyItemListTemp.get(0).getPkid());
                    updRecordAction(progMatqtyItemShowUpd);
                } else if (progMatqtyItemListTemp.size()==0){
                    addRecordAction(progMatqtyItemShowUpd);
                }
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progMatqtyItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
            logger.error("��������ʧ�ܣ�", e);
        }
    }

    public boolean blurProgMat_ThisStageQty(String strBlurOrSubmitFlag){

        if(ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getSubctt_Qty()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp=ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_ThisStageQty()).toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.toString().matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }
            BigDecimal bDEngQMng_ThisStageQtyTemp= 
			ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_ThisStageQty());

            BigDecimal bigDecimalTemp= 
			         bDProgMat_AddUpQtyInDB.add(bDEngQMng_ThisStageQtyTemp).subtract(bDProgMat_ThisStageQtyInDB);

            BigDecimal bDSubctt_Qty= ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getSubctt_Qty());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_Qty)>0){
                    MessageUtil.addError("���ڲ����ۼƹ�Ӧ����+���ڲ��Ϲ�Ӧ����>��ͬ����������ȷ��������ı��ڲ��Ϲ�Ӧ������"
                            + bDEngQMng_ThisStageQtyTemp.toString() + "����");
                    return false;
                 }
                if(!ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_ThisStageQty()).equals( new BigDecimal(0))){
                    progMatqtyItemShowUpd.setProgMat_AddUpQty(bigDecimalTemp);
                }
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                if(ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_ThisStageQty()).equals( new BigDecimal(0))){
                    return false;
                }
                BigDecimal bDEngQMng_AddUpQtyTemp=
                        ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_AddUpQty());

                if(bDEngQMng_AddUpQtyTemp.compareTo(bDProgMat_AddUpQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_Qty)>0){
                        MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                                + bDEngQMng_ThisStageQtyTemp.toString() + "����");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void addRecordAction(ProgMatqtyItemShow progMatqtyItemShowPara){
        try {
            progMatqtyItemService.insertRecord(progMatqtyItemShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgMatqtyItemShow progMatqtyItemShowPara){
        try {
            progMatqtyItemService.updateRecord(progMatqtyItemShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void delRecordAction(ProgMatqtyItemShow progMatqtyItemShowPara){
        try {
            if(progMatqtyItemShowPara.getProgMat_Pkid()==null||
                    progMatqtyItemShowDel.getProgMat_Pkid().equals("")){
                MessageUtil.addError("�޿�ɾ�������ݣ�") ;
            }else{
                int deleteRecordNum = progMatqtyItemService.deleteRecord(
                        progMatqtyItemShowDel.getProgMat_Pkid());
                if (deleteRecordNum <= 0) {
                    MessageUtil.addInfo("�ü�¼��ɾ����");
                } else {
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
            BigDecimal bdSubctt_SignPartPriceA= ToolUtil.getBdIgnoreNull(
                    progMatqtyItemShowSelected.getSubctt_SignPartPriceA());
            BigDecimal bdSubctt_Qty= ToolUtil.getBdIgnoreNull(
                    progMatqtyItemShowSelected.getSubctt_Qty());
            if(strSubmitTypePara.equals("Sel")){
                progMatqtyItemShow =(ProgMatqtyItemShow)BeanUtils.cloneBean(progMatqtyItemShowSelected) ;
                progMatqtyItemShow.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatqtyItemShow.getSubctt_StrNo()));
            }else
            if(strSubmitTypePara.equals("Upd")){
                if(bdSubctt_SignPartPriceA.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_Qty.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�����");
                    return;
                }
                progMatqtyItemShowUpd =(ProgMatqtyItemShow) BeanUtils.cloneBean(progMatqtyItemShowSelected) ;
                progMatqtyItemShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatqtyItemShowUpd.getSubctt_StrNo()));
                progMatqtyItemShowUpd.setProgMat_ProgMatqtyInfoPkid(ToolUtil.getIgnoreSpaceOfStr(strProgMatqtyInfoPkid));
                progMatqtyItemShowUpd.setProgMat_SubcttItemPkid(ToolUtil.getIgnoreSpaceOfStr(progMatqtyItemShowUpd.getSubctt_Pkid()));
                bDProgMat_ThisStageQtyInDB=ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_ThisStageQty());
                bDProgMat_AddUpQtyInDB=
                        ToolUtil.getBdIgnoreNull(progMatqtyItemShowUpd.getProgMat_AddUpQty());
            }else
            if(strSubmitTypePara.equals("Del")){
                if(bdSubctt_SignPartPriceA.compareTo(ToolUtil.bigDecimal0)==0||
                        bdSubctt_Qty.compareTo(ToolUtil.bigDecimal0)==0) {
                    MessageUtil.addInfo("�����ݲ��ǹ��̲������ݣ��޷�ɾ��");
                    return;
                }
                progMatqtyItemShowDel =(ProgMatqtyItemShow) BeanUtils.cloneBean(progMatqtyItemShowSelected) ;
                progMatqtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progMatqtyItemShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /* �����ֶ�Start*/
    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public SubcttItemService getSubcttItemService() {
        return subcttItemService;
    }

    public void setSubcttItemService(SubcttItemService subcttItemService) {
        this.subcttItemService = subcttItemService;
    }

    public ProgMatqtyItemShow getProgMatqtyItemShow() {
        return progMatqtyItemShow;
    }

    public void setProgMatqtyItemShow(ProgMatqtyItemShow progMatqtyItemShow) {
        this.progMatqtyItemShow = progMatqtyItemShow;
    }

    public ProgMatqtyItemShow getProgMatqtyItemShowSelected() {
        return progMatqtyItemShowSelected;
    }

    public void setProgMatqtyItemShowSelected(ProgMatqtyItemShow progMatqtyItemShowSelected) {
        this.progMatqtyItemShowSelected = progMatqtyItemShowSelected;
    }

    public List<ProgMatqtyItemShow> getProgMatqtyItemShowList() {
        return progMatqtyItemShowList;
    }

    public void setProgMatqtyItemShowList(List<ProgMatqtyItemShow> progMatqtyItemShowList) {
        this.progMatqtyItemShowList = progMatqtyItemShowList;
    }

    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
    }

    public ProgMatqtyInfoService getProgMatqtyInfoService() {
        return progMatqtyInfoService;
    }

    public void setProgMatqtyInfoService(ProgMatqtyInfoService progMatqtyInfoService) {
        this.progMatqtyInfoService = progMatqtyInfoService;
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

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public ProgMatqtyItemShow getProgMatqtyItemShowDel() {
        return progMatqtyItemShowDel;
    }

    public void setProgMatqtyItemShowDel(ProgMatqtyItemShow progMatqtyItemShowDel) {
        this.progMatqtyItemShowDel = progMatqtyItemShowDel;
    }

    public ProgMatqtyItemShow getProgMatqtyItemShowUpd() {
        return progMatqtyItemShowUpd;
    }

    public void setProgMatqtyItemShowUpd(ProgMatqtyItemShow progMatqtyItemShowUpd) {
        this.progMatqtyItemShowUpd = progMatqtyItemShowUpd;
    }

    public String getStrProgMatqtyInfoPkid() {
        return strProgMatqtyInfoPkid;
    }

    public void setStrProgMatqtyInfoPkid(String strProgMatqtyInfoPkid) {
        this.strProgMatqtyInfoPkid = strProgMatqtyInfoPkid;
    }
    /*�����ֶ�End*/
}
