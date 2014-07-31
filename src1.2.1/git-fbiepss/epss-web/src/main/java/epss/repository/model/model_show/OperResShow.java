package epss.repository.model.model_show;

public class OperResShow {
    private String pkid;
    private String tid;
    private String operPkid;
    private String operName;
    private String flowStatus;
    private String flowStatusName;
    private String infoType;
    private String infoPkid;
    private String infoPkidName;
    private String archivedFlag;
    private String createdBy;
    private String createdByName;
    private String createdTime;
    private String lastUpdBy;
    private String lastUpdByName;
    private String lastUpdTime;
    private String remark;
    private Integer recversion;
    public String getPkid() {
        return pkid;
    }
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid == null ? null : tid.trim();
    }
    public String getOperPkid() {
        return operPkid;
    }
    public void setOperPkid(String operPkid) {
        this.operPkid = operPkid == null ? null : operPkid.trim();
    }
    public String getFlowStatus() {
        return flowStatus;
    }
    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus == null ? null : flowStatus.trim();
    }
    public String getInfoType() {
        return infoType;
    }
    public void setInfoType(String infoType) {
        this.infoType = infoType == null ? null : infoType.trim();
    }
    public String getInfoPkid() {
        return infoPkid;
    }
    public void setInfoPkid(String infoPkid) {
        this.infoPkid = infoPkid == null ? null : infoPkid.trim();
    }
    public String getArchivedFlag() {
        return archivedFlag;
    }
    public void setArchivedFlag(String archivedFlag) {
        this.archivedFlag = archivedFlag == null ? null : archivedFlag.trim();
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }
    public String getLastUpdBy() {
        return lastUpdBy;
    }
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }
    public String getLastUpdTime() {
        return lastUpdTime;
    }
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public Integer getRecversion() {
        return recversion;
    }
    public void setRecversion(Integer recversion) {
        this.recversion = recversion;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getFlowStatusName() {
        return flowStatusName;
    }

    public void setFlowStatusName(String flowStatusName) {
        this.flowStatusName = flowStatusName;
    }

    public String getInfoPkidName() {
        return infoPkidName;
    }

    public void setInfoPkidName(String infoPkidName) {
        this.infoPkidName = infoPkidName;
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