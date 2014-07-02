package epss.view.item;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.common.enums.*;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.CttItemShow;
import epss.repository.model.EsInitPower;
import epss.service.*;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;
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

@ManagedBean
@ViewScoped
public class ItemSubcttAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemSubcttAction.class);
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    /*�򿪵ĳɱ��ƻ�ҳ����*/
    private List<CttItemShow> cttItemShowList_Cstpl;
    private CttItemShow cttItemShowSelected_Cstpl;

    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    private List<EsCttItem> esCttItemList;
    /*�б���ѡ��һ��*/
    private CttItemShow cttItemShowSelected;
	/*�б���ʾ��*/
    private List<CttItemShow> cttItemShowList;

    /*��������*/
    private String strBelongToType;
    /*������*/
    private String strBelongToPkid;

    /*�ύ����*/
    private String strSubmitType;
    private String strMngNotFinishFlag;
    private String strPasteType;

    /*���ƿؼ��ڻ����ϵĿ�������ʵStart*/
    //��ʾ�Ŀ���
	private StyleModel styleModelNo;
    private StyleModel styleModel;
    // �̶�������ʱ���ƿؼ��Ŀ���
    private StyleModel styleModelCttQty;
    private StyleModel styleModelCttAmount;
    /*���ƿؼ��ڻ����ϵĿ�������ʵEnd*/

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        strBelongToType=ESEnum.ITEMTYPE2.getCode();
        strBelongToPkid=parammap.get("strSubCttPkid").toString();

		List<EsInitPower> esInitPowerList=
                esInitPowerService.selectListByModel(strBelongToType,strBelongToPkid,"NULL");
        strMngNotFinishFlag="true";
        if(esInitPowerList.size()>0){
            if(esInitPowerList.get(0).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())) {
                strMngNotFinishFlag="false";
            }
        }
        resetAction();
        initData() ;
    }

    /*��ʼ������*/
    private void initData() {
        /*�ɱ��ƻ�*/
        cttItemShowList_Cstpl =new ArrayList<CttItemShow>();
        EsCttInfo esCttInfo = esCttInfoService.getCttInfoByPkId(strBelongToPkid);
        String strCstplPkidInInitCtt= esCttInfo.getParentPkid() ;
        esCttItemList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE1.getCode(), strCstplPkidInInitCtt);
        recursiveDataTable("root", esCttItemList, cttItemShowList_Cstpl);
        cttItemShowList_Cstpl =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList_Cstpl);
        /*�ְ���ͬ*/
        esCttItemList =new ArrayList<EsCttItem>();
        cttItemShowList =new ArrayList<CttItemShow>();
        esCttItemList = esCttItemService.getEsItemList(
                strBelongToType, strBelongToPkid);
        cttItemShowList.clear();
        recursiveDataTable("root", esCttItemList, cttItemShowList);
        cttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList);
        /*�ְ���ͬ��Ӧ�ɱ��ƻ��е���*/
        for(CttItemShow itemUnit: cttItemShowList){
            for(CttItemShow itemUnitCstpl: cttItemShowList_Cstpl){
                if(itemUnit.getCorrespondingPkid()!=null&&
                        itemUnit.getCorrespondingPkid().equals(itemUnitCstpl .getPkid())){
                    itemUnit.setStrCorrespondingItemNo(itemUnitCstpl.getStrNo());
                    itemUnit.setStrCorrespondingItemName(itemUnitCstpl .getName());
                }
            }
        }
        // ��Ӻϼ�
        setItemOfCstplAndSubcttList_AddTotal();
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<CttItemShow> cttItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
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
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), esCttItemListPara, cttItemShowListPara);
        }
    }
    private void setItemOfCstplAndSubcttList_AddTotal(){
        List<CttItemShow> cttItemShowListTemp =new ArrayList<CttItemShow>();
        cttItemShowListTemp.addAll(cttItemShowList);

        cttItemShowList.clear();
        // С��
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        CttItemShow itemUnit=new CttItemShow();
        CttItemShow itemUnitNext=new CttItemShow();
        for(int i=0;i< cttItemShowListTemp.size();i++){
            itemUnit = cttItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            if(itemUnit.getUnit()!=null&&!itemUnit.getUnit().equals("")){
                bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            }
            cttItemShowList.add(itemUnit);

            if(i+1< cttItemShowListTemp.size()){
                itemUnitNext = cttItemShowListTemp.get(i+1);
                CttItemShow cttItemShowTemp =new CttItemShow();
                Boolean isRoot=false;
                if(itemUnitNext.getParentPkid()!=null&&itemUnitNext.getParentPkid().equals("root")){
                    cttItemShowTemp.setName("�ϼ�");
                    cttItemShowTemp.setPkid("total"+i);
                    cttItemShowTemp.setContractAmount(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }
                if(isRoot.equals(true)){
                    cttItemShowList.add(cttItemShowTemp);
                }
            } else if(i+1== cttItemShowListTemp.size()){
                itemUnitNext = cttItemShowListTemp.get(i);
                CttItemShow cttItemShowTemp =new CttItemShow();
                cttItemShowTemp.setName("�ϼ�");
                cttItemShowTemp.setPkid("total"+i);
                cttItemShowTemp.setContractAmount(bdTotal);
                bdTotal=new BigDecimal(0);
                cttItemShowList.add(cttItemShowTemp);
                // �ܺϼ�
                cttItemShowTemp =new CttItemShow();
                cttItemShowTemp.setName("�ܺϼ�");
                cttItemShowTemp.setPkid("total_all"+i);
                cttItemShowTemp.setContractAmount(bdAllTotal);
                cttItemShowList.add(cttItemShowTemp);
                bdAllTotal=new BigDecimal(0);
            }
        }
    }

    /*����*/
    public void resetAction(){
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        styleModelCttQty=new StyleModel();
        styleModelCttQty.setDisabled_Flag("false");
        styleModelCttAmount=new StyleModel();
        styleModelCttAmount.setDisabled_Flag("true");
        strPasteType="";
        cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strBelongToPkid);
    }
    public void resetActionForAdd(){
        strSubmitType="Add";
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
    }
    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            if(!strSubmitTypePara.equals("Add")){
                if(cttItemShowSelected.getStrNo()==null){
                    MessageUtil.addError("��ȷ��ѡ����У��ϼ��в��ɱ༭��");
                    return;
                }
            }
            if(strSubmitTypePara.equals("Sel")){
                cttItemShowSel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemShowUpd =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo())) ;
                cttItemShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrCorrespondingItemNo()));
                esCommon.getIndexOfSubcttItemNamelist(cttItemShowUpd.getName());
            }
            else if(strSubmitTypePara.equals("Del")){
                cttItemShowDel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSelected) ;
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrCorrespondingItemNo()));
            }
        } catch (Exception e) {
            logger.error("ѡ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cttItemShowSel.getName()).equals("")){
            Integer intIndex= esCommon.getIndexOfSubcttItemNamelist(cttItemShowSel.getName());
            if(intIndex>=0){
                styleModelCttQty.setDisabled_Flag("true");
                styleModelCttAmount.setDisabled_Flag("false");
                if(strSubmitType!=null && strSubmitType.equals("Upd")) {
                    styleModelNo.setDisabled_Flag("true");
                }else{
                    styleModelNo.setDisabled_Flag("false");
                }
                cttItemShowSel.setUnit(null);
                cttItemShowSel.setContractUnitPrice(null);
                cttItemShowSel.setContractQuantity(null);
                cttItemShowSel.setSignPartAPrice(null);
                cttItemShowSel.setCorrespondingPkid(null);
                cttItemShowSel.setStrCorrespondingItemNo(null);
                cttItemShowSel.setStrCorrespondingItemName(null);
            }
            else{
                styleModelCttQty.setDisabled_Flag("false");
                styleModelCttAmount.setDisabled_Flag("true");
                if(strSubmitType!=null && strSubmitType.equals("Upd")) {
                    styleModelNo.setDisabled_Flag("true");
                }else{
                    styleModelNo.setDisabled_Flag("false");
                }
            }
        }
        return true;
    }
    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            if(cttItemShowAdd.getContractUnitPrice()==null|| cttItemShowAdd.getContractQuantity()==null){
                bigDecimal=null;
            }
            else{
                bigDecimal = cttItemShowAdd.getContractUnitPrice().multiply(cttItemShowAdd.getContractQuantity());
            }
            cttItemShowAdd.setContractAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")){
            if(cttItemShowUpd.getContractUnitPrice()==null|| cttItemShowUpd.getContractQuantity()==null){
                bigDecimal=null;
            }
            else{
                bigDecimal = cttItemShowUpd.getContractUnitPrice().multiply(cttItemShowUpd.getContractQuantity());
            }
            cttItemShowUpd.setContractAmount(bigDecimal);
        }

    }
    public Boolean blurStrNoToGradeAndOrderid(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(cttItemShowTemp.getStrNo());
        if(StringUtils .isEmpty(strIgnoreSpaceOfStr)){
            return true;
        }
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (!strIgnoreSpaceOfStr.matches(strRegex) ){
            MessageUtil.addError("��ȷ������ı��룬����" + strIgnoreSpaceOfStr + "��ʽ����ȷ��");
            return strNoBlurFalse();
        }

        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<EsCttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsCttItemListByParentPkid("root", esCttItemList);

            if(itemHieRelapListSubTemp .size() ==0){
                if(!strIgnoreSpaceOfStr.equals("1") ){
                    MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����1��");
                    return strNoBlurFalse();
                }
            }
            else{
                if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strIgnoreSpaceOfStr)){
                    MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����" +
                            (itemHieRelapListSubTemp.size() + 1) + "��");
                    return strNoBlurFalse();
                }
            }
            cttItemShowTemp.setGrade(1);
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            CttItemShow cttItemShowTemp1 =new CttItemShow();
            cttItemShowTemp1 =getItemOfEsItemHieRelapByStrNo(
                    strParentNo,
                    cttItemShowList);
            if(cttItemShowTemp1 ==null|| cttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("��ȷ������ı��룡����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            }
            else{
                List<EsCttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsCttItemListByParentPkid(
                        cttItemShowTemp1.getPkid(),
                        esCttItemList);
                if(itemHieRelapListSubTemp .size() ==0){
                    if(!cttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����" + strParentNo + ".1��");
                        return strNoBlurFalse();
                    }
                }
                else{
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����" + strParentNo +
                                "." + (itemHieRelapListSubTemp.size() + 1) + "��");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[]= strIgnoreSpaceOfStr.split("\\.");
                cttItemShowTemp.setGrade(strTemps.length) ;
                cttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                cttItemShowTemp.setParentPkid(cttItemShowTemp1.getPkid()) ;
            }
        }
        return true ;
    }
    public Boolean blurCorrespondingPkid(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        if(cttItemShowTemp.getStrCorrespondingItemNo()==null||
                cttItemShowTemp.getStrCorrespondingItemNo().equals("")){
            cttItemShowTemp.setCorrespondingPkid(null);
            cttItemShowTemp.setStrCorrespondingItemName(null);
            return true;
        }
        CttItemShow cttItemShowTemp1 =
                getItemOfEsItemHieRelapByStrNo(
                        cttItemShowTemp.getStrCorrespondingItemNo(),
                        cttItemShowList_Cstpl);
        if(cttItemShowTemp1 !=null && cttItemShowTemp1.getStrNo() !=null){
            cttItemShowTemp.setCorrespondingPkid(cttItemShowTemp1.getPkid());
            cttItemShowTemp.setStrCorrespondingItemName(cttItemShowTemp1.getName());
            return true ;
        }
        else{
            cttItemShowTemp.setCorrespondingPkid(null);
            cttItemShowTemp.setStrCorrespondingItemName(null);
            MessageUtil.addError("��ȷ������Ķ�Ӧ���룬�ñ��벻����ȷ�ĳɱ��ƻ�����룡");
            return strNoBlurFalse();
        }
    }
    private Boolean strNoBlurFalse(){
        cttItemShowSel.setPkid("") ;
        cttItemShowSel.setParentPkid("");
        cttItemShowSel.setGrade(null);
        cttItemShowSel.setOrderid(null);
        return false;
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Del")){
                int delRecordNum=delRecordAction(cttItemShowDel);
                if (delRecordNum<=0){
                    MessageUtil.addInfo("�ü�¼��ɾ����");
                    return;
                }
            }
            else {
                 /*�ύǰ�ļ��*/
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct��grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderid()){
                    return ;
                }
                /*��Ӧ������֤*/
                if(!blurCorrespondingPkid ()){
                    return ;
                }

                if(strSubmitType.equals("Add")){
                    addRecordAction(cttItemShowAdd);
                }else if(strSubmitType.equals("Upd")){
                    Integer intModificationNum= cttItemShowUpd.getModificationNum()==null?0: cttItemShowUpd.getModificationNum();
                    cttItemShowUpd.setModificationNum(intModificationNum+1);
                    updRecordAction(cttItemShowUpd);
                }
            }
            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("�ύ������ɡ�");
                case "Upd" : MessageUtil.addInfo("����������ɡ�");
                case "Del" : MessageUtil.addInfo("ɾ��������ɡ�");
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }
    private void addRecordAction(CttItemShow cttItemShowPara){
        esCttItemService.setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(
                cttItemShowPara.getBelongToType(),
                cttItemShowPara.getBelongToPkid(),
                cttItemShowPara.getParentPkid(),
                cttItemShowPara.getGrade(),
                cttItemShowPara.getOrderid());
        esCttItemService.insertRecord(cttItemShowPara);
    }
    private void updRecordAction(CttItemShow cttItemShowPara){
        esCttItemService.updateRecord(cttItemShowPara) ;
    }
    private int delRecordAction(CttItemShow cttItemShowPara) {
        int deleteRecordNum=esCttItemService.deleteRecord(cttItemShowPara.getPkid()) ;
        esCttItemService.setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(
                cttItemShowPara.getBelongToType(),
                cttItemShowPara.getBelongToPkid(),
                cttItemShowPara.getParentPkid(),
                cttItemShowPara.getGrade(),
                cttItemShowPara.getOrderid());
        return deleteRecordNum;
    }

    /*�ύǰ�ļ�飺�����������*/
	private Boolean subMitActionPreCheck(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strBelongToPkid);
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        if (StringUtils.isEmpty(cttItemShowTemp.getStrNo())) {
            MessageUtil.addError("�������ţ�");
            return false;
        }
        if (StringUtils.isEmpty(cttItemShowTemp.getName())) {
            MessageUtil.addError("���������ƣ�");
            return false;
        }

        if ((cttItemShowTemp.getContractUnitPrice()!=null&&
                cttItemShowTemp.getContractUnitPrice().compareTo(BigDecimal.ZERO)!=0) ||
                (cttItemShowTemp.getContractQuantity()!=null&&
                        cttItemShowTemp.getContractQuantity().compareTo(BigDecimal.ZERO)!=0)){
            //||item_TkcttCstpl.getContractAmount()!=null){
            /*��ǰ̨�ؼ�,�������BigDecimal���ͱ���Ϊnull�ģ��Զ�ת��Ϊ0����������ģ�����null*/
            if (StringUtils.isEmpty(cttItemShowTemp.getUnit())) {
                MessageUtil.addError("�����뵥λ��");
                return false;
            }
        }
        return true;
    }

    public void submitAction_Cstpl(){
        try{
            cttItemShowSelected.setCorrespondingPkid(cttItemShowSelected_Cstpl.getPkid());
            esCttItemService.updateRecord(cttItemShowSelected) ;
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    /*���ܰ���ͬ�б��и��ݱ���ҵ���*/
    private CttItemShow getItemOfEsItemHieRelapByStrNo(
             String strNo,
             List<CttItemShow> cttItemShowListPara){
        CttItemShow cttItemShowTemp =null;
        try{
            for(CttItemShow itemUnit: cttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    cttItemShowTemp =(CttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cttItemShowTemp;
    }

    /*�����ֶ�Start*/
    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }
    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttItemShow getCttItemShowSel() {
        return cttItemShowSel;
    }

    public void setCttItemShowSel(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
    }

    public CttItemShow getCttItemShow() {
        return cttItemShowSel;
    }

    public void setCttItemShow(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
    }

    public CttItemShow getCttItemShowSelected() {
        return cttItemShowSelected;
    }

    public void setCttItemShowSelected(CttItemShow cttItemShowSelected) {
        this.cttItemShowSelected = cttItemShowSelected;
    }

    public List<CttItemShow> getCttItemShowList() {
        return cttItemShowList;
    }

    public void setCttItemShowList(List<CttItemShow> cttItemShowList) {
        this.cttItemShowList = cttItemShowList;
    }

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public List<CttItemShow> getCttItemShowList_Cstpl() {
        return cttItemShowList_Cstpl;
    }

    public void setCttItemShowList_Cstpl(List<CttItemShow> cttItemShowList_Cstpl) {
        this.cttItemShowList_Cstpl = cttItemShowList_Cstpl;
    }

    public CttItemShow getCttItemShowSelected_Cstpl() {
        return cttItemShowSelected_Cstpl;
    }

    public void setCttItemShowSelected_Cstpl(CttItemShow cttItemShowSelected_Cstpl) {
        this.cttItemShowSelected_Cstpl = cttItemShowSelected_Cstpl;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public StyleModel getStyleModelNo() {
        return styleModelNo;
    }

    public StyleModel getStyleModelCttAmount() {
        return styleModelCttAmount;
    }

    public StyleModel getStyleModelCttQty() {
        return styleModelCttQty;
    }

    public CttItemShow getCttItemShowAdd() {
        return cttItemShowAdd;
    }

    public void setCttItemShowAdd(CttItemShow cttItemShowAdd) {
        this.cttItemShowAdd = cttItemShowAdd;
    }

    public CttItemShow getCttItemShowUpd() {
        return cttItemShowUpd;
    }

    public void setCttItemShowUpd(CttItemShow cttItemShowUpd) {
        this.cttItemShowUpd = cttItemShowUpd;
    }


    public CttItemShow getCttItemShowDel() {
        return cttItemShowDel;
    }

    public void setCttItemShowDel(CttItemShow cttItemShowDel) {
        this.cttItemShowDel = cttItemShowDel;
    }

    /*�����ֶ�End*/
}