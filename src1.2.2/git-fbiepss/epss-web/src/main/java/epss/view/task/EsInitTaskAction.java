package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.ToolUtil;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import epss.service.FlowCtrlService;
import epss.service.OperResService;
import epss.service.TaskService;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;
import epss.view.flow.EsFlowControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class EsInitTaskAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitTaskAction.class);
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{platformService}")
    private PlatformService platformService;
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        //整个任务列表
        this.taskShowList = new ArrayList<TaskShow>();
        //合同类型
        String strType;
        //合同状态
        String strStatusFlag;

        OperResShow operResShowTemp=new OperResShow();
        List<OperResShow> operResShowList=
                operResService.selectOperaResRecordsByModelShow(operResShowTemp);

        //通过OperatorManager获取相应权限下菜单列表
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        // 以合同类型和状态为分组,取得各组的数量
        List<TaskShow> taskCountsInFlowGroupListTemp =taskService.getTaskCountsInFlowGroup();
        // 获得详细任务列表
        List<TaskShow> taskShowListTemp=taskService.getTaskShowList();
        //遍历菜单
        for(Ptmenu itemUnit:ptmenuList){
            //如果菜单为空或者地址中不含有总包合同和成本计划的话，继续循环查找
            if(itemUnit.getMenuaction()==null||itemUnit.getTargetmachine().toLowerCase().contains("system")){
                continue;
            }
            //初始化合同类型
            strType="";
            //根据合同的类型，给变量strType赋上相应的值
            if(itemUnit.getMenudesc().contains("0")) {
                strType= ESEnum.ITEMTYPE0.getCode();
            }else if(itemUnit.getMenudesc().contains("1")) {
                strType= ESEnum.ITEMTYPE1.getCode();
            }else if(itemUnit.getMenudesc().contains("2")) {
                strType= ESEnum.ITEMTYPE2.getCode();
            }else if(itemUnit.getMenudesc().contains("3")) {
                strType= ESEnum.ITEMTYPE3.getCode();
            }else if(itemUnit.getMenudesc().contains("4")) {
                strType= ESEnum.ITEMTYPE4.getCode();
            }else if(itemUnit.getMenudesc().contains("5")) {
                strType= ESEnum.ITEMTYPE5.getCode();
            }else if(itemUnit.getMenudesc().contains("6")) {
                strType= ESEnum.ITEMTYPE6.getCode();
            }else if(itemUnit.getMenudesc().contains("7")) {
                strType= ESEnum.ITEMTYPE7.getCode();
            }
            //初始化合同类型
            strStatusFlag="";
            //根据合同状态的类型，给变量strStatusFlag赋上相应的值
            if(itemUnit.getMenudesc().contains("*A")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG0.getCode();
            }else if(itemUnit.getMenudesc().contains("*B")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG1.getCode();
            }else if(itemUnit.getMenudesc().contains("*C")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG2.getCode();
            }else if(itemUnit.getMenudesc().contains("*D")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG3.getCode();
            }else if(itemUnit.getMenudesc().contains("*E")) {
                strStatusFlag= ESEnumStatusFlag.STATUS_FLAG4.getCode();
            }
            //实例化变量 taskShow
            TaskShow taskShow =new TaskShow();
            //给变量taskShow的属性Pkid赋值
            taskShow.setPkid(itemUnit.getMenuid());
            //创建并初始化判断合同是否是特定类型和状态的布尔变量
            Boolean isHasTask;

            //遍历存放“合同类型+状态+数量”信息的集合
            for(TaskShow itemUnitCM: taskCountsInFlowGroupListTemp){
                String strStatusFlagInTaskCountsInFlowGroupList=
                        ToolUtil.getStrIgnoreNull(itemUnitCM.getStatusFlag());
                //任务列表type是排好序的（升序），大于比较的菜单type就不用再比较了
                if(ToolUtil.getStrIgnoreNull(itemUnitCM.getType()).compareTo(strType)>0){
                    break;
                } //当任务类别和合同类型一致的时候
                if(itemUnitCM.getType().equals(strType)){
                    //保证越级的不会出现
                    isHasTask=false;
                    //当合同状态为初始时，其是"录入"中的待处理任务
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals("")){
                            isHasTask=true;//给隐藏类的表头赋值
                        } //当合同状态为审核时，并且任务状态为审核的时，其是“复核”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;//给隐藏类的表头赋值
                        } //当合同状态为复核时，并且任务状态为审核的时，其是“复核”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }//当合同状态为批准时，并且任务状态为复核的时，其是“批准”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }//当合同状态为记账时，并且任务状态为批准的时，其是“记账”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(strStatusFlagInTaskCountsInFlowGroupList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    } //当任务为待处理任务时，统计待处理任务中的任务个数
                    if(isHasTask.equals(true)){
                        //权限筛选
                        Boolean hasPower=false;
                        /*for(OperResShow operResShowUnit:operResShowList){
                            if(operResShowUnit.getInfoType().equals(strType)){
                                hasPower=true;
                                break;
                            }
                        }*/
                        hasPower=true;//暂时用
                        if(hasPower.equals(true)){
                            // 追加标题行
                            taskShow.setName(itemUnit.getMenulabel()+"("+itemUnitCM.getRecordsCountInGroup()+")");
                            taskShow.setPkid("");
                            taskShowList.add(taskShow);
                        }else{
                            continue;
                        }

                        //当合同状态为进入流程时
                        //流程（非‘录入未完’）信息
                        for(TaskShow itemUnitEP:taskShowListTemp){
                            String strStatusFlagInTaskShowList=ToolUtil.getStrIgnoreNull(itemUnitEP.getStatusFlag());
                            if(itemUnitEP.getType().equals(strType)){
                                isHasTask=false;

                                /*//权限筛选
                                hasPower=false;
                                for(OperResShow operResShowUnit:operResShowList){
                                    if(operResShowUnit.getInfoType().equals(strType)&&
                                            operResShowUnit.getInfoPkid().equals(itemUnitEP.getPkid())){
                                        hasPower=true;
                                        break;
                                    }
                                }
                                if(hasPower.equals(false)){
                                    continue;
                                }*/

                                if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                                    if(strStatusFlagInTaskShowList.equals("")){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                                        isHasTask=true;
                                    }
                                }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                                    if(strStatusFlagInTaskShowList.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                                        isHasTask=true;
                                    }
                                }

                                //如果其是待处理任务，在显示列表上添加隐藏列和任务列的具体信息
                                if(isHasTask.equals(true)){
                                    taskShow =new TaskShow();
                                    taskShow.setType(itemUnitEP.getType());
                                    taskShow.setPkid(itemUnitEP.getPkid());
                                    taskShow.setId(itemUnitEP.getId());
                                    taskShow.setName(itemUnitEP.getName());
                                    taskShow.setPeriodNo(itemUnitEP.getPeriodNo());
                                    taskShow.setStatusFlag(strStatusFlagInTaskShowList);
                                    if(!strStatusFlagInTaskShowList.equals("")) {
                                        taskShow.setStatusFlagName(
                                        ESEnumStatusFlag.getValueByKey(strStatusFlagInTaskShowList).getTitle());
                                    }
                                    taskShow.setPreStatusFlag(itemUnitEP.getPreStatusFlag());
                                    if(itemUnitEP.getPreStatusFlag()!=null) {
                                        taskShow.setPreStatusFlagName(
                                        ESEnumPreStatusFlag.getValueByKey(itemUnitEP.getPreStatusFlag()).getTitle());
                                        //颜色区分
                                        if (taskShow.getStatusFlag()!=null){
                                            if (taskShow.getPreStatusFlag().equals("0")){
                                                taskShow.setPreStatusFlagType("1");
                                            }else {
                                                if ((int)(Integer.parseInt(itemUnitEP.getPreStatusFlag()))
                                                        >2*(int)(Integer.parseInt(itemUnitEP.getStatusFlag()))){
                                                    taskShow.setPreStatusFlagType("2");
                                                }else if ((int)(Integer.parseInt(itemUnitEP.getStatusFlag())+(int)Integer.parseInt(itemUnitEP.getStatusFlag()))-1
                                                        ==(int)Integer.parseInt(itemUnitEP.getStatusFlag())){
                                                    taskShow.setPreStatusFlagType("1");
                                                }
                                            }
                                        }else {
                                            taskShow.setPreStatusFlagType("2");
                                        }
                                    }
                                    taskShowList.add(taskShow);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public PlatformService getPlatformService() {
        return platformService;
    }

    public void setPlatformService(PlatformService platformService) {
        this.platformService = platformService;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public List<TaskShow> getTaskShowList() {
        return taskShowList;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
}