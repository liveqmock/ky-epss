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
public class ProgMatqtyItemShow implements Serializable {

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

    private String progMat_Pkid;
    private String progMat_ProgMatqtyInfoPkid;
    private BigDecimal progMat_AddUpQty;
    private BigDecimal progMat_ThisStageQty;
    private BigDecimal progMat_MPurchaseUnitPrice;
    private String progMat_ArchivedFlag;
    private String progMat_OriginFlag;
    private String progMat_CreatedBy;
    private String progMat_CreatedByName;
    private String progMat_CreatedTime;
    private String progMat_UpdatedBy;
    private String progMat_UpdatedByName;
    private String progMat_UpdatedTime;
    private Integer progMat_RecVersion;
    private String progMat_SubcttItemPkid;
    private String progMat_Remark;
    private String progMat_Tid;

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

    public String getProgMat_Pkid() {
        return progMat_Pkid;
    }

    public void setProgMat_Pkid(String progMat_Pkid) {
        this.progMat_Pkid = progMat_Pkid;
    }

    public String getProgMat_ProgMatqtyInfoPkid() {
        return progMat_ProgMatqtyInfoPkid;
    }

    public void setProgMat_ProgMatqtyInfoPkid(String progMat_ProgMatqtyInfoPkid) {
        this.progMat_ProgMatqtyInfoPkid = progMat_ProgMatqtyInfoPkid;
    }

    public BigDecimal getProgMat_AddUpQty() {
        return progMat_AddUpQty;
    }

    public void setProgMat_AddUpQty(BigDecimal progMat_AddUpQty) {
        this.progMat_AddUpQty = progMat_AddUpQty;
    }

    public BigDecimal getProgMat_ThisStageQty() {
        return progMat_ThisStageQty;
    }

    public void setProgMat_ThisStageQty(BigDecimal progMat_ThisStageQty) {
        this.progMat_ThisStageQty = progMat_ThisStageQty;
    }

    public BigDecimal getProgMat_MPurchaseUnitPrice() {
        return progMat_MPurchaseUnitPrice;
    }

    public void setProgMat_MPurchaseUnitPrice(BigDecimal progMat_MPurchaseUnitPrice) {
        this.progMat_MPurchaseUnitPrice = progMat_MPurchaseUnitPrice;
    }

    public String getProgMat_ArchivedFlag() {
        return progMat_ArchivedFlag;
    }

    public void setProgMat_ArchivedFlag(String progMat_ArchivedFlag) {
        this.progMat_ArchivedFlag = progMat_ArchivedFlag;
    }

    public String getProgMat_OriginFlag() {
        return progMat_OriginFlag;
    }

    public void setProgMat_OriginFlag(String progMat_OriginFlag) {
        this.progMat_OriginFlag = progMat_OriginFlag;
    }

    public String getProgMat_CreatedBy() {
        return progMat_CreatedBy;
    }

    public void setProgMat_CreatedBy(String progMat_CreatedBy) {
        this.progMat_CreatedBy = progMat_CreatedBy;
    }

    public String getProgMat_CreatedByName() {
        return progMat_CreatedByName;
    }

    public void setProgMat_CreatedByName(String progMat_CreatedByName) {
        this.progMat_CreatedByName = progMat_CreatedByName;
    }

    public String getProgMat_CreatedTime() {
        return progMat_CreatedTime;
    }

    public void setProgMat_CreatedTime(String progMat_CreatedTime) {
        this.progMat_CreatedTime = progMat_CreatedTime;
    }

    public String getProgMat_UpdatedBy() {
        return progMat_UpdatedBy;
    }

    public void setProgMat_UpdatedBy(String progMat_UpdatedBy) {
        this.progMat_UpdatedBy = progMat_UpdatedBy;
    }

    public String getProgMat_UpdatedByName() {
        return progMat_UpdatedByName;
    }

    public void setProgMat_UpdatedByName(String progMat_UpdatedByName) {
        this.progMat_UpdatedByName = progMat_UpdatedByName;
    }

    public String getProgMat_UpdatedTime() {
        return progMat_UpdatedTime;
    }

    public void setProgMat_UpdatedTime(String progMat_UpdatedTime) {
        this.progMat_UpdatedTime = progMat_UpdatedTime;
    }

    public Integer getProgMat_RecVersion() {
        return progMat_RecVersion;
    }

    public void setProgMat_RecVersion(Integer progMat_RecVersion) {
        this.progMat_RecVersion = progMat_RecVersion;
    }

    public String getProgMat_SubcttItemPkid() {
        return progMat_SubcttItemPkid;
    }

    public void setProgMat_SubcttItemPkid(String progMat_SubcttItemPkid) {
        this.progMat_SubcttItemPkid = progMat_SubcttItemPkid;
    }

    public String getProgMat_Remark() {
        return progMat_Remark;
    }

    public void setProgMat_Remark(String progMat_Remark) {
        this.progMat_Remark = progMat_Remark;
    }

    public String getProgMat_Tid() {
        return progMat_Tid;
    }

    public void setProgMat_Tid(String progMat_Tid) {
        this.progMat_Tid = progMat_Tid;
    }
}
