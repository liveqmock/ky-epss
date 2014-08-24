package epss.view.operRes;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumDeletedFlag;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.JxlsManager;
import skyline.util.MessageUtil;;
import skyline.util.ToolUtil;
import epss.repository.model.EsInitPower;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.*;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
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
public class OperFuncResAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(OperFuncResAction.class);
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;
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
    private TreeNode operRoot;
    @PostConstruct
    public void init() {
        beansMap = new HashMap();

        cttInfoShowAdd=new CttInfoShow();
        cttInfoShowUpd=new CttInfoShow();
        cttInfoShowDel=new CttInfoShow();
        esInitCttList = new ArrayList<>();
        cttInfoShowList = new ArrayList<>();
        taskFunctionList = new ArrayList<>();
        taskFunctionSeled = "";
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
        initOper();
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
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        node0.setExpanded(true);
    }
    private void initOper(){
        DeptOperShow deptOperShowTemp =new DeptOperShow();
        deptOperShowTemp.setPkid("ROOT");
        deptOperShowTemp.setName("人员信息");
        operRoot = new DefaultTreeNode("ROOT", null);
        TreeNode node0 = new DefaultTreeNode(deptOperShowTemp, operRoot);
        recursiveOperTreeNode("0", node0);
        node0.setExpanded(true);
    }
    private void recursiveResTreeNode(String parentPkidPara,TreeNode parentNode) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
                if(operResShowUnit.getFlowStatus().equals("0")){
                    if(strInputOperName.length()==0){
                        strInputOperName = operResShowUnit.getOperName();
                    }else {
                        strInputOperName = strInputOperName + "," + operResShowUnit.getOperName();
                    }
                }else if(operResShowUnit.getFlowStatus().equals("1")){
                    if(strCheckOperName.length()==0){
                        strCheckOperName = operResShowUnit.getOperName();
                    }else {
                        strCheckOperName = strCheckOperName + "," + operResShowUnit.getOperName();
                    }
                }else if(operResShowUnit.getFlowStatus().equals("2")){
                    if(strDoubleCheckOperName.length()==0){
                        strDoubleCheckOperName = operResShowUnit.getOperName();
                    }else {
                        strDoubleCheckOperName = strDoubleCheckOperName + "," + operResShowUnit.getOperName();
                    }
                }else if(operResShowUnit.getFlowStatus().equals("3")){
                    if(strApproveOperName.length()==0){
                        strApproveOperName = operResShowUnit.getOperName();
                    }else {
                        strApproveOperName = strApproveOperName + "," + operResShowUnit.getOperName();
                    }
                }else if(operResShowUnit.getFlowStatus().equals("4")){
                    if(strAccountOperName.length()==0){
                        strAccountOperName = operResShowUnit.getOperName();
                    }else {
                        strAccountOperName = strAccountOperName + "," + operResShowUnit.getOperName();
                    }
                }else if(operResShowUnit.getFlowStatus().equals("5")){
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
            TreeNode childNodeTemp = new DefaultTreeNode(operFuncResShowTemp, parentNode);
            OperFuncResShow operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowTemp);
            operFuncResShowForExcelTemp.setResName(
            ToolUtil.padLeftSpace_DoLevel(Integer.parseInt(operFuncResShowForExcelTemp.getResType()),operFuncResShowForExcelTemp.getResName()));
            operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
            // 结算信息
            if(cttInfoShowTemp.getCttType().equals(ESEnum.ITEMTYPE0.getCode())) {
                // 统计
                operResShowPara = new OperResShow();
                operResShowPara.setInfoType(ESEnum.ITEMTYPE6.getCode());
                operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                List<OperResShow> operResShowForStlListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                operFuncResShowForStlTemp.setResType(ESEnum.ITEMTYPE6.getCode());
                operFuncResShowForStlTemp.setResPkid(cttInfoShowTemp.getPkid());
                operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName()+"__统计");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);
                operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                // 计量
                operResShowPara = new OperResShow();
                operResShowPara.setInfoType(ESEnum.ITEMTYPE7.getCode());
                operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                operFuncResShowForStlTemp.setResType(ESEnum.ITEMTYPE7.getCode());
                operFuncResShowForStlTemp.setResPkid(cttInfoShowTemp.getPkid());
                operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName()+"__计量");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
            }else if(cttInfoShowTemp.getCttType().equals(ESEnum.ITEMTYPE2.getCode())) {
                // 数量结算
                operResShowPara = new OperResShow();
                operResShowPara.setInfoType(ESEnum.ITEMTYPE3.getCode());
                operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                List<OperResShow> operResShowForStlListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                operFuncResShowForStlTemp.setResType(ESEnum.ITEMTYPE3.getCode());
                operFuncResShowForStlTemp.setResPkid(cttInfoShowTemp.getPkid());
                operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName()+"__数量结算");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3,operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
                // 材料结算
                operResShowPara = new OperResShow();
                operResShowPara.setInfoType(ESEnum.ITEMTYPE4.getCode());
                operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                operFuncResShowForStlTemp.setResType(ESEnum.ITEMTYPE4.getCode());
                operFuncResShowForStlTemp.setResPkid(cttInfoShowTemp.getPkid());
                operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName()+"__材料结算");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3,operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);

                // 结算单
                operResShowPara = new OperResShow();
                operResShowPara.setInfoType(ESEnum.ITEMTYPE5.getCode());
                operResShowPara.setInfoPkid(cttInfoShowTemp.getPkid());
                operResShowForStlListTemp = operResService.selectOperaResRecordsByModelShow(operResShowPara);
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
                operFuncResShowForStlTemp.setResType(ESEnum.ITEMTYPE5.getCode());
                operFuncResShowForStlTemp.setResPkid(cttInfoShowTemp.getPkid());
                operFuncResShowForStlTemp.setResName(cttInfoShowTemp.getName()+"__结算单");
                operFuncResShowForStlTemp.setInputOperName(strInputOperName);
                operFuncResShowForStlTemp.setCheckOperName(strCheckOperName);
                operFuncResShowForStlTemp.setDoubleCheckOperName(strDoubleCheckOperName);
                operFuncResShowForStlTemp.setApproveOperName(strApproveOperName);
                operFuncResShowForStlTemp.setAccountOperName(strAccountOperName);
                operFuncResShowForStlTemp.setPlaceOnFileOperName(strPlaceOnFileOperName);
                new DefaultTreeNode(operFuncResShowForStlTemp, parentNode);

                operFuncResShowForExcelTemp= (OperFuncResShow)BeanUtils.cloneBean(operFuncResShowForStlTemp);
                operFuncResShowForExcelTemp.setResName(
                        ToolUtil.padLeftSpace_DoLevel(3,operFuncResShowForExcelTemp.getResName()));
                operFuncResShowFowExcelList.add(operFuncResShowForExcelTemp);
            }

            recursiveResTreeNode(operFuncResShowTemp.getResPkid(),childNodeTemp);
        }
    }
    private void recursiveOperTreeNode(String strLevelParentId,TreeNode parentNode){
        List<DeptOperShow> operResShowListTemp= operResService.getOperList(strLevelParentId);
        for (int i=0;i<operResShowListTemp.size();i++){
            TreeNode childNode = null;
            childNode=new DefaultTreeNode(operResShowListTemp.get(i), parentNode);
            recursiveOperTreeNode(operResShowListTemp.get(i).getPkid(), childNode);
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
            }else if (strSubmitTypePara.equals("Sel")) {
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
                    return;
                }
                if (cttInfoService.isExistInDb(cttInfoShowAdd)) {
                    MessageUtil.addError("该记录已存在，请重新录入！");
                    return;
                } else {
                    cttInfoService.insertRecord(cttInfoShowAdd);
                    MessageUtil.addInfo("新增数据完成。");
                    cttInfoShowAdd = new CttInfoShow();
                }
            } else if (strSubmitTypePara.equals("Upd")) {
                EsInitPower esInitPowerTemp = new EsInitPower();
                esInitPowerTemp.setPowerType(cttInfoShowUpd.getCttType());
                esInitPowerTemp.setPowerPkid(cttInfoShowUpd.getPkid());
                esInitPowerTemp.setPeriodNo("NULL");
                if (flowCtrlService.selectListByModel(esInitPowerTemp).size() > 0) {
                    MessageUtil.addInfo("数据已被引用，不可更新！");
                    return;
                }
                cttInfoService.updateRecord(cttInfoShowUpd);
                MessageUtil.addInfo("更新数据完成。");
            } else if (strSubmitTypePara.equals("Del")) {
                EsInitPower esInitPowerTemp = new EsInitPower();
                esInitPowerTemp.setPowerType(cttInfoShowDel.getCttType());
                esInitPowerTemp.setPowerPkid(cttInfoShowDel.getPkid());
                esInitPowerTemp.setPeriodNo("NULL");
                if (flowCtrlService.selectListByModel(esInitPowerTemp).size() > 0) {
                    MessageUtil.addInfo("数据已被引用，不可删除！");
                    return;
                }
                int deleteRecordNumOfCttItem = cttItemService.deleteRecord(cttInfoShowDel);
                int deleteRecordNumOfCtt = cttInfoService.deleteRecord(cttInfoShowDel.getPkid());
                int deleteRecordNumOfPower = flowCtrlService.deleteRecord(
                        cttInfoShowDel.getCttType(),
                        cttInfoShowDel.getPkid(),
                        "NULL");
                if (deleteRecordNumOfCtt <= 0 && deleteRecordNumOfPower <= 0 && deleteRecordNumOfCttItem <= 0) {
                    MessageUtil.addInfo("该记录已删除。");
                    return;
                }
                MessageUtil.addInfo("删除数据完成。");
            } else if (strSubmitTypePara.equals("Power")) {
                if (taskFunctionSeled.length()==0) {
                    MessageUtil.addError("功能列表不能为空，请选择");
                    return;
                }
                if (deptOperShowSeledList.size() == 0) {
                    MessageUtil.addError("人员列表不能为空，请选择");
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
                    operResTemp.setInfoType(cttInfoShowSel.getCttType());
                    operResTemp.setInfoPkid(cttInfoShowSel.getPkid());
                    operResTemp.setOperPkid(deptOperShowUnit.getPkid());
                    operResTemp.setFlowStatus(taskFunctionSeled);
                    operResTemp.setArchivedFlag(ESEnumDeletedFlag.DELETED_FLAG0.getCode());
                    operResService.insertRecord(operResTemp);
                }
                MessageUtil.addInfo("权限添加成功!");
            }
            initRes();
            initOper();
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
            MessageUtil.addError("请输入名称！");
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

    public TreeNode getOperRoot() {
        return operRoot;
    }

    public void setOperRoot(TreeNode operRoot) {
        this.operRoot = operRoot;
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
}
