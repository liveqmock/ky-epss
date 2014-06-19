package epss.view.task;

import epss.common.enums.ESEnum;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.model_show.TaskShow;
import epss.service.EsCommonService;
import platform.repository.model.Ptmenu;
import platform.service.PlatformService;
import epss.view.common.EsFlowControl;
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

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        this.taskShowList = new ArrayList<TaskShow>();
        List<Ptmenu> ptmenuList = platformService.getPtmenuList();
        List<TaskShow> taskShowListTemp =esCommonService.getPowerListByPowerTypeAndFlowStatus();
        List<TaskShow> esInitPowerList=esCommonService.getTaskModelList();
        String strType;
        String strFlowStatus;
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
            strFlowStatus="";
            if(itemUnit.getMenuaction().contains("cstpl_SubcttEngStlPApprove")) {
                strFlowStatus= EnumFlowStatus.FLOW_STATUS0.getCode();
            }else
            if(itemUnit.getMenuaction().contains("Mng")) {
                strFlowStatus= EnumFlowStatus.FLOW_STATUS0.getCode();
            }else if(itemUnit.getMenuaction().contains("DoubleCheck")) {
                strFlowStatus= EnumFlowStatus.FLOW_STATUS2.getCode();
            }else if(itemUnit.getMenuaction().contains("Check")) {
                strFlowStatus= EnumFlowStatus.FLOW_STATUS1.getCode();
            }else if(itemUnit.getMenuaction().contains("Approve")) {
                strFlowStatus= EnumFlowStatus.FLOW_STATUS3.getCode();
            }else if(itemUnit.getMenuaction().contains("Print")) {
                strFlowStatus= EnumFlowStatus.FLOW_STATUS4.getCode();
            }

            TaskShow taskShow =new TaskShow();
            taskShow.setInfoPkid(itemUnit.getMenuid());
            Integer intGroupCounts=0;
            Boolean isHasTask=false;
            //TODO
            List<TaskShow> taskShowListOfFlowStatus =null;
            if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                if (strType.equals(ESEnum.ITEMTYPE0.getCode())||strType.equals(ESEnum.ITEMTYPE1.getCode())||strType.equals(ESEnum.ITEMTYPE2.getCode())){
                    taskShowListOfFlowStatus =esCommonService.getTaskModelListOfCtt(strType);
                }else {
                    taskShowListOfFlowStatus =esCommonService.getTaskModelListOfStl(strType);
                }
                if (taskShowListOfFlowStatus.size()>0){
                    taskShow.setInfoType(itemUnit.getMenulabel()+"("+ taskShowListOfFlowStatus.size()+")");
                    taskShowList.add(taskShow);
                }else {
                    continue;
                }
                if (taskShowListOfFlowStatus !=null){
                    for(TaskShow itemUnitEP: taskShowListOfFlowStatus){
                        taskShow =new TaskShow();
                        taskShow.setInfoPkid(itemUnitEP.getInfoPkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;"+itemUnitEP.getName());
                        taskShow.setInfoType("&#8195;"+itemUnitEP.getParentName());
                        taskShow.setStage_No(itemUnitEP.getStage_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setFlowSatus(itemUnitEP.getFlowSatus());
                        taskShow.setPre_Status_Flag(itemUnitEP.getPre_Status_Flag());
                        taskShowList.add(taskShow);
                    } }
                continue;
            }


            for(TaskShow itemUnitCM: taskShowListTemp){
                if (itemUnitCM.getInfoType()==null){
                    itemUnitCM.setInfoType("");
                }
                if(itemUnitCM.getInfoType().compareTo(strType)>0){
                    break;
                }
                if(itemUnitCM.getInfoType().equals(strType)){
                    if (itemUnitCM.getFlowSatus()==null){
                        itemUnitCM.setFlowSatus("");
                    }
                    isHasTask=false;
                    /*if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                        isHasTask=true;
                    }else */if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())){
                        if(itemUnitCM.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                            isHasTask=true;
                        }
                    }else if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())){
                        if(itemUnitCM.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS1.getCode())){
                            isHasTask=true;
                        }
                    }else if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS3.getCode())){
                        if(itemUnitCM.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS2.getCode())){
                            isHasTask=true;
                        }
                    }else if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS4.getCode())){
                        if(itemUnitCM.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS3.getCode())){
                            isHasTask=true;
                        }
                    }

                    if(isHasTask.equals(true)){
                        intGroupCounts=intGroupCounts+Integer.parseInt(itemUnitCM.getGroup_Counts());
                    }
                }
            }
            if(intGroupCounts>0){
                taskShow.setInfoType(itemUnit.getMenulabel()+"("+intGroupCounts+")");
                taskShow.setInfoPkid("");
            }
            else{
                continue;
            }
            taskShowList.add(taskShow);

            for(TaskShow itemUnitEP:esInitPowerList){
                if (itemUnitEP.getInfoType()==null){
                    itemUnitEP.setInfoType("");
                }
                if(itemUnitEP.getInfoType().compareTo(strType)>0){
                    break;
                }
                if(itemUnitEP.getInfoType().equals(strType)){
                    if (itemUnitEP.getFlowSatus()==null){
                        itemUnitEP.setFlowSatus("");
                    }
                    isHasTask=false;
                    /*if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                        if(itemUnitEP.getFlowSatus().equals("")){
                            isHasTask=true;
                        }
                    }else*/ if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())){
                        if(itemUnitEP.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS0.getCode())){
                            isHasTask=true;
                        }
                    }else if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())){
                        if(itemUnitEP.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS1.getCode())){
                            isHasTask=true;
                        }
                    }else if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS3.getCode())){
                        if(itemUnitEP.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS2.getCode())){
                            isHasTask=true;
                        }
                    }else if(strFlowStatus.equals(EnumFlowStatus.FLOW_STATUS4.getCode())){
                        if(itemUnitEP.getFlowSatus().equals(EnumFlowStatus.FLOW_STATUS3.getCode())){
                            isHasTask=true;
                        }
                    }

                    if(isHasTask.equals(true)){
                        taskShow =new TaskShow();
                        taskShow.setInfoPkid(itemUnitEP.getInfoPkid());
                        taskShow.setId(itemUnitEP.getId());
                        taskShow.setName("&#8195;"+itemUnitEP.getName());
                        taskShow.setInfoType("&#8195;"+itemUnitEP.getParentName());
                        taskShow.setStage_No(itemUnitEP.getStage_No());
                        taskShow.setName(itemUnitEP.getName());
                        taskShow.setFlowSatus(itemUnitEP.getFlowSatus());
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
}