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
public class ProgStlItemSubMShow implements Serializable {

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
    private String subctt_Note;
    private String subctt_Unit;
    private BigDecimal subctt_ContractUnitPrice;
    private BigDecimal subctt_ContractQuantity;
    private BigDecimal subctt_ContractAmount;
    private BigDecimal subctt_SignPartAPrice;
    private String subctt_SpareField ;

    private String engMMng_Pkid;
    private String engMMng_PeriodNo;
    private String engMMng_SubcttPkid;
    private String engMMng_SubcttItemPkid;
    private BigDecimal engMMng_BeginToCurrentPeriodMQty;
    private BigDecimal engMMng_CurrentPeriodMQty;
    private BigDecimal engMMng_MPurchaseUnitPrice;
    private String engMMng_State;
    private String engMMng_DeletedFlag;
    private String engMMng_OriginFlag;
    private String engMMng_CreatedBy;
    private String engMMng_CreatedByName;
    private String engMMng_CreatedTime;
    private String engMMng_LastUpdBy;
    private String engMMng_LastUpdByName;
    private String engMMng_LastUpdTime;
    private Integer engMMng_ModificationNum;

    public ProgStlItemSubMShow() {

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

    public String getSubctt_Note() {
        return subctt_Note;
    }

    public void setSubctt_Note(String subctt_Note) {
        this.subctt_Note = subctt_Note;
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

    public String getEngMMng_Pkid() {
        return engMMng_Pkid;
    }

    public void setEngMMng_Pkid(String engMMng_Pkid) {
        this.engMMng_Pkid = engMMng_Pkid;
    }

    public String getEngMMng_PeriodNo() {
        return engMMng_PeriodNo;
    }

    public void setEngMMng_PeriodNo(String engMMng_PeriodNo) {
        this.engMMng_PeriodNo = engMMng_PeriodNo;
    }

    public String getEngMMng_SubcttPkid() {
        return engMMng_SubcttPkid;
    }

    public void setEngMMng_SubcttPkid(String engMMng_SubcttPkid) {
        this.engMMng_SubcttPkid = engMMng_SubcttPkid;
    }

    public String getEngMMng_SubcttItemPkid() {
        return engMMng_SubcttItemPkid;
    }

    public void setEngMMng_SubcttItemPkid(String engMMng_SubcttItemPkid) {
        this.engMMng_SubcttItemPkid = engMMng_SubcttItemPkid;
    }

    public BigDecimal getEngMMng_BeginToCurrentPeriodMQty() {
        return engMMng_BeginToCurrentPeriodMQty;
    }

    public void setEngMMng_BeginToCurrentPeriodMQty(BigDecimal engMMng_BeginToCurrentPeriodMQty) {
        this.engMMng_BeginToCurrentPeriodMQty = engMMng_BeginToCurrentPeriodMQty;
    }

    public BigDecimal getEngMMng_CurrentPeriodMQty() {
        return engMMng_CurrentPeriodMQty;
    }

    public void setEngMMng_CurrentPeriodMQty(BigDecimal engMMng_CurrentPeriodMQty) {
        this.engMMng_CurrentPeriodMQty = engMMng_CurrentPeriodMQty;
    }

    public BigDecimal getEngMMng_MPurchaseUnitPrice() {
        return engMMng_MPurchaseUnitPrice;
    }

    public void setEngMMng_MPurchaseUnitPrice(BigDecimal engMMng_MPurchaseUnitPrice) {
        this.engMMng_MPurchaseUnitPrice = engMMng_MPurchaseUnitPrice;
    }

    public String getEngMMng_State() {
        return engMMng_State;
    }

    public void setEngMMng_State(String engMMng_State) {
        this.engMMng_State = engMMng_State;
    }

    public String getEngMMng_DeletedFlag() {
        return engMMng_DeletedFlag;
    }

    public void setEngMMng_DeletedFlag(String engMMng_DeletedFlag) {
        this.engMMng_DeletedFlag = engMMng_DeletedFlag;
    }

    public String getEngMMng_OriginFlag() {
        return engMMng_OriginFlag;
    }

    public void setEngMMng_OriginFlag(String engMMng_OriginFlag) {
        this.engMMng_OriginFlag = engMMng_OriginFlag;
    }

    public String getEngMMng_CreatedBy() {
        return engMMng_CreatedBy;
    }

    public void setEngMMng_CreatedBy(String engMMng_CreatedBy) {
        this.engMMng_CreatedBy = engMMng_CreatedBy;
    }

    public String getEngMMng_LastUpdBy() {
        return engMMng_LastUpdBy;
    }

    public void setEngMMng_LastUpdBy(String engMMng_LastUpdBy) {
        this.engMMng_LastUpdBy = engMMng_LastUpdBy;
    }

    public String getEngMMng_CreatedTime() {
        return engMMng_CreatedTime;
    }

    public void setEngMMng_CreatedTime(String engMMng_CreatedTime) {
        this.engMMng_CreatedTime = engMMng_CreatedTime;
    }

    public String getEngMMng_LastUpdTime() {
        return engMMng_LastUpdTime;
    }

    public void setEngMMng_LastUpdTime(String engMMng_LastUpdTime) {
        this.engMMng_LastUpdTime = engMMng_LastUpdTime;
    }

    public Integer getEngMMng_ModificationNum() {
        return engMMng_ModificationNum;
    }

    public void setEngMMng_ModificationNum(Integer engMMng_ModificationNum) {
        this.engMMng_ModificationNum = engMMng_ModificationNum;
    }

    public String getEngMMng_LastUpdByName() {
        return engMMng_LastUpdByName;
    }

    public void setEngMMng_LastUpdByName(String engMMng_LastUpdByName) {
        this.engMMng_LastUpdByName = engMMng_LastUpdByName;
    }

    public String getEngMMng_CreatedByName() {
        return engMMng_CreatedByName;
    }

    public void setEngMMng_CreatedByName(String engMMng_CreatedByName) {
        this.engMMng_CreatedByName = engMMng_CreatedByName;
    }
}
