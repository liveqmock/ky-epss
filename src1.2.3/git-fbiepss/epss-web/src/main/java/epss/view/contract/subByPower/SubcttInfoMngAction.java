package epss.view.contract.subByPower;

import epss.common.enums.ESEnum;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.CttInfoShow;
import epss.service.CttInfoService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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
public class SubcttInfoMngAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttInfoMngAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private CttInfoShow cttInfoShowUpd;
    private String strSubcttInfoPkid;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        if (parammap.containsKey("strSubcttInfoPkid")) {
            strSubcttInfoPkid = parammap.get("strSubcttInfoPkid").toString();
        }

        cttInfoShowUpd = new CttInfoShow();
        initData();
    }
    public void initData() {
        EsCttInfo esCttInfoTemp = cttInfoService.getCttInfoByPkId(strSubcttInfoPkid);
        cttInfoShowUpd=cttInfoService.fromModelToModelShow(esCttInfoTemp);
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE2.getCode());
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
            cttInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            // ��ѯ
            cttInfoShowPara.setCreatedByName(ToolUtil.getUserName(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(ToolUtil.getUserName(cttInfoShowPara.getLastUpdBy()));
            cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
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
        initData();
    }

    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
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

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }
    /*�����ֶ� End*/
}