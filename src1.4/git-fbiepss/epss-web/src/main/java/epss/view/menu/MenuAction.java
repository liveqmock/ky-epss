package epss.view.menu;

import epss.repository.model.Ptmenu;
import epss.service.MenuService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.MessageUtil;
import skyline.util.StyleModel;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class MenuAction {
    private static final Logger logger = LoggerFactory.getLogger(MenuAction.class);
    @ManagedProperty(value = "#{menuService}")
    private MenuService menuService;

    private Ptmenu ptmenuQry;
    private Ptmenu ptmenuAdd;
    private Ptmenu ptmenuUpd;
    private Ptmenu ptmenuDel;
    private Ptmenu ptmenuSel;
    private List<Ptmenu> ptmenuList;

    private String strSubmitType;
    private String rowSelectedFlag;
    /*控制维护画面层级部分的显示*/
    private StyleModel styleModel;
    private List<SelectItem> isLeafList;
    private List<SelectItem> openWindowList;
    private List<SelectItem> targetMachineList;
    private List<SelectItem> menuTypeList;

    @PostConstruct
    public void init() {
        isLeafList = new ArrayList<>();
        isLeafList.add(new SelectItem("0", "不是"));
        isLeafList.add(new SelectItem("1", "是"));
        openWindowList = new ArrayList<>();
        openWindowList.add(new SelectItem("0", "不弹出"));
        openWindowList.add(new SelectItem("1", "弹出"));
        targetMachineList = new ArrayList<>();
        targetMachineList.add(new SelectItem("default", "业务资源"));
        targetMachineList.add(new SelectItem("system", "系统资源"));
        menuTypeList = new ArrayList<>();
        menuTypeList.add(new SelectItem("0", "查询"));
        menuTypeList.add(new SelectItem("1", "录入"));
        menuTypeList.add(new SelectItem("2", "流程"));
        menuTypeList.add(new SelectItem("3", "系统"));
        resetAction();
    }

    public void resetAction(){
        this.ptmenuList = new ArrayList<Ptmenu>();
        ptmenuQry=new Ptmenu() ;
        ptmenuAdd=new Ptmenu() ;
        ptmenuUpd=new Ptmenu() ;
        ptmenuDel=new Ptmenu() ;
        ptmenuSel=new Ptmenu() ;
        styleModel=new StyleModel();
        styleModel.setDisabled_Flag("false");
        strSubmitType="Add";
        rowSelectedFlag = "false";
    }

    public void resetActionForAdd(){
        ptmenuAdd=new Ptmenu();
        strSubmitType="Add";
    }

    public void onQueryAction(String strQryMsgOutPara) {
        try {
            this.ptmenuList = menuService.selectListByModelOrderLevelidx(ptmenuQry);
            if(strQryMsgOutPara.equals("true")) {
                if (ptmenuList.isEmpty()) {
                    MessageUtil.addWarn("没有查询到数据。");
                }
            }
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public void setMaxIdxPlusOne(){
        Integer intMaxIdx = menuService.getStrMaxIdx()+1;
        if(strSubmitType.equals("Add")) {
            getPtmenuAdd().setLevelidx(intMaxIdx);
        }else if(strSubmitType.equals("Upd")) {
            getPtmenuUpd().setLevelidx(intMaxIdx);
        }
    }

    /**
     * 必须输入项目检查
     */
    public boolean unableNullCheck(Ptmenu ptmenu){
        if (StringUtils.isEmpty(ptmenu.getMenuid())){
            MessageUtil.addError("请输入菜单编号！");
            return false;
        }
        else if (StringUtils.isEmpty(ptmenu.getMenulabel())) {
            MessageUtil.addError("请输入菜单名称！");
            return false;
        }
        return true ;
    }

    public void submitThisRecordAction(){
        if(strSubmitType.equals("Add")){
            if(menuService.isExistInDb(ptmenuAdd)) {
                MessageUtil.addError("该记录已存在，请重新录入！");
            }else {
                if(ToolUtil.getStrIgnoreNull(ptmenuAdd.getParentmenuid()).equals("")) {
                    ptmenuAdd.setParentmenuid("0");
                }
                addRecordAction(ptmenuAdd);
                resetActionForAdd();
            }
        }
        else if(strSubmitType.equals("Upd")){
            if(ToolUtil.getStrIgnoreNull(ptmenuUpd.getParentmenuid()).equals("")) {
                ptmenuUpd.setParentmenuid("0");
                ptmenuUpd.setMenulevel(1);
            }else{
                ptmenuUpd.setMenulevel(2);
            }
            updRecordAction(ptmenuUpd);
        }else if(strSubmitType.equals("Del")){
            deleteRecordAction(ptmenuDel);
        }
        onQueryAction("false");
    }
    public void addRecordAction(Ptmenu ptmenuPara){
        try {
            if(unableNullCheck(ptmenuPara)){
                menuService.insertRecord(ptmenuPara) ;
                MessageUtil.addInfo("新增数据完成。");

            }
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void updRecordAction(Ptmenu ptmenuPara){
        try {
            menuService.updateRecord(ptmenuPara) ;
            MessageUtil.addInfo("更新数据完成。");
        } catch (Exception e) {
            logger.error("更新数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    public void deleteRecordAction(Ptmenu esInitCustPara){
        try {
            int deleteRecordNum= menuService.deleteRecord(esInitCustPara) ;
            if (deleteRecordNum<=0){
                MessageUtil.addInfo("该记录已删除。");
                return;
            }
            MessageUtil.addInfo("删除数据完成。");
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    public void selectRecordAction(String strSubmitTypePara,Ptmenu ptmenuSelectedPara){
        try {
            if (strSubmitTypePara.equals("Sel")){
                ptmenuSel=(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }
            strSubmitType=strSubmitTypePara;
            if(strSubmitTypePara.equals("Add")){
            }else if(strSubmitTypePara.equals("Upd")){
                ptmenuUpd =(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }else if(strSubmitTypePara.equals("Del")){
                ptmenuDel =(Ptmenu) BeanUtils.cloneBean(ptmenuSelectedPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public MenuService getMenuService() {
        return menuService;
    }

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    public Ptmenu getPtmenuQry() {
        return ptmenuQry;
    }

    public void setPtmenuQry(Ptmenu ptmenuQry) {
        this.ptmenuQry = ptmenuQry;
    }

    public Ptmenu getPtmenuAdd() {
        return ptmenuAdd;
    }

    public void setPtmenuAdd(Ptmenu ptmenuAdd) {
        this.ptmenuAdd = ptmenuAdd;
    }

    public Ptmenu getPtmenuUpd() {
        return ptmenuUpd;
    }

    public void setPtmenuUpd(Ptmenu ptmenuUpd) {
        this.ptmenuUpd = ptmenuUpd;
    }

    public Ptmenu getPtmenuDel() {
        return ptmenuDel;
    }

    public void setPtmenuDel(Ptmenu ptmenuDel) {
        this.ptmenuDel = ptmenuDel;
    }

    public Ptmenu getPtmenuSel() {
        return ptmenuSel;
    }

    public void setPtmenuSel(Ptmenu ptmenuSel) {
        this.ptmenuSel = ptmenuSel;
    }

    public List<Ptmenu> getPtmenuList() {
        return ptmenuList;
    }

    public void setPtmenuList(List<Ptmenu> ptmenuList) {
        this.ptmenuList = ptmenuList;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }

    public String getRowSelectedFlag() {
        return rowSelectedFlag;
    }

    public void setRowSelectedFlag(String rowSelectedFlag) {
        this.rowSelectedFlag = rowSelectedFlag;
    }

    public StyleModel getStyleModel() {
        return styleModel;
    }

    public void setStyleModel(StyleModel styleModel) {
        this.styleModel = styleModel;
    }

    public List<SelectItem> getIsLeafList() {
        return isLeafList;
    }

    public void setIsLeafList(List<SelectItem> isLeafList) {
        this.isLeafList = isLeafList;
    }

    public List<SelectItem> getOpenWindowList() {
        return openWindowList;
    }

    public void setOpenWindowList(List<SelectItem> openWindowList) {
        this.openWindowList = openWindowList;
    }

    public List<SelectItem> getTargetMachineList() {
        return targetMachineList;
    }

    public void setTargetMachineList(List<SelectItem> targetMachineList) {
        this.targetMachineList = targetMachineList;
    }

    public List<SelectItem> getMenuTypeList() {
        return menuTypeList;
    }

    public void setMenuTypeList(List<SelectItem> menuTypeList) {
        this.menuTypeList = menuTypeList;
    }
}
