package epss.service.common;

import epss.repository.model.model_show.*;
import epss.repository.dao.common.QueryMapper;
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

    public List<QryShow> getCSList(String strCttType,String strBelongToPkid){
        return queryMapper.getCSList(strCttType,strBelongToPkid) ;
    }

    public List<QryShow> getCSStlMList(String strBelongToPkid,String strPeriodNo){
        return queryMapper.getCSStlMList(strBelongToPkid,strPeriodNo) ;
    }

    public List<QryShow> getCSStlQList(String strBelongToPkid,String strPeriodNo){
        return queryMapper.getCSStlQList(strBelongToPkid,strPeriodNo) ;
    }

    public List<QryShow> getCSStlQBySignPartList(String strCstplPkid,String strPeriodNo){
        return queryMapper.getCSStlQBySignPartList(strCstplPkid,strPeriodNo) ;
    }

    public List<CttItemShow> getEsItemHieRelapOfCstplList(String strBelongToPkid){
        return queryMapper.getEsItemHieRelapOfCstplList(strBelongToPkid) ;
    }
}
