package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumDeletedFlag;
import epss.common.enums.ESEnumStatusFlag;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsInitStl;
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
    private String taskFunctionSeled;
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
    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cttInfoShowAdd=new CttInfoShow();
        cttInfoShowUpd=new CttInfoShow();
        cttInfoShowDel=new CttInfoShow();
        esInitCttList = new ArrayList<>();
        cttInfoShowList = new ArrayList<>();
        taskFunctionList = new ArrayList<>();
        deptOperShowSeledList = new ArrayList<>();
        operFuncResShowFowExcelList= new ArrayList<>();
        taskFunctionList.add(
                new SelectItem(ESEnumStatusFlag.STATUS_FLAG0.getCode(),ESEnumStatusFlag.STATUS_FLAG0.getTitle()));
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

        // 资源-用户-功能
        initRes();
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
        taskFunctionSeled = "";
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
    private void recursiveResTreeNode(String parentPkidPara,TreeNode parentNode)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<CttInfoShow> cttInfoShowList=operResService.selectRecordsFromCtt(parentPkidPara);
        for (int i=0;i<cttInfoShowList.size();i++){
            CttInfoShow cttInfoShowTemp =cttInfoShowList.get(i);
            // 总成分
            OperResShow operResShowPara=new OperResShow();
            operResShowPara.setInfoType(cttInfoShowTemp.getCttType());
            operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
            List<OperResShow> operResShowListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
            String strInputOperName="";
            String strCheckOperName="";
            String strDoubleCheckOperName="";
            String strApproveOperName="";
            String strAccountOperName="";
            String strPlaceOnFileOperName="";
            for(OperResShow operResShowUnit:operResShowListTemp){
                if("0".equals(operResShowUnit.getFlowStatus())){
                    if(strInputOperName.length()==0){
                        strInputOperName = operResShowUnit.getOperName();
                    }else {
                        strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("1".equals(operResShowUnit.getFlowStatus())){
                    if(strCheckOperName.length()==0){
                        strCheckOperName = operResShowUnit.getOperName();
                    }else {
                        strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("2".equals(operResShowUnit.getFlowStatus())){
                    if(strDoubleCheckOperName.length()==0){
                        strDoubleCheckOperName = operResShowUnit.getOperName();
                    }else {
                        strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("3".equals(operResShowUnit.getFlowStatus())){
                    if(strApproveOperName.length()==0){
                        strApproveOperName = operResShowUnit.getOperName();
                    }else {
                        strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("4".equals(operResShowUnit.getFlowStatus())){
                    if(strAccountOperName.length()==0){
                        strAccountOperName = operResShowUnit.getOperName();
                    }else {
                        strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                    }
                }else if("5".equals(operResShowUnit.getFlowStatus())){
                    if(strPlaceOnFileOperName.length()==0){
                        strPlaceOnFileOperName = operResShowUnit.getOperName();
                    }else {
                        strPlaceOnFileOperName = strPlaceOnFileOperName + "," + operResShowUnit.getOperName();
                    }
                }
            }
            OperFuncResShow operFuncResShowTemp=new OperFuncResShow();
            operFuncResShowTemp.setResType(cttInfoShowTemp.getCttType());
            operFuncResShowTemp.setResPkid(cttInfoShowTemp.getPkid());
            operFuncResShowTemp.setResName(cttInfoShowTemp.getName());
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
            if(cttInfoShowTemp.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                // 统计
                EsInitStl esInitStlTemp=new EsInitStl();
                esInitStlTemp.setStlType(ESEnum.ITEMTYPE6.getCode());
                esInitStlTemp.setStlPkid(cttInfoShowTemp.getPkid());
                esInitStlTemp.setPeriodNo("NULL");
                List<EsInitStl> esInitStlListTemp = progStlInfoService.getInitStlListByModelShow(esInitStlTemp);
                if(esInitStlListTemp.size()>0) {
                    operResShowPara = new OperResShow();
                    operResShowPara.setInfoType(ESEnum.ITEMTYPE6.getCode());
                    operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                    List<OperResShow> operResShowForStlListTemp =
                            operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                    operFuncResShowForStlTemp.setResType(esInitStlTemp.getStlType());
                    operFuncResShowForStlTemp.setResPkid(esInitStlTemp.getStlPkid());
                    operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__统计");
                    operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                    operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                    operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                    operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                    operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                    operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                    new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);
                    operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                    operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                }
                // 计量
                esInitStlTemp=new EsInitStl();
                esInitStlTemp.setStlType(ESEnum.ITEMTYPE7.getCode());
                esInitStlTemp.setStlPkid(cttInfoShowTemp.getPkid());
                esInitStlTemp.setPeriodNo("NULL");
                esInitStlListTemp = progStlInfoService.getInitStlListByModelShow(esInitStlTemp);
                if(esInitStlListTemp.size()>0) {
                    operResShowPara = new OperResShow();
                    operResShowPara.setInfoType(ESEnum.ITEMTYPE7.getCode());
                    operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                    List<OperResShow> operResShowForStlListTemp =
                            operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                    operFuncResShowForStlTemp.setResType(esInitStlTemp.getStlType());
                    operFuncResShowForStlTemp.setResPkid(esInitStlTemp.getStlPkid());
                    operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__计量");
                    operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                    operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                    operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                    operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                    operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                    operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                    new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);
                    operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                    operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                }
            }else if(cttInfoShowTemp.getCttType().equals(ESEnum.ITEMTYPE2.getCode())) {
                // 数量结算
                EsInitStl esInitStlTemp = new EsInitStl();
                esInitStlTemp.setStlType(ESEnum.ITEMTYPE3.getCode());
                esInitStlTemp.setStlPkid(cttInfoShowTemp.getPkid());
                esInitStlTemp.setPeriodNo("NULL");
                List<EsInitStl> esInitStlListTemp =
                        progStlInfoService.getInitStlListByModelShow(esInitStlTemp);
                if (esInitStlListTemp.size() > 0) {
                    operResShowPara = new OperResShow();
                    operResShowPara.setInfoType(esInitStlTemp.getStlType());
                    operResShowPara.setInfoPkid(esInitStlTemp.getStlPkid());
                    List<OperResShow> operResShowForStlListTemp =
                            operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                    operFuncResShowForStlTemp.setResType(esInitStlTemp.getStlType());
                    operFuncResShowForStlTemp.setResPkid(esInitStlTemp.getStlPkid());
                    operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__数量结算");
                    operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                    operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                    operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                    operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                    operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                    operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                    new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                    operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                    operFuncResShowForExcelTemp.setResName(
                            ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                    operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                }

                // 材料结算
                esInitStlTemp = new EsInitStl();
                esInitStlTemp.setStlType(ESEnum.ITEMTYPE4.getCode());
                esInitStlTemp.setStlPkid(cttInfoShowTemp.getPkid());
                esInitStlTemp.setPeriodNo("NULL");
                esInitStlListTemp = progStlInfoService.getInitStlListByModelShow(esInitStlTemp);
                if (esInitStlListTemp.size() > 0) {
                    operResShowPara = new OperResShow();
                    operResShowPara.setInfoType(esInitStlTemp.getStlType());
                    operResShowPara.setInfoPkid(esInitStlTemp.getStlPkid());
                    List<OperResShow> operResShowForStlListTemp =
                            operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                    operFuncResShowForStlTemp.setResType(esInitStlTemp.getStlType());
                    operFuncResShowForStlTemp.setResPkid(esInitStlTemp.getStlPkid());
                    operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__材料结算");
                    operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                    operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                    operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                    operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                    operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                    operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                    new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                    operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                    operFuncResShowForExcelTemp.setResName(
                            ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                    operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                }
                // 结算单
                esInitStlTemp = new EsInitStl();
                esInitStlTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
                esInitStlTemp.setStlPkid(cttInfoShowTemp.getPkid());
                esInitStlTemp.setPeriodNo("NULL");
                esInitStlListTemp = progStlInfoService.getInitStlListByModelShow(esInitStlTemp);
                if (esInitStlListTemp.size() > 0) {
                    operResShowPara = new OperResShow();
                    operResShowPara.setInfoType(esInitStlTemp.getStlType());
                    operResShowPara.setInfoPkid(esInitStlTemp.getStlPkid());
                    List<OperResShow> operResShowForStlListTemp =
                            operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                    operFuncResShowForStlTemp.setResType(esInitStlTemp.getStlType());
                    operFuncResShowForStlTemp.setResPkid(esInitStlTemp.getStlPkid());
                    operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName() + "__结算单");
                    operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                    operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                    operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                    operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                    operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                    operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                    new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                    operFuncResShowForExcelTemp = (OperFuncResShow) BeanUtils.cloneBean(operFuncResShowForStlTemp);
                    operFuncResShowForExcelTemp.setResName(
                            ToolUtil.padLeftSpace_DoLevel(3, operFuncResShowForExcelTemp.getResName()));
                    operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                }
            }
            recursiveResTreeNode(operFuncResShowTemp.getResPkid(),childNodeTemp);
        }
    }
    private void recursiveOperTreeNode(String strParentPkidPara,TreeNode parentNode){
        List<DeptOperShow> operResShowListTemp= deptOperService.selectDeptAndOperRecords(strParentPkidPara);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getId(), childNode);
        }
    }

    public void selectRecordAction(String strSubmitTypePara,OperFuncResShow operFuncResShowPara) {
        try {
            if (strSubmitTypePara.equals("Add")) {
                cttInfoShowAdd = new CttInfoShow();
                if(operFuncResShowPara.getResPkid().equals("ROOT")) {
                    cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE0.getCode());
                    cttInfoShowAdd.setParentPkid("ROOT");
                }else if(operFuncResShowPara.getResType().equals(ESEnum.ITEMTYPE0.getCode())) {
                    cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE1.getCode());
                    cttInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }else if(operFuncResShowPara.getResType().equals(ESEnum.ITEMTYPE1.getCode())) {
                    cttInfoShowAdd.setCttType(ESEnum.ITEMTYPE2.getCode());
                    cttInfoShowAdd.setParentPkid(operFuncResShowPara.getResPkid());
                }
                cttInfoShowAdd.setId(cttInfoService.getStrMaxCttId(cttInfoShowAdd.getCttType()));
            } else if (strSubmitTypePara.equals("Upd")){
                cttInfoShowUpd = fromResModelShowToCttInfoShow(operFuncResShowPara);
            } else if (strSubmitTypePara.equals("Del")) {
                cttInfoShowDel = fromResModelShowToCttInfoShow(operFuncResShowPara);
            } else if (strSubmitTypePara.equals("Sel")) {
                cttInfoShowSel = fromResModelShowToCttInfoShow(operFuncResShowPara);
                taskFunctionSeled="";
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
                if (cttInfoService.isExistInDb(cttInfoShowAdd)) {
                    MessageUtil.addError("该记录已存在，请重新录入！");
                    return;
                } else {
                    cttInfoService.insertRecord(cttInfoShowAdd);
                    List<EsCttInfo> esCttInfoList=cttInfoService.selectListByModel(cttInfoShowAdd);
                    try {
                        progStlInfoService.insertRecordForOperRes(esCttInfoList.get(0));
                    }catch (Exception e){
                        logger.error("插入数据失败", e);
                        cttInfoService.deleteRecord(esCttInfoList.get(0).getPkid());
                        MessageUtil.addError(e.getMessage());
                        return;
                    }
                    MessageUtil.addInfo("新增数据完成。");
                    cttInfoShowAdd = new CttInfoShow();
                }
            } else if (strSubmitTypePara.equals("Upd")) {
                if (!submitPreCheck(cttInfoShowUpd)) {
                    MessageUtil.addError("请输入名称！");
                    return;
                }
                cttInfoService.updateRecordForOperRes(cttInfoShowUpd);
                MessageUtil.addInfo("更新数据完成。");
            } else if (strSubmitTypePara.equals("Del")) {
                if (!submitPreCheck(cttInfoShowDel)) {
                    MessageUtil.addError("该记录已被删除！");
                    return;
                }
                MessageUtil.addInfo(operResService.deleteResRecord(cttInfoShowDel));
            } else if (strSubmitTypePara.equals("Power")) {
                if (taskFunctionSeled.length()==0) {
                    MessageUtil.addError("功能列表不能为空，请选择");
                    return;
                }
                OperRes operResTemp = new OperRes();
                operResTemp.setInfoType(cttInfoShowSel.getCttType());
                operResTemp.setInfoPkid(cttInfoShowSel.getPkid());
                operResTemp.setFlowStatus(taskFunctionSeled);
                operResService.deleteRecord(operResTemp);
                for (DeptOperShow deptOperShowUnit:deptOperShowSeledList) {
                    operResTemp = new OperRes();
                    operResTemp.setOperPkid(deptOperShowUnit.getId());
                    operResTemp.setFlowStatus(taskFunctionSeled);
                    operResTemp.setInfoType(cttInfoShowSel.getCttType());
                    operResTemp.setInfoPkid(cttInfoShowSel.getPkid());
                    operResTemp.setArchivedFlag(ESEnumDeletedFlag.DELETED_FLAG0.getCode());
                    operResTemp.setType("business");
                    operResService.insertRecord(operResTemp);
                }
                MessageUtil.addInfo("权限添加成功!");
            }
            initRes();
            initDeptOper();
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

    public String getTaskFunctionSeled() {
        return taskFunctionSeled;
    }

    public void setTaskFunctionSeled(String taskFunctionSeled) {
        this.taskFunctionSeled = taskFunctionSeled;
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
}
