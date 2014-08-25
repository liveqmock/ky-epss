package epss.view.task;

import epss.repository.model.model_show.TaskShow;
import epss.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-2-4
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class DoneTaskAction {
    private static final Logger logger = LoggerFactory.getLogger(DoneTaskAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        //整个任务列表
        taskShowList = new ArrayList<TaskShow>();
        taskShowList=taskService.initTaskShowList("EsInitTask");
    }

    public List<TaskShow> getTaskShowList() {
        return taskShowList;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}