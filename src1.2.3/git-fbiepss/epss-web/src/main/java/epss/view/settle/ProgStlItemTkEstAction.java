package epss.view.settle;

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import epss.common.enums.EnumFlowStatusReason;
import epss.repository.model.model_show.ProgStlInfoShow;
import epss.repository.model.model_show.ReportHeader;
import epss.repository.model.model_show.ProgStlItemTkEstShow;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.flow.EsCommon;
import epss.view.flow.EsFlowControl;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import skyline.util.JxlsManager;
import java.text.SimpleDateFormat;
import java.io.IOException;
import jxl.write.WriteException;
/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgStlItemTkEstAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgStlItemTkEstAction.class);
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esFlowControl}")
    private EsFlowControl esFlowControl;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemTkEstService}")
    private ProgStlItemTkEstService progStlItemTkEstService;
    private List<ProgStlItemTkEstShow> progStlItemTkEstShowList;
    private ProgStlItemTkEstShow progStlItemTkEstShowSel;
    private ProgStlItemTkEstShow progStlItemTkEstShowUpd;
    private ProgStlItemTkEstShow progStlItemTkEstShowDel;

    private BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB;
    private BigDecimal bDEng_CurrentPeriodEQtyInDB;

    private ReportHeader reportHeader;

    /*所属号*/
    private String strStlInfoPkid;
    private String strTkcttPkid;
    private ProgStlInfo progStlInfo;

    private ProgStlInfoShow progStlInfoShow;

    private String strSubmitType;
    private String strPassVisible;
    private String strPassFailVisible;
    private String strFlowType;
    private String strNotPassToStatus;

    private Map beansMap;
    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    private List<ProgStlItemTkEstShow> progStlItemTkEstShowListForExcel;

    // 流程备注内容
    private String strFlowStatusRemark;

    @PostConstruct
    public void init() {
        try {
            beansMap = new HashMap();
            reportHeader =new ReportHeader();
            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            if(parammap.containsKey("strFlowType")){
                strFlowType=parammap.get("strFlowType").toString();
            }
            if(parammap.containsKey("strStlInfoPkid")){
                strStlInfoPkid=parammap.get("strStlInfoPkid").toString();
                this.progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
                strTkcttPkid= this.progStlInfo.getStlPkid();

                strPassVisible = "true";
                strPassFailVisible = "true";
                if ("Mng".equals(strFlowType)) {
                    if (EnumFlowStatus.FLOW_STATUS0.getCode().equals(progStlInfo.getFlowStatus())){
                        strPassVisible = "false";
                    }else {
                        strPassFailVisible = "false";
                    }
                }else {
                    if (("Check".equals(strFlowType)&&EnumFlowStatus.FLOW_STATUS1.getCode().equals(progStlInfo.getFlowStatus()))
                            ||("DoubleCheck".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS2.getCode().equals(progStlInfo.getFlowStatus()))
                            ||("Approve".equals(strFlowType) && EnumFlowStatus.FLOW_STATUS3.getCode().equals(progStlInfo.getFlowStatus()))){
                        strPassVisible = "false";
                    }
                }
                resetAction();
                initData();
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    /*初始化操作*/
    private void initData() {
        try{
             /*分包合同数据*/
            // From StlPkid To SubcttPkid
            ProgStlInfo progStlInfo = progStlInfoService.getProgStlInfoByPkid(strStlInfoPkid);
            reportHeader.setStrSubcttPkid(progStlInfo.getStlPkid());
            reportHeader.setStrStlId(progStlInfo.getId());
            // From SubcttPkid To CstplPkid
            CttInfo cttInfoTemp = cttInfoService.getCttInfoByPkId(reportHeader.getStrSubcttPkid());
            reportHeader.setStrCstplPkid(cttInfoTemp.getParentPkid());
            reportHeader.setStrSubcttId(cttInfoTemp.getId());
            reportHeader.setStrSubcttName(cttInfoTemp.getName());
            reportHeader.setStrSignPartPkid(cttInfoTemp.getSignPartB());
            SignPart signPartTemp=signPartService.getEsInitCustByPkid(
                    reportHeader.getStrSignPartPkid());
            if(signPartTemp!=null) {
                reportHeader.setStrSignPartName(signPartTemp.getName());
            }

            beansMap.put("reportHeader", reportHeader);

            progStlInfoShow =progStlInfoService.fromModelToModelShow(progStlInfo);
            progStlInfoShow.setStlId(cttInfoTemp.getId());
            progStlInfoShow.setStlName(cttInfoTemp.getName());
            progStlInfoShow.setSignPartBName(reportHeader.getStrSignPartName());

            /*分包合同*/
            List<CttItem> cttItemList =new ArrayList<>();
            progStlItemTkEstShowListForExcel =new ArrayList<>();
            cttItemList = cttItemService.getEsItemList(
                    EnumResType.RES_TYPE0.getCode(), strTkcttPkid);
            if(cttItemList.size()<=0){
                return;
            }
            progStlItemTkEstShowList =new ArrayList<>();
            recursiveDataTable("root", cttItemList, progStlItemTkEstShowList);
            progStlItemTkEstShowList =getItemStlTkcttEngSMList_DoFromatNo(progStlItemTkEstShowList);
            setItemOfEsItemHieRelapList_AddTotal();
            // Excel报表形成
            progStlItemTkEstShowListForExcel =new ArrayList<>();
            for(ProgStlItemTkEstShow itemUnit: progStlItemTkEstShowList){
                // 分包合同
                itemUnit.setTkctt_ContractUnitPrice(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getTkctt_ContractUnitPrice()));
                itemUnit.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getTkctt_ContractQuantity()));
                itemUnit.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getTkctt_ContractAmount()));
                // 总包进度工程量统计结算
                itemUnit.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
                itemUnit.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));

                ProgStlItemTkEstShow itemUnitTemp= null;
                itemUnitTemp = (ProgStlItemTkEstShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getTkctt_StrNo()));
                progStlItemTkEstShowListForExcel.add(itemUnitTemp);
            }
            beansMap.put("progStlItemTkEstShowListForExcel", progStlItemTkEstShowListForExcel);
            // 表内容设定
            if(progStlItemTkEstShowList.size()>0){
                strExportToExcelRendered="true";
            }else{
                strExportToExcelRendered="false";
            }
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }
    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<CttItem> cttItemListPara,
                                      List<ProgStlItemTkEstShow> sprogStlItemTkEstShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        BigDecimal bdContractUnitPrice=new BigDecimal(0);
        BigDecimal bdCurrentPeriodQty=new BigDecimal(0);
        BigDecimal bdBeginToCurrentPeriodQty=new BigDecimal(0);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemTkEstShow progStlItemTkEstShowTemp = new ProgStlItemTkEstShow();
            progStlItemTkEstShowTemp.setTkctt_Pkid(itemUnit.getPkid());
            progStlItemTkEstShowTemp.setTkctt_BelongToType(itemUnit.getBelongToType());
            progStlItemTkEstShowTemp.setTkctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemTkEstShowTemp.setTkctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemTkEstShowTemp.setTkctt_Grade(itemUnit.getGrade());
            progStlItemTkEstShowTemp.setTkctt_Orderid(itemUnit.getOrderid());
            progStlItemTkEstShowTemp.setTkctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemTkEstShowTemp.setTkctt_Name(itemUnit.getName());
            progStlItemTkEstShowTemp.setTkctt_Remark(itemUnit.getRemark());
            progStlItemTkEstShowTemp.setTkctt_Unit(itemUnit.getUnit());
            progStlItemTkEstShowTemp.setTkctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            bdContractUnitPrice=ToolUtil.getBdIgnoreNull(itemUnit.getContractUnitPrice());
            progStlItemTkEstShowTemp.setTkctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemTkEstShowTemp.setTkctt_ContractAmount(itemUnit.getContractAmount());

            ProgStlItemTkEst progStlItemTkEst =new ProgStlItemTkEst();
            progStlItemTkEst.setTkcttPkid(strTkcttPkid);
            progStlItemTkEst.setTkcttItemPkid(progStlItemTkEstShowTemp.getTkctt_Pkid());
            progStlItemTkEst.setPeriodNo(progStlInfo.getPeriodNo());
            List<ProgStlItemTkEst> progStlItemTkEstList =
                    progStlItemTkEstService.selectRecordsByExample(progStlItemTkEst);
            if(progStlItemTkEstList.size()>0){
                progStlItemTkEst = progStlItemTkEstList.get(0);
                String strCreatedByName= ToolUtil.getUserName(progStlItemTkEst.getCreatedBy());
                String strLastUpdByName= ToolUtil.getUserName(progStlItemTkEst.getLastUpdBy());
                progStlItemTkEstShowTemp.setEng_Pkid(progStlItemTkEst.getPkid());
                progStlItemTkEstShowTemp.setEng_PeriodNo(progStlItemTkEst.getPeriodNo());
                progStlItemTkEstShowTemp.setEng_TkcttPkid(progStlItemTkEst.getTkcttPkid());
                progStlItemTkEstShowTemp.setEng_TkcttItemPkid(progStlItemTkEst.getTkcttItemPkid());
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEQty(progStlItemTkEst.getCurrentPeriodQty());
                bdCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progStlItemTkEst.getCurrentPeriodQty());
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdCurrentPeriodQty)));

                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEQty(progStlItemTkEst.getBeginToCurrentPeriodQty());
                bdBeginToCurrentPeriodQty=ToolUtil.getBdIgnoreNull(progStlItemTkEst.getBeginToCurrentPeriodQty());
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdContractUnitPrice.multiply(bdBeginToCurrentPeriodQty)));

                progStlItemTkEstShowTemp.setEng_ArchivedFlag(progStlItemTkEst.getArchivedFlag());
                progStlItemTkEstShowTemp.setEng_CreatedBy(progStlItemTkEst.getCreatedBy());
                progStlItemTkEstShowTemp.setEng_CreatedByName(strCreatedByName);
                progStlItemTkEstShowTemp.setEng_CreatedTime(progStlItemTkEst.getCreatedTime());
                progStlItemTkEstShowTemp.setEng_LastUpdBy(progStlItemTkEst.getLastUpdBy());
                progStlItemTkEstShowTemp.setEng_LastUpdByName(strLastUpdByName);
                progStlItemTkEstShowTemp.setEng_LastUpdTime(progStlItemTkEst.getLastUpdTime());
                progStlItemTkEstShowTemp.setEng_RecVersion(progStlItemTkEst.getRecVersion());
                if (ToolUtil.getBdIgnoreNull(progStlItemTkEstShowTemp.getEng_BeginToCurrentPeriodEQty())
                        .compareTo(progStlItemTkEstShowTemp.getTkctt_ContractQuantity())==0){
                    progStlItemTkEstShowTemp.setIsUptoCttQtyFlag(true);
                }
            }
            sprogStlItemTkEstShowListPara.add(progStlItemTkEstShowTemp) ;
            recursiveDataTable(progStlItemTkEstShowTemp.getTkctt_Pkid(), cttItemListPara, sprogStlItemTkEstShowListPara);
        }
    }

    /*重置*/
    public void resetAction(){
        progStlItemTkEstShowSel =new ProgStlItemTkEstShow();
        progStlItemTkEstShowUpd =new ProgStlItemTkEstShow();
        progStlItemTkEstShowDel =new ProgStlItemTkEstShow();
        strSubmitType="Add";
    }

    public void submitThisRecordAction(){
        try{
            if(strSubmitType.equals("Upd")){
                if(!blurEng_CurrentPeriodEQty("submit")){
                    return;
                }
                progStlItemTkEstShowUpd.setEng_PeriodNo(progStlInfo.getPeriodNo());
                List<ProgStlItemTkEst> progStlItemTkEstListTemp =
                        progStlItemTkEstService.isExistInDb(progStlItemTkEstShowUpd);
                if (progStlItemTkEstListTemp.size() > 1) {
                    MessageUtil.addInfo("数据有误，数据库中存在多条记录。");
                    return;
                }
                if (progStlItemTkEstListTemp.size() == 1) {
                    progStlItemTkEstShowUpd.setEng_Pkid(progStlItemTkEstListTemp.get(0).getPkid());
                    progStlItemTkEstService.updateRecord(progStlItemTkEstShowUpd);
                }
                if (progStlItemTkEstListTemp.size()==0){
				    progStlItemTkEstShowUpd.setEng_TkcttPkid(strTkcttPkid);
                    progStlItemTkEstShowUpd.setEng_TkcttItemPkid(progStlItemTkEstShowUpd.getTkctt_Pkid());
                    progStlItemTkEstService.insertRecord(progStlItemTkEstShowUpd);
                }
                MessageUtil.addInfo("更新数据完成。");
            }
            else if(strSubmitType.equals("Del")){
                delRecordAction(progStlItemTkEstShowDel);
            }
            initData();
        }
        catch (Exception e){
            logger.error("提交数据失败，",e);
            MessageUtil.addError("提交数据失败，" + e.getMessage());
        }
    }

    public boolean blurEng_CurrentPeriodEQty(String strBlurOrSubmitFlag){
        if(ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getTkctt_ContractQuantity()).equals( new BigDecimal(0))){
            return true;
        }
        else{
            String strTemp= progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty().toString();
            //String strRegex = "[0-9]+\\.?[0-9]*";
            String strRegex = "[-]?[0-9]+\\.?[0-9]*";
            if (!progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty().toString().matches(strRegex) ){
                MessageUtil.addError("请确认输入的数据，" + strTemp + "不是正确的数据格式！");
                return false;
            }

            BigDecimal bDEng_CurrentPeriodEQtyTemp=
                    ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty());

            BigDecimal bigDecimalTemp=
                    bDEng_BeginToCurrentPeriodEQtyInDB.add(bDEng_CurrentPeriodEQtyTemp).subtract(bDEng_CurrentPeriodEQtyInDB);

            BigDecimal bDTkctt_ContractQuantity=
                    ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getTkctt_ContractQuantity());

            if(strBlurOrSubmitFlag.equals("blur")) {
                if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                    MessageUtil.addError("上期开累工程数量+本期工程数量>合同数量，请确认您输入的本期工程数量（"
                            + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                    return false;
                }
                progStlItemTkEstShowUpd.setEng_BeginToCurrentPeriodEQty(bigDecimalTemp);
            }else if (strBlurOrSubmitFlag.equals("submit")) {
                BigDecimal bDEngQMng_BeginToCurrentPeriodEQtyTemp=
                        ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_BeginToCurrentPeriodEQty());

                if(bDEngQMng_BeginToCurrentPeriodEQtyTemp.compareTo(bDEng_BeginToCurrentPeriodEQtyInDB)==0){
                    if(bigDecimalTemp.compareTo(bDTkctt_ContractQuantity)>0){
                        MessageUtil.addError("上期材料累计供应数量+本期材料供应数量>合同数量，请确认您输入的本期材料供应数量（"
                                + bDEng_CurrentPeriodEQtyTemp.toString() + "）！");
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }

    private void delRecordAction(ProgStlItemTkEstShow progStlItemTkEstShowPara){
        try {
            if(progStlItemTkEstShowPara.getEng_Pkid()==null||
                    progStlItemTkEstShowPara.getEng_Pkid().equals("")){
                MessageUtil.addError("无可删除的数据！") ;
            }else{
                int deleteRecordNum= progStlItemTkEstService.deleteRecord(progStlItemTkEstShowPara.getEng_Pkid()) ;
                if (deleteRecordNum<=0){
                    MessageUtil.addInfo("该记录已删除。");
                }else {
                    MessageUtil.addInfo("删除数据完成。");
                }
            }
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*右单击事件*/
    public void selectRecordAction(String strSubmitTypePara,ProgStlItemTkEstShow progStlItemTkEstShowPara){
        try {
            strSubmitType=strSubmitTypePara;
            if (strSubmitTypePara.equals("Sel")){                
                progStlItemTkEstShowSel =(ProgStlItemTkEstShow)BeanUtils.cloneBean(progStlItemTkEstShowPara) ;
                progStlItemTkEstShowSel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkEstShowSel.getTkctt_StrNo()));
            }            
            String strTkctt_Unit= progStlItemTkEstShowPara.getTkctt_Unit();
            if(strTkctt_Unit==null) {
                if(strSubmitTypePara.equals("Upd")){
                    MessageUtil.addInfo("该数据不是项数据，无法更新");
                }
                else if(strSubmitTypePara.equals("Del")){
                    MessageUtil.addInfo("该数据不是项数据，无法删除");
                }
                resetAction();
                return;
            }
            if(strSubmitTypePara.equals("Upd")){
                progStlItemTkEstShowUpd =(ProgStlItemTkEstShow) BeanUtils.cloneBean(progStlItemTkEstShowPara) ;
                progStlItemTkEstShowUpd.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkEstShowUpd.getTkctt_StrNo()));
                bDEng_CurrentPeriodEQtyInDB=ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_CurrentPeriodEQty());
                bDEng_BeginToCurrentPeriodEQtyInDB=
                        ToolUtil.getBdIgnoreNull(progStlItemTkEstShowUpd.getEng_BeginToCurrentPeriodEQty());
            }
            else if(strSubmitTypePara.equals("Del")){
                progStlItemTkEstShowDel =(ProgStlItemTkEstShow) BeanUtils.cloneBean(progStlItemTkEstShowPara) ;
                progStlItemTkEstShowDel.setTkctt_StrNo(ToolUtil.getIgnoreSpaceOfStr(progStlItemTkEstShowDel.getTkctt_StrNo()));
            }
        } catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void setItemOfEsItemHieRelapList_AddTotal(){
        List<ProgStlItemTkEstShow> progStlItemTkEstShowListTemp =new ArrayList<ProgStlItemTkEstShow>();
        progStlItemTkEstShowListTemp.addAll(progStlItemTkEstShowList);

        progStlItemTkEstShowList.clear();
        // 合同数量小计
        BigDecimal bdQuantityTotal=new BigDecimal(0);
        // 合同数量大计
        BigDecimal bdQuantityAllTotal=new BigDecimal(0);
        // 合同金额小计
        BigDecimal bdAmountTotal=new BigDecimal(0);
        // 合同金额大计
        BigDecimal bdAmountAllTotal=new BigDecimal(0);

        // 开累数量小计
        BigDecimal bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
        // 开累数量大计
        BigDecimal bdBeginToCurrentPeriodEQtyAllTotal=new BigDecimal(0);
        // 当期数量小计
        BigDecimal bdCurrentPeriodEQtyTotal=new BigDecimal(0);
        // 当期数量大计
        BigDecimal bdCurrentPeriodEQtyAllTotal=new BigDecimal(0);

        // 开累金额小计
        BigDecimal bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
        // 开累金额大计
        BigDecimal bdBeginToCurrentPeriodEAmountAllTotal=new BigDecimal(0);
        // 当期金额小计
        BigDecimal bdCurrentPeriodEAmountTotal=new BigDecimal(0);
        // 当期金额大计
        BigDecimal bdCurrentPeriodEAmountAllTotal=new BigDecimal(0);

        ProgStlItemTkEstShow itemUnit=new ProgStlItemTkEstShow();
        ProgStlItemTkEstShow itemUnitNext=new ProgStlItemTkEstShow();

        for(int i=0;i< progStlItemTkEstShowListTemp.size();i++){
            itemUnit = progStlItemTkEstShowListTemp.get(i);
            bdQuantityTotal=bdQuantityTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractQuantity()));
            bdQuantityAllTotal=bdQuantityAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractQuantity()));
            bdAmountTotal=bdAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractAmount()));
            bdAmountAllTotal=bdAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getTkctt_ContractAmount()));

            bdBeginToCurrentPeriodEQtyTotal=
                    bdBeginToCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
            bdBeginToCurrentPeriodEQtyAllTotal=
                    bdBeginToCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEQty()));
            bdCurrentPeriodEQtyTotal=
                    bdCurrentPeriodEQtyTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEQty()));
            bdCurrentPeriodEQtyAllTotal=
                    bdCurrentPeriodEQtyAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEQty()));

            bdBeginToCurrentPeriodEAmountTotal=
                    bdBeginToCurrentPeriodEAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEAmount()));
            bdBeginToCurrentPeriodEAmountAllTotal=
                    bdBeginToCurrentPeriodEAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_BeginToCurrentPeriodEAmount()));
            bdCurrentPeriodEAmountTotal=
                    bdCurrentPeriodEAmountTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEAmount()));
            bdCurrentPeriodEAmountAllTotal=
                    bdCurrentPeriodEAmountAllTotal.add(ToolUtil.getBdIgnoreNull(itemUnit.getEng_CurrentPeriodEAmount()));

            progStlItemTkEstShowList.add(itemUnit);

            if(i+1< progStlItemTkEstShowListTemp.size()){
                itemUnitNext = progStlItemTkEstShowListTemp.get(i+1);
                if(itemUnitNext.getTkctt_ParentPkid().equals("root")){
                    ProgStlItemTkEstShow itemOfEsItemHieRelapTemp=new ProgStlItemTkEstShow();
                    itemOfEsItemHieRelapTemp.setTkctt_Name("合计");
                    itemOfEsItemHieRelapTemp.setTkctt_Pkid("total"+i);
                    itemOfEsItemHieRelapTemp.setTkctt_ContractQuantity(
                            ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                    itemOfEsItemHieRelapTemp.setTkctt_ContractAmount(
                            ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEQty(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                    itemOfEsItemHieRelapTemp.setEng_BeginToCurrentPeriodEAmount(
                            ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                    itemOfEsItemHieRelapTemp.setEng_CurrentPeriodEAmount(
                            ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                    progStlItemTkEstShowList.add(itemOfEsItemHieRelapTemp);
                    bdQuantityTotal=new BigDecimal(0);
                    bdAmountTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                    bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                    bdCurrentPeriodEAmountTotal=new BigDecimal(0);
                }
            } else if(i+1== progStlItemTkEstShowListTemp.size()){
                itemUnitNext = progStlItemTkEstShowListTemp.get(i);
                ProgStlItemTkEstShow progStlItemTkEstShowTemp =new ProgStlItemTkEstShow();
                progStlItemTkEstShowTemp.setTkctt_Name("合计");
                progStlItemTkEstShowTemp.setTkctt_Pkid("total"+i);
                progStlItemTkEstShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityTotal));
                progStlItemTkEstShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountTotal));
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyTotal));
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountTotal));
                progStlItemTkEstShowList.add(progStlItemTkEstShowTemp);
                progStlItemTkEstShowListForExcel.add(progStlItemTkEstShowTemp);
                bdQuantityTotal=new BigDecimal(0);
                bdAmountTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdCurrentPeriodEQtyTotal=new BigDecimal(0);
                bdBeginToCurrentPeriodEAmountTotal=new BigDecimal(0);
                bdCurrentPeriodEAmountTotal=new BigDecimal(0);

                // 总合计
                progStlItemTkEstShowTemp =new ProgStlItemTkEstShow();
                progStlItemTkEstShowTemp.setTkctt_Name("总合计");
                progStlItemTkEstShowTemp.setTkctt_Pkid("total_all"+i);
                progStlItemTkEstShowTemp.setTkctt_ContractQuantity(
                        ToolUtil.getBdFrom0ToNull(bdQuantityAllTotal));
                progStlItemTkEstShowTemp.setTkctt_ContractAmount(
                        ToolUtil.getBdFrom0ToNull(bdAmountAllTotal));

                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEQtyAllTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEQty(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEQtyAllTotal));
                progStlItemTkEstShowTemp.setEng_BeginToCurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdBeginToCurrentPeriodEAmountAllTotal));
                progStlItemTkEstShowTemp.setEng_CurrentPeriodEAmount(
                        ToolUtil.getBdFrom0ToNull(bdCurrentPeriodEAmountAllTotal));
                progStlItemTkEstShowList.add(progStlItemTkEstShowTemp);
                progStlItemTkEstShowListForExcel.add(progStlItemTkEstShowTemp);
            }
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgStlItemTkEstShow> getItemStlTkcttEngSMList_DoFromatNo(
            List<ProgStlItemTkEstShow> progStlItemTkEstShowListPara){
        try{
            String strTemp="";
            Integer intBeforeGrade=-1;
            for(ProgStlItemTkEstShow itemUnit: progStlItemTkEstShowListPara){
                if(itemUnit.getTkctt_Grade().equals(intBeforeGrade)){
                    if(strTemp.lastIndexOf(".")<0) {
                        strTemp=itemUnit.getTkctt_Orderid().toString();
                    }
                    else{
                        strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getTkctt_Orderid().toString();
                    }
                }
                else{
                    if(itemUnit.getTkctt_Grade()==1){
                        strTemp=itemUnit.getTkctt_Orderid().toString() ;
                    }
                    else {
                        if (!itemUnit.getTkctt_Grade().equals(intBeforeGrade)) {
                            if (itemUnit.getTkctt_Grade().compareTo(intBeforeGrade)>0) {
                                strTemp = strTemp + "." + itemUnit.getTkctt_Orderid().toString();
                            } else {
                                Integer intTemp=strTemp.indexOf(".",itemUnit.getTkctt_Grade()-1);
                                strTemp = strTemp .substring(0, intTemp);
                                strTemp = strTemp+"."+itemUnit.getTkctt_Orderid().toString();
                            }
                        }
                    }
                }
                intBeforeGrade=itemUnit.getTkctt_Grade() ;
                itemUnit.setTkctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getTkctt_Grade(), strTemp)) ;
            }
        }
        catch (Exception e) {
            logger.error("", e);
            MessageUtil.addError(e.getMessage());
        }
        return progStlItemTkEstShowListPara;
    }

    public void onExportExcel()throws IOException, WriteException {
        String excelFilename = "总包数量统计-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
        JxlsManager jxls = new JxlsManager();
        jxls.exportList(excelFilename, beansMap,"progStlItemTkEst.xls");
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsCttItemListByParentPkid(
            String strLevelParentPkid,
            List<CttItem> cttItemListPara) {
        List<CttItem> tempCttItemList =new ArrayList<CttItem>();
        /*避开重复链接数据库*/
        for(CttItem itemUnit: cttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempCttItemList.add(itemUnit);
            }
        }
        return tempCttItemList;
    }

    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    public void onClickForPowerAction(String strPowerType){
        try {
            strPowerType=strFlowType+strPowerType;
            if(strPowerType.contains("Mng")){
                if(strPowerType.equals("MngPass")){
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    // 原因：录入完毕
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON0.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据录入完成！");
                }else if(strPowerType.equals("MngFail")){
                    progStlInfo.setFlowStatus(null);
                    progStlInfo.setFlowStatusReason(null);
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据录入未完！");
                }
            }else if(strPowerType.contains("Check")&&!strPowerType.contains("DoubleCheck")){// 审核
                if(strPowerType.equals("CheckPass")){
                    // 状态标志：审核
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    // 原因：审核通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON1.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核通过！");
                }else if(strPowerType.equals("CheckFail")){
                    // 状态标志：初始
                    progStlInfo.setFlowStatus(null);
                    // 原因：审核未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON2.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据审核未过！");
                }
            }else if(strPowerType.contains("DoubleCheck")){// 复核
                if(strPowerType.equals("DoubleCheckPass")){
                    // 状态标志：复核
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS2.getCode());
                    // 原因：复核通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON3.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据复核通过！");
                }else if(strPowerType.equals("DoubleCheckFail")){
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        progStlInfo.setFlowStatus(null);
                    }

                    // 原因：复核未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON4.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据复核未过！");
                }
            } else if(strPowerType.contains("Approve")){// 批准
                if(strPowerType.equals("ApprovePass")){
                    // 状态标志：批准
                    progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
                    // 原因：批准通过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON5.getCode());
                    progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据批准通过！");
                }else if(strPowerType.equals("ApproveFail")){
                    String strTemp = progStlInfoService.progStlInfoAppFailPreCheck(
                            EnumResType.RES_TYPE6.getCode(),
                            progStlInfo.getStlPkid(),
                            progStlInfo.getPeriodNo());
                    if (!"".equals(strTemp)) {
                        MessageUtil.addError(strTemp);
                        return;
                    }else{
                    // 这样写可以实现越级退回
                    if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS2.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS1.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS1.getCode())) {
                        progStlInfo.setFlowStatus(EnumFlowStatus.FLOW_STATUS0.getCode());
                    }else if(strNotPassToStatus.equals(EnumFlowStatus.FLOW_STATUS0.getCode())) {
                        progStlInfo.setFlowStatus(null);
                    }

                    // 原因：批准未过
                    progStlInfo.setFlowStatusReason(EnumFlowStatusReason.FLOW_STATUS_REASON6.getCode());
                        progStlInfo.setFlowStatusRemark(strFlowStatusRemark);
                    progStlInfoService.updAutoLinkTask(progStlInfo);
                    MessageUtil.addInfo("数据批准未过！");
                    }
                }
            }
            strPassVisible="false";
            strPassFailVisible="false";
        } catch (Exception e) {
            logger.error("数据流程化失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /* 智能字段Start*/

    public ProgStlInfoShow getProgStlInfoShow() {
        return progStlInfoShow;
    }

    public void setProgStlInfoShow(ProgStlInfoShow progStlInfoShow) {
        this.progStlInfoShow = progStlInfoShow;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }    

    public ProgStlItemTkEstService getProgStlItemTkEstService() {
        return progStlItemTkEstService;
    }

    public void setProgStlItemTkEstService(ProgStlItemTkEstService ProgStlItemTkEstService) {
        this.progStlItemTkEstService = ProgStlItemTkEstService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public EsFlowControl getEsFlowControl() {
        return esFlowControl;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public void setEsFlowControl(EsFlowControl esFlowControl) {
        this.esFlowControl = esFlowControl;
    }

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public BigDecimal getbDEng_BeginToCurrentPeriodEQtyInDB() {
        return bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public void setbDEng_BeginToCurrentPeriodEQtyInDB(BigDecimal bDEng_BeginToCurrentPeriodEQtyInDB) {
        this.bDEng_BeginToCurrentPeriodEQtyInDB = bDEng_BeginToCurrentPeriodEQtyInDB;
    }

    public ProgStlItemTkEstShow getProgStlItemTkEstShowSel() {
        return progStlItemTkEstShowSel;
    }

    public void setProgStlItemTkEstShowSel(ProgStlItemTkEstShow progStlItemTkEstShowSel) {
        this.progStlItemTkEstShowSel = progStlItemTkEstShowSel;
    }

    public ProgStlItemTkEstShow getProgStlItemTkEstShowDel() {
        return progStlItemTkEstShowDel;
    }

    public void setProgStlItemTkEstShowDel(ProgStlItemTkEstShow progStlItemTkEstShowDel) {
        this.progStlItemTkEstShowDel = progStlItemTkEstShowDel;
    }

    public ProgStlItemTkEstShow getProgStlItemTkEstShowUpd() {
        return progStlItemTkEstShowUpd;
    }

    public void setProgStlItemTkEstShowUpd(ProgStlItemTkEstShow progStlItemTkEstShowUpd) {
        this.progStlItemTkEstShowUpd = progStlItemTkEstShowUpd;
    }

    public List<ProgStlItemTkEstShow> getProgStlItemTkEstShowList() {
        return progStlItemTkEstShowList;
    }

    public void setProgStlItemTkEstShowList(List<ProgStlItemTkEstShow> progStlItemTkEstShowList) {
        this.progStlItemTkEstShowList = progStlItemTkEstShowList;
    }

    public List<ProgStlItemTkEstShow> getProgStlItemTkEstShowListForExcel() {
        return progStlItemTkEstShowListForExcel;
    }

    public void setProgStlItemTkEstShowListForExcel(List<ProgStlItemTkEstShow> progStlItemTkEstShowListForExcel) {
        this.progStlItemTkEstShowListForExcel = progStlItemTkEstShowListForExcel;
    }

    public String getStrFlowType() {
        return strFlowType;
    }

    public void setStrFlowType(String strFlowType) {
        this.strFlowType = strFlowType;
    }

    public String getStrNotPassToStatus() {
        return strNotPassToStatus;
    }

    public void setStrNotPassToStatus(String strNotPassToStatus) {
        this.strNotPassToStatus = strNotPassToStatus;
    }

    public String getStrPassVisible() {
        return strPassVisible;
    }

    public String getStrPassFailVisible() {
        return strPassFailVisible;
    }

    public String getStrFlowStatusRemark() {
        return strFlowStatusRemark;
    }

    public void setStrFlowStatusRemark(String strFlowStatusRemark) {
        this.strFlowStatusRemark = strFlowStatusRemark;
    }
    /*智能字段End*/
}
