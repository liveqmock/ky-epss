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
    private String tkctt_Pkid;
    private String tkctt_ParentPkid;
    private String tkctt_No;
    private String tkctt_Name;
    private String tkctt_Unit;
    private BigDecimal tkctt_CttUnitPrice;
    private BigDecimal tkctt_CttQty;
    private BigDecimal tkctt_CttAmt;

    // 成本计划
    private String cstpl_Pkid;
    private String cstpl_No;
    private BigDecimal cstpl_CttUnitPrice;
    private BigDecimal cstpl_CttQty;
    private BigDecimal cstpl_CttAmt;

    // 当期计量结算工程数量
    private BigDecimal tkcttStl_ThisStageMeaQty;
    // 当期计量结算开累工程数量
    private BigDecimal tkcttStl_AddUpMeaQty;
    // 当期计量结算工程金额
    private BigDecimal tkcttStl_ThisStageMeaAmt;
    // 当期计量结算开累工程金额
    private BigDecimal tkcttStl_AddUpMeaAmt;

    // 当期分包结算工程数量
    private String subctt_CorrespondingPkid;

    private BigDecimal subcttStl_ThisStageQty;
    // 当期分包结算开累工程数量
    private BigDecimal subcttStl_AddUpQty;
    // 当期分包结算工程金额
    private BigDecimal subcttStl_ThisStageAmt;
    // 当期分包结算开累工程金额
    private BigDecimal subcttStl_AddUpAmt;

    private String subctt_SignPartName;
    // 数量差
    private BigDecimal meaS_AddUpQty;
    // 金额差
    private BigDecimal meaS_AddUpAmt;

    private String note;

    public String getTkctt_Pkid() {
        return tkctt_Pkid;
    }

    public void setTkctt_Pkid(String tkctt_Pkid) {
        this.tkctt_Pkid = tkctt_Pkid;
    }

    public String getTkctt_ParentPkid() {
        return tkctt_ParentPkid;
    }

    public void setTkctt_ParentPkid(String tkctt_ParentPkid) {
        this.tkctt_ParentPkid = tkctt_ParentPkid;
    }

    public String getTkctt_No() {
        return tkctt_No;
    }

    public void setTkctt_No(String tkctt_No) {
        this.tkctt_No = tkctt_No;
    }

    public String getTkctt_Name() {
        return tkctt_Name;
    }

    public void setTkctt_Name(String tkctt_Name) {
        this.tkctt_Name = tkctt_Name;
    }

    public String getTkctt_Unit() {
        return tkctt_Unit;
    }

    public void setTkctt_Unit(String tkctt_Unit) {
        this.tkctt_Unit = tkctt_Unit;
    }

    public BigDecimal getTkctt_CttUnitPrice() {
        return tkctt_CttUnitPrice;
    }

    public void setTkctt_CttUnitPrice(BigDecimal tkctt_CttUnitPrice) {
        this.tkctt_CttUnitPrice = tkctt_CttUnitPrice;
    }

    public BigDecimal getTkctt_CttQty() {
        return tkctt_CttQty;
    }

    public void setTkctt_CttQty(BigDecimal tkctt_CttQty) {
        this.tkctt_CttQty = tkctt_CttQty;
    }

    public BigDecimal getTkctt_CttAmt() {
        return tkctt_CttAmt;
    }

    public void setTkctt_CttAmt(BigDecimal tkctt_CttAmt) {
        this.tkctt_CttAmt = tkctt_CttAmt;
    }

    public String getCstpl_Pkid() {
        return cstpl_Pkid;
    }

    public void setCstpl_Pkid(String cstpl_Pkid) {
        this.cstpl_Pkid = cstpl_Pkid;
    }

    public String getCstpl_No() {
        return cstpl_No;
    }

    public void setCstpl_No(String cstpl_No) {
        this.cstpl_No = cstpl_No;
    }

    public BigDecimal getCstpl_CttUnitPrice() {
        return cstpl_CttUnitPrice;
    }

    public void setCstpl_CttUnitPrice(BigDecimal cstpl_CttUnitPrice) {
        this.cstpl_CttUnitPrice = cstpl_CttUnitPrice;
    }

    public BigDecimal getCstpl_CttQty() {
        return cstpl_CttQty;
    }

    public void setCstpl_CttQty(BigDecimal cstpl_CttQty) {
        this.cstpl_CttQty = cstpl_CttQty;
    }

    public BigDecimal getCstpl_CttAmt() {
        return cstpl_CttAmt;
    }

    public void setCstpl_CttAmt(BigDecimal cstpl_CttAmt) {
        this.cstpl_CttAmt = cstpl_CttAmt;
    }

    public BigDecimal getTkcttStl_ThisStageMeaQty() {
        return tkcttStl_ThisStageMeaQty;
    }

    public void setTkcttStl_ThisStageMeaQty(BigDecimal tkcttStl_ThisStageMeaQty) {
        this.tkcttStl_ThisStageMeaQty = tkcttStl_ThisStageMeaQty;
    }

    public BigDecimal getTkcttStl_AddUpMeaQty() {
        return tkcttStl_AddUpMeaQty;
    }

    public void setTkcttStl_AddUpMeaQty(BigDecimal tkcttStl_AddUpMeaQty) {
        this.tkcttStl_AddUpMeaQty = tkcttStl_AddUpMeaQty;
    }

    public BigDecimal getTkcttStl_ThisStageMeaAmt() {
        return tkcttStl_ThisStageMeaAmt;
    }

    public void setTkcttStl_ThisStageMeaAmt(BigDecimal tkcttStl_ThisStageMeaAmt) {
        this.tkcttStl_ThisStageMeaAmt = tkcttStl_ThisStageMeaAmt;
    }

    public BigDecimal getTkcttStl_AddUpMeaAmt() {
        return tkcttStl_AddUpMeaAmt;
    }

    public void setTkcttStl_AddUpMeaAmt(BigDecimal tkcttStl_AddUpMeaAmt) {
        this.tkcttStl_AddUpMeaAmt = tkcttStl_AddUpMeaAmt;
    }

    public String getSubctt_CorrespondingPkid() {
        return subctt_CorrespondingPkid;
    }

    public void setSubctt_CorrespondingPkid(String subctt_CorrespondingPkid) {
        this.subctt_CorrespondingPkid = subctt_CorrespondingPkid;
    }

    public BigDecimal getSubcttStl_ThisStageQty() {
        return subcttStl_ThisStageQty;
    }

    public void setSubcttStl_ThisStageQty(BigDecimal subcttStl_ThisStageQty) {
        this.subcttStl_ThisStageQty = subcttStl_ThisStageQty;
    }

    public BigDecimal getSubcttStl_AddUpQty() {
        return subcttStl_AddUpQty;
    }

    public void setSubcttStl_AddUpQty(BigDecimal subcttStl_AddUpQty) {
        this.subcttStl_AddUpQty = subcttStl_AddUpQty;
    }

    public BigDecimal getSubcttStl_ThisStageAmt() {
        return subcttStl_ThisStageAmt;
    }

    public void setSubcttStl_ThisStageAmt(BigDecimal subcttStl_ThisStageAmt) {
        this.subcttStl_ThisStageAmt = subcttStl_ThisStageAmt;
    }

    public BigDecimal getSubcttStl_AddUpAmt() {
        return subcttStl_AddUpAmt;
    }

    public void setSubcttStl_AddUpAmt(BigDecimal subcttStl_AddUpAmt) {
        this.subcttStl_AddUpAmt = subcttStl_AddUpAmt;
    }

    public String getSubctt_SignPartName() {
        return subctt_SignPartName;
    }

    public void setSubctt_SignPartName(String subctt_SignPartName) {
        this.subctt_SignPartName = subctt_SignPartName;
    }

    public BigDecimal getMeaS_AddUpQty() {
        return meaS_AddUpQty;
    }

    public void setMeaS_AddUpQty(BigDecimal meaS_AddUpQty) {
        this.meaS_AddUpQty = meaS_AddUpQty;
    }

    public BigDecimal getMeaS_AddUpAmt() {
        return meaS_AddUpAmt;
    }

    public void setMeaS_AddUpAmt(BigDecimal meaS_AddUpAmt) {
        this.meaS_AddUpAmt = meaS_AddUpAmt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}