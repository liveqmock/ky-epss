package epss.view.item;

import epss.common.enums.ESEnum;
import epss.common.enums.ESEnumPreStatusFlag;
import epss.common.enums.ESEnumStatusFlag;
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
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
    @ManagedProperty(value = "#{esInitPowerService}")
    private EsInitPowerService esInitPowerService;

    private List<ProgSubstlItemShow> progSubstlItemShowList;
    private List<ProgSubstlItemShow> progSubstlItemShowListForApprove;
    private List<EsItemStlSubcttEngP> progSubstlItemShowListForAccountAndQry;
    private CommStlSubcttEngH commStlSubcttEngH;

    private Map beansMap;
    private EsInitStl esInitStl;

    /*所属号*/
    private String strEsInitStlPkid;
    private Date nowDate;

    // 画面上控件的显示控制
    private String strExportToExcelRendered;
    // 控制记账按钮的显示
    private String strAccountBtnRendered;
    private String strApproveBtnRendered;
    private String strApprovedNotBtnRendered;
    private String strSubmitType;

    @PostConstruct
    public void init() {
        nowDate = new Date();
        beansMap = new HashMap();
        commStlSubcttEngH = new CommStlSubcttEngH();
        Map parammap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parammap.containsKey("strEsInitStlPkid")) {
            strEsInitStlPkid = parammap.get("strEsInitStlPkid").toString();
        }
        if (parammap.containsKey("strSubmitType")) {
            strSubmitType = parammap.get("strSubmitType").toString();
        }
        initData();
    }

    /*初始化操作*/
    private void initData() {
        // 表头设定
        commStlSubcttEngH.setStrDate(PlatformService.dateFormat(nowDate, "yyyy-MM-dd"));
        /*分包合同数据*/
        List<EsCttItem> esCttItemList = new ArrayList<EsCttItem>();
        if ("Account".equals(strSubmitType)||"Qry".equals(strSubmitType)){
            progSubstlItemShowListForAccountAndQry = new ArrayList<EsItemStlSubcttEngP>();
        }
        if ("Approve".equals(strSubmitType)){
            progSubstlItemShowList = new ArrayList<ProgSubstlItemShow>();
            progSubstlItemShowListForApprove = new ArrayList<ProgSubstlItemShow>();
        }
        // From StlPkid To SubcttPkid
        esInitStl = esInitStlService.selectRecordsByPrimaryKey(strEsInitStlPkid);
        // 如果该分包合同价格结算已经记账，则在记账页面上不显示记账按钮
        EsInitPower esInitPowerTemp = new EsInitPower();
        esInitPowerTemp.setPowerType(esInitStl.getStlType());
        esInitPowerTemp.setPowerPkid(esInitStl.getStlPkid());
        esInitPowerTemp.setPeriodNo(esInitStl.getPeriodNo());
        EsInitPower esInitPower = esInitPowerService.selectByPrimaryKey(esInitPowerTemp);
        if (ESEnumStatusFlag.STATUS_FLAG3.getCode().equals(esInitPower.getStatusFlag())) {
            strApproveBtnRendered = "false";
            strApprovedNotBtnRendered = "true";
        } else {
            strApproveBtnRendered = "true";
            strApprovedNotBtnRendered = "false";
        }
        if (ESEnumStatusFlag.STATUS_FLAG4.getCode().equals(esInitPower.getStatusFlag())) {
            strAccountBtnRendered = "false";
        } else {
            strAccountBtnRendered = "true";
        }
        // Excel中的头信息
        commStlSubcttEngH.setStrSubcttPkid(esInitStl.getStlPkid());
        commStlSubcttEngH.setStrStlId(esInitStl.getId());
        // From SubcttPkid To CstplPkid
        EsCttInfo esCttInfo_Subctt = esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrSubcttPkid());
        commStlSubcttEngH.setStrCstplPkid(esCttInfo_Subctt.getParentPkid());
        commStlSubcttEngH.setStrSubcttId(esCttInfo_Subctt.getId());
        commStlSubcttEngH.setStrSubcttName(esCttInfo_Subctt.getName());
        commStlSubcttEngH.setStrSignPartPkid(esCttInfo_Subctt.getSignPartB());
        commStlSubcttEngH.setStrSignPartName(esInitCustService.getEsInitCustByPkid(
                commStlSubcttEngH.getStrSignPartPkid()).getName());
        // From CstplPkid To TkcttPkid
        EsCttInfo esCttInfo_Cstpl = esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrCstplPkid());
        commStlSubcttEngH.setStrTkcttPkid(esCttInfo_Cstpl.getParentPkid());
        commStlSubcttEngH.setStrCstplId(esCttInfo_Cstpl.getId());
        commStlSubcttEngH.setStrCstplName(esCttInfo_Cstpl.getName());

        EsCttInfo esCttInfo_Tkctt = esCttInfoService.getCttInfoByPkId(commStlSubcttEngH.getStrTkcttPkid());
        commStlSubcttEngH.setStrTkcttId(esCttInfo_Tkctt.getId());
        commStlSubcttEngH.setStrTkcttName(esCttInfo_Tkctt.getName());

        // 表内容设定
        //记账
        if ("Account".equals(strSubmitType)||"Qry".equals(strSubmitType)){
            EsItemStlSubcttEngP esItemStlSubcttEngPTemp=new EsItemStlSubcttEngP();
            esItemStlSubcttEngPTemp.setSubcttPkid(commStlSubcttEngH.getStrSubcttPkid());
            esItemStlSubcttEngPTemp.setPeriodNo(esInitStl.getPeriodNo());
            progSubstlItemShowListForAccountAndQry= esItemStlSubcttEngPService.selectRecordsByDetailExampleForAccount(esItemStlSubcttEngPTemp);
            return;
        }
        //批准
        esCttItemList = esCttItemService.getEsItemList(
                ESEnum.ITEMTYPE2.getCode(), commStlSubcttEngH.getStrSubcttPkid());
        if (esCttItemList.size() > 0) {
            strExportToExcelRendered = "true";
            recursiveDataTable("root", esCttItemList, progSubstlItemShowList);
            progSubstlItemShowList = getStlSubCttEngPMngConstructList_DoFromatNo(progSubstlItemShowList);

            /*价格结算数据添加*/
            setStlSubCttEngPMngConstructList_AddSettle();
        } else {
            strExportToExcelRendered = "false";
        }
    }

    /**
     * 根据权限进行审核
     */
    @Transactional
    public void accountAction() {
        try {
            esInitPowerService.accountAction(esInitStl);
            strAccountBtnRendered="false";
            MessageUtil.addInfo("结算数据记账成功！");
        } catch (Exception e) {
            logger.error("结算数据记账失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    private void setStlSubCttEngPMngConstructList_AddSettle() {
        try {
            BigDecimal bd0 = new BigDecimal(0);
            BigDecimal bdCurrentPeriodTotalAmtTemp = new BigDecimal(0);
            BigDecimal bdBeginToCurrentPeriodTotalAmtTemp = new BigDecimal(0);
            // 0:稅金率；1:质保金率；2:付款率；3:安全施工措施费率
            BigDecimal[] bdRates = {bd0, bd0, bd0, bd0};

            beansMap.put("commStlSubcttEngH", commStlSubcttEngH);

            List<ProgSubstlItemShow> progSubstlItemShowListForSubcttEngQ = new ArrayList<ProgSubstlItemShow>();
            List<ProgSubstlItemShow> progSubstlItemShowListForSubcttEngM = new ArrayList<ProgSubstlItemShow>();
            for (ProgSubstlItemShow itemUnit : progSubstlItemShowList) {
                ProgSubstlItemShow progSubstlItemShowForSubcttEngQ = (ProgSubstlItemShow) BeanUtils.cloneBean(itemUnit);
                ProgSubstlItemShow progSubstlItemShowForSubcttEngM = (ProgSubstlItemShow) BeanUtils.cloneBean(itemUnit);
                progSubstlItemShowListForSubcttEngQ.add(progSubstlItemShowForSubcttEngQ);
                progSubstlItemShowListForSubcttEngM.add(progSubstlItemShowForSubcttEngM);
            }
            progSubstlItemShowList.clear();
            ProgSubstlItemShow stl1 = new ProgSubstlItemShow();
            stl1.setSubctt_ItemPkid("stl1");
            stl1.setSubctt_ItemName("造价计算");
            stl1.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl1.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            progSubstlItemShowList.add(stl1);
            progSubstlItemShowListForApprove.add(stl1);

            for (ProgSubstlItemShow itemUnit : progSubstlItemShowListForSubcttEngQ) {
                // 从工程数量结算中抽取结算数据
                EsItemStlSubcttEngQ esItemStlSubcttEngQTemp = new EsItemStlSubcttEngQ();
                esItemStlSubcttEngQTemp.setSubcttPkid(commStlSubcttEngH.getStrSubcttPkid());
                esItemStlSubcttEngQTemp.setSubcttItemPkid(itemUnit.getSubctt_ItemPkid());
                esItemStlSubcttEngQTemp.setPeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlSubcttEngQ> esItemStlSubcttEngQList =
                        esItemStlSubcttEngQService.selectRecordsByExample(esItemStlSubcttEngQTemp);
                itemUnit.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
                itemUnit.setEngPMng_SubStlType(ESEnum.ITEMTYPE3.getCode());
                if (esItemStlSubcttEngQList.size() <= 0) {
                    if (itemUnit.getSubctt_SpareField() != null){
                        //税金率
                        if (ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("0")) {
                            bdRates[0] = ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                        }
                        //质保金率
                        if (ToolUtil.getStrIgnoreNull(itemUnit.getSubctt_SpareField()).equals("1")) {
                            bdRates[1] = ToolUtil.getBdIgnoreNull(itemUnit.getSubctt_ContractAmount());
                        }
                    } else {
                        if ("Approve".equals(strSubmitType)) {
                            progSubstlItemShowListForApprove.add(itemUnit);
                        }
                    }
                } else {
                    esItemStlSubcttEngQTemp = esItemStlSubcttEngQList.get(0);
                    itemUnit.setEngPMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngQTemp.getBeginToCurrentPeriodEQty());
                    itemUnit.setEngPMng_CurrentPeriodEQty(esItemStlSubcttEngQTemp.getCurrentPeriodEQty());

                    BigDecimal bdContractUnitPriceInSubctt = itemUnit.getSubctt_ContractUnitPrice() == null ?
                            new BigDecimal(0) : itemUnit.getSubctt_ContractUnitPrice();

                    if (itemUnit.getEngPMng_BeginToCurrentPeriodEQty() == null) {
                        itemUnit.setEngPMng_BeginToCurrentPeriodAmt(null);
                    } else {
                        BigDecimal bdBeginToCurrentPeriodEQty = itemUnit.getEngPMng_BeginToCurrentPeriodEQty();
                        itemUnit.setEngPMng_BeginToCurrentPeriodAmt(bdContractUnitPriceInSubctt.multiply(bdBeginToCurrentPeriodEQty));
                    }

                    if (itemUnit.getEngPMng_CurrentPeriodEQty() == null) {
                        itemUnit.setEngPMng_CurrentPeriodAmt(null);
                    } else {
                        BigDecimal bdCurrentPeriodEQty = itemUnit.getEngPMng_CurrentPeriodEQty();
                        itemUnit.setEngPMng_CurrentPeriodAmt(bdContractUnitPriceInSubctt.multiply(bdCurrentPeriodEQty));
                    }
                    bdCurrentPeriodTotalAmtTemp = bdCurrentPeriodTotalAmtTemp.add(
                            getBdIgnoreNull(itemUnit.getEngPMng_CurrentPeriodAmt()));
                    bdBeginToCurrentPeriodTotalAmtTemp = bdBeginToCurrentPeriodTotalAmtTemp.add(
                            getBdIgnoreNull(itemUnit.getEngPMng_BeginToCurrentPeriodAmt()));
                    progSubstlItemShowList.add(itemUnit);
                    progSubstlItemShowListForApprove.add(itemUnit);
                }
            }

            List<ProgSubstlItemShow> records0 = new ArrayList<ProgSubstlItemShow>();
            records0.addAll(progSubstlItemShowList);
            beansMap.put("records0", records0);

            //1小计
            ProgSubstlItemShow stl2 = new ProgSubstlItemShow();
            stl2.setSubctt_ItemPkid("stl2");
            stl2.setSubctt_ItemName("小计");
            stl2.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl2.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl2.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp);
            stl2.setEngPMng_BeginToCurrentPeriodAmt(bdBeginToCurrentPeriodTotalAmtTemp);
            progSubstlItemShowList.add(stl2);
            progSubstlItemShowListForApprove.add(stl2);

            //3扣款
            ProgSubstlItemShow stl3 = new ProgSubstlItemShow();
            stl3.setSubctt_ItemPkid("stl3");
            stl3.setSubctt_ItemName("扣款(材料)");
            stl3.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl3.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            progSubstlItemShowList.add(stl3);
            progSubstlItemShowListForApprove.add(stl3);

            BigDecimal bdTotalAmt = new BigDecimal(0);
            BigDecimal bdTotalAllAmt = new BigDecimal(0);
            List<ProgSubstlItemShow> records1 = new ArrayList<ProgSubstlItemShow>();

            for (ProgSubstlItemShow itemUnit : progSubstlItemShowListForSubcttEngM) {
                // 从工程材料结算中抽取结算数据
                EsItemStlSubcttEngM esItemStlSubcttEngM = new EsItemStlSubcttEngM();
                esItemStlSubcttEngM.setSubcttPkid(commStlSubcttEngH.getStrSubcttPkid());
                esItemStlSubcttEngM.setSubcttItemPkid(itemUnit.getSubctt_ItemPkid());
                esItemStlSubcttEngM.setPeriodNo(esInitStl.getPeriodNo());
                List<EsItemStlSubcttEngM> esItemStlSubcttEngMList =
                        esItemStlSubcttEngMService.selectRecordsByExample(esItemStlSubcttEngM);
                itemUnit.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
                itemUnit.setEngPMng_SubStlType(ESEnum.ITEMTYPE4.getCode());
                if (esItemStlSubcttEngMList.size() <= 0) {
                    if ("Approve".equals(strSubmitType)) {
                        progSubstlItemShowListForApprove.add(itemUnit);
                    }
                } else {
                    esItemStlSubcttEngM = esItemStlSubcttEngMList.get(0);
                    itemUnit.setSubctt_ContractUnitPrice(itemUnit.getSubctt_SignPartAPrice());
                    itemUnit.setEngPMng_BeginToCurrentPeriodEQty(esItemStlSubcttEngM.getBeginToCurrentPeriodMQty());
                    itemUnit.setEngPMng_CurrentPeriodEQty(esItemStlSubcttEngM.getCurrentPeriodMQty());

                    BigDecimal bdSubctt_ContractUnitPrice = ToolUtil.getBdIgnoreNull(
                            itemUnit.getSubctt_ContractUnitPrice());
                    BigDecimal bdEngPMng_CurrentPeriodEQty = ToolUtil.getBdIgnoreNull(
                            itemUnit.getEngPMng_CurrentPeriodEQty());
                    BigDecimal bdEngPMng_BeginToCurrentPeriodEQty = ToolUtil.getBdIgnoreNull(
                            itemUnit.getEngPMng_BeginToCurrentPeriodEQty());

                    BigDecimal bdEngPMng_CurrentPeriodAmtTemp = bdSubctt_ContractUnitPrice.multiply(bdEngPMng_CurrentPeriodEQty);
                    BigDecimal bdEngPMng_BeginToCurrentPeriodAmtTemp = bdSubctt_ContractUnitPrice.multiply(bdEngPMng_BeginToCurrentPeriodEQty);

                    bdTotalAmt = bdTotalAmt.add(bdEngPMng_CurrentPeriodAmtTemp);
                    bdTotalAllAmt = bdTotalAllAmt.add(bdSubctt_ContractUnitPrice.multiply(bdEngPMng_BeginToCurrentPeriodEQty));

                    itemUnit.setEngPMng_CurrentPeriodAmt(
                            ToolUtil.getBdFrom0ToNull(bdEngPMng_CurrentPeriodAmtTemp));
                    itemUnit.setEngPMng_BeginToCurrentPeriodAmt(
                            ToolUtil.getBdFrom0ToNull(bdEngPMng_BeginToCurrentPeriodAmtTemp));
                    progSubstlItemShowList.add(itemUnit);
                    progSubstlItemShowListForApprove.add(itemUnit);
                    records1.add(itemUnit);
                }
            }

            beansMap.put("records1", records1);

            ProgSubstlItemShow stl4 = new ProgSubstlItemShow();
            stl4.setSubctt_ItemPkid("stl4");
            stl4.setSubctt_ItemName("扣款(税)");
            stl4.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl4.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl4.setSubctt_ContractUnitPrice(bdRates[0]);
            stl4.setEngPMng_CurrentPeriodAmt(stl2.getEngPMng_CurrentPeriodAmt().multiply(bdRates[0]));
            stl4.setEngPMng_BeginToCurrentPeriodAmt(stl2.getEngPMng_CurrentPeriodAmt().multiply(bdRates[0]));
            progSubstlItemShowList.add(stl4);
            progSubstlItemShowListForApprove.add(stl4);

            ProgSubstlItemShow stl5 = new ProgSubstlItemShow();
            stl5.setSubctt_ItemPkid("stl5");
            stl5.setSubctt_ItemName("小计");
            stl5.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl5.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl5.setEngPMng_CurrentPeriodAmt(bdTotalAmt.add(stl4.getEngPMng_CurrentPeriodAmt()));
            stl5.setEngPMng_BeginToCurrentPeriodAmt(bdTotalAllAmt.add(stl4.getEngPMng_BeginToCurrentPeriodAmt()));
            progSubstlItemShowList.add(stl5);
            progSubstlItemShowListForApprove.add(stl5);

            ProgSubstlItemShow stl6 = new ProgSubstlItemShow();
            stl6.setSubctt_ItemPkid("stl6");
            stl6.setSubctt_ItemName("本期净结算额");
            stl6.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl6.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl6.setEngPMng_CurrentPeriodAmt(
                    stl2.getEngPMng_CurrentPeriodAmt().subtract(stl5.getEngPMng_CurrentPeriodAmt()));
            stl6.setEngPMng_BeginToCurrentPeriodAmt(
                    stl2.getEngPMng_BeginToCurrentPeriodAmt().subtract(stl5.getEngPMng_BeginToCurrentPeriodAmt()));
            progSubstlItemShowList.add(stl6);
            progSubstlItemShowListForApprove.add(stl6);

            ProgSubstlItemShow stl7 = new ProgSubstlItemShow();
            stl7.setSubctt_ItemPkid("stl7");
            stl7.setSubctt_ItemName("其它(质保金)");
            stl7.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl7.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl7.setSubctt_ContractUnitPrice(bdRates[1]);
            stl7.setEngPMng_CurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            stl7.setEngPMng_BeginToCurrentPeriodAmt(bdCurrentPeriodTotalAmtTemp.multiply(bdRates[1]));
            progSubstlItemShowList.add(stl7);
            progSubstlItemShowListForApprove.add(stl7);

            ProgSubstlItemShow stl8 = new ProgSubstlItemShow();
            stl8.setSubctt_ItemPkid("stl8");
            stl8.setSubctt_ItemName("小计");
            stl8.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl8.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl8.setEngPMng_CurrentPeriodAmt(stl7.getEngPMng_CurrentPeriodAmt());
            stl8.setEngPMng_BeginToCurrentPeriodAmt(stl7.getEngPMng_BeginToCurrentPeriodAmt());
            progSubstlItemShowList.add(stl8);
            progSubstlItemShowListForApprove.add(stl8);

            ProgSubstlItemShow stl9 = new ProgSubstlItemShow();
            stl9.setSubctt_ItemPkid("stl9");
            stl9.setSubctt_ItemName("合计(扣除其它栏款项后本期结算价值)");
            stl9.setSubctt_Pkid(commStlSubcttEngH.getStrSubcttPkid());
            stl9.setEngPMng_PeriodNo(esInitStl.getPeriodNo());
            stl9.setEngPMng_CurrentPeriodAmt(
                    stl6.getEngPMng_CurrentPeriodAmt().subtract(stl8.getEngPMng_CurrentPeriodAmt()));
            stl9.setEngPMng_BeginToCurrentPeriodAmt(
                    stl6.getEngPMng_BeginToCurrentPeriodAmt().subtract(stl8.getEngPMng_BeginToCurrentPeriodAmt()));
            progSubstlItemShowList.add(stl9);
            progSubstlItemShowListForApprove.add(stl9);
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

    private BigDecimal getBdIgnoreNull(BigDecimal bigDecimalPara) {
        return bigDecimalPara == null ? new BigDecimal(0) : bigDecimalPara;
    }

    /*根据数据库中层级关系数据列表得到总包合同*/
    private void recursiveDataTable(String strLevelParentId,
                                    List<EsCttItem> esCttItemListPara,
                                    List<ProgSubstlItemShow> sProgSubstlItemShowListPara) {
        // 根据父层级号获得该父层级下的子节点
        List<EsCttItem> subEsCttItemList = new ArrayList<EsCttItem>();
        // 通过父层id查找它的孩子
        subEsCttItemList = getEsItemHieRelapListByLevelParentPkid(strLevelParentId, esCttItemListPara);
        for (EsCttItem itemUnit : subEsCttItemList) {
            ProgSubstlItemShow progSubstlItemShowTemp = new ProgSubstlItemShow();
            progSubstlItemShowTemp.setSubctt_Pkid(itemUnit.getBelongToPkid());
            progSubstlItemShowTemp.setSubctt_Name(commStlSubcttEngH.getStrSubcttName());
            progSubstlItemShowTemp.setSubctt_BelongToType(itemUnit.getBelongToType());
            progSubstlItemShowTemp.setSubctt_BelongToPkid(itemUnit.getBelongToPkid());
            progSubstlItemShowTemp.setSubctt_ParentPkid(itemUnit.getParentPkid());
            progSubstlItemShowTemp.setSubctt_Grade(itemUnit.getGrade());
            progSubstlItemShowTemp.setSubctt_Orderid(itemUnit.getOrderid());
            progSubstlItemShowTemp.setSubctt_ItemPkid(itemUnit.getPkid());
            progSubstlItemShowTemp.setSubctt_CorrespondingPkid(itemUnit.getCorrespondingPkid());
            progSubstlItemShowTemp.setSubctt_ItemName(itemUnit.getName());
            progSubstlItemShowTemp.setSubctt_Unit(itemUnit.getUnit());
            progSubstlItemShowTemp.setSubctt_ContractUnitPrice(itemUnit.getContractUnitPrice());
            progSubstlItemShowTemp.setSubctt_ContractQuantity(itemUnit.getContractQuantity());
            progSubstlItemShowTemp.setSubctt_ContractAmount(itemUnit.getContractAmount());
            progSubstlItemShowTemp.setSubctt_SignPartAPrice(itemUnit.getSignPartAPrice());
            progSubstlItemShowTemp.setSubctt_Note(itemUnit.getNote());
            progSubstlItemShowTemp.setSubctt_SpareField(itemUnit.getSpareField());
            sProgSubstlItemShowListPara.add(progSubstlItemShowTemp);
            recursiveDataTable(progSubstlItemShowTemp.getSubctt_ItemPkid(), esCttItemListPara, sProgSubstlItemShowListPara);
        }
    }

    public String onExportExcel() throws IOException, WriteException {
        if (this.progSubstlItemShowList.size() == 0) {
            MessageUtil.addWarn("记录为空...");
            return null;
        } else {
            String excelFilename = "分包工程预结算单-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xls";
            JxlsManager jxls = new JxlsManager();
            jxls.exportList(excelFilename, beansMap, "stlSubCttEngP.xls");
            // 其他状态的票据需要添加时再修改导出文件名
        }
        return null;
    }

    /*根据group和orderid临时编制编码strNo*/
    private List<ProgSubstlItemShow> getStlSubCttEngPMngConstructList_DoFromatNo(
            List<ProgSubstlItemShow> progSubstlItemShowListPara) {
        String strTemp = "";
        Integer intBeforeGrade = -1;
        for (ProgSubstlItemShow itemUnit : progSubstlItemShowListPara) {
            if (itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                if (strTemp.lastIndexOf(".") < 0) {
                    strTemp = itemUnit.getSubctt_Orderid().toString();
                } else {
                    strTemp = strTemp.substring(0, strTemp.lastIndexOf(".")) + "." + itemUnit.getSubctt_Orderid().toString();
                }
            } else {
                if (itemUnit.getSubctt_Grade() == 1) {
                    strTemp = itemUnit.getSubctt_Orderid().toString();
                } else {
                    if (!itemUnit.getSubctt_Grade().equals(intBeforeGrade)) {
                        if (itemUnit.getSubctt_Grade().compareTo(intBeforeGrade) > 0) {
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        } else {
                            Integer intTemp = strTemp.indexOf(".", itemUnit.getSubctt_Grade() - 1);
                            strTemp = strTemp.substring(0, intTemp);
                            strTemp = strTemp + "." + itemUnit.getSubctt_Orderid().toString();
                        }
                    }
                }
            }
            intBeforeGrade = itemUnit.getSubctt_Grade();
            itemUnit.setSubctt_StrNo(ToolUtil.padLeftSpace_DoLevel(itemUnit.getSubctt_Grade(), strTemp));
        }
        return progSubstlItemShowListPara;
    }

    /*根据数据库中层级关系数据列表得到某一节点下的子节点*/
    private List<EsCttItem> getEsItemHieRelapListByLevelParentPkid(String strLevelParentPkid,
                                                                   List<EsCttItem> esCttItemListPara) {
        List<EsCttItem> tempEsCttItemList = new ArrayList<EsCttItem>();
        /*避开重复链接数据库*/
        for (EsCttItem itemUnit : esCttItemListPara) {
            if (strLevelParentPkid.equalsIgnoreCase(itemUnit.getParentPkid())) {
                tempEsCttItemList.add(itemUnit);
            }
        }
        return tempEsCttItemList;
    }

    /**
     * 根据权限进行审核
     * @param strPowerType
     */
    @Transactional
    public void onClickForPowerAction(String strPowerType){
        try {
                // 批准
                if(strPowerType.contains("Approve")){
                    if(strPowerType.equals("ApprovePass")){
                        EsInitPower esInitPower = new EsInitPower();
                        esInitPower.setPowerType(esInitStl.getStlType());
                        esInitPower.setPowerPkid(esInitStl.getStlPkid());
                        esInitPower.setPeriodNo(esInitStl.getPeriodNo());
                        EsInitPower esInitPowerTemp = esInitPowerService.selectByPrimaryKey(esInitPower);
                        // 价格结算
                        if(!ESEnum.ITEMTYPE5.getCode().equals(esInitPowerTemp.getPowerType())){
                            EsInitStl esInitStlTemp= (EsInitStl) BeanUtils.cloneBean(esInitStl);
                            esInitStlTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
                            esInitStlTemp.setId(getMaxIdPlusOne());
                            // 结算登记表更新
                            esInitStlService.insertRecord(esInitStlTemp);
                            //Power表更新
                            esInitPower.setPowerType(ESEnum.ITEMTYPE5.getCode());
                            esInitPower.setPeriodNo(esInitStl.getPeriodNo());
                            esInitPower.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG3.getCode());
                            esInitPower.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG5.getCode());
                            esInitPowerService.insertRecordByStl(esInitPower);
                            strApproveBtnRendered="false";
                            strApprovedNotBtnRendered="true";
                            //将价格结算的完整数据插入至es_item_stl_subctt_eng_p表
                            for (ProgSubstlItemShow itemUnit:progSubstlItemShowListForApprove){
                                esItemStlSubcttEngPService.insertRecordDetail(itemUnit);
                            }
                        }
                    }else if(strPowerType.equals("ApproveFailToQ")){
                        // 结算登记表更新
                        EsInitStl esInitStlTemp= (EsInitStl) BeanUtils.cloneBean(esInitStl);
                        esInitStlTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
                        esInitStlService.deleteRecord(esInitStlTemp);
                        // 这样写可以实现越级退回
                        EsInitPower esInitPowerTemp = new EsInitPower();
                        esInitPowerTemp.setPowerType(ESEnum.ITEMTYPE3.getCode());
                        esInitPowerTemp.setPowerPkid(esInitStl.getStlPkid());
                        esInitPowerTemp.setPeriodNo(esInitStl.getPeriodNo());
                        EsInitPower esInitPower = esInitPowerService.selectByPrimaryKey(esInitPowerTemp);

                        esInitPower.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                        esInitPower.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
                        esInitPowerService.updateRecordByPower(esInitPower);
                        strApproveBtnRendered="false";
                        strApprovedNotBtnRendered="false";
                    }
                    else if(strPowerType.equals("ApproveFailToM")){
                        // 结算登记表更新
                        EsInitStl esInitStlTemp= (EsInitStl) BeanUtils.cloneBean(esInitStl);
                        esInitStlTemp.setStlType(ESEnum.ITEMTYPE5.getCode());
                        esInitStlService.deleteRecord(esInitStlTemp);
                        // 这样写可以实现越级退回
                        EsInitPower esInitPowerTemp = new EsInitPower();
                        esInitPowerTemp.setPowerType(ESEnum.ITEMTYPE4.getCode());
                        esInitPowerTemp.setPowerPkid(esInitStl.getStlPkid());
                        esInitPowerTemp.setPeriodNo(esInitStl.getPeriodNo());
                        EsInitPower esInitPower = esInitPowerService.selectByPrimaryKey(esInitPowerTemp);

                        esInitPower.setStatusFlag(ESEnumStatusFlag.STATUS_FLAG1.getCode());
                        esInitPower.setPreStatusFlag(ESEnumPreStatusFlag.PRE_STATUS_FLAG6.getCode());
                        esInitPowerService.updateRecordByPower(esInitPower);
                        strApproveBtnRendered="false";
                        strApprovedNotBtnRendered="false";
                    }
                    MessageUtil.addInfo("批准数据完成。");
            }
        } catch (Exception e) {
            logger.error("批准数据失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }
    private String getMaxIdPlusOne(){
        try {
            Integer intTemp;
            String strMaxTkStlId= esInitStlService.getStrMaxStlId(esInitStl.getStlType());
            if(StringUtils.isEmpty(ToolUtil.getStrIgnoreNull(strMaxTkStlId))){
                strMaxTkStlId="STLP"+ esCommon.getStrToday()+"001";
            }
            else{
                if(strMaxTkStlId .length()>3){
                    String strTemp=strMaxTkStlId.substring(strMaxTkStlId .length() -3).replaceFirst("^0+","");
                    if(ToolUtil.strIsDigit(strTemp)){
                        intTemp=Integer.parseInt(strTemp) ;
                        intTemp=intTemp+1;
                        strMaxTkStlId=strMaxTkStlId.substring(0,strMaxTkStlId.length()-3)+StringUtils.leftPad(intTemp.toString(),3,"0");
                    }else{
                        strMaxTkStlId+="001";
                    }
                }
            }
            return strMaxTkStlId ;
        } catch (Exception e) {
            logger.error("结算信息查询失败", e);
            MessageUtil.addError("结算信息查询失败");
            return "";
        }
    }


    /* 智能字段Start*/
    public EsInitPowerService getEsInitPowerService() {
        return esInitPowerService;
    }

    public void setEsInitPowerService(EsInitPowerService esInitPowerService) {
        this.esInitPowerService = esInitPowerService;
    }

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

    public String getStrAccountBtnRendered() {
        return strAccountBtnRendered;
    }

    public void setStrAccountBtnRendered(String strAccountBtnRendered) {
        this.strAccountBtnRendered = strAccountBtnRendered;
    }

    public String getStrSubmitType() {
        return strSubmitType;
    }

    public void setStrSubmitType(String strSubmitType) {
        this.strSubmitType = strSubmitType;
    }
    public String getStrApproveBtnRendered() {
        return strApproveBtnRendered;
    }

    public void setStrApproveBtnRendered(String strApproveBtnRendered) {
        this.strApproveBtnRendered = strApproveBtnRendered;
    }

    public String getStrApprovedNotBtnRendered() {
        return strApprovedNotBtnRendered;
    }

    public void setStrApprovedNotBtnRendered(String strApprovedNotBtnRendered) {
        this.strApprovedNotBtnRendered = strApprovedNotBtnRendered;
    }

    public List<EsItemStlSubcttEngP> getProgSubstlItemShowListForAccountAndQry() {
        return progSubstlItemShowListForAccountAndQry;
    }

    public void setProgSubstlItemShowListForAccountAndQry(List<EsItemStlSubcttEngP> progSubstlItemShowListForAccountAndQry) {
        this.progSubstlItemShowListForAccountAndQry = progSubstlItemShowListForAccountAndQry;
    }

/*智能字段End*/
}
