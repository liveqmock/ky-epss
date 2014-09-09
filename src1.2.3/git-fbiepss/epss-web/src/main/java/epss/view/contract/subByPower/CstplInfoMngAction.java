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
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class CstplInfoMngAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplInfoMngAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private CttInfoShow cttInfoShowUpd;
    private String strCstplInfoPkid;

    @PostConstruct
    public void init() {
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // 从总包合同传递过来的总包合同号
        if (parammap.containsKey("strCstplInfoPkid")) {
            strCstplInfoPkid = parammap.get("strCstplInfoPkid").toString();
        }
        initData();
    }
    public void initData() {
        EsCttInfo esCttInfoTemp = cttInfoService.getCttInfoByPkId(strCstplInfoPkid);
        cttInfoShowUpd=cttInfoService.fromModelToModelShow(esCttInfoTemp);
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(ESEnum.ITEMTYPE1.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "CSTPL" + ToolUtil.getStrToday() + "001";
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
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
        }
    }

    public void selectRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            // 查询
            cttInfoShowPara.setCreatedByName(ToolUtil.getUserName(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(ToolUtil.getUserName(cttInfoShowPara.getLastUpdBy()));
            cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if (StringUtils.isEmpty(cttInfoShowPara.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        return true;
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        if (!submitPreCheck(cttInfoShowUpd)) {
         return;
        }
        updRecordAction(cttInfoShowUpd);
        MessageUtil.addInfo("更新数据完成。");
        initData();
    }

    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoService.updateRecord(cttInfoShowPara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*智能字段 Start*/

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
    /*智能字段 End*/
}
