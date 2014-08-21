package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgInfoShow;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.service.CttInfoService;
import epss.service.FlowCtrlService;
import epss.service.ProgStlInfoService;
import epss.service.ProgWorkqtyItemService;
import epss.service.EsFlowService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgSubstlInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgSubstlInfoAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;

    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgInfoShow progInfoShow;
    private List<ProgInfoShow> progInfoShowList;
    private ProgInfoShow progInfoShowNotForm;
    private List<ProgInfoShow> progInfoShowNotFormList;
    private ProgInfoShow progInfoShowNotFormSelected;
    private ProgInfoShow progInfoShowSel;
    private ProgInfoShow progInfoShowAdd;
    private ProgInfoShow progInfoShowUpd;
    private ProgInfoShow progInfoShowDel;

    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String strRowSelectedFlag;
    private String strApprovedFlag;
	private String strStlType;
    private String strCstplInfoPkid;

    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        progInfoShowList = new ArrayList<ProgInfoShow>();
        progInfoShowNotFormList = new ArrayList<ProgInfoShow>();
        strCstplInfoPkid="";
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strCstplInfoPkid")){
            strCstplInfoPkid=parammap.get("strCstplInfoPkid").toString();
        }
        strStlType =ESEnum.ITEMTYPE5.getCode();
        resetAction();

        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                        ESEnum.ITEMTYPE2.getCode()
                        ,strCstplInfoPkid
                        ,ESEnumStatusFlag.STATUS_FLAG3.getCode());
        subcttList=new ArrayList<SelectItem>();
        if(cttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","全部");
            subcttList.add(selectItem);
            for(CttInfoShow itemUnit: cttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progInfoShow =new ProgInfoShow();
        progInfoShowNotForm =new ProgInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        esFlowControl.getBackToStatusFlagList("Qry");
        strApprovedFlag="false";
        strRowSelectedFlag="false";
    }
    public void resetActionForAdd(){
        progInfoShowAdd =new ProgInfoShow();
        strSubmitType="Add";
        strRowSelectedFlag="false";
    }

    private String getMaxIdPlusOne(){
        try {
            Integer intTemp;
            String strMaxTkStlId= progStlInfoService.getStrMaxStlId(strStlType);
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxTkStlId))){
                strMaxTkStlId="STLP"+ esCommon.getStrToday()+"001";
            }
            else{
                if(strMaxTkStlId .length()>3){
                    String strTemp=strMaxTkStlId.substring(strMaxTkStlId .length() -3).replaceFirst("^0+","");
                    if(ToolUtil.strIsDigit(strTemp)){
                        intTemp=Integer.parseInt(strTemp) ;
                        intTemp=intTemp+1;
                        strMaxTkStlId=strMaxTkStlId.substring(0,strMaxTkStlId.length()-3)+StringUtils.leftPad(intTemp.toString(),3,"0");
                    }else{
                        strMaxTkStlId+="001";
                    }
                }
            }
            return strMaxTkStlId ;
        } catch (Exception e) {
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
            return "";
        }
    }

    public void onQueryFormPreAction(String strQryFlag) {
        try {
            progInfoShowNotFormList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgInfoShow> progInfoShowListTemp =new ArrayList<>();
                List<ProgInfoShow> itemEsInitNotFormStlListTemp=
                    esFlowService.selectNotFormEsInitSubcttStlP(
                            strCstplInfoPkid,
                            ToolUtil.getStrIgnoreNull(progInfoShowNotForm.getStlPkid()),
                            ToolUtil.getStrIgnoreNull(progInfoShowNotForm.getPeriodNo()));
                List<ProgInfoShow> selectFormPreEsInitSubcttStlP=
                        esFlowService.selectFormPreEsInitSubcttStlP(
                                strCstplInfoPkid,
                                ToolUtil.getStrIgnoreNull(progInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progInfoShow.getPeriodNo()));
                for(ProgInfoShow itemUnitAll:itemEsInitNotFormStlListTemp) {
                    Boolean isHasSame=false;
                    for(ProgInfoShow itemUnitForming:selectFormPreEsInitSubcttStlP){
                        if(itemUnitAll.getPkid().equals(itemUnitForming.getPkid())){
                            isHasSame=true;
                            break;
                        }
                    }
                    if(isHasSame.equals(false)){
                        progInfoShowListTemp.add(itemUnitAll);
                    }
                }

                ProgInfoShow progInfoShow1Temp;
                for(int i=0;i< progInfoShowListTemp.size();i++){
                    if(progInfoShowListTemp.get(i).getStlType().equals("3")){
                        progInfoShowListTemp.get(i).setId("分包数量结算("+ progInfoShowListTemp.get(i).getId()+")");
                    }else if(progInfoShowListTemp.get(i).getStlType().equals("4")){
                        progInfoShowListTemp.get(i).setId("分包材料结算("+ progInfoShowListTemp.get(i).getId()+")");
                    }
                    if(i==0){
                        progInfoShow1Temp =new ProgInfoShow();
                        progInfoShow1Temp.setPkid(i+"");
                        progInfoShow1Temp.setId("分包结算单");
                        progInfoShow1Temp.setStlName(progInfoShowListTemp.get(i).getStlName());
                        progInfoShow1Temp.setSignPartBName(progInfoShowListTemp.get(i).getSignPartBName());
                        progInfoShow1Temp.setPeriodNo(progInfoShowListTemp.get(i).getPeriodNo());
                        progInfoShowNotFormList.add(progInfoShow1Temp);
                        progInfoShowNotFormList.add(progInfoShowListTemp.get(0));
                    }else{
                        if(progInfoShowListTemp.get(i-1).getStlPkid().equals(
                                progInfoShowListTemp.get(i).getStlPkid())&&
                           progInfoShowListTemp.get(i-1).getPeriodNo().equals(
                                progInfoShowListTemp.get(i).getPeriodNo())
                        ){
                            progInfoShowNotFormList.add(progInfoShowListTemp.get(i));
                        }else{
                            progInfoShow1Temp =new ProgInfoShow();
                            progInfoShow1Temp.setPkid(i+"");
                            progInfoShow1Temp.setId("分包结算单");
                            progInfoShow1Temp.setStlName(progInfoShowListTemp.get(i).getStlName());
                            progInfoShow1Temp.setSignPartBName(progInfoShowListTemp.get(i).getSignPartBName());
                            progInfoShow1Temp.setPeriodNo(progInfoShowListTemp.get(i).getPeriodNo());
                            progInfoShowNotFormList.add(progInfoShow1Temp);
                            progInfoShowNotFormList.add(progInfoShowListTemp.get(i));
                        }
                    }
                }
            }else if(strQryFlag.equals("Qry")||strQryFlag.equals("Print")){

            }
            if (progInfoShowNotFormList.isEmpty()) {
                MessageUtil.addWarn("没有查询到数据。");
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }
    public void onQueryFormedAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progInfoShowList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgInfoShow> progInfoShowApprovedListTemp =
                        esFlowService.selectFormedEsInitSubcttStlP(
                                strCstplInfoPkid,
                                ToolUtil.getStrIgnoreNull(progInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progInfoShow.getPeriodNo()));
                for(int i=0;i< progInfoShowApprovedListTemp.size();i++){
                    if(progInfoShowApprovedListTemp.get(i).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG2.getCode())||progInfoShowApprovedListTemp.get(i).getStatusFlag().equals(ESEnumStatusFlag.STATUS_FLAG3.getCode())){
                        progInfoShowList.add(progInfoShowApprovedListTemp.get(i));
                    }
                }
            }
            else if(strQryFlag.equals("Qry")){
                progInfoShowList =
                        esFlowService.getFormedAfterEsInitSubcttStlPList(
                                strCstplInfoPkid,
                                ToolUtil.getStrIgnoreNull(progInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progInfoShow.getPeriodNo())
                        );

            }
            else if(strQryFlag.equals("Account")){
                progInfoShowList =
                        esFlowService.getFormedAfterEsInitSubcttStlPList(
                                strCstplInfoPkid,
                                ToolUtil.getStrIgnoreNull(progInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progInfoShow.getPeriodNo())
                        );
            }
            if(strQryMsgOutPara.equals("true")){
                if (progInfoShowList.isEmpty()){
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public void selectRecordAction(String strPowerTypePara,String strSubmitTypePara,ProgInfoShow progInfoShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            strApprovedFlag="true";
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progInfoShowPara.getStatusFlag());
            String strStatusFlagName= esFlowControl.getLabelByValueInStatusFlaglist(progInfoShowPara.getStatusFlag());
            if(strPowerTypePara.equals("Approve")){
                if(strStatusFlagCode.equals(ESEnumStatusFlag .STATUS_FLAG4.getCode())){
                    MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您不能再进行编辑操作！");
                    return;
                }
                progInfoShow =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                //编辑画面，设置只读项
                styleModel.setDisabled_Flag("true");
                if(ESEnumStatusFlag.STATUS_FLAG2.getCode().equals(progInfoShow.getStatusFlag())){
                    strApprovedFlag="true";
                }else{
                    strApprovedFlag="false";
                }
            }else if(strPowerTypePara.equals("Print")){
                if(strStatusFlagCode.equals("")){
                    MessageUtil.addInfo("本期数据还未批准，您暂时不能进行打印操作！");
                    return;
                }
                //编辑画面，设置只读项
                styleModel.setDisabled_Flag("true");
                progInfoShow =(ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段Start*/

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public List<ProgInfoShow> getProgInfoShowList() {
        return progInfoShowList;
    }

    public void setProgInfoShowList(List<ProgInfoShow> progInfoShowList) {
        this.progInfoShowList = progInfoShowList;
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

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ProgInfoShow getProgInfoShow() {
        return progInfoShow;
    }

    public void setProgInfoShow(ProgInfoShow progInfoShow) {
        this.progInfoShow = progInfoShow;
    }

    public ProgInfoShow getProgInfoShowNotForm() {
        return progInfoShowNotForm;
    }

    public void setProgInfoShowNotForm(ProgInfoShow progInfoShowNotForm) {
        this.progInfoShowNotForm = progInfoShowNotForm;
    }

    public ProgInfoShow getProgInfoShowNotFormSelected() {
        return progInfoShowNotFormSelected;
    }

    public void setProgInfoShowNotFormSelected(ProgInfoShow progInfoShowNotFormSelected) {
        this.progInfoShowNotFormSelected = progInfoShowNotFormSelected;
    }

    public List<ProgInfoShow> getProgInfoShowNotFormList() {
        return progInfoShowNotFormList;
    }

    public void setProgInfoShowNotFormList(List<ProgInfoShow> progInfoShowNotFormList) {
        this.progInfoShowNotFormList = progInfoShowNotFormList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getStrCstplInfoPkid() {
        return strCstplInfoPkid;
    }

    public void setStrCstplInfoPkid(String strCstplInfoPkid) {
        this.strCstplInfoPkid = strCstplInfoPkid;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public List<SelectItem> getSubcttList() {
        return subcttList;
    }

    public String getStrRowSelectedFlag() {
        return strRowSelectedFlag;
    }

    public String getStrApprovedFlag() {
        return strApprovedFlag;
    }

    public ProgInfoShow getProgInfoShowSel() {
        return progInfoShowSel;
    }

    public void setProgInfoShowSel(ProgInfoShow progInfoShowSel) {
        this.progInfoShowSel = progInfoShowSel;
    }

    public ProgInfoShow getProgInfoShowAdd() {
        return progInfoShowAdd;
    }

    public void setProgInfoShowAdd(ProgInfoShow progInfoShowAdd) {
        this.progInfoShowAdd = progInfoShowAdd;
    }

    public ProgInfoShow getProgInfoShowUpd() {
        return progInfoShowUpd;
    }

    public void setProgInfoShowUpd(ProgInfoShow progInfoShowUpd) {
        this.progInfoShowUpd = progInfoShowUpd;
    }

    public ProgInfoShow getProgInfoShowDel() {
        return progInfoShowDel;
    }

    public void setProgInfoShowDel(ProgInfoShow progInfoShowDel) {
        this.progInfoShowDel = progInfoShowDel;
    }

    /*智能字段End*/
}
