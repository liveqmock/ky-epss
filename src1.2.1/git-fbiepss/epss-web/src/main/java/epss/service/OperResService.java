package epss.service;

import epss.repository.dao.not_mybatis.MyOperResMapper;
import epss.repository.model.model_show.OperResShow;
import epss.repository.model.model_show.OperRoleSelectShow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class OperResService {
    @Resource
    private MyOperResMapper myOperResMapper;

    public List<OperRoleSelectShow> selectOperaRoleRecords(String parentDeptid){
        return myOperResMapper.selectOperaRoleRecords(parentDeptid);
    }

    public List<OperResShow> selectOperaResRecords(){
        return myOperResMapper.selectOperaResRecords();
    }

    public List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(operResShowPara);
    }
}
