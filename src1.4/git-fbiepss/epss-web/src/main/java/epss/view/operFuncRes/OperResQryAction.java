package epss.view.operFuncRes;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.Oper;
import epss.repository.model.model_show.OperResShow;
import epss.service.DeptOperService;
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
public class OperResQryAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperResQryAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;

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
                new SelectItem(EnumFlowStatus.FLOW_STATUS0.getCode(), EnumFlowStatus.FLOW_STATUS0.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS1.getCode(), EnumFlowStatus.FLOW_STATUS1.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS2.getCode(), EnumFlowStatus.FLOW_STATUS2.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS3.getCode(), EnumFlowStatus.FLOW_STATUS3.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS4.getCode(), EnumFlowStatus.FLOW_STATUS4.getTitle()));
        taskFunctionList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS5.getCode(), EnumFlowStatus.FLOW_STATUS5.getTitle()));
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
            if (!("".equals(ToolUtil.getStrIgnoreNull(operResShowQry.getInfoName()).trim()))) {
                operResShowQry.setInfoName("%"+operResShowQry.getInfoName()+"%");
            }
            String strOperIdTemp=ToolUtil.getStrIgnoreNull(operResShowQry.getOperId()).trim();
            if (!("".equals(strOperIdTemp))) {
                List<Oper> operListTemp=deptOperService.getOperListByOperId(strOperIdTemp);
                if(operListTemp.size()>0) {
                    operResShowQry.setOperPkid(operListTemp.get(0).getPkid());
                }
            }
            List<OperResShow> operResShowQryTempList = operResService.selectOperaResRecordsByModelShow(operResShowQry);
            if (!(operResShowQryTempList.size() > 0)) {
                MessageUtil.addInfo("没有查询到数据。");
            } else {
                for (int i = 0; i < operResShowQryTempList.size(); i++) {
                    if(ToolUtil.getStrIgnoreNull(operResShowQryTempList.get(i).getFlowStatus()).length()>0) {
                        operResShowQryTempList.get(i).setFlowStatusName(
                                EnumFlowStatus.getValueByKey(operResShowQryTempList.get(i).getFlowStatus()).getTitle()
                        );
                    }
                    if(ToolUtil.getStrIgnoreNull(operResShowQryTempList.get(i).getInfoType()).length()>0) {
                        operResShowQryTempList.get(i).setInfoName(
                                "(" + EnumResType.getValueByKey(operResShowQryTempList.get(i).getInfoType()).getTitle() + ")-"
                                        + operResShowQryTempList.get(i).getInfoName()
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

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

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
