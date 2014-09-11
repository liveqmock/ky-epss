package epss.view.settlePower;

import epss.common.enums.*;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsInitStl;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.ProgInfoShow;
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
public class SubcttMatqtyProgInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(SubcttMatqtyProgInfoAction.class);
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private ProgInfoShow progInfoShowQry;
    private ProgInfoShow progInfoShowSel;
    private ProgInfoShow progInfoShowAdd;
    private ProgInfoShow progInfoShowUpd;
    private ProgInfoShow progInfoShowDel;

    private List<ProgInfoShow> progInfoShowList;
    private List<SelectItem> subcttList;

    private String strStlType;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;
    private OperRes operRes;

    @PostConstruct
    public void init() {
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        progInfoShowList = new ArrayList<ProgInfoShow>();
        progInfoShowQry = new ProgInfoShow();
        progInfoShowSel = new ProgInfoShow();
        progInfoShowAdd = new ProgInfoShow();
        progInfoShowUpd = new ProgInfoShow();
        progInfoShowDel = new ProgInfoShow();

        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String strResPkidTemp = parammap.get("strResPkid").toString();
        operRes=operResService.getOperResByPkid(strResPkidTemp);

        strStlType = ESEnum.ITEMTYPE4.getCode();
        List<OperResShow> operResShowListTemp =
                operResService.getInfoListByOperFlowPkid(
                        strStlType,
                        ESEnumStatusFlag.STATUS_FLAG0.getCode());
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
        progInfoShowAdd =new ProgInfoShow();
        progInfoShowAdd.setStlPkid(operRes.getInfoPkid());
    }

    public void setMaxNoPlusOne(String strQryTypePara){
        try {
            Integer intTemp;
            String strMaxId= progStlInfoService.getStrMaxStlId(strStlType);
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLM"+ ToolUtil.getStrToday()+"001";
            } else {
                if(strMaxId .length()>3){
                    String strTemp=strMaxId.substring(strMaxId .length() -3).replaceFirst("^0+","");
                    if(ToolUtil.strIsDigit(strTemp)){
                        intTemp=Integer.parseInt(strTemp) ;
                        intTemp=intTemp+1;
                        strMaxId=strMaxId.substring(0,strMaxId.length()-3)+StringUtils.leftPad(intTemp.toString(),3,"0");
                    }else{
                        strMaxId+="001";
                    }
                }
            }
            if (strQryTypePara.equals("Qry")) {
                progInfoShowQry.setId(strMaxId);
            }else if (strQryTypePara.equals("Add")) {
                progInfoShowAdd.setId(strMaxId);
            }else if (strQryTypePara.equals("Upd")) {
                progInfoShowUpd.setId(strMaxId);
            }
        } catch (Exception e) {
            logger.error("������Ϣ��ѯʧ��", e);
            MessageUtil.addError("������Ϣ��ѯʧ��");
        }
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            progInfoShowQry.setStlType(strStlType);
            progInfoShowQry.setStrStatusFlagBegin(null);
            progInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
            this.progInfoShowList.clear();
            List<ProgInfoShow> progInfoShowConstructsTemp =
                    esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowQry);
            for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                for (SelectItem itemUnit : subcttList) {
                    if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                        progInfoShowList.add(esISSOMPCUnit);
                    }
                }
            }
            if (strQryMsgOutPara.equals("true")) {
                if (progInfoShowList.isEmpty()) {
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
                                   ProgInfoShow progInfoShowPara) {
        try {
            progInfoShowPara.setCreatedByName(ToolUtil.getUserName(progInfoShowPara.getCreatedBy()));
            progInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progInfoShowPara.getLastUpdBy()));
            // ��ѯ
            if (strPowerTypePara.equals("Qry")) {
                progInfoShowSel = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
            } else if (strPowerTypePara.equals("Mng")) {// ά��
                if (strSubmitTypePara.equals("Sel")) {
                    progInfoShowSel = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                } else if (strSubmitTypePara.equals("Add")) {
                } else if (strSubmitTypePara.equals("Upd")) {
                    progInfoShowUpd = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                } else if (strSubmitTypePara.equals("Del")) {
                    progInfoShowDel = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
                }
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * ����������Ŀ���
    */
    private boolean submitPreCheck(ProgInfoShow progInfoShow){
        if (StringUtils.isEmpty(progInfoShow.getId())){
            MessageUtil.addError("����������ţ�");
            return false;
        } else if (StringUtils.isEmpty(progInfoShow.getStlPkid())) {
            MessageUtil.addError("������ְ���ͬ��");
            return false;
        }else if (StringUtils.isEmpty(progInfoShow.getPeriodNo())){
            MessageUtil.addError("�������������룡");
            return false;
        }
        return true;
    }

    /**
     * �ύά��Ȩ��
     */
    public void onClickForMngAction(String strSubmitType){
        if(strSubmitType.equals("Add")){
            progInfoShowAdd.setStlType(strStlType);
            if(!submitPreCheck(progInfoShowAdd)){
                return;
            }
            List<EsInitStl> esInitStlListTemp =
                    progStlInfoService.getInitStlListByModelShow(progInfoShowAdd);
            if(esInitStlListTemp.size()>0) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                return;
            }
            String strTemp=esFlowService.subCttStlCheckForMng(
                    ESEnum.ITEMTYPE4.getCode(),
                    progInfoShowAdd.getStlPkid(),
                    progInfoShowAdd.getPeriodNo());
            if(!"".equals(strTemp)){
                MessageUtil.addError(strTemp);
                return;
            }else{
            	addRecordAction(progInfoShowAdd);
            	resetActionForAdd();
			}
        }
        else if(strSubmitType.equals("Upd")){
            progInfoShowUpd.setStlType(strStlType);
            updRecordAction(progInfoShowUpd) ;
        }
        else if(strSubmitType.equals("Del")){
            progInfoShowDel.setStlType(strStlType);
            //�ж��Ƿ��ѹ��������˷ְ���������
            EsCttInfo esCttInfoTemp=cttInfoService.getCttInfoByPkId(progInfoShowDel.getStlPkid());
            if ((ESEnumSubcttType.TYPE3.getCode()).equals(esCttInfoTemp.getType())||
                    (ESEnumSubcttType.TYPE6.getCode()).equals(esCttInfoTemp.getType())){
                ProgInfoShow progInfoShowQryQ=new ProgInfoShow();
                progInfoShowQryQ.setStlPkid(progInfoShowDel.getStlPkid());
                progInfoShowQryQ.setStlType(ESEnum.ITEMTYPE3.getCode());
                progInfoShowQryQ.setPeriodNo(progInfoShowDel.getPeriodNo());
                List<ProgInfoShow> progInfoShowConstructsTemp =
                        esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowQryQ);
                if (progInfoShowConstructsTemp.size()!=0){
                    for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                        if((!("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus())))&&
                                (progInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))
                                &&(ESEnumAutoLinkFlag.AUTO_LINK_FLAG1.getCode()).equals(
                                ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getAutoLinkAdd()))){
                            MessageUtil.addInfo("�ü�¼�ѹ����ְ��������㣬����ɾ����");
                            return;
                        }else if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getFlowStatus()))&&
                                (progInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))
                                &&(ESEnumAutoLinkFlag.AUTO_LINK_FLAG0.getCode()).equals(
                                ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getAutoLinkAdd()))){
                            deleteStlQAndItemRecordAction(esISSOMPCUnit);
                        }
                    }
                }
            }
            delRecordAction(progInfoShowDel);
        }
        ProgInfoShow progInfoShowTemp = new ProgInfoShow();
        progInfoShowTemp.setStlType(strStlType);
        progInfoShowTemp.setStrStatusFlagBegin(null);
        progInfoShowTemp.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
        this.progInfoShowList.clear();
        List<ProgInfoShow> progInfoShowConstructsTemp =
                esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowTemp);                               //�޷ְ���ͬ������ѯ
        for (SelectItem itemUnit : subcttList) {
            for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())
                        &&!("NULL".equals(esISSOMPCUnit.getPeriodNo()))) {
                    progInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }
    private void addRecordAction(ProgInfoShow progInfoShowPara){
        try {
            MessageUtil.addInfo(progStlInfoService.insertStlMAndItemRecordAction(progInfoShowPara));
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgInfoShow progInfoShowPara){
        try {
            progStlInfoService.updateRecord(progInfoShowPara) ;
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgInfoShow progInfoShowPara){
        try {
            MessageUtil.addInfo(progStlInfoService.deleteStlMAndItemRecord(progInfoShowPara));
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteStlQAndItemRecordAction(ProgInfoShow progInfoShowPara) {
        try {
            progStlInfoService.deleteStlQAndItemRecord(progInfoShowPara);
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ֶ�Start*/
    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public ProgInfoShow getProgInfoShowQry() {
        return progInfoShowQry;
    }

    public void setProgInfoShowQry(ProgInfoShow progInfoShowQry) {
        this.progInfoShowQry = progInfoShowQry;
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

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
    /*�����ֶ�End*/
}
