package epss.view.deptAndOper;

import epss.common.utils.MessageUtil;
import epss.repository.model.Dept;
import epss.repository.model.Oper;
import epss.repository.model.model_show.DeptAndOperShow;
import epss.service.DeptAndOperService;
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
public class DeptAndOperAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DeptAndOperAction.class);
    @ManagedProperty(value = "#{deptAndOperService}")
    private DeptAndOperService deptAndOperService;

    private TreeNode deptRoot;
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
    private DeptAndOperShow deptAndOperShowSel;
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
        deptAndOperShowSel = new DeptAndOperShow();
    }

    private void initData() {
        operSexList = new ArrayList<SelectItem>();
        operSexList.add(new SelectItem("1", "��"));
        operSexList.add(new SelectItem("0", "Ů"));
        operIsSuperList = new ArrayList<SelectItem>();
        operIsSuperList.add(new SelectItem("0", "��"));
        operIsSuperList.add(new SelectItem("1", "��"));
        typeList = new ArrayList<SelectItem>();
        typeList.add(new SelectItem("", "ȫ��"));
        typeList.add(new SelectItem("0", "���ӻ�����Ϣ"));
        typeList.add(new SelectItem("1", "������Ա��Ϣ"));
        initDept();
    }

    private void initDept() {
        deptRoot = new DefaultTreeNode("Root", null);
        DeptAndOperShow deptAndOperShowTemp = new DeptAndOperShow();
        deptAndOperShowTemp.setPkid("root");
        deptAndOperShowTemp.setName("������Ա��Ϣ");
        deptAndOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptAndOperShowTemp, deptRoot);
        recursiveTreeNode("root", node0);
        node0.setExpanded(true);
    }

    private void recursiveTreeNode(String strParentPkid, TreeNode parentNode) {
        List<DeptAndOperShow> deptAndOperShowTempList =new ArrayList<DeptAndOperShow>();
        deptAndOperShowTempList=deptAndOperService.selectDeptAndOperRecords(strParentPkid);
        for (int i = 0; i < deptAndOperShowTempList.size(); i++) {
            TreeNode childNode = null;
            childNode = new DefaultTreeNode(deptAndOperShowTempList.get(i), parentNode);
            if (currentSelectedNode!=null){
                if (((DeptAndOperShow)currentSelectedNode.getData()).getPkid()
                        .equals(((DeptAndOperShow)childNode.getData()).getPkid())){
                    TreeNode treeNodeTemp=childNode;
                    while (!(((DeptAndOperShow)treeNodeTemp.getData()).getPkid().equals("root"))){
                        treeNodeTemp.setExpanded(true);
                        treeNodeTemp=treeNodeTemp.getParent();
                    }
                }
            }
            recursiveTreeNode(deptAndOperShowTempList.get(i).getPkid(), childNode);
        }
    }

    public void selectRecordAction(String strPowerTypePara,
                                   String strSubmitTypePara,
                                   DeptAndOperShow deptAndOperShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            deptAndOperShowSel = (DeptAndOperShow) BeanUtils.cloneBean(deptAndOperShowPara);
            if (strPowerTypePara.equals("Mng")) {
                if ("0".equals(deptAndOperShowSel.getType())) {
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
                            deptUpd = (Dept) deptAndOperService.selectRecordByPkid(deptAndOperShowPara);
                        } else if (strSubmitTypePara.equals("Del")) {
                            deptDel = new Dept();
                            deptDel = (Dept) deptAndOperService.selectRecordByPkid(deptAndOperShowPara);
                        }
                    }
                } else {
                    strRendered0 = "false";
                    strRendered1 = "true";
                    if (strSubmitTypePara.equals("Upd")) {
                        operUpd = new Oper();
                        operUpd = (Oper) deptAndOperService.selectRecordByPkid(deptAndOperShowPara);
                        System.out.println("*******operUpd.getPasswd():"+operUpd.getPasswd());
                    } else if (strSubmitTypePara.equals("Del")) {
                        currentSelectedNode=currentSelectedNode.getParent();
                        operDel = new Oper();
                        operDel = (Oper) deptAndOperService.selectRecordByPkid(deptAndOperShowPara);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("��ʼ����Ϣʧ�ܡ�", e);
            MessageUtil.addError("��ʼ����Ϣʧ�ܡ�");
        }
    }

    public void onClickForMngAction() {
        Object objectTemp;
        if (strSubmitType.equals("Add")) {
            if ("0".equals(strType)) {
                deptAdd.setParentpkid(deptAndOperShowSel.getPkid());
                deptAdd.setTid("NULL");
                objectTemp = deptAdd;
            } else {
                operAdd.setDeptPkid(deptAndOperShowSel.getPkid());
                operAdd.setTid("NULL");
                objectTemp = operAdd;
            }
            if (!submitPreCheck(objectTemp)) {
                return;
            }
            if (deptAndOperService.isExistInDb(objectTemp, strType)) {
                MessageUtil.addError("�ñ�Ŷ�Ӧ��¼�Ѵ��ڣ�������¼�룡");
                return;
            } else {
                addRecordAction(objectTemp, strType);
            }
        } else if (strSubmitType.equals("Upd")) {
            if ("0".equals(deptAndOperShowSel.getType())) {
                objectTemp = deptUpd;
            } else {
                objectTemp = operUpd;
            }
            if (!submitPreCheck(objectTemp)) {
                return;
            }
            updRecordAction(objectTemp, deptAndOperShowSel.getType());
        } else if (strSubmitType.equals("Del")) {
            if ("0".equals(deptAndOperShowSel.getType())) {
                if (deptAndOperService.findChildRecordsByPkid(deptAndOperShowSel)) {
                    MessageUtil.addInfo("�ò�����Ա�����޷�ɾ����");
                    return;
                } else {
                    objectTemp = deptDel;
                }
            } else {
                objectTemp = operDel;
            }
            delRecordAction(objectTemp, deptAndOperShowSel.getType());
        }
        initVariables();
        initData();
    }

    private void fromModelToShowForUpd(Object model,DeptAndOperShow show,String strType){
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
            strTypeTemp = deptAndOperShowSel.getType();
        }
        if ("".equals(strTypeTemp)) {
            MessageUtil.addInfo("��ѡ��������ͣ�");
            return false;
        } else {
            if ("0".equals(strTypeTemp)) {
                Dept deptTemp = (Dept) objectPara;
                if (StringUtils.isEmpty(deptTemp.getName())) {
                    MessageUtil.addInfo("�����벿�����ƣ�");
                    return false;
                }
                if (StringUtils.isEmpty(deptTemp.getId())) {
                    MessageUtil.addInfo("�����벿�ű�ţ�");
                    return false;
                }
            } else {
                Oper operTemp = (Oper) objectPara;
                if (StringUtils.isEmpty(operTemp.getId())) {
                    MessageUtil.addInfo("���������Ա��ţ�");
                    return false;
                }
                if (StringUtils.isEmpty(operTemp.getName())) {
                    MessageUtil.addInfo("���������Ա���ƣ�");
                    return false;
                }
                if (StringUtils.isEmpty(operTemp.getPasswd())) {
                    MessageUtil.addInfo("���������Ա���룡");
                    return false;
                }
            }
        }
        return true;
    }

    private void addRecordAction(Object objectPara, String strTypePara) {
        try {
            deptAndOperService.insertRecord(objectPara, strTypePara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("������Ϣʧ�ܡ�", e);
            MessageUtil.addError("������Ϣʧ�ܡ�");
        }
    }

    private void updRecordAction(Object objectPara, String strTypePara) {
        try {
            deptAndOperService.updateRecord(objectPara, strTypePara);
            MessageUtil.addInfo("����������ɡ�");
        } catch (Exception e) {
            logger.error("������Ϣʧ�ܡ�", e);
            MessageUtil.addError("������Ϣʧ�ܡ�");
        }
    }

    private void delRecordAction(Object objectPara, String strTypePara) {
        try {
            deptAndOperService.deleteRecord(objectPara, strTypePara);
            MessageUtil.addInfo("ɾ��������ɡ�");
        } catch (Exception e) {
            logger.error("ɾ����Ϣʧ�ܡ�", e);
            MessageUtil.addError("ɾ����Ϣʧ�ܡ�");
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
            ((DeptAndOperShow)lastSelectedNode.getData()).setIsDisabled("true");
            lastSelectedNode=currentSelectedNode;
        }
        ((DeptAndOperShow)currentSelectedNode.getData()).setIsDisabled("false");
    }

    /*�����ֶ� Start*/

    public TreeNode getDeptRoot() {
        return deptRoot;
    }

    public void setDeptRoot(TreeNode deptRoot) {
        this.deptRoot = deptRoot;
    }

    public DeptAndOperService getDeptAndOperService() {
        return deptAndOperService;
    }

    public void setDeptAndOperService(DeptAndOperService deptAndOperService) {
        this.deptAndOperService = deptAndOperService;
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

    public DeptAndOperShow getDeptAndOperShowSel() {
        return deptAndOperShowSel;
    }

    public void setDeptAndOperShowSel(DeptAndOperShow deptAndOperShowSel) {
        this.deptAndOperShowSel = deptAndOperShowSel;
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