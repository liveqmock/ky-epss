package epss.view.operFuncRes;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumArchivedFlag;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumTaskDoneFlag;
import epss.repository.model.CttInfo;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import skyline.util.JxlsManager;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.*;
import epss.service.*;
import jxl.write.WriteException;
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
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@ManagedBean
@ViewScoped
public class OperFuncBusiResMngAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncBusiResMngAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
    @ManagedProperty(value = "#{deptOperService}")
    private DeptOperService deptOperService;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;

    private List<SelectItem> taskFunctionList;
    private List<DeptOperShow> deptOperShowSeledList;

    private List<OperFuncResShow> operFuncResShowFowExcelList;
    private Map beansMap;

    private CttInfoShow cttInfoShowSel;
    private CttInfoShow cttInfoShowAdd;
    private CttInfoShow cttInfoShowUpd;
    private CttInfoShow cttInfoShowDel;

    private List<SelectItem> esInitCttList;
    private List<CttInfoShow> cttInfoShowList;
    //ctt tree
    private TreeNode resRoot;
    private TreeNode deptOperRoot;
    private TreeNode currentSelectedNode;
    private TreeNode currentSelectedResNode;
    private TreeNode lastSelectedResNode;

    private String strBtnRender;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cttInfoShowAdd=new CttInfoShow();
        cttInfoShowUpd=new CttInfoShow();
        cttInfoShowDel=new CttInfoShow();
        esInitCttList = new ArrayList<>();
        cttInfoShowList = new ArrayList<>();
        deptOperShowSeledList = new ArrayList<>();
        operFuncResShowFowExcelList= new ArrayList<>();
        strBtnRender="false";
        // 资源-用户-功能
        initRes();
        initFuncListByResType(resRoot);
        initDeptOper();
        beansMap.put("operFuncResShowFowExcelList", operFuncResShowFowExcelList);
    }
    private void initRes(){
        OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
        operFuncResShowTemp.setResPkid("ROOT");
        operFuncResShowTemp.setResName("资源信息");
        resRoot = new DefaultTreeNode("ROOT", null);
        TreeNode node0 = new DefaultTreeNode(operFuncResShowTemp,resRoot);
        try {
            recursiveResTreeNode("ROOT", node0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        node0.setExpanded(true);
    }
    private void initDeptOper(){
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveOperTreeNode("ROOT", node0);
        node0.setExpanded(true);
    }
    private void recursiveResTreeNode(String parentPkidPara,TreeNode parentNode)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<CttInfoShow> cttInfoShowList=cttInfoService.selectRecordsFromCtt(parentPkidPara);
        for (int i=0;i<cttInfoShowList.size();i++){
            CttInfoShow cttInfoShowTemp =cttInfoShowList.get(i);
            // 总成分
            OperResShow operResShowTemp=new OperResShow();
            operResShowTemp.setInfoType(cttInfoShowTemp.getCttType());
            operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
            List<OperResShow> operResShowListTemp =
                    operResService.selectOperaResRecordsByModelShow(operResShowTemp);
            String strInputOperName="";
            String strCheckOperName="";
            String strDoubleCheckOperName="";
            String strApproveOperName="";
            String strAccountOperName="";
            String strPlaceOnFileOperName="";
            for(OperResShow operResShowUnit:operResShowListTemp){
                if("0".equals(operResShowUnit.getFlowStatus())){
                    if(strInputOperName.length()==0){
                        strInputOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("1".equals(operResShowUnit.getFlowStatus())){
                    if(strCheckOperName.length()==0){
                        strCheckOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("2".equals(operResShowUnit.getFlowStatus())){
                    if(strDoubleCheckOperName.length()==0){
                        strDoubleCheckOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("3".equals(operResShowUnit.getFlowStatus())){
                    if(strApproveOperName.length()==0){
                        strApproveOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("4".equals(operResShowUnit.getFlowStatus())){
                    if(strAccountOperName.length()==0){
                        strAccountOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("5".equals(operResShowUnit.getFlowStatus())){
                    if(strPlaceOnFileOperName.length()==0){
                        strPlaceOnFileOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                    }
                }
            }
            OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
            operFuncResShowTemp.setResType(cttInfoShowTemp.getCttType());
            operFuncResShowTemp.setResPkid(cttInfoShowTemp.getPkid());
            String strResTypeName="";
            if(EnumResType.RES_TYPE0.getCode().equals(cttInfoShowTemp.getCttType())){
                strResTypeName="总包合同_";
            }else if(EnumResType.RES_TYPE1.getCode().equals(cttInfoShowTemp.getCttType())){
                strResTypeName="成本计划_";
            }else if(EnumResType.RES_TYPE2.getCode().equals(cttInfoShowTemp.getCttType())){
                strResTypeName="分包合同_";
            }
            operFuncResShowTemp.setResName(strResTypeName+cttInfoShowTemp.getName());
            operFuncResShowTemp.setInputOperName(strInputOperName);
            operFuncResShowTemp.setCheckOperName(strCheckOperName);
            operFuncResShowTemp.setDoubleCheckOperName(strDoubleCheckOperName);
            operFuncResShowTemp.setApproveOperName(strApproveOperName);
            operFuncResShowTemp.setAccountOperName(strAccountOperName);
            operFuncResShowTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
            // 合同
            TreeNode childNodeTemp = new DefaultTreeNode(operFuncResShowTemp, parentNode);
            OperFuncResShow operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowTemp);
            operFuncResShowForExcelTemp.setResName(
            ToolUtil.padLeftSpace_DoLevel(Integer.parseInt(operFuncResShowForExcelTemp.getResType()),operFuncResShowForExcelTemp.getResName()));
            operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
            // 结算信息
            if(cttInfoShowTemp.getCttType().equals(EnumResType.RES_TYPE0.getCode())) {
                // 统计
                operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(EnumResType.RES_TYPE6.getCode());
                operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
                List<OperResShow> operResShowForStlListTemp =
                        operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                strInputOperName = "";
                strCheckOperName = "";
                strDoubleCheckOperName = "";
                strApproveOperName = "";
                strAccountOperName = "";
                strPlaceOnFileOperName = "";
                for (OperResShow operResShowUnit : operResShowForStlListTemp) {
                    if (operResShowUnit.getFlowStatus().equals("0")) {
                        if (strInputOperName.length() == 0) {
                            strInputOperName = operResShowUnit.getOperName();
                        } else {
                            strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("1")) {
                        if (strCheckOperName.length() == 0) {
                            strCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("2")) {
                        if (strDoubleCheckOperName.length() == 0) {
                            strDoubleCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("3")) {
                        if (strApproveOperName.length() == 0) {
                            strApproveOperName = operResShowUnit.getOperName();
                        } else {
                            strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("4")) {
                        if (strAccountOperName.length() == 0) {
                            strAccountOperName = operResShowUnit.getOperName();
                        } else {
                            strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("5")) {
                        if (strInputOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                OperFuncResShow operFuncResShowForStlTemp = new OperFuncResShow();
                operFuncResShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operFuncResShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                /*operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__统计");*/
                operFuncResShowForStlTemp.setResName("总包进度_统计");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                // 安装树节点
                new DefaultTreeNode(operFuncResShowForStlTemp, childNodeTemp);
                operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);

                // 计量
                operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(EnumResType.RES_TYPE7.getCode());
                operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp =
                        operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                strInputOperName = "";
                strCheckOperName = "";
                strDoubleCheckOperName = "";
                strApproveOperName = "";
                strAccountOperName = "";
                strPlaceOnFileOperName = "";
                for (OperResShow operResShowUnit : operResShowForStlListTemp) {
                    if (operResShowUnit.getFlowStatus().equals("0")) {
                        if (strInputOperName.length() == 0) {
                            strInputOperName = operResShowUnit.getOperName();
                        } else {
                            strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("1")) {
                        if (strCheckOperName.length() == 0) {
                            strCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("2")) {
                        if (strDoubleCheckOperName.length() == 0) {
                            strDoubleCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("3")) {
                        if (strApproveOperName.length() == 0) {
                            strApproveOperName = operResShowUnit.getOperName();
                        } else {
                            strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("4")) {
                        if (strAccountOperName.length() == 0) {
                            strAccountOperName = operResShowUnit.getOperName();
                        } else {
                            strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("5")) {
                        if (strInputOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operFuncResShowForStlTemp = new OperFuncResShow();
                operFuncResShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operFuncResShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operFuncResShowForStlTemp.setResName("总包进度_计量");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                // 安装树节点
                new DefaultTreeNode(operFuncResShowForStlTemp, childNodeTemp);
                operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
            }else if(cttInfoShowTemp.getCttType().equals(EnumResType.RES_TYPE2.getCode())) {
                // 工程量结算
                operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(EnumResType.RES_TYPE3.getCode());
                operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
                List<OperResShow> operResShowForStlListTemp =
                        operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                strInputOperName = "";
                strCheckOperName = "";
                strDoubleCheckOperName = "";
                strApproveOperName = "";
                strAccountOperName = "";
                strPlaceOnFileOperName = "";
                for (OperResShow operResShowUnit : operResShowForStlListTemp) {
                    if (operResShowUnit.getFlowStatus().equals("0")) {
                        if (strInputOperName.length() == 0) {
                            strInputOperName = operResShowUnit.getOperName();
                        } else {
                            strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("1")) {
                        if (strCheckOperName.length() == 0) {
                            strCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("2")) {
                        if (strDoubleCheckOperName.length() == 0) {
                            strDoubleCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("3")) {
                        if (strApproveOperName.length() == 0) {
                            strApproveOperName = operResShowUnit.getOperName();
                        } else {
                            strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("4")) {
                        if (strAccountOperName.length() == 0) {
                            strAccountOperName = operResShowUnit.getOperName();
                        } else {
                            strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("5")) {
                        if (strInputOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                OperFuncResShow operFuncResShowForStlTemp = new OperFuncResShow();
                operFuncResShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operFuncResShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operFuncResShowForStlTemp.setResName("分包进度_工程量结算");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                // 安装树节点
                new DefaultTreeNode(operFuncResShowForStlTemp, childNodeTemp);
                operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);

                // 材料消耗量结算
                operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(EnumResType.RES_TYPE4.getCode());
                operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp =
                        operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                strInputOperName = "";
                strCheckOperName = "";
                strDoubleCheckOperName = "";
                strApproveOperName = "";
                strAccountOperName = "";
                strPlaceOnFileOperName = "";
                for (OperResShow operResShowUnit : operResShowForStlListTemp) {
                    if (operResShowUnit.getFlowStatus().equals("0")) {
                        if (strInputOperName.length() == 0) {
                            strInputOperName = operResShowUnit.getOperName();
                        } else {
                            strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("1")) {
                        if (strCheckOperName.length() == 0) {
                            strCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("2")) {
                        if (strDoubleCheckOperName.length() == 0) {
                            strDoubleCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("3")) {
                        if (strApproveOperName.length() == 0) {
                            strApproveOperName = operResShowUnit.getOperName();
                        } else {
                            strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("4")) {
                        if (strAccountOperName.length() == 0) {
                            strAccountOperName = operResShowUnit.getOperName();
                        } else {
                            strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("5")) {
                        if (strInputOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operFuncResShowForStlTemp = new OperFuncResShow();
                operFuncResShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operFuncResShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operFuncResShowForStlTemp.setResName("分包进度_材料消耗量结算");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                // 安装树节点
                new DefaultTreeNode(operFuncResShowForStlTemp, childNodeTemp);
                operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);

                // 费用结算
                operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(EnumResType.RES_TYPE8.getCode());
                operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp =
                        operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                strInputOperName = "";
                strCheckOperName = "";
                strDoubleCheckOperName = "";
                strApproveOperName = "";
                strAccountOperName = "";
                strPlaceOnFileOperName = "";
                for (OperResShow operResShowUnit : operResShowForStlListTemp) {
                    if (operResShowUnit.getFlowStatus().equals("0")) {
                        if (strInputOperName.length() == 0) {
                            strInputOperName = operResShowUnit.getOperName();
                        } else {
                            strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("1")) {
                        if (strCheckOperName.length() == 0) {
                            strCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("2")) {
                        if (strDoubleCheckOperName.length() == 0) {
                            strDoubleCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("3")) {
                        if (strApproveOperName.length() == 0) {
                            strApproveOperName = operResShowUnit.getOperName();
                        } else {
                            strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("4")) {
                        if (strAccountOperName.length() == 0) {
                            strAccountOperName = operResShowUnit.getOperName();
                        } else {
                            strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("5")) {
                        if (strInputOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operFuncResShowForStlTemp = new OperFuncResShow();
                operFuncResShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operFuncResShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operFuncResShowForStlTemp.setResName("分包进度_费用结算");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                // 安装树节点
                new DefaultTreeNode(operFuncResShowForStlTemp, childNodeTemp);
                operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);

                // 结算单
                operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(EnumResType.RES_TYPE5.getCode());
                operResShowTemp.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp =
                        operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                strInputOperName = "";
                strCheckOperName = "";
                strDoubleCheckOperName = "";
                strApproveOperName = "";
                strAccountOperName = "";
                strPlaceOnFileOperName = "";
                for (OperResShow operResShowUnit : operResShowForStlListTemp) {
                    if (operResShowUnit.getFlowStatus().equals("0")) {
                        if (strInputOperName.length() == 0) {
                            strInputOperName = operResShowUnit.getOperName();
                        } else {
                            strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("1")) {
                        if (strCheckOperName.length() == 0) {
                            strCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("2")) {
                        if (strDoubleCheckOperName.length() == 0) {
                            strDoubleCheckOperName = operResShowUnit.getOperName();
                        } else {
                            strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("3")) {
                        if (strApproveOperName.length() == 0) {
                            strApproveOperName = operResShowUnit.getOperName();
                        } else {
                            strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("4")) {
                        if (strAccountOperName.length() == 0) {
                            strAccountOperName = operResShowUnit.getOperName();
                        } else {
                            strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                        }
                    } else if (operResShowUnit.getFlowStatus().equals("5")) {
                        if (strInputOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operFuncResShowForStlTemp = new OperFuncResShow();
                operFuncResShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operFuncResShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operFuncResShowForStlTemp.setResName("分包进度＿结算单");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                // 安装树节点
                new DefaultTreeNode(operFuncResShowForStlTemp, childNodeTemp);

                operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
            }
            if (currentSelectedNode!=null){
                OperFuncResShow operFuncResShow1= (OperFuncResShow) currentSelectedNode.getData();
                OperFuncResShow operFuncResShow2= (OperFuncResShow) childNodeTemp.getData();
                if ("ROOT".equals(operFuncResShow1.getResPkid())){
                    currentSelectedNode.setExpanded(true);
                }else {
                    if (operFuncResShow1.getResType().equals(operFuncResShow2.getResType())
                            &&operFuncResShow1.getResPkid().equals(operFuncResShow2.getResPkid())){
                        TreeNode treeNodeTemp=childNodeTemp;
                        while (!(treeNodeTemp.getParent()==null)){
                            treeNodeTemp.setExpanded(true);
                            treeNodeTemp=treeNodeTemp.getParent();
                        }
                    }
                }
            }
            recursiveResTreeNode(operFuncResShowTemp.getResPkid(),childNodeTemp);
        }
    }
    private void recursiveOperTreeNode(String strParentPkidPara,TreeNode parentNode){
        List<DeptOperShow> operResShowListTemp=
                deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getPkid(), childNode);
        }
    }

    public void recursiveResTreeNode(TreeNode treeNodePara){
        treeNodePara.setExpanded(false);
        if (treeNodePara.getChildCount()!=0){
            for (int i=0;i<treeNodePara.getChildCount();i++){
                recursiveResTreeNode(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void initFuncListByResType(TreeNode treeNodePara) {
        if (!treeNodePara.getData().toString().equals("ROOT")){
            OperFuncResShow operFuncResShowTemp = ((OperFuncResShow) treeNodePara.getData());
            if (EnumResType.RES_TYPE3.getCode().equals(operFuncResShowTemp.getResType())
                    || EnumResType.RES_TYPE4.getCode().equals(operFuncResShowTemp.getResType())) {
                operFuncResShowTemp.setIsMng(EnumFlowStatus.FLOW_STATUS0.getCode());
                operFuncResShowTemp.setIsCheck(EnumFlowStatus.FLOW_STATUS1.getCode());
                operFuncResShowTemp.setIsDoubleCheck(EnumFlowStatus.FLOW_STATUS2.getCode());
                operFuncResShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            } else if (EnumResType.RES_TYPE5.getCode().equals(operFuncResShowTemp.getResType())) {
                operFuncResShowTemp.setIsApprove(EnumFlowStatus.FLOW_STATUS3.getCode());
                operFuncResShowTemp.setIsAccount(EnumFlowStatus.FLOW_STATUS4.getCode());
                operFuncResShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            } else {
                operFuncResShowTemp.setIsMng(EnumFlowStatus.FLOW_STATUS0.getCode());
                operFuncResShowTemp.setIsCheck(EnumFlowStatus.FLOW_STATUS1.getCode());
                operFuncResShowTemp.setIsDoubleCheck(EnumFlowStatus.FLOW_STATUS2.getCode());
                operFuncResShowTemp.setIsApprove(EnumFlowStatus.FLOW_STATUS3.getCode());
                operFuncResShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            }
        }
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                initFuncListByResType(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        recursiveResTreeNode(event.getTreeNode());
    }

    public void findSelectedNode(
            OperFuncResShow operFuncResShowPara,
            TreeNode treeNodePara,
            String strSubmitTypePara) {
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                TreeNode treeNodeTemp = treeNodePara.getChildren().get(i);
                if (operFuncResShowPara == treeNodeTemp.getData()) {
                    if ("Add".equals(strSubmitTypePara)){
                        currentSelectedNode = treeNodeTemp;
                    }else if ("Upd".equals(strSubmitTypePara)||"Del".equals(strSubmitTypePara)||"Sel".equals(strSubmitTypePara)){
                        currentSelectedNode=treeNodeTemp.getParent();
                    }
                    return;
                }
                findSelectedNode(operFuncResShowPara, treeNodeTemp,strSubmitTypePara);
            }
        }
    }

    public void selectRecordAction(String strSubmitTypePara,OperFuncResShow operFuncResShowPara) {
        try {
            findSelectedNode(operFuncResShowPara,resRoot,strSubmitTypePara);
            if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = new CttInfoShow();
                if(operFuncResShowPara.getResPkid().equals("ROOT")) {
                    cttInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
                    cttInfoShowAdd.setParentPkid("ROOT");
                }else if(operFuncResShowPara.getResType().equals(EnumResType.RES_TYPE0.getCode())) {
                    cttInfoShowAdd.setCttType(EnumResType.RES_TYPE1.getCode());
                    cttInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }else if(operFuncResShowPara.getResType().equals(EnumResType.RES_TYPE1.getCode())) {
                    cttInfoShowAdd.setCttType(EnumResType.RES_TYPE2.getCode());
                    cttInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }
                cttInfoShowAdd.setId(setMaxNoPlusOne(cttInfoShowAdd.getCttType()));
            } else if (strSubmitTypePara.equals("Upd")){
                cttInfoShowUpd = fromResModelShowToCttInfoShow(operFuncResShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                cttInfoShowDel = fromResModelShowToCttInfoShow(operFuncResShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void selectRecordAction(String strSubmitTypePara, OperFuncResShow operFuncResShowPara, String strResFlowStatus) {
        try {
            findSelectedNode(operFuncResShowPara, resRoot, strSubmitTypePara);
            if (strSubmitTypePara.equals("Sel")) {
                cttInfoShowSel = fromResModelShowToCttInfoShow(operFuncResShowPara);
                cttInfoShowSel.setFlowStatus(strResFlowStatus);
                initDeptOper();
                deptOperShowSeledList.clear();
                OperResShow operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(operFuncResShowPara.getResType());
                operResShowTemp.setInfoPkid(operFuncResShowPara.getResPkid());
                operResShowTemp.setFlowStatus(strResFlowStatus);
                List<OperResShow> operResShowListTemp = operResService.selectOperaResRecordsByModelShow(operResShowTemp);
                if (operResShowListTemp.size() > 0) {
                    recursiveOperTreeNodeForFuncChange(deptOperRoot, operResShowListTemp);
                }
            }
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

    public String setMaxNoPlusOne(String strResType) {
        Integer intTemp;
        String strType = null;
        if (EnumResType.RES_TYPE0.getCode().equals(strResType)){
            strType="TKCTT";
        }else if (EnumResType.RES_TYPE1.getCode().equals(strResType)){
            strType="CSTPL";
        }else if (EnumResType.RES_TYPE2.getCode().equals(strResType)){
            strType="SUBCTT";
        }
        String strMaxId = cttInfoService.getStrMaxCttId(strResType);
        if (StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxId))) {
            strMaxId = strType + ToolUtil.getStrToday() + "001";
        } else {
            if (strMaxId.length() > 3) {
                String strTemp = strMaxId.substring(strMaxId.length() - 3).replaceFirst("^0+", "");
                if (ToolUtil.strIsDigit(strTemp)) {
                    intTemp = Integer.parseInt(strTemp);
                    intTemp = intTemp + 1;
                    strMaxId = strMaxId.substring(0, strMaxId.length() - 3) + StringUtils.leftPad(intTemp.toString(), 3, "0");
                } else {
                    strMaxId += "001";
                }
            }
        }
        return strMaxId;
    }

    /**
     * 提交维护权限
     *
     * @param
     */
    public void onClickForMngAction(String strSubmitTypePara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                if (!submitPreCheck(cttInfoShowAdd)) {
                    MessageUtil.addError("请输入名称！");
                    return;
                }
                CttInfoShow cttInfoShowTemp=new CttInfoShow();
                cttInfoShowTemp.setCttType(cttInfoShowAdd.getCttType());
                cttInfoShowTemp.setName(cttInfoShowAdd.getName());
                if (cttInfoService.getListByModelShow(cttInfoShowTemp).size()>0) {
                    MessageUtil.addError("该记录已存在，请重新录入！");
                    return;
                } else {
                    cttInfoService.insertRecord(cttInfoShowAdd);
                    MessageUtil.addInfo("新增数据完成。");
					String strCttTypeTemp=cttInfoShowAdd.getCttType();
					String strParentPkidTemp=cttInfoShowAdd.getParentPkid();
                    cttInfoShowAdd = new CttInfoShow();
					cttInfoShowAdd.setCttType(strCttTypeTemp);
                    cttInfoShowAdd.setParentPkid(strParentPkidTemp);
                    cttInfoShowAdd.setId(setMaxNoPlusOne(cttInfoShowAdd.getCttType()));
                }
            } else if (strSubmitTypePara.equals("Upd")) {
                if (!submitPreCheck(cttInfoShowUpd)) {
                    MessageUtil.addError("请输入名称！");
                    return;
                }
                CttInfo cttInfoTemp=cttInfoService.getCttInfoByPkId(cttInfoShowUpd.getPkid());
                cttInfoTemp.setName(cttInfoShowUpd.getName());
                cttInfoService.updateRecord(cttInfoTemp);
                MessageUtil.addInfo("更新数据完成。");
            } else if (strSubmitTypePara.equals("Del")) {
                if (!submitPreCheck(cttInfoShowDel)) {
                    MessageUtil.addError("该记录已被删除！");
                    return;
                }
                MessageUtil.addInfo(operResService.deleteResRecord(cttInfoShowDel));
            } else if (strSubmitTypePara.equals("Power")) {
                OperRes operResTemp = new OperRes();
                operResTemp.setInfoType(cttInfoShowSel.getCttType());
                operResTemp.setInfoPkid(cttInfoShowSel.getPkid());
                operResTemp.setFlowStatus(cttInfoShowSel.getFlowStatus());
                operResService.deleteRecord(operResTemp);
                for (DeptOperShow deptOperShowUnit:deptOperShowSeledList) {
                    operResTemp = new OperRes();
                    operResTemp.setOperPkid(deptOperShowUnit.getPkid());
                    operResTemp.setInfoType(cttInfoShowSel.getCttType());
                    operResTemp.setInfoPkid(cttInfoShowSel.getPkid());
                    operResTemp.setFlowStatus(cttInfoShowSel.getFlowStatus());
                    operResTemp.setArchivedFlag(EnumArchivedFlag.ARCHIVED_FLAG0.getCode());
                    operResTemp.setType("business");
                    operResTemp.setTaskdoneFlag(EnumTaskDoneFlag.TASK_DONE_FLAG0.getCode());
                    operResService.insertRecord(operResTemp);
                }
                MessageUtil.addInfo("权限添加成功!");
            }
            initRes();
            initFuncListByResType(resRoot);
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    private CttInfoShow fromResModelShowToCttInfoShow(OperFuncResShow operFuncResShowPara){
        CttInfoShow cttInfoShowTemp=new CttInfoShow();
        cttInfoShowTemp.setCttType(operFuncResShowPara.getResType());
        cttInfoShowTemp.setPkid(operFuncResShowPara.getResPkid());
        cttInfoShowTemp.setName(operFuncResShowPara.getResName());
        return cttInfoShowTemp;
    }

    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if ("".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()))){
            return false;
        }
        return true;
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.operFuncResShowFowExcelList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "人员权限资源分配表-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"operFuncRes.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }

    private void recursiveOperTreeNodeForFuncChange(TreeNode treeNodePara, List<OperResShow> operResShowListPara) {
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
                recursiveOperTreeNodeForFuncChange(treeNodeTemp, operResShowListPara);
            }
        }
    }

    public void onRowSelect(NodeSelectEvent event) {
        currentSelectedResNode=event.getTreeNode();
        if (lastSelectedResNode==null){
            lastSelectedResNode=currentSelectedResNode;
        }else {
            ((OperFuncResShow)lastSelectedResNode.getData()).setIsActived("false");
            lastSelectedResNode=currentSelectedResNode;
        }
        ((OperFuncResShow)currentSelectedResNode.getData()).setIsActived("true");
        strBtnRender="true";
    }
    /*智能字段 Start*/

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

    public List<SelectItem> getTaskFunctionList() {
        return taskFunctionList;
    }

    public void setTaskFunctionList(List<SelectItem> taskFunctionList) {
        this.taskFunctionList = taskFunctionList;
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

    public TreeNode getResRoot() {
        return resRoot;
    }

    public void setResRoot(TreeNode resRoot) {
        this.resRoot = resRoot;
    }

    public TreeNode getDeptOperRoot() {
        return deptOperRoot;
    }

    public void setDeptOperRoot(TreeNode deptOperRoot) {
        this.deptOperRoot = deptOperRoot;
    }

    public CttInfoShow getCttInfoShowAdd() {
        return cttInfoShowAdd;
    }

    public void setCttInfoShowAdd(CttInfoShow cttInfoShowAdd) {
        this.cttInfoShowAdd = cttInfoShowAdd;
    }

    public CttInfoShow getCttInfoShowUpd() {
        return cttInfoShowUpd;
    }

    public void setCttInfoShowUpd(CttInfoShow cttInfoShowUpd) {
        this.cttInfoShowUpd = cttInfoShowUpd;
    }

    public CttInfoShow getCttInfoShowDel() {
        return cttInfoShowDel;
    }

    public void setCttInfoShowDel(CttInfoShow cttInfoShowDel) {
        this.cttInfoShowDel = cttInfoShowDel;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public DeptOperService getDeptOperService() {
        return deptOperService;
    }

    public void setDeptOperService(DeptOperService deptOperService) {
        this.deptOperService = deptOperService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public String getStrBtnRender() {
        return strBtnRender;
    }

    public void setStrBtnRender(String strBtnRender) {
        this.strBtnRender = strBtnRender;
    }
}
