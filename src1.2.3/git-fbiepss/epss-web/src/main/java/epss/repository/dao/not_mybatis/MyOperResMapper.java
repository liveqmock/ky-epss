package epss.repository.dao.not_mybatis;

import epss.repository.model.model_show.CttInfoShow;
import epss.repository.model.model_show.DeptOperShow;
import epss.repository.model.model_show.OperResShow;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 13-2-13
 * Time: ÏÂÎç8:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface MyOperResMapper {
    List<OperResShow> selectOperaResRecordsByModelShow(OperResShow operResShowPara);

    @Select("select distinct" +
            "   opr.INFO_PKID as infoPkid, " +
            "   eci.NAME as infoPkidName" +
            " from " +
            "   OPER_RES opr" +
            " left join" +
            "   ES_CTT_INFO eci" +
            " on" +
            "   opr.INFO_PKID=eci.PKID" +
            " where " +
            "   opr.INFO_TYPE=#{strInfoType}" +
            " and" +
            "   opr.OPER_PKID=#{strOperPkid}")
    List<OperResShow> getInfoListByOperPkid(@Param("strInfoType") String strInfoType,
                                            @Param("strOperPkid") String strOperPkid);

    @Select(" select" +
            "    t.pkid as pkid," +
            "    t.ctt_type as cttType," +
            "    t.parent_pkid as parentPkid," +
            "    t.id as id," +
            "    t.name as name," +
            "    t.note as note," +
            "    t.end_flag as endFlag," +
            "    t.created_by as createdBy," +
            "    (select name from oper where id=t.created_by) as createdByName," +
            "    t.created_date as createdDate " +
            " from" +
            "    ES_CTT_INFO t" +
            " where" +
            "    t.parent_pkid=#{parentPkid}" +
            " order by" +
            "    t.name")
    List<CttInfoShow> selectRecordsFromCtt(@Param("parentPkid") String parentPkidPara);
}
