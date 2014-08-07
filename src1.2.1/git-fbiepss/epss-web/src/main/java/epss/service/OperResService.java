package epss.service;

import epss.common.utils.ToolUtil;
import epss.repository.dao.EsCttInfoMapper;
import epss.repository.dao.OperResMapper;
import epss.repository.dao.not_mybatis.MyOperResMapper;
import epss.repository.model.EsCttInfo;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.CttAndStlInfoShow;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.OperResShow;
import org.springframework.stereotype.Service;
import skyline.service.PlatformService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class OperResService {
    @Resource
    private MyOperResMapper myOperResMapper;
    @Resource
    private OperResMapper operResMapper;
    @Resource
    private PlatformService platformService;
    @Resource
    private EsCttInfoMapper esCttInfoMapper;

    public List<OperResShow> selectOperaRoleRecords(String parentDeptid){
        return myOperResMapper.selectOperaRoleRecords(parentDeptid);
    }

    public List<OperResShow> selectOperaResRecords(){
        return myOperResMapper.selectOperaResRecords();
    }

    public List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(operResShowPara);
    }
    public void save(OperResShow record){
        operResMapper.insert(fromOperShowToModel(record));
    }
    private OperRes fromOperShowToModel(OperResShow record) {
        OperRes operResPara=new OperRes();
        operResPara.setTid(record.getTid());
        operResPara.setOperPkid(record.getOperPkid());
        operResPara.setFlowStatus(record.getFlowStatus());
        operResPara.setInfoType(record.getInfoType());
        operResPara.setInfoPkid(record.getInfoPkid());
        operResPara.setArchivedFlag(record.getArchivedFlag());
        operResPara.setCreatedBy(platformService.getStrLastUpdBy());
        operResPara.setCreatedTime(record.getCreatedTime());
        operResPara.setLastUpdBy(platformService.getStrLastUpdBy());
        operResPara.setLastUpdTime(platformService.getStrLastUpdDate());
        operResPara.setRemark(record.getRemark());
        operResPara.setRecversion( ToolUtil.getIntIgnoreNull(record.getRecversion()));
        return operResPara;
    }

    public void insertEsCttInfo(CttInfoShow cttInfoShowPara){
        cttInfoShowPara.setCreatedBy(platformService.getStrLastUpdBy());
        cttInfoShowPara.setCreatedDate(platformService.getStrLastUpdDate());
        esCttInfoMapper.insert(fromCttAndStlShowToModel(cttInfoShowPara));
    }
    private EsCttInfo fromCttAndStlShowToModel(CttInfoShow cttInfoShowPara){
        EsCttInfo esCttInfo=new EsCttInfo();
        esCttInfo.setId("NULL");
        esCttInfo.setName(cttInfoShowPara.getName());
        esCttInfo.setParentPkid(cttInfoShowPara.getParentPkid());
        esCttInfo.setCttType(cttInfoShowPara.getType());
        esCttInfo.setNote(cttInfoShowPara.getNote());
        esCttInfo.setCreatedBy(cttInfoShowPara.getCreatedBy());
        esCttInfo.setCreatedDate(cttInfoShowPara.getCreatedDate());
        return esCttInfo;
    }

}
