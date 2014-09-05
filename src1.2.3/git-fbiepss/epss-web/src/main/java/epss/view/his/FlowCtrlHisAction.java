package epss.view.his;

import epss.common.enums.ESEnum;
import epss.repository.model.EsCttInfo;
import epss.repository.model.FlowCtrlHis;
import epss.service.CttInfoService;
import epss.service.FlowCtrlHisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
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
public class FlowCtrlHisAction {
    private static final Logger logger = LoggerFactory.getLogger(FlowCtrlHisAction.class);

    @ManagedProperty(value = "#{flowCtrlHisService}")
    private FlowCtrlHisService flowCtrlHisService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;

    private FlowCtrlHis flowCtrlHis;
    private List<FlowCtrlHis> flowCtrlHisList;

    private String strRendered1;
    private String strRendered2;
    private String strLabel1;
    private String strLabel2;
    private List<SelectItem> esInitCtt1List;
    private List<SelectItem> esInitCtt2List;

    private String strTkcttCstplSelected;

    @PostConstruct
    public void init() {
        this.flowCtrlHisList = new ArrayList<FlowCtrlHis>();
        esInitCtt1List=new ArrayList<SelectItem> ();
        esInitCtt2List=new ArrayList<SelectItem> ();
        strRendered1="false";
        strRendered2="false";
        resetAction();
    }

    public String onQueryAction(String strQryMsgOutPara) {
        try {
            this.flowCtrlHisList = flowCtrlHisService.selectListByModel(flowCtrlHis);
            if(strQryMsgOutPara.equals("true")){
                if (flowCtrlHisList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
        return null;
    }

    public void setEsInitPowerHisActionOfPowerPkidAction(){
        String strCttType= flowCtrlHis.getInfoType();
        if(strCttType.equals("")){
            flowCtrlHis.setInfoPkid("");
            strRendered1="false";
            strRendered2="false";
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            return;
        }
        if(strCttType.equals(ESEnum.ITEMTYPE0.getCode())
        ||strCttType.equals(ESEnum.ITEMTYPE6.getCode())
        ||strCttType.equals(ESEnum.ITEMTYPE7.getCode())){
            strLabel1="";
            strLabel2=ESEnum.ITEMTYPE0.getTitle();
            strRendered1="false";
            strRendered2="true";
            List<EsCttInfo> esCttInfoListTemp =new ArrayList<EsCttInfo>();
            esCttInfoListTemp = cttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE0.getCode());
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            SelectItem selectItem=new SelectItem("","全部");
            esInitCtt2List.add(selectItem);
            if(esCttInfoListTemp.size()>0){
                for(EsCttInfo itemUnit: esCttInfoListTemp){
                    selectItem=new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    esInitCtt2List.add(selectItem);
                }
            }
        }
        else if(strCttType.equals(ESEnum.ITEMTYPE1.getCode())){
            strLabel1=ESEnum.ITEMTYPE0.getTitle();
            strLabel2=ESEnum.ITEMTYPE1.getTitle();
            strRendered1="true";
            strRendered2="true";
            List<EsCttInfo> esCttInfoListTemp =new ArrayList<EsCttInfo>();
            esCttInfoListTemp = cttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE0.getCode());
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            SelectItem selectItem=new SelectItem("","全部");
            esInitCtt1List.add(selectItem);
            if(esCttInfoListTemp.size()>0){
                for(EsCttInfo itemUnit: esCttInfoListTemp){
                    selectItem=new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    esInitCtt1List.add(selectItem);
                }
            }
        }else{
            strLabel1=ESEnum.ITEMTYPE1.getTitle();
            strLabel2=ESEnum.getValueByKey(strCttType).getTitle();
            strRendered1="true";
            strRendered2="true";
            List<EsCttInfo> esCttInfoListTemp =new ArrayList<EsCttInfo>();
            esCttInfoListTemp = cttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE1.getCode());
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            SelectItem selectItem=new SelectItem("","全部");
            esInitCtt1List.add(selectItem);
            if(esCttInfoListTemp.size()>0){
                for(EsCttInfo itemUnit: esCttInfoListTemp){
                    selectItem=new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    esInitCtt1List.add(selectItem);
                }
            }
        }
    }

    public void setFromTkAndCttToSStlAction(){
        List<EsCttInfo> esCttInfoListTemp =new ArrayList<EsCttInfo>();
        String strCttType= flowCtrlHis.getInfoType();
        if(strCttType.equals(ESEnum.ITEMTYPE1.getCode())){
            esCttInfoListTemp = cttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                    ESEnum.ITEMTYPE1.getCode(),strTkcttCstplSelected);
        }else{
            esCttInfoListTemp = cttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                ESEnum.ITEMTYPE2.getCode(),strTkcttCstplSelected);
        }
        esInitCtt2List=new ArrayList<SelectItem>();
        SelectItem selectItem=new SelectItem("","全部");
        esInitCtt2List.add(selectItem);
        if(esCttInfoListTemp.size()>0){
            for(EsCttInfo itemUnit: esCttInfoListTemp){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                esInitCtt2List.add(selectItem);
            }
        }
    }

    public void resetAction(){
        flowCtrlHis =new FlowCtrlHis() ;
    }

    public List<FlowCtrlHis> getFlowCtrlHisList() {
        return flowCtrlHisList;
    }

    public void setFlowCtrlHisList(List<FlowCtrlHis> flowCtrlHisList) {
        this.flowCtrlHisList = flowCtrlHisList;
    }

    public FlowCtrlHis getEsInitPower() {
        return flowCtrlHis;
    }

    public void setEsInitPower(FlowCtrlHis flowCtrlHis) {
        this.flowCtrlHis = flowCtrlHis;
    }

    public FlowCtrlHisService getFlowCtrlHisService() {
        return flowCtrlHisService;
    }

    public void setFlowCtrlHisService(FlowCtrlHisService flowCtrlHisService) {
        this.flowCtrlHisService = flowCtrlHisService;
    }

    public FlowCtrlHis getFlowCtrlHis() {
        return flowCtrlHis;
    }

    public void setFlowCtrlHis(FlowCtrlHis flowCtrlHis) {
        this.flowCtrlHis = flowCtrlHis;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public String getStrTkcttCstplSelected() {
        return strTkcttCstplSelected;
    }

    public void setStrTkcttCstplSelected(String strTkcttCstplSelected) {
        this.strTkcttCstplSelected = strTkcttCstplSelected;
    }

    public String getStrRendered1() {
        return strRendered1;
    }

    public void setStrRendered1(String strRendered1) {
        this.strRendered1 = strRendered1;
    }

    public String getStrRendered2() {
        return strRendered2;
    }

    public void setStrRendered2(String strRendered2) {
        this.strRendered2 = strRendered2;
    }

    public String getStrLabel1() {
        return strLabel1;
    }

    public void setStrLabel1(String strLabel1) {
        this.strLabel1 = strLabel1;
    }

    public String getStrLabel2() {
        return strLabel2;
    }

    public void setStrLabel2(String strLabel2) {
        this.strLabel2 = strLabel2;
    }

    public List<SelectItem> getEsInitCtt1List() {
        return esInitCtt1List;
    }

    public void setEsInitCtt1List(List<SelectItem> esInitCtt1List) {
        this.esInitCtt1List = esInitCtt1List;
    }

    public List<SelectItem> getEsInitCtt2List() {
        return esInitCtt2List;
    }

    public void setEsInitCtt2List(List<SelectItem> esInitCtt2List) {
        this.esInitCtt2List = esInitCtt2List;
    }
}
