package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.AttachmentModel;
import epss.repository.model.model_show.CttInfoShow;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import java.io.*;
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
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private CttInfoShow cttInfoShowQry;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowUpd;
    private List<CttInfoShow> cttInfoShowList;

    private String strSubmitType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;
    //����ʱ�����ã��������̹رգ������Ͻ�������
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
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowUpd.setCttType(ESEnum.ITEMTYPE0.getCode());
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "";
        esFlowControl.getBackToStatusFlagList("Qry");
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE0.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "TKCTT" + ToolUtil.getStrToday() + "001";
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
        return cttInfoService.getCttInfoByPkId(strPkid);
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   CttInfoShow cttInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            // ��ѯ
            cttInfoShowPara.setCreatedByName(ToolUtil.getUserName(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(ToolUtil.getUserName(cttInfoShowPara.getLastUpdBy()));
            if (strPowerTypePara.equals("Qry")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            } else if (strPowerTypePara.equals("Mng")) { // ά��
                if (strSubmitTypePara.equals("Sel")) {
                    cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                }else if (strSubmitTypePara.equals("Upd")) {
                        cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
                }
            } else {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
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
        MessageUtil.addInfo("����������ɡ�");
        onQueryAction("Mng","false");
    }

    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(ESEnum.ITEMTYPE0.getCode());
            cttInfoService.updateRecord(cttInfoShowPara);
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*�����ֶ� Start*/

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
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

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
    /*�����ֶ� End*/

}
