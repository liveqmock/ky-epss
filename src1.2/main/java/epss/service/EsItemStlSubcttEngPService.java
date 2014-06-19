package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.EsItemStlSubcttEngPMapper;
import epss.repository.model.EsItemStlSubcttEngP;
import epss.repository.model.EsItemStlSubcttEngPExample;
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
public class EsItemStlSubcttEngPService {
    @Resource
    private EsItemStlSubcttEngPMapper esItemStlSubcttEngPMapper;
    @Resource
    private PlatformService platformService;

    public EsItemStlSubcttEngP selectRecordsByDetailPrimaryKey(String strPkId){
        return esItemStlSubcttEngPMapper.selectByPrimaryKey(strPkId);
    }

    public List<EsItemStlSubcttEngP> selectRecordsByDetailExample(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        EsItemStlSubcttEngPExample example = new EsItemStlSubcttEngPExample();
        example.createCriteria().andPeriodNoEqualTo(esItemStlSubcttEngPPara.getPeriodNo());
        return esItemStlSubcttEngPMapper.selectByExample(example);
    }

    public void deleteRecordDetail(String strPkId){
        esItemStlSubcttEngPMapper.deleteByPrimaryKey(strPkId);
    }

    public void updateRecordDetail(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        esItemStlSubcttEngPPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(esItemStlSubcttEngPPara.getRecVersion())+1);
        esItemStlSubcttEngPPara.setArchivedFlag("0");
        esItemStlSubcttEngPPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngPPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngPMapper.updateByPrimaryKey(esItemStlSubcttEngPPara) ;
    }

    public void insertRecordDetail(EsItemStlSubcttEngP esItemStlSubcttEngPPara){
        esItemStlSubcttEngPPara.setCreatedBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngPPara.setCreatedDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngPPara.setArchivedFlag("0");
        esItemStlSubcttEngPPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esItemStlSubcttEngPPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esItemStlSubcttEngPMapper.insert(esItemStlSubcttEngPPara) ;
    }

    public void deleteRecordByExample(String strSubcttInfoPkidPara,String strPeriodNoPara){
        EsItemStlSubcttEngPExample example = new EsItemStlSubcttEngPExample();
        example.createCriteria()
                .andSubcttPkidEqualTo(strSubcttInfoPkidPara)
                .andPeriodNoEqualTo(strPeriodNoPara);
        esItemStlSubcttEngPMapper.deleteByExample(example);
    }
}
