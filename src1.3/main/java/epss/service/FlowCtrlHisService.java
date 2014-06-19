package epss.service;

import epss.repository.dao.FlowCtrlHisMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.model.FlowCtrlHis;
import epss.repository.model.FlowCtrlHisExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

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
    @Autowired
    private CommonMapper commonMapper;
    @Resource
    private PlatformService platformService;

    public List<FlowCtrlHis> selectListByModel(FlowCtrlHis flowCtrlHis) {
        FlowCtrlHisExample example= new FlowCtrlHisExample();
        FlowCtrlHisExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeLike("%" + stringIgnoreNull(flowCtrlHis.getInfoType()) + "%")
                .andInfoPkidLike("%" + stringIgnoreNull(flowCtrlHis.getInfoPkid()) + "%")
                .andInfoIdLike("%" + stringIgnoreNull(flowCtrlHis.getInfoId()) + "%");
        if (!stringIgnoreNull(flowCtrlHis.getFlowStatus()).equals("")){
            criteria.andFlowStatusLike("%" + flowCtrlHis.getFlowStatus() + "%");
        }
        if (!stringIgnoreNull(flowCtrlHis.getFlowStatusRemark()).equals("")){
            criteria.andFlowStatusLike("%" + flowCtrlHis.getFlowStatusRemark() + "%");
        }
        if (!stringIgnoreNull(flowCtrlHis.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+ flowCtrlHis.getCreatedBy()+"%");
        }
        if (!stringIgnoreNull(flowCtrlHis.getCreatedTime()).equals("")){
            criteria.andCreatedTimeLike("%" + flowCtrlHis.getCreatedTime() + "%");
        }
        if (!stringIgnoreNull(flowCtrlHis.getRemark()).equals("")){
            criteria.andRemarkLike("%" + flowCtrlHis.getRemark() + "%");
        }
        example.setOrderByClause("INFO_TYPE ASC,INFO_PKID,STAGE_NO ASC") ;
        return flowCtrlHisMapper.selectByExample(example);
    }

    private String stringIgnoreNull(String strOriginal){
        if(strOriginal ==null){
            return "";
        }
        else {
            return strOriginal;
        }
    }
}
