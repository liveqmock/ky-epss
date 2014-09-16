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
public class ProgStlItemSubStlmentShow implements Serializable {
    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    private String subctt_StrNo;
    private String subctt_Pkid;
    private String subctt_BelongToType;
    private String subctt_BelongToPkid;
    private Integer subctt_Orderid;
    private Integer subctt_Grade;
    private String subctt_CorrespondingPkid;
    private String subctt_ItemPkid;
    private String subctt_ItemName;
    private String subctt_ParentPkid;
    private String subctt_Name;
    private String subctt_Remark;
    private String subctt_Unit;
    private BigDecimal subctt_ContractUnitPrice;
    private BigDecimal subctt_ContractQuantity;
    private BigDecimal subctt_ContractAmount;
    private BigDecimal subctt_SignPartAPrice;
    private String subctt_SpareField ;

    private String engPMng_Pkid;
    private String engPMng_SubStlType;
    private String engPMng_PeriodNo;
    private String engPMng_SubcttPkid;
    private String engPMng_ItemPkid;
    private BigDecimal engPMng_CurrentPeriodEQty;
    private BigDecimal engPMng_CurrentPeriodAmt;
    private BigDecimal engPMng_BeginToCurrentPeriodEQty;
    private BigDecimal engPMng_BeginToCurrentPeriodAmt;
    private String engPMng_Remark;
    private String engPMng_State;
    private String engPMng_ArchivedFlag;
    private String engPMng_OriginFlag;
    private String engPMng_CreatedBy;
    private String engPMng_CreatedByName;
    private String engPMng_CreatedTime;
    private String engPMng_LastUpdBy;
    private String engPMng_LastUpdByName;
    private String engPMng_LastUpdTime;
    private Integer engPMng_Recversion;
    private Integer engPMng_RowNo;

    public ProgStlItemSubStlmentShow() {

    }

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

    public String getSubctt_BelongToType() {
        return subctt_BelongToType;
    }

    public void setSubctt_BelongToType(String subctt_BelongToType) {
        this.subctt_BelongToType = subctt_BelongToType;
    }

    public String getSubctt_BelongToPkid() {
        return subctt_BelongToPkid;
    }

    public void setSubctt_BelongToPkid(String subctt_BelongToPkid) {
        this.subctt_BelongToPkid = subctt_BelongToPkid;
    }

    public Integer getSubctt_Orderid() {
        return subctt_Orderid;
    }

    public void setSubctt_Orderid(Integer subctt_Orderid) {
        this.subctt_Orderid = subctt_Orderid;
    }

    public Integer getSubctt_Grade() {
        return subctt_Grade;
    }

    public void setSubctt_Grade(Integer subctt_Grade) {
        this.subctt_Grade = subctt_Grade;
    }

    public String getSubctt_CorrespondingPkid() {
        return subctt_CorrespondingPkid;
    }

    public void setSubctt_CorrespondingPkid(String subctt_CorrespondingPkid) {
        this.subctt_CorrespondingPkid = subctt_CorrespondingPkid;
    }

    public String getSubctt_ItemPkid() {
        return subctt_ItemPkid;
    }

    public void setSubctt_ItemPkid(String subctt_ItemPkid) {
        this.subctt_ItemPkid = subctt_ItemPkid;
    }

    public String getSubctt_ParentPkid() {
        return subctt_ParentPkid;
    }

    public void setSubctt_ParentPkid(String subctt_ParentPkid) {
        this.subctt_ParentPkid = subctt_ParentPkid;
    }

    public String getSubctt_Name() {
        return subctt_Name;
    }

    public void setSubctt_Name(String subctt_Name) {
        this.subctt_Name = subctt_Name;
    }

    public String getSubctt_Remark() {
        return subctt_Remark;
    }

    public void setSubctt_Remark(String subctt_Remark) {
        this.subctt_Remark = subctt_Remark;
    }

    public String getSubctt_Unit() {
        return subctt_Unit;
    }

    public void setSubctt_Unit(String subctt_Unit) {
        this.subctt_Unit = subctt_Unit;
    }

    public BigDecimal getSubctt_ContractUnitPrice() {
        return subctt_ContractUnitPrice;
    }

    public void setSubctt_ContractUnitPrice(BigDecimal subctt_ContractUnitPrice) {
        this.subctt_ContractUnitPrice = subctt_ContractUnitPrice;
    }

    public BigDecimal getSubctt_ContractQuantity() {
        return subctt_ContractQuantity;
    }

    public void setSubctt_ContractQuantity(BigDecimal subctt_ContractQuantity) {
        this.subctt_ContractQuantity = subctt_ContractQuantity;
    }

    public BigDecimal getSubctt_ContractAmount() {
        return subctt_ContractAmount;
    }

    public void setSubctt_ContractAmount(BigDecimal subctt_ContractAmount) {
        this.subctt_ContractAmount = subctt_ContractAmount;
    }

    public BigDecimal getSubctt_SignPartAPrice() {
        return subctt_SignPartAPrice;
    }

    public void setSubctt_SignPartAPrice(BigDecimal subctt_SignPartAPrice) {
        this.subctt_SignPartAPrice = subctt_SignPartAPrice;
    }

    public String getSubctt_SpareField() {
        return subctt_SpareField;
    }

    public void setSubctt_SpareField(String subctt_SpareField) {
        this.subctt_SpareField = subctt_SpareField;
    }

    public String getEngPMng_Pkid() {
        return engPMng_Pkid;
    }

    public void setEngPMng_Pkid(String engPMng_Pkid) {
        this.engPMng_Pkid = engPMng_Pkid;
    }

    public String getEngPMng_PeriodNo() {
        return engPMng_PeriodNo;
    }

    public void setEngPMng_PeriodNo(String engPMng_PeriodNo) {
        this.engPMng_PeriodNo = engPMng_PeriodNo;
    }

    public String getEngPMng_SubcttPkid() {
        return engPMng_SubcttPkid;
    }

    public void setEngPMng_SubcttPkid(String engPMng_SubcttPkid) {
        this.engPMng_SubcttPkid = engPMng_SubcttPkid;
    }

    public String getEngPMng_ItemPkid() {
        return engPMng_ItemPkid;
    }

    public void setEngPMng_ItemPkid(String engPMng_ItemPkid) {
        this.engPMng_ItemPkid = engPMng_ItemPkid;
    }

    public BigDecimal getEngPMng_CurrentPeriodEQty() {
        return engPMng_CurrentPeriodEQty;
    }

    public void setEngPMng_CurrentPeriodEQty(BigDecimal engPMng_CurrentPeriodEQty) {
        this.engPMng_CurrentPeriodEQty = engPMng_CurrentPeriodEQty;
    }

    public BigDecimal getEngPMng_CurrentPeriodAmt() {
        return engPMng_CurrentPeriodAmt;
    }

    public void setEngPMng_CurrentPeriodAmt(BigDecimal engPMng_CurrentPeriodAmt) {
        this.engPMng_CurrentPeriodAmt = engPMng_CurrentPeriodAmt;
    }

    public BigDecimal getEngPMng_BeginToCurrentPeriodEQty() {
        return engPMng_BeginToCurrentPeriodEQty;
    }

    public void setEngPMng_BeginToCurrentPeriodEQty(BigDecimal engPMng_BeginToCurrentPeriodEQty) {
        this.engPMng_BeginToCurrentPeriodEQty = engPMng_BeginToCurrentPeriodEQty;
    }

    public BigDecimal getEngPMng_BeginToCurrentPeriodAmt() {
        return engPMng_BeginToCurrentPeriodAmt;
    }

    public void setEngPMng_BeginToCurrentPeriodAmt(BigDecimal engPMng_BeginToCurrentPeriodAmt) {
        this.engPMng_BeginToCurrentPeriodAmt = engPMng_BeginToCurrentPeriodAmt;
    }

    public String getEngPMng_Remark() {
        return engPMng_Remark;
    }

    public void setEngPMng_Remark(String engPMng_Remark) {
        this.engPMng_Remark = engPMng_Remark;
    }

    public String getEngPMng_State() {
        return engPMng_State;
    }

    public void setEngPMng_State(String engPMng_State) {
        this.engPMng_State = engPMng_State;
    }

    public String getEngPMng_ArchivedFlag() {
        return engPMng_ArchivedFlag;
    }

    public void setEngPMng_ArchivedFlag(String engPMng_ArchivedFlag) {
        this.engPMng_ArchivedFlag = engPMng_ArchivedFlag;
    }

    public String getEngPMng_OriginFlag() {
        return engPMng_OriginFlag;
    }

    public void setEngPMng_OriginFlag(String engPMng_OriginFlag) {
        this.engPMng_OriginFlag = engPMng_OriginFlag;
    }

    public String getEngPMng_CreatedBy() {
        return engPMng_CreatedBy;
    }

    public void setEngPMng_CreatedBy(String engPMng_CreatedBy) {
        this.engPMng_CreatedBy = engPMng_CreatedBy;
    }

    public String getEngPMng_LastUpdBy() {
        return engPMng_LastUpdBy;
    }

    public void setEngPMng_LastUpdBy(String engPMng_LastUpdBy) {
        this.engPMng_LastUpdBy = engPMng_LastUpdBy;
    }

    public String getEngPMng_CreatedTime() {
        return engPMng_CreatedTime;
    }

    public void setEngPMng_CreatedTime(String engPMng_CreatedTime) {
        this.engPMng_CreatedTime = engPMng_CreatedTime;
    }

    public String getEngPMng_LastUpdTime() {
        return engPMng_LastUpdTime;
    }

    public void setEngPMng_LastUpdTime(String engPMng_LastUpdTime) {
        this.engPMng_LastUpdTime = engPMng_LastUpdTime;
    }

    public Integer getEngPMng_Recversion() {
        return engPMng_Recversion;
    }

    public void setEngPMng_Recversion(Integer engPMng_Recversion) {
        this.engPMng_Recversion = engPMng_Recversion;
    }

    public String getEngPMng_CreatedByName() {
        return engPMng_CreatedByName;
    }

    public void setEngPMng_CreatedByName(String engPMng_CreatedByName) {
        this.engPMng_CreatedByName = engPMng_CreatedByName;
    }

    public String getEngPMng_LastUpdByName() {
        return engPMng_LastUpdByName;
    }

    public void setEngPMng_LastUpdByName(String engPMng_LastUpdByName) {
        this.engPMng_LastUpdByName = engPMng_LastUpdByName;
    }

    public String getSubctt_ItemName() {
        return subctt_ItemName;
    }

    public void setSubctt_ItemName(String subctt_ItemName) {
        this.subctt_ItemName = subctt_ItemName;
    }

    public String getEngPMng_SubStlType() {
        return engPMng_SubStlType;
    }

    public void setEngPMng_SubStlType(String engPMng_SubStlType) {
        this.engPMng_SubStlType = engPMng_SubStlType;
    }

    public Integer getEngPMng_RowNo() {
        return engPMng_RowNo;
    }

    public void setEngPMng_RowNo(Integer engPMng_RowNo) {
        this.engPMng_RowNo = engPMng_RowNo;
    }
}
