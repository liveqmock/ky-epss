package epss.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class ProgEstItemShow implements Serializable {

    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    private String tkctt_StrNo;
    private String tkctt_Pkid;
    private String tkctt_TkcttInfoPkid;
    private String tkctt_ParentPkid;
    private Integer tkctt_Grade;
    private Integer tkctt_Orderid;
    private String tkctt_Name;
    private String tkctt_Unit;
    private BigDecimal tkctt_UnitPrice;
    private BigDecimal tkctt_Qty;
    private BigDecimal tkctt_Amt;
    private String tkctt_ArchivedFlag;
    private String tkctt_OriginFlag;
    private String tkctt_CreatedBy;
    private String tkctt_CreatedTime;
    private String tkctt_UpdatedBy;
    private String tkctt_UpdatedTime;
	private Integer tkctt_RecVersion;
    private String tkctt_Remark;
	private String tkctt_Tid;

    private String progEst_Pkid;
    private String progEst_PorgEstInfoPkid;
    private BigDecimal progEst_ThisStageQty;
    private BigDecimal progEst_ThisStageAmt;
    private BigDecimal progEst_AddUpQty;
    private BigDecimal progEst_AddUpAmt;
    private String progEst_ArchivedFlag;
    private String progEst_OriginFlag;
    private String progEst_CreatedBy;
    private String progEst_CreatedByName;
    private String progEst_CreatedTime;
    private String progEst_UpdatedBy;
    private String progEst_UpdatedByName;
    private String progEst_UpdatedTime;
    private Integer progEst_RecVersion;
    private String progEst_Remark;
    private String progEst_TkcttItemPkid;

    public String getTkctt_StrNo() {
        return tkctt_StrNo;
    }

    public void setTkctt_StrNo(String tkctt_StrNo) {
        this.tkctt_StrNo = tkctt_StrNo;
    }

    public String getTkctt_Pkid() {
        return tkctt_Pkid;
    }

    public void setTkctt_Pkid(String tkctt_Pkid) {
        this.tkctt_Pkid = tkctt_Pkid;
    }

    public String getTkctt_TkcttInfoPkid() {
        return tkctt_TkcttInfoPkid;
    }

    public void setTkctt_TkcttInfoPkid(String tkctt_TkcttInfoPkid) {
        this.tkctt_TkcttInfoPkid = tkctt_TkcttInfoPkid;
    }

    public String getTkctt_ParentPkid() {
        return tkctt_ParentPkid;
    }

    public void setTkctt_ParentPkid(String tkctt_ParentPkid) {
        this.tkctt_ParentPkid = tkctt_ParentPkid;
    }

    public Integer getTkctt_Grade() {
        return tkctt_Grade;
    }

    public void setTkctt_Grade(Integer tkctt_Grade) {
        this.tkctt_Grade = tkctt_Grade;
    }

    public Integer getTkctt_Orderid() {
        return tkctt_Orderid;
    }

    public void setTkctt_Orderid(Integer tkctt_Orderid) {
        this.tkctt_Orderid = tkctt_Orderid;
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

    public BigDecimal getTkctt_UnitPrice() {
        return tkctt_UnitPrice;
    }

    public void setTkctt_UnitPrice(BigDecimal tkctt_UnitPrice) {
        this.tkctt_UnitPrice = tkctt_UnitPrice;
    }

    public BigDecimal getTkctt_Qty() {
        return tkctt_Qty;
    }

    public void setTkctt_Qty(BigDecimal tkctt_Qty) {
        this.tkctt_Qty = tkctt_Qty;
    }

    public BigDecimal getTkctt_Amt() {
        return tkctt_Amt;
    }

    public void setTkctt_Amt(BigDecimal tkctt_Amt) {
        this.tkctt_Amt = tkctt_Amt;
    }

    public String getTkctt_ArchivedFlag() {
        return tkctt_ArchivedFlag;
    }

    public void setTkctt_ArchivedFlag(String tkctt_ArchivedFlag) {
        this.tkctt_ArchivedFlag = tkctt_ArchivedFlag;
    }

    public String getTkctt_OriginFlag() {
        return tkctt_OriginFlag;
    }

    public void setTkctt_OriginFlag(String tkctt_OriginFlag) {
        this.tkctt_OriginFlag = tkctt_OriginFlag;
    }

    public String getTkctt_CreatedBy() {
        return tkctt_CreatedBy;
    }

    public void setTkctt_CreatedBy(String tkctt_CreatedBy) {
        this.tkctt_CreatedBy = tkctt_CreatedBy;
    }

    public String getTkctt_CreatedTime() {
        return tkctt_CreatedTime;
    }

    public void setTkctt_CreatedTime(String tkctt_CreatedTime) {
        this.tkctt_CreatedTime = tkctt_CreatedTime;
    }

    public String getTkctt_UpdatedBy() {
        return tkctt_UpdatedBy;
    }

    public void setTkctt_UpdatedBy(String tkctt_UpdatedBy) {
        this.tkctt_UpdatedBy = tkctt_UpdatedBy;
    }

    public String getTkctt_UpdatedTime() {
        return tkctt_UpdatedTime;
    }

    public void setTkctt_UpdatedTime(String tkctt_UpdatedTime) {
        this.tkctt_UpdatedTime = tkctt_UpdatedTime;
    }

    public String getTkctt_Remark() {
        return tkctt_Remark;
    }

    public void setTkctt_Remark(String tkctt_Remark) {
        this.tkctt_Remark = tkctt_Remark;
    }

    public String getProgEst_Pkid() {
        return progEst_Pkid;
    }

    public void setProgEst_Pkid(String progEst_Pkid) {
        this.progEst_Pkid = progEst_Pkid;
    }

    public String getProgEst_PorgEstInfoPkid() {
        return progEst_PorgEstInfoPkid;
    }

    public void setProgEst_PorgEstInfoPkid(String progEst_PorgEstInfoPkid) {
        this.progEst_PorgEstInfoPkid = progEst_PorgEstInfoPkid;
    }

    public String getProgEst_TkcttItemPkid() {
        return progEst_TkcttItemPkid;
    }

    public void setProgEst_TkcttItemPkid(String progEst_TkcttItemPkid) {
        this.progEst_TkcttItemPkid = progEst_TkcttItemPkid;
    }

    public BigDecimal getProgEst_ThisStageQty() {
        return progEst_ThisStageQty;
    }

    public void setProgEst_ThisStageQty(BigDecimal progEst_ThisStageQty) {
        this.progEst_ThisStageQty = progEst_ThisStageQty;
    }

    public BigDecimal getProgEst_ThisStageAmt() {
        return progEst_ThisStageAmt;
    }

    public void setProgEst_ThisStageAmt(BigDecimal progEst_ThisStageAmt) {
        this.progEst_ThisStageAmt = progEst_ThisStageAmt;
    }

    public BigDecimal getProgEst_AddUpQty() {
        return progEst_AddUpQty;
    }

    public void setProgEst_AddUpQty(BigDecimal progEst_AddUpQty) {
        this.progEst_AddUpQty = progEst_AddUpQty;
    }

    public BigDecimal getProgEst_AddUpAmt() {
        return progEst_AddUpAmt;
    }

    public void setProgEst_AddUpAmt(BigDecimal progEst_AddUpAmt) {
        this.progEst_AddUpAmt = progEst_AddUpAmt;
    }

    public String getProgEst_ArchivedFlag() {
        return progEst_ArchivedFlag;
    }

    public void setProgEst_ArchivedFlag(String progEst_ArchivedFlag) {
        this.progEst_ArchivedFlag = progEst_ArchivedFlag;
    }

    public String getProgEst_OriginFlag() {
        return progEst_OriginFlag;
    }

    public void setProgEst_OriginFlag(String progEst_OriginFlag) {
        this.progEst_OriginFlag = progEst_OriginFlag;
    }

    public String getProgEst_CreatedBy() {
        return progEst_CreatedBy;
    }

    public void setProgEst_CreatedBy(String progEst_CreatedBy) {
        this.progEst_CreatedBy = progEst_CreatedBy;
    }

    public String getProgEst_CreatedByName() {
        return progEst_CreatedByName;
    }

    public void setProgEst_CreatedByName(String progEst_CreatedByName) {
        this.progEst_CreatedByName = progEst_CreatedByName;
    }

    public String getProgEst_CreatedTime() {
        return progEst_CreatedTime;
    }

    public void setProgEst_CreatedTime(String progEst_CreatedTime) {
        this.progEst_CreatedTime = progEst_CreatedTime;
    }

    public String getProgEst_UpdatedBy() {
        return progEst_UpdatedBy;
    }

    public void setProgEst_UpdatedBy(String progEst_UpdatedBy) {
        this.progEst_UpdatedBy = progEst_UpdatedBy;
    }

    public String getProgEst_UpdatedByName() {
        return progEst_UpdatedByName;
    }

    public void setProgEst_UpdatedByName(String progEst_UpdatedByName) {
        this.progEst_UpdatedByName = progEst_UpdatedByName;
    }

    public String getProgEst_UpdatedTime() {
        return progEst_UpdatedTime;
    }

    public void setProgEst_UpdatedTime(String progEst_UpdatedTime) {
        this.progEst_UpdatedTime = progEst_UpdatedTime;
    }

    public String getProgEst_Remark() {
        return progEst_Remark;
    }

    public void setProgEst_Remark(String progEst_Remark) {
        this.progEst_Remark = progEst_Remark;
    }

    public Integer getProgEst_RecVersion() {
        return progEst_RecVersion;
    }

    public void setProgEst_RecVersion(Integer progEst_RecVersion) {
        this.progEst_RecVersion = progEst_RecVersion;
    }
}
