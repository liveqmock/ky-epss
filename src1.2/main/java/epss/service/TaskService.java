package epss.service;

import epss.repository.dao.not_mybatis.TaskMapper;
import epss.repository.model.model_show.TaskShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: ����8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaskService {
    @Autowired
    private TaskMapper taskMapper;

    public List<TaskShow> getTaskCountsInFlowGroup(){
        return taskMapper.getTaskCountsInFlowGroup();
    }

    public List<TaskShow> getTaskShowList(){
        return taskMapper.getTaskShowList();
    }
}