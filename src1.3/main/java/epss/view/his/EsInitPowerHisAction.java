package epss.view.his;

import epss.common.enums.ESEnum;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.SubcttInfo;
import epss.service.SubcttInfoService;
import epss.service.FlowCtrlHisService;
import epss.view.common.EsFlowControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

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
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class EsInitPowerHisAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitPowerHisAction.class);
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{flowCtrlHisService}")
    private FlowCtrlHisService flowCtrlHisService;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;

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
        esFlowControl.setFlowStatusListByPower("Qry");
        resetAction();
    }

    public String onQueryAction(String strQryMsgOutPara) {
        try {
            this.flowCtrlHisList = flowCtrlHisService.selectListByModel(flowCtrlHis);
            if(strQryMsgOutPara.equals("true")){
                if (flowCtrlHisList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
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
            List<SubcttInfo> cttInfoListTemp =new ArrayList<SubcttInfo>();
            //cttInfoListTemp = subcttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE0.getCode());
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            SelectItem selectItem=new SelectItem("","ȫ��");
            esInitCtt2List.add(selectItem);
            if(cttInfoListTemp.size()>0){
                for(SubcttInfo itemUnit: cttInfoListTemp){
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
            List<SubcttInfo> cttInfoListTemp =new ArrayList<SubcttInfo>();
            //cttInfoListTemp = subcttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE0.getCode());
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            SelectItem selectItem=new SelectItem("","ȫ��");
            esInitCtt1List.add(selectItem);
            if(cttInfoListTemp.size()>0){
                for(SubcttInfo itemUnit: cttInfoListTemp){
                    selectItem=new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    esInitCtt1List.add(selectItem);
                }
            }
        }else{
            strLabel1=ESEnum.ITEMTYPE1.getTitle();
            strLabel2=ESEnum.valueOfAlias(strCttType).getTitle();
            strRendered1="true";
            strRendered2="true";
            List<SubcttInfo> cttInfoListTemp =new ArrayList<SubcttInfo>();
            //cttInfoListTemp = subcttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE1.getCode());
            esInitCtt1List=new ArrayList<SelectItem>();
            esInitCtt2List=new ArrayList<SelectItem>();
            SelectItem selectItem=new SelectItem("","ȫ��");
            esInitCtt1List.add(selectItem);
            if(cttInfoListTemp.size()>0){
                for(SubcttInfo itemUnit: cttInfoListTemp){
                    selectItem=new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    esInitCtt1List.add(selectItem);
                }
            }
        }
    }

    public void setFromTkAndCttToSStlAction(){
        List<SubcttInfo> cttInfoListTemp =new ArrayList<SubcttInfo>();
        String strCttType= flowCtrlHis.getInfoType();
   /*     if(strCttType.equals(ESEnum.ITEMTYPE1.getCode())){
            cttInfoListTemp = subcttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                    ESEnum.ITEMTYPE1.getCode(),strTkcttCstplSelected);
        }else{
            cttInfoListTemp = subcttInfoService.getEsInitCttByCttTypeAndBelongToPkId(
                ESEnum.ITEMTYPE2.getCode(),strTkcttCstplSelected);
        }*/
        esInitCtt2List=new ArrayList<SelectItem>();
        SelectItem selectItem=new SelectItem("","ȫ��");
        esInitCtt2List.add(selectItem);
        if(cttInfoListTemp.size()>0){
            for(SubcttInfo itemUnit: cttInfoListTemp){
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

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
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

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
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