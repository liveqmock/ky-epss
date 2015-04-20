package epss.view.operFuncRes;

import epss.common.enums.EnumArchivedFlag;
import epss.repository.model.OperRes;
import epss.repository.model.Ptmenu;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperAppointShow;
import epss.repository.model.model_show.OperResShow;
import epss.service.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class MenuAppointOperAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(MenuAppointOperAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private OperAppointShow operAppointShowSeled;
    private List<OperAppointShow> operAppointShowList;
    private List<DeptOperShow> deptOperShowSeledList;
    private TreeNode deptOperRoot;
    private List<SelectItem> menuTypeList;
    private String strMenuTypeSeled;

    @PostConstruct
    public void init() {
        try {
            operAppointShowList = new ArrayList<>();
            deptOperShowSeledList = new ArrayList<>();
            menuTypeList = new ArrayList<>();
            strMenuTypeSeled = "0";
            menuTypeList.add(new SelectItem("", "ȫ��"));
            menuTypeList.add(new SelectItem("0", "��ѯ"));
            menuTypeList.add(new SelectItem("1", "¼��"));
            menuTypeList.add(new SelectItem("2", "����"));
            menuTypeList.add(new SelectItem("3", "ϵͳ"));
            // ��Դ-�û�-����
            initRes(strMenuTypeSeled);
            initDeptOper();
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("��ʼ��ʧ��", e);
        }
    }
    private void initRes(String strMenuTypePara){
        deptOperShowSeledList.clear();
        operAppointShowList.clear();
        Ptmenu ptmenuTemp=new Ptmenu();
        ptmenuTemp.setMenuType(strMenuTypePara);
        List<Ptmenu> ptmenuListTemp=menuService.selectListByModelOrderLabel(ptmenuTemp);
        OperRes operResTemp=new OperRes();
        operResTemp.setType("system");
        List<OperResShow> operResShowListTemp=operResService.selectOperaResRecordsByModel(operResTemp);
        for(Ptmenu ptmenuUnit:ptmenuListTemp){
            String strInputOperName="";
            for(OperResShow operResShowUnit:operResShowListTemp){
                if(ptmenuUnit.getPkid().equals(operResShowUnit.getInfoPkid())){
                    if(strInputOperName.length()==0){
                        strInputOperName =
                                ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strInputOperName = strInputOperName + "," +
                                ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }
                }
            }
            OperAppointShow operAppointShowTemp =new OperAppointShow();
            operAppointShowTemp.setResPkid(ptmenuUnit.getPkid());
            operAppointShowTemp.setResName(ptmenuUnit.getMenulabel());
            operAppointShowTemp.setInputOperName(strInputOperName);
            operAppointShowList.add(operAppointShowTemp);
        }
    }
    private void initDeptOper(){
        deptOperShowSeledList.clear();
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("������Ա��Ϣ");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveOperTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }

    private void recursiveOperTreeNode(String strParentPkidPara, TreeNode parentNode) {
        List<DeptOperShow> operResShowListTemp = deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (DeptOperShow anOperResShowListTemp : operResShowListTemp) {
            TreeNode childNodeTemp = new DefaultTreeNode(anOperResShowListTemp, parentNode);
            recursiveOperTreeNode(anOperResShowListTemp.getPkid(), childNodeTemp);
        }
    }

    public void selectAction(){
        initRes(strMenuTypeSeled);
    }
    
    private void recursiveOperTreeNodeForExpand(
            TreeNode treeNodePara,List<OperResShow> operResShowListPara) {
        if (operResShowListPara==null||operResShowListPara.size()==0){
            return;
        }
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                TreeNode treeNodeTemp = treeNodePara.getChildren().get(i);
                DeptOperShow deptOperShowTemp = (DeptOperShow) treeNodeTemp.getData();
                if (deptOperShowTemp.getPkid()!=null&&"1".equals(deptOperShowTemp.getType())){
                    for (int j = 0; j < operResShowListPara.size(); j++) {
                        if (deptOperShowTemp.getPkid().equals(operResShowListPara.get(j).getOperPkid())) {
                            deptOperShowTemp.setIsSeled(true);
                            deptOperShowSeledList.add(deptOperShowTemp);
                            while (!(treeNodeTemp.getParent()==null)){
                                if (!(treeNodeTemp.isExpanded())&&treeNodeTemp.getChildCount()>0){
                                    treeNodeTemp.setExpanded(true);
                                }
                                treeNodeTemp=treeNodeTemp.getParent();
                            }
                            operResShowListPara.remove(j);
                            break;
                        }
                    }
                }
                recursiveOperTreeNodeForExpand(treeNodeTemp, operResShowListPara);
            }
        }
    }

    public void selectRecordAction(OperAppointShow operAppointShowPara) {
        try {
            operAppointShowSeled = operAppointShowPara;
            initDeptOper();
            OperRes operResTemp=new OperRes();
            operResTemp.setInfoPkid(operAppointShowSeled.getResPkid());
            operResTemp.setType("system");
            List<OperResShow> operResShowListTemp=operResService.selectOperaResRecordsByModel(operResTemp);
            recursiveOperTreeNodeForExpand(deptOperRoot,operResShowListTemp);
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    public void selOperRecordAction(DeptOperShow deptOperShowPara){
        if (deptOperShowPara.getIsSeled()){
            deptOperShowSeledList.add(deptOperShowPara);
        }else{
            deptOperShowSeledList.remove(deptOperShowPara);
        }
    }

    /**
     * �ύά��Ȩ��
     *
     * @param
     */
    public void onClickForMngAction(String strSubmitTypePara) {
        try {
            if (strSubmitTypePara.equals("Power")) {
                OperRes operResTemp = new OperRes();
                operResTemp.setInfoPkid(operAppointShowSeled.getResPkid());
                operResService.deleteRecord(operResTemp);
                operResTemp.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
                operResTemp.setType("system");
                for (DeptOperShow deptOperShowUnit : deptOperShowSeledList) {
                    operResTemp.setOperPkid(deptOperShowUnit.getPkid());
                    operResService.insertRecord(operResTemp);
                }
                MessageUtil.addInfo("Ȩ�����ӳɹ�!");
            }
            initRes(strMenuTypeSeled);
            initDeptOper();
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("��ʼ��ʧ��", e);
        }
    }

    /*�����ֶ� Start*/

    public List<SelectItem> getMenuTypeList() {
        return menuTypeList;
    }

    public void setMenuTypeList(List<SelectItem> menuTypeList) {
        this.menuTypeList = menuTypeList;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }

    public TreeNode getDeptOperRoot() {
        return deptOperRoot;
    }

    public void setDeptOperRoot(TreeNode deptOperRoot) {
        this.deptOperRoot = deptOperRoot;
    }

    public List<DeptOperShow> getDeptOperShowSeledList() {
        return deptOperShowSeledList;
    }

    public void setDeptOperShowSeledList(List<DeptOperShow> deptOperShowSeledList) {
        this.deptOperShowSeledList = deptOperShowSeledList;
    }

    public List<OperAppointShow> getOperAppointShowList() {
        return operAppointShowList;
    }

    public void setOperAppointShowList(List<OperAppointShow> operAppointShowList) {
        this.operAppointShowList = operAppointShowList;
    }

    public String getStrMenuTypeSeled() {
        return strMenuTypeSeled;
    }

    public void setStrMenuTypeSeled(String strMenuTypeSeled) {
        this.strMenuTypeSeled = strMenuTypeSeled;
    }

    public MenuService getMenuService() {
        return menuService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }
}