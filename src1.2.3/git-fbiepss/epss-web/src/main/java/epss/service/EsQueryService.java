package epss.service;

import epss.repository.model.model_show.*;
import epss.repository.dao.not_mybatis.MyQueryMapper;
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
    private MyQueryMapper myQueryMapper;

    public List<QryShow> getCSList(String strCttType,String strBelongToPkid){
        return myQueryMapper.getCSList(strCttType,strBelongToPkid) ;
    }

    public List<QryShow> getCSStlMList(String strBelongToPkid,String strPeriodNo){
        return myQueryMapper.getCSStlMList(strBelongToPkid,strPeriodNo) ;
    }

    public List<QryShow> getCSStlQList(String strBelongToPkid,String strPeriodNo){
        return myQueryMapper.getCSStlQList(strBelongToPkid,strPeriodNo) ;
    }

    public List<QryTkMeaCSStlQShow> getCSStlQBySignPartList(String strCttInfoPkidPara,String strPeriodNo){
        return myQueryMapper.getCSStlQBySignPartList(strCttInfoPkidPara,strPeriodNo) ;
    }
}
