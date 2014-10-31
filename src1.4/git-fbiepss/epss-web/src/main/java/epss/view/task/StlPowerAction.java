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
 * Time: ����4:53
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class StlPowerAction {
    private static final Logger logger = LoggerFactory.getLogger(StlPowerAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private List<TaskShow> stlPowerList;

    @PostConstruct
    public void init() {
        //���������б�
        stlPowerList=taskService.initRecentlyPowerTaskShowList();
    }

    public List<TaskShow> getStlPowerList() {
        return stlPowerList;
    }

    public void setStlPowerList(List<TaskShow> stlPowerList) {
        this.stlPowerList = stlPowerList;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}