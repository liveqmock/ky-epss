package epss.service;

import epss.repository.model.model_show.TaskShow;
import epss.repository.dao.not_mybatis.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.repository.model.Ptoper;
import platform.view.build.utils.Util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: ÉÏÎç8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsCommonService {
    @Autowired
    private CommonMapper commonMapper;

    public List<TaskShow> getTaskCountsInFlowGroup(){
        return commonMapper.getTaskCountsInFlowGroup();
    }

    public List<TaskShow> getTaskModelList(){
        return commonMapper.getTaskModelList();
    }

    public List<TaskShow> getTaskModelListOfCtt(String strCttType){
        return commonMapper.getTaskModelListOfCtt(strCttType);
    }

    public List<TaskShow> getTaskModelListOfStl(String strCttType){
        return commonMapper.getTaskModelListOfStl(strCttType);
    }

    public String getOperNameByOperId(String strOperId){
        return Util.getUserName(strOperId);
    }

    public CommonMapper getCommonMapper() {
        return commonMapper;
    }

    public void setCommonMapper(CommonMapper commonMapper) {
        this.commonMapper = commonMapper;
    }

    public List<Ptoper> getOperList(){
        return commonMapper.getOperList();
    }
}
