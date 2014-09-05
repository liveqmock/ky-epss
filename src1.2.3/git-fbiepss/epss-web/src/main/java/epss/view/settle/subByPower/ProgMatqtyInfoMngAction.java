package epss.view.settle.subByPower;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumAutoLinkFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.enums.ESEnumSubcttType;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsInitStl;
import epss.repository.model.model_show.CttInfoShow;
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
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgMatqtyInfoMngAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgMatqtyInfoMngAction.class);
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
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        this.progInfoShowList = new ArrayList<ProgInfoShow>();
        strStlType =ESEnum.ITEMTYPE4.getCode();

        resetAction();
        //自己拥有权限的分包合同
        List<OperResShow> operResShowListTemp =
                operResService.getInfoListByOperFlowPkid(
                        strStlType,
                        ESEnumStatusFlag.STATUS_FLAG0.getCode(),
                        ToolUtil.getOperatorManager().getOperatorId());
        subcttList = new ArrayList<SelectItem>();
        if (operResShowListTemp.size() > 0) {
            SelectItem selectItem = new SelectItem("", "全部");
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
        progInfoShowQry = new ProgInfoShow();
        progInfoShowSel = new ProgInfoShow();
        progInfoShowAdd = new ProgInfoShow();
        progInfoShowUpd = new ProgInfoShow();
        progInfoShowDel = new ProgInfoShow();
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
    }
    public void resetActionForAdd(){
        progInfoShowAdd =new ProgInfoShow();
    }

    public void setMaxNoPlusOne(String strQryTypePara){
        try {
            Integer intTemp;
            String strMaxId= progStlInfoService.getStrMaxStlId(strStlType);
            if(StringUtils .isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))){
                strMaxId="STLM"+ ToolUtil.getStrToday()+"001";
            }
            else{
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
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
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
            if (progInfoShowQry.getStlPkid() != null) {//有分包合同条件查询
                progInfoShowList.addAll(progInfoShowConstructsTemp);
            } else {                                 //无分包合同条件查询
                for (SelectItem itemUnit : subcttList) {
                    for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                        if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                            progInfoShowList.add(esISSOMPCUnit);
                        }
                    }
                }
            }
            if (strQryMsgOutPara.equals("true")) {
                if (progInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
        }
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   ProgInfoShow progInfoShowPara) {
        try {
            progInfoShowPara.setCreatedByName(ToolUtil.getUserName(progInfoShowPara.getCreatedBy()));
            progInfoShowPara.setLastUpdByName(ToolUtil.getUserName(progInfoShowPara.getLastUpdBy()));
            // 查询
            if (strPowerTypePara.equals("Qry")) {
                progInfoShowSel = (ProgInfoShow) BeanUtils.cloneBean(progInfoShowPara);
            } else if (strPowerTypePara.equals("Mng")) {// 维护
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
     * 必须输入项目检查
    */
    private boolean submitPreCheck(ProgInfoShow progInfoShow){
        if (StringUtils.isEmpty(progInfoShow.getId())){
            MessageUtil.addError("请输入结算编号！");
            return false;
        }
        else if (StringUtils.isEmpty(progInfoShow.getStlPkid())) {
            MessageUtil.addError("请输入分包合同！");
            return false;
        }else if (StringUtils.isEmpty(progInfoShow.getPeriodNo())){
            MessageUtil.addError("请输入期数编码！");
            return false;
        }
        return true ;
    }

    /**
     * 提交维护权限
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
                MessageUtil.addError("该记录已存在，请重新录入！");
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
            //判断是否已关联产生了分包数量结算
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
                        if((!("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getStatusFlag())))&&
                                (progInfoShowDel.getPeriodNo().equals(esISSOMPCUnit.getPeriodNo()))
                                &&(ESEnumAutoLinkFlag.AUTO_LINK_FLAG1.getCode()).equals(
                                ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getAutoLinkAdd()))){
                            MessageUtil.addInfo("该记录已关联分包数量结算，不可删除！");
                            return;
                        }else if(("").equals(ToolUtil.getStrIgnoreNull(esISSOMPCUnit.getStatusFlag()))&&
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
                esFlowService.selectSubcttStlQMByStatusFlagBegin_End(progInfoShowTemp);                               //无分包合同条件查询
        for (SelectItem itemUnit : subcttList) {
            for (ProgInfoShow esISSOMPCUnit : progInfoShowConstructsTemp) {
                if (itemUnit.getValue().equals(esISSOMPCUnit.getStlPkid())) {
                    progInfoShowList.add(esISSOMPCUnit);
                }
            }
        }
    }
    private void addRecordAction(ProgInfoShow progInfoShowPara){
        try {
            progStlInfoService.insertRecord(progInfoShowPara) ;
            progMatqtyItemService.setFromLastStageApproveDataToThisStageBeginData(progInfoShowPara);
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(ProgInfoShow progInfoShowPara){
        try {
            progStlInfoService.updateRecord(progInfoShowPara) ;
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void delRecordAction(ProgInfoShow progInfoShowPara){
        try {
            MessageUtil.addInfo(progStlInfoService.deleteStlMAndItemRecord(progInfoShowPara));
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteStlQAndItemRecordAction(ProgInfoShow progInfoShowPara) {
        try {
            progStlInfoService.deleteStlQAndItemRecord(progInfoShowPara);
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段Start*/
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

    public ProgInfoShow getProgInfoShowQry() {
        return progInfoShowQry;
    }

    public void setProgInfoShowQry(ProgInfoShow progInfoShowQry) {
        this.progInfoShowQry = progInfoShowQry;
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
}
