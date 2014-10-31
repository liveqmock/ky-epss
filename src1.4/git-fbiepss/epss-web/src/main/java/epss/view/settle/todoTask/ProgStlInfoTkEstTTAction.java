package epss.view.settle.todoTask;

import epss.common.enums.EnumResType;
import epss.repository.model.CttInfo;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.model_show.ProgStlInfoShow;
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
public class ProgStlInfoTkEstTTAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoTkEstTTAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;

    private ProgStlInfoShow progStlInfoShowUpd;

    private String strStlType;
    private String strStlInfoPkid;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strStlInfoPkid")) {
            strStlInfoPkid = parammap.get("strStlInfoPkid").toString();
        }
        strStlType = EnumResType.RES_TYPE6.getCode();

        progStlInfoShowUpd = new ProgStlInfoShow();
        initData();
    }

    public void initData() {
        ProgStlInfo progStlInfoTemp = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
        progStlInfoShowUpd =progStlInfoService.fromModelToModelShow(progStlInfoTemp);
        CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId(progStlInfoShowUpd.getStlPkid());
        progStlInfoShowUpd.setStlName(cttInfoTemp.getName());
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = progStlInfoService.getStrMaxStlId(strStlType);
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "STLSta" + ToolUtil.getStrToday() + "001";
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
            progStlInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(ProgStlInfoShow progStlInfoShow) {
        if (StringUtils.isEmpty(progStlInfoShow.getId())) {
            MessageUtil.addError("����������ţ�");
            return false;
        } else if (StringUtils.isEmpty(progStlInfoShow.getStlPkid())) {
            MessageUtil.addError("������ְ���ͬ��");
            return false;
        }else if (StringUtils.isEmpty(progStlInfoShow.getPeriodNo())){
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
            if (!submitPreCheck(progStlInfoShowUpd)) {
                return;
            }
            progStlInfoService.updateRecord(progStlInfoShowUpd);
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

    public ProgStlInfoShow getProgStlInfoShowUpd() {
        return progStlInfoShowUpd;
    }

    public void setProgStlInfoShowUpd(ProgStlInfoShow progStlInfoShowUpd) {
        this.progStlInfoShowUpd = progStlInfoShowUpd;
    }
    /*�����ֶ�End*/
}
