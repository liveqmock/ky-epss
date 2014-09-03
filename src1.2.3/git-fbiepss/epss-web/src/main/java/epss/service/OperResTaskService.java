package epss.service;

import epss.repository.dao.OperResTaskMapper;
import epss.repository.model.OperResTask;
import epss.repository.model.OperResTaskExample;
import epss.repository.model.model_show.CttInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class OperResTaskService {
    @Autowired
    private OperResTaskMapper operResTaskMapper;

    public List<OperResTask> selectListByModel(OperResTask operResTaskPara) {
        OperResTaskExample example= new OperResTaskExample();
        OperResTaskExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeEqualTo(ToolUtil.getStrIgnoreNull(operResTaskPara.getInfoType()))
                .andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(operResTaskPara.getInfoPkid()) + "%")
                .andInfoNameLike("%" + ToolUtil.getStrIgnoreNull(operResTaskPara.getInfoName()) + "%");
        example.setOrderByClause("INFO_TYPE,INFO_NAME ASC") ;
        return operResTaskMapper.selectByExample(example);
    }

    public void insertRecord(OperResTask operResTaskPara) {
        String strOperatorId=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTime=ToolUtil.getStrLastUpdTime();
        operResTaskPara.setCreatedBy(strOperatorId);
        operResTaskPara.setCreatedTime(strLastUpdTime);
        operResTaskPara.setLastUpdBy(strOperatorId);
        operResTaskPara.setLastUpdTime(strLastUpdTime);
        operResTaskMapper.insertSelective(operResTaskPara);
    }

    public void updateRecord(OperResTask operResTaskPara){
        operResTaskPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(operResTaskPara.getModificationNum())+1);
        operResTaskPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        operResTaskPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        operResTaskMapper.updateByPrimaryKey(operResTaskPara);
    }

    public int deleteRecord(String strPkId){
        return operResTaskMapper.deleteByPrimaryKey(strPkId);
    }

}
