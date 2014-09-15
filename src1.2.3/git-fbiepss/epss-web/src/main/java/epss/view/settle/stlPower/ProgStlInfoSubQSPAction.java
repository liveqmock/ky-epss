package epss.view.settle.stlPower;

import epss.common.enums.*;
import epss.repository.model.CttInfo;
import epss.repository.model.ProgStlInfo;
import epss.repository.model.OperRes;
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
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlInfoSubQSPAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoSubQSPAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgStlInfoShow progStlInfoShowQry;
    private ProgStlInfoShow progStlInfoShowSel;
    private ProgStlInfoShow progStlInfoShowAdd;
    private ProgStlInfoShow progStlInfoShowUpd;
    private ProgStlInfoShow progStlInfoShowDel;

    private List<ProgStlInfoShow> progStlInfoShowList;

    private List<SelectItem> subcttList;

    private String strStlType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;
    private OperRes operRes;

    @PostConstruct
    public void init() {
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        progStlInfoShowList = new ArrayList<ProgStlInfoShow>();
        progStlInfoShowQry = new ProgStlInfoShow();
        progStlInfoShowSel = new ProgStlInfoShow();
        progStlInfoShowAdd = new ProgStlInfoShow();
        progStlInfoShowUpd = new ProgStlInfoShow();
        progStlInfoShowDel = new ProgStlInfoShow();

        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String strResPkidTemp = parammap.get("strResPkid").toString();
        operRes=operResService.getOperResByPkid(strResPkidTemp);

        strStlType = EnumResType.RES_TYPE3.getCode();
        List<OperResShow> operResShowListTemp =
                operResService.getInfoListByOperFlowPkid(
                        strStlType,
                        EnumFlowStatus.FLOW_STATUS0.getCode());
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

    public void resetActionForAdd(){
        progStlInfoShowAdd = new ProgStlInfoShow();
        progStlInfoShowAdd.setStlPkid(operRes.getInfoPkid());
    }

    public void setMaxNoPlusOne(String strQryTypePara) {
        try {
            Integer intTemp;
            String strMaxId = progStlInfoService.getStrMaxStlId(strStlType);
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "STLQ" + ToolUtil.getStrToday() + "001";
            } else {
                if (strMaxId.length() > 3) {
                    String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                    if (ToolUtil.strIsDigit(strTemp)) {
                        intTemp = Integer.parseInt(strTemp);
                        intTemp = intTemp + 1;
                        strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                    } else {
                        strMaxId += "001";
                    }
                }
            }
            if (strQryTypePara.equals("Qry")) {
                progStlInfoShowQry.setId(strMaxId);
            }else if (strQryTypePara.equals("Add")) {
                progStlInfoShowAdd.setId(strMaxId);
            }else if (strQryTypePara.equals("Upd")) {
                progStlInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            progStlInfoShowQry.setStlType(strStlType);
            progStlInfoShowQry.setStrStatusFlagBegin(null);
            progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
            this.progStlInfoShowList.clear();
            List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                    progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQry);
            for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                for (SelectItem itemUnit : subcttList) {
                    if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                        progStlInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if (strQryMsgOutPara.equals("true")) {
                if (progStlInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   ProgStlInfoShow progStlInfoShowPara) {
        try {
            progStlInfoShowPara.setCreatedByName(ToolUtil.getUserName(progStlInfoShowPara.getCreatedBy()));
            progStlInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progStlInfoShowPara.getLastUpdBy()));
            // ��ѯ
            if (strSubmitTypePara.equals("Sel")) {
                progStlInfoShowSel = (ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            } else if (strSubmitTypePara.equals("Add")) {
            } else if (strSubmitTypePara.equals("Upd")) {
                progStlInfoShowUpd = (ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                progStlInfoShowDel = (ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
     */
    private boolean submitPreCheck(ProgStlInfoShow progStlInfoShow) {
        if (StringUtils.isEmpty(progStlInfoShow.getId())) {
            MessageUtil.addError("����������ţ�");
            return false;
        } else if (StringUtils.isEmpty(progStlInfoShow.getStlPkid())) {
            MessageUtil.addError("������ְ���ͬ��");
            return false;
        }else if (StringUtils.isEmpty(progStlInfoShow.getPeriodNo())){
            MessageUtil.addError("�������������룡");
            return false;
        }
        return true;
    }

    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction(String strSubmitType) {
        if (strSubmitType.equals("Add")) {
            progStlInfoShowAdd.setStlType(strStlType);
            if (!submitPreCheck(progStlInfoShowAdd)) {
                return;
            }
            List<ProgStlInfo> progStlInfoListTemp =
                    progStlInfoService.getInitStlListByModelShow(progStlInfoShowAdd);
            if(progStlInfoListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp = progStlInfoService.subCttStlCheckForMng(
                    EnumResType.RES_TYPE3.getCode(),
                    progStlInfoShowAdd.getStlPkid(),
                    progStlInfoShowAdd.getPeriodNo());
            if (!"".equals(strTemp)) {
                MessageUtil.addError(strTemp);
                return;
            }else{
                addRecordAction(progStlInfoShowAdd);
                resetActionForAdd();
                if(!EnumTaskDoneFlag.TASK_DONE_FLAG1.getCode().equals(operRes.getTaskdoneFlag())){
                    operRes.setTaskdoneFlag(EnumTaskDoneFlag.TASK_DONE_FLAG1.getCode());
                   operResService.updateRecord(operRes);
                }
            }
        } else if (strSubmitType.equals("Upd")) {
            progStlInfoShowUpd.setStlType(strStlType);
            updRecordAction(progStlInfoShowUpd);
            if(!EnumTaskDoneFlag.TASK_DONE_FLAG1.getCode().equals(operRes.getTaskdoneFlag())){
                operRes.setTaskdoneFlag(EnumTaskDoneFlag.TASK_DONE_FLAG1.getCode());
                operResService.updateRecord(operRes);
            }
        } else if (strSubmitType.equals("Del")) {
            progStlInfoShowDel.setStlType(strStlType);
            //�ж��Ƿ��ѹ��������˷ְ����Ͻ���
            CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId( progStlInfoShowDel.getStlPkid());
            if ((EnumSubcttType.TYPE3.getCode()).equals(cttInfoTemp.getType())||
                    (EnumSubcttType.TYPE6.getCode()).equals(cttInfoTemp.getType())){
                ProgStlInfoShow progStlInfoShowQryM =new ProgStlInfoShow();
                progStlInfoShowQryM.setStlPkid( progStlInfoShowDel.getStlPkid());
                progStlInfoShowQryM.setStlType(EnumResType.RES_TYPE4.getCode());
                progStlInfoShowQryM.setPeriodNo( progStlInfoShowDel.getPeriodNo());
                List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                        progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQryM);
                if (progStlInfoShowConstructsTemp.size()!=0){
                    for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                        if((!("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus())))&&
                                (progStlInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))
                                &&(EnumAutoLinkFlag.AUTO_LINK_FLAG1.getCode()).equals(
                                ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getAutoLinkAdd()))){
                            MessageUtil.addInfo("�ü�¼�ѹ����ְ����Ͻ��㣬����ɾ����");
                            return;
                        }else if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))&&
                                (progStlInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))
                                &&(EnumAutoLinkFlag.AUTO_LINK_FLAG0.getCode()).equals(
                                ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getAutoLinkAdd()))){
                            deleteStlMAndItemRecordAction(esISSOMPCUnit);
                        }
                    }
                }
            }
            delRecordAction(progStlInfoShowDel);
        }
        ProgStlInfoShow progStlInfoShowTemp = new ProgStlInfoShow();
        progStlInfoShowTemp.setStlType(strStlType);
        progStlInfoShowTemp.setStrStatusFlagBegin(null);
        progStlInfoShowTemp.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
        this.progStlInfoShowList.clear();
        List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowTemp);//�޷ְ���ͬ������ѯ
        for (SelectItem itemUnit : subcttList) {
            for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
                if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                    progStlInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }
    private void addRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        try {
            MessageUtil.addInfo(progStlInfoService.insertStlQAndItemRecordAction(progStlInfoShowPara));
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        try {
            progStlInfoService.updateRecord(progStlInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        try {
            MessageUtil.addInfo(progStlInfoService.deleteStlQAndItemRecord(progStlInfoShowPara));
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteStlMAndItemRecordAction(ProgStlInfoShow progStlInfoShowPara){
        try {
            progStlInfoService.deleteStlMAndItemRecord(progStlInfoShowPara);
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ�Start*/
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

    public void setSubcttList(List<SelectItem> subcttList) {
        this.subcttList = subcttList;
    }

    public ProgStlInfoShow getProgStlInfoShowQry() {
        return progStlInfoShowQry;
    }

    public void setProgStlInfoShowQry(ProgStlInfoShow progStlInfoShowQry) {
        this.progStlInfoShowQry = progStlInfoShowQry;
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

    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
    /*�����ֶ�End*/
}
