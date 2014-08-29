package epss.service;

import epss.repository.dao.DeptMapper;
import epss.repository.dao.OperMapper;
import epss.repository.dao.not_mybatis.MyDeptAndOperMapper;
import epss.repository.model.Dept;
import epss.repository.model.DeptExample;
import epss.repository.model.Oper;
import epss.repository.model.OperExample;
import epss.repository.model.model_show.DeptOperShow;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XIANGYANG on 2014/7/27.
 */
@Service
public class DeptOperService {
    @Resource
    private MyDeptAndOperMapper myDeptAndOperMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private OperMapper operMapper;

    public List<DeptOperShow> selectDeptAndOperRecords(String parentPkidPara) {
        return myDeptAndOperMapper.selectDeptAndOperRecords(parentPkidPara);
    }

    public boolean findChildRecordsByPkid(String strDeptOperPkidPara) {
        DeptExample example = new DeptExample();
        example.createCriteria()
                .andParentpkidEqualTo(strDeptOperPkidPara);
        OperExample operExample=new OperExample();
        operExample.createCriteria()
                .andDeptPkidEqualTo(strDeptOperPkidPara);
        return (deptMapper.selectByExample(example).size()>0||operMapper.selectByExample(operExample).size()>0);
    }

    public Object selectRecordByPkid(DeptOperShow deptOperShowPara) {
        if ("0".equals(deptOperShowPara.getType())){
            return deptMapper.selectByPrimaryKey(deptOperShowPara.getPkid());
        }else {
            return operMapper.selectByPrimaryKey(deptOperShowPara.getPkid());
        }
    }

    public boolean isExistInDeptDb(Dept deptPara) {
            DeptExample deptExample=new DeptExample();
            deptExample.createCriteria()
                    .andIdEqualTo(deptPara.getId());
            return deptMapper.selectByExample(deptExample).size()>0;
    }
    public boolean isExistInOperDb(Oper operPara) {
        OperExample operExample=new OperExample();
        operExample.createCriteria()
                .andIdEqualTo(operPara.getId());
        return operMapper.selectByExample(operExample).size()>0;
    }
    public void insertDeptRecord(Dept deptPara){
        deptPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        deptPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        deptMapper.insert(deptPara);
    }
    public void insertOperRecord(Oper operPara){
        operPara.setArchivedFlag("0");
        operPara.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
        operPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        operMapper.insert(operPara);
    }
    public void updateDeptRecord(Dept deptPara){
        deptPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        deptPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        deptMapper.updateByPrimaryKey(deptPara);
    }
    public void updateOperRecord(Oper operPara){
        operPara.setArchivedFlag("0");
        operPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
        operPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        operMapper.updateByPrimaryKey(operPara);
    }
    public void deleteDeptRecord(Dept deptPara){
        deptMapper.deleteByPrimaryKey(deptPara.getPkid());
    }
    public void deleteOperRecord(Oper operPara){
        operMapper.deleteByPrimaryKey(operPara.getPkid());
    }
}
