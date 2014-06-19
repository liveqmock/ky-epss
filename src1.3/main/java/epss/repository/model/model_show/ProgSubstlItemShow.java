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
public class ProgSubstlItemShow implements Serializable {
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

    private String progSubstl_Pkid;
    private String progSubstl_ProgSubstlInfoPkid;
    private String progSubstl_Unit;
    private BigDecimal progSubstl_UnitPrice;
    private BigDecimal progSubstl_ThisStageQty;
    private BigDecimal progSubstl_ThisStageAmt;
    private BigDecimal progSubstl_AddUpQty;
    private BigDecimal progSubstl_AddUpAmt;
    private String progSubstl_ArchivedFlag;
    private String progSubstl_OriginFlag;
    private String progSubstl_CreatedBy;
    private String progSubstl_CreatedByName;
    private String progSubstl_CreatedTime;
    private String progSubstl_UpdatedBy;
    private String progSubstl_UpdatedByName;
    private String progSubstl_UpdatedTime;
    private Integer progSubstl_RecVersion;
    private String progSubstl_Remark;
    private String progSubstl_SubcttItemPkid;
    private String progSubstl_Tid;

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

    public String getProgSubstl_Pkid() {
        return progSubstl_Pkid;
    }

    public void setProgSubstl_Pkid(String progSubstl_Pkid) {
        this.progSubstl_Pkid = progSubstl_Pkid;
    }

    public String getProgSubstl_ProgSubstlInfoPkid() {
        return progSubstl_ProgSubstlInfoPkid;
    }

    public void setProgSubstl_ProgSubstlInfoPkid(String progSubstl_ProgSubstlInfoPkid) {
        this.progSubstl_ProgSubstlInfoPkid = progSubstl_ProgSubstlInfoPkid;
    }

    public String getProgSubstl_Unit() {
        return progSubstl_Unit;
    }

    public void setProgSubstl_Unit(String progSubstl_Unit) {
        this.progSubstl_Unit = progSubstl_Unit;
    }

    public BigDecimal getProgSubstl_UnitPrice() {
        return progSubstl_UnitPrice;
    }

    public void setProgSubstl_UnitPrice(BigDecimal progSubstl_UnitPrice) {
        this.progSubstl_UnitPrice = progSubstl_UnitPrice;
    }

    public BigDecimal getProgSubstl_ThisStageQty() {
        return progSubstl_ThisStageQty;
    }

    public void setProgSubstl_ThisStageQty(BigDecimal progSubstl_ThisStageQty) {
        this.progSubstl_ThisStageQty = progSubstl_ThisStageQty;
    }

    public BigDecimal getProgSubstl_ThisStageAmt() {
        return progSubstl_ThisStageAmt;
    }

    public void setProgSubstl_ThisStageAmt(BigDecimal progSubstl_ThisStageAmt) {
        this.progSubstl_ThisStageAmt = progSubstl_ThisStageAmt;
    }

    public BigDecimal getProgSubstl_AddUpQty() {
        return progSubstl_AddUpQty;
    }

    public void setProgSubstl_AddUpQty(BigDecimal progSubstl_AddUpQty) {
        this.progSubstl_AddUpQty = progSubstl_AddUpQty;
    }

    public BigDecimal getProgSubstl_AddUpAmt() {
        return progSubstl_AddUpAmt;
    }

    public void setProgSubstl_AddUpAmt(BigDecimal progSubstl_AddUpAmt) {
        this.progSubstl_AddUpAmt = progSubstl_AddUpAmt;
    }

    public String getProgSubstl_ArchivedFlag() {
        return progSubstl_ArchivedFlag;
    }

    public void setProgSubstl_ArchivedFlag(String progSubstl_ArchivedFlag) {
        this.progSubstl_ArchivedFlag = progSubstl_ArchivedFlag;
    }

    public String getProgSubstl_OriginFlag() {
        return progSubstl_OriginFlag;
    }

    public void setProgSubstl_OriginFlag(String progSubstl_OriginFlag) {
        this.progSubstl_OriginFlag = progSubstl_OriginFlag;
    }

    public String getProgSubstl_CreatedBy() {
        return progSubstl_CreatedBy;
    }

    public void setProgSubstl_CreatedBy(String progSubstl_CreatedBy) {
        this.progSubstl_CreatedBy = progSubstl_CreatedBy;
    }

    public String getProgSubstl_CreatedByName() {
        return progSubstl_CreatedByName;
    }

    public void setProgSubstl_CreatedByName(String progSubstl_CreatedByName) {
        this.progSubstl_CreatedByName = progSubstl_CreatedByName;
    }

    public String getProgSubstl_CreatedTime() {
        return progSubstl_CreatedTime;
    }

    public void setProgSubstl_CreatedTime(String progSubstl_CreatedTime) {
        this.progSubstl_CreatedTime = progSubstl_CreatedTime;
    }

    public String getProgSubstl_UpdatedBy() {
        return progSubstl_UpdatedBy;
    }

    public void setProgSubstl_UpdatedBy(String progSubstl_UpdatedBy) {
        this.progSubstl_UpdatedBy = progSubstl_UpdatedBy;
    }

    public String getProgSubstl_UpdatedByName() {
        return progSubstl_UpdatedByName;
    }

    public void setProgSubstl_UpdatedByName(String progSubstl_UpdatedByName) {
        this.progSubstl_UpdatedByName = progSubstl_UpdatedByName;
    }

    public String getProgSubstl_UpdatedTime() {
        return progSubstl_UpdatedTime;
    }

    public void setProgSubstl_UpdatedTime(String progSubstl_UpdatedTime) {
        this.progSubstl_UpdatedTime = progSubstl_UpdatedTime;
    }

    public Integer getProgSubstl_RecVersion() {
        return progSubstl_RecVersion;
    }

    public void setProgSubstl_RecVersion(Integer progSubstl_RecVersion) {
        this.progSubstl_RecVersion = progSubstl_RecVersion;
    }

    public String getProgSubstl_Remark() {
        return progSubstl_Remark;
    }

    public void setProgSubstl_Remark(String progSubstl_Remark) {
        this.progSubstl_Remark = progSubstl_Remark;
    }

    public String getProgSubstl_SubcttItemPkid() {
        return progSubstl_SubcttItemPkid;
    }

    public void setProgSubstl_SubcttItemPkid(String progSubstl_SubcttItemPkid) {
        this.progSubstl_SubcttItemPkid = progSubstl_SubcttItemPkid;
    }

    public String getProgSubstl_Tid() {
        return progSubstl_Tid;
    }

    public void setProgSubstl_Tid(String progSubstl_Tid) {
        this.progSubstl_Tid = progSubstl_Tid;
    }
}
