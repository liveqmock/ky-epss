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
public class ProgWorkqtyItemShow implements Serializable {
    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    private String subctt_StrNo;
    private String subctt_Pkid;
    private String subctt_SubcttInfoPkid;
    private String subctt_ParentPkid;
    private Integer subctt_Grade;
    private Integer subctt_Orderid;
    private String subctt_Name;
    private String subctt_Unit;
    private BigDecimal subctt_UnitPrice;
    private BigDecimal subctt_Qty;
    private BigDecimal subctt_Amt;
    private BigDecimal subctt_SignPartPriceA;
    private String subctt_ArchivedFlag;
    private String subctt_OriginFlag;
    private String subctt_CreatedBy;
    private String subctt_CreatedTime;
    private String subctt_UpdatedBy;
    private String subctt_UpdatedTime;
    private Integer subctt_RecVersion;
    private String subctt_Remark;
    private String subctt_CstplItemPkid;
    private String subctt_Tid;

    private String progWork_Pkid;
    private String progWork_ProgWorkqtyInfoPkid;
    private BigDecimal progWork_AddUpQty;
    private BigDecimal progWork_ThisStageQty;
    private String progWork_ArchivedFlag;
    private String progWork_OriginFlag;
    private String progWork_CreatedBy;
    private String progWork_CreatedByName;
    private String progWork_CreatedTime;
    private String progWork_UpdatedBy;
    private String progWork_UpdatedByName;
    private String progWork_UpdatedTime;
    private Integer progWork_RecVersion;
    private String progWork_SubcttItemPkid;
    private String progWork_Remark;
    private String progWork_Tid;

    public String getSubctt_StrNo() {
        return subctt_StrNo;
    }

    public void setSubctt_StrNo(String subctt_StrNo) {
        this.subctt_StrNo = subctt_StrNo;
    }

    public String getSubctt_Pkid() {
        return subctt_Pkid;
    }

    public void setSubctt_Pkid(String subctt_Pkid) {
        this.subctt_Pkid = subctt_Pkid;
    }

    public String getSubctt_SubcttInfoPkid() {
        return subctt_SubcttInfoPkid;
    }

    public void setSubctt_SubcttInfoPkid(String subctt_SubcttInfoPkid) {
        this.subctt_SubcttInfoPkid = subctt_SubcttInfoPkid;
    }

    public String getSubctt_ParentPkid() {
        return subctt_ParentPkid;
    }

    public void setSubctt_ParentPkid(String subctt_ParentPkid) {
        this.subctt_ParentPkid = subctt_ParentPkid;
    }

    public Integer getSubctt_Grade() {
        return subctt_Grade;
    }

    public void setSubctt_Grade(Integer subctt_Grade) {
        this.subctt_Grade = subctt_Grade;
    }

    public Integer getSubctt_Orderid() {
        return subctt_Orderid;
    }

    public void setSubctt_Orderid(Integer subctt_Orderid) {
        this.subctt_Orderid = subctt_Orderid;
    }

    public String getSubctt_Name() {
        return subctt_Name;
    }

    public void setSubctt_Name(String subctt_Name) {
        this.subctt_Name = subctt_Name;
    }

    public String getSubctt_Unit() {
        return subctt_Unit;
    }

    public void setSubctt_Unit(String subctt_Unit) {
        this.subctt_Unit = subctt_Unit;
    }

    public BigDecimal getSubctt_UnitPrice() {
        return subctt_UnitPrice;
    }

    public void setSubctt_UnitPrice(BigDecimal subctt_UnitPrice) {
        this.subctt_UnitPrice = subctt_UnitPrice;
    }

    public BigDecimal getSubctt_Qty() {
        return subctt_Qty;
    }

    public void setSubctt_Qty(BigDecimal subctt_Qty) {
        this.subctt_Qty = subctt_Qty;
    }

    public BigDecimal getSubctt_Amt() {
        return subctt_Amt;
    }

    public void setSubctt_Amt(BigDecimal subctt_Amt) {
        this.subctt_Amt = subctt_Amt;
    }

    public BigDecimal getSubctt_SignPartPriceA() {
        return subctt_SignPartPriceA;
    }

    public void setSubctt_SignPartPriceA(BigDecimal subctt_SignPartPriceA) {
        this.subctt_SignPartPriceA = subctt_SignPartPriceA;
    }

    public String getSubctt_ArchivedFlag() {
        return subctt_ArchivedFlag;
    }

    public void setSubctt_ArchivedFlag(String subctt_ArchivedFlag) {
        this.subctt_ArchivedFlag = subctt_ArchivedFlag;
    }

    public String getSubctt_OriginFlag() {
        return subctt_OriginFlag;
    }

    public void setSubctt_OriginFlag(String subctt_OriginFlag) {
        this.subctt_OriginFlag = subctt_OriginFlag;
    }

    public String getSubctt_CreatedBy() {
        return subctt_CreatedBy;
    }

    public void setSubctt_CreatedBy(String subctt_CreatedBy) {
        this.subctt_CreatedBy = subctt_CreatedBy;
    }

    public String getSubctt_CreatedTime() {
        return subctt_CreatedTime;
    }

    public void setSubctt_CreatedTime(String subctt_CreatedTime) {
        this.subctt_CreatedTime = subctt_CreatedTime;
    }

    public String getSubctt_UpdatedBy() {
        return subctt_UpdatedBy;
    }

    public void setSubctt_UpdatedBy(String subctt_UpdatedBy) {
        this.subctt_UpdatedBy = subctt_UpdatedBy;
    }

    public String getSubctt_UpdatedTime() {
        return subctt_UpdatedTime;
    }

    public void setSubctt_UpdatedTime(String subctt_UpdatedTime) {
        this.subctt_UpdatedTime = subctt_UpdatedTime;
    }

    public Integer getSubctt_RecVersion() {
        return subctt_RecVersion;
    }

    public void setSubctt_RecVersion(Integer subctt_RecVersion) {
        this.subctt_RecVersion = subctt_RecVersion;
    }

    public String getSubctt_Remark() {
        return subctt_Remark;
    }

    public void setSubctt_Remark(String subctt_Remark) {
        this.subctt_Remark = subctt_Remark;
    }

    public String getSubctt_CstplItemPkid() {
        return subctt_CstplItemPkid;
    }

    public void setSubctt_CstplItemPkid(String subctt_CstplItemPkid) {
        this.subctt_CstplItemPkid = subctt_CstplItemPkid;
    }

    public String getSubctt_Tid() {
        return subctt_Tid;
    }

    public void setSubctt_Tid(String subctt_Tid) {
        this.subctt_Tid = subctt_Tid;
    }

    public String getProgWork_Pkid() {
        return progWork_Pkid;
    }

    public void setProgWork_Pkid(String progWork_Pkid) {
        this.progWork_Pkid = progWork_Pkid;
    }

    public String getProgWork_ProgWorkqtyInfoPkid() {
        return progWork_ProgWorkqtyInfoPkid;
    }

    public void setProgWork_ProgWorkqtyInfoPkid(String progWork_ProgWorkqtyInfoPkid) {
        this.progWork_ProgWorkqtyInfoPkid = progWork_ProgWorkqtyInfoPkid;
    }

    public BigDecimal getProgWork_AddUpQty() {
        return progWork_AddUpQty;
    }

    public void setProgWork_AddUpQty(BigDecimal progWork_AddUpQty) {
        this.progWork_AddUpQty = progWork_AddUpQty;
    }

    public BigDecimal getProgWork_ThisStageQty() {
        return progWork_ThisStageQty;
    }

    public void setProgWork_ThisStageQty(BigDecimal progWork_ThisStageQty) {
        this.progWork_ThisStageQty = progWork_ThisStageQty;
    }

    public String getProgWork_ArchivedFlag() {
        return progWork_ArchivedFlag;
    }

    public void setProgWork_ArchivedFlag(String progWork_ArchivedFlag) {
        this.progWork_ArchivedFlag = progWork_ArchivedFlag;
    }

    public String getProgWork_OriginFlag() {
        return progWork_OriginFlag;
    }

    public void setProgWork_OriginFlag(String progWork_OriginFlag) {
        this.progWork_OriginFlag = progWork_OriginFlag;
    }

    public String getProgWork_CreatedBy() {
        return progWork_CreatedBy;
    }

    public void setProgWork_CreatedBy(String progWork_CreatedBy) {
        this.progWork_CreatedBy = progWork_CreatedBy;
    }

    public String getProgWork_CreatedByName() {
        return progWork_CreatedByName;
    }

    public void setProgWork_CreatedByName(String progWork_CreatedByName) {
        this.progWork_CreatedByName = progWork_CreatedByName;
    }

    public String getProgWork_CreatedTime() {
        return progWork_CreatedTime;
    }

    public void setProgWork_CreatedTime(String progWork_CreatedTime) {
        this.progWork_CreatedTime = progWork_CreatedTime;
    }

    public String getProgWork_UpdatedBy() {
        return progWork_UpdatedBy;
    }

    public void setProgWork_UpdatedBy(String progWork_UpdatedBy) {
        this.progWork_UpdatedBy = progWork_UpdatedBy;
    }

    public String getProgWork_UpdatedByName() {
        return progWork_UpdatedByName;
    }

    public void setProgWork_UpdatedByName(String progWork_UpdatedByName) {
        this.progWork_UpdatedByName = progWork_UpdatedByName;
    }

    public String getProgWork_UpdatedTime() {
        return progWork_UpdatedTime;
    }

    public void setProgWork_UpdatedTime(String progWork_UpdatedTime) {
        this.progWork_UpdatedTime = progWork_UpdatedTime;
    }

    public Integer getProgWork_RecVersion() {
        return progWork_RecVersion;
    }

    public void setProgWork_RecVersion(Integer progWork_RecVersion) {
        this.progWork_RecVersion = progWork_RecVersion;
    }

    public String getProgWork_SubcttItemPkid() {
        return progWork_SubcttItemPkid;
    }

    public void setProgWork_SubcttItemPkid(String progWork_SubcttItemPkid) {
        this.progWork_SubcttItemPkid = progWork_SubcttItemPkid;
    }

    public String getProgWork_Remark() {
        return progWork_Remark;
    }

    public void setProgWork_Remark(String progWork_Remark) {
        this.progWork_Remark = progWork_Remark;
    }

    public String getProgWork_Tid() {
        return progWork_Tid;
    }

    public void setProgWork_Tid(String progWork_Tid) {
        this.progWork_Tid = progWork_Tid;
    }
}
