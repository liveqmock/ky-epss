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
public class ProgStlItemSubFShow implements Serializable {

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

    private String engSMng_Pkid;
    private String engSMng_PeriodNo;
    private String engSMng_SubcttPkid;
    private String engSMng_SubcttItemPkid;
    private BigDecimal engSMng_AddUpToAmt;
    private BigDecimal engSMng_ThisStageAmt;
    private String engSMng_State;
    private String engSMng_ArchivedFlag;
    private String engSMng_OriginFlag;
    private String engSMng_CreatedBy;
    private String engSMng_CreatedByName;
    private String engSMng_CreatedTime;
    private String engSMng_LastUpdBy;
    private String engSMng_LastUpdByName;
    private String engSMng_LastUpdTime;
    private Integer engSMng_RecVersion;
    private String engSMng_Remark;
    private Boolean isUptoCttAmtFlag;
    private Boolean isUptoCttQtyFlag;
    private Boolean isRenderedFlag;

//    private String engSMng_BeginToCurrentPeriodEQty;
//    private String engSMng_CurrentPeriodEQty;

    public ProgStlItemSubFShow() {

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
        this.subctt_SpareField = subctt_SpareField==null?subctt_SpareField:"";
    }

    public String getEngSMng_Pkid() {
        return engSMng_Pkid;
    }

    public void setEngSMng_Pkid(String engSMng_Pkid) {
        this.engSMng_Pkid = engSMng_Pkid;
    }

    public String getEngSMng_PeriodNo() {
        return engSMng_PeriodNo;
    }

    public void setEngSMng_PeriodNo(String engSMng_PeriodNo) {
        this.engSMng_PeriodNo = engSMng_PeriodNo;
    }

    public String getEngSMng_SubcttPkid() {
        return engSMng_SubcttPkid;
    }

    public void setEngSMng_SubcttPkid(String engSMng_SubcttPkid) {
        this.engSMng_SubcttPkid = engSMng_SubcttPkid;
    }

    public String getEngSMng_SubcttItemPkid() {
        return engSMng_SubcttItemPkid;
    }

    public void setEngSMng_SubcttItemPkid(String engSMng_SubcttItemPkid) {
        this.engSMng_SubcttItemPkid = engSMng_SubcttItemPkid;
    }

    public String getEngSMng_State() {
        return engSMng_State;
    }

    public void setEngSMng_State(String engSMng_State) {
        this.engSMng_State = engSMng_State;
    }

    public String getEngSMng_ArchivedFlag() {
        return engSMng_ArchivedFlag;
    }

    public void setEngSMng_ArchivedFlag(String engSMng_ArchivedFlag) {
        this.engSMng_ArchivedFlag = engSMng_ArchivedFlag;
    }

    public String getEngSMng_OriginFlag() {
        return engSMng_OriginFlag;
    }

    public void setEngSMng_OriginFlag(String engSMng_OriginFlag) {
        this.engSMng_OriginFlag = engSMng_OriginFlag;
    }

    public String getEngSMng_CreatedBy() {
        return engSMng_CreatedBy;
    }

    public void setEngSMng_CreatedBy(String engSMng_CreatedBy) {
        this.engSMng_CreatedBy = engSMng_CreatedBy;
    }

    public String getEngSMng_LastUpdBy() {
        return engSMng_LastUpdBy;
    }

    public void setEngSMng_LastUpdBy(String engSMng_LastUpdBy) {
        this.engSMng_LastUpdBy = engSMng_LastUpdBy;
    }

    public String getEngSMng_CreatedTime() {
        return engSMng_CreatedTime;
    }

    public void setEngSMng_CreatedTime(String engSMng_CreatedTime) {
        this.engSMng_CreatedTime = engSMng_CreatedTime;
    }

    public String getEngSMng_LastUpdTime() {
        return engSMng_LastUpdTime;
    }

    public void setEngSMng_LastUpdTime(String engSMng_LastUpdTime) {
        this.engSMng_LastUpdTime = engSMng_LastUpdTime;
    }

    public Integer getEngSMng_RecVersion() {
        return engSMng_RecVersion;
    }

    public void setEngSMng_RecVersion(Integer engSMng_RecVersion) {
        this.engSMng_RecVersion = engSMng_RecVersion;
    }

    public String getEngSMng_LastUpdByName() {
        return engSMng_LastUpdByName;
    }

    public void setEngSMng_LastUpdByName(String engSMng_LastUpdByName) {
        this.engSMng_LastUpdByName = engSMng_LastUpdByName;
    }

    public String getEngSMng_CreatedByName() {
        return engSMng_CreatedByName;
    }

    public void setEngSMng_CreatedByName(String engSMng_CreatedByName) {
        this.engSMng_CreatedByName = engSMng_CreatedByName;
    }

    public BigDecimal getEngSMng_AddUpToAmt() {
        return engSMng_AddUpToAmt;
    }

    public void setEngSMng_AddUpToAmt(BigDecimal engSMng_AddUpToAmt) {
        this.engSMng_AddUpToAmt = engSMng_AddUpToAmt;
    }

    public BigDecimal getEngSMng_ThisStageAmt() {
        return engSMng_ThisStageAmt;
    }

    public void setEngSMng_ThisStageAmt(BigDecimal engSMng_ThisStageAmt) {
        this.engSMng_ThisStageAmt = engSMng_ThisStageAmt;
    }

    public Boolean getIsUptoCttAmtFlag() {
        return isUptoCttAmtFlag;
    }

    public void setIsUptoCttAmtFlag(Boolean isUptoCttAmtFlag) {
        this.isUptoCttAmtFlag = isUptoCttAmtFlag;
    }

    public Boolean getIsRenderedFlag() {
        return isRenderedFlag;
    }

    public void setIsRenderedFlag(Boolean isRenderedFlag) {
        this.isRenderedFlag = isRenderedFlag;
    }

    public Boolean getIsUptoCttQtyFlag() {
        return isUptoCttQtyFlag;
    }

    public void setIsUptoCttQtyFlag(Boolean isUptoCttQtyFlag) {
        this.isUptoCttQtyFlag = isUptoCttQtyFlag;
    }

    public String getEngSMng_Remark() {
        return engSMng_Remark;
    }

    public void setEngSMng_Remark(String engSMng_Remark) {
        this.engSMng_Remark = engSMng_Remark;
    }
}
