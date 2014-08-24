package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.MessageUtil;;
import skyline.util.ToolUtil;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.OperResShow;
import epss.service.CttInfoService;
import epss.service.CttItemService;
import epss.service.EsFlowService;
import epss.service.FlowCtrlService;
import epss.service.OperResService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
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
import java.util.*;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperResAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperResAction.class);
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
    private List<OperResShow> operResShowList;
    // task_function
    private List<SelectItem> taskFunctionList;
    //��ȡ��������panel�����ݣ����־û�
    private List<CttInfoShow> resSeledList;
    private List<OperResShow> operSeledList;
    private List<SelectItem> taskFunctionSeledList;

    private OperResShow operResShowQry;
    private OperResShow operResShowSel;
    private List<OperResShow> operResShowQryList;

    private List<SelectItem> esInitCttList;
    private List<CttInfoShow> cttInfoShowList;
    private CttInfoShow cttInfoShowAdd;
    private String strSubmitType;
    private List<SelectItem> itemTypeList;
    //ctt tree
    private TreeNode operRoot;
    private TreeNode resRoot;
    @PostConstruct
    public void init() {
        strSubmitType = "Add";

        resSeledList = new ArrayList<>();
        operSeledList = new ArrayList<>();
        esInitCttList = new ArrayList<SelectItem>();
        cttInfoShowList = new ArrayList<CttInfoShow>();
        operResShowQryList = new ArrayList<>();

        cttInfoShowAdd = new CttInfoShow();
        operResShowQry = new OperResShow();
        operResShowSel = new OperResShow();
        // ��Դ
        initRes();
        // ����
        initFunc();
        // �û�
        initOper();
        // ��Դ-�û�-����
        initOperRes();

        initEnuSelectItemList();
    }
    private void initRes(){
        this.resSeledList=new ArrayList<CttInfoShow>();
        CttInfoShow cttInfoShow=new CttInfoShow();
        cttInfoShow.setName("��Դ��Ϣ");
        resRoot = new DefaultTreeNode("ROOT", null);
        TreeNode node0 = new DefaultTreeNode(cttInfoShow,resRoot);
        recursiveResTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }
    private void recursiveResTreeNode(String parentPkidPara,TreeNode parentNode){
        List<CttInfoShow> cttInfoShowList=operResService.selectRecordsFromCtt(parentPkidPara);
        for (int i=0;i<cttInfoShowList.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(cttInfoShowList.get(i), parentNode);
            recursiveResTreeNode(cttInfoShowList.get(i).getPkid(),childNode);
        }
    }

    private void initOper(){
        operRoot = new DefaultTreeNode("Root", null);
        OperResShow operResShowTemp=new OperResShow();
        operResShowTemp.setOperName("��Ա��Ȩ");
        operResShowTemp.setInfoType("1");
        TreeNode node0 = new DefaultTreeNode(operResShowTemp, operRoot);
        recursiveTreeNode("0", node0);
        node0.setExpanded(true);
    }
    private void recursiveTreeNode(String strLevelParentId,TreeNode parentNode){
        List<OperResShow> operResShowListTemp= operResService.selectOperaRoleRecords(strLevelParentId);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveTreeNode(operResShowListTemp.get(i).getOperPkid(),childNode);
        }
    }

    private void initOperRes(){
        operResShowList=new ArrayList<OperResShow>();
        this.operSeledList=new ArrayList<OperResShow>();
        List<OperResShow> operResShowTempList=new ArrayList<OperResShow>();
        operResShowTempList=operResService.selectOperaResRecords();
        if (operResShowTempList.size()>0){
            String strStatusFlag=null;
            for (int i=0;i<operResShowTempList.size();i++){
                strStatusFlag=operResShowTempList.get(i).getFlowStatus();
                operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.getValueByKey(strStatusFlag).getTitle());
                operResShowList.add(operResShowTempList.get(i));
            }
        }
    }
    public void onClickForMngAction() {
        try {
            if (strSubmitType.equals("Add")) {
                addRecordAction(cttInfoShowAdd);
                initRes();
            }
        } catch (Exception e) {
            logger.error("����ʧ�ܡ�", e);
            MessageUtil.addError("����ʧ�ܡ�");
        }
    }

    private void addRecordAction(CttInfoShow cttInfoShowPara) {
        if (!submitPreCheck(cttInfoShowPara)) {
            return;
        }
        if (ESEnum.ITEMTYPE0.getCode().equals(cttInfoShowPara.getType())
                || ESEnum.ITEMTYPE1.getCode().equals(cttInfoShowPara.getType())
                || ESEnum.ITEMTYPE2.getCode().equals(cttInfoShowPara.getType())) {
            if (ESEnum.ITEMTYPE0.getCode().equals(cttInfoShowPara.getType())) {
                cttInfoShowPara.setParentPkid("ROOT");
            }
            cttInfoService.insertRecord(cttInfoShowPara);
            MessageUtil.addInfo("����������ɡ�");
        }
    }

    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if ("".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getType()))) {
            MessageUtil.addError("��ѡ���������ͣ�");
            return false;
        }
        if ("".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()))){
                MessageUtil.addError("���������ƣ�");
                return false;
        }
        if (ESEnum.ITEMTYPE1.getCode().equals(cttInfoShowPara.getType())
                &&"".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()))){
                MessageUtil.addError("��ѡ���ܰ���ͬ���ƣ�");
                return false;
        }
        if (ESEnum.ITEMTYPE2.getCode().equals(cttInfoShowPara.getType())
                &&"".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()))){
            MessageUtil.addError("��ѡ��ɱ��ƻ����ƣ�");
            return false;
        }
        return true;
    }

    private void initEnuSelectItemList() {
        itemTypeList=new ArrayList<SelectItem>();
        itemTypeList.add(new SelectItem("", "ȫ��"));
        itemTypeList.add(new SelectItem(ESEnum.ITEMTYPE0.getCode(),ESEnum.ITEMTYPE0.getTitle()));
        itemTypeList.add(new SelectItem(ESEnum.ITEMTYPE1.getCode(),ESEnum.ITEMTYPE1.getTitle()));
        itemTypeList.add(new SelectItem(ESEnum.ITEMTYPE2.getCode(),ESEnum.ITEMTYPE2.getTitle()));
    }

    private void initFunc(){
        taskFunctionList = new ArrayList<SelectItem>();		
        this.taskFunctionSeledList= new ArrayList<SelectItem>();
        taskFunctionList.add(new SelectItem("","ȫ��"));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG0.getCode(),ESEnumStatusFlag.STATUS_FLAG0.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG1.getCode(),ESEnumStatusFlag.STATUS_FLAG1.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG2.getCode(),ESEnumStatusFlag.STATUS_FLAG2.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG3.getCode(),ESEnumStatusFlag.STATUS_FLAG3.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG4.getCode(),ESEnumStatusFlag.STATUS_FLAG4.getTitle()));
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG5.getCode(),ESEnumStatusFlag.STATUS_FLAG5.getTitle()));
    }

    public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSel= (OperResShow) BeanUtils.cloneBean(operResShowPara);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void onQueryAction() {
        try {
            operResShowQryList.clear();
            List<OperResShow> operResShowQryTempList = new ArrayList<OperResShow>();
            if (!("".equals(ToolUtil.getStrIgnoreNull(operResShowQry.getInfoPkidName()).trim()))) {
                operResShowQry.setInfoPkidName("%"+operResShowQry.getInfoPkidName()+"%");
            }
            operResShowQryTempList = operResService.selectOperaResRecordsByModelShow(operResShowQry);
            if (!(operResShowQryTempList.size() > 0)) {
                MessageUtil.addInfo("û�в�ѯ�����ݡ�");
            } else {
                for (int i = 0; i < operResShowQryTempList.size(); i++) {
                    operResShowQryTempList.get(i).setFlowStatusName(
                            ESEnumStatusFlag.getValueByKey(operResShowQryTempList.get(i).getFlowStatus()).getTitle()
                    );
                    operResShowQryTempList.get(i).setInfoPkidName(
                            "("+ESEnum.getValueByKey(operResShowQryTempList.get(i).getInfoType()).getTitle()+")-"
                                    +operResShowQryTempList.get(i).getInfoPkidName()
                    );
                    operResShowQryList.add(operResShowQryTempList.get(i));
                }
            }
        } catch (Exception e) {
            logger.error("Ȩ��׷����Ϣ��ѯʧ��", e);
            MessageUtil.addError("Ȩ��׷����Ϣ��ѯʧ��");
        }
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

    public List<OperResShow> getOperResShowList() {
        return operResShowList;
    }

    public void setOperResShowList(List<OperResShow> operResShowList) {
        this.operResShowList = operResShowList;
    }

    public List<CttInfoShow> getResSeledList() {
        return resSeledList;
    }

    public void setResSeledList(List<CttInfoShow> resSeledList) {
        this.resSeledList = resSeledList;
    }

    public List<OperResShow> getOperSeledList() {
        return operSeledList;
    }

    public void setOperSeledList(List<OperResShow> operSeledList) {
        this.operSeledList = operSeledList;
    }

    public List<SelectItem> getTaskFunctionSeledList() {
        return taskFunctionSeledList;
    }

    public void setTaskFunctionSeledList(List<SelectItem> taskFunctionSeledList) {
        this.taskFunctionSeledList = taskFunctionSeledList;
    }

    public List<SelectItem> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<SelectItem> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
    }

    public TreeNode getOperRoot() {
        return operRoot;
    }

    public void setOperRoot(TreeNode operRoot) {
        this.operRoot = operRoot;
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

    public CttInfoShow getCttInfoShowAdd() {
        return cttInfoShowAdd;
    }

    public void setCttInfoShowAdd(CttInfoShow cttInfoShowAdd) {
        this.cttInfoShowAdd = cttInfoShowAdd;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public TreeNode getResRoot() {
        return resRoot;
    }

    public void setResRoot(TreeNode resRoot) {
        this.resRoot = resRoot;
    }

    public List<SelectItem> getItemTypeList() {
        return itemTypeList;
    }

    public void setItemTypeList(List<SelectItem> itemTypeList) {
        this.itemTypeList = itemTypeList;
    }

    public OperResShow getOperResShowQry() {
        return operResShowQry;
    }

    public void setOperResShowQry(OperResShow operResShowQry) {
        this.operResShowQry = operResShowQry;
    }

    public OperResShow getOperResShowSel() {
        return operResShowSel;
    }

    public void setOperResShowSel(OperResShow operResShowSel) {
        this.operResShowSel = operResShowSel;
    }

    public List<OperResShow> getOperResShowQryList() {
        return operResShowQryList;
    }

    public void setOperResShowQryList(List<OperResShow> operResShowQryList) {
        this.operResShowQryList = operResShowQryList;
    }
}
