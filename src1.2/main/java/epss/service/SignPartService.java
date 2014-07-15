package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.EsInitCustMapper;
import epss.repository.dao.not_mybatis.SignPartMapper;
import epss.repository.model.EsInitCust;
import epss.repository.model.EsInitCustExample;
import epss.repository.model.model_show.SignPartShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-1-31
 * Time: ÏÂÎç6:31
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SignPartService {
    @Autowired
    private SignPartMapper signPartMapper;
    @Autowired
    private EsInitCustMapper esInitCustMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private EsCommonService esCommonService;

    public List<SignPartShow> selectListByModel(SignPartShow signPartShowPara) {
        EsInitCustExample example= new EsInitCustExample();
        EsInitCustExample.Criteria criteria = example.createCriteria();
        criteria.andIdLike("%" + stringIgnoreNull(signPartShowPara.getId()) + "%")
                .andNameLike("%" + stringIgnoreNull(signPartShowPara.getName()) + "%")
                .andDeletedFlagLike("%" + stringIgnoreNull(signPartShowPara.getDeletedFlag()) + "%");
        if (!stringIgnoreNull(signPartShowPara.getNote()).equals("")){
            criteria.andNoteLike("%"+signPartShowPara .getNote()+"%");
        }
        example.setOrderByClause("ID ASC") ;
        List<EsInitCust> esInitCustListTemp=esInitCustMapper.selectByExample(example);
        List<SignPartShow> signPartShowListTemp=new ArrayList<>();
        for(EsInitCust itemUnit:esInitCustListTemp){
            SignPartShow signPartShowTemp=new SignPartShow();
            signPartShowTemp=fromModelToShow(itemUnit);
            signPartShowTemp.setCreatedByName(esCommonService.getOperNameByOperId(itemUnit.getCreatedBy()));
            signPartShowTemp.setLastUpdByName(esCommonService.getOperNameByOperId(itemUnit.getLastUpdBy()));
            signPartShowListTemp.add(signPartShowTemp) ;
        }
        return signPartShowListTemp;
    }

    public List<EsInitCust> selectListByModel() {
        EsInitCustExample example= new EsInitCustExample();
        EsInitCustExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("ID ASC") ;
        return esInitCustMapper.selectByExample(example);
    }

    public List<EsInitCust> getSignPartById(String strId){
        EsInitCustExample example= new EsInitCustExample();
        example .createCriteria().andIdLike("%" + strId + "%") ;
        return esInitCustMapper.selectByExample(example);
    }

    private String stringIgnoreNull(String strOriginal){
        if(strOriginal ==null){
            return "";
        }
        else {
            return strOriginal;
        }
    }

    public EsInitCust getEsInitCustByPkid(String strPkid){
        return esInitCustMapper .selectByPrimaryKey(strPkid) ;
    }

    public boolean isExistInDb(SignPartShow signPartShowPara) {
        EsInitCustExample example = new EsInitCustExample();
        example.createCriteria().andIdEqualTo(signPartShowPara.getId());
        return esInitCustMapper .countByExample(example) >= 1;
    }

    public String getMaxId(){
        return signPartMapper.strMaxCustId() ;
    }

    public void insertRecord(SignPartShow signPartShowPara) {
        signPartShowPara.setDeletedFlag("0");
        signPartShowPara.setOriginFlag("0");
        signPartShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        signPartShowPara.setCreatedDate(platformService.getStrLastUpdDate());
        signPartShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        signPartShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitCustMapper.insertSelective(fromShowToModel(signPartShowPara));
    }

    private EsInitCust fromShowToModel(SignPartShow signPartShowPara) {
        EsInitCust esInitCustTemp = new EsInitCust();
        esInitCustTemp.setPkid(signPartShowPara.getPkid());
        esInitCustTemp.setId(signPartShowPara.getId());
        esInitCustTemp.setName(signPartShowPara.getName());
        esInitCustTemp.setMobilephone(signPartShowPara.getMobilephone());
        esInitCustTemp.setEmail(signPartShowPara.getEmail());
        esInitCustTemp.setOperphone(signPartShowPara.getOperphone());
        esInitCustTemp.setOtherphone(signPartShowPara.getOtherphone());
        esInitCustTemp.setFax(signPartShowPara.getFax());
        esInitCustTemp.setDeletedFlag(signPartShowPara.getDeletedFlag());
        esInitCustTemp.setOriginFlag(signPartShowPara.getOriginFlag());
        esInitCustTemp.setCreatedBy(signPartShowPara.getCreatedBy());
        esInitCustTemp.setCreatedDate(signPartShowPara.getCreatedDate()) ;
        esInitCustTemp.setLastUpdBy(signPartShowPara.getLastUpdBy());
        esInitCustTemp.setLastUpdDate(signPartShowPara.getLastUpdDate());
        esInitCustTemp.setModificationNum(signPartShowPara.getModificationNum());
        esInitCustTemp.setNote(signPartShowPara.getNote());
        return esInitCustTemp;
    }
    private SignPartShow fromModelToShow(EsInitCust esInitCustPara) {
        SignPartShow signPartShowTemp = new SignPartShow();
        signPartShowTemp.setPkid(esInitCustPara.getPkid());
        signPartShowTemp.setId(esInitCustPara.getId());
        signPartShowTemp.setName(esInitCustPara.getName());
        signPartShowTemp.setMobilephone(esInitCustPara.getMobilephone());
        signPartShowTemp.setEmail(esInitCustPara.getEmail());
        signPartShowTemp.setOperphone(esInitCustPara.getOperphone());
        signPartShowTemp.setOtherphone(esInitCustPara.getOtherphone());
        signPartShowTemp.setFax(esInitCustPara.getFax());
        signPartShowTemp.setDeletedFlag(esInitCustPara.getDeletedFlag());
        signPartShowTemp.setOriginFlag(esInitCustPara.getOriginFlag());
        signPartShowTemp.setCreatedBy(esInitCustPara.getCreatedBy());
        signPartShowTemp.setCreatedDate(esInitCustPara.getCreatedDate()) ;
        signPartShowTemp.setLastUpdBy(esInitCustPara.getLastUpdBy());
        signPartShowTemp.setLastUpdDate(esInitCustPara.getLastUpdDate());
        signPartShowTemp.setModificationNum(esInitCustPara.getModificationNum());
        signPartShowTemp.setNote(esInitCustPara.getNote());
        return signPartShowTemp;
    }


    public void updateRecord(SignPartShow signPartShowPara){
        signPartShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(signPartShowPara.getModificationNum())+1);
        signPartShowPara.setDeletedFlag("0");
        signPartShowPara.setOriginFlag("0");
        signPartShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        signPartShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitCustMapper.updateByPrimaryKey(fromShowToModel(signPartShowPara));
    }

    public int deleteRecord(String strPkId){
        return esInitCustMapper.deleteByPrimaryKey(strPkId);
    }
}
