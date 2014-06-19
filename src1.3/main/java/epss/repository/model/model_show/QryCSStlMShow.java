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
    private BigDecimal bdSubctt_UnitPrice;
    private BigDecimal bdSubctt_Qty;

    private BigDecimal bdSubctt_AddUpMQty;
    private BigDecimal bdSubctt_CurrentPeriodMQty;
    private BigDecimal bdSubctt_AddUpMAmount;
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

    public BigDecimal getBdSubctt_AddUpMQty() {
        return bdSubctt_AddUpMQty;
    }

    public void setBdSubctt_AddUpMQty(BigDecimal bdSubctt_AddUpMQty) {
        this.bdSubctt_AddUpMQty = bdSubctt_AddUpMQty;
    }

    public BigDecimal getBdSubctt_CurrentPeriodMQty() {
        return bdSubctt_CurrentPeriodMQty;
    }

    public void setBdSubctt_CurrentPeriodMQty(BigDecimal bdSubctt_CurrentPeriodMQty) {
        this.bdSubctt_CurrentPeriodMQty = bdSubctt_CurrentPeriodMQty;
    }

    public BigDecimal getBdSubctt_AddUpMAmount() {
        return bdSubctt_AddUpMAmount;
    }

    public void setBdSubctt_AddUpMAmount(BigDecimal bdSubctt_AddUpMAmount) {
        this.bdSubctt_AddUpMAmount = bdSubctt_AddUpMAmount;
    }

    public BigDecimal getBdSubctt_CurrentPeriodMAmount() {
        return bdSubctt_CurrentPeriodMAmount;
    }

    public void setBdSubctt_CurrentPeriodMAmount(BigDecimal bdSubctt_CurrentPeriodMAmount) {
        this.bdSubctt_CurrentPeriodMAmount = bdSubctt_CurrentPeriodMAmount;
    }
}