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

    public boolean findChildRecordsByPkid(DeptOperShow deptOperShowPara) {
        DeptExample example = new DeptExample();
        example.createCriteria()
                .andParentpkidEqualTo(deptOperShowPara.getPkid());
        OperExample operExample=new OperExample();
        operExample.createCriteria()
                .andDeptPkidEqualTo(deptOperShowPara.getPkid());
        return (deptMapper.selectByExample(example).size()>0||operMapper.selectByExample(operExample).size()>0);
    }
    public int deleteByPkid(DeptOperShow deptOperShowPara) {
        if ("1".equals(deptOperShowPara.getType())){
            return deptMapper.deleteByPrimaryKey(deptOperShowPara.getPkid());
        }else {
            return operMapper.deleteByPrimaryKey(deptOperShowPara.getPkid());
        }
    }
    public Object selectRecordByPkid(DeptOperShow deptOperShowPara) {
        if ("0".equals(deptOperShowPara.getType())){
            return deptMapper.selectByPrimaryKey(deptOperShowPara.getPkid());
        }else {
            return operMapper.selectByPrimaryKey(deptOperShowPara.getPkid());
        }
    }
    public boolean isExistInDb(Object objectPara,String strTypePara) {
        if ("0".equals(strTypePara)){
            DeptExample deptExample=new DeptExample();
            deptExample.createCriteria()
                    .andIdEqualTo(((Dept)objectPara).getId());
            return deptMapper.selectByExample(deptExample).size()>0;
        }else {
            OperExample operExample=new OperExample();
            operExample.createCriteria()
                    .andIdEqualTo(((Oper) objectPara).getId());
            return operMapper.selectByExample(operExample).size()>0;

        }
    }
    public void insertRecord(Object objectPara,String strTypePara){
        if ("0".equals(strTypePara)){
            Dept dept=(Dept) objectPara;
            dept.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
            dept.setCreatedTime(ToolUtil.getStrLastUpdDate());
            deptMapper.insert(dept);
        }else {
            Oper oper=(Oper) objectPara;
            oper.setCreatedBy(ToolUtil.getOperatorManager().getOperatorId());
            oper.setCreatedTime(ToolUtil.getStrLastUpdDate());
            operMapper.insert(oper);
        }
    }
    public void updateRecord(Object objectPara,String strTypePara) {
        if ("0".equals(strTypePara)){
            Dept dept=(Dept) objectPara;
            dept.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
            dept.setLastUpdTime(ToolUtil.getStrLastUpdDate());
            deptMapper.updateByPrimaryKey(dept);
        }else {
            Oper oper=(Oper) objectPara;
            oper.setLastUpdBy(ToolUtil.getOperatorManager().getOperatorId());
            oper.setLastUpdTime(ToolUtil.getStrLastUpdDate());
            operMapper.updateByPrimaryKey(oper);
        }
    }
    public void deleteRecord(Object objectPara,String strTypePara) {
        if ("0".equals(strTypePara)){
            deptMapper.deleteByPrimaryKey(((Dept) objectPara).getPkid());
        }else {
            operMapper.deleteByPrimaryKey(((Oper) objectPara).getPkid());
        }
    }
}
