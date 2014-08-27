package epss.view.enu;
import epss.service.EnumService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import epss.repository.model.Ptenumain;
import epss.repository.model.PtenumainExample;
import epss.repository.model.Ptenudetail;
import epss.repository.model.PtenudetailExample;
import epss.repository.model.PtenudetailKey;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class EnumAction {
    private static final Logger logger = LoggerFactory.getLogger(EnumAction.class);
    @ManagedProperty(value = "#{enumService}")
    private EnumService enumService;
    private Ptenumain enumainQry;
    private Ptenumain enumainAdd;
    private Ptenumain enumainUpd;
    private Ptenumain enumainDel;
    private Ptenumain enumainSel;
    private List<Ptenumain> enumainList;

    private Ptenudetail enudetailQry;
    private Ptenudetail enudetailAdd;
    private Ptenudetail enudetailUpd;
    private Ptenudetail enudetailDel;
    private Ptenudetail enudetailSel;
    private List<Ptenudetail> enudetailList;

    private String strSubmitType;
    private String rowSelectedFlag;
    /*����ά������㼶���ֵ���ʾ*/
    private StyleModel styleModel;

    @PostConstruct
    public void init() {
        resetAction();
    }

    public void resetAction(){
        this.enumainList = new ArrayList<Ptenumain>();
        enumainQry=new Ptenumain() ;
        enumainAdd=new Ptenumain() ;
        enumainUpd=new Ptenumain() ;
        enumainDel=new Ptenumain() ;
        enumainSel=new Ptenumain() ;
        this.enudetailList = new ArrayList<Ptenudetail>();
        enudetailQry=new Ptenudetail() ;
        enudetailAdd=new Ptenudetail() ;
        enudetailUpd=new Ptenudetail() ;
        enudetailDel=new Ptenudetail() ;
        enudetailSel=new Ptenudetail() ;
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        rowSelectedFlag = "false";
    }

    public void resetActionForAdd(){
        enumainAdd=new Ptenumain();
        enudetailAdd=new Ptenudetail();
        strSubmitType="Add";
    }

    public void onQueryAction(String enutypePara) {
        try {
             if (enutypePara.equals("main")){
                 this.enumainList.clear();
                 enumainList = enumService.selectMainListByModel(enumainQry);
                 if (enumainList.isEmpty()) {
                     MessageUtil.addWarn("û�в�ѯ�����ݡ�");
                 }
             }else if(enutypePara.equals("detail")){
                 this.enudetailList.clear();
                 enudetailList = enumService.selectDetailListByModel(enudetailQry);
                 if (enudetailList.isEmpty()) {
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
    public boolean unableNullCheck(Ptenumain Ptenumain){
        if (StringUtils.isEmpty(Ptenumain.getEnutype())){
            MessageUtil.addError("������ö�����ͣ�");
            return false;
        }
        else if (StringUtils.isEmpty(Ptenumain.getEnuname())) {
            MessageUtil.addError("������ö�����ƣ�");
            return false;
        }
        return true ;
    }

    public void submitThisRecordAction(String enutypePara,String strSubmitType){
        if(enutypePara.equals("main")){
            if(strSubmitType.equals("Add")){
                if(enumService.mainIsExistInDb(enumainAdd)) {
                    MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                }else {
                    addMainRecordAction(enumainAdd);
                    resetActionForAdd();
                }
            }
            else if(strSubmitType.equals("Upd")){
                updMainRecordAction(enumainUpd);
            }else if(strSubmitType.equals("Del")){
                deleteMainRecordAction(enumainDel);
            }
            onQueryAction("main");
        }else if(enutypePara.equals("detail")){
            if(strSubmitType.equals("Add")){
                if(enumService.detailIsExistInDb(enudetailAdd)) {
                    MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                }else {
                    addDetailRecordAction(enudetailAdd);
                    resetActionForAdd();
                }
            }
            else if(strSubmitType.equals("Upd")){
                updDetailRecordAction(enudetailUpd);
            }else if(strSubmitType.equals("Del")){
                deleteDetailRecordAction(enudetailDel);
            }
            onQueryAction("detail");
        }
    }
    public void addMainRecordAction(Ptenumain enumainPara){
        try {
            if(unableNullCheck(enumainPara)){
                enumService.insertMainRecord(enumainPara) ;
                MessageUtil.addInfo("����������ɡ�");
            }
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void updMainRecordAction(Ptenumain enumainPara){
        try {
            enumService.updateMainRecord(enumainPara) ;
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void deleteMainRecordAction(Ptenumain enumainPara){
        try {
            int deleteRecordNum= enumService.deleteMainByPrimaryKey(enumainPara.getEnutype()) ;
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

    public void selectMainRecordAction(String strSubmitTypePara,Ptenumain enumainSelectedPara){
        try {
            if (strSubmitTypePara.equals("Sel")){
                enumainSel=(Ptenumain) BeanUtils.cloneBean(enumainSelectedPara);
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Add")){
            }else if(strSubmitTypePara.equals("Upd")){
                enumainUpd =(Ptenumain) BeanUtils.cloneBean(enumainSelectedPara);
            }else if(strSubmitTypePara.equals("Del")){
                enumainDel =(Ptenumain) BeanUtils.cloneBean(enumainSelectedPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    /**
     * ����������Ŀ���
     */
    public boolean detailUnableNullCheck(Ptenudetail Ptenudetail){
        if (StringUtils.isEmpty(Ptenudetail.getEnutype())){
            MessageUtil.addError("������ö�����ͣ�");
            return false;
        }
        else if (StringUtils.isEmpty(Ptenudetail.getEnuitemvalue())) {
            MessageUtil.addError("������ö��Ԫ��ֵ��");
            return false;
        }else if (StringUtils.isEmpty(Ptenudetail.getEnuitemlabel())) {
            MessageUtil.addError("������ö��Ԫ�ر�ǩ��");
            return false;
        }else if (StringUtils.isEmpty(Ptenudetail.getDispno().toString())) {
            MessageUtil.addError("��������ʾ˳��");
            return false;
        }
        return true ;
    }
    public void addDetailRecordAction(Ptenudetail enudetailPara){
        try {
            if(detailUnableNullCheck(enudetailPara)){
                enumService.insertDetailRecord(enudetailPara) ;
                MessageUtil.addInfo("����������ɡ�");
            }
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void updDetailRecordAction(Ptenudetail enudetailPara){
        try {
            enumService.updateDetailRecord(enudetailPara) ;
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void deleteDetailRecordAction(Ptenudetail enudetailPara){
        try {
            int deleteRecordNum= enumService.deleteDetailByPrimaryKey(enudetailPara) ;
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

    public void selectDetailRecordAction(String strSubmitTypePara,Ptenudetail enudetailSelectedPara){
        try {
            if (strSubmitTypePara.equals("Sel")){
                enudetailSel=(Ptenudetail) BeanUtils.cloneBean(enudetailSelectedPara);
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Add")){
            }else if(strSubmitTypePara.equals("Upd")){
                enudetailUpd =(Ptenudetail) BeanUtils.cloneBean(enudetailSelectedPara);
            }else if(strSubmitTypePara.equals("Del")){
                enudetailDel =(Ptenudetail) BeanUtils.cloneBean(enudetailSelectedPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public EnumService getEnumService() {
        return enumService;
    }

    public void setEnumService(EnumService enumService) {
        this.enumService = enumService;
    }

    public Ptenumain getEnumainQry() {
        return enumainQry;
    }

    public void setEnumainQry(Ptenumain enumainQry) {
        this.enumainQry = enumainQry;
    }

    public Ptenumain getEnumainAdd() {
        return enumainAdd;
    }

    public void setEnumainAdd(Ptenumain enumainAdd) {
        this.enumainAdd = enumainAdd;
    }

    public Ptenumain getEnumainUpd() {
        return enumainUpd;
    }

    public void setEnumainUpd(Ptenumain enumainUpd) {
        this.enumainUpd = enumainUpd;
    }

    public Ptenumain getEnumainDel() {
        return enumainDel;
    }

    public void setEnumainDel(Ptenumain enumainDel) {
        this.enumainDel = enumainDel;
    }

    public Ptenumain getEnumainSel() {
        return enumainSel;
    }

    public void setEnumainSel(Ptenumain enumainSel) {
        this.enumainSel = enumainSel;
    }

    public List<Ptenumain> getEnumainList() {
        return enumainList;
    }

    public void setEnumainList(List<Ptenumain> enumainList) {
        this.enumainList = enumainList;
    }

    public Ptenudetail getEnudetailQry() {
        return enudetailQry;
    }

    public void setEnudetailQry(Ptenudetail enudetailQry) {
        this.enudetailQry = enudetailQry;
    }

    public Ptenudetail getEnudetailAdd() {
        return enudetailAdd;
    }

    public void setEnudetailAdd(Ptenudetail enudetailAdd) {
        this.enudetailAdd = enudetailAdd;
    }

    public Ptenudetail getEnudetailUpd() {
        return enudetailUpd;
    }

    public void setEnudetailUpd(Ptenudetail enudetailUpd) {
        this.enudetailUpd = enudetailUpd;
    }

    public Ptenudetail getEnudetailDel() {
        return enudetailDel;
    }

    public void setEnudetailDel(Ptenudetail enudetailDel) {
        this.enudetailDel = enudetailDel;
    }

    public Ptenudetail getEnudetailSel() {
        return enudetailSel;
    }

    public void setEnudetailSel(Ptenudetail enudetailSel) {
        this.enudetailSel = enudetailSel;
    }

    public List<Ptenudetail> getEnudetailList() {
        return enudetailList;
    }

    public void setEnudetailList(List<Ptenudetail> enudetailList) {
        this.enudetailList = enudetailList;
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
