package epss.view.deptOper;

import skyline.security.MD5Helper;
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

    private TreeNode deptOperRoot;
    private String strSubmitType;
    private Dept deptAdd;
    private Dept deptUpd;
    private Dept deptDel;
    private Oper operAdd;
    private Oper operUpd;
    private Oper operDel;
    private List<SelectItem> operSexList;
    private List<SelectItem> operIsSuperList;
    private List<SelectItem> typeList;
    private List<SelectItem> enableList;

    @PostConstruct
    public void init() {
        initVariables();
        initData();
    }

    private void initVariables() {
        deptAdd = new Dept();
        deptUpd = new Dept();
        deptDel = new Dept();
        operAdd = new Oper();
        operUpd = new Oper();
        operDel = new Oper();
    }

    private void initData() {
        operSexList = new ArrayList<SelectItem>();
        operSexList.add(new SelectItem("1", "��"));
        operSexList.add(new SelectItem("0", "Ů"));
        operIsSuperList = new ArrayList<SelectItem>();
        operIsSuperList.add(new SelectItem("0", "��"));
        operIsSuperList.add(new SelectItem("1", "��"));
        typeList = new ArrayList<SelectItem>();
        typeList.add(new SelectItem("0", "���ӻ�����Ϣ"));
        typeList.add(new SelectItem("1", "������Ա��Ϣ"));
        enableList= new ArrayList<SelectItem>();
        enableList.add(new SelectItem("1", "����"));
        enableList.add(new SelectItem("0", "������"));
        initDeptOper();
    }

    private void initDeptOper() {
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp = new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("������Ա��Ϣ");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }

    private void recursiveTreeNode(String strParentPkidPara, TreeNode parentNode) {
        List<DeptOperShow> deptOperShowTempList= deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (int i = 0; i < deptOperShowTempList.size(); i++) {
            TreeNode childNode = new DefaultTreeNode(deptOperShowTempList.get(i), parentNode);
            recursiveTreeNode(deptOperShowTempList.get(i).getId(), childNode);
        }
    }

    public void selectRecordAction(String strSubmitTypePara,
                                     DeptOperShow deptOperShowPara) {
        try {
            strSubmitType = strSubmitTypePara;
            if (strSubmitTypePara.contains("Dept")) {
                if (strSubmitTypePara.contains("Add")) {
                    deptAdd = new Dept();
                    deptAdd.setParentpkid(deptOperShowPara.getPkid());
                } else {
                    if (strSubmitTypePara.contains("Upd")) {
                        deptUpd = new Dept();
                        deptUpd = (Dept) deptOperService.selectRecordByPkid(deptOperShowPara);
                    } else if (strSubmitTypePara.contains("Del")) {
                        deptDel = new Dept();
                        deptDel = (Dept) deptOperService.selectRecordByPkid(deptOperShowPara);
                    }
                }
            } else if (strSubmitTypePara.contains("Oper")) {
                if (strSubmitTypePara.contains("Add")){
                    operAdd = new Oper();
                    operAdd.setDeptPkid(deptOperShowPara.getPkid());
                }else if (strSubmitTypePara.contains("Upd")) {
                    operUpd = new Oper();
                    operUpd = (Oper) deptOperService.selectRecordByPkid(deptOperShowPara);
                    operUpd.setPasswd(MD5Helper.getMD5String(operUpd.getPasswd()));
                } else if (strSubmitTypePara.contains("Del")) {
                    operDel = new Oper();
                    operDel = (Oper) deptOperService.selectRecordByPkid(deptOperShowPara);
                    operUpd.setPasswd(MD5Helper.getMD5String(operUpd.getPasswd()));
                }
            }
        } catch (Exception e) {
            logger.error("��ʼ����Ϣʧ�ܡ�", e);
            MessageUtil.addError("��ʼ����Ϣʧ�ܡ�");
        }
    }

    public void onClickForMngAction() {
        try {
            if (strSubmitType.contains("Dept")) {
                if (strSubmitType.contains("Add")) {
                    if (!submitDeptPreCheck(deptAdd)) {
                        return;
                    }
                    if (deptOperService.isExistInDeptDb(deptAdd)) {
                        MessageUtil.addError("�ñ�Ż����Ѵ��ڣ�������¼�룡");
                        return;
                    }
                    deptOperService.insertDeptRecord(deptAdd);
                } else if (strSubmitType.contains("Upd")) {
                    if (!submitDeptPreCheck(deptUpd)) {
                        return;
                    }
                    deptOperService.updateDeptRecord(deptUpd);
                } else if (strSubmitType.contains("Del")) {
                    if (deptOperService.findChildRecordsByPkid(deptDel.getPkid())) {
                        MessageUtil.addInfo("�ò�����Ա�����޷�ɾ����");
                        return;
                    }
                    deptOperService.deleteDeptRecord(deptDel);
                }
            } else if (strSubmitType.contains("Oper")) {
                if (strSubmitType.contains("Add")) {
                    if (!submitOperPreCheck(operAdd)) {
                        return;
                    }
                    if (deptOperService.isExistInOperDb(operAdd)) {
                        MessageUtil.addError("�ñ���û��Ѵ��ڣ�������¼�룡");
                        return;
                    }
                    operAdd.setPasswd(MD5Helper.getMD5String(operAdd.getPasswd()));
                    deptOperService.insertOperRecord(operAdd);
                } else if (strSubmitType.contains("Upd")) {
                    if (!submitOperPreCheck(operUpd)) {
                        return;
                    }
                    operUpd.setPasswd(MD5Helper.getMD5String(operUpd.getPasswd()));
                    deptOperService.updateOperRecord(operUpd);
                } else if (strSubmitType.contains("Del")) {
                    deptOperService.deleteOperRecord(operDel);
                }
            }
            initVariables();
            initData();
            MessageUtil.addInfo("���ݴ���ɹ���");
        }catch (Exception e){
            logger.error("���ݴ���ʧ�ܡ�", e);
            MessageUtil.addError("���ݴ���ʧ�ܡ�");
        }
    }

    private boolean submitDeptPreCheck(Dept deptPara) {
        if (StringUtils.isEmpty(deptPara.getName())) {
            MessageUtil.addInfo("�����벿�����ƣ�");
            return false;
        }
        if (StringUtils.isEmpty(deptPara.getId())) {
            MessageUtil.addInfo("�����벿�ű�ţ�");
            return false;
        }
        return true;
    }
    private boolean submitOperPreCheck(Oper operPara) {
        if (StringUtils.isEmpty(operPara.getId())) {
            MessageUtil.addInfo("���������Ա��ţ�");
            return false;
        }
        if (StringUtils.isEmpty(operPara.getName())) {
            MessageUtil.addInfo("���������Ա���ƣ�");
            return false;
        }
        if (StringUtils.isEmpty(operPara.getPasswd())) {
            MessageUtil.addInfo("���������Ա���룡");
            return false;
        }
        return true;
    }

    /*�����ֶ� Start*/

    public TreeNode getDeptOperRoot() {
        return deptOperRoot;
    }

    public void setDeptOperRoot(TreeNode deptOperRoot) {
        this.deptOperRoot = deptOperRoot;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
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

    public List<SelectItem> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<SelectItem> typeList) {
        this.typeList = typeList;
    }

    public List<SelectItem> getEnableList() {
        return enableList;
    }

    public void setEnableList(List<SelectItem> enableList) {
        this.enableList = enableList;
    }
}
