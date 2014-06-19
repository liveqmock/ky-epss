package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.SubcttInfo;
import epss.repository.model.model_show.ProgMatqtyInfoShow;
import epss.repository.model.model_show.ProgSubstlInfoShow;
import epss.repository.model.model_show.ProgWorkqtyInfoShow;
import epss.repository.model.model_show.SubcttInfoShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.service.*;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgSubstlInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgSubstlInfoAction.class);
    @ManagedProperty(value = "#{progSubstlInfoService}")
    private ProgSubstlInfoService progSubstlInfoService;
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;

    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgSubstlInfoShow progSubstlInfoShow;
    private List<ProgSubstlInfoShow> progSubstlInfoShowList;
    private ProgSubstlInfoShow progSubstlInfoShowSelected;

    private ProgSubstlInfoShow progSubstlInfoShowNotForm;
    private List<ProgSubstlInfoShow> progSubstlInfoShowNotFormList;
    private ProgSubstlInfoShow progSubstlInfoShowNotFormSelected;
    private ProgSubstlInfoShow progSubstlInfoShowSel;
    private ProgSubstlInfoShow progSubstlInfoShowAdd;
    private ProgSubstlInfoShow progSubstlInfoShowUpd;
    private ProgSubstlInfoShow progSubstlInfoShowDel;

    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String strRowSelectedFlag;
    private String strApprovedFlag;
    private String strCstplPkid;

    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        progSubstlInfoShowList = new ArrayList<ProgSubstlInfoShow>();
        progSubstlInfoShowNotFormList = new ArrayList<ProgSubstlInfoShow>();
        strCstplPkid="";
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strCstplPkid")){
            strCstplPkid=parammap.get("strCstplPkid").toString();
        }
        resetAction();

        SubcttInfoShow subcttInfoTemp=new SubcttInfoShow();
        subcttInfoTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        List<SubcttInfo> subcttInfoList =
                subcttInfoService.getSubcttInfoListByModel(subcttInfoTemp);
        subcttList=new ArrayList<SelectItem>();
        if(subcttInfoList.size()>0){
            SelectItem selectItem=new SelectItem("","ȫ��");
            subcttList.add(selectItem);
            for(SubcttInfo itemUnit: subcttInfoList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progSubstlInfoShow =new ProgSubstlInfoShow();
        progSubstlInfoShowNotForm =new ProgSubstlInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        strApprovedFlag="false";
        strRowSelectedFlag="false";
    }
    public void resetActionForAdd(){
        progSubstlInfoShowAdd =new ProgSubstlInfoShow();
        strSubmitType="Add";
        strRowSelectedFlag="false";
    }

    private String getMaxIdPlusOne(){
        try {
            Integer intTemp;
            String strMaxTkStlId= progSubstlInfoService.getStrMaxProgSubstlInfoId();
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxTkStlId))){
                strMaxTkStlId="STLP"+ esCommon.getStrToday()+"001";
            }
            else{
                if(strMaxTkStlId .length()>3){
                    String strTemp=strMaxTkStlId.substring(strMaxTkStlId .length() -3).replaceFirst("^0+","");
                    if(ToolUtil.strIsDigit(strTemp)){
                        intTemp=Integer.parseInt(strTemp) ;
                        intTemp=intTemp+1;
                        strMaxTkStlId=strMaxTkStlId.substring(0,strMaxTkStlId.length()-3)+StringUtils.leftPad(intTemp.toString(),3,"0");
                    }else{
                        strMaxTkStlId+="001";
                    }
                }
            }
            return strMaxTkStlId ;
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
            return "";
        }
    }

    public void onQueryFormPreAction(String strQryFlag) {
        try {
            progSubstlInfoShowNotFormList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgSubstlInfoShow> progSubstlInfoShowListTemp =new ArrayList<>();
                List<ProgSubstlInfoShow> notFormProgSubstlInfoShowListTemp=
                    progSubstlInfoService.getNotFormProgSubstlInfoShowList(
                            strCstplPkid,
                            ToolUtil.getStrIgnoreNull(progSubstlInfoShowNotForm.getSubcttInfoPkid()),
                            ToolUtil.getStrIgnoreNull(progSubstlInfoShowNotForm.getStageNo()));
                List<ProgSubstlInfoShow> formPreProgSubstlInfoShowListTemp=
                    progSubstlInfoService.getFormPreProgSubstlInfoShowList(
                            strCstplPkid,
                            ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getSubcttInfoPkid()),
                            ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getStageNo()));
                for(ProgSubstlInfoShow itemUnitAll:notFormProgSubstlInfoShowListTemp) {
                    Boolean isHasSame=false;
                    for(ProgSubstlInfoShow itemUnitForming:formPreProgSubstlInfoShowListTemp){
                        if(itemUnitAll.getPkid().equals(itemUnitForming.getPkid())){
                            isHasSame=true;
                            break;
                        }
                    }
                    if(isHasSame.equals(false)){
                        if(itemUnitAll.getSubstlType().equals(ESEnum.ITEMTYPE3.getCode())) {
                        itemUnitAll.setId("�ְ���������("+itemUnitAll.getId() +")");
                        }else if(itemUnitAll.getSubstlType().equals(ESEnum.ITEMTYPE3.getCode())) {
                            itemUnitAll.setId("�ְ���������("+itemUnitAll.getId() +")");
                        }
                        progSubstlInfoShowListTemp.add(itemUnitAll);
                    }
                }

                ProgSubstlInfoShow progSubstlInfoShow1Temp;
                for(int i=0;i< progSubstlInfoShowListTemp.size();i++){
                    if(i==0){
                        progSubstlInfoShow1Temp =new ProgSubstlInfoShow();
                        progSubstlInfoShow1Temp.setPkid(i+"");
                        progSubstlInfoShow1Temp.setId("�ְ����㵥");
                        progSubstlInfoShow1Temp.setSignPartNameB(progSubstlInfoShowListTemp.get(i).getSignPartNameB());
                        progSubstlInfoShow1Temp.setStageNo(progSubstlInfoShowListTemp.get(i).getStageNo());
                        progSubstlInfoShowNotFormList.add(progSubstlInfoShow1Temp);
                        progSubstlInfoShowNotFormList.add(progSubstlInfoShowListTemp.get(0));
                    }else{
                        if(progSubstlInfoShowListTemp.get(i-1).getSubcttInfoPkid().equals(
                                progSubstlInfoShowListTemp.get(i).getSubcttInfoPkid())&&
                           progSubstlInfoShowListTemp.get(i-1).getStageNo().equals(
                                progSubstlInfoShowListTemp.get(i).getStageNo())
                        ){
                            progSubstlInfoShowNotFormList.add(progSubstlInfoShowListTemp.get(i));
                        }else{
                            progSubstlInfoShow1Temp =new ProgSubstlInfoShow();
                            progSubstlInfoShow1Temp.setPkid(i+"");
                            progSubstlInfoShow1Temp.setId("�ְ����㵥");
                            progSubstlInfoShow1Temp.setSignPartNameB(progSubstlInfoShowListTemp.get(i).getSignPartNameB());
                            progSubstlInfoShow1Temp.setStageNo(progSubstlInfoShowListTemp.get(i).getStageNo());
                            progSubstlInfoShowNotFormList.add(progSubstlInfoShow1Temp);
                            progSubstlInfoShowNotFormList.add(progSubstlInfoShowListTemp.get(i));
                        }
                    }
                }
            }else if(strQryFlag.equals("Qry")||strQryFlag.equals("Print")){

            }
            if (progSubstlInfoShowNotFormList.isEmpty()) {
                MessageUtil.addWarn("û�в�ѯ�����ݡ�");
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }
    public void onQueryFormedAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progSubstlInfoShowList.clear();
            if(strQryFlag.equals("Approve")){
                // �ְ���������ͷְ����Ͻ��㶼������
                List<ProgSubstlInfoShow> progSubstlInfoShowFormingListTemp =
                        progSubstlInfoService.getFormingProgSubstlInfoShowList(
                                    strCstplPkid,
                                    ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getSubcttInfoPkid()),
                                    ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getStageNo()));
                List<ProgSubstlInfoShow> progSubstlInfoShowApprovedListTemp =
                        progSubstlInfoService.getFormedProgSubstlInfoShowList(
                                strCstplPkid,
                                ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getSubcttInfoPkid()),
                                ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getStageNo()));
                ProgSubstlInfoShow progSubstlInfoShowTemp =new ProgSubstlInfoShow();
                for(int i=0;i< progSubstlInfoShowFormingListTemp.size();i++){
                    progSubstlInfoShowTemp = progSubstlInfoShowFormingListTemp.get(i);
                    Boolean isHasSame=false;
                    for(int j=0;j< progSubstlInfoShowApprovedListTemp.size();j++){
                        if(progSubstlInfoShowTemp.getSubcttInfoPkid().equals(progSubstlInfoShowApprovedListTemp.get(j).getSubcttInfoPkid())
                         && progSubstlInfoShowTemp.getStageNo().equals(progSubstlInfoShowApprovedListTemp.get(j).getStageNo())){
                            isHasSame=true;
                            break;
                        }
                    }
                    if(isHasSame.equals(false)){
                        progSubstlInfoShowList.add(progSubstlInfoShowTemp);
                    }
                }
                // �Ѿ����ɽ��㵥����׼�˵�
                for(int i=0;i< progSubstlInfoShowApprovedListTemp.size();i++){
                    progSubstlInfoShowList.add(progSubstlInfoShowApprovedListTemp.get(i));
                }
            }
            else if(strQryFlag.equals("Qry")||strQryFlag.equals("Print")){
                progSubstlInfoShowList =
                        progSubstlInfoService.getFormedProgSubstlInfoShowList(
                                strCstplPkid,
                                ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getSubcttInfoPkid()),
                                ToolUtil.getStrIgnoreNull(progSubstlInfoShow.getStageNo()));
            }
            if(strQryMsgOutPara.equals("true")){
                if (progSubstlInfoShowList.isEmpty()){
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(String strPowerTypePara,String strSubmitTypePara){
        try {
            strSubmitType=strSubmitTypePara;
            strRowSelectedFlag="true";
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progSubstlInfoShowSelected.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInFlowStatuslist(progSubstlInfoShowSelected.getFlowStatus());
            if(strPowerTypePara.equals("Approve")){
                if(strStatusFlagCode.equals(EnumFlowStatus.FLOW_STATUS4.getCode())){
                    MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"���������ٽ��б༭������");
                    return;
                }
                progSubstlInfoShow =(ProgSubstlInfoShow) BeanUtils.cloneBean(progSubstlInfoShowSelected);
                //�༭���棬����ֻ����
                styleModel.setDisabled_Flag("true");
                if(EnumFlowStatus.FLOW_STATUS2.getCode().equals(progSubstlInfoShow.getFlowStatus())){
                    strApprovedFlag="true";
                }else{
                    strApprovedFlag="false";
                }
            }else if(strPowerTypePara.equals("Print")){
                if(strStatusFlagCode.equals("")){
                    MessageUtil.addInfo("�������ݻ�δ��׼������ʱ���ܽ��д�ӡ������");
                    return;
                }
                //�༭���棬����ֻ����
                styleModel.setDisabled_Flag("true");
                progSubstlInfoShow =(ProgSubstlInfoShow) BeanUtils.cloneBean(progSubstlInfoShowSelected);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����Ȩ�޽������
     * @param strPowerType
     */
    @Transactional
    public void onClickForPowerAction(String strPowerType){
        try {
            if(progSubstlInfoShow !=null&& progSubstlInfoShow.getPkid()!=null){
                // ��׼
                if(strPowerType.contains("Approve")){
                    if(strPowerType.equals("ApprovePass")){
                        // ״̬��־����׼
                        progSubstlInfoShow.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                        // ԭ����׼ͨ��
                        progSubstlInfoShow.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                        // �۸����
                        if(!ESEnum.ITEMTYPE5.getCode().equals(progSubstlInfoShow.getSubstlType())){
                            progSubstlInfoShow.setSubcttInfoPkid(progSubstlInfoShow.getSubcttInfoPkid());
                            progSubstlInfoShow.setId(getMaxIdPlusOne());
                            // ����ǼǱ����
                            progSubstlInfoService.insertRecord(progSubstlInfoShow);
                        }
                    }else if(strPowerType.equals("ApproveFailToQ")){
                        // ����ǼǱ����
                        progSubstlInfoService.deleteRecord(progSubstlInfoShow);
                        // ����д����ʵ��Խ���˻�
                        progSubstlInfoShow.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                        progSubstlInfoShow.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());

                        ProgWorkqtyInfoShow progWorkqtyInfoShowTemp=new ProgWorkqtyInfoShow();
                        progWorkqtyInfoShowTemp.setSubcttInfoPkid(progSubstlInfoShow.getSubcttInfoPkid());
                        progWorkqtyInfoShowTemp.setStageNo(progSubstlInfoShow.getStageNo());
                        progWorkqtyInfoShowTemp.setFlowStatus(progSubstlInfoShow.getFlowStatus());
                        progWorkqtyInfoShowTemp.setFlowStatusRemark(progSubstlInfoShow.getFlowStatusRemark());
                        progWorkqtyInfoService.updateRecordByProgSubstlInfo(progWorkqtyInfoShowTemp);
                    }
                    else if(strPowerType.equals("ApproveFailToM")){
                        // ����ǼǱ����
                        progSubstlInfoService.deleteRecord(progSubstlInfoShow);
                        // ����д����ʵ��Խ���˻�
                        progSubstlInfoShow.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                        progSubstlInfoShow.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());

                        ProgMatqtyInfoShow progMatqtyInfoShowTemp=new ProgMatqtyInfoShow();
                        progMatqtyInfoShowTemp.setSubcttInfoPkid(progSubstlInfoShow.getSubcttInfoPkid());
                        progMatqtyInfoShowTemp.setStageNo(progSubstlInfoShow.getStageNo());
                        progMatqtyInfoShowTemp.setFlowStatus(progSubstlInfoShow.getFlowStatus());
                        progMatqtyInfoShowTemp.setFlowStatusRemark(progSubstlInfoShow.getFlowStatusRemark());
                        progMatqtyInfoService.updateRecordByProgSubstlInfo(progMatqtyInfoShowTemp);
                    }

                    MessageUtil.addInfo("��׼������ɡ�");
                    onQueryFormedAction("Approve","false");
                }
            }
        } catch (Exception e) {
            logger.error("��׼����ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }


    /*�����ֶ�Start*/

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public List<ProgSubstlInfoShow> getProgSubstlInfoShowList() {
        return progSubstlInfoShowList;
    }

    public void setProgSubstlInfoShowList(List<ProgSubstlInfoShow> progSubstlInfoShowList) {
        this.progSubstlInfoShowList = progSubstlInfoShowList;
    }

    public ProgSubstlInfoService getProgSubstlInfoService() {
        return progSubstlInfoService;
    }

    public void setProgSubstlInfoService(ProgSubstlInfoService progSubstlInfoService) {
        this.progSubstlInfoService = progSubstlInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShow() {
        return progSubstlInfoShow;
    }

    public void setProgSubstlInfoShow(ProgSubstlInfoShow progSubstlInfoShow) {
        this.progSubstlInfoShow = progSubstlInfoShow;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowSelected() {
        return progSubstlInfoShowSelected;
    }

    public void setProgSubstlInfoShowSelected(ProgSubstlInfoShow progSubstlInfoShowSelected) {
        this.progSubstlInfoShowSelected = progSubstlInfoShowSelected;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowNotForm() {
        return progSubstlInfoShowNotForm;
    }

    public void setProgSubstlInfoShowNotForm(ProgSubstlInfoShow progSubstlInfoShowNotForm) {
        this.progSubstlInfoShowNotForm = progSubstlInfoShowNotForm;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowNotFormSelected() {
        return progSubstlInfoShowNotFormSelected;
    }

    public void setProgSubstlInfoShowNotFormSelected(ProgSubstlInfoShow progSubstlInfoShowNotFormSelected) {
        this.progSubstlInfoShowNotFormSelected = progSubstlInfoShowNotFormSelected;
    }

    public List<ProgSubstlInfoShow> getProgSubstlInfoShowNotFormList() {
        return progSubstlInfoShowNotFormList;
    }

    public void setProgSubstlInfoShowNotFormList(List<ProgSubstlInfoShow> progSubstlInfoShowNotFormList) {
        this.progSubstlInfoShowNotFormList = progSubstlInfoShowNotFormList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getStrCstplPkid() {
        return strCstplPkid;
    }

    public void setStrCstplPkid(String strCstplPkid) {
        this.strCstplPkid = strCstplPkid;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public List<SelectItem> getSubcttList() {
        return subcttList;
    }

    public String getStrRowSelectedFlag() {
        return strRowSelectedFlag;
    }

    public String getStrApprovedFlag() {
        return strApprovedFlag;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowSel() {
        return progSubstlInfoShowSel;
    }

    public void setProgSubstlInfoShowSel(ProgSubstlInfoShow progSubstlInfoShowSel) {
        this.progSubstlInfoShowSel = progSubstlInfoShowSel;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowAdd() {
        return progSubstlInfoShowAdd;
    }

    public void setProgSubstlInfoShowAdd(ProgSubstlInfoShow progSubstlInfoShowAdd) {
        this.progSubstlInfoShowAdd = progSubstlInfoShowAdd;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowUpd() {
        return progSubstlInfoShowUpd;
    }

    public void setProgSubstlInfoShowUpd(ProgSubstlInfoShow progSubstlInfoShowUpd) {
        this.progSubstlInfoShowUpd = progSubstlInfoShowUpd;
    }

    public ProgSubstlInfoShow getProgSubstlInfoShowDel() {
        return progSubstlInfoShowDel;
    }

    public void setProgSubstlInfoShowDel(ProgSubstlInfoShow progSubstlInfoShowDel) {
        this.progSubstlInfoShowDel = progSubstlInfoShowDel;
    }

    /*�����ֶ�End*/
}
