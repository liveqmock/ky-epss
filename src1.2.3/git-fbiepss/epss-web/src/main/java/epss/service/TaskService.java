package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
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
 * Time: ����8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaskService {
    @Autowired
    private MyTaskMapper myTaskMapper;

    public List<TaskShow> getTaskFlowGroup() {
        return myTaskMapper.getTaskFlowGroup();
    }

    public List<TaskShow> getOwnRencentlyPowerTaskFlowGroup(String strOperPkidPara) {
        return myTaskMapper.getOwnRencentlyPowerTaskFlowGroup(strOperPkidPara);
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
        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();
        // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
        List<TaskShow> ownTaskFlowGroupListTemp = getOwnRencentlyPowerTaskFlowGroup(strOperIdTemp);
        // �����ϸ�����б�
        List<TaskShow> detailTaskShowListTemp = getRencentlyPowerDetailTaskShowList(strOperIdTemp);
        for (TaskShow taskShowGroupUnit : ownTaskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    ESEnumStatusFlag.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                intHasRecordCount++;
                detailTaskShowUnit.setId(
                        "("+ESEnum.getValueByKey(detailTaskShowUnit.getType()).getTitle()+")"+detailTaskShowUnit.getId());
                detailTaskShowUnit.setFlowStatusName("����Ȩ");
                taskShowList.add(detailTaskShowUnit);
            }
            taskShowGroupUnit.setOperResFlowStatusName("��¼��("+intHasRecordCount+")");
        }
        return taskShowList;
    }
    public List<TaskShow> initTodoTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        //ͨ��OperatorManager��ȡ��ӦȨ���²˵��б�
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();
        // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
        List<TaskShow> ownTaskFlowGroupListTemp = getOwnTaskFlowGroup(strOperIdTemp);
        // �����ϸ�����б�
        List<TaskShow> detailTaskShowListTemp = getDetailTodoTaskShowList(strOperIdTemp);
        for (TaskShow taskShowGroupUnit : ownTaskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    ESEnumStatusFlag.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getOperResFlowStatus())){
                    if (taskShowGroupUnit.getFlowStatus().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())
                            &&detailTaskShowUnit.getFlowStatus()==null){
                        intHasRecordCount++;
                        detailTaskShowUnit.setId(
                                "("+ESEnum.getValueByKey(detailTaskShowUnit.getType()).getTitle()+")"+detailTaskShowUnit.getId());
                        taskShowList.add(detailTaskShowUnit);
                        continue;
                    }
                    if (detailTaskShowUnit.getFlowStatus() != null) {
                        if (taskShowGroupUnit.getFlowStatus().compareTo(detailTaskShowUnit.getFlowStatus())==1){
                            intHasRecordCount++;
                            detailTaskShowUnit.setId(
                                    "("+ESEnum.getValueByKey(detailTaskShowUnit.getType()).getTitle()+")"+detailTaskShowUnit.getId());
                            detailTaskShowUnit.setFlowStatusName(
                                    ESEnumStatusFlag.getValueByKey(detailTaskShowUnit.getFlowStatus()).getTitle());
                            detailTaskShowUnit.setFlowStatusReasonName(
                                    ESEnumPreStatusFlag.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                            taskShowList.add(detailTaskShowUnit);
                        }
                        //��ɫ����
                        if (detailTaskShowUnit.getFlowStatusReason().equals("0")) {
                            detailTaskShowUnit.setStrColorType("1");
                        } else {
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
                }
            }
            taskShowGroupUnit.setOperResFlowStatusName(
                    taskShowGroupUnit.getOperResFlowStatusName()+"("+intHasRecordCount+")");
        }
        return taskShowList;
    }
    public List<TaskShow> initDoneTaskShowList(){
        List<TaskShow> taskShowList = new ArrayList<TaskShow>();
        // �Ժ�ͬ���ͺ�״̬Ϊ����,ȡ�ø��������
        List<TaskShow> taskFlowGroupListTemp = getTaskFlowGroup();
        //ͨ��OperatorManager��ȡ��ӦȨ������Դ�б�
        String strOperIdTemp = ToolUtil.getOperatorManager().getOperatorId();

        // �����ϸ�����б�
        List<TaskShow> detailTaskShowListTemp = getDetailDoneTaskShowList(strOperIdTemp);
        for (TaskShow taskShowGroupUnit : taskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    ESEnumStatusFlag.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getFlowStatus())) {
                    intHasRecordCount++;
                    detailTaskShowUnit.setId(
                            "(" + ESEnum.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")" + detailTaskShowUnit.getId());
                    detailTaskShowUnit.setFlowStatusName(
                            ESEnumStatusFlag.getValueByKey(detailTaskShowUnit.getFlowStatus()).getTitle());
                    detailTaskShowUnit.setFlowStatusReasonName(
                            ESEnumPreStatusFlag.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
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
