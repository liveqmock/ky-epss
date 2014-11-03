package epss.service;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.common.enums.EnumTaskDoneFlag;
import epss.repository.dao.CttInfoMapper;
import epss.repository.dao.ProgStlInfoMapper;
import epss.repository.dao.SignPartMapper;
import epss.repository.dao.not_mybatis.MyTaskMapper;
import epss.repository.model.CttInfo;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.SignPart;
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
    private CttInfoMapper cttInfoMapper;
    @Autowired
    private SignPartMapper signPartMapper;
    @Autowired
    private ProgStlInfoMapper progStlInfoMapper;
    @Autowired
    private MyTaskMapper myTaskMapper;
    @Autowired
    private ProgStlInfoService progStlInfoService;

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
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getRencentlyPowerDetailTaskShowList(strOperPkidTemp);
        //添加任务甲方乙方信息
        setTaskShowListOfSignPart(detailTaskShowListTemp);

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
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();
        // 以流程状态为分组
        List<TaskShow> ownTaskFlowGroupListTemp = getOwnTaskFlowGroup(strOperPkidTemp);
        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getDetailTodoTaskShowList(strOperPkidTemp);
        //添加任务甲方乙方信息
        setTaskShowListOfSignPart(detailTaskShowListTemp);

        for (TaskShow taskShowGroupUnit : ownTaskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    EnumFlowStatus.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            // 录入权限时
            if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(taskShowGroupUnit.getFlowStatus())){
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if(taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getOperResFlowStatus())) {
                        // 资源录入启动且权限分配为录入状态
                        if (detailTaskShowUnit.getFlowStatus() == null) {
                            intHasRecordCount++;
                            detailTaskShowUnit.setId(
                                    "(" + EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")" +
                                            ToolUtil.getStrIgnoreNull(detailTaskShowUnit.getId()));
                            if (detailTaskShowUnit.getFlowStatusReason() == null) {
                                detailTaskShowUnit.setFlowStatusReasonName(null);
                            } else {
                                detailTaskShowUnit.setFlowStatusReasonName(
                                        EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                            }
                            taskShowList.add(detailTaskShowUnit);
                            if (detailTaskShowUnit.getFlowStatusReason() == null) {
                                detailTaskShowUnit.setStrColorType("1");
                            } else {
                                detailTaskShowUnit.setStrColorType("2");
                            }
                        }
                    }
                }
                taskShowGroupUnit.setOperResFlowStatusName(
                        taskShowGroupUnit.getOperResFlowStatusName()+"("+intHasRecordCount+")");
            }else {// 审复批记归
                for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                    if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getOperResFlowStatus())) {
                        if (detailTaskShowUnit.getFlowStatus() != null) {
                            if (EnumFlowStatus.FLOW_STATUS2.getCode().equals(detailTaskShowUnit.getFlowStatus())) {
                                if (EnumResType.RES_TYPE3.getCode().equals(detailTaskShowUnit.getType()) ||
                                        EnumResType.RES_TYPE4.getCode().equals(detailTaskShowUnit.getType())) {
                                    continue;
                                }
                            }
                            if ((((Integer.parseInt(taskShowGroupUnit.getFlowStatus())) - 1) + "")
                                    .equals(detailTaskShowUnit.getFlowStatus())) {
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
        String strOperPkidTemp = ToolUtil.getOperatorManager().getOperator().getPkid();

        // 获得详细任务列表
        List<TaskShow> detailTaskShowListTemp = getDetailDoneTaskShowList(strOperPkidTemp);

        //添加任务甲方乙方信息
        setTaskShowListOfSignPart(detailTaskShowListTemp);

        for (TaskShow taskShowGroupUnit : taskFlowGroupListTemp) {
            taskShowGroupUnit.setOperResFlowStatusName(
                    EnumFlowStatus.getValueByKey(taskShowGroupUnit.getFlowStatus()).getTitle());
            taskShowList.add(taskShowGroupUnit);
            int intHasRecordCount=0;
            for (TaskShow detailTaskShowUnit : detailTaskShowListTemp) {
                if (taskShowGroupUnit.getFlowStatus().equals(detailTaskShowUnit.getFlowStatus())) {
                    intHasRecordCount++;
                    detailTaskShowUnit.setId(
                            "(" + EnumResType.getValueByKey(detailTaskShowUnit.getType()).getTitle() + ")"
                                    + detailTaskShowUnit.getId());
                    detailTaskShowUnit.setFlowStatusName(
                            EnumFlowStatus.getValueByKey(detailTaskShowUnit.getFlowStatus()).getTitle());
                    detailTaskShowUnit.setFlowStatusReasonName(
                            EnumFlowStatusReason.getValueByKey(detailTaskShowUnit.getFlowStatusReason()).getTitle());
                    if(detailTaskShowUnit.getOperResFlowStatus().equals(detailTaskShowUnit.getFlowStatus())) {
                        detailTaskShowUnit.setIsOwnTaskFlowFlag("true");
                        if (EnumResType.RES_TYPE3.getCode().equals(detailTaskShowUnit.getType())
                                ||EnumResType.RES_TYPE4.getCode().equals(detailTaskShowUnit.getType())){
                            ProgStlInfo progStlInfoTemp=new ProgStlInfo();
                            progStlInfoTemp.setStlType(EnumResType.RES_TYPE5.getCode());
                            progStlInfoTemp.setStlPkid(detailTaskShowUnit.getStlPkid());
                            progStlInfoTemp.setPeriodNo(detailTaskShowUnit.getPeriodNo());
                            List<ProgStlInfo> progStlInfoListTemp= progStlInfoService.getInitStlListByModel(progStlInfoTemp);
                            if (progStlInfoListTemp.size()!=0){
                                if (EnumFlowStatus.FLOW_STATUS2.getCode().compareTo(
                                        ToolUtil.getStrIgnoreNull(progStlInfoListTemp.get(0).getFlowStatus()))<0){
                                    detailTaskShowUnit.setIsOwnTaskFlowFlag("false");
                                }
                            }
                        }
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

    private List<TaskShow> setTaskShowListOfSignPart(List<TaskShow> taskShowListPara){
        // 添加任务甲方乙方信息
        for(TaskShow taskShowUnit:taskShowListPara){
            String strTypeTemp = ToolUtil.getStrIgnoreNull(taskShowUnit.getType());
            // 总包合同，取对应的甲方信息
            if (strTypeTemp.equals(EnumResType.RES_TYPE0.getCode())) {
                CttInfo cttInfoTemp=cttInfoMapper.selectByPrimaryKey(taskShowUnit.getPkid());
                if(cttInfoTemp!=null) {
                    String strSignPartAPkidTemp = ToolUtil.getStrIgnoreNull(cttInfoTemp.getSignPartA());
                    SignPart signPartTemp = signPartMapper.selectByPrimaryKey(strSignPartAPkidTemp);
                    if (signPartTemp != null) {
                        taskShowUnit.setSignPartBName(signPartTemp.getName());
                    }
                }
            }// 成本计划，取对应的总包合同的甲方信息
            else if (strTypeTemp.equals(EnumResType.RES_TYPE1.getCode())) {
                CttInfo cttInfoTemp=cttInfoMapper.selectByPrimaryKey(taskShowUnit.getPkid());
                if(cttInfoTemp!=null) {
                    CttInfo tkCttInfoTemp = cttInfoMapper.selectByPrimaryKey(cttInfoTemp.getParentPkid());
                    String strSignPartAPkidTemp = ToolUtil.getStrIgnoreNull(tkCttInfoTemp.getSignPartA());
                    SignPart signPartTemp = signPartMapper.selectByPrimaryKey(strSignPartAPkidTemp);
                    if (signPartTemp != null) {
                        taskShowUnit.setSignPartBName(signPartTemp.getName());
                    }
                }
            }// 分包合同,取对应的乙方信息
            else if (strTypeTemp.equals(EnumResType.RES_TYPE2.getCode())) {
                CttInfo cttInfoTemp=cttInfoMapper.selectByPrimaryKey(taskShowUnit.getPkid());
                if(cttInfoTemp!=null) {
                    String strSignPartBPkidTemp = ToolUtil.getStrIgnoreNull(cttInfoTemp.getSignPartB());
                    SignPart signPartTemp = signPartMapper.selectByPrimaryKey(strSignPartBPkidTemp);
                    if (signPartTemp != null) {
                        taskShowUnit.setSignPartBName(signPartTemp.getName());
                    }
                }
            }// 分包合同及衍生的结算，取对应的分包合同的乙方信息
            else if (strTypeTemp.equals(EnumResType.RES_TYPE3.getCode())||
                      strTypeTemp.equals(EnumResType.RES_TYPE4.getCode())||
                      strTypeTemp.equals(EnumResType.RES_TYPE5.getCode()))  {
                ProgStlInfo progStlInfoTemp=progStlInfoMapper.selectByPrimaryKey(taskShowUnit.getPkid());
                if(progStlInfoTemp!=null) {
                    CttInfo cttInfoTemp = cttInfoMapper.selectByPrimaryKey(progStlInfoTemp.getStlPkid());
                    String strSignPartBPkidTemp = ToolUtil.getStrIgnoreNull(cttInfoTemp.getSignPartB());
                    SignPart signPartTemp = signPartMapper.selectByPrimaryKey(strSignPartBPkidTemp);
                    if (signPartTemp != null) {
                        taskShowUnit.setSignPartBName(signPartTemp.getName());
                    }
                }
            }// 总包合同及衍生的结算，取对应的总包合同的甲方信息
            else if (strTypeTemp.equals(EnumResType.RES_TYPE6.getCode())||
                      strTypeTemp.equals(EnumResType.RES_TYPE7.getCode()))  {
                ProgStlInfo progStlInfoTemp=progStlInfoMapper.selectByPrimaryKey(taskShowUnit.getPkid());
                if(progStlInfoTemp!=null) {
                    CttInfo cttInfoTemp = cttInfoMapper.selectByPrimaryKey(progStlInfoTemp.getStlPkid());
                    String strSignPartAPkidTemp = ToolUtil.getStrIgnoreNull(cttInfoTemp.getSignPartA());
                    SignPart signPartTemp = signPartMapper.selectByPrimaryKey(strSignPartAPkidTemp);
                    if (signPartTemp != null) {
                        taskShowUnit.setSignPartBName(signPartTemp.getName());
                    }
                }
            }
        }
        return taskShowListPara;
    }
}
