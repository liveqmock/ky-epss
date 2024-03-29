package epss.view.item;

import epss.common.enums.ESEnum;
import epss.repository.model.model_show.CommStlSubcttEngH;
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
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ItemSubcttEngPStlAction {
    private static final Logger logger = LoggerFactory.getLogger(ItemSubcttEngPStlAction.class);
    @ManagedProperty(value = "#{esCttInfoService}")
    private EsCttInfoService esCttInfoService;
    @ManagedProperty(value = "#{esCttItemService}")
    private EsCttItemService esCttItemService;
    @ManagedProperty(value = "#{esInitCustService}")
    private EsInitCustService esInitCustService;
    @ManagedProperty(value = "#{esCommon}")
    private EsCommon esCommon;
    @ManagedProperty(value = "#{esInitStlService}")
    private EsInitStlService esInitStlService;
    @ManagedProperty(value = "#{esItemStlSubcttEngPService}")
    private EsItemStlSubcttEngPService esItemStlSubcttEngPService;
    @ManagedProperty(value = "#{esItemStlSubcttEngMService}")
    private EsItemStlSubcttEngMService esItemStlSubcttEngMService;
    @ManagedProperty(value = "#{esItemStlSubcttEngQService}")
    private EsItemStlSubcttEngQService esItemStlSubcttEngQService;

    private List<ProgSubstlItemShow> progSubstlItemShowList;
    private CommStlSubcttEngH commStlSubcttEngH;

    private Map beansMap;
    private EsInitStl esInitStl;

    /*所属号*/
    private String strEsInitStlSubcttEng;
    private Date nowDate;

    // 画面上控件的显示控制
    private String strExportToExcelRendered;

    @PostConstruct
    public void init() {
        nowDate=new Date();
        beansMap = new HashMap();
        commStlSubcttEngH =new CommStlSubcttEngH();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(parammap.containsKey("strEsInitStlSubcttEng")){
            strEsInitStlSubcttEng=parammap.get("strEsInitStlSubcttEng").toString();
        }
        initData();
    }

    /*初始化操作*/
    private void initData() {
        // 表头设定
        commStlSubcttEngH.setStrDate(PlatformService.dateFormat(nowDate, "yyyy-MM-dd"));

        /*分包合同数据*/
        List<EsCttItem> esCttItemList =new ArrayList<EsCttItem>();
        progSubstlItemShowList =new ArrayList<ProgSubstlItemShow>();
        // From StlPkid To SubcttPkid
        esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlSubcttEng);
        commStlSubcttEngH.setStrSubcttPkid(esInitStl.getStlPkid());
        commStlSubcttEngH.setStrStlId(esInitStl.getId());
        // From SubcttPkid To CstplPkid
        EsCttInfo esCttInfo_Subctt=esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrSubcttPkid());
        commStlSubcttEngH.setStrCstplPkid(esCttInfo_Subctt.getParentPkid());
        commStlSubcttEngH.setStrSubcttId(esCttInfo_Subctt.getId());
        commStlSubcttEngH.setStrSubcttName(esCttInfo_Subctt.getName());
        commStlSubcttEngH.setStrSignPartPkid(esCttInfo_Subctt.getSignPartB());
        commStlSubcttEngH.setStrSignPartName(esInitCustService.getEsInitCustByPkid(
                commStlSubcttEngH.getStrSignPartPkid()).getName());
        // From CstplPkid To TkcttPkid
        EsCttInfo esCttInfo_Cstpl=esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrCstplPkid());
        commStlSubcttEngH.setStrTkcttPkid(esCttInfo_Cstpl.getParentPkid());
        commStlSubcttEngH.setStrCstplId(esCttInfo_Cstpl.getId());
        commStlSubcttEngH.setStrCstplName(esCttInfo_Cstpl.getName());

        EsCttInfo esCttInfo_Tkctt=esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrTkcttPkid());
        commStlSubcttEngH.setStrTkcttId(esCttInfo_Tkctt.getId());
        commStlSubcttEngH.setStrTkcttName(esCttInfo_Tkctt.getName());

        // 表内容设定
        esCttItemList = esCttItemService.getEsItemHieRelapListByTypeAndPkid(
                ESEnum.ITEMTYPE2.getCode(), commStlSubcttEngH.getStrSubcttPkid());
        if(esCttItemList.size()>0){
            strExportToExcelRendered="true";
            recursiveDataTable("root", esCttItemList, progSubstlItemShowList);
            progSubstlItemShowList =getStlSubCttEngPMngConstructList_DoFromatNo(progSubstlItemShowList);

            /*价格结算数据添加*/
            setStlSubCttEngPMngConstructList_AddSettle();
        }else{
            strExportToExcelRendered="false";
        }
    }

    private void setStlSubCttEngPMngConstructList_AddSettle(){
        try {
            BigDecimal bd0=new BigDecimal(0);
            BigDecimal bdCurrentPeriodTotalAmtTemp=new BigDecimal(0);
            BigDecimal bdBeginToCurrentPeriodTotalAmtTemp=new BigDecimal(0);
            // 0:稅金率；1:质保金率；2:付款率；3:安全施工措施费率
            BigDecimal [] bdRates ={bd0,bd0,bd0,bd0};

            beansMap.put("commStlSubcttEngH", commStlSubcttEngH);

            //扣款用
            List<ProgSubstlItemShow> progSubstlItemShowListForReduction =new ArrayList<ProgSubstlItemShow>() ;
            for(ProgSubstlItemShow itemUnit: progSubstlItemShowList){
                if(!ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_ItemPkid()).equals("")){
                    // 从工程数量结算中抽取结算数据
                    EsItemStlSubcttEngQ esItemStlSubcttEngQ=new EsItemStlSubcttEngQ();
                    esItemStlSubcttEngQ.setSubcttPkid(commStlSubcttEngH.getStrSubcttPkid());
                    esItemStlSubcttEngQ.setSubcttItemPkid(itemUnit.getSubctt_ItemPkid());
                    esItemStlSubcttEngQ.setPeriodNo(esInitStl.getPeriodNo());
                    List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList = esItemStlSubcttEngQService.selectRecordsByExample(esItemStlSubcttEngQ);
                    if(esItemStlSubcttEngQList.size()>0){
                        esItemStlSubcttEngQ= esItemStlSubcttEngQList.get(0);
                        itemUnit.setEngPMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngQ.getBeginToCurrentPeriodEQty());
                        itemUnit.setEngPMng_CurrentPeriodEQty(esItemStlSubcttEngQ.getCurrentPeriodEQty());
                    }
                }

                BigDecimal bdContractUnitPriceInSubctt=itemUnit.getSubctt_ContractUnitPrice() == null ?
                        new BigDecimal(0) : itemUnit.getSubctt_ContractUnitPrice();

                if(itemUnit.getEngPMng_BeginToCurrentPeriodEQty() == null){
                    itemUnit.setEngPMng_BeginToCurrentPeriodAmt(null);
                }else{
                    BigDecimal bdBeginToCurrentPeriodEQty=itemUnit.getEngPMng_BeginToCurrentPeriodEQty();
                    itemUnit.setEngPMng_BeginToCurrentPeriodAmt(bdContractUnitPriceInSubctt.multiply(bdBeginToCurrentPeriodEQty));
                }

                if(itemUnit.getEngPMng_CurrentPeriodEQty() == null){
                    itemUnit.setEngPMng_CurrentPeriodAmt(null);
                }else{
                    BigDecimal bdCurrentPeriodEQty=itemUnit.getEngPMng_CurrentPeriodEQty();
                    itemUnit.setEngPMng_CurrentPeriodAmt(bdContractUnitPriceInSubctt.multiply(bdCurrentPeriodEQty));
                }

                EsItemStlSubcttEngP esItemStlSubcttEngPTemp=new EsItemStlSubcttEngP();
                esItemStlSubcttEngPTemp.setPeriodNo(esCommon.getStrDateThisPeriod());
                List<EsItemStlSubcttEngP> esItemStlSubcttEngPListTemp =
                        esItemStlSubcttEngPService.selectRecordsByDetailExample(esItemStlSubcttEngPTemp);
                if(esItemStlSubcttEngPListTemp.size()>0){
                    esItemStlSubcttEngPTemp= esItemStlSubcttEngPListTemp.get(0);
                    itemUnit.setEngPMng_Pkid(esItemStlSubcttEngPTemp.getPkid());
                    itemUnit.setEngPMng_PeriodNo(esItemStlSubcttEngPTemp.getPeriodNo());
                    itemUnit.setEngPMng_SubcttPkid(commStlSubcttEngH.getStrSubcttPkid());
                    itemUnit.setEngPMng_ItemPkid(esItemStlSubcttEngPTemp.getSubcttItemPkid());

                    itemUnit.setEngPMng_CurrentPeriodEQty(esItemStlSubcttEngPTemp.getCurrentPeriodEQty());
                    itemUnit.setEngPMng_CurrentPeriodAmt(esItemStlSubcttEngPTemp.getCurrentPeriodEAmt());
                    itemUnit.setEngPMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngPTemp.getBeginToCurrentPeriodEQty());
                    itemUnit.setEngPMng_BeginToCurrentPeriodAmt(esItemStlSubcttEngPTemp.getBeginToCurrentPeriodEAmt());

                    itemUnit.setEngPMng_DeletedFlag(esItemStlSubcttEngPTemp.getDeleteFlag());
                    itemUnit.setEngPMng_CreatedBy(esItemStlSubcttEngPTemp.getCreatedBy());
                    itemUnit.setEngPMng_CreatedDate(esItemStlSubcttEngPTemp.getCreatedDate());
                    itemUnit.setEngPMng_LastUpdBy(esItemStlSubcttEngPTemp.getLastUpdBy());
                    itemUnit.setEngPMng_LastUpdDate(esItemStlSubcttEngPTemp.getLastUpdDate());
                    itemUnit.setEngPMng_ModificationNum(esItemStlSubcttEngPTemp.getModificationNum());
                }
                //税金率
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("0")){
                    bdRates[0]=ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                }
                //质保金率
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("1")){
                    bdRates[1]= ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                }
                //付款率
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("2")){
                    bdRates[2]=ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                }
                //安全施工措施费率
                if(ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("3")){
                    bdRates[3]=ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                }
                //扣款用
                if(itemUnit.getSubctt_SignPartAPrice()!=null && itemUnit.getSubctt_SignPartAPrice().compareTo(bd0)!=0) {
                    progSubstlItemShowListForReduction.add(itemUnit);
                }
                bdCurrentPeriodTotalAmtTemp=bdCurrentPeriodTotalAmtTemp.add(
                        getBdIgnoreNull(itemUnit.getEngPMng_CurrentPeriodAmt()));
                bdBeginToCurrentPeriodTotalAmtTemp=bdBeginToCurrentPeriodTotalAmtTemp.add(
                        getBdIgnoreNull(itemUnit.getEngPMng_BeginToCurrentPeriodAmt()));
            }

            List<ProgSubstlItemShow> records0=new ArrayList<ProgSubstlItemShow>();
            records0.addAll(progSubstlItemShowList);
            beansMap.put("records0", records0);

            //1小计
            ProgSubstlItemShow stl1 = new ProgSubstlItemShow();
            stl1.setSubctt_Name("小计");
            stl1.setSubctt_Pkid("setl_1");
            stl1.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp);
            stl1.setEngPMng_BeginToCurrentPeriodAmt(bdBeginToCurrentPeriodTotalAmtTemp);
            progSubstlItemShowList.add(stl1);

            //2其中(安全施工措施费)
            ProgSubstlItemShow stl2 = new ProgSubstlItemShow();
            stl2.setSubctt_Name("其中:安全施工措施费");
            stl2.setSubctt_Pkid("setl_2");
            stl2.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[3]));
            stl2.setEngPMng_BeginToCurrentPeriodAmt(bdBeginToCurrentPeriodTotalAmtTemp.multiply(bdRates[3]));
            progSubstlItemShowList.add(stl2);

            //3扣款
            ProgSubstlItemShow stl3 = new ProgSubstlItemShow();
            //stl3.setSubctt_StrNo("7");
            stl3.setSubctt_Name("扣款(材料)");
            stl3.setSubctt_Pkid("setl_3");
            progSubstlItemShowList.add(stl3);

            BigDecimal bdTotalAmt=new BigDecimal(0);
            BigDecimal bdTotalAllAmt=new BigDecimal(0);
            List<ProgSubstlItemShow> records1=new ArrayList<ProgSubstlItemShow>();

            for(ProgSubstlItemShow itemUnit: progSubstlItemShowListForReduction){
                ProgSubstlItemShow progSubstlItemShowTemp =(ProgSubstlItemShow)BeanUtils.cloneBean(itemUnit) ;
                progSubstlItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getSubctt_SignPartAPrice());

                // 从工程材料结算中抽取结算数据
                EsItemStlSubcttEngM esItemStlSubcttEngM=new EsItemStlSubcttEngM();
                esItemStlSubcttEngM.setSubcttPkid(commStlSubcttEngH.getStrSubcttPkid());
                esItemStlSubcttEngM.setSubcttItemPkid(progSubstlItemShowTemp.getSubctt_ItemPkid());
                esItemStlSubcttEngM.setPeriodNo(esCommon.getStrDateThisPeriod());
                List<EsItemStlSubcttEngM> esItemStlSubcttEngMList = esItemStlSubcttEngMService.selectRecordsByExample(esItemStlSubcttEngM);
                if(esItemStlSubcttEngMList.size()>0){
                    esItemStlSubcttEngM= esItemStlSubcttEngMList.get(0);
                    progSubstlItemShowTemp.setEngPMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngM.getBeginToCurrentPeriodMQty());
                    progSubstlItemShowTemp.setEngPMng_CurrentPeriodEQty(esItemStlSubcttEngM.getCurrentPeriodMQty());

                    BigDecimal bdSubctt_ContractUnitPrice=ToolUtil.getBdIgnoreNull(
                            progSubstlItemShowTemp.getSubctt_ContractUnitPrice());
                    BigDecimal bdEngPMng_CurrentPeriodEQty=ToolUtil.getBdIgnoreNull(
                            progSubstlItemShowTemp.getEngPMng_CurrentPeriodEQty());
                    BigDecimal bdEngPMng_BeginToCurrentPeriodEQty=ToolUtil.getBdIgnoreNull(
                            progSubstlItemShowTemp.getEngPMng_BeginToCurrentPeriodEQty());

                    BigDecimal bdEngPMng_CurrentPeriodAmtTemp=bdSubctt_ContractUnitPrice.multiply(bdEngPMng_CurrentPeriodEQty);
                    BigDecimal bdEngPMng_BeginToCurrentPeriodAmtTemp=bdSubctt_ContractUnitPrice.multiply(bdEngPMng_BeginToCurrentPeriodEQty);

                    bdTotalAmt=bdTotalAmt.add(bdEngPMng_CurrentPeriodAmtTemp);
                    bdTotalAllAmt=bdTotalAllAmt.add(bdSubctt_ContractUnitPrice.multiply(bdEngPMng_BeginToCurrentPeriodEQty));

                    progSubstlItemShowTemp.setEngPMng_CurrentPeriodAmt(bdEngPMng_CurrentPeriodAmtTemp);
                    progSubstlItemShowTemp.setEngPMng_BeginToCurrentPeriodAmt(bdEngPMng_BeginToCurrentPeriodAmtTemp);
                }
                records1.add(progSubstlItemShowTemp);
                progSubstlItemShowList.add(progSubstlItemShowTemp);
            }

            beansMap.put("records1", records1);

            ProgSubstlItemShow stl4 = new ProgSubstlItemShow();
            //stl4.setSubctt_StrNo("8");
            stl4.setSubctt_Name("扣款(税)");
            stl4.setSubctt_Pkid("setl_4");
            stl4.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[0]));
            stl4.setEngPMng_BeginToCurrentPeriodAmt(bdBeginToCurrentPeriodTotalAmtTemp.multiply(bdRates[0]));
            progSubstlItemShowList.add(stl4);

            ProgSubstlItemShow stl5 = new ProgSubstlItemShow();
            stl5.setSubctt_Name("小计");
            stl5.setSubctt_Pkid("setl_5");
            stl5.setEngPMng_CurrentPeriodAmt(bdTotalAmt.add(stl4.getEngPMng_CurrentPeriodAmt()));
            stl5.setEngPMng_BeginToCurrentPeriodAmt(bdTotalAllAmt.add(stl4.getEngPMng_BeginToCurrentPeriodAmt()));
            progSubstlItemShowList.add(stl5);

            ProgSubstlItemShow stl6 = new ProgSubstlItemShow();
            stl6.setSubctt_Name("本期净结算额");
            stl6.setSubctt_Pkid("setl_6");
            stl6.setEngPMng_CurrentPeriodAmt(
                    stl1.getEngPMng_CurrentPeriodAmt().subtract(stl5.getEngPMng_CurrentPeriodAmt()));
            stl6.setEngPMng_BeginToCurrentPeriodAmt(
                    stl1.getEngPMng_BeginToCurrentPeriodAmt().subtract(stl5.getEngPMng_BeginToCurrentPeriodAmt()));
            progSubstlItemShowList.add(stl6);

            ProgSubstlItemShow stl7 = new ProgSubstlItemShow();
            //stl7.setSubctt_StrNo("9");
            stl7.setSubctt_Name("其它(质保金)");
            stl7.setSubctt_Pkid("setl_7");
            stl7.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            stl7.setEngPMng_BeginToCurrentPeriodAmt(bdBeginToCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            progSubstlItemShowList.add(stl7);

            ProgSubstlItemShow stl8 = new ProgSubstlItemShow();
            stl8.setSubctt_Name("小计");
            stl8.setSubctt_Pkid("setl_8");
            stl8.setEngPMng_CurrentPeriodAmt(stl7.getEngPMng_CurrentPeriodAmt());
            stl8.setEngPMng_BeginToCurrentPeriodAmt(stl7.getEngPMng_BeginToCurrentPeriodAmt());
            progSubstlItemShowList.add(stl8);

            ProgSubstlItemShow stl9 = new ProgSubstlItemShow();
            stl9.setSubctt_Name("合计(扣除其它栏款项后本期结算价值)");
            stl9.setSubctt_Pkid("setl_9");
            stl9.setEngPMng_CurrentPeriodAmt(
                    stl6.getEngPMng_CurrentPeriodAmt().subtract(stl8.getEngPMng_CurrentPeriodAmt()));
            stl9.setEngPMng_BeginToCurrentPeriodAmt(
                    stl6.getEngPMng_BeginToCurrentPeriodAmt().subtract(stl8.getEngPMng_BeginToCurrentPeriodAmt()));
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
            MessageUtil.addInfo("结算数据引取失败！" + e.getMessage());
        }
    }

    private BigDecimal getBdIgnoreNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?new BigDecimal(0):bigDecimalPara;
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
        List<EsCttItem> esCttItemListPara,
        List<ProgSubstlItemShow> sProgSubstlItemShowListPara){
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList =new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList =getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        for(EsCttItem itemUnit: subEsCttItemList){
            ProgSubstlItemShow progSubstlItemShowTemp = new ProgSubstlItemShow();
            progSubstlItemShowTemp.setSubctt_Pkid(itemUnit.getPkid());
            progSubstlItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progSubstlItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progSubstlItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progSubstlItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progSubstlItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progSubstlItemShowTemp.setSubctt_ItemPkid(itemUnit.getPkid());
            progSubstlItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());

            progSubstlItemShowTemp.setSubctt_Name(itemUnit.getName());
            progSubstlItemShowTemp.setSubctt_Note(itemUnit.getNote());

            progSubstlItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progSubstlItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progSubstlItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progSubstlItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progSubstlItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            sProgSubstlItemShowListPara.add(progSubstlItemShowTemp) ;
            recursiveDataTable(progSubstlItemShowTemp.getSubctt_Pkid(), esCttItemListPara, sProgSubstlItemShowListPara);
        }
    }

    public String onExportExcel()throws IOException,WriteException{
        if (this.progSubstlItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "分包工程预结算单-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap,"stlSubCttEngP.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }
    /*根据group和orderid临时编制编码strNo*/
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

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
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

   /* 智能字段Start*/
    public EsCttItemService getEsCttItemService() {
        return esCttItemService;
    }

    public void setEsCttItemService(EsCttItemService esCttItemService) {
        this.esCttItemService = esCttItemService;
    }

    public EsCommon getEsCommon() {
        return esCommon;
    }

    public void setEsCommon(EsCommon esCommon) {
        this.esCommon = esCommon;
    }

    public List<ProgSubstlItemShow> getProgSubstlItemShowList() {
        return progSubstlItemShowList;
    }

    public void setProgSubstlItemShowList(List<ProgSubstlItemShow> progSubstlItemShowList) {
        this.progSubstlItemShowList = progSubstlItemShowList;
    }

    public EsInitCustService getEsInitCustService() {
        return esInitCustService;
    }

    public void setEsInitCustService(EsInitCustService esInitCustService) {
        this.esInitCustService = esInitCustService;
    }

    public EsCttInfoService getEsCttInfoService() {
        return esCttInfoService;
    }

    public void setEsCttInfoService(EsCttInfoService esCttInfoService) {
        this.esCttInfoService = esCttInfoService;
    }

    public EsItemStlSubcttEngPService getEsItemStlSubcttEngPService() {
        return esItemStlSubcttEngPService;
    }

    public void setEsItemStlSubcttEngPService(EsItemStlSubcttEngPService esItemStlSubcttEngPService) {
        this.esItemStlSubcttEngPService = esItemStlSubcttEngPService;
    }

    public EsItemStlSubcttEngMService getEsItemStlSubcttEngMService() {
        return esItemStlSubcttEngMService;
    }

    public void setEsItemStlSubcttEngMService(EsItemStlSubcttEngMService esItemStlSubcttEngMService) {
        this.esItemStlSubcttEngMService = esItemStlSubcttEngMService;
    }

    public EsItemStlSubcttEngQService getEsItemStlSubcttEngQService() {
        return esItemStlSubcttEngQService;
    }

    public EsInitStlService getEsInitStlService() {
        return esInitStlService;
    }

    public void setEsInitStlService(EsInitStlService esInitStlService) {
        this.esInitStlService = esInitStlService;
    }

    public void setEsItemStlSubcttEngQService(EsItemStlSubcttEngQService esItemStlSubcttEngQService) {
        this.esItemStlSubcttEngQService = esItemStlSubcttEngQService;
    }

    public String getStrExportToExcelRendered() {
        return strExportToExcelRendered;
    }

    public void setStrExportToExcelRendered(String strExportToExcelRendered) {
        this.strExportToExcelRendered = strExportToExcelRendered;
    }

    public CommStlSubcttEngH getCommStlSubcttEngH() {
        return commStlSubcttEngH;
    }

    public void setCommStlSubcttEngH(CommStlSubcttEngH commStlSubcttEngH) {
        this.commStlSubcttEngH = commStlSubcttEngH;
    }
    /*智能字段End*/
}
