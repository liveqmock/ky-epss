package epss.repository.model.model_show;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
public class OperRoleSelectShow {
    private String selid;
    private String slename;
    private String seltype;
    private String countnumer;
    private Boolean isSel;

    public String getSelid() {
        return selid;
    }

    public void setSelid(String selid) {
        this.selid = selid;
    }

    public String getSlename() {
        return slename;
    }

    public void setSlename(String slename) {
        this.slename = slename;
    }

    public String getSeltype() {
        return seltype;
    }

    public void setSeltype(String seltype) {
        this.seltype = seltype;
    }

    public String getCountnumer() {
        return countnumer;
    }

    public void setCountnumer(String countnumer) {
        this.countnumer = countnumer;
    }
    
    public Boolean getIsSel() {
        return isSel;
    }

    public void setIsSel(Boolean isSel) {
        this.isSel = isSel;
    }
}
