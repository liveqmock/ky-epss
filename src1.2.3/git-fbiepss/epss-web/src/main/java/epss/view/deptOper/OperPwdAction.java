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
    private String strPasswd;
    private String strNewPasswd;
    private String strConfirmPasswd;

    @PostConstruct
    public void init() {
        try {
            operUpd = new Oper();
            OperatorManager operatorManagerTemp=ToolUtil.getOperatorManager();
            BeanUtils.copyProperties(operUpd,operatorManagerTemp.getOperator());
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public void onClickForMngAction() {
        try {
            if (!submitOperPreCheck(operUpd)) {
                return;
            }
            operUpd.setPasswd(MD5Helper.getMD5String(strNewPasswd));
            deptOperService.updateOperRecord(operUpd);
            MessageUtil.addInfo("数据处理成功！");
        }catch (Exception e){
            logger.error("数据处理失败。", e);
            MessageUtil.addError("数据处理失败。");
        }
    }

    private boolean submitOperPreCheck(Oper operPara) {
        if (StringUtils.isEmpty(strPasswd)) {
            MessageUtil.addInfo("请输入原密码！");
            return false;
        }
        if (StringUtils.isEmpty(strNewPasswd)) {
            MessageUtil.addInfo("请输入新密码！");
            return false;
        }
        if (StringUtils.isEmpty(strConfirmPasswd)) {
            MessageUtil.addInfo("请输入新密码确认！");
            return false;
        }
        if(!operPara.getPasswd().equals(MD5Helper.getMD5String(strPasswd))){
            MessageUtil.addInfo("原密码错误，请重新输入或联系管理员修改密码！！");
            return false;
        }
        if(!strNewPasswd.equals(strConfirmPasswd)){
            MessageUtil.addInfo("两次输入密码不相同，请重新输入！！");
            return false;
        }
        return true;
    }

    /*智能字段 Start*/

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

    public String getStrPasswd() {
        return strPasswd;
    }

    public void setStrPasswd(String strPasswd) {
        this.strPasswd = strPasswd;
    }

    public String getStrNewPasswd() {
        return strNewPasswd;
    }

    public void setStrNewPasswd(String strNewPasswd) {
        this.strNewPasswd = strNewPasswd;
    }
}
