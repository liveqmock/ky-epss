package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.SignPartMapper;
import epss.repository.dao.notMyBits.MySignPartMapper;
import epss.repository.model.SignPart;
import epss.repository.model.SignPartExample;
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
    private MySignPartMapper mySignPartMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private EsCommonService esCommonService;

    public List<SignPartShow> selectListByModel(SignPartShow signPartShowPara) {
        SignPartExample example = new SignPartExample();
        SignPartExample.Criteria criteria = example.createCriteria();
        criteria.andIdLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getId()) + "%")
                .andNameLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getName()) + "%")
                .andArchivedFlagLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getArchivedFlag()) + "%");

        if (!ToolUtil.getStrIgnoreNull(signPartShowPara.getMobilephone()).equals("")) {
            criteria.andMobilephoneLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getMobilephone()) + "%");
        }
        if (!ToolUtil.getStrIgnoreNull(signPartShowPara.getOperphone()).equals("")) {
            criteria.andOperphoneLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getOperphone()) + "%");
        }
        if (!ToolUtil.getStrIgnoreNull(signPartShowPara.getEmail()).equals("")) {
            criteria.andEmailLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getEmail()) + "%");
        }
        if (!ToolUtil.getStrIgnoreNull(signPartShowPara.getFax()).equals("")) {
            criteria.andFaxLike("%" + ToolUtil.getStrIgnoreNull(signPartShowPara.getFax()) + "%");
        }
        example.setOrderByClause("ID ASC");
        List<SignPart> signPartListTemp = signPartMapper.selectByExample(example);
        List<SignPartShow> signPartShowListTemp = new ArrayList<>();
        for (SignPart itemUnit : signPartListTemp) {
            SignPartShow signPartShowTemp = fromModelToShow(itemUnit);
            signPartShowTemp.setCreatedByName(esCommonService.getOperNameByOperId(itemUnit.getCreatedBy()));
            signPartShowTemp.setUpdatedByName(esCommonService.getOperNameByOperId(itemUnit.getUpdatedBy()));
            signPartShowListTemp.add(signPartShowTemp);
        }
        return signPartShowListTemp;
    }

    public List<SignPart> selectListByModel() {
        SignPartExample example = new SignPartExample();
        SignPartExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("ID ASC");
        return signPartMapper.selectByExample(example);
    }

    public List<SignPart> getSignPartById(String strId) {
        SignPartExample example = new SignPartExample();
        example.createCriteria().andIdLike("%" + strId + "%");
        return signPartMapper.selectByExample(example);
    }

    public SignPart getEsInitCustByPkid(String strPkid) {
        return signPartMapper.selectByPrimaryKey(strPkid);
    }

    public boolean isExistInDb(SignPartShow signPartShowPara) {
        SignPartExample example = new SignPartExample();
        example.createCriteria().andIdEqualTo(signPartShowPara.getId());
        return signPartMapper.countByExample(example) >= 1;
    }

    public String getStrMaxSignPartId() {
        return mySignPartMapper.getStrMaxSignPartId();
    }

    public void insertRecord(SignPartShow signPartShowPara) {
        signPartShowPara.setArchivedFlag("0");
        signPartShowPara.setOriginFlag("0");
        signPartShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        signPartShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        signPartShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        signPartShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
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
        signPartTemp.setArchivedFlag(signPartShowPara.getArchivedFlag());
        signPartTemp.setOriginFlag(signPartShowPara.getOriginFlag());
        signPartTemp.setCreatedBy(signPartShowPara.getCreatedBy());
        signPartTemp.setCreatedTime(signPartShowPara.getCreatedTime());
        signPartTemp.setUpdatedBy(signPartShowPara.getUpdatedBy());
        signPartTemp.setUpdatedTime(signPartShowPara.getUpdatedTime());
        signPartTemp.setRecVersion(signPartShowPara.getRecVersion());
        signPartTemp.setRemark(signPartShowPara.getRemark());
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
        signPartShowTemp.setArchivedFlag(signPartPara.getArchivedFlag());
        signPartShowTemp.setOriginFlag(signPartPara.getOriginFlag());
        signPartShowTemp.setCreatedBy(signPartPara.getCreatedBy());
        signPartShowTemp.setCreatedTime(signPartPara.getCreatedTime());
        signPartShowTemp.setUpdatedBy(signPartPara.getUpdatedBy());
        signPartShowTemp.setUpdatedTime(signPartPara.getUpdatedTime());
        signPartShowTemp.setRecVersion(signPartPara.getRecVersion());
        signPartShowTemp.setRemark(signPartPara.getRemark());
        return signPartShowTemp;
    }

    public void updateRecord(SignPartShow signPartShowPara) {
        signPartShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(signPartShowPara.getRecVersion()) + 1);
        signPartShowPara.setArchivedFlag("0");
        signPartShowPara.setOriginFlag("0");
        signPartShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        signPartShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        signPartMapper.updateByPrimaryKey(fromShowToModel(signPartShowPara));
    }

    public int deleteRecord(String strPkId) {
        return signPartMapper.deleteByPrimaryKey(strPkId);
    }
}
