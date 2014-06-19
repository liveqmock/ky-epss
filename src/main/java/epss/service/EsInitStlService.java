package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.model.model_show.ProgInfoShow;
import epss.repository.dao.EsInitStlMapper;
import epss.repository.dao.common.CommonMapper;
import epss.repository.model.*;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-20
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsInitStlService {
    @Resource
    private EsInitStlMapper esInitStlMapper;
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private EsInitPowerService esInitPowerService;
    @Resource
    private PlatformService platformService;

    /**
     * 判断记录是否已存在
     *
     * @param progInfoShowPara
     * @return
     */
    public List<EsInitStl> getExistedEsInitStlSubcttEngInDb(ProgInfoShow progInfoShowPara) {
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        return esInitStlMapper.selectByExample(example);
    }

    private EsInitStl fromConstructToModel(ProgInfoShow progInfoShowPara){
        EsInitStl esInitStlTemp =new EsInitStl();
        esInitStlTemp.setPkid(progInfoShowPara.getPkid());
        esInitStlTemp.setStlType(progInfoShowPara.getStlType());
        esInitStlTemp.setStlPkid(progInfoShowPara.getStlPkid());
        esInitStlTemp.setId(progInfoShowPara.getId());
        esInitStlTemp.setPeriodNo(progInfoShowPara.getPeriodNo());
        esInitStlTemp.setNote(progInfoShowPara.getNote());
        esInitStlTemp.setAttachment(progInfoShowPara.getAttachment());
        esInitStlTemp.setDeletedFlag(progInfoShowPara.getDeletedFlag());
        esInitStlTemp.setEndFlag(progInfoShowPara.getEndFlag());
        esInitStlTemp.setCreatedBy(progInfoShowPara.getCreatedBy());
        esInitStlTemp.setCreatedDate(progInfoShowPara.getCreatedDate());
        esInitStlTemp.setLastUpdBy(progInfoShowPara.getLastUpdBy());
        esInitStlTemp.setLastUpdDate(progInfoShowPara.getLastUpdDate());
        esInitStlTemp.setModificationNum(progInfoShowPara.getModificationNum());
        return esInitStlTemp;
    }

    public EsInitStl selectRecordsByPrimaryKey(String strPkId){
        return esInitStlMapper.selectByPrimaryKey(strPkId);
    }
    public void insertRecord(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setCreatedDate(platformService.getStrLastUpdDate());
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.insert(fromConstructToModel(progInfoShowPara)) ;
    }
    public void insertRecord(EsInitStl esInitStlPara){
        esInitStlPara.setCreatedBy(platformService.getStrLastUpdBy());
        esInitStlPara.setCreatedDate(platformService.getStrLastUpdDate());
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitStlPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.insert(esInitStlPara) ;
    }
    public void updateRecord(ProgInfoShow progInfoShowPara){
        progInfoShowPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(progInfoShowPara.getModificationNum())+1);
        progInfoShowPara.setDeletedFlag("0");
        progInfoShowPara.setLastUpdBy(platformService.getStrLastUpdBy());
        progInfoShowPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(fromConstructToModel(progInfoShowPara)) ;
    }
    public void updateRecord(EsInitStl esInitStlPara){
        esInitStlPara.setModificationNum(
                ToolUtil.getIntIgnoreNull(esInitStlPara.getModificationNum())+1);
        esInitStlPara.setDeletedFlag("0");
        esInitStlPara.setLastUpdBy(platformService.getStrLastUpdBy());
        esInitStlPara.setLastUpdDate(platformService.getStrLastUpdDate());
        esInitStlMapper.updateByPrimaryKey(esInitStlPara) ;
    }
    public void deleteRecord(ProgInfoShow progInfoShowPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria().andStlTypeEqualTo(progInfoShowPara.getStlType())
                .andStlPkidEqualTo(progInfoShowPara.getStlPkid())
                .andPeriodNoEqualTo(progInfoShowPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);

        esInitPowerService.deleteRecord(
                progInfoShowPara.getStlType()
                , progInfoShowPara.getStlPkid()
                , progInfoShowPara.getPeriodNo());
    }
    public void deleteRecord(EsInitStl esInitStlPara){
        EsInitStlExample example = new EsInitStlExample();
        example.createCriteria()
                .andStlTypeEqualTo(esInitStlPara.getStlType())
                .andStlPkidEqualTo(esInitStlPara.getStlPkid())
                .andPeriodNoEqualTo(esInitStlPara.getPeriodNo());
        esInitStlMapper.deleteByExample(example);

        esInitPowerService.deleteRecord(
                esInitStlPara.getStlType()
                , esInitStlPara.getStlPkid()
                , esInitStlPara.getPeriodNo());

    }
    public int deleteRecord(String strPkId){
        return esInitStlMapper.deleteByPrimaryKey(strPkId);
    }

    public String getStrMaxStlId(String strCttType){
        return commonMapper.getStrMaxStlId(strCttType) ;
    }
}
