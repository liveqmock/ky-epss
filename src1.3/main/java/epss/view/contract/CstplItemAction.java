package epss.view.contract;

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
import epss.repository.model.CstplInfo;
import epss.repository.model.CstplItem;
import epss.repository.model.TkcttItem;
import epss.repository.model.model_show.CstplItemShow;
import epss.service.*;
import epss.service.CstplInfoService;
import epss.view.common.EsCommon;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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
public class CstplItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplItemAction.class);
    @ManagedProperty(value = "#{cstplInfoService}")
    private CstplInfoService cstplInfoService;
    @ManagedProperty(value = "#{cstplItemService}")
    private CstplItemService cstplItemService;
    @ManagedProperty(value = "#{tkcttItemService}")
    private TkcttItemService tkcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    /*��ѯ���ύ��*/
    private CstplItemShow cstplItemShowSel;
    private CstplItemShow cstplItemShowAdd;
    private CstplItemShow cstplItemShowUpd;
    private CstplItemShow cstplItemShowDel;
    /*�б���ѡ��һ��*/
    private CstplItemShow cstplItemShowSelected;
    /*�б���ʾ��*/
    private List<CstplItemShow> cstplItemShowList;

    /*������*/
    private String strCstplInfoPkid;

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
        strCstplInfoPkid=parammap.get("strCstplInfoPkid").toString();
        resetAction();
        initData() ;
    }
    private void initData() {
        try{
            /*�ܰ���ͬ�б�*/
            List<TkcttItem> tkcttItemList =new ArrayList<TkcttItem>();
            // ���Ҹóɱ��ƻ���Ӧ���ܰ���ͬ
            CstplInfo cstplInfoTemp = cstplInfoService.getCstplInfoByPkid(strCstplInfoPkid);
            strMngNotFinishFlag="true";
            if(cstplInfoTemp!=null&& EnumFlowStatus.FLOW_STATUS0.getCode().equals(cstplInfoTemp.getFlowStatus())) {
                strMngNotFinishFlag="false";
            }
            String strTkcttInfoPkidInCstplInfo= cstplInfoTemp.getTkcttInfoPkid();
            // ���Ҹóɱ��ƻ���Ӧ���ܰ���ͬ
            tkcttItemList = tkcttItemService.getTkcttItemListByTkcttInfoPkid(strTkcttInfoPkidInCstplInfo);
            List<CstplItemShow> cstplItemShowListTkctt =new ArrayList<>();
            recursiveByTkcttItemList("root", tkcttItemList, cstplItemShowListTkctt);
            cstplItemShowListTkctt =getItemOfEsItemHieRelapList_DoFromatNo(cstplItemShowListTkctt);

            /*�ɱ��ƻ��б�*/
            List<CstplItem> cstplItemListCstpl = cstplItemService.getCstplListByCstplInfoPkid(strCstplInfoPkid);

            List<CstplItemShow> cstplItemShowListCstpl =new ArrayList<>();
            recursiveByCstplItemList("root", cstplItemListCstpl, cstplItemShowListCstpl);
            cstplItemShowListCstpl =getItemOfEsItemHieRelapList_DoFromatNo(
                    cstplItemShowListCstpl) ;

            /*ƴװ�б�*/
            List<CstplItemShow> cstplItemShowList_ForSort =new ArrayList<>();
            int i=0;
            for(CstplItemShow itemTkctt: cstplItemShowListTkctt){
                i++;
                Boolean insertedFlag=false ;
                String strFrontNoAndName="";
                CstplItemShow itemTkcttInsertItem;
                for(CstplItemShow itemCstpl: cstplItemShowListCstpl){
                    itemTkcttInsertItem=new CstplItemShow();
                    if(itemTkctt.getTkctt_Pkid().equals(itemCstpl.getCstpl_TkcttItemPkid())){
                        //�ܰ���ͬ
                        if(strFrontNoAndName.equals(itemTkctt.getTkctt_StrNo()+itemTkctt .getTkctt_Name())){
                            itemTkcttInsertItem.setTkctt_StrNo("") ;
                            itemTkcttInsertItem.setTkctt_Name("") ;
                            itemTkcttInsertItem.setTkctt_Remark("");
                            itemTkcttInsertItem.setTkctt_Pkid(cstplItemShowList_ForSort.size() +";"+itemTkctt.getTkctt_Pkid());
                            itemTkcttInsertItem.setTkctt_Unit("");
                            itemTkcttInsertItem.setTkctt_UnitPrice(null) ;
                            itemTkcttInsertItem.setTkctt_Qty(null) ;
                            itemTkcttInsertItem.setTkctt_Amt(null) ;
                        }else{
                            strFrontNoAndName=itemTkctt.getTkctt_StrNo()+itemTkctt .getTkctt_Name();
                            itemTkcttInsertItem.setTkctt_StrNo(itemTkctt.getTkctt_StrNo());
                            itemTkcttInsertItem.setTkctt_Name(itemTkctt.getTkctt_Name());
                            itemTkcttInsertItem.setTkctt_Remark(itemTkctt.getTkctt_Remark());
                            itemTkcttInsertItem.setTkctt_Pkid(itemTkctt.getTkctt_Pkid());
                            itemTkcttInsertItem.setTkctt_Unit(itemTkctt.getTkctt_Unit());
                            itemTkcttInsertItem.setTkctt_UnitPrice(itemTkctt.getTkctt_UnitPrice()) ;
                            itemTkcttInsertItem.setTkctt_Qty(itemTkctt.getTkctt_Qty()) ;
                            itemTkcttInsertItem.setTkctt_Amt(itemTkctt.getTkctt_Amt()) ;
                        }
                        itemTkcttInsertItem.setTkctt_Grade(itemTkctt.getTkctt_Grade());
                        itemTkcttInsertItem.setTkctt_Orderid(itemTkctt.getTkctt_Orderid()) ;
                        itemTkcttInsertItem.setTkctt_ArchivedFlag(itemTkctt.getTkctt_ArchivedFlag());
                        itemTkcttInsertItem.setTkctt_OriginFlag(itemTkctt.getTkctt_OriginFlag()) ;
                        itemTkcttInsertItem.setTkctt_CreatedBy(itemTkctt.getTkctt_CreatedBy());
                        itemTkcttInsertItem.setTkctt_CreatedTime(itemTkctt.getTkctt_CreatedTime());
                        itemTkcttInsertItem.setTkctt_UpdatedBy(itemTkctt.getTkctt_UpdatedBy());
                        itemTkcttInsertItem.setTkctt_UpdatedTime(itemTkctt.getTkctt_UpdatedTime());
                        itemTkcttInsertItem.setTkctt_RecVersion(itemTkctt.getTkctt_RecVersion());
                        //�ɱ��ƻ�
                        itemTkcttInsertItem.setCstpl_StrNo(itemCstpl.getCstpl_StrNo());
                        itemTkcttInsertItem.setCstpl_Pkid(itemCstpl.getCstpl_Pkid());
                        itemTkcttInsertItem.setCstpl_CstplInfoPkid(itemCstpl.getCstpl_CstplInfoPkid());
                        itemTkcttInsertItem.setCstpl_ParentPkid(itemCstpl.getCstpl_ParentPkid());
                        itemTkcttInsertItem.setCstpl_Grade(itemCstpl.getCstpl_Grade());
                        itemTkcttInsertItem.setCstpl_Orderid(itemCstpl.getCstpl_Orderid());
                        itemTkcttInsertItem.setCstpl_Name(itemCstpl.getCstpl_Name());
                         itemTkcttInsertItem.setCstpl_Unit(itemCstpl.getCstpl_Unit());
                        itemTkcttInsertItem.setCstpl_UnitPrice(itemCstpl.getCstpl_UnitPrice());
                        itemTkcttInsertItem.setCstpl_Qty(itemCstpl.getCstpl_Qty());
                        itemTkcttInsertItem.setCstpl_Amt(itemCstpl.getCstpl_Amt());
                        itemTkcttInsertItem.setCstpl_ArchivedFlag(itemCstpl.getCstpl_ArchivedFlag());
                        itemTkcttInsertItem.setCstpl_OriginFlag(itemCstpl.getCstpl_OriginFlag());
                        itemTkcttInsertItem.setCstpl_CreatedBy(itemCstpl.getCstpl_CreatedBy());
                        itemTkcttInsertItem.setCstpl_CreatedTime(itemCstpl.getCstpl_CreatedTime());
                        itemTkcttInsertItem.setCstpl_UpdatedBy(itemCstpl.getCstpl_UpdatedBy());
                        itemTkcttInsertItem.setCstpl_UpdatedTime(itemCstpl.getCstpl_UpdatedTime());
                        itemTkcttInsertItem.setCstpl_RecVersion(itemCstpl.getCstpl_RecVersion());
                        itemTkcttInsertItem.setCstpl_TkcttItemPkid(itemCstpl.getCstpl_TkcttItemPkid());
                        itemTkcttInsertItem.setCstpl_Remark(itemCstpl.getCstpl_Remark());
                        if(ToolUtil.getStrIgnoreNull(itemTkcttInsertItem.getCstpl_Pkid()).equals("")){
                            itemTkcttInsertItem.setCstpl_Pkid(cstplItemShowList_ForSort.size() +"");
                        }
                        insertedFlag=true ;
                        cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                    }
                }
                if (insertedFlag.equals(false)){
                    itemTkcttInsertItem=(CstplItemShow)BeanUtils.cloneBean(itemTkctt);
                    cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                }
            }

            for(CstplItemShow itemCstpl: cstplItemShowListCstpl){
                Boolean insertedFlag=false ;
                for(CstplItemShow itemTkctt: cstplItemShowListTkctt){
                    if(itemTkctt.getTkctt_Pkid().equals(itemCstpl.getCstpl_TkcttItemPkid())){
                        insertedFlag=true;
                        break;
                    }
                }
                if(insertedFlag.equals(false)){
                    CstplItemShow itemTkcttInsertItem=(CstplItemShow)BeanUtils.cloneBean(itemCstpl);
                    if (ToolUtil.getStrIgnoreNull(itemTkcttInsertItem.getTkctt_Pkid()).equals("")){
                        itemTkcttInsertItem.setTkctt_Pkid(cstplItemShowList_ForSort.size() +"");
                    }
                    cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                }
            }

            for(CstplItemShow itemUnit: cstplItemShowList_ForSort){
                if(itemUnit.getCstpl_StrNo()!=null){
                    String strCstplTkcttItemNoTemp=ToolUtil.getStrIgnoreNull(itemUnit.getCstpl_TkcttItemNo());
                    CstplItemShow cstplItemShowTemp =
                            getItemOfTkcttAndCstplByPkid(strCstplTkcttItemNoTemp, cstplItemShowList_ForSort, "Tkctt");
                    if(cstplItemShowTemp !=null){
                        itemUnit.setCstpl_TkcttItemNo(cstplItemShowTemp.getCstpl_StrNo());
                    }
                } else{
                    itemUnit.setCstpl_TkcttItemNo(itemUnit.getCstpl_StrNo());
                }
            }

            cstplItemShowList =new ArrayList<CstplItemShow>();
            cstplItemShowList =getItemOfTkcttAndCstplListSorted(cstplItemShowList_ForSort,0);
            // ��Ӻϼ�
            setItemOfTkcttAndCstplList_AddTotal();
        } catch (Exception e) {
            logger.error("��ʼ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void resetAction(){
        strSubmitType="Add";
        styleModelNo=new StyleModel();
        styleModelNo.setDisabled_Flag("false");
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cstplItemShowSel =new CstplItemShow();
        cstplItemShowSel.setCstpl_CstplInfoPkid(strCstplInfoPkid);
        cstplItemShowAdd =new CstplItemShow();
        cstplItemShowAdd.setCstpl_CstplInfoPkid(strCstplInfoPkid);
        cstplItemShowUpd =new CstplItemShow();
        cstplItemShowUpd.setCstpl_CstplInfoPkid(strCstplInfoPkid);
        cstplItemShowDel =new CstplItemShow();
        cstplItemShowDel.setCstpl_CstplInfoPkid(strCstplInfoPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cstplItemShowAdd =new CstplItemShow();
        cstplItemShowAdd.setCstpl_CstplInfoPkid(strCstplInfoPkid);
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cstplItemShowSel.getCstpl_Name()).equals("")){
            if(ToolUtil.getStrIgnoreNull(strSubmitType).equals("Upd")) {
                styleModelNo.setDisabled_Flag("true");
            }else{
                styleModelNo.setDisabled_Flag("false");
            }
        }
        return true;
    }
    public void selectRecordAction(String strSubmitTypePara){
        try {
            String  strSubmitTypeBefore= strSubmitType;
            strSubmitType=strSubmitTypePara;
            switch (strSubmitTypePara) {
                case "Sel" :
                    cstplItemShowSel =(CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
                    cstplItemShowSel.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSel.getCstpl_StrNo())) ;
                    cstplItemShowSel.setCstpl_TkcttItemNo(
                            ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSel.getCstpl_TkcttItemNo()));
                    break;
                case "Upd":
                    cstplItemShowUpd = (CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
                    cstplItemShowUpd.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowUpd.getCstpl_StrNo()));
                    cstplItemShowUpd.setCstpl_TkcttItemNo(
                            ToolUtil.getIgnoreSpaceOfStr(cstplItemShowUpd.getCstpl_TkcttItemNo()));
                    break;
                case "Del":
                    if(cstplItemShowSelected.getCstpl_StrNo()==null) {
                        MessageUtil.addInfo("û�п�ɾ�������ݣ�");
                        cstplItemShowDel =new CstplItemShow();
                        return;
                    }
                    else{
                        cstplItemShowDel =(CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
                    }
                    cstplItemShowDel.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowDel.getCstpl_StrNo())); ;
                    cstplItemShowDel.setCstpl_TkcttItemNo(
                            ToolUtil.getIgnoreSpaceOfStr(cstplItemShowDel.getCstpl_TkcttItemNo()));
                    break;
                case "From_tkctt_to_cstpl":
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
            if (cstplItemShowAdd.getCstpl_UnitPrice() == null || cstplItemShowAdd.getCstpl_Qty() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cstplItemShowAdd.getCstpl_UnitPrice().multiply(cstplItemShowAdd.getCstpl_Qty());
            }
            cstplItemShowAdd.setCstpl_Amt(bigDecimal);
        }
        if (strSubmitType.equals("Upd")) {
            if (cstplItemShowUpd.getCstpl_UnitPrice() == null || cstplItemShowUpd.getCstpl_Qty() == null) {
                bigDecimal = null;
            } else {
                bigDecimal = cstplItemShowUpd.getCstpl_UnitPrice().multiply(cstplItemShowUpd.getCstpl_Qty());
            }
            cstplItemShowUpd.setCstpl_Amt(bigDecimal);
        }
    }
    public Boolean blurStrNoToGradeAndOrderid(String strIsBlur){
        CstplItemShow cstplItemShowTemp =new CstplItemShow(strCstplInfoPkid);
        if (strSubmitType.equals("Add")){
            cstplItemShowTemp = cstplItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cstplItemShowTemp = cstplItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCstpl_StrNo());
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
                cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(
                        strIgnoreSpaceOfStr, cstplItemShowList, "Cstpl");

                String correspondingItemNoContrast= ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCstpl_TkcttItemNo());
                if(!ToolUtil.getStrIgnoreNull(cstplItemShowTemp.getCstpl_TkcttItemNo())
                        .equals(cstplItemShowTemp.getCstpl_TkcttItemNo())){
                    MessageUtil.addError("�ñ���(" + strIgnoreSpaceOfStr + ")����Ӧ����(" + correspondingItemNoContrast
                            + ")�Ѿ�����,������Ķ�Ӧ���루" + cstplItemShowTemp.getCstpl_TkcttItemNo() +
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
            cstplItemShowTemp.setCstpl_Grade(1);
            cstplItemShowTemp.setCstpl_Orderid(Integer.parseInt(strIgnoreSpaceOfStr));
            cstplItemShowTemp.setCstpl_ParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            cstplItemShowTemp =new CstplItemShow();
            cstplItemShowTemp =getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList, "Cstpl");
            if(cstplItemShowTemp ==null|| cstplItemShowTemp.getTkctt_Pkid()==null){
                MessageUtil.addError("��ȷ������ı��룡����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            }
            else{
                List<CstplItemShow> cstplItemShowSubTemp =new ArrayList<CstplItemShow>();
                cstplItemShowSubTemp =getItemOfTkcttAndCstplByLevelParentPkid(
                        cstplItemShowTemp.getCstpl_Pkid(),
                        cstplItemShowList, "Cstpl");
                if(cstplItemShowSubTemp.size() ==0){
                    if(!cstplItemShowTemp.getCstpl_StrNo().equals(strParentNo+".1") ){
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
                cstplItemShowTemp.setCstpl_Grade(strTemps.length) ;
                cstplItemShowTemp.setCstpl_Orderid(Integer.parseInt(strTemps[strTemps.length - 1]));
                cstplItemShowTemp.setCstpl_ParentPkid(cstplItemShowTemp.getCstpl_TkcttItemPkid()) ;
            }
        }
        if(strIsBlur.equals("true")){
            cstplItemShowTemp =
                    getItemOfTkcttAndCstplByStrNo(strIgnoreSpaceOfStr, cstplItemShowList,"Tkctt");
            if(cstplItemShowTemp !=null){
                cstplItemShowTemp.setCstpl_TkcttItemNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowTemp.getCstpl_StrNo())) ;
                cstplItemShowTemp.setCstpl_TkcttItemPkid(cstplItemShowTemp.getCstpl_Pkid());
            }else{
                cstplItemShowTemp.setCstpl_TkcttItemNo(null);
                cstplItemShowTemp.setCstpl_TkcttItemPkid(null);
            }
        }
        return true ;
    }

    public Boolean blurCorrespondingPkid(){
        CstplItemShow cstplItemShowTemp =new CstplItemShow(strCstplInfoPkid );
        if (strSubmitType.equals("Add")){
            cstplItemShowTemp = cstplItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cstplItemShowTemp = cstplItemShowUpd;
        }
        cstplItemShowTemp =
                getItemOfTkcttAndCstplByStrNo(cstplItemShowTemp.getCstpl_TkcttItemNo(), cstplItemShowList, "Tkctt");
        if(cstplItemShowTemp !=null && cstplItemShowTemp.getCstpl_StrNo() !=null){

            String strNo= cstplItemShowTemp.getCstpl_StrNo();
            if(strNo!=null&&!strNo.equals("")){
                CstplItemShow cstplTempForCstplItemShow =getItemOfTkcttAndCstplByStrNo(
                        strNo, cstplItemShowList, "Cstpl");
                if(cstplTempForCstplItemShow !=null) {
                    String correspondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(cstplTempForCstplItemShow.getCstpl_TkcttItemNo());
                    if(cstplItemShowTemp.getCstpl_TkcttItemNo()!=null&&
                            !correspondingItemNoContrast.equals(cstplItemShowTemp.getCstpl_TkcttItemNo())){
                        MessageUtil.addError("�ñ���(" + strNo + ")����Ӧ����(" + correspondingItemNoContrast
                                + ")�Ѿ�����,������Ķ�Ӧ���루" + cstplItemShowTemp.getCstpl_TkcttItemNo() +
                                ")ִ�в������ʱ������߼����󣬽��޷����룡");
                        return strNoBlurFalse();
                    }
                }
            }
            cstplItemShowTemp.setCstpl_TkcttItemNo(cstplItemShowTemp.getCstpl_Pkid());
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
                    if(ToolUtil.getIntIgnoreNull(itemUnitConstructItem .getCstpl_Grade())
                            <=intItemUnitConstructSelectedGrade){
                        break;
                    }
                    else{
                        cstplItemService.deleteRecord(itemUnitConstructItem.getCstpl_Pkid()) ;
                    }
                }
                if(intItemUnitConstructSelectedGrade==-1){
                    if(itemUnitConstructItem.equals(cstplItemShowSelected) ){
                        intItemUnitConstructSelectedGrade=itemUnitConstructItem.getCstpl_Grade();
                        int deleteRecordNumOfSelf= cstplItemService.deleteRecord(itemUnitConstructItem.getCstpl_Pkid()) ;
                        if (deleteRecordNumOfSelf<=0){
                            MessageUtil.addInfo("�ü�¼��ɾ����");
                            return;
                        }
                    }
                }
            }
            cstplItemService.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                    cstplItemShowSelected.getCstpl_CstplInfoPkid(),
                    cstplItemShowSelected.getCstpl_ParentPkid(),
                    cstplItemShowSelected.getCstpl_Grade());
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
                Integer intRecVersion=ToolUtil.getIntIgnoreNull(cstplItemShowUpd.getCstpl_RecVersion());
                cstplItemShowUpd.setCstpl_RecVersion(intRecVersion + 1);
                cstplItemService.updateRecord(cstplItemShowUpd) ;
            }
            else if(strSubmitType .equals("Add")) {
                CstplItem cstplItemTemp = cstplItemService.fromShowModelToModel(cstplItemShowAdd);
                if (cstplItemService.isExistSameRecordInDb(cstplItemTemp)){
                    MessageUtil.addInfo("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�롣");
                    return;
                }
                cstplItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        cstplItemShowAdd.getCstpl_CstplInfoPkid(),
                        cstplItemShowAdd.getCstpl_ParentPkid(),
                        cstplItemShowAdd.getCstpl_Grade());
                cstplItemService.insertRecord(cstplItemShowAdd);
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
        CstplItemShow cstplItemShowTemp =new CstplItemShow(strCstplInfoPkid);
        if (strSubmitType.equals("Add")){
            cstplItemShowTemp = cstplItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cstplItemShowTemp = cstplItemShowUpd;
        }
        if (StringUtils.isEmpty(cstplItemShowTemp.getCstpl_StrNo())) {

            MessageUtil.addError("�������ţ�");
            return false;
        }
        if (StringUtils.isEmpty(cstplItemShowTemp.getCstpl_Name())) {
            MessageUtil.addError("���������ƣ�");
            return false;
        }
        return true;
    }

    private void setItemOfTkcttAndCstplList_AddTotal(){
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        cstplItemShowListTemp.addAll(cstplItemShowList);

        cstplItemShowList.clear();
        // С��
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);

        BigDecimal bdTotalCstpl=new BigDecimal(0);
        BigDecimal bdAllTotalCstpl=new BigDecimal(0);

        CstplItemShow itemUnit=new CstplItemShow();
        CstplItemShow itemUnitNext=new CstplItemShow();
        for(int i=0;i< cstplItemShowListTemp.size();i++){
            itemUnit = cstplItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_Amt()));
            // ����
            bdTotalCstpl=bdTotalCstpl.add(ToolUtil.getBdIgnoreNull(itemUnit.getCstpl_Amt()));
            bdAllTotalCstpl=bdAllTotalCstpl.add(ToolUtil.getBdIgnoreNull(itemUnit.getCstpl_Amt()));
            cstplItemShowList.add(itemUnit);

            if(i+1< cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i+1);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                Boolean isRoot=false;
                if(ToolUtil.getStrIgnoreNull(itemUnitNext.getCstpl_ParentPkid()).equals("root")){
                    cstplItemShowTemp.setTkctt_Name("�ϼ�");
                    cstplItemShowTemp.setTkctt_Pkid("total"+i);
                    cstplItemShowTemp.setTkctt_Amt(bdTotal);
                    bdTotal=new BigDecimal(0);
                    isRoot=true;
                }

                if(ToolUtil.getStrIgnoreNull(itemUnitNext.getCstpl_ParentPkid()).equals("root")){
                    cstplItemShowTemp.setTkctt_Pkid("total"+i);
                    cstplItemShowTemp.setCstpl_Name("�ϼ�");
                    cstplItemShowTemp.setCstpl_Pkid("total_contrast"+i);
                    cstplItemShowTemp.setCstpl_Amt(bdTotalCstpl);
                    bdTotalCstpl = new BigDecimal(0);
                    isRoot=true;
                }

                if(isRoot.equals(true)){
                    cstplItemShowList.add(cstplItemShowTemp);
                }
            } else if(i+1== cstplItemShowListTemp.size()){
                itemUnitNext = cstplItemShowListTemp.get(i);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setTkctt_Name("�ϼ�");
                cstplItemShowTemp.setTkctt_Pkid("total"+i);
                cstplItemShowTemp.setTkctt_Amt(bdTotal);

                cstplItemShowTemp.setCstpl_Name("�ϼ�");
                cstplItemShowTemp.setCstpl_Pkid("total_contrast"+i);
                cstplItemShowTemp.setCstpl_Amt(bdTotalCstpl);
                cstplItemShowList.add(cstplItemShowTemp);
                bdTotal=new BigDecimal(0);
                bdTotalCstpl = new BigDecimal(0);

                // �ܺϼ�
                cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp.setTkctt_Name("�ܺϼ�");
                cstplItemShowTemp.setTkctt_Pkid("total_all"+i);
                cstplItemShowTemp.setTkctt_Amt(bdAllTotal);

                cstplItemShowTemp.setCstpl_Name("�ܺϼ�");
                cstplItemShowTemp.setCstpl_Pkid("total_all_contrast"+i);
                cstplItemShowTemp.setCstpl_Amt(bdAllTotalCstpl);
                cstplItemShowList.add(cstplItemShowTemp);
                bdAllTotal=new BigDecimal(0);
                bdAllTotalCstpl = new BigDecimal(0);
            }
        }
    }
    private boolean fromTkcttToCstplAction(){
        try{
            if(cstplItemShowSelected.getCstpl_StrNo() ==null||
                    cstplItemShowSelected.getCstpl_StrNo().equals("")) {
                MessageUtil.addInfo("�޿ɸ��ƣ�");
                return false;
            }
            if(!ToolUtil.getStrIgnoreNull(cstplItemShowSelected.getCstpl_StrNo()).equals("")) {
                MessageUtil.addInfo("�ɱ��ƻ��Ϊ�գ��ܰ���ͬ���ɱ��ƻ������޷����ƣ����踴�ƣ�����ɾ���óɱ��ƻ��");
                return false;
            }
            String strIgnoreSpaceOfStr=ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSelected.getCstpl_StrNo());
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
            cstplItemShowSel = (CstplItemShow)BeanUtils.cloneBean(cstplItemShowSelected);
            cstplItemShowSel.setCstpl_StrNo(ToolUtil.getIgnoreSpaceOfStr(cstplItemShowSel.getCstpl_StrNo())) ;
            cstplItemShowSel.setCstpl_CstplInfoPkid(strCstplInfoPkid);
            Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");
            if(intLastIndexof>0){
                String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
                CstplItemShow cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList,"Cstpl");
                cstplItemShowSel.setCstpl_ParentPkid(cstplItemShowTemp.getCstpl_Pkid());
                cstplItemShowSel.setCstpl_Grade(cstplItemShowTemp.getCstpl_Grade() + 1);
            }else{
                cstplItemShowSel.setCstpl_ParentPkid("root");
            }

            cstplItemShowSel.setCstpl_TkcttItemPkid(cstplItemShowSel.getCstpl_Pkid());
            cstplItemShowSel.setCstpl_TkcttItemNo(cstplItemShowSel.getCstpl_StrNo()) ;
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
        for(;i<modelList.size();i++){//���ѭ��ʵ�ֶ�����list�ı���
            int startI=i;            //���浱ǰԪ�صĳ�ʼλ��i,��Ϊ���ڲ�ѭ���л�ı�i��ֵ
            int countParent=0;       //ͳ�ƺ͵�ǰԪ�أ�����Ԫ�أ���ͬ��Ԫ�ظ�������Ԫ��ֻ����˳����ͬ
            int countChild=0;        //ͳ��ƥ�丸Ԫ�صĺ���Ԫ�صĸ���������Ԫ��Ҳ��˳�����
            int positionOfChild=0;   //��¼�º͸�Ԫ��ƥ��ĵ�һ�����ӵ�λ��
            strThisCorrespondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(modelList.get(i).getCstpl_TkcttItemNo());
            for(int j=i+1; j<modelList.size();j++){
                strThisCorrespondingItemNoContrastAfter=ToolUtil.getIgnoreSpaceOfStr(modelList.get(j).getCstpl_TkcttItemNo());
                if(strThisCorrespondingItemNoContrast.equals(strThisCorrespondingItemNoContrastAfter)){
                //�ж��ڲ�ѭ�����Ƿ��к����ѭ���е�Ԫ����ͬ��Ԫ�أ�����¼����������countParent
                    if(countParent==0){
                    //����ڲ�ѭ���д��ں��������ͬ��Ԫ�أ������ǵ�һ������������countParent��ֵ����Ϊ0��
                        countParent+=2;
                        //���״�����ʱ�ڲ�����Ҫ��countParent+2
                        continue;
                    }
                    ++countParent;
                }
                else{//else�·�Ϊ�������
                    if(countParent==0||!strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast))
                        break;
                        //1.�ڲ���û�к������Ԫ����ͬ��Ԫ�أ�����֪������list֪���������ֱ���˳��ڲ�ѭ�����ɣ�
                        //2.�ڲ����к������Ԫ����ͬ��Ԫ�أ������ڲ���û���亢�ӣ�����֪������list֪���������ֱ���˳��ڲ�ѭ������
                    if((strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast)&&
                            strThisCorrespondingItemNoContrastAfter.compareTo(strThisCorrespondingItemNoContrast)>0)){
                        if(positionOfChild==0){
                            //�ڲ����к������Ԫ����ͬ��Ԫ�أ�
                            positionOfChild=j;
                            //��¼�µ�һ�����ӵ�λ�ñ�����positionOfChild�С�
                            countChild+=1;
                            //�ڲ����к������Ԫ����ͬ��Ԫ�أ����һ����亢�ӣ���ͳ���亢�ӵĸ�����������countChild������countParent��ֵ����Ϊ0�����ڲ�����Ҫ��countParent+1
                            continue;
                        }
                        ++countChild;
                    }
                    if(countChild!=0)//�Ժ��Ӳ��õݹ�˼��
                        modelList=getItemOfTkcttAndCstplListSorted(modelList, positionOfChild);
                        //�ݹ��õ���ƥ�丸Ԫ�صĺ��ӵ���ȷ����
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
    private void recursiveByTkcttItemList(String strLevelParentId,
                                            List<TkcttItem> tkcttItemListPara,
                                            List<CstplItemShow> cstplItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<TkcttItem> tkcttItemListTemp =new ArrayList<TkcttItem>();
        // ͨ������id�������ĺ���
        tkcttItemListTemp =getTkcttItemListByLevelParentPkid(strLevelParentId, tkcttItemListPara);
        for(TkcttItem itemUnit: tkcttItemListTemp){
            CstplItemShow cstplItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strUpdatedByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
            // �㼶��
            cstplItemShowTemp = new CstplItemShow(
                itemUnit.getPkid(),
                itemUnit.getTkcttInfoPkid(),
                itemUnit.getParentPkid(),
                "",
                itemUnit.getGrade(),
                itemUnit.getOrderid(),
                itemUnit.getName(),
                itemUnit.getUnit(),
                itemUnit.getUnitPrice(),
                itemUnit.getQty(),
                itemUnit.getAmt(),
                itemUnit.getArchivedFlag(),
                itemUnit.getOriginFlag(),
                itemUnit.getCreatedBy(),
                strCreatedByName,
                itemUnit.getCreatedTime(),
                itemUnit.getUpdatedBy(),
                strUpdatedByName,
                itemUnit.getUpdatedTime(),
                itemUnit.getRecVersion(),
                itemUnit.getRemark(),
                itemUnit.getTid()
            );
            cstplItemShowListPara.add(cstplItemShowTemp) ;
            recursiveByTkcttItemList(cstplItemShowTemp.getCstpl_Pkid(), tkcttItemListPara, cstplItemShowListPara);
        }
    }
    private void recursiveByCstplItemList(
            String strLevelParentId,
            List<CstplItem> cstplItemListPara,
            List<CstplItemShow> cstplItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CstplItem> cstplItemList =new ArrayList<CstplItem>();
        // ͨ������id�������ĺ���
        cstplItemList =getCstplItemListByLevelParentPkid(strLevelParentId, cstplItemListPara);
        for(CstplItem itemUnit: cstplItemList){
            CstplItemShow cstplItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strUpdatedByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
            // �㼶��
            cstplItemShowTemp = new CstplItemShow(
                    itemUnit.getPkid(),
                    itemUnit.getCstplInfoPkid(),
                    itemUnit.getParentPkid(),
                    "",
                    itemUnit.getGrade(),
                    itemUnit.getOrderid(),
                    itemUnit.getName(),
                    itemUnit.getUnit(),
                    itemUnit.getUnitPrice(),
                    itemUnit.getQty(),
                    itemUnit.getAmt(),
                    itemUnit.getArchivedFlag(),
                    itemUnit.getOriginFlag(),
                    itemUnit.getCreatedBy(),
                    strCreatedByName,
                    itemUnit.getCreatedTime(),
                    itemUnit.getUpdatedBy(),
                    strUpdatedByName,
                    itemUnit.getUpdatedTime(),
                    itemUnit.getRecVersion(),
                    itemUnit.getRemark(),
                    itemUnit.getTkcttItemPkid(),
                    "",
                    itemUnit.getTid()
            );
            cstplItemShowListPara.add(cstplItemShowTemp) ;
            recursiveByCstplItemList(cstplItemShowTemp.getCstpl_Pkid(), cstplItemListPara, cstplItemShowListPara);
        }
    }
    /*�����Ϲ淶�ı������벢�뿪ʱ,�����Ӧ�Ķ���*/
    private Boolean strNoBlurFalse(){
        cstplItemShowSel.setCstpl_Pkid("") ;
        cstplItemShowSel.setCstpl_ParentPkid("");
        cstplItemShowSel.setCstpl_Grade(null);
        cstplItemShowSel.setCstpl_Orderid(null);
        return false;
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<TkcttItem> getTkcttItemListByLevelParentPkid(
            String strLevelParentPkid,
            List<TkcttItem> tkcttItemListPara) {
        List<TkcttItem> tkcttItemListTemp =new ArrayList<TkcttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(TkcttItem itemUnit: tkcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tkcttItemListTemp.add(itemUnit);
            }
        }
        return tkcttItemListTemp;
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CstplItem> getCstplItemListByLevelParentPkid(
            String strLevelParentPkid,
            List<CstplItem> cstplItemListPara) {
        List<CstplItem> tempCstplItemList =new ArrayList<CstplItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(CstplItem itemUnit: cstplItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempCstplItemList.add(itemUnit);
            }
        }
        return tempCstplItemList;
    }
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CstplItemShow> getItemOfTkcttAndCstplByLevelParentPkid(
            String strLevelParentPkid,
            List<CstplItemShow> esCstplItemShowListPara
            ,String strTkcttOrCstpl) {
        List<CstplItemShow> cstplItemShowListTemp =new ArrayList<CstplItemShow>();
        /*�ܿ��ظ��������ݿ�*/
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getCstpl_ParentPkid()).equalsIgnoreCase(strLevelParentPkid)){
                        cstplItemShowListTemp.add(itemUnit);
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: esCstplItemShowListPara){
                    if(ToolUtil.getStrIgnoreNull(itemUnit.getCstpl_ParentPkid()).equalsIgnoreCase(strLevelParentPkid)){
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
    private CstplItemShow getItemOfTkcttAndCstplByStrNo(
            String strNo,
            List<CstplItemShow> cstplItemShowListPara,
            String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getCstpl_StrNo()).equals(strNo)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getCstpl_StrNo()).equals(strNo)) {
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
    private CstplItemShow getItemOfTkcttAndCstplByPkid(
            String strPkid,
            List<CstplItemShow> cstplItemShowListPara,
            String strTkcttOrCstpl){
        CstplItemShow cstplItemShowTemp =null;
        try{
            if(strTkcttOrCstpl .equals("Tkctt")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getTkctt_Pkid()).equals(strPkid)) {
                        cstplItemShowTemp =(CstplItemShow)BeanUtils.cloneBean(itemUnit);
                        break;
                    }
                }
            }
            else if(strTkcttOrCstpl .equals("Cstpl")){
                for(CstplItemShow itemUnit: cstplItemShowListPara){
                    if(itemUnit!=null&&ToolUtil.getIgnoreSpaceOfStr(itemUnit.getCstpl_Pkid()).equals(strPkid)) {
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

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<CstplItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<CstplItemShow> cstplItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(CstplItemShow itemUnit: cstplItemShowListPara){
            if(itemUnit.getCstpl_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0){
                    strTemp=itemUnit.getCstpl_Orderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getCstpl_Orderid().toString();
                }
            }
            else{
                if(itemUnit.getCstpl_Grade()==1){
                    strTemp=itemUnit.getCstpl_Orderid().toString() ;
                }
                else {
                    if (!itemUnit.getCstpl_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getCstpl_Grade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getCstpl_Orderid().toString();
                        } else {
                            Integer intTemp=ToolUtil.lookIndex(strTemp,'.',itemUnit.getCstpl_Grade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getCstpl_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getCstpl_Grade() ;
            itemUnit.setCstpl_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getCstpl_Grade(), strTemp)) ;
        }
        return cstplItemShowListPara;
    }

    /*�����ֶ�Start*/
    public CstplItemService getCstplItemService() {
        return cstplItemService;
    }

    public void setCstplItemService(CstplItemService cstplItemService) {
        this.cstplItemService = cstplItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CstplInfoService getCstplInfoService() {
        return cstplInfoService;
    }

    public void setCstplInfoService(CstplInfoService cstplInfoService) {
        this.cstplInfoService = cstplInfoService;
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

    public CstplItemShow getCstplItemShowAdd() {
        return cstplItemShowAdd;
    }

    public void setCstplItemShowAdd(CstplItemShow cstplItemShowAdd) {
        this.cstplItemShowAdd = cstplItemShowAdd;
    }

    public CstplItemShow getCstplItemShowDel() {
        return cstplItemShowDel;
    }

    public void setCstplItemShowDel(CstplItemShow cstplItemShowDel) {
        this.cstplItemShowDel = cstplItemShowDel;
    }

    public CstplItemShow getCstplItemShowUpd() {
        return cstplItemShowUpd;
    }

    public void setCstplItemShowUpd(CstplItemShow cstplItemShowUpd) {
        this.cstplItemShowUpd = cstplItemShowUpd;
    }

    public CstplItemShow getCstplItemShowSel() {
        return cstplItemShowSel;
    }

    public void setCstplItemShowSel(CstplItemShow cstplItemShowSel) {
        this.cstplItemShowSel = cstplItemShowSel;
    }

    public TkcttItemService getTkcttItemService() {
        return tkcttItemService;
    }

    public void setTkcttItemService(TkcttItemService tkcttItemService) {
        this.tkcttItemService = tkcttItemService;
    }
/*�����ֶ�End*/
}