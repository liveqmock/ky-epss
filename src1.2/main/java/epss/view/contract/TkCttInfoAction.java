package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.CttInfoShow;
import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsInitStl;
import epss.service.EsCttInfoService;
import epss.service.EsInitPowerService;
import epss.service.common.EsFlowService;
import epss.view.common.EsCommon;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

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
public class TkCttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(TkCttInfoAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    private CttInfoShow cttInfoShowQry;
    private String strNotPassToStatus;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;
    private List<CttInfoShow> cttInfoShowList;

    private String strSubmitType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        initData();
    }
    public void initData() {
        this.cttInfoShowList = new ArrayList<CttInfoShow>();
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowSel = new CttInfoShow();
        cttInfoShowSel.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowDel = new CttInfoShow();
        cttInfoShowDel.setCttType(ESEnum.ITEMTYPE0.getCode());
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
        esFlowControl.setStatusFlagListByPower("Qry");
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = esCttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE0.getCode());
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
            cttInfoShowAdd.setId(strMaxId);
            cttInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            if (strQryFlag.equals("Qry")) {

            } else if (strQryFlag.contains("Mng")) {
                cttInfoShowQry.setStrStatusFlagBegin(null);
                cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
            } else if (strQryFlag.contains("Check")) {
                if (strQryFlag.contains("DoubleCheck")) {
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                }
            }  else if (strQryFlag.contains("Approve")) {
                if (strQryFlag.equals("ApprovedQry")) {
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                }
            }
            cttInfoShowQry.setPeriodNo("NULL");
            this.cttInfoShowList.clear();
            cttInfoShowList = esFlowService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
            if(strQryMsgOutPara.equals("true"))  {
                if (cttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("��ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��ͬ��Ϣ��ѯʧ��");
        }
        return null;
    }

    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return esCttInfoService.getCttInfoByPkId(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE0.getCode());
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   CttInfoShow cttInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            esFlowControl.setStatusFlagListByPower(strPowerTypePara);
            // ��ѯ
            cttInfoShowPara.setCreatedByName(esCommon.getOperNameByOperId(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(esCommon.getOperNameByOperId(cttInfoShowPara.getLastUpdBy()));
            if (strPowerTypePara.equals("Qry")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            } else if (strPowerTypePara.equals("Mng")) { // ά��
                if (strSubmitTypePara.equals("Sel")) {
                    cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                } else if (strSubmitTypePara.equals("Add")) {
                    cttInfoShowAdd = new CttInfoShow();
                } else {
                    if (strSubmitTypePara.equals("Upd")) {
                        cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                    } else if (strSubmitTypePara.equals("Del")) {
                        cttInfoShowDel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                    }
                }
            } else {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                //�������̻���,��ʾ��ͬ���˻ص�״̬
                esFlowControl.setStatusFlagListByPower(strPowerTypePara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if (StringUtils.isEmpty(cttInfoShowPara.getId())) {
            MessageUtil.addError("�������ͬ�ţ�");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getName())) {
            MessageUtil.addError("�������ͬ����");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignDate())) {
            MessageUtil.addError("������ǩ�����ڣ�");
            return false;
        }
        if (StringUtils.isEmpty(cttInfoShowPara.getSignPartA())) {
            MessageUtil.addError("������ǩ���׷���");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignPartB())) {
            MessageUtil.addError("������ǩ���ҷ���");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttStartDate())) {
            MessageUtil.addError("�������ͬ��ʼʱ�䣡");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttEndDate())) {
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
                    esFlowControl.mngFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("����¼����ɣ�");
                } else if (strPowerTypePara.equals("MngFail")) {
                    esFlowControl.mngNotFinishAction(
                            cttInfoShowSel.getCttType(),
                            cttInfoShowSel.getPkid(),
                            "NULL");
                    MessageUtil.addInfo("����¼��δ�꣡");
                }
            }// ���
            else if (strPowerTypePara.contains("Check") && !strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("CheckPass")) {
                    // ״̬��־�����
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                    // ԭ�����ͨ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG1.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("�������ͨ����");
                } else if (strPowerTypePara.equals("CheckFail")) {
                    // ״̬��־����ʼ
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    // ԭ�����δ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG2.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("�������δ����");
                }
            } // ����
            else if (strPowerTypePara.contains("DoubleCheck")) {
                if (strPowerTypePara.equals("DoubleCheckPass")) {
                    // ״̬��־������
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG2.getCode());
                    // ԭ�򣺸���ͨ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG3.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���ͨ����");
                } else if (strPowerTypePara.equals("DoubleCheckFail")) {
                    // ����д����ʵ��Խ���˻�
                    cttInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // ԭ�򣺸���δ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG4.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("���ݸ���δ����");
                }
            }// ��׼
            else if (strPowerTypePara.contains("Approve")) {
                if (strPowerTypePara.equals("ApprovePass")) {
                    // ״̬��־����׼
                    cttInfoShowSel.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    // ԭ����׼ͨ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);
                    MessageUtil.addInfo("������׼ͨ����");
                } else if (strPowerTypePara.equals("ApproveFail")) {
                    // ����Ƿ�ʹ��
                    String strCttTypeTemp = "";
                    if (cttInfoShowSel.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE1.getCode();
                    } else if (cttInfoShowSel.getCttType().equals(ESEnum.ITEMTYPE1.getCode())) {
                        strCttTypeTemp = ESEnum.ITEMTYPE2.getCode();
                    }

                    // ����д����ʵ��Խ���˻�
                    cttInfoShowSel.setStatusFlag(strNotPassToStatus);
                    // ԭ����׼δ��
                    cttInfoShowSel.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());

                    esInitPowerService.updateRecordByCtt(cttInfoShowSel);

                    List<EsInitStl> esInitStlListTemp =
                            esFlowService.selectIsUsedInQMPBySubcttPkid(cttInfoShowSel.getPkid());
                    if (esInitStlListTemp.size() > 0) {
                        MessageUtil.addInfo("�������Ѿ���["
                                + ESEnum.valueOfAlias(esInitStlListTemp.get(0).getStlType()).getTitle()
                                + "]ʹ�ã�������׼δ��,�����ر༭��");
                    } else {
                        if (esFlowService.getChildrenOfThisRecordInEsInitCtt(strCttTypeTemp,
                                cttInfoShowSel.getPkid()) > 0) {
                            MessageUtil.addInfo("�������Ѿ���[" + ESEnum.valueOfAlias(strCttTypeTemp).getTitle()
                                    + "]ʹ�ã�������׼δ��,�����ر༭��");
                        } else {
                            MessageUtil.addInfo("������׼δ����");
                        }
                    }
                }
            }
            // ���²�ѯ���Ѳ����Ľ��
            onQueryAction(strPowerTypePara,"false");
        } catch (Exception e) {
            logger.error("�������̻�ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * �ύά��Ȩ��
     *
     * @param
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Add")) {
            if (!submitPreCheck(cttInfoShowAdd)) {
                return;
            }
            if (esCttInfoService.isExistInDb(cttInfoShowAdd)) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
            } else {
                addRecordAction(cttInfoShowAdd);
            }
        } else if (strSubmitType.equals("Upd")) {
            updRecordAction(cttInfoShowUpd);
        } else if (strSubmitType.equals("Del")) {
            deleteRecordAction(cttInfoShowDel);
        }
        onQueryAction("Mng","false");
    }

    private void addRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE0.getCode());
            if (cttInfoShowPara.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                cttInfoShowPara.setParentPkid("ROOT");
            }
            esCttInfoService.insertRecord(cttInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE0.getCode());
            esCttInfoService.updateRecord(cttInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE0.getCode());
            int deleteRecordNumOfCtt= esCttInfoService.deleteRecord(cttInfoShowPara.getPkid());
            int deleteRecordNumOfPower=esInitPowerService.deleteRecord(
                    cttInfoShowPara.getCttType(),
                    cttInfoShowPara.getPkid(),
                    "NULL");
            if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfPower<=0){
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ� Start*/
    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public CttInfoShow getCttInfoShowQry() {
        return cttInfoShowQry;
    }

    public void setCttInfoShowQry(CttInfoShow cttInfoShowQry) {
        this.cttInfoShowQry = cttInfoShowQry;
    }

    public List<CttInfoShow> getCttInfoShowList() {
        return cttInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowAdd() {
        return cttInfoShowAdd;
    }

    public void setCttInfoShowAdd(CttInfoShow cttInfoShowAdd) {
        this.cttInfoShowAdd = cttInfoShowAdd;
    }

    public CttInfoShow getCttInfoShowDel() {
        return cttInfoShowDel;
    }

    public void setCttInfoShowDel(CttInfoShow cttInfoShowDel) {
        this.cttInfoShowDel = cttInfoShowDel;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    /*�����ֶ� End*/
}
