package epss.view.ctt;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.repository.model.model_show.CttInfoShow;
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
public class TkcttInfoAction {
    private static final Logger logger = LoggerFactory.getLogger(TkcttInfoAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private CttInfoShow cttInfoShowQry;
    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;
    private List<CttInfoShow> cttInfoShowList;

    private String strSubmitType;
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;
    //更新时检验用，符合立刻关闭，不符合进行提升
    @PostConstruct
    public void init() {
        initData();
    }
    public void initData() {
        this.cttInfoShowList = new ArrayList<CttInfoShow>();
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(EnumResType.RES_TYPE0.getCode());
        cttInfoShowSel = new CttInfoShow();
        cttInfoShowSel.setCttType(EnumResType.RES_TYPE0.getCode());
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
        cttInfoShowUpd = new CttInfoShow();
        cttInfoShowUpd.setCttType(EnumResType.RES_TYPE0.getCode());
        cttInfoShowDel = new CttInfoShow();
        cttInfoShowDel.setCttType(EnumResType.RES_TYPE0.getCode());
        styleModel = new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType = "";
    }

    public void setMaxNoPlusOne() {
        try {
            Integer intTemp;
            String strMaxId = cttInfoService.getStrMaxCttId(EnumResType.RES_TYPE0.getCode());
            if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
                strMaxId = "TKCTT" + ToolUtil.getStrToday() + "001";
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
            logger.error("总包合同信息查询失败", e);
            MessageUtil.addError("总包合同信息查询失败");
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
            cttInfoShowList = cttInfoService.selectCttByStatusFlagBegin_End(cttInfoShowQry);

            if(strQryMsgOutPara.equals("true"))  {
                if (cttInfoShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("合同信息查询失败", e);
            MessageUtil.addError("合同信息查询失败");
        }
        return null;
    }

    public void resetActionForAdd(){
        strSubmitType="Add";
        cttInfoShowAdd = new CttInfoShow();
        cttInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
    }
    public void selectRecordAction(
                                   String strSubmitTypePara,
                                   CttInfoShow cttInfoShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            // 查询
            cttInfoShowPara.setCreatedByName(ToolUtil.getUserName(cttInfoShowPara.getCreatedBy()));
            cttInfoShowPara.setLastUpdByName(ToolUtil.getUserName(cttInfoShowPara.getLastUpdBy()));
            if (strSubmitTypePara.equals("Sel")) {
                 cttInfoShowSel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            }else if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            }else if (strSubmitTypePara.equals("Upd")) {
                 cttInfoShowUpd = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            }else if (strSubmitTypePara.equals("Del")){
                cttInfoShowDel = (CttInfoShow) BeanUtils.cloneBean(cttInfoShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    /**
     * 必须输入项目检查
     */
    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if (StringUtils.isEmpty(cttInfoShowPara.getId())) {
            MessageUtil.addError("请输入合同号！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getName())) {
            MessageUtil.addError("请输入合同名！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignDate())) {
            MessageUtil.addError("请输入签订日期！");
            return false;
        }
        if (StringUtils.isEmpty(cttInfoShowPara.getSignPartA())) {
            MessageUtil.addError("请输入签订甲方！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getSignPartB())) {
            MessageUtil.addError("请输入签订乙方！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttStartDate())) {
            MessageUtil.addError("请输入合同开始时间！");
            return false;
        } else if (StringUtils.isEmpty(cttInfoShowPara.getCttEndDate())) {
            MessageUtil.addError("请输入合同截止时间！");
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
            if (!submitPreCheck(cttInfoShowAdd)) {
                return;
            }
            if (cttInfoService.isExistInDb(cttInfoShowAdd)) {
                MessageUtil.addError("该记录已存在，请重新录入！");
            } else {
                addRecordAction(cttInfoShowAdd);
                MessageUtil.addInfo("新增数据完成。");
                resetActionForAdd();
            }
        } else if (strSubmitType.equals("Upd")) {
			if (!submitPreCheck(cttInfoShowUpd)) {
	         return;
	        }
            updRecordAction(cttInfoShowUpd);
            MessageUtil.addInfo("更新数据完成。");
        } else if (strSubmitType.equals("Del")) {
            deleteRecordAction(cttInfoShowDel);
            MessageUtil.addInfo("删除数据完成。");
        }
        onQueryAction("Mng","false");
    }

    private void addRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            if (cttInfoShowPara.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                cttInfoShowPara.setParentPkid("ROOT");
            }
            cttInfoService.insertRecord(cttInfoShowPara);
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void updRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            cttInfoService.updateRecord(cttInfoShowPara);
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private void deleteRecordAction(CttInfoShow cttInfoShowPara) {
        try {
            cttInfoShowPara.setCttType(EnumResType.RES_TYPE0.getCode());
            int deleteRecordNumOfCttItem= cttItemService.deleteRecord(cttInfoShowPara);
            int deleteRecordNumOfCtt= cttInfoService.deleteRecord(cttInfoShowPara.getPkid());
            if (deleteRecordNumOfCtt<=0&&deleteRecordNumOfCttItem<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*智能字段 Start*/

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public CttInfoShow getCttInfoShowQry() {
        return cttInfoShowQry;
    }

    public void setCttInfoShowQry(CttInfoShow cttInfoShowQry) {
        this.cttInfoShowQry = cttInfoShowQry;
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

    public List<CttInfoShow> getCttInfoShowList() {
        return cttInfoShowList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowSel() {
        return cttInfoShowSel;
    }

    public void setCttInfoShowSel(CttInfoShow cttInfoShowSel) {
        this.cttInfoShowSel = cttInfoShowSel;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
    /*智能字段 End*/

}
