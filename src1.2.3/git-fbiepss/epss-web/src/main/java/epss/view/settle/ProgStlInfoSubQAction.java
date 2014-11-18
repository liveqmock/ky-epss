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
 * Time: 下午4:53
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
    //在某一成本计划下的分包合同
    private List<SelectItem> subcttList;

    private String strSubmitType;
    private String strStlType;
    /*控制维护画面层级部分的显示*/
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
            //在某一成本计划下的分包合同
            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                            EnumResType.RES_TYPE2.getCode()
                            , strCttInfoPkid
                            , EnumFlowStatus.FLOW_STATUS3.getCode());
            subcttList = new ArrayList<>();
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
        }catch (Exception e){
            logger.error("初始化失败", e);
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
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
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
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
        }
    }

    public void selectRecordAction(
                                   String strSubmitTypePara,
                                   ProgStlInfoShow progStlInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            progStlInfoShowPara.setCreatedByName(ToolUtil.getUserName(progStlInfoShowPara.getCreatedBy()));
            progStlInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progStlInfoShowPara.getLastUpdBy()));
            // 查询
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
     * 必须输入项目检查
     */
    private boolean submitPreCheck(ProgStlInfoShow progStlInfoShow) {
        if (StringUtils.isEmpty(progStlInfoShow.getId())) {
            MessageUtil.addError("请输入结算编号！");
            return false;
        } else if (StringUtils.isEmpty(progStlInfoShow.getStlPkid())) {
            MessageUtil.addError("请输入分包合同！");
            return false;
        }
        return true;
    }

    /**
     * 提交维护权限
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
                MessageUtil.addError("该记录已存在，请重新录入！");
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
            //判断是否已关联产生了分包材料结算
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
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void updRecordAction(ProgStlInfoShow progStlInfoShowPara) {
        try {
            progStlInfoService.updateRecord(progStlInfoShowPara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    /*智能字段Start*/
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
    /*智能字段End*/
}
