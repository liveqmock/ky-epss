package epss.view.settle.subByPower;

import epss.common.enums.ESEnum;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsInitStl;
import epss.repository.model.model_show.ProgInfoShow;
import epss.service.CttInfoService;
import epss.service.ProgStlInfoService;
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
public class ProgMeaInfoTaskAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMeaInfoTaskAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;

    private ProgInfoShow progInfoShowUpd;

    private String strStlType;
    private String strStlInfoPkid;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strStlInfoPkid")) {
            strStlInfoPkid = parammap.get("strStlInfoPkid").toString();
        }
        strStlType = ESEnum.ITEMTYPE7.getCode();

        progInfoShowUpd = new ProgInfoShow();
        initData();
    }

    public void initData() {
        EsInitStl esInitStlTemp = progStlInfoService.selectRecordsByPrimaryKey(strStlInfoPkid);
        progInfoShowUpd=progStlInfoService.fromModelToModelShow(esInitStlTemp);
        EsCttInfo esCttInfoTemp=cttInfoService.getCttInfoByPkId(progInfoShowUpd.getStlPkid());
        progInfoShowUpd.setStlName(esCttInfoTemp.getName());
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = progStlInfoService.getStrMaxStlId(strStlType);
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "STLMea" + ToolUtil.getStrToday() + "001";
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
            progInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(ProgInfoShow progInfoShow) {
        if (StringUtils.isEmpty(progInfoShow.getId())) {
            MessageUtil.addError("����������ţ�");
            return false;
        } else if (StringUtils.isEmpty(progInfoShow.getStlPkid())) {
            MessageUtil.addError("������ְ���ͬ��");
            return false;
        }else if (StringUtils.isEmpty(progInfoShow.getPeriodNo())){
            MessageUtil.addError("�������������룡");
            return false;
        }
        return true;
    }

    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction() {
        try {
            if (!submitPreCheck(progInfoShowUpd)) {
                return;
            }
            progStlInfoService.updateRecord(progInfoShowUpd);
            initData();
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ�Start*/
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public ProgInfoShow getProgInfoShowUpd() {
        return progInfoShowUpd;
    }

    public void setProgInfoShowUpd(ProgInfoShow progInfoShowUpd) {
        this.progInfoShowUpd = progInfoShowUpd;
    }
    /*�����ֶ�End*/
}
