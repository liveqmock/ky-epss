package epss.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: ÏÂÎç9:54
 * To change this template use File | Settings | File Templates.
 */

public class QryCSStlQShow implements Serializable {
    private String strCstpl_Pkid;
    private String strCstpl_ParentPkid;
    private String strCstpl_No;
    private String strCstpl_Name;
    private String strCstpl_Unit;
    private BigDecimal bdCstpl_ContractUnitPrice;

    private BigDecimal bdTkcttStl_MeaQuantity;
    private BigDecimal bdTkcttStl_MeaAmount;

    private String    strSubctt_SignPartName;
    private BigDecimal bdSubctt_ContractUnitPrice;

    private BigDecimal bdSubcttStl_BeginToCurrentPeriodQQty;
    private BigDecimal bdSubcttStl_BeginToCurrentPeriodMAmount;

    private BigDecimal bdMeaS_ContractUnitPrice;
    private BigDecimal bdMeaS_BeginToCurrentPeriodQQty;
    private BigDecimal bdMeaS_BeginToCurrentPeriodMAmount;

    public String getStrCstpl_Pkid() {
        return strCstpl_Pkid;
    }

    public void setStrCstpl_Pkid(String strCstpl_Pkid) {
        this.strCstpl_Pkid = strCstpl_Pkid;
    }

    public String getStrCstpl_ParentPkid() {
        return strCstpl_ParentPkid;
    }

    public void setStrCstpl_ParentPkid(String strCstpl_ParentPkid) {
        this.strCstpl_ParentPkid = strCstpl_ParentPkid;
    }

    public String getStrCstpl_No() {
        return strCstpl_No;
    }

    public void setStrCstpl_No(String strCstpl_No) {
        this.strCstpl_No = strCstpl_No;
    }

    public String getStrCstpl_Name() {
        return strCstpl_Name;
    }

    public void setStrCstpl_Name(String strCstpl_Name) {
        this.strCstpl_Name = strCstpl_Name;
    }

    public String getStrCstpl_Unit() {
        return strCstpl_Unit;
    }

    public void setStrCstpl_Unit(String strCstpl_Unit) {
        this.strCstpl_Unit = strCstpl_Unit;
    }

    public BigDecimal getBdCstpl_ContractUnitPrice() {
        return bdCstpl_ContractUnitPrice;
    }

    public void setBdCstpl_ContractUnitPrice(BigDecimal bdCstpl_ContractUnitPrice) {
        this.bdCstpl_ContractUnitPrice = bdCstpl_ContractUnitPrice;
    }

    public BigDecimal getBdTkcttStl_MeaQuantity() {
        return bdTkcttStl_MeaQuantity;
    }

    public void setBdTkcttStl_MeaQuantity(BigDecimal bdTkcttStl_MeaQuantity) {
        this.bdTkcttStl_MeaQuantity = bdTkcttStl_MeaQuantity;
    }

    public BigDecimal getBdTkcttStl_MeaAmount() {
        return bdTkcttStl_MeaAmount;
    }

    public void setBdTkcttStl_MeaAmount(BigDecimal bdTkcttStl_MeaAmount) {
        this.bdTkcttStl_MeaAmount = bdTkcttStl_MeaAmount;
    }

    public String getStrSubctt_SignPartName() {
        return strSubctt_SignPartName;
    }

    public void setStrSubctt_SignPartName(String strSubctt_SignPartName) {
        this.strSubctt_SignPartName = strSubctt_SignPartName;
    }

    public BigDecimal getBdSubctt_ContractUnitPrice() {
        return bdSubctt_ContractUnitPrice;
    }

    public void setBdSubctt_ContractUnitPrice(BigDecimal bdSubctt_ContractUnitPrice) {
        this.bdSubctt_ContractUnitPrice = bdSubctt_ContractUnitPrice;
    }

    public BigDecimal getBdSubcttStl_BeginToCurrentPeriodQQty() {
        return bdSubcttStl_BeginToCurrentPeriodQQty;
    }

    public void setBdSubcttStl_BeginToCurrentPeriodQQty(BigDecimal bdSubcttStl_BeginToCurrentPeriodQQty) {
        this.bdSubcttStl_BeginToCurrentPeriodQQty = bdSubcttStl_BeginToCurrentPeriodQQty;
    }

    public BigDecimal getBdSubcttStl_BeginToCurrentPeriodMAmount() {
        return bdSubcttStl_BeginToCurrentPeriodMAmount;
    }

    public void setBdSubcttStl_BeginToCurrentPeriodMAmount(BigDecimal bdSubcttStl_BeginToCurrentPeriodMAmount) {
        this.bdSubcttStl_BeginToCurrentPeriodMAmount = bdSubcttStl_BeginToCurrentPeriodMAmount;
    }

    public BigDecimal getBdMeaS_ContractUnitPrice() {
        return bdMeaS_ContractUnitPrice;
    }

    public void setBdMeaS_ContractUnitPrice(BigDecimal bdMeaS_ContractUnitPrice) {
        this.bdMeaS_ContractUnitPrice = bdMeaS_ContractUnitPrice;
    }

    public BigDecimal getBdMeaS_BeginToCurrentPeriodQQty() {
        return bdMeaS_BeginToCurrentPeriodQQty;
    }

    public void setBdMeaS_BeginToCurrentPeriodQQty(BigDecimal bdMeaS_BeginToCurrentPeriodQQty) {
        this.bdMeaS_BeginToCurrentPeriodQQty = bdMeaS_BeginToCurrentPeriodQQty;
    }

    public BigDecimal getBdMeaS_BeginToCurrentPeriodMAmount() {
        return bdMeaS_BeginToCurrentPeriodMAmount;
    }

    public void setBdMeaS_BeginToCurrentPeriodMAmount(BigDecimal bdMeaS_BeginToCurrentPeriodMAmount) {
        this.bdMeaS_BeginToCurrentPeriodMAmount = bdMeaS_BeginToCurrentPeriodMAmount;
    }
}