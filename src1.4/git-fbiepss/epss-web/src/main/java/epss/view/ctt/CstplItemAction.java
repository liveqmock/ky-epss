package epss.view.ctt;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */
import epss.repository.model.CttItem;
import epss.repository.model.model_show.AttachmentModel;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import skyline.util.JxlsManager;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.common.enums.*;
import epss.repository.model.CttInfo;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.model_show.CstplItemShow;
import epss.repository.model.model_show.CttItemShow;
import epss.service.*;
import epss.service.CttInfoService;
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
public class CstplItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplItemAction.class);
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;

    private CttInfo cttInfo;
    private CttItemShow cttItemShowSel;
    private CttItemShow cttItemShowAdd;
    private CttItemShow cttItemShowUpd;
    private CttItemShow cttItemShowDel;
    /*�б���ʾ��*/
    private List<CstplItemShow> cstplItemShowList;

    /*��������*/
    private String strBelongToType;
    /*������*/
    private String strCttInfoPkid;

    /*�ύ����*/
    private String strSubmitType;
    private String strPassVisible;
    private String strPassFailVisible;
    private String strNotPassToStatus;
    private String strFlowType;
    /*���ƿؼ��ڻ����ϵĿ�������ʵStart*/
    //��ʾ�Ŀ���
    private StyleModel styleModel;
    /*���ƿؼ��ڻ����ϵĿ�������ʵEnd*/
    private List<CstplItemShow> cstplItemShowListExcel;
    private Map beansMap;

    //����
    private List<AttachmentModel> attachmentList;
    private HtmlGraphicImage image;
    //�ϴ������ļ�
    private StreamedContent downloadFile;
    // ¼�뱸ע
    private String strFlowStatusRemark;
    @PostConstruct
    public void init() {
        try {
            this.attachmentList=new ArrayList<>();
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            beansMap = new HashMap();
            strBelongToType= EnumResType.RES_TYPE1.getCode();
            if (parammap.containsKey("strCttInfoPkid")) {
                strCttInfoPkid = parammap.get("strCttInfoPkid").toString();
                cttInfo = cttInfoService.getCttInfoByPkId(strCttInfoPkid);
            }
            if (parammap.containsKey("strFlowType")) {
                strFlowType = parammap.get("strFlowType").toString();
            }

            strPassVisible = "true";
            strPassFailVisible = "true";
            if ("Mng".equals(strFlowType)) {
                if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(cttInfo.getFlowStatus())){
                    strPassVisible = "false";
                }else {
                    strPassFailVisible = "false";
                }
            }else {
                if (("Check".equals(strFlowType)&&EnumFlowStatus.FLOW_STATUS1.getCode().equals(cttInfo.getFlowStatus()))
                        ||("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(cttInfo.getFlowStatus()))
                        ||("Approve".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS3.getCode().equals(cttInfo.getFlowStatus()))){
                    strPassVisible = "false";
                }
            }

            resetAction();
            initData() ;
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }
    private boolean checkPreMng(CttInfo cttInfoPara) {
        if (StringUtils.isEmpty(cttInfoPara.getId())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getName())) {
            return false;
        } else if (StringUtils.isEmpty(cttInfoPara.getSignDate())) {
            return false;
        }
        return true;
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
                    if (!strCttInfoPkid.equals("")) {
                        cttInfo=cttInfoService.getCttInfoByPkId(strCttInfoPkid);
                    }
                    if(!checkPreMng(cttInfo)){
                        MessageUtil.addError("��ͬ��Ϣδά���������޷�¼����ɣ�");
                        return ;
                    }
                    // ״̬��־����ʼ
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ��¼�����
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    cttInfoService.updateRecord(cttInfo);
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cttInfo.setFlowStatus(null);
                    cttInfo.setFlowStatusReason(null);
                    cttInfoService.updateRecord(cttInfo);
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    cttInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
                    cttInfoService.updateRecord(cttInfo);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    cttInfo.setFlowStatus(null);
                    // ԭ�����δ��
                    cttInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
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
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
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
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
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
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
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
                    cttInfo.setFlowStatusRemark(strFlowStatusRemark);
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
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void resetAction(){
        strSubmitType="Add";
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        cttItemShowSel =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowAdd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowUpd =new CttItemShow(strBelongToType ,strCttInfoPkid);
        cttItemShowDel =new CttItemShow(strBelongToType ,strCttInfoPkid);
    }

    public Boolean blurStrName(){
        if(!ToolUtil.getStrIgnoreNull(cttItemShowSel.getName()).equals("")){
            String intIndex= esCommon.getIndexOfCstplItemNamelist(cttItemShowUpd.getName());
            if(!intIndex.equals("-1")){
                cttItemShowSel.setUnit(null);
                cttItemShowSel.setContractUnitPrice(null);
                cttItemShowSel.setContractQuantity(null);
                cttItemShowSel.setStrCorrespondingItemNo(null);
                cttItemShowSel.setCorrespondingPkid(null);
            }
        }
        return true;
    }

    public void selectRecordAction(String strSubmitTypePara,
                                   CstplItemShow cstplItemShowPara) {
        try {
            String  strSubmitTypeBefore= strSubmitType;
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Sel")){
                if(cstplItemShowPara.getBelongToTypeContrast()==null) {
                    cttItemShowSel =new CttItemShow(strBelongToType ,strCttInfoPkid);
                }else{
                    cttItemShowSel =getItemOfEsItemHieRelapByItem(cstplItemShowPara,"Cstpl");
                }
                cttItemShowSel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrNo())) ;
                cttItemShowSel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowSel.getStrCorrespondingItemNo()));
            }
            if(strSubmitTypePara.equals("Upd")){
                cttItemShowUpd = getItemOfEsItemHieRelapByItem(cstplItemShowPara, "Cstpl");
                cttItemShowUpd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrNo()));
                cttItemShowUpd.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowUpd.getStrCorrespondingItemNo()));
            }
            else if(strSubmitTypePara.equals("Del")){
                cttItemShowDel =getItemOfEsItemHieRelapByItem(cstplItemShowPara,"Cstpl");
                cttItemShowDel.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrNo())) ;
                cttItemShowDel.setStrCorrespondingItemNo(
                        ToolUtil.getIgnoreSpaceOfStr(cttItemShowDel.getStrCorrespondingItemNo()));
            }
            else if(strSubmitTypePara.equals("From_tkctt_to_cstpl")){
                if(fromTkcttToCstplAction(cstplItemShowPara)){
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
        CttItemShow cttItemShowTemp =new CttItemShow();
        if (strSubmitType.equals("Add")){
            cttItemShowTemp = cttItemShowAdd;
        }
        if (strSubmitType.equals("Upd")){
            cttItemShowTemp = cttItemShowUpd;
        }
        CstplItemShow cstplItemShowTemp =
                getItemOfTkcttAndCstplByStrNo(cttItemShowTemp.getStrCorrespondingItemNo(), cstplItemShowList, "Tkctt");
        if(cstplItemShowTemp !=null && cstplItemShowTemp.getStrNo() !=null){
            if (strSubmitType.equals("Add")) {
                String strNo = cttItemShowTemp.getStrNo();
                if (strNo != null && !strNo.equals("")) {
                    CstplItemShow cstplTempForCstplItemShow = getItemOfTkcttAndCstplByStrNo(strNo, cstplItemShowList, "Cstpl");
                    if (cstplTempForCstplItemShow != null) {
                        String correspondingItemNoContrast = ToolUtil.getIgnoreSpaceOfStr(cstplTempForCstplItemShow.getCorrespondingItemNoContrast());
                        if (cttItemShowTemp.getStrCorrespondingItemNo() != null &&
                                !correspondingItemNoContrast.equals(cttItemShowTemp.getStrCorrespondingItemNo())) {
                            MessageUtil.addError("�ñ���(" + strNo + ")����Ӧ����(" + correspondingItemNoContrast
                                    + ")�Ѿ�����,������Ķ�Ӧ���루" + cttItemShowTemp.getStrCorrespondingItemNo() +
                                    ")ִ�в������ʱ������߼����󣬽��޷����룡");
                            return strNoBlurFalse();
                        }
                    }
                }
            }
            cttItemShowTemp.setCorrespondingPkid(cstplItemShowTemp.getPkid());
        }

        return true ;
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
        return true;
    }
    public void submitThisRecordAction(){
        try{
            /*�ύǰ�ļ��*/
            if (strSubmitType.equals("Del")) {
                cttItemService.setAfterThisOrderidSubOneByNode(cttItemShowDel);
            } else {
                if (!subMitActionPreCheck()) {
                    return;
                }
            /*������֤*/
                if (!blurStrNoToGradeAndOrderid("false")) {
                    return;
                }
            /*��Ӧ������֤*/
                if (!blurCorrespondingPkid()) {
                    return;
                }
                if (strSubmitType.equals("Upd")) {
                    if("".equals(cttItemShowUpd.getStrCorrespondingItemNo())){
                        cttItemShowUpd.setCorrespondingPkid("");
                    }
                    cttItemService.updateRecord(cttItemShowUpd);
                } else if (strSubmitType.equals("Add")) {
                    CttItem cttItemTemp = cttItemService.fromModelShowToModel(cttItemShowAdd);
                    if (cttItemService.isExistSameRecordInDb(cttItemTemp)) {
                        MessageUtil.addInfo("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�롣");
                        return;
                    }
                    cttItemService.setAfterThisOrderidPlusOneByNode(cttItemShowAdd);
                    resetAction();
                }
            }
            switch (strSubmitType){
                case "Add" : MessageUtil.addInfo("����������ɡ�");
                    break;
                case "Upd" : MessageUtil.addInfo("����������ɡ�");
                    break;
                case "Del" : MessageUtil.addInfo("ɾ��������ɡ�");
            }
            initData();
        } catch (Exception e) {
            switch (strSubmitType){
                case "Add" : MessageUtil.addError("��������ʧ�ܣ�"+ e.getMessage());
                    break;
                case "Upd" : MessageUtil.addError("��������ʧ�ܣ�"+ e.getMessage());
                    break;
                case "Del" : MessageUtil.addError("ɾ������ʧ�ܣ�"+ e.getMessage());
            }
        }
    }

    private void initData() {
        try {
            /*�ܰ���ͬ�б�*/
            List<CttItem> cttItemListTkctt =new ArrayList<>();
            if(ToolUtil.getStrIgnoreNull(strCttInfoPkid).length()!=0) {
                // ������¼���List
                attachmentList=ToolUtil.getListAttachmentByStrAttachment(cttInfo.getAttachment());

                beansMap.put("cttInfo", cttInfo);
                String strTkcttPkidInCstpl = cttInfo.getParentPkid();
                cttItemListTkctt =
                        cttItemService.getEsItemList(EnumResType.RES_TYPE0.getCode(), strTkcttPkidInCstpl);
                List<CttItemShow> cttItemShowListTkctt = new ArrayList<>();
                recursiveDataTable("root", cttItemListTkctt, cttItemShowListTkctt);
                cttItemShowListTkctt = getCstplItemList_DoFromatNo(cttItemShowListTkctt);

            /*�ɱ��ƻ��б�*/
                List<CttItem> cttItemListCstpl = cttItemService.getEsItemList(
                        strBelongToType, strCttInfoPkid);

                List<CttItemShow> cttItemShowListCstpl = new ArrayList<>();
                recursiveDataTable("root", cttItemListCstpl, cttItemShowListCstpl);
                cttItemShowListCstpl = getCstplItemList_DoFromatNo(
                        cttItemShowListCstpl);

            /*ƴװ�б�*/
                List<CstplItemShow> cstplItemShowList_ForSort = new ArrayList<>();

                for (CttItemShow itemTkctt : cttItemShowListTkctt) {
                    Boolean insertedFlag = false;
                    for (CttItemShow itemCstpl : cttItemShowListCstpl) {
                        CstplItemShow itemTkcttInsertItem = getCstplItem(itemTkctt, "Tkctt");
                        if (itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())) {
                            //�ܰ���ͬ
                            if (insertedFlag.equals(true)) {
                                itemTkcttInsertItem.setPkid(null);
                                itemTkcttInsertItem.setStrNo(null);
                                itemTkcttInsertItem.setName(null);
                                itemTkcttInsertItem.setRemark(null);
                                itemTkcttInsertItem.setUnit(null);
                                itemTkcttInsertItem.setContractUnitPrice(null);
                                itemTkcttInsertItem.setContractQuantity(null);
                                itemTkcttInsertItem.setContractAmount(null);
                                itemTkcttInsertItem.setBelongToType(null);
                                itemTkcttInsertItem.setBelongToPkid(null);
                                itemTkcttInsertItem.setParentPkid(null);
                                itemTkcttInsertItem.setGrade(null);
                                itemTkcttInsertItem.setOrderid(null);
                                itemTkcttInsertItem.setSignPartAPrice(null);
                                itemTkcttInsertItem.setArchivedFlag(null);
                                itemTkcttInsertItem.setOriginFlag(null);
                                itemTkcttInsertItem.setCreatedBy(null);
                                itemTkcttInsertItem.setCreatedTime(null);
                                itemTkcttInsertItem.setLastUpdBy(null);
                                itemTkcttInsertItem.setLastUpdTime(null);
                                itemTkcttInsertItem.setRecVersion(null);
                                itemTkcttInsertItem.setCorrespondingPkid(null);
                            } else {
                                // �ܰ���ͬ�еĵ��ۣ����������
                                itemTkcttInsertItem.setContractUnitPrice(itemTkcttInsertItem.getContractUnitPrice());
                                itemTkcttInsertItem.setContractQuantity(itemTkcttInsertItem.getContractQuantity());
                                itemTkcttInsertItem.setContractAmount(itemTkcttInsertItem.getContractAmount());
                            }
                            //�ɱ��ƻ�
                            itemTkcttInsertItem.setStrNoContrast(itemCstpl.getStrNo());
                            itemTkcttInsertItem.setPkidContrast(itemCstpl.getPkid());
                            itemTkcttInsertItem.setBelongToTypeContrast(itemCstpl.getBelongToType());
                            itemTkcttInsertItem.setBelongToPkidContrast(itemCstpl.getBelongToPkid());
                            itemTkcttInsertItem.setParentPkidContrast(itemCstpl.getParentPkid());
                            itemTkcttInsertItem.setGradeContrast(itemCstpl.getGrade());
                            itemTkcttInsertItem.setOrderidContrast(itemCstpl.getOrderid());
                            itemTkcttInsertItem.setNameContrast(itemCstpl.getName());
                            itemTkcttInsertItem.setRemarkContrast(itemCstpl.getRemark());
                            itemTkcttInsertItem.setUnitContrast(itemCstpl.getUnit());
                            // �ɱ��ƻ��еĵ��ۣ����������׹��ĵ���
                            itemTkcttInsertItem.setContractUnitPriceContrast(itemCstpl.getContractUnitPrice());
                            itemTkcttInsertItem.setContractQuantityContrast(itemCstpl.getContractQuantity());
                            itemTkcttInsertItem.setContractAmountContrast(itemCstpl.getContractAmount());
                            itemTkcttInsertItem.setSignPartAPriceContrast(itemCstpl.getSignPartAPrice());
                            itemTkcttInsertItem.setArchivedFlagContrast(itemCstpl.getArchivedFlag());
                            itemTkcttInsertItem.setOriginFlagContrast(itemCstpl.getOriginFlag());
                            itemTkcttInsertItem.setCreatedByContrast(itemCstpl.getCreatedBy());
                            itemTkcttInsertItem.setCreatedTimeContrast(itemCstpl.getCreatedTime());
                            itemTkcttInsertItem.setLastUpdByContrast(itemCstpl.getLastUpdBy());
                            itemTkcttInsertItem.setLastUpdTimeContrast(itemCstpl.getLastUpdTime());
                            itemTkcttInsertItem.setRecVersionContrast(itemCstpl.getRecVersion());
                            itemTkcttInsertItem.setCorrespondingPkidContrast(itemCstpl.getCorrespondingPkid());
                            if (itemTkcttInsertItem.getPkid() == null || itemTkcttInsertItem.getPkid().equals("")) {
                                itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() + "");
                            }
                            insertedFlag = true;
                            cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                        }
                    }
                    if (insertedFlag.equals(false)) {
                        CstplItemShow itemTkcttInsertItem = getCstplItem(itemTkctt, "Tkctt");
                        cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                    }
                }

                for (CttItemShow itemCstpl : cttItemShowListCstpl) {
                    Boolean insertedFlag = false;
                    for (CttItemShow itemTkctt : cttItemShowListTkctt) {
                        if (itemTkctt.getPkid().equals(itemCstpl.getCorrespondingPkid())) {
                            insertedFlag = true;
                            break;
                        }
                    }
                    if (insertedFlag.equals(false)) {
                        CstplItemShow itemTkcttInsertItem = getCstplItem(itemCstpl, "Cstpl");
                        if (itemTkcttInsertItem.getPkid() == null || itemTkcttInsertItem.getPkid().equals("")) {
                            itemTkcttInsertItem.setPkid(cstplItemShowList_ForSort.size() + "");
                        }
                        cstplItemShowList_ForSort.add(itemTkcttInsertItem);
                    }
                }

                for (CstplItemShow itemUnit : cstplItemShowList_ForSort) {
                    if (itemUnit.getStrNoContrast() != null) {
                        String correspondingItemNoContrast =
                                itemUnit.getCorrespondingPkidContrast() == null ? "" : itemUnit.getCorrespondingPkidContrast();
                        CstplItemShow cstplItemShowTemp =
                                getItemOfTkcttAndCstplByPkid(correspondingItemNoContrast, cstplItemShowList_ForSort, "Tkctt");
                        if (cstplItemShowTemp != null) {
                            itemUnit.setCorrespondingItemNoContrast(cstplItemShowTemp.getStrNo());
                        }
                    } else {
                        itemUnit.setCorrespondingItemNoContrast(itemUnit.getStrNo());
                    }
                }
                cstplItemShowList = new ArrayList<>();
                cstplItemShowList.addAll(cstplItemShowList_ForSort);
                // ��Ӻϼ�
                setCstplItemList_AddTotal();
                // Excel�����γ�
                cstplItemShowListExcel = new ArrayList<>();
                for (CstplItemShow itemUnit : cstplItemShowList) {
                    // �ܰ���ͬ�еĵ��ۣ����������
                    itemUnit.setContractUnitPrice(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractUnitPrice()));
                    itemUnit.setContractQuantity(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractQuantity()));
                    itemUnit.setContractAmount(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractAmount()));

                    // �ɱ��ƻ��е��ۺϵ��ۣ������������׹��ĵ���
                    itemUnit.setContractUnitPriceContrast(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractUnitPriceContrast()));
                    itemUnit.setContractQuantityContrast(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractQuantityContrast()));
                    itemUnit.setContractAmountContrast(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getContractAmountContrast()));
                    itemUnit.setSignPartAPriceContrast(
                            ToolUtil.getBdFrom0ToNull(itemUnit.getSignPartAPriceContrast()));

                    CstplItemShow itemUnitTemp = (CstplItemShow) BeanUtils.cloneBean(itemUnit);
                    itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                    itemUnitTemp.setStrNoContrast(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNoContrast()));
                    cstplItemShowListExcel.add(itemUnitTemp);
                }
                beansMap.put("cstplItemShowListExcel", cstplItemShowListExcel);
                resetAction();
            }
        }catch (Exception e) {
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }
    private void setCstplItemList_AddTotal(){
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
    private boolean fromTkcttToCstplAction(CstplItemShow cstplItemShowPara){
        try{
            if(cstplItemShowPara.getStrNo() ==null||
                    cstplItemShowPara.getStrNo().equals("")) {
                MessageUtil.addInfo("�޿ɸ��ƣ�");
                return false;
            }
            if(!ToolUtil.getStrIgnoreNull(cstplItemShowPara.getStrNoContrast()).equals("")) {
                MessageUtil.addInfo("�ɱ��ƻ��Ϊ�գ��ܰ���ͬ���ɱ��ƻ������޷����ƣ����踴�ƣ�����ɾ���óɱ��ƻ��");
                return false;
            }
            String strIgnoreSpaceOfStr=ToolUtil.getIgnoreSpaceOfStr(cstplItemShowPara.getStrNo());
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
            cttItemShowAdd = getItemOfEsItemHieRelapByItem(cstplItemShowPara,"Tkctt");
            cttItemShowAdd.setStrNo(ToolUtil.getIgnoreSpaceOfStr(cttItemShowAdd.getStrNo())) ;
            cttItemShowAdd.setBelongToType(strBelongToType) ;
            cttItemShowAdd.setBelongToPkid(strCttInfoPkid) ;
            Integer intLastIndexof=strIgnoreSpaceOfStr.lastIndexOf(".");
            if(intLastIndexof>0){
                String strParentNo=strIgnoreSpaceOfStr.substring(0,intLastIndexof);
                CstplItemShow cstplItemShowTemp =new CstplItemShow();
                cstplItemShowTemp =
                        getItemOfTkcttAndCstplByStrNo(strParentNo, cstplItemShowList,"Cstpl");
                cttItemShowAdd.setParentPkid(cstplItemShowTemp.getPkid());
                cttItemShowAdd.setGrade(cstplItemShowTemp.getGrade() + 1) ;
            }else{
                cttItemShowAdd.setParentPkid("root");
            }

            cttItemShowAdd.setCorrespondingPkid(cttItemShowAdd.getPkid()) ;
            cttItemShowAdd.setStrCorrespondingItemNo(cttItemShowAdd.getStrNo()) ;
            strSubmitType="Add";
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
            int startI=i;                  //���浱ǰԪ�صĳ�ʼλ��i,��Ϊ���ڲ�ѭ���л�ı�i��ֵ
            int countParent=0;             //ͳ�ƺ͵�ǰԪ�أ�����Ԫ�أ���ͬ��Ԫ�ظ�������Ԫ��ֻ����˳����ͬ
            int countChild=0;              //ͳ��ƥ�丸Ԫ�صĺ���Ԫ�صĸ���������Ԫ��Ҳ��˳�����
            int positionOfChild=0;         //��¼�º͸�Ԫ��ƥ��ĵ�һ�����ӵ�λ��
            strThisCorrespondingItemNoContrast=ToolUtil.getIgnoreSpaceOfStr(modelList.get(i).getCorrespondingItemNoContrast());
            for(int j=i+1; j<modelList.size();j++){
                strThisCorrespondingItemNoContrastAfter=ToolUtil.getIgnoreSpaceOfStr(modelList.get(j).getCorrespondingItemNoContrast());
                if(strThisCorrespondingItemNoContrast.equals(strThisCorrespondingItemNoContrastAfter)){
                //�ж��ڲ�ѭ�����Ƿ��к����ѭ���е�Ԫ����ͬ��Ԫ�أ�����¼����������countParent
                    if(countParent==0){     //����ڲ�ѭ���д��ں��������ͬ��Ԫ�أ������ǵ�һ������������countParent��ֵ����Ϊ0��
                        countParent+=2;     //���״�����ʱ�ڲ�����Ҫ��countParent+2
                        continue;
                    }
                    ++countParent;
                }
                else{                       //else�·�Ϊ�������
                    if(countParent==0||!strThisCorrespondingItemNoContrastAfter.startsWith(strThisCorrespondingItemNoContrast))
                        break;
                        //1.�ڲ���û�к������Ԫ����ͬ��Ԫ�أ�����֪������list֪���������ֱ���˳��ڲ�ѭ�����ɣ�
                        // ||2.�ڲ����к������Ԫ����ͬ��Ԫ�أ������ڲ���û���亢�ӣ�����֪������list֪���������ֱ���˳��ڲ�ѭ������
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
                                    List<CttItem> cttItemListPara,
                                    List<CttItemShow> cttItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // ͨ������id�������ĺ���
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            CttItemShow cttItemShowTemp = null;
            String strCreatedByName= cttInfoService.getUserName(itemUnit.getCreatedBy());
            String strLastUpdByName= cttInfoService.getUserName(itemUnit.getLastUpdBy());
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
                    "",
                    itemUnit.getSpareField()
            );
            cttItemShowListPara.add(cttItemShowTemp) ;
            recursiveDataTable(cttItemShowTemp.getPkid(), cttItemListPara, cttItemShowListPara);
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
    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<CstplItemShow> getItemOfTkcttAndCstplByLevelParentPkid(
            String strLevelParentPkid,
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
    private CstplItemShow getItemOfTkcttAndCstplByStrNo(
            String strNo,
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
    private CstplItemShow getItemOfTkcttAndCstplByPkid(
            String strPkid,
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
            cttItemShowTemp.setRemark(cstplItemShowPara.getRemark()) ;

            cttItemShowTemp.setUnit(cstplItemShowPara.getUnit());
            cttItemShowTemp.setContractUnitPrice(cstplItemShowPara.getContractUnitPrice()) ;
            cttItemShowTemp.setContractQuantity(cstplItemShowPara.getContractQuantity()) ;
            cttItemShowTemp.setContractAmount(cstplItemShowPara.getContractAmount()) ;
            cttItemShowTemp.setSignPartAPrice(cstplItemShowPara.getSignPartAPrice()) ;
            cttItemShowTemp.setArchivedFlag(cstplItemShowPara.getArchivedFlag());
            cttItemShowTemp.setOriginFlag(cstplItemShowPara.getOriginFlag()) ;
            cttItemShowTemp.setCreatedBy(cstplItemShowPara.getCreatedBy());
            cttItemShowTemp.setCreatedTime(cstplItemShowPara.getCreatedTime());
            cttItemShowTemp.setLastUpdBy(cstplItemShowPara.getLastUpdBy());
            cttItemShowTemp.setLastUpdTime(cstplItemShowPara.getLastUpdTime());
            cttItemShowTemp.setRecVersion(cstplItemShowPara.getRecVersion());
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
            cttItemShowTemp.setRemark(cstplItemShowPara.getRemarkContrast()) ;

            cttItemShowTemp.setUnit(cstplItemShowPara.getUnitContrast());
            cttItemShowTemp.setContractUnitPrice(cstplItemShowPara.getContractUnitPriceContrast()) ;
            cttItemShowTemp.setContractQuantity(cstplItemShowPara.getContractQuantityContrast()) ;
            cttItemShowTemp.setContractAmount(cstplItemShowPara.getContractAmountContrast()) ;
            cttItemShowTemp.setSignPartAPrice(cstplItemShowPara.getSignPartAPriceContrast()) ;
            cttItemShowTemp.setArchivedFlag(cstplItemShowPara.getArchivedFlagContrast());
            cttItemShowTemp.setOriginFlag(cstplItemShowPara.getOriginFlagContrast()) ;
            cttItemShowTemp.setCreatedBy(cstplItemShowPara.getCreatedByContrast());
            cttItemShowTemp.setCreatedTime(cstplItemShowPara.getCreatedTimeContrast());
            cttItemShowTemp.setLastUpdBy(cstplItemShowPara.getLastUpdByContrast());
            cttItemShowTemp.setLastUpdTime(cstplItemShowPara.getLastUpdTimeContrast());
            cttItemShowTemp.setRecVersion(cstplItemShowPara.getRecVersionContrast());

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
    private CstplItemShow getCstplItem(
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
            cstplItemShowTemp.setRemark(cttItemShowPara.getRemark()) ;
            cstplItemShowTemp.setCorrespondingPkid(cttItemShowPara.getCorrespondingPkid());

            cstplItemShowTemp.setUnit(cttItemShowPara.getUnit());
            cstplItemShowTemp.setContractUnitPrice(cttItemShowPara.getContractUnitPrice()) ;
            cstplItemShowTemp.setContractQuantity(cttItemShowPara.getContractQuantity()) ;
            cstplItemShowTemp.setContractAmount(cttItemShowPara.getContractAmount()) ;
            cstplItemShowTemp.setSignPartAPrice(cttItemShowPara.getSignPartAPrice()) ;
            cstplItemShowTemp.setArchivedFlag(cttItemShowPara.getArchivedFlag());
            cstplItemShowTemp.setOriginFlag(cttItemShowPara.getOriginFlag()) ;
            cstplItemShowTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
            cstplItemShowTemp.setCreatedTime(cttItemShowPara.getCreatedTime());
            cstplItemShowTemp.setLastUpdBy(cttItemShowPara.getLastUpdBy());
            cstplItemShowTemp.setLastUpdTime(cttItemShowPara.getLastUpdTime());
            cstplItemShowTemp.setRecVersion(cttItemShowPara.getRecVersion());
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
            cstplItemShowTemp.setRemarkContrast(cttItemShowPara.getRemark()) ;
            cstplItemShowTemp.setCorrespondingPkidContrast(cttItemShowPara.getCorrespondingPkid());

            cstplItemShowTemp.setUnitContrast(cttItemShowPara.getUnit());
            cstplItemShowTemp.setContractUnitPriceContrast(cttItemShowPara.getContractUnitPrice()) ;
            cstplItemShowTemp.setContractQuantityContrast(cttItemShowPara.getContractQuantity()) ;
            cstplItemShowTemp.setContractAmountContrast(cttItemShowPara.getContractAmount()) ;
            cstplItemShowTemp.setSignPartAPriceContrast(cttItemShowPara.getSignPartAPrice()) ;
            cstplItemShowTemp.setArchivedFlagContrast(cttItemShowPara.getArchivedFlag());
            cstplItemShowTemp.setOriginFlagContrast(cttItemShowPara.getOriginFlag()) ;
            cstplItemShowTemp.setCreatedByContrast(cttItemShowPara.getCreatedBy());
            cstplItemShowTemp.setCreatedTimeContrast(cttItemShowPara.getCreatedTime());
            cstplItemShowTemp.setLastUpdByContrast(cttItemShowPara.getLastUpdBy());
            cstplItemShowTemp.setLastUpdTimeContrast(cttItemShowPara.getLastUpdTime());
            cstplItemShowTemp.setRecVersionContrast(cttItemShowPara.getRecVersion());
        }
        return cstplItemShowTemp;
    }

    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<CttItemShow> getCstplItemList_DoFromatNo(
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
    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "�ɱ��ƻ�-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"oriCstpl.xls");
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
                downloadFile = new DefaultStreamedContent(stream, new MimetypesFileTypeMap().getContentType(file), new String(strAttachment.getBytes("gbk"),"iso8859-1"));
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
    public CttInfo getCttInfo() {
        return cttInfo;
    }

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

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public List<CstplItemShow> getCstplItemShowList() {
        return cstplItemShowList;
    }

    public void setCstplItemShowList(List<CstplItemShow> cstplItemShowList) {
        this.cstplItemShowList = cstplItemShowList;
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
    public StyleModel getStyleModel() {
        return styleModel;
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

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public String getStrFlowType() {
        return strFlowType;
    }

    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }

    public List<CstplItemShow> getCstplItemShowListExcel() {
        return cstplItemShowListExcel;
    }

    public void setCstplItemShowListExcel(List<CstplItemShow> cstplItemShowListExcel) {
        this.cstplItemShowListExcel = cstplItemShowListExcel;
    }

    public List<AttachmentModel> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentModel> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public HtmlGraphicImage getImage() {
        return image;
    }

    public void setImage(HtmlGraphicImage image) {
        this.image = image;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }
    /*�����ֶ�End*/

    public String getStrPassVisible() {
        return strPassVisible;
    }

    public String getStrPassFailVisible() {
        return strPassFailVisible;
    }

    public String getStrFlowStatusRemark() {
        return strFlowStatusRemark;
    }

    public void setStrFlowStatusRemark(String strFlowStatusRemark) {
        this.strFlowStatusRemark = strFlowStatusRemark;
    }
/*�����ֶ�End*/
}