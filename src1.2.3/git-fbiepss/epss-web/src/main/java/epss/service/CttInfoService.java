package epss.service;

import epss.repository.dao.EsCttInfoMapper;
import epss.repository.dao.not_mybatis.MyCttInfoMapper;
import epss.repository.model.EsCttInfo;
import epss.repository.model.EsCttInfoExample;
import epss.repository.model.model_show.CttInfoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: 下午6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CttInfoService {
    @Autowired
    private EsCttInfoMapper esCttInfoMapper;
    @Autowired
    private MyCttInfoMapper myCttInfoMapper;

    public List<EsCttInfo> selectListByModel(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example= new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttType()))
                .andIdLike("%" + ToolUtil.getStrIgnoreNull(cttInfoShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(cttInfoShowPara.getName()) + "%");
        //可以为NULL的项
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()).equals("")){
            criteria.andParentPkidLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getParentPkid()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignDate()).equals("")){
            criteria.andSignDateLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignDate()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartA()).equals("")){
            criteria.andSignPartALike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartA()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartB()).equals("")){
            criteria.andSignPartBLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getSignPartB()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttStartDate()).equals("")){
            criteria.andCttStartDateLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttStartDate()));
        }
        if(!ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttEndDate()).equals("")){
            criteria.andCttEndDateLike(ToolUtil.getStrIgnoreNull(cttInfoShowPara.getCttEndDate()));
        }
        example.setOrderByClause("ID ASC") ;
        return esCttInfoMapper.selectByExample(example);
    }

    public List<CttInfoShow> getCttInfoListByCttType_Status(String strCttyTypePara,String strStatusPara) {
        return myCttInfoMapper.getCttInfoListByCttType_Status(strCttyTypePara,strStatusPara);
    }

    public List<CttInfoShow> getCttInfoListByCttType_ParentPkid_Status(
            String strCttyTypePara,
            String strParentPkidPara,
            String strStatusPara) {
        return myCttInfoMapper.getCttInfoListByCttType_ParentPkid_Status(
                strCttyTypePara,
                strParentPkidPara,
                strStatusPara);
    }

    public List<EsCttInfo> getEsInitCttByCttTypeAndBelongToPkId(String strCttType,String strBelongToPkid) {
        EsCttInfoExample example= new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(strCttType))
                .andParentPkidEqualTo(ToolUtil.getStrIgnoreNull(strBelongToPkid));
        example.setOrderByClause("ID ASC") ;
        return esCttInfoMapper.selectByExample(example);
    }

    public List<EsCttInfo> getEsInitCttListByCttType(String strCttType) {
        EsCttInfoExample example= new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(ToolUtil.getStrIgnoreNull(strCttType));
        example.setOrderByClause("ID ASC") ;
        return esCttInfoMapper.selectByExample(example);
    }

    public EsCttInfo getCttInfoByPkId(String strPkid) {
        return esCttInfoMapper.selectByPrimaryKey(strPkid);
    }

    /**
     * 判断记录是否已存在
     *
     * @param   cttInfoShowPara
     * @return
     */
    public boolean isExistInDb(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(cttInfoShowPara.getCttType())
                .andIdEqualTo(cttInfoShowPara.getId())
                .andNameEqualTo(cttInfoShowPara.getName());
        return esCttInfoMapper.countByExample(example) >= 1;
    }
    public boolean isExistInDb(EsCttInfo esCttInfoPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCttTypeEqualTo(esCttInfoPara.getCttType())
                .andIdEqualTo(esCttInfoPara.getId())
                .andNameEqualTo(esCttInfoPara.getName());
        return esCttInfoMapper.countByExample(example) >= 1;
    }
    //验证合同编号和名称是否已存在
    public boolean IdisExistInDb(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(cttInfoShowPara.getId());
        return esCttInfoMapper.countByExample(example) >= 1;
    }
    public boolean NameisExistInDb(CttInfoShow cttInfoShowPara) {
        EsCttInfoExample example = new EsCttInfoExample();
        EsCttInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(cttInfoShowPara.getName());
        return esCttInfoMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CttInfoShow cttInfoShowPara) {
        cttInfoShowPara.setDeletedFlag("0");
        cttInfoShowPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        cttInfoShowPara.setCreatedDate(ToolUtil.getStrLastUpdTime());
        cttInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        cttInfoShowPara.setLastUpdDate(ToolUtil.getStrLastUpdTime());
        esCttInfoMapper.insertSelective(fromModelShowToModel(cttInfoShowPara));
    }
    public void insertRecord(EsCttInfo esCttInfoPara) {
        esCttInfoPara.setDeletedFlag("0");
        esCttInfoPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        esCttInfoPara.setCreatedDate(ToolUtil.getStrLastUpdTime());
        esCttInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esCttInfoPara.setLastUpdDate(ToolUtil.getStrLastUpdTime());
        esCttInfoMapper.insertSelective(esCttInfoPara);
    }

    public void updateRecord(EsCttInfo esCttInfoPara){
        esCttInfoPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esCttInfoPara.getModificationNum())+1);
        esCttInfoPara.setDeletedFlag("0");
        esCttInfoPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        esCttInfoPara.setLastUpdDate(ToolUtil.getStrLastUpdTime());
        esCttInfoMapper.updateByPrimaryKey(esCttInfoPara);
    }
    public void updateRecord(CttInfoShow cttInfoShowPara){
        cttInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(cttInfoShowPara.getModificationNum())+1);
        cttInfoShowPara.setDeletedFlag("0");
        cttInfoShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        cttInfoShowPara.setLastUpdDate(ToolUtil.getStrLastUpdTime());
        esCttInfoMapper.updateByPrimaryKey(fromModelShowToModel(cttInfoShowPara));
    }

    public int deleteRecord(String strPkId){
        return esCttInfoMapper.deleteByPrimaryKey(strPkId);
    }

    public String getStrMaxCttId(String strCttType){
        return myCttInfoMapper.getStrMaxCttId(strCttType) ;
    }

    private EsCttInfo fromModelShowToModel(CttInfoShow cttInfoShowPara) {
        EsCttInfo esCttInfoTemp = new EsCttInfo();
        esCttInfoTemp.setPkid(cttInfoShowPara.getPkid());
        esCttInfoTemp.setCttType(cttInfoShowPara.getCttType());
        esCttInfoTemp.setParentPkid(cttInfoShowPara.getParentPkid());
        esCttInfoTemp.setId(cttInfoShowPara.getId());
        esCttInfoTemp.setName(cttInfoShowPara.getName());
        esCttInfoTemp.setCttStartDate(cttInfoShowPara.getCttStartDate());
        esCttInfoTemp.setCttEndDate(cttInfoShowPara.getCttEndDate());
        esCttInfoTemp.setSignDate(cttInfoShowPara.getSignDate());
        esCttInfoTemp.setSignPartA(cttInfoShowPara.getSignPartA());
        esCttInfoTemp.setSignPartB(cttInfoShowPara.getSignPartB());
        esCttInfoTemp.setNote(cttInfoShowPara.getNote());
        esCttInfoTemp.setAttachment(cttInfoShowPara.getAttachment());
        esCttInfoTemp.setDeletedFlag(cttInfoShowPara.getDeletedFlag());
        esCttInfoTemp.setEndFlag(cttInfoShowPara.getEndFlag());
        esCttInfoTemp.setCreatedBy(cttInfoShowPara.getCreatedBy());
        esCttInfoTemp.setCreatedDate(cttInfoShowPara.getCreatedDate());
        esCttInfoTemp.setLastUpdBy(cttInfoShowPara.getLastUpdBy());
        esCttInfoTemp.setLastUpdDate(cttInfoShowPara.getLastUpdDate());
        esCttInfoTemp.setModificationNum(cttInfoShowPara.getModificationNum());
        esCttInfoTemp.setType(cttInfoShowPara.getType());
        return esCttInfoTemp;
    }
    //更新甲供材情况
    public int  updateByPKid(EsCttInfo esCttInfoPara){
      // return esCttInfoMapper.updateByPrimaryKey(fromModelShowToModel(cttInfoShowPara));
        return esCttInfoMapper.updateByPrimaryKey(esCttInfoPara);
    }

    /*public List<AllTabColumns>  getColumnNameByTableName(String strTableName){
        *//*return commonMapper.getColumnNameByTableName(strTableName) ;*//*
        AllTabColumnsExample  example = new AllTabColumnsExample();
        example.createCriteria().andTableNameEqualTo(strTableName);
        return allTabColumnsMapper.selectByExample(example) ;
    }*/
}
