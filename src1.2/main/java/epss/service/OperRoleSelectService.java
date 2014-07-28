package epss.service;

import epss.repository.dao.not_mybatis.OperRoleSelectMapper;
import epss.repository.model.model_show.OperRoleSelectShow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class OperRoleSelectService {
    @Resource
    private OperRoleSelectMapper operRoleSelectMapper;

    public List<OperRoleSelectShow> selectOperaRoleRecords(String parentDeptid){
        return operRoleSelectMapper.selectOperaRoleRecords(parentDeptid);
    }
}
