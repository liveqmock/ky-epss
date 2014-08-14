package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumDeletedFlag;
import epss.common.enums.ESEnumEndFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsInitPower;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.*;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.service.PlatformService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperFuncResAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncResAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;

    private List<CommColModel> taskFunctionList;
    private List<CommColModel> taskFunctionSeledList;

    private List<DeptAndOperShow> deptAndOperShowSeledList;

    private List<OperFuncResShow> operFuncResShowList;

    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;

    private List<SelectItem> esInitCttList;
    private List<CttInfoShow> cttInfoShowList;
    //ctt tree
    private TreeNode resRoot;
    private TreeNode operRoot;
    @PostConstruct
    public void init() {
        cttInfoShowAdd=new CttInfoShow();
        cttInfoShowUpd=new CttInfoShow();
        cttInfoShowDel=new CttInfoShow();
        esInitCttList = new ArrayList<>();
        cttInfoShowList = new ArrayList<>();
        taskFunctionList = new ArrayList<>();
        taskFunctionSeledList = new ArrayList<>();
        deptAndOperShowSeledList= new ArrayList<>();
        taskFunctionList.add(
                new CommColModel(ESEnumStatusFlag.STATUS_FLAG0.getCode(),ESEnumStatusFlag.STATUS_FLAG0.getTitle()));
        taskFunctionList.add(
                new CommColModel(ESEnumStatusFlag.STATUS_FLAG1.getCode(),ESEnumStatusFlag.STATUS_FLAG1.getTitle()));
        taskFunctionList.add(
                new CommColModel(ESEnumStatusFlag.STATUS_FLAG2.getCode(),ESEnumStatusFlag.STATUS_FLAG2.getTitle()));
        taskFunctionList.add(
                new CommColModel(ESEnumStatusFlag.STATUS_FLAG3.getCode(),ESEnumStatusFlag.STATUS_FLAG3.getTitle()));
        taskFunctionList.add(
                new CommColModel(ESEnumStatusFlag.STATUS_FLAG4.getCode(),ESEnumStatusFlag.STATUS_FLAG4.getTitle()));
        taskFunctionList.add(
                new CommColModel(ESEnumStatusFlag.STATUS_FLAG5.getCode(),ESEnumStatusFlag.STATUS_FLAG5.getTitle()));

        // ��Դ-�û�-����
        initRes();
        initOper();
    }
    private void initRes(){
        OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
        operFuncResShowTemp.setResPkid("ROOT");
        operFuncResShowTemp.setResName("��Դ��Ϣ");
        resRoot = new DefaultTreeNode("ROOT", null);
        TreeNode node0 = new DefaultTreeNode(operFuncResShowTemp,resRoot);
        recursiveResTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }
    private void initOper(){
        DeptAndOperShow deptAndOperShowTemp=new DeptAndOperShow();
        deptAndOperShowTemp.setPkid("ROOT");
        deptAndOperShowTemp.setName("��Ա��Ϣ");
        operRoot = new DefaultTreeNode("ROOT", null);
        TreeNode node0 = new DefaultTreeNode(deptAndOperShowTemp, operRoot);
        recursiveOperTreeNode("0", node0);
        node0.setExpanded(true);
    }
    private void recursiveResTreeNode(String parentPkidPara,TreeNode parentNode){
        List<CttInfoShow> cttInfoShowList=operResService.selectRecordsFromCtt(parentPkidPara);
        for (int i=0;i<cttInfoShowList.size();i++){
            CttInfoShow cttInfoShowTemp =cttInfoShowList.get(i);
            OperResShow operResShowPara=new OperResShow();
            operResShowPara.setInfoType(cttInfoShowTemp.getType());
            operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
            List<OperResShow> operResShowListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
            String strInputOperName="";
            String strCheckOperName="";
            String strDoubleCheckOperName="";
            String strApproveOperName="";
            String strAccountOperName="";
            String strPlaceOnFileOperName="";
            for(OperResShow operResShowUnit:operResShowListTemp){
                if(operResShowUnit.getFlowStatus().equals("0")){
                    strInputOperName= strInputOperName +"," + operResShowUnit.getOperName();
                }else if(operResShowUnit.getFlowStatus().equals("1")){
                    strCheckOperName= strCheckOperName +"," + operResShowUnit.getOperName();
                }else if(operResShowUnit.getFlowStatus().equals("2")){
                    strDoubleCheckOperName= strDoubleCheckOperName +"," + operResShowUnit.getOperName();
                }else if(operResShowUnit.getFlowStatus().equals("3")){
                    strApproveOperName= strApproveOperName +"," + operResShowUnit.getOperName();
                }else if(operResShowUnit.getFlowStatus().equals("4")){
                    strAccountOperName= strAccountOperName +"," + operResShowUnit.getOperName();
                }else if(operResShowUnit.getFlowStatus().equals("5")){
                    strPlaceOnFileOperName= strPlaceOnFileOperName +"," + operResShowUnit.getOperName();
                }
            }
            OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
            operFuncResShowTemp.setResType(cttInfoShowTemp.getCttType());
            operFuncResShowTemp.setResPkid(cttInfoShowTemp.getPkid());
            operFuncResShowTemp.setResName(cttInfoShowTemp.getName());
            operFuncResShowTemp.setInputOperName(strInputOperName);
            operFuncResShowTemp.setCheckOperName(strCheckOperName);
            operFuncResShowTemp.setDoubleCheckOperName(strDoubleCheckOperName);
            operFuncResShowTemp.setApproveOperName(strApproveOperName);
            operFuncResShowTemp.setAccountOperName(strAccountOperName);
            operFuncResShowTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
            TreeNode childNodeTemp = new DefaultTreeNode(operFuncResShowTemp, parentNode);
            recursiveResTreeNode(operFuncResShowTemp.getResPkid(),childNodeTemp);
        }
    }
    private void recursiveOperTreeNode(String strLevelParentId,TreeNode parentNode){
        List<DeptAndOperShow> operResShowListTemp= operResService.getOperList(strLevelParentId);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getPkid(), childNode);
        }
    }

    public void selectRecordAction(String strSubmitTypePara,OperFuncResShow operFuncResShowPara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = new CttInfoShow();
                if(operFuncResShowPara.getResPkid().equals("ROOT")) {
                    cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE0.getCode());
                    cttInfoShowAdd.setParentPkid("ROOT");
                }else if(operFuncResShowPara.getResType().equals(ESEnum.ITEMTYPE0.getCode())) {
                    cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE1.getCode());
                    cttInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }else if(operFuncResShowPara.getResPkid().equals(ESEnum.ITEMTYPE1.getCode())) {
                    cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE2.getCode());
                    cttInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }
                cttInfoShowAdd.setId(cttInfoService.getStrMaxCttId(cttInfoShowAdd.getCttType()));
            } else if (strSubmitTypePara.equals("Upd")){
                cttInfoShowUpd = fromResModelShowToCttInfoShow(operFuncResShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                cttInfoShowDel = fromResModelShowToCttInfoShow(operFuncResShowPara);
            }else if (strSubmitTypePara.equals("Sel")) {
                cttInfoShowSel = fromResModelShowToCttInfoShow(operFuncResShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    public void selOperRecordAction(DeptAndOperShow deptAndOperShowPara){
        if (deptAndOperShowPara.getIsSeled()){
            deptAndOperShowSeledList.add(deptAndOperShowPara);
        }else{
            deptAndOperShowSeledList.remove(deptAndOperShowPara);
        }
    }

    /**
     * �ύά��Ȩ��
     *
     * @param
     */
    public void onClickForMngAction(String strSubmitTypePara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                if (!submitPreCheck(cttInfoShowAdd)) {
                    return;
                }
                if (cttInfoService.isExistInDb(cttInfoShowAdd)) {
                    MessageUtil.addError("�ü�¼�Ѵ��ڣ�������¼�룡");
                    return;
                } else {
                    cttInfoService.insertRecord(cttInfoShowAdd);
                    MessageUtil.addInfo("����������ɡ�");
                    cttInfoShowAdd = new CttInfoShow();
                }
            } else if (strSubmitTypePara.equals("Upd")) {
                EsInitPower esInitPowerTemp = new EsInitPower();
                esInitPowerTemp.setPowerType(cttInfoShowUpd.getCttType());
                esInitPowerTemp.setPowerPkid(cttInfoShowUpd.getPkid());
                esInitPowerTemp.setPeriodNo("NULL");
                if (flowCtrlService.selectListByModel(esInitPowerTemp).size() > 0) {
                    MessageUtil.addInfo("�����ѱ����ã����ɸ��£�");
                    return;
                }
                cttInfoService.updateRecord(cttInfoShowUpd);
                MessageUtil.addInfo("����������ɡ�");
            } else if (strSubmitTypePara.equals("Del")) {
                EsInitPower esInitPowerTemp = new EsInitPower();
                esInitPowerTemp.setPowerType(cttInfoShowDel.getCttType());
                esInitPowerTemp.setPowerPkid(cttInfoShowDel.getPkid());
                esInitPowerTemp.setPeriodNo("NULL");
                if (flowCtrlService.selectListByModel(esInitPowerTemp).size() > 0) {
                    MessageUtil.addInfo("�����ѱ����ã�����ɾ����");
                    return;
                }
                int deleteRecordNumOfCttItem = cttItemService.deleteRecord(cttInfoShowDel);
                int deleteRecordNumOfCtt = cttInfoService.deleteRecord(cttInfoShowDel.getPkid());
                int deleteRecordNumOfPower = flowCtrlService.deleteRecord(
                        cttInfoShowDel.getCttType(),
                        cttInfoShowDel.getPkid(),
                        "NULL");
                if (deleteRecordNumOfCtt <= 0 && deleteRecordNumOfPower <= 0 && deleteRecordNumOfCttItem <= 0) {
                    MessageUtil.addInfo("�ü�¼��ɾ����");
                    return;
                }
                MessageUtil.addInfo("ɾ��������ɡ�");
            } else if (strSubmitTypePara.equals("Power")) {
                if (taskFunctionSeledList.size() == 0) {
                    MessageUtil.addError("�����б�����Ϊ�գ���ѡ��");
                    return;
                }
                if (deptAndOperShowSeledList.size() == 0) {
                    MessageUtil.addError("��Ա�б�����Ϊ�գ���ѡ��");
                    return;
                }
                for (int m = 0; m < taskFunctionSeledList.size(); m++) {
                    for (int n = 0; n < deptAndOperShowSeledList.size(); n++) {
                        OperRes operResTemp = new OperRes();
                        operResTemp.setInfoType(cttInfoShowSel.getCttType());
                        operResTemp.setInfoPkid(cttInfoShowSel.getPkid());
                        operResTemp.setOperPkid(deptAndOperShowSeledList.get(n).getPkid());
                        operResTemp.setFlowStatus(taskFunctionSeledList.get(m).getColumn_id());
                        operResTemp.setArchivedFlag(ESEnumDeletedFlag.DELETED_FLAG0.getCode());
                        operResService.insertRecord(operResTemp);
                    }
                }
            }
            initRes();
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("��ʼ��ʧ��", e);
        }
    }

    private CttInfoShow fromResModelShowToCttInfoShow(OperFuncResShow operFuncResShowPara){
        CttInfoShow cttInfoShowTemp=new CttInfoShow();
        cttInfoShowTemp.setCttType(operFuncResShowPara.getResType());
        cttInfoShowTemp.setPkid(operFuncResShowPara.getResPkid());
        cttInfoShowTemp.setName(operFuncResShowPara.getResName());
        return cttInfoShowTemp;
    }

    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if ("".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()))){
            MessageUtil.addError("���������ƣ�");
            return false;
        }
        return true;
    }

    /*�����ֶ� Start*/

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }

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

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsFlowService getEsFlowService() {
        return esFlowService;
    }

    public void setEsFlowService(EsFlowService esFlowService) {
        this.esFlowService = esFlowService;
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

    public List<CommColModel> getTaskFunctionSeledList() {
        return taskFunctionSeledList;
    }

    public void setTaskFunctionSeledList(List<CommColModel> taskFunctionSeledList) {
        this.taskFunctionSeledList = taskFunctionSeledList;
    }

    public List<CommColModel> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<CommColModel> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
    }

    public List<SelectItem> getEsInitCttList() {
        return esInitCttList;
    }

    public void setEsInitCttList(List<SelectItem> esInitCttList) {
        this.esInitCttList = esInitCttList;
    }

    public List<CttInfoShow> getCttInfoShowList() {
        return cttInfoShowList;
    }

    public void setCttInfoShowList(List<CttInfoShow> cttInfoShowList) {
        this.cttInfoShowList = cttInfoShowList;
    }

    public TreeNode getResRoot() {
        return resRoot;
    }

    public void setResRoot(TreeNode resRoot) {
        this.resRoot = resRoot;
    }

    public TreeNode getOperRoot() {
        return operRoot;
    }

    public void setOperRoot(TreeNode operRoot) {
        this.operRoot = operRoot;
    }

    public List<OperFuncResShow> getOperFuncResShowList() {
        return operFuncResShowList;
    }

    public void setOperFuncResShowList(List<OperFuncResShow> operFuncResShowList) {
        this.operFuncResShowList = operFuncResShowList;
    }

    public CttInfoShow getCttInfoShowAdd() {
        return cttInfoShowAdd;
    }

    public void setCttInfoShowAdd(CttInfoShow cttInfoShowAdd) {
        this.cttInfoShowAdd = cttInfoShowAdd;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowDel() {
        return cttInfoShowDel;
    }

    public void setCttInfoShowDel(CttInfoShow cttInfoShowDel) {
        this.cttInfoShowDel = cttInfoShowDel;
    }
}