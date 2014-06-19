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
    private BigDecimal bdTkctt_UnitPrice;
    private BigDecimal bdTkctt_Qty;
    private BigDecimal bdTkctt_Amt;

    // 成本计划
    private String strCstpl_Pkid;
    private BigDecimal bdCstpl_UnitPrice;
    private BigDecimal bdCstpl_Qty;
    private BigDecimal bdCstpl_Amt;

    // 当期计量结算工程数量
    private BigDecimal bdTkcttStl_CurrentPeriodMeaQuantity;
    // 当期计量结算开累工程数量
    private BigDecimal bdTkcttStl_AddUpMeaQuantity;
    // 当期计量结算工程金额
    private BigDecimal bdTkcttStl_CurrentPeriodMeaAmount;
    // 当期计量结算开累工程金额
    private BigDecimal bdTkcttStl_AddUpMeaAmount;

    // 当期分包结算工程数量
    private BigDecimal bdSubcttStl_CurrentPeriodQQty;
    // 当期分包结算开累工程数量
    private BigDecimal bdSubcttStl_AddUpQQty;
    // 当期分包结算工程金额
    private BigDecimal bdSubcttStl_CurrentPeriodAmount;
    // 当期分包结算开累工程金额
    private BigDecimal bdSubcttStl_AddUpAmount;

    private String strSubctt_SignPartName;
    // 数量差
    private BigDecimal bdMeaS_AddUpQQty;
    // 金额差
    private BigDecimal bdMeaS_AddUpAmount;

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

    public BigDecimal getBdTkctt_Qty() {
        return bdTkctt_Qty;
    }

    public void setBdTkctt_Qty(BigDecimal bdTkctt_Qty) {
        this.bdTkctt_Qty = bdTkctt_Qty;
    }

    public String getStrCstpl_Pkid() {
        return strCstpl_Pkid;
    }

    public void setStrCstpl_Pkid(String strCstpl_Pkid) {
        this.strCstpl_Pkid = strCstpl_Pkid;
    }

    public BigDecimal getBdCstpl_Qty() {
        return bdCstpl_Qty;
    }

    public void setBdCstpl_Qty(BigDecimal bdCstpl_Qty) {
        this.bdCstpl_Qty = bdCstpl_Qty;
    }

    public BigDecimal getBdTkcttStl_CurrentPeriodMeaQuantity() {
        return bdTkcttStl_CurrentPeriodMeaQuantity;
    }

    public void setBdTkcttStl_CurrentPeriodMeaQuantity(BigDecimal bdTkcttStl_CurrentPeriodMeaQuantity) {
        this.bdTkcttStl_CurrentPeriodMeaQuantity = bdTkcttStl_CurrentPeriodMeaQuantity;
    }

    public BigDecimal getBdTkcttStl_AddUpMeaQuantity() {
        return bdTkcttStl_AddUpMeaQuantity;
    }

    public void setBdTkcttStl_AddUpMeaQuantity(BigDecimal bdTkcttStl_AddUpMeaQuantity) {
        this.bdTkcttStl_AddUpMeaQuantity = bdTkcttStl_AddUpMeaQuantity;
    }

    public BigDecimal getBdSubcttStl_CurrentPeriodQQty() {
        return bdSubcttStl_CurrentPeriodQQty;
    }

    public void setBdSubcttStl_CurrentPeriodQQty(BigDecimal bdSubcttStl_CurrentPeriodQQty) {
        this.bdSubcttStl_CurrentPeriodQQty = bdSubcttStl_CurrentPeriodQQty;
    }

    public BigDecimal getBdSubcttStl_AddUpQQty() {
        return bdSubcttStl_AddUpQQty;
    }

    public void setBdSubcttStl_AddUpQQty(BigDecimal bdSubcttStl_AddUpQQty) {
        this.bdSubcttStl_AddUpQQty = bdSubcttStl_AddUpQQty;
    }

    public String getStrSubctt_SignPartName() {
        return strSubctt_SignPartName;
    }

    public void setStrSubctt_SignPartName(String strSubctt_SignPartName) {
        this.strSubctt_SignPartName = strSubctt_SignPartName;
    }

    public BigDecimal getBdMeaS_AddUpQQty() {
        return bdMeaS_AddUpQQty;
    }

    public void setBdMeaS_AddUpQQty(BigDecimal bdMeaS_AddUpQQty) {
        this.bdMeaS_AddUpQQty = bdMeaS_AddUpQQty;
    }

    public String getStrNote() {
        return strNote;
    }

    public void setStrNote(String strNote) {
        this.strNote = strNote;
    }

    public BigDecimal getBdTkctt_UnitPrice() {
        return bdTkctt_UnitPrice;
    }

    public void setBdTkctt_UnitPrice(BigDecimal bdTkctt_UnitPrice) {
        this.bdTkctt_UnitPrice = bdTkctt_UnitPrice;
    }

    public BigDecimal getBdTkctt_Amt() {
        return bdTkctt_Amt;
    }

    public void setBdTkctt_Amt(BigDecimal bdTkctt_Amt) {
        this.bdTkctt_Amt = bdTkctt_Amt;
    }

    public BigDecimal getBdCstpl_UnitPrice() {
        return bdCstpl_UnitPrice;
    }

    public void setBdCstpl_UnitPrice(BigDecimal bdCstpl_UnitPrice) {
        this.bdCstpl_UnitPrice = bdCstpl_UnitPrice;
    }

    public BigDecimal getBdCstpl_Amt() {
        return bdCstpl_Amt;
    }

    public void setBdCstpl_Amt(BigDecimal bdCstpl_Amt) {
        this.bdCstpl_Amt = bdCstpl_Amt;
    }

    public BigDecimal getBdTkcttStl_CurrentPeriodMeaAmount() {
        return bdTkcttStl_CurrentPeriodMeaAmount;
    }

    public void setBdTkcttStl_CurrentPeriodMeaAmount(BigDecimal bdTkcttStl_CurrentPeriodMeaAmount) {
        this.bdTkcttStl_CurrentPeriodMeaAmount = bdTkcttStl_CurrentPeriodMeaAmount;
    }

    public BigDecimal getBdTkcttStl_AddUpMeaAmount() {
        return bdTkcttStl_AddUpMeaAmount;
    }

    public void setBdTkcttStl_AddUpMeaAmount(BigDecimal bdTkcttStl_AddUpMeaAmount) {
        this.bdTkcttStl_AddUpMeaAmount = bdTkcttStl_AddUpMeaAmount;
    }

    public BigDecimal getBdSubcttStl_CurrentPeriodAmount() {
        return bdSubcttStl_CurrentPeriodAmount;
    }

    public void setBdSubcttStl_CurrentPeriodAmount(BigDecimal bdSubcttStl_CurrentPeriodAmount) {
        this.bdSubcttStl_CurrentPeriodAmount = bdSubcttStl_CurrentPeriodAmount;
    }

    public BigDecimal getBdSubcttStl_AddUpAmount() {
        return bdSubcttStl_AddUpAmount;
    }

    public void setBdSubcttStl_AddUpAmount(BigDecimal bdSubcttStl_AddUpAmount) {
        this.bdSubcttStl_AddUpAmount = bdSubcttStl_AddUpAmount;
    }

    public BigDecimal getBdMeaS_AddUpAmount() {
        return bdMeaS_AddUpAmount;
    }

    public void setBdMeaS_AddUpAmount(BigDecimal bdMeaS_AddUpAmount) {
        this.bdMeaS_AddUpAmount = bdMeaS_AddUpAmount;
    }
}