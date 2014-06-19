package epss.view.cust;

import epss.common.utils.StyleModel;
import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.SignPartShow;
import epss.view.common.EsCommon;
import epss.repository.model.EsInitCust;
import epss.service.EsInitCustService;
import epss.view.common.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.common.utils.MessageUtil;

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
public class EsInitCustAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitCustAction.class);
    @ManagedProperty(value = "#{esInitCustService}")
    private EsInitCustService esInitCustService;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    private SignPartShow signPartShowQry;
    private SignPartShow signPartShowAdd;
    private SignPartShow signPartShowUpd;
    private SignPartShow signPartShowDel;
    private SignPartShow signPartShowSel;
    private List<SignPartShow> signPartShowList;
    private SignPartShow signPartShowSelected;

    private String strSubmitType;
    private String rowSelectedFlag;
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        resetAction();
    }

    public void resetAction(){
        this.signPartShowList = new ArrayList<SignPartShow>();
        signPartShowQry=new SignPartShow() ;
        signPartShowAdd=new SignPartShow() ;
        signPartShowUpd=new SignPartShow() ;
        signPartShowDel=new SignPartShow() ;
        signPartShowSel=new SignPartShow() ;
        signPartShowSelected=new SignPartShow() ;
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        rowSelectedFlag = "false";
    }

    public void resetActionForAdd(){
        signPartShowAdd=new SignPartShow();
        strSubmitType="Add";
    }

    public void setMaxNoPlusOne(){
        try {
            String strMaxId;
            Integer intTemp;
            strMaxId= esInitCustService.getMaxId();
            if(StringUtils .isEmpty(strMaxId)){
                strMaxId="CUST"+ esCommon.getStrToday()+"001";
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
            signPartShowAdd.setId(strMaxId);
            signPartShowUpd.setId(strMaxId);
        } catch (Exception e) {
            logger.error("客户信息查询失败", e);
            MessageUtil.addError("客户信息查询失败。");
        }
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            this.signPartShowList = esInitCustService.selectListByModel(signPartShowQry);
            if(strQryMsgOutPara.equals("true")) {
                if (signPartShowList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /**
     * 必须输入项目检查
     */
    public boolean unableNullCheck(SignPartShow signPartShow){
        if (StringUtils.isEmpty(signPartShow.getId())){
            MessageUtil.addError("请输入客户编号！");
            return false;
        }
        else if (StringUtils.isEmpty(signPartShow.getName())) {
            MessageUtil.addError("请输入客户名称！");
            return false;
        }
        return true ;
    }

    public void submitThisRecordAction(){
        if(strSubmitType.equals("Add")){
            if(esInitCustService .isExistInDb(signPartShowAdd)) {
                MessageUtil.addError("该记录已存在，请重新录入！");
            }else {
                addRecordAction(signPartShowAdd);
            }
        }
        else if(strSubmitType.equals("Upd")){
            updRecordAction(signPartShowUpd);
        }else if(strSubmitType.equals("Del")){
            deleteRecordAction(signPartShowDel);
        }
        onQueryAction("false");
    }
    public void addRecordAction(SignPartShow signPartShowPara){
        try {
            if(unableNullCheck(signPartShowPara)){
                esInitCustService.insertRecord(signPartShowPara) ;
                MessageUtil.addInfo("新增数据完成。");

            }
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void updRecordAction(SignPartShow signPartShowPara){
        try {
            esInitCustService.updateRecord(signPartShowPara) ;
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void deleteRecordAction(SignPartShow esInitCustPara){
        try {
            int deleteRecordNum=esInitCustService.deleteRecord(esInitCustPara.getPkid()) ;
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void selectRecordAction(String strSubmitTypePara){
        try {
            if (strSubmitTypePara.equals("Sel")){
                signPartShowSel=(SignPartShow) BeanUtils.cloneBean(signPartShowSelected);
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Add")){
            }else if(strSubmitTypePara.equals("Upd")){
                signPartShowUpd =(SignPartShow) BeanUtils.cloneBean(signPartShowSelected);
            }else if(strSubmitTypePara.equals("Del")){
                signPartShowDel =(SignPartShow) BeanUtils.cloneBean(signPartShowSelected);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public EsInitCustService getEsInitCustService() {
        return esInitCustService;
    }

    public void setEsInitCustService(EsInitCustService esInitCustService) {
        this.esInitCustService = esInitCustService;
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

    public SignPartShow getSignPartShowQry() {
        return signPartShowQry;
    }

    public void setSignPartShowQry(SignPartShow signPartShowQry) {
        this.signPartShowQry = signPartShowQry;
    }

    public SignPartShow getSignPartShowAdd() {
        return signPartShowAdd;
    }

    public void setSignPartShowAdd(SignPartShow signPartShowAdd) {
        this.signPartShowAdd = signPartShowAdd;
    }

    public SignPartShow getSignPartShowUpd() {
        return signPartShowUpd;
    }

    public void setSignPartShowUpd(SignPartShow signPartShowUpd) {
        this.signPartShowUpd = signPartShowUpd;
    }

    public SignPartShow getSignPartShowDel() {
        return signPartShowDel;
    }

    public void setSignPartShowDel(SignPartShow signPartShowDel) {
        this.signPartShowDel = signPartShowDel;
    }

    public SignPartShow getSignPartShowSel() {
        return signPartShowSel;
    }

    public void setSignPartShowSel(SignPartShow signPartShowSel) {
        this.signPartShowSel = signPartShowSel;
    }

    public List<SignPartShow> getSignPartShowList() {
        return signPartShowList;
    }

    public void setSignPartShowList(List<SignPartShow> signPartShowList) {
        this.signPartShowList = signPartShowList;
    }

    public SignPartShow getSignPartShowSelected() {
        return signPartShowSelected;
    }

    public void setSignPartShowSelected(SignPartShow signPartShowSelected) {
        this.signPartShowSelected = signPartShowSelected;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public void setRowSelectedFlag(String rowSelectedFlag) {
        this.rowSelectedFlag = rowSelectedFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }
}
