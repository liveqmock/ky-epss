package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.SubcttInfoMapper;
import epss.repository.dao.notMyBits.MySubcttInfoMapper;
import epss.repository.model.SubcttInfo;
import epss.repository.model.SubcttInfoExample;
import epss.repository.model.model_show.SubcttInfoShow;
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
public class SubcttInfoService {
    @Autowired
    private SubcttInfoMapper subcttInfoMapper;
    @Autowired
    private MySubcttInfoMapper mySubcttInfoMapper;
    @Resource
    private PlatformService platformService;

    public List<SubcttInfo> getSubcttInfoListByModel(SubcttInfoShow tkcttInfoShowPara) {
        SubcttInfoExample example= new SubcttInfoExample();
        SubcttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andPkidLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getPkid()) + "%")
                .andIdLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getName()) + "%")
                .andSignDateLike("%" + ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getSignDate()) + "%")
                .andSignPartPkidALike("%" +ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getSignPartPkidA())+ "%")
                .andSignPartPkidBLike("%" +ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getSignPartPkidB())+ "%")
                .andStartDateLike("%" +ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getStartDate())+ "%")
                .andEndDateLike("%" +ToolUtil.getStrIgnoreNull(tkcttInfoShowPara.getEndDate())+ "%");
        example.setOrderByClause("ID ASC") ;
        return subcttInfoMapper.selectByExample(example);
    }

    public String getStrMaxSubcttInfoId(){
        return mySubcttInfoMapper.getStrMaxSubcttInfoId();
    }

    public List<SubcttInfoShow> getSubcttInfoListByFlowStatusBegin_End(SubcttInfoShow subcttInfoShowPara){
        return mySubcttInfoMapper.getSubcttInfoListByFlowStatusBegin_End(subcttInfoShowPara);
    }
    public List<SubcttInfoShow> getSubcttInfoShowListByShowModel(SubcttInfoShow subcttInfoShowPara){
        return mySubcttInfoMapper.getSubcttInfoShowListByShowModel(subcttInfoShowPara);
    }

   /* public List<SubcttInfoShow> getSubcttInfoListByCttType_Status(String strCttyTypePara,String strStatusPara) {
        return subcttInfoMapper.getCttInfoListByCttType_Status(strCttyTypePara,strStatusPara);
    }

    public List<SubcttInfoShow> getCttInfoListByCttType_ParentPkid_Status(
            String strCttyTypePara,
            String strParentPkidPara,
            String strStatusPara) {
        return subcttInfoMapper.getCttInfoListByCttType_ParentPkid_Status(
                strCttyTypePara,
                strParentPkidPara,
                strStatusPara);
    }*/

    public SubcttInfo getSubcttInfoByPkid(String strPkid) {
        return subcttInfoMapper.selectByPrimaryKey(strPkid);
    }

    /**
     * 判断记录是否已存在
     *
     * @param   subcttInfoShowPara
     * @return
     */
    public boolean isExistInDb(SubcttInfoShow subcttInfoShowPara) {
        SubcttInfoExample example = new SubcttInfoExample();
        SubcttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(subcttInfoShowPara.getId())
                .andNameEqualTo(subcttInfoShowPara.getName());
        return subcttInfoMapper.countByExample(example) >= 1;
    }

    public void insertRecord(SubcttInfoShow subcttInfoShowPara) {
        subcttInfoShowPara.setArchivedFlag("0");
        subcttInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        subcttInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        subcttInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        subcttInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        subcttInfoMapper.insertSelective(fromShowModelToModel(subcttInfoShowPara));
    }

    public void updateRecord(SubcttInfo cttInfoPara){
        cttInfoPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttInfoPara.getRecVersion()) + 1);
        cttInfoPara.setArchivedFlag("0");
        cttInfoPara.setUpdatedBy(platformService.getStrLastUpdBy());
        cttInfoPara.setUpdatedTime(platformService.getStrLastUpdTime());
        subcttInfoMapper.updateByPrimaryKey(cttInfoPara);
    }
    public void updateRecord(SubcttInfoShow subcttInfoShowPara){
        subcttInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(subcttInfoShowPara.getRecVersion()) + 1);
        subcttInfoShowPara.setArchivedFlag("0");
        subcttInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        subcttInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        subcttInfoMapper.updateByPrimaryKey(fromShowModelToModel(subcttInfoShowPara));
    }

    public int deleteRecord(String strPkId){
        return subcttInfoMapper.deleteByPrimaryKey(strPkId);
    }

    private SubcttInfo fromShowModelToModel(SubcttInfoShow subcttInfoShowPara) {
        SubcttInfo subcttTemp = new SubcttInfo();
        subcttTemp.setPkid(subcttInfoShowPara.getPkid());
        subcttTemp.setCstplInfoPkid(subcttInfoShowPara.getCstplInfoPkid());
        subcttTemp.setId(subcttInfoShowPara.getId());
        subcttTemp.setName(subcttInfoShowPara.getName());
        subcttTemp.setStartDate(subcttInfoShowPara.getStartDate());
        subcttTemp.setEndDate(subcttInfoShowPara.getEndDate());
        subcttTemp.setSignDate(subcttInfoShowPara.getSignDate());
        subcttTemp.setSignPartPkidA(subcttInfoShowPara.getSignPartPkidA());
        subcttTemp.setSignPartPkidB(subcttInfoShowPara.getSignPartPkidB());
        subcttTemp.setArchivedFlag(subcttInfoShowPara.getArchivedFlag());
        subcttTemp.setOriginFlag(subcttInfoShowPara.getOriginFlag());
        subcttTemp.setFlowStatus(subcttInfoShowPara.getFlowStatus());
        subcttTemp.setFlowStatusRemark(subcttInfoShowPara.getFlowStatusRemark());
        subcttTemp.setCreatedBy(subcttInfoShowPara.getCreatedBy());
        subcttTemp.setCreatedTime(subcttInfoShowPara.getCreatedTime());
        subcttTemp.setUpdatedBy(subcttInfoShowPara.getUpdatedBy());
        subcttTemp.setUpdatedTime(subcttInfoShowPara.getUpdatedTime());
        subcttTemp.setRecVersion(subcttInfoShowPara.getRecVersion());
        subcttTemp.setAttachment(subcttInfoShowPara.getAttachment());
        subcttTemp.setRemark(subcttInfoShowPara.getRemark());
        return subcttTemp;
    }
}
