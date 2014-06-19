package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgMeaInfoMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.dao.notMyBits.MyProgMeaInfoMapper;
import epss.repository.model.ProgMeaInfo;
import epss.repository.model.ProgMeaInfoExample;
import epss.repository.model.model_show.ProgMeaInfoShow;
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
public class ProgMeaInfoService {
    @Resource
    private ProgMeaInfoMapper progMeaInfoMapper;
    @Resource
    private MyProgMeaInfoMapper myProgMeaInfoMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    /**
     * 判断记录是否已存在
     *
     * @param progMeaInfoShowPara
     * @return
     */
    public List<ProgMeaInfo> getProgMeaInfoListByModel(ProgMeaInfoShow progMeaInfoShowPara) {
        ProgMeaInfoExample example = new ProgMeaInfoExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(progMeaInfoShowPara.getTkcttInfoPkid())
                                .andStageNoEqualTo(progMeaInfoShowPara.getStageNo());
        return progMeaInfoMapper.selectByExample(example);
    }
    public List<ProgMeaInfo> getProgMeaInfoListByModel(ProgMeaInfo progMeaInfoPara) {
        ProgMeaInfoExample example = new ProgMeaInfoExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(progMeaInfoPara.getTkcttInfoPkid())
                .andStageNoEqualTo(progMeaInfoPara.getStageNo());
        return progMeaInfoMapper.selectByExample(example);
    }

    public List<ProgMeaInfoShow> getProgMeaInfoListByFlowStatusBegin_End(ProgMeaInfoShow progMeaInfoShowPara){
        return myProgMeaInfoMapper.getProgMeaInfoListByFlowStatusBegin_End(progMeaInfoShowPara);
    }

    public String getStrMaxProgMeaInfoId(){
        return myProgMeaInfoMapper.getStrMaxProgMeaInfoId();
    }

    public String getLatestStageNo(String strSubcttInfoPkidPara,String strFlowStatusPara) {
        return myProgMeaInfoMapper.getLatestStageNo(strSubcttInfoPkidPara,strFlowStatusPara);
    }

    public String getLatestStageNoByEndStage(String strSubcttInfoPkidPara,
                                             String strEndStageNoPara,
                                             String strFlowStatusPara) {
        return myProgMeaInfoMapper.getLatestStageNoByEndStage(
                strSubcttInfoPkidPara,
                strEndStageNoPara,
                strFlowStatusPara);
    }
    private ProgMeaInfo fromShowModelToModel(ProgMeaInfoShow progMeaInfoShowPara){
        ProgMeaInfo progMeaInfoTemp =new ProgMeaInfo();
        progMeaInfoTemp.setPkid(progMeaInfoShowPara.getPkid());
        progMeaInfoTemp.setId(progMeaInfoShowPara.getId());
        progMeaInfoTemp.setTkcttInfoPkid(progMeaInfoShowPara.getTkcttInfoPkid());
        progMeaInfoTemp.setStageNo(progMeaInfoShowPara.getStageNo());
        progMeaInfoTemp.setArchivedFlag(progMeaInfoShowPara.getArchivedFlag());
        progMeaInfoTemp.setOriginFlag(progMeaInfoShowPara.getOriginFlag());
        progMeaInfoTemp.setFlowStatus(progMeaInfoShowPara.getFlowStatus());
        progMeaInfoTemp.setFlowStatusRemark(progMeaInfoShowPara.getFlowStatusRemark());
        progMeaInfoTemp.setCreatedBy(progMeaInfoShowPara.getCreatedBy());
        progMeaInfoTemp.setCreatedTime(progMeaInfoShowPara.getCreatedTime());
        progMeaInfoTemp.setUpdatedBy(progMeaInfoShowPara.getUpdatedBy());
        progMeaInfoTemp.setUpdatedTime(progMeaInfoShowPara.getUpdatedTime());
        progMeaInfoTemp.setRecVersion(progMeaInfoShowPara.getRecVersion());
        progMeaInfoTemp.setAttachment(progMeaInfoShowPara.getAttachment());
        progMeaInfoTemp.setRemark(progMeaInfoShowPara.getRemark());
        progMeaInfoTemp.setTid(progMeaInfoShowPara.getTid());
        return progMeaInfoTemp;
    }

    public ProgMeaInfo selectRecordsByPrimaryKey(String strPkId){
        return progMeaInfoMapper.selectByPrimaryKey(strPkId);
    }
    public void insertRecord(ProgMeaInfoShow progMeaInfoShowPara){
        progMeaInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progMeaInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progMeaInfoShowPara.setArchivedFlag("0");
        progMeaInfoMapper.insert(fromShowModelToModel(progMeaInfoShowPara)) ;
    }
    public void updateRecord(ProgMeaInfoShow progMeaInfoShowPara){
        progMeaInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progMeaInfoShowPara.getRecVersion()) + 1);
        progMeaInfoShowPara.setArchivedFlag("0");
        progMeaInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progMeaInfoShowPara.setUpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progMeaInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progMeaInfoMapper.updateByPrimaryKey(fromShowModelToModel(progMeaInfoShowPara)) ;
    }
    public void deleteRecord(ProgMeaInfoShow progMeaInfoShowPara){
        ProgMeaInfoExample example = new ProgMeaInfoExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(progMeaInfoShowPara.getTkcttInfoPkid())
                                .andStageNoEqualTo(progMeaInfoShowPara.getStageNo());
        progMeaInfoMapper.deleteByExample(example);
    }

    public int deleteRecord(String strPkId){
        return progMeaInfoMapper.deleteByPrimaryKey(strPkId);
    }


    public String getMaxStageNo(String tkCttInfoPkid) {
        return myProgMeaInfoMapper.getMaxStageNo(tkCttInfoPkid);
    }

    public String getFlowStatus(String tkCttInfoPkid,String strMeaQtyMaxStageNo){
        return myProgMeaInfoMapper.getFlowStatus(tkCttInfoPkid,strMeaQtyMaxStageNo);
    }
}
