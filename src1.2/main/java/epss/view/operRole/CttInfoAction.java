package epss.view.contract;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsCttItem;
import epss.repository.model.model_show.CttInfoShow;
import epss.service.CttInfoService;
import epss.service.CttItemService;
import epss.service.EsFlowService;
import epss.service.FlowCtrlService;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.primefaces.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: ÏÂÎç4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class CttInfoAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CttInfoAction.class);
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
    private CttInfoShow cttInfoShowQry;
    private TreeNode root;
    private TreeNode selectedNode;
    //tkctt-cstpl
    private static List<CttInfoShow> cttInfoShowTreeList;
    private static List<EsCttItem> cttItemShowTreeList;
    //cstpl-subctt
    private CttInfoShow subCttInfoShowQry;
    private static List<CttInfoShow> subCttInfoShowTreeList;
    private CttInfoShow cttInfoShow;

    @PostConstruct
    public void init() {
        initCttInfo();
        cttInfoShow = new CttInfoShow();
    }

    public void initCttInfo() {
        cttInfoShowTreeList = new ArrayList<CttInfoShow>();
        root = new DefaultTreeNode("root", null);
        cttInfoShowQry = new CttInfoShow();
        cttInfoShowQry.setCttType(ESEnum.ITEMTYPE0.getCode());
        cttInfoShowQry.setStrStatusFlagBegin(null);
        cttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
        cttInfoShowQry.setPeriodNo("NULL");
        cttInfoShowTreeList = esFlowService.selectCttByStatusFlagBegin_End(cttInfoShowQry);
        for (CttInfoShow item : cttInfoShowTreeList) {
            TreeNode childNodeTkctt = null;
            if (item.getPkid() != null) {
                childNodeTkctt = new DefaultTreeNode(new CttInfoShow(item.getId(), item.getName(),
                        esCommon.getCustNameByCustIdFromList(item.getSignPartA()),
                        esCommon.getCustNameByCustIdFromList(item.getSignPartB()),
                        esFlowControl.getLabelByValueInPreStatusFlaglist(item.getStatusFlag()),
                        esFlowControl.getLabelByValueInPreStatusFlaglist(item.getPreStatusFlag()),
                        item.getCttStartDate(), item.getCttEndDate(),
                        item.getSignDate(), item.getNote()), root);
                cttItemShowTreeList = cttItemService.getEsItemList(item.getCttType(), item.getPkid());
                for (EsCttItem cttItem : cttItemShowTreeList) {
                    TreeNode childNodeCstpl = new DefaultTreeNode(new CttInfoShow(cttItem.getPkid(), cttItem.getName(), "-", "-", "-", "-", "-", "-", "-", cttItem.getNote()), childNodeTkctt);
                    subCttInfoShowQry = new CttInfoShow();
                    subCttInfoShowQry.setCttType(ESEnum.ITEMTYPE2.getCode());
                    subCttInfoShowQry.setStrStatusFlagBegin(null);
                    subCttInfoShowQry.setStrStatusFlagEnd(ESEnumStatusFlag.STATUS_FLAG0.getCode());
                    subCttInfoShowQry.setParentPkid(item.getPkid());
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

    /*ÖÇÄÜ×Ö¶Î Start*/

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

    public CttInfoShow getCttInfoShowQry() {
        return cttInfoShowQry;
    }

    public void setCttInfoShowQry(CttInfoShow cttInfoShowQry) {
        this.cttInfoShowQry = cttInfoShowQry;
    }

/*ÖÇÄÜ×Ö¶Î End*/

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

    public static List<CttInfoShow> getCttInfoShowTreeList() {
        return cttInfoShowTreeList;
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

    public static void setSubCttInfoShowTreeList(List<CttInfoShow> subCttInfoShowTreeList) {
        CttInfoAction.subCttInfoShowTreeList = subCttInfoShowTreeList;
    }

    public CttInfoShow getSubCttInfoShowQry() {
        return subCttInfoShowQry;
    }

    public void setSubCttInfoShowQry(CttInfoShow subCttInfoShowQry) {
        this.subCttInfoShowQry = subCttInfoShowQry;
    }
}
