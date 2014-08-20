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

public class QryCSStlMShow implements Serializable {
    private String strPkid;
    private String strParentPkid;
    private String strNo;
    private String strName;

    private String    strSubctt_SignPartName;
    private String    strSubctt_Unit;
    private BigDecimal bdSubctt_ContractUnitPrice;
    private BigDecimal bdSubctt_ContractQuantity;

    private BigDecimal bdSubctt_BeginToCurrentPeriodMQty;
    private BigDecimal bdSubctt_CurrentPeriodMQty;
    private BigDecimal bdSubctt_BeginToCurrentPeriodMAmount;
    private BigDecimal bdSubctt_CurrentPeriodMAmount;

    public String getStrPkid() {
        return strPkid;
    }

    public void setStrPkid(String strPkid) {
        this.strPkid = strPkid;
    }

    public String getStrParentPkid() {
        return strParentPkid;
    }

    public void setStrParentPkid(String strParentPkid) {
        this.strParentPkid = strParentPkid;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrSubctt_SignPartName() {
        return strSubctt_SignPartName;
    }

    public void setStrSubctt_SignPartName(String strSubctt_SignPartName) {
        this.strSubctt_SignPartName = strSubctt_SignPartName;
    }

    public String getStrSubctt_Unit() {
        return strSubctt_Unit;
    }

    public void setStrSubctt_Unit(String strSubctt_Unit) {
        this.strSubctt_Unit = strSubctt_Unit;
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

    public BigDecimal getBdSubctt_BeginToCurrentPeriodMQty() {
        return bdSubctt_BeginToCurrentPeriodMQty;
    }

    public void setBdSubctt_BeginToCurrentPeriodMQty(BigDecimal bdSubctt_BeginToCurrentPeriodMQty) {
        this.bdSubctt_BeginToCurrentPeriodMQty = bdSubctt_BeginToCurrentPeriodMQty;
    }

    public BigDecimal getBdSubctt_CurrentPeriodMQty() {
        return bdSubctt_CurrentPeriodMQty;
    }

    public void setBdSubctt_CurrentPeriodMQty(BigDecimal bdSubctt_CurrentPeriodMQty) {
        this.bdSubctt_CurrentPeriodMQty = bdSubctt_CurrentPeriodMQty;
    }

    public BigDecimal getBdSubctt_BeginToCurrentPeriodMAmount() {
        return bdSubctt_BeginToCurrentPeriodMAmount;
    }

    public void setBdSubctt_BeginToCurrentPeriodMAmount(BigDecimal bdSubctt_BeginToCurrentPeriodMAmount) {
        this.bdSubctt_BeginToCurrentPeriodMAmount = bdSubctt_BeginToCurrentPeriodMAmount;
    }

    public BigDecimal getBdSubctt_CurrentPeriodMAmount() {
        return bdSubctt_CurrentPeriodMAmount;
    }

    public void setBdSubctt_CurrentPeriodMAmount(BigDecimal bdSubctt_CurrentPeriodMAmount) {
        this.bdSubctt_CurrentPeriodMAmount = bdSubctt_CurrentPeriodMAmount;
    }
}