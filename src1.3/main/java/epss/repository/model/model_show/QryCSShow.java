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

public class QryCSShow implements Serializable {
    private String strCstpl_Pkid;
    private String strCstpl_ParentPkid;
    private String strCstpl_No;
    private String strCstpl_Name;
    private String strCstpl_Unit;
    private BigDecimal bdCstpl_UnitPrice;
    private BigDecimal bdCstpl_Qty;
    private BigDecimal bdCstpl_Amt;

    private BigDecimal bdSubctt_UnitPrice;
    private BigDecimal bdSubctt_Qty;
    private BigDecimal bdSubctt_Amt;
    private String strSubctt_SignPartName;

    private BigDecimal bdC_S_UnitPrice;
    private BigDecimal bdC_S_Qty;
    private BigDecimal bdC_S_Amt;

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

    public BigDecimal getBdCstpl_UnitPrice() {
        return bdCstpl_UnitPrice;
    }

    public void setBdCstpl_UnitPrice(BigDecimal bdCstpl_UnitPrice) {
        this.bdCstpl_UnitPrice = bdCstpl_UnitPrice;
    }

    public BigDecimal getBdCstpl_Qty() {
        return bdCstpl_Qty;
    }

    public void setBdCstpl_Qty(BigDecimal bdCstpl_Qty) {
        this.bdCstpl_Qty = bdCstpl_Qty;
    }

    public BigDecimal getBdCstpl_Amt() {
        return bdCstpl_Amt;
    }

    public void setBdCstpl_Amt(BigDecimal bdCstpl_Amt) {
        this.bdCstpl_Amt = bdCstpl_Amt;
    }

    public BigDecimal getBdSubctt_UnitPrice() {
        return bdSubctt_UnitPrice;
    }

    public void setBdSubctt_UnitPrice(BigDecimal bdSubctt_UnitPrice) {
        this.bdSubctt_UnitPrice = bdSubctt_UnitPrice;
    }

    public BigDecimal getBdSubctt_Qty() {
        return bdSubctt_Qty;
    }

    public void setBdSubctt_Qty(BigDecimal bdSubctt_Qty) {
        this.bdSubctt_Qty = bdSubctt_Qty;
    }

    public BigDecimal getBdSubctt_Amt() {
        return bdSubctt_Amt;
    }

    public void setBdSubctt_Amt(BigDecimal bdSubctt_Amt) {
        this.bdSubctt_Amt = bdSubctt_Amt;
    }

    public String getStrSubctt_SignPartName() {
        return strSubctt_SignPartName;
    }

    public void setStrSubctt_SignPartName(String strSubctt_SignPartName) {
        this.strSubctt_SignPartName = strSubctt_SignPartName;
    }

    public BigDecimal getBdC_S_UnitPrice() {
        return bdC_S_UnitPrice;
    }

    public void setBdC_S_UnitPrice(BigDecimal bdC_S_UnitPrice) {
        this.bdC_S_UnitPrice = bdC_S_UnitPrice;
    }

    public BigDecimal getBdC_S_Qty() {
        return bdC_S_Qty;
    }

    public void setBdC_S_Qty(BigDecimal bdC_S_Qty) {
        this.bdC_S_Qty = bdC_S_Qty;
    }

    public BigDecimal getBdC_S_Amt() {
        return bdC_S_Amt;
    }

    public void setBdC_S_Amt(BigDecimal bdC_S_Amt) {
        this.bdC_S_Amt = bdC_S_Amt;
    }
}