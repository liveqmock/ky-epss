package epss.view.operRole;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.OperRoleSelectShow;
import epss.service.CttInfoService;
import epss.service.CttItemService;
import epss.service.EsFlowService;
import epss.service.FlowCtrlService;
import epss.service.OperRoleSelectService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperRoleSelectAction {
    private static final Logger logger = LoggerFactory.getLogger(OperRoleSelectAction.class);
    @ManagedProperty(value = "#{operRoleSelectService}")
    private OperRoleSelectService operRoleSelectService;
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
    private TreeNode cttroot;
    private TreeNode selectedNode;
    //tkctt
    private CttInfoShow tkCttInfoShowQry;
    private static List<CttInfoShow> tkCttInfoShowTreeList;
    //cstpl
    private CttInfoShow cstplInfoShowQry;
    private static List<CttInfoShow> cstplInfoShowTreeList;
    // subctt
    private CttInfoShow subCttInfoShowQry;
    private static List<CttInfoShow> subCttInfoShowTreeList;
    private CttInfoShow cttInfoShow;
    private TreeNode root;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        cttroot = new DefaultTreeNode("cttroot", null);
        OperRoleSelectShow operRoleSelectShow=new OperRoleSelectShow();
        operRoleSelectShow.setSlename("人员授权");
        operRoleSelectShow.setSeltype("1");
        TreeNode node0 = new DefaultTreeNode(operRoleSelectShow, root);
        recursiveTreeNode("0",node0);
        node0.setExpanded(true);
        //expandTreeNode(node0);
        cttInfoShow = new CttInfoShow();
        initCttInfo();
    }
    private void recursiveTreeNode(String strLevelParentId,TreeNode parentNode){
        List<OperRoleSelectShow> operRoleSelectShowList=operRoleSelectService.selectOperaRoleRecords(strLevelParentId);
        for (int i=0;i<operRoleSelectShowList.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(operRoleSelectShowList.get(i), parentNode);
            recursiveTreeNode(operRoleSelectShowList.get(i).getSelid(),childNode);
        }
    }

    private void expandTreeNode(TreeNode node){
        node.setExpanded(true);
        for (int i=0;i<node.getChildCount();i++){
            node.getChildren().get(i).setExpanded(true);
            expandTreeNode(node.getChildren().get(i));
        }
    }

    public OperRoleSelectService getOperRoleSelectService() {
        return operRoleSelectService;
    }

    public void setOperRoleSelectService(OperRoleSelectService operRoleSelectService) {
        this.operRoleSelectService = operRoleSelectService;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getCttroot() {
        return cttroot;
    }

    public void setCttroot(TreeNode cttroot) {
        this.cttroot = cttroot;
    }
    public void initCttInfo() {
        tkCttInfoShowTreeList = new ArrayList<CttInfoShow>();
        cttroot = new DefaultTreeNode("cttroot", null);
        tkCttInfoShowQry = new CttInfoShow();
        tkCttInfoShowQry.setCttType(ESEnum.ITEMTYPE0.getCode());
        tkCttInfoShowQry.setStrStatusFlagBegin(null);
        tkCttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
        tkCttInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(tkCttInfoShowQry);
        for (CttInfoShow item : tkCttInfoShowTreeList) {
            TreeNode childNodeTkctt = null;
            if (item.getPkid() != null) {
                cstplInfoShowTreeList = new ArrayList<CttInfoShow>();
                childNodeTkctt = new DefaultTreeNode(new CttInfoShow(item.getId(), item.getName(),
                        esCommon.getCustNameByCustIdFromList(item.getSignPartA()),
                        esCommon.getCustNameByCustIdFromList(item.getSignPartB()),
                        esFlowControl.getLabelByValueInPreStatusFlaglist(item.getStatusFlag()),
                        esFlowControl.getLabelByValueInPreStatusFlaglist(item.getPreStatusFlag()),
                        item.getCttStartDate(), item.getCttEndDate(),
                        item.getSignDate(), item.getNote()), cttroot);
                cstplInfoShowQry = new CttInfoShow();
                cstplInfoShowQry.setCttType(ESEnum.ITEMTYPE1.getCode());
                cstplInfoShowQry.setStrStatusFlagBegin(null);
                cstplInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                cstplInfoShowQry.setParentPkid(item.getPkid());
                cstplInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(cstplInfoShowQry);
                for (CttInfoShow cstplItem : cstplInfoShowTreeList) {
                    TreeNode childNodeCstpl = new DefaultTreeNode(new CttInfoShow(cstplItem.getId(), cstplItem .getName(),
                            esCommon.getCustNameByCustIdFromList(cstplItem.getSignPartA()),
                            esCommon.getCustNameByCustIdFromList(cstplItem.getSignPartB()),
                            esFlowControl.getLabelByValueInPreStatusFlaglist(cstplItem .getStatusFlag()),
                            esFlowControl.getLabelByValueInPreStatusFlaglist(cstplItem .getPreStatusFlag()),
                            cstplItem .getCttStartDate(), cstplItem .getCttEndDate(),
                            cstplItem .getSignDate(), cstplItem.getNote()), childNodeTkctt);
                    subCttInfoShowTreeList = new ArrayList<CttInfoShow>();
                    subCttInfoShowQry = new CttInfoShow();
                    subCttInfoShowQry.setCttType(ESEnum.ITEMTYPE2.getCode());
                    subCttInfoShowQry.setStrStatusFlagBegin(null);
                    subCttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                    subCttInfoShowQry.setParentPkid(cstplItem.getPkid());
                    subCttInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(subCttInfoShowQry);
                    for (CttInfoShow subItem : subCttInfoShowTreeList) {
                        if (subItem.getPkid() != null) {
                            TreeNode childNodeSubctt = new DefaultTreeNode(new CttInfoShow(subItem.getId(), subItem.getName(),
                                    esCommon.getCustNameByCustIdFromList(subItem.getSignPartA()),
                                    esCommon.getCustNameByCustIdFromList(subItem.getSignPartB()),
                                    esFlowControl.getLabelByValueInPreStatusFlaglist(subItem.getStatusFlag()),
                                    esFlowControl.getLabelByValueInPreStatusFlaglist(subItem.getPreStatusFlag()),
                                    subItem.getCttStartDate(), subItem.getCttEndDate(),
                                    subItem.getSignDate(), subItem.getNote()), childNodeCstpl);
                        }
                    }
                }
            } else {
                childNodeTkctt = new DefaultTreeNode(new CttInfoShow("-", item.getName(), "-", "-", "-", "-", "-", "-", "-", "-"), root);
            }
        }
    }

    public void addNode() {
        TreeNode childNode = new DefaultTreeNode(new CttInfoShow(cttInfoShow.getId(), cttInfoShow.getName(), esCommon.getCustNameByCustIdFromList(cttInfoShow.getSignPartA()),
                esCommon.getCustNameByCustIdFromList(cttInfoShow.getSignPartB()), cttInfoShow.getStatusFlag(),
                cttInfoShow.getPreStatusFlag(),
                cttInfoShow.getCttStartDate(), cttInfoShow.getCttEndDate(),
                cttInfoShow.getSignDate(), cttInfoShow.getNote()));
    }

    public void deleteNode() {
        selectedNode.getChildren().clear();
        selectedNode.getParent().getChildren().remove(selectedNode);
        selectedNode.setParent(null);
        selectedNode = null;
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

/*智能字段 End*/

    public CttInfoShow getTkCttInfoShowQry() {
        return tkCttInfoShowQry;
    }

    public void setTkCttInfoShowQry(CttInfoShow tkCttInfoShowQry) {
        this.tkCttInfoShowQry = tkCttInfoShowQry;
    }


    public static List<CttInfoShow> getTkCttInfoShowTreeList() {
        return tkCttInfoShowTreeList;
    }

    public static List<CttInfoShow> getCstplInfoShowTreeList() {
        return cstplInfoShowTreeList;
    }

    public CttInfoShow getCttInfoShow() {
        return cttInfoShow;
    }

    public void setCttInfoShow(CttInfoShow cttInfoShow) {
        this.cttInfoShow = cttInfoShow;
    }

    public static List<CttInfoShow> getSubCttInfoShowTreeList() {
        return subCttInfoShowTreeList;
    }


    public CttInfoShow getCstplInfoShowQry() {
        return cstplInfoShowQry;
    }

    public void setCstplInfoShowQry(CttInfoShow cstplInfoShowQry) {
        this.cstplInfoShowQry = cstplInfoShowQry;
    }

    public CttInfoShow getSubCttInfoShowQry() {
        return subCttInfoShowQry;
    }

    public void setSubCttInfoShowQry(CttInfoShow subCttInfoShowQry) {
        this.subCttInfoShowQry = subCttInfoShowQry;
    }
}
