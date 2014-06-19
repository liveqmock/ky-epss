package epss.repository.model.model_show;

public class TaskShow {
    private String power_Type;
    private String power_Pkid;
    private String period_No;
    private String status_Flag;
    private String status_Flag_Name;
    private String pre_Status_Flag;
    private String pre_Status_Flag_Name;
    private String group_Counts;
    private String id;
    private String name;
    private String belong_To_Pkid;
    private String parentName;

    public String getPower_Type() {
        return power_Type;
    }

    public void setPower_Type(String power_Type) {
        this.power_Type = power_Type;
    }

    public String getPower_Pkid() {
        return power_Pkid;
    }

    public void setPower_Pkid(String power_Pkid) {
        this.power_Pkid = power_Pkid;
    }

    public String getPeriod_No() {
        return period_No;
    }

    public void setPeriod_No(String period_No) {
        this.period_No = period_No;
    }

    public String getStatus_Flag() {
        return status_Flag;
    }

    public void setStatus_Flag(String status_Flag) {
        this.status_Flag = status_Flag;
    }

    public String getPre_Status_Flag() {
        return pre_Status_Flag;
    }

    public void setPre_Status_Flag(String pre_Status_Flag) {
        this.pre_Status_Flag = pre_Status_Flag;
    }

    public String getGroup_Counts() {
        return group_Counts;
    }

    public void setGroup_Counts(String group_Counts) {
        this.group_Counts = group_Counts;
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

    public String getBelong_To_Pkid() {
        return belong_To_Pkid;
    }

    public void setBelong_To_Pkid(String belong_To_Pkid) {
        this.belong_To_Pkid = belong_To_Pkid;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPre_Status_Flag_Name() {
        return pre_Status_Flag_Name;
    }

    public void setPre_Status_Flag_Name(String pre_Status_Flag_Name) {
        this.pre_Status_Flag_Name = pre_Status_Flag_Name;
    }

    public String getStatus_Flag_Name() {
        return status_Flag_Name;
    }

    public void setStatus_Flag_Name(String status_Flag_Name) {
        this.status_Flag_Name = status_Flag_Name;
    }
}