package epss.repository.model.model_show;

public class CommColModel {
    private String column_id;
    private String column_name;
    private String comments;
    private String disabled_flag;
    private String rendered_flag;
    private String disabled_style;

    public CommColModel(){

    }

    public CommColModel(String disabled_flag, String rendered_flag){
        this .disabled_flag=disabled_flag ;
        this .rendered_flag=rendered_flag ;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRendered_flag() {
        return rendered_flag;
    }

    public void setRendered_flag(String rendered_flag) {
        this.rendered_flag = rendered_flag;
    }

    public String getDisabled_flag() {
        return disabled_flag;
    }

    public void setDisabled_flag(String disabled_flag) {
        this.disabled_flag = disabled_flag;
    }

    public String getDisabled_style() {
        if(disabled_flag .equals("true")){
            disabled_style ="font-weight:bold;background-color:gainsboro";
        }else {
            disabled_style ="font-weight:bold;";
        }
        return disabled_style;
    }

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
    }
}