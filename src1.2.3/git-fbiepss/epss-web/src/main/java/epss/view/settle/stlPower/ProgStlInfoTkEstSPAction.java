package epss.view.settle.stlPower;

import epss.common.enums.*;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.service.*;
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
import java.util.ArrayList;
import java.util.List;
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
public class ProgStlInfoTkEstSPAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoTkEstSPAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemTkEstService}")
    private ProgStlItemTkEstService progStlItemTkEstService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private ProgStlInfoShow progStlInfoShowQry;
    private ProgStlInfoShow progStlInfoShowAdd;
    private ProgStlInfoShow progStlInfoShowUpd;
    private ProgStlInfoShow progStlInfoShowDel;

    private List<ProgStlInfoShow> progStlInfoShowList;

    private OperRes operRes;

    @PostConstruct
    public void init() {
        progStlInfoShowList = new ArrayList<>();
        progStlInfoShowQry = new ProgStlInfoShow();
        progStlInfoShowAdd = new ProgStlInfoShow();
        progStlInfoShowUpd = new ProgStlInfoShow();
        progStlInfoShowDel = new ProgStlInfoShow();

        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String strResPkidTemp = parammap.get("strResPkid").toString();
        operRes=operResService.getOperResByPkid(strResPkidTemp);
    }
    public void setMaxNoPlusOne(String strQryTypePara) {
        try {
            Integer intTemp;
            String strMaxId = progStlInfoService.getStrMaxStlId(operRes.getInfoType());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "STLEst" + ToolUtil.getStrToday() + "001";
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
                progStlInfoShowQry.setId(strMaxId);
            }else if (strQryTypePara.equals("Add")) {
                progStlInfoShowAdd.setId(strMaxId);
            }else if (strQryTypePara.equals("Upd")) {
                progStlInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
        }
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            progStlInfoShowList =
                    progStlInfoService.getInitStlShowListByInfoTypePkid(
                            operRes.getInfoType(), operRes.getInfoPkid());
            if (strQryMsgOutPara.equals("true")) {
                if (progStlInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public void selectRecordAction(String strSubmitTypePara,
                                     ProgStlInfoShow progStlInfoShowPara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                progStlInfoShowAdd = new ProgStlInfoShow();
                progStlInfoShowAdd.setStlType(operRes.getInfoType());
                progStlInfoShowAdd.setStlPkid(operRes.getInfoPkid());
                progStlInfoShowAdd.setStlName(cttInfoService.getCttInfoByPkId(operRes.getInfoPkid()).getName());
            }else {
                if (strSubmitTypePara.equals("Upd")) {
                    progStlInfoShowUpd = (ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
                } else if (strSubmitTypePara.equals("Del")) {
                    progStlInfoShowDel = (ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(ProgStlInfoShow progStlInfoShow) {
        if (StringUtils.isEmpty(progStlInfoShow.getId())) {
            MessageUtil.addError("请输入结算编号！");
            return false;
        }else if (StringUtils.isEmpty(progStlInfoShow.getStlPkid())) {
            MessageUtil.addError("请输入总包合同！");
            return false;
        }else if (StringUtils.isEmpty(progStlInfoShow.getPeriodNo())){
            MessageUtil.addError("请输入期数编码！");
            return false;
        }
        return true;
    }

    /**
     * 提交维护权限
     */
    public void onClickForMngAction(String strSubmitType) {
        if (strSubmitType.equals("Add")) {
            if (!submitPreCheck(progStlInfoShowAdd)) {
                return;
            }
            List<ProgStlInfo> progStlInfoListTemp =
                    progStlInfoService.getInitStlListByModelShow(progStlInfoShowAdd);
            if(progStlInfoListTemp.size()>0) {
                MessageUtil.addError("该记录已存在，请重新录入！");
                return;
            }
            String strTemp = progStlInfoService.progStlInfoMngPreCheck(progStlInfoShowAdd);
            if (!"".equals(strTemp)) {
                MessageUtil.addError(strTemp);
                return;
            }else{
                progStlInfoService.addTkStlEstInfoAndItemInitDataAction(progStlInfoShowAdd);
                if(!EnumTaskDoneFlag.TASK_DONE_FLAG1.getCode().equals(operRes.getTaskdoneFlag())){
                    operRes.setTaskdoneFlag(EnumTaskDoneFlag.TASK_DONE_FLAG1.getCode());
                    operResService.updateRecord(operRes);
                }
                MessageUtil.addInfo("新增数据完成。");
            }
        } else if (strSubmitType.equals("Upd")) {
            progStlInfoService.updateRecord(progStlInfoShowUpd);
            MessageUtil.addInfo("更新数据完成。");
        } else if (strSubmitType.equals("Del")) {
            progStlInfoService.delTkEstStlInfoAndItem(progStlInfoShowDel);
            MessageUtil.addInfo("删除数据完成。");
        }
        onQueryAction("false");
    }

    /*智能字段Start*/
    public ProgStlItemTkEstService getProgStlItemTkEstService() {
        return progStlItemTkEstService;
    }

    public void setProgStlItemTkEstService(ProgStlItemTkEstService progStlItemTkEstService) {
        this.progStlItemTkEstService = progStlItemTkEstService;
    }

    public List<ProgStlInfoShow> getProgStlInfoShowList() {
        return progStlInfoShowList;
    }

    public void setProgStlInfoShowList(List<ProgStlInfoShow> progStlInfoShowList) {
        this.progStlInfoShowList = progStlInfoShowList;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public ProgStlInfoShow getProgStlInfoShowQry() {
        return progStlInfoShowQry;
    }

    public void setProgStlInfoShowQry(ProgStlInfoShow progStlInfoShowQry) {
        this.progStlInfoShowQry = progStlInfoShowQry;
    }

    public ProgStlInfoShow getProgStlInfoShowAdd() {
        return progStlInfoShowAdd;
    }

    public void setProgStlInfoShowAdd(ProgStlInfoShow progStlInfoShowAdd) {
        this.progStlInfoShowAdd = progStlInfoShowAdd;
    }

    public ProgStlInfoShow getProgStlInfoShowUpd() {
        return progStlInfoShowUpd;
    }

    public void setProgStlInfoShowUpd(ProgStlInfoShow progStlInfoShowUpd) {
        this.progStlInfoShowUpd = progStlInfoShowUpd;
    }
    public ProgStlInfoShow getProgStlInfoShowDel() {
        return progStlInfoShowDel;
    }

    public void setProgStlInfoShowDel(ProgStlInfoShow progStlInfoShowDel) {
        this.progStlInfoShowDel = progStlInfoShowDel;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
    /*智能字段End*/
}
