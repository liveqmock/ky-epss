package epss.view.contract;

import epss.common.enums.EnumFlowStatusRemark;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.MessageUtil;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.CstplInfo;
import epss.repository.model.model_show.CstplInfoShow;
import epss.service.CstplInfoService;
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
public class CstplInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplInfoAction.class);
    @ManagedProperty(value = "#{cstplInfoService}")
    private CstplInfoService cstplInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private CstplInfoShow cstplInfoShowQry;
    private String strNotPassToStatus;
    private CstplInfoShow cstplInfoShowSel;
    private CstplInfoShow cstplInfoShowAdd;
    private CstplInfoShow cstplInfoShowUpd;
    private CstplInfoShow cstplInfoShowDel;
    private List<CstplInfoShow> cstplInfoShowList;    
	private CstplInfoShow cstplInfoShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;

    // ����֮�䴫�ݹ����Ĳ���
    private String strTkcttInfoPkid;

    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // ���ܰ���ͬ���ݹ������ܰ���ͬ��
        if (parammap.containsKey("strTkcttInfoPkid")) {
            strTkcttInfoPkid = parammap.get("strTkcttInfoPkid").toString();
        }else{
            strTkcttInfoPkid="";
        }
        initData();
    }
    public void initData() {
        this.cstplInfoShowList = new ArrayList<CstplInfoShow>();
        cstplInfoShowQry = new CstplInfoShow();
        cstplInfoShowQry.setTkcttInfoPkid(strTkcttInfoPkid);
        cstplInfoShowSel = new CstplInfoShow();
        cstplInfoShowAdd = new CstplInfoShow();
        cstplInfoShowAdd.setTkcttInfoPkid(strTkcttInfoPkid);
        cstplInfoShowUpd = new CstplInfoShow();
        cstplInfoShowDel = new CstplInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setFlowStatusListByPower("Qry");
        rowSelectedFlag = "false";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cstplInfoService.getStrMaxCstplInfoId();
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "CSTPL" + esCommon.getStrToday() + "001";
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
                cstplInfoShowAdd.setId(strMaxId);
            }else if("Upd".equals(strSubmitType)) {
                cstplInfoShowUpd.setId(strMaxId);
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
                    cstplInfoShowQry.setFlowStatusBegin("");
                    cstplInfoShowQry.setFlowStatusEnd("");
                    break;
                case "Mng":
                case "MngPass":
                case "MngFail":
                    cstplInfoShowQry.setFlowStatusBegin("");
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
                    break;
                case "Check":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                    break;
                case "DoubleCheck":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                    break;
                case "Approve":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
                case "ApprovedQry":
                    cstplInfoShowQry.setFlowStatusBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    cstplInfoShowQry.setFlowStatusEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                    break;
            }
            this.cstplInfoShowList.clear();
            cstplInfoShowQry.setTkcttInfoPkid(strTkcttInfoPkid);
            cstplInfoShowList = cstplInfoService.getCstplInfoListByFlowStatusBegin_End(cstplInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (cstplInfoShowList.isEmpty()) {
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

    public CstplInfo getCttInfoByPkid(String strPkid) {
        return cstplInfoService.getCstplInfoByPkid(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cstplInfoShowAdd = new CstplInfoShow();
        cstplInfoShowAdd.setTkcttInfoPkid(strTkcttInfoPkid);
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(String strPowerTypePara,
                                     String strSubmitTypePara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setFlowStatusListByPower(strPowerTypePara);
            cstplInfoShowSelected.setCreatedByName(esCommon.getOperNameByOperId(cstplInfoShowSelected.getCreatedBy()));
            cstplInfoShowSelected.setUpdatedByName(esCommon.getOperNameByOperId(cstplInfoShowSelected.getUpdatedBy()));
            switch (strPowerTypePara){
                case "Qry":// ��ѯ
                cstplInfoShowSel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected); break;
                case "Mng":// ά��
                    if (strSubmitTypePara.equals("Sel")) {
                        cstplInfoShowSel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
                        rowSelectedFlag = "true";
                    } else if (strSubmitTypePara.equals("Add")) {
                        cstplInfoShowAdd = new CstplInfoShow();
                        cstplInfoShowAdd.setTkcttInfoPkid(strTkcttInfoPkid);
                        rowSelectedFlag = "false";
                    } else {
                        if (strSubmitTypePara.equals("Upd")) {
                            cstplInfoShowUpd = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
                            rowSelectedFlag = "false";
                        } else if (strSubmitTypePara.equals("Del")) {
                            cstplInfoShowDel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
                            rowSelectedFlag = "false";
                        }
                    }
                    break;
                default:
                    cstplInfoShowSel = (CstplInfoShow) BeanUtils.cloneBean(cstplInfoShowSelected);
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
    private boolean submitPreCheck(CstplInfoShow cstplInfoShow) {
        if (StringUtils.isEmpty(cstplInfoShow.getId())) {
            MessageUtil.addError("�������ͬ�ţ�");
            return false;
        } else if (StringUtils.isEmpty(cstplInfoShow.getName())) {
            MessageUtil.addError("�������ͬ����");
            return false;
        } else if (StringUtils.isEmpty(cstplInfoShow.getSignDate())) {
            MessageUtil.addError("������ǩ�����ڣ�");
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
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    cstplInfoShowSel.setFlowStatus(null);
                    cstplInfoShowSel.setFlowStatusRemark(null);
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // ԭ�����ͨ��
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // ԭ�����δ��
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // ԭ�򣺸���ͨ��
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    cstplInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    cstplInfoShowSel.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // ԭ����׼ͨ��
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");

                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����д����ʵ��Խ���˻�
                    cstplInfoShowSel.setFlowStatus(strNotPassToStatus);
                    // ԭ����׼δ��
                    cstplInfoShowSel.setFlowStatusRemark(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode());
                    cstplInfoService.updateRecord(cstplInfoShowSel);
                    /*List<ProgInfo> progInfoListTemp =
                            esFlowService.selectIsUsedInQMPByCstplInfoPkid(cstplInfoShowSel.getPkid());
                    if (progInfoListTemp.size() > 0) {
                        MessageUtil.addInfo("�������Ѿ���["
                                + ESEnum.valueOfAlias(progInfoListTemp.get(0).getInfoType()).getTitle()
                                + "]ʹ�ã�������׼δ��,�����ر༭��");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cstplInfoShowSel.getPkid()) > 0) {
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
                    if (!submitPreCheck(cstplInfoShowAdd)) {
                        return;
                    }
                    if (cstplInfoService.isExistInDb(cstplInfoShowAdd)) {
                        MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                    } else {
                        cstplInfoService.insertRecord(cstplInfoShowAdd);
                        MessageUtil.addInfo("�������ݳɹ���");
                    }
                    break;
                case "Upd":
                    cstplInfoService.updateRecord(cstplInfoShowUpd);
                    MessageUtil.addInfo("�������ݳɹ���");
                    break;
                case "Del":
                    int deleteRecordNumOfCtt= cstplInfoService.deleteRecord(cstplInfoShowDel.getPkid());
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
    public CstplInfoService getCstplInfoService() {
        return cstplInfoService;
    }

    public void setCstplInfoService(CstplInfoService cstplInfoService) {
        this.cstplInfoService = cstplInfoService;
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

    public CstplInfoShow getCstplInfoShowSelected() {
        return cstplInfoShowSelected;
    }

    public void setCstplInfoShowSelected(CstplInfoShow cstplInfoShowSelected) {
        this.cstplInfoShowSelected = cstplInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public CstplInfoShow getCstplInfoShowQry() {
        return cstplInfoShowQry;
    }

    public void setCstplInfoShowQry(CstplInfoShow cstplInfoShowQry) {
        this.cstplInfoShowQry = cstplInfoShowQry;
    }

    public List<CstplInfoShow> getCstplInfoShowList() {
        return cstplInfoShowList;
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

    public CstplInfoShow getCstplInfoShowUpd() {
        return cstplInfoShowUpd;
    }

    public void setCstplInfoShowUpd(CstplInfoShow cstplInfoShowUpd) {
        this.cstplInfoShowUpd = cstplInfoShowUpd;
    }

    public CstplInfoShow getCstplInfoShowAdd() {
        return cstplInfoShowAdd;
    }

    public void setCstplInfoShowAdd(CstplInfoShow cstplInfoShowAdd) {
        this.cstplInfoShowAdd = cstplInfoShowAdd;
    }

    public CstplInfoShow getCstplInfoShowDel() {
        return cstplInfoShowDel;
    }

    public void setCstplInfoShowDel(CstplInfoShow cstplInfoShowDel) {
        this.cstplInfoShowDel = cstplInfoShowDel;
    }

    public CstplInfoShow getCstplInfoShowSel() {
        return cstplInfoShowSel;
    }

    public void setCstplInfoShowSel(CstplInfoShow cstplInfoShowSel) {
        this.cstplInfoShowSel = cstplInfoShowSel;
    }
    /*�����ֶ� End*/
}
