package epss.repository.model.model_show;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: 下午9:54
 * To change this template use File | Settings | File Templates.
 */

public class CstplItemShow implements Serializable {
    private String pkid;
    private String belongToType;
    private String belongToPkid;
    private String strNo;
    private Integer orderid;
    private Integer grade;
    private String name;
    private String itemPkid;
    private String parentPkid;
    private String remark;
    private String correspondingPkid;
    private String strCorrespondingItemNo;
    private String unit;
    private BigDecimal contractUnitPrice;
    private BigDecimal contractQuantity;
    private BigDecimal contractAmount;
    private BigDecimal signPartAPrice;
    private String archivedFlag;
    private String originFlag;
    private String createdBy;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdTime;
    private Integer recversion;

    // 对比项
    private String pkidContrast;
    private String belongToTypeContrast;
    private String belongToPkidContrast;
    private String strNoContrast;
    private Integer orderidContrast;
    private Integer gradeContrast;
    private String nameContrast;
    private String itemPkidContrast;
    private String parentPkidContrast;
    private String remarkContrast;
    private String correspondingPkidContrast;
    private String unitContrast;
    private BigDecimal contractUnitPriceContrast;
    private BigDecimal contractQuantityContrast;
    private BigDecimal contractAmountContrast;
    private BigDecimal signPartAPriceContrast;
    private String archivedFlagContrast;
    private String originFlagContrast;
    private String createdByContrast;
    private String createdTimeContrast;
    private String lastUpdByContrast;
    private String lastUpdTimeContrast;
    private Integer recversionContrast;

    /*编号，用作显示用，不在数据库里存储，动态显示。依据correspondingPkid*/
    private String correspondingItemNoContrast;
    private String spareField;
    public CstplItemShow() {

    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getBelongToType() {
        return belongToType;
    }

    public void setBelongToType(String belongToType) {
        this.belongToType = belongToType;
    }

    public String getBelongToPkid() {
        return belongToPkid;
    }

    public void setBelongToPkid(String belongToPkid) {
        this.belongToPkid = belongToPkid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemPkid() {
        return itemPkid;
    }

    public void setItemPkid(String itemPkid) {
        this.itemPkid = itemPkid;
    }

    public String getParentPkid() {
        return parentPkid;
    }

    public void setParentPkid(String parentPkid) {
        this.parentPkid = parentPkid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCorrespondingPkid() {
        return correspondingPkid;
    }

    public void setCorrespondingPkid(String correspondingPkid) {
        this.correspondingPkid = correspondingPkid;
    }

    public String getStrCorrespondingItemNo() {
        return strCorrespondingItemNo;
    }

    public void setStrCorrespondingItemNo(String strCorrespondingItemNo) {
        this.strCorrespondingItemNo = strCorrespondingItemNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getContractUnitPrice() {
        return contractUnitPrice;
    }

    public void setContractUnitPrice(BigDecimal contractUnitPrice) {
        this.contractUnitPrice = contractUnitPrice;
    }

    public BigDecimal getContractQuantity() {
        return contractQuantity;
    }

    public void setContractQuantity(BigDecimal contractQuantity) {
        this.contractQuantity = contractQuantity;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getSignPartAPrice() {
        return signPartAPrice;
    }

    public void setSignPartAPrice(BigDecimal signPartAPrice) {
        this.signPartAPrice = signPartAPrice;
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

    public String getLastUpdBy() {
        return lastUpdBy;
    }

    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy;
    }

    public String getPkidContrast() {
        return pkidContrast;
    }

    public void setPkidContrast(String pkidContrast) {
        this.pkidContrast = pkidContrast;
    }

    public String getBelongToTypeContrast() {
        return belongToTypeContrast;
    }

    public void setBelongToTypeContrast(String belongToTypeContrast) {
        this.belongToTypeContrast = belongToTypeContrast;
    }

    public String getBelongToPkidContrast() {
        return belongToPkidContrast;
    }

    public void setBelongToPkidContrast(String belongToPkidContrast) {
        this.belongToPkidContrast = belongToPkidContrast;
    }

    public Integer getOrderidContrast() {
        return orderidContrast;
    }

    public void setOrderidContrast(Integer orderidContrast) {
        this.orderidContrast = orderidContrast;
    }

    public Integer getGradeContrast() {
        return gradeContrast;
    }

    public void setGradeContrast(Integer gradeContrast) {
        this.gradeContrast = gradeContrast;
    }

    public String getNameContrast() {
        return nameContrast;
    }

    public void setNameContrast(String nameContrast) {
        this.nameContrast = nameContrast;
    }

    public String getItemPkidContrast() {
        return itemPkidContrast;
    }

    public void setItemPkidContrast(String itemPkidContrast) {
        this.itemPkidContrast = itemPkidContrast;
    }

    public String getParentPkidContrast() {
        return parentPkidContrast;
    }

    public void setParentPkidContrast(String parentPkidContrast) {
        this.parentPkidContrast = parentPkidContrast;
    }

    public String getRemarkContrast() {
        return remarkContrast;
    }

    public void setRemarkContrast(String remarkContrast) {
        this.remarkContrast = remarkContrast;
    }

    public String getCorrespondingPkidContrast() {
        return correspondingPkidContrast;
    }

    public void setCorrespondingPkidContrast(String correspondingPkidContrast) {
        this.correspondingPkidContrast = correspondingPkidContrast;
    }

    public String getUnitContrast() {
        return unitContrast;
    }

    public void setUnitContrast(String unitContrast) {
        this.unitContrast = unitContrast;
    }

    public BigDecimal getContractUnitPriceContrast() {
        return contractUnitPriceContrast;
    }

    public void setContractUnitPriceContrast(BigDecimal contractUnitPriceContrast) {
        this.contractUnitPriceContrast = contractUnitPriceContrast;
    }

    public BigDecimal getContractQuantityContrast() {
        return contractQuantityContrast;
    }

    public void setContractQuantityContrast(BigDecimal contractQuantityContrast) {
        this.contractQuantityContrast = contractQuantityContrast;
    }

    public BigDecimal getContractAmountContrast() {
        return contractAmountContrast;
    }

    public void setContractAmountContrast(BigDecimal contractAmountContrast) {
        this.contractAmountContrast = contractAmountContrast;
    }

    public BigDecimal getSignPartAPriceContrast() {
        return signPartAPriceContrast;
    }

    public void setSignPartAPriceContrast(BigDecimal signPartAPriceContrast) {
        this.signPartAPriceContrast = signPartAPriceContrast;
    }

    public String getOriginFlagContrast() {
        return originFlagContrast;
    }

    public void setOriginFlagContrast(String originFlagContrast) {
        this.originFlagContrast = originFlagContrast;
    }

    public String getCreatedByContrast() {
        return createdByContrast;
    }

    public void setCreatedByContrast(String createdByContrast) {
        this.createdByContrast = createdByContrast;
    }

    public String getLastUpdByContrast() {
        return lastUpdByContrast;
    }

    public void setLastUpdByContrast(String lastUpdByContrast) {
        this.lastUpdByContrast = lastUpdByContrast;
    }

    public String getLastUpdTimeContrast() {
        return lastUpdTimeContrast;
    }

    public void setLastUpdTimeContrast(String lastUpdTimeContrast) {
        this.lastUpdTimeContrast = lastUpdTimeContrast;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastUpdTime() {
        return lastUpdTime;
    }

    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime;
    }

    public String getCreatedTimeContrast() {
        return createdTimeContrast;
    }

    public void setCreatedTimeContrast(String createdTimeContrast) {
        this.createdTimeContrast = createdTimeContrast;
    }

    public String getArchivedFlag() {
        return archivedFlag;
    }

    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag;
    }

    public Integer getRecversion() {
        return recversion;
    }

    public void setRecversion(Integer recversion) {
        this.recversion = recversion;
    }

    public String getArchivedFlagContrast() {
        return archivedFlagContrast;
    }

    public void setArchivedFlagContrast(String archivedFlagContrast) {
        this.archivedFlagContrast = archivedFlagContrast;
    }

    public Integer getRecversionContrast() {
        return recversionContrast;
    }

    public void setRecversionContrast(Integer recversionContrast) {
        this.recversionContrast = recversionContrast;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStrNoContrast() {
        return strNoContrast;
    }

    public void setStrNoContrast(String strNoContrast) {
        this.strNoContrast = strNoContrast;
    }

    public String getCorrespondingItemNoContrast() {
        return correspondingItemNoContrast;
    }

    public void setCorrespondingItemNoContrast(String correspondingItemNoContrast) {
        this.correspondingItemNoContrast = correspondingItemNoContrast;
    }

    public String getSpareField() {
        return spareField;
    }

    public void setSpareField(String spareField) {
        this.spareField = spareField;
    }
}