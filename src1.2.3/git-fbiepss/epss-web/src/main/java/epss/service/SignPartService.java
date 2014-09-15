package epss.service;

import epss.repository.dao.SignPartMapper;
import epss.repository.model.SignPartExample;
import skyline.util.ToolUtil;
import epss.repository.dao.not_mybatis.MySignPartMapper;
import epss.repository.model.SignPart;
import epss.repository.model.model_show.SignPartShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MySignPartMapper mySignPartMapper;
    @Autowired
    private SignPartMapper signPartMapper;

    public List<SignPartShow> selectListByModel(SignPartShow signPartShowPara) {
        SignPartExample example= new SignPartExample();
        SignPartExample.Criteria criteria = example.createCriteria();
        criteria.andIdLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getName()) + "%")
                .andArchivedFlagLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getDeletedFlag()) + "%");
        if (!ToolUtil.getStrIgnoreNull(signPartShowPara.getNote()).equals("")){
            criteria.andNoteLike("%"+signPartShowPara .getNote()+"%");
        }
        example.setOrderByClause("ID ASC") ;
        List<SignPart> signPartListTemp = signPartMapper.selectByExample(example);
        List<SignPartShow> signPartShowListTemp=new ArrayList<>();
        for(SignPart itemUnit: signPartListTemp){
            SignPartShow signPartShowTemp=new SignPartShow();
            signPartShowTemp=fromModelToShow(itemUnit);
            signPartShowTemp.setCreatedByName(ToolUtil.getUserName(itemUnit.getCreatedBy()));
            signPartShowTemp.setLastUpdByName(ToolUtil.getUserName(itemUnit.getLastUpdBy()));
            signPartShowListTemp.add(signPartShowTemp) ;
        }
        return signPartShowListTemp;
    }

    public List<SignPart> selectListByModel() {
        SignPartExample example= new SignPartExample();
        SignPartExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("ID ASC") ;
        return signPartMapper.selectByExample(example);
    }

    public List<SignPart> getSignPartById(String strId){
        SignPartExample example= new SignPartExample();
        example .createCriteria().andIdLike("%" + strId + "%") ;
        return signPartMapper.selectByExample(example);
    }

    public SignPart getEsInitCustByPkid(String strPkid){
        return signPartMapper.selectByPrimaryKey(strPkid) ;
    }

    public boolean isExistInDb(SignPartShow signPartShowPara) {
        SignPartExample example = new SignPartExample();
        example.createCriteria().andIdEqualTo(signPartShowPara.getId());
        return signPartMapper.countByExample(example) >= 1;
    }

    public String getMaxId(){
        return mySignPartMapper.strMaxCustId() ;
    }

    public void insertRecord(SignPartShow signPartShowPara) {
        signPartShowPara.setDeletedFlag("0");
        signPartShowPara.setOriginFlag("0");
        signPartShowPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        signPartShowPara.setCreatedDate(ToolUtil.getStrLastUpdDate());
        signPartShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        signPartShowPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        signPartMapper.insertSelective(fromShowToModel(signPartShowPara));
    }

    private SignPart fromShowToModel(SignPartShow signPartShowPara) {
        SignPart signPartTemp = new SignPart();
        signPartTemp.setPkid(signPartShowPara.getPkid());
        signPartTemp.setId(signPartShowPara.getId());
        signPartTemp.setName(signPartShowPara.getName());
        signPartTemp.setMobilephone(signPartShowPara.getMobilephone());
        signPartTemp.setEmail(signPartShowPara.getEmail());
        signPartTemp.setOperphone(signPartShowPara.getOperphone());
        signPartTemp.setOtherphone(signPartShowPara.getOtherphone());
        signPartTemp.setFax(signPartShowPara.getFax());
        signPartTemp.setArchivedFlag(signPartShowPara.getDeletedFlag());
        signPartTemp.setOriginFlag(signPartShowPara.getOriginFlag());
        signPartTemp.setCreatedBy(signPartShowPara.getCreatedBy());
        signPartTemp.setCreatedDate(signPartShowPara.getCreatedDate()) ;
        signPartTemp.setLastUpdBy(signPartShowPara.getLastUpdBy());
        signPartTemp.setLastUpdDate(signPartShowPara.getLastUpdDate());
        signPartTemp.setModificationNum(signPartShowPara.getModificationNum());
        signPartTemp.setNote(signPartShowPara.getNote());
        return signPartTemp;
    }
    private SignPartShow fromModelToShow(SignPart signPartPara) {
        SignPartShow signPartShowTemp = new SignPartShow();
        signPartShowTemp.setPkid(signPartPara.getPkid());
        signPartShowTemp.setId(signPartPara.getId());
        signPartShowTemp.setName(signPartPara.getName());
        signPartShowTemp.setMobilephone(signPartPara.getMobilephone());
        signPartShowTemp.setEmail(signPartPara.getEmail());
        signPartShowTemp.setOperphone(signPartPara.getOperphone());
        signPartShowTemp.setOtherphone(signPartPara.getOtherphone());
        signPartShowTemp.setFax(signPartPara.getFax());
        signPartShowTemp.setDeletedFlag(signPartPara.getArchivedFlag());
        signPartShowTemp.setOriginFlag(signPartPara.getOriginFlag());
        signPartShowTemp.setCreatedBy(signPartPara.getCreatedBy());
        signPartShowTemp.setCreatedDate(signPartPara.getCreatedDate()) ;
        signPartShowTemp.setLastUpdBy(signPartPara.getLastUpdBy());
        signPartShowTemp.setLastUpdDate(signPartPara.getLastUpdDate());
        signPartShowTemp.setModificationNum(signPartPara.getModificationNum());
        signPartShowTemp.setNote(signPartPara.getNote());
        return signPartShowTemp;
    }


    public void updateRecord(SignPartShow signPartShowPara){
        signPartShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(signPartShowPara.getModificationNum())+1);
        signPartShowPara.setDeletedFlag("0");
        signPartShowPara.setOriginFlag("0");
        signPartShowPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        signPartShowPara.setLastUpdDate(ToolUtil.getStrLastUpdDate());
        signPartMapper.updateByPrimaryKey(fromShowToModel(signPartShowPara));
    }

    public int deleteRecord(String strPkId){
        return signPartMapper.deleteByPrimaryKey(strPkId);
    }
}
