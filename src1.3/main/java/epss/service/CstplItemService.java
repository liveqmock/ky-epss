package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.CstplItemMapper;
import epss.repository.dao.notMyBits.MyCstplItemMapper;
import epss.repository.model.CstplItem;
import epss.repository.model.CstplItemExample;
import epss.repository.model.model_show.CstplItemShow;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vincent
 * Date: 13-2-19
 * Time: 下午9:44
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CstplItemService {
    @Resource
    private CstplItemMapper cstplItemMapper;
    @Resource
    private MyCstplItemMapper myCstplItemMapper;
    @Resource
    private PlatformService platformService;

    public Integer getMaxOrderidInEsItemHieRelapList(String strBelongToPkid,
                                                      String strParentPkid,
                                                      Integer intGrade){
        CstplItemExample example = new CstplItemExample();
        example.createCriteria()
                .andCstplInfoPkidEqualTo(strBelongToPkid)
                .andParentPkidEqualTo(strParentPkid)
                .andGradeEqualTo(intGrade);
        example .setOrderByClause("ORDERID DESC") ;
        List<CstplItem> cstplItemList = cstplItemMapper.selectByExample(example);
        if(cstplItemList.size() ==0){
            return 0;
        }
        else{
            return  cstplItemList.get(0).getOrderid();
        }
    }
    public List<CstplItem> getCstplListByCstplInfoPkid(String strCstplInfoPkidPara){
        CstplItemExample example = new CstplItemExample();
        example.createCriteria()
                .andCstplInfoPkidEqualTo(strCstplInfoPkidPara);
        example .setOrderByClause("GRADE ASC,ORDERID ASC") ;
        return cstplItemMapper.selectByExample(example);
        /*return commonMapper.selEsItemHieRelapListByTypeAndId(strBelongToType,strItemBelongToId);*/
    }

    public void setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(String strTkcttInfoPkidPara,
                                                                   String strParentPkid,
                                                                   Integer intGrade){
        myCstplItemMapper.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                strTkcttInfoPkidPara,
                strParentPkid,
                intGrade);
    }

    public void setOrderidSubOneByInfoPkidAndParentPkidAndGrade(String strTkcttInfoPkidPara,
                                                                  String strParentPkidPara,
                                                                  Integer intGradePara){
        myCstplItemMapper.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                strTkcttInfoPkidPara,
                strParentPkidPara,
                intGradePara);
    }

    /**
     * 判断记录是否已存在
     *
     * @param   cstplItem
     * @return
     */
    public boolean isExistSameIdInDb(CstplItem cstplItem) {
        CstplItemExample example = new CstplItemExample();
        CstplItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andCstplInfoPkidEqualTo(cstplItem.getCstplInfoPkid())
                .andParentPkidEqualTo(cstplItem.getParentPkid())
                .andGradeEqualTo(cstplItem.getGrade())
                .andOrderidEqualTo(cstplItem.getOrderid());
        return cstplItemMapper.countByExample(example) >= 1;
    }
    public boolean isExistSameRecordInDb(CstplItem cstplItem) {
        CstplItemExample example = new CstplItemExample();
        CstplItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andCstplInfoPkidEqualTo(cstplItem.getCstplInfoPkid())
                .andParentPkidEqualTo(cstplItem.getParentPkid())
                .andGradeEqualTo(cstplItem.getGrade())
                .andOrderidEqualTo(cstplItem.getOrderid());

        if(cstplItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(cstplItem.getName());
        }
        return cstplItemMapper.countByExample(example) >= 1;
    }

    public void insertRecord(CstplItemShow cstplItemShowPara) {
        CstplItem cstplItemTemp =fromShowModelToModel(cstplItemShowPara);
        cstplItemTemp.setArchivedFlag("0");
        cstplItemTemp.setOriginFlag("0");
        cstplItemTemp.setCreatedBy(platformService.getStrLastUpdBy());
        cstplItemTemp.setCreatedTime(platformService.getStrLastUpdTime());
        cstplItemTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        cstplItemTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        cstplItemMapper.insertSelective(cstplItemTemp);
    }

    /*成本计划到层级关系*/
    public CstplItem fromShowModelToModel(CstplItemShow cstplItemShowPara){
        CstplItem cstplItemTemp =new CstplItem() ;
        cstplItemTemp.setPkid(cstplItemShowPara.getCstpl_Pkid()) ;
        cstplItemTemp.setUnit(cstplItemShowPara.getCstpl_Unit());
        cstplItemTemp.setCstplInfoPkid(cstplItemShowPara.getCstpl_CstplInfoPkid());
        cstplItemTemp.setParentPkid(cstplItemShowPara.getCstpl_ParentPkid()) ;
        cstplItemTemp.setGrade(cstplItemShowPara.getCstpl_Grade()) ;
        cstplItemTemp.setOrderid(cstplItemShowPara.getCstpl_Orderid()) ;
        cstplItemTemp.setName(cstplItemShowPara.getCstpl_Name()) ;
        cstplItemTemp.setUnitPrice(cstplItemShowPara.getCstpl_UnitPrice());
        cstplItemTemp.setQty(cstplItemShowPara.getCstpl_Qty());
        cstplItemTemp.setAmt(cstplItemShowPara.getCstpl_Amt());
        cstplItemTemp.setArchivedFlag(cstplItemShowPara.getCstpl_ArchivedFlag());
        cstplItemTemp.setOriginFlag(cstplItemShowPara.getCstpl_OriginFlag());
        cstplItemTemp.setCreatedTime(cstplItemShowPara.getCstpl_CreatedTime());
        cstplItemTemp.setCreatedBy(cstplItemShowPara.getCstpl_CreatedBy());
        cstplItemTemp.setUpdatedTime(cstplItemShowPara.getCstpl_UpdatedTime());
        cstplItemTemp.setUpdatedBy(cstplItemShowPara.getCstpl_UpdatedBy());
        cstplItemTemp.setRemark(cstplItemShowPara.getCstpl_Remark());
        cstplItemTemp.setTkcttItemPkid(cstplItemShowPara.getCstpl_TkcttItemPkid());
        cstplItemTemp.setRecVersion(cstplItemShowPara.getCstpl_RecVersion());
        return cstplItemTemp;
    }

    public void updateRecord(CstplItemShow cstplItemShowPara) {
        CstplItem cstplItemTemp =fromShowModelToModel(cstplItemShowPara);
        cstplItemTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(cstplItemTemp.getRecVersion()) + 1);
        cstplItemTemp.setArchivedFlag("0");
        cstplItemTemp.setOriginFlag("0");
        cstplItemTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        cstplItemTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        cstplItemMapper.updateByPrimaryKey(cstplItemTemp) ;
    }

    public int deleteRecord(String strPkId){
        return cstplItemMapper.deleteByPrimaryKey(strPkId);
    }

    public CstplItem getEsItemHieRelapByPkId(String strPkId){
        return cstplItemMapper.selectByPrimaryKey(strPkId) ;
    }
}
