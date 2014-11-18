package epss.view.settle;

import epss.common.enums.*;
import epss.repository.model.CttInfo;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.ProgStlInfoShow;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.repository.model.ProgStlInfo;
import epss.service.*;
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
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlInfoSubQAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlInfoSubQAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{progStlItemSubMService}")
    private ProgStlItemSubMService progStlItemSubMService;
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
    //��ĳһ�ɱ��ƻ��µķְ���ͬ
    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String strStlType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        try {
            this.progStlInfoShowList = new ArrayList<>();
            String strCttInfoPkid = "";
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            if (parammap.containsKey("strCttInfoPkid")) {
                strCttInfoPkid = parammap.get("strCttInfoPkid").toString();
            }
            strStlType = EnumResType.RES_TYPE3.getCode();

            resetAction();
            //��ĳһ�ɱ��ƻ��µķְ���ͬ
            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                            EnumResType.RES_TYPE2.getCode()
                            , strCttInfoPkid
                            , EnumFlowStatus.FLOW_STATUS3.getCode());
            subcttList = new ArrayList<>();
            if (cttInfoShowList.size() > 0) {
                SelectItem selectItem = new SelectItem("", "ȫ��");
                subcttList.add(selectItem);
                for (CttInfoShow itemUnit : cttInfoShowList) {
                    selectItem = new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    subcttList.add(selectItem);
                }
            }
        }catch (Exception e){
            logger.error("��ʼ��ʧ��", e);
        }
    }

    public void resetAction() {
        progStlInfoShowQry = new ProgStlInfoShow();
        progStlInfoShowSel = new ProgStlInfoShow();
        progStlInfoShowAdd = new ProgStlInfoShow();
        progStlInfoShowUpd = new ProgStlInfoShow();
        progStlInfoShowDel = new ProgStlInfoShow();
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "Add";
    }

    public void resetActionForAdd() {
        progStlInfoShowAdd = new ProgStlInfoShow();
        strSubmitType = "Add";
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

    public void onQueryAction(String strQryFlag, String strQryMsgOutPara) {
        try {
            progStlInfoShowQry.setStlType(strStlType);
            if (strQryFlag.equals("Qry")) {
                progStlInfoShowQry.setStrStatusFlagBegin(progStlInfoShowQry.getFlowStatus());
                progStlInfoShowQry.setStrStatusFlagEnd(progStlInfoShowQry.getFlowStatus());
            } else if (strQryFlag.equals("Mng")) {
                progStlInfoShowQry.setStrStatusFlagBegin(null);
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
            } else if (strQryFlag.equals("Check")) {
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
            } else if (strQryFlag.equals("DoubleCheck")) {
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
            } else if (strQryFlag.equals("Approve")) {
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            } else if (strQryFlag.equals("Print")) {
                progStlInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                progStlInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
            }
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
            logger.error("�ܰ���ͬ��Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ܰ���ͬ��Ϣ��ѯʧ��");
        }
    }

    public void selectRecordAction(
                                   String strSubmitTypePara,
                                   ProgStlInfoShow progStlInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            progStlInfoShowPara.setCreatedByName(ToolUtil.getUserName(progStlInfoShowPara.getCreatedBy()));
            progStlInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progStlInfoShowPara.getLastUpdBy()));
            // ��ѯ
            if (strSubmitTypePara.equals("Sel")) {
                progStlInfoShowSel = (ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
            } else if (strSubmitTypePara.equals("Add")) {
                progStlInfoShowAdd =(ProgStlInfoShow) BeanUtils.cloneBean(progStlInfoShowPara);
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
        }
        return true;
    }

    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Add")) {
            progStlInfoShowAdd.setStlType(strStlType);
            if (!submitPreCheck(progStlInfoShowAdd)) {
                return;
            }
            List<ProgStlInfo> progStlInfoListTemp =
                    progStlInfoService.getInitStlListByModelShow(progStlInfoShowAdd);
            if (progStlInfoListTemp.size() > 0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp = progStlInfoService.progStlInfoMngPreCheck(progStlInfoShowAdd);
            if (!"".equals(strTemp)) {
                MessageUtil.addError(strTemp);
                return;
            }else{
                addRecordAction(progStlInfoShowAdd);
                resetActionForAdd();
            }
        } else if (strSubmitType.equals("Upd")) {
            progStlInfoShowUpd.setStlType(strStlType);
            if (!submitPreCheck(progStlInfoShowUpd)) {
                return;
            }
            updRecordAction(progStlInfoShowUpd);
        } else if (strSubmitType.equals("Del")) {
            progStlInfoShowDel.setStlType(strStlType);
            //�ж��Ƿ��ѹ��������˷ְ����Ͻ���
            CttInfo cttInfoTemp =cttInfoService.getCttInfoByPkId( progStlInfoShowDel.getStlPkid());
            if ((EnumSubcttType.TYPE3.getCode()).equals(cttInfoTemp.getType())
                    ||(EnumSubcttType.TYPE6.getCode()).equals(cttInfoTemp.getType())){
                ProgStlInfoShow progStlInfoShowQryM =new ProgStlInfoShow();
                progStlInfoShowQryM.setStlPkid( progStlInfoShowDel.getStlPkid());
                progStlInfoShowQryM.setStlType(EnumResType.RES_TYPE4.getCode());
                progStlInfoShowQryM.setPeriodNo( progStlInfoShowDel.getPeriodNo());
                List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                        progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowQryM);

            }
        }
        ProgStlInfoShow progStlInfoShowTemp = new ProgStlInfoShow();
        progStlInfoShowTemp.setStlType(strStlType);
        progStlInfoShowTemp.setStrStatusFlagBegin(null);
        progStlInfoShowTemp.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
        this.progStlInfoShowList.clear();
        List<ProgStlInfoShow> progStlInfoShowConstructsTemp =
                progStlInfoService.selectSubcttStlQMByStatusFlagBegin_End(progStlInfoShowTemp);

        for (ProgStlInfoShow esISSOMPCUnit : progStlInfoShowConstructsTemp) {
            for (SelectItem itemUnit : subcttList) {
                if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                    progStlInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }

    private void addRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        try {
            progStlInfoService.insertRecord(progStlInfoShowPara);
            progStlItemSubQService.setFromLastStageAddUpToDataToThisStageBeginData(progStlInfoShowPara);
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

    public String getStrSubmitType() {
        return strSubmitType;
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

    public ProgStlItemSubMService getProgStlItemSubMService() {
        return progStlItemSubMService;
    }

    public void setProgStlItemSubMService(ProgStlItemSubMService progStlItemSubMService) {
        this.progStlItemSubMService = progStlItemSubMService;
    }
    /*�����ֶ�End*/
}
