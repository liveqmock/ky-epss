package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumStatusFlag;
import skyline.util.JxlsManager;
import epss.repository.model.model_show.*;
import skyline.util.MessageUtil;;
import skyline.util.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.EsFlowService;
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
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esFlowService}")
    private EsFlowService esFlowService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progStlInfoService}")
    private ProgStlInfoService progStlInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;

    private List<EsCttItem> esCttItemList;

    /*列表显示用*/
    private List<ProgWorkqtyItemShow> progWorkqtyItemShowList;
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
    private CommStlSubcttEngH commStlSubcttEngH;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        commColSetList =new ArrayList<CommCol>();
        for(int i=0;i<24;i++){
            CommCol commCol =new CommCol();
            commCol.setHeader("");
            commCol.setRendered_flag("false");
            commColSetList.add(commCol);
        }

        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // 从成本计划传递过来的成本计划号
        if (parammap.containsKey("strCstplInfoPkid")){
            strCstplPkid=parammap.get("strCstplInfoPkid").toString();
        }

        List<CttInfoShow> cttInfoShowList =
                cttInfoService.getCttInfoListByCttType_ParentPkid_Status(
                        ESEnum.ITEMTYPE2.getCode()
                        , strCstplPkid
                        , ESEnumStatusFlag.STATUS_FLAG3.getCode());
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
        strStartPeriodNo=(new BigDecimal(strStartPeriodNo.substring(0,4)).subtract(new BigDecimal("2"))).toString()+strStartPeriodNo.substring(4);
        strEndPeriodNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException,WriteException {
        if (this.progWorkqtyItemShowList.size() == 0) {
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
        EsCttInfo esCttInfo_Subctt= cttInfoService.getCttInfoByPkId(strBelongToPkid);
        commStlSubcttEngH.setStrCstplPkid(esCttInfo_Subctt.getParentPkid());
        commStlSubcttEngH.setStrSubcttId(esCttInfo_Subctt.getId());
        commStlSubcttEngH.setStrSubcttName(esCttInfo_Subctt.getName());
        commStlSubcttEngH.setStrSignPartPkid(esCttInfo_Subctt.getSignPartB());
        commStlSubcttEngH.setStrSignPartName(signPartService.getEsInitCustByPkid(
                commStlSubcttEngH.getStrSignPartPkid()).getName());
        beansMap.put("commStlSubcttEngH", commStlSubcttEngH);

        strLatestApprovedPeriodNo=ToolUtil.getStrIgnoreNull(
                esFlowService.getLatestApprovedPeriodNoByEndPeriod(
                ESEnum.ITEMTYPE3.getCode(),
                strSubcttPkid,
                strEndPeriodNo));

        /*分包合同*/
        esCttItemList =new ArrayList<EsCttItem>();

        esCttItemList = cttItemService.getEsItemList(
                ESEnum.ITEMTYPE2.getCode(), strSubcttPkid);
        if(esCttItemList.size()<=0){
            return;
        }
        progWorkqtyItemShowList =new ArrayList<ProgWorkqtyItemShow>();
        recursiveDataTable("root", esCttItemList, progWorkqtyItemShowList);
        progWorkqtyItemShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progWorkqtyItemShowList);

        List<String> periodList = progWorkqtyItemService.getProgWorkqtyItemPeriodsByPeriod(
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

        List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList =
                progWorkqtyItemService.getProgWorkqtyItemListByPeriod(
                        strSubcttPkid,
                        strStartPeriodNo,
                        strEndPeriodNo);

        qryCSStlQPeriodShowList =new ArrayList<QryCSStlQPeriodShow>();

        for(ProgWorkqtyItemShow itemUnit : progWorkqtyItemShowList){
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
                        for(EsItemStlSubcttEngQ esItemStlSubcttEngQ:esItemStlSubcttEngQList){
                            if(strItemPkid.equals(esItemStlSubcttEngQ.getPkid())){
                                if(commColSetList.get(j).getHeader().equals(esItemStlSubcttEngQ.getPeriodNo())){
                                    propertyDescriptor.getWriteMethod().invoke(qryCSStlQPeriodShow,esItemStlSubcttEngQ.getCurrentPeriodEQty());
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
        } catch (Exception e) {
            logger.error("信息查询失败", e);
            MessageUtil.addError("信息查询失败");
        }

        if(qryCSStlQPeriodShowListForExcel.size()>0){
            strExportToExcelRendered="true";
        }else{
            strExportToExcelRendered="false";
        }
        beansMap.put("columnHeaderList", columnHeaderList);
        beansMap.put("columnSetList", commColSetList);
        beansMap.put("qryCSStlQPeriodShowListForExcel", qryCSStlQPeriodShowListForExcel);
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgWorkqtyItemShow> sProgWorkqtyItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsCttItemListByParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgWorkqtyItemShow progWorkqtyItemShowTemp = new ProgWorkqtyItemShow();
            progWorkqtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progWorkqtyItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progWorkqtyItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progWorkqtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progWorkqtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progWorkqtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progWorkqtyItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progWorkqtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progWorkqtyItemShowTemp.setSubctt_Note(itemUnit.getNote());
            progWorkqtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progWorkqtyItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progWorkqtyItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progWorkqtyItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progWorkqtyItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            if(ToolUtil.getStrIgnoreNull(strLatestApprovedPeriodNo).length()!=0){
                EsItemStlSubcttEngQ esItemStlSubcttEngQ=new EsItemStlSubcttEngQ();
                esItemStlSubcttEngQ.setSubcttPkid(strSubcttPkid);
                esItemStlSubcttEngQ.setSubcttItemPkid(itemUnit.getPkid());
                esItemStlSubcttEngQ.setPeriodNo(strLatestApprovedPeriodNo);
                List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList =
                        progWorkqtyItemService.selectRecordsByExample(esItemStlSubcttEngQ);
                if(esItemStlSubcttEngQList.size()>0){
                    esItemStlSubcttEngQ= esItemStlSubcttEngQList.get(0);
                    progWorkqtyItemShowTemp.setEngQMng_Pkid(esItemStlSubcttEngQ.getPkid());
                    progWorkqtyItemShowTemp.setEngQMng_PeriodNo(esItemStlSubcttEngQ.getPeriodNo());
                    progWorkqtyItemShowTemp.setEngQMng_SubcttPkid(esItemStlSubcttEngQ.getSubcttPkid());
                    progWorkqtyItemShowTemp.setEngQMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngQ.getBeginToCurrentPeriodEQty());
                    progWorkqtyItemShowTemp.setEngQMng_CurrentPeriodEQty(esItemStlSubcttEngQ.getCurrentPeriodEQty());
                    progWorkqtyItemShowTemp.setEngQMng_DeletedFlag(esItemStlSubcttEngQ.getDeleteFlag());
                    progWorkqtyItemShowTemp.setEngQMng_CreatedBy(esItemStlSubcttEngQ.getCreatedBy());
                    progWorkqtyItemShowTemp.setEngQMng_CreatedDate(esItemStlSubcttEngQ.getCreatedDate());
                    progWorkqtyItemShowTemp.setEngQMng_LastUpdBy(esItemStlSubcttEngQ.getLastUpdBy());
                    progWorkqtyItemShowTemp.setEngQMng_LastUpdDate(esItemStlSubcttEngQ.getLastUpdDate());
                    progWorkqtyItemShowTemp.setEngQMng_ModificationNum(esItemStlSubcttEngQ.getModificationNum());
                }
            }
            sProgWorkqtyItemShowListPara.add(progWorkqtyItemShowTemp) ;
            recursiveDataTable(progWorkqtyItemShowTemp.getSubctt_Pkid(), esCttItemListPara, sProgWorkqtyItemShowListPara);
        }
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgWorkqtyItemShow> getStlSubCttEngQMngConstructList_DoFromatNo(
            List<ProgWorkqtyItemShow> progWorkqtyItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgWorkqtyItemShow itemUnit: progWorkqtyItemShowListPara){
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
        return progWorkqtyItemShowListPara;
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsCttItemListByParentPkid(String strLevelParentPkid,
                                                                        List<EsCttItem> esCttItemListPara) {
        List<EsCttItem> tempEsCttItemList =new ArrayList<EsCttItem>();
        /*避开重复链接数据库*/
        for(EsCttItem itemUnit: esCttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempEsCttItemList.add(itemUnit);
            }
        }
        return tempEsCttItemList;
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

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public List<EsCttItem> getEsCttItemList() {
        return esCttItemList;
    }

    public void setEsCttItemList(List<EsCttItem> esCttItemList) {
        this.esCttItemList = esCttItemList;
    }

    public List<ProgWorkqtyItemShow> getProgWorkqtyItemShowList() {
        return progWorkqtyItemShowList;
    }

    public void setProgWorkqtyItemShowList(List<ProgWorkqtyItemShow> progWorkqtyItemShowList) {
        this.progWorkqtyItemShowList = progWorkqtyItemShowList;
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

    public CommStlSubcttEngH getCommStlSubcttEngH() {
        return commStlSubcttEngH;
    }

    public void setCommStlSubcttEngH(CommStlSubcttEngH commStlSubcttEngH) {
        this.commStlSubcttEngH = commStlSubcttEngH;
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