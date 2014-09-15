package epss.service;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.common.enums.EnumTaskDoneFlag;
import epss.repository.dao.not_mybatis.MyTaskMapper;
import epss.repository.model.model_show.TaskShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: 上午8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaskService {
    @Autowired
    private MyTaskMapper myTaskMapper;

    public List<TaskShow> getTaskFlowGroup() {
        return myTaskMapper.getTaskFlowGroup();
    }

    public List<TaskShow> getOwnTaskFlowGroup(String strOperPkidPara) {
        return myTaskMapper.getOwnTaskFlowGroup(strOperPkidPara);
    }

    public List<TaskShow> getDetailTodoTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getDetailTodoTaskShowList(strOperPkidPara);
    }
    public List<TaskShow> getDetailDoneTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getDetailDoneTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> getRencentlyPowerDetailTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getRencentlyPowerDetailTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> initRecentlyPowerTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager获取相应权限下菜单列表
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getRencentlyPowerDetailTaskShowList(strOperIdTemp);
        TaskShow taskShowTemp=new TaskShow();
        taskShowTemp.setOperResFlowStatusName("待录入(" + detailTaskShowListTemp.size() + ")");
        taskShowList.add(taskShowTemp);
        for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
            //分包价格结算的生成:分包数量结算和分包材料结算均复核后自动生成分包价格结算单
            if(EnumResType.RES_TYPE5.getCode().equals(detailTaskShowUnit.getType())){
                continue;
            }
            detailTaskShowUnit.setId(
                    "(" + EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")" + detailTaskShowUnit.getId());
            detailTaskShowUnit.setFlowStatusName("已授权");
            detailTaskShowUnit.setTaskDoneFlagName(
                    EnumTaskDoneFlag.getValueByKey(detailTaskShowUnit.getTaskDoneFlag()).getTitle());
            taskShowList.add(detailTaskShowUnit);
        }
        return taskShowList;
    }
    public List<TaskShow> initTodoTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager获取相应权限下菜单列表
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();
        // 以合同类型和状态为分组,取得各组的数量
        List<TaskShow> ownTaskFlowGroupListTemp = getOwnTaskFlowGroup(strOperIdTemp);
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getDetailTodoTaskShowList(strOperIdTemp);
        for (TaskShow taskShowGroupUnit : ownTaskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    EnumFlowStatus.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(taskShowGroupUnit.getFlowStatus())){
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if(detailTaskShowUnit.getFlowStatus()==null) {
                        intHasRecordCount++;
                        detailTaskShowUnit.setId(
                                "("+ EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle()+")"+
                                        ToolUtil.getStrIgnoreNull(detailTaskShowUnit.getId()));
                        if(detailTaskShowUnit.getFlowStatusReason()==null){
                            detailTaskShowUnit.setFlowStatusReasonName(null);
                        }else {
                            detailTaskShowUnit.setFlowStatusReasonName(
                                    EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                        }
                        taskShowList.add(detailTaskShowUnit);
                        if (detailTaskShowUnit.getFlowStatusReason()==null) {
                            detailTaskShowUnit.setStrColorType("1");
                        }else{
                            detailTaskShowUnit.setStrColorType("2");
                        }
                    }
                }
                taskShowGroupUnit.setOperResFlowStatusName(
                        taskShowGroupUnit.getOperResFlowStatusName()+"("+intHasRecordCount+")");
            }else {
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if (detailTaskShowUnit.getFlowStatus() != null) {
                        if(EnumFlowStatus.FLOW_STATUS2.getCode().equals(detailTaskShowUnit.getFlowStatus())){
                            if(EnumResType.RES_TYPE3.getCode().equals(detailTaskShowUnit.getType())||
                                    EnumResType.RES_TYPE4.getCode().equals(detailTaskShowUnit.getType())){
                                continue;
                            }
                        }
                        if (taskShowGroupUnit.getFlowStatus().compareTo(detailTaskShowUnit.getFlowStatus()) == 1) {
                            intHasRecordCount++;
                            detailTaskShowUnit.setId(
                                    "(" + EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")" +
                                            ToolUtil.getStrIgnoreNull(detailTaskShowUnit.getId()));
                            detailTaskShowUnit.setFlowStatusReasonName(
                                    EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                            taskShowList.add(detailTaskShowUnit);
                        }
                        //颜色区分
                        if ((int) (Integer.parseInt(detailTaskShowUnit.getFlowStatusReason()))
                                > 2 * (int) (Integer.parseInt(detailTaskShowUnit.getFlowStatus()))) {
                            detailTaskShowUnit.setStrColorType("2");
                        } else if (
                                (int) (Integer.parseInt(detailTaskShowUnit.getFlowStatus()) +
                                        (int) Integer.parseInt(detailTaskShowUnit.getFlowStatus())) - 1
                                        == (int) Integer.parseInt(detailTaskShowUnit.getFlowStatus())) {
                            detailTaskShowUnit.setStrColorType("1");
                        }
                    }
                }
                taskShowGroupUnit.setOperResFlowStatusName(
                        taskShowGroupUnit.getOperResFlowStatusName() + "(" + intHasRecordCount + ")");
            }
        }
        return taskShowList;
    }
    public List<TaskShow> initDoneTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        // 以合同类型和状态为分组,取得各组的数量
        List<TaskShow> taskFlowGroupListTemp = getTaskFlowGroup();
        //通过OperatorManager获取相应权限下资源列表
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();

        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getDetailDoneTaskShowList(strOperIdTemp);
        for (TaskShow taskShowGroupUnit : taskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    EnumFlowStatus.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getFlowStatus())) {
                    intHasRecordCount++;
                    detailTaskShowUnit.setId(
                            "(" + EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")" + detailTaskShowUnit.getId());
                    detailTaskShowUnit.setFlowStatusName(
                            EnumFlowStatus.getValueByKey(detailTaskShowUnit.getFlowStatus()).getTitle());
                    detailTaskShowUnit.setFlowStatusReasonName(
                            EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                    if(detailTaskShowUnit.getOperResFlowStatus().equals(detailTaskShowUnit.getFlowStatus())) {
                        detailTaskShowUnit.setIsOwnTaskFlowFlag("true");
                    }else{
                        detailTaskShowUnit.setIsOwnTaskFlowFlag("false");
                    }
                    taskShowList.add(detailTaskShowUnit);
                }
            }
            taskShowGroupUnit.setOperResFlowStatusName(
                    taskShowGroupUnit.getOperResFlowStatusName()+"("+intHasRecordCount+")");
        }
        return taskShowList;
    }
}
