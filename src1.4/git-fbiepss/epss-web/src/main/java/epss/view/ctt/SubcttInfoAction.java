package epss.view.ctt;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.CttItemShow;
import epss.service.CttInfoService;
import epss.service.CttItemService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
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
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
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
    private String rowSelectedFlag;

    // ����֮�䴫�ݹ����Ĳ���
    private String strBelongToPkid;

    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;
    //ʵ�ּ׹������
    private CttItemShow cttItemShow;
    //��֤�ְ���ͬ��ź������Ƿ��ظ�����ʾ��Ϣ
    String strWarnMsg;

    @PostConstruct
    public void init() {
        try {
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            // �ӳɱ��ƻ����ݹ����ĳɱ��ƻ���
            if (parammap.containsKey("strCttInfoPkid")) {
                strBelongToPkid = parammap.get("strCttInfoPkid").toString();
            } else {// �ܰ���ͬҳ����
                strBelongToPkid = null;
            }
            initData();
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public void initData() {
        try {
            this.cttInfoShowList = new ArrayList<>();
            cttInfoShowQry = new CttInfoShow();
            cttInfoShowQry.setCttType(EnumResType.RES_TYPE2.getCode());
            cttInfoShowQry.setParentPkid(strBelongToPkid);
            cttInfoShowAdd = new CttInfoShow();
            cttInfoShowAdd.setCttType(EnumResType.RES_TYPE2.getCode());
            cttInfoShowAdd.setParentPkid(strBelongToPkid);
            cttInfoShowSel = new CttInfoShow();
            cttInfoShowUpd = new CttInfoShow();
            cttInfoShowDel = new CttInfoShow();
            styleModel = new StyleModel();
            styleModel.setDisabled_Flag("false");
            strSubmitType = "Add";
            rowSelectedFlag = "false";
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
            MessageUtil.addError("��ʼ��ʧ��");
        }
    }

    //�ְ���ͬ¼��ʱ����֤�ְ���ͬ����Ƿ�Ϸ��ظ�
    public void validSubCttId() {
        strWarnMsg = "";
        CttInfoShow cttInfoShowTemp=new CttInfoShow();
        String subCttIdFromPage = cttInfoShowAdd.getId();
        if (!(subCttIdFromPage.matches("^[a-zA-Z0-9]+$"))) {
            strWarnMsg = "��ͬ���Ӧ����ĸ���ֿ�ͷ�����������롣";
        } else {
            cttInfoShowTemp.setId(cttInfoShowAdd.getId());
            if (cttInfoService.getListByModelShow(cttInfoShowTemp).size()>0) {
                strWarnMsg = "�ú�ͬ����Ѵ��ڣ����������롣";
            }
        }
    }

    //�ְ���ͬ¼��ʱ����֤�ְ���ͬ�����Ƿ�Ϸ��ظ�
    public void validSubCttName() {
        strWarnMsg = "";
        CttInfoShow cttInfoShowTemp=new CttInfoShow();
        cttInfoShowTemp.setName(cttInfoShowAdd.getName());
        if (cttInfoService.getListByModelShow(cttInfoShowTemp).size()>0) {
            strWarnMsg = "�ú�ͬ���Ѵ��ڣ����������롣";
        }
    }

    public void setMaxNoPlusOne(String strQryTypePara) {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(EnumResType.RES_TYPE2.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "SUBCTT" + ToolUtil.getStrToday() + "001";
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
            if (strQryTypePara.equals("Qry")) {
                cttInfoShowQry.setId(strMaxId);
            }else if (strQryTypePara.equals("Add")) {
                cttInfoShowAdd.setId(strMaxId);
            }else if (strQryTypePara.equals("Upd")) {
                cttInfoShowUpd.setId(strMaxId);
            }
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
                cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
            } else if (strQryFlag.contains("Check")) {
                if (strQryFlag.contains("DoubleCheck")) {
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                }
            }  else if (strQryFlag.contains("Approve")) {
                if (strQryFlag.equals("ApprovedQry")) {
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                }
            }
            this.cttInfoShowList.clear();
            cttInfoShowQry.setParentPkid(strBelongToPkid);
            cttInfoShowList = cttInfoService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (cttInfoShowList.isEmpty()) {
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

    public void selectRecordAction(
                                     String strSubmitTypePara,
                                     CttInfoShow cttInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            cttInfoShowPara.setCreatedByName(ToolUtil.getUserName(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(ToolUtil.getUserName(cttInfoShowPara.getLastUpdBy()));
            // ��ѯ
            if (strSubmitTypePara.equals("Sel")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                rowSelectedFlag = "true";
            } else if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = new CttInfoShow();
                cttInfoShowAdd.setParentPkid(strBelongToPkid);
                rowSelectedFlag = "false";
            } else if (strSubmitTypePara.equals("Upd")) {
                cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                rowSelectedFlag = "false";
            } else if (strSubmitTypePara.equals("Del")) {
                cttInfoShowDel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                rowSelectedFlag = "false";
            } else {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                rowSelectedFlag = "true";
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
     * �ύά��Ȩ��
     *
     * @param
     */
    public void onClickForMngAction() {
             if (!submitPreCheck(cttInfoShowUpd)) {
                 return;
             }
                 updRecordAction(cttInfoShowUpd);
                 onQueryAction("Mng","false");
    }
    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(EnumResType.RES_TYPE2.getCode());
            cttInfoService.updateRecord(cttInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ� Start*/
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    /*�����ֶ� End*/

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
    }

    public CttItemShow getCttItemShow() {
        return cttItemShow;
    }

    public void setCttItemShow(CttItemShow cttItemShow) {
        this.cttItemShow = cttItemShow;
    }

    public String getStrWarnMsg() {
        return strWarnMsg;
    }

    public void setStrWarnMsg(String strWarnMsg) {
        this.strWarnMsg = strWarnMsg;
    }
}
