package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.service.*;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
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
public class ProgStlInfoSubStlmentAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoSubStlmentAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgStlInfoShow progStlInfoShow;
    private List<ProgStlInfoShow> progStlInfoShowList;
    private ProgStlInfoShow progStlInfoShowNotForm;
    private List<ProgStlInfoShow> progStlInfoShowNotFormList;
    private ProgStlInfoShow progStlInfoShowNotFormSelected;
    private ProgStlInfoShow progStlInfoShowSel;
    private ProgStlInfoShow progStlInfoShowAdd;
    private ProgStlInfoShow progStlInfoShowUpd;
    private ProgStlInfoShow progStlInfoShowDel;

    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String strRowSelectedFlag;
    private String strApprovedFlag;
	private String strStlType;
    private String strCttInfoPkid;

    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        progStlInfoShowList = new ArrayList<ProgStlInfoShow>();
        progStlInfoShowNotFormList = new ArrayList<ProgStlInfoShow>();
        strCttInfoPkid="";
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strCttInfoPkid")){
            strCttInfoPkid=parammap.get("strCttInfoPkid").toString();
        }
        strStlType = EnumResType.RES_TYPE5.getCode();
        resetAction();

        //在某一成本计划下的分包合同
        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                        EnumResType.RES_TYPE2.getCode()
                        , strCttInfoPkid
                        , EnumFlowStatus.FLOW_STATUS3.getCode());
        subcttList = new ArrayList<SelectItem>();
        if (cttInfoShowList.size() > 0) {
            SelectItem selectItem = new SelectItem("", "全部");
            subcttList.add(selectItem);
            for (CttInfoShow itemUnit : cttInfoShowList) {
                selectItem = new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
    }

    public void resetAction(){
        progStlInfoShow =new ProgStlInfoShow();
        progStlInfoShowNotForm =new ProgStlInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        strApprovedFlag="false";
        strRowSelectedFlag="false";
    }
    public void resetActionForAdd(){
        progStlInfoShowAdd =new ProgStlInfoShow();
        strSubmitType="Add";
        strRowSelectedFlag="false";
    }

    private String getMaxIdPlusOne(){
        try {
            Integer intTemp;
            String strMaxTkStlId= progStlInfoService.getStrMaxStlId(strStlType);
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxTkStlId))){
                strMaxTkStlId="STLP"+ ToolUtil.getStrToday()+"001";
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
            progStlInfoShowNotFormList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgStlInfoShow> progStlInfoShowListTemp =new ArrayList<>();
                List<ProgStlInfoShow> itemEsInitNotFormStlListTemp=
                        progStlInfoService.selectNotFormEsInitSubcttStlP(
                            strCttInfoPkid,
                            ToolUtil.getStrIgnoreNull(progStlInfoShowNotForm.getStlPkid()),
                            ToolUtil.getStrIgnoreNull(progStlInfoShowNotForm.getPeriodNo()));
                List<ProgStlInfoShow> selectFormPreEsInitSubcttStlP=
                        progStlInfoService.selectFormPreEsInitSubcttStlP(
                                strCttInfoPkid,
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getPeriodNo()));
                for(ProgStlInfoShow itemUnitAll:itemEsInitNotFormStlListTemp) {
                    Boolean isHasSame=false;
                    for(ProgStlInfoShow itemUnitForming:selectFormPreEsInitSubcttStlP){
                        if(itemUnitAll.getPkid().equals(itemUnitForming.getPkid())){
                            isHasSame=true;
                            break;
                        }
                    }
                    if(isHasSame.equals(false)){
                        progStlInfoShowListTemp.add(itemUnitAll);
                    }
                }

                ProgStlInfoShow progStlInfoShow1Temp;
                for(int i=0;i< progStlInfoShowListTemp.size();i++){
                    if(progStlInfoShowListTemp.get(i).getStlType().equals("3")){
                        progStlInfoShowListTemp.get(i).setId("分包数量结算("+ progStlInfoShowListTemp.get(i).getId()+")");
                    }else if(progStlInfoShowListTemp.get(i).getStlType().equals("4")){
                        progStlInfoShowListTemp.get(i).setId("分包材料结算("+ progStlInfoShowListTemp.get(i).getId()+")");
                    }
                    if(i==0){
                        progStlInfoShow1Temp =new ProgStlInfoShow();
                        progStlInfoShow1Temp.setPkid(i+"");
                        progStlInfoShow1Temp.setId("分包结算单");
                        progStlInfoShow1Temp.setStlName(progStlInfoShowListTemp.get(i).getStlName());
                        progStlInfoShow1Temp.setSignPartBName(progStlInfoShowListTemp.get(i).getSignPartBName());
                        progStlInfoShow1Temp.setPeriodNo(progStlInfoShowListTemp.get(i).getPeriodNo());
                        progStlInfoShowNotFormList.add(progStlInfoShow1Temp);
                        progStlInfoShowNotFormList.add(progStlInfoShowListTemp.get(0));
                    }else{
                        if(progStlInfoShowListTemp.get(i-1).getStlPkid().equals(
                                progStlInfoShowListTemp.get(i).getStlPkid())&&
                           progStlInfoShowListTemp.get(i-1).getPeriodNo().equals(
                                progStlInfoShowListTemp.get(i).getPeriodNo())
                        ){
                            progStlInfoShowNotFormList.add(progStlInfoShowListTemp.get(i));
                        }else{
                            progStlInfoShow1Temp =new ProgStlInfoShow();
                            progStlInfoShow1Temp.setPkid(i+"");
                            progStlInfoShow1Temp.setId("分包结算单");
                            progStlInfoShow1Temp.setStlName(progStlInfoShowListTemp.get(i).getStlName());
                            progStlInfoShow1Temp.setSignPartBName(progStlInfoShowListTemp.get(i).getSignPartBName());
                            progStlInfoShow1Temp.setPeriodNo(progStlInfoShowListTemp.get(i).getPeriodNo());
                            progStlInfoShowNotFormList.add(progStlInfoShow1Temp);
                            progStlInfoShowNotFormList.add(progStlInfoShowListTemp.get(i));
                        }
                    }
                }
            }else if(strQryFlag.equals("Qry")||strQryFlag.equals("Print")){

            }
            if (progStlInfoShowNotFormList.isEmpty()) {
                MessageUtil.addWarn("没有查询到数据。");
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }
    public void onQueryFormedAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progStlInfoShowList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgStlInfoShow> progStlInfoShowApprovedListTemp =
                        progStlInfoService.selectFormedEsInitSubcttStlP(
                                strCttInfoPkid,
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getPeriodNo()));
                for(int i=0;i< progStlInfoShowApprovedListTemp.size();i++){
                    if(progStlInfoShowApprovedListTemp.get(i).getFlowStatus().equals(EnumFlowStatus.FLOW_STATUS2.getCode())|| progStlInfoShowApprovedListTemp.get(i).getFlowStatus().equals(EnumFlowStatus.FLOW_STATUS3.getCode())){
                        progStlInfoShowList.add(progStlInfoShowApprovedListTemp.get(i));
                    }
                }
            }
            else if(strQryFlag.equals("Qry")){
                progStlInfoShowList =
                        progStlInfoService.getFormedAfterEsInitSubcttStlPList(
                                strCttInfoPkid,
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getPeriodNo())
                        );

            }
            else if(strQryFlag.equals("Account")){
                progStlInfoShowList =
                        progStlInfoService.getFormedAfterEsInitSubcttStlPList(
                                strCttInfoPkid,
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getPeriodNo())
                        );
            }
            if(strQryMsgOutPara.equals("true")){
                if (progStlInfoShowList.isEmpty()){
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public void selectRecordAction(String strPowerTypePara,String strSubmitTypePara,ProgStlInfoShow progStlInfoShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            strApprovedFlag="true";
            String strStatusFlagCode=ToolUtil.getStrIgnoreNull(progStlInfoShowPara.getFlowStatus());
            String strStatusFlagName= esFlowControl.getLabelByValueInStatusFlaglist(progStlInfoShowPara.getFlowStatus());
            if(strPowerTypePara.equals("Approve")){
                if(strStatusFlagCode.equals(EnumFlowStatus.FLOW_STATUS4.getCode())){
                    MessageUtil.addInfo("本期数据已经"+strStatusFlagName+"，您不能再进行编辑操作！");
                    return;
                }
                progStlInfoShow =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
                //编辑画面，设置只读项
                styleModel.setDisabled_Flag("true");
                if(EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfoShow.getFlowStatus())){
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
                progStlInfoShow =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
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

    public List<ProgStlInfoShow> getProgStlInfoShowList() {
        return progStlInfoShowList;
    }

    public void setProgStlInfoShowList(List<ProgStlInfoShow> progStlInfoShowList) {
        this.progStlInfoShowList = progStlInfoShowList;
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

    public ProgStlInfoShow getProgStlInfoShow() {
        return progStlInfoShow;
    }

    public void setProgStlInfoShow(ProgStlInfoShow progStlInfoShow) {
        this.progStlInfoShow = progStlInfoShow;
    }

    public ProgStlInfoShow getProgStlInfoShowNotForm() {
        return progStlInfoShowNotForm;
    }

    public void setProgStlInfoShowNotForm(ProgStlInfoShow progStlInfoShowNotForm) {
        this.progStlInfoShowNotForm = progStlInfoShowNotForm;
    }

    public ProgStlInfoShow getProgStlInfoShowNotFormSelected() {
        return progStlInfoShowNotFormSelected;
    }

    public void setProgStlInfoShowNotFormSelected(ProgStlInfoShow progStlInfoShowNotFormSelected) {
        this.progStlInfoShowNotFormSelected = progStlInfoShowNotFormSelected;
    }

    public List<ProgStlInfoShow> getProgStlInfoShowNotFormList() {
        return progStlInfoShowNotFormList;
    }

    public void setProgStlInfoShowNotFormList(List<ProgStlInfoShow> progStlInfoShowNotFormList) {
        this.progStlInfoShowNotFormList = progStlInfoShowNotFormList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
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

    public ProgStlInfoShow getProgStlInfoShowSel() {
        return progStlInfoShowSel;
    }

    public void setProgStlInfoShowSel(ProgStlInfoShow progStlInfoShowSel) {
        this.progStlInfoShowSel = progStlInfoShowSel;
    }

    public ProgStlInfoShow getProgStlInfoShowAdd() {
        return progStlInfoShowAdd;
    }

    public void setProgStlInfoShowAdd(ProgStlInfoShow progStlInfoShowAdd) {
        this.progStlInfoShowAdd = progStlInfoShowAdd;
    }

    public ProgStlInfoShow getProgStlInfoShowUpd() {
        return progStlInfoShowUpd;
    }

    public void setProgStlInfoShowUpd(ProgStlInfoShow progStlInfoShowUpd) {
        this.progStlInfoShowUpd = progStlInfoShowUpd;
    }

    public ProgStlInfoShow getProgStlInfoShowDel() {
        return progStlInfoShowDel;
    }

    public void setProgStlInfoShowDel(ProgStlInfoShow progStlInfoShowDel) {
        this.progStlInfoShowDel = progStlInfoShowDel;
    }
/*智能字段End*/
}
