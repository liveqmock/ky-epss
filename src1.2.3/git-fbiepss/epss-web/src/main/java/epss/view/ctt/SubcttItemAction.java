package epss.view.ctt;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.repository.model.model_show.AttachmentModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.common.enums.*;
import epss.repository.model.*;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class SubcttItemAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttItemAction.class);
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    /*�򿪵ĳɱ��ƻ�ҳ����*/
    private List<CttItemShow> cttItemShowList_Cstpl;
    private CttItemShow cttItemShowSelected_Cstpl;

    private CttInfo cttInfo;
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    private List<CttItem> cttItemList;
    /*�б���ѡ��һ��*/
    private CttItemShow cttItemShowSelected;
	/*�б���ʾ��*/
    private List<CttItemShow> cttItemShowList;

    /*��������*/
    private String strBelongToType;
    /*������*/
    private String strCttInfoPkid;

    /*�ύ����*/
    private String strSubmitType;
    private String strPassFlag;
    private String strFlowType;
    private String strNotPassToStatus;

    /*���ƿؼ��ڻ����ϵĿ�������ʵStart*/
    //��ʾ�Ŀ���
    private StyleModel styleModel;
    // �̶�������ʱ���ƿؼ��Ŀ���
    private StyleModel styleModelCttQty;
    private StyleModel styleModelCttAmount;
    /*���ƿؼ��ڻ����ϵĿ�������ʵEnd*/
    private Map beansMap;
    private List<CttItemShow> cttItemShowListExcel;

    //����
    private CttInfoShow cttInfoShowAttachment;
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //�ϴ������ļ�
    private StreamedContent downloadFile;

    @PostConstruct
    public void init() {
        this.attachmentList=new ArrayList<>();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        beansMap = new HashMap();
        strBelongToType= EnumResType.RES_TYPE2.getCode();
        if (parammap.containsKey("strCttInfoPkid")) {
            strCttInfoPkid = parammap.get("strCttInfoPkid").toString();
            cttInfo=cttInfoService.getCttInfoByPkId(strCttInfoPkid);
        }
        if (parammap.containsKey("strFlowType")) {
            strFlowType = parammap.get("strFlowType").toString();
        }
        strPassFlag="true";
        if("Mng".equals(strFlowType)&& EnumFlowStatus.FLOW_STATUS0.getCode().equals(cttInfo.getFlowStatus())) {
            strPassFlag="false";
        }
        resetAction();
        initData() ;
    }

    /*��ʼ������*/
    private void initData() {
        try{
            cttItemShowList_Cstpl =new ArrayList<>();
            if(ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
        /*�ְ���ͬ*/
                cttInfo = cttInfoService.getCttInfoByPkId(strCttInfoPkid);
                // ������¼���List
                attachmentList = ToolUtil.getListAttachmentByStrAttachment(cttInfo.getAttachment());
                beansMap.put("cttInfo", cttInfo);
        /*�ɱ��ƻ�*/
                String strCstplPkidInInitCtt = cttInfo.getParentPkid();
                cttItemList = cttItemService.getEsItemList(
                        EnumResType.RES_TYPE1.getCode(), strCstplPkidInInitCtt);
                recursiveDataTable("root", cttItemList, cttItemShowList_Cstpl);
                cttItemShowList_Cstpl = getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList_Cstpl);
        /*�ְ���ͬ*/
                cttItemList = new ArrayList<CttItem>();
                cttItemShowList = new ArrayList<CttItemShow>();
                cttItemList = cttItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);
                cttItemShowList.clear();
                recursiveDataTable("root", cttItemList, cttItemShowList);
                cttItemShowList = getItemOfEsItemHieRelapList_DoFromatNo(cttItemShowList);
        /*�ְ���ͬ��Ӧ�ɱ��ƻ��е���*/
                for (CttItemShow itemUnit : cttItemShowList) {
                    for (CttItemShow itemUnitCstpl : cttItemShowList_Cstpl) {
                        if (itemUnit.getCorrespondingPkid() != null &&
                                itemUnit.getCorrespondingPkid().equals(itemUnitCstpl.getPkid())) {
                            itemUnit.setStrCorrespondingItemNo(itemUnitCstpl.getStrNo());
                            itemUnit.setStrCorrespondingItemName(itemUnitCstpl.getName());
                        }
                    }
                }
                // ��Ӻϼ�
                setItemOfCstplAndSubcttList_AddTotal();
                cttItemShowListExcel = new ArrayList<CttItemShow>();
                for (CttItemShow itemUnit : cttItemShowList) {
                    CttItemShow itemUnitTemp = (CttItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    itemUnitTemp.setStrCorrespondingItemNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrCorrespondingItemNo()));
                    cttItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cttItemShowListExcel", cttItemShowListExcel);
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }
    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
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
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        styleModelCttQty=new StyleModel();
        styleModelCttQty.setDisabled_Flag("false");
        styleModelCttAmount=new StyleModel();
        styleModelCttAmount.setDisabled_Flag("true");
        cttItemShowSel =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strCttInfoPkid);
    }
    public void resetActionForAdd(){
        strSubmitType="Add";
        cttItemShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
    }
    /*�ҵ����¼�*/
    public void selectRecordAction(String strSubmitTypePara,CttItemShow cttItemShowSeledPara){
        try {
            strSubmitType=strSubmitTypePara;
            if(!strSubmitTypePara.equals("Add")){
                if(cttItemShowSeledPara.getStrNo()==null){
                    MessageUtil.addError("��ȷ��ѡ����У��ϼ��в��ɱ༭��");
                    return;
                }
            }
            if(strSubmitTypePara.equals("Sel")){
                cttItemShowSel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemShowUpd =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo())) ;
                cttItemShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrCorrespondingItemNo()));
                esCommon.getIndexOfSubcttItemNamelist(cttItemShowUpd.getName());
            }
            if(strSubmitTypePara.equals("Del")){
                cttItemShowDel =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("FromCstplToSubctt")){
                cttItemShowSelected =(CttItemShow) BeanUtils.cloneBean(cttItemShowSeledPara) ;
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
            }
        }
        return true;
    }
    public void blurCalculateAmountAction(){
        BigDecimal bigDecimal;
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
            List<CttItem> itemHieRelapListSubTemp=new ArrayList<>();
            itemHieRelapListSubTemp=getEsCttItemListByParentPkid("root", cttItemList);

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
            cttItemShowTemp1 =getEsCttItemByStrNo(
                    strParentNo,
                    cttItemShowList);
            if(cttItemShowTemp1 ==null|| cttItemShowTemp1.getPkid()==null){
                MessageUtil.addError("��ȷ������ı��룡����" + strParentNo + "�����ڣ�");
                return strNoBlurFalse();
            }
            else{
                List<CttItem> itemHieRelapListSubTemp=new ArrayList<>();
                itemHieRelapListSubTemp=getEsCttItemListByParentPkid(
                        cttItemShowTemp1.getPkid(),
                        cttItemList);
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
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
                getEsCttItemByStrNo(
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
                    resetActionForAdd();
                }else if(strSubmitType.equals("Upd")){
                    updRecordAction(cttItemShowUpd);
                }
            }
            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("�ύ������ɡ�");
                    break;
                case "Upd" : MessageUtil.addInfo("����������ɡ�");
                    break;
                case "Del" : MessageUtil.addInfo("ɾ��������ɡ�");
            }
            initData();
        }
        catch (Exception e){
            MessageUtil.addError("�ύ����ʧ�ܣ�" + e.getMessage());
        }
    }
    private void addRecordAction(CttItemShow cttItemShowPara){
        cttItemService.setAfterThisOrderidPlusOneByNode(
                cttItemShowPara.getBelongToType(),
                cttItemShowPara.getBelongToPkid(),
                cttItemShowPara.getParentPkid(),
                cttItemShowPara.getGrade(),
                cttItemShowPara.getOrderid());
        cttItemService.insertRecord(cttItemShowPara);
    }
    private void updRecordAction(CttItemShow cttItemShowPara){
        cttItemService.updateRecord(cttItemShowPara) ;
    }
    private int delRecordAction(CttItemShow cttItemShowPara) {
        int deleteRecordNum= cttItemService.deleteRecord(cttItemShowPara.getPkid()) ;
        cttItemService.setAfterThisOrderidSubOneByNode(
                cttItemShowPara.getBelongToType(),
                cttItemShowPara.getBelongToPkid(),
                cttItemShowPara.getParentPkid(),
                cttItemShowPara.getGrade(),
                cttItemShowPara.getOrderid());
        return deleteRecordNum;
    }

    /*�ύǰ�ļ�飺�����������*/
	private Boolean subMitActionPreCheck(){
        CttItemShow cttItemShowTemp =new CttItemShow(strBelongToType ,strCttInfoPkid);
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
            cttItemService.updateRecord(cttItemShowSelected) ;
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
    /*���ܰ���ͬ�б��и��ݱ���ҵ���*/
    private CttItemShow getEsCttItemByStrNo(
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
    /**
     * ����Ȩ�޽������
     *
     * @param strPowerTypePara
     */
    public void onClickForPowerAction(String strPowerTypePara) {
        try {
            strPowerTypePara=strFlowType+strPowerTypePara;
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    List<CttItem> cttItemList = cttItemService.getEsItemList(cttInfo.getCttType(), cttInfo.getPkid());
                    if (cttItemList.isEmpty()) {
                        MessageUtil.addInfo("����ϸ���ݣ�");
                        return;
                    }
                    int checkPriceZero = 0;
                    int checkQuantiyZero=0;
                    int checkSecurityZero=0;
                    for (CttItem cttItemTemp : cttItemList) {
                        //�׹��ġ���������ȫ��ʩ�ѷ���ǿ�ʱ����1���������0
                        if (!(cttItemTemp.getSignPartAPrice() == null)){
                            if (cttItemTemp.getSignPartAPrice().compareTo(new BigDecimal(0))!=0){
                                checkPriceZero=1;
                            }
                        }else{
                                checkPriceZero=0;
                        }
                        if (!(cttItemTemp.getContractQuantity() == null)){
                            if (cttItemTemp.getContractQuantity().compareTo(new BigDecimal(0))!=0){
                                checkQuantiyZero=1;
                            }
                        }else{
                            checkQuantiyZero=0;
                        }
                        if (("��ȫʩ����ʩ����").equals(cttItemTemp.getName())){
                            if (!(cttItemTemp.getContractAmount()==null)){
                                if (!(cttItemTemp.getContractAmount().compareTo(new BigDecimal(0))!=0){
                                    checkSecurityZero=1;
                                }
                            }
                        }else{
                            checkSecurityZero=0;
                        }
                    }
                    if (checkQuantiyZero==1&&checkPriceZero == 0&&checkSecurityZero==0) {
                        cttInfo.setType("0");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    } else if (checkQuantiyZero==0&&checkPriceZero == 1&&checkSecurityZero==0){
                        cttInfo.setType("1");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    } else if (checkQuantiyZero==0&&checkPriceZero == 0&&checkSecurityZero==1){
                        cttInfo.setType("2");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    }else if (checkQuantiyZero==1&&checkPriceZero == 1&&checkSecurityZero==0){
                        cttInfo.setType("3");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    }else if (checkQuantiyZero==1&&checkPriceZero == 0&&checkSecurityZero==1){
                        cttInfo.setType("4");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    }else if (checkQuantiyZero==0&&checkPriceZero == 1&&checkSecurityZero==1){
                        cttInfo.setType("5");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    }else if (checkQuantiyZero==1&&checkPriceZero == 1&&checkSecurityZero==1){
                        cttInfo.setType("6");
                        cttInfo.setPkid(cttInfo.getPkid());
                        cttInfoService.updateByPKid(cttInfo);
                    }
                    // ״̬��־����ʼ
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ��¼�����
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfo.setFlowStatus(null);
                    cttInfo.setFlowStatusReason(null);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    cttInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("�������δ����");
                }
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }
                    // ԭ�򣺸���δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("������׼ͨ����");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����Ƿ�ʹ��
                    String strCttTypeTemp = "";
                    if (cttInfo.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                        strCttTypeTemp = EnumResType.RES_TYPE1.getCode();
                    } else if (cttInfo.getCttType().equals(EnumResType.RES_TYPE1.getCode())) {
                        strCttTypeTemp = EnumResType.RES_TYPE2.getCode();
                    }

                    // ����д����ʵ��Խ���˻�
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        cttInfo.setFlowStatus(null);
                    }

                    // ԭ����׼δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                    cttInfoService.updateRecord(cttInfo);

                    List<ProgStlInfo> progStlInfoListTemp =
                            progStlInfoService.selectIsUsedInQMPBySubcttPkid(cttInfo.getPkid());
                    if (progStlInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("�������Ѿ���["
                                + EnumResType.getValueByKey(progStlInfoListTemp.get(0).getStlType()).getTitle()
                                + "]ʹ�ã�������׼δ��,�����ر༭��");
                    } else {
                        if (cttInfoService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cttInfo.getPkid()) > 0) {
                            MessageUtil.addInfo("�������Ѿ���[" + EnumResType.getValueByKey(strCttTypeTemp).getTitle()
                                    + "]ʹ�ã�������׼δ��,�����ر༭��");
                        } else {
                            MessageUtil.addInfo("������׼δ����");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "�ְ���ͬ-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"oriSubctt.xls");
    }

    //������ط���
    public void onViewAttachment(AttachmentModel attachmentModelPara) {
        image.setValue("/upload/" + attachmentModelPara.getCOLUMN_NAME());
    }

    public void delAttachmentRecordAction(AttachmentModel attachmentModelPara){
        try {
            File deletingFile = new File(attachmentModelPara.getCOLUMN_PATH());
            deletingFile.delete();
            attachmentList.remove(attachmentModelPara) ;
            StringBuffer sbTemp = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sbTemp.append(item.getCOLUMN_PATH() + ";");
            }

            cttInfo.setAttachment(sbTemp.toString());
            cttInfoService.updateRecord(cttInfo);
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void download(String strAttachment){
        try{
            if(StringUtils .isEmpty(strAttachment) ){
                MessageUtil.addError("·��Ϊ�գ��޷����أ�");
                logger.error("·��Ϊ�գ��޷����أ�");
            }
            else {
                String fileName=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload")+"/"+strAttachment;
                File file = new File(fileName);
                InputStream stream = new FileInputStream(fileName);
                downloadFile = new DefaultStreamedContent(
                        stream,
                        new MimetypesFileTypeMap().getContentType(file),
                        new String(strAttachment.getBytes("gbk"),"iso8859-1"));
            }
        } catch (Exception e) {
            logger.error("�����ļ�ʧ��", e);
            MessageUtil.addError("�����ļ�ʧ��,"+e.getMessage()+strAttachment);
        }
    }

    public void upload(FileUploadEvent event) {
        BufferedInputStream inStream = null;
        FileOutputStream fileOutputStream = null;
        UploadedFile uploadedFile = event.getFile();
        AttachmentModel attachmentModel = new AttachmentModel();
        if (uploadedFile != null) {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload");
            File superFile = new File(path);
            if (!superFile.exists()) {
                superFile.mkdirs();
            }
            File descFile = new File(superFile, uploadedFile.getFileName());
            attachmentModel.setCOLUMN_ID(ToolUtil.getIntIgnoreNull(attachmentList.size()) + "");
            attachmentModel.setCOLUMN_NAME(uploadedFile.getFileName());
            attachmentModel.setCOLUMN_PATH(descFile.getAbsolutePath());
            for (AttachmentModel item : attachmentList){
                if (item.getCOLUMN_NAME().equals(attachmentModel.getCOLUMN_NAME())) {
                    MessageUtil.addError("�����Ѵ��ڣ�");
                    return;
                }
            }

            attachmentList.add(attachmentModel);

            StringBuffer sb = new StringBuffer();
            for (AttachmentModel item : attachmentList) {
                sb.append(item.getCOLUMN_NAME() + ";");
            }
            if(sb.length()>4000){
                MessageUtil.addError("����·��("+sb.toString()+")�����ѳ����������ֵ4000��������⣬����ϵϵͳ����Ա��");
                return;
            }
            cttInfo.setAttachment(sb.toString());
            cttInfoService.updateRecord(cttInfo);
            try {
                inStream = new BufferedInputStream(uploadedFile.getInputstream());
                fileOutputStream = new FileOutputStream(descFile);
                byte[] buf = new byte[1024];
                int num;
                while ((num = inStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
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

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public String getStrPassFlag() {
        return strPassFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
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

    public String getStrFlowType() {
        return strFlowType;
    }

    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public CttInfo getCttInfo() {
        return cttInfo;
    }

    public void setCttInfo(CttInfo cttInfo) {
        this.cttInfo = cttInfo;
    }

    public List<CttItemShow> getCttItemShowListExcel() {
        return cttItemShowListExcel;
    }

    public void setCttItemShowListExcel(List<CttItemShow> cttItemShowListExcel) {
        this.cttItemShowListExcel = cttItemShowListExcel;
    }

    public CttInfoShow getCttInfoShowAttachment() {
        return cttInfoShowAttachment;
    }

    public void setCttInfoShowAttachment(CttInfoShow cttInfoShowAttachment) {
        this.cttInfoShowAttachment = cttInfoShowAttachment;
    }

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }
    /*�����ֶ�End*/
}