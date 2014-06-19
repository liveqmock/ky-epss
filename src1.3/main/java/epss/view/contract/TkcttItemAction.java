package epss.view.contract;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.TkcttInfo;
import epss.repository.model.TkcttItem;
import epss.repository.model.model_show.TkcttItemShow;
import epss.service.SignPartService;
import epss.service.TkcttInfoService;
import epss.service.TkcttItemService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class TkcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttItemAction.class);
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

    private TkcttItemShow tkcttItemShowSel;
    private TkcttItemShow tkcttItemShowAdd;
    private TkcttItemShow tkcttItemShowUpd;
    private TkcttItemShow tkcttItemShowDel;
    private List<TkcttItem> tkcttItemList;

    private TkcttItemShow tkcttItemShowCopy;
    private TkcttItemShow tkcttItemShowPaste;
    private List<TkcttItemShow> tkcttItemShowList;
    /*�б���ѡ��һ��*/
    private TkcttItemShow tkcttItemShowSelected;
    /*������*/
    private String strTkcttInfoPkid;

    /*�ύ����*/
    private String strSubmitType;
    private String strPasteType;

    /*���ƿؼ��ڻ����ϵĿ�������ʵStart*/
    private StyleModel styleModelNo;
    private StyleModel styleModel;
    //��ʾ�Ŀ���
    private String strPasteBtnRendered;
    private String strMngNotFinishFlag;
    /*���ƿؼ��ڻ����ϵĿ�������ʵEnd*/

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        strTkcttInfoPkid=parammap.get("strTkcttInfoPkid").toString();
        TkcttInfo tkcttInfoTemp =
                tkcttInfoService.getTkcttInfoByPkid(strTkcttInfoPkid);
        strMngNotFinishFlag="true";
        if(EnumFlowStatus.FLOW_STATUS0.getCode().equals(tkcttInfoTemp.getFlowStatus())) {
            strMngNotFinishFlag="false";
        }
        resetAction();
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        /*�γɹ�ϵ��*/
        tkcttItemList =new ArrayList<TkcttItem>();
        tkcttItemShowList =new ArrayList<TkcttItemShow>();
        tkcttItemList = tkcttItemService.getTkcttItemListByTkcttInfoPkid(strTkcttInfoPkid);
        recursiveDataTable("root", tkcttItemList);
        tkcttItemShowList =getItemOfEsItemHieRelapList_DoFromatNo(tkcttItemShowList);
        setItemOfEsItemHieRelapList_AddTotal();
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,List<TkcttItem> tkcttItemListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<TkcttItem> subTkcttItemList =new ArrayList<TkcttItem>();
        // ͨ������id�������ĺ���
        subTkcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, tkcttItemListPara);
        for(TkcttItem itemUnit: subTkcttItemList){
            TkcttItemShow tkcttItemShowTemp = null;
            String strCreatedByName= esCommon.getOperNameByOperId(itemUnit.getCreatedBy());
            String strLastUpdByName= esCommon.getOperNameByOperId(itemUnit.getUpdatedBy());
            // �㼶��
            tkcttItemShowTemp = new TkcttItemShow(
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
                strLastUpdByName,
                itemUnit.getUpdatedTime(),
                itemUnit.getRecVersion(),
                itemUnit.getRemark(),
                itemUnit.getTid()
            );
            tkcttItemShowList.add(tkcttItemShowTemp) ;
            recursiveDataTable(tkcttItemShowTemp.getPkid(), tkcttItemListPara);
        }
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<TkcttItemShow> getItemOfEsItemHieRelapList_DoFromatNo(
            List<TkcttItemShow> tkcttItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(TkcttItemShow itemUnit: tkcttItemShowListPara){
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
        return tkcttItemShowListPara;
    }
    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<TkcttItemShow> tkcttItemShowListTemp =new ArrayList<TkcttItemShow>();
        tkcttItemShowListTemp.addAll(tkcttItemShowList);

        tkcttItemShowList.clear();
        // С��
        BigDecimal bdTotal=new BigDecimal(0);
        BigDecimal bdAllTotal=new BigDecimal(0);
        TkcttItemShow itemUnit=new TkcttItemShow();
        TkcttItemShow itemUnitNext=new TkcttItemShow();
        for(int i=0;i< tkcttItemShowListTemp.size();i++){
            itemUnit = tkcttItemShowListTemp.get(i);
            bdTotal=bdTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmt()));
            bdAllTotal=bdAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getAmt()));
            tkcttItemShowList.add(itemUnit);

            if(i+1< tkcttItemShowListTemp.size()){
                itemUnitNext = tkcttItemShowListTemp.get(i+1);
                if(itemUnitNext.getParentPkid().equals("root")){
                    TkcttItemShow tkcttItemShowTemp =new TkcttItemShow();
                    tkcttItemShowTemp.setName("�ϼ�");
                    tkcttItemShowTemp.setPkid("total"+i);
                    tkcttItemShowTemp.setAmt(bdTotal);
                    tkcttItemShowList.add(tkcttItemShowTemp);
                    bdTotal=new BigDecimal(0);
                }
            } else if(i+1== tkcttItemShowListTemp.size()){
                itemUnitNext = tkcttItemShowListTemp.get(i);
                TkcttItemShow tkcttItemShowTemp =new TkcttItemShow();
                tkcttItemShowTemp.setName("�ϼ�");
                tkcttItemShowTemp.setPkid("total"+i);
                tkcttItemShowTemp.setAmt(bdTotal);
                tkcttItemShowList.add(tkcttItemShowTemp);
                bdTotal=new BigDecimal(0);

                // �ܺϼ�
                tkcttItemShowTemp =new TkcttItemShow();
                tkcttItemShowTemp.setName("�ܺϼ�");
                tkcttItemShowTemp.setPkid("total_all"+i);
                tkcttItemShowTemp.setAmt(bdAllTotal);
                tkcttItemShowList.add(tkcttItemShowTemp);
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
        strPasteBtnRendered="false";
        strPasteType="";
        tkcttItemShowSel =new TkcttItemShow(strTkcttInfoPkid);
        tkcttItemShowAdd =new TkcttItemShow(strTkcttInfoPkid);
        tkcttItemShowUpd =new TkcttItemShow(strTkcttInfoPkid);
        tkcttItemShowDel =new TkcttItemShow(strTkcttInfoPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        tkcttItemShowAdd =new TkcttItemShow(strTkcttInfoPkid);
    }

    /*�ύǰ�ļ�飺�����������*/
    private Boolean subMitActionPreCheck(){
        TkcttItemShow tkcttItemShowTemp =new TkcttItemShow(strTkcttInfoPkid);
        if (strSubmitType.equals("Add")){
            tkcttItemShowTemp = tkcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            tkcttItemShowTemp = tkcttItemShowUpd;
        }
        if (StringUtils.isEmpty(tkcttItemShowTemp.getStrNo())) {
            MessageUtil.addError("�������ţ�");
            return false;
        }
        if (StringUtils.isEmpty(tkcttItemShowTemp.getName())) {
            MessageUtil.addError("���������ƣ�");
            return false;
        }
        return true;
    }

    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        if (strSubmitType.equals("Add")){
            if(tkcttItemShowAdd.getUnitPrice()==null|| tkcttItemShowAdd.getQty()==null){
                bigDecimal=null;
            }else{
                bigDecimal = tkcttItemShowAdd.getUnitPrice().multiply(tkcttItemShowAdd.getQty());
            }
            tkcttItemShowAdd.setAmt(bigDecimal);
        }
        if (strSubmitType.equals("Upd")){
            if(tkcttItemShowUpd.getUnitPrice()==null|| tkcttItemShowUpd.getQty()==null){
                bigDecimal=null;
            }else{
                bigDecimal = tkcttItemShowUpd.getUnitPrice().multiply(tkcttItemShowUpd.getQty());
            }
            tkcttItemShowUpd.setAmt(bigDecimal);
        }
    }

    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara){
        try {
            if(strSubmitTypePara.equals("Add")) {
                return;
            }
            if(tkcttItemShowSelected.getStrNo()==null){
                MessageUtil.addError("��ȷ��ѡ����У��ϼ��в��ɱ༭��");
                return;
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                tkcttItemShowSel = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected);
                tkcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowSel.getStrNo())) ;
            }
            if(strSubmitTypePara.equals("Upd")){
                strPasteBtnRendered="false";
                tkcttItemShowUpd =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowUpd.getStrNo())) ;
                tkcttItemShowCopy =null;
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Del")){
                strPasteBtnRendered="false";
                tkcttItemShowDel =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowDel.getStrNo())) ;
                tkcttItemShowCopy =null;
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Copy")){
                strPasteBtnRendered="true";
                tkcttItemShowCopy =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowSel.getStrNo())) ;
                strPasteType="Copy";
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.equals("Cut")){
                strPasteBtnRendered="true";
                tkcttItemShowCopy =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowSel.getStrNo())) ;
                strPasteType="Cut";
                tkcttItemShowPaste =null;
            }else if(strSubmitTypePara.contains("Paste")){
                strPasteBtnRendered="false";
                tkcttItemShowPaste = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowSelected) ;
                tkcttItemShowSel =new TkcttItemShow(strTkcttInfoPkid);
                pasteAction();
            }
        } catch (Exception e) {
            logger.error("ѡ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*ɾ��*/
    public void delThisRecordAction(){
        try {
            int deleteRecordNum= tkcttItemService.deleteRecord(tkcttItemShowDel.getPkid()) ;
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
            tkcttItemService.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                    tkcttItemShowDel.getTkcttInfoPkid(),
                    tkcttItemShowDel.getParentPkid(),
                    tkcttItemShowDel.getGrade());
            MessageUtil.addInfo("ɾ��������ɡ�");
            initData();
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public Boolean blurStrNoToGradeAndOrderidAction(){
        TkcttItemShow tkcttItemShowTemp =new TkcttItemShow(strTkcttInfoPkid);
        if (strSubmitType.equals("Add")){
            tkcttItemShowTemp = tkcttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            tkcttItemShowTemp = tkcttItemShowUpd;
        }
        String strIgnoreSpaceOfStr= ToolUtil.getIgnoreSpaceOfStr(tkcttItemShowTemp.getStrNo());
        if(StringUtils .isEmpty(strIgnoreSpaceOfStr)){
            return true;
        }
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (!strIgnoreSpaceOfStr.matches(strRegex) ){
            MessageUtil.addError("��ȷ������ı��룬����"+strIgnoreSpaceOfStr+"��ʽ����ȷ��");
            return strNoBlurFalse();
        }

        //�ñ����Ѿ�����
        if(!strSubmitType.equals("Upd")){
            if(getItemOfEsItemHieRelapByStrNo(strIgnoreSpaceOfStr, tkcttItemShowList)!=null){
            }
        }
        Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");

        if(intLastIndexof <0){
            List<TkcttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid("root", tkcttItemList);

            if(itemHieRelapListSubTemp .size() ==0){
                if(!strIgnoreSpaceOfStr.equals("1") ){
                    MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����1��");
                    return strNoBlurFalse();
                }
            }
            else{
                if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strIgnoreSpaceOfStr)){
                    MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����"+(itemHieRelapListSubTemp.size() +1)+"��");
                    return strNoBlurFalse();
                }
            }
            tkcttItemShowTemp.setGrade(1) ;
            tkcttItemShowTemp.setOrderid(Integer.parseInt(strIgnoreSpaceOfStr));
            tkcttItemShowTemp.setParentPkid("root");
        }else{
            String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
            TkcttItemShow tkcttItemShowTemp1 =new TkcttItemShow();
            tkcttItemShowTemp1 =getItemOfEsItemHieRelapByStrNo(strParentNo, tkcttItemShowList);
            if(tkcttItemShowTemp1 ==null|| tkcttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("��ȷ������ı��룡����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            }
            else{
                List<TkcttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsItemHieRelapListByLevelParentPkid(
                        tkcttItemShowTemp1.getPkid(),
                        tkcttItemList);
                if(itemHieRelapListSubTemp .size() ==0){
                    if(!tkcttItemShowTemp.getStrNo().equals(strParentNo+".1") ){
                        MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����"+strParentNo+".1��");
                        return strNoBlurFalse();
                    }
                }
                else{
                    String strOrderid=strIgnoreSpaceOfStr.substring(intLastIndexof+1);
                    if(itemHieRelapListSubTemp.size() +1<Integer.parseInt(strOrderid)){
                        MessageUtil.addError("��ȷ������ı��룡�ñ��벻���Ϲ淶��Ӧ����"+strParentNo+"."+
                                (itemHieRelapListSubTemp.size() +1)+"��");
                        return strNoBlurFalse();
                    }
                }
                String strTemps[]= strIgnoreSpaceOfStr.split("\\.");
                tkcttItemShowTemp.setGrade(strTemps.length) ;
                tkcttItemShowTemp.setOrderid(Integer.parseInt(strTemps[strTemps.length -1]));
                tkcttItemShowTemp.setParentPkid(tkcttItemShowTemp1.getPkid()) ;
            }
        }
        return true ;
    }
    public void submitThisRecordAction(){
        try{
            /*�ύǰ�ļ��*/
            if(strSubmitType .equals("Del")) {
                delThisRecordAction();
            }else{
                if(!subMitActionPreCheck()){
                    return ;
                }
                /*itemUnitConstruct��grade,orderid,parentpkid*/
                if(!blurStrNoToGradeAndOrderidAction()){
                    return ;
                }
                if(strSubmitType .equals("Upd")) {
                    tkcttItemService.updateRecord(tkcttItemShowUpd) ;
                }
                else if(strSubmitType .equals("Add")) {
                    TkcttItem cttItemTemp = tkcttItemService.fromShowModelToModel(tkcttItemShowAdd);
                    if (tkcttItemService.isExistSameRecordInDb(cttItemTemp)){
                        MessageUtil.addInfo("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�롣");
                        return;
                    }
                    tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                            tkcttItemShowAdd.getTkcttInfoPkid(),
                            tkcttItemShowAdd.getParentPkid(),
                            tkcttItemShowAdd.getGrade(),
                            tkcttItemShowAdd.getOrderid());
                    tkcttItemService.insertRecord(tkcttItemShowAdd);
                }
                MessageUtil.addInfo("�ύ������ɡ�");
                initData();
            }
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�"+e.getMessage() );
        }
    }

    /*ճ��*/
    private void pasteAction(){
        try{
            TkcttItemShow tkcttItemShowCopyTemp =new TkcttItemShow() ;
            if(strPasteType.equals("Copy")){
                /*���ƵĶ���*/
                tkcttItemShowCopyTemp = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowCopy);
                /*���Ƶ�Ŀ��λ��*/
                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                if(strSubmitType .equals("Paste_brother_up")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid();
                }else if(strSubmitType.equals("Paste_brother_down")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid()+1;
                }else if(strSubmitType.equals("Paste_children")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= tkcttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                                    tkcttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                tkcttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                tkcttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*���¸��Ƶ�Ŀ��λ���Ժ������*/
                tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                        tkcttItemShowCopyTemp.getParentPkid(),
                        tkcttItemShowCopyTemp.getGrade(),
                        tkcttItemShowCopyTemp.getOrderid());
                /*�ڸ��Ƶ�Ŀ��λ�ô����븴�ƵĶ���esItemHieRelapTemp�е�Pkid������ɲ�ͬ����ǰ����*/
                tkcttItemService.insertRecord(tkcttItemShowCopyTemp);

                /*����ǰ��㼶�仯����*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= tkcttItemShowPaste.getGrade() - tkcttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (tkcttItemShowPaste.getGrade()+1) - tkcttItemShowCopy.getGrade() ;
                }

                /*�������ƶ�����ӽڵ�����Start*/
                 /*��¼�²���ĵ�ǰ������ΪPkid�����Զ������Ѿ��仯��*/
                TkcttItemShow tkcttItemShowTemp =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowCopy);
                /*�����ܰ���ͬ�б�������*/
                List<TkcttItemShow> tkcttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTempΪ�ź�����ܰ���ͬ��Ϣ�б�*/
                tkcttItemShowListTemp.addAll(tkcttItemShowList);
                /*��¼֮ǰ�ڵ���*/
                TkcttItemShow tkcttItemShowBefore =new TkcttItemShow();
                Boolean isBegin=false;
                for(TkcttItemShow itemUnit: tkcttItemShowListTemp){
                    TkcttItemShow tkcttItemShowNewInsert =(TkcttItemShow) BeanUtils.cloneBean(itemUnit);
                     /*�ҵ����ж����Ѱ�Ҹö������ӽڵ㣨�㼶�Ŵ��ڸö���Ĳ㼶�ŵ����ݣ������и���*/
                    if(isBegin.equals(true) &&
                            ToolUtil.getIntIgnoreNull(tkcttItemShowNewInsert.getGrade())<= tkcttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        /*ͬ��������*/
                        if(tkcttItemShowNewInsert.getGrade().equals(tkcttItemShowBefore.getGrade())){
                            /*���ñ������ݵĸ��׽ڵ��Ϊ�ղŲ������ݵĸ��׽ڵ��*/
                            tkcttItemShowNewInsert.setParentPkid(tkcttItemShowTemp.getParentPkid());
                        }
                        /*�Ӳ��������*/
                        else{
                            /*���ñ������ݵĸ��׽ڵ��Ϊ�ղŲ������ݵĽڵ��*/
                            tkcttItemShowNewInsert.setParentPkid(tkcttItemShowTemp.getPkid());
                        }
                        /*���ñ������ݵĲ㼶��Ϊԭ�ȵĲ㼶�żӱ任ǰ��Ĳ㼶��*/
                        tkcttItemShowNewInsert.setGrade(tkcttItemShowNewInsert.getGrade()+intGradeGap);
                        /*����������*/
                        tkcttItemService.insertRecord(tkcttItemShowNewInsert);
                        /*��¼�²���ĵ�ǰ������ΪPkid�����Զ������Ѿ��仯��*/
                        tkcttItemShowTemp =(TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowNewInsert);
                        /*��¼��ǰ������*/
                        tkcttItemShowBefore =itemUnit;
                    }
                    /*�ҵ����ƶ���*/
                    if(tkcttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }
                /*�������ƶ�����ӽڵ�����End*/
            }
            else if (strPasteType.equals("Cut")){
                /*ճ���Ĳ���*/
                /*�޸�Ŀ��ڵ�ĸ��ӹ�ϵ���㼶�ź����*/
                tkcttItemShowCopyTemp = (TkcttItemShow) BeanUtils.cloneBean(tkcttItemShowCopy);

                Integer intCutPasteActionGrade=0;
                Integer intCutPasteActionOrderid=0;
                if(strSubmitType .equals("Paste_brother_up")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid();
                }else if(strSubmitType.equals("Paste_brother_down")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getParentPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade();
                    intCutPasteActionOrderid= tkcttItemShowPaste.getOrderid()+1;
                }else if(strSubmitType.equals("Paste_children")){
                    tkcttItemShowCopyTemp.setParentPkid(tkcttItemShowPaste.getPkid());
                    intCutPasteActionGrade= tkcttItemShowPaste.getGrade()+1;
                    intCutPasteActionOrderid= tkcttItemService.
                            getMaxOrderidInEsItemHieRelapList(
                                    tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                                    tkcttItemShowCopyTemp.getParentPkid(),
                                    intCutPasteActionGrade)+1;
                }

                tkcttItemShowCopyTemp.setGrade(intCutPasteActionGrade);
                tkcttItemShowCopyTemp.setOrderid(intCutPasteActionOrderid);

                /*1���¼��е�Ŀ��λ���Ժ������*/
                tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                        tkcttItemShowCopyTemp.getParentPkid(),
                        tkcttItemShowCopyTemp.getGrade(),
                        tkcttItemShowCopyTemp.getOrderid());
                /*2�޸ı������ݵ�ParentPkid��Grade,Orderid*/
                tkcttItemService.updateRecord(tkcttItemShowCopyTemp) ;
                /*3�޸��ӽڵ�Ĳ㼶��ϵ*/
                /*����ǰ��㼶�仯����*/
                Integer intGradeGap=null;
                if(strSubmitType .equals("Paste_brother_up")||strSubmitType.equals("Paste_brother_down")){
                    intGradeGap= tkcttItemShowPaste.getGrade() - tkcttItemShowCopy.getGrade() ;
                }else if(strSubmitType .equals("Paste_children")){
                    intGradeGap= (tkcttItemShowPaste.getGrade()+1) - tkcttItemShowCopy.getGrade() ;
                }

                /*�����ܰ���ͬ�б�������*/
                List<TkcttItemShow> tkcttItemShowListTemp =new ArrayList<>();
                /*itemOfEsItemHieRelapListTempΪ�ź�����ܰ���ͬ��Ϣ�б�*/
                tkcttItemShowListTemp.addAll(tkcttItemShowList);
                Boolean isBegin=false;
                for(TkcttItemShow itemUnit: tkcttItemShowListTemp){
                    /*�ҵ����ж����Ѱ�Ҹö������ӽڵ㣨�㼶�Ŵ��ڸö���Ĳ㼶�ŵ����ݣ������и���*/
                    if(isBegin.equals(true) && itemUnit.getGrade()<= tkcttItemShowCopy.getGrade()){
                        break;
                    }
                    else if(isBegin.equals(true)){
                        itemUnit.setGrade(itemUnit.getGrade()+intGradeGap);
                        tkcttItemService.updateRecord(itemUnit) ;
                    }
                    /*�ҵ����ж���*/
                    if(tkcttItemShowCopy.equals(itemUnit)) {
                        isBegin=true;
                    }
                }

                /*���еĲ���*/
                tkcttItemService.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                        tkcttItemShowCopyTemp.getTkcttInfoPkid(),
                        tkcttItemShowCopyTemp.getParentPkid(),
                        tkcttItemShowCopyTemp.getGrade(),
                        tkcttItemShowCopyTemp.getOrderid());
            }
            MessageUtil.addInfo("ճ���ɹ�!");
            initData();
        }
        catch (Exception e){
            logger.error("ճ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
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
    /*�㼶��ϵ�б���ͨ��Pkid�ĸ���*/
    private TkcttItemShow getItemOfEsItemHieRelapByPkid(String strPkid,
              List<TkcttItemShow> tkcttItemShowListPara){
        TkcttItemShow tkcttItemShowTemp =new TkcttItemShow();
        try{
            for(TkcttItemShow itemUnit: tkcttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getPkid()).equals(strPkid)) {
                    tkcttItemShowTemp =(TkcttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return tkcttItemShowTemp;
    }
    /*���ܰ���ͬ�б��и��ݱ���ҵ���*/
    private TkcttItemShow getItemOfEsItemHieRelapByStrNo(
             String strNo,
             List<TkcttItemShow> tkcttItemShowListPara){
        TkcttItemShow tkcttItemShowTemp =null;
        try{
            for(TkcttItemShow itemUnit: tkcttItemShowListPara){
                if(ToolUtil.getIgnoreSpaceOfStr(itemUnit.getStrNo()).equals(strNo)) {
                    tkcttItemShowTemp =(TkcttItemShow)BeanUtils.cloneBean(itemUnit);
                    break;
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
        return tkcttItemShowTemp;
    }
    private Boolean strNoBlurFalse(){
        tkcttItemShowSel.setPkid("") ;
        tkcttItemShowSel.setParentPkid("");
        tkcttItemShowSel.setGrade(null);
        tkcttItemShowSel.setOrderid(null);
        return false;
    }

    /*�����ֶ�Start*/
    public TkcttItemService getTkcttItemService() {
        return tkcttItemService;
    }
    public void setTkcttItemService(TkcttItemService tkcttItemService) {
        this.tkcttItemService = tkcttItemService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }
    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public TkcttItemShow getTkcttItemShowSel() {
        return tkcttItemShowSel;
    }

    public void setTkcttItemShowSel(TkcttItemShow tkcttItemShowSel) {
        this.tkcttItemShowSel = tkcttItemShowSel;
    }

    public TkcttItemShow getTkcttItemShowSelected() {
        return tkcttItemShowSelected;
    }
    public void setTkcttItemShowSelected(TkcttItemShow tkcttItemShowSelected) {
        this.tkcttItemShowSelected = tkcttItemShowSelected;
    }
    public List<TkcttItemShow> getTkcttItemShowList() {
        return tkcttItemShowList;
    }
    public void setTkcttItemShowList(List<TkcttItemShow> tkcttItemShowList) {
        this.tkcttItemShowList = tkcttItemShowList;
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
    public String getStrPasteBtnRendered() {
        return strPasteBtnRendered;
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

    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
    }

    public TkcttItemShow getTkcttItemShowAdd() {
        return tkcttItemShowAdd;
    }

    public void setTkcttItemShowAdd(TkcttItemShow tkcttItemShowAdd) {
        this.tkcttItemShowAdd = tkcttItemShowAdd;
    }

    public TkcttItemShow getTkcttItemShowDel() {
        return tkcttItemShowDel;
    }

    public void setTkcttItemShowDel(TkcttItemShow tkcttItemShowDel) {
        this.tkcttItemShowDel = tkcttItemShowDel;
    }

    public TkcttItemShow getTkcttItemShowUpd() {
        return tkcttItemShowUpd;
    }

    public void setTkcttItemShowUpd(TkcttItemShow tkcttItemShowUpd) {
        this.tkcttItemShowUpd = tkcttItemShowUpd;
    }
    /*�����ֶ�End*/
}