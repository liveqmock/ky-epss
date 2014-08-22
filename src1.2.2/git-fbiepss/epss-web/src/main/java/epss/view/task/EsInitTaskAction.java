package epss.view.task;

import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.TaskShow;
import epss.service.OperResService;
import epss.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import skyline.util.ToolUtil;

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
public class EsInitTaskAction {
    private static final Logger logger = LoggerFactory.getLogger(EsInitTaskAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;
    @ManagedProperty(value = "#{operResService}")
    private OperResService operResService;

    private List<TaskShow> taskShowList;

    @PostConstruct
    public void init() {
        //整个任务列表
        taskShowList = new ArrayList<TaskShow>();
        taskShowList=taskService.initTaskShowList("EsInitTask");
        OperResShow operResShowTemp = new OperResShow();
        operResShowTemp.setOperPkid(ToolUtil.getOperatorManager().getOperatorId());
        List<OperResShow> operResShowList =
                operResService.selectOperaResRecordsByModelShow(operResShowTemp);
    }

    public List<TaskShow> getTaskShowList() {
        return taskShowList;
    }

    public OperResService getOperResService() {
        return operResService;
    }

    public void setOperResService(OperResService operResService) {
        this.operResService = operResService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}