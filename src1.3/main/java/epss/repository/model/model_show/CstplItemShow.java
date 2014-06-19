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

public class CstplItemShow implements Serializable {
    private String tkctt_Pkid;
    private String tkctt_TkcttInfoPkid;
    private String tkctt_ParentPkid;
    private String tkctt_StrNo;
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
    private String tkctt_CreatedByName;
    private String tkctt_CreatedTime;
    private String tkctt_UpdatedBy;
    private String tkctt_UpdatedByName;
    private String tkctt_UpdatedTime;
    private Integer tkctt_RecVersion;
    private String tkctt_Remark;
    private String tkctt_Tid;

    private String cstpl_Pkid;
    private String cstpl_CstplInfoPkid;
    private String cstpl_ParentPkid;
    private String cstpl_StrNo;
    private Integer cstpl_Grade;
    private Integer cstpl_Orderid;
    private String cstpl_Name;
    private String cstpl_Unit;
    private BigDecimal cstpl_UnitPrice;
    private BigDecimal cstpl_Qty;
    private BigDecimal cstpl_Amt;
    private String cstpl_ArchivedFlag;
    private String cstpl_OriginFlag;
    private String cstpl_CreatedBy;
    private String cstpl_CreatedByName;
    private String cstpl_CreatedTime;
    private String cstpl_UpdatedBy;
    private String cstpl_UpdatedByName;
    private String cstpl_UpdatedTime;
    private Integer cstpl_RecVersion;
    private String cstpl_Remark;
    private String cstpl_TkcttItemPkid;
    private String cstpl_TkcttItemNo;
    private String cstpl_Tid;

    public CstplItemShow() {
    }

    public CstplItemShow(String strCstplInfoPkidPara) {
        this.cstpl_CstplInfoPkid=strCstplInfoPkidPara;
    }

    public CstplItemShow(
            String tkctt_PkidPara,
            String tkctt_TkcttInfoPkidPara,
            String tkctt_ParentPkidPara,
            String tkctt_StrNoPara,
            Integer tkctt_GradePara,
            Integer tkctt_OrderidPara,
            String tkctt_NamePara,
            String tkctt_UnitPara,
            BigDecimal tkctt_UnitPricePara,
            BigDecimal tkctt_QtyPara,
            BigDecimal tkctt_AmtPara,
            String tkctt_ArchivedFlagPara,
            String tkctt_OriginFlagPara,
            String tkctt_CreatedByPara,
            String tkctt_CreatedByNamePara,
            String tkctt_CreatedTimePara,
            String tkctt_UpdatedByPara,
            String tkctt_UpdatedByNamePara,
            String tkctt_UpdatedTimePara,
            Integer tkctt_RecVersionPara,
            String tkctt_RemarkPara,
            String tkctt_TidPara) {
        tkctt_Pkid=tkctt_PkidPara;
        tkctt_TkcttInfoPkid=tkctt_TkcttInfoPkidPara;
        tkctt_ParentPkid=tkctt_ParentPkidPara;
        tkctt_StrNo=tkctt_StrNoPara;
        tkctt_Grade=tkctt_GradePara;
        tkctt_Orderid=tkctt_OrderidPara;
        tkctt_Name=tkctt_NamePara;
        tkctt_Unit=tkctt_UnitPara;
        tkctt_UnitPrice=tkctt_UnitPricePara;
        tkctt_Qty=tkctt_QtyPara;
        tkctt_Amt=tkctt_AmtPara;
        tkctt_ArchivedFlag=tkctt_ArchivedFlagPara;
        tkctt_OriginFlag=tkctt_OriginFlagPara;
        tkctt_CreatedBy=tkctt_CreatedByPara;
        tkctt_CreatedByName=tkctt_CreatedByNamePara;
        tkctt_CreatedTime=tkctt_CreatedTimePara;
        tkctt_UpdatedBy=tkctt_UpdatedByPara;
        tkctt_UpdatedByName=tkctt_UpdatedByNamePara;
        tkctt_UpdatedTime=tkctt_UpdatedTimePara;
        tkctt_RecVersion=tkctt_RecVersionPara;
        tkctt_Remark=tkctt_RemarkPara;
        tkctt_Tid=tkctt_TidPara;
    }

    public CstplItemShow(
            String cstpl_PkidPara,
            String cstpl_CstplInfoPkidPara,
            String cstpl_ParentPkidPara,
            String cstpl_StrNoPara,
            Integer cstpl_GradePara,
            Integer cstpl_OrderidPara,
            String cstpl_NamePara,
            String cstpl_UnitPara,
            BigDecimal cstpl_UnitPricePara,
            BigDecimal cstpl_QtyPara,
            BigDecimal cstpl_AmtPara,
            String cstpl_ArchivedFlagPara,
            String cstpl_OriginFlagPara,
            String cstpl_CreatedByPara,
            String cstpl_CreatedByNamePara,
            String cstpl_CreatedTimePara,
            String cstpl_UpdatedByPara,
            String cstpl_UpdatedByNamePara,
            String cstpl_UpdatedTimePara,
            Integer cstpl_RecVersionPara,
            String cstpl_RemarkPara,
            String cstpl_TkcttItemPkidPara,
            String cstpl_TkcttItemNoPara,
            String cstpl_TidPara) {
        cstpl_Pkid=cstpl_PkidPara;
        cstpl_CstplInfoPkid=cstpl_CstplInfoPkidPara;
        cstpl_ParentPkid=cstpl_ParentPkidPara;
        cstpl_StrNo=cstpl_StrNoPara;
        cstpl_Grade=cstpl_GradePara;
        cstpl_Orderid=cstpl_OrderidPara;
        cstpl_Name=cstpl_NamePara;
        cstpl_Unit=cstpl_UnitPara;
        cstpl_UnitPrice=cstpl_UnitPricePara;
        cstpl_Qty=cstpl_QtyPara;
        cstpl_Amt=cstpl_AmtPara;
        cstpl_ArchivedFlag=cstpl_ArchivedFlagPara;
        cstpl_OriginFlag=cstpl_OriginFlagPara;
        cstpl_CreatedBy=cstpl_CreatedByPara;
        cstpl_CreatedByName=cstpl_CreatedByNamePara;
        cstpl_CreatedTime=cstpl_CreatedTimePara;
        cstpl_UpdatedBy=cstpl_UpdatedByPara;
        cstpl_UpdatedByName=cstpl_UpdatedByNamePara;
        cstpl_UpdatedTime=cstpl_UpdatedTimePara;
        cstpl_RecVersion=cstpl_RecVersionPara;
        cstpl_Remark=cstpl_RemarkPara;
        cstpl_TkcttItemPkid=cstpl_TkcttItemPkidPara;
        cstpl_TkcttItemNo=cstpl_TkcttItemNoPara;
        cstpl_Tid=cstpl_TidPara;
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

    public String getTkctt_StrNo() {
        return tkctt_StrNo;
    }

    public void setTkctt_StrNo(String tkctt_StrNo) {
        this.tkctt_StrNo = tkctt_StrNo;
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

    public String getCstpl_Pkid() {
        return cstpl_Pkid;
    }

    public void setCstpl_Pkid(String cstpl_Pkid) {
        this.cstpl_Pkid = cstpl_Pkid;
    }

    public String getCstpl_CstplInfoPkid() {
        return cstpl_CstplInfoPkid;
    }

    public void setCstpl_CstplInfoPkid(String cstpl_CstplInfoPkid) {
        this.cstpl_CstplInfoPkid = cstpl_CstplInfoPkid;
    }

    public String getCstpl_ParentPkid() {
        return cstpl_ParentPkid;
    }

    public void setCstpl_ParentPkid(String cstpl_ParentPkid) {
        this.cstpl_ParentPkid = cstpl_ParentPkid;
    }

    public String getCstpl_StrNo() {
        return cstpl_StrNo;
    }

    public void setCstpl_StrNo(String cstpl_StrNo) {
        this.cstpl_StrNo = cstpl_StrNo;
    }

    public Integer getCstpl_Grade() {
        return cstpl_Grade;
    }

    public void setCstpl_Grade(Integer cstpl_Grade) {
        this.cstpl_Grade = cstpl_Grade;
    }

    public Integer getCstpl_Orderid() {
        return cstpl_Orderid;
    }

    public void setCstpl_Orderid(Integer cstpl_Orderid) {
        this.cstpl_Orderid = cstpl_Orderid;
    }

    public String getCstpl_Name() {
        return cstpl_Name;
    }

    public void setCstpl_Name(String cstpl_Name) {
        this.cstpl_Name = cstpl_Name;
    }

    public String getCstpl_Unit() {
        return cstpl_Unit;
    }

    public void setCstpl_Unit(String cstpl_Unit) {
        this.cstpl_Unit = cstpl_Unit;
    }

    public BigDecimal getCstpl_UnitPrice() {
        return cstpl_UnitPrice;
    }

    public void setCstpl_UnitPrice(BigDecimal cstpl_UnitPrice) {
        this.cstpl_UnitPrice = cstpl_UnitPrice;
    }

    public BigDecimal getCstpl_Qty() {
        return cstpl_Qty;
    }

    public void setCstpl_Qty(BigDecimal cstpl_Qty) {
        this.cstpl_Qty = cstpl_Qty;
    }

    public BigDecimal getCstpl_Amt() {
        return cstpl_Amt;
    }

    public void setCstpl_Amt(BigDecimal cstpl_Amt) {
        this.cstpl_Amt = cstpl_Amt;
    }

    public String getCstpl_ArchivedFlag() {
        return cstpl_ArchivedFlag;
    }

    public void setCstpl_ArchivedFlag(String cstpl_ArchivedFlag) {
        this.cstpl_ArchivedFlag = cstpl_ArchivedFlag;
    }

    public String getCstpl_OriginFlag() {
        return cstpl_OriginFlag;
    }

    public void setCstpl_OriginFlag(String cstpl_OriginFlag) {
        this.cstpl_OriginFlag = cstpl_OriginFlag;
    }

    public String getCstpl_CreatedBy() {
        return cstpl_CreatedBy;
    }

    public void setCstpl_CreatedBy(String cstpl_CreatedBy) {
        this.cstpl_CreatedBy = cstpl_CreatedBy;
    }

    public String getCstpl_CreatedByName() {
        return cstpl_CreatedByName;
    }

    public void setCstpl_CreatedByName(String cstpl_CreatedByName) {
        this.cstpl_CreatedByName = cstpl_CreatedByName;
    }

    public String getCstpl_CreatedTime() {
        return cstpl_CreatedTime;
    }

    public void setCstpl_CreatedTime(String cstpl_CreatedTime) {
        this.cstpl_CreatedTime = cstpl_CreatedTime;
    }

    public String getCstpl_UpdatedBy() {
        return cstpl_UpdatedBy;
    }

    public void setCstpl_UpdatedBy(String cstpl_UpdatedBy) {
        this.cstpl_UpdatedBy = cstpl_UpdatedBy;
    }

    public String getCstpl_UpdatedByName() {
        return cstpl_UpdatedByName;
    }

    public void setCstpl_UpdatedByName(String cstpl_UpdatedByName) {
        this.cstpl_UpdatedByName = cstpl_UpdatedByName;
    }

    public String getCstpl_UpdatedTime() {
        return cstpl_UpdatedTime;
    }

    public void setCstpl_UpdatedTime(String cstpl_UpdatedTime) {
        this.cstpl_UpdatedTime = cstpl_UpdatedTime;
    }

    public Integer getCstpl_RecVersion() {
        return cstpl_RecVersion;
    }

    public void setCstpl_RecVersion(Integer cstpl_RecVersion) {
        this.cstpl_RecVersion = cstpl_RecVersion;
    }

    public String getCstpl_Remark() {
        return cstpl_Remark;
    }

    public void setCstpl_Remark(String cstpl_Remark) {
        this.cstpl_Remark = cstpl_Remark;
    }

    public String getCstpl_TkcttItemPkid() {
        return cstpl_TkcttItemPkid;
    }

    public void setCstpl_TkcttItemPkid(String cstpl_TkcttItemPkid) {
        this.cstpl_TkcttItemPkid = cstpl_TkcttItemPkid;
    }

    public String getCstpl_TkcttItemNo() {
        return cstpl_TkcttItemNo;
    }

    public void setCstpl_TkcttItemNo(String cstpl_TkcttItemNo) {
        this.cstpl_TkcttItemNo = cstpl_TkcttItemNo;
    }

    public String getCstpl_Tid() {
        return cstpl_Tid;
    }

    public void setCstpl_Tid(String cstpl_Tid) {
        this.cstpl_Tid = cstpl_Tid;
    }
}