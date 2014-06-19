package epss.service;

import epss.repository.model.model_show.*;
import epss.repository.dao.notMyBits.QueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Think
 * Date: 13-5-5
 * Time: ÉÏÎç8:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EsQueryService {
    @Autowired
    private QueryMapper queryMapper;

    public List<QryShow> getCSList(String strCstplInfoPkidPara){
        return queryMapper.getCSList(strCstplInfoPkidPara) ;
    }

    public List<QryShow> getCSStlMList(String strBelongToPkid,String strStageNo){
        return queryMapper.getCSStlMList(strBelongToPkid,strStageNo) ;
    }

    public List<QryShow> getCSStlQList(String strBelongToPkid,String strStageNo){
        return queryMapper.getCSStlQList(strBelongToPkid,strStageNo) ;
    }

    public List<QryShow> getCSStlQBySignPartList(String strCstplInfoPkid,String strStageNo){
        return queryMapper.getCSStlQBySignPartList(strCstplInfoPkid,strStageNo) ;
    }

    public List<SubcttItemShow> getEsItemHieRelapOfCstplList(String strBelongToPkid){
        return queryMapper.getEsItemHieRelapOfCstplList(strBelongToPkid) ;
    }
}
