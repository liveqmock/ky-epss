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
public class ProgStlItemSubQShow implements Serializable {

    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    private String subctt_StrNo;
    private String subctt_Pkid;
    private String subctt_BelongToType;
    private String subctt_BelongToPkid;
    private Integer subctt_Orderid;
    private Integer subctt_Grade;
    private String subctt_CorrespondingPkid;
    private String subctt_ParentPkid;
    private String subctt_Name;
    private String subctt_Remark;
    private String subctt_Unit;
    private BigDecimal subctt_ContractUnitPrice;
    private BigDecimal subctt_ContractQuantity;
    private BigDecimal subctt_ContractAmount;
    private BigDecimal subctt_SignPartAPrice;
    private String subctt_SpareField ;

    private String engQMng_Pkid;
    private String engQMng_PeriodNo;
    private String engQMng_SubcttPkid;
    private String engQMng_SubcttItemPkid;
    private BigDecimal engQMng_BeginToCurrentPeriodEQty;
    private BigDecimal engQMng_CurrentPeriodEQty;
    private String engQMng_State;
    private String engQMng_ArchivedFlag;
    private String engQMng_OriginFlag;
    private String engQMng_CreatedByName;
    private String engQMng_CreatedBy;
    private String engQMng_CreatedTime;
    private String engQMng_LastUpdBy;
    private String engQMng_LastUpdByName;
    private String engQMng_LastUpdTime;
    private Integer engQMng_RecVersion;
    private Boolean isUptoCttContentFlag;

    public ProgStlItemSubQShow() {

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

    public String getEngQMng_Pkid() {
        return engQMng_Pkid;
    }

    public void setEngQMng_Pkid(String engQMng_Pkid) {
        this.engQMng_Pkid = engQMng_Pkid;
    }

    public String getEngQMng_PeriodNo() {
        return engQMng_PeriodNo;
    }

    public void setEngQMng_PeriodNo(String engQMng_PeriodNo) {
        this.engQMng_PeriodNo = engQMng_PeriodNo;
    }

    public String getEngQMng_SubcttPkid() {
        return engQMng_SubcttPkid;
    }

    public void setEngQMng_SubcttPkid(String engQMng_SubcttPkid) {
        this.engQMng_SubcttPkid = engQMng_SubcttPkid;
    }

    public String getEngQMng_SubcttItemPkid() {
        return engQMng_SubcttItemPkid;
    }

    public void setEngQMng_SubcttItemPkid(String engQMng_SubcttItemPkid) {
        this.engQMng_SubcttItemPkid = engQMng_SubcttItemPkid;
    }

    public BigDecimal getEngQMng_BeginToCurrentPeriodEQty() {
        return engQMng_BeginToCurrentPeriodEQty;
    }

    public void setEngQMng_BeginToCurrentPeriodEQty(BigDecimal engQMng_BeginToCurrentPeriodEQty) {
        this.engQMng_BeginToCurrentPeriodEQty = engQMng_BeginToCurrentPeriodEQty;
    }

    public BigDecimal getEngQMng_CurrentPeriodEQty() {
        return engQMng_CurrentPeriodEQty;
    }

    public void setEngQMng_CurrentPeriodEQty(BigDecimal engQMng_CurrentPeriodEQty) {
        this.engQMng_CurrentPeriodEQty = engQMng_CurrentPeriodEQty;
    }

    public String getEngQMng_State() {
        return engQMng_State;
    }

    public void setEngQMng_State(String engQMng_State) {
        this.engQMng_State = engQMng_State;
    }

    public String getEngQMng_ArchivedFlag() {
        return engQMng_ArchivedFlag;
    }

    public void setEngQMng_ArchivedFlag(String engQMng_ArchivedFlag) {
        this.engQMng_ArchivedFlag = engQMng_ArchivedFlag;
    }

    public String getEngQMng_OriginFlag() {
        return engQMng_OriginFlag;
    }

    public void setEngQMng_OriginFlag(String engQMng_OriginFlag) {
        this.engQMng_OriginFlag = engQMng_OriginFlag;
    }

    public String getEngQMng_CreatedBy() {
        return engQMng_CreatedBy;
    }

    public void setEngQMng_CreatedBy(String engQMng_CreatedBy) {
        this.engQMng_CreatedBy = engQMng_CreatedBy;
    }

    public String getEngQMng_LastUpdBy() {
        return engQMng_LastUpdBy;
    }

    public void setEngQMng_LastUpdBy(String engQMng_LastUpdBy) {
        this.engQMng_LastUpdBy = engQMng_LastUpdBy;
    }

    public String getEngQMng_LastUpdByName() {
        return engQMng_LastUpdByName;
    }

    public void setEngQMng_LastUpdByName(String engQMng_LastUpdByName) {
        this.engQMng_LastUpdByName = engQMng_LastUpdByName;
    }

    public String getEngQMng_CreatedByName() {
        return engQMng_CreatedByName;
    }

    public void setEngQMng_CreatedByName(String engQMng_CreatedByName) {
        this.engQMng_CreatedByName = engQMng_CreatedByName;
    }

    public String getEngQMng_CreatedTime() {
        return engQMng_CreatedTime;
    }

    public void setEngQMng_CreatedTime(String engQMng_CreatedTime) {
        this.engQMng_CreatedTime = engQMng_CreatedTime;
    }

    public String getEngQMng_LastUpdTime() {
        return engQMng_LastUpdTime;
    }

    public void setEngQMng_LastUpdTime(String engQMng_LastUpdTime) {
        this.engQMng_LastUpdTime = engQMng_LastUpdTime;
    }

    public Integer getEngQMng_RecVersion() {
        return engQMng_RecVersion;
    }

    public void setEngQMng_RecVersion(Integer engQMng_RecVersion) {
        this.engQMng_RecVersion = engQMng_RecVersion;
    }

    public Boolean getIsUptoCttContentFlag() {
        return isUptoCttContentFlag;
    }

    public void setIsUptoCttContentFlag(Boolean isUptoCttContentFlag) {
        this.isUptoCttContentFlag = isUptoCttContentFlag;
    }
}
