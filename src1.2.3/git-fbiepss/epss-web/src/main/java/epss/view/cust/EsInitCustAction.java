package epss.view.cust;

import skyline.util.StyleModel;
import skyline.util.ToolUtil;
import epss.repository.model.model_show.SignPartShow;
import epss.view.flow.EsCommon;
import epss.service.SignPartService;
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
public class EsInitCustAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitCustAction.class);
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    private SignPartShow signPartShowQry;
    private SignPartShow signPartShowAdd;
    private SignPartShow signPartShowUpd;
    private SignPartShow signPartShowDel;
    private SignPartShow signPartShowSel;
    private List<SignPartShow> signPartShowList;

    private String strSubmitType;
    private String rowSelectedFlag;
    /*����ά������㼶���ֵ���ʾ*/
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
            strMaxId= signPartService.getMaxId();
            if(StringUtils .isEmpty(strMaxId)){
                strMaxId="CUST"+ ToolUtil.getStrToday()+"001";
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
            logger.error("�ͻ���Ϣ��ѯʧ��", e);
            MessageUtil.addError("�ͻ���Ϣ��ѯʧ�ܡ�");
        }
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            this.signPartShowList = signPartService.selectListByModel(signPartShowQry);
            if(strQryMsgOutPara.equals("true")) {
                if (signPartShowList.isEmpty()) {
                    MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                }
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    /**
     * ����������Ŀ���
     */
    public boolean unableNullCheck(SignPartShow signPartShow){
        if (StringUtils.isEmpty(signPartShow.getId())){
            MessageUtil.addError("������ͻ���ţ�");
            return false;
        }
        else if (StringUtils.isEmpty(signPartShow.getName())) {
            MessageUtil.addError("������ͻ����ƣ�");
            return false;
        }
        return true ;
    }

    public void submitThisRecordAction(){
        if(strSubmitType.equals("Add")){
            if(signPartService.isExistInDb(signPartShowAdd)) {
                MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
            }else {
                addRecordAction(signPartShowAdd);
                resetActionForAdd();
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
                signPartService.insertRecord(signPartShowPara) ;
                MessageUtil.addInfo("����������ɡ�");

            }
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void updRecordAction(SignPartShow signPartShowPara){
        try {
            signPartService.updateRecord(signPartShowPara) ;
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void deleteRecordAction(SignPartShow esInitCustPara){
        try {
            int deleteRecordNum= signPartService.deleteRecord(esInitCustPara.getPkid()) ;
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("�ü�¼��ɾ����");
                return;
            }
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void selectRecordAction(String strSubmitTypePara,SignPartShow signPartShowSelectedPara){
        try {
            if (strSubmitTypePara.equals("Sel")){
                signPartShowSel=(SignPartShow) BeanUtils.cloneBean(signPartShowSelectedPara);
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Add")){
            }else if(strSubmitTypePara.equals("Upd")){
                signPartShowUpd =(SignPartShow) BeanUtils.cloneBean(signPartShowSelectedPara);
            }else if(strSubmitTypePara.equals("Del")){
                signPartShowDel =(SignPartShow) BeanUtils.cloneBean(signPartShowSelectedPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
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
