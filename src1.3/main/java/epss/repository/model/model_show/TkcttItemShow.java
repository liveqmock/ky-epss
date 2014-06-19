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
public class TkcttItemShow implements Serializable {
    private String pkid;
    private String tkcttInfoPkid;
    private String parentPkid;
    private String strNo;
    private Integer grade;
    private Integer orderid;
    private String name;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal qty;
    private BigDecimal amt;
    private String archivedFlag;
    private String originFlag;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String updatedBy;
    private String updatedByName;
    private String updatedTime;
    private Integer recVersion;
    private String remark;
    private String tid;

    public TkcttItemShow(){

    }

    public TkcttItemShow(String tkcttInfoPkidPara){
        tkcttInfoPkid=tkcttInfoPkidPara;
    }

    public TkcttItemShow(String pkidPara,
            String tkcttInfoPkidPara,
            String parentPkidPara,
            String strNoPara,
            Integer gradePara,
            Integer orderidPara,
            String namePara,
            String unitPara,
            BigDecimal unitPricePara,
            BigDecimal qtyPara,
            BigDecimal amtPara,
            String archivedFlagPara,
            String originFlagPara,
            String createdByPara,
            String createdByNamePara,
            String createdTimePara,
            String updatedByPara,
            String updatedByNamePara,
            String updatedTimePara,
            Integer recVersionPara,
            String remarkPara,
            String tidPara) {
        pkid=pkidPara;
        tkcttInfoPkid=tkcttInfoPkidPara;
        parentPkid=parentPkidPara;
        strNo=strNoPara;
        grade=gradePara;
        orderid=orderidPara;
        name=namePara;
        unit=unitPara;
        unitPrice=unitPricePara;
        qty=qtyPara;
        amt=amtPara;
        archivedFlag=archivedFlagPara;
        originFlag=originFlagPara;
        createdBy=createdByPara;
        createdByName=createdByNamePara;
        createdTime=createdTimePara;
        updatedBy=updatedByPara;
        updatedByName=updatedByNamePara;
        updatedTime=updatedTimePara;
        recVersion=recVersionPara;
        remark=remarkPara;
        tid=tidPara;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getTkcttInfoPkid() {
        return tkcttInfoPkid;
    }

    public void setTkcttInfoPkid(String tkcttInfoPkid) {
        this.tkcttInfoPkid = tkcttInfoPkid;
    }

    public String getParentPkid() {
        return parentPkid;
    }

    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getArchivedFlag() {
        return archivedFlag;
    }

    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag;
    }

    public String getOriginFlag() {
        return originFlag;
    }

    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}