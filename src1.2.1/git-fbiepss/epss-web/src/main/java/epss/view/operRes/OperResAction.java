package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumEndFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.EsCttInfo;
import epss.repository.model.model_show.CttAndStlInfoShow;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.OperRoleSelectShow;
import epss.service.CttInfoService;
import epss.service.CttItemService;
import epss.service.EsFlowService;
import epss.service.FlowCtrlService;
import epss.service.OperResService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
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
import java.text.SimpleDateFormat;
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
    //获取界面三大panel的内容，并持久化
    private List<CttInfoShow> selResList=new ArrayList<>();
    private List<OperRoleSelectShow>  selOperList=new ArrayList<>();
    // task_function
    private List<SelectItem> taskFunctionList;
    private TreeNode root;
    private TreeNode cttroot;
    private TreeNode selectedNode;
    private List<SelectItem> selTaskFunctionList;

    private String strRendered1;
    private String strRendered2;
    private String strLabel;
    private List<SelectItem> esInitCttList;

    private List<CttAndStlInfoShow> cttAndStlInfoShowList;
    private CttAndStlInfoShow cttAndStlInfoShowAdd;
    private String strSubmitType;
    //ctt tree
    private  TreeNode resRoot;
    @PostConstruct
    public void init() {
        strRendered1 = "false";
        strRendered2 = "false";
        strLabel = "";
        strSubmitType = "Add";
        cttAndStlInfoShowAdd = new CttAndStlInfoShow();
        esInitCttList = new ArrayList<SelectItem>();
        cttAndStlInfoShowList = new ArrayList<CttAndStlInfoShow>();
        initOper();
        initOperRes();
        initFunc();

        cttroot = new DefaultTreeNode("cttroot", null);
        //initCttInfo();
        initRes();
    }
    private void initRes(){
        resRoot = new DefaultTreeNode("ROOT", null);
        CttAndStlInfoShow cttAndStlInfoShow=new CttAndStlInfoShow();
        cttAndStlInfoShow.setName("资源信息");
        TreeNode node0 = new DefaultTreeNode(cttAndStlInfoShow,resRoot);
        recursiveResTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }
    private void recursiveResTreeNode(String parentPkidPara,TreeNode parentNode){
        List<CttAndStlInfoShow> cttAndStlInfoShowList=operResService.selectRecordsFromCtt(parentPkidPara);
        for (int i=0;i<cttAndStlInfoShowList.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(cttAndStlInfoShowList.get(i), parentNode);
            recursiveResTreeNode(cttAndStlInfoShowList.get(i).getPkid(),childNode);
        }
    }
    private void initOper(){
        root = new DefaultTreeNode("Root", null);
        OperRoleSelectShow operRoleSelectShow=new OperRoleSelectShow();
        operRoleSelectShow.setSlename("人员授权");
        operRoleSelectShow.setSeltype("1");
        TreeNode node0 = new DefaultTreeNode(operRoleSelectShow, root);
        recursiveTreeNode("0", node0);
        node0.setExpanded(true);
    }

    private void recursiveTreeNode(String strLevelParentId,TreeNode parentNode){
        List<OperRoleSelectShow> operRoleSelectShowList= operResService.selectOperaRoleRecords(strLevelParentId);
        for (int i=0;i<operRoleSelectShowList.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(operRoleSelectShowList.get(i), parentNode);
            recursiveTreeNode(operRoleSelectShowList.get(i).getSelid(),childNode);
        }
    }

    private void initOperRes(){
        operResShowList=new ArrayList<OperResShow>();
        List<OperResShow> operResShowTempList=new ArrayList<OperResShow>();
        operResShowTempList=operResService.selectOperaResRecords();
        if (operResShowTempList.size()>0){
            String strStatusFlag=null;
            for (int i=0;i<operResShowTempList.size();i++){
                strStatusFlag=operResShowTempList.get(i).getFlowStatus();
                if (ESEnumStatusFlag.STATUS_FLAG0.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG0.getTitle());
                }else if (ESEnumStatusFlag.STATUS_FLAG1.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG1.getTitle());
                }else if (ESEnumStatusFlag.STATUS_FLAG2.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG2.getTitle());
                }else if (ESEnumStatusFlag.STATUS_FLAG3.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG3.getTitle());
                }else if (ESEnumStatusFlag.STATUS_FLAG4.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG4.getTitle());
                }else if (ESEnumStatusFlag.STATUS_FLAG5.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG5.getTitle());
                }
                operResShowList.add(operResShowTempList.get(i));
            }
        }
    }
    public void setEsInitPowerHisActionOfPowerPkidAction() {
        String strCttType = cttAndStlInfoShowAdd.getType();
        if (!strCttType.equals("")) {
            if (strCttType.equals(ESEnum.ITEMTYPE1.getCode())
                    || strCttType.equals(ESEnum.ITEMTYPE6.getCode())
                    || strCttType.equals(ESEnum.ITEMTYPE7.getCode())) {
                strLabel = ESEnum.ITEMTYPE0.getTitle();
                strRendered1 = "true";
                if (strCttType.equals(ESEnum.ITEMTYPE1.getCode())) {
                    strRendered2 = "true";
                } else {
                    strRendered2 = "false";
                }
                List<EsCttInfo> esCttInfoListTemp = new ArrayList<EsCttInfo>();
                esCttInfoListTemp = cttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE0.getCode());
                SelectItem selectItem = new SelectItem("", "全部");
                esInitCttList.add(selectItem);
                if (esCttInfoListTemp.size() > 0) {
                    for (EsCttInfo itemUnit : esCttInfoListTemp) {
                        selectItem = new SelectItem();
                        selectItem.setValue(itemUnit.getPkid());
                        selectItem.setLabel(itemUnit.getName());
                        esInitCttList.add(selectItem);
                    }
                }
            } else if (strCttType.equals(ESEnum.ITEMTYPE0.getCode())) {
                strRendered2 = "true";
            } else {
                strLabel = ESEnum.ITEMTYPE1.getTitle();
                strRendered1 = "true";
                if (strCttType.equals(ESEnum.ITEMTYPE2.getCode())) {
                    strRendered2 = "true";
                } else {
                    strRendered2 = "false";
                }
                List<EsCttInfo> esCttInfoListTemp = new ArrayList<EsCttInfo>();
                esCttInfoListTemp = cttInfoService.getEsInitCttListByCttType(ESEnum.ITEMTYPE1.getCode());
                SelectItem selectItem = new SelectItem("", "全部");
                esInitCttList.add(selectItem);
                if (esCttInfoListTemp.size() > 0) {
                    for (EsCttInfo itemUnit : esCttInfoListTemp) {
                        selectItem = new SelectItem();
                        selectItem.setValue(itemUnit.getPkid());
                        selectItem.setLabel(itemUnit.getName());
                        esInitCttList.add(selectItem);
                    }
                }
            }
        }
    }
    public void onClickForMngAction() {
        try {
            if (strSubmitType.equals("Add")) {
                addRecordAction(cttAndStlInfoShowAdd);
            }
        } catch (Exception e) {
            logger.error("操作失败。", e);
            MessageUtil.addError("操作失败。");
        }
    }

    private void addRecordAction(CttAndStlInfoShow cttAndStlInfoShowPara) {
        if (!submitPreCheck(cttAndStlInfoShowPara)) {
            return;
        }
        if (ESEnum.ITEMTYPE0.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE1.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE2.getCode().equals(cttAndStlInfoShowPara.getType())) {
            if (ESEnum.ITEMTYPE0.getCode().equals(cttAndStlInfoShowPara.getType())) {
                cttAndStlInfoShowPara.setCorrespondingPkid("ROOT");
            }
            operResService.insertEsCttInfo(cttAndStlInfoShowPara);
            MessageUtil.addInfo("新增数据完成。");
        } else {
        }
    }

    private boolean submitPreCheck(CttAndStlInfoShow cttAndStlInfoShowPara) {
        if ("".equals(ToolUtil.getStrIgnoreNull(cttAndStlInfoShowPara.getType()))) {
            MessageUtil.addError("请选择所属类型！");
            return false;
        }
        if (ESEnum.ITEMTYPE0.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE1.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE2.getCode().equals(cttAndStlInfoShowPara.getType())) {
            if ("".equals(ToolUtil.getStrIgnoreNull(cttAndStlInfoShowPara.getName()))) {
                MessageUtil.addError("请输入名称！");
                return false;
            } else {
                if (ESEnum.ITEMTYPE1.getCode().equals(cttAndStlInfoShowPara.getType())
                        && "".equals(ToolUtil.getStrIgnoreNull(cttAndStlInfoShowPara.getCorrespondingPkid()))) {
                    MessageUtil.addError("请选择总包合同名称！");
                    return false;
                }
                if (ESEnum.ITEMTYPE2.getCode().equals(cttAndStlInfoShowPara.getType())){
                    MessageUtil.addError("请选择成本计划名称！");
                    return false;
                }
            }
        }
        if (ESEnum.ITEMTYPE3.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE4.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE5.getCode().equals(cttAndStlInfoShowPara.getType())
                || ESEnum.ITEMTYPE6.getCode().equals(cttAndStlInfoShowPara.getType()))  {
            if (ESEnum.ITEMTYPE3.getCode().equals(cttAndStlInfoShowPara.getType())
                    || ESEnum.ITEMTYPE4.getCode().equals(cttAndStlInfoShowPara.getType())) {
                MessageUtil.addError("请选择成本计划名称！");
                return false;
            } else {
                MessageUtil.addError("请选择总包合同名称！");
                return false;
            }
        }
        return true;
    }
    private void initFunc(){
        taskFunctionList = new ArrayList<SelectItem>();		
        this.selTaskFunctionList= new ArrayList<SelectItem>();
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

    public void selRes(CttInfoShow cttInfoShowPara) {
        if (cttInfoShowPara.getIsSeled()){
            selResList.add(cttInfoShowPara);
        }else{
            selResList.remove(cttInfoShowPara);
        }
    }
    
    public void SelOper(OperRoleSelectShow operRoleSelectShowPara) {
         if (operRoleSelectShowPara.getIsSel()){
             selOperList.add(operRoleSelectShowPara);
         }else{
            selOperList.remove(operRoleSelectShowPara);
         }
    }
    public void saveSelectedMultiple() {
       if(selResList.size()==0){
           MessageUtil.addError("资源列表不能为空，请选择");
           return;
       }
       if(selTaskFunctionList.size()==0){
           MessageUtil.addError("功能列表不能为空，请选择");
           return;
       }
       if(selOperList.size()==0){
           MessageUtil.addError("人员列表不能为空，请选择");
           return;
       }
       for(int i=0;i<selResList.size();i++){
           for(int m=0;m<selTaskFunctionList.size();m++){
               for(int n=0;n<selOperList.size();n++){
                   OperResShow operResShowAdd=new OperResShow();
                   operResShowAdd.setOperPkid(selOperList.get(n).getSelid());
                   operResShowAdd.setOperName(selOperList.get(n).getSlename());
                   operResShowAdd.setFlowStatus((String) selTaskFunctionList.get(m).getValue());
                   operResShowAdd.setFlowStatusName(selResList.get(i).getPreStatusFlag());
                   operResShowAdd.setInfoPkid(selResList.get(i).getPkid());
                   operResShowAdd.setInfoPkidName(selResList.get(i).getName());
                   operResShowAdd.setArchivedFlag(selResList.get(i).getEndFlag());
                   operResShowAdd.setCreatedBy(selResList.get(i).getCreatedBy());
                   operResShowAdd.setCreatedByName(selResList.get(i).getCreatedByName());
                   operResShowAdd.setCreatedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                   //operResShowAdd.setTid(selOperList.get(n).getTid());//人员归属地，待开发
                   operResShowAdd.setInfoType(selResList.get(i).getCttType());
                   operResShowAdd.setLastUpdBy(selResList.get(i).getLastUpdBy());
                   operResShowAdd.setLastUpdTime(selResList.get(i).getLastUpdDate());
                   if (("null").equals(selResList.get(i).getModificationNum())){
                       operResShowAdd.setRecversion(0);
                   }else{
                       operResShowAdd.setRecversion(selResList.get(i).getModificationNum());
                   }
                  operResService.save(operResShowAdd);
               }
           }
       }
       clearList();
    }

    private void clearList(){
        selResList.clear();
        selOperList.clear();
    }
    /*智能字段 Start*/
    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
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

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getCttroot() {
        return cttroot;
    }

    public void setCttroot(TreeNode cttroot) {
        this.cttroot = cttroot;
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
    public List<SelectItem> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<SelectItem> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
    }
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
	
	 public List<SelectItem> getSelTaskFunctionList() {
        return selTaskFunctionList;
    }

    public void setSelTaskFunctionList(List<SelectItem> selTaskFunctionList) {
        this.selTaskFunctionList = selTaskFunctionList;
    }
    

    public List<OperResShow> getOperResShowList() {
        return operResShowList;
    }

    public void setOperResShowList(List<OperResShow> operResShowList) {
        this.operResShowList = operResShowList;
    }
    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public CttAndStlInfoShow getCttAndStlInfoShowAdd() {
        return cttAndStlInfoShowAdd;
    }

    public void setCttAndStlInfoShowAdd(CttAndStlInfoShow cttAndStlInfoShowAdd) {
        this.cttAndStlInfoShowAdd = cttAndStlInfoShowAdd;
    }

    public List<CttAndStlInfoShow> getCttAndStlInfoShowList() {
        return cttAndStlInfoShowList;
    }

    public void setCttAndStlInfoShowList(List<CttAndStlInfoShow> cttAndStlInfoShowList) {
        this.cttAndStlInfoShowList = cttAndStlInfoShowList;
    }

    public List<SelectItem> getEsInitCttList() {
        return esInitCttList;
    }

    public void setEsInitCttList(List<SelectItem> esInitCttList) {
        this.esInitCttList = esInitCttList;
    }

    public String getStrLabel() {
        return strLabel;
    }

    public void setStrLabel(String strLabel) {
        this.strLabel = strLabel;
    }

    public String getStrRendered2() {
        return strRendered2;
    }

    public void setStrRendered2(String strRendered2) {
        this.strRendered2 = strRendered2;
    }

    public String getStrRendered1() {
        return strRendered1;
    }

    public void setStrRendered1(String strRendered1) {
        this.strRendered1 = strRendered1;
    }

    public TreeNode getResRoot() {
        return resRoot;
    }

    public void setResRoot(TreeNode resRoot) {
        this.resRoot = resRoot;
    }
}
