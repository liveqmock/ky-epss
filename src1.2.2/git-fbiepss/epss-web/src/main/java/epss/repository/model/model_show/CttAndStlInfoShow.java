package epss.repository.model.model_show;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-4-22
 * Time: ÏÂÎç4:18
 * To change this template use File | Settings | File Templates.
 */
public class CttAndStlInfoShow implements Serializable {
    private String pkid;
    private String type;
    private String correspondingPkid;
    private String id;
    private String name;
    private String note;
    private String createdBy;
    private String createdByName;
    private String createdDate;
    private Boolean isSeled;

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCorrespondingPkid() {
        return correspondingPkid;
    }

    public void setCorrespondingPkid(String correspondingPkid) {
        this.correspondingPkid = correspondingPkid;
    }

    public Boolean getIsSeled() {
        return isSeled;
    }

    public void setIsSeled(Boolean isSeled) {
        this.isSeled = isSeled;
    }
}
