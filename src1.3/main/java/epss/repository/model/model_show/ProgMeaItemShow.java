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
public class ProgMeaItemShow implements Serializable {
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

    private String progMea_Pkid;
    private String progMea_PorgMeaInfoPkid;
    private BigDecimal progMea_ThisStageQty;
    private BigDecimal progMea_ThisStageAmt;
    private BigDecimal progMea_AddUpQty;
    private BigDecimal progMea_AddUpAmt;
    private String progMea_ArchivedFlag;
    private String progMea_OriginFlag;
    private String progMea_CreatedBy;
    private String progMea_CreatedByName;
    private String progMea_CreatedTime;
    private String progMea_UpdatedBy;
    private String progMea_UpdatedByName;
    private String progMea_UpdatedTime;
    private Integer progMea_RecVersion;
    private String progMea_Remark;
    private String progMea_TkcttItemPkid;
    private String progMea_Tid;

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

    public Integer getTkctt_RecVersion() {
        return tkctt_RecVersion;
    }

    public void setTkctt_RecVersion(Integer tkctt_RecVersion) {
        this.tkctt_RecVersion = tkctt_RecVersion;
    }

    public String getTkctt_Remark() {
        return tkctt_Remark;
    }

    public void setTkctt_Remark(String tkctt_Remark) {
        this.tkctt_Remark = tkctt_Remark;
    }

    public String getTkctt_Tid() {
        return tkctt_Tid;
    }

    public void setTkctt_Tid(String tkctt_Tid) {
        this.tkctt_Tid = tkctt_Tid;
    }

    public String getProgMea_Pkid() {
        return progMea_Pkid;
    }

    public void setProgMea_Pkid(String progMea_Pkid) {
        this.progMea_Pkid = progMea_Pkid;
    }

    public String getProgMea_PorgMeaInfoPkid() {
        return progMea_PorgMeaInfoPkid;
    }

    public void setProgMea_PorgMeaInfoPkid(String progMea_PorgMeaInfoPkid) {
        this.progMea_PorgMeaInfoPkid = progMea_PorgMeaInfoPkid;
    }

    public BigDecimal getProgMea_ThisStageQty() {
        return progMea_ThisStageQty;
    }

    public void setProgMea_ThisStageQty(BigDecimal progMea_ThisStageQty) {
        this.progMea_ThisStageQty = progMea_ThisStageQty;
    }

    public BigDecimal getProgMea_ThisStageAmt() {
        return progMea_ThisStageAmt;
    }

    public void setProgMea_ThisStageAmt(BigDecimal progMea_ThisStageAmt) {
        this.progMea_ThisStageAmt = progMea_ThisStageAmt;
    }

    public BigDecimal getProgMea_AddUpQty() {
        return progMea_AddUpQty;
    }

    public void setProgMea_AddUpQty(BigDecimal progMea_AddUpQty) {
        this.progMea_AddUpQty = progMea_AddUpQty;
    }

    public BigDecimal getProgMea_AddUpAmt() {
        return progMea_AddUpAmt;
    }

    public void setProgMea_AddUpAmt(BigDecimal progMea_AddUpAmt) {
        this.progMea_AddUpAmt = progMea_AddUpAmt;
    }

    public String getProgMea_ArchivedFlag() {
        return progMea_ArchivedFlag;
    }

    public void setProgMea_ArchivedFlag(String progMea_ArchivedFlag) {
        this.progMea_ArchivedFlag = progMea_ArchivedFlag;
    }

    public String getProgMea_OriginFlag() {
        return progMea_OriginFlag;
    }

    public void setProgMea_OriginFlag(String progMea_OriginFlag) {
        this.progMea_OriginFlag = progMea_OriginFlag;
    }

    public String getProgMea_CreatedBy() {
        return progMea_CreatedBy;
    }

    public void setProgMea_CreatedBy(String progMea_CreatedBy) {
        this.progMea_CreatedBy = progMea_CreatedBy;
    }

    public String getProgMea_CreatedByName() {
        return progMea_CreatedByName;
    }

    public void setProgMea_CreatedByName(String progMea_CreatedByName) {
        this.progMea_CreatedByName = progMea_CreatedByName;
    }

    public String getProgMea_CreatedTime() {
        return progMea_CreatedTime;
    }

    public void setProgMea_CreatedTime(String progMea_CreatedTime) {
        this.progMea_CreatedTime = progMea_CreatedTime;
    }

    public String getProgMea_UpdatedBy() {
        return progMea_UpdatedBy;
    }

    public void setProgMea_UpdatedBy(String progMea_UpdatedBy) {
        this.progMea_UpdatedBy = progMea_UpdatedBy;
    }

    public String getProgMea_UpdatedByName() {
        return progMea_UpdatedByName;
    }

    public void setProgMea_UpdatedByName(String progMea_UpdatedByName) {
        this.progMea_UpdatedByName = progMea_UpdatedByName;
    }

    public String getProgMea_UpdatedTime() {
        return progMea_UpdatedTime;
    }

    public void setProgMea_UpdatedTime(String progMea_UpdatedTime) {
        this.progMea_UpdatedTime = progMea_UpdatedTime;
    }

    public Integer getProgMea_RecVersion() {
        return progMea_RecVersion;
    }

    public void setProgMea_RecVersion(Integer progMea_RecVersion) {
        this.progMea_RecVersion = progMea_RecVersion;
    }

    public String getProgMea_Remark() {
        return progMea_Remark;
    }

    public void setProgMea_Remark(String progMea_Remark) {
        this.progMea_Remark = progMea_Remark;
    }

    public String getProgMea_TkcttItemPkid() {
        return progMea_TkcttItemPkid;
    }

    public void setProgMea_TkcttItemPkid(String progMea_TkcttItemPkid) {
        this.progMea_TkcttItemPkid = progMea_TkcttItemPkid;
    }

    public String getProgMea_Tid() {
        return progMea_Tid;
    }

    public void setProgMea_Tid(String progMea_Tid) {
        this.progMea_Tid = progMea_Tid;
    }
}
