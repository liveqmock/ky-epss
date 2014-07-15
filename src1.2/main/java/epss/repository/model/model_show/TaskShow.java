package epss.repository.model.model_show;

public class TaskShow {
    private String type;
    private String pkid;
    private String id;
    private String name;
    private String periodNo;
    private String statusFlag;
    private String statusFlagName;
    private String preStatusFlag;
    private String preStatusFlagName;
    private Integer recordsCountInGroup;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getStatusFlagName() {
        return statusFlagName;
    }

    public void setStatusFlagName(String statusFlagName) {
        this.statusFlagName = statusFlagName;
    }

    public String getPreStatusFlag() {
        return preStatusFlag;
    }

    public void setPreStatusFlag(String preStatusFlag) {
        this.preStatusFlag = preStatusFlag;
    }

    public String getPreStatusFlagName() {
        return preStatusFlagName;
    }

    public void setPreStatusFlagName(String preStatusFlagName) {
        this.preStatusFlagName = preStatusFlagName;
    }

    public Integer getRecordsCountInGroup() {
        return recordsCountInGroup;
    }

    public void setRecordsCountInGroup(Integer recordsCountInGroup) {
        this.recordsCountInGroup = recordsCountInGroup;
    }
}