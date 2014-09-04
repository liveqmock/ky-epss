package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.OperResShow;
import epss.service.OperResService;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
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
public class OperFuncResQryAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncResQryAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private OperResShow operResShowQry;
    private OperResShow operResShowSel;
    private List<SelectItem> taskFunctionList;
    private List<OperResShow> operResShowQryList;

    @PostConstruct
    public void init() {
        operResShowQry = new OperResShow();
        operResShowSel = new OperResShow();
        operResShowQryList = new ArrayList<>();
        initFunc();
    }

    private void initFunc(){
        taskFunctionList = new ArrayList<>();
        taskFunctionList.add(new SelectItem("","全部"));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG0.getCode(),ESEnumStatusFlag.STATUS_FLAG0.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG1.getCode(),ESEnumStatusFlag.STATUS_FLAG1.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG2.getCode(),ESEnumStatusFlag.STATUS_FLAG2.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG3.getCode(),ESEnumStatusFlag.STATUS_FLAG3.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG4.getCode(),ESEnumStatusFlag.STATUS_FLAG4.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG5.getCode(),ESEnumStatusFlag.STATUS_FLAG5.getTitle()));
    }

    public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSel= (OperResShow) BeanUtils.cloneBean(operResShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void onQueryAction() {
        try {
            operResShowQryList.clear();
            if (!("".equals(ToolUtil.getStrIgnoreNull(operResShowQry.getInfoPkidName()).trim()))) {
                operResShowQry.setInfoPkidName("%"+operResShowQry.getInfoPkidName()+"%");
            }
            List<OperResShow> operResShowQryTempList = operResService.selectOperaResRecordsByModelShow(operResShowQry);
            if (!(operResShowQryTempList.size() > 0)) {
                MessageUtil.addInfo("没有查询到数据。");
            } else {
                for (int i = 0; i < operResShowQryTempList.size(); i++) {
                    if(ToolUtil.getStrIgnoreNull(operResShowQryTempList.get(i).getFlowStatus()).length()>0) {
                        operResShowQryTempList.get(i).setFlowStatusName(
                                ESEnumStatusFlag.getValueByKey(operResShowQryTempList.get(i).getFlowStatus()).getTitle()
                        );
                    }
                    if(ToolUtil.getStrIgnoreNull(operResShowQryTempList.get(i).getInfoType()).length()>0) {
                        operResShowQryTempList.get(i).setInfoPkidName(
                                "(" + ESEnum.getValueByKey(operResShowQryTempList.get(i).getInfoType()).getTitle() + ")-"
                                        + operResShowQryTempList.get(i).getInfoPkidName()
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

    /*智能字段 Start*/
    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
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

    public List<OperResShow> getOperResShowQryList() {
        return operResShowQryList;
    }

    public void setOperResShowQryList(List<OperResShow> operResShowQryList) {
        this.operResShowQryList = operResShowQryList;
    }
}
