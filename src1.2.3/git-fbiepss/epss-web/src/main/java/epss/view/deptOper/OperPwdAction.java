package epss.view.deptOper;

import epss.repository.model.Dept;
import epss.repository.model.Oper;
import epss.repository.model.model_show.DeptOperShow;
import epss.service.DeptOperService;
import epss.service.TidkeysService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.platform.security.OperatorManager;
import skyline.security.MD5Helper;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class OperPwdAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(OperPwdAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;

    private Oper operUpd;
    private String strConfirmPasswd;

    @PostConstruct
    public void init() {
        operUpd = new Oper();
        OperatorManager operatorManagerTemp=ToolUtil.getOperatorManager();
        try {
            BeanUtils.copyProperties(operUpd,operatorManagerTemp.getOperator());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void onClickForMngAction() {
        try {
            if (!submitOperPreCheck(operUpd)) {
                return;
            }
            if(!operUpd.getPasswd().equals(strConfirmPasswd)){
                MessageUtil.addInfo("�����������벻��ͬ�����������룡��");
                return;
            }
            operUpd.setPasswd(MD5Helper.getMD5String(operUpd.getPasswd()));
            deptOperService.updateOperRecord(operUpd);
            MessageUtil.addInfo("���ݴ���ɹ���");
        }catch (Exception e){
            logger.error("���ݴ���ʧ�ܡ�", e);
            MessageUtil.addError("���ݴ���ʧ�ܡ�");
        }
    }

    private boolean submitOperPreCheck(Oper operPara) {
        if (StringUtils.isEmpty(operPara.getPasswd())) {
            MessageUtil.addInfo("���������Ա���룡");
            return false;
        }
        return true;
    }

    /*�����ֶ� Start*/

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public Oper getOperUpd() {
        return operUpd;
    }

    public void setOperUpd(Oper operUpd) {
        this.operUpd = operUpd;
    }

    public String getStrConfirmPasswd() {
        return strConfirmPasswd;
    }

    public void setStrConfirmPasswd(String strConfirmPasswd) {
        this.strConfirmPasswd = strConfirmPasswd;
    }
}
