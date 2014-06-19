package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgSubstlItemMapper;
import epss.repository.model.ProgSubstlItem;
import epss.repository.model.ProgSubstlItemExample;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: ÉÏÎç10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ProgSubstlItemService {
    @Resource
    private ProgSubstlItemMapper progSubstlItemMapper;
    @Resource
    private PlatformService platformService;

    public ProgSubstlItem selectRecordsByDetailPrimaryKey(String strPkId){
        return progSubstlItemMapper.selectByPrimaryKey(strPkId);
    }

     public List<ProgSubstlItem> selectRecordsByDetailExample(ProgSubstlItem progSubstlItemPara){
        ProgSubstlItemExample example = new ProgSubstlItemExample();
        example.createCriteria().andProgSubstlInfoPkidEqualTo(progSubstlItemPara.getProgSubstlInfoPkid());
        return progSubstlItemMapper.selectByExample(example);
    }

    public void deleteRecordDetail(String strPkId){
        progSubstlItemMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecordDetail(ProgSubstlItem progSubstlItemPara){
        progSubstlItemPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progSubstlItemPara.getRecVersion())+1);
        progSubstlItemPara.setArchivedFlag("0");
        progSubstlItemPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progSubstlItemPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progSubstlItemMapper.updateByPrimaryKey(progSubstlItemPara) ;
    }

    public void insertRecordDetail(ProgSubstlItem progSubstlItemPara){
        progSubstlItemPara.setCreatedBy(platformService.getStrLastUpdBy());
        progSubstlItemPara.setCreatedTime(platformService.getStrLastUpdTime());
        progSubstlItemPara.setArchivedFlag("0");
        progSubstlItemPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progSubstlItemPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progSubstlItemMapper.insert(progSubstlItemPara) ;
    }
}
