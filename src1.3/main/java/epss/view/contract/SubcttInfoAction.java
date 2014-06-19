package epss.view.contract;

import epss.common.enums.EnumFlowStatusRemark;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.SubcttInfoShow;
import epss.service.ProgMatqtyInfoService;
import epss.service.ProgSubstlInfoService;
import epss.service.ProgWorkqtyInfoService;
import epss.service.SubcttInfoService;
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
import java.util.ArrayList;
import java.util.List;
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
public class SubcttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttInfoAction.class);
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progSubstlInfoService}")
    private ProgSubstlInfoService progSubstlInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private SubcttInfoShow subcttInfoShowQry;
    private String strNotPassToStatus;
    private SubcttInfoShow subcttInfoShowSel;
    private SubcttInfoShow subcttInfoShowAdd;
    private SubcttInfoShow subcttInfoShowUpd;
    private SubcttInfoShow subcttInfoShowDel;
    private List<SubcttInfoShow> subcttInfoShowList;
    private SubcttInfoShow subcttInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;

    // ����֮�䴫�ݹ����Ĳ���
    private String strCstplInfoPkid;

    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // �ӳɱ��ƻ����ݹ����ĳɱ��ƻ���
        if (parammap.containsKey("strCstplInfoPkid")) {
            strCstplInfoPkid = parammap.get("strCstplInfoPkid").toString();
        } else {// �ܰ���ͬҳ����
            strCstplInfoPkid = "";
        }
        initData();
    }
    public void initData() {
        this.subcttInfoShowList = new ArrayList<SubcttInfoShow>();
        subcttInfoShowQry = new SubcttInfoShow();
        subcttInfoShowQry.setCstplInfoPkid(strCstplInfoPkid);
		subcttInfoShowSel = new SubcttInfoShow();
        subcttInfoShowAdd = new SubcttInfoShow();
        subcttInfoShowAdd.setCstplInfoPkid(strCstplInfoPkid);
        subcttInfoShowUpd = new SubcttInfoShow();
        subcttInfoShowDel = new SubcttInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = subcttInfoService.getStrMaxSubcttInfoId();
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "SUBCTT" + esCommon.getStrToday() + "001";
            } else {
                if (strMaxId.length() > 3) {
                    String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                    if (ToolUtil.strIsDigit(strTemp)) {
                        intTemp = Integer.parseInt(strTemp);
                        intTemp = intTemp + 1;
                        strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                    } else {
                        strMaxId += "001";
                    }
                }
            }
            if("Add".equals(strSubmitType)) {
                subcttInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                subcttInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            switch (strQryFlag){
                case "Qry":
                    subcttInfoShowQry.setFlowStatusBegin("");
                    subcttInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                case "MngPass":
                case "MngFail":
                    subcttInfoShowQry.setFlowStatusBegin("");
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "ApprovedQry":
                    subcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    subcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.subcttInfoShowList.clear();
            subcttInfoShowQry.setCstplInfoPkid(strCstplInfoPkid);
            subcttInfoShowList = subcttInfoService.getSubcttInfoListByFlowStatusBegin_End(subcttInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (subcttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
            rowSelectedFlag = "false";
        } catch (Exception e) {
            logger.error("��ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��ͬ��Ϣ��ѯʧ��");
        }
        return null;
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        subcttInfoShowAdd = new SubcttInfoShow();
        subcttInfoShowAdd.setCstplInfoPkid(strCstplInfoPkid);
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
            subcttInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(subcttInfoShowSelected.getCreatedBy()));
            subcttInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(subcttInfoShowSelected.getUpdatedBy()));
            switch (strPowerTypePara){
                case "Qry":// ��ѯ
                    subcttInfoShowSel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected); break;
                case "Mng":// ά��
                    if (strSubmitTypePara.equals("Sel")) {
                        subcttInfoShowSel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                        rowSelectedFlag = "true";
                    } else if (strSubmitTypePara.equals("Add")) {
                        subcttInfoShowAdd = new SubcttInfoShow();
                        subcttInfoShowAdd.setCstplInfoPkid(strCstplInfoPkid);
                        rowSelectedFlag = "false";
                    } else {
                        if (strSubmitTypePara.equals("Upd")) {
                            subcttInfoShowUpd = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        } else if (strSubmitTypePara.equals("Del")) {
                            subcttInfoShowDel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        }
                    }
                    break;
                default:
                    subcttInfoShowSel = (SubcttInfoShow) BeanUtils.cloneBean(subcttInfoShowSelected);
                    rowSelectedFlag = "true";
            }
            //�������̻���,��ʾ��ͬ���˻ص�״̬
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(SubcttInfoShow subcttInfoShowPara) {
        if (StringUtils.isEmpty(subcttInfoShowPara.getId())) {
            MessageUtil.addError("�������ͬ�ţ�");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getName())) {
            MessageUtil.addError("�������ͬ����");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getSignDate())) {
            MessageUtil.addError("������ǩ�����ڣ�");
            return false;
        }
        if (StringUtils.isEmpty(subcttInfoShowPara.getSignPartPkidA())) {
            MessageUtil.addError("������ǩ���׷���");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getSignPartPkidB())) {
            MessageUtil.addError("������ǩ���ҷ���");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getStartDate())) {
            MessageUtil.addError("�������ͬ��ʼʱ�䣡");
            return false;
        } else if (StringUtils.isEmpty(subcttInfoShowPara.getEndDate())) {
            MessageUtil.addError("�������ͬ��ֹʱ�䣡");
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
            if (strPowerTypePara.contains("Mng")) {
                if (strPowerTypePara.equals("MngPass")) {
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    subcttInfoShowSel.setFlowStatus(null);
                    subcttInfoShowSel.setFlowStatusRemark(null);
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    subcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    subcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����д����ʵ��Խ���˻�
                    subcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    subcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    subcttInfoService.updateRecord(subcttInfoShowSel);
                    /*List<ProgInfo> progInfoListTemp =
                            progWorkqtyInfoService.selectIsUsedInQMPBySubcttPkid(subcttInfoShowSel.getPkid());
                    if (progInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("�������Ѿ���["
                                + ESEnum.valueOfAlias(progInfoListTemp.get(0).getInfoType()).getTitle()
                                + "]ʹ�ã�������׼δ��,�����ر༭��");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                subcttInfoShowSel.getPkid()) > 0) {
                            MessageUtil.addInfo("�������Ѿ���[" + ESEnum.valueOfAlias(strCttTypeTemp).getTitle()
                                    + "]ʹ�ã�������׼δ��,�����ر༭��");
                        } else {
                            MessageUtil.addInfo("������׼δ����");
                        }
                    }*/
                }
            }
            // ���²�ѯ���Ѳ����Ľ��
            onQueryAction(strPowerTypePara,"false");
            rowSelectedFlag="false";
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction() {
        try {
            switch (strSubmitType){
                case "Add":
                    if (!submitPreCheck(subcttInfoShowAdd)) {
                        return;
                    }
                    if (subcttInfoService.isExistInDb(subcttInfoShowAdd)) {
                        MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                    } else {
                        subcttInfoService.insertRecord(subcttInfoShowAdd);
                        MessageUtil.addInfo("�������ݳɹ���");
                    }
                    break;
                case "Upd":
                    subcttInfoService.updateRecord(subcttInfoShowUpd);
                    MessageUtil.addInfo("�������ݳɹ���");
                    break;
                case "Del":
                    int deleteRecordNumOfCtt= subcttInfoService.deleteRecord(subcttInfoShowDel.getPkid());
                    if (deleteRecordNumOfCtt<=0){
                        MessageUtil.addInfo("�ü�¼��ɾ����");
                    }else {
                        MessageUtil.addInfo("ɾ�����ݳɹ���");
                    }
            }
            onQueryAction("Mng", "false");
        } catch (Exception e) {
            logger.error("�ύ����ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ� Start*/
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

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public SubcttInfoShow getSubcttInfoShowSelected() {
        return subcttInfoShowSelected;
    }

    public void setSubcttInfoShowSelected(SubcttInfoShow subcttInfoShowSelected) {
        this.subcttInfoShowSelected = subcttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public SubcttInfoShow getSubcttInfoShowQry() {
        return subcttInfoShowQry;
    }

    public void setSubcttInfoShowQry(SubcttInfoShow subcttInfoShowQry) {
        this.subcttInfoShowQry = subcttInfoShowQry;
    }

    public List<SubcttInfoShow> getSubcttInfoShowList() {
        return subcttInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public SubcttInfoShow getSubcttInfoShowUpd() {
        return subcttInfoShowUpd;
    }

    public void setSubcttInfoShowUpd(SubcttInfoShow subcttInfoShowUpd) {
        this.subcttInfoShowUpd = subcttInfoShowUpd;
    }

    public SubcttInfoShow getSubcttInfoShowAdd() {
        return subcttInfoShowAdd;
    }

    public void setSubcttInfoShowAdd(SubcttInfoShow subcttInfoShowAdd) {
        this.subcttInfoShowAdd = subcttInfoShowAdd;
    }

    public SubcttInfoShow getSubcttInfoShowDel() {
        return subcttInfoShowDel;
    }

    public void setSubcttInfoShowDel(SubcttInfoShow subcttInfoShowDel) {
        this.subcttInfoShowDel = subcttInfoShowDel;
    }

    public SubcttInfoShow getSubcttInfoShowSel() {
        return subcttInfoShowSel;
    }

    public void setSubcttInfoShowSel(SubcttInfoShow subcttInfoShowSel) {
        this.subcttInfoShowSel = subcttInfoShowSel;
    }

    public ProgWorkqtyInfoService getProgWorkqtyInfoService() {
        return progWorkqtyInfoService;
    }

    public void setProgWorkqtyInfoService(ProgWorkqtyInfoService progWorkqtyInfoService) {
        this.progWorkqtyInfoService = progWorkqtyInfoService;
    }

    public ProgMatqtyInfoService getProgMatqtyInfoService() {
        return progMatqtyInfoService;
    }

    public void setProgMatqtyInfoService(ProgMatqtyInfoService progMatqtyInfoService) {
        this.progMatqtyInfoService = progMatqtyInfoService;
    }

    public ProgSubstlInfoService getProgSubstlInfoService() {
        return progSubstlInfoService;
    }

    public void setProgSubstlInfoService(ProgSubstlInfoService progSubstlInfoService) {
        this.progSubstlInfoService = progSubstlInfoService;
    }
/*�����ֶ� End*/
}
