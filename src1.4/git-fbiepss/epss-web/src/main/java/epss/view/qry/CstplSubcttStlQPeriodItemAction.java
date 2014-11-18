package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.EnumResType;
import epss.common.enums.EnumFlowStatus;
import skyline.util.JxlsManager;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.EsQueryService;
import epss.view.flow.EsCommon;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class CstplSubcttStlQPeriodItemAction {
    private static final Logger logger = LoggerFactory.getLogger(CstplSubcttStlQPeriodItemAction.class);
    @ManagedProperty(value = "#{cttItemService}")
    private CttItemService cttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{cttInfoService}")
    private CttInfoService cttInfoService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progStlItemSubQService}")
    private ProgStlItemSubQService progStlItemSubQService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;

    private List<CttItem> cttItemList;

    /*列表显示用*/
    private List<ProgStlItemSubQShow> progStlItemSubQShowList;
    private List<QryCSStlQPeriodShow> qryCSStlQPeriodShowList;
    private List<QryCSStlQPeriodShow> qryCSStlQPeriodShowListForExcel;
    private List<CommCol> commColSetList;
    private String columnHeaderList[]=new String[24];

    private List<SelectItem> subcttList;

    private String strCstplPkid;
    private String strSubcttPkid;
    private String strStartPeriodNo;
    private String strEndPeriodNo;

    private String strLatestApprovedPeriodNo;

    // 画面上控件的显示控制
    private ReportHeader reportHeader;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        try {
            beansMap = new HashMap();
            reportHeader =new ReportHeader();
            commColSetList =new ArrayList<CommCol>();
            for(int i=0;i<24;i++){
                CommCol commCol =new CommCol();
                commCol.setHeader("");
                commCol.setRendered_flag("false");
                commColSetList.add(commCol);
            }

            Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            // 从成本计划传递过来的成本计划号
            if (parammap.containsKey("strCttInfoPkid")){
                strCstplPkid=parammap.get("strCttInfoPkid").toString();
            }

            List<CttInfoShow> cttInfoShowList =
                    cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                            EnumResType.RES_TYPE2.getCode()
                            , strCstplPkid
                            , EnumFlowStatus.FLOW_STATUS3.getCode());
            subcttList=new ArrayList<SelectItem>();
            if(cttInfoShowList.size()>0){
                SelectItem selectItem=new SelectItem("","");
                subcttList.add(selectItem);
                for(CttInfoShow itemUnit: cttInfoShowList){
                    selectItem=new SelectItem();
                    selectItem.setValue(itemUnit.getPkid());
                    selectItem.setLabel(itemUnit.getName());
                    subcttList.add(selectItem);
                }
            }
            strStartPeriodNo=ToolUtil.getStrThisMonth();
            strStartPeriodNo=(new BigDecimal(strStartPeriodNo.substring(0,4)).subtract(new BigDecimal("2"))).toString()
                    +strStartPeriodNo.substring(4);
            strEndPeriodNo=ToolUtil.getStrThisMonth();
        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public String onExportExcel()throws IOException,WriteException {
        if (this.progStlItemSubQShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "分包合同阶段工程量结算-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCSStlQPeriod.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }
    private void initData(String strBelongToPkid) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        CttInfo cttInfo_Subctt = cttInfoService.getCttInfoByPkId(strBelongToPkid);
        reportHeader.setStrCstplPkid(cttInfo_Subctt.getParentPkid());
        reportHeader.setStrSubcttId(cttInfo_Subctt.getId());
        reportHeader.setStrSubcttName(cttInfo_Subctt.getName());
        reportHeader.setStrSignPartPkid(cttInfo_Subctt.getSignPartB());
        reportHeader.setStrSignPartName(signPartService.getEsInitCustByPkid(
                reportHeader.getStrSignPartPkid()).getName());
        beansMap.put("reportHeader", reportHeader);

        strLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                progStlInfoService.getLatestApprovedPeriodNoByEndPeriod(
                EnumResType.RES_TYPE3.getCode(),
                strSubcttPkid,
                strEndPeriodNo));

        /*分包合同*/
        cttItemList =new ArrayList<CttItem>();

        cttItemList = cttItemService.getEsItemList(
                EnumResType.RES_TYPE2.getCode(), strSubcttPkid);
        if(cttItemList.size()<=0){
            return;
        }
        progStlItemSubQShowList =new ArrayList<ProgStlItemSubQShow>();
        recursiveDataTable("root", cttItemList, progStlItemSubQShowList);
        progStlItemSubQShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progStlItemSubQShowList);

        List<String> periodList = progStlItemSubQService.getProgWorkqtyItemPeriodsByPeriod(
                                 strSubcttPkid,
                                 strStartPeriodNo,
                                 strEndPeriodNo);

        for(int i=0;i<periodList.size();i++){
            commColSetList.get(i).setHeader(periodList.get(i));
            commColSetList.get(i).setRendered_flag("true");
            columnHeaderList[i]=periodList.get(i);
        }
        for(int i=periodList.size();i<24;i++){
            commColSetList.get(i).setHeader("");
            commColSetList.get(i).setRendered_flag("false");
            columnHeaderList[i]="";
        }

        List<ProgStlItemSubQ> progStlItemSubQList =
                progStlItemSubQService.getProgWorkqtyItemListByPeriod(
                        strSubcttPkid,
                        strStartPeriodNo,
                        strEndPeriodNo);

        qryCSStlQPeriodShowList =new ArrayList<>();

        for(ProgStlItemSubQShow itemUnit : progStlItemSubQShowList){
            QryCSStlQPeriodShow qryCSStlQPeriodShow =new QryCSStlQPeriodShow();
            qryCSStlQPeriodShow.setStrPkid(itemUnit.getSubctt_Pkid());
            qryCSStlQPeriodShow.setStrNo(itemUnit.getSubctt_StrNo());
            qryCSStlQPeriodShow.setStrName(itemUnit.getSubctt_Name());
            qryCSStlQPeriodShow.setStrUnit(itemUnit.getSubctt_Unit());
            qryCSStlQPeriodShow.setBdBeginToCurrentPeriodEQty(itemUnit.getEngQMng_BeginToCurrentPeriodEQty());
            qryCSStlQPeriodShow.setBdContractQuantity(itemUnit.getSubctt_ContractQuantity());
            qryCSStlQPeriodShow.setBdSignPartAPrice(itemUnit.getSubctt_SignPartAPrice());

            String strItemPkid=ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_Pkid());

            BeanInfo beanInfo = Introspector.getBeanInfo(QryCSStlQPeriodShow.class, Object.class);
            PropertyDescriptor[] pDescriptors = beanInfo.getPropertyDescriptors();
            for(int j=0;j< commColSetList.size();j++){
                for (PropertyDescriptor propertyDescriptor : pDescriptors) {
                    String proName = propertyDescriptor.getName();
                    if (proName.equals("bdCurrentPeriodEQty"+j)) {//这个地方的id可以是已知属性值的那些属性名
                        for(ProgStlItemSubQ progStlItemSubQ : progStlItemSubQList){
                            if(strItemPkid.equals(progStlItemSubQ.getSubcttItemPkid())){
                                if(commColSetList.get(j).getHeader().equals(progStlItemSubQ.getPeriodNo())){
                                    propertyDescriptor.getWriteMethod().invoke(qryCSStlQPeriodShow, progStlItemSubQ.getCurrentPeriodEQty());
                                }
                            }
                        }
                    }
                }
            }
            qryCSStlQPeriodShowList.add(qryCSStlQPeriodShow);
        }

        /*拼装列表*/
        try {
            qryCSStlQPeriodShowListForExcel =new ArrayList<QryCSStlQPeriodShow>();
            for(QryCSStlQPeriodShow itemUnit: qryCSStlQPeriodShowList){
                QryCSStlQPeriodShow itemUnitTemp= (QryCSStlQPeriodShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                qryCSStlQPeriodShowListForExcel.add(itemUnitTemp);
            }

        if(qryCSStlQPeriodShowListForExcel.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("columnHeaderList", columnHeaderList);
        beansMap.put("columnSetList", commColSetList);
        beansMap.put("qryCSStlQPeriodShowListForExcel", qryCSStlQPeriodShowListForExcel);
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<CttItem> cttItemListPara,
                                    List<ProgStlItemSubQShow> sProgStlItemSubQShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<CttItem> subCttItemList =new ArrayList<CttItem>();
        // 通过父层id查找它的孩子
        subCttItemList =getEsCttItemListByParentPkid(strLevelParentId, cttItemListPara);
        for(CttItem itemUnit: subCttItemList){
            ProgStlItemSubQShow progStlItemSubQShowTemp = new ProgStlItemSubQShow();
            progStlItemSubQShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progStlItemSubQShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progStlItemSubQShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progStlItemSubQShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progStlItemSubQShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progStlItemSubQShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progStlItemSubQShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progStlItemSubQShowTemp.setSubctt_Name(itemUnit.getName());
            progStlItemSubQShowTemp.setSubctt_Remark(itemUnit.getRemark());
            progStlItemSubQShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progStlItemSubQShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progStlItemSubQShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progStlItemSubQShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progStlItemSubQShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            if(ToolUtil.getStrIgnoreNull(strLatestApprovedPeriodNo).length()!=0){
                ProgStlItemSubQ progStlItemSubQ =new ProgStlItemSubQ();
                progStlItemSubQ.setSubcttPkid(strSubcttPkid);
                progStlItemSubQ.setSubcttItemPkid(itemUnit.getPkid());
                progStlItemSubQ.setPeriodNo(strLatestApprovedPeriodNo);
                List<ProgStlItemSubQ> progStlItemSubQList =
                        progStlItemSubQService.selectRecordsByExample(progStlItemSubQ);
                if(progStlItemSubQList.size()>0){
                    progStlItemSubQ = progStlItemSubQList.get(0);
                    progStlItemSubQShowTemp.setEngQMng_Pkid(progStlItemSubQ.getPkid());
                    progStlItemSubQShowTemp.setEngQMng_PeriodNo(progStlItemSubQ.getPeriodNo());
                    progStlItemSubQShowTemp.setEngQMng_SubcttPkid(progStlItemSubQ.getSubcttPkid());
                    progStlItemSubQShowTemp.setEngQMng_BeginToCurrentPeriodEQty(progStlItemSubQ.getBeginToCurrentPeriodEQty());
                    progStlItemSubQShowTemp.setEngQMng_CurrentPeriodEQty(progStlItemSubQ.getCurrentPeriodEQty());
                    progStlItemSubQShowTemp.setEngQMng_ArchivedFlag(progStlItemSubQ.getArchivedFlag());
                    progStlItemSubQShowTemp.setEngQMng_CreatedBy(progStlItemSubQ.getCreatedBy());
                    progStlItemSubQShowTemp.setEngQMng_CreatedTime(progStlItemSubQ.getCreatedTime());
                    progStlItemSubQShowTemp.setEngQMng_LastUpdBy(progStlItemSubQ.getLastUpdBy());
                    progStlItemSubQShowTemp.setEngQMng_LastUpdTime(progStlItemSubQ.getLastUpdTime());
                    progStlItemSubQShowTemp.setEngQMng_RecVersion(progStlItemSubQ.getRecVersion());
                }
            }
            sProgStlItemSubQShowListPara.add(progStlItemSubQShowTemp) ;
            recursiveDataTable(progStlItemSubQShowTemp.getSubctt_Pkid(), cttItemListPara, sProgStlItemSubQShowListPara);
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgStlItemSubQShow> getStlSubCttEngQMngConstructList_DoFromatNo(
            List<ProgStlItemSubQShow> progStlItemSubQShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgStlItemSubQShow itemUnit: progStlItemSubQShowListPara){
            if(itemUnit.getSubctt_Grade().equals(intBeforeGrade)){
                if(strTemp.lastIndexOf(".")<0) {
                    strTemp=itemUnit.getSubctt_Orderid().toString();
                }
                else{
                    strTemp=strTemp.substring(0,strTemp.lastIndexOf(".")) +"."+itemUnit.getSubctt_Orderid().toString();
                }
            }
            else{
                if(itemUnit.getSubctt_Grade()==1){
                    strTemp=itemUnit.getSubctt_Orderid().toString() ;
                }
                else {
                    if (!itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getSubctt_Grade().compareTo(intBeforeGrade)>0) {
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        } else {
                            Integer intTemp=strTemp.indexOf(".",itemUnit.getSubctt_Grade()-1);
                            strTemp = strTemp .substring(0, intTemp);
                            strTemp = strTemp+"."+itemUnit.getSubctt_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade=itemUnit.getSubctt_Grade() ;
            itemUnit.setSubctt_StrNo(ToolUtil.padLeft_DoLevel(itemUnit.getSubctt_Grade(), strTemp)) ;
        }
        return progStlItemSubQShowListPara;
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<CttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
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

    public void onQueryAction() {
        try {
            if(ToolUtil.getStrIgnoreNull(strSubcttPkid).equals("")){
                MessageUtil.addWarn("请选择分包合同项。");
                return;
            }
            if(!blurPeriodNo()){
                return;
            }
            initData(strSubcttPkid);
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }
    }

    public boolean blurPeriodNo(){
        if(strEndPeriodNo.compareTo(strStartPeriodNo)<0){
            MessageUtil.addError("开始期(" + strStartPeriodNo + "）大于结束期（" + strEndPeriodNo + ")，请确认输入的期码！");
            return false;
        }
        if(ToolUtil.getDateByStr(strStartPeriodNo,strEndPeriodNo)>24){
            MessageUtil.addError("一次查询只可在12个月("+strStartPeriodNo+"-"+strEndPeriodNo+")范围内!");
            return false;
        }
        return true;
    }

    /*智能字段Start*/

    public CttItemService getCttItemService() {
        return cttItemService;
    }

    public void setCttItemService(CttItemService cttItemService) {
        this.cttItemService = cttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public CttInfoService getCttInfoService() {
        return cttInfoService;
    }

    public void setCttInfoService(CttInfoService cttInfoService) {
        this.cttInfoService = cttInfoService;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public ProgStlInfoService getProgStlInfoService() {
        return progStlInfoService;
    }

    public void setProgStlInfoService(ProgStlInfoService progStlInfoService) {
        this.progStlInfoService = progStlInfoService;
    }

    public ProgStlItemSubQService getProgStlItemSubQService() {
        return progStlItemSubQService;
    }

    public void setProgStlItemSubQService(ProgStlItemSubQService progStlItemSubQService) {
        this.progStlItemSubQService = progStlItemSubQService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public List<CttItem> getCttItemList() {
        return cttItemList;
    }

    public void setCttItemList(List<CttItem> cttItemList) {
        this.cttItemList = cttItemList;
    }

    public List<ProgStlItemSubQShow> getProgStlItemSubQShowList() {
        return progStlItemSubQShowList;
    }

    public void setProgStlItemSubQShowList(List<ProgStlItemSubQShow> progStlItemSubQShowList) {
        this.progStlItemSubQShowList = progStlItemSubQShowList;
    }

    public List<QryCSStlQPeriodShow> getQryCSStlQPeriodShowList() {
        return qryCSStlQPeriodShowList;
    }

    public void setQryCSStlQPeriodShowList(List<QryCSStlQPeriodShow> qryCSStlQPeriodShowList) {
        this.qryCSStlQPeriodShowList = qryCSStlQPeriodShowList;
    }

    public List<QryCSStlQPeriodShow> getQryCSStlQPeriodShowListForExcel() {
        return qryCSStlQPeriodShowListForExcel;
    }

    public void setQryCSStlQPeriodShowListForExcel(List<QryCSStlQPeriodShow> qryCSStlQPeriodShowListForExcel) {
        this.qryCSStlQPeriodShowListForExcel = qryCSStlQPeriodShowListForExcel;
    }

    public List<SelectItem> getSubcttList() {
        return subcttList;
    }

    public void setSubcttList(List<SelectItem> subcttList) {
        this.subcttList = subcttList;
    }

    public String getStrCstplPkid() {
        return strCstplPkid;
    }

    public void setStrCstplPkid(String strCstplPkid) {
        this.strCstplPkid = strCstplPkid;
    }

    public String getStrSubcttPkid() {
        return strSubcttPkid;
    }

    public void setStrSubcttPkid(String strSubcttPkid) {
        this.strSubcttPkid = strSubcttPkid;
    }

    public String getStrStartPeriodNo() {
        return strStartPeriodNo;
    }

    public void setStrStartPeriodNo(String strStartPeriodNo) {
        this.strStartPeriodNo = strStartPeriodNo;
    }

    public String getStrEndPeriodNo() {
        return strEndPeriodNo;
    }

    public void setStrEndPeriodNo(String strEndPeriodNo) {
        this.strEndPeriodNo = strEndPeriodNo;
    }

    public String getStrLatestApprovedPeriodNo() {
        return strLatestApprovedPeriodNo;
    }

    public void setStrLatestApprovedPeriodNo(String strLatestApprovedPeriodNo) {
        this.strLatestApprovedPeriodNo = strLatestApprovedPeriodNo;
    }

    public ReportHeader getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(ReportHeader reportHeader) {
        this.reportHeader = reportHeader;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public List<CommCol> getCommColSetList() {
        return commColSetList;
    }

    public void setCommColSetList(List<CommCol> commColSetList) {
        this.commColSetList = commColSetList;
    }

    public String[] getColumnHeaderList() {
        return columnHeaderList;
    }

    public void setColumnHeaderList(String[] columnHeaderList) {
        this.columnHeaderList = columnHeaderList;
    }
    /*智能字段End*/
}