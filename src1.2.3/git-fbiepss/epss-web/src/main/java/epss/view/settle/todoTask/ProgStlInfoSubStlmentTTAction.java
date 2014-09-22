package epss.view.settle.todoTask;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;

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
public class ProgStlInfoSubStlmentTTAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoSubStlmentTTAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
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

    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        progStlInfoShowList = new ArrayList<>();
        progStlInfoShowNotFormList = new ArrayList<>();
        strStlType = EnumResType.RES_TYPE5.getCode();
        resetAction();

        //�Լ�ӵ��Ȩ�޵ķְ���ͬ
        List<OperResShow> operResShowListTemp =
                operResService.getInfoListByOperFlowPkid(
                        strStlType,
                        EnumFlowStatus.FLOW_STATUS3.getCode());
        subcttList = new ArrayList<>();
        if (operResShowListTemp.size() > 0) {
            SelectItem selectItem = new SelectItem("", "ȫ��");
            subcttList.add(selectItem);
            for (OperResShow operResShowUnit : operResShowListTemp) {
                selectItem = new SelectItem();
                selectItem.setValue(operResShowUnit.getInfoPkid());
                selectItem.setLabel(operResShowUnit.getInfoPkidName());
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

    public void onQueryFormPreAction(String strQryFlag) {
        try {
            progStlInfoShowNotFormList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgStlInfoShow> progStlInfoShowListTemp =new ArrayList<>();
                List<ProgStlInfoShow> itemEsInitNotFormStlListTemp=
                        progStlInfoService.selectNotFormEsInitSubcttStlP(
                            "",
                            ToolUtil.getStrIgnoreNull(progStlInfoShowNotForm.getStlPkid()),
                            ToolUtil.getStrIgnoreNull(progStlInfoShowNotForm.getPeriodNo()));
                List<ProgStlInfoShow> selectFormPreEsInitSubcttStlP=
                        progStlInfoService.selectFormPreEsInitSubcttStlP(
                                "",
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
                        progStlInfoShowListTemp.get(i).setId("�ְ���������("+ progStlInfoShowListTemp.get(i).getId()+")");
                    }else if(progStlInfoShowListTemp.get(i).getStlType().equals("4")){
                        progStlInfoShowListTemp.get(i).setId("�ְ����Ͻ���("+ progStlInfoShowListTemp.get(i).getId()+")");
                    }
                    if(i==0){
                        progStlInfoShow1Temp =new ProgStlInfoShow();
                        progStlInfoShow1Temp.setPkid(i+"");
                        progStlInfoShow1Temp.setId("�ְ����㵥");
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
                            progStlInfoShow1Temp.setId("�ְ����㵥");
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
                MessageUtil.addWarn("û�в�ѯ�����ݡ�");
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }
    public void onQueryFormedAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            progStlInfoShowList.clear();
            if(strQryFlag.equals("Approve")){
                List<ProgStlInfoShow> progStlInfoShowApprovedListTemp =
                        progStlInfoService.selectFormedEsInitSubcttStlP(
                                "",
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
                                "",
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getPeriodNo())
                        );

            }
            else if(strQryFlag.equals("Account")){
                progStlInfoShowList =
                        progStlInfoService.getFormedAfterEsInitSubcttStlPList(
                                "",
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getStlPkid()),
                                ToolUtil.getStrIgnoreNull(progStlInfoShow.getPeriodNo())
                        );
            }
            if(strQryMsgOutPara.equals("true")){
                if (progStlInfoShowList.isEmpty()){
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
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
                    MessageUtil.addInfo("���������Ѿ�"+strStatusFlagName+"���������ٽ��б༭������");
                    return;
                }
                progStlInfoShow =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
                //�༭���棬����ֻ����
                styleModel.setDisabled_Flag("true");
                if(EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfoShow.getFlowStatus())){
                    strApprovedFlag="true";
                }else{
                    strApprovedFlag="false";
                }
            }else if(strPowerTypePara.equals("Print")){
                if(strStatusFlagCode.equals("")){
                    MessageUtil.addInfo("�������ݻ�δ��׼������ʱ���ܽ��д�ӡ������");
                    return;
                }
                //�༭���棬����ֻ����
                styleModel.setDisabled_Flag("true");
                progStlInfoShow =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ�Start*/

    public ProgStlItemSubQService getProgStlItemSubQService() {
        return progStlItemSubQService;
    }

    public void setProgStlItemSubQService(ProgStlItemSubQService progStlItemSubQService) {
        this.progStlItemSubQService = progStlItemSubQService;
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

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
/*�����ֶ�End*/
}
