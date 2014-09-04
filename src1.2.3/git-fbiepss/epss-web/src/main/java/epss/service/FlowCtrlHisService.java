package epss.service;

import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.dao.not_mybatis.MyFlowMapper;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.FlowCtrlHisExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FlowCtrlHisService {
    @Autowired
    private FlowCtrlHisMapper flowCtrlHisMapper;
    @Resource
    private MyFlowMapper myFlowMapper;

    public List<FlowCtrlHis> selectListByModel(FlowCtrlHis flowCtrlHis) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getInfoType()) + "%")
                .andInfoPkidLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getInfoPkid()) + "%")
                .andPeriodNoLike("%" + ToolUtil.getStrIgnoreNull(flowCtrlHis.getPeriodNo()) + "%");
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getFlowStatus()).equals("")){
            criteria.andFlowStatusLike("%"+ flowCtrlHis.getFlowStatus()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getFlowStatusReason()).equals("")){
            criteria.andFlowStatusReasonLike("%"+ flowCtrlHis.getFlowStatusReason()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ flowCtrlHis.getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%"+ flowCtrlHis.getCreatedTime()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(flowCtrlHis.getRemark()).equals("")){
            criteria.andRemarkLike("%"+ flowCtrlHis.getRemark()+"%");
        }
        example.setOrderByClause("INFO_TYPE ASC,INFO_PKID ASC,PERIOD_NO ASC,FLOW_STATUS ASC,CREATED_TIME ASC") ;
        return flowCtrlHisMapper.selectByExample(example);
    }

    public List<FlowCtrlHis> getMngFromPowerHisForSubcttStlList(String powerPkid,String periodNo){
        return myFlowMapper.getMngFromPowerHisForSubcttStlList(powerPkid,periodNo);
    }
}
