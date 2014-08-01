package epss.view.operRes;


import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.OperResShow;
import epss.service.CttInfoService;
import epss.service.OperResService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperResQryAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(OperResQryAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private OperResShow operResShowQry;
    private OperResShow operResShowSel;
    private List<OperResShow> operResShowQryList;
    private List<SelectItem> taskFunctionList;

    @PostConstruct
    public void init() {
        operResShowQry = new OperResShow();
        operResShowSel = new OperResShow();
        operResShowQryList = new ArrayList<OperResShow>();
        initFunc();
    }

    private void initFunc() {
        taskFunctionList = new ArrayList<SelectItem>();
        taskFunctionList.add(
                new SelectItem("", "全部"));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG0.getCode(), ESEnumStatusFlag.STATUS_FLAG0.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG1.getCode(), ESEnumStatusFlag.STATUS_FLAG1.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG2.getCode(), ESEnumStatusFlag.STATUS_FLAG2.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG3.getCode(), ESEnumStatusFlag.STATUS_FLAG3.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG4.getCode(), ESEnumStatusFlag.STATUS_FLAG4.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG5.getCode(), ESEnumStatusFlag.STATUS_FLAG5.getTitle()));
    }

    public void onQueryAction() {
        try {
            operResShowQryList.clear();
            List<OperResShow> operResShowQryTempList = new ArrayList<OperResShow>();
            if (!("".equals(ToolUtil.getStrIgnoreNull(operResShowQry.getInfoPkidName()).trim()))) {
                operResShowQry.setInfoPkidName("%"+operResShowQry.getInfoPkidName()+"%");
            }
            operResShowQryTempList = operResService.selectOperaResRecordsByModelShow(operResShowQry);
            if (!(operResShowQryTempList.size() > 0)) {
                MessageUtil.addInfo("没有查询到数据。");
            } else {
                String strStatusFlag = null;
                String strInfoType = null;
                for (int i = 0; i < operResShowQryTempList.size(); i++) {
                    strStatusFlag = operResShowQryTempList.get(i).getFlowStatus();
                    strInfoType = operResShowQryTempList.get(i).getInfoType();
                    if (ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(strStatusFlag)) {
                        operResShowQryTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG0.getTitle());
                    } else if (ESEnumStatusFlag.STATUS_FLAG1.getCode().equals(strStatusFlag)) {
                        operResShowQryTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG1.getTitle());
                    } else if (ESEnumStatusFlag.STATUS_FLAG2.getCode().equals(strStatusFlag)) {
                        operResShowQryTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG2.getTitle());
                    } else if (ESEnumStatusFlag.STATUS_FLAG3.getCode().equals(strStatusFlag)) {
                        operResShowQryTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG3.getTitle());
                    } else if (ESEnumStatusFlag.STATUS_FLAG4.getCode().equals(strStatusFlag)) {
                        operResShowQryTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG4.getTitle());
                    } else if (ESEnumStatusFlag.STATUS_FLAG5.getCode().equals(strStatusFlag)) {
                        operResShowQryTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG5.getTitle());
                    }
                    if (ESEnum.ITEMTYPE0.getCode().equals(strInfoType)){
                        operResShowQryTempList.get(i).setInfoPkidName(
                                "("+ESEnum.ITEMTYPE0.getTitle()+")-"+operResShowQryTempList.get(i).getInfoPkidName()
                        );
                    }else if (ESEnum.ITEMTYPE1.getCode().equals(strInfoType)){
                        operResShowQryTempList.get(i).setInfoPkidName(
                                "("+ESEnum.ITEMTYPE1.getTitle()+")-"+operResShowQryTempList.get(i).getInfoPkidName()
                        );
                    }else if (ESEnum.ITEMTYPE2.getCode().equals(strInfoType)){
                        operResShowQryTempList.get(i).setInfoPkidName(
                                "("+ESEnum.ITEMTYPE2.getTitle()+")-"+operResShowQryTempList.get(i).getInfoPkidName()
                        );
                    }
                    operResShowQryList.add(operResShowQryTempList.get(i));
                }
            }
        } catch (Exception e) {
            logger.error("权限追加信息查询失败", e);
            MessageUtil.addError("权限追加信息查询失败");
        }
    }

    public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSel= (OperResShow) BeanUtils.cloneBean(operResShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    /*智能字段 Start*/

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }

    public List<OperResShow> getOperResShowQryList() {
        return operResShowQryList;
    }

    public void setOperResShowQryList(List<OperResShow> operResShowQryList) {
        this.operResShowQryList = operResShowQryList;
    }

    public List<SelectItem> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<SelectItem> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
    }

    public OperResShow getOperResShowQry() {
        return operResShowQry;
    }

    public void setOperResShowQry(OperResShow operResShowQry) {
        this.operResShowQry = operResShowQry;
    }

    public OperResShow getOperResShowSel() {
        return operResShowSel;
    }

    public void setOperResShowSel(OperResShow operResShowSel) {
        this.operResShowSel = operResShowSel;
    }
}
