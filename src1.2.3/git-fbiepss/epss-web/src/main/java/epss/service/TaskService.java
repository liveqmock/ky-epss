package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.dao.not_mybatis.MyTaskMapper;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.repository.dao.not_mybatis.PtCommonMapper;
import skyline.repository.model.Ptmenu;
import skyline.util.ToolUtil;

import java.lang.reflect.InvocationTargetException;
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
    @Autowired
    private PtCommonMapper ptCommonMapper;

    public List<TaskShow> getTaskFlowGroup(String strOperPkidPara) {
        return myTaskMapper.getTaskFlowGroup(strOperPkidPara);
    }

    public List<TaskShow> getDetailTaskShowList(String strOperPkidPara) {
        return myTaskMapper.getDetailTaskShowList(strOperPkidPara);
    }

    public List<TaskShow> initTaskShowList(String strActionType){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //通过OperatorManager获取相应权限下菜单列表
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();
        // 以合同类型和状态为分组,取得各组的数量
        List<TaskShow> taskFlowGroupListTemp = getTaskFlowGroup(strOperIdTemp);
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getDetailTaskShowList(strOperIdTemp);
        for (TaskShow taskShowGroupUnit : taskFlowGroupListTemp) {
            String strFlowStatusNameUnit = ESEnumStatusFlag.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle();
            taskShowGroupUnit.setFlowStatusName(strFlowStatusNameUnit);
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                if (taskShowGroupUnit.getFlowStatus().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())
                        &&detailTaskShowUnit.getFlowStatus()==null){
                    intHasRecordCount++;
                    String strTypeName=ESEnum.getValueByKey(detailTaskShowUnit.getType()).getTitle();
                    detailTaskShowUnit.setId("("+strTypeName+")"+detailTaskShowUnit.getId());
                    taskShowList.add(detailTaskShowUnit);
                }else
                if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getFlowStatus())){
                    intHasRecordCount++;
                    String strTypeName=ESEnum.getValueByKey(detailTaskShowUnit.getType()).getTitle();
                    detailTaskShowUnit.setId("("+strTypeName+")"+detailTaskShowUnit.getId());
                    String strDetailTaskShowUnitTemp =
                            ESEnumPreStatusFlag.getValueByKey(detailTaskShowUnit.getPreFlowStatus()).getTitle();
                    detailTaskShowUnit.setPreFlowStatusName(strDetailTaskShowUnitTemp);

                    //颜色区分
                    if (detailTaskShowUnit.getFlowStatus() != null) {
                        if (detailTaskShowUnit.getPreFlowStatus().equals("0")) {
                            detailTaskShowUnit.setStrColorType("1");
                        } else {
                            if ((int) (Integer.parseInt(detailTaskShowUnit.getPreFlowStatus()))
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
                    taskShowList.add(detailTaskShowUnit);
                }
            }
            if(intHasRecordCount>0) {
                taskShowGroupUnit.setFlowStatusName(taskShowGroupUnit.getFlowStatusName()+"("+intHasRecordCount+")");
            }
        }
        return taskShowList;
    }
}
