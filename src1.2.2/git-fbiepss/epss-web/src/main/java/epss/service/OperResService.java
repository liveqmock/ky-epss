package epss.service;

import skyline.util.ToolUtil;
import epss.repository.dao.OperResMapper;
import epss.repository.dao.not_mybatis.MyOperResMapper;
import epss.repository.model.OperRes;
import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperResShow;
import org.springframework.stereotype.Service;
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

    public List<OperResShow> selectOperaRoleRecords(String parentDeptid){
        return myOperResMapper.selectOperaRoleRecords(parentDeptid);
    }

    public List<DeptOperShow> getOperList(String parentDeptid){
        return myOperResMapper.getOperList(parentDeptid);
    }

    public List<OperResShow> selectOperaResRecords(){
        return myOperResMapper.selectOperaResRecords();
    }

    public List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(operResShowPara);
    }
    public List<OperResShow> selectOperaResRecordsByModel(OperRes operResPara){
        return myOperResMapper.selectOperaResRecordsByModelShow(fromModelToModelShow(operResPara));
    }
    public List<OperResShow> getInfoListByOperPkid(String strInfoTypePara,String strOperPkidPara){
        return myOperResMapper.getInfoListByOperPkid(strInfoTypePara,strOperPkidPara);
    }
	public List<CttInfoShow> selectRecordsFromCtt(String parentPkidPara){
        return  myOperResMapper.selectRecordsFromCtt(parentPkidPara);
    }
    public void insertRecord(OperResShow record){
        operResMapper.insert(fromOperShowToModel(record));
    }
    public void insertRecord(OperRes operResPara){
        operResPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        operResPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        operResPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        operResPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        operResMapper.insert(operResPara);
    }
    private OperRes fromOperShowToModel(OperResShow record) {
        OperRes operResPara=new OperRes();
        operResPara.setTid(record.getTid());
        operResPara.setOperPkid(record.getOperPkid());
        operResPara.setFlowStatus(record.getFlowStatus());
        operResPara.setInfoType(record.getInfoType());
        operResPara.setInfoPkid(record.getInfoPkid());
        operResPara.setArchivedFlag(record.getArchivedFlag());
        operResPara.setCreatedBy(record.getCreatedBy());
        operResPara.setCreatedTime(record.getCreatedTime());
        operResPara.setLastUpdBy(record.getLastUpdBy());
        operResPara.setLastUpdTime(record.getLastUpdTime());
        operResPara.setRemark(record.getRemark());
        operResPara.setRecversion( ToolUtil.getIntIgnoreNull(record.getRecversion()));
        return operResPara;
    }
    private OperResShow fromModelToModelShow(OperRes operResPara) {
        OperResShow operResShowTemp=new OperResShow();
        operResShowTemp.setTid(operResPara.getTid());
        operResShowTemp.setOperPkid(operResPara.getOperPkid());
        operResShowTemp.setFlowStatus(operResPara.getFlowStatus());
        operResShowTemp.setInfoType(operResPara.getInfoType());
        operResShowTemp.setInfoPkid(operResPara.getInfoPkid());
        operResShowTemp.setArchivedFlag(operResPara.getArchivedFlag());
        operResShowTemp.setCreatedBy(operResPara.getCreatedBy());
        operResShowTemp.setCreatedTime(operResPara.getCreatedTime());
        operResShowTemp.setLastUpdBy(operResPara.getLastUpdBy());
        operResShowTemp.setLastUpdTime(operResPara.getLastUpdTime());
        operResShowTemp.setRemark(operResPara.getRemark());
        operResShowTemp.setRecversion( ToolUtil.getIntIgnoreNull(operResPara.getRecversion()));
        return operResShowTemp;
    }
}
