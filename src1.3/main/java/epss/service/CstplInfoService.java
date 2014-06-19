package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.CstplInfoMapper;
import epss.repository.dao.notMyBits.MyCstplInfoMapper;
import epss.repository.model.CstplInfo;
import epss.repository.model.CstplInfoExample;
import epss.repository.model.model_show.CstplInfoShow;
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
public class CstplInfoService {
    @Autowired
    private CstplInfoMapper cstplInfoMapper;
    @Autowired
    private MyCstplInfoMapper myCstplInfoMapper;
    @Resource
    private PlatformService platformService;

    public List<CstplInfo> getCstplInfoListByShowModel(CstplInfoShow cstplInfoShowPara) {
        CstplInfoExample example = new CstplInfoExample();
        CstplInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdLike("%" + ToolUtil.getStrIgnoreNull(cstplInfoShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(cstplInfoShowPara.getName()) + "%");
        //可以为NULL的项
        if (!ToolUtil.getStrIgnoreNull(cstplInfoShowPara.getTkcttInfoPkid()).equals("")) {
            criteria.andTkcttInfoPkidLike(ToolUtil.getStrIgnoreNull(cstplInfoShowPara.getTkcttInfoPkid()));
        }
        if (!ToolUtil.getStrIgnoreNull(cstplInfoShowPara.getSignDate()).equals("")) {
            criteria.andSignDateLike(ToolUtil.getStrIgnoreNull(cstplInfoShowPara.getSignDate()));
        }
        example.setOrderByClause("ID ASC");
        return cstplInfoMapper.selectByExample(example);
    }

    public List<CstplInfo> getCstplInfoListByModel(CstplInfo cstplInfoara) {
        CstplInfoExample example = new CstplInfoExample();
        CstplInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdLike("%" + ToolUtil.getStrIgnoreNull(cstplInfoara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(cstplInfoara.getName()) + "%");
        //可以为NULL的项
        if (!ToolUtil.getStrIgnoreNull(cstplInfoara.getTkcttInfoPkid()).equals("")) {
            criteria.andTkcttInfoPkidLike(ToolUtil.getStrIgnoreNull(cstplInfoara.getTkcttInfoPkid()));
        }
        if (!ToolUtil.getStrIgnoreNull(cstplInfoara.getSignDate()).equals("")) {
            criteria.andSignDateLike(ToolUtil.getStrIgnoreNull(cstplInfoara.getSignDate()));
        }
        example.setOrderByClause("ID ASC");
        return cstplInfoMapper.selectByExample(example);
    }

   /* public List<CstplInfoShow> getCstplInfoListByCttType_Status(String strCttyTypePara,String strStatusPara) {
        return myCstplInfoMapper.getCstplInfoListByCttType_Status(strCttyTypePara,strStatusPara);
    }

    public List<CstplInfoShow> getCstplInfoListByCttType_ParentPkid_Status(
            String strCttyTypePara,
            String strParentPkidPara,
            String strStatusPara) {
        return myCstplInfoMapper.getCstplInfoListByCttType_ParentPkid_Status(
                strCttyTypePara,
                strParentPkidPara,
                strStatusPara);
    }*/

    public List<CstplInfo> getEsInitCttByCttTypeAndBelongToPkId(String strBelongToPkid) {
        CstplInfoExample example = new CstplInfoExample();
        CstplInfoExample.Criteria criteria = example.createCriteria();
        criteria.andTkcttInfoPkidEqualTo(ToolUtil.getStrIgnoreNull(strBelongToPkid));
        example.setOrderByClause("ID ASC");
        return cstplInfoMapper.selectByExample(example);
    }

    public CstplInfo getCstplInfoByPkid(String strPkid) {
        return cstplInfoMapper.selectByPrimaryKey(strPkid);
    }

    public String getStrMaxCstplInfoId() {
        return myCstplInfoMapper.getStrMaxCstplInfoId();
    }

    public List<CstplInfoShow> getCstplInfoListByFlowStatusBegin_End(CstplInfoShow cstplInfoShowPara) {
        return myCstplInfoMapper.getCstplInfoListByFlowStatusBegin_End(cstplInfoShowPara);
    }

    /**
     * 判断记录是否已存在
     *
     * @param cstplInfoShowPara
     * @return
     */
    public boolean isExistInDb(CstplInfoShow cstplInfoShowPara) {
        CstplInfoExample example = new CstplInfoExample();
        CstplInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(cstplInfoShowPara.getId())
                .andNameEqualTo(cstplInfoShowPara.getName());
        return cstplInfoMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CstplInfoShow cstplInfoShowPara) {
        cstplInfoShowPara.setArchivedFlag("0");
        cstplInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        cstplInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        cstplInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        cstplInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        cstplInfoMapper.insertSelective(fromShowModelToModel(cstplInfoShowPara));
    }

    public void updateRecord(CstplInfo cstplInfoPara) {
        cstplInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(cstplInfoPara.getRecVersion()) + 1);
        cstplInfoPara.setArchivedFlag("0");
        cstplInfoPara.setUpdatedBy(platformService.getStrLastUpdBy());
        cstplInfoPara.setUpdatedTime(platformService.getStrLastUpdTime());
        cstplInfoMapper.updateByPrimaryKey(cstplInfoPara);
    }
    public void updateRecord(CstplInfoShow cstplInfoShowPara) {
        cstplInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(cstplInfoShowPara.getRecVersion()) + 1);
        cstplInfoShowPara.setArchivedFlag("0");
        cstplInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        cstplInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        cstplInfoMapper.updateByPrimaryKey(fromShowModelToModel(cstplInfoShowPara));
    }

    public int deleteRecord(String strPkId) {
        return cstplInfoMapper.deleteByPrimaryKey(strPkId);
    }

    private CstplInfo fromShowModelToModel(CstplInfoShow cstplInfoShowPara) {
        CstplInfo cstplInfoTemp = new CstplInfo();
        cstplInfoTemp.setPkid(cstplInfoShowPara.getPkid());
        cstplInfoTemp.setTkcttInfoPkid(cstplInfoShowPara.getTkcttInfoPkid());
        cstplInfoTemp.setId(cstplInfoShowPara.getId());
        cstplInfoTemp.setName(cstplInfoShowPara.getName());
        cstplInfoTemp.setSignDate(cstplInfoShowPara.getSignDate());
        cstplInfoTemp.setArchivedFlag(cstplInfoShowPara.getArchivedFlag());
        cstplInfoTemp.setOriginFlag(cstplInfoShowPara.getOriginFlag());
        cstplInfoTemp.setFlowStatus(cstplInfoShowPara.getFlowStatus());
        cstplInfoTemp.setFlowStatusRemark(cstplInfoShowPara.getFlowStatusRemark());
        cstplInfoTemp.setCreatedBy(cstplInfoShowPara.getCreatedBy());
        cstplInfoTemp.setCreatedTime(cstplInfoShowPara.getCreatedTime());
        cstplInfoTemp.setUpdatedBy(cstplInfoShowPara.getUpdatedBy());
        cstplInfoTemp.setUpdatedTime(cstplInfoShowPara.getUpdatedTime());
        cstplInfoTemp.setRecVersion(cstplInfoShowPara.getRecVersion());
        cstplInfoTemp.setAttachment(cstplInfoShowPara.getAttachment());
        cstplInfoTemp.setRemark(cstplInfoShowPara.getRemark());
        return cstplInfoTemp;
    }

    /*public List<AllTabColumns>  getColumnNameByTableName(String strTableName){
        *//*return commonMapper.getColumnNameByTableName(strTableName) ;*//*
        AllTabColumnsExample  example = new AllTabColumnsExample();
        example.createCriteria().andTableNameEqualTo(strTableName);
        return allTabColumnsMapper.selectByExample(example) ;
    }*/
}
