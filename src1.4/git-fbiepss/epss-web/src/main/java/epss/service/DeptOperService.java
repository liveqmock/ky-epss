package epss.service;

import epss.repository.dao.DeptMapper;
import epss.repository.dao.OperMapper;
import epss.repository.dao.not_mybatis.MyDeptAndOperMapper;
import epss.repository.model.Dept;
import epss.repository.model.DeptExample;
import epss.repository.model.Oper;
import epss.repository.model.OperExample;
import epss.repository.model.model_show.DeptOperShow;
import org.apache.poi.util.StringUtil;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import skyline.platform.utils.PropertyManager;
import skyline.util.MessageUtil;
import skyline.util.ToolUtil;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.*;
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

    public List<DeptOperShow> selectDeptAndOperRecords(String parentPkidPara,String strQryFlag) {
        return myDeptAndOperMapper.selectDeptAndOperRecords(parentPkidPara,strQryFlag);
    }

    public List<Dept> getDeptList() {
        DeptExample example=new DeptExample();
        return deptMapper.selectByExample(example);
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
    public List<Oper> getOperListByModelShow(DeptOperShow deptOperShowPara){
        OperExample example=new OperExample();
        OperExample.Criteria criteria = example.createCriteria();
        //可以为NULL的项
        // 部门ID
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getOperId()).equals("")){
            criteria.andIdLike("%"+deptOperShowPara.getOperId()+"%");
        }
        // 部门名称
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getOperName()).equals("")){
            criteria.andNameLike("%"+deptOperShowPara.getOperName()+"%");
        }
        // 接收者Pkid
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getDeptPkid()).equals("")){
            criteria.andDeptPkidEqualTo(deptOperShowPara.getDeptPkid());
        }
        // 备注内容
        if(!ToolUtil.getStrIgnoreNull(deptOperShowPara.getOperRemark()).equals("")){
            criteria.andRemarkLike("%"+ deptOperShowPara.getOperRemark()+"%");
        }
        example.setOrderByClause("NAME ASC") ;
        return operMapper.selectByExample(example);
    }

    public String getStrMaxDeptId(){
        return ToolUtil.getMaxIdPlusOne("DEPT",myDeptAndOperMapper.getStrMaxDeptId()) ;
    }
    public String getStrMaxOperId(){
        return ToolUtil.getMaxIdPlusOne("OPER",myDeptAndOperMapper.getStrMaxOperId()) ;
    }

    public boolean isExistInDeptDb(Dept deptPara) {
            DeptExample deptExample=new DeptExample();
            deptExample.createCriteria()
                    .andIdEqualTo(deptPara.getId());
            return deptMapper.selectByExample(deptExample).size()>0;
    }
    public int existRecordCountsInOperDb(Oper operPara) {
        OperExample operExample=new OperExample();
        if(ToolUtil.getStrIgnoreNull(operPara.getId()).length()>0) {
            operExample.createCriteria()
                    .andIdEqualTo(operPara.getId());
        }
        return operMapper.selectByExample(operExample).size();
    }
    public void insertDeptRecord(Dept deptPara){
        deptPara.setCreatedBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        deptPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        deptMapper.insert(deptPara);
    }
    public void insertOperRecord(Oper operPara) {
        UploadedFile uploadedFile=operPara.getFile();
        String strFileName = uploadedFile.getFileName();
        if (!(StringUtils.isEmpty(strFileName))){
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
            BufferedInputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                File dirFile = new File(path);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                File file = new File(dirFile, strFileName);
                inputStream = new BufferedInputStream(uploadedFile.getInputstream());
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int num;
                while ((num = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            operPara.setAttachment(strFileName);
        }
        operPara.setArchivedFlag("0");
        operPara.setCreatedBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        operPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        operMapper.insert(operPara);
    }
    public void updateDeptRecord(Dept deptPara){
        deptPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        deptPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        deptMapper.updateByPrimaryKey(deptPara);
    }
    public void updateOperRecord(Oper operPara){
        UploadedFile uploadedFile=operPara.getFile();
        if (uploadedFile!=null){
            String strFileName = uploadedFile.getFileName();
            if (!(StringUtils.isEmpty(strFileName))){
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
                BufferedInputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    Oper operTemp=operMapper.selectByPrimaryKey(operPara.getPkid());
                    String strDbFileName=operTemp.getAttachment();
                    File file=null;
                    if (strDbFileName!=null){
                        file = new File(dirFile, strDbFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    file = new File(dirFile, strFileName);
                    inputStream = new BufferedInputStream(uploadedFile.getInputstream());
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int num;
                    while ((num = inputStream.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, num);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                operPara.setAttachment(strFileName);
            }
        }
        operPara.setArchivedFlag("0");
        operPara.setLastUpdBy(ToolUtil.getOperatorManager().getOperator().getPkid());
        operPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        operMapper.updateByPrimaryKey(operPara);
    }
    public void deleteDeptRecord(Dept deptPara){
        deptMapper.deleteByPrimaryKey(deptPara.getPkid());
    }
    public void deleteOperRecord(Oper operPara){
        String strDbFileName=operPara.getAttachment();
        if (strDbFileName!=null&&!(StringUtils.isEmpty(strDbFileName))){
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/operPicture");
            File file = new File(path+"/"+strDbFileName);
            if (file.exists()) {
                file.delete();
            }
        }
        operMapper.deleteByPrimaryKey(operPara.getPkid());
    }
    public List<Oper> getOperListByOperId(String strOperIdPara){
        OperExample example=new OperExample();
        OperExample.Criteria criteria = example.createCriteria();
        if (strOperIdPara!=null){
            criteria.andIdEqualTo(strOperIdPara);
        }
        return operMapper.selectByExample(example);
    }
    public Oper getOperByPkid(String strPkidPara){
        return operMapper.selectByPrimaryKey(strPkidPara);
    }
    public void updateRecord(Oper operPara){
        operMapper.updateByPrimaryKey(operPara);
    }
}
