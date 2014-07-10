package epss.view.flow;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: œ¬ŒÁ6:12
 * To change this template use File | Settings | File Templates.
 */

import epss.repository.model.model_show.FlowCtrlShow;
import epss.service.EsFlowService;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.service.EsCommonService;
import epss.service.SignPartService;
import epss.service.FlowCtrlService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.view.build.utils.Util;
import platform.service.ToolsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * User Think
 * Date 13-3-26
 * Time œ¬ŒÁ6:12
 */
@ManagedBean
@ViewScoped
public class EsFlowControl implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EsFlowControl.class);
    private static final SelectItem siAllForSelect= new SelectItem("","»´≤ø");
    private static final SelectItem siNullForSelect= new SelectItem("","");

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{esCommonService}")
    private EsCommonService esCommonService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    private List<SelectItem> statusFlagFromDBList;

    private List<SelectItem> preStatusFlagList;
    private List<SelectItem> preStatusFlagFromDBList;

    private List<SelectItem> deleteFlagList;
    private List<SelectItem> endFlagList;

    @PostConstruct
    public void init() {
        this.statusFlagFromDBList = toolsService.getEnuSelectItemList("STATUS_FLAG", false, false);
        this.preStatusFlagFromDBList= toolsService.getEnuSelectItemList("PRESTATUS_FLAG", false, false);
        this.deleteFlagList = toolsService.getEnuSelectItemList("DELETED_FLAG", true, false);
        this.endFlagList = toolsService.getEnuSelectItemList("END_FLAG", true, false);
    }

    public String getOperNameByOperId(String strOperId){
        return Util.getUserName(strOperId);
    }

    public String getLabelByValueInStatusFlaglist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:statusFlagFromDBList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

    public String getLabelByValueInPreStatusFlaglist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:preStatusFlagFromDBList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

    public List<SelectItem> getBackToStatusFlagList(String strFlowStatusPara){
        List<SelectItem> statusFlagListTemp=new ArrayList<SelectItem>();

        if(strFlowStatusPara.equals("Qry")){
            statusFlagListTemp.addAll(statusFlagFromDBList);
            statusFlagListTemp.add(0,siAllForSelect);
        }else if(strFlowStatusPara.equals("Mng")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().contains("≥ı º")){
                    statusFlagListTemp.add(itemUnit);
                    statusFlagListTemp.add(0,siNullForSelect);
                }
            }
        }else if(strFlowStatusPara.equals("Check")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().equals("≥ı º")){
                    statusFlagListTemp.add(itemUnit);
                }
            }
        }else if(strFlowStatusPara.equals("DoubleCheck")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().equals("≥ı º")||
                        itemUnit.getLabel().equals("…Û∫À")){
                    statusFlagListTemp.add(itemUnit);
                }
            }
        }else if(strFlowStatusPara.equals("Approve")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().equals("≥ı º")||
                        itemUnit.getLabel().equals("…Û∫À")||
                        itemUnit.getLabel().equals("∏¥∫À")){
                    statusFlagListTemp.add(itemUnit);
                }
            }
        }
        return statusFlagListTemp;
    }

    public void mngFinishAction(String strPowerTypePara,
                                  String strPowerPkidPara,
                                  String strPeriodNoPara){
        try {
            FlowCtrlShow flowCtrlShowTemp =new FlowCtrlShow();
            flowCtrlShowTemp.setPowerType(strPowerTypePara);
            flowCtrlShowTemp.setPowerPkid(strPowerPkidPara);
            flowCtrlShowTemp.setPeriodNo(strPeriodNoPara);
            flowCtrlShowTemp.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG0.getCode());
            flowCtrlShowTemp.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG0.getCode());

            if(flowCtrlService.selectListByModel(flowCtrlShowTemp.getPowerType(),
                    flowCtrlShowTemp.getPowerPkid(), flowCtrlShowTemp.getPeriodNo()).size()<=0){
                flowCtrlService.insertRecord(flowCtrlShowTemp);
            }else {
                flowCtrlService.updateRecord(flowCtrlShowTemp);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public void mngNotFinishAction(String strPowerTypePara,
                                     String strPowerPkidPara,
                                     String strPeriodNoPara){
        try {
            flowCtrlService.deleteRecord(strPowerTypePara,strPowerPkidPara,strPeriodNoPara);
        } catch (Exception e) {
            throw e;
        }
    }

    /*÷«ƒ‹◊÷∂Œ Start*/

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public EsCommonService getEsCommonService() {
        return esCommonService;
    }

    public void setEsCommonService(EsCommonService esCommonService) {
        this.esCommonService = esCommonService;
    }

    public List<SelectItem> getDeleteFlagList() {
        return deleteFlagList;
    }

    public void setDeleteFlagList(List<SelectItem> deleteFlagList) {
        this.deleteFlagList = deleteFlagList;
    }

    public List<SelectItem> getEndFlagList() {
        return endFlagList;
    }

    public void setEndFlagList(List<SelectItem> endFlagList) {
        this.endFlagList = endFlagList;
    }

    public List<SelectItem> getPreStatusFlagList() {
        return preStatusFlagList;
    }

    public void setPreStatusFlagList(List<SelectItem> preStatusFlagList) {
        this.preStatusFlagList = preStatusFlagList;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }
}
