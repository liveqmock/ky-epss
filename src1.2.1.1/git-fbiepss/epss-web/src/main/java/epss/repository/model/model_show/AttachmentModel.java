package epss.repository.model.model_show;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-17
 * Time: ÏÂÎç9:54
 * To change this template use File | Settings | File Templates.
 */

public class AttachmentModel implements Serializable {
    private String COLUMN_ID;
    private String COLUMN_NAME;
    private String COLUMN_PATH;

    public String getCOLUMN_PATH() {
        return COLUMN_PATH;
    }

    public void setCOLUMN_PATH(String COLUMN_PATH) {
        this.COLUMN_PATH = COLUMN_PATH;
    }

    public AttachmentModel() {
    }


    public AttachmentModel(String id, String name, String path) {
        this.COLUMN_ID = id;
        this.COLUMN_NAME = name;
        this.COLUMN_PATH = path;
    }

    public String getCOLUMN_ID() {
        return COLUMN_ID;
    }

    public void setCOLUMN_ID(String COLUMN_ID) {
        this.COLUMN_ID = COLUMN_ID;
    }

    public String getCOLUMN_NAME() {
        return COLUMN_NAME;
    }

    public void setCOLUMN_NAME(String COLUMN_NAME) {
        this.COLUMN_NAME = COLUMN_NAME;
    }
}