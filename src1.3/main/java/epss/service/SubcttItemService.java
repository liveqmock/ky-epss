package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.SubcttItemMapper;
import epss.repository.dao.notMyBits.MySubcttItemMapper;
import epss.repository.model.SubcttItem;
import epss.repository.model.SubcttItemExample;
import epss.repository.model.model_show.SubcttItemShow;
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
public class SubcttItemService {
    @Resource
    private SubcttItemMapper subcttItemMapper;
    @Resource
    private MySubcttItemMapper mySubcttItemMapper;
    @Resource
    private PlatformService platformService;

    public Integer getMaxOrderidInEsItemHieRelapList(String strBelongToPkid,
                                                      String strParentPkid,
                                                      Integer intGrade){
        SubcttItemExample example = new SubcttItemExample();
        example.createCriteria()
                .andSubcttInfoPkidEqualTo(strBelongToPkid)
                .andParentPkidEqualTo(strParentPkid)
                .andGradeEqualTo(intGrade);
        example .setOrderByClause("ORDERID DESC") ;
        List<SubcttItem> cttItemList = subcttItemMapper.selectByExample(example);
        if(cttItemList.size() ==0){
            return 0;
        }
        else{
            return  cttItemList.get(0).getOrderid();
        }
    }
    public List<SubcttItem> getSubcttItemListBySubcttInfoPkid(String strSubcttInfoPkidPara){
        SubcttItemExample example = new SubcttItemExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(strSubcttInfoPkidPara);
        example .setOrderByClause("GRADE ASC,ORDERID ASC") ;
        return subcttItemMapper.selectByExample(example);
    }

    public void setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(String strCstplInfoPkidPara,
                                                                   String strParentPkid,
                                                                   Integer intGrade){
        mySubcttItemMapper.setOrderidPlusOneByInfoPkidAndParentPkidAndGrade(
                strCstplInfoPkidPara,
                strParentPkid,
                intGrade);
    }

    public void setOrderidSubOneByInfoPkidAndParentPkidAndGrade(String strCstplInfoPkidPara,
                                                                  String strParentPkidPara,
                                                                  Integer intGradePara){
        mySubcttItemMapper.setOrderidSubOneByInfoPkidAndParentPkidAndGrade(
                strCstplInfoPkidPara,
                strParentPkidPara,
                intGradePara);
    }

    public boolean isExistSameRecordInDb(SubcttItem cttItem) {
        SubcttItemExample example = new SubcttItemExample();
        SubcttItemExample.Criteria criteria = example.createCriteria();
        criteria
                .andSubcttInfoPkidEqualTo(cttItem.getSubcttInfoPkid())
                .andParentPkidEqualTo(cttItem.getParentPkid())
                .andGradeEqualTo(cttItem.getGrade())
                .andOrderidEqualTo(cttItem.getOrderid());

        if(cttItem.getName()==null){
            criteria.andNameIsNull();
        }
        else{
            criteria.andNameEqualTo(cttItem.getName());
        }
        return subcttItemMapper.countByExample(example) >= 1;
    }

    public void insertRecord(SubcttItemShow cttItemShowPara) {
        SubcttItem cttItemTemp =fromShowModelToModel(cttItemShowPara);
        cttItemTemp.setArchivedFlag("0");
        cttItemTemp.setOriginFlag("0");
        cttItemTemp.setCreatedBy(platformService.getStrLastUpdBy());
        cttItemTemp.setCreatedTime(platformService.getStrLastUpdTime());
        cttItemTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        cttItemTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        subcttItemMapper.insertSelective(cttItemTemp);
    }

    /*总包合同到层级关系*/
    public SubcttItem fromShowModelToModel(SubcttItemShow cttItemShowPara){
        SubcttItem cttItemTemp =new SubcttItem() ;
        cttItemTemp.setUnit(cttItemShowPara.getUnit());
        cttItemTemp.setPkid(cttItemShowPara.getPkid()) ;
        cttItemTemp.setSubcttInfoPkid(cttItemShowPara.getSubcttInfoPkid());
        cttItemTemp.setParentPkid(cttItemShowPara.getParentPkid()) ;
        cttItemTemp.setGrade(cttItemShowPara.getGrade()) ;
        cttItemTemp.setOrderid(cttItemShowPara.getOrderid()) ;
        cttItemTemp.setName(cttItemShowPara.getName()) ;
        cttItemTemp.setUnitPrice(cttItemShowPara.getUnitPrice());
        cttItemTemp.setQty(cttItemShowPara.getQty());
        cttItemTemp.setSignPartPriceA(cttItemShowPara.getSignPartPriceA());
        cttItemTemp.setAmt(cttItemShowPara.getAmt());
        cttItemTemp.setArchivedFlag(cttItemShowPara.getArchivedFlag());
        cttItemTemp.setOriginFlag(cttItemShowPara.getOriginFlag());
        cttItemTemp.setCreatedTime(cttItemShowPara.getCreatedTime());
        cttItemTemp.setCreatedBy(cttItemShowPara.getCreatedBy());
        cttItemTemp.setUpdatedTime(cttItemShowPara.getUpdatedTime());
        cttItemTemp.setUpdatedBy(cttItemShowPara.getUpdatedBy());
        cttItemTemp.setRemark(cttItemShowPara.getRemark());
        cttItemTemp.setCstplItemPkid(cttItemShowPara.getCstplItemPkid());
        cttItemTemp.setRecVersion(cttItemShowPara.getRecVersion());
        return cttItemTemp;
    }
    /*层级关系到总包合同*/
    private SubcttItemShow getitemOfEsItemHieRelapFromEsItemHieRelap(SubcttItem subcttItemPara){
        SubcttItemShow cttItemShowTemp =new SubcttItemShow() ;
        cttItemShowTemp.setPkid(subcttItemPara.getPkid()) ;
        cttItemShowTemp.setSubcttInfoPkid(subcttItemPara.getSubcttInfoPkid());
        cttItemShowTemp.setParentPkid(subcttItemPara.getParentPkid()) ;
        cttItemShowTemp.setGrade(subcttItemPara.getGrade()) ;
        cttItemShowTemp.setOrderid(subcttItemPara.getOrderid()) ;
        cttItemShowTemp.setName(subcttItemPara.getName()) ;
        cttItemShowTemp.setRemark(subcttItemPara.getRemark());
        cttItemShowTemp.setCstplItemPkid(subcttItemPara.getCstplItemPkid());
        return cttItemShowTemp;
    }

    public void updateRecord(SubcttItemShow cttItemShowPara) {
        SubcttItem cttItemTemp =fromShowModelToModel(cttItemShowPara);
        cttItemTemp.setRecVersion(
                ToolUtil.getIntIgnoreNull(cttItemTemp.getRecVersion()) + 1);
        cttItemTemp.setArchivedFlag("0");
        cttItemTemp.setOriginFlag("0");
        cttItemTemp.setUpdatedBy(platformService.getStrLastUpdBy());
        cttItemTemp.setUpdatedTime(platformService.getStrLastUpdTime());
        subcttItemMapper.updateByPrimaryKey(cttItemTemp) ;
    }

    public int deleteRecord(String strPkId){
        return subcttItemMapper.deleteByPrimaryKey(strPkId);
    }
}
