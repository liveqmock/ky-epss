package epss.view.operRole;

import epss.repository.model.model_show.OperRoleSelectShow;
import epss.service.OperRoleSelectService;
import epss.view.flow.EsFlowControl;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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

    private TreeNode root;
    private TreeNode selectedNode;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        OperRoleSelectShow operRoleSelectShow=new OperRoleSelectShow();
        operRoleSelectShow.setSlename("»À‘± ⁄»®");
        operRoleSelectShow.setSeltype("1");
        TreeNode node0 = new DefaultTreeNode(operRoleSelectShow, root);
        recursiveTreeNode("0",node0);
        expandTreeNode(node0);
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
}
