package epss.view.common;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: ÏÂÎç6:12
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusRemark;
import epss.repository.model.FlowCtrl;
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
 * Time ÏÂÎç6:12
 */
@ManagedBean
@ViewScoped
public class EsFlowControl implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(EsFlowControl.class);
    private static final SelectItem siAllForSelect= new SelectItem("","È«²¿");
    private static final SelectItem siNullForSelect= new SelectItem("","");

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{esCommonService}")
    private EsCommonService esCommonService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private List<SelectItem> flowStatusList;
    private List<SelectItem> flowStatusFromDBList;

    private List<SelectItem> flowStatusRemarkList;
    private List<SelectItem> flowStatusRemarkFromDBList;

    private List<SelectItem> deleteFlagList;
    private List<SelectItem> deleteFlagFromDBList;

    private List<SelectItem> endFlagList;
    private List<SelectItem> endFlagFromDBList;

    @PostConstruct
    public void init() {
        flowStatusFromDBList = new ArrayList<SelectItem>();
        flowStatusRemarkList = new ArrayList<SelectItem>();
        flowStatusFromDBList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS0.getCode(),EnumFlowStatus.FLOW_STATUS0.getTitle()));
        flowStatusFromDBList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS1.getCode(),EnumFlowStatus.FLOW_STATUS1.getTitle()));
        flowStatusFromDBList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS2.getCode(),EnumFlowStatus.FLOW_STATUS2.getTitle()));
        flowStatusFromDBList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS3.getCode(),EnumFlowStatus.FLOW_STATUS3.getTitle()));
        flowStatusFromDBList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS4.getCode(),EnumFlowStatus.FLOW_STATUS4.getTitle()));
        flowStatusFromDBList.add(
                new SelectItem(EnumFlowStatus.FLOW_STATUS5.getCode(),EnumFlowStatus.FLOW_STATUS5.getTitle()));
        this.flowStatusRemarkFromDBList= new ArrayList<SelectItem>();
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK0.getTitle()));
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK1.getTitle()));
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK2.getTitle()));
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK3.getTitle()));
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK4.getTitle()));
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK5.getTitle()));
        flowStatusRemarkFromDBList.add(
                new SelectItem(EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getCode(),EnumFlowStatusRemark.FLOW_STATUS_REMARK6.getTitle()));
        flowStatusRemarkList.addAll(flowStatusRemarkFromDBList);
          this.deleteFlagList = toolsService.getEnuSelectItemList("ARCHIVED_FLAG", true, false);
        this.endFlagList = toolsService.getEnuSelectItemList("END_FLAG", true, false);
    }

    public String getOperNameByOperId(String strOperId){
        return Util.getUserName(strOperId);
    }

    public String getLabelByValueInFlowStatuslist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:flowStatusFromDBList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }
    public String getValueByLabelInFlowStatuslist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:flowStatusFromDBList){
                if(itemUnit.getLabel().equals(strLabel)){
                    return itemUnit.getValue().toString();
                }
            }
        }
        return "";
    }

    public String getLabelByValueInPreFlowStatuslist(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:flowStatusRemarkFromDBList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }
    public String getValueByLabelInPreFlowStatuslist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:flowStatusRemarkFromDBList){
                if(itemUnit.getLabel().equals(strLabel)){
                    return itemUnit.getValue().toString();
                }
            }
        }
        return "";
    }

    public void setFlowStatusListByPower(String strPageName){
        List<SelectItem> selectItemsTemp=new ArrayList<SelectItem>();
        selectItemsTemp.addAll(flowStatusFromDBList);
        flowStatusList=new ArrayList<SelectItem>();

        switch (strPageName){
            case "Qry":
                flowStatusList.addAll(flowStatusFromDBList);
                flowStatusList.add(0,siAllForSelect);
                break;
            case "Mng":
                for(SelectItem itemUnit:selectItemsTemp) {
                    if(itemUnit.getLabel().contains("³õÊ¼")){
                        flowStatusList.add(itemUnit);
                        flowStatusList.add(0,siNullForSelect);
                    }
                }
                break;
            case "Check":
                for(SelectItem itemUnit:selectItemsTemp) {
                    if(itemUnit.getLabel().equals("³õÊ¼")){
                        flowStatusList.add(itemUnit);
                    }
                }
                break;
            case "DoubleCheck":
                for(SelectItem itemUnit:selectItemsTemp) {
                    if(itemUnit.getLabel().equals("³õÊ¼")||
                            itemUnit.getLabel().equals("ÉóºË")){
                        flowStatusList.add(itemUnit);
                    }
                }
                break;
            case "Approve":
                for(SelectItem itemUnit:selectItemsTemp) {
                    if(itemUnit.getLabel().equals("³õÊ¼")||
                            itemUnit.getLabel().equals("ÉóºË")||
                            itemUnit.getLabel().equals("¸´ºË")){
                        flowStatusList.add(itemUnit);
                    }
                }
        }
    }
    public void setDeleteFlagListByPower(String strPageName){
        deleteFlagList=new ArrayList<SelectItem>();
        deleteFlagList.addAll(deleteFlagFromDBList) ;
        deleteFlagList.add(0,siAllForSelect);
    }
    public void setEndFlagListByPower(String strPageName){
        endFlagList=new ArrayList<SelectItem>();
        endFlagList.addAll(endFlagFromDBList) ;
        endFlagList.add(0,siAllForSelect);
    }

    public FlowCtrl getEsInitPowerByTypeAndPkidAndStageNo(String strPowerTypePara,
                                                               String strPowerPkidPara,
                                                               String strStageNoPara){
        List<FlowCtrl> flowCtrlListTemp =
                flowCtrlService.selectListByModel(strPowerTypePara,strPowerPkidPara,strStageNoPara);
        if(flowCtrlListTemp.size()>0){
            return flowCtrlService.selectListByModel(strPowerTypePara,strPowerPkidPara,strStageNoPara).get(0);
        }else{
            return null;
        }
    }

    /*ÖÇÄÜ×Ö¶Î Start*/
    public String getLableByValueInDeleteFlagList(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:deleteFlagList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

    public String getLabelByValueInEndFlagList(String strValue){
        if(!StringUtils.isEmpty(strValue)){
            for(SelectItem itemUnit:endFlagList){
                if(itemUnit.getValue().equals(strValue)){
                    return itemUnit.getLabel();
                }
            }
        }
        return "";
    }

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

     public List<SelectItem> getFlowStatusList() {
        return flowStatusList;
    }

    public void setFlowStatusList(List<SelectItem> flowStatusList) {
        this.flowStatusList = flowStatusList;
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

    public List<SelectItem> getFlowStatusRemarkList() {
        return flowStatusRemarkList;
    }

    public void setFlowStatusRemarkList(List<SelectItem> flowStatusRemarkList) {
        this.flowStatusRemarkList = flowStatusRemarkList;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }
}
