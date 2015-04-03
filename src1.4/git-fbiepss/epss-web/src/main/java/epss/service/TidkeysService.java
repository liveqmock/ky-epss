package epss.service;

import epss.repository.dao.TidKeysMapper;
import epss.repository.model.TidKeys;
import epss.repository.model.TidKeysExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skyline.util.ToolUtil;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by Administrator on 2014/8/27.
 */
@Service
public class TidKeysService {
    @Autowired
    private TidKeysMapper tidKeysMapper;

    public List<TidKeys> getTidKeysList(TidKeys tidKeysPara) {
        TidKeysExample example= new TidKeysExample();
        TidKeysExample.Criteria criteria = example.createCriteria();
        if (!ToolUtil.getStrIgnoreNull(tidKeysPara.getTid()).equals("")){
            criteria.andTidEqualTo(tidKeysPara.getTid());
        }
        return tidKeysMapper.selectByExample(example);
    }

    public boolean isExistInDb(TidKeys tidKeysPara) {
        TidKeysExample example = new TidKeysExample();
        example.createCriteria().andTidEqualTo(tidKeysPara.getTid());
        return tidKeysMapper.countByExample(example) >= 1;
    }

    public void insertRecord(TidKeys tidKeysPara) {
        tidKeysMapper.insert(tidKeysPara);
    }

    public void updateRecord(TidKeys tidKeysPara){
        tidKeysMapper.updateByPrimaryKey(tidKeysPara);
    }

    public int deleteRecord(TidKeys tidKeysPara){
        return tidKeysMapper.deleteByPrimaryKey(tidKeysPara.getPkid());
    }
}
