package epss.view.common;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: ÏÂÎç6:12
 * To change this template use File | Settings | File Templates.
 */

import epss.repository.model.model_show.FlowCtrlShow;
import epss.service.common.EsFlowService;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsInitPower;
import epss.service.common.EsCommonService;
import epss.service.EsInitCustService;
import epss.service.EsInitPowerService;
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
    @ManagedProperty(value = "#{esInitCustService}")
    private EsInitCustService esInitCustService;
    @ManagedProperty(value = "#{esCommonService}")
    private EsCommonService esCommonService;
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private List<SelectItem> statusFlagList;
    private List<SelectItem> statusFlagFromDBList;

    private List<SelectItem> preStatusFlagList;
    private List<SelectItem> preStatusFlagFromDBList;

    private List<SelectItem> deleteFlagList;
    private List<SelectItem> deleteFlagFromDBList;

    private List<SelectItem> endFlagList;
    private List<SelectItem> endFlagFromDBList;

    @PostConstruct
    public void init() {
        this.statusFlagFromDBList = toolsService.getEnuSelectItemList("STATUS_FLAG", false, false);
        this.statusFlagList = toolsService.getEnuSelectItemList("STATUS_FLAG", true, false);
        this.preStatusFlagFromDBList= toolsService.getEnuSelectItemList("PRESTATUS_FLAG", false, false);
        this.deleteFlagList = toolsService.getEnuSelectItemList("DELETED_FLAG", true, false);
        this.endFlagList = toolsService.getEnuSelectItemList("END_FLAG", true, false);
    }

    public String getCustName(String strPkid) {
        String strTemp= esInitCustService.getEsInitCustByPkid(strPkid).getName();
        return strTemp==null?"1":strTemp;
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
    public String getValueByLabelInStatusFlaglist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:statusFlagFromDBList){
                if(itemUnit.getLabel().equals(strLabel)){
                    return itemUnit.getValue().toString();
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
    public String getValueByLabelInPreStatusFlaglist(String strLabel){
        if(!StringUtils.isEmpty(strLabel)){
            for(SelectItem itemUnit:preStatusFlagFromDBList){
                if(itemUnit.getLabel().equals(strLabel)){
                    return itemUnit.getValue().toString();
                }
            }
        }
        return "";
    }

    public void setStatusFlagListByPower(String strPageName){
        List<SelectItem> selectItemsTemp=new ArrayList<SelectItem>();
        selectItemsTemp.addAll(statusFlagFromDBList);

        statusFlagList=new ArrayList<SelectItem>();

        if(strPageName.equals("Qry")){
            statusFlagList.addAll(statusFlagFromDBList);
            statusFlagList.add(0,siAllForSelect);
        }else if(strPageName.equals("Mng")){
            for(SelectItem itemUnit:selectItemsTemp) {
                if(itemUnit.getLabel().contains("³õÊ¼")){
                    statusFlagList.add(itemUnit);
                    statusFlagList.add(0,siNullForSelect);
                }
            }
        }else if(strPageName.equals("Check")){
            for(SelectItem itemUnit:selectItemsTemp) {
                if(itemUnit.getLabel().equals("³õÊ¼")){
                    statusFlagList.add(itemUnit);
                }
            }
        }else if(strPageName.equals("DoubleCheck")){
            for(SelectItem itemUnit:selectItemsTemp) {
                if(itemUnit.getLabel().equals("³õÊ¼")||
                        itemUnit.getLabel().equals("ÉóºË")){
                    statusFlagList.add(itemUnit);
                }
            }
        }else if(strPageName.equals("Approve")){
            for(SelectItem itemUnit:selectItemsTemp) {
                if(itemUnit.getLabel().equals("³õÊ¼")||
                        itemUnit.getLabel().equals("ÉóºË")||
                        itemUnit.getLabel().equals("¸´ºË")){
                    statusFlagList.add(itemUnit);
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

            if(esInitPowerService.selectListByModel(flowCtrlShowTemp.getPowerType(),
                    flowCtrlShowTemp.getPowerPkid(), flowCtrlShowTemp.getPeriodNo()).size()<=0){
                esInitPowerService.insertRecord(flowCtrlShowTemp);
            }else {
                esInitPowerService.updateRecord(flowCtrlShowTemp);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public void mngNotFinishAction(String strPowerTypePara,
                                     String strPowerPkidPara,
                                     String strPeriodNoPara){
        try {
            esInitPowerService.deleteRecord(strPowerTypePara,strPowerPkidPara,strPeriodNoPara);
        } catch (Exception e) {
            throw e;
        }
    }

    public EsInitPower getEsInitPowerByTypeAndPkidAndPeriodNo(String strPowerTypePara,
                                                               String strPowerPkidPara,
                                                               String strPeriodNoPara){
        List<EsInitPower> esInitPowerListTemp=
                esInitPowerService.selectListByModel(strPowerTypePara,strPowerPkidPara,strPeriodNoPara);
        if(esInitPowerListTemp.size()>0){
            return esInitPowerService.selectListByModel(strPowerTypePara,strPowerPkidPara,strPeriodNoPara).get(0);
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

    public EsInitCustService getEsInitCustService() {
        return esInitCustService;
    }

    public void setEsInitCustService(EsInitCustService esInitCustService) {
        this.esInitCustService = esInitCustService;
    }

    public EsCommonService getEsCommonService() {
        return esCommonService;
    }

    public void setEsCommonService(EsCommonService esCommonService) {
        this.esCommonService = esCommonService;
    }

     public List<SelectItem> getStatusFlagList() {
        return statusFlagList;
    }

    public void setStatusFlagList(List<SelectItem> statusFlagList) {
        this.statusFlagList = statusFlagList;
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

    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }
}
