package epss.repository.model.model_show;

public class TaskShow {
    private String type;
    private String pkid;
    private String id;
    private String name;
    private String periodNo;
    private String flowStatus;
    private String flowStatusName;
    private String flowStatusReason;
    private String flowStatusReasonName;
    private String strColorType;
    private String operResFlowStatus;
    private String operResFlowStatusName;
    private String isOwnTaskFlowFlag;

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

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getFlowStatusName() {
        return flowStatusName;
    }

    public void setFlowStatusName(String flowStatusName) {
        this.flowStatusName = flowStatusName;
    }

    public String getFlowStatusReasonName() {
        return flowStatusReasonName;
    }

    public void setFlowStatusReasonName(String flowStatusReasonName) {
        this.flowStatusReasonName = flowStatusReasonName;
    }

    public String getFlowStatusReason() {
        return flowStatusReason;
    }

    public void setFlowStatusReason(String flowStatusReason) {
        this.flowStatusReason = flowStatusReason;
    }

    public String getStrColorType() {
        return strColorType;
    }

    public void setStrColorType(String strColorType) {
        this.strColorType = strColorType;
    }

    public String getOperResFlowStatus() {
        return operResFlowStatus;
    }

    public void setOperResFlowStatus(String operResFlowStatus) {
        this.operResFlowStatus = operResFlowStatus;
    }

    public String getOperResFlowStatusName() {
        return operResFlowStatusName;
    }

    public void setOperResFlowStatusName(String operResFlowStatusName) {
        this.operResFlowStatusName = operResFlowStatusName;
    }

    public String getIsOwnTaskFlowFlag() {
        return isOwnTaskFlowFlag;
    }

    public void setIsOwnTaskFlowFlag(String isOwnTaskFlowFlag) {
        this.isOwnTaskFlowFlag = isOwnTaskFlowFlag;
    }

}