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
    private BigDecimal bdCstpl_ContractUnitPrice;
    private BigDecimal bdCstpl_ContractQuantity;
    private BigDecimal bdCstpl_ContractAmount;

    private String strSubctt_Name;
    private BigDecimal bdSubctt_ContractUnitPrice;
    private BigDecimal bdSubctt_ContractQuantity;
    private BigDecimal bdSubctt_ContractAmount;
    private String strSubctt_SignPartName;

    private BigDecimal bdC_S_ContractUnitPrice;
    private BigDecimal bdC_S_ContractQuantity;
    private BigDecimal bdC_S_ContractAmount;

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

    public BigDecimal getBdCstpl_ContractQuantity() {
        return bdCstpl_ContractQuantity;
    }

    public void setBdCstpl_ContractQuantity(BigDecimal bdCstpl_ContractQuantity) {
        this.bdCstpl_ContractQuantity = bdCstpl_ContractQuantity;
    }

    public BigDecimal getBdCstpl_ContractAmount() {
        return bdCstpl_ContractAmount;
    }

    public void setBdCstpl_ContractAmount(BigDecimal bdCstpl_ContractAmount) {
        this.bdCstpl_ContractAmount = bdCstpl_ContractAmount;
    }

    public BigDecimal getBdSubctt_ContractUnitPrice() {
        return bdSubctt_ContractUnitPrice;
    }

    public void setBdSubctt_ContractUnitPrice(BigDecimal bdSubctt_ContractUnitPrice) {
        this.bdSubctt_ContractUnitPrice = bdSubctt_ContractUnitPrice;
    }

    public BigDecimal getBdSubctt_ContractQuantity() {
        return bdSubctt_ContractQuantity;
    }

    public void setBdSubctt_ContractQuantity(BigDecimal bdSubctt_ContractQuantity) {
        this.bdSubctt_ContractQuantity = bdSubctt_ContractQuantity;
    }

    public BigDecimal getBdSubctt_ContractAmount() {
        return bdSubctt_ContractAmount;
    }

    public void setBdSubctt_ContractAmount(BigDecimal bdSubctt_ContractAmount) {
        this.bdSubctt_ContractAmount = bdSubctt_ContractAmount;
    }

    public String getStrSubctt_SignPartName() {
        return strSubctt_SignPartName;
    }

    public void setStrSubctt_SignPartName(String strSubctt_SignPartName) {
        this.strSubctt_SignPartName = strSubctt_SignPartName;
    }

    public BigDecimal getBdC_S_ContractUnitPrice() {
        return bdC_S_ContractUnitPrice;
    }

    public void setBdC_S_ContractUnitPrice(BigDecimal bdC_S_ContractUnitPrice) {
        this.bdC_S_ContractUnitPrice = bdC_S_ContractUnitPrice;
    }

    public BigDecimal getBdC_S_ContractQuantity() {
        return bdC_S_ContractQuantity;
    }

    public void setBdC_S_ContractQuantity(BigDecimal bdC_S_ContractQuantity) {
        this.bdC_S_ContractQuantity = bdC_S_ContractQuantity;
    }

    public BigDecimal getBdC_S_ContractAmount() {
        return bdC_S_ContractAmount;
    }

    public void setBdC_S_ContractAmount(BigDecimal bdC_S_ContractAmount) {
        this.bdC_S_ContractAmount = bdC_S_ContractAmount;
    }

    public String getStrSubctt_Name() {
        return strSubctt_Name;
    }

    public void setStrSubctt_Name(String strSubctt_Name) {
        this.strSubctt_Name = strSubctt_Name;
    }
}