package epss.repository.model.model_show;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-4-22
 * Time: ÏÂÎç4:18
 * To change this template use File | Settings | File Templates.
 */
public class QryShow {

    private String strPkid;
    private String strItemPkid;
    private String strCorrespondingPkid;

    private String strPeriodNo;
    private String strUnit;
    private String strName;
    private BigDecimal bdUnitPrice;
    private BigDecimal bdSignPartAMPrice;
    private BigDecimal bdQuantity;
    private BigDecimal bdCurrentPeriodQuantity;
    private BigDecimal bdBeginToCurrentPeriodQuantity;
    private BigDecimal bdAmount;

    public String getStrPkid() {
        return strPkid;
    }

    public void setStrPkid(String strPkid) {
        this.strPkid = strPkid;
    }

    public String getStrItemPkid() {
        return strItemPkid;
    }

    public void setStrItemPkid(String strItemPkid) {
        this.strItemPkid = strItemPkid;
    }

    public String getStrCorrespondingPkid() {
        return strCorrespondingPkid;
    }

    public void setStrCorrespondingPkid(String strCorrespondingPkid) {
        this.strCorrespondingPkid = strCorrespondingPkid;
    }

    public String getStrPeriodNo() {
        return strPeriodNo;
    }

    public void setStrPeriodNo(String strPeriodNo) {
        this.strPeriodNo = strPeriodNo;
    }

    public String getStrUnit() {
        return strUnit;
    }

    public void setStrUnit(String strUnit) {
        this.strUnit = strUnit;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public BigDecimal getBdUnitPrice() {
        return bdUnitPrice;
    }

    public void setBdUnitPrice(BigDecimal bdUnitPrice) {
        this.bdUnitPrice = bdUnitPrice;
    }

    public BigDecimal getBdQuantity() {
        return bdQuantity;
    }

    public void setBdQuantity(BigDecimal bdQuantity) {
        this.bdQuantity = bdQuantity;
    }

    public BigDecimal getBdCurrentPeriodQuantity() {
        return bdCurrentPeriodQuantity;
    }

    public void setBdCurrentPeriodQuantity(BigDecimal bdCurrentPeriodQuantity) {
        this.bdCurrentPeriodQuantity = bdCurrentPeriodQuantity;
    }

    public BigDecimal getBdBeginToCurrentPeriodQuantity() {
        return bdBeginToCurrentPeriodQuantity;
    }

    public void setBdBeginToCurrentPeriodQuantity(BigDecimal bdBeginToCurrentPeriodQuantity) {
        this.bdBeginToCurrentPeriodQuantity = bdBeginToCurrentPeriodQuantity;
    }

    public BigDecimal getBdAmount() {
        return bdAmount;
    }

    public void setBdAmount(BigDecimal bdAmount) {
        this.bdAmount = bdAmount;
    }

    public BigDecimal getBdSignPartAMPrice() {
        return bdSignPartAMPrice;
    }

    public void setBdSignPartAMPrice(BigDecimal bdSignPartAMPrice) {
        this.bdSignPartAMPrice = bdSignPartAMPrice;
    }
}
