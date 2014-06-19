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
    private String deletedFlag;
    private String originFlag;
    private String createdBy;
    private String createdByName;
    private String createdDate;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdDate;
    private Integer modificationNum;
    private String note;
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
    public String getDeletedFlag() {
        return deletedFlag;
    }
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
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
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate == null ? null : createdDate.trim();
    }
    public String getLastUpdBy() {
        return lastUpdBy;
    }
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }
    public String getLastUpdDate() {
        return lastUpdDate;
    }
    public void setLastUpdDate(String lastUpdDate) {
        this.lastUpdDate = lastUpdDate == null ? null : lastUpdDate.trim();
    }
    public Integer getModificationNum() {
        return modificationNum;
    }
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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