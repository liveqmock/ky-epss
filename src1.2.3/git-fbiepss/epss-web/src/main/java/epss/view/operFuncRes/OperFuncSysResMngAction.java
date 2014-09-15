package epss.view.operFuncRes;

import epss.common.enums.EnumArchivedFlag;
import epss.repository.model.OperRes;
import epss.repository.model.Ptmenu;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperFuncResShow;
import epss.repository.model.model_show.OperResShow;
import epss.service.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class OperFuncSysResMngAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncSysResMngAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private OperFuncResShow operFuncResShowSeled;
    private List<OperFuncResShow> operFuncResShowList;
    private List<DeptOperShow> deptOperShowSeledList;
    private TreeNode deptOperRoot;
    private List<SelectItem> targetMachineList;
    private String strResTypeSeled;

    @PostConstruct
    public void init() {
        operFuncResShowList = new ArrayList<>();
        deptOperShowSeledList = new ArrayList<>();
        targetMachineList = new ArrayList<>();
        strResTypeSeled="default";
        targetMachineList.add(new SelectItem("default", "业务资源"));
        targetMachineList.add(new SelectItem("system", "系统资源"));
        // 资源-用户-功能
        initRes(strResTypeSeled);
        initDeptOper();
    }
    private void initRes(String strTargetmachinePara){
        deptOperShowSeledList.clear();
        operFuncResShowList.clear();
        Ptmenu ptmenuTemp=new Ptmenu();
        ptmenuTemp.setTargetmachine(strTargetmachinePara);
        List<Ptmenu> ptmenuListTemp=menuService.selectListByModel(ptmenuTemp);
        OperRes operResTemp=new OperRes();
        operResTemp.setType("system");
        List<OperResShow> operResShowListTemp=operResService.selectOperaResRecordsByModel(operResTemp);
        for(Ptmenu ptmenuUnit:ptmenuListTemp){
            String strInputOperName="";
            for(OperResShow operResShowUnit:operResShowListTemp){
                if(ptmenuUnit.getPkid().equals(operResShowUnit.getInfoPkid())){
                    if(strInputOperName.length()==0){
                        strInputOperName = operResShowUnit.getOperName();
                    }else {
                        strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                    }
                }
            }
            OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
            operFuncResShowTemp.setResPkid(ptmenuUnit.getPkid());
            operFuncResShowTemp.setResName(ptmenuUnit.getMenulabel());
            operFuncResShowTemp.setInputOperName(strInputOperName);
            operFuncResShowList.add(operFuncResShowTemp);
        }
    }
    private void initDeptOper(){
        deptOperShowSeledList.clear();
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveOperTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }
    private void recursiveOperTreeNode(String strParentPkidPara,TreeNode parentNode){
        List<DeptOperShow> operResShowListTemp= deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getId(), childNode);
        }
    }

    public void selectAction(){
        initRes(strResTypeSeled);
    }

    public void selectRecordAction(OperFuncResShow operFuncResShowPara) {
        try {
            operFuncResShowSeled=operFuncResShowPara;
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
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction(String strSubmitTypePara) {
        try {
            if (strSubmitTypePara.equals("Power")) {
                OperRes operResTemp = new OperRes();
                operResTemp.setInfoPkid(operFuncResShowSeled.getResPkid());
                operResService.deleteRecord(operResTemp);
                for (DeptOperShow deptOperShowUnit : deptOperShowSeledList) {
                    operResTemp = new OperRes();
                    operResTemp.setOperPkid(deptOperShowUnit.getId());
                    operResTemp.setInfoPkid(operFuncResShowSeled.getResPkid());
                    operResTemp.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
                    operResTemp.setType("system");
                    operResService.insertRecord(operResTemp);
                }
                MessageUtil.addInfo("权限添加成功!");
            }
            initRes(strResTypeSeled);
            initDeptOper();
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    /*智能字段 Start*/

    public List<SelectItem> getTargetMachineList() {
        return targetMachineList;
    }

    public void setTargetMachineList(List<SelectItem> targetMachineList) {
        this.targetMachineList = targetMachineList;
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

    public List<OperFuncResShow> getOperFuncResShowList() {
        return operFuncResShowList;
    }

    public void setOperFuncResShowList(List<OperFuncResShow> operFuncResShowList) {
        this.operFuncResShowList = operFuncResShowList;
    }

    public String getStrResTypeSeled() {
        return strResTypeSeled;
    }

    public void setStrResTypeSeled(String strResTypeSeled) {
        this.strResTypeSeled = strResTypeSeled;
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
