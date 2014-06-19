package epss.view.contract;

import epss.common.enums.EnumFlowStatusRemark;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.TkcttInfoShow;
import epss.repository.model.TkcttInfo;

import epss.service.TkcttInfoService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TkcttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttInfoAction.class);
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private TkcttInfoShow tkcttInfoShowQry;
    private String strNotPassToStatus;
    private TkcttInfoShow tkcttInfoShowSel;
    private TkcttInfoShow tkcttInfoShowAdd;
    private TkcttInfoShow tkcttInfoShowUpd;
    private TkcttInfoShow tkcttInfoShowDel;
    private List<TkcttInfoShow> tkcttInfoShowList;
    private TkcttInfoShow tkcttInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        initData();
    }
    public void initData() {
        this.tkcttInfoShowList = new ArrayList<TkcttInfoShow>();
        tkcttInfoShowQry = new TkcttInfoShow();
        tkcttInfoShowSel = new TkcttInfoShow();
        tkcttInfoShowAdd = new TkcttInfoShow();
        tkcttInfoShowUpd = new TkcttInfoShow();
        tkcttInfoShowDel = new TkcttInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = tkcttInfoService.getStrMaxTkcttInfoId();
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "TKCTT" + esCommon.getStrToday() + "001";
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
                tkcttInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                tkcttInfoShowUpd.setId(strMaxId);
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
                    tkcttInfoShowQry.setFlowStatusBegin("");
                    tkcttInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                case "MngPass":
                case "MngFail":
                    tkcttInfoShowQry.setFlowStatusBegin("");
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "ApprovedQry":
                    tkcttInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    tkcttInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
            this.tkcttInfoShowList.clear();
            tkcttInfoShowList = tkcttInfoService.getTkcttInfoListByFlowStatusBegin_End(tkcttInfoShowQry);
            if(strQryMsgOutPara.equals("true"))  {
                if (tkcttInfoShowList.isEmpty()) {
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

    public TkcttInfo getCttInfoByPkId(String strPkid) {
        return tkcttInfoService.getTkcttInfoByPkid(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        tkcttInfoShowAdd = new TkcttInfoShow();
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
            // ��ѯ
            tkcttInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(tkcttInfoShowSelected.getCreatedBy()));
            tkcttInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(tkcttInfoShowSelected.getUpdatedBy()));
            switch (strPowerTypePara){
                case "Qry":
                    tkcttInfoShowSel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                    break;
                case "Mng":// ά��
                    if (strSubmitTypePara.equals("Sel")) {
                        tkcttInfoShowSel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                        rowSelectedFlag = "true";
                    } else if (strSubmitTypePara.equals("Add")) {
                        tkcttInfoShowAdd = new TkcttInfoShow();
                        rowSelectedFlag = "false";
                    } else {
                        if (strSubmitTypePara.equals("Upd")) {
                            tkcttInfoShowUpd = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        } else if (strSubmitTypePara.equals("Del")) {
                            tkcttInfoShowDel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
                            rowSelectedFlag = "false";
                        }
                    }
                    break;
                default:
                    tkcttInfoShowSel = (TkcttInfoShow) BeanUtils.cloneBean(tkcttInfoShowSelected);
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
    private boolean submitPreCheck(TkcttInfoShow tkcttInfoShowPara) {
        if (StringUtils.isEmpty(tkcttInfoShowPara.getId())) {
            MessageUtil.addError("�������ͬ�ţ�");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getName())) {
            MessageUtil.addError("�������ͬ����");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getSignDate())) {
            MessageUtil.addError("������ǩ�����ڣ�");
            return false;
        }
        if (StringUtils.isEmpty(tkcttInfoShowPara.getSignPartPkidA())) {
            MessageUtil.addError("������ǩ���׷���");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getSignPartPkidB())) {
            MessageUtil.addError("������ǩ���ҷ���");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getStartDate())) {
            MessageUtil.addError("�������ͬ��ʼʱ�䣡");
            return false;
        } else if (StringUtils.isEmpty(tkcttInfoShowPara.getEndDate())) {
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
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    tkcttInfoShowSel.setFlowStatus(null);
                    tkcttInfoShowSel.setFlowStatusRemark(null);
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    tkcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    tkcttInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����д����ʵ��Խ���˻�
                    tkcttInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    tkcttInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    tkcttInfoService.updateRecord(tkcttInfoShowSel);
                    /*List<ProgInfo> progInfoListTemp =
                            esFlowService.selectIsUsedInQMPByTkcttInfoPkid(tkcttInfoShowSel.getPkid());
                    if (progInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("�������Ѿ���["
                                + ESEnum.valueOfAlias(progInfoListTemp.get(0).getInfoType()).getTitle()
                                + "]ʹ�ã�������׼δ��,�����ر༭��");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                tkcttInfoShowSel.getPkid()) > 0) {
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
                    if (!submitPreCheck(tkcttInfoShowAdd)) {
                        return;
                    }
                    if (tkcttInfoService.isExistInDb(tkcttInfoShowAdd)) {
                        MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                    } else {
                        tkcttInfoService.insertRecord(tkcttInfoShowAdd);
                        MessageUtil.addInfo("�������ݳɹ���");
                    }
                    break;
                case "Upd":
                    tkcttInfoService.updateRecord(tkcttInfoShowUpd);
                    MessageUtil.addInfo("�������ݳɹ���");
                    break;
                case "Del":
                    int deleteRecordNumOfCtt= tkcttInfoService.deleteRecord(tkcttInfoShowDel.getPkid());
                    if (deleteRecordNumOfCtt<=0){
                        MessageUtil.addInfo("�ü�¼��ɾ����");
                    }else {
                        MessageUtil.addInfo("ɾ�����ݳɹ���");
                    }
            }
            onQueryAction("Mng","false");
        } catch (Exception e) {
            logger.error("�ύ����ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ� Start*/
    public TkcttInfoService getTkcttInfoService() {
        return tkcttInfoService;
    }

    public void setTkcttInfoService(TkcttInfoService tkcttInfoService) {
        this.tkcttInfoService = tkcttInfoService;
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

    public TkcttInfoShow getTkcttInfoShowSelected() {
        return tkcttInfoShowSelected;
    }

    public void setTkcttInfoShowSelected(TkcttInfoShow tkcttInfoShowSelected) {
        this.tkcttInfoShowSelected = tkcttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public TkcttInfoShow getTkcttInfoShowQry() {
        return tkcttInfoShowQry;
    }

    public void setTkcttInfoShowQry(TkcttInfoShow tkcttInfoShowQry) {
        this.tkcttInfoShowQry = tkcttInfoShowQry;
    }

    public List<TkcttInfoShow> getTkcttInfoShowList() {
        return tkcttInfoShowList;
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

    public TkcttInfoShow getTkcttInfoShowUpd() {
        return tkcttInfoShowUpd;
    }

    public void setTkcttInfoShowUpd(TkcttInfoShow tkcttInfoShowUpd) {
        this.tkcttInfoShowUpd = tkcttInfoShowUpd;
    }

    public TkcttInfoShow getTkcttInfoShowAdd() {
        return tkcttInfoShowAdd;
    }

    public void setTkcttInfoShowAdd(TkcttInfoShow tkcttInfoShowAdd) {
        this.tkcttInfoShowAdd = tkcttInfoShowAdd;
    }

    public TkcttInfoShow getTkcttInfoShowDel() {
        return tkcttInfoShowDel;
    }

    public void setTkcttInfoShowDel(TkcttInfoShow tkcttInfoShowDel) {
        this.tkcttInfoShowDel = tkcttInfoShowDel;
    }

    public TkcttInfoShow getTkcttInfoShowSel() {
        return tkcttInfoShowSel;
    }

    public void setTkcttInfoShowSel(TkcttInfoShow tkcttInfoShowSel) {
        this.tkcttInfoShowSel = tkcttInfoShowSel;
    }

    /*�����ֶ� End*/
}
