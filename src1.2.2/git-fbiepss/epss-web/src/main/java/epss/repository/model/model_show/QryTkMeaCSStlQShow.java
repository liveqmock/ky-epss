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
    private String tkcttItem_Pkid;
    private String tkcttItem_ParentPkid;
    private String tkcttItem_No;
    private String tkcttItem_Name;
    private String tkcttItem_Unit;
    private BigDecimal tkcttItem_CttUnitPrice;
    private BigDecimal tkcttItem_CttQty;
    private BigDecimal tkcttItem_CttAmt;

    // 成本计划
    private String cstplItem_Pkid;
    private String cstplItem_No;
    private String cstplItem_Name;
    private BigDecimal cstplItem_UnitPrice;
    private BigDecimal cstplItem_Qty;
    private BigDecimal cstplItem_Amt;
    private BigDecimal cstplTkcttItem_TotalAmt;
    private BigDecimal cstplTkcttItem_TotalUnitPrice;

    // 当期计量结算工程数量
    private BigDecimal tkcttStlItem_ThisStageQty;
    // 当期计量结算开累工程数量
    private BigDecimal tkcttStlItem_AddUpQty;
    // 当期计量结算工程金额
    private BigDecimal tkcttStlItem_ThisStageAmt;
    // 当期计量结算开累工程金额
    private BigDecimal tkcttStlItem_AddUpAmt;
    // 当期计量结算成本工程金额
    private BigDecimal tkcttStlCstplItem_ThisStageAmt;
    // 当期计量结算成本开累工程金额
    private BigDecimal tkcttStlCstplItem_AddUpAmt;

    // 当期分包结算工程数量
    private String subcttItem_CorrPkid;
    private String subcttItem_SignPartName;
    private String subcttItem_Name;
    private BigDecimal subcttItem_UnitPrice;

    private BigDecimal subcttStlItem_ThisStageQty;
    // 当期分包结算开累工程数量
    private BigDecimal subcttStlItem_AddUpQty;
    // 当期分包结算工程金额
    private BigDecimal subcttStlItem_ThisStageAmt;
    // 当期分包结算开累工程金额
    private BigDecimal subcttStlItem_AddUpAmt;

    // 数量差
    private BigDecimal meaSItem_AddUpQty;
    // 计量分包结算金额差
    private BigDecimal meaSItem_AddUpAmt;
    // 计量成本分包结算金额差
    private BigDecimal meaSCstplItem_AddUpAmt;

    private String itemNote;

    public String getTkcttItem_Pkid() {
        return tkcttItem_Pkid;
    }

    public void setTkcttItem_Pkid(String tkcttItem_Pkid) {
        this.tkcttItem_Pkid = tkcttItem_Pkid;
    }

    public String getTkcttItem_ParentPkid() {
        return tkcttItem_ParentPkid;
    }

    public void setTkcttItem_ParentPkid(String tkcttItem_ParentPkid) {
        this.tkcttItem_ParentPkid = tkcttItem_ParentPkid;
    }

    public String getTkcttItem_No() {
        return tkcttItem_No;
    }

    public void setTkcttItem_No(String tkcttItem_No) {
        this.tkcttItem_No = tkcttItem_No;
    }

    public String getTkcttItem_Name() {
        return tkcttItem_Name;
    }

    public void setTkcttItem_Name(String tkcttItem_Name) {
        this.tkcttItem_Name = tkcttItem_Name;
    }

    public String getTkcttItem_Unit() {
        return tkcttItem_Unit;
    }

    public void setTkcttItem_Unit(String tkcttItem_Unit) {
        this.tkcttItem_Unit = tkcttItem_Unit;
    }

    public BigDecimal getTkcttItem_CttUnitPrice() {
        return tkcttItem_CttUnitPrice;
    }

    public void setTkcttItem_CttUnitPrice(BigDecimal tkcttItem_CttUnitPrice) {
        this.tkcttItem_CttUnitPrice = tkcttItem_CttUnitPrice;
    }

    public BigDecimal getTkcttItem_CttQty() {
        return tkcttItem_CttQty;
    }

    public void setTkcttItem_CttQty(BigDecimal tkcttItem_CttQty) {
        this.tkcttItem_CttQty = tkcttItem_CttQty;
    }

    public BigDecimal getTkcttItem_CttAmt() {
        return tkcttItem_CttAmt;
    }

    public void setTkcttItem_CttAmt(BigDecimal tkcttItem_CttAmt) {
        this.tkcttItem_CttAmt = tkcttItem_CttAmt;
    }

    public String getCstplItem_Pkid() {
        return cstplItem_Pkid;
    }

    public void setCstplItem_Pkid(String cstplItem_Pkid) {
        this.cstplItem_Pkid = cstplItem_Pkid;
    }

    public String getCstplItem_No() {
        return cstplItem_No;
    }

    public void setCstplItem_No(String cstplItem_No) {
        this.cstplItem_No = cstplItem_No;
    }

    public String getCstplItem_Name() {
        return cstplItem_Name;
    }

    public void setCstplItem_Name(String cstplItem_Name) {
        this.cstplItem_Name = cstplItem_Name;
    }

    public BigDecimal getCstplItem_UnitPrice() {
        return cstplItem_UnitPrice;
    }

    public void setCstplItem_UnitPrice(BigDecimal cstplItem_UnitPrice) {
        this.cstplItem_UnitPrice = cstplItem_UnitPrice;
    }

    public BigDecimal getCstplItem_Qty() {
        return cstplItem_Qty;
    }

    public void setCstplItem_Qty(BigDecimal cstplItem_Qty) {
        this.cstplItem_Qty = cstplItem_Qty;
    }

    public BigDecimal getCstplItem_Amt() {
        return cstplItem_Amt;
    }

    public void setCstplItem_Amt(BigDecimal cstplItem_Amt) {
        this.cstplItem_Amt = cstplItem_Amt;
    }

    public BigDecimal getCstplTkcttItem_TotalAmt() {
        return cstplTkcttItem_TotalAmt;
    }

    public void setCstplTkcttItem_TotalAmt(BigDecimal cstplTkcttItem_TotalAmt) {
        this.cstplTkcttItem_TotalAmt = cstplTkcttItem_TotalAmt;
    }

    public BigDecimal getCstplTkcttItem_TotalUnitPrice() {
        return cstplTkcttItem_TotalUnitPrice;
    }

    public void setCstplTkcttItem_TotalUnitPrice(BigDecimal cstplTkcttItem_TotalUnitPrice) {
        this.cstplTkcttItem_TotalUnitPrice = cstplTkcttItem_TotalUnitPrice;
    }

    public BigDecimal getTkcttStlItem_ThisStageQty() {
        return tkcttStlItem_ThisStageQty;
    }

    public void setTkcttStlItem_ThisStageQty(BigDecimal tkcttStlItem_ThisStageQty) {
        this.tkcttStlItem_ThisStageQty = tkcttStlItem_ThisStageQty;
    }

    public BigDecimal getTkcttStlItem_AddUpQty() {
        return tkcttStlItem_AddUpQty;
    }

    public void setTkcttStlItem_AddUpQty(BigDecimal tkcttStlItem_AddUpQty) {
        this.tkcttStlItem_AddUpQty = tkcttStlItem_AddUpQty;
    }

    public BigDecimal getTkcttStlItem_ThisStageAmt() {
        return tkcttStlItem_ThisStageAmt;
    }

    public void setTkcttStlItem_ThisStageAmt(BigDecimal tkcttStlItem_ThisStageAmt) {
        this.tkcttStlItem_ThisStageAmt = tkcttStlItem_ThisStageAmt;
    }

    public BigDecimal getTkcttStlItem_AddUpAmt() {
        return tkcttStlItem_AddUpAmt;
    }

    public void setTkcttStlItem_AddUpAmt(BigDecimal tkcttStlItem_AddUpAmt) {
        this.tkcttStlItem_AddUpAmt = tkcttStlItem_AddUpAmt;
    }

    public BigDecimal getTkcttStlCstplItem_ThisStageAmt() {
        return tkcttStlCstplItem_ThisStageAmt;
    }

    public void setTkcttStlCstplItem_ThisStageAmt(BigDecimal tkcttStlCstplItem_ThisStageAmt) {
        this.tkcttStlCstplItem_ThisStageAmt = tkcttStlCstplItem_ThisStageAmt;
    }

    public BigDecimal getTkcttStlCstplItem_AddUpAmt() {
        return tkcttStlCstplItem_AddUpAmt;
    }

    public void setTkcttStlCstplItem_AddUpAmt(BigDecimal tkcttStlCstplItem_AddUpAmt) {
        this.tkcttStlCstplItem_AddUpAmt = tkcttStlCstplItem_AddUpAmt;
    }

    public String getSubcttItem_CorrPkid() {
        return subcttItem_CorrPkid;
    }

    public void setSubcttItem_CorrPkid(String subcttItem_CorrPkid) {
        this.subcttItem_CorrPkid = subcttItem_CorrPkid;
    }

    public String getSubcttItem_SignPartName() {
        return subcttItem_SignPartName;
    }

    public void setSubcttItem_SignPartName(String subcttItem_SignPartName) {
        this.subcttItem_SignPartName = subcttItem_SignPartName;
    }

    public String getSubcttItem_Name() {
        return subcttItem_Name;
    }

    public void setSubcttItem_Name(String subcttItem_Name) {
        this.subcttItem_Name = subcttItem_Name;
    }

    public BigDecimal getSubcttItem_UnitPrice() {
        return subcttItem_UnitPrice;
    }

    public void setSubcttItem_UnitPrice(BigDecimal subcttItem_UnitPrice) {
        this.subcttItem_UnitPrice = subcttItem_UnitPrice;
    }

    public BigDecimal getSubcttStlItem_ThisStageQty() {
        return subcttStlItem_ThisStageQty;
    }

    public void setSubcttStlItem_ThisStageQty(BigDecimal subcttStlItem_ThisStageQty) {
        this.subcttStlItem_ThisStageQty = subcttStlItem_ThisStageQty;
    }

    public BigDecimal getSubcttStlItem_AddUpQty() {
        return subcttStlItem_AddUpQty;
    }

    public void setSubcttStlItem_AddUpQty(BigDecimal subcttStlItem_AddUpQty) {
        this.subcttStlItem_AddUpQty = subcttStlItem_AddUpQty;
    }

    public BigDecimal getSubcttStlItem_ThisStageAmt() {
        return subcttStlItem_ThisStageAmt;
    }

    public void setSubcttStlItem_ThisStageAmt(BigDecimal subcttStlItem_ThisStageAmt) {
        this.subcttStlItem_ThisStageAmt = subcttStlItem_ThisStageAmt;
    }

    public BigDecimal getSubcttStlItem_AddUpAmt() {
        return subcttStlItem_AddUpAmt;
    }

    public void setSubcttStlItem_AddUpAmt(BigDecimal subcttStlItem_AddUpAmt) {
        this.subcttStlItem_AddUpAmt = subcttStlItem_AddUpAmt;
    }

    public BigDecimal getMeaSItem_AddUpQty() {
        return meaSItem_AddUpQty;
    }

    public void setMeaSItem_AddUpQty(BigDecimal meaSItem_AddUpQty) {
        this.meaSItem_AddUpQty = meaSItem_AddUpQty;
    }

    public BigDecimal getMeaSItem_AddUpAmt() {
        return meaSItem_AddUpAmt;
    }

    public void setMeaSItem_AddUpAmt(BigDecimal meaSItem_AddUpAmt) {
        this.meaSItem_AddUpAmt = meaSItem_AddUpAmt;
    }

    public BigDecimal getMeaSCstplItem_AddUpAmt() {
        return meaSCstplItem_AddUpAmt;
    }

    public void setMeaSCstplItem_AddUpAmt(BigDecimal meaSCstplItem_AddUpAmt) {
        this.meaSCstplItem_AddUpAmt = meaSCstplItem_AddUpAmt;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }
}