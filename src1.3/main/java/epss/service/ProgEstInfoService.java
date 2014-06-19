package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.ProgEstInfoMapper;
import epss.repository.dao.notMyBits.CommonMapper;
import epss.repository.dao.notMyBits.MyProgEstInfoMapper;
import epss.repository.model.ProgEstInfo;
import epss.repository.model.ProgEstInfoExample;
import epss.repository.model.model_show.ProgEstInfoShow;
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
public class ProgEstInfoService {
    @Resource
    private ProgEstInfoMapper progEstInfoMapper;
    @Resource
    private MyProgEstInfoMapper myProgEstInfoMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private CommonMapper commonMapper;

    /**
     * 判断记录是否已存在
     *
     * @param progEstInfoShowPara
     * @return
     */
    public List<ProgEstInfo> getProgEstInfoListByModel(ProgEstInfoShow progEstInfoShowPara) {
        ProgEstInfoExample example = new ProgEstInfoExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(progEstInfoShowPara.getTkcttInfoPkid())
                                .andStageNoEqualTo(progEstInfoShowPara.getStageNo());
        return progEstInfoMapper.selectByExample(example);
    }
    public List<ProgEstInfo> getProgEstInfoListByModel(ProgEstInfo progEstInfoPara) {
        ProgEstInfoExample example = new ProgEstInfoExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(progEstInfoPara.getTkcttInfoPkid())
                .andStageNoEqualTo(progEstInfoPara.getStageNo());
        return progEstInfoMapper.selectByExample(example);
    }
    public List<ProgEstInfoShow> getProgEstInfoListByFlowStatusBegin_End(ProgEstInfoShow progEstInfoShowPara){
        return myProgEstInfoMapper.getProgEstInfoListByFlowStatusBegin_End(progEstInfoShowPara);
    }

    public String getStrMaxProgEstInfoId(){
        return myProgEstInfoMapper.getStrMaxProgEstInfoId();
    }

    public String getLatestStageNo(String strSubcttInfoPkidPara,String strFlowStatusPara) {
        return myProgEstInfoMapper.getLatestStageNo(strSubcttInfoPkidPara,strFlowStatusPara);
    }

    public String getLatestStageNoByEndStage(String strSubcttInfoPkidPara,
                                             String strEndStageNoPara,
                                             String strFlowStatusPara) {
        return myProgEstInfoMapper.getLatestStageNoByEndStage(
                strSubcttInfoPkidPara,
                strEndStageNoPara,
                strFlowStatusPara);
    }
    private ProgEstInfo fromShowModelToModel(ProgEstInfoShow progEstInfoShowPara){
        ProgEstInfo progEstInfoTemp =new ProgEstInfo();
        progEstInfoTemp.setPkid(progEstInfoShowPara.getPkid());
        progEstInfoTemp.setId(progEstInfoShowPara.getId());
        progEstInfoTemp.setTkcttInfoPkid(progEstInfoShowPara.getTkcttInfoPkid());
        progEstInfoTemp.setStageNo(progEstInfoShowPara.getStageNo());
        progEstInfoTemp.setArchivedFlag(progEstInfoShowPara.getArchivedFlag());
        progEstInfoTemp.setOriginFlag(progEstInfoShowPara.getOriginFlag());
        progEstInfoTemp.setFlowStatus(progEstInfoShowPara.getFlowStatus());
        progEstInfoTemp.setFlowStatusRemark(progEstInfoShowPara.getFlowStatusRemark());
        progEstInfoTemp.setCreatedBy(progEstInfoShowPara.getCreatedBy());
        progEstInfoTemp.setCreatedTime(progEstInfoShowPara.getCreatedTime());
        progEstInfoTemp.setUpdatedBy(progEstInfoShowPara.getUpdatedBy());
        progEstInfoTemp.setUpdatedTime(progEstInfoShowPara.getUpdatedTime());
        progEstInfoTemp.setRecVersion(progEstInfoShowPara.getRecVersion());
        progEstInfoTemp.setAttachment(progEstInfoShowPara.getAttachment());
        progEstInfoTemp.setRemark(progEstInfoShowPara.getRemark());
        progEstInfoTemp.setTid(progEstInfoShowPara.getTid());
        return progEstInfoTemp;
    }

    public ProgEstInfo selectRecordsByPrimaryKey(String strPkId){
        return progEstInfoMapper.selectByPrimaryKey(strPkId);
    }
    public void insertRecord(ProgEstInfoShow progEstInfoShowPara){
        progEstInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setCreatedTime(platformService.getStrLastUpdTime());
        progEstInfoShowPara.setArchivedFlag("0");
        progEstInfoMapper.insert(fromShowModelToModel(progEstInfoShowPara));
    }
    public void updateRecord(ProgEstInfoShow progEstInfoShowPara){
        progEstInfoShowPara.setRecVersion(
                ToolUtil.getIntIgnoreNull(progEstInfoShowPara.getRecVersion()) + 1);
        progEstInfoShowPara.setArchivedFlag("0");
        progEstInfoShowPara.setUpdatedBy(platformService.getStrLastUpdBy());
        progEstInfoShowPara.setUpdatedByName(commonMapper.selectOpernameByCreatedBy(platformService.getStrLastUpdBy()));
        progEstInfoShowPara.setUpdatedTime(platformService.getStrLastUpdTime());
        progEstInfoMapper.updateByPrimaryKey(fromShowModelToModel(progEstInfoShowPara)) ;
    }
    public void deleteRecord(ProgEstInfoShow progEstInfoShowPara){
        ProgEstInfoExample example = new ProgEstInfoExample();
        example.createCriteria().andTkcttInfoPkidEqualTo(progEstInfoShowPara.getTkcttInfoPkid())
                                .andStageNoEqualTo(progEstInfoShowPara.getStageNo());
        progEstInfoMapper.deleteByExample(example);
    }

    public int deleteRecord(String strPkId){
        return progEstInfoMapper.deleteByPrimaryKey(strPkId);
    }

    public String getMaxStageNo(String tkCttInfoPkid) {
        return myProgEstInfoMapper.getMaxStageNo(tkCttInfoPkid);
    }

    public String getFlowStatus(String tkCttInfoPkid,String strStaQtyMaxStageNo){
        return myProgEstInfoMapper.getFlowStatus(tkCttInfoPkid,strStaQtyMaxStageNo);
    }
}
