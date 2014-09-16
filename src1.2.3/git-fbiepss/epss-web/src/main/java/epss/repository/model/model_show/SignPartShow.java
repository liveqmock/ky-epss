package epss.repository.model.model_show;

public class SignPartShow {
    private String pkid;
    private String id;
    private String name;
    private String mobilephone;
    private String email;
    private String operphone;
    private String otherphone;
    private String fax;
    private String archivedFlag;
    private String originFlag;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdTime;
    private Integer recversion;
    private String remark;
    public String getPkid() {
        return pkid;
    }
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public String getMobilephone() {
        return mobilephone;
    }
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
    public String getOperphone() {
        return operphone;
    }
    public void setOperphone(String operphone) {
        this.operphone = operphone == null ? null : operphone.trim();
    }
    public String getOtherphone() {
        return otherphone;
    }
    public void setOtherphone(String otherphone) {
        this.otherphone = otherphone == null ? null : otherphone.trim();
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }
    public String getArchivedFlag() {
        return archivedFlag;
    }
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }
    public String getOriginFlag() {
        return originFlag;
    }
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }
    public String getLastUpdBy() {
        return lastUpdBy;
    }
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
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
    public Integer getRecversion() {
        return recversion;
    }
    public void setRecversion(Integer recversion) {
        this.recversion = recversion;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreatedByName() {
        return createdByName;
    }
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
    public String getLastUpdByName() {
        return lastUpdByName;
    }
    public void setLastUpdByName(String lastUpdByName) {
        this.lastUpdByName = lastUpdByName;
    }
}