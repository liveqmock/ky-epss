package epss.view.scrollInfo;

import epss.repository.model.model_show.TaskShow;
import epss.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skyline.util.ToolUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/8/11.
 */
@ManagedBean
@ViewScoped
public class ScrollInfoAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ScrollInfoAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private String strCurrentViewMng;
    private String strLastViewMng;

    @PostConstruct
    public void init() {
        getViewMsg();
        if ("".equals(ToolUtil.getStrIgnoreNull(strCurrentViewMng))){
            strCurrentViewMng="欢迎您！！";
        }
        strLastViewMng=strCurrentViewMng;
    }

    public void getViewMsg() {
        getViewMsgFromTaskAction();
    }
    private void getViewMsgFromTaskAction(){
        List<TaskShow> taskShowTempList =taskService.initTaskShowList("ScrollInfoAction");
        if (taskShowTempList != null) {
            String strViewMngTemp="待处理任务：";
            for (TaskShow taskShowItem : taskShowTempList) {
                strViewMngTemp+=taskShowItem.getName()+";";
            }
            if (!(strViewMngTemp.equals(strLastViewMng))){
                strCurrentViewMng=strViewMngTemp;
                strLastViewMng=strCurrentViewMng;
            }
        }
    }

    /*智能字段 Start*/

    public String getStrCurrentViewMng() {
        return strCurrentViewMng;
    }

    public void setStrCurrentViewMng(String strCurrentViewMng) {
        this.strCurrentViewMng = strCurrentViewMng;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
