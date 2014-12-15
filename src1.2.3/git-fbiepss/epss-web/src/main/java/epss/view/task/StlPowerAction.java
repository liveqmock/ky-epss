package epss.view.task;

import epss.repository.model.model_show.TaskShow;
import epss.service.TaskService;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
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
public class StlPowerAction {
    private static final Logger logger = LoggerFactory.getLogger(StlPowerAction.class);
    @ManagedProperty(value = "#{taskService}")
    private TaskService taskService;

    private TreeNode stlPowerRoot;

    @PostConstruct
    public void init() {
        //整个任务列表
        try {
            stlPowerRoot = new DefaultTreeNode("ROOT", null);
            List<TaskShow> stlPowerList=taskService.initRecentlyPowerTaskShowList();
            for (int i=0;i<stlPowerList.size();i++){

            }

        }catch (Exception e){
            logger.error("初始化失败", e);
        }
    }

    public TreeNode getStlPowerRoot() {
        return stlPowerRoot;
    }

    public void setStlPowerRoot(TreeNode stlPowerRoot) {
        this.stlPowerRoot = stlPowerRoot;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}