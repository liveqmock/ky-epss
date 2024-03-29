package epss.service;

import epss.repository.dao.EsInitPowerHisMapper;
import epss.repository.dao.common.CommonMapper;
import epss.repository.model.EsInitPowerHis;
import epss.repository.model.EsInitPowerHisExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ����6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsInitPowerHisService {
    @Autowired
    private EsInitPowerHisMapper esInitPowerHisMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Resource
    private PlatformService platformService;

    public List<EsInitPowerHis> selectListByModel(EsInitPowerHis esInitPowerHis) {
        EsInitPowerHisExample example= new EsInitPowerHisExample();
        EsInitPowerHisExample.Criteria criteria = example.createCriteria();
        criteria.andPowerTypeLike("%" + stringIgnoreNull(esInitPowerHis.getPowerType()) + "%")
                .andPowerPkidLike("%" + stringIgnoreNull(esInitPowerHis.getPowerPkid()) + "%")
                .andPeriodNoLike("%" + stringIgnoreNull(esInitPowerHis.getPeriodNo()) + "%");
        if (!stringIgnoreNull(esInitPowerHis.getStatusFlag()).equals("")){
            criteria.andStatusFlagLike("%"+esInitPowerHis .getStatusFlag()+"%");
        }
        if (!stringIgnoreNull(esInitPowerHis.getPreStatusFlag()).equals("")){
            criteria.andPreStatusFlagLike("%"+esInitPowerHis .getPreStatusFlag()+"%");
        }
        if (!stringIgnoreNull(esInitPowerHis.getCreatedBy()).equals("")){
            criteria.andCreatedByLike("%"+esInitPowerHis .getCreatedBy()+"%");
        }
        if (!stringIgnoreNull(esInitPowerHis.getCreatedDate()).equals("")){
            criteria.andCreatedDateLike("%"+esInitPowerHis .getCreatedDate()+"%");
        }
        if (!stringIgnoreNull(esInitPowerHis.getSpareField()).equals("")){
            criteria.andSpareFieldLike("%"+esInitPowerHis .getSpareField()+"%");
        }
        example.setOrderByClause("POWER_TYPE ASC,POWER_PKID,PERIOD_NO ASC") ;
        return esInitPowerHisMapper.selectByExample(example);
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
