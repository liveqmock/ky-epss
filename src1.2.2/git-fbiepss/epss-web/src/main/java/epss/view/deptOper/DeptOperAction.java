package epss.view.deptOper;

import skyline.util.MessageUtil;;
import epss.repository.model.Dept;
import epss.repository.model.Oper;
import epss.repository.model.model_show.DeptOperShow;
import epss.service.DeptOperService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class DeptOperAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DeptOperAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;

    private TreeNode deptRoot;
    private TreeNode deptRootSeled;
    private TreeNode currentSelectedNode;
    private TreeNode lastSelectedNode;
    private String strSubmitType;
    private String strType;
    private String strRendered0;
    private String strRendered1;
    private Dept deptAdd;
    private Dept deptUpd;
    private Dept deptDel;
    private Oper operAdd;
    private Oper operUpd;
    private Oper operDel;
    private DeptOperShow deptOperShowSel;
    private List<SelectItem> operSexList;
    private List<SelectItem> operIsSuperList;
    private List<SelectItem> typeList;

    @PostConstruct
    public void init() {
        initVariables();
        initData();
    }

    private void initVariables() {
        strType = "";
        strRendered0 = "false";
        strRendered1 = "false";
        deptAdd = new Dept();
        deptUpd = new Dept();
        deptDel = new Dept();
        operAdd = new Oper();
        operUpd = new Oper();
        operDel = new Oper();
        deptOperShowSel = new DeptOperShow();
    }

    private void initData() {
        operSexList = new ArrayList<SelectItem>();
        operSexList.add(new SelectItem("1", "男"));
        operSexList.add(new SelectItem("0", "女"));
        operIsSuperList = new ArrayList<SelectItem>();
        operIsSuperList.add(new SelectItem("0", "否"));
        operIsSuperList.add(new SelectItem("1", "是"));
        typeList = new ArrayList<SelectItem>();
        typeList.add(new SelectItem("0", "增加机构信息"));
        typeList.add(new SelectItem("1", "增加人员信息"));
        initDept();
    }

    private void initDept() {
        deptRoot = new DefaultTreeNode("Root", null);
        DeptOperShow deptOperShowTemp = new DeptOperShow();
        deptOperShowTemp.setPkid("root");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptRoot);
        recursiveTreeNode("root", node0);
        node0.setExpanded(true);
    }

    private void recursiveTreeNode(String strParentPkid, TreeNode parentNode) {
        List<DeptOperShow> deptOperShowTempList =new ArrayList<DeptOperShow>();
        deptOperShowTempList = deptOperService.selectDeptAndOperRecords(strParentPkid);
        for (int i = 0; i < deptOperShowTempList.size(); i++) {
            TreeNode childNode = null;
            childNode = new DefaultTreeNode(deptOperShowTempList.get(i), parentNode);
            if (currentSelectedNode!=null){
                if (((DeptOperShow)currentSelectedNode.getData()).getPkid()
                        .equals(((DeptOperShow)childNode.getData()).getPkid())){
                    TreeNode treeNodeTemp=childNode;
                    while (!(((DeptOperShow)treeNodeTemp.getData()).getPkid().equals("root"))){
                        treeNodeTemp.setExpanded(true);
                        treeNodeTemp=treeNodeTemp.getParent();
                    }
                }
            }
            recursiveTreeNode(deptOperShowTempList.get(i).getPkid(), childNode);
        }
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   DeptOperShow deptOperShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            deptOperShowSel = (DeptOperShow) BeanUtils.cloneBean(deptOperShowPara);
            if (strPowerTypePara.equals("Mng")) {
                if ("0".equals(deptOperShowSel.getType())) {
                    if (strSubmitTypePara.equals("Add")) {
                        strType = "";
                        strRendered0 = "false";
                        strRendered1 = "false";
                        deptAdd = new Dept();
                    } else {
                        strRendered0 = "true";
                        strRendered1 = "false";
                        if (strSubmitTypePara.equals("Upd")) {
                            deptUpd = new Dept();
                            deptUpd = (Dept) deptOperService.selectRecordByPkid(deptOperShowPara);
                        } else if (strSubmitTypePara.equals("Del")) {
                            deptDel = new Dept();
                            deptDel = (Dept) deptOperService.selectRecordByPkid(deptOperShowPara);
                        }
                    }
                } else {
                    strRendered0 = "false";
                    strRendered1 = "true";
                    if (strSubmitTypePara.equals("Upd")) {
                        operUpd = new Oper();
                        operUpd = (Oper) deptOperService.selectRecordByPkid(deptOperShowPara);
                        System.out.println("*******operUpd.getPasswd():"+operUpd.getPasswd());
                    } else if (strSubmitTypePara.equals("Del")) {
                        currentSelectedNode=currentSelectedNode.getParent();
                        operDel = new Oper();
                        operDel = (Oper) deptOperService.selectRecordByPkid(deptOperShowPara);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("初始化信息失败。", e);
            MessageUtil.addError("初始化信息失败。");
        }
    }

    public void onClickForMngAction() {
        Object objectTemp;
        if (strSubmitType.equals("Add")) {
            if ("0".equals(strType)) {
                deptAdd.setParentpkid(deptOperShowSel.getPkid());
                deptAdd.setTid("NULL");
                objectTemp = deptAdd;
            } else {
                operAdd.setDeptPkid(deptOperShowSel.getPkid());
                operAdd.setTid("NULL");
                objectTemp = operAdd;
            }
            if (!submitPreCheck(objectTemp)) {
                return;
            }
            if (deptOperService.isExistInDb(objectTemp, strType)) {
                MessageUtil.addError("该编号对应记录已存在，请重新录入！");
                return;
            } else {
                addRecordAction(objectTemp, strType);
            }
        } else if (strSubmitType.equals("Upd")) {
            if ("0".equals(deptOperShowSel.getType())) {
                objectTemp = deptUpd;
            } else {
                objectTemp = operUpd;
            }
            if (!submitPreCheck(objectTemp)) {
                return;
            }
            updRecordAction(objectTemp, deptOperShowSel.getType());
        } else if (strSubmitType.equals("Del")) {
            if ("0".equals(deptOperShowSel.getType())) {
                if (deptOperService.findChildRecordsByPkid(deptOperShowSel)) {
                    MessageUtil.addInfo("该部门有员工，无法删除。");
                    return;
                } else {
                    objectTemp = deptDel;
                }
            } else {
                objectTemp = operDel;
            }
            delRecordAction(objectTemp, deptOperShowSel.getType());
        }
        initVariables();
        initData();
    }

    private void fromModelToShowForUpd(Object model,DeptOperShow show,String strType){
        if ("0".equals(strType)){
            Dept deptmodel=(Dept)model;
            show.setName(deptmodel.getName());
            show.setId(deptmodel.getId());
        }else if ("1".equals(strType)){
            Oper operModel=(Oper)model;
            show.setName(operModel.getName());
            show.setId(operModel.getId());
        }
    }

    private boolean submitPreCheck(Object objectPara) {
        String strTypeTemp = null;
        if ("Add".equals(strSubmitType)) {
            strTypeTemp = strType;
        } else if ("Upd".equals(strSubmitType)) {
            strTypeTemp = deptOperShowSel.getType();
        }
        if ("".equals(strTypeTemp)) {
            MessageUtil.addInfo("请选择操作类型！");
            return false;
        } else {
            if ("0".equals(strTypeTemp)) {
                Dept deptTemp = (Dept) objectPara;
                if (StringUtils.isEmpty(deptTemp.getName())) {
                    MessageUtil.addInfo("请输入部门名称！");
                    return false;
                }
                if (StringUtils.isEmpty(deptTemp.getId())) {
                    MessageUtil.addInfo("请输入部门编号！");
                    return false;
                }
            } else {
                Oper operTemp = (Oper) objectPara;
                if (StringUtils.isEmpty(operTemp.getId())) {
                    MessageUtil.addInfo("请输入操作员编号！");
                    return false;
                }
                if (StringUtils.isEmpty(operTemp.getName())) {
                    MessageUtil.addInfo("请输入操作员名称！");
                    return false;
                }
                if (StringUtils.isEmpty(operTemp.getPasswd())) {
                    MessageUtil.addInfo("请输入操作员密码！");
                    return false;
                }
            }
        }
        return true;
    }

    private void addRecordAction(Object objectPara, String strTypePara) {
        try {
            deptOperService.insertRecord(objectPara, strTypePara);
            MessageUtil.addInfo("新增数据完成。");
        } catch (Exception e) {
            logger.error("增加信息失败。", e);
            MessageUtil.addError("增加信息失败。");
        }
    }

    private void updRecordAction(Object objectPara, String strTypePara) {
        try {
            deptOperService.updateRecord(objectPara, strTypePara);
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新信息失败。", e);
            MessageUtil.addError("更新信息失败。");
        }
    }

    private void delRecordAction(Object objectPara, String strTypePara) {
        try {
            deptOperService.deleteRecord(objectPara, strTypePara);
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除信息失败。", e);
            MessageUtil.addError("删除信息失败。");
        }
    }

    public void viewByType() {
        if ("".equals(strType)) {
            strRendered0 = "false";
            strRendered1 = "false";
        } else {
            if ("0".equals(strType)) {
                strRendered0 = "true";
                strRendered1 = "false";
            } else if ("1".equals(strType)) {
                strRendered0 = "false";
                strRendered1 = "true";
            }
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        currentSelectedNode=event.getTreeNode();
        if (lastSelectedNode==null){
            lastSelectedNode=currentSelectedNode;
        }else {
            ((DeptOperShow)lastSelectedNode.getData()).setIsDisabled("true");
            lastSelectedNode=currentSelectedNode;
        }
        ((DeptOperShow)currentSelectedNode.getData()).setIsDisabled("false");
    }

    /*智能字段 Start*/

    public TreeNode getDeptRoot() {
        return deptRoot;
    }

    public void setDeptRoot(TreeNode deptRoot) {
        this.deptRoot = deptRoot;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public String getStrRendered0() {
        return strRendered0;
    }

    public void setStrRendered0(String strRendered0) {
        this.strRendered0 = strRendered0;
    }

    public String getStrRendered1() {
        return strRendered1;
    }

    public void setStrRendered1(String strRendered1) {
        this.strRendered1 = strRendered1;
    }

    public Dept getDeptAdd() {
        return deptAdd;
    }

    public void setDeptAdd(Dept deptAdd) {
        this.deptAdd = deptAdd;
    }

    public Dept getDeptUpd() {
        return deptUpd;
    }

    public void setDeptUpd(Dept deptUpd) {
        this.deptUpd = deptUpd;
    }

    public Dept getDeptDel() {
        return deptDel;
    }

    public void setDeptDel(Dept deptDel) {
        this.deptDel = deptDel;
    }

    public Oper getOperAdd() {
        return operAdd;
    }

    public void setOperAdd(Oper operAdd) {
        this.operAdd = operAdd;
    }

    public Oper getOperUpd() {
        return operUpd;
    }

    public void setOperUpd(Oper operUpd) {
        this.operUpd = operUpd;
    }

    public Oper getOperDel() {
        return operDel;
    }

    public void setOperDel(Oper operDel) {
        this.operDel = operDel;
    }

    public DeptOperShow getDeptOperShowSel() {
        return deptOperShowSel;
    }

    public void setDeptOperShowSel(DeptOperShow deptOperShowSel) {
        this.deptOperShowSel = deptOperShowSel;
    }

    public List<SelectItem> getOperSexList() {
        return operSexList;
    }

    public void setOperSexList(List<SelectItem> operSexList) {
        this.operSexList = operSexList;
    }

    public List<SelectItem> getOperIsSuperList() {
        return operIsSuperList;
    }

    public void setOperIsSuperList(List<SelectItem> operIsSuperList) {
        this.operIsSuperList = operIsSuperList;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public List<SelectItem> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<SelectItem> typeList) {
        this.typeList = typeList;
    }
}
