package epss.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */

public class QryTkMeaCSStlQShow implements Serializable {
    // 总包合同
    private String strTkctt_Pkid;
    private String strTkctt_ParentPkid;
    private String strTkctt_No;
    private String strTkctt_Name;
    private String strTkctt_Unit;
    private BigDecimal bdTkctt_ContractUnitPrice;
    private BigDecimal bdTkctt_ContractQuantity;
    private BigDecimal bdTkctt_ContractAmount;

    // 成本计划
    private String strCstpl_Pkid;
    private String strCstpl_No;
    private BigDecimal bdCstpl_ContractUnitPrice;
    private BigDecimal bdCstpl_ContractQuantity;
    private BigDecimal bdCstpl_ContractAmount;

    // 当期计量结算工程数量
    private BigDecimal bdTkcttStl_CurrentPeriodMeaQuantity;
    // 当期计量结算开累工程数量
    private BigDecimal bdTkcttStl_BeginToCurrentPeriodMeaQuantity;
    // 当期计量结算工程金额
    private BigDecimal bdTkcttStl_CurrentPeriodMeaAmount;
    // 当期计量结算开累工程金额
    private BigDecimal bdTkcttStl_BeginToCurrentPeriodMeaAmount;

    // 当期分包结算工程数量
    private BigDecimal bdSubcttStl_CurrentPeriodQQty;
    // 当期分包结算开累工程数量
    private BigDecimal bdSubcttStl_BeginToCurrentPeriodQQty;
    // 当期分包结算工程金额
    private BigDecimal bdSubcttStl_CurrentPeriodAmount;
    // 当期分包结算开累工程金额
    private BigDecimal bdSubcttStl_BeginToCurrentPeriodAmount;

    private String strSubctt_SignPartName;
    // 数量差
    private BigDecimal bdMeaS_BeginToCurrentPeriodQQty;
    // 金额差
    private BigDecimal bdMeaS_BeginToCurrentPeriodAmount;

    private String strNote;

    public String getStrTkctt_Pkid() {
        return strTkctt_Pkid;
    }

    public void setStrTkctt_Pkid(String strTkctt_Pkid) {
        this.strTkctt_Pkid = strTkctt_Pkid;
    }

    public String getStrTkctt_ParentPkid() {
        return strTkctt_ParentPkid;
    }

    public void setStrTkctt_ParentPkid(String strTkctt_ParentPkid) {
        this.strTkctt_ParentPkid = strTkctt_ParentPkid;
    }

    public String getStrTkctt_No() {
        return strTkctt_No;
    }

    public void setStrTkctt_No(String strTkctt_No) {
        this.strTkctt_No = strTkctt_No;
    }

    public String getStrTkctt_Name() {
        return strTkctt_Name;
    }

    public void setStrTkctt_Name(String strTkctt_Name) {
        this.strTkctt_Name = strTkctt_Name;
    }

    public String getStrTkctt_Unit() {
        return strTkctt_Unit;
    }

    public void setStrTkctt_Unit(String strTkctt_Unit) {
        this.strTkctt_Unit = strTkctt_Unit;
    }

    public BigDecimal getBdTkctt_ContractQuantity() {
        return bdTkctt_ContractQuantity;
    }

    public void setBdTkctt_ContractQuantity(BigDecimal bdTkctt_ContractQuantity) {
        this.bdTkctt_ContractQuantity = bdTkctt_ContractQuantity;
    }

    public String getStrCstpl_Pkid() {
        return strCstpl_Pkid;
    }

    public void setStrCstpl_Pkid(String strCstpl_Pkid) {
        this.strCstpl_Pkid = strCstpl_Pkid;
    }

    public String getStrCstpl_No() {
        return strCstpl_No;
    }

    public void setStrCstpl_No(String strCstpl_No) {
        this.strCstpl_No = strCstpl_No;
    }

    public BigDecimal getBdCstpl_ContractQuantity() {
        return bdCstpl_ContractQuantity;
    }

    public void setBdCstpl_ContractQuantity(BigDecimal bdCstpl_ContractQuantity) {
        this.bdCstpl_ContractQuantity = bdCstpl_ContractQuantity;
    }

    public BigDecimal getBdTkcttStl_CurrentPeriodMeaQuantity() {
        return bdTkcttStl_CurrentPeriodMeaQuantity;
    }

    public void setBdTkcttStl_CurrentPeriodMeaQuantity(BigDecimal bdTkcttStl_CurrentPeriodMeaQuantity) {
        this.bdTkcttStl_CurrentPeriodMeaQuantity = bdTkcttStl_CurrentPeriodMeaQuantity;
    }

    public BigDecimal getBdTkcttStl_BeginToCurrentPeriodMeaQuantity() {
        return bdTkcttStl_BeginToCurrentPeriodMeaQuantity;
    }

    public void setBdTkcttStl_BeginToCurrentPeriodMeaQuantity(BigDecimal bdTkcttStl_BeginToCurrentPeriodMeaQuantity) {
        this.bdTkcttStl_BeginToCurrentPeriodMeaQuantity = bdTkcttStl_BeginToCurrentPeriodMeaQuantity;
    }

    public BigDecimal getBdSubcttStl_CurrentPeriodQQty() {
        return bdSubcttStl_CurrentPeriodQQty;
    }

    public void setBdSubcttStl_CurrentPeriodQQty(BigDecimal bdSubcttStl_CurrentPeriodQQty) {
        this.bdSubcttStl_CurrentPeriodQQty = bdSubcttStl_CurrentPeriodQQty;
    }

    public BigDecimal getBdSubcttStl_BeginToCurrentPeriodQQty() {
        return bdSubcttStl_BeginToCurrentPeriodQQty;
    }

    public void setBdSubcttStl_BeginToCurrentPeriodQQty(BigDecimal bdSubcttStl_BeginToCurrentPeriodQQty) {
        this.bdSubcttStl_BeginToCurrentPeriodQQty = bdSubcttStl_BeginToCurrentPeriodQQty;
    }

    public String getStrSubctt_SignPartName() {
        return strSubctt_SignPartName;
    }

    public void setStrSubctt_SignPartName(String strSubctt_SignPartName) {
        this.strSubctt_SignPartName = strSubctt_SignPartName;
    }

    public BigDecimal getBdMeaS_BeginToCurrentPeriodQQty() {
        return bdMeaS_BeginToCurrentPeriodQQty;
    }

    public void setBdMeaS_BeginToCurrentPeriodQQty(BigDecimal bdMeaS_BeginToCurrentPeriodQQty) {
        this.bdMeaS_BeginToCurrentPeriodQQty = bdMeaS_BeginToCurrentPeriodQQty;
    }

    public String getStrNote() {
        return strNote;
    }

    public void setStrNote(String strNote) {
        this.strNote = strNote;
    }

    public BigDecimal getBdTkctt_ContractUnitPrice() {
        return bdTkctt_ContractUnitPrice;
    }

    public void setBdTkctt_ContractUnitPrice(BigDecimal bdTkctt_ContractUnitPrice) {
        this.bdTkctt_ContractUnitPrice = bdTkctt_ContractUnitPrice;
    }

    public BigDecimal getBdTkctt_ContractAmount() {
        return bdTkctt_ContractAmount;
    }

    public void setBdTkctt_ContractAmount(BigDecimal bdTkctt_ContractAmount) {
        this.bdTkctt_ContractAmount = bdTkctt_ContractAmount;
    }

    public BigDecimal getBdCstpl_ContractUnitPrice() {
        return bdCstpl_ContractUnitPrice;
    }

    public void setBdCstpl_ContractUnitPrice(BigDecimal bdCstpl_ContractUnitPrice) {
        this.bdCstpl_ContractUnitPrice = bdCstpl_ContractUnitPrice;
    }

    public BigDecimal getBdCstpl_ContractAmount() {
        return bdCstpl_ContractAmount;
    }

    public void setBdCstpl_ContractAmount(BigDecimal bdCstpl_ContractAmount) {
        this.bdCstpl_ContractAmount = bdCstpl_ContractAmount;
    }

    public BigDecimal getBdTkcttStl_CurrentPeriodMeaAmount() {
        return bdTkcttStl_CurrentPeriodMeaAmount;
    }

    public void setBdTkcttStl_CurrentPeriodMeaAmount(BigDecimal bdTkcttStl_CurrentPeriodMeaAmount) {
        this.bdTkcttStl_CurrentPeriodMeaAmount = bdTkcttStl_CurrentPeriodMeaAmount;
    }

    public BigDecimal getBdTkcttStl_BeginToCurrentPeriodMeaAmount() {
        return bdTkcttStl_BeginToCurrentPeriodMeaAmount;
    }

    public void setBdTkcttStl_BeginToCurrentPeriodMeaAmount(BigDecimal bdTkcttStl_BeginToCurrentPeriodMeaAmount) {
        this.bdTkcttStl_BeginToCurrentPeriodMeaAmount = bdTkcttStl_BeginToCurrentPeriodMeaAmount;
    }

    public BigDecimal getBdSubcttStl_CurrentPeriodAmount() {
        return bdSubcttStl_CurrentPeriodAmount;
    }

    public void setBdSubcttStl_CurrentPeriodAmount(BigDecimal bdSubcttStl_CurrentPeriodAmount) {
        this.bdSubcttStl_CurrentPeriodAmount = bdSubcttStl_CurrentPeriodAmount;
    }

    public BigDecimal getBdSubcttStl_BeginToCurrentPeriodAmount() {
        return bdSubcttStl_BeginToCurrentPeriodAmount;
    }

    public void setBdSubcttStl_BeginToCurrentPeriodAmount(BigDecimal bdSubcttStl_BeginToCurrentPeriodAmount) {
        this.bdSubcttStl_BeginToCurrentPeriodAmount = bdSubcttStl_BeginToCurrentPeriodAmount;
    }

    public BigDecimal getBdMeaS_BeginToCurrentPeriodAmount() {
        return bdMeaS_BeginToCurrentPeriodAmount;
    }

    public void setBdMeaS_BeginToCurrentPeriodAmount(BigDecimal bdMeaS_BeginToCurrentPeriodAmount) {
        this.bdMeaS_BeginToCurrentPeriodAmount = bdMeaS_BeginToCurrentPeriodAmount;
    }
}