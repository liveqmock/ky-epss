package epss.service;

import epss.repository.dao.TidkeysMapper;
import epss.repository.model.Tidkeys;
import epss.repository.model.TidkeysExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2014/8/27.
 */
@Service
public class TidkeysService {
    @Autowired
    private TidkeysMapper tidkeysMapper;

    public Tidkeys getTidkeysList(String strTidPara) {
        TidkeysExample example= new TidkeysExample();
        example.createCriteria()
                .andTidLike("%" + strTidPara + "%");
        List<Tidkeys> tidkeysList=tidkeysMapper.selectByExample(example);
        return tidkeysList.get(0);
    }
}
