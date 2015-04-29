package epss.view.operFuncRes;

import epss.repository.model.model_show.OperAppointShow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.OperRes;
import epss.repository.model.Oper;
import epss.repository.model.Ptmenu;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperResShow;
import epss.service.DeptOperService;
import epss.service.MenuService;
import epss.service.OperResService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class OperAppointMenuAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperAppointMenuAction.class);
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private OperResShow operResShowSeled;
    private List<OperResShow> operResShowList;
    private List<OperResShow> filteredMenuAppointShowList;
    private List<OperResShow> operResShowList_Res;
    private List<OperResShow> operResSel;

    @PostConstruct
    public void init() {
        try {
            operResShowList = new ArrayList<>();
            filteredMenuAppointShowList = new ArrayList<>();
            operResShowList_Res = new ArrayList<>();
            operResSel = new ArrayList<>();
            // 资源-用户-功能
            initOper();
            filteredMenuAppointShowList.addAll(operResShowList);
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    public void selectRecordAction(OperResShow operResShowPara) {
        try {
            operResShowSeled = operResShowPara;
            operResShowList_Res.clear();
            operResSel.clear();
            Ptmenu ptmenuTemp=new Ptmenu();
            List<Ptmenu> ptmenuListTemp=menuService.selectListByModelOrderLabel(ptmenuTemp);
            OperResShow operResShowTemp = new OperResShow();
            operResShowTemp.setOperPkid(operResShowPara.getOperPkid());
            List<OperResShow> operResListTemp=
                    operResService.getMenuAppointShowList(operResShowTemp);
            for (Ptmenu ptmenuUnit:ptmenuListTemp){
                OperResShow operRes =new OperResShow();
                operRes.setInfoPkid(ptmenuUnit.getPkid());
                if(ToolUtil.getStrIgnoreNull(ptmenuUnit.getTargetmachine()).equals("system")){
                    operRes.setInfoName("系统资源_"+ptmenuUnit.getMenulabel());
                }else {
                    operRes.setInfoName(ptmenuUnit.getMenulabel());
                }
                for (OperResShow operResShowUnit :operResListTemp){
                    if(ptmenuUnit.getPkid().equals(operResShowUnit.getInfoPkid())){
                        operResSel.add(operRes);
                        break;
                    }
                }
                operResShowList_Res.add(operRes);
            }

        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }
    private void initOper(){
        operResShowList.clear();
        // 确定人员列表
        DeptOperShow deptOperShowPara=new DeptOperShow();
        List<Oper> operListTemp=deptOperService.getOperListByModelShow(deptOperShowPara);
        // 人员列表对应的资源信息
        List<OperResShow> operMenuShowListOfMenuAppoint =
                operResService.getMenuAppointShowList(new OperResShow());
        for(Oper operUnit:operListTemp){
            String strResName="";
            for(OperResShow item_OperMenuPtmenu: operMenuShowListOfMenuAppoint){
                if(operUnit.getPkid().equals(item_OperMenuPtmenu.getOperPkid())){
                    if(strResName.length()==0){
                        strResName =
                                ToolUtil.getStrIgnoreNull(item_OperMenuPtmenu.getInfoName());
                    }else {
                        strResName = strResName + "," +
                                ToolUtil.getStrIgnoreNull(item_OperMenuPtmenu.getInfoName());
                    }
                }
            }
            OperResShow operResShowTemp =new OperResShow();
            operResShowTemp.setOperPkid(operUnit.getPkid());
            operResShowTemp.setOperName(operUnit.getName());
            operResShowTemp.setInfoName(strResName);
            operResShowList.add(operResShowTemp);
        }
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction() {
        try {
            OperRes operResTemp = new OperRes();
            operResTemp.setOperPkid(operResShowSeled.getOperPkid());
            operResTemp.setType("system");
            operResService.deleteRecordByOperPkid(operResTemp);
            for (OperResShow operResShowUnit : operResSel) {
                operResTemp.setInfoPkid(operResShowUnit.getInfoPkid());
                operResService.insertRecord(operResTemp);
            }
            MessageUtil.addInfo("权限添加成功!");
            initOper();
            //过滤需要和原数据同步
            int selIndex= filteredMenuAppointShowList.indexOf(operResShowSeled);
            filteredMenuAppointShowList.remove(operResShowSeled);
            for(OperResShow operResShowUnit : operResShowList){
                if(operResShowUnit.getOperPkid().equals(operResShowSeled.getOperPkid())){
                    filteredMenuAppointShowList.add(selIndex, operResShowUnit);
                }
            }
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    /*智能字段 Start*/

    public List<OperResShow> getMenuAppointShowList_Res() {
        return operResShowList_Res;
    }

    public void setMenuAppointShowList_Res(List<OperResShow> operResShowList_Res) {
        this.operResShowList_Res = operResShowList_Res;
    }

    public List<OperResShow> getFilteredMenuAppointShowList() {
        return filteredMenuAppointShowList;
    }

    public void setFilteredMenuAppointShowList(List<OperResShow> filteredMenuAppointShowList) {
        this.filteredMenuAppointShowList = filteredMenuAppointShowList;
    }

    public List<OperResShow> getMenuAppointSel() {
        return operResSel;
    }

    public void setMenuAppointSel(List<OperResShow> operResSel) {
        this.operResSel = operResSel;
    }

    public List<OperResShow> getMenuAppointShowList() {
        return operResShowList;
    }

    public void setMenuAppointShowList(List<OperResShow> operResShowList) {
        this.operResShowList = operResShowList;
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

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }
}
