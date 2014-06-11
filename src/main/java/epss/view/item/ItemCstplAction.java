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
import epss.repository.model.model_show.CstplItemShow;
import epss.repository.model.model_show.CttItemShow;
import epss.repository.model.EsInitPower;
import epss.service.*;
import epss.service.EsCttInfoService;
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
public class ItemCstplAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemCstplAction.class);
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

    /*��ѯ���ύ��*/
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    /*�б���ѡ��һ��*/
    private CstplItemShow cstplItemShowSelected;
    /*�б���ʾ��*/
    private List<CstplItemShow> cstplItemShowList;

    /*��������*/
    private String strBelongToType;
    /*������*/
    private String strBelongToPkid;

    /*�ύ����*/
    private String strSubmitType;
    private String strMngNotFinishFlag;

    /*���ƿؼ��ڻ����ϵĿ�������ʵStart*/
    //��ʾ�Ŀ���
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    /*���ƿؼ��ڻ����ϵĿ�������ʵEnd*/
    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if  (parammap.containsKey("strType")){
            strBelongToType=parammap.get("strType").toString();
        }

        if(strBelongToType==null){
            return ;
        }
        strBelongToPkid=parammap.get("strCstplPkid").toString();

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

    public void resetAction(){
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strBelongToPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strBelongToPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttItemShowAdd =new CttItemShow(strBelongToType ,strBelongToPkid);
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cttItemShowSel.getName()).equals("")){
            Integer intIndex= esCommon.getIndexOfCstplItemNamelist(cttItemShowSel.getName());
            if(intIndex>=0){
                if(ToolUtil.getStrIgnoreNull(strSubmitType).equals("Upd")) {
                    styleModelNo.setDisabled_Flag("true");
                }else{
                    styleModelNo.setDisabled_Flag("false");
                }
                cttItemShowSel.setUnit(null);
                cttItemShowSel.setContractUnitPrice(null);
                cttItemShowSel.setContractQuantity(null);
                cttItemShowSel.setStrCorrespondingItemNo(null);
                cttItemShowSel.setCorrespondingPkid(null);
            }else{
                if(ToolUtil.getStrIgnoreNull(strSubmitType).equals("Upd")) {
                    styleModelNo.setDisabled_Flag("true");
                }else{
                    styleModelNo.setDisabled_Flag("false");
                }
            }
        }
        return true;
    }
    public void selectRecordAction(String strSubmitTypePara){
        try {
            String  strSubmitTypeBefore= strSubmitType;
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                CstplItemShow esInitSubcttTemp= cstplItemShowSelected;
                if(esInitSubcttTemp.getBelongToTypeContrast()==null) {
                    cttItemShowSel =new CttItemShow(strBelongToType ,strBelongToPkid);
                }
                else{
                    cttItemShowSel =getItemOfEsItemHieRelapByItem(esInitSubcttTemp,"Cstpl");
                }
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemShowUpd = getItemOfEsItemHieRelapByItem(cstplItemShowSelected, "Cstpl");

                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo()));
                cttItemShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrCorrespondingItemNo()));
                Integer intIndex = esCommon.getIndexOfCstplItemNamelist(cttItemShowUpd.getName());
            }
            else if(strSubmitTypePara.equals("Del")){
                if(cstplItemShowSelected.getStrNoContrast()==null) {
                    MessageUtil.addInfo("û�п�ɾ�������ݣ�");
                    cttItemShowDel =new CttItemShow(strBelongToType ,strBelongToPkid);
                    return;
                }
                else{
                    cttItemShowDel =getItemOfEsItemHieRelapByItem(cstplItemShowSelected,"Cstpl");
                }
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrCorrespondingItemNo()));
            }
            else if(strSubmitTypePara.equals("From_tkctt_to_cstpl")){
                if(!fromTkcttToCstplAction()){
                    strSubmitType=strSubmitTypeBefore;
                }
            }
        } catch (Exception e) {
            logger.error("ѡ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")) {
            if (cttItemShowAdd.getContractUnitPrice() == null || cttItemShowAdd.getContractQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cttItemShowAdd.getContractUnitPrice().multiply(cttItemShowAdd.getContractQuantity());
            }
            cttItemShowAdd.setContractAmount(bigDecimal);
        }
        if (strSubmitType.equals("Upd")) {
            if (cttItemShowUpd.getContractUnitPrice() == null || cttItemShowUpd.getContractQuantity() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cttItemShowUpd.getContractUnitPrice().multiply(cttItemShowUpd.getContractQuantity());
            }
            cttItemShowUpd.setContractAmount(bigDecimal);
        }
    }
    public Boolean blurStrNoToGradeAndOrderid(String strIsBlur){
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

        //�ñ����Ѿ�����
        if(!strSubmitType.equals("Upd")){
            if(getItemOfTkcttAndCstplByStrNo(strIgnoreSpaceOfStr, cstplItemShowList, "Cstpl")!=null){
                CstplItemShow cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(
                        strIgnoreSpaceOfStr, cstplItemShowList, "Cstpl");

                String correspondingItemNoContrast= ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCorrespondingItemNoContrast());
                if(cttItemShowTemp.getStrCorrespondingItemNo()!=null&&
                        !correspondingItemNoContrast.equals(cttItemShowTemp.getStrCorrespondingItemNo())){
                    MessageUtil.addError("�ñ���(" + strIgnoreSpaceOfStr + ")����Ӧ����(" + correspondingItemNoContrast
                            + ")�Ѿ�����,������Ķ�Ӧ���루" + cttItemShowTemp.getStrCorrespondingItemNo() +
                            ")ִ�в������ʱ������߼����󣬽��޷����룡");
                    return strNoBlurFalse();
                }
            }
        }

        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<CstplItemShow> cstplItemShowSubTemp =new ArrayList<CstplItemShow>();
            cstplItemShowSubTemp =getItemOfTkcttAndCstplByLevelParentPkid(
                    "root",
                    cstplItemShowList,"Cstpl");
            if(cstplItemShowSubTemp.size() ==0){
                if(!strIgnoreSpaceOfStr.equals("1") ){
                    MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����1��");
                    return strNoBlurFalse();
                }
            }
            else{
                if(cstplItemShowSubTemp.size() +1<Integer.parseInt(strIgnoreSpaceOfStr)){
                    MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����" + (cstplItemShowSubTemp.size() + 1) + "��");
                    return strNoBlurFalse();
                }
            }
            cttItemShowTemp.setGrade(1);
            cttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            CstplItemShow cstplItemShowTemp =new CstplItemShow();
            cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList, "Cstpl");
            if(cstplItemShowTemp ==null|| cstplItemShowTemp.getPkid()==null){
                MessageUtil.addError("��ȷ������ı��룡����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            }
            else{
                List<CstplItemShow> cstplItemShowSubTemp =new ArrayList<CstplItemShow>();
                cstplItemShowSubTemp =getItemOfTkcttAndCstplByLevelParentPkid(
                        cstplItemShowTemp.getPkidContrast(),
                        cstplItemShowList, "Cstpl");
                if(cstplItemShowSubTemp.size() ==0){
                    if(!cttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����" + strParentNo + ".1��");
                        return strNoBlurFalse();
                    }
                }
                else{
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(cstplItemShowSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����" + strParentNo + "." +
                                (cstplItemShowSubTemp.size() + 1) + "��");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[]= strIgnoreSpaceOfStr.split("\\.");
                cttItemShowTemp.setGrade(strTemps.length) ;
                cttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                cttItemShowTemp.setParentPkid( cstplItemShowTemp.getPkidContrast()) ;
            }
        }
        if(strIsBlur.equals("true")){
            CstplItemShow cstplItemShowTemp =
                    getItemOfTkcttAndCstplByStrNo(strIgnoreSpaceOfStr, cstplItemShowList,"Tkctt");
            if(cstplItemShowTemp !=null){
                cttItemShowTemp.setStrCorrespondingItemNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getStrNo())) ;
                cttItemShowTemp.setCorrespondingPkid(cstplItemShowTemp.getPkid());
            }else{
                cttItemShowTemp.setStrCorrespondingItemNo(null) ;
                cttItemShowTemp.setCorrespondingPkid(null) ;
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
        CstplItemShow cstplItemShowTemp =
                getItemOfTkcttAndCstplByStrNo(cttItemShowTemp.getStrCorrespondingItemNo(), cstplItemShowList, "Tkctt");
        if(cstplItemShowTemp !=null && cstplItemShowTemp.getStrNo() !=null){

            String strNo= cttItemShowTemp.getStrNo();
            if(strNo!=null&&!strNo.equals("")){
                CstplItemShow cstplTempForCstplItemShow =getItemOfTkcttAndCstplByStrNo(
                        strNo, cstplItemShowList, "Cstpl");
                if(cstplTempForCstplItemShow !=null) {
                    String correspondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(cstplTempForCstplItemShow.getCorrespondingItemNoContrast());
                    if(cttItemShowTemp.getStrCorrespondingItemNo()!=null&&
                            !correspondingItemNoContrast.equals(cttItemShowTemp.getStrCorrespondingItemNo())){
                        MessageUtil.addError("�ñ���(" + strNo + ")����Ӧ����(" + correspondingItemNoContrast
                                + ")�Ѿ�����,������Ķ�Ӧ���루" + cttItemShowTemp.getStrCorrespondingItemNo() +
                                ")ִ�в������ʱ������߼����󣬽��޷����룡");
                        return strNoBlurFalse();
                    }
                }
            }
            cttItemShowTemp.setCorrespondingPkid(cstplItemShowTemp.getPkid());
        }
        else{
            //MessageUtil.addError("��ȷ������Ķ�Ӧ���룬�ñ��벻����ȷ���ܰ���ͬ����룡");
            //return strNoBlurFalse();
        }
        return true ;
    }
    public void delThisRecordAction() {
        try {
            Integer intItemUnitConstructSelectedGrade=-1;
            List<CstplItemShow> cstplItemShowListTemp =new ArrayList<>();
            cstplItemShowListTemp.addAll(cstplItemShowList);
            for(CstplItemShow itemUnitConstructItem: cstplItemShowListTemp){
                if(intItemUnitConstructSelectedGrade>-1){
                    if((itemUnitConstructItem .getGradeContrast()==null?0:itemUnitConstructItem .getGradeContrast())
                            <=intItemUnitConstructSelectedGrade){
                        break;
                    }
                    else{
                        esCttItemService.deleteRecord(itemUnitConstructItem.getPkidContrast()) ;
                    }
                }
                if(intItemUnitConstructSelectedGrade==-1){
                    if(itemUnitConstructItem.equals(cstplItemShowSelected) ){
                        intItemUnitConstructSelectedGrade=itemUnitConstructItem.getGradeContrast();
                        int deleteRecordNumOfSelf=esCttItemService.deleteRecord(itemUnitConstructItem.getPkidContrast()) ;
                        if (deleteRecordNumOfSelf<=0){
                            MessageUtil.addInfo("�ü�¼��ɾ����");
                            return;
                        }
                    }
                }
            }
            esCttItemService.setAfterThisOrderidSubOneByTypeAndIdAndParentPkidAndGrade(
                    cstplItemShowSelected.getBelongToTypeContrast(),
                    cstplItemShowSelected.getBelongToPkidContrast(),
                    cstplItemShowSelected.getParentPkidContrast(),
                    cstplItemShowSelected.getGradeContrast(),
                    cstplItemShowSelected.getOrderidContrast());
            initData();
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void submitThisRecordAction(){
        try{
            /*�ύǰ�ļ��*/
            if(strSubmitType .equals("Del")) {
                delThisRecordAction();
                return;
            }
            if(!subMitActionPreCheck()){
                return ;
            }
            /*������֤*/
            if(!blurStrNoToGradeAndOrderid("false")){
                return ;
            }
            /*��Ӧ������֤*/
            if(!blurCorrespondingPkid()){
                return ;
            }
            if(strSubmitType .equals("Upd")) {
                Integer intModificationNum=
                        cttItemShowUpd.getModificationNum()==null?0: cttItemShowUpd.getModificationNum();
                cttItemShowUpd.setModificationNum(intModificationNum+1);
                esCttItemService.updateRecord(cttItemShowUpd) ;
            }
            else if(strSubmitType .equals("Add")) {
                EsCttItem esCttItemTemp=esCttItemService.fromConstructToModel(cttItemShowAdd);
                if (esCttItemService.isExistSameRecordInDb(esCttItemTemp)){
                    MessageUtil.addInfo("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�롣");
                    return;
                }
                esCttItemService.setAfterThisOrderidPlusOneByTypeAndIdAndParentPkidAndGrade(
                        cttItemShowAdd.getBelongToType(),
                        cttItemShowAdd.getBelongToPkid(),
                        cttItemShowAdd.getParentPkid(),
                        cttItemShowAdd.getGrade(),
                        cttItemShowAdd.getOrderid());
                esCttItemService.insertRecord(cttItemShowAdd);
            }
            MessageUtil.addInfo("�ύ������ɡ�");
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
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
        return true;
    }

    private void initData() {
        /*�ܰ���ͬ�б�*/
        List<EsCttItem> esCttItemListTkctt =new ArrayList<EsCttItem>();
        EsCttInfo esCttInfo = esCttInfoService.getCttInfoByPkId(strBelongToPkid);
        String strTkcttPkidInCstpl= esCttInfo.getParentPkid();
        esCttItemListTkctt =
                esCttItemService.getEsItemHieRelapListByTypeAndPkid(ESEnum.ITEMTYPE0.getCode(), strTkcttPkidInCstpl);
        List<CttItemShow> cttItemShowListTkctt =new ArrayList<>();
        recursiveDataTable("root", esCttItemListTkctt, cttItemShowListTkctt);
        cttItemShowListTkctt =getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowListTkctt);

        /*�ɱ��ƻ��б�*/
        List<EsCttItem> esCttItemListCstpl = esCttItemService.getEsItemHieRelapListByTypeAndPkid(
                strBelongToType, strBelongToPkid);

        List<CttItemShow> cttItemShowListCstpl =new ArrayList<>();
        recursiveDataTable("root", esCttItemListCstpl, cttItemShowListCstpl);
        cttItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(
                cttItemShowListCstpl) ;

        /*ƴװ�б�*/
        List<CstplItemShow> cstplItemShowList_ForSort =new ArrayList<>();
        int i=0;
        for(CttItemShow itemTkctt: cttItemShowListTkctt){
            i++;
            Boolean insertedFlag=false ;
            String strFrontNoAndName="";
            CstplItemShow itemTkcttInsertItem;
            for(CttItemShow itemCstpl: cttItemShowListCstpl){
                itemTkcttInsertItem=new CstplItemShow();
                if(itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())){
                    //�ܰ���ͬ
                    if(strFrontNoAndName.equals(itemTkctt.getStrNo()+itemTkctt .getName())){
                        itemTkcttInsertItem.setStrNo("") ;
                        itemTkcttInsertItem.setName("") ;
                        itemTkcttInsertItem.setNote("") ;
                        itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() +";"+itemTkctt.getPkid());
                        itemTkcttInsertItem.setUnit("");
                        itemTkcttInsertItem.setContractUnitPrice(null) ;
                        itemTkcttInsertItem.setContractQuantity(null) ;
                        itemTkcttInsertItem.setContractAmount(null) ;
                    }else{
                        strFrontNoAndName=itemTkctt.getStrNo()+itemTkctt .getName();
                        itemTkcttInsertItem.setStrNo(itemTkctt.getStrNo());
                        itemTkcttInsertItem.setName(itemTkctt.getName());
                        itemTkcttInsertItem.setNote(itemTkctt.getNote());
                        itemTkcttInsertItem.setPkid(itemTkctt.getPkid());
                        itemTkcttInsertItem.setUnit(itemTkctt.getUnit());
                        itemTkcttInsertItem.setContractUnitPrice(itemTkctt.getContractUnitPrice()) ;
                        itemTkcttInsertItem.setContractQuantity(itemTkctt.getContractQuantity()) ;
                        itemTkcttInsertItem.setContractAmount(itemTkctt.getContractAmount()) ;
                    }
                    itemTkcttInsertItem.setBelongToType(itemTkctt.getBelongToType()) ;
                    itemTkcttInsertItem.setBelongToPkid(itemTkctt.getBelongToPkid()) ;
                    itemTkcttInsertItem.setParentPkid(itemTkctt.getParentPkid());
                    itemTkcttInsertItem.setGrade(itemTkctt.getGrade());
                    itemTkcttInsertItem.setOrderid(itemTkctt.getOrderid()) ;
                    itemTkcttInsertItem.setCorrespondingPkid(itemTkctt .getCorrespondingPkid());

                    itemTkcttInsertItem.setSignPartAPrice(itemTkctt.getSignPartAPrice()) ;
                    itemTkcttInsertItem.setDeletedFlag(itemTkctt.getDeletedFlag());
                    itemTkcttInsertItem.setOriginFlag(itemTkctt.getOriginFlag()) ;
                    itemTkcttInsertItem.setCreatedBy(itemTkctt.getCreatedBy());
                    itemTkcttInsertItem.setCreatedDate(itemTkctt.getCreatedDate());
                    itemTkcttInsertItem.setLastUpdBy(itemTkctt.getLastUpdBy());
                    itemTkcttInsertItem.setLastUpdDate(itemTkctt.getLastUpdDate());
                    itemTkcttInsertItem.setModificationNum(itemTkctt.getModificationNum());
                    //�ɱ��ƻ�
                    itemTkcttInsertItem.setStrNoContrast(itemCstpl.getStrNo()) ;
                    itemTkcttInsertItem.setPkidContrast(itemCstpl.getPkid());
                    itemTkcttInsertItem.setBelongToTypeContrast(itemCstpl.getBelongToType());
                    itemTkcttInsertItem.setBelongToPkidContrast(itemCstpl.getBelongToPkid());
                    itemTkcttInsertItem.setParentPkidContrast(itemCstpl.getParentPkid());
                    itemTkcttInsertItem.setGradeContrast(itemCstpl.getGrade());
                    itemTkcttInsertItem.setOrderidContrast(itemCstpl.getOrderid()) ;
                    itemTkcttInsertItem.setNameContrast(itemCstpl.getName());
                    itemTkcttInsertItem.setNoteContrast(itemCstpl.getNote());
                    itemTkcttInsertItem.setUnitContrast(itemCstpl.getUnit());
                    itemTkcttInsertItem.setContractUnitPriceContrast(itemCstpl.getContractUnitPrice()) ;
                    itemTkcttInsertItem.setContractQuantityContrast(itemCstpl.getContractQuantity()) ;
                    itemTkcttInsertItem.setContractAmountContrast(itemCstpl.getContractAmount()) ;
                    itemTkcttInsertItem.setSignPartAPriceContrast(itemCstpl.getSignPartAPrice()) ;
                    itemTkcttInsertItem.setDeletedFlagContrast(itemCstpl.getDeletedFlag());
                    itemTkcttInsertItem.setOriginFlagContrast(itemCstpl.getOriginFlag()) ;
                    itemTkcttInsertItem.setCreatedByContrast(itemCstpl.getCreatedBy());
                    itemTkcttInsertItem.setCreatedDateContrast(itemCstpl.getCreatedDate());
                    itemTkcttInsertItem.setLastUpdByContrast(itemCstpl.getLastUpdBy());
                    itemTkcttInsertItem.setLastUpdDateContrast(itemCstpl.getLastUpdDate());
                    itemTkcttInsertItem.setModificationNumContrast(itemCstpl.getModificationNum());
                    itemTkcttInsertItem.setCorrespondingPkidContrast(itemCstpl.getCorrespondingPkid());
                    if(itemTkcttInsertItem.getPkid() ==null||itemTkcttInsertItem.getPkid().equals("")){
                        itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() +"");
                    }
                    insertedFlag=true ;
                    cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                }
            }
            if (insertedFlag.equals(false)){
                itemTkcttInsertItem=new CstplItemShow();
                itemTkcttInsertItem=getItemOfTkcttAndCstplByItemOfEsItemHieRelap(itemTkctt,"Tkctt");
                cstplItemShowList_ForSort.add(itemTkcttInsertItem);
            }
        }

        for(CttItemShow itemCstpl: cttItemShowListCstpl){
            Boolean insertedFlag=false ;
            for(CttItemShow itemTkctt: cttItemShowListTkctt){
                if(itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())){
                    insertedFlag=true;
                    break;
                }
            }
            if(insertedFlag.equals(false)){
                CstplItemShow itemTkcttInsertItem=getItemOfTkcttAndCstplByItemOfEsItemHieRelap(itemCstpl,"Cstpl");
                if (itemTkcttInsertItem.getPkid() ==null||itemTkcttInsertItem.getPkid().equals("")){
                    itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() +"");
                }
                cstplItemShowList_ForSort.add(itemTkcttInsertItem);
            }
        }

        for(CstplItemShow itemUnit: cstplItemShowList_ForSort){
            if(itemUnit.getStrNoContrast()!=null){
                String correspondingItemNoContrast=itemUnit.getCorrespondingPkidContrast()==null?"":itemUnit.getCorrespondingPkidContrast();
                CstplItemShow cstplItemShowTemp =
                        getItemOfTkcttAndCstplByPkid(correspondingItemNoContrast, cstplItemShowList_ForSort, "Tkctt");
                if(cstplItemShowTemp !=null){
                    itemUnit.setCorrespondingItemNoContrast(cstplItemShowTemp.getStrNo());
                }
            } else{
                itemUnit.setCorrespondingItemNoContrast(itemUnit.getStrNo());
            }
        }

        cstplItemShowList =new ArrayList<CstplItemShow>();
        cstplItemShowList =getItemOfTkcttAndCstplListSorted(cstplItemShowList_ForSort,0);
        // ��Ӻϼ�
        setItemOfTkcttAndCstplList_AddTotal();
    }
    private void setItemOfTkcttAndCstplList_AddTotal(){
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        cstplItemShowListTemp.addAll(cstplItemShowList);

        cstplItemShowList.clear();
        // С��
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        BigDecimal bdTotalContrast=new BigDecimal(0);
        BigDecimal bdAllTotalContrast=new BigDecimal(0);

        CstplItemShow itemUnit=new CstplItemShow();
        CstplItemShow itemUnitNext=new CstplItemShow();
        for(int i=0;i< cstplItemShowListTemp.size();i++){
            itemUnit = cstplItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmount()));
            // ����
            bdTotalContrast=bdTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));
            bdAllTotalContrast=bdAllTotalContrast.add(ToolUtil.getBdIgnoreNull(itemUnit.getContractAmountContrast()));
            cstplItemShowList.add(itemUnit);

            if(i+1< cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i+1);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                Boolean isRoot=false;
                if(itemUnitNext.getParentPkid()!=null&&itemUnitNext.getParentPkid().equals("root")){
                    cstplItemShowTemp.setName("�ϼ�");
                    cstplItemShowTemp.setPkid("total"+i);
                    cstplItemShowTemp.setContractAmount(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

                if(itemUnitNext.getParentPkidContrast()!=null && itemUnitNext.getParentPkidContrast().equals("root")){
                    cstplItemShowTemp.setPkid("total"+i);
                    cstplItemShowTemp.setNameContrast("�ϼ�");
                    cstplItemShowTemp.setPkidContrast("total_contrast"+i);
                    cstplItemShowTemp.setContractAmountContrast(bdTotalContrast);
                    bdTotalContrast = new BigDecimal(0);
                    isRoot=true;
                }

                if(isRoot.equals(true)){
                    cstplItemShowList.add(cstplItemShowTemp);
                }
            } else if(i+1== cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setName("�ϼ�");
                cstplItemShowTemp.setPkid("total"+i);
                cstplItemShowTemp.setContractAmount(bdTotal);

                cstplItemShowTemp.setNameContrast("�ϼ�");
                cstplItemShowTemp.setPkidContrast("total_contrast"+i);
                cstplItemShowTemp.setContractAmountContrast(bdTotalContrast);
                cstplItemShowList.add(cstplItemShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalContrast = new BigDecimal(0);

                // �ܺϼ�
                cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setName("�ܺϼ�");
                cstplItemShowTemp.setPkid("total_all"+i);
                cstplItemShowTemp.setContractAmount(bdAllTotal);

                cstplItemShowTemp.setNameContrast("�ܺϼ�");
                cstplItemShowTemp.setPkidContrast("total_all_contrast"+i);
                cstplItemShowTemp.setContractAmountContrast(bdAllTotalContrast);
                cstplItemShowList.add(cstplItemShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalContrast = new BigDecimal(0);
            }
        }
    }
    private boolean fromTkcttToCstplAction(){
        try{
            if(cstplItemShowSelected.getStrNo() ==null||
                    cstplItemShowSelected.getStrNo().equals("")) {
                MessageUtil.addInfo("�޿ɸ��ƣ�");
                return false;
            }
            if(!ToolUtil.getStrIgnoreNull(cstplItemShowSelected.getStrNoContrast()).equals("")) {
                MessageUtil.addInfo("�ɱ��ƻ��Ϊ�գ��ܰ���ͬ���ɱ��ƻ������޷����ƣ����踴�ƣ�����ɾ���óɱ��ƻ��");
                return false;
            }
            String strIgnoreSpaceOfStr=ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSelected.getStrNo());
            String strNoSplited[]  = strIgnoreSpaceOfStr.split("\\.") ;
            String strTemp="";
            for(int i=0;i<strNoSplited.length-1 ;i++) {
                if(i==0) {
                    strTemp+=strNoSplited[i] ;
                }
                else{
                    strTemp+="."+ strNoSplited[i] ;
                }
                CstplItemShow cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strTemp, cstplItemShowList,"Cstpl");
                if(cstplItemShowTemp ==null){
                    MessageUtil.addInfo("���루" + strTemp + "�����ݲ����ڣ����Ƚ�����" + strTemp + "�������ݣ�");
                    return false;
                }
            }
            cttItemShowSel = getItemOfEsItemHieRelapByItem(cstplItemShowSelected,"Tkctt");
            cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
            cttItemShowSel.setBelongToType(strBelongToType) ;
            cttItemShowSel.setBelongToPkid(strBelongToPkid) ;
            Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");
            if(intLastIndexof>0){
                String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList,"Cstpl");
                cttItemShowSel.setParentPkid(cstplItemShowTemp.getPkid());
                cttItemShowSel.setGrade(cstplItemShowTemp.getGrade() + 1) ;
            }else{
                cttItemShowSel.setParentPkid("root");
            }

            cttItemShowSel.setCorrespondingPkid(cttItemShowSel.getPkid()) ;
            cttItemShowSel.setStrCorrespondingItemNo(cttItemShowSel.getStrNo()) ;
            submitThisRecordAction();
            initData();
            return true;
        }catch (Exception e){
            logger.error("�ܰ���ͬ���ɱ��ƻ������ʧ�ܣ�", e);
            MessageUtil.addError("�ܰ���ͬ���ɱ��ƻ������ʧ�ܣ�" + e.getMessage());
            return false ;
        }
    }
    private List<CstplItemShow> getItemOfTkcttAndCstplListSorted(List<CstplItemShow> listPara,int i){
        List<CstplItemShow> modelList=new ArrayList<CstplItemShow>();
        modelList.addAll(listPara);
        //ct����ͳ�Ƶ�ǰ���ѭ��ʱԪ�ص�����;ct1����ͳ�Ƶ�ǰ�ڲ�ѭ���з������ѭ���ж�ӦԪ�ص�����;
        String strThisCorrespondingItemNoContrast;
        String strThisCorrespondingItemNoContrastAfter;
        for(;i<modelList.size();i++){      //���ѭ��ʵ�ֶ�����list�ı���
            int startI=i;                    //���浱ǰԪ�صĳ�ʼλ��i,��Ϊ���ڲ�ѭ���л�ı�i��ֵ
            int countParent=0;               //ͳ�ƺ͵�ǰԪ�أ�����Ԫ�أ���ͬ��Ԫ�ظ�������Ԫ��ֻ����˳����ͬ
            int countChild=0;                //ͳ��ƥ�丸Ԫ�صĺ���Ԫ�صĸ���������Ԫ��Ҳ��˳�����
            int positionOfChild=0;           //��¼�º͸�Ԫ��ƥ��ĵ�һ�����ӵ�λ��
            strThisCorrespondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(modelList.get(i).getCorrespondingItemNoContrast());
            for(int j=i+1; j<modelList.size();j++){
                strThisCorrespondingItemNoContrastAfter=ToolUtil.getIgnoreSpaceOfStr(modelList.get(j).getCorrespondingItemNoContrast());
                if(strThisCorrespondingItemNoContrast.equals(strThisCorrespondingItemNoContrastAfter)){//�ж��ڲ�ѭ�����Ƿ��к����ѭ���е�Ԫ����ͬ��Ԫ�أ�����¼����������countParent
                    if(countParent==0){        //����ڲ�ѭ���д��ں��������ͬ��Ԫ�أ������ǵ�һ������������countParent��ֵ����Ϊ0��
                        countParent+=2;          //���״�����ʱ�ڲ�����Ҫ��countParent+2
                        continue;
                    }
                    ++countParent;
                }
                else{                        //else�·�Ϊ�������
                    if(countParent==0||!strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast))
                        break;//1.�ڲ���û�к������Ԫ����ͬ��Ԫ�أ�����֪������list֪���������ֱ���˳��ڲ�ѭ�����ɣ�||2.�ڲ����к������Ԫ����ͬ��Ԫ�أ������ڲ���û���亢�ӣ�����֪������list֪���������ֱ���˳��ڲ�ѭ������
                    if((strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast)&&
                            strThisCorrespondingItemNoContrastAfter.compareTo(strThisCorrespondingItemNoContrast)>0)){
                        if(positionOfChild==0){//�ڲ����к������Ԫ����ͬ��Ԫ�أ�
                            positionOfChild=j;  //��¼�µ�һ�����ӵ�λ�ñ�����positionOfChild�С�
                            countChild+=1;      //�ڲ����к������Ԫ����ͬ��Ԫ�أ����һ����亢�ӣ���ͳ���亢�ӵĸ�����������countChild������countParent��ֵ����Ϊ0�����ڲ�����Ҫ��countParent+1
                            continue;
                        }
                        ++countChild;
                    }
                    if(countChild!=0)         //�Ժ��Ӳ��õݹ�˼��
                        modelList=getItemOfTkcttAndCstplListSorted(modelList, positionOfChild);//�ݹ��õ���ƥ�丸Ԫ�صĺ��ӵ���ȷ����
                }
            }
            if(countParent!=0&&countChild!=0){//����֮ǰ��¼���������ݽ�������
                for(int x=1;x<=countChild;x++){
                    CstplItemShow m=modelList.get(positionOfChild);//����Ҫ�ƶ��ĺ���Ԫ�ص�ֵ
                    modelList.remove(positionOfChild);     //�Ƴ�����Ԫ�أ�
                    modelList.add(i+1, m);                 //�����Ӳ��븸Ԫ����
                    ++i;                                   //��һ��Ҫ����ĺ��ӵ�λ��
                    ++positionOfChild;
                }
                --countParent;
            }
            i=startI+countParent+countChild;//������й�����������ѭ����i��ֵ�ض������仯��
        }                                   //�任���i��ֵ=��ʼʱ�ñ���startI��¼���������iֵ+��Ԫ�ظ���+ƥ�丸Ԫ�ص���Ԫ�ظ���
        return modelList;
    }
    /*�ݹ�����*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<CttItemShow> cttItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // ͨ������id�������ĺ���
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getLastUpdBy());
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
    /*�����Ϲ淶�ı������벢�뿪ʱ,�����Ӧ�Ķ���*/
    private Boolean strNoBlurFalse(){
        cttItemShowSel.setPkid("") ;
        cttItemShowSel.setParentPkid("");
        cttItemShowSel.setGrade(null);
        cttItemShowSel.setOrderid(null);
        return false;
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
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CstplItemShow> getItemOfTkcttAndCstplByLevelParentPkid(String strLevelParentPkid,
                                                                        List<CstplItemShow> esCstplItemShowListPara,String strTkcttOrCstpl) {
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        /*�ܿ��ظ��������ݿ�*/
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(itemUnit!=null&&itemUnit.getParentPkidContrast()!=null&&
                            strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                        cstplItemShowListTemp.add(itemUnit);
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(itemUnit!=null&&itemUnit.getParentPkidContrast()!=null&&
                            strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkidContrast())){
                        cstplItemShowListTemp.add(itemUnit);
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cstplItemShowListTemp;
    }
    /*ͨ����ŵõ�����*/
    private CstplItemShow getItemOfTkcttAndCstplByStrNo(String strNo,
                                                        List<CstplItemShow> cstplItemShowListPara,String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNoContrast()).equals(strNo)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cstplItemShowTemp;
    }
    /*ͨ��Pkid�õ�����*/
    private CstplItemShow getItemOfTkcttAndCstplByPkid(String strPkid,
                                                       List<CstplItemShow> cstplItemShowListPara,String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkid()).equals(strPkid)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkidContrast()).equals(strPkid)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return cstplItemShowTemp;
    }

    /*�ܳɹ�ϵ��ȡ���㼶��ϵ��*/
    private CttItemShow getItemOfEsItemHieRelapByItem(
            CstplItemShow cstplItemShowPara,String strTkcttOrCstpl){
        CttItemShow cttItemShowTemp =new CttItemShow() ;
        if(strTkcttOrCstpl .equals("Tkctt")){
            cttItemShowTemp.setStrNo(cstplItemShowPara.getStrNo());
            cttItemShowTemp.setPkid(cstplItemShowPara.getPkid()) ;
            cttItemShowTemp.setBelongToType(cstplItemShowPara.getBelongToType());
            cttItemShowTemp.setBelongToPkid(cstplItemShowPara.getBelongToPkid());
            cttItemShowTemp.setParentPkid(cstplItemShowPara.getParentPkid()) ;
            cttItemShowTemp.setGrade(cstplItemShowPara.getGrade()) ;
            cttItemShowTemp.setOrderid(cstplItemShowPara.getOrderid()) ;
            cttItemShowTemp.setName(cstplItemShowPara.getName()) ;
            cttItemShowTemp.setNote(cstplItemShowPara.getNote()) ;

            cttItemShowTemp.setUnit(cstplItemShowPara.getUnit());
            cttItemShowTemp.setContractUnitPrice(cstplItemShowPara.getContractUnitPrice()) ;
            cttItemShowTemp.setContractQuantity(cstplItemShowPara.getContractQuantity()) ;
            cttItemShowTemp.setContractAmount(cstplItemShowPara.getContractAmount()) ;
            cttItemShowTemp.setSignPartAPrice(cstplItemShowPara.getSignPartAPrice()) ;
            cttItemShowTemp.setDeletedFlag(cstplItemShowPara.getDeletedFlag());
            cttItemShowTemp.setOriginFlag(cstplItemShowPara.getOriginFlag()) ;
            cttItemShowTemp.setCreatedBy(cstplItemShowPara.getCreatedBy());
            cttItemShowTemp.setCreatedDate(cstplItemShowPara.getCreatedDate());
            cttItemShowTemp.setLastUpdBy(cstplItemShowPara.getLastUpdBy());
            cttItemShowTemp.setLastUpdDate(cstplItemShowPara.getLastUpdDate());
            cttItemShowTemp.setModificationNum(cstplItemShowPara.getModificationNum());
            cttItemShowTemp.setCorrespondingPkid(cstplItemShowPara.getCorrespondingPkid());
            cttItemShowTemp.setStrCorrespondingItemNo(null);
        }
        else if(strTkcttOrCstpl .equals("Cstpl")){
            cttItemShowTemp.setStrNo(cstplItemShowPara.getStrNoContrast());
            cttItemShowTemp.setPkid(cstplItemShowPara.getPkidContrast()) ;
            cttItemShowTemp.setBelongToType(cstplItemShowPara.getBelongToTypeContrast());
            cttItemShowTemp.setBelongToPkid(cstplItemShowPara.getBelongToPkidContrast());
            cttItemShowTemp.setParentPkid(cstplItemShowPara.getParentPkidContrast()) ;
            cttItemShowTemp.setGrade(cstplItemShowPara.getGradeContrast()) ;
            cttItemShowTemp.setOrderid(cstplItemShowPara.getOrderidContrast()) ;
            cttItemShowTemp.setName(cstplItemShowPara.getNameContrast()) ;
            cttItemShowTemp.setNote(cstplItemShowPara.getNoteContrast()) ;

            cttItemShowTemp.setUnit(cstplItemShowPara.getUnitContrast());
            cttItemShowTemp.setContractUnitPrice(cstplItemShowPara.getContractUnitPriceContrast()) ;
            cttItemShowTemp.setContractQuantity(cstplItemShowPara.getContractQuantityContrast()) ;
            cttItemShowTemp.setContractAmount(cstplItemShowPara.getContractAmountContrast()) ;
            cttItemShowTemp.setSignPartAPrice(cstplItemShowPara.getSignPartAPriceContrast()) ;
            cttItemShowTemp.setDeletedFlag(cstplItemShowPara.getDeletedFlagContrast());
            cttItemShowTemp.setOriginFlag(cstplItemShowPara.getOriginFlagContrast()) ;
            cttItemShowTemp.setCreatedBy(cstplItemShowPara.getCreatedByContrast());
            cttItemShowTemp.setCreatedDate(cstplItemShowPara.getCreatedDateContrast());
            cttItemShowTemp.setLastUpdBy(cstplItemShowPara.getLastUpdByContrast());
            cttItemShowTemp.setLastUpdDate(cstplItemShowPara.getLastUpdDateContrast());
            cttItemShowTemp.setModificationNum(cstplItemShowPara.getModificationNumContrast());

            String strCorrespondingPkid= cstplItemShowPara.getCorrespondingPkidContrast();
            cttItemShowTemp.setCorrespondingPkid(strCorrespondingPkid);

            if(getItemOfTkcttAndCstplByPkid(strCorrespondingPkid, cstplItemShowList,"Tkctt")!=null) {
                cttItemShowTemp.setStrCorrespondingItemNo(
                        getItemOfTkcttAndCstplByPkid(strCorrespondingPkid, cstplItemShowList,"Tkctt").getStrNo());
            }else{
                cttItemShowTemp.setStrCorrespondingItemNo(null);
            }
        }
        return cttItemShowTemp;
    }
    /*�ܰ���ͬ���ɱ��ƻ�*/
    private CstplItemShow getItemOfTkcttAndCstplByItemOfEsItemHieRelap(
            CttItemShow cttItemShowPara,String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =new CstplItemShow() ;
        if(strTkcttOrCstpl .equals("Tkctt")){
            cstplItemShowTemp.setStrNo(cttItemShowPara.getStrNo());
            cstplItemShowTemp.setPkid(cttItemShowPara.getPkid()) ;
            cstplItemShowTemp.setBelongToType(cttItemShowPara.getBelongToType()) ;
            cstplItemShowTemp.setBelongToPkid(cttItemShowPara.getBelongToPkid()) ;
            cstplItemShowTemp.setParentPkid(cttItemShowPara.getParentPkid()) ;
            cstplItemShowTemp.setGrade(cttItemShowPara.getGrade()) ;
            cstplItemShowTemp.setOrderid(cttItemShowPara.getOrderid()) ;
            cstplItemShowTemp.setName(cttItemShowPara.getName()) ;
            cstplItemShowTemp.setNote(cttItemShowPara.getNote()) ;
            cstplItemShowTemp.setCorrespondingPkid(cttItemShowPara.getCorrespondingPkid());

            cstplItemShowTemp.setUnit(cttItemShowPara.getUnit());
            cstplItemShowTemp.setContractUnitPrice(cttItemShowPara.getContractUnitPrice()) ;
            cstplItemShowTemp.setContractQuantity(cttItemShowPara.getContractQuantity()) ;
            cstplItemShowTemp.setContractAmount(cttItemShowPara.getContractAmount()) ;
            cstplItemShowTemp.setSignPartAPrice(cttItemShowPara.getSignPartAPrice()) ;
            cstplItemShowTemp.setDeletedFlag(cttItemShowPara.getDeletedFlag());
            cstplItemShowTemp.setOriginFlag(cttItemShowPara.getOriginFlag()) ;
            cstplItemShowTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
            cstplItemShowTemp.setCreatedDate(cttItemShowPara.getCreatedDate());
            cstplItemShowTemp.setLastUpdBy(cttItemShowPara.getLastUpdBy());
            cstplItemShowTemp.setLastUpdDate(cttItemShowPara.getLastUpdDate());
            cstplItemShowTemp.setModificationNum(cttItemShowPara.getModificationNum());
        }
        else if(strTkcttOrCstpl .equals("Cstpl")){
            cstplItemShowTemp.setStrNoContrast(cttItemShowPara.getStrNo());
            cstplItemShowTemp.setPkidContrast(cttItemShowPara.getPkid()) ;
            cstplItemShowTemp.setBelongToTypeContrast(cttItemShowPara.getBelongToType()) ;
            cstplItemShowTemp.setBelongToPkidContrast(cttItemShowPara.getBelongToPkid()) ;
            cstplItemShowTemp.setParentPkidContrast(cttItemShowPara.getParentPkid()) ;
            cstplItemShowTemp.setGradeContrast(cttItemShowPara.getGrade()) ;
            cstplItemShowTemp.setOrderidContrast(cttItemShowPara.getOrderid()) ;
            cstplItemShowTemp.setNameContrast(cttItemShowPara.getName()) ;
            cstplItemShowTemp.setNoteContrast(cttItemShowPara.getNote()) ;
            cstplItemShowTemp.setCorrespondingPkidContrast(cttItemShowPara.getCorrespondingPkid());

            cstplItemShowTemp.setUnitContrast(cttItemShowPara.getUnit());
            cstplItemShowTemp.setContractUnitPriceContrast(cttItemShowPara.getContractUnitPrice()) ;
            cstplItemShowTemp.setContractQuantityContrast(cttItemShowPara.getContractQuantity()) ;
            cstplItemShowTemp.setContractAmountContrast(cttItemShowPara.getContractAmount()) ;
            cstplItemShowTemp.setSignPartAPriceContrast(cttItemShowPara.getSignPartAPrice()) ;
            cstplItemShowTemp.setDeletedFlagContrast(cttItemShowPara.getDeletedFlag());
            cstplItemShowTemp.setOriginFlagContrast(cttItemShowPara.getOriginFlag()) ;
            cstplItemShowTemp.setCreatedByContrast(cttItemShowPara.getCreatedBy());
            cstplItemShowTemp.setCreatedDateContrast(cttItemShowPara.getCreatedDate());
            cstplItemShowTemp.setLastUpdByContrast(cttItemShowPara.getLastUpdBy());
            cstplItemShowTemp.setLastUpdDateContrast(cttItemShowPara.getLastUpdDate());
            cstplItemShowTemp.setModificationNumContrast(cttItemShowPara.getModificationNum());
        }
        return cstplItemShowTemp;
    }

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<CttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CttItemShow> cttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CttItemShow itemUnit: cttItemShowListPara){
            if(itemUnit.getGrade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0){
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

    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public List<CstplItemShow> getCstplItemShowList() {
        return cstplItemShowList;
    }

    public void setCstplItemShowList(List<CstplItemShow> cstplItemShowList) {
        this.cstplItemShowList = cstplItemShowList;
    }

    public CstplItemShow getCstplItemShowSelected() {
        return cstplItemShowSelected;
    }

    public void setCstplItemShowSelected(CstplItemShow cstplItemShowSelected) {
        this.cstplItemShowSelected = cstplItemShowSelected;
    }

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
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

    public String getStrSubmitType() {
        return strSubmitType;
    }
    public StyleModel getStyleModelNo() {
        return styleModelNo;
    }
    public StyleModel getStyleModel() {
        return styleModel;
    }
    public String getStrMngNotFinishFlag() {
        return strMngNotFinishFlag;
    }

    public CttItemShow getCttItemShowAdd() {
        return cttItemShowAdd;
    }

    public void setCttItemShowAdd(CttItemShow cttItemShowAdd) {
        this.cttItemShowAdd = cttItemShowAdd;
    }

    public CttItemShow getCttItemShowDel() {
        return cttItemShowDel;
    }

    public void setCttItemShowDel(CttItemShow cttItemShowDel) {
        this.cttItemShowDel = cttItemShowDel;
    }

    public CttItemShow getCttItemShowUpd() {
        return cttItemShowUpd;
    }

    public void setCttItemShowUpd(CttItemShow cttItemShowUpd) {
        this.cttItemShowUpd = cttItemShowUpd;
    }

    public CttItemShow getCttItemShowSel() {
        return cttItemShowSel;
    }

    public void setCttItemShowSel(CttItemShow cttItemShowSel) {
        this.cttItemShowSel = cttItemShowSel;
    }

    /*�����ֶ�End*/
}