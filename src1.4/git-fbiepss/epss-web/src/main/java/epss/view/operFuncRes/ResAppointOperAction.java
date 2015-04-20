package epss.view.operFuncRes;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumArchivedFlag;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumTaskDoneFlag;
import epss.repository.model.CttInfo;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeSelectEvent;
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
public class ResAppointOperAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(ResAppointOperAction.class);
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

    private List<OperAppointShow> operAppointShowFowExcelList;
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
    // 作为查询条件总包合同名
    private String strTkcttInfoName;

    private String strBtnRender;

    private String strFuncSelected;
    private List<OperAppointShow> operAppointShowList;//批量更新，存放选中资源

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cttInfoShowAdd=new CttInfoShow();
        cttInfoShowUpd=new CttInfoShow();
        cttInfoShowDel=new CttInfoShow();
        esInitCttList = new ArrayList<>();
        cttInfoShowList = new ArrayList<>();
        deptOperShowSeledList = new ArrayList<>();
        operAppointShowFowExcelList = new ArrayList<>();
        strBtnRender="false";
        resRoot = new DefaultTreeNode("ROOT", null);
        initDeptOper("Mng");
        beansMap.put("operFuncResShowFowExcelList", operAppointShowFowExcelList);
        operAppointShowList =new ArrayList<OperAppointShow>();
    }

    private void initRes(){
        resRoot = new DefaultTreeNode("ROOT", null);
        OperAppointShow operAppointShowTemp =new OperAppointShow();
        operAppointShowTemp.setResPkid("ROOT");
        operAppointShowTemp.setResName("资源信息");
        TreeNode node0 = new DefaultTreeNode(operAppointShowTemp,resRoot);
        try {
            recursiveResTreeNode("ROOT", node0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        node0.setExpanded(true);
    }
    private void initDeptOper(String strQryFlagPara){
        deptOperRoot = new DefaultTreeNode("ROOT", null);
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("机构人员信息");
        deptOperShowTemp.setType("0");
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, deptOperRoot);
        recursiveOperTreeNode("ROOT", node0,strQryFlagPara);
        node0.setExpanded(true);
    }
    private void recursiveResTreeNode(String strParentPkidPara,TreeNode parentNode)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<CttInfoShow> cttInfoShowList;
        if("ROOT".equals(strParentPkidPara)){
            cttInfoShowList=cttInfoService.selectRecordsFromCtt(strParentPkidPara,strTkcttInfoName);
        }else{
            cttInfoShowList=cttInfoService.selectRecordsFromCtt(strParentPkidPara,"");
        }

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
            String strQryOperName="";
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
                else if("6".equals(operResShowUnit.getFlowStatus())){
                    if(strQryOperName.length()==0){
                        strQryOperName = ToolUtil.getStrIgnoreNull(operResShowUnit.getOperName());
                    }else {
                        strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                    }
                }
            }
            OperAppointShow operAppointShowTemp =new OperAppointShow();
            operAppointShowTemp.setResType(cttInfoShowTemp.getCttType());
            operAppointShowTemp.setResPkid(cttInfoShowTemp.getPkid());
            String strResTypeName="";
            if(EnumResType.RES_TYPE0.getCode().equals(cttInfoShowTemp.getCttType())){
                strResTypeName="总包合同_";
            }else if(EnumResType.RES_TYPE1.getCode().equals(cttInfoShowTemp.getCttType())){
                strResTypeName="成本计划_";
            }else if(EnumResType.RES_TYPE2.getCode().equals(cttInfoShowTemp.getCttType())){
                strResTypeName="分包合同_";
            }
            operAppointShowTemp.setResName(strResTypeName+cttInfoShowTemp.getName());
            operAppointShowTemp.setInputOperName(strInputOperName);
            operAppointShowTemp.setCheckOperName(strCheckOperName);
            operAppointShowTemp.setDoubleCheckOperName(strDoubleCheckOperName);
            operAppointShowTemp.setApproveOperName(strApproveOperName);
            operAppointShowTemp.setAccountOperName(strAccountOperName);
            operAppointShowTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
            operAppointShowTemp.setQryOperName(strQryOperName);
            // 合同
            TreeNode childNodeTemp = new DefaultTreeNode(operAppointShowTemp, parentNode);
            OperAppointShow operAppointShowForExcelTemp = (OperAppointShow)BeanUtils.cloneBean(operAppointShowTemp);
            operAppointShowForExcelTemp.setResName(
            ToolUtil.padLeftSpace_DoLevel(Integer.parseInt(operAppointShowForExcelTemp.getResType()), operAppointShowForExcelTemp.getResName()));
            operAppointShowFowExcelList.add(operAppointShowForExcelTemp);
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
                strQryOperName="";
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
                        if (strPlaceOnFileOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }else if (operResShowUnit.getFlowStatus().equals("6")) {
                        if (strQryOperName.length() == 0) {
                            strQryOperName = operResShowUnit.getOperName();
                        } else {
                            strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                OperAppointShow operAppointShowForStlTemp = new OperAppointShow();
                operAppointShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operAppointShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                /*operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__统计");*/
                operAppointShowForStlTemp.setResName("总包进度_统计");
                operAppointShowForStlTemp.setInputOperName(strInputOperName);
                operAppointShowForStlTemp.setCheckOperName(strCheckOperName);
                operAppointShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operAppointShowForStlTemp.setApproveOperName(strApproveOperName);
                operAppointShowForStlTemp.setAccountOperName(strAccountOperName);
                operAppointShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                operAppointShowForStlTemp.setQryOperName(strQryOperName);
                // 安装树节点
                new DefaultTreeNode(operAppointShowForStlTemp, childNodeTemp);
                operAppointShowForExcelTemp = (OperAppointShow) BeanUtils.cloneBean(operAppointShowForStlTemp);
                operAppointShowFowExcelList.add(operAppointShowForExcelTemp);

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
                strQryOperName="";
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
                        if (strPlaceOnFileOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }else if (operResShowUnit.getFlowStatus().equals("6")) {
                        if (strQryOperName.length() == 0) {
                            strQryOperName = operResShowUnit.getOperName();
                        } else {
                            strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operAppointShowForStlTemp = new OperAppointShow();
                operAppointShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operAppointShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operAppointShowForStlTemp.setResName("总包进度_计量");
                operAppointShowForStlTemp.setInputOperName(strInputOperName);
                operAppointShowForStlTemp.setCheckOperName(strCheckOperName);
                operAppointShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operAppointShowForStlTemp.setApproveOperName(strApproveOperName);
                operAppointShowForStlTemp.setAccountOperName(strAccountOperName);
                operAppointShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                operAppointShowForStlTemp.setQryOperName(strQryOperName);
                // 安装树节点
                new DefaultTreeNode(operAppointShowForStlTemp, childNodeTemp);
                operAppointShowForExcelTemp = (OperAppointShow) BeanUtils.cloneBean(operAppointShowForStlTemp);
                operAppointShowFowExcelList.add(operAppointShowForExcelTemp);
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
                strQryOperName="";
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
                        if (strPlaceOnFileOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }else if (operResShowUnit.getFlowStatus().equals("6")) {
                        if (strQryOperName.length() == 0) {
                            strQryOperName = operResShowUnit.getOperName();
                        } else {
                            strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                OperAppointShow operAppointShowForStlTemp = new OperAppointShow();
                operAppointShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operAppointShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operAppointShowForStlTemp.setResName("分包进度_工程量结算");
                operAppointShowForStlTemp.setInputOperName(strInputOperName);
                operAppointShowForStlTemp.setCheckOperName(strCheckOperName);
                operAppointShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operAppointShowForStlTemp.setApproveOperName(strApproveOperName);
                operAppointShowForStlTemp.setAccountOperName(strAccountOperName);
                operAppointShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                operAppointShowForStlTemp.setQryOperName(strQryOperName);
                // 安装树节点
                new DefaultTreeNode(operAppointShowForStlTemp, childNodeTemp);
                operAppointShowForExcelTemp = (OperAppointShow) BeanUtils.cloneBean(operAppointShowForStlTemp);
                operAppointShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operAppointShowForExcelTemp.getResName()));
                operAppointShowFowExcelList.add(operAppointShowForExcelTemp);

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
                strQryOperName="";
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
                        if (strPlaceOnFileOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }else if (operResShowUnit.getFlowStatus().equals("6")) {
                        if (strQryOperName.length() == 0) {
                            strQryOperName = operResShowUnit.getOperName();
                        } else {
                            strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operAppointShowForStlTemp = new OperAppointShow();
                operAppointShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operAppointShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operAppointShowForStlTemp.setResName("分包进度_材料消耗量结算");
                operAppointShowForStlTemp.setInputOperName(strInputOperName);
                operAppointShowForStlTemp.setCheckOperName(strCheckOperName);
                operAppointShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operAppointShowForStlTemp.setApproveOperName(strApproveOperName);
                operAppointShowForStlTemp.setAccountOperName(strAccountOperName);
                operAppointShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                operAppointShowForStlTemp.setQryOperName(strQryOperName);
                // 安装树节点
                new DefaultTreeNode(operAppointShowForStlTemp, childNodeTemp);
                operAppointShowForExcelTemp = (OperAppointShow) BeanUtils.cloneBean(operAppointShowForStlTemp);
                operAppointShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operAppointShowForExcelTemp.getResName()));
                operAppointShowFowExcelList.add(operAppointShowForExcelTemp);

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
                strQryOperName="";
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
                        if (strPlaceOnFileOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }else if (operResShowUnit.getFlowStatus().equals("6")) {
                        if (strQryOperName.length() == 0) {
                            strQryOperName = operResShowUnit.getOperName();
                        } else {
                            strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operAppointShowForStlTemp = new OperAppointShow();
                operAppointShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operAppointShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operAppointShowForStlTemp.setResName("分包进度_费用结算");
                operAppointShowForStlTemp.setInputOperName(strInputOperName);
                operAppointShowForStlTemp.setCheckOperName(strCheckOperName);
                operAppointShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operAppointShowForStlTemp.setApproveOperName(strApproveOperName);
                operAppointShowForStlTemp.setAccountOperName(strAccountOperName);
                operAppointShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                operAppointShowForStlTemp.setQryOperName(strQryOperName);
                // 安装树节点
                new DefaultTreeNode(operAppointShowForStlTemp, childNodeTemp);
                operAppointShowForExcelTemp = (OperAppointShow) BeanUtils.cloneBean(operAppointShowForStlTemp);
                operAppointShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operAppointShowForExcelTemp.getResName()));
                operAppointShowFowExcelList.add(operAppointShowForExcelTemp);

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
                strQryOperName="";
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
                        if (strPlaceOnFileOperName.length() == 0) {
                            strPlaceOnFileOperName = operResShowUnit.getOperName();
                        } else {
                            strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                        }
                    }else if (operResShowUnit.getFlowStatus().equals("6")) {
                        if (strQryOperName.length() == 0) {
                            strQryOperName = operResShowUnit.getOperName();
                        } else {
                            strQryOperName = strQryOperName + "," + operResShowUnit.getOperName();
                        }
                    }
                }
                operAppointShowForStlTemp = new OperAppointShow();
                operAppointShowForStlTemp.setResType(operResShowTemp.getInfoType());
                operAppointShowForStlTemp.setResPkid(operResShowTemp.getInfoPkid());
                operAppointShowForStlTemp.setResName("分包进度＿结算单");
                operAppointShowForStlTemp.setInputOperName(strInputOperName);
                operAppointShowForStlTemp.setCheckOperName(strCheckOperName);
                operAppointShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operAppointShowForStlTemp.setApproveOperName(strApproveOperName);
                operAppointShowForStlTemp.setAccountOperName(strAccountOperName);
                operAppointShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                operAppointShowForStlTemp.setQryOperName(strQryOperName);
                // 安装树节点
                new DefaultTreeNode(operAppointShowForStlTemp, childNodeTemp);

                operAppointShowForExcelTemp = (OperAppointShow) BeanUtils.cloneBean(operAppointShowForStlTemp);
                operAppointShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3, operAppointShowForExcelTemp.getResName()));
                operAppointShowFowExcelList.add(operAppointShowForExcelTemp);
            }
            if (currentSelectedNode!=null){
                OperAppointShow operAppointShow1 = (OperAppointShow) currentSelectedNode.getData();
                OperAppointShow operAppointShow2 = (OperAppointShow) childNodeTemp.getData();
                if ("ROOT".equals(operAppointShow1.getResPkid())){
                    currentSelectedNode.setExpanded(true);
                }else {
                    if (operAppointShow1.getResType().equals(operAppointShow2.getResType())
                            && operAppointShow1.getResPkid().equals(operAppointShow2.getResPkid())){
                        TreeNode treeNodeTemp=childNodeTemp;
                        while (!(treeNodeTemp.getParent()==null)){
                            treeNodeTemp.setExpanded(true);
                            treeNodeTemp=treeNodeTemp.getParent();
                        }
                    }
                }
            }
            recursiveResTreeNode(operAppointShowTemp.getResPkid(),childNodeTemp);
        }
    }
    private void recursiveOperTreeNode(String strParentPkidPara,TreeNode parentNode,String strQryFlagPara){
        List<DeptOperShow> operResShowListTemp=
                deptOperService.selectDeptAndOperRecords(strParentPkidPara,strQryFlagPara);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getPkid(), childNode,strQryFlagPara);
        }
    }

    public void recursiveResTreeNodeForExpanded(TreeNode treeNodePara){
        treeNodePara.setExpanded(false);
        if (treeNodePara.getChildCount()!=0){
            for (int i=0;i<treeNodePara.getChildCount();i++){
                recursiveResTreeNodeForExpanded(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void initFuncListByResType(TreeNode treeNodePara) {
        if (!treeNodePara.getData().toString().equals("ROOT")){
            OperAppointShow operAppointShowTemp = ((OperAppointShow) treeNodePara.getData());
            if (EnumResType.RES_TYPE3.getCode().equals(operAppointShowTemp.getResType())
                    || EnumResType.RES_TYPE4.getCode().equals(operAppointShowTemp.getResType())) {
                operAppointShowTemp.setIsMng(EnumFlowStatus.FLOW_STATUS0.getCode());
                operAppointShowTemp.setIsCheck(EnumFlowStatus.FLOW_STATUS1.getCode());
                operAppointShowTemp.setIsDoubleCheck(EnumFlowStatus.FLOW_STATUS2.getCode());
                operAppointShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            } else if (EnumResType.RES_TYPE5.getCode().equals(operAppointShowTemp.getResType())) {
                operAppointShowTemp.setIsApprove(EnumFlowStatus.FLOW_STATUS3.getCode());
                operAppointShowTemp.setIsAccount(EnumFlowStatus.FLOW_STATUS4.getCode());
                operAppointShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            } else {
                operAppointShowTemp.setIsMng(EnumFlowStatus.FLOW_STATUS0.getCode());
                operAppointShowTemp.setIsCheck(EnumFlowStatus.FLOW_STATUS1.getCode());
                operAppointShowTemp.setIsDoubleCheck(EnumFlowStatus.FLOW_STATUS2.getCode());
                operAppointShowTemp.setIsApprove(EnumFlowStatus.FLOW_STATUS3.getCode());
                operAppointShowTemp.setIsPlaceOnFile(EnumFlowStatus.FLOW_STATUS5.getCode());
            }
        }
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                initFuncListByResType(treeNodePara.getChildren().get(i));
            }
        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        recursiveResTreeNodeForExpanded(event.getTreeNode());
    }

    public void findSelectedNode(
            OperAppointShow operAppointShowPara,
            TreeNode treeNodePara,
            String strSubmitTypePara) {
        if (treeNodePara.getChildCount() != 0) {
            for (int i = 0; i < treeNodePara.getChildCount(); i++) {
                TreeNode treeNodeTemp = treeNodePara.getChildren().get(i);
                if (operAppointShowPara == treeNodeTemp.getData()) {
                    if ("Add".equals(strSubmitTypePara)){
                        currentSelectedNode = treeNodeTemp;
                    }else if ("Upd".equals(strSubmitTypePara)||"Del".equals(strSubmitTypePara)||"Sel".equals(strSubmitTypePara)){
                        currentSelectedNode=treeNodeTemp.getParent();
                    }
                    return;
                }
                findSelectedNode(operAppointShowPara, treeNodeTemp,strSubmitTypePara);
            }
        }
    }

    public void onQueryAction(){
        // 资源-用户-功能
        initRes();
        initFuncListByResType(resRoot);
        operAppointShowList.clear();
    }

    public void selectRecordAction(String strSubmitTypePara,OperAppointShow operAppointShowPara) {
        try {
            findSelectedNode(operAppointShowPara,resRoot,strSubmitTypePara);
            if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = new CttInfoShow();
                if(operAppointShowPara.getResPkid().equals("ROOT")) {
                    cttInfoShowAdd.setCttType(EnumResType.RES_TYPE0.getCode());
                    cttInfoShowAdd.setParentPkid("ROOT");
                }else if(operAppointShowPara.getResType().equals(EnumResType.RES_TYPE0.getCode())) {
                    cttInfoShowAdd.setCttType(EnumResType.RES_TYPE1.getCode());
                    cttInfoShowAdd.setParentPkid(operAppointShowPara.getResPkid());
                }else if(operAppointShowPara.getResType().equals(EnumResType.RES_TYPE1.getCode())) {
                    cttInfoShowAdd.setCttType(EnumResType.RES_TYPE2.getCode());
                    cttInfoShowAdd.setParentPkid(operAppointShowPara.getResPkid());
                }
                cttInfoShowAdd.setId(setMaxNoPlusOne(cttInfoShowAdd.getCttType()));
            } else if (strSubmitTypePara.equals("Upd")){
                cttInfoShowUpd = fromResModelShowToCttInfoShow(operAppointShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                cttInfoShowDel = fromResModelShowToCttInfoShow(operAppointShowPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public void selectRecordAction(String strSubmitTypePara, OperAppointShow operAppointShowPara, String strResFlowStatus) {
        try {
            findSelectedNode(operAppointShowPara, resRoot, strSubmitTypePara);
            if (strSubmitTypePara.equals("Sel")) {
                cttInfoShowSel = fromResModelShowToCttInfoShow(operAppointShowPara);
                cttInfoShowSel.setFlowStatus(strResFlowStatus);
                initDeptOper("Mng");
                deptOperShowSeledList.clear();
                OperResShow operResShowTemp = new OperResShow();
                operResShowTemp.setInfoType(operAppointShowPara.getResType());
                operResShowTemp.setInfoPkid(operAppointShowPara.getResPkid());
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

    public void selectFuncDispatchAction(String strFuncSelectedPara) {//批量操作，初始化人员列表
        String strQryFlagTemp="Mng";
        if("6".equals(strFuncSelectedPara)){
            strQryFlagTemp="Qry";
        }
        if (operAppointShowList.size()==1){
            initDeptOper(strQryFlagTemp);
            deptOperShowSeledList.clear();
            OperResShow operResShowTemp = new OperResShow();
            operResShowTemp.setInfoType(operAppointShowList.get(0).getResType());
            operResShowTemp.setInfoPkid(operAppointShowList.get(0).getResPkid());
            operResShowTemp.setFlowStatus(strFuncSelectedPara);
            List<OperResShow> operResShowListTemp = operResService.selectOperaResRecordsByModelShow(operResShowTemp);
            if (operResShowListTemp.size() > 0) {
                recursiveOperTreeNodeForFuncChange(deptOperRoot, operResShowListTemp);
            }
            strFuncSelected=strFuncSelectedPara;
        }else if (operAppointShowList.size()>1){
            initDeptOper(strQryFlagTemp);
            deptOperShowSeledList.clear();
            strFuncSelected=strFuncSelectedPara;
        }
        else{
            MessageUtil.addError("请选择操作资源！");
        }
    }

    public boolean clickFuncDispatchAction() {//批量操作，初始化人员列表
        if (operAppointShowList.size()>0){
            return true;
        }else{
            return false;
        }
    }

    public void selResRecordsAction(OperAppointShow operAppointShowPara){//批量更新，资源选择监听函数
        if (operAppointShowPara.getIsSeled()){//被选中，执行该判断
            operAppointShowList.add(operAppointShowPara);
        }else{//取消选择
            operAppointShowList.remove(operAppointShowPara);
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
            } else if (strSubmitTypePara.equals("BatchPower")) {//批量权限指派
                operResService.insertRecordsBatch(strFuncSelected, operAppointShowList,deptOperShowSeledList);
                operAppointShowList.clear();
                MessageUtil.addInfo("权限指派成功!");
            }
            initRes();
            initFuncListByResType(resRoot);
        }catch (Exception e){
            MessageUtil.addError(e.getMessage());
            logger.error("初始化失败", e);
        }
    }

    private CttInfoShow fromResModelShowToCttInfoShow(OperAppointShow operAppointShowPara){
        CttInfoShow cttInfoShowTemp=new CttInfoShow();
        cttInfoShowTemp.setCttType(operAppointShowPara.getResType());
        cttInfoShowTemp.setPkid(operAppointShowPara.getResPkid());
        cttInfoShowTemp.setName(operAppointShowPara.getResName());
        return cttInfoShowTemp;
    }

    private boolean submitPreCheck(CttInfoShow cttInfoShowPara) {
        if ("".equals(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()))){
            return false;
        }
        return true;
    }

    public String onExportExcel()throws IOException, WriteException {
        if (this.operAppointShowFowExcelList.size() == 0) {
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
            ((OperAppointShow)lastSelectedResNode.getData()).setIsActived("false");
            lastSelectedResNode=currentSelectedResNode;
        }
        ((OperAppointShow)currentSelectedResNode.getData()).setIsActived("true");
        strBtnRender="true";
    }

    /*智能字段 Start*/

    public String getStrTkcttInfoName() {
        return strTkcttInfoName;
    }

    public void setStrTkcttInfoName(String strTkcttInfoName) {
        this.strTkcttInfoName = strTkcttInfoName;
    }

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

    public List<OperAppointShow> getOperAppointShowList() {
        return operAppointShowList;
    }

    public void setOperAppointShowList(List<OperAppointShow> operAppointShowList) {
        this.operAppointShowList = operAppointShowList;
    }
}
