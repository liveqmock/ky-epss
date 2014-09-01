package epss.service;

import epss.repository.dao.EsInitPowerHisMapper;
import epss.repository.dao.not_mybatis.MyFlowMapper;
import epss.repository.model.EsInitPowerHis;
import epss.repository.model.EsInitPowerHisExample;
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
    private EsInitPowerHisMapper esInitPowerHisMapper;
    @Resource
    private MyFlowMapper myFlowMapper;

    public List<EsInitPowerHis> selectListByModel(EsInitPowerHis esInitPowerHis) {
        EsInitPowerHisExample example= new EsInitPowerHisExample();
        EsInitPowerHisExample.Criteria criteria = example.createCriteria();
        criteria.andPowerTypeLike("%" + ToolUtil.getStrIgnoreNull(esInitPowerHis.getPowerType()) + "%")
                .andPowerPkidLike("%" + ToolUtil.getStrIgnoreNull(esInitPowerHis.getPowerPkid()) + "%")
                .andPeriodNoLike("%" + ToolUtil.getStrIgnoreNull(esInitPowerHis.getPeriodNo()) + "%");
        if (!ToolUtil.getStrIgnoreNull(esInitPowerHis.getStatusFlag()).equals("")){
            criteria.andStatusFlagLike("%"+esInitPowerHis .getStatusFlag()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(esInitPowerHis.getPreStatusFlag()).equals("")){
            criteria.andPreStatusFlagLike("%"+esInitPowerHis .getPreStatusFlag()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(esInitPowerHis.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+esInitPowerHis .getCreatedBy()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(esInitPowerHis.getCreatedDate()).equals("")){
            criteria.andCreatedDateLike("%"+esInitPowerHis .getCreatedDate()+"%");
        }
        if (!ToolUtil.getStrIgnoreNull(esInitPowerHis.getSpareField()).equals("")){
            criteria.andSpareFieldLike("%"+esInitPowerHis .getSpareField()+"%");
        }
        example.setOrderByClause("POWER_TYPE ASC,POWER_PKID ASC,PERIOD_NO ASC,STATUS_FLAG ASC,CREATED_DATE ASC") ;
        return esInitPowerHisMapper.selectByExample(example);
    }

    public List<EsInitPowerHis> getMngFromPowerHisForSubcttStlList(String powerPkid,String periodNo){
        return myFlowMapper.getMngFromPowerHisForSubcttStlList(powerPkid,periodNo);
    }
}
