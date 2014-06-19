package epss.view.settle;

import epss.common.enums.ESEnum;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.SubcttInfoShow;
import epss.repository.model.model_show.ProgSubstlItemShow;
import epss.common.utils.JxlsManager;
import epss.common.utils.MessageUtil;
import epss.common.utils.ToolUtil;
import epss.repository.model.*;
import epss.service.*;
import epss.view.common.EsCommon;
import jxl.write.WriteException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PlatformService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ����9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ProgSubstlItemAction {
    private static final Logger logger = LoggerFactory.getLogger(ProgSubstlItemAction.class);
    @ManagedProperty(value = "#{tkcttInfoService}")
    private TkcttInfoService tkcttInfoService;
    @ManagedProperty(value = "#{cstplInfoService}")
    private CstplInfoService cstplInfoService;
    @ManagedProperty(value = "#{subcttInfoService}")
    private SubcttInfoService subcttInfoService;

    @ManagedProperty(value = "#{subcttItemService}")
    private SubcttItemService subcttItemService;
    @ManagedProperty(value = "#{signPartService}")
    private SignPartService signPartService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;

    @ManagedProperty(value = "#{progSubstlInfoService}")
    private ProgSubstlInfoService progSubstlInfoService;
    @ManagedProperty(value = "#{progSubstlItemService}")
    private ProgSubstlItemService progSubstlItemService;
    @ManagedProperty(value = "#{progMatqtyInfoService}")
    private ProgMatqtyInfoService progMatqtyInfoService;
    @ManagedProperty(value = "#{progMatqtyItemService}")
    private ProgMatqtyItemService progMatqtyItemService;
    @ManagedProperty(value = "#{progWorkqtyInfoService}")
    private ProgWorkqtyInfoService progWorkqtyInfoService;
    @ManagedProperty(value = "#{progWorkqtyItemService}")
    private ProgWorkqtyItemService progWorkqtyItemService;

    private List<ProgSubstlItemShow> progSubstlItemShowList;
    private CttInfoShow cttInfoShow;

    private Map beansMap;
    private ProgSubstlInfo progSubstlInfo;
    private String strProgSubstlInfoPkid;
    private Date nowDate;

    // �����Ͽؼ�����ʾ����
    private String strExportToExcelRendered;

    @PostConstruct
    public void init() {
        nowDate=new Date();
        beansMap = new HashMap();
        cttInfoShow =new CttInfoShow();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strProgSubstlInfoPkid")){
            strProgSubstlInfoPkid=parammap.get("strProgSubstlInfoPkid").toString();
        }
        initData();
    }

    /*��ʼ������*/
    private void initData() {
        // ��ͷ�趨
        cttInfoShow.setStrDate(PlatformService.dateFormat(nowDate, "yyyy-MM-dd"));

        /*�ְ���ͬ����*/
        List<SubcttItem> subcttItemList =new ArrayList<SubcttItem>();
        progSubstlItemShowList =new ArrayList<ProgSubstlItemShow>();
        // From StlPkid To SubcttPkid
        progSubstlInfo = progSubstlInfoService.selectRecordsByPrimaryKey(strProgSubstlInfoPkid);
        cttInfoShow.setStrSubcttInfoPkid(progSubstlInfo.getSubcttInfoPkid());
        cttInfoShow.setStrStlId(progSubstlInfo.getId());
        // From SubcttPkid To CstplPkid
        SubcttInfo subcttInfoTemp = subcttInfoService.getSubcttInfoByPkid(cttInfoShow.getStrSubcttInfoPkid());
        cttInfoShow.setStrCstplInfoId(subcttInfoTemp.getCstplInfoPkid());
        cttInfoShow.setStrSubcttInfoId(subcttInfoTemp.getId());
        cttInfoShow.setStrSubcttInfoName(subcttInfoTemp.getName());
        cttInfoShow.setStrSubcttSignPartPkidB(subcttInfoTemp.getSignPartPkidB());
        cttInfoShow.setStrSubcttSignPartNameB(signPartService.getEsInitCustByPkid(
                subcttInfoTemp.getSignPartPkidB()).getName());
        // From CstplPkid To TkcttPkid
        CstplInfo cstplInfoTemp = cstplInfoService.getCstplInfoByPkid(cttInfoShow.getStrCstplInfoPkid());
        cttInfoShow.setStrTkcttInfoId(cstplInfoTemp.getTkcttInfoPkid());
        cttInfoShow.setStrCstplInfoId(cstplInfoTemp.getId());
        cttInfoShow.setStrCstplInfoName(cstplInfoTemp.getName());

        TkcttInfo tkcttInfoTemp = tkcttInfoService.getTkcttInfoByPkid(cttInfoShow.getStrTkcttInfoPkid());
        cttInfoShow.setStrTkcttInfoId(tkcttInfoTemp.getId());
        cttInfoShow.setStrTkcttInfoName(tkcttInfoTemp.getName());

        // �������趨
        subcttItemList = subcttItemService.getSubcttItemListBySubcttInfoPkid(cttInfoShow.getStrSubcttInfoPkid());
        if(subcttItemList.size()>0){
            strExportToExcelRendered="true";
            recursiveDataTable("root", subcttItemList, progSubstlItemShowList);
            progSubstlItemShowList =getStlSubCttEngPMngConstructList_DoFromatNo(progSubstlItemShowList);

            /*�۸�����������*/
            setStlSubCttEngPMngConstructList_AddSettle();
        }else{
            strExportToExcelRendered="false";
        }
    }

    private void setStlSubCttEngPMngConstructList_AddSettle(){
        try {
            BigDecimal bd0=new BigDecimal(0);
            BigDecimal bdCurrentPeriodTotalAmtTemp=new BigDecimal(0);
            BigDecimal bdAddUpTotalAmtTemp=new BigDecimal(0);
            // 0:�����ʣ�1:�ʱ����ʣ�2:�����ʣ�3:��ȫʩ����ʩ����
            BigDecimal [] bdRates ={bd0,bd0,bd0,bd0};

            beansMap.put("subcttInfoShow", cttInfoShow);

            //�ۿ���
            List<ProgSubstlItemShow> progSubstlItemShowListForReduction =new ArrayList<ProgSubstlItemShow>() ;
            for(ProgSubstlItemShow itemUnit: progSubstlItemShowList){
                if(!ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_Pkid()).equals("")){
                    // �ӹ������������г�ȡ��������
                    ProgWorkqtyItem progWorkqtyItem =new ProgWorkqtyItem();
                    ProgWorkqtyInfo progWorkqtyInfoTemp=new ProgWorkqtyInfo();
                    progWorkqtyInfoTemp.setSubcttInfoPkid(cttInfoShow.getStrSubcttInfoId());
                    progWorkqtyInfoTemp.setStageNo(progSubstlInfo.getStageNo());
                    List<ProgWorkqtyInfo> progWorkqtyInfoListTemp=
                            progWorkqtyInfoService.getProgWorkqtyInfoListByModel(progWorkqtyInfoTemp);
                    progWorkqtyItem.setProgWorkqtyInfoPkid(progWorkqtyInfoListTemp.get(0).getPkid());
                    progWorkqtyItem.setSubcttItemPkid(itemUnit.getSubctt_Pkid());
                    List<ProgWorkqtyItem> progWorkqtyItemList = progWorkqtyItemService.selectRecordsByExample(progWorkqtyItem);
                    if(progWorkqtyItemList.size()>0){
                        progWorkqtyItem = progWorkqtyItemList.get(0);
                        itemUnit.setProgSubstl_AddUpQty(progWorkqtyItem.getAddUpQty());
                        itemUnit.setProgSubstl_ThisStageQty(progWorkqtyItem.getThisStageQty());
                    }
                }

                BigDecimal bdUnitPriceInSubctt=itemUnit.getSubctt_UnitPrice() == null ?
                        new BigDecimal(0) : itemUnit.getSubctt_UnitPrice();

                if(itemUnit.getProgSubstl_AddUpQty() == null){
                    itemUnit.setProgSubstl_AddUpAmt(null);
                }else{
                    BigDecimal bdAddUpQty=itemUnit.getProgSubstl_AddUpQty();
                    itemUnit.setProgSubstl_AddUpAmt(bdUnitPriceInSubctt.multiply(bdAddUpQty));
                }

                if(itemUnit.getProgSubstl_ThisStageQty() == null){
                    itemUnit.setProgSubstl_ThisStageAmt(null);
                }else{
                    BigDecimal bdThisStageQty=itemUnit.getProgSubstl_ThisStageQty();
                    itemUnit.setProgSubstl_ThisStageAmt(bdUnitPriceInSubctt.multiply(bdThisStageQty));
                }

                ProgSubstlItem progSubstlItemTemp =new ProgSubstlItem();
                List<ProgSubstlItem> progSubstlItemListTemp =
                        progSubstlItemService.selectRecordsByDetailExample(progSubstlItemTemp);
                if(progSubstlItemListTemp.size()>0){
                    progSubstlItemTemp = progSubstlItemListTemp.get(0);
                    itemUnit.setProgSubstl_Pkid(progSubstlItemTemp.getPkid());
                    itemUnit.setProgSubstl_ProgSubstlInfoPkid(cttInfoShow.getStrSubcttInfoId());
                    itemUnit.setProgSubstl_SubcttItemPkid(progSubstlItemTemp.getSubcttItemPkid());

                    itemUnit.setProgSubstl_ThisStageQty(progSubstlItemTemp.getThisStageQty());
                    itemUnit.setProgSubstl_ThisStageAmt(progSubstlItemTemp.getThisStageAmt());
                    itemUnit.setProgSubstl_AddUpQty(progSubstlItemTemp.getAddUpQty());
                    itemUnit.setProgSubstl_AddUpAmt(progSubstlItemTemp.getAddUpAmt());

                    itemUnit.setProgSubstl_ArchivedFlag(progSubstlItemTemp.getArchivedFlag());
                    itemUnit.setProgSubstl_CreatedBy(progSubstlItemTemp.getCreatedBy());
                    itemUnit.setProgSubstl_CreatedTime(progSubstlItemTemp.getCreatedTime());
                    itemUnit.setProgSubstl_UpdatedBy(progSubstlItemTemp.getUpdatedBy());
                    itemUnit.setProgSubstl_UpdatedTime(progSubstlItemTemp.getUpdatedTime());
                    itemUnit.setProgSubstl_RecVersion(progSubstlItemTemp.getRecVersion());
                }
                //˰����
                /*if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("0")){
                    bdRates[0]=ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_Amt());
                }
                //�ʱ�����
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("1")){
                    bdRates[1]= ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_Amt());
                }
                //������
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("2")){
                    bdRates[2]=ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_Amt());
                }
                //��ȫʩ����ʩ����
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("3")){
                    bdRates[3]=ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_Amt());
                }*/
                //�ۿ���
                if(itemUnit.getSubctt_SignPartPriceA()!=null && itemUnit.getSubctt_SignPartPriceA().compareTo(bd0)!=0) {
                    progSubstlItemShowListForReduction.add(itemUnit);
                }
                bdCurrentPeriodTotalAmtTemp=bdCurrentPeriodTotalAmtTemp.add(
                        getBdIgnoreNull(itemUnit.getProgSubstl_ThisStageAmt()));
                bdAddUpTotalAmtTemp=bdAddUpTotalAmtTemp.add(
                        getBdIgnoreNull(itemUnit.getProgSubstl_AddUpAmt()));
            }

            List<ProgSubstlItemShow> records0=new ArrayList<ProgSubstlItemShow>();
            records0.addAll(progSubstlItemShowList);
            beansMap.put("records0", records0);

            //1С��
            ProgSubstlItemShow stl1 = new ProgSubstlItemShow();
            stl1.setSubctt_Name("С��");
            stl1.setSubctt_Pkid("setl_1");
            stl1.setProgSubstl_ThisStageAmt(bdCurrentPeriodTotalAmtTemp);
            stl1.setProgSubstl_AddUpAmt(bdAddUpTotalAmtTemp);
            progSubstlItemShowList.add(stl1);

            //2����(��ȫʩ����ʩ��)
            ProgSubstlItemShow stl2 = new ProgSubstlItemShow();
            stl2.setSubctt_Name("����:��ȫʩ����ʩ��");
            stl2.setSubctt_Pkid("setl_2");
            stl2.setProgSubstl_ThisStageAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[3]));
            stl2.setProgSubstl_AddUpAmt(bdAddUpTotalAmtTemp.multiply(bdRates[3]));
            progSubstlItemShowList.add(stl2);

            //3�ۿ�
            ProgSubstlItemShow stl3 = new ProgSubstlItemShow();
            //stl3.setSubctt_StrNo("7");
            stl3.setSubctt_Name("�ۿ�(����)");
            stl3.setSubctt_Pkid("setl_3");
            progSubstlItemShowList.add(stl3);

            BigDecimal bdTotalAmt=new BigDecimal(0);
            BigDecimal bdTotalAllAmt=new BigDecimal(0);
            List<ProgSubstlItemShow> records1=new ArrayList<ProgSubstlItemShow>();

            for(ProgSubstlItemShow itemUnit: progSubstlItemShowListForReduction){
                ProgSubstlItemShow progSubstlItemShowTemp =(ProgSubstlItemShow)BeanUtils.cloneBean(itemUnit) ;
                progSubstlItemShowTemp.setSubctt_UnitPrice(itemUnit.getSubctt_SignPartPriceA());

                // �ӹ��̲��Ͻ����г�ȡ��������
                ProgMatqtyItem progMatqtyItem =new ProgMatqtyItem();
                progMatqtyItem.setProgMatqtyInfoPkid(cttInfoShow.getStrSubcttInfoPkid());
                progMatqtyItem.setSubcttItemPkid(progSubstlItemShowTemp.getSubctt_Pkid());
                List<ProgMatqtyItem> progMatqtyItemList = progMatqtyItemService.selectRecordsByExample(progMatqtyItem);
                if(progMatqtyItemList.size()>0){
                    progMatqtyItem = progMatqtyItemList.get(0);
                    progSubstlItemShowTemp.setProgSubstl_AddUpQty(progMatqtyItem.getAddUpQty());
                    progSubstlItemShowTemp.setProgSubstl_ThisStageQty(progMatqtyItem.getThisStageQty());

                    BigDecimal bdSubctt_UnitPrice=ToolUtil.getBdIgnoreNull(
                            progSubstlItemShowTemp.getSubctt_UnitPrice());
                    BigDecimal bdProgSubstl_ThisStageQty=ToolUtil.getBdIgnoreNull(
                            progSubstlItemShowTemp.getProgSubstl_ThisStageQty());
                    BigDecimal bdProgSubstl_AddUpQty=ToolUtil.getBdIgnoreNull(
                            progSubstlItemShowTemp.getProgSubstl_AddUpQty());

                    BigDecimal bdProgSubstl_ThisStageAmtTemp=bdSubctt_UnitPrice.multiply(bdProgSubstl_ThisStageQty);
                    BigDecimal bdProgSubstl_AddUpAmtTemp=bdSubctt_UnitPrice.multiply(bdProgSubstl_AddUpQty);

                    bdTotalAmt=bdTotalAmt.add(bdProgSubstl_ThisStageAmtTemp);
                    bdTotalAllAmt=bdTotalAllAmt.add(bdSubctt_UnitPrice.multiply(bdProgSubstl_AddUpQty));

                    progSubstlItemShowTemp.setProgSubstl_ThisStageAmt(bdProgSubstl_ThisStageAmtTemp);
                    progSubstlItemShowTemp.setProgSubstl_AddUpAmt(bdProgSubstl_AddUpAmtTemp);
                }
                records1.add(progSubstlItemShowTemp);
                progSubstlItemShowList.add(progSubstlItemShowTemp);
            }

            beansMap.put("records1", records1);

            ProgSubstlItemShow stl4 = new ProgSubstlItemShow();
            //stl4.setSubctt_StrNo("8");
            stl4.setSubctt_Name("�ۿ�(˰)");
            stl4.setSubctt_Pkid("setl_4");
            stl4.setProgSubstl_ThisStageAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[0]));
            stl4.setProgSubstl_AddUpAmt(bdAddUpTotalAmtTemp.multiply(bdRates[0]));
            progSubstlItemShowList.add(stl4);

            ProgSubstlItemShow stl5 = new ProgSubstlItemShow();
            stl5.setSubctt_Name("С��");
            stl5.setSubctt_Pkid("setl_5");
            stl5.setProgSubstl_ThisStageAmt(bdTotalAmt.add(stl4.getProgSubstl_ThisStageAmt()));
            stl5.setProgSubstl_AddUpAmt(bdTotalAllAmt.add(stl4.getProgSubstl_AddUpAmt()));
            progSubstlItemShowList.add(stl5);

            ProgSubstlItemShow stl6 = new ProgSubstlItemShow();
            stl6.setSubctt_Name("���ھ������");
            stl6.setSubctt_Pkid("setl_6");
            stl6.setProgSubstl_ThisStageAmt(
                    stl1.getProgSubstl_ThisStageAmt().subtract(stl5.getProgSubstl_ThisStageAmt()));
            stl6.setProgSubstl_AddUpAmt(
                    stl1.getProgSubstl_AddUpAmt().subtract(stl5.getProgSubstl_AddUpAmt()));
            progSubstlItemShowList.add(stl6);

            ProgSubstlItemShow stl7 = new ProgSubstlItemShow();
            //stl7.setSubctt_StrNo("9");
            stl7.setSubctt_Name("����(�ʱ���)");
            stl7.setSubctt_Pkid("setl_7");
            stl7.setProgSubstl_ThisStageAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            stl7.setProgSubstl_AddUpAmt(bdAddUpTotalAmtTemp.multiply(bdRates[1]));
            progSubstlItemShowList.add(stl7);

            ProgSubstlItemShow stl8 = new ProgSubstlItemShow();
            stl8.setSubctt_Name("С��");
            stl8.setSubctt_Pkid("setl_8");
            stl8.setProgSubstl_ThisStageAmt(stl7.getProgSubstl_ThisStageAmt());
            stl8.setProgSubstl_AddUpAmt(stl7.getProgSubstl_AddUpAmt());
            progSubstlItemShowList.add(stl8);

            ProgSubstlItemShow stl9 = new ProgSubstlItemShow();
            stl9.setSubctt_Name("�ϼ�(�۳�������������ڽ����ֵ)");
            stl9.setSubctt_Pkid("setl_9");
            stl9.setProgSubstl_ThisStageAmt(
                    stl6.getProgSubstl_ThisStageAmt().subtract(stl8.getProgSubstl_ThisStageAmt()));
            stl9.setProgSubstl_AddUpAmt(
                    stl6.getProgSubstl_AddUpAmt().subtract(stl8.getProgSubstl_AddUpAmt()));
            progSubstlItemShowList.add(stl9);
            beansMap.put("stl1", stl1);
            beansMap.put("stl2", stl2);
            beansMap.put("stl3", stl3);
            beansMap.put("stl4", stl4);
            beansMap.put("stl5", stl5);
            beansMap.put("stl6", stl6);
            beansMap.put("stl7", stl7);
            beansMap.put("stl8", stl8);
            beansMap.put("stl9", stl9);
        } catch (Exception e) {
            MessageUtil.addInfo("����������ȡʧ�ܣ�" + e.getMessage());
        }
    }

    private BigDecimal getBdIgnoreNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?new BigDecimal(0):bigDecimalPara;
    }

    /*�������ݿ��в㼶��ϵ�����б�õ��ܰ���ͬ*/
    private void recursiveDataTable(String strLevelParentId,
        List<SubcttItem> subcttItemListPara,
        List<ProgSubstlItemShow> sProgSubstlItemShowListPara){
        // ���ݸ��㼶�Ż�øø��㼶�µ��ӽڵ�
        List<SubcttItem> subcttItemList =new ArrayList<SubcttItem>();
        // ͨ������id�������ĺ���
        subcttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, subcttItemListPara);
        for(SubcttItem itemUnit: subcttItemList){
            ProgSubstlItemShow progSubstlItemShowTemp = new ProgSubstlItemShow();
            progSubstlItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progSubstlItemShowTemp.setSubctt_SubcttInfoPkid(itemUnit.getSubcttInfoPkid());
            progSubstlItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progSubstlItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progSubstlItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progSubstlItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progSubstlItemShowTemp.setSubctt_CstplItemPkid(itemUnit.getCstplItemPkid());
            progSubstlItemShowTemp.setSubctt_Name(itemUnit.getName());
            progSubstlItemShowTemp.setSubctt_Remark(itemUnit.getRemark());
            progSubstlItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progSubstlItemShowTemp.setSubctt_UnitPrice(itemUnit.getUnitPrice());
            progSubstlItemShowTemp.setSubctt_Qty(itemUnit.getQty());
            progSubstlItemShowTemp.setSubctt_Amt(itemUnit.getAmt());
            progSubstlItemShowTemp.setSubctt_SignPartPriceA(itemUnit.getSignPartPriceA());
            sProgSubstlItemShowListPara.add(progSubstlItemShowTemp) ;
            recursiveDataTable(progSubstlItemShowTemp.getSubctt_Pkid(), subcttItemListPara, sProgSubstlItemShowListPara);
        }
    }

    public String onExportExcel()throws IOException,WriteException{
        if (this.progSubstlItemShowList.size() == 0) {
            MessageUtil.addWarn("��¼Ϊ��...");
            return null;
        } else {
            String excelFilename = "�ְ�����Ԥ���㵥-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlSubCttEngP.xls");
            // ����״̬��Ʊ����Ҫ���ʱ���޸ĵ����ļ���
        }
        return null;
    }
    /*����group��orderid��ʱ���Ʊ���strNo*/
    private List<ProgSubstlItemShow> getStlSubCttEngPMngConstructList_DoFromatNo(
             List<ProgSubstlItemShow> progSubstlItemShowListPara){
        String strTemp="";
        Integer intBeforeGrade=-1;
        for(ProgSubstlItemShow itemUnit: progSubstlItemShowListPara){
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
            itemUnit.setSubctt_StrNo(ToolUtil.padLeftSpace_DoLevel(itemUnit.getSubctt_Grade(), strTemp)) ;
        }
        return progSubstlItemShowListPara;
    }

    /*�������ݿ��в㼶��ϵ�����б�õ�ĳһ�ڵ��µ��ӽڵ�*/
    private List<SubcttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
             List<SubcttItem> subcttItemListPara) {
        List<SubcttItem> tempSubsubcttItemList =new ArrayList<SubcttItem>();
        /*�ܿ��ظ��������ݿ�*/
        for(SubcttItem itemUnit: subcttItemListPara){
            if(strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())){
                tempSubsubcttItemList.add(itemUnit);
            }
        }
        return tempSubsubcttItemList;
    }

   /* �����ֶ�Start*/

    public SubcttInfoService getSubcttInfoService() {
        return subcttInfoService;
    }

    public void setSubcttInfoService(SubcttInfoService subcttInfoService) {
        this.subcttInfoService = subcttInfoService;
    }

    public SubcttItemService getSubcttItemService() {
        return subcttItemService;
    }

    public void setSubcttItemService(SubcttItemService subcttItemService) {
        this.subcttItemService = subcttItemService;
    }

    public SignPartService getSignPartService() {
        return signPartService;
    }

    public void setSignPartService(SignPartService signPartService) {
        this.signPartService = signPartService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public ProgSubstlInfoService getProgSubstlInfoService() {
        return progSubstlInfoService;
    }

    public void setProgSubstlInfoService(ProgSubstlInfoService progSubstlInfoService) {
        this.progSubstlInfoService = progSubstlInfoService;
    }

    public ProgSubstlItemService getProgSubstlItemService() {
        return progSubstlItemService;
    }

    public void setProgSubstlItemService(ProgSubstlItemService progSubstlItemService) {
        this.progSubstlItemService = progSubstlItemService;
    }

    public ProgMatqtyInfoService getProgMatqtyInfoService() {
        return progMatqtyInfoService;
    }

    public void setProgMatqtyInfoService(ProgMatqtyInfoService progMatqtyInfoService) {
        this.progMatqtyInfoService = progMatqtyInfoService;
    }

    public ProgMatqtyItemService getProgMatqtyItemService() {
        return progMatqtyItemService;
    }

    public void setProgMatqtyItemService(ProgMatqtyItemService progMatqtyItemService) {
        this.progMatqtyItemService = progMatqtyItemService;
    }

    public ProgWorkqtyInfoService getProgWorkqtyInfoService() {
        return progWorkqtyInfoService;
    }

    public void setProgWorkqtyInfoService(ProgWorkqtyInfoService progWorkqtyInfoService) {
        this.progWorkqtyInfoService = progWorkqtyInfoService;
    }

    public ProgWorkqtyItemService getProgWorkqtyItemService() {
        return progWorkqtyItemService;
    }

    public void setProgWorkqtyItemService(ProgWorkqtyItemService progWorkqtyItemService) {
        this.progWorkqtyItemService = progWorkqtyItemService;
    }

    public List<ProgSubstlItemShow> getProgSubstlItemShowList() {
        return progSubstlItemShowList;
    }

    public void setProgSubstlItemShowList(List<ProgSubstlItemShow> progSubstlItemShowList) {
        this.progSubstlItemShowList = progSubstlItemShowList;
    }

    public CttInfoShow getCttInfoShow() {
        return cttInfoShow;
    }

    public void setCttInfoShow(CttInfoShow cttInfoShow) {
        this.cttInfoShow = cttInfoShow;
    }

    public Map getBeansMap() {
        return beansMap;
    }

    public void setBeansMap(Map beansMap) {
        this.beansMap = beansMap;
    }

    public ProgSubstlInfo getProgSubstlInfo() {
        return progSubstlInfo;
    }

    public void setProgSubstlInfo(ProgSubstlInfo progSubstlInfo) {
        this.progSubstlInfo = progSubstlInfo;
    }

    public String getStrProgSubstlInfoPkid() {
        return strProgSubstlInfoPkid;
    }

    public void setStrProgSubstlInfoPkid(String strProgSubstlInfoPkid) {
        this.strProgSubstlInfoPkid = strProgSubstlInfoPkid;
    }

    public Date getNowDate() {
        return nowDate;
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = nowDate;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }
/*�����ֶ�End*/
}
