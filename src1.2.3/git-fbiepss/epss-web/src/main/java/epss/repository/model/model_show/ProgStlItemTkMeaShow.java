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
public class ProgStlItemTkMeaShow implements Serializable {

    /*编号，用作显示用，不在数据库里存储，动态显示.依据grade,orderid*/
    private String tkctt_StrNo;
    private String tkctt_Pkid;
    private String tkctt_BelongToType;
    private String tkctt_BelongToPkid;
    private Integer tkctt_Orderid;
    private Integer tkctt_Grade;
    private String tkctt_CorrespondingPkid;
    private String tkctt_ParentPkid;
    private String tkctt_Name;
    private String tkctt_Remark;
    private String tkctt_Unit;
    private BigDecimal tkctt_ContractUnitPrice;
    private BigDecimal tkctt_ContractQuantity;
    private BigDecimal tkctt_ContractAmount;

    private String eng_Pkid;
    private String eng_PeriodNo;
    private String eng_TkcttPkid;
    private String eng_TkcttItemPkid;
    private BigDecimal eng_BeginToCurrentPeriodEQty;
    private BigDecimal eng_BeginToCurrentPeriodEAmount;
    private BigDecimal eng_CurrentPeriodEQty;
    private BigDecimal eng_CurrentPeriodEAmount;
    private String eng_State;
    private String eng_ArchivedFlag;
    private String eng_CreatedBy;
    private String eng_CreatedByName;
    private String eng_OriginFlag;
    private String eng_CreatedTime;
    private String eng_LastUpdBy;
    private String eng_LastUpdByName;
    private String eng_LastUpdTime;
    private Integer eng_RecVersion;
    private Boolean isUptoCttQtyFlag;
    private Boolean isRenderedFlag;

    public ProgStlItemTkMeaShow() {

    }

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

    public String getTkctt_BelongToType() {
        return tkctt_BelongToType;
    }

    public void setTkctt_BelongToType(String tkctt_BelongToType) {
        this.tkctt_BelongToType = tkctt_BelongToType;
    }

    public String getTkctt_BelongToPkid() {
        return tkctt_BelongToPkid;
    }

    public void setTkctt_BelongToPkid(String tkctt_BelongToPkid) {
        this.tkctt_BelongToPkid = tkctt_BelongToPkid;
    }

    public Integer getTkctt_Orderid() {
        return tkctt_Orderid;
    }

    public void setTkctt_Orderid(Integer tkctt_Orderid) {
        this.tkctt_Orderid = tkctt_Orderid;
    }

    public Integer getTkctt_Grade() {
        return tkctt_Grade;
    }

    public void setTkctt_Grade(Integer tkctt_Grade) {
        this.tkctt_Grade = tkctt_Grade;
    }

    public String getTkctt_CorrespondingPkid() {
        return tkctt_CorrespondingPkid;
    }

    public void setTkctt_CorrespondingPkid(String tkctt_CorrespondingPkid) {
        this.tkctt_CorrespondingPkid = tkctt_CorrespondingPkid;
    }

    public String getTkctt_ParentPkid() {
        return tkctt_ParentPkid;
    }

    public void setTkctt_ParentPkid(String tkctt_ParentPkid) {
        this.tkctt_ParentPkid = tkctt_ParentPkid;
    }

    public String getTkctt_Name() {
        return tkctt_Name;
    }

    public void setTkctt_Name(String tkctt_Name) {
        this.tkctt_Name = tkctt_Name;
    }

    public String getTkctt_Remark() {
        return tkctt_Remark;
    }

    public void setTkctt_Remark(String tkctt_Remark) {
        this.tkctt_Remark = tkctt_Remark;
    }

    public String getTkctt_Unit() {
        return tkctt_Unit;
    }

    public void setTkctt_Unit(String tkctt_Unit) {
        this.tkctt_Unit = tkctt_Unit;
    }

    public BigDecimal getTkctt_ContractUnitPrice() {
        return tkctt_ContractUnitPrice;
    }

    public void setTkctt_ContractUnitPrice(BigDecimal tkctt_ContractUnitPrice) {
        this.tkctt_ContractUnitPrice = tkctt_ContractUnitPrice;
    }

    public BigDecimal getTkctt_ContractQuantity() {
        return tkctt_ContractQuantity;
    }

    public void setTkctt_ContractQuantity(BigDecimal tkctt_ContractQuantity) {
        this.tkctt_ContractQuantity = tkctt_ContractQuantity;
    }

    public BigDecimal getTkctt_ContractAmount() {
        return tkctt_ContractAmount;
    }

    public void setTkctt_ContractAmount(BigDecimal tkctt_ContractAmount) {
        this.tkctt_ContractAmount = tkctt_ContractAmount;
    }

    public String getEng_Pkid() {
        return eng_Pkid;
    }

    public void setEng_Pkid(String eng_Pkid) {
        this.eng_Pkid = eng_Pkid;
    }

    public String getEng_PeriodNo() {
        return eng_PeriodNo;
    }

    public void setEng_PeriodNo(String eng_PeriodNo) {
        this.eng_PeriodNo = eng_PeriodNo;
    }

    public String getEng_TkcttPkid() {
        return eng_TkcttPkid;
    }

    public void setEng_TkcttPkid(String eng_TkcttPkid) {
        this.eng_TkcttPkid = eng_TkcttPkid;
    }

    public String getEng_TkcttItemPkid() {
        return eng_TkcttItemPkid;
    }

    public void setEng_TkcttItemPkid(String eng_TkcttItemPkid) {
        this.eng_TkcttItemPkid = eng_TkcttItemPkid;
    }

    public BigDecimal getEng_BeginToCurrentPeriodEQty() {
        return eng_BeginToCurrentPeriodEQty;
    }

    public void setEng_BeginToCurrentPeriodEQty(BigDecimal eng_BeginToCurrentPeriodEQty) {
        this.eng_BeginToCurrentPeriodEQty = eng_BeginToCurrentPeriodEQty;
    }

    public BigDecimal getEng_CurrentPeriodEQty() {
        return eng_CurrentPeriodEQty;
    }

    public void setEng_CurrentPeriodEQty(BigDecimal eng_CurrentPeriodEQty) {
        this.eng_CurrentPeriodEQty = eng_CurrentPeriodEQty;
    }

    public String getEng_State() {
        return eng_State;
    }

    public void setEng_State(String eng_State) {
        this.eng_State = eng_State;
    }

    public String getEng_ArchivedFlag() {
        return eng_ArchivedFlag;
    }

    public void setEng_ArchivedFlag(String eng_ArchivedFlag) {
        this.eng_ArchivedFlag = eng_ArchivedFlag;
    }

    public String getEng_OriginFlag() {
        return eng_OriginFlag;
    }

    public void setEng_OriginFlag(String eng_OriginFlag) {
        this.eng_OriginFlag = eng_OriginFlag;
    }

    public String getEng_CreatedBy() {
        return eng_CreatedBy;
    }

    public void setEng_CreatedBy(String eng_CreatedBy) {
        this.eng_CreatedBy = eng_CreatedBy;
    }

    public String getEng_LastUpdBy() {
        return eng_LastUpdBy;
    }

    public void setEng_LastUpdBy(String eng_LastUpdBy) {
        this.eng_LastUpdBy = eng_LastUpdBy;
    }

    public String getEng_CreatedTime() {
        return eng_CreatedTime;
    }

    public void setEng_CreatedTime(String eng_CreatedTime) {
        this.eng_CreatedTime = eng_CreatedTime;
    }

    public String getEng_LastUpdTime() {
        return eng_LastUpdTime;
    }

    public void setEng_LastUpdTime(String eng_LastUpdTime) {
        this.eng_LastUpdTime = eng_LastUpdTime;
    }

    public Integer getEng_RecVersion() {
        return eng_RecVersion;
    }

    public void setEng_RecVersion(Integer eng_RecVersion) {
        this.eng_RecVersion = eng_RecVersion;
    }

    public BigDecimal getEng_BeginToCurrentPeriodEAmount() {
        return eng_BeginToCurrentPeriodEAmount;
    }

    public void setEng_BeginToCurrentPeriodEAmount(BigDecimal eng_BeginToCurrentPeriodEAmount) {
        this.eng_BeginToCurrentPeriodEAmount = eng_BeginToCurrentPeriodEAmount;
    }

    public BigDecimal getEng_CurrentPeriodEAmount() {
        return eng_CurrentPeriodEAmount;
    }

    public void setEng_CurrentPeriodEAmount(BigDecimal eng_CurrentPeriodEAmount) {
        this.eng_CurrentPeriodEAmount = eng_CurrentPeriodEAmount;
    }

    public String getEng_CreatedByName() {
        return eng_CreatedByName;
    }

    public void setEng_CreatedByName(String eng_CreatedByName) {
        this.eng_CreatedByName = eng_CreatedByName;
    }

    public String getEng_LastUpdByName() {
        return eng_LastUpdByName;
    }

    public void setEng_LastUpdByName(String eng_LastUpdByName) {
        this.eng_LastUpdByName = eng_LastUpdByName;
    }

    public Boolean getIsUptoCttQtyFlag() {
        return isUptoCttQtyFlag;
    }

    public void setIsUptoCttQtyFlag(Boolean isUptoCttQtyFlag) {
        this.isUptoCttQtyFlag = isUptoCttQtyFlag;
    }

    public Boolean getIsRenderedFlag() {
        return isRenderedFlag;
    }

    public void setIsRenderedFlag(Boolean isRenderedFlag) {
        this.isRenderedFlag = isRenderedFlag;
    }
}
