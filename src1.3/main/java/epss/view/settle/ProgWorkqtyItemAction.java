package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.ProgWorkqtyInfoShow;
import epss.repository.model.model_show.ProgWorkqtyItemShow;
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
public class ProgWorkqtyItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgWorkqtyItemAction.class);
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{subcttItemService}")
    private SubcttItemService subcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;

    private List<ProgWorkqtyItemShow> progWorkqtyItemShowList;
    private ProgWorkqtyItemShow progWorkqtyItemShow;
    private ProgWorkqtyItemShow progWorkqtyItemShowUpd;
    private ProgWorkqtyItemShow progWorkqtyItemShowDel;
    private ProgWorkqtyItemShow progWorkqtyItemShowSelected;
    private BigDecimal bdProgWork_AddUpQtyInDB;
    private BigDecimal bdProgWork_ThisStageQtyInDB;

    private String strSubmitType;
    private ProgWorkqtyInfo progWorkqtyInfo;

    /*����ά������㼶���ֵ���ʾ*/
    private String strMngNotFinishFlag;
    private String strProgWorkqtyInfoPkid;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strProgWorkqtyInfoPkid")){
            strProgWorkqtyInfoPkid=parammap.get("strProgWorkqtyInfoPkid").toString();
            progWorkqtyInfo = progWorkqtyInfoService.selectRecordsByPrimaryKey(strProgWorkqtyInfoPkid);
        }
        strMngNotFinishFlag="true";
        if(progWorkqtyInfo!=null&&EnumFlowStatus.FLOW_STATUS0.getCode().equals(progWorkqtyInfo.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }
        resetAction();
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        /*�ְ���ͬ*/
        List<SubcttItem> cttItemList =new ArrayList<SubcttItem>();
        cttItemList = subcttItemService.getSubcttItemListBySubcttInfoPkid(progWorkqtyInfo.getSubcttInfoPkid());
        if(cttItemList.size()<=0){
            return;
        }
        progWorkqtyItemShowList =new ArrayList<ProgWorkqtyItemShow>();
        recursiveDataTable("root", cttItemList, progWorkqtyItemShowList);
        progWorkqtyItemShowList =getProgWorkqtyItemShowList_DoFromatNo(progWorkqtyItemShowList);
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<SubcttItem> cttItemListPara,
                                    List<ProgWorkqtyItemShow> progWorkqtyItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<SubcttItem> subSubcttItemList =new ArrayList<SubcttItem>();
        // ͨ������id�������ĺ���
        subSubcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, cttItemListPara);
        for(SubcttItem itemUnit: subSubcttItemList){
            ProgWorkqtyItemShow progWorkqtyItemShowTemp = new ProgWorkqtyItemShow();
            progWorkqtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progWorkqtyItemShowTemp.setSubctt_SubcttInfoPkid(itemUnit.getSubcttInfoPkid());
            progWorkqtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progWorkqtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progWorkqtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progWorkqtyItemShowTemp.setSubctt_CstplItemPkid(itemUnit.getCstplItemPkid());
            progWorkqtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progWorkqtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progWorkqtyItemShowTemp.setSubctt_UnitPrice(itemUnit.getUnitPrice());
            progWorkqtyItemShowTemp.setSubctt_Qty(itemUnit.getQty());
            progWorkqtyItemShowTemp.setSubctt_Amt(itemUnit.getAmt());
            progWorkqtyItemShowTemp.setSubctt_SignPartPriceA(itemUnit.getSignPartPriceA());
			progWorkqtyItemShowTemp.setSubctt_Remark(itemUnit.getRemark());

            ProgWorkqtyItem progWorkqtyItem =new ProgWorkqtyItem();
            progWorkqtyItem.setProgWorkqtyInfoPkid(progWorkqtyInfo.getPkid());
            progWorkqtyItem.setSubcttItemPkid(progWorkqtyItemShowTemp.getSubctt_Pkid());
             List<ProgWorkqtyItem> progWorkqtyItemList =
                    progWorkqtyItemService.selectRecordsByExample(progWorkqtyItem);
            if(progWorkqtyItemList.size()>0){

                progWorkqtyItem = progWorkqtyItemList.get(0);
                String strCreatedByName= esCommon.getOperNameByOperId(progWorkqtyItem.getCreatedBy());
                String strLastUpdByName= esCommon.getOperNameByOperId(progWorkqtyItem.getUpdatedBy());
                progWorkqtyItemShowTemp.setProgWork_Pkid(progWorkqtyItem.getPkid());
                progWorkqtyItemShowTemp.setProgWork_ProgWorkqtyInfoPkid(progWorkqtyItem.getProgWorkqtyInfoPkid());
                progWorkqtyItemShowTemp.setProgWork_SubcttItemPkid(progWorkqtyItem.getSubcttItemPkid());
                progWorkqtyItemShowTemp.setProgWork_AddUpQty(progWorkqtyItem.getAddUpQty());
                progWorkqtyItemShowTemp.setProgWork_ThisStageQty(progWorkqtyItem.getThisStageQty());
                progWorkqtyItemShowTemp.setProgWork_ArchivedFlag(progWorkqtyItem.getArchivedFlag());
                progWorkqtyItemShowTemp.setProgWork_CreatedBy(progWorkqtyItem.getCreatedBy());
                progWorkqtyItemShowTemp.setProgWork_CreatedByName(strCreatedByName);
                progWorkqtyItemShowTemp.setProgWork_CreatedTime(progWorkqtyItem.getCreatedTime());
                progWorkqtyItemShowTemp.setProgWork_UpdatedBy(strLastUpdByName);
                progWorkqtyItemShowTemp.setProgWork_UpdatedByName(strLastUpdByName);
                progWorkqtyItemShowTemp.setProgWork_UpdatedTime(progWorkqtyItem.getUpdatedTime());
                progWorkqtyItemShowTemp.setProgWork_RecVersion(progWorkqtyItem.getRecVersion());
            }
            progWorkqtyItemShowListPara.add(progWorkqtyItemShowTemp) ;
            recursiveDataTable(progWorkqtyItemShowTemp.getSubctt_Pkid(), cttItemListPara, progWorkqtyItemShowListPara);
        }
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgWorkqtyItemShow> getProgWorkqtyItemShowList_DoFromatNo(
            List<ProgWorkqtyItemShow> progWorkqtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgWorkqtyItemShow itemUnit: progWorkqtyItemShowListPara){
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
        return progWorkqtyItemShowListPara;
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
        progWorkqtyItemShow =new ProgWorkqtyItemShow();
        progWorkqtyItemShowUpd =new ProgWorkqtyItemShow();
        progWorkqtyItemShowDel =new ProgWorkqtyItemShow();
        strSubmitType="";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurProgWork_CurrentPeriodMQty("submit")){
                    MessageUtil.addInfo("��¼�뱾�ڹ�������");
                    return;
                }
                List<ProgWorkqtyItem> progWorkqtyItemListTemp =
                        progWorkqtyItemService.isExistInDb(progWorkqtyItemShowUpd);
                if (progWorkqtyItemListTemp.size() > 1) {
                    MessageUtil.addInfo("�����������ݿ��д��ڶ�����¼��");
                    return;
                }
                if (progWorkqtyItemListTemp.size() == 1) {
                    progWorkqtyItemShowUpd.setProgWork_Pkid (progWorkqtyItemListTemp.get(0).getPkid());
                    progWorkqtyItemService.updateRecord(progWorkqtyItemShowUpd);
                }
                if (progWorkqtyItemListTemp.size()==0){
                    progWorkqtyItemShowUpd.setProgWork_SubcttItemPkid(progWorkqtyItemShowUpd.getSubctt_Pkid());
                    progWorkqtyItemService.insertRecord(progWorkqtyItemShowUpd);
                }
                MessageUtil.addInfo("����������ɡ�");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progWorkqtyItemShowDel);
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }

    public boolean blurProgWork_CurrentPeriodMQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getSubctt_Qty()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp=ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_ThisStageQty()).toString();
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!strTemp.matches(strRegex) ){
                MessageUtil.addError("��ȷ����������ݣ�" + strTemp + "������ȷ�����ݸ�ʽ��");
                return false;
            }

            BigDecimal bDProgWork_ThisStageQtyTemp=
                    ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_ThisStageQty());

            BigDecimal bigDecimalTemp=
                    bdProgWork_AddUpQtyInDB.add(bDProgWork_ThisStageQtyTemp).subtract(bdProgWork_ThisStageQtyInDB);

            BigDecimal bDSubctt_Qty=
                    ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getSubctt_Qty());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDSubctt_Qty)>0){
                    MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                            + bDProgWork_ThisStageQtyTemp.toString() + "����");
                    return false;
                }
                if (!ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_ThisStageQty()).equals(new BigDecimal(0))) {
                    progWorkqtyItemShowUpd.setProgWork_AddUpQty(bigDecimalTemp);
                }
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                if(ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_ThisStageQty()).equals( new BigDecimal(0))){
                    return false;
                }
                BigDecimal bDProgWork_AddUpQtyTemp=
                        ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_AddUpQty());

                if(bDProgWork_AddUpQtyTemp.compareTo(bdProgWork_AddUpQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDSubctt_Qty)>0){
                        MessageUtil.addError("���ڿ��۹�������+���ڹ�������>��ͬ��������ȷ��������ı��ڹ���������"
                                + bDProgWork_ThisStageQtyTemp.toString() + "����");
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }
    private void delRecordAction(ProgWorkqtyItemShow progWorkqtyItemShowPara){
        try {
            int deleteRecordNum= progWorkqtyItemService.deleteRecord(progWorkqtyItemShowPara.getProgWork_Pkid());
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                progWorkqtyItemShow =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowSelected) ;
                progWorkqtyItemShow.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShow.getSubctt_StrNo()));
            }
            else if(strSubmitTypePara.equals("Upd")){
                progWorkqtyItemShowUpd =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowSelected) ;
                progWorkqtyItemShowUpd.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowUpd.getSubctt_StrNo()));
                progWorkqtyItemShowUpd.setProgWork_ProgWorkqtyInfoPkid(ToolUtil.getIgnoreSpaceOfStr(strProgWorkqtyInfoPkid));
                progWorkqtyItemShowUpd.setProgWork_SubcttItemPkid(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowUpd.getSubctt_Pkid()));
                bdProgWork_ThisStageQtyInDB=ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_ThisStageQty());
                bdProgWork_AddUpQtyInDB=
                        ToolUtil.getBdIgnoreNull(progWorkqtyItemShowUpd.getProgWork_AddUpQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progWorkqtyItemShowDel =(ProgWorkqtyItemShow) BeanUtils.cloneBean(progWorkqtyItemShowSelected) ;
                progWorkqtyItemShowDel.setSubctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progWorkqtyItemShowDel.getSubctt_StrNo()));
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public void setStrMngNotFinishFlag(String strMngNotFinishFlag) {
        this.strMngNotFinishFlag = strMngNotFinishFlag;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShow() {
        return progWorkqtyItemShow;
    }

    public void setProgWorkqtyItemShow(ProgWorkqtyItemShow progWorkqtyItemShow) {
        this.progWorkqtyItemShow = progWorkqtyItemShow;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowDel() {
        return progWorkqtyItemShowDel;
    }

    public void setProgWorkqtyItemShowDel(ProgWorkqtyItemShow progWorkqtyItemShowDel) {
        this.progWorkqtyItemShowDel = progWorkqtyItemShowDel;
    }

    public List<ProgWorkqtyItemShow> getProgWorkqtyItemShowList() {
        return progWorkqtyItemShowList;
    }

    public void setProgWorkqtyItemShowList(List<ProgWorkqtyItemShow> progWorkqtyItemShowList) {
        this.progWorkqtyItemShowList = progWorkqtyItemShowList;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowSelected() {
        return progWorkqtyItemShowSelected;
    }

    public void setProgWorkqtyItemShowSelected(ProgWorkqtyItemShow progWorkqtyItemShowSelected) {
        this.progWorkqtyItemShowSelected = progWorkqtyItemShowSelected;
    }

    public ProgWorkqtyItemShow getProgWorkqtyItemShowUpd() {
        return progWorkqtyItemShowUpd;
    }

    public void setProgWorkqtyItemShowUpd(ProgWorkqtyItemShow progWorkqtyItemShowUpd) {
        this.progWorkqtyItemShowUpd = progWorkqtyItemShowUpd;
    }

    public BigDecimal getBdProgWork_AddUpQtyInDB() {
        return bdProgWork_AddUpQtyInDB;
    }

    public void setBdProgWork_AddUpQtyInDB(BigDecimal bdProgWork_AddUpQtyInDB) {
        this.bdProgWork_AddUpQtyInDB = bdProgWork_AddUpQtyInDB;
    }

    public BigDecimal getBdProgWork_ThisStageQtyInDB() {
        return bdProgWork_ThisStageQtyInDB;
    }

    public void setBdProgWork_ThisStageQtyInDB(BigDecimal bdProgWork_ThisStageQtyInDB) {
        this.bdProgWork_ThisStageQtyInDB = bdProgWork_ThisStageQtyInDB;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgWorkqtyInfo getProgWorkqtyInfo() {
        return progWorkqtyInfo;
    }

    public void setProgWorkqtyInfo(ProgWorkqtyInfo progWorkqtyInfo) {
        this.progWorkqtyInfo = progWorkqtyInfo;
    }

    public ProgWorkqtyInfoService getProgWorkqtyInfoService() {
        return progWorkqtyInfoService;
    }

    public void setProgWorkqtyInfoService(ProgWorkqtyInfoService progWorkqtyInfoService) {
        this.progWorkqtyInfoService = progWorkqtyInfoService;
    }

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public SubcttItemService getSubcttItemService() {
        return subcttItemService;
    }

    public void setSubcttItemService(SubcttItemService subcttItemService) {
        this.subcttItemService = subcttItemService;
    }
/* �����ֶ�Start*/


    /*�����ֶ�End*/
}

