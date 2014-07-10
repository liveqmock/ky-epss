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
 * Time: ÏÂÎç4:53
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
        this.taskShowList = new ArrayList<TaskShow>();
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        List<TaskShow> taskShowListTemp =esCommonService.getPowerListByPowerTypeAndStatusFlag();
        List<TaskShow> esInitPowerList=esCommonService.getTaskModelList();
        String strType;
        String strStatusFlag;
        for(Ptmenu itemUnit:ptmenuList){
            if(itemUnit.getMenuaction()==null||
                    (!itemUnit.getMenuaction().toLowerCase().contains("cstpl")&&
                            !itemUnit.getMenuaction().toLowerCase().contains("tkctt"))){
                continue;
            }
            strType="";
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
            strStatusFlag="";
            
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

            TaskShow taskShow =new TaskShow();
            taskShow.setPower_Pkid(itemUnit.getMenuid());
            Integer intGroupCounts=0;
            Boolean isHasTask=false;
            //TODO
            List<TaskShow> taskShowListOfStatusFlag =null;
            if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                if (strType.equals(ESEnum.ITEMTYPE0.getCode())||strType.equals(ESEnum.ITEMTYPE1.getCode())||strType.equals(ESEnum.ITEMTYPE2.getCode())){
                    taskShowListOfStatusFlag =esCommonService.getTaskModelListOfCtt(strType);
                }else {
                    taskShowListOfStatusFlag =esCommonService.getTaskModelListOfStl(strType);
                }
                if (taskShowListOfStatusFlag.size()>0){
                    taskShow.setPower_Type(itemUnit.getMenulabel()+"("+ taskShowListOfStatusFlag.size()+")");
                    taskShowList.add(taskShow);
                }else {
                    continue;
                }
                if (taskShowListOfStatusFlag !=null){
                    for(TaskShow itemUnitEP: taskShowListOfStatusFlag){
                        taskShow =new TaskShow();
                        taskShow.setPower_Pkid(itemUnitEP.getPower_Pkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;"+itemUnitEP.getName());
                        taskShow.setPower_Type("&#8195;"+itemUnitEP.getParentName());
                        taskShow.setPeriod_No(itemUnitEP.getPeriod_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatus_Flag(itemUnitEP.getStatus_Flag());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
                        taskShowList.add(taskShow);
                    } }
                continue;
            }


            for(TaskShow itemUnitCM: taskShowListTemp){
                if (itemUnitCM.getPower_Type()==null){
                    itemUnitCM.setPower_Type("");
                }
                if(itemUnitCM.getPower_Type().compareTo(strType)>0){
                    break;
                }
                if(itemUnitCM.getPower_Type().equals(strType)){
                    if (itemUnitCM.getStatus_Flag()==null){
                        itemUnitCM.setStatus_Flag("");
                    }
                    isHasTask=false;
                    /*if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        isHasTask=true;
                    }else */if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())){
                            isHasTask=true;
                        }
                    }else if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG4.getCode())){
                        if(itemUnitCM.getStatus_Flag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                            isHasTask=true;
                        }
                    }

                    if(isHasTask.equals(true)){
                        intGroupCounts=intGroupCounts+Integer.parseInt(itemUnitCM.getGroup_Counts());
                    }
                }
            }
            if(intGroupCounts>0){
                taskShow.setPower_Type(itemUnit.getMenulabel()+"("+intGroupCounts+")");
                taskShow.setPower_Pkid("");
            }
            else{
                continue;
            }
            taskShowList.add(taskShow);

            for(TaskShow itemUnitEP:esInitPowerList){
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
                    /*if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG0.getCode())){
                        if(itemUnitEP.getStatus_Flag().equals("")){
                            isHasTask=true;
                        }
                    }else*/ if(strStatusFlag.equals(ESEnumStatusFlag.STATUS_FLAG1.getCode())){
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

                    if(isHasTask==true){
                        taskShow =new TaskShow();
                        taskShow.setPower_Pkid(itemUnitEP.getPower_Pkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;"+itemUnitEP.getName());
                        taskShow.setPower_Type("&#8195;"+itemUnitEP.getParentName());
                        taskShow.setPeriod_No(itemUnitEP.getPeriod_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setStatus_Flag(itemUnitEP.getStatus_Flag());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
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