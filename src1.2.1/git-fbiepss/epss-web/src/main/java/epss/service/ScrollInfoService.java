package epss.service;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import org.springframework.stereotype.Service;
import skyline.repository.model.Ptmenu;
import skyline.service.PlatformService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class ScrollInfoService {
    @Resource
    private PlatformService platformService;
    @Resource
    private TaskService taskService;
    @Resource
    private OperResService operResService;

    private List<TaskShow> taskShowList;

    public List<TaskShow> getViewMsgFromTask(){
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
                if(itemUnit.getMenuaction()==null||
                        (!itemUnit.getMenuaction().toLowerCase().contains("cstpl")&&
                                !itemUnit.getMenuaction().toLowerCase().contains("tkctt"))){
                    continue;
                }
                //初始化合同类型
                strType="";
                //根据合同的类型，给变量strType赋上相应的值
                if(itemUnit.getMenuaction().contains("tkctt_Tkctt")) {
                    strType= ESEnum.ITEMTYPE0.getCode();
                }else if(itemUnit.getMenuaction().contains("tkctt_Cstpl")) {
                    strType= ESEnum.ITEMTYPE1.getCode();
                }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlQ")) {
                    strType= ESEnum.ITEMTYPE3.getCode();
                }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlM")) {
                    strType= ESEnum.ITEMTYPE4.getCode();
                }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlP")) {
                    strType= ESEnum.ITEMTYPE5.getCode();
                }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngSta")) {
                    strType= ESEnum.ITEMTYPE6.getCode();
                }else if(itemUnit.getMenuaction().contains("cstpl_SubcttEngMea")) {
                    strType= ESEnum.ITEMTYPE7.getCode();
                }else if(itemUnit.getMenuaction().contains("cstpl_Subctt")) {
                    strType= ESEnum.ITEMTYPE2.getCode();
                }
                //初始化合同类型
                strStatusFlag="";
                //根据合同状态的类型，给变量strStatusFlag赋上相应的值
                if(itemUnit.getMenuaction().contains("Mng")) {
                    strStatusFlag= ESEnumStatusFlag.STATUS_FLAG0.getCode();
                }else if(itemUnit.getMenuaction().contains("DoubleCheck")) {
                    strStatusFlag= ESEnumStatusFlag.STATUS_FLAG2.getCode();
                }else if(itemUnit.getMenuaction().contains("Check")) {
                    strStatusFlag= ESEnumStatusFlag.STATUS_FLAG1.getCode();
                }else if(itemUnit.getMenuaction().contains("Approve")) {
                    strStatusFlag= ESEnumStatusFlag.STATUS_FLAG3.getCode();
                }else if(itemUnit.getMenuaction().contains("Print")) {
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
                            break;
                        }
                    }
                }
            }
        return taskShowList;
    }
}
