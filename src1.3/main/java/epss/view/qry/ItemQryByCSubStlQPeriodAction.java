package epss.view.qry;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-18
 * Time: ����1:53
 * To change this template use File | Settings | File Templates.
 */

import epss.common.enums.ESEnum;
import epss.common.enums.EnumFlowStatus;
import epss.common.utils.JxlsManager;
import epss.repository.model.model_show.*;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.service.EsQueryService;
import epss.view.common.EsCommon;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ItemQryByCSubStlQPeriodAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemQryByCSubStlQPeriodAction.class);
    @ManagedProperty(value = "#{subcttItemService}")
    private SubcttItemService subcttItemService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;
    @ManagedProperty(value = "#{flowCtrlService}")
    private FlowCtrlService flowCtrlService;
    @ManagedProperty(value = "#{esQueryService}")
    private EsQueryService esQueryService;
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;

    private List<CstplItem> cstplItemList;

    /*�б���ʾ��*/
    private List<ProgWorkqtyItemShow> progWorkqtyItemShowList;
    private List<QryCSStlQPeriodShow> qryCSStlQPeriodShowList;
    private List<QryCSStlQPeriodShow> qryCSStlQPeriodShowListForExcel;
    private List<CommCol> commColSetList;
    private String columnHeaderList[]=new String[24];

    private List<SelectItem> subcttList;

    private String strCstplPkid;
    private String strSubcttInfoPkid;
    private String strStartStageNo;
    private String strEndStageNo;

    private  List<ProgWorkqtyInfoShow> latestStageProgWorkqtyInfoShowList;

    // �����Ͽؼ�����ʾ����
    private CttInfoShow cttInfoShow;
    private String strExportToExcelRendered;
    private Map beansMap;

    @PostConstruct
    public void init() {
        beansMap = new HashMap();
        cttInfoShow =new CttInfoShow();
        commColSetList =new ArrayList<CommCol>();
        for(int i=0;i<24;i++){
            CommCol commCol =new CommCol();
            commCol.setHeader("");
            commCol.setRendered_flag("false");
            commColSetList.add(commCol);
        }

        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // �ӳɱ��ƻ����ݹ����ĳɱ��ƻ���
        if (parammap.containsKey("strCstplPkid")){
            strCstplPkid=parammap.get("strCstplPkid").toString();
        }

        SubcttInfoShow subcttInfoShowTemp=new SubcttInfoShow();
        subcttInfoShowTemp.setFlowStatus(EnumFlowStatus.FLOW_STATUS3.getCode());
        List<SubcttInfoShow> subcttInfoShowList =
                subcttInfoService.getSubcttInfoShowListByShowModel(subcttInfoShowTemp);
        subcttList=new ArrayList<SelectItem>();
        if(subcttInfoShowList.size()>0){
            SelectItem selectItem=new SelectItem("","");
            subcttList.add(selectItem);
            for(SubcttInfoShow itemUnit: subcttInfoShowList){
                selectItem=new SelectItem();
                selectItem.setValue(itemUnit.getPkid());
                selectItem.setLabel(itemUnit.getName());
                subcttList.add(selectItem);
            }
        }
        strStartStageNo=ToolUtil.getStrThisMonth();
        strEndStageNo=ToolUtil.getStrThisMonth();
    }

    public String onExportExcel()throws IOException,WriteException {
        if (this.progWorkqtyItemShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ���ͬ�׶ι���������-" + ToolUtil.getStrToday() + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"qryCSStlQPeriod.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    private void initData(String strSubcttInfoPkidPara) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        SubcttInfo subcttInfoTemp = subcttInfoService.getSubcttInfoByPkid(strSubcttInfoPkidPara);
        cttInfoShow.setStrCstplInfoPkid(subcttInfoTemp.getCstplInfoPkid());
        cttInfoShow.setStrCstplInfoId(subcttInfoTemp.getId());
        cttInfoShow.setStrSubcttInfoName(subcttInfoTemp.getName());
        cttInfoShow.setStrSubcttSignPartPkidB(subcttInfoTemp.getSignPartPkidB());
        cttInfoShow.setStrSubcttSignPartNameB(signPartService.getEsInitCustByPkid(
                cttInfoShow.getStrSubcttSignPartPkidB()).getName());
        beansMap.put("cttInfoShow", cttInfoShow);

        latestStageProgWorkqtyInfoShowList=
                progWorkqtyInfoService.getLatestStageProgWorkqtyInfoShowList(
                        strSubcttInfoPkid, strEndStageNo, EnumFlowStatus.FLOW_STATUS2.getCode());

        /*�ְ���ͬ*/
        List<SubcttItem> subcttItemListTemp=new ArrayList<SubcttItem>();
        subcttItemListTemp = subcttItemService.getSubcttItemListBySubcttInfoPkid(strSubcttInfoPkid);
        if(subcttItemListTemp.size()<=0){
            return;
        }
        progWorkqtyItemShowList =new ArrayList<ProgWorkqtyItemShow>();
        recursiveDataTable("root", subcttItemListTemp, progWorkqtyItemShowList);
        progWorkqtyItemShowList =getStlSubCttEngQMngConstructList_DoFromatNo(progWorkqtyItemShowList);

        List<ProgWorkqtyInfoShow> stageList = progWorkqtyInfoService.getSgageListByStageBegin_End(
                                 strSubcttInfoPkid,
                                 strStartStageNo,
                                 strEndStageNo);

        for(int i=0;i<stageList.size();i++){
            commColSetList.get(i).setHeader(stageList.get(i).getStageNo());
            commColSetList.get(i).setRendered_flag("true");
            columnHeaderList[i]=stageList.get(i).getStageNo();
        }
        for(int i=stageList.size();i<24;i++){
            commColSetList.get(i).setHeader("");
            commColSetList.get(i).setRendered_flag("false");
            columnHeaderList[i]="";
        }

        qryCSStlQPeriodShowList =new ArrayList<QryCSStlQPeriodShow>();

        for(ProgWorkqtyItemShow progWorkqtyItemShow : progWorkqtyItemShowList){
            QryCSStlQPeriodShow qryCSStlQPeriodShow =new QryCSStlQPeriodShow();
            qryCSStlQPeriodShow.setStrPkid(progWorkqtyItemShow.getSubctt_Pkid());
            qryCSStlQPeriodShow.setStrNo(progWorkqtyItemShow.getSubctt_StrNo());
            qryCSStlQPeriodShow.setStrName(progWorkqtyItemShow.getSubctt_Name());
            qryCSStlQPeriodShow.setStrUnit(progWorkqtyItemShow.getSubctt_Unit());
            qryCSStlQPeriodShow.setBdAddUpEQty(progWorkqtyItemShow.getProgWork_AddUpQty());
            qryCSStlQPeriodShow.setBdQty(progWorkqtyItemShow.getSubctt_Qty());
            qryCSStlQPeriodShow.setBdSignPartPriceA(progWorkqtyItemShow.getSubctt_SignPartPriceA());

            String strItemPkid=ToolUtil.getStrIgnoreNull(progWorkqtyItemShow.getSubctt_Pkid());

            BeanInfo beanInfo = Introspector.getBeanInfo(QryCSStlQPeriodShow.class, Object.class);
            PropertyDescriptor[] pDescriptors = beanInfo.getPropertyDescriptors();
            for(int j=0;j< commColSetList.size();j++){
                for (PropertyDescriptor propertyDescriptor : pDescriptors) {
                    String proName = propertyDescriptor.getName();
                    if (proName.equals("bdCurrentPeriodEQty"+j)) {//����ط���id��������֪����ֵ����Щ������

                      /*  for(ProgWorkqtyItem progWorkqtyItem : progWorkqtyItemList){
                            if(strItemPkid.equals(progWorkqtyItem.getPkid())){
                                if(commColSetList.get(j).getHeader().equals(progWorkqtyItem.getStageNo())){
                                    propertyDescriptor.getWriteMethod().invoke(qryCSStlQPeriodShow, progWorkqtyItem.getThisStageQty());
                                }
                            }
                        }*/
                    }
                }
            }
            qryCSStlQPeriodShowList.add(qryCSStlQPeriodShow);
        }

        /*ƴװ�б�*/
        try {
            qryCSStlQPeriodShowListForExcel =new ArrayList<QryCSStlQPeriodShow>();
            for(QryCSStlQPeriodShow itemUnit: qryCSStlQPeriodShowList){
                QryCSStlQPeriodShow itemUnitTemp= (QryCSStlQPeriodShow) BeanUtils.cloneBean(itemUnit);
                itemUnitTemp.setStrNo(ToolUtil.getIgnoreSpaceOfStr(itemUnitTemp.getStrNo()));
                qryCSStlQPeriodShowListForExcel.add(itemUnitTemp);
            }
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
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

    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
                                      List<SubcttItem> subcttItemListPara,
                                      List<ProgWorkqtyItemShow> sProgWorkqtyItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<SubcttItem> subCttItemList =new ArrayList<SubcttItem>();
        // ͨ������id�������ĺ���
        subCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, subcttItemListPara);
        for(SubcttItem itemUnit: subCttItemList){
            ProgWorkqtyItemShow progWorkqtyItemShowTemp = new ProgWorkqtyItemShow();
            progWorkqtyItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progWorkqtyItemShowTemp.setSubctt_SubcttInfoPkid(itemUnit.getSubcttInfoPkid());
            progWorkqtyItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progWorkqtyItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progWorkqtyItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progWorkqtyItemShowTemp.setSubctt_CstplItemPkid(itemUnit.getCstplItemPkid());
            progWorkqtyItemShowTemp.setSubctt_Name(itemUnit.getName());
            progWorkqtyItemShowTemp.setSubctt_Remark(itemUnit.getRemark());
            progWorkqtyItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progWorkqtyItemShowTemp.setSubctt_UnitPrice(itemUnit.getUnitPrice());
            progWorkqtyItemShowTemp.setSubctt_Qty(itemUnit.getQty());
            progWorkqtyItemShowTemp.setSubctt_Amt(itemUnit.getAmt());
            progWorkqtyItemShowTemp.setSubctt_SignPartPriceA(itemUnit.getSignPartPriceA());
            if(latestStageProgWorkqtyInfoShowList.size()!=0){
                ProgWorkqtyItem progWorkqtyItem =new ProgWorkqtyItem();
                progWorkqtyItem.setProgWorkqtyInfoPkid(strSubcttInfoPkid);
                progWorkqtyItem.setSubcttItemPkid(itemUnit.getPkid());
                List<ProgWorkqtyItem> progWorkqtyItemList =
                        progWorkqtyItemService.selectRecordsByExample(progWorkqtyItem);
                if(progWorkqtyItemList.size()>0){
                    progWorkqtyItem = progWorkqtyItemList.get(0);
                    progWorkqtyItemShowTemp.setProgWork_Pkid(progWorkqtyItem.getPkid());
                    progWorkqtyItemShowTemp.setProgWork_ProgWorkqtyInfoPkid(progWorkqtyItem.getProgWorkqtyInfoPkid());
                    progWorkqtyItemShowTemp.setProgWork_AddUpQty(progWorkqtyItem.getAddUpQty());
                    progWorkqtyItemShowTemp.setProgWork_ThisStageQty(progWorkqtyItem.getThisStageQty());
                    progWorkqtyItemShowTemp.setProgWork_ArchivedFlag(progWorkqtyItem.getArchivedFlag());
                    progWorkqtyItemShowTemp.setProgWork_CreatedBy(progWorkqtyItem.getCreatedBy());
                    progWorkqtyItemShowTemp.setProgWork_CreatedTime(progWorkqtyItem.getCreatedTime());
                    progWorkqtyItemShowTemp.setProgWork_UpdatedBy(progWorkqtyItem.getUpdatedBy());
                    progWorkqtyItemShowTemp.setProgWork_UpdatedTime(progWorkqtyItem.getUpdatedTime());
                    progWorkqtyItemShowTemp.setProgWork_RecVersion(progWorkqtyItem.getRecVersion());
                }
            }
            sProgWorkqtyItemShowListPara.add(progWorkqtyItemShowTemp) ;
            recursiveDataTable(progWorkqtyItemShowTemp.getSubctt_Pkid(), subcttItemListPara, sProgWorkqtyItemShowListPara);
        }
    }

    /*����group��orderid��ʱ���Ʊ���strNo*/
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

    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<SubcttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
                                                                        List<SubcttItem> subcttItemListPara) {
        List<SubcttItem> subcttItemListTemp =new ArrayList<SubcttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(SubcttItem itemUnit: subcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                subcttItemListTemp.add(itemUnit);
            }
        }
        return subcttItemListTemp;
    }

    public void onQueryAction() {
        try {
            if(ToolUtil.getStrIgnoreNull(strSubcttInfoPkid).equals("")){
                MessageUtil.addWarn("��ѡ��ְ���ͬ�");
                return;
            }
            if(!blurStageNo()){
                return;
            }
            initData(strSubcttInfoPkid);
        } catch (Exception e) {
            logger.error("��Ϣ��ѯʧ��", e);
            MessageUtil.addError("��Ϣ��ѯʧ��");
        }
    }

    public boolean blurStageNo(){
        if(strEndStageNo.compareTo(strStartStageNo)<0){
            MessageUtil.addError("��ʼ��(" + strStartStageNo + "�����ڽ����ڣ�" + strEndStageNo + ")����ȷ����������룡");
            return false;
        }
        if(ToolUtil.getDateByStr(strStartStageNo,strEndStageNo)>12){
            MessageUtil.addError("һ�β�ѯֻ����12����("+strStartStageNo+"-"+strEndStageNo+")��Χ��!");
            return false;
        }
        return true;
    }

    /*�����ֶ�Start*/

    public SubcttItemService getSubcttItemService() {
        return subcttItemService;
    }

    public void setSubcttItemService(SubcttItemService subcttItemService) {
        this.subcttItemService = subcttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public FlowCtrlService getFlowCtrlService() {
        return flowCtrlService;
    }

    public void setFlowCtrlService(FlowCtrlService flowCtrlService) {
        this.flowCtrlService = flowCtrlService;
    }

    public EsQueryService getEsQueryService() {
        return esQueryService;
    }

    public void setEsQueryService(EsQueryService esQueryService) {
        this.esQueryService = esQueryService;
    }

    public ProgMatqtyInfoService getProgMatqtyInfoService() {
        return progMatqtyInfoService;
    }

    public String getStrSubcttInfoPkid() {
        return strSubcttInfoPkid;
    }

    public void setStrSubcttInfoPkid(String strSubcttInfoPkid) {
        this.strSubcttInfoPkid = strSubcttInfoPkid;
    }

    public void setProgMatqtyInfoService(ProgMatqtyInfoService progMatqtyInfoService) {
        this.progMatqtyInfoService = progMatqtyInfoService;
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

    public String getStrStartStageNo() {
        return strStartStageNo;
    }

    public void setStrStartStageNo(String strStartStageNo) {
        this.strStartStageNo = strStartStageNo;
    }

    public String getStrEndStageNo() {
        return strEndStageNo;
    }

    public void setStrEndStageNo(String strEndStageNo) {
        this.strEndStageNo = strEndStageNo;
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
    /*�����ֶ�End*/
}