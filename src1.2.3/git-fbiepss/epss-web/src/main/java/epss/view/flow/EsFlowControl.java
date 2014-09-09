package epss.view.flow;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-3-26
 * Time: ÏÂÎç6:12
 * To change this template use File | Settings | File Templates.
 */

import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.FlowCtrlShow;
import epss.service.CttInfoService;
import epss.service.EsFlowService;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.service.SignPartService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.service.ToolsService;
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
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;

    private List<SelectItem> statusFlagFromDBList;
    private List<SelectItem> preStatusFlagList;
    private List<SelectItem> preStatusFlagFromDBList;
    private List<SelectItem> deleteFlagList;
    private List<SelectItem> endFlagList;

    @PostConstruct
    public void init() {
        this.statusFlagFromDBList = toolsService.getEnuSelectItemList("FLOW_STATUS", false, false);
        this.preStatusFlagFromDBList= toolsService.getEnuSelectItemList("FLOW_STATUS_REASON", false, false);
        this.deleteFlagList = toolsService.getEnuSelectItemList("DELETED_FLAG", true, false);
        this.endFlagList = toolsService.getEnuSelectItemList("END_FLAG", true, false);
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


    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return cttInfoService.getCttInfoByPkId(strPkid);
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
        List<SelectItem> statusFlagListTemp=new ArrayList<>();
        if(strFlowStatusPara.equals("Qry")){
            statusFlagListTemp.addAll(statusFlagFromDBList);
            statusFlagListTemp.add(0,siAllForSelect);
        }else if(strFlowStatusPara.equals("Mng")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().contains("³õÊ¼")){
                    statusFlagListTemp.add(itemUnit);
                    statusFlagListTemp.add(0,siNullForSelect);
                }
            }
        }else if(strFlowStatusPara.equals("Check")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().equals("³õÊ¼")){
                    statusFlagListTemp.add(itemUnit);
                }
            }
        }else if(strFlowStatusPara.equals("DoubleCheck")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().equals("³õÊ¼")||
                        itemUnit.getLabel().equals("ÉóºË")){
                    statusFlagListTemp.add(itemUnit);
                }
            }
        }else if(strFlowStatusPara.equals("Approve")){
            for(SelectItem itemUnit:statusFlagFromDBList) {
                if(itemUnit.getLabel().equals("³õÊ¼")||
                        itemUnit.getLabel().equals("ÉóºË")||
                        itemUnit.getLabel().equals("¸´ºË")){
                    statusFlagListTemp.add(itemUnit);
                }
            }
        }
        return statusFlagListTemp;
    }

    /*ÖÇÄÜ×Ö¶Î Start*/
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

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }
}
