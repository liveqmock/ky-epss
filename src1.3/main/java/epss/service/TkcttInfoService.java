package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.TkcttInfoMapper;
import epss.repository.dao.notMyBits.MyTkcttInfoMapper;
import epss.repository.model.TkcttInfo;
import epss.repository.model.TkcttInfoExample;
import epss.repository.model.model_show.TkcttInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: 下午6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TkcttInfoService {
    @Autowired
    private TkcttInfoMapper tkcttInfoMapper;
    @Autowired
    private MyTkcttInfoMapper myTkcttInfoMapper;
    @Resource
    private PlatformService platformService;

    public List<TkcttInfo> getTkcttInfoListByModel(TkcttInfoShow tkcttInfoShowPara) {
        TkcttInfoExample example = new TkcttInfoExample();
        TkcttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andPkidLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getPkid()) + "%")
                .andIdLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getName()) + "%")
                .andSignDateLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getSignDate()) + "%")
                .andSignPartPkidALike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getSignPartPkidA()) + "%")
                .andSignPartPkidBLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getSignPartPkidB()) + "%")
                .andStartDateLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getStartDate()) + "%")
                .andEndDateLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getEndDate()) + "%")
                .andFlowStatusLike("%" + ToolUtil.getStrIgnoreNull((tkcttInfoShowPara.getFlowStatus() + "%")));
        example.setOrderByClause("ID ASC");
        return tkcttInfoMapper.selectByExample(example);
    }

    public String getStrMaxTkcttInfoId() {
        return myTkcttInfoMapper.getStrMaxTkcttInfoId();
    }

    public List<TkcttInfoShow> getTkcttInfoListByFlowStatusBegin_End(TkcttInfoShow tkcttInfoShowPara) {
        return myTkcttInfoMapper.getTkcttInfoListByFlowStatusBegin_End(tkcttInfoShowPara);
    }

    public TkcttInfo getTkcttInfoByPkid(String strPkid) {
        return tkcttInfoMapper.selectByPrimaryKey(strPkid);
    }

    /**
     * 判断记录是否已存在
     *
     * @param tkcttInfoShowPara
     * @return
     */
    public boolean isExistInDb(TkcttInfoShow tkcttInfoShowPara) {
        TkcttInfoExample example = new TkcttInfoExample();
        TkcttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(tkcttInfoShowPara.getId())
                .andNameEqualTo(tkcttInfoShowPara.getName());
        return tkcttInfoMapper.countByExample(example) >= 1;
    }

    public void insertRecord(TkcttInfoShow tkcttInfoShowPara) {
        tkcttInfoShowPara.setArchivedFlag("0");
        tkcttInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        tkcttInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        tkcttInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        tkcttInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        tkcttInfoMapper.insertSelective(fromShowModelToModel(tkcttInfoShowPara));
    }

    public void updateRecord(TkcttInfo tkcttInfoPara) {
        tkcttInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(tkcttInfoPara.getRecVersion()) + 1);
        tkcttInfoPara.setArchivedFlag("0");
        tkcttInfoPara.setUpdatedBy(platformService.getStrLastUpdBy());
        tkcttInfoPara.setUpdatedTime(platformService.getStrLastUpdTime());
        tkcttInfoMapper.updateByPrimaryKey(tkcttInfoPara);
    }
    public void updateRecord(TkcttInfoShow tkcttInfoShowPara) {
        tkcttInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(tkcttInfoShowPara.getRecVersion()) + 1);
        tkcttInfoShowPara.setArchivedFlag("0");
        tkcttInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        tkcttInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        tkcttInfoMapper.updateByPrimaryKey(fromShowModelToModel(tkcttInfoShowPara));
    }

    public int deleteRecord(String strPkId) {
        return tkcttInfoMapper.deleteByPrimaryKey(strPkId);
    }

    private TkcttInfo fromShowModelToModel(TkcttInfoShow tkcttInfoShowPara) {
        TkcttInfo tkcttInfoTemp = new TkcttInfo();
        tkcttInfoTemp.setPkid(tkcttInfoShowPara.getPkid());
        tkcttInfoTemp.setId(tkcttInfoShowPara.getId());
        tkcttInfoTemp.setName(tkcttInfoShowPara.getName());
        tkcttInfoTemp.setStartDate(tkcttInfoShowPara.getStartDate());
        tkcttInfoTemp.setEndDate(tkcttInfoShowPara.getEndDate());
        tkcttInfoTemp.setSignDate(tkcttInfoShowPara.getSignDate());
        tkcttInfoTemp.setSignPartPkidA(tkcttInfoShowPara.getSignPartPkidA());
        tkcttInfoTemp.setSignPartPkidB(tkcttInfoShowPara.getSignPartPkidB());
        tkcttInfoTemp.setArchivedFlag(tkcttInfoShowPara.getArchivedFlag());
        tkcttInfoTemp.setOriginFlag(tkcttInfoShowPara.getOriginFlag());
        tkcttInfoTemp.setFlowStatus(tkcttInfoShowPara.getFlowStatus());
        tkcttInfoTemp.setFlowStatusRemark(tkcttInfoShowPara.getFlowStatusRemark());
        tkcttInfoTemp.setCreatedBy(tkcttInfoShowPara.getCreatedBy());
        tkcttInfoTemp.setCreatedTime(tkcttInfoShowPara.getCreatedTime());
        tkcttInfoTemp.setUpdatedBy(tkcttInfoShowPara.getUpdatedBy());
        tkcttInfoTemp.setUpdatedTime(tkcttInfoShowPara.getUpdatedTime());
        tkcttInfoTemp.setRecVersion(tkcttInfoShowPara.getRecVersion());
        tkcttInfoTemp.setAttachment(tkcttInfoShowPara.getAttachment());
        tkcttInfoTemp.setRemark(tkcttInfoShowPara.getRemark());
        return tkcttInfoTemp;
    }

    /*public List<AllTabColumns>  getColumnNameByTableName(String strTableName){
        *//*return commonMapper.getColumnNameByTableName(strTableName) ;*//*
        AllTabColumnsExample  example = new AllTabColumnsExample();
        example.createCriteria().andTableNameEqualTo(strTableName);
        return allTabColumnsMapper.selectByExample(example) ;
    }*/
}
