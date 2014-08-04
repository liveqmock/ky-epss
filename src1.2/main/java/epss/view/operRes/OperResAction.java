package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumEndFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.common.utils.MessageUtil;
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
    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        cttroot = new DefaultTreeNode("cttroot", null);
        OperRoleSelectShow operRoleSelectShow=new OperRoleSelectShow();
        operRoleSelectShow.setSlename("人员授权");
        operRoleSelectShow.setSeltype("1");
        TreeNode node0 = new DefaultTreeNode(operRoleSelectShow, root);
        recursiveTreeNode("0", node0);
        node0.setExpanded(true);
        //expandTreeNode(node0);
        initOperRes();
        initCttInfo();
        this.taskFunctionList = new ArrayList<SelectItem>();
        this.selTaskFunctionList= new ArrayList<SelectItem>();
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG0.getCode(), ESEnumStatusFlag.STATUS_FLAG0.getTitle()));
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
    private void recursiveTreeNode(String strLevelParentId,TreeNode parentNode){
        List<OperRoleSelectShow> operRoleSelectShowList= operResService.selectOperaRoleRecords(strLevelParentId);
        for (int i=0;i<operRoleSelectShowList.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(operRoleSelectShowList.get(i), parentNode);
            recursiveTreeNode(operRoleSelectShowList.get(i).getSelid(), childNode);
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
                    operResShowTempList.get(i).setIsSel(false);
                }else if (ESEnumStatusFlag.STATUS_FLAG1.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG1.getTitle());
                    operResShowTempList.get(i).setIsSel(false);
                }else if (ESEnumStatusFlag.STATUS_FLAG2.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG2.getTitle());
                    operResShowTempList.get(i).setIsSel(false);
                }else if (ESEnumStatusFlag.STATUS_FLAG3.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG3.getTitle());
                    operResShowTempList.get(i).setIsSel(false);
                }else if (ESEnumStatusFlag.STATUS_FLAG4.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG4.getTitle());
                    operResShowTempList.get(i).setIsSel(false);
                }else if (ESEnumStatusFlag.STATUS_FLAG5.getCode().equals(strStatusFlag)){
                    operResShowTempList.get(i).setFlowStatusName(ESEnumStatusFlag.STATUS_FLAG5.getTitle());
                    operResShowTempList.get(i).setIsSel(false);
                }
                operResShowList.add(operResShowTempList.get(i));
            }
        }
    }
    public void initCttInfo() {
        CttInfoShow tkCttInfoShowQry = new CttInfoShow();
        tkCttInfoShowQry.setCttType(ESEnum.ITEMTYPE0.getCode());
        tkCttInfoShowQry.setStrStatusFlagBegin(null);
        tkCttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG5.getCode());
        List<CttInfoShow> tkCttInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(tkCttInfoShowQry);
        for (CttInfoShow item : tkCttInfoShowTreeList) {
            TreeNode childNodeTkctt = new DefaultTreeNode(
                    new CttInfoShow(
                            item.getPkid(),
                            item.getId(),
                            item.getCttType(),
                            item.getName(),
                            item.getNote(),
                            esFlowControl.getLabelByValueInPreStatusFlaglist(item.getStatusFlag()),
                            esFlowControl.getLabelByValueInPreStatusFlaglist(item.getPreStatusFlag()),
                            item.getEndFlag(),
                            item.getLastUpdBy(),
                            item.getLastUpdDate(),
                            item.getModificationNum(),
                            false),
                        cttroot);
            CttInfoShow cstplInfoShowQry = new CttInfoShow();
            cstplInfoShowQry.setCttType(ESEnum.ITEMTYPE1.getCode());
            cstplInfoShowQry.setStrStatusFlagBegin(null);
            cstplInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG5.getCode());
            cstplInfoShowQry.setParentPkid(item.getPkid());
            List<CttInfoShow> cstplInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(cstplInfoShowQry);
            for (CttInfoShow cstplItem : cstplInfoShowTreeList) {
                TreeNode childNodeCstpl = new DefaultTreeNode(
                        new CttInfoShow(
                                cstplItem.getPkid(),
                                cstplItem.getId(),
                                cstplItem.getCttType(),
                                cstplItem.getName(),
                                cstplItem.getNote(),
                                esFlowControl.getLabelByValueInPreStatusFlaglist(cstplItem.getStatusFlag()),
                                esFlowControl.getLabelByValueInPreStatusFlaglist(cstplItem.getPreStatusFlag()),
                                cstplItem.getEndFlag(),
                                cstplItem.getLastUpdBy(),
                                cstplItem.getLastUpdDate(),
                                cstplItem.getModificationNum(),
                                false),
                        childNodeTkctt);
                CttInfoShow subCttInfoShowQry = new CttInfoShow();
                subCttInfoShowQry.setCttType(ESEnum.ITEMTYPE2.getCode());
                subCttInfoShowQry.setStrStatusFlagBegin(null);
                subCttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG5.getCode());
                subCttInfoShowQry.setParentPkid(cstplItem.getPkid());
                List<CttInfoShow> subCttInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(subCttInfoShowQry);
                for (CttInfoShow subItem : subCttInfoShowTreeList) {
                    if (subItem.getPkid() != null) {
                        new DefaultTreeNode(
                            new CttInfoShow(
                                subItem.getPkid(),
                                subItem.getId(),
                                subItem.getCttType(),
                                subItem.getName(),
                                subItem.getNote(),
                                esFlowControl.getLabelByValueInPreStatusFlaglist(subItem.getStatusFlag()),
                                esFlowControl.getLabelByValueInPreStatusFlaglist(subItem.getPreStatusFlag()),
                                subItem.getEndFlag(),
                                subItem.getLastUpdBy(),
                                subItem.getLastUpdDate(),
                                subItem.getModificationNum(),
                                false),
                            childNodeCstpl
                        );
                    }
                }
            }
        }
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

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
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

    public List<SelectItem> getSelTaskFunctionList() {
        return selTaskFunctionList;
    }

    public void setSelTaskFunctionList(List<SelectItem> selTaskFunctionList) {
        this.selTaskFunctionList = selTaskFunctionList;
    }
}
