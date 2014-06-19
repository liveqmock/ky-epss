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
    private BigDecimal bdCstpl_UnitPrice;

    private BigDecimal bdTkcttStl_MeaQuantity;
    private BigDecimal bdTkcttStl_MeaAmount;

    private String    strSubctt_SignPartName;
    private BigDecimal bdSubctt_UnitPrice;

    private BigDecimal bdSubcttStl_AddUpQQty;
    private BigDecimal bdSubcttStl_AddUpMAmount;

    private BigDecimal bdMeaS_UnitPrice;
    private BigDecimal bdMeaS_AddUpQQty;
    private BigDecimal bdMeaS_AddUpMAmount;

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

    public BigDecimal getBdSubctt_UnitPrice() {
        return bdSubctt_UnitPrice;
    }

    public void setBdSubctt_UnitPrice(BigDecimal bdSubctt_UnitPrice) {
        this.bdSubctt_UnitPrice = bdSubctt_UnitPrice;
    }

    public BigDecimal getBdSubcttStl_AddUpQQty() {
        return bdSubcttStl_AddUpQQty;
    }

    public void setBdSubcttStl_AddUpQQty(BigDecimal bdSubcttStl_AddUpQQty) {
        this.bdSubcttStl_AddUpQQty = bdSubcttStl_AddUpQQty;
    }

    public BigDecimal getBdSubcttStl_AddUpMAmount() {
        return bdSubcttStl_AddUpMAmount;
    }

    public void setBdSubcttStl_AddUpMAmount(BigDecimal bdSubcttStl_AddUpMAmount) {
        this.bdSubcttStl_AddUpMAmount = bdSubcttStl_AddUpMAmount;
    }

    public BigDecimal getBdMeaS_UnitPrice() {
        return bdMeaS_UnitPrice;
    }

    public void setBdMeaS_UnitPrice(BigDecimal bdMeaS_UnitPrice) {
        this.bdMeaS_UnitPrice = bdMeaS_UnitPrice;
    }

    public BigDecimal getBdMeaS_AddUpQQty() {
        return bdMeaS_AddUpQQty;
    }

    public void setBdMeaS_AddUpQQty(BigDecimal bdMeaS_AddUpQQty) {
        this.bdMeaS_AddUpQQty = bdMeaS_AddUpQQty;
    }

    public BigDecimal getBdMeaS_AddUpMAmount() {
        return bdMeaS_AddUpMAmount;
    }

    public void setBdMeaS_AddUpMAmount(BigDecimal bdMeaS_AddUpMAmount) {
        this.bdMeaS_AddUpMAmount = bdMeaS_AddUpMAmount;
    }
}