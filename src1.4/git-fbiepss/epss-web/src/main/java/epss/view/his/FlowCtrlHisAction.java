package epss.view.his;

import epss.common.enums.EnumResType;
import epss.repository.model.CttInfo;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.model_show.FlowCtrlShow;
import epss.service.CttInfoService;
import epss.service.FlowCtrlHisService;
import org.apache.commons.lang.StringUtils;
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
    private FlowCtrlShow flowCtrlShow;
    private List<FlowCtrlHis> flowCtrlHisList;

    private String strRendered1;
    private String strRendered2;
    private String strLabel1;
    private String strLabel2;
    private List<SelectItem> esInitCtt1List;
    private List<SelectItem> esInitCtt2List;
    private List<FlowCtrlShow> flowCtrlShowList;
    private String strTkcttCstplSelected;
    private boolean Flag = false;
    private List<SelectItem> selectItemList_067;
    private List<SelectItem> selectItemList_1;
    private List<SelectItem> selectItemList_23458;

    // 完成情况
    private String strFinish_Flag;
    private List<SelectItem> finish_FlagList;

    @PostConstruct
    public void init() {
        this.flowCtrlHisList = new ArrayList<FlowCtrlHis>();
        flowCtrlShow = new FlowCtrlShow();
        esInitCtt1List=new ArrayList<SelectItem> ();
        esInitCtt2List=new ArrayList<SelectItem> ();
        finish_FlagList=new ArrayList<>();

        finish_FlagList.add(new SelectItem("", "全部"));
        finish_FlagList.add(new SelectItem("0", "在途"));
        finish_FlagList.add(new SelectItem("1", "完成"));

        strRendered1="false";
        strRendered2="false";
        resetAction();
        List<CttInfo> cttInfoList = cttInfoService.getEsInitCttListByCttType(null);
        if(cttInfoList.size()>0){
            selectItemList_067 = new ArrayList<SelectItem> ();
            selectItemList_1 = new ArrayList<SelectItem> ();
            selectItemList_23458 = new ArrayList<SelectItem> ();
            SelectItem all=new SelectItem("","全部");
            selectItemList_23458.add(all);
            selectItemList_1.add(all);
            selectItemList_067.add(all);
            for(CttInfo cttInfo: cttInfoList){
                if(EnumResType.RES_TYPE0.getCode().equals(cttInfo.getCttType())
                        ||EnumResType.RES_TYPE6.getCode().equals(cttInfo.getCttType())
                        ||EnumResType.RES_TYPE7.getCode().equals(cttInfo.getCttType())){
                    SelectItem selectItem067 = new SelectItem();
                    selectItem067.setValue(cttInfo.getPkid());
                    selectItem067.setLabel(cttInfo.getName());
                    selectItemList_067.add(selectItem067);
                }else if(EnumResType.RES_TYPE1.getCode().equals(cttInfo.getCttType())){
                    SelectItem selectItem1 = new SelectItem();
                    selectItem1.setValue(cttInfo.getPkid());
                    selectItem1.setLabel(cttInfo.getName());
                    selectItemList_1.add(selectItem1);
                }else if(EnumResType.RES_TYPE2.getCode().equals(cttInfo.getCttType())
                        ||EnumResType.RES_TYPE3.getCode().equals(cttInfo.getCttType())
                        ||EnumResType.RES_TYPE4.getCode().equals(cttInfo.getCttType())
                        ||EnumResType.RES_TYPE5.getCode().equals(cttInfo.getCttType())
                        ||EnumResType.RES_TYPE8.getCode().equals(cttInfo.getCttType())){
                    SelectItem selectItem23458 = new SelectItem();
                    selectItem23458.setValue(cttInfo.getPkid());
                    selectItem23458.setLabel(cttInfo.getName());
                    selectItemList_23458.add(selectItem23458);
                }
            }
        }
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

    /**
     * 2015-03-24追加
     * auto:hu
     * @return
     */
    public String onQueryFlowCtrlHisAction(String strQryMsgOutPara) {
        try {
            flowCtrlShow.setInfoType(flowCtrlHis.getInfoType());
            flowCtrlShow.setInfoPkid(flowCtrlHis.getInfoPkid());
            flowCtrlShow.setPeriodNo(flowCtrlHis.getPeriodNo());

            flowCtrlShowList = flowCtrlHisService.selectListByFlowCtrlHis(flowCtrlShow,strFinish_Flag);
            if(strQryMsgOutPara.equals("true")){
                if (flowCtrlShowList.isEmpty()) {
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
        if(StringUtils.isBlank(strCttType)){
            strRendered2 = "false";
            return;
        }else{
            strRendered2 = "true";
        }
        //hu 追加
        if("1".equals(strCttType)||"2".equals(strCttType)||"0".equals(strCttType)){
            Flag = false;
        }else{
            Flag = true;
        }//hu追加结束
        if(EnumResType.RES_TYPE0.getCode().equals(strCttType)
                ||EnumResType.RES_TYPE6.getCode().equals(strCttType)
                ||EnumResType.RES_TYPE7.getCode().equals(strCttType)){
                     esInitCtt2List = selectItemList_067;
                     strLabel2 = EnumResType.RES_TYPE0.getTitle();
        }else if(EnumResType.RES_TYPE1.getCode().equals(strCttType)){
                     esInitCtt2List = selectItemList_1;
                     strLabel2 = EnumResType.RES_TYPE1.getTitle();
        }else if(EnumResType.RES_TYPE2.getCode().equals(strCttType)
                ||EnumResType.RES_TYPE3.getCode().equals(strCttType)
                ||EnumResType.RES_TYPE4.getCode().equals(strCttType)
                ||EnumResType.RES_TYPE5.getCode().equals(strCttType)
                ||EnumResType.RES_TYPE8.getCode().equals(strCttType)){
                     esInitCtt2List = selectItemList_23458;
                     strLabel2 = EnumResType.RES_TYPE2.getTitle();
        }
        strRendered2 = "true";
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

    public List<FlowCtrlShow> getFlowCtrlShowList() {
        return flowCtrlShowList;
    }

    public void setFlowCtrlShowList(List<FlowCtrlShow> flowCtrlShowList) {
        this.flowCtrlShowList = flowCtrlShowList;
    }

    public FlowCtrlShow getFlowCtrlShow() {
        return flowCtrlShow;
    }

    public void setFlowCtrlShow(FlowCtrlShow flowCtrlShow) {
        this.flowCtrlShow = flowCtrlShow;
    }

    public boolean isFlag() {
        return Flag;
    }

    public void setFlag(boolean flag) {
        Flag = flag;
    }

    public List<SelectItem> getFinish_FlagList() {
        return finish_FlagList;
    }

    public void setFinish_FlagList(List<SelectItem> finish_FlagList) {
        this.finish_FlagList = finish_FlagList;
    }

    public String getStrFinish_Flag() {
        return strFinish_Flag;
    }

    public void setStrFinish_Flag(String strFinish_Flag) {
        this.strFinish_Flag = strFinish_Flag;
    }
}
