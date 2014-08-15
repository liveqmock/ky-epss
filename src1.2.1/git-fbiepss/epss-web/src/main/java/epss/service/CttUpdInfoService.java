package epss.service;

import epss.repository.dao.CttUpdInfoMapper;
import epss.repository.dao.not_mybatis.MyCttInfoMapper;
import epss.repository.model.CttUpdInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2014/8/15.
 */
public class CttUpdInfoService {
    @Autowired
    private CttUpdInfoMapper cttUpdInfoMapper;
   public int updateByPrimaryKey(CttUpdInfo record){
        return  cttUpdInfoMapper.updateByPrimaryKey(record);
    }
}
