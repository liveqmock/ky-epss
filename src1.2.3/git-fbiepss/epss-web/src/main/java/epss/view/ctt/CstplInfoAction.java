package epss.view.ctt;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.CttInfo;
import epss.repository.model.model_show.CttInfoShow;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;
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
import javax.faces.context.FacesContext;
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
public class CstplInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplInfoAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")

    private EsFlowControl esFlowControl;
    private CttInfoShow cttInfoShowQry;
    private String strNotPassToStatus;
    private CttInfoShow cttInfoShowSelected;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;
    private List<CttInfoShow> cttInfoShowList;
    private String strSubmitType;
    private String rowSelectedFlag;

    // 画面之间传递过来的参数
    private String strBelongToPkid;
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        try {
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            // 从总包合同传递过来的总包合同号
            if (parammap.containsKey("strCttInfoPkid")) {
                strBelongToPkid = parammap.get("strCttInfoPkid").toString();
            }
            initData();
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }
    public void initData() {
        try {
            this.cttInfoShowList = new ArrayList<CttInfoShow>();
            cttInfoShowQry = new CttInfoShow();
            cttInfoShowQry.setCttType(EnumResType.RES_TYPE1.getCode());
            cttInfoShowQry.setParentPkid(strBelongToPkid);
            cttInfoShowSel = new CttInfoShow();
            cttInfoShowSel.setCttType(EnumResType.RES_TYPE1.getCode());
            cttInfoShowSel.setParentPkid(strBelongToPkid);
            cttInfoShowAdd = new CttInfoShow();
            cttInfoShowAdd.setCttType(EnumResType.RES_TYPE1.getCode());
            cttInfoShowAdd.setParentPkid(strBelongToPkid);
            cttInfoShowUpd = new CttInfoShow();
            cttInfoShowUpd.setCttType(EnumResType.RES_TYPE1.getCode());
            cttInfoShowUpd.setParentPkid(strBelongToPkid);
            cttInfoShowDel = new CttInfoShow();
            cttInfoShowDel.setCttType(EnumResType.RES_TYPE1.getCode());
            cttInfoShowDel.setParentPkid(strBelongToPkid);
            styleModel = new StyleModel();
            styleModel.setDisabled_Flag("false");
            strSubmitType = "Add";
            rowSelectedFlag = "false";
        }catch (Exception e) {
            logger.error("初始化失败", e);
            MessageUtil.addError("初始化失败");
        }
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(EnumResType.RES_TYPE1.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "CSTPL" + ToolUtil.getStrToday() + "001";
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
            cttInfoShowAdd.setId(strMaxId);
            cttInfoShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public String onQueryAction(String strQryFlag,String strQryMsgOutPara) {
        try {
            if (strQryFlag.equals("Qry")) {

            } else if (strQryFlag.contains("Mng")) {
                cttInfoShowQry.setStrStatusFlagBegin(null);
                cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS0.getCode());
            } else if (strQryFlag.contains("Check")) {
                if (strQryFlag.contains("DoubleCheck")) {
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS1.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS2.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS0.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS1.getCode());
                }
            }  else if (strQryFlag.contains("Approve")) {
                if (strQryFlag.equals("ApprovedQry")) {
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS3.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                }else{
                    cttInfoShowQry.setStrStatusFlagBegin(EnumFlowStatus.FLOW_STATUS2.getCode());
                    cttInfoShowQry.setStrStatusFlagEnd(EnumFlowStatus.FLOW_STATUS3.getCode());
                }
            }
            this.cttInfoShowList.clear();
            cttInfoShowQry.setParentPkid(strBelongToPkid);
            cttInfoShowList = cttInfoService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
            if(strQryMsgOutPara.equals("true")){
                if (cttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
            rowSelectedFlag = "false";
        } catch (Exception e) {
            logger.error("合同信息查询失败", e);
            MessageUtil.addError("合同信息查询失败");
        }
        return null;
    }

    public CttInfo getCttInfoByPkId(String strPkid) {
        return cttInfoService.getCttInfoByPkId(strPkid);
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(EnumResType.RES_TYPE1.getCode());
        cttInfoShowAdd.setParentPkid(strBelongToPkid);
        rowSelectedFlag = "false";
    }

    public void selectRecordAction(
                  String strSubmitTypePara,
                  CttInfoShow cttInfoShowSelected) {
        try {
            strSubmitType = strSubmitTypePara;
            cttInfoShowSelected.setCreatedByName(cttInfoService.getUserName(cttInfoShowSelected.getCreatedBy()));
            cttInfoShowSelected.setLastUpdByName(cttInfoService.getUserName(cttInfoShowSelected.getLastUpdBy()));
            if (strSubmitTypePara.equals("Sel")) {
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                rowSelectedFlag = "true";
            } else if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = new CttInfoShow();
                cttInfoShowAdd.setParentPkid(strBelongToPkid);
                rowSelectedFlag = "false";
            } else if (strSubmitTypePara.equals("Upd")) {
                cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                rowSelectedFlag = "false";
            } else if (strSubmitTypePara.equals("Del")) {
                cttInfoShowDel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                rowSelectedFlag = "false";
            }else{
                cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowSelected);
                rowSelectedFlag = "true";
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShow) {
        if (StringUtils.isEmpty(cttInfoShow.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShow.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShow.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        return true;
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        if (strSubmitType.equals("Add")) {
            cttInfoShowAdd.setCttType(EnumResType.RES_TYPE1.getCode());
            if (!submitPreCheck(cttInfoShowAdd)) {
                return;
            }
            CttInfoShow cttInfoShowTemp=new CttInfoShow();
            cttInfoShowTemp.setCttType(cttInfoShowAdd.getCttType());
            cttInfoShowTemp.setName(cttInfoShowAdd.getName());
            if (cttInfoService.getListByModelShow(cttInfoShowTemp).size()>0) {
                MessageUtil.addError("该记录已存在，请重新录入！");
                return;
            } else {
                try {
                    if (cttInfoShowAdd.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                        cttInfoShowAdd.setParentPkid("ROOT");
                    }
                    cttInfoService.insertRecord(cttInfoShowAdd);
                    MessageUtil.addInfo("新增数据完成。");
                    resetActionForAdd();
                } catch (Exception e) {
                    logger.error("新增数据失败，", e);
                    MessageUtil.addError(e.getMessage());
                    return;
                }
            }
        } else if (strSubmitType.equals("Upd")) {
            cttInfoShowUpd.setCttType(EnumResType.RES_TYPE1.getCode());
            try {
                cttInfoService.updateRecord(cttInfoShowUpd);
                MessageUtil.addInfo("更新数据完成。");
            } catch (Exception e) {
                logger.error("更新数据失败，", e);
                MessageUtil.addError(e.getMessage());
                return;
            }
        } else if (strSubmitType.equals("Del")) {
            cttInfoShowDel.setCttType(EnumResType.RES_TYPE1.getCode());
            try {
                cttInfoShowDel.setCttType(EnumResType.RES_TYPE1.getCode());
                int deleteRecordNumOfCttItem= cttItemService.deleteRecord(cttInfoShowDel);
                int deleteRecordNumOfCtt= cttInfoService.deleteRecord(cttInfoShowDel.getPkid());
                if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfCttItem<=0){
                    MessageUtil.addInfo("该记录已删除。");
                    return;
                }
                MessageUtil.addInfo("删除数据完成。");
            } catch (Exception e) {
                logger.error("删除数据失败，", e);
                MessageUtil.addError(e.getMessage());
                return;
            }
        }
        onQueryAction("Mng","false");
    }

    /*智能字段 Start*/
    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public CttInfoShow getCttInfoShowSelected() {
        return cttInfoShowSelected;
    }

    public void setCttInfoShowSelected(CttInfoShow cttInfoShowSelected) {
        this.cttInfoShowSelected = cttInfoShowSelected;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public CttInfoShow getCttInfoShowQry() {
        return cttInfoShowQry;
    }

    public void setCttInfoShowQry(CttInfoShow cttInfoShowQry) {
        this.cttInfoShowQry = cttInfoShowQry;
    }

    public List<CttInfoShow> getCttInfoShowList() {
        return cttInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowAdd() {
        return cttInfoShowAdd;
    }

    public void setCttInfoShowAdd(CttInfoShow cttInfoShowAdd) {
        this.cttInfoShowAdd = cttInfoShowAdd;
    }

    public CttInfoShow getCttInfoShowDel() {
        return cttInfoShowDel;
    }

    public void setCttInfoShowDel(CttInfoShow cttInfoShowDel) {
        this.cttInfoShowDel = cttInfoShowDel;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    public String getStrBelongToPkid() {
        return strBelongToPkid;
    }

    public void setStrBelongToPkid(String strBelongToPkid) {
        this.strBelongToPkid = strBelongToPkid;
    }

    /*智能字段 End*/
}
