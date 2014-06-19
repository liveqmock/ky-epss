package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgSubstlInfoMapper;
import epss.repository.dao.notMyBits.MyProgSubstlInfoMapper;
import epss.repository.model.ProgSubstlInfo;
import epss.repository.model.ProgSubstlInfoExample;
import epss.repository.model.model_show.ProgSubstlInfoShow;
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
public class ProgSubstlInfoService {
    @Resource
    private ProgSubstlInfoMapper progSubstlInfoMapper;
    @Resource
    private MyProgSubstlInfoMapper myProgSubstlInfoMapper;
    @Resource
    private PlatformService platformService;

    /**
     * 判断记录是否已存在
     *
     * @param progEstInfoShowPara
     * @return
     */
    public List<ProgSubstlInfo> getExistedEsInitStlSubcttEngInDb(ProgSubstlInfoShow progEstInfoShowPara) {
        ProgSubstlInfoExample example = new ProgSubstlInfoExample();
        example.createCriteria().andSubcttInfoPkidEqualTo(progEstInfoShowPara.getSubcttInfoPkid())
                                .andStageNoEqualTo(progEstInfoShowPara.getStageNo());
        return progSubstlInfoMapper.selectByExample(example);
    }

    public List<ProgSubstlInfoShow> getProgSubstlInfoShowListByFlowStatusBegin_End(ProgSubstlInfoShow progSubstlInfoShowPara){
        return myProgSubstlInfoMapper.getProgSubstlInfoShowListByFlowStatusBegin_End(
                progSubstlInfoShowPara.getFlowStatusBegin(),
                progSubstlInfoShowPara.getFlowStatusEnd()
        );
    }

    public String getStrMaxProgSubstlInfoId(){
        return myProgSubstlInfoMapper.getStrMaxProgSubstlInfoId();
    }

    public List<ProgSubstlInfoShow> getNotFormProgSubstlInfoShowList(String strCstplInfoPkidPara,
                                                                      String strSubInfoPkidPara,
                                                                      String strStageNoPara){
        return myProgSubstlInfoMapper.getNotFormProgSubstlInfoShowList(
                strCstplInfoPkidPara, strSubInfoPkidPara, strStageNoPara);
    }
    public List<ProgSubstlInfoShow> getFormPreProgSubstlInfoShowList(String strCstplInfoPkidPara,
                                                                      String strSubInfoPkidPara,
                                                                      String strStageNoPara){
        return myProgSubstlInfoMapper.getFormPreProgSubstlInfoShowList(
                strCstplInfoPkidPara, strSubInfoPkidPara, strStageNoPara);
    }
    public List<ProgSubstlInfoShow> getFormingProgSubstlInfoShowList(String strCstplInfoPkidPara,
                                                                      String strSubInfoPkidPara,
                                                                      String strStageNoPara){
        return myProgSubstlInfoMapper.getFormingProgSubstlInfoShowList(
                strCstplInfoPkidPara, strSubInfoPkidPara, strStageNoPara);
    }
    public List<ProgSubstlInfoShow> getFormedProgSubstlInfoShowList(String strCstplInfoPkidPara,
                                                                     String strSubInfoPkidPara,
                                                                     String strStageNoPara){
        return myProgSubstlInfoMapper.getFormedProgSubstlInfoShowList(
                strCstplInfoPkidPara, strSubInfoPkidPara, strStageNoPara);
    }

    private ProgSubstlInfo fromShowModelToModel(ProgSubstlInfoShow progEstInfoShowPara){
        ProgSubstlInfo progSubstlInfoTemp =new ProgSubstlInfo();
        progSubstlInfoTemp.setPkid(progEstInfoShowPara.getPkid());
        progSubstlInfoTemp.setSubcttInfoPkid(progEstInfoShowPara.getSubcttInfoPkid());
        progSubstlInfoTemp.setId(progEstInfoShowPara.getId());
        progSubstlInfoTemp.setStageNo(progEstInfoShowPara.getStageNo());
        progSubstlInfoTemp.setAttachment(progEstInfoShowPara.getAttachment());
        progSubstlInfoTemp.setArchivedFlag(progEstInfoShowPara.getArchivedFlag());
        progSubstlInfoTemp.setOriginFlag(progEstInfoShowPara.getOriginFlag());
        progSubstlInfoTemp.setCreatedBy(progEstInfoShowPara.getCreatedBy());
        progSubstlInfoTemp.setCreatedTime(progEstInfoShowPara.getCreatedTime());
        progSubstlInfoTemp.setUpdatedBy(progEstInfoShowPara.getUpdatedBy());
        progSubstlInfoTemp.setUpdatedTime(progEstInfoShowPara.getUpdatedTime());
        progSubstlInfoTemp.setRecVersion(progEstInfoShowPara.getRecVersion());
        progSubstlInfoTemp.setRemark(progEstInfoShowPara.getRemark());
        return progSubstlInfoTemp;
    }

    public ProgSubstlInfo selectRecordsByPrimaryKey(String strPkId){
        return progSubstlInfoMapper.selectByPrimaryKey(strPkId);
    }
    public void insertRecord(ProgSubstlInfoShow progEstInfoShowPara){
        progEstInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progEstInfoShowPara.setArchivedFlag("0");
        progEstInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progSubstlInfoMapper.insert(fromShowModelToModel(progEstInfoShowPara)) ;
    }
    public void updateRecord(ProgSubstlInfoShow progEstInfoShowPara){
        progEstInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progEstInfoShowPara.getRecVersion()) + 1);
        progEstInfoShowPara.setArchivedFlag("0");
        progEstInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progSubstlInfoMapper.updateByPrimaryKey(fromShowModelToModel(progEstInfoShowPara)) ;
    }
    public void deleteRecord(ProgSubstlInfoShow progEstInfoShowPara){
        ProgSubstlInfoExample example = new ProgSubstlInfoExample();
        example.createCriteria()
                .andSubcttInfoPkidEqualTo(progEstInfoShowPara.getSubcttInfoPkid())
                .andStageNoEqualTo(progEstInfoShowPara.getStageNo());
        progSubstlInfoMapper.deleteByExample(example);
    }

    public int deleteRecord(String strPkId){
        return progSubstlInfoMapper.deleteByPrimaryKey(strPkId);
    }
}
