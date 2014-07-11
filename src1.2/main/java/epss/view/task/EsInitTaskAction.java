package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.TaskShow;
import epss.service.EsCommonService;
import epss.service.FlowCtrlService;
import platform.repository.model.Ptmenu;
import platform.service.PlatformService;
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
    @ManagedProperty(value = "#{esCommonService}")
    private EsCommonService esCommonService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        //整个任务列表
        this.taskShowList = new ArrayList<TaskShow>();
        //获取操作菜单，包括地址，菜单基本等等，并且通过OperatorManager实现不同权限下菜单的显示
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        //根据合同类型和状态获取合同的类型+状态+数量信息
        List<TaskShow> taskShowListTemp =esCommonService.getPowerListByPowerTypeAndStatusFlag();
        //获取指定类型和状态下合同的具体信息
        List<TaskShow> esInitPowerList=esCommonService.getTaskModelList();
        //合同类型
        String strType;
        //合同状态
        String strStatusFlag;
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
            //给变量taskShow的属性Power_Pkid赋值
            taskShow.setPower_Pkid(itemUnit.getMenuid());
            //创建并初始化特定类型和状态合同的数量
            Integer intGroupCounts=0;
            //创建并初始化判断合同是否是特定类型和状态的布尔变量
            Boolean isHasTask=false;
            //创建并初始化存放一定状态合同的集合
            List<TaskShow> taskShowListOfStatusFlag =null;
            //当合同状态为录入时
            if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                //根据合同类型是总成分合同还是其它类型的合同进行查询（因为分包数量、分包材料等有了期的限定）
                if (strType.equals(ESEnum.ITEMTYPE0.getCode())||strType.equals(ESEnum.ITEMTYPE1.getCode())||strType.equals(ESEnum.ITEMTYPE2.getCode())){
                    taskShowListOfStatusFlag =esCommonService.getTaskModelListOfCtt(strType);
                }else {
                    taskShowListOfStatusFlag =esCommonService.getTaskModelListOfStl(strType);
                }
                //当存放合同的集合不为空时，设置显示列表的任务类别列的表头，小标题的组成为合同类型+合同状态+合同数量；
                //同时给对应的隐藏列表头赋值
                if (taskShowListOfStatusFlag.size()>0){
                    taskShow.setPower_Type(itemUnit.getMenulabel() + "(" + taskShowListOfStatusFlag.size() + ")");
                    if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                        taskShow.setCttType("总包合同录入");
                    }
                    if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                        taskShow.setCttType("成本计划录入");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                        taskShow.setCttType("分包合同录入");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                        taskShow.setCttType("分包数量结算录入");
                    }
                    if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                        taskShow.setCttType("分包材料结算录入");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                        taskShow.setCttType("分包价格结算录入");
                    }
                    if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                        taskShow.setCttType("总包数量统计录入");
                    }
                    if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                        taskShow.setCttType("总包数量计量录入");
                    }
                    taskShowList.add(taskShow);
                }else {
                    continue;
                }//当存放合同的计划不为空时，将符合特定类型和状态的合同——即录入类型的合同的具体信息添加到显示表格中
                if (taskShowListOfStatusFlag !=null){
                    for(TaskShow itemUnitEP: taskShowListOfStatusFlag){
                        if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                            itemUnitEP.setCttType("总包合同录入");
                        }
                        if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                            itemUnitEP.setCttType("成本计划录入");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                            itemUnitEP.setCttType("分包合同录入");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                            itemUnitEP.setCttType("分包数量结算录入");
                        }
                        if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                            itemUnitEP.setCttType("分包材料结算录入");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                            itemUnitEP.setCttType("分包价格结算录入");
                        }
                        if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                            itemUnitEP.setCttType("总包数量统计录入");
                        }
                        if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                            itemUnitEP.setCttType("总包数量计量录入");
                        }
                        taskShow =new TaskShow();
                        taskShow.setPower_Pkid(itemUnitEP.getPower_Pkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;" + itemUnitEP.getName());
                        taskShow.setPower_Type("&#8195;" + itemUnitEP.getParentName());
                        taskShow.setPeriod_No(itemUnitEP.getPeriod_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatus_Flag(itemUnitEP.getStatus_Flag());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
                        taskShow.setCttType(itemUnitEP.getCttType());
                        taskShowList.add(taskShow);
                    } }
                continue;
            }
            //遍历存放“合同类型+状态+数量”信息的集合
            for(TaskShow itemUnitCM: taskShowListTemp){
                //如果任务类别为空的话，赋值为空
                if (itemUnitCM.getPower_Type()==null){
                    itemUnitCM.setPower_Type("");
                }
                //如果任务类别和合同类型不符的话，终止
                if(itemUnitCM.getPower_Type().compareTo(strType)>0){
                    break;
                } //当任务类别和合同类型一致的时候
                if(itemUnitCM.getPower_Type().equals(strType)){
                    //如果任务状态为空，赋值为空
                    if (itemUnitCM.getStatus_Flag()==null){
                        itemUnitCM.setStatus_Flag("");
                    }
                    //保证越级的不会出现
                    isHasTask=false;
                    //当合同状态为初始时，其是"录入"中的待处理任务
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        isHasTask=true;//当合同状态为审核时，并且任务状态为初始的时，其是“审核”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;//给隐藏类的表头赋值
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                taskShow.setCttType("总包合同审核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                taskShow.setCttType("成本计划审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                taskShow.setCttType("分包合同审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                taskShow.setCttType("分包数量结算审核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                taskShow.setCttType("分包材料结算审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("分包价格结算审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                taskShow.setCttType("总包数量统计审核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                taskShow.setCttType("总包数量计量审核");
                            }
                        } //当合同状态为复核时，并且任务状态为审核的时，其是“复核”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                taskShow.setCttType("总包合同复核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                taskShow.setCttType("成本计划复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                taskShow.setCttType("分包合同复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                taskShow.setCttType("分包数量结算复核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                taskShow.setCttType("分包材料结算复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("分包价格结算复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                taskShow.setCttType("总包数量统计复核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                taskShow.setCttType("总包数量计量复核");
                            }
                        }//当合同状态为批准时，并且任务状态为复核的时，其是“批准”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                taskShow.setCttType("总包合同批准");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                taskShow.setCttType("成本计划批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                taskShow.setCttType("分包合同批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                taskShow.setCttType("分包数量结算批准");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                taskShow.setCttType("分包材料结算批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("分包价格结算批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                taskShow.setCttType("总包数量统计批准");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                taskShow.setCttType("总包数量计量批准");
                            }
                        }//当合同状态为记账时，并且任务状态为批准的时，其是“记账”中的待处理任务
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                taskShow.setCttType("分包价格结算记账");
                            }
                        }
                    } //当任务为待处理任务时，统计待处理任务中的任务个数
                    if(isHasTask.equals(true)){
                        intGroupCounts=intGroupCounts+Integer.parseInt(itemUnitCM.getGroup_Counts());
                    }
                }
            } //当待处理任务的个数不为空的时候，将值赋给待处理任务的表头
            if(intGroupCounts>0){
                taskShow.setPower_Type(itemUnit.getMenulabel()+"("+intGroupCounts+")");
                taskShow.setPower_Pkid("");
            }
            else{
                continue;
            }
            taskShowList.add(taskShow);
            /*遍历存放“指定类型和状态下合同”的集合
            当合同状态为初始时，其是"录入"中的待处理任务
            当合同状态为审核时，并且任务状态为初始的时，其是“审核”中的待处理任务
            当合同状态为复核时，并且任务状态为审核的时，其是“复核”中的待处理任务
            当合同状态为批准时，并且任务状态为复核的时，其是“批准”中的待处理任务
            当合同状态为记账时，并且任务状态为批准的时，其是“记账”中的待处理任务*/
            for(TaskShow itemUnitEP:esInitPowerList){
                if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())&&
                        itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                }
                if (itemUnitEP.getPower_Type()==null){
                    itemUnitEP.setPower_Type("");
                }
                if(itemUnitEP.getPower_Type().compareTo(strType)>0){
                    break;
                }
                if(itemUnitEP.getPower_Type().equals(strType)){
                    if (itemUnitEP.getStatus_Flag()==null){
                        itemUnitEP.setStatus_Flag("");
                    }
                    isHasTask=false;
                    if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals("")){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    }
                    //如果其是待处理任务，在显示列表上添加隐藏列和任务列的具体信息
                    if(isHasTask.equals(true)){
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                itemUnitEP.setCttType("总包合同审核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                itemUnitEP.setCttType("成本计划审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                itemUnitEP.setCttType("分包合同审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                itemUnitEP.setCttType("分包数量结算审核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                itemUnitEP.setCttType("分包材料结算审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("分包价格结算审核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                itemUnitEP.setCttType("总包数量统计审核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                itemUnitEP.setCttType("总包数量计量审核");
                            }
                        }
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                itemUnitEP.setCttType("总包合同复核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                itemUnitEP.setCttType("成本计划复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                itemUnitEP.setCttType("分包合同复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                itemUnitEP.setCttType("分包数量结算复核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                itemUnitEP.setCttType("分包材料结算复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("分包价格结算复核");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                itemUnitEP.setCttType("总包数量统计复核");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                itemUnitEP.setCttType("总包数量计量复核");
                            }
                        }
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE0.getCode())){
                                itemUnitEP.setCttType("总包合同批准");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE1.getCode())){
                                itemUnitEP.setCttType("成本计划批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE2.getCode())){
                                itemUnitEP.setCttType("分包合同批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE3.getCode())){
                                itemUnitEP.setCttType("分包数量结算批准");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE4.getCode())){
                                itemUnitEP.setCttType("分包材料结算批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("分包价格结算批准");
                            }
                            if (strType.equals(ESEnum.ITEMTYPE6.getCode())){
                                itemUnitEP.setCttType("总包数量统计批准");
                            }
                            if(strType.equals(ESEnum.ITEMTYPE7.getCode())){
                                itemUnitEP.setCttType("总包数量计量批准");
                            }
                        }
                        if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())&&
                                itemUnitEP.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            if (strType.equals(ESEnum.ITEMTYPE5.getCode())){
                                itemUnitEP.setCttType("分包价格结算记账");
                            }
                        }
                        taskShow =new TaskShow();
                        taskShow.setPower_Pkid(itemUnitEP.getPower_Pkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;"+itemUnitEP.getName());
                        taskShow.setPower_Type("&#8195;"+itemUnitEP.getParentName());
                        taskShow.setPeriod_No(itemUnitEP.getPeriod_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatus_Flag(itemUnitEP.getStatus_Flag());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
                        taskShow.setCttType(itemUnitEP.getCttType());
                        taskShowList.add(taskShow);
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

    public void setTaskShowList(List<TaskShow> taskShowList) {
        this.taskShowList = taskShowList;
    }

    public EsCommonService getEsCommonService() {
        return esCommonService;
    }

    public void setEsCommonService(EsCommonService esCommonService) {
        this.esCommonService = esCommonService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }
}